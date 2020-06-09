package com.oyyo.gmall.wms.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.wms.dao.WareSkuDao;
import com.oyyo.gmall.wms.entity.WareSkuEntity;
import com.oyyo.gmall.wms.service.WareSkuService;
import com.oyyo.gmall.wms.vo.SkuLockVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("wareSkuService")
@Slf4j
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private WareSkuDao wareSkuDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "stock:lock";

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageVo(page);
    }
    /**
     * 查询库存 并锁定
     * @param skuLockVOS
     * @return
     */
    @Override
    public String checkAndLockStore(List<SkuLockVO> skuLockVOS) {
        if (CollectionUtils.isEmpty(skuLockVOS)) {
            log.info("未选择商品");
            return "未选择商品";
        }
        //检验并锁定库存
        skuLockVOS.forEach(skuLockVO -> {
            lockStrore(skuLockVO);
        });
        List<SkuLockVO> unLockSku = skuLockVOS.stream().filter(skuLockVO -> skuLockVO.getLock() == false).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unLockSku)) {
            //解锁已锁定商品
            log.info("解锁已锁定商品");
            List<SkuLockVO> lockSkuIds = skuLockVOS.stream().filter(SkuLockVO::getLock).collect(Collectors.toList());
            lockSkuIds.forEach(skuLockVO -> {
                wareSkuDao.unLockStore(skuLockVO.getWareSkuId(),skuLockVO.getCount());
            });
            //提示锁定失败的商品
            List<Long> unLockSkuIds = unLockSku.stream().map(SkuLockVO::getSkuId).collect(Collectors.toList());

            return "下单失败，商品库存不足:" + unLockSkuIds.toString();
        }
        String orderToken = skuLockVOS.get(0).getOrderToken();
        redisTemplate.opsForValue().set(KEY_PREFIX + orderToken, JSON.toJSONString(skuLockVOS),16,TimeUnit.MINUTES);
        return "查询库存并锁定成功";

    }

    private void lockStrore(SkuLockVO skuLockVO){
        //开启分布式锁
        log.info("开启分布式锁");
        RLock lock = redissonClient.getLock("stok:" + skuLockVO.getSkuId());
        lock.lock(100, TimeUnit.SECONDS);
        //查询剩余库存是否足够
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.checkStore(skuLockVO.getSkuId(),skuLockVO.getCount());
        if (!CollectionUtils.isEmpty(wareSkuEntities)) {
            //锁定库存
            Long id = wareSkuEntities.get(0).getId();
            log.info("锁定id=[{}]库存",id);
            int result = wareSkuDao.lockStore(id, skuLockVO.getCount());
            if (result > 0){
                skuLockVO.setWareSkuId(id);
                skuLockVO.setLock(true);
            }
        } else {
            skuLockVO.setLock(false);
        }
        log.info("解锁");
        lock.unlock();
    }

}