package com.apinayami.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IChatBotService {
    String askChatBot(String id, String question) throws JsonProcessingException;
}
