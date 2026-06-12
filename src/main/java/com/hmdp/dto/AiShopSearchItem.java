package com.hmdp.dto;

import com.hmdp.entity.Shop;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * AI 商铺搜索结果项。
 */
@Data
@Accessors(chain = true)
public class AiShopSearchItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商铺信息
     */
    private Shop shop;

    /**
     * 语义相似度
     */
    private Double similarity;

    /**
     * 业务特征得分
     */
    private Double businessScore;

    /**
     * 综合排序得分
     */
    private Double finalScore;

    /**
     * AI 推荐理由
     */
    private String reason;
}
