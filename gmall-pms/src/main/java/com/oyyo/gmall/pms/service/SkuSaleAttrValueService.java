package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.entity.SkuSaleAttrValue;

/**
 * sku销售属性&值
 *
 * @author oy
 * @since  2020-04-21 16:16:48
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValue> {

    PageVo queryPage(QueryCondition params);
}

