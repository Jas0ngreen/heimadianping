package com.hmdp.controller;

import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.Result;
import com.hmdp.service.AiShopSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI 商铺搜索控制器。
 */
@RestController
@RequestMapping("/ai/shop")
public class AiShopSearchController {

    private final AiShopSearchService aiShopSearchService;

    public AiShopSearchController(AiShopSearchService aiShopSearchService) {
        this.aiShopSearchService = aiShopSearchService;
    }

    @GetMapping("/search")
    public Result search(@RequestParam("query") String query,
                         @RequestParam(value = "limit", required = false) Integer limit) {
        if (StrUtil.isBlank(query)) {
            return Result.fail("搜索内容不能为空");
        }
        return Result.ok(aiShopSearchService.search(query, limit));
    }
}
