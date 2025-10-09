package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.ChatbotRequestDTO;
import com.apinayami.demo.dto.response.ChatbotResponseDTO;
import com.apinayami.demo.service.IChatbotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatController {
    private final IChatbotService chatbotService;

    @PostMapping("/chat")
    public ResponseEntity<ChatbotResponseDTO> getProductSuggestions(
            @Valid @RequestBody ChatbotRequestDTO request) {
        ChatbotResponseDTO response = chatbotService.getProductSuggestions(request);
        return ResponseEntity.ok(response);
    }

}
