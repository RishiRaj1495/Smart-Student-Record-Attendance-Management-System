package com.vit.sms.ui;

import com.vit.sms.dao.UserDAO;
import com.vit.sms.model.User;
import com.vit.sms.util.DatabaseConnection;
import com.vit.sms.util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

/**
 * LoginFrame - Swing Login UI (Unit 4)
 * Demonstrates: JFrame, JPanel, JTextField, JPasswordField,
 *               Event Handling, Layout Managers, Swing components
 */
public class LoginFrame extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JButton        btnLogin;
    private JLabel         lblStatus;
    private final UserDAO  userDAO = new UserDAO();

    public LoginFrame() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("VIT Bhopal — Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(860, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // ── LEFT PANEL (branding) ──────────────────────────────────────────
        JPanel left = new JPanel(new GridBagLayout());
        left.setBackground(UITheme.BG_SIDEBAR);
        left.setPreferredSize(new Dimension(380, 520));

        JPanel brandBox = new JPanel();
        brandBox.setOpaque(false);
        brandBox.setLayout(new BoxLayout(brandBox, BoxLayout.Y_AXIS));
        brandBox.setBorder(new EmptyBorder(10, 30, 10, 30));

        // VIT circle logo placeholder
        JLabel logo = new JLabel("VIT", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 52));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(new CompoundBorder(
            new LineBorder(UITheme.ACCENT, 3, true),
            new EmptyBorder(10, 20, 10, 20)
        ));

        JLabel lbl1 = new JLabel("VIT Bhopal University", SwingConstants.CENTER);
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lbl1.setForeground(Color.WHITE);
        lbl1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl2 = new JLabel("Student Management System", SwingConstants.CENTER);
        lbl2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl2.setForeground(new Color(180, 200, 255));
        lbl2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 90, 160));
        sep.setMaximumSize(new Dimension(260, 2));

        JLabel lbl3 = new JLabel("CSE4019 — Advanced Java", SwingConstants.CENTER);
        lbl3.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lbl3.setForeground(new Color(150, 180, 255));
        lbl3.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl4 = new JLabel("Faculty: Vandana Shakya", SwingConstants.CENTER);
        lbl4.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl4.setForeground(new Color(150, 180, 255));
        lbl4.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Default credentials hint
        JTextArea hint = new JTextArea(
            "Default Credentials:\n" +
            "Admin    → admin / admin123\n" +
            "Faculty  → vandana / faculty123"
        );
        hint.setEditable(false);
        hint.setFont(new Font("Monospaced", Font.PLAIN, 11));
        hint.setForeground(new Color(170, 200, 255));
        hint.setBackground(new Color(30, 50, 100));
        hint.setBorder(new CompoundBorder(
            new LineBorder(new Color(60, 90, 160), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        hint.setMaximumSize(new Dimension(280, 80));

        brandBox.add(Box.createVerticalStrut(10));
        brandBox.add(logo);
        brandBox.add(Box.createVerticalStrut(16));
        brandBox.add(lbl1);
        brandBox.add(Box.createVerticalStrut(6));
        brandBox.add(lbl2);
        brandBox.add(Box.createVerticalStrut(20));
        brandBox.add(sep);
        brandBox.add(Box.createVerticalStrut(12));
        brandBox.add(lbl3);
        brandBox.add(Box.createVerticalStrut(4));
        brandBox.add(lbl4);
        brandBox.add(Box.createVerticalStrut(24));
        brandBox.add(hint);

        left.add(brandBox);

        // ── RIGHT PANEL (login form) ──────────────────────────────────────
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Color.WHITE);
        right.setBorder(new EmptyBorder(0, 40, 0, 40));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(320, 400));

        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(UITheme.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Sign in to your account");
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(title);
        form.add(Box.createVerticalStrut(6));
        form.add(sub);
        form.add(Box.createVerticalStrut(28));

        // Username
        form.add(fieldLabel("Username"));
        form.add(Box.createVerticalStrut(4));
        txtUsername = styledField(20);
        txtUsername.setText("admin");
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(txtUsername);
        form.add(Box.createVerticalStrut(16));

        // Password
        form.add(fieldLabel("Password"));
        form.add(Box.createVerticalStrut(4));
        txtPassword = new JPasswordField(20);
        txtPassword.setText("admin123");
        stylePasswordField(txtPassword);
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(txtPassword);
        form.add(Box.createVerticalStrut(24));

        // Login button
        btnLogin = new JButton("Sign In →");
        btnLogin.setBackground(UITheme.PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(280, 40));
        btnLogin.setMaximumSize(new Dimension(280, 40));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(btnLogin);
        form.add(Box.createVerticalStrut(12));

        // Status label
        lblStatus = new JLabel(" ");
        lblStatus.setFont(UITheme.FONT_SMALL);
        lblStatus.setForeground(UITheme.DANGER);
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblStatus);

        right.add(form);

        // ── Actions ───────────────────────────────────────────────────────
        btnLogin.addActionListener(e -> performLogin());
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) performLogin();
            }
        });

        add(left,  BorderLayout.WEST);
        add(right, BorderLayout.CENTER);
    }

    private void performLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            lblStatus.setText("⚠  Please enter username and password.");
            return;
        }

        btnLogin.setEnabled(false);
        lblStatus.setForeground(UITheme.INFO);
        lblStatus.setText("Authenticating...");

        // Background thread for DB call (Unit 1 - Threading)
        new Thread(() -> {
            try {
                UserDAO dao  = new UserDAO();
                User    loggedIn = dao.authenticate(user, pass);
                SwingUtilities.invokeLater(() -> {
                    if (loggedIn != null) {
                        lblStatus.setForeground(UITheme.SUCCESS);
                        lblStatus.setText("✓  Login successful! Loading...");
                        Timer t = new Timer(600, ev -> {
                            dispose();
                            new DashboardFrame(loggedIn);
                        });
                        t.setRepeats(false);
                        t.start();
                    } else {
                        lblStatus.setForeground(UITheme.DANGER);
                        lblStatus.setText("✗  Invalid username or password.");
                        btnLogin.setEnabled(true);
                    }
                });
            } catch (SQLException ex) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setForeground(UITheme.DANGER);
                    lblStatus.setText("✗  DB Error: " + ex.getMessage().substring(0, Math.min(50, ex.getMessage().length())));
                    btnLogin.setEnabled(true);
                });
            }
        }).start();
    }

    private JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField styledField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(UITheme.FONT_BODY);
        tf.setBorder(new CompoundBorder(
            new LineBorder(UITheme.BORDER_COLOR, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        tf.setPreferredSize(new Dimension(280, 38));
        tf.setMaximumSize(new Dimension(280, 38));
        return tf;
    }

    private void stylePasswordField(JPasswordField pf) {
        pf.setFont(UITheme.FONT_BODY);
        pf.setBorder(new CompoundBorder(
            new LineBorder(UITheme.BORDER_COLOR, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        pf.setPreferredSize(new Dimension(280, 38));
        pf.setMaximumSize(new Dimension(280, 38));
    }
}
