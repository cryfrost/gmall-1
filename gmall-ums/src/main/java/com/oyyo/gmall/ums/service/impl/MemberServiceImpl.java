package com.oyyo.gmall.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.ums.dao.MemberDao;
import com.oyyo.gmall.ums.entity.MemberEntity;
import com.oyyo.gmall.ums.service.MemberService;
import com.oyyo.gmall.ums.vo.RegisterVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


/**
 * @author likui
 */
@Service("memberService")
@Slf4j
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    /**
     * 注册短信验证码 缓存key前缀
     */
    private static final String KEY_PREFIX = "user:verity";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public Boolean checkData(String data, Integer type) {
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        log.info("进入数据校验：data=【{}】,type=【{}】",data,type);
        switch (type) {
            case 1 : queryWrapper.eq("username",data);break;
            case 2 : queryWrapper.eq("mobile",data);break;
            case 3 : queryWrapper.eq("email",data);break;
            default: return false;
        }
        log.info("数据校验结束");
        return count(queryWrapper) == 0;
    }

    @Override
    public Boolean register(RegisterVO registerVO) {
        MemberEntity memberEntity = new MemberEntity();
        //校验手机验证码
        String verifyCode = redisTemplate.opsForValue().get(KEY_PREFIX + registerVO.getMobile());
        if (StringUtils.isBlank(verifyCode)) {
            log.info("验证码未发送或超过三分钟");
            return false;
        }
        boolean flag = StringUtils.equals(registerVO.getCode(), verifyCode);
        if (!flag) {
            log.info("验证码输入错误");
            return false;
        }
        log.info("验证码通过，开始注册");
        memberEntity.setMobile(registerVO.getMobile());
        memberEntity.setEmail(registerVO.getEmail());

        //生成盐
        String salt = UUID.randomUUID().toString().substring(0, 6);
        memberEntity.setSalt(salt);
        //加盐加密 salt + password +salt
        String password = DigestUtils.md5Hex(salt + registerVO.getPassword() + salt);
        memberEntity.setPassword(password);

        //初始化默认值
        memberEntity.setGrowth(0);
        memberEntity.setIntegration(0);
        memberEntity.setLevelId(0L);
        memberEntity.setCreateTime(new Date());
        memberEntity.setStatus(1);
        memberEntity.setUsername(registerVO.getUsername());

        //新增用户
        boolean saveResult = save(memberEntity);

        // 删除redis 中手机验证码
        if (saveResult) {
            log.info("保存成功，删除缓存");
            redisTemplate.delete(KEY_PREFIX + registerVO.getMobile());
        }

        return saveResult;

    }

    @Override
    public MemberEntity queryUser(String username, String password) {
        MemberEntity user = getOne(new QueryWrapper<MemberEntity>().eq("username", username));
        if (user == null) {
            log.info("用户名不存在");
            return null;
        }
        //对用户输入的密码加密
        String userPwd = DigestUtils.md5Hex(user.getSalt() + password + user.getSalt());
        boolean pwdFlag = StringUtils.equals(userPwd, user.getPassword());
        if (pwdFlag) {
            log.info("用户名密码正确");
            //屏蔽用户重要信息
            user.setPassword(null);
            user.setSalt(null);
            return user;
        }
        log.info("密码错误");
        return null;
    }

}