# 2006ICT Tetris - Final Submission

Enhanced JavaFX Tetris developed from the PG2 Milestone 1 project.

## Requirements and run commands

- JDK 21
- Maven 3.9+

```bash
mvn clean test
mvn jacoco:report
mvn javafx:run
```

The coverage report is generated at `target/site/jacoco/index.html`.

## Controls

- Player 1: arrow keys to move/rotate/soft-drop; Space to hard-drop
- Player 2 Human: A/D to move, W to rotate, C to soft-drop, X to hard-drop
- P: pause/resume all boards
- M: music toggle
- S: sound-effects toggle in every game mode

## Final features

- Correct line scoring: 100, 300, 600 and 1000 points
- Live player type, level, score and lines display
- JSON persistence for configuration and top-ten high scores
- New-high-score name dialog and clear/reset control
- Distinct sound effects for movement, rotation, hard drop, piece locking, line clearing, pause/resume and game over
- Dynamic, centered window sizing for configured fields and player count
- AI player using a board-evaluation Strategy
- Reconnecting external-player TCP client at `localhost:3000`
- Simultaneous two-player mode with a shared deterministic seven-bag sequence
- Singleton (`SettingsManager`, `AudioManager`), Factory (`TetrominoFactory`), Observer (`GameEventListener`), Command (`GameCommand`) and Strategy (`PlayerStrategy`) patterns
- Enum, streams, external JavaFX CSS, lambdas and Comparator/Comparable
- Background threads, Gson JSON, networking, file I/O and generics
- JUnit 5 tests, parameterized tests, Fake, Spy, Mockito Mock and JaCoCo

## External-player protocol

Start the supplied `TetrisServer.jar` on port 3000 before or during a game. The client implements its exact request/response protocol: it sends a one-line JSON snapshot containing the board cells, current shape and next shape. The server replies with `opX` and `opRotate`. A pending move is retried every second, so starting the server during a game resumes External control automatically.

## Runtime files

The application creates `data/settings.json` and `data/highscores.json` automatically.

## Architecture

- Model: `GameBoard`, `Tetromino`, `TetrominoType`, `GameSettings`, `HighScore`
- View: `TetrisApplication`, `GamePane`, `tetris.css`
- Controller: `GameController` and Command bindings in `GamePane`
- Services: JSON repositories, settings/audio singletons, AI strategy and networking client
