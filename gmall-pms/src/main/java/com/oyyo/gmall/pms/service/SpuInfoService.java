package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.entity.SpuInfoEntity;
import com.oyyo.gmall.pms.vo.SpuInfoVo;


/**
 * spu信息
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-05 22:41:37
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo querySpuByPage(QueryCondition queryCondition, Long catId);

    void saveGoodsInfo(SpuInfoVo spuInfoVo);

    void updateSpu(SpuInfoEntity spuInfo, Long spuCurrentPrice);
}

