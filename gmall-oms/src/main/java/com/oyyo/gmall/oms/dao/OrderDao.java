package com.oyyo.gmall.oms.dao;

import com.oyyo.gmall.oms.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-05 19:37:52
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
    /**
     * 关闭订单
     * @return
     */
    int closeOrder(String orderToken);
}
