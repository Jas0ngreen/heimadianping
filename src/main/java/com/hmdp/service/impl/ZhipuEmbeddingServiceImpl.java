package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.hmdp.service.EmbeddingService;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基于 LangChain4j OpenAI 兼容接口的智谱 Embedding 服务。
 */
@Service
public class ZhipuEmbeddingServiceImpl implements EmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final String modelName;

    public ZhipuEmbeddingServiceImpl(EmbeddingModel embeddingModel,
                                     @Value("${langchain4j.open-ai.embedding-model.model-name}") String modelName) {
        this.embeddingModel = embeddingModel;
        this.modelName = modelName;
    }

    @Override
    public List<Float> embed(String text) {
        if (StrUtil.isBlank(text)) {
            throw new IllegalArgumentException("Embedding text must not be blank");
        }
        Response<Embedding> response = embeddingModel.embed(text);
        return response.content().vectorAsList();
    }

    @Override
    public String modelName() {
        return modelName;
    }
}
