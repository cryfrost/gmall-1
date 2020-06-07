package com.oyyo.gmall.order.vo;

import com.oyyo.gmall.oms.vo.OrderItemVo;
import com.oyyo.gmall.ums.entity.MemberReceiveAddressEntity;
import lombok.Data;

import java.util.List;

@Data
public class OrderConfirmVO {
    //收货地址信息
    private List<MemberReceiveAddressEntity> address;
    //订单详情
    private List<OrderItemVo> orderItems;
    //积分
    private Integer bounds;
    //防止表单重复提交 唯一标志
    private String orderToken;

}
