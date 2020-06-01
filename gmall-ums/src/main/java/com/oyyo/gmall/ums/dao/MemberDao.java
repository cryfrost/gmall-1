package com.oyyo.gmall.ums.dao;

import com.oyyo.gmall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-01 22:58:30
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
