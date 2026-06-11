package com.hmdp.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ZhipuEmbeddingServiceImplTest {

    @Test
    void embedShouldRejectBlankText() {
        ZhipuEmbeddingServiceImpl embeddingService = new ZhipuEmbeddingServiceImpl(null, "embedding-3");

        assertThrows(IllegalArgumentException.class, () -> embeddingService.embed(" "));
    }
}
