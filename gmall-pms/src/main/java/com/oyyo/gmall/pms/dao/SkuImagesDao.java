package com.oyyo.gmall.pms.dao;

import com.oyyo.gmall.pms.entity.SkuImagesEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * sku图片
 * 
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-05 22:41:38
 */
@Mapper
public interface SkuImagesDao extends BaseMapper<SkuImagesEntity> {

    List<String> querySkuImgsBySkuId(Long skuId);
}
