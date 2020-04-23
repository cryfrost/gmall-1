package com.oyyo.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.sms.entity.CouponEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 优惠券信息
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-04-23 17:20:38
 */
public interface CouponService extends IService<CouponEntity> {

    PageVo queryPage(QueryCondition params);
}

