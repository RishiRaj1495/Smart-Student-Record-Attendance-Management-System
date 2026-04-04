package com.vit.sms.util;

import java.sql.*;

/**
 * DatabaseConnection - JDBC Connection Management (Unit 3)
 * Demonstrates: JDBC Driver loading, Connection pooling concept, Singleton pattern
 *
 * Project  : Smart Student Record & Attendance Management System
 * Institute: VIT Bhopal University
 */
public class DatabaseConnection {

    // ── Configure your MySQL credentials below ─────────────────────────────
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/vit_sms_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "root";
    private static final String DB_DRIVER   = "com.mysql.cj.jdbc.Driver";
    // ────────────────────────────────────────────────────────────────────────

    private static Connection connection = null;

    /** Singleton connection getter */
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DB_DRIVER);
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Please add mysql-connector-j.jar to classpath.\n" + e.getMessage());
        }
        return connection;
    }

    /** Safely close connection */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ignored) {}
    }

    /** Test if DB is reachable */
    public static boolean testConnection() {
        try {
            return getConnection() != null && !getConnection().isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /** Create all tables and seed initial data */
    public static void initializeDatabase() throws SQLException {
        Connection conn = getConnection();
        Statement  stmt = conn.createStatement();

        // ── Users Table ──────────────────────────────────────────────────────
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS users (" +
            "  id        INT AUTO_INCREMENT PRIMARY KEY," +
            "  username  VARCHAR(50)  UNIQUE NOT NULL," +
            "  password  VARCHAR(100) NOT NULL," +
            "  role      VARCHAR(20)  NOT NULL," +
            "  full_name VARCHAR(100)," +
            "  email     VARCHAR(100)," +
            "  active    BOOLEAN DEFAULT TRUE" +
            ")"
        );

        // ── Students Table ───────────────────────────────────────────────────
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS students (" +
            "  id        INT AUTO_INCREMENT PRIMARY KEY," +
            "  reg_no    VARCHAR(20) UNIQUE NOT NULL," +
            "  name      VARCHAR(100) NOT NULL," +
            "  email     VARCHAR(100)," +
            "  phone     VARCHAR(15)," +
            "  branch    VARCHAR(50)," +
            "  semester  INT DEFAULT 1," +
            "  cgpa      DECIMAL(4,2) DEFAULT 0.00," +
            "  gender    VARCHAR(10)," +
            "  status    VARCHAR(20) DEFAULT 'ACTIVE'," +
            "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")"
        );

        // ── Attendance Table ─────────────────────────────────────────────────
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS attendance (" +
            "  id              INT AUTO_INCREMENT PRIMARY KEY," +
            "  student_id      INT NOT NULL," +
            "  subject         VARCHAR(100)," +
            "  attendance_date DATE NOT NULL," +
            "  status          VARCHAR(10) NOT NULL," +
            "  remarks         VARCHAR(255)," +
            "  FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE" +
            ")"
        );

        // ── Seed Users ───────────────────────────────────────────────────────
        stmt.execute("INSERT IGNORE INTO users (username,password,role,full_name,email) VALUES " +
            "('admin','admin123','ADMIN','System Administrator','admin@vitbhopal.ac.in')");
        stmt.execute("INSERT IGNORE INTO users (username,password,role,full_name,email) VALUES " +
            "('vandana','faculty123','FACULTY','Vandana Shakya','vandana.shakya@vitbhopal.ac.in')");
        stmt.execute("INSERT IGNORE INTO users (username,password,role,full_name,email) VALUES " +
            "('rishi','rishi123','STUDENT','Rishi Raj','rishi.raj@vitbhopal.ac.in')");

        stmt.close();
    }
}
