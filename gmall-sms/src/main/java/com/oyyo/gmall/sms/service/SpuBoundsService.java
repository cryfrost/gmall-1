package com.oyyo.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.sms.entity.SpuBoundsEntity;


/**
 * 商品spu积分设置
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-04-23 17:20:36
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageVo queryPage(QueryCondition params);

}

