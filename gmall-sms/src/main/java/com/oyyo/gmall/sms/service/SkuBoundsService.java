package com.oyyo.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.sms.entity.SkuBoundsEntity;
import com.oyyo.gmall.sms.vo.SalseVO;
import com.oyyo.gmall.sms.vo.SkuSaleVO;

import java.util.List;


/**
 * 商品sku积分设置
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-04-23 17:20:37
 */
public interface SkuBoundsService extends IService<SkuBoundsEntity> {

    PageVo queryPage(QueryCondition params);
    void saveSaleInfo(SkuSaleVO skuSaleVO);

    List<SalseVO> querySkuSalseBySkuId(Long skuId);
}

