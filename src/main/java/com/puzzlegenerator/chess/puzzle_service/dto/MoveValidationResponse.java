package com.puzzlegenerator.chess.puzzle_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveValidationResponse {
    private boolean correct;
    private String opponentMove;
    private boolean puzzleComplete;
    private int movesRemaining;
}
