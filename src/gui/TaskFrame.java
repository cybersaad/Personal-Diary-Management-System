package gui;

import manager.TaskManager;
import model.Priority;
import model.Task;
import util.FileUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TaskFrame extends JFrame {
    private JTextField titleField;
    private JTextField deadlineField;
    private JComboBox<Priority> priorityBox;
    private JCheckBox completedBox;
    private JList<Task> taskList;
    private DefaultListModel<Task> listModel;
    private TaskManager manager = new TaskManager();

    public TaskFrame() {
        UITheme.styleFrame(this, "Task Planner", 580, 560);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.NAVY_DARK);
        add(UITheme.createHeaderBar("📋  Task Planner", UITheme.ACCENT_ORANGE), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        loadTasks();
        setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel main = new JPanel(new BorderLayout(0, 12));
        main.setBorder(new EmptyBorder(16, 16, 16, 16));
        main.setOpaque(false);
        main.add(createFormCard(), BorderLayout.NORTH);
        main.add(createListCard(), BorderLayout.CENTER);
        return main;
    }

    private JPanel createFormCard() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 10));

        card.add(UITheme.createSectionTitle("Add / Edit Task"), BorderLayout.NORTH);

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setOpaque(false);

        titleField = UITheme.createTextField("Task title...");
        deadlineField = UITheme.createTextField("YYYY-MM-DD");
        priorityBox = UITheme.createComboBox(Priority.values());
        completedBox = UITheme.createCheckBox("Mark as Completed");

        fields.add(UITheme.createLabel("TASK TITLE"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(titleField);
        fields.add(Box.createVerticalStrut(10));
        fields.add(UITheme.createLabel("DEADLINE (YYYY-MM-DD)"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(deadlineField);
        fields.add(Box.createVerticalStrut(10));
        fields.add(UITheme.createLabel("PRIORITY"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(priorityBox);
        fields.add(Box.createVerticalStrut(8));
        fields.add(completedBox);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton addBtn = UITheme.createPrimaryButton("Add");
        JButton updateBtn = UITheme.createSecondaryButton("Update");
        JButton deleteBtn = UITheme.createDangerButton("Delete");
        addBtn.addActionListener(e -> addTask());
        updateBtn.addActionListener(e -> updateTask());
        deleteBtn.addActionListener(e -> deleteTask());
        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);

        card.add(fields, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createListCard() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 8));

        card.add(UITheme.createSectionTitle("Tasks"), BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        UITheme.styleList(taskList);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Custom cell renderer with color-coded priority indicators
        taskList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JPanel panel = new JPanel(new BorderLayout(8, 0));
                panel.setBorder(new EmptyBorder(6, 4, 6, 12));

                Task task = (Task) value;

                // Priority color indicator (left bar)
                Color priorityColor = getPriorityColor(task.getPriority());
                JPanel indicator = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(priorityColor);
                        g2.fillRoundRect(0, 2, getWidth(), getHeight() - 4, 4, 4);
                        g2.dispose();
                    }
                };
                indicator.setPreferredSize(new Dimension(5, 0));
                indicator.setOpaque(false);

                // Task text
                JLabel textLabel = new JLabel(task.toString());
                textLabel.setFont(UITheme.FONT_BODY);
                textLabel.setBorder(new EmptyBorder(0, 8, 0, 0));

                // Priority badge
                JLabel badge = new JLabel(task.getPriority().name());
                badge.setFont(UITheme.FONT_SMALL);
                badge.setForeground(priorityColor);
                badge.setBorder(new EmptyBorder(0, 0, 0, 4));

                if (isSelected) {
                    panel.setBackground(UITheme.ACCENT_BLUE);
                    textLabel.setForeground(UITheme.SOFT_WHITE);
                    badge.setForeground(UITheme.SOFT_WHITE);
                } else {
                    panel.setBackground(UITheme.INPUT_BG);
                    if (task.isCompleted()) {
                        textLabel.setForeground(UITheme.MUTED_TEXT);
                    } else {
                        textLabel.setForeground(UITheme.SOFT_WHITE);
                    }
                }

                panel.add(indicator, BorderLayout.WEST);
                panel.add(textLabel, BorderLayout.CENTER);
                panel.add(badge, BorderLayout.EAST);
                return panel;
            }
        });

        taskList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Task task = taskList.getSelectedValue();
                if (task != null) {
                    titleField.setText(task.getTitle());
                    deadlineField.setText(task.getDeadline());
                    priorityBox.setSelectedItem(task.getPriority());
                    completedBox.setSelected(task.isCompleted());
                }
            }
        });

        card.add(UITheme.createScrollPane(taskList), BorderLayout.CENTER);
        return card;
    }

    /** Returns the color for a given priority level */
    private Color getPriorityColor(Priority priority) {
        switch (priority) {
            case HIGH:   return UITheme.ACCENT_RED;
            case MEDIUM: return UITheme.ACCENT_ORANGE;
            case LOW:    return UITheme.ACCENT_GREEN;
            default:     return UITheme.MUTED_TEXT;
        }
    }

    private void addTask() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a task title.",
                    "Missing Title", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String deadline = deadlineField.getText().trim();
        if (!FileUtil.isValidDate(deadline)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid deadline in YYYY-MM-DD format.",
                    "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Task task = new Task(
                titleField.getText().trim(),
                deadline,
                (Priority) priorityBox.getSelectedItem());
        task.setCompleted(completedBox.isSelected());
        manager.addTask(task);
        listModel.addElement(task);
        clearForm();
    }

    private void updateTask() {
        int index = taskList.getSelectedIndex();
        if (index == -1) return;
        String deadline = deadlineField.getText().trim();
        if (!FileUtil.isValidDate(deadline)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid deadline in YYYY-MM-DD format.",
                    "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Task task = listModel.get(index);
        task.setCompleted(completedBox.isSelected());
        listModel.set(index, task);
        saveAll();
        clearForm();
    }

    private void deleteTask() {
        int index = taskList.getSelectedIndex();
        if (index == -1) return;
        listModel.remove(index);
        saveAll();
        clearForm();
    }

    private void loadTasks() {
        List<Task> tasks = manager.getAllTasks();
        for (Task task : tasks) {
            listModel.addElement(task);
        }
    }

    private void saveAll() {
        List<Task> tasks = java.util.Collections.list(listModel.elements());
        manager.saveAll(tasks);
    }

    private void clearForm() {
        titleField.setText("");
        deadlineField.setText("");
        priorityBox.setSelectedIndex(0);
        completedBox.setSelected(false);
        taskList.clearSelection();
    }
}
