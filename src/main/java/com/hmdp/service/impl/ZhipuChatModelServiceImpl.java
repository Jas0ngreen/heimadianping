package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.AiSearchIntent;
import com.hmdp.service.ChatModelService;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Service;

/**
 * 基于 LangChain4j OpenAI 兼容接口的智谱 Chat 服务。
 */
@Service
public class ZhipuChatModelServiceImpl implements ChatModelService {

    private final ChatModel chatModel;

    public ZhipuChatModelServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public AiSearchIntent parseSearchIntent(String query) {
        if (StrUtil.isBlank(query)) {
            throw new IllegalArgumentException("Search query must not be blank");
        }
        String prompt = buildIntentPrompt(query);
        String answer = chatModel.chat(prompt);
        String json = extractJsonObject(answer);
        return JSONUtil.toBean(json, AiSearchIntent.class);
    }

    @Override
    public String generateRecommendationReason(String query, String shopContent) {
        if (StrUtil.isBlank(query)) {
            throw new IllegalArgumentException("Search query must not be blank");
        }
        if (StrUtil.isBlank(shopContent)) {
            throw new IllegalArgumentException("Shop content must not be blank");
        }
        String prompt = buildReasonPrompt(query, shopContent);
        return chatModel.chat(prompt);
    }

    private String buildIntentPrompt(String query) {
        return "你是本地生活搜索助手。请从用户搜索语句中提取消费意图，只返回 JSON，不要返回解释。\n"
                + "JSON 字段固定为：category、scene、area、maxPrice、keywords。\n"
                + "无法判断的字段返回 null，keywords 返回字符串数组。\n"
                + "用户搜索语句：" + query;
    }

    private String buildReasonPrompt(String query, String shopContent) {
        return "你是本地生活推荐助手。请根据用户需求和商铺信息，生成一句不超过 60 字的中文推荐理由。\n"
                + "要求：具体、自然、不要编造商铺信息。\n"
                + "用户需求：" + query + "\n"
                + "商铺信息：" + shopContent;
    }

    private String extractJsonObject(String text) {
        if (StrUtil.isBlank(text)) {
            throw new IllegalStateException("Chat model returned blank response");
        }
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start < 0 || end <= start) {
            throw new IllegalStateException("Chat model response does not contain JSON object: " + text);
        }
        return text.substring(start, end + 1);
    }
}
