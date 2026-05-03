package com.puzzlegenerator.chess.puzzle_service.kafka;

import com.puzzlegenerator.chess.puzzle_service.dto.PuzzleSolvedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PuzzleSolvedProducer {

    private final KafkaTemplate<String, PuzzleSolvedEvent> kafkaTemplate;

    public void sendSolvedEvent(PuzzleSolvedEvent event) {
        log.info("Publishing puzzle solved event: userId={}, puzzleId={}, difficulty={}",
                event.getUserId(), event.getPuzzleId(), event.getDifficulty());
        kafkaTemplate.send("puzzle.solved", event);
    }
}
