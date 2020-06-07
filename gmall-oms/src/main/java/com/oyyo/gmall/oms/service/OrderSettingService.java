package com.oyyo.gmall.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.oms.entity.OrderSettingEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 订单配置信息
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-05 19:37:51
 */
public interface OrderSettingService extends IService<OrderSettingEntity> {

    PageVo queryPage(QueryCondition params);
}

