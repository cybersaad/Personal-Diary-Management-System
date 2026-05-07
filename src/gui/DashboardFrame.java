package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * DashboardFrame is the main landing screen of the application.
 * It displays the PDMS logo and provides navigation buttons to each module.
 */
public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        UITheme.applyGlobalTheme();
        UITheme.styleFrame(this, "PDMS — Personal Diary Management System", 1050, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.NAVY_DARK);

        // Set the window icon (taskbar / title bar)
        setWindowIcon();

        add(createHeader(), BorderLayout.NORTH);
        add(createMainArea(), BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Sets the application window icon from the images folder.
     */
    private void setWindowIcon() {
        ImageIcon icon = UITheme.loadIcon("logo.png", 32, 32);
        if (icon != null) {
            setIconImage(icon.getImage());
        }
    }

    /**
     * Creates the top header bar with the app name and tagline.
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                        0, 0, UITheme.NAVY_DARK,
                        getWidth(), 0, new Color(20, 30, 55));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Blue accent line at the bottom
                g2.setColor(UITheme.ACCENT_BLUE);
                g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(18, 28, 18, 28));

        JPanel titleArea = new JPanel();
        titleArea.setOpaque(false);
        titleArea.setLayout(new BoxLayout(titleArea, BoxLayout.Y_AXIS));

        JLabel appTitle = new JLabel("Personal Diary Management System");
        appTitle.setFont(UITheme.FONT_TITLE);
        appTitle.setForeground(UITheme.SOFT_WHITE);
        appTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel("Organize your thoughts, tasks, and memories — all in one place.");
        tagline.setFont(UITheme.FONT_SMALL);
        tagline.setForeground(UITheme.MUTED_TEXT);
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleArea.add(appTitle);
        titleArea.add(Box.createVerticalStrut(4));
        titleArea.add(tagline);

        header.add(titleArea, BorderLayout.WEST);
        return header;
    }

    /**
     * Builds the main content area with the logo panel and navigation menu.
     */
    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(UITheme.NAVY_DARK);
        main.add(createLogoPanel(), BorderLayout.CENTER);
        main.add(createNavigationPanel(), BorderLayout.EAST);
        return main;
    }

    /**
     * Creates the center panel that displays the PDMS logo.
     */
    private JPanel createLogoPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Rounded card background behind the logo
                g2.setColor(UITheme.NAVY_MID);
                g2.fill(new RoundRectangle2D.Float(
                        20, 12, getWidth() - 40, getHeight() - 24, 20, 20));
                g2.dispose();
            }
        };
        panel.setBorder(new EmptyBorder(10, 15, 10, 5));
        panel.setOpaque(false);

        // Load and display the logo image
        ImageIcon logoIcon = UITheme.loadIcon("logo.png", 320, 320);
        JLabel logoLabel;
        if (logoIcon != null) {
            logoLabel = new JLabel(logoIcon);
        } else {
            // Graceful fallback if the logo file isn't found
            logoLabel = new JLabel("PDMS");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
            logoLabel.setForeground(UITheme.MUTED_TEXT);
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(logoLabel);
        return panel;
    }

    /**
     * Creates the right-side navigation panel with buttons for each module.
     */
    private JPanel createNavigationPanel() {
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(new EmptyBorder(20, 15, 30, 20));
        nav.setOpaque(false);
        nav.setPreferredSize(new Dimension(290, 0));

        // Section heading
        JLabel heading = UITheme.createLabel("NAVIGATION");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 11));
        heading.setForeground(UITheme.MUTED_TEXT);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        nav.add(heading);
        nav.add(Box.createVerticalStrut(16));

        // Module buttons — one for each feature
        nav.add(UITheme.createDashboardButton("Diary Entry",  "\u270D", UITheme.ACCENT_BLUE,   () -> new DiaryFrame()));
        nav.add(Box.createVerticalStrut(10));
        nav.add(UITheme.createDashboardButton("Grocery List", "\uD83D\uDED2", UITheme.ACCENT_GREEN,  () -> new GroceryFrame()));
        nav.add(Box.createVerticalStrut(10));
        nav.add(UITheme.createDashboardButton("Task Manager", "\u2611", UITheme.ACCENT_ORANGE, () -> new TaskFrame()));
        nav.add(Box.createVerticalStrut(10));
        nav.add(UITheme.createDashboardButton("Mood Tracker", "\u263A", UITheme.ACCENT_TEAL,   () -> new MoodFrame()));
        nav.add(Box.createVerticalStrut(10));
        nav.add(UITheme.createDashboardButton("Memories",     "\u2B50", UITheme.ACCENT_PURPLE, () -> new MemoryFrame()));

        nav.add(Box.createVerticalGlue());

        // Version label at the bottom
        JLabel version = UITheme.createLabel("PDMS v1.0");
        version.setFont(UITheme.FONT_SMALL);
        version.setAlignmentX(Component.CENTER_ALIGNMENT);
        nav.add(version);

        return nav;
    }
}
