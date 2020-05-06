package com.oyyo.gmall.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.sms.dao.SpuBoundsDao;
import com.oyyo.gmall.sms.entity.SkuBoundsEntity;
import com.oyyo.gmall.sms.entity.SpuBoundsEntity;
import com.oyyo.gmall.sms.service.SpuBoundsService;
import com.oyyo.gmall.sms.vo.SkuSaleVO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("spuBoundsService")
public class SpuBoundsServiceImpl extends ServiceImpl<SpuBoundsDao, SpuBoundsEntity> implements SpuBoundsService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuBoundsEntity> page = this.page(
                new Query<SpuBoundsEntity>().getPage(params),
                new QueryWrapper<SpuBoundsEntity>()
        );

        return new PageVo(page);
    }



}