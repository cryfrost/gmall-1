package com.oyyo.gmall.ums.controller;

import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.ums.entity.MemberEntity;
import com.oyyo.gmall.ums.service.MemberService;
import com.oyyo.gmall.ums.vo.RegisterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;




/**
 * 会员
 *
 * @author oy
 * @email oy@lcd.com
 * @date 2020-06-01 22:58:30
 */
@Api(tags = "会员 管理")
@RestController
@RequestMapping("ums/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 查询用户信息
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public Resp<MemberEntity> queryUser(@RequestParam("username")String username,@RequestParam("password")String password){

        MemberEntity memberEntity = memberService.queryUser(username,password);

        return Resp.ok(memberEntity);
    }

    /**
     * 注册功能
     * @param registerVO
     * @return
     */
    @PostMapping("register")
    public Resp<Boolean> register(RegisterVO registerVO){
        Boolean result = memberService.register(registerVO);
        return Resp.ok(result);
    }

    /**
     * 数据校验
     * @param data
     * @param type 1-用户名 2-手机号 3-邮箱
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public Resp<Boolean> checkData(@PathVariable("data")String data,@PathVariable("type")Integer type){
        Boolean flag = memberService.checkData(data,type);

        return Resp.ok(flag);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ums:member:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = memberService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('ums:member:info')")
    public Resp<MemberEntity> info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return Resp.ok(member);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ums:member:save')")
    public Resp<Object> save(@RequestBody MemberEntity member){
		memberService.save(member);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ums:member:update')")
    public Resp<Object> update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ums:member:delete')")
    public Resp<Object> delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return Resp.ok(null);
    }

}
