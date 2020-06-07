package com.oyyo.gmall.oms.vo;

import com.oyyo.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.oyyo.gmall.sms.vo.SalseVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemVo {
    private Long skuId;
    private String title;
    private String defaultImage;
    private BigDecimal price; //当前价格

    private Integer count;
    private Boolean store;
    private List<SkuSaleAttrValueEntity> saleAttrValues;
    private List<SalseVO> sales;
    private BigDecimal weight;
}
