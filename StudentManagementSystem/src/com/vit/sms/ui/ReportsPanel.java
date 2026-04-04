package com.vit.sms.ui;

import com.vit.sms.dao.AttendanceDAO;
import com.vit.sms.dao.StudentDAO;
import com.vit.sms.model.Student;
import com.vit.sms.util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * ReportsPanel - Reports & Analytics view (Unit 4)
 * Demonstrates: JTabbedPane, Custom painting, JTable, Progress Bars,
 *               Swing advanced components
 */
public class ReportsPanel extends JPanel {

    private final StudentDAO    stuDAO = new StudentDAO();
    private final AttendanceDAO attDAO = new AttendanceDAO();

    private DefaultTableModel cgpaModel;
    private DefaultTableModel branchModel;
    private JPanel            barChartPanel;

    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_MAIN);
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        JLabel title = new JLabel("Reports & Analytics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UITheme.PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UITheme.FONT_HEADING);
        tabs.setBackground(Color.WHITE);
        tabs.addTab("📊  CGPA Analysis",    buildCgpaTab());
        tabs.addTab("🏛️  Branch Summary",   buildBranchTab());
        tabs.addTab("📈  Visual Charts",    buildChartsTab());
        add(tabs, BorderLayout.CENTER);
    }

    // ── CGPA TABLE ────────────────────────────────────────────────────────
    private JPanel buildCgpaTab() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(14, 14, 14, 14));
        p.setBackground(Color.WHITE);

        String[] cols = {"#","Reg No","Name","Branch","Sem","CGPA","Grade","Status"};
        cgpaModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tbl = buildStyledTable(cgpaModel);
        tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 255));
                    if (col == 6 && v != null) {
                        switch (v.toString()) {
                            case "O":  setForeground(new Color(0, 150, 0));  break;
                            case "A+": setForeground(UITheme.SUCCESS);       break;
                            case "A":  setForeground(UITheme.INFO);          break;
                            case "B+": setForeground(UITheme.WARNING);       break;
                            default:   setForeground(UITheme.DANGER);        break;
                        }
                    } else {
                        setForeground(UITheme.TEXT_PRIMARY);
                    }
                }
                setBorder(new EmptyBorder(0,8,0,8));
                return this;
            }
        });

        JButton btnRefresh = UITheme.primaryButton("↻ Load");
        btnRefresh.addActionListener(e -> loadCgpaData());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        top.add(btnRefresh);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(tbl), BorderLayout.CENTER);
        return p;
    }

    private void loadCgpaData() {
        new Thread(() -> {
            try {
                List<Student> list = stuDAO.getAllStudents();
                SwingUtilities.invokeLater(() -> {
                    cgpaModel.setRowCount(0);
                    int r = 1;
                    for (Student s : list) {
                        cgpaModel.addRow(new Object[]{
                            r++,
                            s.getRegNo(),
                            s.getName(),
                            s.getBranch(),
                            "Sem " + s.getSemester(),
                            String.format("%.2f", s.getCgpa()),
                            cgpaGrade(s.getCgpa()),
                            s.getStatus()
                        });
                    }
                });
            } catch (SQLException ignored) {}
        }).start();
    }

    private String cgpaGrade(double c) {
        if (c >= 9.0) return "O";
        if (c >= 8.5) return "A+";
        if (c >= 8.0) return "A";
        if (c >= 7.0) return "B+";
        if (c >= 6.0) return "B";
        return "C";
    }

    // ── BRANCH SUMMARY ────────────────────────────────────────────────────
    private JPanel buildBranchTab() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(14, 14, 14, 14));
        p.setBackground(Color.WHITE);

        String[] cols = {"Branch", "Student Count", "Percentage"};
        branchModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tbl = buildStyledTable(branchModel);

        JButton btnRefresh = UITheme.primaryButton("↻ Load");
        btnRefresh.addActionListener(e -> loadBranchData());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        top.add(btnRefresh);
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(tbl), BorderLayout.CENTER);
        return p;
    }

    private void loadBranchData() {
        new Thread(() -> {
            try {
                List<Object[]> rows = stuDAO.getCountByBranch();
                int total = rows.stream().mapToInt(r -> (int)r[1]).sum();
                SwingUtilities.invokeLater(() -> {
                    branchModel.setRowCount(0);
                    for (Object[] row : rows) {
                        double pct = total > 0 ? ((int)row[1] * 100.0 / total) : 0;
                        branchModel.addRow(new Object[]{
                            row[0], row[1], String.format("%.1f%%", pct)
                        });
                    }
                });
            } catch (SQLException ignored) {}
        }).start();
    }

    // ── VISUAL CHARTS (custom painting) ───────────────────────────────────
    private JPanel buildChartsTab() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(14, 14, 14, 14));
        p.setBackground(Color.WHITE);

        JButton btnLoad = UITheme.primaryButton("↻ Load Chart");
        btnLoad.addActionListener(e -> loadChartData());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        top.add(btnLoad);
        p.add(top, BorderLayout.NORTH);

        barChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Chart is populated via loadChartData()
            }
        };
        barChartPanel.setBackground(Color.WHITE);
        barChartPanel.setLayout(new BoxLayout(barChartPanel, BoxLayout.Y_AXIS));
        barChartPanel.setBorder(new CompoundBorder(
            new LineBorder(UITheme.BORDER_COLOR, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel hint = new JLabel("Click 'Load Chart' to view CGPA distribution by grade");
        hint.setFont(UITheme.FONT_BODY);
        hint.setForeground(UITheme.TEXT_SECONDARY);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        barChartPanel.add(Box.createVerticalGlue());
        barChartPanel.add(hint);
        barChartPanel.add(Box.createVerticalGlue());

        p.add(barChartPanel, BorderLayout.CENTER);
        return p;
    }

    private void loadChartData() {
        new Thread(() -> {
            try {
                List<Student> students = stuDAO.getAllStudents();
                int[] grades = new int[6]; // O, A+, A, B+, B, C
                String[] gradeNames = {"O (9.0+)", "A+ (8.5+)", "A (8.0+)", "B+ (7.0+)", "B (6.0+)", "C (<6.0)"};
                Color[]  gradeColors = {
                    new Color(0, 150, 0),   // O - dark green
                    UITheme.SUCCESS,         // A+
                    UITheme.INFO,            // A
                    UITheme.WARNING,         // B+
                    new Color(180, 100, 0),  // B
                    UITheme.DANGER           // C
                };

                for (Student s : students) {
                    double c = s.getCgpa();
                    if (c >= 9.0) grades[0]++;
                    else if (c >= 8.5) grades[1]++;
                    else if (c >= 8.0) grades[2]++;
                    else if (c >= 7.0) grades[3]++;
                    else if (c >= 6.0) grades[4]++;
                    else grades[5]++;
                }

                int max = 0;
                for (int g : grades) if (g > max) max = g;
                final int finalMax = max == 0 ? 1 : max;

                SwingUtilities.invokeLater(() -> {
                    barChartPanel.removeAll();
                    barChartPanel.setLayout(new BoxLayout(barChartPanel, BoxLayout.Y_AXIS));

                    JLabel chartTitle = new JLabel("CGPA Grade Distribution");
                    chartTitle.setFont(UITheme.FONT_HEADING);
                    chartTitle.setForeground(UITheme.PRIMARY);
                    chartTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                    barChartPanel.add(chartTitle);
                    barChartPanel.add(Box.createVerticalStrut(16));

                    for (int i = 0; i < gradeNames.length; i++) {
                        JPanel row = new JPanel(new BorderLayout(10, 0));
                        row.setOpaque(false);
                        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
                        row.setBorder(new EmptyBorder(2,0,2,0));

                        JLabel nameLbl = new JLabel(gradeNames[i]);
                        nameLbl.setFont(UITheme.FONT_BODY);
                        nameLbl.setPreferredSize(new Dimension(100, 28));

                        JProgressBar bar = new JProgressBar(0, finalMax);
                        bar.setValue(grades[i]);
                        bar.setStringPainted(true);
                        bar.setString(grades[i] + " students");
                        bar.setForeground(gradeColors[i]);
                        bar.setBackground(new Color(235, 240, 250));
                        bar.setFont(UITheme.FONT_SMALL);
                        bar.setPreferredSize(new Dimension(0, 28));

                        row.add(nameLbl, BorderLayout.WEST);
                        row.add(bar,     BorderLayout.CENTER);
                        barChartPanel.add(row);
                        barChartPanel.add(Box.createVerticalStrut(6));
                    }
                    barChartPanel.revalidate();
                    barChartPanel.repaint();
                });
            } catch (SQLException ignored) {}
        }).start();
    }

    private JTable buildStyledTable(DefaultTableModel model) {
        JTable tbl = new JTable(model);
        tbl.setRowHeight(28);
        tbl.setFont(UITheme.FONT_BODY);
        tbl.setGridColor(UITheme.BORDER_COLOR);
        tbl.setSelectionBackground(UITheme.PRIMARY);
        tbl.setSelectionForeground(Color.WHITE);
        tbl.getTableHeader().setFont(UITheme.FONT_HEADING);
        tbl.getTableHeader().setBackground(UITheme.PRIMARY);
        tbl.getTableHeader().setForeground(Color.WHITE);
        tbl.getTableHeader().setReorderingAllowed(false);
        return tbl;
    }

    public void refresh() {
        loadCgpaData();
        loadBranchData();
    }
}
