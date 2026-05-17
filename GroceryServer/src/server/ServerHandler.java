package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles all communication for a single connected client.
 */
public class ServerHandler implements Runnable {
    private static final String VALID_USERNAME = "Youssef_Ayman";
    private static final String VALID_PASSWORD = "Youssef_33263053";

    private static final Map<String, Double> PRICES = new LinkedHashMap<String, Double>();

    static {
        PRICES.put("Apples", 20.0);
        PRICES.put("Banana", 30.0);
        PRICES.put("Oranges", 10.0);
        PRICES.put("Tomatoes", 15.0);
        PRICES.put("Potatoes", 8.0);
        PRICES.put("Grapes", 45.0);
    }

    private final Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (Socket client = socket;
             DataInputStream input = new DataInputStream(client.getInputStream());
             DataOutputStream output = new DataOutputStream(client.getOutputStream())) {

            boolean loggedIn = false;

            while (true) {
                String message = input.readUTF();
                System.out.println("Received from client: " + message);

                if ("EXIT".equalsIgnoreCase(message)) {
                    System.out.println("Client requested exit.");
                    break;
                }

                if (!loggedIn) {
                    loggedIn = handleLogin(message, output);
                    continue;
                }

                if (message.startsWith("CHECKOUT:")) {
                    double total = calculateTotal(message.substring("CHECKOUT:".length()));
                    output.writeUTF("TOTAL:" + total);
                    output.flush();
                } else {
                    output.writeUTF("ERROR:Unknown request");
                    output.flush();
                }
            }
        } catch (IOException e) {
            System.err.println("Client connection closed: " + e.getMessage());
        }
    }

    private boolean handleLogin(String credentials, DataOutputStream output) throws IOException {
        String[] parts = credentials.split(":", 2);
        boolean success = parts.length == 2
                && VALID_USERNAME.equals(parts[0])
                && VALID_PASSWORD.equals(parts[1]);

        output.writeUTF(success ? "LOGIN_SUCCESS" : "LOGIN_FAILED");
        output.flush();
        return success;
    }

    private double calculateTotal(String orderText) {
        double total = 0.0;

        if (orderText.trim().isEmpty()) {
            return total;
        }

        String[] items = orderText.split(",");
        for (String itemEntry : items) {
            String[] parts = itemEntry.split("=", 2);
            if (parts.length != 2) {
                continue;
            }

            String itemName = parts[0].trim();
            int quantity = parseQuantity(parts[1].trim());
            Double price = PRICES.get(itemName);

            if (price != null && quantity > 0) {
                total += price * quantity;
            }
        }

        return total;
    }

    private int parseQuantity(String quantityText) {
        try {
            return Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
