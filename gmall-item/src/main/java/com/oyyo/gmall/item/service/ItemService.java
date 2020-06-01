package com.oyyo.gmall.item.service;

import com.oyyo.gmall.item.vo.ItemVO;

public interface ItemService {
    /**
     * 查询商品详情页vo
     * @param skuId
     * @return
     */
    ItemVO queryItemVO(Long skuId);
}
