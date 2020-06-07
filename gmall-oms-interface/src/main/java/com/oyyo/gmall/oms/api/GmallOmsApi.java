package com.oyyo.gmall.oms.api;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.oms.entity.OrderEntity;
import com.oyyo.gmall.oms.vo.OrderSubmitVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface GmallOmsApi {
    /**
     * 保存订单
     * @param orderSubmitVO
     * @return
     */
    @PostMapping("oms/order")
    Resp<OrderEntity> saveOrder(@RequestBody OrderSubmitVO orderSubmitVO);
}
