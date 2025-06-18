# Sokoban Project 24-25

### Description
This project implements a Sokoban puzzle game in Java. It allows the player to move boxes to designated goal tiles using keyboard controls. It has been developed for the non-compulsory subject **'Programming Project'** at **ETSIINF, Universidad Politécnica de Madrid**.

### Systems Used
For the development of this project, we have used:
- **Java 11** → Main programming language.
- **Maven** → Project management and build automation.
- **GitLab** → Version control and continuous integration.
- **JUnit 5** → Unit testing.
- **SLF4J + Log4j** → Logging system.
- **SonarQube** → Static code analysis and test coverage.

### How to Install and Test
(You need to have Java, Git, and Maven installed on your machine)

1. **Clone the repository**
   ```sh
   git clone $PROJECT_URL
   ```
2. **Move to the project directory**
   ```sh
   cd project-sokoban-24-25
   ```
3. **Run the tests**
   ```sh
   mvn test
   ```
4. **Run the quality analysis**
   ```sh
   mvn clean verify sonar:sonar -Dsonar.id=$XXX -Dsonar.login=$YYY
   ```

### Sokoban's features

#### 1. Game Logic
- Level loading and parsing from `.txt` files
- Movement tracking and undo system
- Victory detection and scoring system
- Box-goal matching logic
- Level validation before loading

#### 2. Interface
- Graphical interface built with Swing
- Responsive to `WASD` or arrow keys
- Menu for game options (pause, reset, quit, etc.)

#### 3. Extras
- Logging with SLF4J+Log4j at `logs/application.log`
- Game state saving and loading with serialization
- Audio effects and background music controllers

---

### File Structure Overview

#### `src/main/java/`
- `controller/`: Manages input and game flow
- `model/`: Core logic and entities like `Player`, `Box`, `Tile`, `Level`, etc.
- `view/`: GUI implementation
- `exceptions/`: Custom exception classes

#### `src/main/resources/`
- Level files (`level1.txt`, `level2.txt`, …)
- Audio files (`glitch.mp3`, …)
- Image files (`box.jpg`, `floor1.jpg`, …)

#### `logs/`
- Application logs saved here

#### `deliverables/`
- CSVs for backlogs and sprint tracking

---

### Game Controls
- `↑/↓/←/→` or `W/A/S/D`: Move the player
- Menu buttons: Access save/load, reset, undo, and exit

---

### Level File Format
Each level is a plain text file with the following characters:
- `#` = Wall  
- `.` = Goal  
- `@` = Player  
- `$` = Box  
- Space = Floor

Example:
```
#####
#@$.#
#####
```

---

### CI/CD Pipeline
Configured with `.gitlab-ci.yml` for:
- Automatic testing
- Build checks
- SonarQube integration