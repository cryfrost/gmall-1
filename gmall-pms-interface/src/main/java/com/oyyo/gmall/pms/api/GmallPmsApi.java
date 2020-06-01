package com.oyyo.gmall.pms.api;

import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.pms.entity.*;
import com.oyyo.gmall.pms.vo.CategoryVO;
import com.oyyo.gmall.pms.vo.ItemGroupVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GmallPmsApi {

    /**
     * 查询一级分类下的 二级分类和三级分类
     *
     * @param pid
     * @return
     */
    @GetMapping("pms/category/{pid}")
    Resp<List<CategoryVO>> querySubCategories(@PathVariable("pid") Long pid);

    /**
     * 查询一级分类
     *
     * @param level
     * @param pid
     * @return
     */
    @GetMapping("pms/category")
    Resp<List<CategoryEntity>> queryCategoriesByPidOrLevel(
            @RequestParam(value = "level", defaultValue = "0") Integer level,
            @RequestParam(value = "parentCid", required = false) Long pid);

    /**
     * 分页查询spu
     * @param queryCondition
     * @return
     */
    @PostMapping("pms/spuinfo/page")
    Resp<List<SpuInfoEntity>> querySpusByPage(@RequestBody QueryCondition queryCondition);
    /**
     * 信息
     */
    @GetMapping("pms/spuinfo/info/{id}")
    Resp<SpuInfoEntity> querySpuById(@PathVariable("id") Long id);
    /**
     * 查询 sku 所有图片
     * @param skuId
     * @return
     */
    @GetMapping("pms/skuimages/getImgs/{skuId}")
     Resp<String> querySkuImgsBySkuId(@PathVariable("skuId") Long skuId) ;

    /**
     * 根据skuId 查询 sku信息
     */
    @GetMapping("pms/skuinfo/info/{skuId}")
    Resp<SkuInfoEntity> querySkuBySkuId(@PathVariable("skuId") Long skuId);

    /**
     * 根据skuId 查询图片列表
     * @param skuId
     * @return
     */
    @GetMapping("pms/skuimages/{skuId}")
    Resp<List<SkuImagesEntity>> querySkuImagesBySkuId(@PathVariable("skuId")Long skuId);

    /**
     * 列表
     */
    @GetMapping("pms/spuinfo/list")
    Resp<PageVo> list(QueryCondition queryCondition);

    /**
     * 根据 spuId查询 sku
     * @param spuId
     * @return
     */
    @GetMapping("pms/skuinfo/{spuId}")
    Resp<List<SkuInfoEntity>> querySkusBySpuId(@PathVariable("spuId")Long spuId);


    /**
     * 信息
     */
    @GetMapping("pms/brand/info/{brandId}")
    Resp<BrandEntity> queryBrandById(@PathVariable("brandId") Long brandId);

    /**
     * 信息
     */
    @GetMapping("pms/category/info/{catId}")
    Resp<CategoryEntity> queryCategoryById(@PathVariable("catId") Long catId);

    @GetMapping("pms/productattrvalue/{spuId}")
    Resp<List<ProductAttrValueEntity>> querySearchAttrValueBySpuId(@PathVariable("spuId")Long spuId);

    /**
     * 查询sku销售属性
     *
     * @param spuId
     * @return
     */
    @GetMapping("pms/skusaleattrvalue/{spuId}")
    Resp<List<SkuSaleAttrValueEntity>> querySkuSalesAttrValuesBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 查询海报信息
     */
    @GetMapping("pms/spuinfodesc/info/{spuId}")
    Resp<SpuInfoDescEntity> querySpuDescBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 规格参数及组 的值 api接口
     *
     * @param cid
     * @param spuId
     * @return
     */
    @GetMapping("pms/attrgroup/item/group/{cid}/{spuId}")
    Resp<List<ItemGroupVO>> queryItemGroupVOByCidAndSpuId(@PathVariable("cid") Long cid, @PathVariable("spuId") Long spuId);

}
