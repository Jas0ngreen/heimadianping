package com.hmdp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * AI 商铺搜索结果。
 */
@Data
@Accessors(chain = true)
public class AiShopSearchResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户原始查询
     */
    private String query;

    /**
     * 大模型解析出的搜索意图
     */
    private AiSearchIntent intent;

    /**
     * 推荐商铺列表
     */
    private List<AiShopSearchItem> shops;
}
