package gui;

import manager.MoodManager;
import model.Mood;
import model.MoodEntry;
import util.FileUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class MoodFrame extends JFrame {
    private JComboBox<Mood> moodBox;
    private JTextField dateField;
    private JTextArea noteArea;
    private JList<MoodEntry> moodList;
    private DefaultListModel<MoodEntry> listModel;
    private JLabel moodSummaryLabel; // New: displays most frequent mood
    private MoodManager manager = new MoodManager();

    public MoodFrame() {
        UITheme.styleFrame(this, "Mood Tracker", 500, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.NAVY_DARK);
        add(UITheme.createHeaderBar("😊  Mood Tracker", UITheme.ACCENT_TEAL), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        loadMoods();
        updateMoodSummary();
        setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel main = new JPanel(new BorderLayout(0, 12));
        main.setBorder(new EmptyBorder(16, 16, 16, 16));
        main.setOpaque(false);

        // Top: Summary card
        main.add(createSummaryCard(), BorderLayout.NORTH);

        // Center: form + history
        JPanel centerPanel = new JPanel(new BorderLayout(0, 12));
        centerPanel.setOpaque(false);
        centerPanel.add(createFormCard(), BorderLayout.NORTH);
        centerPanel.add(createHistoryCard(), BorderLayout.CENTER);

        main.add(centerPanel, BorderLayout.CENTER);
        return main;
    }

    private JPanel createSummaryCard() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient card
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 184, 166, 40),
                        getWidth(), getHeight(), new Color(59, 130, 246, 20));
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                // Border
                g2.setColor(new Color(20, 184, 166, 60));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel icon = new JLabel("📊");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Mood Summary");
        titleLabel.setFont(UITheme.FONT_HEADING);
        titleLabel.setForeground(UITheme.SOFT_WHITE);

        moodSummaryLabel = new JLabel("No moods logged yet");
        moodSummaryLabel.setFont(UITheme.FONT_BODY);
        moodSummaryLabel.setForeground(UITheme.ACCENT_TEAL);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(moodSummaryLabel);

        card.add(icon, BorderLayout.WEST);
        JPanel spacer = new JPanel(new BorderLayout());
        spacer.setOpaque(false);
        spacer.setBorder(new EmptyBorder(0, 12, 0, 0));
        spacer.add(textPanel);
        card.add(spacer, BorderLayout.CENTER);

        return card;
    }

    private JPanel createFormCard() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 10));

        card.add(UITheme.createSectionTitle("Log Your Mood"), BorderLayout.NORTH);

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setOpaque(false);

        moodBox = UITheme.createComboBox(Mood.values());
        dateField = UITheme.createTextField("YYYY-MM-DD");
        noteArea = UITheme.createTextArea(3, 20);

        fields.add(UITheme.createLabel("SELECT MOOD"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(moodBox);
        fields.add(Box.createVerticalStrut(10));
        fields.add(UITheme.createLabel("DATE (YYYY-MM-DD)"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(dateField);
        fields.add(Box.createVerticalStrut(10));
        fields.add(UITheme.createLabel("NOTES (OPTIONAL)"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(UITheme.createScrollPane(noteArea));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton saveBtn = UITheme.createPrimaryButton("Save Mood");
        saveBtn.addActionListener(e -> saveMood());
        buttons.add(saveBtn);

        card.add(fields, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createHistoryCard() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 8));

        card.add(UITheme.createSectionTitle("Mood History"), BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        moodList = new JList<>(listModel);
        UITheme.styleList(moodList);
        moodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Custom renderer with mood emoji
        moodList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(UITheme.FONT_BODY);
                label.setBorder(new EmptyBorder(6, 12, 6, 12));
                label.setOpaque(true);

                MoodEntry entry = (MoodEntry) value;
                if (entry != null) {
                    String emoji = getMoodEmoji(entry.getMood());
                    label.setText(emoji + "  " + entry.toString());
                }

                if (isSelected) {
                    label.setBackground(UITheme.ACCENT_BLUE);
                    label.setForeground(UITheme.SOFT_WHITE);
                } else {
                    label.setBackground(UITheme.INPUT_BG);
                    label.setForeground(UITheme.SOFT_WHITE);
                }
                return label;
            }
        });

        moodList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                MoodEntry entry = moodList.getSelectedValue();
                if (entry != null) {
                    moodBox.setSelectedItem(entry.getMood());
                    dateField.setText(entry.getDate());
                    noteArea.setText(entry.getNote());
                }
            }
        });

        card.add(UITheme.createScrollPane(moodList), BorderLayout.CENTER);
        return card;
    }

    private String getMoodEmoji(Mood mood) {
        if (mood == null) return "😐";
        switch (mood) {
            case HAPPY:    return "😄";
            case SAD:      return "😢";
            case RELAXED:  return "😌";
            case STRESSED: return "😰";
            case ANGRY:    return "😡";
            default:       return "😐";
        }
    }

    private void saveMood() {
        String date = dateField.getText().trim();
        if (!FileUtil.isValidDate(date)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid date in YYYY-MM-DD format.",
                    "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }
        MoodEntry entry = new MoodEntry(
                (Mood) moodBox.getSelectedItem(),
                date,
                noteArea.getText());
        manager.addMood(entry);
        listModel.addElement(entry);
        updateMoodSummary();
        clearForm();
    }

    private void loadMoods() {
        List<MoodEntry> moods = manager.getAllMoods();
        for (MoodEntry m : moods) {
            listModel.addElement(m);
        }
    }

    private void updateMoodSummary() {
        Mood mostFrequent = manager.getMostFrequentMood();
        if (mostFrequent != null) {
            String emoji = getMoodEmoji(mostFrequent);
            moodSummaryLabel.setText("Most frequent mood: " + emoji + " " + mostFrequent.name());
        } else {
            moodSummaryLabel.setText("No moods logged yet");
        }
    }

    private void clearForm() {
        dateField.setText("");
        noteArea.setText("");
        moodBox.setSelectedIndex(0);
        moodList.clearSelection();
    }
}
