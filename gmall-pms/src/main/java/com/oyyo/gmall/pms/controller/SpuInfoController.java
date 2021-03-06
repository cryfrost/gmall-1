package com.oyyo.gmall.pms.controller;

import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.pms.entity.SpuInfoEntity;
import com.oyyo.gmall.pms.service.SpuInfoService;
import com.oyyo.gmall.pms.vo.SpuInfoPriceVO;
import com.oyyo.gmall.pms.vo.SpuInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * spu信息
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-05-05 22:41:37
 */
@Api(tags = "spu信息 管理")
@RestController
@RequestMapping("pms/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 商品列表
     */
    @GetMapping
    public Resp<PageVo> querySpuByPage(QueryCondition queryCondition,@RequestParam("catId")Long catId){
        PageVo pageVo = spuInfoService.querySpuByPage(queryCondition,catId);
        return Resp.ok(pageVo);
    }

    @PostMapping("page")
    public Resp<List<SpuInfoEntity>> querySpusByPage(@RequestBody QueryCondition queryCondition){
        PageVo page = spuInfoService.queryPage(queryCondition);
        List<SpuInfoEntity> list = (List<SpuInfoEntity>) page.getList();

        return Resp.ok(list);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:spuinfo:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = spuInfoService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('pms:spuinfo:info')")
    public Resp<SpuInfoEntity> querySpuById(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return Resp.ok(spuInfo);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:spuinfo:save')")
    public Resp<Object> save(@RequestBody SpuInfoVo spuInfoVo){
		spuInfoService.saveGoodsInfo(spuInfoVo);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:spuinfo:update')")
    public Resp<Object> update(@RequestBody SpuInfoPriceVO spuInfoPriceVO){

        SpuInfoEntity spuInfo = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfoPriceVO,spuInfo);

        spuInfoService.updateSpu(spuInfo,Long.parseLong(spuInfoPriceVO.getSpuCurrentPrice().toString()));

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:spuinfo:delete')")
    public Resp<Object> delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return Resp.ok(null);
    }

}
