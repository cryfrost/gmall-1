package com.oyyo.gmall.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.wms.entity.FeightTemplateEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 运费模板
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-06 15:23:27
 */
public interface FeightTemplateService extends IService<FeightTemplateEntity> {

    PageVo queryPage(QueryCondition params);
}

