package com.hmdp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryTypeList() {

        //1.查询redis
        String key = RedisConstants.CACHE_SHOP_TYPE_KEY;
        //从redis中查询所有店铺类型数据
        List<String> shopTypesJson = stringRedisTemplate.opsForList().range(key, 0, -1);

        //2.存在直接返回
        if (CollectionUtil.isNotEmpty(shopTypesJson)) {
            //将shopTypesJson转换为对象
            List<ShopType> shopTypeList = JSONUtil.toList(shopTypesJson.toString(), ShopType.class);
            CollectionUtil.sort(shopTypeList, (o1, o2) -> o1.getSort() - o2.getSort());
            return Result.ok(shopTypeList);
        }

        //3.不存在，查询数据库
        List<ShopType> shopTypeList = query().orderByAsc("sort").list();

        //4.不存在，返回
        if (CollectionUtil.isEmpty(shopTypeList)) {
            return Result.fail("店铺类型不存在");
        }

        //5.存在，写入redis
        //不能只写入，只写入和读取，因为写入的格式和读取的格式不一致
        //stringRedisTemplate.opsForList().rightPushAll(key, JSONUtil.toJsonStr(shopTypeList));

        //将每个ShopType对象转换为JSON字符串
        for (ShopType shopType : shopTypeList) {
            stringRedisTemplate.opsForList().rightPush(key, JSONUtil.toJsonStr(shopType));
        }
        //6.返回结果
        return Result.ok(shopTypeList);
    }
}
