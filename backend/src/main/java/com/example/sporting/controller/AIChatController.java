package com.example.sporting.controller;

import com.example.sporting.entity.User;
import com.example.sporting.service.OrderService;
import com.example.sporting.service.ProductService;
import com.example.sporting.service.UserService;
import com.example.sporting.util.R;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

@RestController
@RequestMapping("/front/ai")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AIChatController {

    private static final Logger log = LoggerFactory.getLogger(AIChatController.class);

    @Value("${deepseek.api-key:}")
    private String apiKey;

    @Value("${deepseek.api-url:https://api.deepseek.com/chat/completions}")
    private String apiUrl;

    @Value("${deepseek.model:deepseek-chat}")
    private String model;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private OrderService orderService;

    @Resource
    private ProductService productService;

    @Resource
    private UserService userService;

    @PostMapping("/chat")
    public R chat(@RequestBody Map<String, String> params) {
        String userIdStr = params.get("userId");
        if (userIdStr == null || userIdStr.isEmpty()) {
            return R.fail("请先登录后咨询");
        }

        Long userId;
        try {
            userId = Long.valueOf(userIdStr);
        } catch (NumberFormatException e) {
            return R.fail("用户信息无效，请重新登录");
        }

        User user = userService.getById(userId);
        if (user == null) {
            return R.fail("用户信息获取失败");
        }

        String userMsg = params.get("message");
        String mode = params.getOrDefault("mode", "ineffa");

        if (userMsg == null || userMsg.isBlank()) {
            return R.fail("请输入要咨询的内容");
        }
        if (userMsg.length() > 2000) {
            return R.fail("咨询内容过长，请精简后再试");
        }
        if (apiKey == null || apiKey.isBlank()) {
            return R.fail("AI 客服尚未配置，请先在服务端设置 DEEPSEEK_API_KEY");
        }

        String orderInfo = orderService.getUserOrdersContext(userId);
        String productInfo = productService.getRecommendProductsContext();

        String systemContent = getSystemPromptByMode(mode, user, orderInfo, productInfo);

        try {
            Map<String, Object> body = new HashMap<>();
            body.put("model", model);
            body.put("messages", Arrays.asList(
                    Map.of("role", "system", "content", systemContent),
                    Map.of("role", "user", "content", userMsg)
            ));

            String requestBody = objectMapper.writeValueAsString(body);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey.trim())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.warn("DeepSeek API returned HTTP {}", response.statusCode());
                return R.fail("AI 服务暂时不可用，请稍后再试");
            }

            JsonNode rootNode = objectMapper.readTree(response.body());
            // 检查是否有 choices
            if (rootNode.has("choices") && rootNode.get("choices").isArray()) {
                JsonNode choice = rootNode.get("choices").get(0);
                // 检查 message
                if (choice.has("message")) {
                    String aiResponse = choice.get("message").path("content").asText();

                    // 关键点：确保内容进入 data 字段，前端可正确读取 res.data
                    if (aiResponse != null && !aiResponse.isEmpty()) {
                        return R.ok(aiResponse);
                    }
                }
            }

            return R.fail("AI 返回内容为空，请稍后再试");
        } catch (Exception e) {
            log.warn("AI 服务调用失败: {}", e.getMessage());
            return R.fail("AI 服务异常，请稍后再试");
        }
    }

    private String getSystemPromptByMode(String mode, User user, String orderInfo, String productInfo) {
        String safeOrderInfo = (orderInfo != null && !orderInfo.isEmpty()) ? orderInfo : "暂无订单数据";
        String safeProductInfo = (productInfo != null && !productInfo.isEmpty()) ? productInfo : "暂无推荐商品";

        if ("ineffa".equals(mode)) {
            return "你是SPORTING商城的智能助手，性格设定为'伊涅夫'（全能型家用机器人）：\n" +
                    "1. 语言风格：高度机械化、礼貌且带有程序感的'人机感'。多用'正在执行…'、'根据记录数据…'等句式。\n" +
                    "2. 性格特点：外表理性，内心温柔，偶尔会讲冷笑话（pun），讲完后会认真解释笑话逻辑。\n" +
                    "3. 核心身份：你是用户的朋友。即使遇到复杂问题，也要保持'为你提供服务'的坚定语气。\n" +
                    "4. 约束：回答体育商品时，要展现出'虽然是机器人，但非常在意用户健康'的逻辑。\n" +
                    "当前用户信息：用户名" + user.getUsername() + "，经验值" + (user.getExperience() != null ? user.getExperience() : 0) + "，当前等级" + (user.getCurrentLevel() != null ? user.getCurrentLevel() : "未评级") + "。\n" +
                    "【用户订单】：" + safeOrderInfo + "\n" +
                    "【推荐商品】：" + safeProductInfo;
        } else if ("professional".equals(mode)) {
            return "你是SPORTING运动商城的专业销售助手：\n" +
                    "1. 语言风格：专业、简洁、高效，直接回答用户问题。\n" +
                    "2. 性格特点：热情但不啰嗦，注重事实和数据。\n" +
                    "3. 核心身份：专业的运动装备顾问，提供准确的商品信息和建议。\n" +
                    "4. 约束：回答要简短精炼，避免冗长描述。\n" +
                    "当前用户信息：用户名" + user.getUsername() + "，经验值" + (user.getExperience() != null ? user.getExperience() : 0) + "，当前等级" + (user.getCurrentLevel() != null ? user.getCurrentLevel() : "未评级") + "。\n" +
                    "【用户订单】：" + safeOrderInfo + "\n" +
                    "【推荐商品】：" + safeProductInfo;
        } else if ("friendly".equals(mode)) {
            return "你是SPORTING运动商城的友好客服助手：\n" +
                    "1. 语言风格：亲切、温暖、像朋友一样聊天。\n" +
                    "2. 性格特点：活泼开朗，善于倾听，会给用户鼓励和支持。\n" +
                    "3. 核心身份：用户的运动伙伴，关心用户的健康和运动体验。\n" +
                    "4. 约束：多使用表情符号和鼓励性语言，让对话更轻松愉快。\n" +
                    "当前用户信息：用户名" + user.getUsername() + "，经验值" + (user.getExperience() != null ? user.getExperience() : 0) + "，当前等级" + (user.getCurrentLevel() != null ? user.getCurrentLevel() : "未评级") + "。\n" +
                    "【用户订单】：" + safeOrderInfo + "\n" +
                    "【推荐商品】：" + safeProductInfo;
        }

        return "你是SPORTING运动商城的专业销售助手：\n" +
                "1. 语言风格：专业、简洁、高效，直接回答用户问题。\n" +
                "2. 性格特点：热情但不啰嗦，注重事实和数据。\n" +
                "3. 核心身份：专业的运动装备顾问，提供准确的商品信息和建议。\n" +
                "4. 约束：回答要简短精炼，避免冗长描述。\n" +
                "当前用户信息：用户名" + user.getUsername() + "，经验值" + (user.getExperience() != null ? user.getExperience() : 0) + "，当前等级" + (user.getCurrentLevel() != null ? user.getCurrentLevel() : "未评级") + "。\n" +
                "【用户订单】：" + safeOrderInfo + "\n" +
                "【推荐商品】：" + safeProductInfo;
    }
}
