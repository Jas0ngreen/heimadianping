package com.hmdp.service.impl;

import com.hmdp.entity.Shop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ShopVectorContentBuilderTest {

    @Test
    void buildShouldIncludeCoreShopFields() {
        Shop shop = new Shop()
                .setName("海底捞火锅")
                .setArea("大关")
                .setAddress("上塘路458号")
                .setAvgPrice(104L)
                .setScore(49)
                .setSold(4125)
                .setComments(2764)
                .setOpenHours("10:00-07:00");

        String content = new ShopVectorContentBuilder().build(shop);

        assertTrue(content.contains("海底捞火锅"));
        assertTrue(content.contains("大关"));
        assertTrue(content.contains("人均价格：104元"));
        assertTrue(content.contains("评分：4.9分"));
    }
}
