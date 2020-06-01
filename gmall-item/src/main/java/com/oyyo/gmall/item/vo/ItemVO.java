package com.oyyo.gmall.item.vo;

import com.oyyo.gmall.pms.entity.BrandEntity;
import com.oyyo.gmall.pms.entity.CategoryEntity;
import com.oyyo.gmall.pms.entity.SkuImagesEntity;
import com.oyyo.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.oyyo.gmall.pms.vo.ItemGroupVO;
import com.oyyo.gmall.sms.vo.SalseVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName: ItemVO
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-1 11:38
 * @Version: 1.0
 */
@Data
public class ItemVO {
    private Long skuId;

    /**
     *  //分类对象
     */
    private CategoryEntity categoryEntity;

    /**
     *   //品牌信息
     */
    private BrandEntity brandEntity;

    private Long spuId;
    private String spuName;

    private String skuTitle;
    private String skuSubTitle;
    private BigDecimal price;
    private BigDecimal weight;

    /**
     *   //详情页图片
     */
    private List<SkuImagesEntity> skuImagesEntities;

    /**
     * //sku营销信息
     */
    private List<SalseVO> sales;

    /**
     * 库存信息 是否有货
     */
    private Boolean store;

    /**
     * 销售属性
     */
    private List<SkuSaleAttrValueEntity> saleAttrs;

    /**
     * spu的海报
     */
    private List<String> posterImgs;

    /**
     * 规格参数组及 组下的规格参数 包括值
     */
    private List<ItemGroupVO> itemGroupVOS;
}