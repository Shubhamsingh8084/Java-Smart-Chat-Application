package server;

import model.Message;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;

import database.DatabaseConnection;

public class ClientHandler implements Runnable {

    private Socket socket;

    private ChatManager chatManager;

    private BufferedReader reader;

    private PrintWriter writer;

    private String username;

    public ClientHandler(
            Socket socket,
            ChatManager chatManager) {

        this.socket = socket;
        this.chatManager = chatManager;
    }

    @Override
    public void run() {

        try {

            reader = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            writer = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            // Receive username
            username = reader.readLine();

            if (username == null ||
                    username.trim().isEmpty()) {

                closeConnection();
                return;
            }

            username = username.trim();

            // Check duplicate username
            if (!chatManager.addClient(
                    username,
                    this)) {

                sendMessage(
                        "ERROR: Username already exists."
                );

                closeConnection();
                return;
            }

            // Save user in database
            saveUserToDatabase(username);

            // Welcome message
            sendMessage(
                    "Welcome " + username + "!"
            );

            sendMessage(
                    "Type /users to see online users."
            );

            // Inform everyone
            chatManager.broadcast(
                    "[Server] " + username +
                    " joined the chat."
            );

            // Read messages
            String message;

            while ((message = reader.readLine()) != null) {

                message = message.trim();

                if (message.isEmpty()) {
                    continue;
                }

                // Commands
                if (message.equalsIgnoreCase("/users")) {

                    sendMessage(
                            "Online users: "
                                    + chatManager.getOnlineUsers()
                    );

                } else if (message.equalsIgnoreCase("/quit")) {

                    break;

                } else if (message.startsWith("/msg ")) {

                    handlePrivateMessage(message);

                } else {

                    handleNormalMessage(message);
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Connection error with "
                            + username
                            + ": "
                            + e.getMessage()
            );

        } finally {

            disconnect();
        }
    }

    private void handleNormalMessage(String text) {

        Message message =
                new Message(username, text);

        // Save message to database
        saveMessageToDatabase(message);

        // Send to everyone
        chatManager.broadcast(
                message.toString()
        );
    }

    private void handlePrivateMessage(String text) {

        try {

            String data = text.substring(5);

            int spaceIndex = data.indexOf(" ");

            if (spaceIndex == -1) {

                sendMessage(
                        "Usage: /msg username message"
                );

                return;
            }

            String receiver =
                    data.substring(0, spaceIndex);

            String message =
                    data.substring(spaceIndex + 1);

            chatManager.sendPrivateMessage(
                    username,
                    receiver,
                    message
            );

        } catch (Exception e) {

            sendMessage(
                    "Usage: /msg username message"
            );
        }
    }

    private void saveUserToDatabase(
            String username) {

        String sql =
                "INSERT IGNORE INTO users(username) VALUES(?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);

            statement.executeUpdate();

        } catch (Exception e) {

            System.out.println(
                    "Could not save user: "
                            + e.getMessage()
            );
        }
    }

    private void saveMessageToDatabase(
            Message message) {

        String sql =
                "INSERT INTO messages(username, message) VALUES(?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    message.getSender()
            );

            statement.setString(
                    2,
                    message.getContent()
            );

            statement.executeUpdate();

        } catch (Exception e) {

            System.out.println(
                    "Could not save message: "
                            + e.getMessage()
            );
        }
    }

    public synchronized void sendMessage(
            String message) {

        if (writer != null) {

            writer.println(message);

            writer.flush();
        }
    }

    private void disconnect() {

        if (username != null) {

            chatManager.removeClient(username);

            chatManager.broadcast(
                    "[Server] " + username +
                    " left the chat."
            );

            System.out.println(
                    username + " disconnected."
            );
        }

        closeConnection();
    }

    private void closeConnection() {

        try {

            if (reader != null) {
                reader.close();
            }

            if (writer != null) {
                writer.close();
            }

            if (socket != null &&
                    !socket.isClosed()) {

                socket.close();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error closing connection."
            );
        }
    }
}