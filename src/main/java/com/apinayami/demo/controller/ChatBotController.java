package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.ChatBotRequest;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.service.IChatBotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbot")
@Validated
public class ChatBotController {
    private final IChatBotService ChatBotService;

    @PostMapping("/chat")
    public ResponseData<?> getMessage(@RequestBody ChatBotRequest request) throws JsonProcessingException {
        String question = request.getQuestion();
        String userId = request.getUserId();
        String message = ChatBotService.askChatBot(userId, question);
        return new ResponseData<>(HttpStatus.OK.value(), "Response", message);
    }
}
