package com.oyyo.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.dao.CommentReplayDao;
import com.oyyo.gmall.pms.entity.CommentReplay;
import com.oyyo.gmall.pms.service.CommentReplayService;
import org.springframework.stereotype.Service;

@Service("commentReplayService")
public class CommentReplayServiceImpl extends ServiceImpl<CommentReplayDao, CommentReplay> implements CommentReplayService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<CommentReplay> page = this.page(
                new Query<CommentReplay>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageVo(page);
    }

}