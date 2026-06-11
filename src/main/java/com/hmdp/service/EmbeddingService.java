package com.hmdp.service;

import java.util.List;

/**
 * 文本向量化服务。
 */
public interface EmbeddingService {

    /**
     * 将文本转换为 Embedding 向量。
     *
     * @param text 待向量化文本
     * @return Embedding 向量
     */
    List<Float> embed(String text);

    /**
     * 当前使用的 Embedding 模型名称。
     *
     * @return 模型名称
     */
    String modelName();
}
