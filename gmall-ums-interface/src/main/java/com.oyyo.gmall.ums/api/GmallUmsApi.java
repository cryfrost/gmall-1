package com.oyyo.gmall.ums.api;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.ums.entity.MemberEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: GmallUmsApi
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-3 16:49
 * @Version: 1.0
 */
public interface GmallUmsApi {
    /**
     * 查询用户信息
     *
     * @param username
     * @param password
     * @return
     */
    @GetMapping("ums/member/query")
    Resp<MemberEntity> queryUser(@RequestParam("username") String username, @RequestParam("password") String password);
}