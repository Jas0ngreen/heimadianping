package com.hmdp.service;

import com.hmdp.dto.AiSearchIntent;

/**
 * 大模型对话能力封装。
 */
public interface ChatModelService {

    /**
     * 解析用户自然语言搜索意图。
     *
     * @param query 用户查询
     * @return 结构化搜索意图
     */
    AiSearchIntent parseSearchIntent(String query);

    /**
     * 为候选商铺生成推荐理由。
     *
     * @param query 用户查询
     * @param shopContent 商铺语义文本
     * @return 推荐理由
     */
    String generateRecommendationReason(String query, String shopContent);
}
