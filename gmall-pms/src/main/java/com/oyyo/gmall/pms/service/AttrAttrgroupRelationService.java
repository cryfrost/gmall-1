package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.entity.AttrAttrgroupRelation;

/**
 * 属性&属性分组关联
 *
 * @author oy
 * @since  2020-04-21 16:16:49
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelation> {

    PageVo queryPage(QueryCondition params);
}

