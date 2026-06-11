package com.hmdp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * AI 解析出的商铺搜索意图。
 */
@Data
@NoArgsConstructor
public class AiSearchIntent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 品类偏好，例如火锅、西餐、KTV
     */
    private String category;

    /**
     * 消费场景，例如约会、聚餐、亲子
     */
    private String scene;

    /**
     * 商圈或位置偏好
     */
    private String area;

    /**
     * 最高人均预算
     */
    private Long maxPrice;

    /**
     * 用户额外偏好关键词
     */
    private List<String> keywords;
}
