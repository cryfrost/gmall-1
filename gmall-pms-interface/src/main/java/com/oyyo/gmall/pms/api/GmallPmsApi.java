package com.oyyo.gmall.pms.api;

import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.pms.entity.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface GmallPmsApi {
    @PostMapping("pms/spuinfo/page")
    Resp<List<SpuInfoEntity>> querySpusByPage(@RequestBody QueryCondition queryCondition);

    /**
     * 查询 sku 所有图片
     * @param skuId
     * @return
     */
    @GetMapping("pms/skuimages/getImgs/{skuId}")
     Resp<String> querySkuImgsBySkuId(@PathVariable("skuId") String skuId) ;
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
