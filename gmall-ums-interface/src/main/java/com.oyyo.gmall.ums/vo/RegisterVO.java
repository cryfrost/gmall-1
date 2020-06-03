package com.oyyo.gmall.ums.vo;

import lombok.Data;

/**
 * @ClassName: RegisterVO
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-2 11:24
 * @Version: 1.0
 */
@Data
public class RegisterVO {
    private String username;
    private String password;
    private String mobile;
    private String email;
    private String code;
}