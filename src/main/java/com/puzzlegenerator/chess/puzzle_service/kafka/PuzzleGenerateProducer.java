package com.puzzlegenerator.chess.puzzle_service.kafka;

import com.puzzlegenerator.chess.puzzle_service.dto.PuzzleGenerateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PuzzleGenerateProducer {

    private final KafkaTemplate<String, PuzzleGenerateEvent> kafkaTemplate;

    public void sendGenerateEvent(PuzzleGenerateEvent event) {
        log.info("Publishing puzzle generate event: difficulty={}, count={}, requestedBy={}",
                event.getDifficulty(), event.getCount(), event.getRequestedBy());
        kafkaTemplate.send("puzzle.generate", event);
    }
}
