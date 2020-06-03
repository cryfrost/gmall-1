package com.oyyo.gmall.auth.service.impl;

import com.oyyo.core.bean.Resp;
import com.oyyo.core.utils.JwtUtils;
import com.oyyo.gmall.auth.config.JwtProperties;
import com.oyyo.gmall.auth.feign.GmallUmsClient;
import com.oyyo.gmall.auth.service.AuthService;
import com.oyyo.gmall.ums.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: implAuthServiceImpl
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-3 18:02
 * @Version: 1.0
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthServiceImpl implements AuthService {

    @Autowired
    private GmallUmsClient gmallUmsClient;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 授权token
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public Boolean accredit(String username, String password) {
        //远程调用 校验用户名和密码
        Resp<MemberEntity> memberEntityResp = gmallUmsClient.queryUser(username, password);
        MemberEntity memberEntity = memberEntityResp.getData();

        //判断用户是否存在
        if (memberEntity == null) {
            return false;
        }
        //制作 jwt
        Map<String, Object> map = new HashMap<>();
        map.put("id",memberEntity.getId());
        map.put("username",memberEntity.getUsername());

        String token = JwtUtils.generateToken(map, jwtProperties.getPrivateKey(), jwtProperties.getExpire());


        //放入cookie
        return null;
    }
}