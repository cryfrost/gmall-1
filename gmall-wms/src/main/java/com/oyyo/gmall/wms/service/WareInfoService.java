package com.oyyo.gmall.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.wms.entity.WareInfoEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 仓库信息
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-06 15:23:27
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageVo queryPage(QueryCondition params);
}

