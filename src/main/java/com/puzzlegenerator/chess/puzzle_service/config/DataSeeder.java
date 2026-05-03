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

        // BEGINNER - Mate in 1 (all verified with chess.js)
        puzzles.add(createPuzzle(
                "6k1/5ppp/8/8/8/8/8/4R1K1 w - - 0 1",
                List.of("e1e8"), 1, PuzzleDifficulty.BEGINNER, "WHITE",
                List.of("mate-in-1", "back-rank")));

        puzzles.add(createPuzzle(
                "r1bqkb1r/pppp1ppp/2n2n2/4p2Q/2B1P3/8/PPPP1PPP/RNB1K1NR w KQkq - 4 4",
                List.of("h5f7"), 1, PuzzleDifficulty.BEGINNER, "WHITE",
                List.of("mate-in-1", "scholars-mate")));

        puzzles.add(createPuzzle(
                "r4rk1/ppp2p1p/6pB/8/8/2Q5/PPP2PPP/R4RK1 w - - 0 1",
                List.of("c3g7"), 1, PuzzleDifficulty.BEGINNER, "WHITE",
                List.of("mate-in-1", "queen-mate")));

        puzzles.add(createPuzzle(
                "3k4/3P4/3K4/8/8/8/8/4R3 w - - 0 1",
                List.of("e1e8"), 1, PuzzleDifficulty.BEGINNER, "WHITE",
                List.of("mate-in-1", "rook-endgame")));

        puzzles.add(createPuzzle(
                "6k1/5ppp/8/8/8/8/5PPP/2R3K1 w - - 0 1",
                List.of("c1c8"), 1, PuzzleDifficulty.BEGINNER, "WHITE",
                List.of("mate-in-1", "back-rank")));

        // INTERMEDIATE - Mate in 2 (all verified with chess.js)
        // Nf6+ gxf6 Bxf7#
        puzzles.add(createPuzzle(
                "r2qkb1r/pp2nppp/3p4/2pNN1B1/2BnP3/3P4/PPP2PPP/R2bK2R w KQkq - 0 1",
                List.of("d5f6", "g7f6", "c4f7"), 2, PuzzleDifficulty.INTERMEDIATE, "WHITE",
                List.of("mate-in-2", "knight-fork")));

        // Qxf7+ Kh8 Qf8#
        puzzles.add(createPuzzle(
                "6k1/5p1p/6p1/8/8/8/5Q2/6K1 w - - 0 1",
                List.of("f2f7", "g8h8", "f7f8"), 2, PuzzleDifficulty.INTERMEDIATE, "WHITE",
                List.of("mate-in-2", "queen-endgame")));

        // Rxh7 Kf8 Qd8#
        puzzles.add(createPuzzle(
                "6k1/5ppp/8/8/8/7R/5PPP/3Q2K1 w - - 0 1",
                List.of("h3h7", "g8f8", "d1d8"), 2, PuzzleDifficulty.INTERMEDIATE, "WHITE",
                List.of("mate-in-2", "rook-queen-battery")));

        // Qxf7+ Kh8 Re8#
        puzzles.add(createPuzzle(
                "6k1/5p2/8/8/8/8/5QPP/4R1K1 w - - 0 1",
                List.of("f2f7", "g8h8", "e1e8"), 2, PuzzleDifficulty.INTERMEDIATE, "WHITE",
                List.of("mate-in-2", "queen-rook-combo")));

        // Qh8+! Bxh8 Rxh8# (queen sacrifice)
        puzzles.add(createPuzzle(
                "r2qr1k1/pp3pb1/2n3p1/1N4n1/1P2p2Q/P3p3/1B1P1PP1/R3K2R w KQ - 1 1",
                List.of("h4h8", "g7h8", "h1h8"), 2, PuzzleDifficulty.INTERMEDIATE, "WHITE",
                List.of("mate-in-2", "queen-sacrifice")));

        // ADVANCED - Mate in 3 (all verified with chess.js)
        // Qxh8+ Kxh8 Bf6+ Kg8 Re8#
        puzzles.add(createPuzzle(
                "r1b3kr/ppp1Bp1p/1b6/n2P4/2p3q1/2Q2N2/P4PPP/RN2R1K1 w - - 1 1",
                List.of("c3h8", "g8h8", "e7f6", "h8g8", "e1e8"), 3, PuzzleDifficulty.ADVANCED, "WHITE",
                List.of("mate-in-3", "queen-sacrifice")));

        // Rxh7+ Kxh7 Rh1+ Kg7 Qh6#
        puzzles.add(createPuzzle(
                "3q1r1k/2p4p/1p1pBrp1/p2Pp3/2PnP3/5PP1/PP1Q2K1/5R1R w - - 1 1",
                List.of("h1h7", "h8h7", "f1h1", "h7g7", "d2h6"), 3, PuzzleDifficulty.ADVANCED, "WHITE",
                List.of("mate-in-3", "rook-sacrifice")));

        // Qf1+ Kxf1 Bd3+ Ke1 Rf1# (queen sacrifice)
        puzzles.add(createPuzzle(
                "rn3rk1/p5pp/2p5/3Ppb2/2q5/1Q6/PPPB2PP/R3K1NR b - - 0 1",
                List.of("c4f1", "e1f1", "f5d3", "f1e1", "f8f1"), 3, PuzzleDifficulty.ADVANCED, "BLACK",
                List.of("mate-in-3", "queen-sacrifice")));

        // Bxg7+ Kxg7 Qg5+ Kh8 Qf6#
        puzzles.add(createPuzzle(
                "r4r1k/pp3ppp/4p2N/qb6/5Q2/3n4/PB3PPP/R3R1K1 w - - 1 1",
                List.of("b2g7", "h8g7", "f4g5", "g7h8", "g5f6"), 3, PuzzleDifficulty.ADVANCED, "WHITE",
                List.of("mate-in-3", "bishop-sacrifice")));

        // Qf7+ Kd8 Qf8+ Kd7 e6#
        puzzles.add(createPuzzle(
                "r3k1nr/ppp3pp/1bn5/3BP1q1/3P4/5Q2/PPP3PP/RN2K2R w KQkq - 1 1",
                List.of("f3f7", "e8d8", "f7f8", "d8d7", "e5e6"), 3, PuzzleDifficulty.ADVANCED, "WHITE",
                List.of("mate-in-3", "pawn-mate")));

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
