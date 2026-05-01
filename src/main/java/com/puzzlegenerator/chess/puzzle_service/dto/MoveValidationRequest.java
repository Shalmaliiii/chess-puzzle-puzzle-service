package com.puzzlegenerator.chess.puzzle_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveValidationRequest {

    @NotBlank(message = "Move is required")
    private String move;

    @Min(value = 1, message = "Move number must be at least 1")
    private int moveNumber;
}
