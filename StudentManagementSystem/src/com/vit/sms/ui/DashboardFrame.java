package com.vit.sms.ui;

import com.vit.sms.dao.AttendanceDAO;
import com.vit.sms.dao.StudentDAO;
import com.vit.sms.model.User;
import com.vit.sms.util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * DashboardFrame - Main Window with Sidebar Navigation (Unit 4)
 * Demonstrates: JFrame, CardLayout, Swing MVC, Layout Managers,
 *               Menus, Toolbars, Dialog Boxes, Component Organizers
 */
public class DashboardFrame extends JFrame {

    private final User       currentUser;
    private final CardLayout cardLayout   = new CardLayout();
    private final JPanel     contentPanel = new JPanel(cardLayout);

    private JButton btnActiveMenu = null;

    // Panels
    private HomePanel       homePanel;
    private StudentPanel    studentPanel;
    private AttendancePanel attendancePanel;
    private ReportsPanel    reportsPanel;

    public DashboardFrame(User user) {
        this.currentUser = user;
        initComponents();
        loadHomePanel();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("VIT Bhopal — Student Management System  |  " + currentUser.getFullName());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_MAIN);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { confirmExit(); }
        });

        // ── Menubar ──────────────────────────────────────────────────────
        setJMenuBar(buildMenuBar());

        // ── Main Layout ───────────────────────────────────────────────────
        add(buildSidebar(), BorderLayout.WEST);
        add(buildTopBar(), BorderLayout.NORTH);
        add(contentPanel,  BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        // ── Register Panels ───────────────────────────────────────────────
        homePanel       = new HomePanel(currentUser);
        studentPanel    = new StudentPanel();
        attendancePanel = new AttendancePanel();
        reportsPanel    = new ReportsPanel();

        contentPanel.add(homePanel,       "HOME");
        contentPanel.add(studentPanel,    "STUDENTS");
        contentPanel.add(attendancePanel, "ATTENDANCE");
        contentPanel.add(reportsPanel,    "REPORTS");
    }

    // ── SIDEBAR ───────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UITheme.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Logo area
        JPanel logoArea = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoArea.setOpaque(false);
        logoArea.setBorder(new EmptyBorder(18, 10, 18, 10));
        JLabel logoLbl = new JLabel("VIT SMS");
        logoLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLbl.setForeground(Color.WHITE);
        JLabel subLbl = new JLabel("Bhopal");
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLbl.setForeground(new Color(170, 190, 255));
        JPanel logoBrand = new JPanel();
        logoBrand.setOpaque(false);
        logoBrand.setLayout(new BoxLayout(logoBrand, BoxLayout.Y_AXIS));
        logoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        subLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoBrand.add(logoLbl);
        logoBrand.add(subLbl);
        logoArea.add(logoBrand);
        sidebar.add(logoArea);

        // Divider
        sidebar.add(sidebarDivider());

        // Nav Items
        btnActiveMenu = addNavItem(sidebar, "🏠  Dashboard",   "HOME",       true);
                        addNavItem(sidebar, "👥  Students",     "STUDENTS",   false);
                        addNavItem(sidebar, "📋  Attendance",   "ATTENDANCE", false);
                        addNavItem(sidebar, "📊  Reports",      "REPORTS",    false);

        sidebar.add(sidebarDivider());
        sidebar.add(Box.createVerticalGlue());

        // User info at bottom
        JPanel userArea = new JPanel();
        userArea.setOpaque(false);
        userArea.setLayout(new BoxLayout(userArea, BoxLayout.Y_AXIS));
        userArea.setBorder(new EmptyBorder(10, 15, 20, 15));

        JLabel nameL = new JLabel(currentUser.getFullName());
        nameL.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameL.setForeground(Color.WHITE);
        nameL.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleL = new JLabel(currentUser.getRole());
        roleL.setFont(UITheme.FONT_SMALL);
        roleL.setForeground(new Color(150, 180, 255));
        roleL.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnLogout = new JButton("⏻  Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLogout.setForeground(new Color(255, 120, 120));
        btnLogout.setBackground(new Color(30, 20, 60));
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(190, 30));
        btnLogout.addActionListener(e -> confirmLogout());

        userArea.add(nameL);
        userArea.add(Box.createVerticalStrut(2));
        userArea.add(roleL);
        userArea.add(Box.createVerticalStrut(10));
        userArea.add(btnLogout);
        sidebar.add(userArea);

        return sidebar;
    }

    private JButton addNavItem(JPanel sidebar, String label, String card, boolean active) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(active ? Color.WHITE : new Color(180, 200, 255));
        btn.setBackground(active ? UITheme.BG_SIDEBAR_H : UITheme.BG_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(11, 22, 11, 10));
        btn.setMaximumSize(new Dimension(220, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            if (btnActiveMenu != null) {
                btnActiveMenu.setBackground(UITheme.BG_SIDEBAR);
                btnActiveMenu.setForeground(new Color(180, 200, 255));
            }
            btn.setBackground(UITheme.BG_SIDEBAR_H);
            btn.setForeground(Color.WHITE);
            btnActiveMenu = btn;
            cardLayout.show(contentPanel, card);
            if (card.equals("HOME"))       homePanel.refresh();
            if (card.equals("STUDENTS"))   studentPanel.refresh();
            if (card.equals("ATTENDANCE")) attendancePanel.refresh();
            if (card.equals("REPORTS"))    reportsPanel.refresh();
        });

        sidebar.add(btn);
        return btn;
    }

    private JSeparator sidebarDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(50, 70, 130));
        sep.setMaximumSize(new Dimension(220, 2));
        return sep;
    }

    // ── TOP BAR ───────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setPreferredSize(new Dimension(0, 52));
        bar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, UITheme.BORDER_COLOR),
            new EmptyBorder(0, 20, 0, 20)
        ));

        JLabel welcome = new JLabel("Welcome, " + currentUser.getFullName() + "!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcome.setForeground(UITheme.PRIMARY);

        JLabel dateLbl = new JLabel(LocalDate.now().toString() + "  |  Slot: B14+D21");
        dateLbl.setFont(UITheme.FONT_SMALL);
        dateLbl.setForeground(UITheme.TEXT_SECONDARY);

        bar.add(welcome, BorderLayout.WEST);
        bar.add(dateLbl, BorderLayout.EAST);
        return bar;
    }

    // ── STATUS BAR ────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UITheme.BG_SIDEBAR);
        bar.setPreferredSize(new Dimension(0, 26));
        bar.setBorder(new EmptyBorder(0, 15, 0, 15));

        JLabel statusLbl = new JLabel("✓  Connected to Database  |  VIT Bhopal University  |  CSE4019 Advanced Java");
        statusLbl.setFont(UITheme.FONT_SMALL);
        statusLbl.setForeground(new Color(150, 180, 255));

        JLabel versionLbl = new JLabel("v1.0  |  Group 5");
        versionLbl.setFont(UITheme.FONT_SMALL);
        versionLbl.setForeground(new Color(150, 180, 255));

        bar.add(statusLbl, BorderLayout.WEST);
        bar.add(versionLbl, BorderLayout.EAST);
        return bar;
    }

    // ── MENU BAR ─────────────────────────────────────────────────────────
    private JMenuBar buildMenuBar() {
        JMenuBar mb = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem miLogout = new JMenuItem("Logout");
        JMenuItem miExit   = new JMenuItem("Exit");
        miLogout.addActionListener(e -> confirmLogout());
        miExit.addActionListener(e -> confirmExit());
        fileMenu.add(miLogout);
        fileMenu.addSeparator();
        fileMenu.add(miExit);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem miAbout = new JMenuItem("About");
        miAbout.addActionListener(e -> showAbout());
        helpMenu.add(miAbout);

        mb.add(fileMenu);
        mb.add(helpMenu);
        return mb;
    }

    private void loadHomePanel() {
        cardLayout.show(contentPanel, "HOME");
    }

    private void confirmLogout() {
        int r = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?", "Logout",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }

    private void confirmExit() {
        int r = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit the application?", "Exit",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "<html><b>Smart Student Record & Attendance Management System</b><br><br>" +
            "Course   : CSE4019 — Advanced Java Programming<br>" +
            "Faculty  : Vandana Shakya<br>" +
            "Slot     : B14+D21<br><br>" +
            "<b>Group Members:</b><br>" +
            "• Rishi Raj         (24BCE10149)<br>" +
            "• Abhilash Singh    (24BCE10706)<br>" +
            "• Md Afzal Khan     (24BCE11247)<br>" +
            "• Ayush Mishra      (24BCE10019)<br>" +
            "• Pranav Kumar      (24BCE10080)<br><br>" +
            "VIT Bhopal University | 2024-25</html>",
            "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
