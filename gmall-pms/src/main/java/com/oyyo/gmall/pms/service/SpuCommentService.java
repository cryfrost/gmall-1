package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.entity.SpuComment;

/**
 * 商品评价
 *
 * @author oy
 * @since  2020-04-21 16:16:47
 */
public interface SpuCommentService extends IService<SpuComment> {

    PageVo queryPage(QueryCondition params);
}

