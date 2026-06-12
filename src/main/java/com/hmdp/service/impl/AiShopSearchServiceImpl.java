package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.dto.AiSearchIntent;
import com.hmdp.dto.AiShopSearchItem;
import com.hmdp.dto.AiShopSearchResult;
import com.hmdp.entity.Shop;
import com.hmdp.entity.ShopVector;
import com.hmdp.mapper.ShopVectorMapper;
import com.hmdp.service.AiShopSearchService;
import com.hmdp.service.ChatModelService;
import com.hmdp.service.EmbeddingService;
import com.hmdp.service.IShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AI 商铺搜索服务实现。
 */
@Service
public class AiShopSearchServiceImpl implements AiShopSearchService {

    private static final int DEFAULT_LIMIT = 5;
    private static final int MAX_LIMIT = 20;

    @Resource
    private EmbeddingService embeddingService;

    @Resource
    private ChatModelService chatModelService;

    @Resource
    private ShopVectorMapper shopVectorMapper;

    @Resource
    private IShopService shopService;

    @Resource
    private AiShopSearchScorer aiShopSearchScorer;

    @Override
    public AiShopSearchResult search(String query, Integer limit) {
        if (StrUtil.isBlank(query)) {
            throw new IllegalArgumentException("Search query must not be blank");
        }
        int size = normalizeLimit(limit);
        AiSearchIntent intent = chatModelService.parseSearchIntent(query); //intent存在的作用是，查询向量只能告诉语义像不像，但不能保证具体，比如“价格<100”，这时候就要靠intent了
        List<Float> queryVector = embeddingService.embed(query); // 这里根据用户的查询语句去获得查询向量，不根据上面的得到的查询意图来embed得到查询向量是因为，原始查询语句比intent内容更丰富不会丢失原始语义信息。
        List<AiShopSearchItem> items = recallAndRank(query, intent, queryVector, size);

        return new AiShopSearchResult()
                .setQuery(query)
                .setIntent(intent)
                .setShops(items);
    }

    private List<AiShopSearchItem> recallAndRank(String query,
                                                 AiSearchIntent intent,
                                                 List<Float> queryVector,
                                                 int limit) {
        List<ShopVector> vectors = shopVectorMapper.selectList(
                new QueryWrapper<ShopVector>().eq("embedding_model", embeddingService.modelName())
        );
        List<AiShopSearchItem> candidates = new ArrayList<>(vectors.size());
        for (ShopVector vector : vectors) {
            Shop shop = shopService.getById(vector.getShopId());
            if (shop == null || !aiShopSearchScorer.matchBusinessFilters(shop, intent)) {
                continue;
            }
            List<Float> shopVector = parseEmbedding(vector.getEmbeddingJson());
            double similarity = aiShopSearchScorer.cosineSimilarity(queryVector, shopVector);
            double businessScore = aiShopSearchScorer.businessScore(shop);
            double finalScore = aiShopSearchScorer.finalScore(similarity, businessScore);

            candidates.add(new AiShopSearchItem()
                    .setShop(shop)
                    .setSimilarity(similarity)
                    .setBusinessScore(businessScore)
                    .setFinalScore(finalScore)
                    .setReason(""));
        }

        return candidates.stream()
                .sorted(Comparator.comparing(AiShopSearchItem::getFinalScore).reversed())
                .limit(limit)
                .map(item -> fillReason(query, item))
                .collect(Collectors.toList());
    }

    private AiShopSearchItem fillReason(String query, AiShopSearchItem item) {
        String shopContent = new ShopVectorContentBuilder().build(item.getShop());
        String reason = chatModelService.generateRecommendationReason(query, shopContent);
        return item.setReason(reason);
    }

    private List<Float> parseEmbedding(String embeddingJson) {
        return JSONUtil.parseArray(embeddingJson).toList(Float.class);
    }

    private int normalizeLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }
}
