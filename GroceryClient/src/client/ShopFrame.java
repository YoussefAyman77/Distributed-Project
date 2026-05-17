package client;

import client.ui.CheckoutDialog;
import client.ui.UiKit;
import client.ui.UiTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Modern grocery shop window. Reuses the socket created by LoginFrame.
 */
public class ShopFrame extends JFrame {
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("0.00");

    private static final Map<String, GroceryItem> ITEMS = new LinkedHashMap<String, GroceryItem>();

    static {
        ITEMS.put("Apples", new GroceryItem("\uD83C\uDF4E", "Apples", "Sweet & crisp", 20.0,
                new Color(255, 228, 225), new Color(185, 28, 28)));
        ITEMS.put("Banana", new GroceryItem("\uD83C\uDF4C", "Banana", "Perfect ripeness", 30.0,
                new Color(254, 249, 195), new Color(161, 98, 7)));
        ITEMS.put("Oranges", new GroceryItem("\uD83C\uDF4A", "Oranges", "Juicy & bright", 10.0,
                new Color(255, 237, 213), new Color(194, 65, 12)));
        ITEMS.put("Tomatoes", new GroceryItem("\uD83C\uDF45", "Tomatoes", "Vine-ripened", 15.0,
                new Color(255, 228, 230), new Color(190, 18, 60)));
        ITEMS.put("Potatoes", new GroceryItem("\uD83E\uDD54", "Potatoes", "Farm staple", 8.0,
                new Color(254, 243, 199), new Color(146, 64, 14)));
        ITEMS.put("Grapes", new GroceryItem("\uD83C\uDF47", "Grapes", "Seedless bunch", 45.0,
                new Color(252, 231, 243), new Color(157, 23, 77)));
    }

    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final Map<String, UiKit.QuantityStepper> steppers = new LinkedHashMap<String, UiKit.QuantityStepper>();
    private final JLabel totalLabel = UiKit.label("0.00 LE", UiTheme.FONT_TOTAL, UiTheme.PRIMARY_DEEP, SwingConstants.CENTER);
    private final JLabel itemCountLabel = UiKit.label(
            "Add items to your basket",
            UiTheme.FONT_CAPTION,
            UiTheme.TEXT_MUTED,
            SwingConstants.LEFT);
    private final JLabel estimateHint = UiKit.label(
            "Updates as you shop",
            UiTheme.FONT_CAPTION,
            UiTheme.TEXT_MUTED,
            SwingConstants.CENTER);

    public ShopFrame(Socket socket, DataInputStream input, DataOutputStream output) {
        this.socket = socket;
        this.input = input;
        this.output = output;

        setTitle("Fresh Basket Market");
        setSize(1140, 780);
        setMinimumSize(new Dimension(1020, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(createContentPanel());
        addCloseHandler();
    }

    private JPanel createContentPanel() {
        JPanel background = UiKit.decorativeBackdrop(new BorderLayout(24, 24));
        background.setBorder(new EmptyBorder(28, 32, 28, 32));
        background.add(createHeaderPanel(), BorderLayout.NORTH);
        background.add(createItemsPanel(), BorderLayout.CENTER);
        background.add(createCheckoutPanel(), BorderLayout.EAST);
        return background;
    }

    private JPanel createHeaderPanel() {
        JPanel header = UiKit.gradientCard(new BorderLayout(20, 0), UiTheme.PRIMARY_DEEP, UiTheme.ACCENT);
        header.setBorder(new EmptyBorder(26, 32, 26, 32));

        JPanel textCol = UiKit.transparent(new GridLayout(2, 1, 0, 6));
        textCol.add(UiKit.label("\uD83D\uDED2  Fresh Basket Market",
                UiTheme.FONT_DISPLAY, UiTheme.TEXT_ON_PRIMARY, SwingConstants.LEFT));
        textCol.add(UiKit.label("Handpicked produce, delightful prices, easy checkout",
                UiTheme.FONT_BODY, UiTheme.TEXT_CREAM, SwingConstants.LEFT));

        UiKit.RoundedPanel badge = new UiKit.RoundedPanel(20, UiTheme.HIGHLIGHT, 0, false);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 10));
        badge.setBorder(new EmptyBorder(6, 18, 6, 18));
        badge.add(UiKit.label("Open now", UiTheme.FONT_BODY_BOLD, UiTheme.PRIMARY_DEEP, SwingConstants.CENTER));

        header.add(textCol, BorderLayout.CENTER);
        header.add(badge, BorderLayout.EAST);
        return header;
    }

    private JScrollPane createItemsPanel() {
        JPanel grid = UiKit.transparent(new GridLayout(0, 2, 20, 20));
        for (GroceryItem item : ITEMS.values()) {
            grid.add(createItemCard(item));
        }
        return UiKit.styledScroll(grid);
    }

    private JPanel createItemCard(GroceryItem item) {
        UiKit.RoundedPanel card = new UiKit.RoundedPanel(28, UiTheme.CARD, 10, true);
        card.setLayout(new BorderLayout(18, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER_SOFT, 1),
                new EmptyBorder(22, 22, 22, 22)));

        UiKit.RoundedPanel iconBubble = new UiKit.RoundedPanel(26, item.accentColor, 0, false);
        iconBubble.setPreferredSize(new Dimension(100, 100));
        iconBubble.setLayout(new BorderLayout());
        iconBubble.add(UiKit.label(item.icon, UiTheme.FONT_EMOJI_LARGE, item.tagColor, SwingConstants.CENTER),
                BorderLayout.CENTER);

        JPanel info = UiKit.transparent(new GridLayout(4, 1, 0, 4));
        info.add(UiKit.label(item.name, UiTheme.FONT_HEADING, UiTheme.TEXT_DARK, SwingConstants.LEFT));
        info.add(UiKit.label(MONEY_FORMAT.format(item.price) + " LE / kg",
                UiTheme.FONT_PRICE, UiTheme.PRIMARY, SwingConstants.LEFT));
        info.add(UiKit.label(item.tagline, UiTheme.FONT_CAPTION, UiTheme.TEXT_MUTED, SwingConstants.LEFT));

        UiKit.RoundedPanel tag = new UiKit.RoundedPanel(12, item.accentColor, 0, false);
        tag.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 2));
        tag.setBorder(new EmptyBorder(4, 10, 4, 10));
        tag.add(UiKit.label("Fresh today", UiTheme.FONT_CAPTION, item.tagColor, SwingConstants.LEFT));
        info.add(tag);

        UiKit.QuantityStepper stepper = UiKit.quantityStepper(0, 99, v -> updateBasketSummary());
        steppers.put(item.name, stepper);

        JPanel right = UiKit.transparent(new BorderLayout(0, 14));
        right.add(info, BorderLayout.CENTER);

        JPanel qtyRow = UiKit.transparent(new BorderLayout());
        qtyRow.add(UiKit.label("Quantity (kg)", UiTheme.FONT_CAPTION, UiTheme.TEXT_MUTED, SwingConstants.LEFT),
                BorderLayout.NORTH);
        qtyRow.add(stepper, BorderLayout.CENTER);
        right.add(qtyRow, BorderLayout.SOUTH);

        card.add(iconBubble, BorderLayout.WEST);
        card.add(right, BorderLayout.CENTER);
        UiKit.installHoverLift(card);
        return card;
    }

    private JPanel createCheckoutPanel() {
        UiKit.RoundedPanel panel = new UiKit.RoundedPanel(30, UiTheme.CARD, 12, true);
        panel.setPreferredSize(new Dimension(320, 0));
        panel.setLayout(new BorderLayout(0, 22));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER_SOFT, 1),
                new EmptyBorder(28, 26, 28, 26)));

        JPanel top = UiKit.transparent(new GridLayout(2, 1, 0, 6));
        top.add(UiKit.label("Your basket", UiTheme.FONT_TITLE, UiTheme.TEXT_DARK, SwingConstants.LEFT));
        top.add(itemCountLabel);

        UiKit.RoundedPanel totalCard = new UiKit.RoundedPanel(26, UiTheme.SURFACE_WARM, 0, false);
        totalCard.setLayout(new GridLayout(3, 1, 0, 6));
        totalCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER, 1),
                new EmptyBorder(24, 16, 24, 16)));
        totalCard.add(UiKit.label("Basket total", UiTheme.FONT_BODY_BOLD, UiTheme.TEXT_MUTED, SwingConstants.CENTER));
        totalCard.add(totalLabel);
        totalCard.add(estimateHint);

        javax.swing.JButton checkoutButton = UiKit.primaryButton("Checkout", this::checkout);
        checkoutButton.setPreferredSize(new Dimension(260, 52));

        JPanel bottom = UiKit.transparent(new BorderLayout());
        bottom.add(checkoutButton, BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);
        panel.add(totalCard, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void updateBasketSummary() {
        int selectedTypes = 0;
        int totalKg = 0;
        double estimate = 0.0;

        for (Map.Entry<String, UiKit.QuantityStepper> entry : steppers.entrySet()) {
            int qty = entry.getValue().getValue();
            if (qty > 0) {
                selectedTypes++;
                totalKg += qty;
                GroceryItem item = ITEMS.get(entry.getKey());
                estimate += item.price * qty;
            }
        }

        if (selectedTypes == 0) {
            itemCountLabel.setText("Add items to your basket");
            totalLabel.setText("0.00 LE");
            totalLabel.setForeground(UiTheme.TEXT_MUTED);
            estimateHint.setText("Updates as you shop");
        } else {
            itemCountLabel.setText(selectedTypes + " products, " + totalKg + " kg selected");
            totalLabel.setText(MONEY_FORMAT.format(estimate) + " LE");
            totalLabel.setForeground(UiTheme.PRIMARY_DEEP);
        }
    }

    private void checkout() {
        StringBuilder orderMessage = new StringBuilder("CHECKOUT:");
        List<CheckoutDialog.ReceiptLine> receiptLines = new ArrayList<CheckoutDialog.ReceiptLine>();
        boolean hasItems = false;

        for (Map.Entry<String, UiKit.QuantityStepper> entry : steppers.entrySet()) {
            int quantity = entry.getValue().getValue();
            if (quantity > 0) {
                if (hasItems) {
                    orderMessage.append(",");
                }
                orderMessage.append(entry.getKey()).append("=").append(quantity);

                GroceryItem item = ITEMS.get(entry.getKey());
                double lineTotal = item.price * quantity;
                receiptLines.add(new CheckoutDialog.ReceiptLine(
                        item.icon, item.name, quantity, lineTotal));
                hasItems = true;
            }
        }

        if (!hasItems) {
            CheckoutDialog.showWarning(this,
                    "Your basket is empty",
                    "Please add at least one item before checking out.");
            return;
        }

        try {
            output.writeUTF(orderMessage.toString());
            output.flush();

            String response = input.readUTF();
            if (response.startsWith("TOTAL:")) {
                double total = Double.parseDouble(response.substring("TOTAL:".length()));
                totalLabel.setText(MONEY_FORMAT.format(total) + " LE");
                totalLabel.setForeground(UiTheme.PRIMARY_DEEP);
                estimateHint.setText("Order complete");
                CheckoutDialog.showReceipt(this, receiptLines, total);
            } else {
                CheckoutDialog.showError(this,
                        "Checkout failed",
                        "Something went wrong. Please try again.");
            }
        } catch (IOException e) {
            CheckoutDialog.showError(this,
                    "Checkout failed",
                    "We could not complete your order. Please check your connection.");
        } catch (NumberFormatException e) {
            CheckoutDialog.showError(this,
                    "Checkout failed",
                    "We received an invalid total. Please try again.");
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
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private static class GroceryItem {
        private final String icon;
        private final String name;
        private final String tagline;
        private final double price;
        private final Color accentColor;
        private final Color tagColor;

        private GroceryItem(String icon, String name, String tagline, double price, Color accentColor, Color tagColor) {
            this.icon = icon;
            this.name = name;
            this.tagline = tagline;
            this.price = price;
            this.accentColor = accentColor;
            this.tagColor = tagColor;
        }
    }
}
