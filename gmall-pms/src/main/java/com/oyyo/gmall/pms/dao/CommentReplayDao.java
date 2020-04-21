package com.oyyo.gmall.pms.dao;

import com.oyyo.gmall.pms.entity.CommentReplay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 * @author oy
 * @since  2020-04-21 16:16:48
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplay> {
	
}
