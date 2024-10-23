import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Terminal {

    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static final String SCHEMA_FILE = "schema.schema";
    private Connection connection;

    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        terminal.displayWelcomeMessage();
        terminal.initializeDatabase();
        terminal.showLoginMenu();
    }

    private void displayWelcomeMessage() {
        System.out.println("Welcome to Alouer Terminal");
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            if (connection != null) {
                System.out.println("Connected to the database successfully.");
                if (!schemaExists() || hasSchemaChanged()) {
                    System.out.println("Schema has changed or does not exist. Reinitializing database...");
                    wipeAndReinitialize();
                } else {
                    System.out.println("Database schema is up to date.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection to database failed: " + e.getMessage());
        }
    }

    private void showLoginMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String role = validateLogin(email, password);
        if (role != null) {
            System.out.println("Login successful! Role: " + role);
            showOperationsMenu(role);
        } else {
            System.out.println("Invalid email or password. Please try again.");
            showLoginMenu();
        }
        scanner.close();
    }

    private String validateLogin(String email, String password) {
        String role = null;
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT role FROM Client WHERE email = '" + email + "' AND password = '" + password + "';");
            if (rs.next()) {
                role = rs.getString("role");
            }
        } catch (SQLException e) {
            System.out.println("Error validating login: " + e.getMessage());
        }
        return role;
    }

    private void showOperationsMenu(String role) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an operation:");
        if ("ADMIN".equals(role)) {
            System.out.println("1. View all clients");
            System.out.println("2. Manage instructors");
            System.out.println("3. Manage locations");
        } else if ("CLIENT".equals(role)) {
            System.out.println("1. View my bookings");
            System.out.println("2. Book a lesson");
            System.out.println("3. Update my profile");
        }
        System.out.println("4. Logout");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                if ("ADMIN".equals(role)) {
                    viewAllClients();
                } else if ("CLIENT".equals(role)) {
                    viewMyBookings();
                }
                break;
            case 2:
                if ("ADMIN".equals(role)) {
                    manageInstructors();
                } else {
                    bookALesson();
                }
                break;
            case 3:
                if ("ADMIN".equals(role)) {
                    manageLocations();
                } else {
                    updateMyProfile();
                }
                break;
            case 4:
                System.out.println("Logging out...");
                return;
            default:
                System.out.println("Invalid option, please try again.");
                showOperationsMenu(role);
                break;
        }
        scanner.close();
    }

    private void viewAllClients() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Client;")) {
            while (rs.next()) {
                System.out.println("Client ID: " + rs.getInt("id") + ", Name: " + rs.getString("firstName") + " " + rs.getString("lastName"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving clients: " + e.getMessage());
        }
    }

    private void viewMyBookings() {
        // Placeholder for viewing client bookings
        System.out.println("Viewing my bookings...");
    }

    private void manageInstructors() {
        // Placeholder for managing instructors
        System.out.println("Managing instructors...");
    }

    private void bookALesson() {
        // Placeholder for booking a lesson
        System.out.println("Booking a lesson...");
    }

    private void manageLocations() {
        // Placeholder for managing locations
        System.out.println("Managing locations...");
    }

    private void updateMyProfile() {
        // Placeholder for updating client profile
        System.out.println("Updating my profile...");
    }

    private boolean schemaExists() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Client';");
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking schema: " + e.getMessage());
            return false;
        }
    }

    private boolean hasSchemaChanged() {
        String currentSchema = getCurrentSchema();
        String fileSchema = getSchemaFromFile();
        return !currentSchema.equals(fileSchema);
    }

    private String getCurrentSchema() {
        StringBuilder schema = new StringBuilder();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT sql FROM sqlite_master WHERE type='table'")) {
            while (rs.next()) {
                schema.append(rs.getString("sql")).append("\n");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving current schema: " + e.getMessage());
        }
        return schema.toString();
    }

    private String getSchemaFromFile() {
        StringBuilder schema = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(SCHEMA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                schema.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading schema file: " + e.getMessage());
        }
        return schema.toString();
    }

    private void wipeAndReinitialize() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Client;");
            // Add other drop statements for other tables as necessary
            createSchemaFromFile();
        } catch (SQLException e) {
            System.out.println("Error wiping and reinitializing database: " + e.getMessage());
        }
    }

    private void createSchemaFromFile() {
        try {
            String schema = new String(Files.readAllBytes(Paths.get(SCHEMA_FILE)));
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(schema);
                System.out.println("Database schema created successfully from schema.sql.");
            }
        } catch (IOException e) {
            System.out.println("Error reading schema file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error creating schema from file: " + e.getMessage());
        }
    }
    

    private void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error while disconnecting: " + e.getMessage());
        }
    }
}
