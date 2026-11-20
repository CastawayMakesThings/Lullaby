package io.github.equinoxelectronic.lullaby.ui;

import io.github.equinoxelectronic.lullaby.gamedata.GameData;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.gamedata.Inventory;
import io.github.equinoxelectronic.lullaby.gamedata.Item;
import io.github.equinoxelectronic.lullaby.gamedata.ItemClass;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class InventoryDialog {

    public static JDialog show(Component parent, GameState gameState, GameData gameData) {
        Window owner = parent != null ? SwingUtilities.getWindowAncestor(parent) : null;
        JDialog dialog;
        if (owner instanceof Frame) {
            dialog = new JDialog((Frame) owner, "Inventory", true);
        } else if (owner instanceof Dialog) {
            dialog = new JDialog((Dialog) owner, "Inventory", true);
        } else {
            dialog = new JDialog((Frame) null, "Inventory", true);
        }

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(460, 520);
        dialog.setMinimumSize(new Dimension(380, 400));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(new Color(28, 29, 32));
        mainPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(28, 29, 32));
        headerPanel.setOpaque(true);

        JLabel titleLabel = new JLabel("Inventory");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(245, 245, 248));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));

        Inventory inv = gameState != null ? gameState.getInventory() : null;
        int totalItems = 0;
        int distinctItems = 0;
        if (inv != null && inv.items != null) {
            for (Item it : inv.items.values()) {
                if (it != null && it.quantity > 0) {
                    distinctItems++;
                    totalItems += it.quantity;
                }
            }
        }

        String subText = distinctItems == 0 ? "No items in your inventory"
                : distinctItems + (distinctItems == 1 ? " item" : " items") + " (" + totalItems + " total)";
        JLabel subLabel = new JLabel(subText);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(new Color(158, 163, 172));
        subLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(subLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Items List Panel
        JPanel itemsList = new JPanel();
        itemsList.setLayout(new BoxLayout(itemsList, BoxLayout.Y_AXIS));
        itemsList.setBackground(new Color(28, 29, 32));
        itemsList.setBorder(new EmptyBorder(5, 0, 5, 5));

        if (inv == null || inv.items == null || distinctItems == 0) {
            JPanel emptyPanel = new JPanel(new GridBagLayout());
            emptyPanel.setBackground(new Color(28, 29, 32));
            JLabel emptyLabel = new JLabel("Your inventory is currently empty.");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyLabel.setForeground(new Color(140, 145, 155));
            emptyPanel.add(emptyLabel);
            itemsList.add(emptyPanel);
        } else {
            Map<String, ItemClass> gameItems = inv.getGameItems();
            for (Item item : inv.items.values()) {
                if (item == null || item.quantity <= 0) continue;

                ItemClass itemClass = gameItems != null ? gameItems.get(item.classPointer) : null;
                String displayName = (itemClass != null && itemClass.displayName != null && !itemClass.displayName.trim().isEmpty())
                        ? itemClass.displayName
                        : item.classPointer;
                String description = (itemClass != null && itemClass.description != null && !itemClass.description.trim().isEmpty())
                        ? itemClass.description
                        : null;

                JPanel itemCard = new JPanel();
                itemCard.setLayout(new BorderLayout(10, 6));
                itemCard.setBackground(new Color(40, 42, 47));
                itemCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(55, 58, 65), 1, true),
                        new EmptyBorder(10, 14, 10, 14)
                ));
                itemCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, description != null ? 80 : 50));
                itemCard.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Top row with Name and Quantity
                JPanel topRow = new JPanel(new BorderLayout());
                topRow.setOpaque(false);

                JLabel nameLabel = new JLabel(displayName);
                nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                nameLabel.setForeground(new Color(240, 240, 245));
                topRow.add(nameLabel, BorderLayout.WEST);

                JLabel qtyBadge = new JLabel(" x" + item.quantity + " ");
                qtyBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
                qtyBadge.setForeground(new Color(220, 235, 255));
                qtyBadge.setBackground(new Color(45, 60, 85));
                qtyBadge.setOpaque(true);
                qtyBadge.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 95, 135), 1, true),
                        new EmptyBorder(2, 6, 2, 6)
                ));
                topRow.add(qtyBadge, BorderLayout.EAST);

                itemCard.add(topRow, BorderLayout.NORTH);

                if (description != null) {
                    JLabel descLabel = new JLabel("<html><body style='width: 250px;'>" + description.replace("\n", "<br>") + "</body></html>");
                    descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    descLabel.setForeground(new Color(170, 175, 185));
                    itemCard.add(descLabel, BorderLayout.CENTER);
                }

                itemsList.add(itemCard);
                itemsList.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(itemsList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(45, 48, 54), 1, true));
        scrollPane.getViewport().setBackground(new Color(28, 29, 32));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Close Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomPanel.setBackground(new Color(28, 29, 32));

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.setPreferredSize(new Dimension(90, 32));
        closeBtn.putClientProperty("JButton.buttonType", "roundRect");
        closeBtn.addActionListener(e -> dialog.dispose());
        bottomPanel.add(closeBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        if (owner != null && owner.isShowing()) {
            dialog.setLocationRelativeTo(owner);
        } else if (parent != null && parent.isShowing()) {
            dialog.setLocationRelativeTo(parent);
        } else {
            dialog.setLocationRelativeTo(null);
        }

        dialog.setVisible(true);
        return dialog;
    }
}
