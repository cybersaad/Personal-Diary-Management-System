package gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Centralized UI theme for the Personal Diary Management System.
 * Deep navy + soft white flat design with modern fonts, rounded buttons, and hover effects.
 */
public class UITheme {

    // ─── Color Palette ──────────────────────────────────────────────
    public static final Color NAVY_DARK     = new Color(15, 23, 42);     // Sidebar / header
    public static final Color NAVY_MID      = new Color(30, 41, 59);     // Cards / panels
    public static final Color NAVY_LIGHT    = new Color(51, 65, 85);     // Input backgrounds
    public static final Color NAVY_HOVER    = new Color(71, 85, 105);    // Hover states
    public static final Color SOFT_WHITE    = new Color(241, 245, 249);  // Primary text
    public static final Color MUTED_TEXT    = new Color(148, 163, 184);  // Secondary text
    public static final Color ACCENT_BLUE   = new Color(59, 130, 246);   // Primary accent
    public static final Color ACCENT_HOVER  = new Color(96, 165, 250);   // Accent hover
    public static final Color ACCENT_GREEN  = new Color(34, 197, 94);    // Success / green
    public static final Color ACCENT_RED    = new Color(239, 68, 68);    // Danger / red
    public static final Color ACCENT_ORANGE = new Color(249, 115, 22);   // Warning / orange
    public static final Color ACCENT_PURPLE = new Color(168, 85, 247);   // Purple accent
    public static final Color ACCENT_TEAL   = new Color(20, 184, 166);   // Teal accent
    public static final Color CARD_BG       = new Color(30, 41, 59);     // Card backgrounds
    public static final Color INPUT_BG      = new Color(51, 65, 85);     // Input field bg
    public static final Color INPUT_BORDER  = new Color(71, 85, 105);    // Input field border
    public static final Color DIVIDER       = new Color(51, 65, 85);     // Dividers

    // ─── Fonts ──────────────────────────────────────────────────────
    public static final Font FONT_TITLE     = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SUBTITLE  = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADING   = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY      = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL     = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON    = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_LABEL     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_MONO      = new Font("Consolas", Font.PLAIN, 13);

    // ─── Dimensions ─────────────────────────────────────────────────
    public static final int CORNER_RADIUS   = 12;
    public static final int BUTTON_HEIGHT   = 40;
    public static final int INPUT_HEIGHT    = 36;

    // ─── Apply Global Look & Feel ────────────────────────────────────
    public static void applyGlobalTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        UIManager.put("Panel.background", NAVY_DARK);
        UIManager.put("OptionPane.background", NAVY_MID);
        UIManager.put("OptionPane.messageForeground", SOFT_WHITE);
        UIManager.put("OptionPane.messageFont", FONT_BODY);
        UIManager.put("OptionPane.buttonFont", FONT_BUTTON);
        UIManager.put("Button.background", ACCENT_BLUE);
        UIManager.put("Button.foreground", SOFT_WHITE);
        UIManager.put("Button.font", FONT_BUTTON);
    }

    // ─── Styled JTextField ─────────────────────────────────────────
    public static JTextField createTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(MUTED_TEXT);
                    g2.setFont(FONT_LABEL);
                    Insets insets = getInsets();
                    g2.drawString(placeholder, insets.left + 4, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        field.setFont(FONT_BODY);
        field.setForeground(SOFT_WHITE);
        field.setBackground(INPUT_BG);
        field.setCaretColor(SOFT_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(INPUT_BORDER, 8),
                new EmptyBorder(6, 12, 6, 12)));
        field.setPreferredSize(new Dimension(200, INPUT_HEIGHT));
        return field;
    }

    // ─── Styled JTextArea ──────────────────────────────────────────
    public static JTextArea createTextArea(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        area.setFont(FONT_BODY);
        area.setForeground(SOFT_WHITE);
        area.setBackground(INPUT_BG);
        area.setCaretColor(SOFT_WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(8, 12, 8, 12));
        return area;
    }

    // ─── Styled JScrollPane ────────────────────────────────────────
    public static JScrollPane createScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(new RoundedBorder(INPUT_BORDER, 8));
        scrollPane.getViewport().setBackground(INPUT_BG);
        scrollPane.setBackground(INPUT_BG);
        styleScrollBar(scrollPane.getVerticalScrollBar());
        styleScrollBar(scrollPane.getHorizontalScrollBar());
        return scrollPane;
    }

    private static void styleScrollBar(JScrollBar scrollBar) {
        scrollBar.setBackground(NAVY_MID);
        scrollBar.setPreferredSize(new Dimension(8, 8));
        scrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = NAVY_HOVER;
                trackColor = NAVY_MID;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            private JButton createZeroButton() {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                btn.setMinimumSize(new Dimension(0, 0));
                btn.setMaximumSize(new Dimension(0, 0));
                return btn;
            }
        });
    }

    // ─── Styled JComboBox ──────────────────────────────────────────
    public static <T> JComboBox<T> createComboBox(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setFont(FONT_BODY);
        combo.setForeground(SOFT_WHITE);
        combo.setBackground(INPUT_BG);
        combo.setBorder(new RoundedBorder(INPUT_BORDER, 8));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(FONT_BODY);
                label.setBorder(new EmptyBorder(4, 8, 4, 8));
                if (isSelected) {
                    label.setBackground(ACCENT_BLUE);
                    label.setForeground(SOFT_WHITE);
                } else {
                    label.setBackground(INPUT_BG);
                    label.setForeground(SOFT_WHITE);
                }
                return label;
            }
        });
        return combo;
    }

    // ─── Styled JCheckBox ──────────────────────────────────────────
    public static JCheckBox createCheckBox(String text) {
        JCheckBox check = new JCheckBox(text);
        check.setFont(FONT_BODY);
        check.setForeground(SOFT_WHITE);
        check.setBackground(NAVY_MID);
        check.setFocusPainted(false);
        return check;
    }

    // ─── Styled JLabel ─────────────────────────────────────────────
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(MUTED_TEXT);
        return label;
    }

    public static JLabel createHeadingLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_HEADING);
        label.setForeground(SOFT_WHITE);
        return label;
    }

    // ─── Primary Rounded Button (Accent Blue) ─────────────────────
    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, ACCENT_BLUE, ACCENT_HOVER, SOFT_WHITE);
    }

    // ─── Danger Button (Red) ──────────────────────────────────────
    public static JButton createDangerButton(String text) {
        return createStyledButton(text, ACCENT_RED, new Color(248, 113, 113), SOFT_WHITE);
    }

    // ─── Success Button (Green) ───────────────────────────────────
    public static JButton createSuccessButton(String text) {
        return createStyledButton(text, ACCENT_GREEN, new Color(74, 222, 128), SOFT_WHITE);
    }

    // ─── Secondary/Outline Button ─────────────────────────────────
    public static JButton createSecondaryButton(String text) {
        return createStyledButton(text, NAVY_LIGHT, NAVY_HOVER, SOFT_WHITE);
    }

    // ─── Generic styled rounded button with hover ─────────────────
    public static JButton createStyledButton(String text, Color bg, Color hoverBg, Color fg) {
        JButton btn = new JButton(text) {
            private boolean hovered = false;
            {
                setContentAreaFilled(false);
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color paintColor = hovered ? hoverBg : bg;
                g2.setColor(paintColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BUTTON);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, BUTTON_HEIGHT));
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));
        return btn;
    }

    // ─── Dashboard menu button (larger, with icon emoji) ──────────
    public static JButton createDashboardButton(String text, String emoji, Color accentColor, Runnable action) {
        JButton btn = new JButton() {
            private boolean hovered = false;
            {
                setContentAreaFilled(false);
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background
                Color cardBg = hovered ? NAVY_HOVER : NAVY_MID;
                g2.setColor(cardBg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));

                // Left accent bar
                g2.setColor(accentColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, 5, getHeight(), 4, 4));

                // Emoji
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
                g2.setColor(accentColor);
                FontMetrics efm = g2.getFontMetrics();
                g2.drawString(emoji, 20, getHeight() / 2 + efm.getAscent() / 2 - 2);

                // Text
                g2.setFont(FONT_BUTTON);
                g2.setColor(SOFT_WHITE);
                g2.drawString(text, 56, getHeight() / 2 + 5);

                // Arrow indicator
                g2.setFont(FONT_BODY);
                g2.setColor(hovered ? accentColor : MUTED_TEXT);
                g2.drawString("→", getWidth() - 30, getHeight() / 2 + 5);

                g2.dispose();
            }
        };
        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(260, 56));
        btn.setPreferredSize(new Dimension(260, 56));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    // ─── Styled JList ──────────────────────────────────────────────
    public static <T> void styleList(JList<T> list) {
        list.setFont(FONT_BODY);
        list.setForeground(SOFT_WHITE);
        list.setBackground(INPUT_BG);
        list.setSelectionBackground(ACCENT_BLUE);
        list.setSelectionForeground(SOFT_WHITE);
        list.setFixedCellHeight(32);
        list.setBorder(new EmptyBorder(4, 8, 4, 8));
    }

    // ─── Card Panel ────────────────────────────────────────────────
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        return card;
    }

    // ─── Section Title ─────────────────────────────────────────────
    public static JPanel createSectionTitle(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBTITLE);
        label.setForeground(SOFT_WHITE);
        panel.add(label, BorderLayout.WEST);

        // Subtle divider line
        JPanel divider = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(DIVIDER);
                g2.fillRect(0, getHeight() / 2, getWidth(), 1);
                g2.dispose();
            }
        };
        divider.setOpaque(false);
        divider.setPreferredSize(new Dimension(0, 2));
        panel.add(divider, BorderLayout.SOUTH);

        return panel;
    }

    // ─── Header Bar ────────────────────────────────────────────────
    public static JPanel createHeaderBar(String title, Color accentColor) {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient header
                GradientPaint gp = new GradientPaint(0, 0, NAVY_DARK, getWidth(), 0,
                        new Color(NAVY_DARK.getRed(), NAVY_DARK.getGreen(), NAVY_DARK.getBlue() + 15));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Bottom accent line
                g2.setColor(accentColor);
                g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                g2.dispose();
            }
        };
        header.setBorder(new EmptyBorder(18, 24, 18, 24));
        header.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(SOFT_WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        return header;
    }

    // ─── Rounded Border ────────────────────────────────────────────
    public static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }
    }

    // ─── Load icon image from the images folder ─────────────────
    /**
     * Loads an image from src/images/ using a relative path strategy.
     * Tries classpath first, then falls back to file-system paths.
     * Returns null if the image can't be found.
     */
    public static ImageIcon loadIcon(String filename, int width, int height) {
        // 1) Try classpath (works when running from IDE with src on classpath)
        java.net.URL url = UITheme.class.getResource("/images/" + filename);
        if (url != null) {
            ImageIcon raw = new ImageIcon(url);
            Image scaled = raw.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
        // 2) Try relative file paths
        String[] candidates = {
            "src/images/" + filename,
            "images/" + filename
        };
        for (String path : candidates) {
            java.io.File file = new java.io.File(path);
            if (file.exists()) {
                ImageIcon raw = new ImageIcon(file.getAbsolutePath());
                Image scaled = raw.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        }
        return null; // not found
    }

    // ─── Styled Frame defaults ────────────────────────────────────
    /**
     * Applies standard frame properties: title, size, position, icon.
     */
    public static void styleFrame(JFrame frame, String title, int width, int height) {
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(NAVY_DARK);

        // Set the window icon for every frame
        ImageIcon icon = loadIcon("logo.png", 32, 32);
        if (icon != null) {
            frame.setIconImage(icon.getImage());
        }
    }
}
