package com.oyyo.gmall.index.service.impl;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.index.annotation.GmallCache;
import com.oyyo.gmall.index.fegin.GmallPmsClient;
import com.oyyo.gmall.index.service.IndexService;
import com.oyyo.gmall.pms.entity.CategoryEntity;
import com.oyyo.gmall.pms.vo.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

    @Autowired
    private GmallPmsClient gmallPmsClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    private static final String KEY_PREFIX = "index:cates:";

    @Override
    public List<CategoryEntity> queryLevel1Categories() {
        Resp<List<CategoryEntity>> listResp = gmallPmsClient.queryCategoriesByPidOrLevel(1, null);

        return listResp.getData();
    }

    @Override
    @GmallCache(prefix = "index:cates:",timeout = 300,random = 360)
    public List<CategoryVO> querySubCategories(Long pid) {

//        //1 判断缓存中是否存在 如果存在 返回
//        String catesJson = redisTemplate.opsForValue().get(KEY_PREFIX + pid);
//        if (!StringUtils.isEmpty(catesJson)) {
//            //json 转换为 vo对象
//            log.info("缓存命中");
//            return JSON.parseArray(catesJson,CategoryVO.class);
//        }
//        //分布式锁
//        RLock lock = redissonClient.getLock("catesLock" + pid);
//        lock.lock();
//        //再次去缓存中判断
//        //1 判断缓存中是否存在 如果存在 返回
//        String catesJson2 = redisTemplate.opsForValue().get(KEY_PREFIX + pid);
//        if (!StringUtils.isEmpty(catesJson2)) {
//            //json 转换为 vo对象
//            log.info("缓存命中");
//            lock.unlock();
//            return JSON.parseArray(catesJson2,CategoryVO.class);
//        }
//        //2 缓存中不存在 则去查数据库
//        log.info("缓存未命中，进入数据库查询");
        Resp<List<CategoryVO>> listResp = gmallPmsClient.querySubCategories(pid);
//        //3 查完之后设置缓存
        List<CategoryVO> categoryVOS = listResp.getData();
//        log.info("设置缓存");
//        redisTemplate.opsForValue().set(KEY_PREFIX + pid,JSON.toJSONString(categoryVOS),3 + new Random().nextInt(5), TimeUnit.DAYS);
//        lock.unlock();
        return categoryVOS;

    }
}
