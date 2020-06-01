package com.oyyo.gmall.sms.controller;

import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.sms.entity.SkuBoundsEntity;
import com.oyyo.gmall.sms.service.SkuBoundsService;
import com.oyyo.gmall.sms.vo.SalseVO;
import com.oyyo.gmall.sms.vo.SkuSaleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 商品sku积分设置
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-04-23 17:20:37
 */
@Api(tags = "商品sku积分设置 管理")
@RestController
@RequestMapping("sms/skubounds")
public class SkuBoundsController {
    @Autowired
    private SkuBoundsService skuBoundsService;

    /**
     * 保存营销信息
     * @param skuSaleVO
     * @return
     */
    @PostMapping("sku/sale/save")
    public Resp<Object> saveSaleInfo(@RequestBody SkuSaleVO skuSaleVO){
        skuBoundsService.saveSaleInfo(skuSaleVO);
        return Resp.ok(null);
    }

    /**
     * 查询营销信息
     * @param skuId
     * @return
     */
    @GetMapping("{skuId}")
    public Resp<List<SalseVO>> querySkuSalseBySkuId(@PathVariable("skuId")Long skuId){

        List<SalseVO> salseVOS = skuBoundsService.querySkuSalseBySkuId(skuId);
        return Resp.ok(salseVOS);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sms:skubounds:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = skuBoundsService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sms:skubounds:info')")
    public Resp<SkuBoundsEntity> info(@PathVariable("id") Long id){
		SkuBoundsEntity skuBounds = skuBoundsService.getById(id);

        return Resp.ok(skuBounds);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sms:skubounds:save')")
    public Resp<Object> save(@RequestBody SkuBoundsEntity skuBounds){
		skuBoundsService.save(skuBounds);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sms:skubounds:update')")
    public Resp<Object> update(@RequestBody SkuBoundsEntity skuBounds){
		skuBoundsService.updateById(skuBounds);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sms:skubounds:delete')")
    public Resp<Object> delete(@RequestBody Long[] ids){
		skuBoundsService.removeByIds(Arrays.asList(ids));

        return Resp.ok(null);
    }

}
