package com.oyyo.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.oyyo.gmall.pms.dao.AttrDao;
import com.oyyo.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.oyyo.gmall.pms.entity.AttrEntity;
import com.oyyo.gmall.pms.service.AttrService;
import com.oyyo.gmall.pms.vo.AttrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryAttrsByCid(QueryCondition condition, Long cid, Integer type) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();

        if (cid != null) {
            wrapper.eq("catelog_id",cid);
        }
        wrapper.eq("attr_type",type);
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(condition),
                wrapper
        );
        return new PageVo(page);
    }

    @Override
    public void saveAttr(AttrVO attrVO) {
        //新增 attr
        save(attrVO);
        Long attrId = attrVO.getAttrId();
        //新增中间表 attrattrgrouprelationdao
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attrId);
        relationEntity.setAttrGroupId(attrVO.getAttrGroupId());
        relationDao.insert(relationEntity);
    }

}