package com.hmdp.service.impl;

import com.hmdp.dto.AiSearchIntent;
import com.hmdp.entity.Shop;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiShopSearchScorerTest {

    @Test
    void cosineSimilarityShouldPreferSameDirection() {
        AiShopSearchScorer scorer = new AiShopSearchScorer();

        double same = scorer.cosineSimilarity(
                Arrays.asList(1.0F, 0.0F),
                Arrays.asList(1.0F, 0.0F)
        );
        double different = scorer.cosineSimilarity(
                Arrays.asList(1.0F, 0.0F),
                Arrays.asList(0.0F, 1.0F)
        );

        assertTrue(same > different);
    }

    @Test
    void shouldFilterShopOverBudget() {
        AiShopSearchScorer scorer = new AiShopSearchScorer();
        AiSearchIntent intent = new AiSearchIntent();
        intent.setMaxPrice(100L);

        Shop shop = new Shop().setAvgPrice(120L);

        assertFalse(scorer.matchBusinessFilters(shop, intent));
    }

    @Test
    void businessScoreShouldPreferHigherRatedShop() {
        AiShopSearchScorer scorer = new AiShopSearchScorer();
        Shop highRated = new Shop().setScore(49).setSold(100).setComments(100);
        Shop lowRated = new Shop().setScore(35).setSold(100).setComments(100);

        assertTrue(scorer.businessScore(highRated) > scorer.businessScore(lowRated));
    }
}
