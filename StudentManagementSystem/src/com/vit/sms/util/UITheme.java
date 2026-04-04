package com.vit.sms.util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * UITheme - Centralised Swing styling (Unit 4)
 * Demonstrates: Swing customization, MVC pattern support
 */
public class UITheme {

    // ── VIT Bhopal brand colours ─────────────────────────────────────────
    public static final Color PRIMARY       = new Color(0,  51, 153);   // Deep VIT blue
    public static final Color PRIMARY_DARK  = new Color(0,  35, 110);
    public static final Color PRIMARY_LIGHT = new Color(30, 80, 180);
    public static final Color ACCENT        = new Color(255, 165, 0);   // Orange accent
    public static final Color SUCCESS       = new Color(39, 174, 96);
    public static final Color DANGER        = new Color(192, 57, 43);
    public static final Color WARNING       = new Color(230, 126, 34);
    public static final Color INFO          = new Color(41, 128, 185);

    public static final Color BG_MAIN       = new Color(245, 247, 252);
    public static final Color BG_CARD       = Color.WHITE;
    public static final Color BG_SIDEBAR    = new Color(15,  30,  80);
    public static final Color BG_SIDEBAR_H  = new Color(30,  60, 130);

    public static final Color TEXT_PRIMARY   = new Color(33,  37,  41);
    public static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    public static final Color TEXT_LIGHT     = Color.WHITE;
    public static final Color BORDER_COLOR   = new Color(220, 225, 235);

    // ── Fonts ────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD,  20);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD,  14);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD,  12);

    // ── Dimension presets ────────────────────────────────────────────────
    public static final Dimension BTN_SIZE      = new Dimension(120, 34);
    public static final Dimension BTN_WIDE      = new Dimension(160, 34);
    public static final Dimension INPUT_SIZE    = new Dimension(220, 32);

    /** Apply FlatLaf-style defaults for standard Swing */
    public static void applyGlobalDefaults() {
        UIManager.put("Panel.background",            BG_MAIN);
        UIManager.put("Table.background",            BG_CARD);
        UIManager.put("Table.alternateRowColor",     new Color(235, 240, 255));
        UIManager.put("Table.selectionBackground",   PRIMARY);
        UIManager.put("Table.selectionForeground",   Color.WHITE);
        UIManager.put("Table.gridColor",             BORDER_COLOR);
        UIManager.put("TableHeader.background",      PRIMARY);
        UIManager.put("TableHeader.foreground",      Color.WHITE);
        UIManager.put("TableHeader.font",            FONT_HEADING);
        UIManager.put("TextField.font",              FONT_BODY);
        UIManager.put("ComboBox.font",               FONT_BODY);
        UIManager.put("Label.font",                  FONT_BODY);
        UIManager.put("Button.font",                 FONT_BUTTON);
        UIManager.put("TabbedPane.selected",         PRIMARY_LIGHT);
        UIManager.put("ScrollBar.width",             8);
    }

    /** Create a styled primary button */
    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(BTN_SIZE);
        return btn;
    }

    /** Create a styled success (green) button */
    public static JButton successButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(SUCCESS);
        return btn;
    }

    /** Create a styled danger (red) button */
    public static JButton dangerButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(DANGER);
        return btn;
    }

    /** Create a styled warning (orange) button */
    public static JButton warningButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(WARNING);
        return btn;
    }

    /** Create a card panel with shadow-like border */
    public static JPanel cardPanel() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        return p;
    }

    /** Create a section header label */
    public static JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_HEADING);
        lbl.setForeground(PRIMARY);
        return lbl;
    }

    /** Create a rounded text field */
    public static JTextField styledField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(FONT_BODY);
        tf.setPreferredSize(new Dimension(cols * 8, 32));
        tf.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
        return tf;
    }

    /** Create a styled combo box */
    public static <T> JComboBox<T> styledCombo(T[] items) {
        JComboBox<T> cb = new JComboBox<>(items);
        cb.setFont(FONT_BODY);
        cb.setPreferredSize(new Dimension(180, 32));
        return cb;
    }
}
