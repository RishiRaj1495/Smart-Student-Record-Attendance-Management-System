package com.vit.sms.ui;

import com.vit.sms.dao.AttendanceDAO;
import com.vit.sms.dao.StudentDAO;
import com.vit.sms.model.User;
import com.vit.sms.util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * HomePanel - Dashboard overview with stat cards (Unit 4)
 * Demonstrates: Swing Panels, GridLayout, BorderLayout, Custom painting
 */
public class HomePanel extends JPanel {

    private final User        currentUser;
    private final StudentDAO  studentDAO    = new StudentDAO();
    private final AttendanceDAO attDAO      = new AttendanceDAO();

    private JLabel lblTotalStudents;
    private JLabel lblAvgCgpa;
    private JLabel lblAttendance;

    public HomePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_MAIN);
        setBorder(new EmptyBorder(24, 28, 24, 28));
        initComponents();
        refresh();
    }

    private void initComponents() {
        // ── Page Title ────────────────────────────────────────────────────
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UITheme.PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 18, 0));
        add(title, BorderLayout.NORTH);

        // ── Content ───────────────────────────────────────────────────────
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // Stat cards row
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        lblTotalStudents = new JLabel("...");
        lblAvgCgpa       = new JLabel("...");
        lblAttendance    = new JLabel("...");

        statsRow.add(statCard("Total Students", lblTotalStudents, UITheme.PRIMARY,     "👥"));
        statsRow.add(statCard("Average CGPA",   lblAvgCgpa,       UITheme.SUCCESS,     "📈"));
        statsRow.add(statCard("Attendance Logs", lblAttendance,   UITheme.INFO,        "📋"));

        content.add(statsRow);
        content.add(Box.createVerticalStrut(24));

        // Info row
        JPanel infoRow = new JPanel(new GridLayout(1, 2, 16, 0));
        infoRow.setOpaque(false);
        infoRow.add(buildQuickInfo());
        infoRow.add(buildGroupInfo());
        content.add(infoRow);

        add(content, BorderLayout.CENTER);
    }

    private JPanel statCard(String title, JLabel valueLbl, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(UITheme.BORDER_COLOR, 1, true),
            new EmptyBorder(18, 20, 18, 20)
        ));

        JLabel iconLbl = new JLabel(icon + "  " + title);
        iconLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        iconLbl.setForeground(UITheme.TEXT_SECONDARY);

        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 34));
        valueLbl.setForeground(color);

        JPanel topBar = new JPanel();
        topBar.setBackground(color);
        topBar.setPreferredSize(new Dimension(0, 4));

        card.add(topBar,    BorderLayout.NORTH);
        card.add(iconLbl,   BorderLayout.CENTER);
        card.add(valueLbl,  BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildQuickInfo() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(UITheme.BORDER_COLOR, 1, true),
            new EmptyBorder(18, 20, 18, 20)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel h = new JLabel("📌  Course Information");
        h.setFont(UITheme.FONT_HEADING);
        h.setForeground(UITheme.PRIMARY);
        card.add(h);
        card.add(Box.createVerticalStrut(12));

        String[][] info = {
            {"Course Code",  "CSE4019"},
            {"Course Name",  "Advanced Java Programming"},
            {"Faculty",      "Vandana Shakya"},
            {"Slot",         "B14+D21"},
            {"Credits",      "3 LP"},
            {"Academic Year","2024–25"},
        };

        for (String[] row : info) {
            JPanel r = new JPanel(new BorderLayout());
            r.setOpaque(false);
            r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
            JLabel k = new JLabel(row[0]);
            k.setFont(UITheme.FONT_SMALL);
            k.setForeground(UITheme.TEXT_SECONDARY);
            k.setPreferredSize(new Dimension(130, 22));
            JLabel v = new JLabel(row[1]);
            v.setFont(new Font("Segoe UI", Font.BOLD, 12));
            v.setForeground(UITheme.TEXT_PRIMARY);
            r.add(k, BorderLayout.WEST);
            r.add(v, BorderLayout.CENTER);
            card.add(r);
        }
        return card;
    }

    private JPanel buildGroupInfo() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(UITheme.BORDER_COLOR, 1, true),
            new EmptyBorder(18, 20, 18, 20)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel h = new JLabel("👥  Project Group Members");
        h.setFont(UITheme.FONT_HEADING);
        h.setForeground(UITheme.PRIMARY);
        card.add(h);
        card.add(Box.createVerticalStrut(12));

        String[][] members = {
            {"Rishi Raj",      "24BCE10149", "Team Lead"},
            {"Abhilash Singh", "24BCE10706", "Backend"},
            {"Md Afzal Khan",  "24BCE11247", "Database"},
            {"Ayush Mishra",   "24BCE10019", "Frontend"},
            {"Pranav Kumar",   "24BCE10080", "Testing"},
        };

        for (String[] m : members) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            row.setBorder(new EmptyBorder(2, 0, 2, 0));

            JLabel nameL = new JLabel("● " + m[0]);
            nameL.setFont(new Font("Segoe UI", Font.BOLD, 12));
            nameL.setForeground(UITheme.TEXT_PRIMARY);
            nameL.setPreferredSize(new Dimension(155, 24));

            JLabel regL = new JLabel(m[1]);
            regL.setFont(UITheme.FONT_SMALL);
            regL.setForeground(UITheme.TEXT_SECONDARY);
            regL.setPreferredSize(new Dimension(110, 24));

            JLabel roleL = new JLabel(m[2]);
            roleL.setFont(UITheme.FONT_SMALL);
            roleL.setForeground(UITheme.PRIMARY_LIGHT);

            row.add(nameL, BorderLayout.WEST);
            row.add(regL,  BorderLayout.CENTER);
            row.add(roleL, BorderLayout.EAST);
            card.add(row);
        }
        return card;
    }

    /** Reload stats from DB (called when navigating to this panel) */
    public void refresh() {
        new Thread(() -> {
            try {
                int    total = studentDAO.getTotalCount();
                double avg   = studentDAO.getAverageCgpa();
                int    att   = attDAO.getTotalAttendanceRecords();
                SwingUtilities.invokeLater(() -> {
                    lblTotalStudents.setText(String.valueOf(total));
                    lblAvgCgpa.setText(String.format("%.2f", avg));
                    lblAttendance.setText(String.valueOf(att));
                });
            } catch (SQLException e) {
                SwingUtilities.invokeLater(() -> {
                    lblTotalStudents.setText("—");
                    lblAvgCgpa.setText("—");
                    lblAttendance.setText("—");
                });
            }
        }).start();
    }
}
