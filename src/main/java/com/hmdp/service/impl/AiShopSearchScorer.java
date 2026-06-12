package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.AiSearchIntent;
import com.hmdp.entity.Shop;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * AI 商铺搜索排序评分器。
 */
@Component
public class AiShopSearchScorer {

    public boolean matchBusinessFilters(Shop shop, AiSearchIntent intent) {
        if (shop == null || intent == null) {
            return true;
        }
        if (intent.getMaxPrice() != null && shop.getAvgPrice() != null
                && shop.getAvgPrice() > intent.getMaxPrice()) {
            return false;
        }
        return StrUtil.isBlank(intent.getArea())
                || StrUtil.containsIgnoreCase(shop.getArea(), intent.getArea())
                || StrUtil.containsIgnoreCase(shop.getAddress(), intent.getArea());
    }

    public double finalScore(double similarity, double businessScore) {
        return similarity * 0.75 + businessScore * 0.25;
    }

    // 计算商铺的评分
    public double businessScore(Shop shop) {
        if (shop == null) {
            return 0.0;
        }
        double score = normalize(shop.getScore(), 50.0);
        double sold = logNormalize(shop.getSold(), 10000.0);
        double comments = logNormalize(shop.getComments(), 10000.0);
        return score * 0.65 + sold * 0.2 + comments * 0.15;
    }

    public double cosineSimilarity(List<Float> left, List<Float> right) {
        if (left == null || right == null || left.isEmpty() || left.size() != right.size()) {
            return 0.0;
        }
        double dot = 0.0;
        double leftNorm = 0.0;
        double rightNorm = 0.0;
        for (int i = 0; i < left.size(); i++) {
            float l = left.get(i);
            float r = right.get(i);
            dot += l * r;
            leftNorm += l * l;
            rightNorm += r * r;
        }
        if (leftNorm == 0.0 || rightNorm == 0.0) {
            return 0.0;
        }
        return dot / (Math.sqrt(leftNorm) * Math.sqrt(rightNorm));
    }

    private double normalize(Number value, double max) {
        if (value == null || max <= 0) {
            return 0.0;
        }
        return Math.min(value.doubleValue() / max, 1.0);
    }

    private double logNormalize(Number value, double max) {
        if (value == null || max <= 0) {
            return 0.0;
        }
        return Math.min(Math.log1p(value.doubleValue()) / Math.log1p(max), 1.0);
    }
}
