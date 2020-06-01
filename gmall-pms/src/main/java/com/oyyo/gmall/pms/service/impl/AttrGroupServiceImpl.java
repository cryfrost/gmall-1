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
import com.oyyo.gmall.pms.dao.ProductAttrValueDao;
import com.oyyo.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.oyyo.gmall.pms.entity.AttrEntity;
import com.oyyo.gmall.pms.entity.AttrGroupEntity;
import com.oyyo.gmall.pms.entity.ProductAttrValueEntity;
import com.oyyo.gmall.pms.service.AttrGroupService;
import com.oyyo.gmall.pms.vo.GroupVO;
import com.oyyo.gmall.pms.vo.ItemGroupVO;
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
    @Autowired
    private ProductAttrValueDao attrValueDao;

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

    @Override
    public List<GroupVO> queryGroupWithAttrsByCatId(Long catId) {

        //1. 根据 cid 查询三级分类下所有的组
        List<AttrGroupEntity> groupEntities = list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId));
        // 将 groupEntities 转换为 GroupVO
        // 根据 中间表 的 attrids 查询参数
        return groupEntities.stream().map(attrGroupEntity ->
            queryGroupWithAttrByGid(attrGroupEntity.getAttrGroupId())
        ).collect(Collectors.toList());

    }

    @Override
    public List<ItemGroupVO> queryItemGroupVOByCidAndSpuId(Long cid, Long spuId) {
        List<AttrGroupEntity> attrGroupEntities = list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", cid));

        return attrGroupEntities.stream().map(group -> {
            ItemGroupVO itemGroupVO = new ItemGroupVO();
            itemGroupVO.setGroupName(group.getAttrGroupName());

            //查询中间表
            List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", group.getAttrGroupId()));
            //规格参数id
            List<Long> attrIds = relationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
            List<ProductAttrValueEntity> productAttrValueEntities = attrValueDao.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId).in("attr_id", attrIds));

            itemGroupVO.setProductAttrs(productAttrValueEntities);
            return itemGroupVO;
        }).collect(Collectors.toList());

    }

}