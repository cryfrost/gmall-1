package com.oyyo.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.ums.entity.MemberEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 会员
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-01 22:58:30
 */
public interface MemberService extends IService<MemberEntity> {

    PageVo queryPage(QueryCondition params);

    /**
     * 数据校验
     * @param data
     * @param type
     * @return
     */
    Boolean checkData(String data, Integer type);
}

