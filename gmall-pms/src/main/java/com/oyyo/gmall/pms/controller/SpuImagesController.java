package com.oyyo.gmall.pms.controller;

import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.pms.entity.SpuImages;
import com.oyyo.gmall.pms.service.SpuImagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * spu图片
 * @author oy
 * @since  2020-04-21 16:16:47
 */
@Api(tags = "spu图片 管理")
@RestController
@RequestMapping("pms/spuimages")
public class SpuImagesController {
    @Autowired
    private SpuImagesService spuImagesService;

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:spuimages:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = spuImagesService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('pms:spuimages:info')")
    public Resp<SpuImages> info(@PathVariable("id") Long id){
		SpuImages spuImages = spuImagesService.getById(id);

        return Resp.ok(spuImages);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:spuimages:save')")
    public Resp<Object> save(@RequestBody SpuImages spuImages){
		spuImagesService.save(spuImages);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:spuimages:update')")
    public Resp<Object> update(@RequestBody SpuImages spuImages){
		spuImagesService.updateById(spuImages);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:spuimages:delete')")
    public Resp<Object> delete(@RequestBody Long[] ids){
		spuImagesService.removeByIds(Arrays.asList(ids));

        return Resp.ok(null);
    }

}
