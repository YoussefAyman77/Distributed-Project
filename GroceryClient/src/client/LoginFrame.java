package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * Polished login window for the grocery shop client.
 */
public class LoginFrame extends JFrame {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1254;

    private static final Color FOREST = new Color(20, 83, 45);
    private static final Color GREEN = new Color(22, 163, 74);
    private static final Color MINT = new Color(220, 252, 231);
    private static final Color CREAM = new Color(255, 251, 235);
    private static final Color FIELD_BORDER = new Color(187, 247, 208);
    private static final Color TEXT_DARK = new Color(31, 41, 55);
    private static final Color TEXT_MUTED = new Color(107, 114, 128);
    private static final Color ERROR_RED = new Color(220, 38, 38);

    private final JTextField usernameField = new JTextField("Username");
    private final JPasswordField passwordField = new JPasswordField("Password");
    private final JLabel statusLabel = new JLabel("Enter your shop credentials to continue");

    public LoginFrame() {
        setTitle("Grocery Shop Login");
        setSize(860, 560);
        setMinimumSize(new Dimension(860, 560));
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(createContentPanel());
    }

    private JPanel createContentPanel() {
        GradientPanel background = new GradientPanel(new BorderLayout(), new Color(236, 253, 245), CREAM);
        background.setBorder(new EmptyBorder(34, 42, 34, 42));

        RoundedPanel card = new RoundedPanel(34, Color.WHITE);
        card.setLayout(new GridLayout(1, 2, 0, 0));
        card.setBorder(new EmptyBorder(0, 0, 0, 0));
        card.add(createHeroPanel());
        card.add(createFormPanel());

        background.add(card, BorderLayout.CENTER);
        return background;
    }

    private JPanel createHeroPanel() {
        GradientPanel hero = new GradientPanel(new BorderLayout(), FOREST, new Color(22, 163, 74));
        hero.setBorder(new EmptyBorder(42, 36, 42, 36));

        JLabel iconLabel = new JLabel("\uD83D\uDED2", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 68));

        JLabel titleLabel = new JLabel("<html><center>Fresh Basket<br>Market</center></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("<html><center>Beautiful groceries, live TCP checkout,<br>and prices calculated by the server.</center></html>", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitleLabel.setForeground(new Color(220, 252, 231));

        RoundedPanel featurePanel = new RoundedPanel(26, new Color(240, 253, 244));
        featurePanel.setLayout(new GridLayout(3, 1, 0, 10));
        featurePanel.setBorder(new EmptyBorder(18, 20, 18, 20));
        featurePanel.add(createFeatureLabel("Fresh fruit cards"));
        featurePanel.add(createFeatureLabel("Persistent socket session"));
        featurePanel.add(createFeatureLabel("Server-side total calculation"));

        JPanel textPanel = new TransparentPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 18, 0);
        gbc.gridy = 0;
        textPanel.add(iconLabel, gbc);
        gbc.gridy = 1;
        textPanel.add(titleLabel, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 28, 0);
        textPanel.add(subtitleLabel, gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 18, 0, 18);
        textPanel.add(featurePanel, gbc);

        hero.add(textPanel, BorderLayout.CENTER);
        return hero;
    }

    private JLabel createFeatureLabel(String text) {
        JLabel label = new JLabel("  " + text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(FOREST);
        return label;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(44, 48, 44, 48));

        styleTextField(usernameField);
        stylePasswordField(passwordField);
        addPlaceholderBehavior(usernameField, "Username");
        addPasswordPlaceholderBehavior(passwordField, "Password");

        JButton loginButton = new RoundedButton("Login");
        loginButton.addActionListener(e -> attemptLogin());

        JLabel headingLabel = new JLabel("Welcome back");
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headingLabel.setForeground(TEXT_DARK);

        JLabel helpLabel = new JLabel("<html>Enter the username and password, then open the grocery shop.</html>");
        helpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        helpLabel.setForeground(TEXT_MUTED);

        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 6, 0);
        panel.add(headingLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(helpLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 18, 0);
        panel.add(createFieldShell("Username", usernameField), gbc);

        gbc.gridy = 3;
        panel.add(createFieldShell("Password", passwordField), gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(24, 0, 14, 0);
        panel.add(loginButton, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(statusLabel, gbc);

        return panel;
    }

    private JPanel createFieldShell(String label, JTextField field) {
        RoundedPanel shell = new RoundedPanel(22, new Color(248, 250, 252));
        shell.setLayout(new BorderLayout(8, 6));
        shell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER, 2),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fieldLabel.setForeground(FOREST);

        shell.add(fieldLabel, BorderLayout.NORTH);
        shell.add(field, BorderLayout.CENTER);
        return shell;
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(340, 42));
        field.setFont(new Font("Segoe UI", Font.BOLD, 18));
        field.setForeground(TEXT_MUTED);
        field.setBackground(Color.WHITE);
        field.setOpaque(true);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                new EmptyBorder(8, 12, 8, 12)));
        field.setCaretColor(FOREST);
    }

    private void stylePasswordField(JPasswordField field) {
        styleTextField(field);
        field.setEchoChar((char) 0);
    }

    private void addPlaceholderBehavior(JTextField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (placeholder.equals(field.getText())) {
                    field.setText("");
                    field.setForeground(TEXT_DARK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(TEXT_MUTED);
                }
            }
        });
    }

    private void addPasswordPlaceholderBehavior(JPasswordField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (placeholder.equals(new String(field.getPassword()))) {
                    field.setText("");
                    field.setEchoChar('\u2022');
                    field.setForeground(TEXT_DARK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(field.getPassword()).trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setEchoChar((char) 0);
                    field.setForeground(TEXT_MUTED);
                }
            }
        });
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if ("Username".equals(username) || "Password".equals(password) || username.isEmpty() || password.isEmpty()) {
            showFailure("Please enter username and password.");
            return;
        }

        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            output.writeUTF(username + ":" + password);
            output.flush();

            String response = input.readUTF();
            if ("LOGIN_SUCCESS".equals(response)) {
                statusLabel.setForeground(FOREST);
                statusLabel.setText("Login successful. Opening market...");
                dispose();
                new ShopFrame(socket, input, output).setVisible(true);
            } else {
                closeSocket(socket);
                showFailure("Invalid username or password.");
            }
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not connect to the server. Please run Server.java first.",
                    "Connection Refused",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Connection error: " + e.getMessage(),
                    "Network Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showFailure(String message) {
        statusLabel.setForeground(ERROR_RED);
        statusLabel.setText(message);
        passwordField.setText("");
        passwordField.setEchoChar('\u2022');
        passwordField.setForeground(TEXT_DARK);
        shakeWindow();
    }

    private void shakeWindow() {
        Point originalLocation = getLocation();
        for (int i = 0; i < 8; i++) {
            int offset = (i % 2 == 0) ? 8 : -8;
            setLocation(originalLocation.x + offset, originalLocation.y);
            try {
                Thread.sleep(18);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        setLocation(originalLocation);
    }

    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException ignored) {
            // Nothing else to do when closing a failed login connection.
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Use default Swing look and feel if the system theme is unavailable.
            }
            new LoginFrame().setVisible(true);
        });
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
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 34, 34);
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
            setPreferredSize(new Dimension(360, 54));
            setBackground(FOREST);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 17));
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
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
