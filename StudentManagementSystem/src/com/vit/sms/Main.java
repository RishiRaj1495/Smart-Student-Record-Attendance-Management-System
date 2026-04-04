package com.vit.sms;

import com.vit.sms.dao.StudentDAO;
import com.vit.sms.model.Student;
import com.vit.sms.ui.LoginFrame;
import com.vit.sms.util.DatabaseConnection;
import com.vit.sms.util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ╔══════════════════════════════════════════════════════════════════════════╗
 * ║   Smart Student Record & Attendance Management System                   ║
 * ║   VIT Bhopal University  |  CSE4019 — Advanced Java Programming         ║
 * ║                                                                          ║
 * ║   Group Members:                                                         ║
 * ║     • Rishi Raj         (24BCE10149)   — Team Lead                      ║
 * ║     • Abhilash Singh    (24BCE10706)   — Backend                        ║
 * ║     • Md Afzal Khan     (24BCE11247)   — Database                       ║
 * ║     • Ayush Mishra      (24BCE10019)   — Frontend                       ║
 * ║     • Pranav Kumar      (24BCE10080)   — Testing                        ║
 * ║                                                                          ║
 * ║   Faculty : Vandana Shakya  |  Slot : B14+D21                           ║
 * ╚══════════════════════════════════════════════════════════════════════════╝
 *
 * Main class — Application entry point
 * Demonstrates: Multi-threading (Unit 1), Swing Look & Feel (Unit 4)
 */
public class Main {

    public static void main(String[] args) {
        // Set Look & Feel on EDT
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        UITheme.applyGlobalDefaults();

        // Show splash on EDT while initialising DB in background
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.setVisible(true);

            // Background thread for DB init (Unit 1 — Threading)
            new Thread(() -> {
                try {
                    splash.setStatus("Connecting to database...");
                    Thread.sleep(600);

                    DatabaseConnection.initializeDatabase();
                    splash.setStatus("Database ready. Seeding sample data...");
                    Thread.sleep(400);

                    seedSampleData();
                    splash.setStatus("Loading application...");
                    Thread.sleep(500);

                } catch (Exception e) {
                    SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(null,
                            "Database connection failed!\n\n" +
                            "Please ensure MySQL is running and credentials in\n" +
                            "DatabaseConnection.java are correct.\n\n" +
                            "Error: " + e.getMessage(),
                            "Connection Error", JOptionPane.ERROR_MESSAGE)
                    );
                }

                SwingUtilities.invokeLater(() -> {
                    splash.dispose();
                    new LoginFrame();
                });
            }).start();
        });
    }

    // ── Seed classmate data ───────────────────────────────────────────────
    private static void seedSampleData() {
        try {
            StudentDAO dao = new StudentDAO();
            if (dao.getTotalCount() > 0) return; // Already seeded

            List<Student> students = new ArrayList<>();
            // 23BCE batch
            students.add(make("23BCE10014","Muskan Srivastav",   "CSE", 4, 8.7,  "Female"));
            students.add(make("23BCE10108","Shikhar Sharma",     "CSE", 4, 8.2,  "Male"));
            students.add(make("23BCE10189","Naman Gupta",        "CSE", 4, 7.9,  "Male"));
            students.add(make("23BCE10194","Shorya Pathak",      "CSE", 4, 8.5,  "Male"));
            students.add(make("23BCE10259","Dhruv",              "CSE", 4, 7.6,  "Male"));
            students.add(make("23BCE10271","Pragati Tripathi",   "CSE", 4, 9.1,  "Female"));
            students.add(make("23BCE10306","Khushbu",            "CSE", 4, 8.8,  "Female"));
            students.add(make("23BCE10339","Krishna",            "CSE", 4, 8.0,  "Male"));
            students.add(make("23BCE10343","Himanshu Rathore",   "CSE", 4, 7.4,  "Male"));
            students.add(make("23BCE10355","Kumar Arya",         "CSE", 4, 8.3,  "Male"));
            students.add(make("23BCE10385","Ritik Mangawa",      "CSE", 4, 7.8,  "Male"));
            students.add(make("23BCE10401","Kuanr Ajay Pratap",  "CSE", 4, 7.5,  "Male"));
            students.add(make("23BCE10436","Shriya Dhawan",      "CSE", 4, 9.0,  "Female"));
            students.add(make("23BCE10447","Amit Yadav",         "CSE", 4, 8.1,  "Male"));
            students.add(make("23BCE10546","Divyanshu Mittal",   "CSE", 4, 8.6,  "Male"));
            students.add(make("23BCE10596","Advay Singh",        "CSE", 4, 7.7,  "Male"));
            students.add(make("23BCE10626","Radha Agarwal",      "CSE", 4, 9.2,  "Female"));
            students.add(make("23BCE10628","Devesh Murjani",     "CSE", 4, 8.4,  "Male"));
            students.add(make("23BCE10700","Tanishk Vamne",      "CSE", 4, 7.3,  "Male"));
            students.add(make("23BCE10717","Nitya Agarwal",      "CSE", 4, 8.9,  "Female"));
            students.add(make("23BCE10746","Yashvi Kohli",       "CSE", 4, 9.3,  "Female"));
            students.add(make("23BCE10752","Rushil Walia",       "CSE", 4, 7.1,  "Male"));
            students.add(make("23BCE10760","Shashank Pandey",    "CSE", 4, 8.0,  "Male"));
            students.add(make("23BCE10777","Ansh Tyagi",         "CSE", 4, 7.9,  "Male"));
            students.add(make("23BCE10815","Kabir Roy",          "CSE", 4, 8.5,  "Male"));
            students.add(make("23BCE10831","Snehal Chakrabarti", "CSE", 4, 9.0,  "Female"));
            students.add(make("23BCE10838","Shreeved Kiran Dhanait","CSE",4,8.2, "Male"));
            students.add(make("23BCE10849","Yashvardhan Singh Sarangdevot","CSE",4,7.8,"Male"));
            students.add(make("23BCE10861","Mannat Sachdeva",    "CSE", 4, 9.1,  "Female"));
            students.add(make("23BCE10874","Yash Rawalkar",      "CSE", 4, 8.3,  "Male"));
            students.add(make("23BCE10889","Nikhil Tiwari",      "CSE", 4, 7.6,  "Male"));
            students.add(make("23BCE10899","Chinmay Bhardwaj",   "CSE", 4, 8.7,  "Male"));
            students.add(make("23BCE10928","Sainyam Bansal",     "CSE", 4, 8.9,  "Male"));
            students.add(make("23BCE10937","Revanta Choudhary",  "CSE", 4, 7.5,  "Male"));
            students.add(make("23BCE10983","Taufiq Ahamed",      "CSE", 4, 8.1,  "Male"));
            students.add(make("23BCE11125","Anuj Tiwari",        "CSE", 4, 7.4,  "Male"));
            students.add(make("23BCE11130","Jahnvi Verma",       "CSE", 4, 8.6,  "Female"));
            students.add(make("23BCE11169","Devraj Bijpuria",    "CSE", 4, 7.2,  "Male"));
            students.add(make("23BCE11176","Swastik Das",        "CSE", 4, 8.0,  "Male"));
            students.add(make("23BCE11215","Pingaksh Sharma",    "CSE", 4, 7.8,  "Male"));
            students.add(make("23BCE11226","Samyukta Sandeep Menon","CSE",4,9.2,"Female"));
            students.add(make("23BCE11247","Alok Kumar Gupta",   "CSE", 4, 8.4,  "Male"));
            students.add(make("23BCE11304","Ayush Kumar",        "CSE", 4, 7.7,  "Male"));
            students.add(make("23BCE11313","Harshika Sharma",    "CSE", 4, 9.0,  "Female"));
            students.add(make("23BCE11315","Yashwardhan Sandilya","CSE",4,8.3,  "Male"));
            students.add(make("23BCE11356","Yash Kumar Singh",   "CSE", 4, 7.9,  "Male"));
            students.add(make("23BCE11374","Utkarsh Arya",       "CSE", 4, 8.5,  "Male"));
            students.add(make("23BCE11377","Abhinav Verma",      "CSE", 4, 7.6,  "Male"));
            students.add(make("23BCE11384","Pratyush Srivastava","CSE", 4, 8.8,  "Male"));
            students.add(make("23BCE11387","Aryan Gupta",        "CSE", 4, 7.3,  "Male"));
            students.add(make("23BCE11419","Parv Khandelwal",    "CSE", 4, 8.1,  "Male"));
            students.add(make("23BCE11449","Gargee Singh",       "CSE", 4, 9.1,  "Female"));
            students.add(make("23BCE11558","Yash Aman",          "CSE", 4, 7.7,  "Male"));
            students.add(make("23BCE11586","Shriyam Rastogi",    "CSE", 4, 8.0,  "Male"));
            students.add(make("23BCE11596","Ayush Kumar",        "CSE", 4, 8.4,  "Male"));
            students.add(make("23BCE11614","Yash Saini",         "CSE", 4, 7.5,  "Male"));
            students.add(make("23BCE11631","Sarthak Vyas",       "CSE", 4, 8.2,  "Male"));
            students.add(make("23BCE11634","Suryanshi Ranawat",  "CSE", 4, 9.0,  "Female"));
            students.add(make("23BCE11635","Manvi Nayak",        "CSE", 4, 8.7,  "Female"));
            students.add(make("23BCE11675","Harsh Raj Singh",    "CSE", 4, 7.8,  "Male"));
            students.add(make("23BCE11691","Anand Singh",        "CSE", 4, 8.3,  "Male"));
            students.add(make("23BCE11700","Vaibhav Verma",      "CSE", 4, 7.6,  "Male"));
            students.add(make("23BCE11720","Hiya Dosi",          "CSE", 4, 9.2,  "Female"));
            students.add(make("23BCE11728","Aaditya Pathak",     "CSE", 4, 8.5,  "Male"));
            students.add(make("23BCE11740","Pranhavee Tyagi",    "CSE", 4, 8.9,  "Female"));
            students.add(make("23BCE11752","Chirayu Zalke",      "CSE", 4, 7.4,  "Male"));
            students.add(make("23BCE11761","Suryansh Singh",     "CSE", 4, 8.1,  "Male"));
            students.add(make("23BCE11783","Prince Saini",       "CSE", 4, 7.9,  "Male"));
            students.add(make("23BCE11793","Kumar Vaibhav",      "CSE", 4, 8.6,  "Male"));
            students.add(make("23BCE11825","Ansh Pandey",        "CSE", 4, 7.7,  "Male"));
            students.add(make("23BCE11828","Parvathi M",         "CSE", 4, 9.0,  "Female"));
            students.add(make("23BCG10082","Mohd Bashar",        "CSE", 4, 8.2,  "Male"));

            // 24BCE batch (includes project group)
            students.add(make("24BCE10019","Ayush Mishra",       "CSE", 2, 8.4,  "Male"));
            students.add(make("24BCE10058","Anuj Parashar",      "CSE", 2, 7.8,  "Male"));
            students.add(make("24BCE10080","Pranav Kumar",       "CSE", 2, 8.6,  "Male"));
            students.add(make("24BCE10149","Rishi Raj",          "CSE", 2, 9.1,  "Male"));
            students.add(make("24BCE10176","Klemens Nile Danny", "CSE", 2, 7.5,  "Male"));
            students.add(make("24BCE10288","Shivam Sikka",       "CSE", 2, 8.0,  "Male"));
            students.add(make("24BCE10293","Hardik Pichholiya",  "CSE", 2, 8.3,  "Male"));
            students.add(make("24BCE10306","Arunima Mohan Rai",  "CSE", 2, 9.0,  "Female"));
            students.add(make("24BCE10348","Saurav Choudhary",   "CSE", 2, 7.6,  "Male"));
            students.add(make("24BCE10366","Yadvendra Raj Yadav","CSE", 2, 8.1,  "Male"));
            students.add(make("24BCE10401","Parth Sarthi",       "CSE", 2, 7.9,  "Male"));
            students.add(make("24BCE10459","Jashwin Sharma",     "CSE", 2, 8.7,  "Male"));
            students.add(make("24BCE10475","Vivek Lohar",        "CSE", 2, 7.4,  "Male"));
            students.add(make("24BCE10478","Kinshuk Sharma",     "CSE", 2, 8.5,  "Male"));
            students.add(make("24BCE10519","Deepak Sharma",      "CSE", 2, 7.7,  "Male"));
            students.add(make("24BCE10525","Priyanshi Gupta",    "CSE", 2, 9.2,  "Female"));
            students.add(make("24BCE10541","Shreyanshi Tiwari",  "CSE", 2, 8.8,  "Female"));
            students.add(make("24BCE10646","Krish",              "CSE", 2, 7.3,  "Male"));
            students.add(make("24BCE10677","Atharva Dixit",      "CSE", 2, 8.2,  "Male"));
            students.add(make("24BCE10685","Ayush Ranjan",       "CSE", 2, 8.0,  "Male"));
            students.add(make("24BCE10706","Abhilash Singh",     "CSE", 2, 8.9,  "Male"));
            students.add(make("24BCE10743","Sankalp Kabra",      "CSE", 2, 7.8,  "Male"));
            students.add(make("24BCE10765","Saksham Pandey",     "CSE", 2, 8.4,  "Male"));
            students.add(make("24BCE10801","Khengar Chaun",      "CSE", 2, 7.1,  "Male"));
            students.add(make("24BCE10803","Anirudh Sharma",     "CSE", 2, 8.3,  "Male"));
            students.add(make("24BCE10820","Suraj Kumar Chaudhary","CSE",2,7.6, "Male"));
            students.add(make("24BCE10867","Sakshi Vibhash Chandra","CSE",2,8.7,"Female"));
            students.add(make("24BCE10935","Shivam Kumar",       "CSE", 2, 7.9,  "Male"));
            students.add(make("24BCE10942","Krishanu Das",       "CSE", 2, 8.1,  "Male"));
            students.add(make("24BCE10979","Aayushmaan Kumar Pandey","CSE",2,7.5,"Male"));
            students.add(make("24BCE10993","Saurish Verma",      "CSE", 2, 8.6,  "Male"));
            students.add(make("24BCE11096","Aditya Chandra",     "CSE", 2, 7.8,  "Male"));
            students.add(make("24BCE11194","Kalpesh Parashar",   "CSE", 2, 8.0,  "Male"));
            students.add(make("24BCE11197","Divyansh Sharma",    "CSE", 2, 8.5,  "Male"));
            students.add(make("24BCE11234","Diya Mittal",        "CSE", 2, 9.3,  "Female"));
            students.add(make("24BCE11247","Md Afzal Khan",      "CSE", 2, 8.8,  "Male"));
            students.add(make("24BCE11260","Arav Sharma",        "CSE", 2, 7.7,  "Male"));
            students.add(make("24BCE11307","Shreshth Gupta",     "CSE", 2, 8.2,  "Male"));
            students.add(make("24BCE11360","Shishir Kant Upadhyay","CSE",2,7.4, "Male"));
            students.add(make("24BCE11384","Sarthak Solanki",    "CSE", 2, 8.9,  "Male"));
            students.add(make("24BCE11391","Athar Abbas Zaidi",  "CSE", 2, 7.6,  "Male"));
            students.add(make("24BCE11393","Aarshi Jaiswal",     "CSE", 2, 9.0,  "Female"));
            students.add(make("24BCE11418","Tejas Singh",        "CSE", 2, 8.3,  "Male"));
            students.add(make("24BCE11434","Sanskar Shrivastava","CSE", 2, 7.8,  "Male"));
            students.add(make("24BCE11444","Aditya Kumar Singh", "CSE", 2, 8.1,  "Male"));
            students.add(make("24BCE11460","Daksh Taneja",       "CSE", 2, 7.5,  "Male"));
            students.add(make("24BCE11505","Smridhi Narwal",     "CSE", 2, 8.7,  "Female"));
            students.add(make("24BCE11514","Daksh Maru",         "CSE", 2, 7.9,  "Male"));

            dao.batchInsert(students);
        } catch (Exception ignored) {
            // If seeding fails, app still works
        }
    }

    private static Student make(String reg, String name, String branch, int sem, double cgpa, String gender) {
        Student s = new Student();
        s.setRegNo   (reg);
        s.setName    (name);
        s.setEmail   (reg.toLowerCase() + "@vitbhopal.ac.in");
        s.setPhone   ("9" + (long)(Math.random() * 900000000L + 100000000L));
        s.setBranch  (branch);
        s.setSemester(sem);
        s.setCgpa    (cgpa);
        s.setGender  (gender);
        s.setStatus  ("ACTIVE");
        return s;
    }

    // ── Splash Screen ─────────────────────────────────────────────────────
    static class SplashScreen extends JWindow {
        private JLabel statusLabel;

        public SplashScreen() {
            setSize(480, 260);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(UITheme.BG_SIDEBAR);
            panel.setBorder(new LineBorder(UITheme.ACCENT, 2, true));

            JPanel center = new JPanel();
            center.setOpaque(false);
            center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
            center.setBorder(new EmptyBorder(30, 40, 20, 40));

            JLabel logo = new JLabel("VIT Bhopal University", SwingConstants.CENTER);
            logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
            logo.setForeground(Color.WHITE);
            logo.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel sub = new JLabel("Student Management System", SwingConstants.CENTER);
            sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            sub.setForeground(new Color(180, 200, 255));
            sub.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel course = new JLabel("CSE4019 — Advanced Java Programming", SwingConstants.CENTER);
            course.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            course.setForeground(new Color(140, 170, 255));
            course.setAlignmentX(Component.CENTER_ALIGNMENT);

            JProgressBar progress = new JProgressBar();
            progress.setIndeterminate(true);
            progress.setForeground(UITheme.ACCENT);
            progress.setBackground(new Color(40, 60, 120));
            progress.setMaximumSize(new Dimension(380, 8));
            progress.setBorderPainted(false);
            progress.setAlignmentX(Component.CENTER_ALIGNMENT);

            statusLabel = new JLabel("Initializing...", SwingConstants.CENTER);
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            statusLabel.setForeground(new Color(140, 170, 255));
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            center.add(logo);
            center.add(Box.createVerticalStrut(6));
            center.add(sub);
            center.add(Box.createVerticalStrut(4));
            center.add(course);
            center.add(Box.createVerticalStrut(24));
            center.add(progress);
            center.add(Box.createVerticalStrut(10));
            center.add(statusLabel);
            center.add(Box.createVerticalStrut(10));

            JLabel group = new JLabel("Group: Rishi Raj | Abhilash | Md Afzal | Ayush | Pranav", SwingConstants.CENTER);
            group.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            group.setForeground(new Color(100, 130, 200));
            group.setAlignmentX(Component.CENTER_ALIGNMENT);
            center.add(group);

            panel.add(center, BorderLayout.CENTER);
            setContentPane(panel);
        }

        public void setStatus(String msg) {
            SwingUtilities.invokeLater(() -> statusLabel.setText(msg));
        }
    }
}
