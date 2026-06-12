package com.hmdp.service;

import com.hmdp.dto.AiShopSearchResult;

/**
 * AI 商铺搜索服务。
 */
public interface AiShopSearchService {

    /**
     * 根据自然语言查询搜索商铺。
     *
     * @param query 用户自然语言查询
     * @param limit 返回数量
     * @return AI 商铺搜索结果
     */
    AiShopSearchResult search(String query, Integer limit);
}
