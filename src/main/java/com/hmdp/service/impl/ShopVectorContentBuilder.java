package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.hmdp.entity.Shop;
import org.springframework.stereotype.Component;

/**
 * 构建用于生成商铺向量的语义文本。
 */
@Component
public class ShopVectorContentBuilder {

    public String build(Shop shop) {
        StringBuilder content = new StringBuilder();
        append(content, "店铺名称", shop.getName());
        append(content, "商圈", shop.getArea());
        append(content, "地址", shop.getAddress());
        append(content, "人均价格", formatPrice(shop.getAvgPrice()));
        append(content, "评分", formatScore(shop.getScore()));
        append(content, "销量", shop.getSold());
        append(content, "评论数", shop.getComments());
        append(content, "营业时间", shop.getOpenHours());
        return content.toString();
    }

    private void append(StringBuilder content, String label, Object value) {
        if (value == null) {
            return;
        }
        String text = String.valueOf(value);
        if (StrUtil.isBlank(text)) {
            return;
        }
        if (content.length() > 0) {
            content.append('\n');
        }
        content.append(label).append('：').append(text);
    }

    private String formatPrice(Long avgPrice) {
        if (avgPrice == null) {
            return null;
        }
        return avgPrice + "元";
    }

    private String formatScore(Integer score) {
        if (score == null) {
            return null;
        }
        return String.format("%.1f分", score / 10.0);
    }
}
