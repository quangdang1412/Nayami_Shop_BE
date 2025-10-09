package com.apinayami.demo.dto.response;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatbotResponseDTO {
    private String message; 
    private List<SuggestedProductDTO> suggestedProducts; 
    private String conversationId;
}
