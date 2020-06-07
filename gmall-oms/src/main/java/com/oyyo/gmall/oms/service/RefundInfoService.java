package com.oyyo.gmall.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.oms.entity.RefundInfoEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 退款信息
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-05 19:37:51
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageVo queryPage(QueryCondition params);
}

