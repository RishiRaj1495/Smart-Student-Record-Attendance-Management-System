package com.vit.sms.dao;

import com.vit.sms.model.User;
import com.vit.sms.util.DatabaseConnection;

import java.sql.*;

/**
 * UserDAO - User authentication via JDBC (Unit 3)
 */
public class UserDAO {

    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=? AND password=? AND active=TRUE";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId      (rs.getInt    ("id"));
                    u.setUsername(rs.getString ("username"));
                    u.setPassword(rs.getString ("password"));
                    u.setRole    (rs.getString ("role"));
                    u.setFullName(rs.getString ("full_name"));
                    u.setEmail   (rs.getString ("email"));
                    u.setActive  (rs.getBoolean("active"));
                    return u;
                }
            }
        }
        return null;
    }

    public boolean addUser(User u) throws SQLException {
        String sql = "INSERT INTO users (username,password,role,full_name,email) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            ps.setString(4, u.getFullName());
            ps.setString(5, u.getEmail());
            return ps.executeUpdate() > 0;
        }
    }
}
