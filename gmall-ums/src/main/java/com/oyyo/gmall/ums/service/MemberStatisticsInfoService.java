package com.oyyo.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.ums.entity.MemberStatisticsInfoEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 会员统计信息
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-01 22:58:29
 */
public interface MemberStatisticsInfoService extends IService<MemberStatisticsInfoEntity> {

    PageVo queryPage(QueryCondition params);
}

