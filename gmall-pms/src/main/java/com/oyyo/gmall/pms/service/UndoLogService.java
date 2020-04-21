package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.entity.UndoLog;

/**
 * 
 *
 * @author oy
 * @since  2020-04-21 16:16:47
 */
public interface UndoLogService extends IService<UndoLog> {

    PageVo queryPage(QueryCondition params);
}

