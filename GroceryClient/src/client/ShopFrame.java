package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Modern grocery shop window. Reuses the socket created by LoginFrame.
 */
public class ShopFrame extends JFrame {
    private static final Color FOREST = new Color(20, 83, 45);
    private static final Color GREEN = new Color(22, 163, 74);
    private static final Color MINT = new Color(220, 252, 231);
    private static final Color CREAM = new Color(255, 251, 235);
    private static final Color CARD = new Color(255, 255, 255);
    private static final Color TEXT_DARK = new Color(31, 41, 55);
    private static final Color TEXT_MUTED = new Color(107, 114, 128);
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("0.00");

    private static final Map<String, GroceryItem> ITEMS = new LinkedHashMap<String, GroceryItem>();

    static {
        ITEMS.put("Apples", new GroceryItem("\uD83C\uDF4E", "Apples", 20.0, new Color(254, 226, 226)));
        ITEMS.put("Banana", new GroceryItem("\uD83C\uDF4C", "Banana", 30.0, new Color(254, 249, 195)));
        ITEMS.put("Oranges", new GroceryItem("\uD83C\uDF4A", "Oranges", 10.0, new Color(255, 237, 213)));
        ITEMS.put("Tomatoes", new GroceryItem("\uD83C\uDF45", "Tomatoes", 15.0, new Color(255, 228, 230)));
        ITEMS.put("Potatoes", new GroceryItem("\uD83E\uDD54", "Potatoes", 8.0, new Color(254, 243, 199)));
        ITEMS.put("Grapes", new GroceryItem("\uD83C\uDF47", "Grapes", 45.0, new Color(243, 232, 255)));
    }

    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final Map<String, JSpinner> quantitySpinners = new LinkedHashMap<String, JSpinner>();
    private final JLabel totalLabel = new JLabel("0.00 LE");
    private final JLabel itemCountLabel = new JLabel("Choose your groceries");

    public ShopFrame(Socket socket, DataInputStream input, DataOutputStream output) {
        this.socket = socket;
        this.input = input;
        this.output = output;

        setTitle("\uD83D\uDED2 Fresh Grocery Market");
        setSize(1040, 720);
        setMinimumSize(new Dimension(980, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(createContentPanel());
        addCloseHandler();
    }

    private JPanel createContentPanel() {
        GradientPanel background = new GradientPanel(new BorderLayout(22, 22), new Color(236, 253, 245), CREAM);
        background.setBorder(new EmptyBorder(26, 30, 26, 30));
        background.add(createHeaderPanel(), BorderLayout.NORTH);
        background.add(createItemsPanel(), BorderLayout.CENTER);
        background.add(createCheckoutPanel(), BorderLayout.EAST);
        return background;
    }

    private JPanel createHeaderPanel() {
        RoundedPanel headerPanel = new RoundedPanel(30, FOREST);
        headerPanel.setLayout(new BorderLayout(18, 0));
        headerPanel.setBorder(new EmptyBorder(28, 32, 28, 32));

        JLabel titleLabel = new JLabel("\uD83D\uDED2 Fresh Grocery Market");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Hand-picked produce, clear prices, server-calculated checkout");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitleLabel.setForeground(MINT);

        JPanel textPanel = new TransparentPanel(new GridLayout(2, 1, 0, 4));
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        RoundedPanel badge = new RoundedPanel(24, new Color(236, 253, 245));
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 18, 10));
        JLabel badgeLabel = new JLabel("Premium Fresh Picks");
        badgeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        badgeLabel.setForeground(FOREST);
        badge.add(badgeLabel);

        headerPanel.add(textPanel, BorderLayout.CENTER);
        headerPanel.add(badge, BorderLayout.EAST);
        return headerPanel;
    }

    private JScrollPane createItemsPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 18, 18));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        for (GroceryItem item : ITEMS.values()) {
            gridPanel.add(createItemCard(item));
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createItemCard(GroceryItem item) {
        RoundedPanel card = new RoundedPanel(28, CARD);
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 252, 231), 1),
                new EmptyBorder(20, 20, 20, 20)));

        RoundedPanel iconBubble = new RoundedPanel(28, item.accentColor);
        iconBubble.setPreferredSize(new Dimension(92, 92));
        iconBubble.setLayout(new BorderLayout());
        JLabel iconLabel = new JLabel(item.icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 46));
        iconBubble.add(iconLabel, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(item.name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(TEXT_DARK);

        JLabel priceLabel = new JLabel(MONEY_FORMAT.format(item.price) + " LE / Kg");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        priceLabel.setForeground(GREEN);

        JLabel hintLabel = new JLabel("Fresh stock available");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hintLabel.setForeground(TEXT_MUTED);

        JPanel textPanel = new TransparentPanel(new GridLayout(3, 1, 0, 3));
        textPanel.add(nameLabel);
        textPanel.add(priceLabel);
        textPanel.add(hintLabel);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        spinner.setFont(new Font("Segoe UI", Font.BOLD, 16));
        spinner.setPreferredSize(new Dimension(84, 38));
        spinner.addChangeListener(e -> updateSelectionSummary());
        quantitySpinners.put(item.name, spinner);

        RoundedPanel quantityPanel = new RoundedPanel(18, new Color(249, 250, 251));
        quantityPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 9));
        JLabel kgLabel = new JLabel("Kg");
        kgLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        kgLabel.setForeground(TEXT_MUTED);
        quantityPanel.add(kgLabel);
        quantityPanel.add(spinner);

        JPanel rightPanel = new TransparentPanel(new BorderLayout(0, 12));
        rightPanel.add(textPanel, BorderLayout.CENTER);
        rightPanel.add(quantityPanel, BorderLayout.SOUTH);

        card.add(iconBubble, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createCheckoutPanel() {
        RoundedPanel panel = new RoundedPanel(30, Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 0));
        panel.setLayout(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 252, 231), 1),
                new EmptyBorder(28, 24, 28, 24)));

        JLabel title = new JLabel("Your Basket");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);

        itemCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        itemCountLabel.setForeground(TEXT_MUTED);

        JPanel topPanel = new TransparentPanel(new GridLayout(2, 1, 0, 6));
        topPanel.add(title);
        topPanel.add(itemCountLabel);

        RoundedPanel totalCard = new RoundedPanel(26, new Color(240, 253, 244));
        totalCard.setLayout(new GridLayout(3, 1, 0, 8));
        totalCard.setBorder(new EmptyBorder(22, 18, 22, 18));

        JLabel totalCaption = new JLabel("Total Price", SwingConstants.CENTER);
        totalCaption.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalCaption.setForeground(TEXT_MUTED);

        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        totalLabel.setForeground(FOREST);

        JLabel totalHint = new JLabel("Calculated by server", SwingConstants.CENTER);
        totalHint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        totalHint.setForeground(TEXT_MUTED);

        totalCard.add(totalCaption);
        totalCard.add(totalLabel);
        totalCard.add(totalHint);

        JButton checkoutButton = new RoundedButton("Checkout Order");
        checkoutButton.addActionListener(e -> checkout());

        JLabel protocolLabel = new JLabel("<html><center>Secure TCP checkout<br>using writeUTF/readUTF</center></html>");
        protocolLabel.setHorizontalAlignment(SwingConstants.CENTER);
        protocolLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        protocolLabel.setForeground(TEXT_MUTED);

        JPanel bottomPanel = new TransparentPanel(new BorderLayout(0, 12));
        bottomPanel.add(checkoutButton, BorderLayout.NORTH);
        bottomPanel.add(protocolLabel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(totalCard, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void updateSelectionSummary() {
        int selectedTypes = 0;
        int totalKg = 0;

        for (JSpinner spinner : quantitySpinners.values()) {
            int quantity = (Integer) spinner.getValue();
            if (quantity > 0) {
                selectedTypes++;
                totalKg += quantity;
            }
        }

        if (selectedTypes == 0) {
            itemCountLabel.setText("Choose your groceries");
        } else {
            itemCountLabel.setText(selectedTypes + " item types, " + totalKg + " kg selected");
        }
    }

    private void checkout() {
        StringBuilder orderMessage = new StringBuilder("CHECKOUT:");
        StringBuilder receipt = new StringBuilder("Fresh Grocery Market\n\n");
        boolean hasItems = false;

        for (Map.Entry<String, JSpinner> entry : quantitySpinners.entrySet()) {
            int quantity = (Integer) entry.getValue().getValue();
            if (quantity > 0) {
                if (hasItems) {
                    orderMessage.append(",");
                }
                orderMessage.append(entry.getKey()).append("=").append(quantity);

                GroceryItem item = ITEMS.get(entry.getKey());
                receipt.append(item.name)
                        .append("  x ")
                        .append(quantity)
                        .append(" kg   ")
                        .append(MONEY_FORMAT.format(item.price * quantity))
                        .append(" LE\n");
                hasItems = true;
            }
        }

        if (!hasItems) {
            JOptionPane.showMessageDialog(this,
                    "Please select at least one item before checkout.",
                    "No Items Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            output.writeUTF(orderMessage.toString());
            output.flush();

            String response = input.readUTF();
            if (response.startsWith("TOTAL:")) {
                double total = Double.parseDouble(response.substring("TOTAL:".length()));
                totalLabel.setText(MONEY_FORMAT.format(total) + " LE");
                receipt.append("\nTotal: ").append(MONEY_FORMAT.format(total)).append(" LE");
                JOptionPane.showMessageDialog(this,
                        receipt.toString(),
                        "Checkout Receipt",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Server returned an unexpected response: " + response,
                        "Checkout Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not complete checkout: " + e.getMessage(),
                    "Network Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Server returned an invalid total.",
                    "Checkout Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCloseHandler() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sendExitAndClose();
            }
        });
    }

    private void sendExitAndClose() {
        try {
            output.writeUTF("EXIT");
            output.flush();
        } catch (IOException ignored) {
            // The connection may already be closed.
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
                // Nothing else to clean up after the window closes.
            }
        }
    }

    private static class GroceryItem {
        private final String icon;
        private final String name;
        private final double price;
        private final Color accentColor;

        private GroceryItem(String icon, String name, double price, Color accentColor) {
            this.icon = icon;
            this.name = name;
            this.price = price;
            this.accentColor = accentColor;
        }
    }

    private static class GradientPanel extends JPanel {
        private final Color startColor;
        private final Color endColor;

        private GradientPanel(java.awt.LayoutManager layout, Color startColor, Color endColor) {
            super(layout);
            this.startColor = startColor;
            this.endColor = endColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color backgroundColor;

        private RoundedPanel(int radius, Color backgroundColor) {
            this.radius = radius;
            this.backgroundColor = backgroundColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class TransparentPanel extends JPanel {
        private TransparentPanel(java.awt.LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }
    }

    private static class RoundedButton extends JButton {
        private RoundedButton(String text) {
            super(text);
            setPreferredSize(new Dimension(210, 50));
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, GREEN, getWidth(), getHeight(), FOREST));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
