package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    private String sender;
    private String content;
    private LocalDateTime timestamp;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public Message(String sender, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTime() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        return timestamp.format(formatter);
    }

    @Override
    public String toString() {
        return "[" + getFormattedTime() + "] "
                + sender + ": " + content;
    }
}