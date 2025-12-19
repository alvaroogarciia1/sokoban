# Sokoban – Java Desktop Game (Programming Project)

## 📌 Description
This project is an implementation of the classic puzzle game **Sokoban**, developed in **Java** following object-oriented design principles and good software engineering practices.

The objective of the game is to move all boxes to their corresponding goal positions by pushing them inside a warehouse-like board, following the game rules and constraints.

The project was originally developed as part of the **Programming Project** course at the **Universidad Politécnica de Madrid (UPM)** and includes multiple levels, undo functionality, game persistence, automated testing and continuous integration.

---

## 🛠️ Technologies
- Java
- Maven
- Java Swing (GUI)
- JUnit 5 (testing)
- SLF4J (logging)
- Git & Continuous Integration

---

## 🎮 Game Features

### Core gameplay
- Multiple levels loaded from plain text files
- Grid-based board composed of walls, boxes, goals and player
- Player movement restricted to horizontal and vertical directions
- Box pushing mechanics with collision detection
- Visual indication when a box is placed on a goal
- Automatic detection of level completion

### Levels
- Levels are read from `levelN.txt` files
- Each level defines:
  - Level name
  - Board dimensions
  - Initial layout
- Level validation:
  - Exactly one player
  - Same number of boxes and goals
  - At least one box and one goal

### Scoring system
- Level score based on the number of player movements
- Total game score accumulates the scores of completed levels

---

## 🔄 Advanced Functionality

### Undo system
- All movements in the current level can be undone
- Full movement history is recorded since the beginning of the level

### Save & Load
- The game state can be saved to a file selected by the user
- Saved information includes:
  - Current level and board state
  - Undo movement history
  - Current level score
  - Total game score
- Saved games can be restored at any time

### Graphical User Interface
- Desktop graphical interface built using Java Swing
- Displays:
  - Game board
  - Level name
  - Level score
  - Total game score
- Menu options:
  - New game
  - Restart level
  - Undo move
  - Save game
  - Load game
  - Exit application

---

## 🧪 Testing & Quality
- Automated test suite developed with **JUnit**
- Tests are executed automatically using `mvn test`
- Logging system implemented using **SLF4J**
- Continuous Integration pipeline configured to run the test suite on each push

---

## 📚 What I Learned
- Object-oriented design in Java applications
- Separation of concerns (model, view and controller)
- File parsing and validation
- Undo mechanisms and state management
- Game state persistence
- Automated testing with JUnit
- Maven project structuring
- Continuous Integration workflows
- Logging and debugging techniques

---

## 🚀 How to Build and Run

### Requirements
- Java 11 or higher
- Maven

### Build and run tests
```bash
mvn clean test
```
### Run the application
```bash
mvn exec:java
