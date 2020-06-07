package com.oyyo.gmall.oms.vo;

import com.oyyo.gmall.ums.entity.MemberReceiveAddressEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderSubmitVO {
    private String orderToken;
    private MemberReceiveAddressEntity address;
    private Integer payType;
    private String deliveryCompany;
    private List<OrderItemVo> items;
    private Integer bounds;
    //总价格校验
    private BigDecimal totalPrice;
    private Long userId;
}
