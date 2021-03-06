package com.oyyo.gmall.sms.dao;

import com.oyyo.gmall.sms.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author oy
 * @email oy@lcd.com
 * @date 2020-04-23 17:20:38
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
