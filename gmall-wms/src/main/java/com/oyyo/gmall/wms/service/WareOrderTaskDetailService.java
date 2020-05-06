package com.oyyo.gmall.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.wms.entity.WareOrderTaskDetailEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 库存工作单
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-06 15:23:27
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageVo queryPage(QueryCondition params);
}

