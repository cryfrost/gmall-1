package com.oyyo.gmall.pms.dao;

import com.oyyo.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-05 22:41:37
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
