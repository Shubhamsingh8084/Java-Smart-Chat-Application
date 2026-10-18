package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

        private static final String SERVER_ADDRESS = "localhost";

        private static final int SERVER_PORT = 5000;

        public static void main(String[] args) {

                System.out.println(
                                "=================================");

                System.out.println(
                                "        JAVA CHAT CLIENT");

                System.out.println(
                                "=================================");

                try (
                                Socket socket = new Socket(
                                                SERVER_ADDRESS,
                                                SERVER_PORT);

                                BufferedReader reader = new BufferedReader(
                                                new InputStreamReader(
                                                                socket.getInputStream()));

                                PrintWriter writer = new PrintWriter(
                                                socket.getOutputStream(),
                                                true);

                                Scanner scanner = new Scanner(System.in)) {

                        System.out.println(
                                        "Connected to Chat Server!");

                        System.out.println(
                                        "Enter your username:");

                        String username = scanner.nextLine();

                        username = username.trim();

                        if (username.isEmpty()) {

                                System.out.println(
                                                "Username cannot be empty.");

                                return;
                        }

                        // Send username to server
                        writer.println(username);

                        writer.flush();

                        // Start receiver thread
                        Thread receiverThread = new Thread(
                                        new MessageReceiver(reader));

                        receiverThread.setDaemon(true);

                        receiverThread.start();

                        System.out.println(
                                        "You can start chatting now!");

                        System.out.println(
                                        "Commands:");

                        System.out.println(
                                        "/users - Show online users");

                        System.out.println(
                                        "/msg username message - Private message");

                        System.out.println(
                                        "/quit - Exit chat");

                        System.out.println(
                                        "---------------------------------");

                        // Send messages
                        while (true) {

                                String message = scanner.nextLine();

                                if (message.trim().isEmpty()) {
                                        continue;
                                }

                                writer.println(message);

                                writer.flush();

                                if (message.equalsIgnoreCase("/quit")) {

                                        break;
                                }
                        }

                        System.out.println(
                                        "Disconnected from chat.");

                } catch (IOException e) {

                        System.out.println(
                                        "Could not connect to server.");

                        System.out.println(
                                        "Make sure Server is running.");

                        System.out.println(
                                        "Error: " + e.getMessage());
                }
        }
}