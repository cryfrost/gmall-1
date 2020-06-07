package com.oyyo.gmall.order.controller;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.order.service.OrderService;
import com.oyyo.gmall.order.vo.OrderConfirmVO;
import com.oyyo.gmall.oms.vo.OrderSubmitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单确认页
     * @return
     */
    @GetMapping("confirm")
    public Resp<OrderConfirmVO> orderConfirm(){
        OrderConfirmVO confirmVO = orderService.orderConfirm();
        return Resp.ok(confirmVO);
    }

    /**
     * 订单提交
     * @param orderSubmitVO
     * @return
     */
    @PostMapping("submit")
    public Resp<Object> submit(@RequestBody OrderSubmitVO orderSubmitVO){
        orderService.submit(orderSubmitVO);

        return Resp.ok(null);
    }

}
