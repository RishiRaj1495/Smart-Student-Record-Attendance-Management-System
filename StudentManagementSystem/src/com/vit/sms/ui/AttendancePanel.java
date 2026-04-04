package com.vit.sms.ui;

import com.vit.sms.dao.AttendanceDAO;
import com.vit.sms.dao.StudentDAO;
import com.vit.sms.model.Attendance;
import com.vit.sms.model.Student;
import com.vit.sms.util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * AttendancePanel - Attendance management (Unit 3 + 4)
 * Demonstrates: JSplitPane, JComboBox, JTable, Event handling, JDBC Joins
 */
public class AttendancePanel extends JPanel {

    private final AttendanceDAO attDAO = new AttendanceDAO();
    private final StudentDAO    stuDAO = new StudentDAO();

    private DefaultTableModel logModel;
    private JTable            logTable;
    private JComboBox<String> cmbStudent, cmbSubject, cmbStatus;
    private JTextField        txtDate;
    private JTextField        txtRemarks;
    private JLabel            lblMsg;

    private static final String[] SUBJECTS = {
        "Advanced Java Programming (CSE4019)",
        "Data Structures & Algorithms",
        "Operating Systems",
        "Computer Networks",
        "Database Management Systems",
        "Software Engineering",
        "Machine Learning",
        "Web Technologies"
    };

    public AttendancePanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_MAIN);
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        JLabel title = new JLabel("Attendance Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UITheme.PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        add(title, BorderLayout.NORTH);

        // Split: form left, log right
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            buildMarkForm(), buildLogTable());
        split.setDividerLocation(340);
        split.setDividerSize(6);
        split.setContinuousLayout(true);
        split.setBorder(null);
        split.setBackground(UITheme.BG_MAIN);
        add(split, BorderLayout.CENTER);
    }

    // ── LEFT: MARK ATTENDANCE FORM ────────────────────────────────────────
    private JPanel buildMarkForm() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(UITheme.BORDER_COLOR, 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel h = new JLabel("Mark Attendance");
        h.setFont(UITheme.FONT_HEADING);
        h.setForeground(UITheme.PRIMARY);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(h);
        card.add(Box.createVerticalStrut(16));

        // Student selector
        card.add(formLabel("Student"));
        cmbStudent = new JComboBox<>();
        cmbStudent.setFont(UITheme.FONT_BODY);
        cmbStudent.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cmbStudent.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cmbStudent);
        card.add(Box.createVerticalStrut(10));

        // Subject
        card.add(formLabel("Subject"));
        cmbSubject = new JComboBox<>(SUBJECTS);
        cmbSubject.setFont(UITheme.FONT_BODY);
        cmbSubject.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cmbSubject.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cmbSubject);
        card.add(Box.createVerticalStrut(10));

        // Date
        card.add(formLabel("Date (YYYY-MM-DD)"));
        txtDate = new JTextField(LocalDate.now().toString());
        styleField(txtDate);
        card.add(txtDate);
        card.add(Box.createVerticalStrut(10));

        // Status
        card.add(formLabel("Status"));
        cmbStatus = new JComboBox<>(new String[]{"PRESENT","ABSENT","LATE"});
        cmbStatus.setFont(UITheme.FONT_BODY);
        cmbStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cmbStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cmbStatus);
        card.add(Box.createVerticalStrut(10));

        // Remarks
        card.add(formLabel("Remarks (optional)"));
        txtRemarks = new JTextField();
        styleField(txtRemarks);
        card.add(txtRemarks);
        card.add(Box.createVerticalStrut(16));

        // Mark button
        JButton btnMark = UITheme.successButton("✓  Mark Attendance");
        btnMark.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnMark.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnMark.setPreferredSize(new Dimension(Integer.MAX_VALUE, 38));
        btnMark.addActionListener(e -> markAttendance());
        card.add(btnMark);
        card.add(Box.createVerticalStrut(10));

        lblMsg = new JLabel(" ");
        lblMsg.setFont(UITheme.FONT_SMALL);
        lblMsg.setForeground(UITheme.SUCCESS);
        lblMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblMsg);

        return card;
    }

    // ── RIGHT: ATTENDANCE LOG TABLE ───────────────────────────────────────
    private JPanel buildLogTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 12, 0, 0));

        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        hdr.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel h = new JLabel("Attendance Log");
        h.setFont(UITheme.FONT_HEADING);
        h.setForeground(UITheme.PRIMARY);
        JButton btnRefresh = UITheme.primaryButton("↻ Refresh");
        btnRefresh.addActionListener(e -> refresh());
        hdr.add(h, BorderLayout.WEST);
        hdr.add(btnRefresh, BorderLayout.EAST);
        panel.add(hdr, BorderLayout.NORTH);

        String[] cols = {"#","Reg No","Student Name","Subject","Date","Status","Remarks"};
        logModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        logTable = new JTable(logModel);
        logTable.setRowHeight(28);
        logTable.setFont(UITheme.FONT_BODY);
        logTable.setGridColor(UITheme.BORDER_COLOR);
        logTable.setSelectionBackground(UITheme.PRIMARY);
        logTable.setSelectionForeground(Color.WHITE);
        logTable.getTableHeader().setFont(UITheme.FONT_HEADING);
        logTable.getTableHeader().setBackground(UITheme.PRIMARY);
        logTable.getTableHeader().setForeground(Color.WHITE);
        logTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 255));
                if (col == 5 && val != null && !sel) {
                    switch (val.toString()) {
                        case "PRESENT": setForeground(UITheme.SUCCESS); break;
                        case "ABSENT":  setForeground(UITheme.DANGER);  break;
                        case "LATE":    setForeground(UITheme.WARNING); break;
                    }
                } else if (!sel) setForeground(UITheme.TEXT_PRIMARY);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        int[] widths = {35,100,160,200,100,80,120};
        for (int i = 0; i < widths.length; i++)
            logTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(logTable);
        scroll.setBorder(new LineBorder(UITheme.BORDER_COLOR, 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void markAttendance() {
        if (cmbStudent.getItemCount() == 0) {
            lblMsg.setForeground(UITheme.DANGER);
            lblMsg.setText("⚠ No students loaded. Please refresh.");
            return;
        }
        String sel = cmbStudent.getSelectedItem().toString();
        int stuId  = Integer.parseInt(sel.split("\\|")[0].trim());

        Attendance a = new Attendance();
        a.setStudentId     (stuId);
        a.setSubject       (cmbSubject.getSelectedItem().toString());
        a.setAttendanceDate(txtDate.getText().trim());
        a.setStatus        (cmbStatus.getSelectedItem().toString());
        a.setRemarks       (txtRemarks.getText().trim());

        try {
            attDAO.markAttendance(a);
            lblMsg.setForeground(UITheme.SUCCESS);
            lblMsg.setText("✓  Attendance marked successfully!");
            txtRemarks.setText("");
            refresh();
        } catch (SQLException e) {
            lblMsg.setForeground(UITheme.DANGER);
            lblMsg.setText("✗  Error: " + e.getMessage().substring(0, Math.min(45, e.getMessage().length())));
        }
    }

    public void refresh() {
        // Load students into combo
        new Thread(() -> {
            try {
                List<Student> students = stuDAO.getAllStudents();
                SwingUtilities.invokeLater(() -> {
                    cmbStudent.removeAllItems();
                    for (Student s : students) {
                        cmbStudent.addItem(s.getId() + " | " + s.getRegNo() + " — " + s.getName());
                    }
                });
            } catch (SQLException ignored) {}
        }).start();

        // Load attendance log
        new Thread(() -> {
            try {
                List<Attendance> log = attDAO.getAllAttendance();
                SwingUtilities.invokeLater(() -> {
                    logModel.setRowCount(0);
                    int r = 1;
                    for (Attendance a : log) {
                        logModel.addRow(new Object[]{
                            r++,
                            a.getRegNo(),
                            a.getStudentName(),
                            shortenSubject(a.getSubject()),
                            a.getAttendanceDate(),
                            a.getStatus(),
                            a.getRemarks()
                        });
                    }
                });
            } catch (SQLException ignored) {}
        }).start();
    }

    private String shortenSubject(String s) {
        if (s == null) return "";
        return s.length() > 30 ? s.substring(0, 30) + "…" : s;
    }

    private JLabel formLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void styleField(JTextField tf) {
        tf.setFont(UITheme.FONT_BODY);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        tf.setBorder(new CompoundBorder(
            new LineBorder(UITheme.BORDER_COLOR, 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
    }
}
