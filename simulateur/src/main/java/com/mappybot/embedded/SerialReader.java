package com.mappybot.embedded;

import java.util.ArrayList;
import java.util.List;

public class SerialReader {
    private final List<MessageListener> listeners = new ArrayList<>();
    private String buffer = "";

    public void feedChars(String s) {
        buffer += s;
        if (buffer.contains("_")) {
            int limit = buffer.indexOf("_") + 1;
            listeners.forEach(ml -> ml.newMessage(buffer.substring(0, limit)));
            buffer = buffer.substring(limit);
        }
    }

    public void addMessageListener(MessageListener ml) {
        listeners.add(ml);
    }
}
