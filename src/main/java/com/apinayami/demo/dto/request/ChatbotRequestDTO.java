package com.apinayami.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatbotRequestDTO {
    @NotBlank(message = "User message must not be blank")
    private String message;

    private String conversationId;
}
