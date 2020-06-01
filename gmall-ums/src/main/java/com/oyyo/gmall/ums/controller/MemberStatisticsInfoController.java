package com.oyyo.gmall.ums.controller;

import java.util.Arrays;
import java.util.Map;


import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.oyyo.gmall.ums.entity.MemberStatisticsInfoEntity;
import com.oyyo.gmall.ums.service.MemberStatisticsInfoService;




/**
 * 会员统计信息
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-01 22:58:29
 */
@Api(tags = "会员统计信息 管理")
@RestController
@RequestMapping("ums/memberstatisticsinfo")
public class MemberStatisticsInfoController {
    @Autowired
    private MemberStatisticsInfoService memberStatisticsInfoService;

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ums:memberstatisticsinfo:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = memberStatisticsInfoService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('ums:memberstatisticsinfo:info')")
    public Resp<MemberStatisticsInfoEntity> info(@PathVariable("id") Long id){
		MemberStatisticsInfoEntity memberStatisticsInfo = memberStatisticsInfoService.getById(id);

        return Resp.ok(memberStatisticsInfo);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ums:memberstatisticsinfo:save')")
    public Resp<Object> save(@RequestBody MemberStatisticsInfoEntity memberStatisticsInfo){
		memberStatisticsInfoService.save(memberStatisticsInfo);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ums:memberstatisticsinfo:update')")
    public Resp<Object> update(@RequestBody MemberStatisticsInfoEntity memberStatisticsInfo){
		memberStatisticsInfoService.updateById(memberStatisticsInfo);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ums:memberstatisticsinfo:delete')")
    public Resp<Object> delete(@RequestBody Long[] ids){
		memberStatisticsInfoService.removeByIds(Arrays.asList(ids));

        return Resp.ok(null);
    }

}
