package com.oyyo.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.dao.BrandDao;
import com.oyyo.gmall.pms.entity.Brand;
import com.oyyo.gmall.pms.service.BrandService;
import org.springframework.stereotype.Service;

@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, Brand> implements BrandService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<Brand> page = this.page(
                new Query<Brand>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageVo(page);
    }

}