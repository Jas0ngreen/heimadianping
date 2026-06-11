package com.hmdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.dto.AiShopIndexResult;
import com.hmdp.entity.Shop;
import com.hmdp.entity.ShopVector;
import com.hmdp.mapper.ShopVectorMapper;
import com.hmdp.service.AiShopIndexService;
import com.hmdp.service.EmbeddingService;
import com.hmdp.service.IShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * AI 商铺索引服务实现。
 */
@Slf4j
@Service
public class AiShopIndexServiceImpl implements AiShopIndexService {

    @Resource
    private IShopService shopService;

    @Resource
    private ShopVectorMapper shopVectorMapper;

    @Resource
    private EmbeddingService embeddingService;

    @Resource
    private ShopVectorContentBuilder shopVectorContentBuilder;

    @Override
    public AiShopIndexResult rebuildShopVectorIndex() {
        List<Shop> shops = shopService.list();
        int success = 0;
        int failed = 0;

        for (Shop shop : shops) {
            try {
                rebuildOne(shop);
                success++;
            } catch (Exception e) {
                failed++;
                log.warn("重建商铺向量索引失败，shopId={}", shop.getId(), e);
            }
        }

        return new AiShopIndexResult()
                .setTotal(shops.size())
                .setSuccess(success)
                .setFailed(failed);
    }

    private void rebuildOne(Shop shop) {
        String content = shopVectorContentBuilder.build(shop);
        List<Float> embedding = embeddingService.embed(content);

        ShopVector shopVector = shopVectorMapper.selectOne(
                new QueryWrapper<ShopVector>().eq("shop_id", shop.getId())
        );
        if (shopVector == null) {
            shopVector = new ShopVector();
            shopVector.setShopId(shop.getId());
        }

        shopVector.setContent(content);
        shopVector.setEmbeddingJson(JSONUtil.toJsonStr(embedding));
        shopVector.setEmbeddingModel(embeddingService.modelName());
        shopVector.setVectorDimension(embedding.size());

        if (shopVector.getId() == null) {
            shopVectorMapper.insert(shopVector);
        } else {
            shopVectorMapper.updateById(shopVector);
        }
    }
}
