package com.puzzlegenerator.chess.puzzle_service.service;

import com.puzzlegenerator.chess.puzzle_service.dto.*;
import com.puzzlegenerator.chess.puzzle_service.exception.NoPuzzlesAvailableException;
import com.puzzlegenerator.chess.puzzle_service.exception.PuzzleNotFoundException;
import com.puzzlegenerator.chess.puzzle_service.kafka.PuzzleGenerateProducer;
import com.puzzlegenerator.chess.puzzle_service.kafka.PuzzleSolvedProducer;
import com.puzzlegenerator.chess.puzzle_service.model.Puzzle;
import com.puzzlegenerator.chess.puzzle_service.model.PuzzleDifficulty;
import com.puzzlegenerator.chess.puzzle_service.model.PuzzleStatus;
import com.puzzlegenerator.chess.puzzle_service.repository.PuzzleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class PuzzleService {

    private final PuzzleRepository puzzleRepository;
    private final PuzzleGenerateProducer generateProducer;
    private final PuzzleSolvedProducer solvedProducer;
    private final MongoTemplate mongoTemplate;

    private static final String ANONYMOUS = "anonymous";

    public PuzzleResponse getNextPuzzle(String userId, String difficulty) {
        List<Puzzle> puzzles;
        boolean isAnonymous = ANONYMOUS.equals(userId);

        if (difficulty != null && !difficulty.isBlank()) {
            PuzzleDifficulty diff = PuzzleDifficulty.valueOf(difficulty.toUpperCase());
            puzzles = isAnonymous
                    ? puzzleRepository.findByDifficultyAndStatus(diff, PuzzleStatus.ACTIVE)
                    : puzzleRepository.findByDifficultyAndStatusAndSolvedByNotContaining(
                            diff, PuzzleStatus.ACTIVE, userId);
        } else {
            puzzles = isAnonymous
                    ? puzzleRepository.findByStatus(PuzzleStatus.ACTIVE)
                    : puzzleRepository.findByStatusAndSolvedByNotContaining(
                            PuzzleStatus.ACTIVE, userId);
        }

        if (puzzles.isEmpty()) {
            generateProducer.sendGenerateEvent(PuzzleGenerateEvent.builder()
                    .difficulty(difficulty != null && !difficulty.isBlank() ? difficulty.toUpperCase() : "INTERMEDIATE")
                    .count(5)
                    .requestedBy(userId)
                    .build());
            throw new NoPuzzlesAvailableException();
        }

        Puzzle puzzle = puzzles.get(ThreadLocalRandom.current().nextInt(puzzles.size()));
        return toPuzzleResponse(puzzle);
    }

    public PuzzleResponse getPuzzleById(String id) {
        Puzzle puzzle = puzzleRepository.findById(id)
                .orElseThrow(() -> new PuzzleNotFoundException(id));
        return toPuzzleResponse(puzzle);
    }

    public MoveValidationResponse validateMove(String id, String userId, MoveValidationRequest request) {
        Puzzle puzzle = puzzleRepository.findById(id)
                .orElseThrow(() -> new PuzzleNotFoundException(id));

        List<String> solutionLine = puzzle.getSolutionLine();
        int moveNumber = request.getMoveNumber();

        // Player moves are at even indices (0, 2, 4...)
        // moveNumber=1 → index 0, moveNumber=2 → index 2, moveNumber=3 → index 4
        int playerMoveIndex = (moveNumber - 1) * 2;

        if (playerMoveIndex >= solutionLine.size()) {
            return MoveValidationResponse.builder()
                    .correct(false)
                    .puzzleComplete(false)
                    .movesRemaining(0)
                    .build();
        }

        // Increment attemptedCount atomically on first move
        if (moveNumber == 1) {
            mongoTemplate.updateFirst(
                    Query.query(Criteria.where("id").is(id)),
                    new Update().inc("attemptedCount", 1),
                    Puzzle.class);
        }

        String expectedMove = solutionLine.get(playerMoveIndex);
        boolean correct = expectedMove.equalsIgnoreCase(request.getMove());

        if (!correct) {
            int totalPlayerMoves = (solutionLine.size() + 1) / 2;
            int remaining = totalPlayerMoves - moveNumber + 1;
            return MoveValidationResponse.builder()
                    .correct(false)
                    .puzzleComplete(false)
                    .movesRemaining(remaining)
                    .build();
        }

        // Check if there's an opponent move next
        int opponentMoveIndex = playerMoveIndex + 1;
        boolean hasOpponentMove = opponentMoveIndex < solutionLine.size();
        boolean puzzleComplete = !hasOpponentMove || (playerMoveIndex + 2 >= solutionLine.size());

        // If the opponent has a response and there are more player moves, puzzle is not complete
        if (hasOpponentMove && (opponentMoveIndex + 1) < solutionLine.size()) {
            puzzleComplete = false;
        }

        String opponentMove = hasOpponentMove ? solutionLine.get(opponentMoveIndex) : null;
        int totalPlayerMoves = (solutionLine.size() + 1) / 2;
        int remaining = puzzleComplete ? 0 : totalPlayerMoves - moveNumber;

        return MoveValidationResponse.builder()
                .correct(true)
                .opponentMove(puzzleComplete && !hasOpponentMove ? null : opponentMove)
                .puzzleComplete(puzzleComplete)
                .movesRemaining(remaining)
                .build();
    }

    public PuzzleSolveResponse solvePuzzle(String id, String userId, PuzzleSolveRequest request) {
        Puzzle puzzle = puzzleRepository.findById(id)
                .orElseThrow(() -> new PuzzleNotFoundException(id));

        if (!ANONYMOUS.equals(userId)) {
            com.mongodb.client.result.UpdateResult result = mongoTemplate.updateFirst(
                    Query.query(Criteria.where("id").is(id)
                            .and("solvedBy").nin(userId)),
                    new Update()
                            .addToSet("solvedBy", userId)
                            .inc("solvedByCount", 1)
                            .inc("totalSolveTimeMs", request.getTimeMs()),
                    Puzzle.class);

            if (result.getModifiedCount() > 0) {
                solvedProducer.sendSolvedEvent(PuzzleSolvedEvent.builder()
                        .userId(userId)
                        .puzzleId(id)
                        .difficulty(puzzle.getDifficulty().name())
                        .timeMs(request.getTimeMs())
                        .correct(true)
                        .mateIn(puzzle.getMateIn())
                        .build());
            }
        }

        return new PuzzleSolveResponse("Puzzle solved successfully");
    }

    public Map<String, Object> requestGeneration(String userId, PuzzleGenerateRequest request) {
        generateProducer.sendGenerateEvent(PuzzleGenerateEvent.builder()
                .difficulty(request.getDifficulty().toUpperCase())
                .count(request.getCount())
                .requestedBy(userId)
                .build());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Generation request submitted");
        response.put("count", request.getCount());
        return response;
    }

    public Map<String, Object> getSolution(String id) {
        Puzzle puzzle = puzzleRepository.findById(id)
                .orElseThrow(() -> new PuzzleNotFoundException(id));

        Map<String, Object> response = new HashMap<>();
        response.put("solutionLine", puzzle.getSolutionLine());
        response.put("fen", puzzle.getFen());
        response.put("mateIn", puzzle.getMateIn());
        return response;
    }

    public PuzzleStatsResponse getStats() {
        long total = puzzleRepository.count();

        Map<String, Long> byDifficulty = new HashMap<>();
        for (PuzzleDifficulty diff : PuzzleDifficulty.values()) {
            byDifficulty.put(diff.name(), puzzleRepository.countByDifficulty(diff));
        }

        Map<String, Long> byStatus = new HashMap<>();
        for (PuzzleStatus status : PuzzleStatus.values()) {
            byStatus.put(status.name(), puzzleRepository.countByStatus(status));
        }

        return PuzzleStatsResponse.builder()
                .total(total)
                .byDifficulty(byDifficulty)
                .byStatus(byStatus)
                .build();
    }

    private PuzzleResponse toPuzzleResponse(Puzzle puzzle) {
        return PuzzleResponse.builder()
                .id(puzzle.getId())
                .fen(puzzle.getFen())
                .sideToMove(puzzle.getSideToMove())
                .mateIn(puzzle.getMateIn())
                .difficulty(puzzle.getDifficulty())
                .build();
    }
}
