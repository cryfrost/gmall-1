package com.oyyo.gmall.pms.vo;

import com.oyyo.gmall.pms.entity.ProductAttrValueEntity;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: ItemGroupVO
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-1 12:19
 * @Version: 1.0
 */
@Data
public class ItemGroupVO {
    /**
     * 组的名称
     */
    private String groupName;

    /**
     * 规格属性
     */
    private List<ProductAttrValueEntity> productAttrs;
}