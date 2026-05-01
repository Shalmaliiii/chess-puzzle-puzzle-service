package com.puzzlegenerator.chess.puzzle_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic puzzleGenerateTopic() {
        return TopicBuilder.name("puzzle.generate")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic puzzleSolvedTopic() {
        return TopicBuilder.name("puzzle.solved")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic puzzleGeneratedTopic() {
        return TopicBuilder.name("puzzle.generated")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
