package com.vit.sms.ui;

import com.vit.sms.dao.StudentDAO;
import com.vit.sms.model.Student;
import com.vit.sms.util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

/**
 * StudentPanel - Student CRUD operations (Unit 3 + 4)
 * Demonstrates: JTable, TableModel, Swing dialogs, Event handling,
 *               JDBC CRUD, Search, BorderLayout, GridBagLayout
 */
public class StudentPanel extends JPanel {

    private final StudentDAO     dao         = new StudentDAO();
    private       DefaultTableModel tableModel;
    private       JTable           table;
    private       JTextField       txtSearch;
    private       JLabel           lblCount;

    private static final String[] COLUMNS = {
        "#", "Reg No", "Name", "Branch", "Semester", "CGPA", "Gender", "Status"
    };

    public StudentPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_MAIN);
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        // ── Header ────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel title = new JLabel("Student Records");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UITheme.PRIMARY);

        lblCount = new JLabel("Loading...");
        lblCount.setFont(UITheme.FONT_SMALL);
        lblCount.setForeground(UITheme.TEXT_SECONDARY);

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.add(title);
        titleBox.add(lblCount);
        header.add(titleBox, BorderLayout.WEST);

        // Search + Action Buttons
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionBar.setOpaque(false);

        txtSearch = UITheme.styledField(18);
        txtSearch.setToolTipText("Search by name, reg no, or branch");
        JButton btnSearch = UITheme.primaryButton("🔍 Search");
        JButton btnClear  = UITheme.warningButton("✕ Clear");
        JButton btnAdd    = UITheme.successButton("+ Add");
        JButton btnEdit   = UITheme.primaryButton("✎ Edit");
        JButton btnDelete = UITheme.dangerButton("✕ Delete");
        JButton btnRefresh= UITheme.primaryButton("↻ Refresh");

        actionBar.add(txtSearch);
        actionBar.add(btnSearch);
        actionBar.add(btnClear);
        actionBar.add(new JSeparator(SwingConstants.VERTICAL));
        actionBar.add(btnAdd);
        actionBar.add(btnEdit);
        actionBar.add(btnDelete);
        actionBar.add(btnRefresh);
        header.add(actionBar, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────────────────────────
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(UITheme.FONT_BODY);
        table.setShowHorizontalLines(true);
        table.setGridColor(UITheme.BORDER_COLOR);
        table.setSelectionBackground(UITheme.PRIMARY);
        table.setSelectionForeground(Color.WHITE);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(UITheme.FONT_HEADING);
        table.getTableHeader().setBackground(UITheme.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        // Column widths
        int[] widths = {40, 110, 200, 120, 80, 80, 80, 80};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Alternating row colours renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(235, 240, 255));
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                // Colour-code status column
                if (col == 7 && val != null) {
                    setForeground(val.toString().equals("ACTIVE") ? UITheme.SUCCESS : UITheme.DANGER);
                } else {
                    if (!sel) setForeground(UITheme.TEXT_PRIMARY);
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(UITheme.BORDER_COLOR, 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // ── Action Listeners ──────────────────────────────────────────────
        btnSearch.addActionListener(e -> performSearch());
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) performSearch();
            }
        });
        btnClear.addActionListener(e -> { txtSearch.setText(""); refresh(); });
        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> refresh());

        // Double-click to edit
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) showEditDialog();
            }
        });
    }

    // ── LOAD DATA ─────────────────────────────────────────────────────────
    public void refresh() {
        new Thread(() -> {
            try {
                List<Student> list = dao.getAllStudents();
                SwingUtilities.invokeLater(() -> populateTable(list));
            } catch (SQLException e) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(this, "Error loading students:\n" + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void populateTable(List<Student> list) {
        tableModel.setRowCount(0);
        int row = 1;
        for (Student s : list) {
            tableModel.addRow(new Object[]{
                row++,
                s.getRegNo(),
                s.getName(),
                s.getBranch(),
                "Sem " + s.getSemester(),
                String.format("%.2f", s.getCgpa()),
                s.getGender(),
                s.getStatus()
            });
        }
        lblCount.setText("Total: " + list.size() + " student(s)");
    }

    private void performSearch() {
        String kw = txtSearch.getText().trim();
        if (kw.isEmpty()) { refresh(); return; }
        new Thread(() -> {
            try {
                List<Student> res = dao.searchStudents(kw);
                SwingUtilities.invokeLater(() -> populateTable(res));
            } catch (SQLException e) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    // ── ADD DIALOG ────────────────────────────────────────────────────────
    private void showAddDialog() {
        StudentFormDialog dlg = new StudentFormDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), null);
        dlg.setVisible(true);
        if (dlg.isSaved()) refresh();
    }

    // ── EDIT DIALOG ───────────────────────────────────────────────────────
    private void showEditDialog() {
        int sel = table.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Please select a student to edit."); return; }
        String regNo = tableModel.getValueAt(sel, 1).toString();
        try {
            List<Student> all = dao.getAllStudents();
            Student s = all.stream().filter(x -> x.getRegNo().equals(regNo)).findFirst().orElse(null);
            if (s == null) return;
            StudentFormDialog dlg = new StudentFormDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), s);
            dlg.setVisible(true);
            if (dlg.isSaved()) refresh();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────
    private void deleteSelected() {
        int sel = table.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Please select a student to delete."); return; }
        String name  = tableModel.getValueAt(sel, 2).toString();
        String regNo = tableModel.getValueAt(sel, 1).toString();
        int opt = JOptionPane.showConfirmDialog(this,
            "Delete student: " + name + " (" + regNo + ")?\nThis cannot be undone.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opt != JOptionPane.YES_OPTION) return;
        try {
            List<Student> all = dao.getAllStudents();
            Student s = all.stream().filter(x -> x.getRegNo().equals(regNo)).findFirst().orElse(null);
            if (s != null) {
                dao.deleteStudent(s.getId());
                JOptionPane.showMessageDialog(this, "Student deleted successfully.");
                refresh();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Delete error: " + e.getMessage());
        }
    }
}
