package client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/** Styled modal dialogs for checkout and messages. */
public final class CheckoutDialog {
    private static final DecimalFormat MONEY = new DecimalFormat("0.00");

    private CheckoutDialog() {
    }

    public static class ReceiptLine {
        public final String icon;
        public final String name;
        public final int quantityKg;
        public final double lineTotal;

        public ReceiptLine(String icon, String name, int quantityKg, double lineTotal) {
            this.icon = icon;
            this.name = name;
            this.quantityKg = quantityKg;
            this.lineTotal = lineTotal;
        }
    }

    public static void showReceipt(JFrame owner, List<ReceiptLine> lines, double total) {
        JDialog dialog = createBaseDialog(owner, "Order complete", 540, 720);
        dialog.setContentPane(buildReceiptContent(dialog, lines, total, DialogType.SUCCESS));
        dialog.setVisible(true);
    }

    public static void showWarning(JFrame owner, String title, String message) {
        JDialog dialog = createBaseDialog(owner, title, 420, 280);
        dialog.setContentPane(buildSimpleContent(dialog, title, message, DialogType.WARNING));
        dialog.setVisible(true);
    }

    public static void showError(JFrame owner, String title, String message) {
        JDialog dialog = createBaseDialog(owner, title, 420, 280);
        dialog.setContentPane(buildSimpleContent(dialog, title, message, DialogType.ERROR));
        dialog.setVisible(true);
    }

    private enum DialogType { SUCCESS, WARNING, ERROR }

    private static JDialog createBaseDialog(JFrame owner, String title, int w, int h) {
        JDialog dialog = new JDialog(owner, title, true);
        dialog.setUndecorated(true);
        dialog.setSize(w, h);
        dialog.setLocationRelativeTo(owner);
        dialog.setBackground(new Color(0, 0, 0, 0));
        return dialog;
    }

    private static JPanel buildReceiptContent(JDialog dialog, List<ReceiptLine> lines, double total, DialogType type) {
        JPanel root = backdropPanel();
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        UiKit.RoundedPanel card = new UiKit.RoundedPanel(28, UiTheme.CARD, 12, true);
        card.setLayout(new BorderLayout(0, 0));

        card.add(buildReceiptHeader(), BorderLayout.NORTH);
        card.add(buildReceiptBody(lines), BorderLayout.CENTER);
        card.add(buildReceiptFooter(dialog, total), BorderLayout.SOUTH);

        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private static JPanel buildReceiptHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(10, 26, 14), getWidth(), getHeight(), UiTheme.PRIMARY_DEEP));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel icon = UiKit.label("\uD83C\uDF89", new Font("Segoe UI Emoji", Font.PLAIN, 40),
                Color.WHITE, SwingConstants.CENTER);
        JLabel title = UiKit.label("Thank you for your order!",
                UiTheme.FONT_TITLE, Color.WHITE, SwingConstants.CENTER);
        JLabel sub = UiKit.label("Your receipt is ready below",
                UiTheme.FONT_BODY, UiTheme.TEXT_CREAM, SwingConstants.CENTER);

        JPanel stack = UiKit.transparent(new GridLayout(3, 1, 0, 6));
        stack.add(icon);
        stack.add(title);
        stack.add(sub);
        header.add(stack, BorderLayout.CENTER);
        return header;
    }

    private static JScrollPane buildReceiptBody(List<ReceiptLine> lines) {
        JPanel list = UiKit.transparent(new GridLayout(0, 1, 0, 10));
        list.setBorder(new EmptyBorder(20, 22, 12, 22));

        for (ReceiptLine line : lines) {
            list.add(buildLineRow(line));
        }

        JScrollPane scroll = UiKit.styledScroll(list);
        scroll.setPreferredSize(new Dimension(480, 400));
        return scroll;
    }

    private static JPanel buildLineRow(ReceiptLine line) {
        UiKit.RoundedPanel row = new UiKit.RoundedPanel(18, UiTheme.SURFACE, 0, false);
        row.setLayout(new BorderLayout(12, 0));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER_SOFT, 1),
                new EmptyBorder(12, 14, 12, 14)));

        JLabel emoji = UiKit.label(line.icon, UiTheme.FONT_EMOJI_LARGE, UiTheme.PRIMARY, SwingConstants.CENTER);
        emoji.setPreferredSize(new Dimension(52, 52));

        JPanel mid = UiKit.transparent(new GridLayout(2, 1, 0, 2));
        mid.add(UiKit.label(line.name, UiTheme.FONT_BODY_BOLD, UiTheme.TEXT_DARK, SwingConstants.LEFT));
        mid.add(UiKit.label(line.quantityKg + " kg", UiTheme.FONT_CAPTION, UiTheme.TEXT_MUTED, SwingConstants.LEFT));

        JLabel price = UiKit.label(MONEY.format(line.lineTotal) + " LE",
                UiTheme.FONT_BODY_BOLD, UiTheme.PRIMARY, SwingConstants.RIGHT);

        row.add(emoji, BorderLayout.WEST);
        row.add(mid, BorderLayout.CENTER);
        row.add(price, BorderLayout.EAST);
        return row;
    }

    private static JPanel buildReceiptFooter(JDialog dialog, double total) {
        JPanel footer = UiKit.transparent(new BorderLayout(0, 16));
        footer.setBorder(new EmptyBorder(8, 22, 24, 22));

        UiKit.RoundedPanel totalBox = new UiKit.RoundedPanel(22, UiTheme.SURFACE_WARM, 0, false);
        totalBox.setLayout(new GridLayout(2, 1, 0, 4));
        totalBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER, 1),
                new EmptyBorder(18, 20, 18, 20)));
        totalBox.add(UiKit.label("Amount paid", UiTheme.FONT_CAPTION, UiTheme.TEXT_MUTED, SwingConstants.CENTER));
        totalBox.add(UiKit.label(MONEY.format(total) + " LE",
                UiTheme.FONT_RECEIPT_TOTAL, UiTheme.PRIMARY_DEEP, SwingConstants.CENTER));

        JButton done = UiKit.primaryButton("Continue shopping", dialog::dispose);
        done.setPreferredSize(new Dimension(280, 50));

        footer.add(totalBox, BorderLayout.NORTH);
        footer.add(done, BorderLayout.SOUTH);
        return footer;
    }

    private static JPanel buildSimpleContent(JDialog dialog, String title, String message, DialogType type) {
        JPanel root = backdropPanel();
        root.setBorder(new EmptyBorder(24, 24, 24, 24));

        UiKit.RoundedPanel card = new UiKit.RoundedPanel(24, UiTheme.CARD, 10, true);
        card.setLayout(new BorderLayout(0, 20));
        card.setBorder(new EmptyBorder(28, 28, 28, 28));

        Color accent = type == DialogType.ERROR ? UiTheme.ERROR
                : type == DialogType.WARNING ? UiTheme.WARNING : UiTheme.SUCCESS;
        String emoji = type == DialogType.ERROR ? "\u26A0" : "\uD83D\uDED2";

        JLabel icon = UiKit.label(emoji, new Font("Segoe UI Emoji", Font.PLAIN, 36), accent, SwingConstants.CENTER);
        JLabel titleLabel = UiKit.label(title, UiTheme.FONT_HEADING, UiTheme.TEXT_DARK, SwingConstants.CENTER);
        JLabel msgLabel = UiKit.label("<html><center>" + escapeHtml(message) + "</center></html>",
                UiTheme.FONT_BODY, UiTheme.TEXT_BODY, SwingConstants.CENTER);

        JPanel center = UiKit.transparent(new GridLayout(3, 1, 0, 10));
        center.add(icon);
        center.add(titleLabel);
        center.add(msgLabel);

        JButton ok = UiKit.primaryButton("OK", dialog::dispose);
        ok.setPreferredSize(new Dimension(200, 46));

        card.add(center, BorderLayout.CENTER);
        card.add(ok, BorderLayout.SOUTH);
        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;");
    }

    private static JPanel backdropPanel() {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(5, 14, 8, 175));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
    }
}
