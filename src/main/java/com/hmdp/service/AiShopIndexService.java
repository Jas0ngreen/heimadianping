package com.hmdp.service;

import com.hmdp.dto.AiShopIndexResult;

/**
 * AI 商铺索引服务。
 */
public interface AiShopIndexService {

    /**
     * 重建所有商铺的向量索引。
     *
     * @return 索引构建结果
     */
    AiShopIndexResult rebuildShopVectorIndex();
}
