package com.oyyo.gmall.wms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oyyo.gmall.wms.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-06 15:23:27
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    /**
     * 查询库存列表
     * @param skuId
     * @return
     */
    List<WareSkuEntity> checkStore(@Param("skuId") Long skuId, @Param("count")Integer count);

    /**
     * 锁定库存
     * @param id
     * @param count
     */
    int lockStore(@Param("id") Long id, @Param("count") Integer count);

    /**
     * 解锁
     * @param wareSkuId
     * @param count
     * @return
     */
    int unLockStore(Long wareSkuId, Integer count);
}
