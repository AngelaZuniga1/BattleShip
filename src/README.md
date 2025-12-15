# Naval Battle Game

A complete implementation of the classic Battleship game in Java with JavaFX, following MVC architecture and SOLID principles.

## Features Implemented

### User Stories:
- **HU-1: Ship Placement** - Place ships on your board with validation
- **HU-2: Firing** - Shoot at enemy board with real-time feedback
- **HU-3: View Computer Board** - Teacher mode to verify computer's ship placement
- **HU-4: Computer AI** - Intelligent computer opponent with random ship placement and shooting
- **HU-5: Auto Save** - Game automatically saves after every move

### Technical Features:
- **MVC Architecture** - Clear separation of concerns
- **SOLID Principles** - All 5 principles implemented
- **Design Patterns**:
    - Command Pattern (Game actions)
    - Observer Pattern (Game state updates)
    - Strategy Pattern (AI strategies)
- **Concurrency** - Computer turns run in separate threads
- **Persistence**:
    - Serializable files for game state
    - Flat files for player statistics
- **Custom Exceptions** - Both checked and unchecked
- **Unit Tests** - Comprehensive test coverage with JUnit 5
- **2D Graphics** - Detailed visual representation of ships, hits, misses, and sunk ships

