package server;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {

    private final Map<String, ClientHandler> clients =
            new ConcurrentHashMap<>();

    public boolean addClient(String username, ClientHandler clientHandler) {

        if (clients.containsKey(username)) {
            return false;
        }

        clients.put(username, clientHandler);

        return true;
    }

    public void removeClient(String username) {

        if (username != null) {
            clients.remove(username);
        }
    }

    public void broadcast(String message) {

        for (ClientHandler client : clients.values()) {

            client.sendMessage(message);
        }
    }

    public void sendPrivateMessage(
            String sender,
            String receiver,
            String message) {

        ClientHandler receiverClient =
                clients.get(receiver);

        if (receiverClient != null) {

            receiverClient.sendMessage(
                    "[Private] " + sender + ": " + message
            );

        } else {

            ClientHandler senderClient =
                    clients.get(sender);

            if (senderClient != null) {

                senderClient.sendMessage(
                        "User '" + receiver + "' is not online."
                );
            }
        }
    }

    public String getOnlineUsers() {

        Set<String> usernames = clients.keySet();

        if (usernames.isEmpty()) {
            return "No users online.";
        }

        return String.join(", ", usernames);
    }

    public int getOnlineUserCount() {

        return clients.size();
    }
}