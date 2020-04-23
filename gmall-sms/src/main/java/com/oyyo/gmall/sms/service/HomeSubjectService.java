package com.oyyo.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.sms.entity.HomeSubjectEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-04-23 17:20:38
 */
public interface HomeSubjectService extends IService<HomeSubjectEntity> {

    PageVo queryPage(QueryCondition params);
}

