package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.entity.CommentReplay;

/**
 * 商品评价回复关系
 *
 * @author oy
 * @since  2020-04-21 16:16:48
 */
public interface CommentReplayService extends IService<CommentReplay> {

    PageVo queryPage(QueryCondition params);
}

