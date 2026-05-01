# Chess Puzzle Service

A Spring Boot microservice for the Chess Puzzle Platform that manages chess puzzles, move validation, and puzzle statistics.

## Tech Stack

- **Java 21** (LTS)
- **Spring Boot 3.4.x**
- **Spring Data MongoDB**
- **Spring Kafka**
- **Gradle** (Groovy DSL)

## Features

- **Puzzle CRUD** - Create, read, and manage chess puzzles
- **Move Validation** - Validate player moves against puzzle solutions
- **Puzzle Solving** - Track puzzle completions and solve times
- **Kafka Integration** - Publish/consume puzzle generation and solving events
- **Data Seeding** - Auto-seeds 20 puzzles on first startup
- **Admin Endpoints** - Puzzle generation requests (admin-only)
- **Statistics** - Puzzle pool statistics by difficulty and status

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/puzzles/next` | Get next unsolved puzzle |
| GET | `/api/puzzles/{id}` | Get puzzle by ID |
| POST | `/api/puzzles/{id}/validate` | Validate a move |
| POST | `/api/puzzles/{id}/solve` | Mark puzzle as solved |
| POST | `/api/puzzles/generate` | Request puzzle generation (admin) |
| GET | `/api/puzzles/stats` | Get puzzle statistics |

## Configuration

The service runs on port **8082** by default.

### Default Profile
```yaml
spring.data.mongodb.uri: mongodb://localhost:27017/chess_puzzles
spring.kafka.bootstrap-servers: kafka:9092
```

### Docker Profile
```yaml
spring.data.mongodb.uri: mongodb://mongodb:27017/chess_puzzles
```

## Running Locally

### Prerequisites
- Java 21
- MongoDB running on localhost:27017
- Kafka (optional, for event-driven features)

### Build & Run
```bash
./gradlew bootRun
```

### Build JAR
```bash
./gradlew bootJar
```

### Run Tests
```bash
./gradlew test
```

## Docker

```bash
docker build -t puzzle-service .
docker run -p 8082:8082 --network chess-network puzzle-service
```

## Kafka Topics

| Topic | Direction | Description |
|-------|-----------|-------------|
| `puzzle.generate` | Produce | Request puzzle generation |
| `puzzle.solved` | Produce | Puzzle solved event |
| `puzzle.generated` | Consume | Receive generated puzzles |

## Package Structure

```
com.puzzlegenerator.chess.puzzle_service
├── config/          # Kafka config, data seeder
├── controller/      # REST controllers
├── dto/             # Request/response/event DTOs
├── exception/       # Custom exceptions and handler
├── kafka/           # Kafka producers and consumers
├── model/           # MongoDB documents and enums
├── repository/      # Spring Data MongoDB repositories
└── service/         # Business logic
```
