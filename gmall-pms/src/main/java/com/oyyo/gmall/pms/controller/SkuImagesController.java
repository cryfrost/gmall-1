package com.oyyo.gmall.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.pms.entity.SkuImagesEntity;
import com.oyyo.gmall.pms.service.SkuImagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * sku图片
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-05 22:41:38
 */
@Api(tags = "sku图片 管理")
@RestController
@RequestMapping("pms/skuimages")
public class SkuImagesController {
    @Autowired
    private SkuImagesService skuImagesService;

    /**
     * 根据skuId 查询 图片列表
     * @param skuId
     * @return
     */
    @GetMapping("{skuId}")
    public Resp<List<SkuImagesEntity>> querySkuImagesBySkuId(@PathVariable("skuId")Long skuId){
        List<SkuImagesEntity> skuImagesEntities = skuImagesService.list(new QueryWrapper<SkuImagesEntity>().eq("sku_id",skuId));
        return Resp.ok(skuImagesEntities);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:skuimages:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = skuImagesService.queryPage(queryCondition);

        return Resp.ok(page);
    }

    @ApiOperation("获取imgs")
    @GetMapping("/getImgs/{skuId}")
    @PreAuthorize("hasAnyAuthority('pms:shuimages:listImgs')")
    public Resp<String> querySkuImgsBySkuId(@PathVariable("skuId") Long skuId) {
        String imgsUri = skuImagesService.querySkuImgsBySkuId(skuId);


        return Resp.ok(imgsUri);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('pms:skuimages:info')")
    public Resp<SkuImagesEntity> info(@PathVariable("id") Long id){
		SkuImagesEntity skuImages = skuImagesService.getById(id);

        return Resp.ok(skuImages);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:skuimages:save')")
    public Resp<Object> save(@RequestBody SkuImagesEntity skuImages){
		skuImagesService.save(skuImages);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:skuimages:update')")
    public Resp<Object> update(@RequestBody SkuImagesEntity skuImages){
		skuImagesService.updateById(skuImages);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:skuimages:delete')")
    public Resp<Object> delete(@RequestBody Long[] ids){
		skuImagesService.removeByIds(Arrays.asList(ids));

        return Resp.ok(null);
    }

}
