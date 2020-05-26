package com.oyyo.gmall.pms.api;

import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.pms.entity.*;
import com.oyyo.gmall.pms.vo.CategoryVO;
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

}
