package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.ChatbotRequestDTO;
import com.apinayami.demo.dto.response.ChatbotResponseDTO;

public interface IChatbotService {
    ChatbotResponseDTO getProductSuggestions(ChatbotRequestDTO request);
}
