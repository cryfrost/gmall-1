package com.oyyo.gmall.pms.vo;

import com.oyyo.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.oyyo.gmall.pms.entity.AttrEntity;
import com.oyyo.gmall.pms.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: GroupVO
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-5-6 11:31
 * @Version: 1.0
 */
@Data
public class GroupVO extends AttrGroupEntity {
    private List<AttrEntity> attrEntities;
    private List<AttrAttrgroupRelationEntity> relations;
}