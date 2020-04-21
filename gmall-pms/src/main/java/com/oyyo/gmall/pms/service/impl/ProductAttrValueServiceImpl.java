package com.oyyo.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.dao.ProductAttrValueDao;
import com.oyyo.gmall.pms.entity.ProductAttrValue;
import com.oyyo.gmall.pms.service.ProductAttrValueService;
import org.springframework.stereotype.Service;

@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValue> implements ProductAttrValueService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<ProductAttrValue> page = this.page(
                new Query<ProductAttrValue>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageVo(page);
    }

}