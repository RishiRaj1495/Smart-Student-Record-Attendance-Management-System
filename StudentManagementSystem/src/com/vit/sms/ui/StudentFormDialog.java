package com.vit.sms.ui;

import com.vit.sms.dao.StudentDAO;
import com.vit.sms.model.Student;
import com.vit.sms.util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * StudentFormDialog - Add / Edit student form (Unit 4)
 * Demonstrates: JDialog, GridBagLayout, JComboBox, JTextField, validation
 */
public class StudentFormDialog extends JDialog {

    private final Student  existing;
    private final StudentDAO dao = new StudentDAO();
    private boolean saved = false;

    // Form fields
    private JTextField  txtRegNo, txtName, txtEmail, txtPhone;
    private JComboBox<String> cmbBranch, cmbSemester, cmbGender, cmbStatus;
    private JTextField  txtCgpa;

    public StudentFormDialog(JFrame parent, Student s) {
        super(parent, s == null ? "Add New Student" : "Edit Student", true);
        this.existing = s;
        initComponents();
        if (s != null) populateFields(s);
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setMinimumSize(new Dimension(480, 500));
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(new EmptyBorder(20, 24, 16, 24));
        main.setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel(existing == null ? "Add New Student" : "Edit Student");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UITheme.PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        main.add(title, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        String[] branches  = {"CSE","ECE","EEE","ME","CE","IT","AIDS","AIML","CSD","CSM"};
        String[] semesters = {"1","2","3","4","5","6","7","8"};
        String[] genders   = {"Male","Female","Other"};
        String[] statuses  = {"ACTIVE","INACTIVE"};

        txtRegNo   = field(); txtName  = field();
        txtEmail   = field(); txtPhone = field(); txtCgpa = field();
        cmbBranch  = new JComboBox<>(branches);
        cmbSemester= new JComboBox<>(semesters);
        cmbGender  = new JComboBox<>(genders);
        cmbStatus  = new JComboBox<>(statuses);

        Object[][] rows = {
            {"Registration No *", txtRegNo},
            {"Full Name *",        txtName},
            {"Email",              txtEmail},
            {"Phone",              txtPhone},
            {"Branch",             cmbBranch},
            {"Semester",           cmbSemester},
            {"CGPA (0.0–10.0)",    txtCgpa},
            {"Gender",             cmbGender},
            {"Status",             cmbStatus},
        };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.3;
            JLabel lbl = new JLabel(rows[i][0].toString());
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(UITheme.TEXT_SECONDARY);
            form.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.7;
            Component comp = (Component) rows[i][1];
            if (comp instanceof JTextField) {
                ((JTextField)comp).setPreferredSize(new Dimension(220, 32));
            }
            form.add(comp, gbc);
        }

        if (existing != null) {
            txtRegNo.setEditable(false);
            txtRegNo.setForeground(UITheme.TEXT_SECONDARY);
        }

        main.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton btnSave   = UITheme.successButton(existing == null ? "Save" : "Update");
        JButton btnCancel = UITheme.dangerButton("Cancel");

        btnSave.setPreferredSize(new Dimension(100, 34));
        btnCancel.setPreferredSize(new Dimension(100, 34));

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        main.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(main);
    }

    private void populateFields(Student s) {
        txtRegNo.setText(s.getRegNo());
        txtName.setText(s.getName());
        txtEmail.setText(s.getEmail() != null ? s.getEmail() : "");
        txtPhone.setText(s.getPhone() != null ? s.getPhone() : "");
        txtCgpa.setText(String.valueOf(s.getCgpa()));
        selectItem(cmbBranch,   s.getBranch());
        selectItem(cmbSemester, String.valueOf(s.getSemester()));
        selectItem(cmbGender,   s.getGender());
        selectItem(cmbStatus,   s.getStatus());
    }

    private void save() {
        String regNo = txtRegNo.getText().trim();
        String name  = txtName.getText().trim();

        if (regNo.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Registration No and Name are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double cgpa = 0;
        try {
            String cgpaText = txtCgpa.getText().trim();
            if (!cgpaText.isEmpty()) cgpa = Double.parseDouble(cgpaText);
            if (cgpa < 0 || cgpa > 10) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "CGPA must be a number between 0.0 and 10.0", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student s = existing == null ? new Student() : existing;
        s.setRegNo   (regNo);
        s.setName    (name);
        s.setEmail   (txtEmail.getText().trim());
        s.setPhone   (txtPhone.getText().trim());
        s.setBranch  (cmbBranch.getSelectedItem().toString());
        s.setSemester(Integer.parseInt(cmbSemester.getSelectedItem().toString()));
        s.setCgpa    (cgpa);
        s.setGender  (cmbGender.getSelectedItem().toString());
        s.setStatus  (cmbStatus.getSelectedItem().toString());

        try {
            if (existing == null) {
                dao.addStudent(s);
                JOptionPane.showMessageDialog(this, "Student added successfully!");
            } else {
                dao.updateStudent(s);
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
            }
            saved = true;
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Save error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() { return saved; }

    private JTextField field() {
        JTextField tf = new JTextField();
        tf.setFont(UITheme.FONT_BODY);
        tf.setBorder(new CompoundBorder(
            new LineBorder(UITheme.BORDER_COLOR, 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
        return tf;
    }

    private void selectItem(JComboBox<String> cmb, String val) {
        if (val == null) return;
        for (int i = 0; i < cmb.getItemCount(); i++) {
            if (cmb.getItemAt(i).equalsIgnoreCase(val)) { cmb.setSelectedIndex(i); return; }
        }
    }
}
