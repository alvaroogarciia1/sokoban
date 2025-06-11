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

### Application Information

#### Files Read/Written
- **Level files**: Read from `src/main/resources/levelX.txt`.
- **Log file**: Written to `logs/application.log` using Log4j.
- **Saved games**: Written/read from `savegame.dat`.

#### Game Controls
- **Arrow Keys or W,A,S,D** → Move the player (↑==W ↓==S ←==A →==D).
- **Main buttons** → Use different actions of the main window of the game (not all implemented right now!).

#### File Formats
- **Level files**: Plain text, using characters like `#` (wall), `.` (goal), `@` (player), `$` (box), and space (floor).
- **Log file**: Structured plain text (one event per line).
- **Savegame**: Java-serialized object storing player, boxes, and level info.