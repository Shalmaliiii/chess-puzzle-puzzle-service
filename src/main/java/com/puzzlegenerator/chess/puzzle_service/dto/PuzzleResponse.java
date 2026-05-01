package com.puzzlegenerator.chess.puzzle_service.dto;

import com.puzzlegenerator.chess.puzzle_service.model.PuzzleDifficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PuzzleResponse {
    private String id;
    private String fen;
    private String sideToMove;
    private int mateIn;
    private PuzzleDifficulty difficulty;
}
