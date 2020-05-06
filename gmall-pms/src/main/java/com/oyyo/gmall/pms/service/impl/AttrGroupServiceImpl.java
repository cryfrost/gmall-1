package com.oyyo.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.oyyo.gmall.pms.dao.AttrDao;
import com.oyyo.gmall.pms.dao.AttrGroupDao;
import com.oyyo.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.oyyo.gmall.pms.entity.AttrEntity;
import com.oyyo.gmall.pms.entity.AttrGroupEntity;
import com.oyyo.gmall.pms.service.AttrGroupService;
import com.oyyo.gmall.pms.vo.GroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Autowired
    private AttrDao attrDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryGroupByPage(QueryCondition queryCondition, Long catId) {
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        if (catId != null) {
            queryWrapper.eq("catelog_id",catId);
        }
        IPage<AttrGroupEntity> page = page(
                new Query<AttrGroupEntity>().getPage(queryCondition),
                queryWrapper
        );

        return new PageVo(page);

    }

    @Override
    public GroupVO queryGroupWithAttrByGid(Long gid) {

        GroupVO groupVO = new GroupVO();
        //查询 group
        AttrGroupEntity groupEntity = getById(gid);
        BeanUtils.copyProperties(groupEntity,groupVO);

        // 根据 gid 查询关联关系 并获取 attrids
        List<AttrAttrgroupRelationEntity> relations = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", gid));
        if (CollectionUtils.isEmpty(relations)) {
            return groupVO;
        }
        groupVO.setRelations(relations);
        //根据attrids查询所有的规格参数
        List<Long> attrIds = relations.stream().map(relationsEntity -> relationsEntity.getAttrId()).collect(Collectors.toList());
        List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIds);
        groupVO.setAttrEntities(attrEntities);
        return groupVO;
    }

}