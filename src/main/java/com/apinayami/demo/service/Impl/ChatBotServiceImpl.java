package com.apinayami.demo.service.Impl;

import com.apinayami.demo.service.IChatBotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatBotServiceImpl implements IChatBotService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String askChatBot(String id, String question) throws JsonProcessingException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userId", id);
        jsonBody.put("msg", question);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonBody.toString(), headers);
        String response = restTemplate.postForObject(
                "http://localhost:5678/webhook/chatbot",
                request,
                String.class
        );
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        return node.get("output").asText();
    }
}
