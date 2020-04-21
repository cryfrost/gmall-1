package com.oyyo.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.dao.SkuInfoDao;
import com.oyyo.gmall.pms.entity.SkuInfo;
import com.oyyo.gmall.pms.service.SkuInfoService;
import org.springframework.stereotype.Service;

@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfo> implements SkuInfoService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuInfo> page = this.page(
                new Query<SkuInfo>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageVo(page);
    }

}