package com.oyyo.gmall.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.oms.entity.OrderReturnReasonEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 退货原因
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-05 19:37:51
 */
public interface OrderReturnReasonService extends IService<OrderReturnReasonEntity> {

    PageVo queryPage(QueryCondition params);
}

