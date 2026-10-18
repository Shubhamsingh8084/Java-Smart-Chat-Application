package server;

import java.net.ServerSocket;
import java.net.Socket;

import database.DatabaseConnection;

public class Server {

        private static final int PORT = 5000;

        public static void main(String[] args) {

                System.out.println(
                                "=================================");

                System.out.println(
                                "       JAVA CHAT SERVER");

                System.out.println(
                                "=================================");

                // Test database
                DatabaseConnection.testConnection();

                ChatManager chatManager = new ChatManager();

                try (
                                ServerSocket serverSocket = new ServerSocket(PORT)) {

                        System.out.println(
                                        "Chat Server started!");

                        System.out.println(
                                        "Waiting for clients on port "
                                                        + PORT);

                        while (true) {

                                Socket socket = serverSocket.accept();

                                System.out.println(
                                                "New client connected: "
                                                                + socket.getInetAddress());

                                ClientHandler clientHandler = new ClientHandler(
                                                socket,
                                                chatManager);

                                Thread thread = new Thread(clientHandler);

                                thread.start();
                        }

                } catch (Exception e) {

                        System.out.println(
                                        "Server error: "
                                                        + e.getMessage());
                }
        }
}