package com.puzzlegenerator.chess.puzzle_service.controller;

import com.puzzlegenerator.chess.puzzle_service.dto.*;
import com.puzzlegenerator.chess.puzzle_service.service.PuzzleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/puzzles")
@RequiredArgsConstructor
public class PuzzleController {

    private final PuzzleService puzzleService;

    @GetMapping("/next")
    public ResponseEntity<PuzzleResponse> getNextPuzzle(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestParam(required = false) String difficulty) {
        log.info("Getting next puzzle for user: {}, difficulty: {}", userId, difficulty);

        if (userId == null || userId.isBlank()) {
            userId = "anonymous";
        }

        PuzzleResponse response = puzzleService.getNextPuzzle(userId, difficulty);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuzzleResponse> getPuzzleById(@PathVariable String id) {
        log.info("Getting puzzle by id: {}", id);
        PuzzleResponse response = puzzleService.getPuzzleById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<MoveValidationResponse> validateMove(
            @PathVariable String id,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody MoveValidationRequest request) {
        log.info("Validating move for puzzle: {}, user: {}, move: {}, moveNumber: {}",
                id, userId, request.getMove(), request.getMoveNumber());

        if (userId == null || userId.isBlank()) {
            userId = "anonymous";
        }

        MoveValidationResponse response = puzzleService.validateMove(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/solve")
    public ResponseEntity<PuzzleSolveResponse> solvePuzzle(
            @PathVariable String id,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody PuzzleSolveRequest request) {
        log.info("Solving puzzle: {} by user: {}", id, userId);

        if (userId == null || userId.isBlank()) {
            userId = "anonymous";
        }

        PuzzleSolveResponse response = puzzleService.solvePuzzle(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generatePuzzles(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Role", required = false) String userRole,
            @Valid @RequestBody PuzzleGenerateRequest request) {
        log.info("Generate request from user: {}, role: {}", userId, userRole);

        if (!"ADMIN".equalsIgnoreCase(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", (Object) "Admin access required"));
        }

        if (userId == null || userId.isBlank()) {
            userId = "anonymous";
        }

        Map<String, Object> response = puzzleService.requestGeneration(userId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<PuzzleStatsResponse> getStats() {
        log.info("Getting puzzle stats");
        PuzzleStatsResponse response = puzzleService.getStats();
        return ResponseEntity.ok(response);
    }
}
