package com.puzzlegenerator.chess.puzzle_service.config;

import com.puzzlegenerator.chess.puzzle_service.model.Puzzle;
import com.puzzlegenerator.chess.puzzle_service.model.PuzzleDifficulty;
import com.puzzlegenerator.chess.puzzle_service.model.PuzzleStatus;
import com.puzzlegenerator.chess.puzzle_service.repository.PuzzleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final PuzzleRepository puzzleRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (puzzleRepository.count() > 0) {
            log.info("Puzzles already exist, skipping seed data");
            return;
        }

        log.info("Seeding puzzle database with initial puzzles...");
        List<Puzzle> puzzles = new ArrayList<>();

        // BEGINNER - Mate in 1
        puzzles.add(createPuzzle(
                "6k1/5ppp/8/8/8/8/8/4R1K1 w - - 0 1",
                List.of("e1e8"), 1, PuzzleDifficulty.BEGINNER, "WHITE",
                List.of("mate-in-1", "rook-endgame")));

        puzzles.add(createPuzzle(
                "5rk1/5ppp/8/8/8/8/8/3QR1K1 w - - 0 1",
                List.of("d1d8"), 1, PuzzleDifficulty.BEGINNER, "WHITE",
                List.of("mate-in-1", "back-rank")));

        puzzles.add(createPuzzle(
                "r1bqkb1r/pppp1ppp/2n2n2/4p2Q/2B1P3/8/PPPP1PPP/RNB1K1NR w KQkq - 4 4",
                List.of("h5f7"), 1, PuzzleDifficulty.BEGINNER, "WHITE",
                List.of("mate-in-1", "scholars-mate")));

        puzzles.add(createPuzzle(
                "6k1/8/6K1/8/8/8/8/7R w - - 0 1",
                List.of("h1h8"), 1, PuzzleDifficulty.BEGINNER, "WHITE",
                List.of("mate-in-1", "rook-endgame")));

        puzzles.add(createPuzzle(
                "3qk3/8/8/8/8/8/8/3QK3 w - - 0 1",
                List.of("d1d8"), 1, PuzzleDifficulty.BEGINNER, "WHITE",
                List.of("mate-in-1", "queen-endgame")));

        // INTERMEDIATE - Mate in 2
        puzzles.add(createPuzzle(
                "2bqkbn1/2pppppp/np6/r3P3/1p2N3/5Q2/PPPPBPPP/RNB1K2R w KQ - 0 1",
                List.of("f3f7", "e8d8", "f7f8"), 2, PuzzleDifficulty.INTERMEDIATE, "WHITE",
                List.of("mate-in-2", "queen-sacrifice")));

        puzzles.add(createPuzzle(
                "r1b1k2r/ppppqppp/2n2n2/2b1p3/2B1P3/3P1N2/PPP2PPP/RNBQR1K1 w kq - 0 1",
                List.of("c4f7", "e7f7", "d1b3"), 2, PuzzleDifficulty.INTERMEDIATE, "WHITE",
                List.of("mate-in-2", "bishop-sacrifice")));

        puzzles.add(createPuzzle(
                "r2qk2r/ppp2ppp/2np1n2/2b1p1B1/2B1P1b1/3P1N2/PPP2PPP/RN1QR1K1 w kq - 0 1",
                List.of("c4f7", "e8f8", "g5f6"), 2, PuzzleDifficulty.INTERMEDIATE, "WHITE",
                List.of("mate-in-2", "pin")));

        puzzles.add(createPuzzle(
                "rnb1kbnr/pppp1ppp/8/4p3/5PPq/8/PPPPP2P/RNBQKBNR w KQkq - 1 3",
                List.of("g4g5", "h4g5", "d1h5"), 2, PuzzleDifficulty.INTERMEDIATE, "WHITE",
                List.of("mate-in-2", "pawn-push")));

        puzzles.add(createPuzzle(
                "r1bqkbnr/1ppp1ppp/p1n5/4p3/2B1P3/5Q2/PPPP1PPP/RNB1K1NR w KQkq - 0 4",
                List.of("f3f7", "e8e7", "c4d5"), 2, PuzzleDifficulty.INTERMEDIATE, "WHITE",
                List.of("mate-in-2", "italian-game")));

        // ADVANCED - Mate in 3
        puzzles.add(createPuzzle(
                "r1bq2r1/b4pk1/p1pp1p2/1p2pP2/1P2P1PB/3P4/1PPQ4/2K4R w - - 0 1",
                List.of("h4g5", "f6g5", "d2h6", "g7h6", "h1h6"), 3, PuzzleDifficulty.ADVANCED, "WHITE",
                List.of("mate-in-3", "bishop-sacrifice")));

        puzzles.add(createPuzzle(
                "6k1/pp4p1/2p5/2bp4/8/P5Pb/1P3rrP/2BRRK2 b - - 0 1",
                List.of("g2g1", "f1e2", "f2f2", "e2d3", "f2f3"), 3, PuzzleDifficulty.ADVANCED, "BLACK",
                List.of("mate-in-3", "rook-attack")));

        puzzles.add(createPuzzle(
                "r2qr1k1/ppp2ppp/2np4/2bBpb2/2P1P3/3P1N2/PP3PPP/R1BQ1RK1 w - - 0 1",
                List.of("d5f7", "f5f7", "d1b3", "d6d5", "b3f7"), 3, PuzzleDifficulty.ADVANCED, "WHITE",
                List.of("mate-in-3", "discovered-attack")));

        puzzles.add(createPuzzle(
                "r3k2r/pbppqppp/1pn2n2/2b1p3/2B1P3/2NP1N2/PPPBQPPP/R3K2R w KQkq - 0 1",
                List.of("c4f7", "e7f7", "e2b5", "e8d8", "b5b8"), 3, PuzzleDifficulty.ADVANCED, "WHITE",
                List.of("mate-in-3", "king-hunt")));

        puzzles.add(createPuzzle(
                "rn1qkbnr/ppp2Bpp/3p4/4N3/4P1b1/8/PPPP1PPP/RNBQK2R b KQkq - 0 1",
                List.of("g4d1", "f7g8", "d8b6", "g8f7", "b6f2"), 3, PuzzleDifficulty.ADVANCED, "BLACK",
                List.of("mate-in-3", "fried-liver")));

        // MASTER - Mate in 4+
        puzzles.add(createPuzzle(
                "r1bqk2r/pppp1ppp/2n2n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 4 4",
                List.of("d2d4", "e5d4", "e4e5", "d4d3", "e5f6", "d3c2", "f6g7"), 4, PuzzleDifficulty.MASTER, "WHITE",
                List.of("mate-in-4", "italian-game")));

        puzzles.add(createPuzzle(
                "r1b1kb1r/pppp1ppp/5n2/4p1q1/2BnP3/2N2N2/PPPP1PPP/R1BQK2R w KQkq - 0 1",
                List.of("f3d4", "g5g2", "d4f5", "g2f1", "e1d2", "f1g2", "d1g4", "g2g4", "f5h6"), 4, PuzzleDifficulty.MASTER, "WHITE",
                List.of("mate-in-4", "knight-attack")));

        puzzles.add(createPuzzle(
                "rnbqk1nr/pppp1ppp/4p3/8/1bPP4/2N5/PP2PPPP/R1BQKBNR w KQkq - 2 3",
                List.of("e2e4", "b4c3", "d1g4", "c3a1", "g4g7", "h8f8", "d4d5", "e6d5", "c1h6"), 4, PuzzleDifficulty.MASTER, "WHITE",
                List.of("mate-in-4", "queens-gambit")));

        puzzles.add(createPuzzle(
                "r2q1rk1/ppp1bppp/2n5/3np1b1/8/2N2NP1/PPPPPPBP/R1BQ1RK1 w - - 0 1",
                List.of("f3e5", "d5c3", "e5c6", "d8d1", "c6e7", "g8h8", "f1d1", "c3e2", "g1f1"), 4, PuzzleDifficulty.MASTER, "WHITE",
                List.of("mate-in-4", "knight-fork")));

        puzzles.add(createPuzzle(
                "r1bq1rk1/pppp1ppp/2n2n2/2b1p3/2B1P3/3P1N2/PPP2PPP/RNBQ1RK1 w - - 0 5",
                List.of("c1g5", "h7h6", "g5f6", "d8f6", "c4f7", "f8f7", "d1b3", "d7d5", "b3f7"), 4, PuzzleDifficulty.MASTER, "WHITE",
                List.of("mate-in-4", "giuoco-piano")));

        puzzleRepository.saveAll(puzzles);
        log.info("Seeded {} puzzles successfully", puzzles.size());
    }

    private Puzzle createPuzzle(String fen, List<String> solutionLine, int mateIn,
                                PuzzleDifficulty difficulty, String sideToMove, List<String> themes) {
        return Puzzle.builder()
                .fen(fen)
                .solutionLine(solutionLine)
                .mateIn(mateIn)
                .difficulty(difficulty)
                .sideToMove(sideToMove)
                .status(PuzzleStatus.ACTIVE)
                .themes(themes)
                .solvedBy(new ArrayList<>())
                .generatedAt(Instant.now())
                .generatedBy("seed-data")
                .build();
    }
}
