package com.oyyo.gmall.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.wms.entity.WareSkuEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.wms.vo.SkuLockVO;

import java.util.List;


/**
 * 商品库存
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-06 15:23:27
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageVo queryPage(QueryCondition params);

    /**
     * 查询商品库存并锁定
     * @param skuLockVOS
     * @return
     */
    String checkAndLockStore(List<SkuLockVO> skuLockVOS);
}

