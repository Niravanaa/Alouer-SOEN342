package com.alouer.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.alouer.enums.UserType;

public class Session {
    private Integer userId;
    private String email;
    private UserType role;

    private static final String DB_URL = "jdbc:sqlite:alouer.db";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    public Session(Integer userId, UserType role) {
        this.userId = userId;
        this.role = role;
    }

    public Session(String email, UserType role) {
        this.email = email;
        this.role = role;
    }

    public boolean save() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO session (userId, email, role, createdAt, expiresAt) VALUES (?, ?, ?, CURRENT_TIMESTAMP, DATETIME('now', '+30 seconds'))";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                if ("ADMINISTRATOR".equals(role.toString())) {
                    stmt.setNull(1, Types.INTEGER);
                    stmt.setString(2, email);
                } else {
                    stmt.setInt(1, userId);
                    stmt.setNull(2, Types.VARCHAR);
                }
                stmt.setString(3, role.toString());
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasActiveSession(int userId, UserType userType) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM session WHERE userId = ? AND role = ? AND expiresAt > CURRENT_TIMESTAMP";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setString(2, userType.toString());
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasActiveSession(String email) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM session WHERE email = ? AND expiresAt > CURRENT_TIMESTAMP";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void deleteSession(int userId, UserType userType) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM session WHERE userId = ? AND role = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setString(2, userType.toString());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteSession(String email) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM session WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
