package com.oyyo.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.entity.AttrGroupEntity;
import com.oyyo.gmall.pms.vo.GroupVO;
import com.oyyo.gmall.pms.vo.ItemGroupVO;

import java.util.List;


/**
 * 属性分组
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-05 22:41:39
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryGroupByPage(QueryCondition queryCondition, Long catId);

    GroupVO queryGroupWithAttrByGid(Long gid);

    List<GroupVO> queryGroupWithAttrsByCatId(Long catId);

    /**
     * 查询规格参数及分组
     * @param cid
     * @param spuId
     * @return
     */
    List<ItemGroupVO> queryItemGroupVOByCidAndSpuId(Long cid, Long spuId);
}

