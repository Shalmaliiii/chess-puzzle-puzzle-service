package com.puzzlegenerator.chess.puzzle_service.exception;

public class NoPuzzlesAvailableException extends RuntimeException {

    public NoPuzzlesAvailableException() {
        super("No puzzles available matching your criteria");
    }
}
