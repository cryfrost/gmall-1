package com.oyyo.gmall.wms.vo;

import lombok.Data;

@Data
public class SkuLockVO {
    private Long skuId;
    private Integer count;
    //锁定状态
    private Boolean lock;
    //锁定库存id
    private Long wareSkuId;
}
