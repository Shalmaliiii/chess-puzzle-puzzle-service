package com.puzzlegenerator.chess.puzzle_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuzzleGenerateRequest {

    @NotBlank(message = "Difficulty is required")
    private String difficulty;

    @Min(value = 1, message = "Count must be at least 1")
    private int count;
}
