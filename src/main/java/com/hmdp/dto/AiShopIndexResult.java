package com.hmdp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 商铺向量索引构建结果。
 */
@Data
@Accessors(chain = true)
public class AiShopIndexResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 本次处理的商铺总数
     */
    private Integer total;

    /**
     * 成功构建索引的商铺数
     */
    private Integer success;

    /**
     * 构建失败的商铺数
     */
    private Integer failed;
}
