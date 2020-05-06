package com.oyyo.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.oyyo.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.oyyo.gmall.pms.service.AttrAttrgroupRelationService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public void deleteRelations(List<AttrAttrgroupRelationEntity> relationEntityList) {

        relationEntityList.forEach(relationEntity -> {
        remove(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id",relationEntity.getAttrId())
                .eq("attr_group_id",relationEntity.getAttrGroupId()));
        });
    }

}