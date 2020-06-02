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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author likui
 */
@Service("memberService")
@Slf4j
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

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

}