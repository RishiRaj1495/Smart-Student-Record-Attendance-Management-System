package com.vit.sms.dao;

import com.vit.sms.model.Attendance;
import com.vit.sms.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AttendanceDAO - JDBC operations for attendance (Unit 3)
 */
public class AttendanceDAO {

    public boolean markAttendance(Attendance a) throws SQLException {
        String sql = "INSERT INTO attendance (student_id,subject,attendance_date,status,remarks) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt   (1, a.getStudentId());
            ps.setString(2, a.getSubject());
            ps.setString(3, a.getAttendanceDate());
            ps.setString(4, a.getStatus());
            ps.setString(5, a.getRemarks());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Attendance> getAttendanceByStudent(int studentId) throws SQLException {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.*, s.name as student_name, s.reg_no " +
                     "FROM attendance a JOIN students s ON a.student_id = s.id " +
                     "WHERE a.student_id = ? ORDER BY a.attendance_date DESC";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Attendance> getAllAttendance() throws SQLException {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.*, s.name as student_name, s.reg_no " +
                     "FROM attendance a JOIN students s ON a.student_id = s.id " +
                     "ORDER BY a.attendance_date DESC LIMIT 500";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    /** Returns attendance % for a student in a subject */
    public double getAttendancePercent(int studentId, String subject) throws SQLException {
        String sql = "SELECT " +
                     "  COUNT(*) as total, " +
                     "  SUM(CASE WHEN status='PRESENT' THEN 1 ELSE 0 END) as present " +
                     "FROM attendance WHERE student_id=? AND subject=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt   (1, studentId);
            ps.setString(2, subject);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int total   = rs.getInt("total");
                    int present = rs.getInt("present");
                    return total > 0 ? (present * 100.0 / total) : 0.0;
                }
            }
        }
        return 0.0;
    }

    public int getTotalAttendanceRecords() throws SQLException {
        String sql = "SELECT COUNT(*) FROM attendance";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Attendance mapRow(ResultSet rs) throws SQLException {
        Attendance a = new Attendance();
        a.setId            (rs.getInt   ("id"));
        a.setStudentId     (rs.getInt   ("student_id"));
        a.setStudentName   (rs.getString("student_name"));
        a.setRegNo         (rs.getString("reg_no"));
        a.setSubject       (rs.getString("subject"));
        a.setAttendanceDate(rs.getString("attendance_date"));
        a.setStatus        (rs.getString("status"));
        a.setRemarks       (rs.getString("remarks"));
        return a;
    }
}
