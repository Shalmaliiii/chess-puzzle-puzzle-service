package com.puzzlegenerator.chess.puzzle_service.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuzzleSolveRequest {

    @Min(value = 0, message = "Time must be non-negative")
    private long timeMs;
}
