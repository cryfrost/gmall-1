package com.oyyo.gmall.pms.dao;

import com.oyyo.gmall.pms.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * @author oy
 * @since  2020-04-21 16:16:49
 */
@Mapper
public interface CategoryDao extends BaseMapper<Category> {
	
}
