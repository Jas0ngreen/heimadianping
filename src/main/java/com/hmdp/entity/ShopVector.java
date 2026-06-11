package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商铺语义向量索引。
 *
 * @author zyy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_shop_vector")
public class ShopVector implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商铺id
     */
    private Long shopId;

    /**
     * 用于生成向量的商铺语义文本
     */
    private String content;

    /**
     * 向量 JSON，保存 Embedding 模型返回的浮点数组
     */
    private String embeddingJson;

    /**
     * 生成向量使用的模型名称
     */
    private String embeddingModel;

    /**
     * 向量维度
     */
    private Integer vectorDimension;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
