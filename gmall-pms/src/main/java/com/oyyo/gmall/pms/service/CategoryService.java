package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.entity.Category;

/**
 * 商品三级分类
 *
 * @author oy
 * @since  2020-04-21 16:16:49
 */
public interface CategoryService extends IService<Category> {

    PageVo queryPage(QueryCondition params);
}

