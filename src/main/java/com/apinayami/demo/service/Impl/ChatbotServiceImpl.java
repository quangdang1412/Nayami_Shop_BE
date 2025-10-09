package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.ChatbotRequestDTO;
import com.apinayami.demo.dto.request.ProductDTO;
import com.apinayami.demo.dto.response.ChatbotResponseDTO;
import com.apinayami.demo.dto.response.SuggestedProductDTO;
import com.apinayami.demo.service.IChatbotService;
import com.apinayami.demo.service.IProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotServiceImpl implements IChatbotService {

    private final ChatClient chatClient;
    private final IProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ChatbotResponseDTO getProductSuggestions(ChatbotRequestDTO request) {
        try {
            List<ProductDTO> allProducts = productService.getAllProductDisplayStatusTrue();

            String productContext = buildProductContext(allProducts);

            String systemPrompt = buildSystemPrompt(productContext);

            String aiResponse = callAI(systemPrompt, request.getMessage());

            ChatbotResponseDTO response = parseAIResponse(aiResponse, allProducts);

            // Tạo conversation ID nếu chưa có
            if (request.getConversationId() == null || request.getConversationId().isEmpty()) {
                response.setConversationId(UUID.randomUUID().toString());
            } else {
                response.setConversationId(request.getConversationId());
            }

            return response;

        } catch (Exception e) {
            log.error("Error getting product suggestions: ", e);
            return ChatbotResponseDTO.builder()
                    .message("Xin lỗi, tôi gặp sự cố khi xử lý yêu cầu của bạn. Vui lòng thử lại sau.")
                    .suggestedProducts(new ArrayList<>())
                    .conversationId(request.getConversationId())
                    .build();
        }
    }

    private String buildProductContext(List<ProductDTO> products) {
        StringBuilder context = new StringBuilder();
        context.append("Danh sách sản phẩm hiện có:\n\n");

        for (ProductDTO product : products) {
            context.append(String.format(
                    "- ID: %d\n" +
                            "  Tên: %s\n" +
                            "  Mô tả: %s\n" +
                            "  Giá gốc: %.0f VNĐ\n" +
                            "  Giá bán: %.0f VNĐ\n" +
                            "  Danh mục: %s\n" +
                            "  Thương hiệu: %s\n" +
                            "  Đánh giá: %d/5 sao\n" +
                            "  Số lượng: %d\n\n",
                    product.getId(),
                    product.getName(),
                    product.getDescription() != null
                            ? product.getDescription().substring(0, Math.min(150, product.getDescription().length()))
                            : "N/A",
                    product.getOriginalPrice(),
                    product.getUnitPrice(),
                    product.getCategoryDTO() != null ? product.getCategoryDTO().getCategoryName() : "N/A",
                    product.getBrandDTO() != null ? product.getBrandDTO().getName() : "N/A",
                    product.getRatingAvg(),
                    product.getQuantity()));
        }

        return context.toString();
    }

    private String buildSystemPrompt(String productContext) {
        return String.format(
                "Bạn là một trợ lý AI thông minh của cửa hàng Nayami Shop, chuyên tư vấn và gợi ý sản phẩm cho khách hàng.\n\n"
                        +
                        "THÔNG TIN SẢN PHẨM:\n%s\n\n" +
                        "NHIỆM VỤ CỦA BẠN:\n" +
                        "1. Phân tích yêu cầu/nhu cầu của khách hàng một cách chi tiết\n" +
                        "2. Dựa trên danh sách sản phẩm trên, gợi ý 3-5 sản phẩm phù hợp nhất\n" +
                        "3. Giải thích tại sao những sản phẩm này phù hợp với nhu cầu của khách hàng\n\n" +
                        "ĐỊNH DẠNG TRỢ LÝ:\n" +
                        "Trả lời theo format JSON như sau:\n" +
                        "{\n" +
                        "  \"message\": \"Lời tư vấn thân thiện và chi tiết của bạn\",\n" +
                        "  \"productIds\": [id1, id2, id3]\n" +
                        "}\n\n" +
                        "LƯU Ý:\n" +
                        "- Chỉ gợi ý sản phẩm CÓ TRONG danh sách trên\n" +
                        "- Ưu tiên sản phẩm còn hàng (quantity > 0)\n" +
                        "- Xem xét đánh giá và giá cả hợp lý\n" +
                        "- Trả lời bằng tiếng Việt thân thiện, nhiệt tình\n" +
                        "- Nếu khách hàng hỏi về thông tin cửa hàng hoặc chính sách, hãy trả lời một cách hữu ích\n" +
                        "- Nếu không tìm thấy sản phẩm phù hợp, hãy đề xuất sản phẩm tương tự hoặc hỏi thêm thông tin",
                productContext);
    }

    private String callAI(String systemPrompt, String userMessage) {
        SystemMessage systemMessage = new SystemMessage(systemPrompt);
        UserMessage userMsg = new UserMessage(userMessage);

        Prompt prompt = new Prompt(List.of(systemMessage, userMsg));

        return chatClient.prompt(prompt)
                .call()
                .content();
    }

    private ChatbotResponseDTO parseAIResponse(String aiResponse, List<ProductDTO> allProducts) {
        try {
            String jsonResponse = extractJSON(aiResponse);

            Map<String, Object> responseMap = objectMapper.readValue(
                    jsonResponse,
                    new TypeReference<Map<String, Object>>() {
                    });

            String message = (String) responseMap.get("message");

            @SuppressWarnings("unchecked")
            List<Integer> productIds = (List<Integer>) responseMap.get("productIds");


            List<SuggestedProductDTO> suggestedProducts = new ArrayList<>();
            if (productIds != null) {
                suggestedProducts = allProducts.stream()
                        .filter(p -> productIds.contains((int) p.getId()))
                        .map(p -> SuggestedProductDTO.builder()
                                .id(p.getId())
                                .name(p.getName())
                                .unitPrice(p.getUnitPrice())
                                .image(p.getListImage() != null && !p.getListImage().isEmpty()
                                        ? p.getListImage().get(0)
                                        : null)
                                .link("http://localhost:5173/product-detail/" + p.getId())
                                .build())
                        .collect(Collectors.toList());
            }

            return ChatbotResponseDTO.builder()
                    .message(message)
                    .suggestedProducts(suggestedProducts)
                    .build();

        } catch (Exception e) {
            log.error("Error parsing AI response: ", e);

            List<SuggestedProductDTO> topProducts = allProducts.stream()
                    .filter(p -> p.getQuantity() > 0)
                    .sorted(Comparator.comparingInt(ProductDTO::getRatingAvg).reversed())
                    .limit(3)
                    .map(p -> SuggestedProductDTO.builder()
                            .id(p.getId())
                            .name(p.getName())
                            .unitPrice(p.getUnitPrice())
                            .image(p.getListImage() != null && !p.getListImage().isEmpty()
                                    ? p.getListImage().get(0)
                                    : null)
                            .link("http://localhost:5173/product-detail/" + p.getId())
                            .build())
                    .collect(Collectors.toList());

            return ChatbotResponseDTO.builder()
                    .message(aiResponse)
                    .suggestedProducts(topProducts)
                    .build();
        }
    }

    private String extractJSON(String text) {
        int startIndex = text.indexOf("{");
        int endIndex = text.lastIndexOf("}");

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return text.substring(startIndex, endIndex + 1);
        }

        throw new IllegalArgumentException("No JSON found in response");
    }
}
