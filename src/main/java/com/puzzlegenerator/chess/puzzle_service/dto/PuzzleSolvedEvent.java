package com.puzzlegenerator.chess.puzzle_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PuzzleSolvedEvent {
    private String userId;
    private String puzzleId;
    private String difficulty;
    private long timeMs;
    private boolean correct;
    private int mateIn;
}
