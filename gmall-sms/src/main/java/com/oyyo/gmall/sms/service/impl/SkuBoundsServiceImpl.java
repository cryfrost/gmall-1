package com.oyyo.gmall.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.sms.dao.SkuBoundsDao;
import com.oyyo.gmall.sms.dao.SkuFullReductionDao;
import com.oyyo.gmall.sms.dao.SkuLadderDao;
import com.oyyo.gmall.sms.entity.SkuBoundsEntity;
import com.oyyo.gmall.sms.entity.SkuFullReductionEntity;
import com.oyyo.gmall.sms.entity.SkuLadderEntity;
import com.oyyo.gmall.sms.service.SkuBoundsService;
import com.oyyo.gmall.sms.vo.SkuSaleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("skuBoundsService")
public class SkuBoundsServiceImpl extends ServiceImpl<SkuBoundsDao, SkuBoundsEntity> implements SkuBoundsService {

    @Autowired
    private SkuLadderDao skuLadderDao;
    @Autowired
    private SkuFullReductionDao skuFullReductionDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuBoundsEntity> page = this.page(
                new Query<SkuBoundsEntity>().getPage(params),
                new QueryWrapper<SkuBoundsEntity>()
        );

        return new PageVo(page);
    }
    @Transactional
    @Override
    @Transactional
    public void saveSaleInfo(SkuSaleVO skuSaleVO) {
        //保存营销数据
        SkuBoundsEntity skuBoundsEntity = new SkuBoundsEntity();
        skuBoundsEntity.setSkuId(skuSaleVO.getSkuId());
        skuBoundsEntity.setGrowBounds(skuSaleVO.getGrowBounds());
        skuBoundsEntity.setBuyBounds(skuSaleVO.getBuyBounds());
        List<Integer> work = skuSaleVO.getWork();
        skuBoundsEntity.setWork(work.get(3) * 1 + work.get(2) * 2 + work.get(1) * 4 + work.get(0) * 8);
        save(skuBoundsEntity);

        //保存打折信息
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuSaleVO.getSkuId());
        skuLadderEntity.setPrice(skuSaleVO.getPrice());
        skuLadderEntity.setFullCount(skuSaleVO.getFullCount());
        skuLadderEntity.setAddOther(skuSaleVO.getAddOther());
        skuLadderEntity.setDiscount(skuSaleVO.getDiscount());

        skuLadderDao.insert(skuLadderEntity);

        //保存满减信息
        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
        reductionEntity.setAddOther(skuSaleVO.getAddOther());
        reductionEntity.setReducePrice(skuSaleVO.getReducePrice());
        reductionEntity.setFullPrice(skuSaleVO.getFullPrice());
        reductionEntity.setSkuId(skuSaleVO.getSkuId());

        skuFullReductionDao.insert(reductionEntity);
    }
}