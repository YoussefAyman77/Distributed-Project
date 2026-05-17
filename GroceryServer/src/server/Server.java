package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Starts the grocery shop TCP server and creates one handler thread per client.
 */
public class Server {
    public static final int PORT = 1254;

    public static void main(String[] args) {
        System.out.println("Grocery server starting on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new Thread(new ServerHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
