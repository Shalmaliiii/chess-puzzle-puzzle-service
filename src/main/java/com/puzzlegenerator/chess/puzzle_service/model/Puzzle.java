package com.puzzlegenerator.chess.puzzle_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "puzzles")
@CompoundIndex(name = "difficulty_status_idx", def = "{'difficulty': 1, 'status': 1}")
public class Puzzle {

    @Id
    private String id;

    @Indexed(unique = true)
    private String fen;

    private List<String> solutionLine;

    private int mateIn;

    private PuzzleDifficulty difficulty;

    private String sideToMove;

    private PuzzleStatus status;

    private List<String> themes;

    @Builder.Default
    private int solvedByCount = 0;

    @Builder.Default
    private int attemptedCount = 0;

    @Builder.Default
    private long averageSolveTimeMs = 0;

    @Builder.Default
    private long totalSolveTimeMs = 0;

    @Builder.Default
    @Indexed
    private List<String> solvedBy = new ArrayList<>();

    private Instant generatedAt;

    @Builder.Default
    private String generatedBy = "engine-service";
}
