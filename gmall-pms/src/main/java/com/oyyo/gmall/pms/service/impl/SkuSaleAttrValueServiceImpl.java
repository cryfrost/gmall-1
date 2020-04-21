package com.oyyo.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.dao.SkuSaleAttrValueDao;
import com.oyyo.gmall.pms.entity.SkuSaleAttrValue;
import com.oyyo.gmall.pms.service.SkuSaleAttrValueService;
import org.springframework.stereotype.Service;

@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValue> implements SkuSaleAttrValueService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuSaleAttrValue> page = this.page(
                new Query<SkuSaleAttrValue>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageVo(page);
    }

}