package com.oyyo.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.dao.SpuImagesDao;
import com.oyyo.gmall.pms.entity.SpuImages;
import com.oyyo.gmall.pms.service.SpuImagesService;
import org.springframework.stereotype.Service;

@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImages> implements SpuImagesService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuImages> page = this.page(
                new Query<SpuImages>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageVo(page);
    }

}