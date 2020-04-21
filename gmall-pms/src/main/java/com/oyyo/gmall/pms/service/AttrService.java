package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.entity.Attr;

/**
 * 商品属性
 *
 * @author oy
 * @since  2020-04-21 16:16:49
 */
public interface AttrService extends IService<Attr> {

    PageVo queryPage(QueryCondition params);
}

