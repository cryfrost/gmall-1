package com.oyyo.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.gmall.ums.entity.MemberCollectSpuEntity;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;


/**
 * 会员收藏的商品
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-01 22:58:30
 */
public interface MemberCollectSpuService extends IService<MemberCollectSpuEntity> {

    PageVo queryPage(QueryCondition params);
}

