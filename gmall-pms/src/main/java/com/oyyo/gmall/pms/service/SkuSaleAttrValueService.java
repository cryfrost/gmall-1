package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;

import java.util.List;


/**
 * sku销售属性&值
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-05 22:41:38
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageVo queryPage(QueryCondition params);

    /**
     * 查询sku销售属性
     * @param spuId
     * @return
     */
    List<SkuSaleAttrValueEntity> querySkuSalesAttrValuesBySpuId(Long spuId);
}

