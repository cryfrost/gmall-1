package com.oyyo.gmall.index.service.impl;

import com.alibaba.fastjson.JSON;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.index.fegin.GmallPmsClient;
import com.oyyo.gmall.index.service.IndexService;
import com.oyyo.gmall.pms.entity.CategoryEntity;
import com.oyyo.gmall.pms.vo.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

    @Autowired
    private GmallPmsClient gmallPmsClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "index:cates:";

    @Override
    public List<CategoryEntity> queryLevel1Categories() {
        Resp<List<CategoryEntity>> listResp = gmallPmsClient.queryCategoriesByPidOrLevel(1, null);

        return listResp.getData();
    }

    @Override
    public List<CategoryVO> querySubCategories(Long pid) {

        //1 判断缓存中是否存在 如果存在 返回
        String catesJson = redisTemplate.opsForValue().get(KEY_PREFIX + pid);
        if (!StringUtils.isEmpty(catesJson)) {
            //json 转换为 vo对象
            log.info("缓存命中");
            return JSON.parseArray(catesJson,CategoryVO.class);
        }
        //2 缓存中不存在 则去查数据库
        log.info("缓存未命中，进入数据库查询");
        Resp<List<CategoryVO>> listResp = gmallPmsClient.querySubCategories(pid);
        //3 查完之后设置缓存
        List<CategoryVO> categoryVOS = listResp.getData();
        log.info("设置缓存");
        redisTemplate.opsForValue().set(KEY_PREFIX + pid,JSON.toJSONString(categoryVOS),3 + new Random().nextInt(5), TimeUnit.DAYS);

        return categoryVOS;

    }
}
