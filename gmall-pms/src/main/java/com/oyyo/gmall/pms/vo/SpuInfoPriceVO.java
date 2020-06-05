package com.oyyo.gmall.pms.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: SpuInfoPriceVO
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-5 11:36
 * @Version: 1.0
 */
@Data
public class SpuInfoPriceVO extends SpuInfoVo {
    private BigDecimal spuCurrentPrice;
}