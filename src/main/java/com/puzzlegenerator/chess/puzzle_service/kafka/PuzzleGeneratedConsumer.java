package com.puzzlegenerator.chess.puzzle_service.kafka;

import com.puzzlegenerator.chess.puzzle_service.dto.PuzzleGeneratedEvent;
import com.puzzlegenerator.chess.puzzle_service.model.Puzzle;
import com.puzzlegenerator.chess.puzzle_service.model.PuzzleDifficulty;
import com.puzzlegenerator.chess.puzzle_service.model.PuzzleStatus;
import com.puzzlegenerator.chess.puzzle_service.repository.PuzzleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class PuzzleGeneratedConsumer {

    private final PuzzleRepository puzzleRepository;

    @KafkaListener(topics = "puzzle.generated", groupId = "puzzle-service")
    public void handlePuzzleGenerated(PuzzleGeneratedEvent event) {
        log.info("Received puzzle generated event: fen={}, difficulty={}", event.getFen(), event.getDifficulty());

        try {
            Puzzle puzzle = Puzzle.builder()
                    .fen(event.getFen())
                    .solutionLine(event.getSolutionLine())
                    .mateIn(event.getMateIn())
                    .difficulty(PuzzleDifficulty.valueOf(event.getDifficulty()))
                    .sideToMove(event.getSideToMove())
                    .status(PuzzleStatus.ACTIVE)
                    .themes(new ArrayList<>())
                    .solvedBy(new ArrayList<>())
                    .generatedAt(Instant.now())
                    .generatedBy("engine-service")
                    .build();

            puzzleRepository.save(puzzle);
            log.info("Saved new puzzle with fen: {}", event.getFen());
        } catch (DuplicateKeyException e) {
            log.warn("Duplicate puzzle with fen: {}. Skipping.", event.getFen());
        } catch (IllegalArgumentException e) {
            log.error("Invalid difficulty value: {}", event.getDifficulty(), e);
        }
    }
}
