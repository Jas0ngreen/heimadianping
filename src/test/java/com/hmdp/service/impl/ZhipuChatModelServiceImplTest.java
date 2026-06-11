package com.hmdp.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ZhipuChatModelServiceImplTest {

    @Test
    void parseSearchIntentShouldRejectBlankQuery() {
        ZhipuChatModelServiceImpl chatModelService = new ZhipuChatModelServiceImpl(null);

        assertThrows(IllegalArgumentException.class, () -> chatModelService.parseSearchIntent(""));
    }

    @Test
    void generateRecommendationReasonShouldRejectBlankShopContent() {
        ZhipuChatModelServiceImpl chatModelService = new ZhipuChatModelServiceImpl(null);

        assertThrows(IllegalArgumentException.class,
                () -> chatModelService.generateRecommendationReason("找火锅", " "));
    }
}
