package com.oyyo.gmall.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.oms.entity.OrderEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.oms.vo.OrderSubmitVO;


/**
 * 订单
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-05 19:37:52
 */
public interface OrderService extends IService<OrderEntity> {

    PageVo queryPage(QueryCondition params);

    /**
     * 保存订单
     * @param submitVO
     * @return
     */
    OrderEntity saveOrder(OrderSubmitVO submitVO);
    /**
     * 关闭订单
     * @return
     */
    int closeOrder(String orderToken);
}

