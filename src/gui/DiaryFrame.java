package gui;

import manager.DiaryManager;
import model.DiaryEntry;
import model.Mood;
import util.FileUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class DiaryFrame extends JFrame {
    private JTextField titleField, dateField, searchField;
    private JTextArea contentArea;
    private JComboBox<String> moodBox; // New: Mood dropdown for diary entries
    private JList<DiaryEntry> diaryList;
    private DefaultListModel<DiaryEntry> listModel;
    private int selectedIndex = -1;
    private DiaryManager manager = new DiaryManager();

    public DiaryFrame() {
        UITheme.styleFrame(this, "Personal Diary", 920, 580);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.NAVY_DARK);
        add(UITheme.createHeaderBar("📖  Personal Diary", UITheme.ACCENT_BLUE), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        loadEntries();
        setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 0));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.setOpaque(false);
        panel.add(createFormPanel());
        panel.add(createListPanel());
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 12));

        // Section title
        card.add(UITheme.createSectionTitle("Add / Edit Entry"), BorderLayout.NORTH);

        // Fields
        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setOpaque(false);

        titleField = UITheme.createTextField("Entry title...");
        dateField = UITheme.createTextField("YYYY-MM-DD");
        contentArea = UITheme.createTextArea(6, 20);

        // Mood dropdown
        String[] moodOptions = new String[Mood.values().length + 1];
        moodOptions[0] = "— Select Mood —";
        for (int i = 0; i < Mood.values().length; i++) {
            moodOptions[i + 1] = Mood.values()[i].name();
        }
        moodBox = UITheme.createComboBox(moodOptions);

        fields.add(UITheme.createLabel("TITLE"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(titleField);
        fields.add(Box.createVerticalStrut(10));
        fields.add(UITheme.createLabel("DATE (YYYY-MM-DD)"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(dateField);
        fields.add(Box.createVerticalStrut(10));
        fields.add(UITheme.createLabel("MOOD"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(moodBox);
        fields.add(Box.createVerticalStrut(10));
        fields.add(UITheme.createLabel("CONTENT"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(UITheme.createScrollPane(contentArea));

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton saveBtn = UITheme.createPrimaryButton("Save");
        JButton updateBtn = UITheme.createSecondaryButton("Update");
        JButton deleteBtn = UITheme.createDangerButton("Delete");
        saveBtn.addActionListener(e -> saveEntry());
        updateBtn.addActionListener(e -> updateEntry());
        deleteBtn.addActionListener(e -> deleteEntry());
        buttons.add(saveBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);

        card.add(fields, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createListPanel() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 10));

        card.add(UITheme.createSectionTitle("Saved Entries"), BorderLayout.NORTH);

        searchField = UITheme.createTextField("Search by title or date...");
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterEntries();
            }
        });

        listModel = new DefaultListModel<>();
        diaryList = new JList<>(listModel);
        UITheme.styleList(diaryList);
        diaryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        diaryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selectEntry();
        });

        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.setOpaque(false);
        searchWrapper.setBorder(new EmptyBorder(0, 0, 8, 0));
        searchWrapper.add(searchField);

        card.add(searchWrapper, BorderLayout.NORTH);
        card.add(UITheme.createScrollPane(diaryList), BorderLayout.CENTER);
        return card;
    }

    private void saveEntry() {
        String date = dateField.getText().trim();
        if (!FileUtil.isValidDate(date)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid date in YYYY-MM-DD format.",
                    "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a title.",
                    "Missing Title", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Mood mood = getSelectedMood();
        DiaryEntry entry = new DiaryEntry(
                titleField.getText().trim(),
                date,
                contentArea.getText(),
                mood);
        manager.addEntry(entry);
        listModel.addElement(entry);
        clearForm();
    }

    private void updateEntry() {
        if (selectedIndex == -1) return;
        String date = dateField.getText().trim();
        if (!FileUtil.isValidDate(date)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid date in YYYY-MM-DD format.",
                    "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Mood mood = getSelectedMood();
        DiaryEntry updated = new DiaryEntry(
                titleField.getText().trim(),
                date,
                contentArea.getText(),
                mood);
        manager.updateEntry(selectedIndex, updated);
        listModel.set(selectedIndex, updated);
        clearForm();
    }

    private void deleteEntry() {
        if (selectedIndex == -1) return;
        listModel.remove(selectedIndex);
        manager.saveAll(java.util.Collections.list(listModel.elements()));
        clearForm();
    }

    private void loadEntries() {
        List<DiaryEntry> entries = manager.getAllEntries();
        for (DiaryEntry e : entries)
            listModel.addElement(e);
    }

    private void selectEntry() {
        selectedIndex = diaryList.getSelectedIndex();
        DiaryEntry e = diaryList.getSelectedValue();
        if (e != null) {
            titleField.setText(e.getTitle());
            dateField.setText(e.getDate());
            contentArea.setText(e.getContent());
            if (e.getMood() != null) {
                moodBox.setSelectedItem(e.getMood().name());
            } else {
                moodBox.setSelectedIndex(0);
            }
        }
    }

    private void filterEntries() {
        String keyword = searchField.getText().toLowerCase();
        listModel.clear();
        List<DiaryEntry> filtered = manager.getAllEntries().stream()
                .filter(e -> e.getTitle().toLowerCase().contains(keyword)
                        || e.getDate().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        for (DiaryEntry e : filtered)
            listModel.addElement(e);
    }

    private Mood getSelectedMood() {
        int idx = moodBox.getSelectedIndex();
        if (idx <= 0) return null;
        try {
            return Mood.valueOf((String) moodBox.getSelectedItem());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void clearForm() {
        titleField.setText("");
        dateField.setText("");
        contentArea.setText("");
        moodBox.setSelectedIndex(0);
        selectedIndex = -1;
        diaryList.clearSelection();
    }
}
