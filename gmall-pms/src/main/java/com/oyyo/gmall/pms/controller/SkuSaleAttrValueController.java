package com.oyyo.gmall.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.oyyo.gmall.pms.service.SkuSaleAttrValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * sku销售属性&值
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-05 22:41:38
 */
@Api(tags = "sku销售属性&值 管理")
@RestController
@RequestMapping("pms/skusaleattrvalue")
public class SkuSaleAttrValueController {
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    /**
     * 查询spu销售属性
     * @param spuId
     * @return
     */
    @GetMapping("{spuId}")
    public Resp<List<SkuSaleAttrValueEntity>> querySkuSalesAttrValuesBySpuId(@PathVariable("spuId")Long spuId){
        List<SkuSaleAttrValueEntity> saleAttrValueEntities = skuSaleAttrValueService.querySkuSalesAttrValuesBySpuId(spuId);
        return Resp.ok(saleAttrValueEntities);
    }

    /**
     * 查询sku销售属性
     * @param skuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    public Resp<List<SkuSaleAttrValueEntity>> querySkuSalesAttrValuesBySkuId(@PathVariable("skuId")Long skuId){
        List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = skuSaleAttrValueService.list(new QueryWrapper<SkuSaleAttrValueEntity>().eq("sku_id",skuId));
        return Resp.ok(skuSaleAttrValueEntities);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:skusaleattrvalue:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = skuSaleAttrValueService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('pms:skusaleattrvalue:info')")
    public Resp<SkuSaleAttrValueEntity> info(@PathVariable("id") Long id){
		SkuSaleAttrValueEntity skuSaleAttrValue = skuSaleAttrValueService.getById(id);

        return Resp.ok(skuSaleAttrValue);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:skusaleattrvalue:save')")
    public Resp<Object> save(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue){
		skuSaleAttrValueService.save(skuSaleAttrValue);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:skusaleattrvalue:update')")
    public Resp<Object> update(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue){
		skuSaleAttrValueService.updateById(skuSaleAttrValue);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:skusaleattrvalue:delete')")
    public Resp<Object> delete(@RequestBody Long[] ids){
		skuSaleAttrValueService.removeByIds(Arrays.asList(ids));

        return Resp.ok(null);
    }

}
