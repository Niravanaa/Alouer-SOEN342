# Alouer

A lesson booking system for the SOEN342 course, created by team 6.

## Description

The system, called Alouer, is a lesson booking system that allows clients and their dependents to book lessons. These lessons are assigned to seasonal instructors and are available at various locations. The system is managed by a single administrator who inputs lessons into the system.

The system supports persistence through an SQLite database and implements table-based session management. This prevents the same account of any type from being logged into multiple instances of the system simultaneously. The terminal UI interface heavily utilizes the `Scanner` class to take input from users.

## Getting Started

### Dependencies

- Java (JDK 8 or higher)
- Maven (as the build tool)
- SQLite (for database storage)
- JUnit (for testing)

### Installing

1. Clone the project repository.
   ```bash
   git clone https://github.com/Niravanaa/Alouer-SOEN342.git
   ```
2. Navigate to the project directory.
   ```bash
   cd Alouer-SOEN342/alouer
   ```
3. Build the project using Maven.
   ```bash
   mvn clean install
   ```

### Executing Program

1. Run the application from the terminal.
   ```bash
   java -jar target/alouer-0.1.jar
   ```
2. Follow the on-screen prompts to interact with the system through the terminal UI.

## Diagrams

Link to the [use case diagram](./iterationDeliverables/iteration-1/README.md)

Link to the [class diagram](./iterationDeliverables/iteration-2-3/ClassDiagram/README.md)

Link to the [domain model](./iterationDeliverables/iteration-2-3/DomainModel/README.md)

Link to the [interaction diagrams](./iterationDeliverables/iteration-2-3/InteractionDiagrams/README.md)

Link to the [operation contracts](./iterationDeliverables/iteration-2-3/OperationContracts/README.md)

Link to the [package diagram](./iterationDeliverables/iteration-2-3/PackageDiagram/README.md)

Link to the [system sequence diagrams](./iterationDeliverables/iteration-2-3/SystemSequenceDiagrams/README.md)

Link to the [OCL requirements](./iterationDeliverables/iteration-4/OCL-Requirements/README.md)

Link to the [relational data model](./iterationDeliverables/iteration-4/RelationalDataModel/README.md)

Link to the [demonstration (demo) video](./iterationDeliverables/iteration-4/DemoVideo/README.md)

## Help

If you encounter issues or require assistance, please contact either of the main developers Nirav Patel or Laurenz Gomez.

## Authors

- Nirav Patel (40248940)
- Laurenz Gomez (40252190)

## Course Information

- **Course:** SOEN342
- **Team Number:** 6
- **TA:** Amarta Lohana

## License

This project is licensed under the [MIT License](LICENSE.md).

## Acknowledgments

Special thanks to:

- Dr. Constantinos Constantinides for his teaching.
- Amarta Lohana for her guidance throughout the project.
- The people behind the maintenance of JDBC, JUnit, and SQLite.
