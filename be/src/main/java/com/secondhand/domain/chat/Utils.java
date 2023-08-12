package com.secondhand.domain.chat;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Utils() {
    }

    public static ChatMessage getObject(final String message) throws Exception {
        return objectMapper.readValue(message, ChatMessage.class);
    }

    public static String getString(final ChatMessage message) throws Exception {
        return objectMapper.writeValueAsString(message);
    }
}
