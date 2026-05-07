package gui;

import manager.MemoryManager;
import model.Memory;
import util.FileUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryFrame extends JFrame {
    private JTextField titleField, dateField, locationField, searchField;
    private JTextArea descriptionArea;
    private JList<Memory> memoryList;
    private DefaultListModel<Memory> listModel;
    private MemoryManager manager = new MemoryManager();

    public MemoryFrame() {
        UITheme.styleFrame(this, "Memories", 960, 580);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.NAVY_DARK);
        add(UITheme.createHeaderBar("💫  Personal Memories", UITheme.ACCENT_PURPLE), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        loadMemories();
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

        card.add(UITheme.createSectionTitle("Add / View Memory"), BorderLayout.NORTH);

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setOpaque(false);

        titleField = UITheme.createTextField("Memory title...");
        dateField = UITheme.createTextField("YYYY-MM-DD");
        locationField = UITheme.createTextField("Location...");
        descriptionArea = UITheme.createTextArea(5, 20);

        fields.add(UITheme.createLabel("TITLE"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(titleField);
        fields.add(Box.createVerticalStrut(10));
        fields.add(UITheme.createLabel("DATE (YYYY-MM-DD)"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(dateField);
        fields.add(Box.createVerticalStrut(10));
        fields.add(UITheme.createLabel("LOCATION"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(locationField);
        fields.add(Box.createVerticalStrut(10));
        fields.add(UITheme.createLabel("DESCRIPTION"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(UITheme.createScrollPane(descriptionArea));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton saveBtn = UITheme.createStyledButton("Save Memory", UITheme.ACCENT_PURPLE,
                new Color(192, 132, 252), UITheme.SOFT_WHITE);
        saveBtn.addActionListener(e -> saveMemory());
        buttons.add(saveBtn);

        card.add(fields, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createListPanel() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 10));

        card.add(UITheme.createSectionTitle("Saved Memories"), BorderLayout.NORTH);

        searchField = UITheme.createTextField("Search by title, date, or location...");
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterMemories();
            }
        });

        listModel = new DefaultListModel<>();
        memoryList = new JList<>(listModel);
        UITheme.styleList(memoryList);
        memoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Custom renderer with location info
        memoryList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBorder(new EmptyBorder(6, 12, 6, 12));

                Memory memory = (Memory) value;

                JLabel main = new JLabel(memory.getTitle() + "  (" + memory.getDate() + ")");
                main.setFont(UITheme.FONT_BODY);

                JLabel loc = new JLabel("📍 " + memory.getLocation());
                loc.setFont(UITheme.FONT_SMALL);

                if (isSelected) {
                    panel.setBackground(UITheme.ACCENT_BLUE);
                    main.setForeground(UITheme.SOFT_WHITE);
                    loc.setForeground(new Color(200, 220, 255));
                } else {
                    panel.setBackground(UITheme.INPUT_BG);
                    main.setForeground(UITheme.SOFT_WHITE);
                    loc.setForeground(UITheme.MUTED_TEXT);
                }

                panel.add(main, BorderLayout.CENTER);
                panel.add(loc, BorderLayout.EAST);
                return panel;
            }
        });

        memoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selectMemory();
        });

        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.setOpaque(false);
        searchWrapper.setBorder(new EmptyBorder(0, 0, 8, 0));
        searchWrapper.add(searchField);

        card.add(searchWrapper, BorderLayout.NORTH);
        card.add(UITheme.createScrollPane(memoryList), BorderLayout.CENTER);
        return card;
    }

    private void saveMemory() {
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
        Memory memory = new Memory(
                titleField.getText().trim(),
                date,
                descriptionArea.getText(),
                locationField.getText().trim());
        manager.addMemory(memory);
        listModel.addElement(memory);
        clearForm();
    }

    private void loadMemories() {
        List<Memory> memories = manager.getAllMemories();
        for (Memory m : memories) {
            listModel.addElement(m);
        }
    }

    private void selectMemory() {
        Memory m = memoryList.getSelectedValue();
        if (m != null) {
            titleField.setText(m.getTitle());
            dateField.setText(m.getDate());
            locationField.setText(m.getLocation());
            descriptionArea.setText(m.getDescription());
        }
    }

    private void filterMemories() {
        String keyword = searchField.getText().toLowerCase();
        listModel.clear();
        List<Memory> filtered = manager.getAllMemories().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(keyword) ||
                        m.getDate().toLowerCase().contains(keyword) ||
                        m.getLocation().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        for (Memory m : filtered) {
            listModel.addElement(m);
        }
    }

    private void clearForm() {
        titleField.setText("");
        dateField.setText("");
        locationField.setText("");
        descriptionArea.setText("");
        memoryList.clearSelection();
    }
}
