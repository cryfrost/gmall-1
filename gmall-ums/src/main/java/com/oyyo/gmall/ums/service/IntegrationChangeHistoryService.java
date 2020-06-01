package com.oyyo.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.ums.entity.IntegrationChangeHistoryEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 积分变化历史记录
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-01 22:58:30
 */
public interface IntegrationChangeHistoryService extends IService<IntegrationChangeHistoryEntity> {

    PageVo queryPage(QueryCondition params);
}

