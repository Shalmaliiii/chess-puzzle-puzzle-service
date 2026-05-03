package com.puzzlegenerator.chess.puzzle_service.repository;

import com.puzzlegenerator.chess.puzzle_service.model.Puzzle;
import com.puzzlegenerator.chess.puzzle_service.model.PuzzleDifficulty;
import com.puzzlegenerator.chess.puzzle_service.model.PuzzleStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PuzzleRepository extends MongoRepository<Puzzle, String> {

    List<Puzzle> findByDifficultyAndStatusAndSolvedByNotContaining(
            PuzzleDifficulty difficulty, PuzzleStatus status, String userId);

    List<Puzzle> findByStatusAndSolvedByNotContaining(PuzzleStatus status, String userId);

    List<Puzzle> findByDifficultyAndStatus(PuzzleDifficulty difficulty, PuzzleStatus status);

    List<Puzzle> findByStatus(PuzzleStatus status);

    long countByDifficulty(PuzzleDifficulty difficulty);

    long countByStatus(PuzzleStatus status);

    Optional<Puzzle> findByFen(String fen);

    boolean existsByFen(String fen);
}
