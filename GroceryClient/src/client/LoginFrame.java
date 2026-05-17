package client;

import client.ui.CheckoutDialog;
import client.ui.UiKit;
import client.ui.UiTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

    private final JTextField usernameField = new JTextField("Username");
    private final JPasswordField passwordField = new JPasswordField("Password");
    private final JLabel statusLabel = UiKit.label(
            "Sign in to browse fresh produce",
            UiTheme.FONT_BODY_BOLD,
            UiTheme.TEXT_MUTED,
            SwingConstants.CENTER);

    public LoginFrame() {
        setTitle("Fresh Basket - Sign In");
        setSize(940, 620);
        setMinimumSize(new Dimension(940, 620));
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(createContentPanel());
    }

    private JPanel createContentPanel() {
        JPanel background = UiKit.decorativeBackdrop(new BorderLayout());
        background.setBorder(new EmptyBorder(36, 48, 36, 48));

        UiKit.RoundedPanel card = new UiKit.RoundedPanel(36, UiTheme.CARD, 16, true);
        card.setLayout(new GridLayout(1, 2, 0, 0));
        card.add(createHeroPanel());
        card.add(createFormPanel());

        background.add(card, BorderLayout.CENTER);
        return background;
    }

    private JPanel createHeroPanel() {
        JPanel hero = UiKit.gradientCard(new BorderLayout(), UiTheme.PRIMARY_DEEP, UiTheme.ROSE);
        hero.setBorder(new EmptyBorder(48, 40, 48, 40));

        JLabel iconLabel = UiKit.label("\uD83C\uDF4E\uD83C\uDF4C\uD83E\uDD6C",
                UiTheme.FONT_EMOJI_HERO, Color.WHITE, SwingConstants.CENTER);

        JLabel titleLabel = UiKit.label(
                "<html><center>Fresh Basket<br><span style='font-size:17px;font-weight:normal'>Market</span></center></html>",
                UiTheme.FONT_DISPLAY,
                Color.WHITE,
                SwingConstants.CENTER);

        JLabel subtitleLabel = UiKit.label(
                "<html><center style='color:#FFEDD5'>A warm, beautiful place to shop<br>for fruit and vegetables.</center></html>",
                UiTheme.FONT_BODY,
                UiTheme.TEXT_CREAM,
                SwingConstants.CENTER);

        JPanel features = UiKit.rounded(UiTheme.HIGHLIGHT, 24);
        features.setLayout(new GridLayout(3, 1, 0, 12));
        features.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 140), 1),
                new EmptyBorder(20, 22, 20, 22)));
        features.add(featureRow("Curated produce"));
        features.add(featureRow("Live basket totals"));
        features.add(featureRow("Quick checkout"));

        JPanel stack = UiKit.transparent(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 16, 0);
        gbc.gridy = 0;
        stack.add(iconLabel, gbc);
        gbc.gridy = 1;
        stack.add(titleLabel, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 28, 0);
        stack.add(subtitleLabel, gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 8, 0, 8);
        stack.add(features, gbc);

        hero.add(stack, BorderLayout.CENTER);
        return hero;
    }

    private JLabel featureRow(String text) {
        return UiKit.label("  +  " + text,
                UiTheme.FONT_BODY_BOLD, UiTheme.PRIMARY_DEEP, SwingConstants.LEFT);
    }

    private JPanel createFormPanel() {
        JPanel panel = UiKit.transparent(new GridBagLayout());
        panel.setBorder(new EmptyBorder(52, 52, 52, 52));

        UiKit.styleInput(usernameField);
        UiKit.stylePassword(passwordField);
        usernameField.setForeground(UiTheme.TEXT_MUTED);
        passwordField.setForeground(UiTheme.TEXT_MUTED);
        passwordField.setEchoChar((char) 0);
        addPlaceholderBehavior(usernameField, "Username");
        addPasswordPlaceholderBehavior(passwordField, "Password");

        JLabel headingLabel = UiKit.label("Welcome back",
                UiTheme.FONT_DISPLAY, UiTheme.TEXT_DARK, SwingConstants.LEFT);
        JLabel helpLabel = UiKit.label(
                "Enter your credentials to open the market.",
                UiTheme.FONT_BODY, UiTheme.TEXT_MUTED, SwingConstants.LEFT);

        javax.swing.JButton loginButton = UiKit.primaryButton("Sign in to shop", this::attemptLogin);
        loginButton.setPreferredSize(new Dimension(360, 54));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        panel.add(headingLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 32, 0);
        panel.add(helpLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(UiKit.fieldShell("Username", usernameField), gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 28, 0);
        panel.add(UiKit.fieldShell("Password", passwordField), gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 16, 0);
        panel.add(loginButton, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(statusLabel, gbc);

        return panel;
    }

    private void addPlaceholderBehavior(JTextField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (placeholder.equals(field.getText())) {
                    field.setText("");
                    field.setForeground(UiTheme.TEXT_DARK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(UiTheme.TEXT_MUTED);
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
                    field.setForeground(UiTheme.TEXT_DARK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(field.getPassword()).trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setEchoChar((char) 0);
                    field.setForeground(UiTheme.TEXT_MUTED);
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
                statusLabel.setForeground(UiTheme.SUCCESS);
                statusLabel.setText("Welcome! Opening the market...");
                dispose();
                new ShopFrame(socket, input, output).setVisible(true);
            } else {
                closeSocket(socket);
                showFailure("Invalid username or password.");
            }
        } catch (ConnectException e) {
            CheckoutDialog.showError(this,
                    "Cannot connect",
                    "The shop is unavailable. Please start the server and try again.");
        } catch (IOException e) {
            CheckoutDialog.showError(this,
                    "Connection problem",
                    "Something went wrong while connecting. Please try again.");
        }
    }

    private void showFailure(String message) {
        statusLabel.setForeground(UiTheme.ERROR);
        statusLabel.setText(message);
        passwordField.setText("");
        passwordField.setEchoChar('\u2022');
        passwordField.setForeground(UiTheme.TEXT_DARK);
        UiKit.shakeWindow(this);
    }

    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new LoginFrame().setVisible(true);
        });
    }
}
