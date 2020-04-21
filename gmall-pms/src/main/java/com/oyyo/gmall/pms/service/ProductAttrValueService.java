package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.entity.ProductAttrValue;

/**
 * spu属性值
 *
 * @author oy
 * @since  2020-04-21 16:16:48
 */
public interface ProductAttrValueService extends IService<ProductAttrValue> {

    PageVo queryPage(QueryCondition params);
}

