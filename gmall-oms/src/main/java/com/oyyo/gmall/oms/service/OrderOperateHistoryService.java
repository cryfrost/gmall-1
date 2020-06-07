package com.oyyo.gmall.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.oms.entity.OrderOperateHistoryEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 订单操作历史记录
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-05 19:37:52
 */
public interface OrderOperateHistoryService extends IService<OrderOperateHistoryEntity> {

    PageVo queryPage(QueryCondition params);
}

