package com.oyyo.gmall.ums.api;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.ums.entity.MemberEntity;
import com.oyyo.gmall.ums.entity.MemberReceiveAddressEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName: GmallUmsApi
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-3 16:49
 * @Version: 1.0
 */
public interface GmallUmsApi {
    /**
     * 根据用户名和密码查询用户信息
     *
     * @param username
     * @param password
     * @return
     */
    @GetMapping("ums/member/query")
    Resp<MemberEntity> queryUser(@RequestParam("username") String username, @RequestParam("password") String password);

    /**
     * 查询用户收获地址列表
     *
     * @return
     */
    @GetMapping("ums/memberreceiveaddress/{userId}")
    Resp<List<MemberReceiveAddressEntity>> queryAddressesByUserId(@PathVariable("userId") Long userId);
    /**
     * 根据 userId查询用户信息
     */
    @GetMapping("ums/member/info/{id}")
    Resp<MemberEntity> queryMemberById(@PathVariable("id") Long id);
}