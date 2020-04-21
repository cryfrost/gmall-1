package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.entity.Brand;

/**
 * 品牌
 *
 * @author oy
 * @since  2020-04-21 16:16:49
 */
public interface BrandService extends IService<Brand> {

    PageVo queryPage(QueryCondition params);
}

