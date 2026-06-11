package com.hmdp.controller;

import com.hmdp.dto.Result;
import com.hmdp.service.AiShopIndexService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * AI 商铺索引控制器。
 */
@RestController
@RequestMapping("/ai/shop/index")
public class AiShopIndexController {

    @Resource
    private AiShopIndexService aiShopIndexService;

    @PostMapping("/rebuild")
    public Result rebuildShopVectorIndex() {
        return Result.ok(aiShopIndexService.rebuildShopVectorIndex());
    }
}
