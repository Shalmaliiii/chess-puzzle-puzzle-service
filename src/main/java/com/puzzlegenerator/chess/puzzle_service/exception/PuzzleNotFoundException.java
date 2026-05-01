package com.puzzlegenerator.chess.puzzle_service.exception;

public class PuzzleNotFoundException extends RuntimeException {

    public PuzzleNotFoundException(String id) {
        super("Puzzle not found with id: " + id);
    }
}
