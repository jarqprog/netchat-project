package com.codecool.networking.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public Message(String content, String author) {
        this.content = content;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        String dateTimePattern = "YYYY-MM-dd HH:mm:ss";
        return String.format(" ### %s [%s]: %s", author,
                createdAt.format(DateTimeFormatter.ofPattern(dateTimePattern)), content);
    }
}
