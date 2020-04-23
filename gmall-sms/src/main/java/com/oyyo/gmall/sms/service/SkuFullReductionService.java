package com.oyyo.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.sms.entity.SkuFullReductionEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 商品满减信息
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-04-23 17:20:37
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageVo queryPage(QueryCondition params);
}

