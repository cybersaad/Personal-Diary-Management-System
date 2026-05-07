package gui;

import manager.GroceryManager;
import model.GroceryItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class GroceryFrame extends JFrame {
    private JTextField itemField;
    private JCheckBox purchasedBox;
    private JList<GroceryItem> itemList;
    private DefaultListModel<GroceryItem> listModel;
    private GroceryManager manager = new GroceryManager();

    public GroceryFrame() {
        UITheme.styleFrame(this, "Grocery List", 480, 520);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.NAVY_DARK);
        add(UITheme.createHeaderBar("🛒  Grocery List", UITheme.ACCENT_GREEN), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        loadItems();
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

        card.add(UITheme.createSectionTitle("Add Item"), BorderLayout.NORTH);

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setOpaque(false);

        itemField = UITheme.createTextField("Item name...");
        purchasedBox = UITheme.createCheckBox("Already Purchased");

        fields.add(UITheme.createLabel("ITEM NAME"));
        fields.add(Box.createVerticalStrut(4));
        fields.add(itemField);
        fields.add(Box.createVerticalStrut(8));
        fields.add(purchasedBox);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton addBtn = UITheme.createSuccessButton("Add");
        JButton updateBtn = UITheme.createSecondaryButton("Update");
        JButton deleteBtn = UITheme.createDangerButton("Delete");
        addBtn.addActionListener(e -> addItem());
        updateBtn.addActionListener(e -> updateItem());
        deleteBtn.addActionListener(e -> deleteItem());
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

        card.add(UITheme.createSectionTitle("Shopping Items"), BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        UITheme.styleList(itemList);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Custom cell renderer for purchased items
        itemList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(UITheme.FONT_BODY);
                label.setBorder(new EmptyBorder(6, 12, 6, 12));
                label.setOpaque(true);

                GroceryItem item = (GroceryItem) value;
                if (isSelected) {
                    label.setBackground(UITheme.ACCENT_BLUE);
                    label.setForeground(UITheme.SOFT_WHITE);
                } else {
                    label.setBackground(UITheme.INPUT_BG);
                    if (item != null && item.isPurchased()) {
                        label.setForeground(UITheme.ACCENT_GREEN);
                    } else {
                        label.setForeground(UITheme.SOFT_WHITE);
                    }
                }
                return label;
            }
        });

        itemList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                GroceryItem item = itemList.getSelectedValue();
                if (item != null) {
                    itemField.setText(item.getName());
                    purchasedBox.setSelected(item.isPurchased());
                }
            }
        });

        card.add(UITheme.createScrollPane(itemList), BorderLayout.CENTER);
        return card;
    }

    private void addItem() {
        if (itemField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an item name.",
                    "Missing Name", JOptionPane.WARNING_MESSAGE);
            return;
        }
        GroceryItem item = new GroceryItem(itemField.getText().trim());
        item.setPurchased(purchasedBox.isSelected());
        manager.addItem(item);
        listModel.addElement(item);
        clearForm();
    }

    private void updateItem() {
        int index = itemList.getSelectedIndex();
        if (index == -1) return;
        GroceryItem item = listModel.get(index);
        item.setPurchased(purchasedBox.isSelected());
        listModel.set(index, item);
        saveAll();
        clearForm();
    }

    private void deleteItem() {
        int index = itemList.getSelectedIndex();
        if (index == -1) return;
        listModel.remove(index);
        saveAll();
        clearForm();
    }

    private void loadItems() {
        List<GroceryItem> items = manager.getAllItems();
        for (GroceryItem item : items) {
            listModel.addElement(item);
        }
    }

    private void saveAll() {
        List<GroceryItem> items = java.util.Collections.list(listModel.elements());
        manager.saveAll(items);
    }

    private void clearForm() {
        itemField.setText("");
        purchasedBox.setSelected(false);
        itemList.clearSelection();
    }
}
