package com.oyyo.gmall.pms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oyyo.gmall.pms.entity.SkuInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * sku信息
 * 
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-05 22:41:38
 */
@Mapper
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity> {

    Boolean updateCurrentPrice(Long skuId, Long currentPrice);

}
