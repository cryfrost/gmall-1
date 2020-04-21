package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.entity.SpuInfoDesc;

/**
 * spu信息介绍
 *
 * @author oy
 * @since  2020-04-21 16:16:46
 */
public interface SpuInfoDescService extends IService<SpuInfoDesc> {

    PageVo queryPage(QueryCondition params);
}

