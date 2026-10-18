package client;

import java.io.BufferedReader;
import java.io.IOException;

public class MessageReceiver implements Runnable {

    private BufferedReader reader;

    public MessageReceiver(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {

        try {

            String message;

            while ((message = reader.readLine()) != null) {

                System.out.println(message);
            }

        } catch (IOException e) {

            System.out.println(
                    "Disconnected from server."
            );
        }
    }
}