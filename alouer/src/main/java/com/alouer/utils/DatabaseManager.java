package com.alouer.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:alouer.db";
    private static final String SCHEMA_FILE = "/schema.sql";
    private static final String SEED_FILE = "/seed.sql";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, "", "");
    }

    public static void initializeDatabase(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                promptResetAndSeed(conn, scanner);
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    private static void promptResetAndSeed(Connection conn, Scanner scanner) {
        System.out.print("Would you like to reset and seed the database? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            resetAndSeedDatabase(conn);
        } else {
            System.out.println("Database initialization complete without resetting.");
        }
    }

    private static void resetAndSeedDatabase(Connection conn) {
        try {
            System.out.println("Resetting database...");
            executeSqlFile(conn, SCHEMA_FILE);
            System.out.println("Seeding initial data...");
            executeSqlFile(conn, SEED_FILE);
        } catch (Exception e) {
            System.err.println("Error resetting and seeding database: " + e.getMessage());
        }
    }

    private static void executeSqlFile(Connection conn, String filePath) {
        try (InputStream is = DatabaseManager.class.getResourceAsStream(filePath);
                Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
            scanner.useDelimiter(";");
            try (Statement stmt = conn.createStatement()) {
                while (scanner.hasNext()) {
                    String sqlStatement = scanner.next().trim();
                    if (!sqlStatement.isEmpty()) {
                        stmt.execute(sqlStatement);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error executing SQL file " + filePath + ": " + e.getMessage());
        }
    }
}
