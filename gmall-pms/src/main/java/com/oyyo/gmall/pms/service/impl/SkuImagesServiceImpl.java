package com.oyyo.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.dao.SkuImagesDao;
import com.oyyo.gmall.pms.entity.SkuImages;
import com.oyyo.gmall.pms.service.SkuImagesService;
import org.springframework.stereotype.Service;

@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImages> implements SkuImagesService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuImages> page = this.page(
                new Query<SkuImages>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageVo(page);
    }

}