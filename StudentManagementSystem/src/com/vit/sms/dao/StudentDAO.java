package com.vit.sms.dao;

import com.vit.sms.model.Student;
import com.vit.sms.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentDAO - JDBC CRUD operations (Unit 3)
 * Demonstrates: PreparedStatement, ResultSet, Transaction management,
 *               SQL-to-Java Type Mapping, java.sql API usage
 */
public class StudentDAO {

    // ── INSERT ────────────────────────────────────────────────────────────
    public boolean addStudent(Student s) throws SQLException {
        String sql = "INSERT INTO students (reg_no,name,email,phone,branch,semester,cgpa,gender,status) " +
                     "VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, s.getRegNo());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getBranch());
            ps.setInt   (6, s.getSemester());
            ps.setDouble(7, s.getCgpa());
            ps.setString(8, s.getGender());
            ps.setString(9, s.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    // ── SELECT ALL ────────────────────────────────────────────────────────
    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY reg_no";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // ── SELECT BY ID ─────────────────────────────────────────────────────
    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    // ── SEARCH ───────────────────────────────────────────────────────────
    public List<Student> searchStudents(String keyword) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE " +
                     "LOWER(name) LIKE ? OR LOWER(reg_no) LIKE ? OR LOWER(branch) LIKE ? " +
                     "ORDER BY reg_no";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            String kw = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────
    public boolean updateStudent(Student s) throws SQLException {
        String sql = "UPDATE students SET name=?,email=?,phone=?,branch=?,semester=?," +
                     "cgpa=?,gender=?,status=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getBranch());
            ps.setInt   (5, s.getSemester());
            ps.setDouble(6, s.getCgpa());
            ps.setString(7, s.getGender());
            ps.setString(8, s.getStatus());
            ps.setInt   (9, s.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────
    public boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── COUNT ─────────────────────────────────────────────────────────────
    public int getTotalCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM students WHERE status='ACTIVE'";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // ── AVG CGPA ─────────────────────────────────────────────────────────
    public double getAverageCgpa() throws SQLException {
        String sql = "SELECT AVG(cgpa) FROM students WHERE status='ACTIVE'";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }

    // ── COUNT BY BRANCH ───────────────────────────────────────────────────
    public List<Object[]> getCountByBranch() throws SQLException {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT branch, COUNT(*) as cnt FROM students GROUP BY branch ORDER BY cnt DESC";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new Object[]{ rs.getString("branch"), rs.getInt("cnt") });
            }
        }
        return result;
    }

    // ── BATCH INSERT (for seeding sample data) ────────────────────────────
    public void batchInsert(List<Student> students) throws SQLException {
        String sql = "INSERT IGNORE INTO students (reg_no,name,email,phone,branch,semester,cgpa,gender,status) " +
                     "VALUES (?,?,?,?,?,?,?,?,?)";
        Connection conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);   // Transaction management
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Student s : students) {
                ps.setString(1, s.getRegNo());
                ps.setString(2, s.getName());
                ps.setString(3, s.getEmail());
                ps.setString(4, s.getBranch() != null ? s.getBranch() : "CSE");
                ps.setString(5, s.getBranch() != null ? s.getBranch() : "CSE");
                ps.setInt   (6, s.getSemester() > 0 ? s.getSemester() : 4);
                ps.setDouble(7, s.getCgpa() > 0 ? s.getCgpa() : 7.5 + Math.random() * 2.0);
                ps.setString(8, s.getGender() != null ? s.getGender() : "Male");
                ps.setString(9, "ACTIVE");
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // ── ROW MAPPER ────────────────────────────────────────────────────────
    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId      (rs.getInt   ("id"));
        s.setRegNo   (rs.getString("reg_no"));
        s.setName    (rs.getString("name"));
        s.setEmail   (rs.getString("email"));
        s.setPhone   (rs.getString("phone"));
        s.setBranch  (rs.getString("branch"));
        s.setSemester(rs.getInt   ("semester"));
        s.setCgpa    (rs.getDouble("cgpa"));
        s.setGender  (rs.getString("gender"));
        s.setStatus  (rs.getString("status"));
        return s;
    }
}
