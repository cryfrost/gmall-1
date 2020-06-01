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
import com.oyyo.gmall.sms.vo.SalseVO;
import com.oyyo.gmall.sms.vo.SkuSaleVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Override
    public List<SalseVO> querySkuSalseBySkuId(Long skuId) {

        List<SalseVO> salseVOS = new ArrayList<>();

        //查询积分信息
        SalseVO bounsVO = new SalseVO();
        SkuBoundsEntity skuBoundsEntity = getOne(new QueryWrapper<SkuBoundsEntity>().eq("sku_id", skuId));
        if (skuBoundsEntity != null) {
            bounsVO.setType("积分");
            StringBuffer stringBuffer = new StringBuffer();
            if (skuBoundsEntity.getGrowBounds() != null && skuBoundsEntity.getGrowBounds().intValue() > 0) {
                stringBuffer.append("成长积分 ↑：" + skuBoundsEntity.getGrowBounds());
            }
            if (skuBoundsEntity.getBuyBounds() != null && skuBoundsEntity.getBuyBounds().intValue() > 0) {
                if (StringUtils.isNoneBlank(stringBuffer)) {
                    stringBuffer.append("，");
                }
                stringBuffer.append("赠送积分 ↑：" + skuBoundsEntity.getBuyBounds());
            }
            bounsVO.setDesc(stringBuffer.toString());
            salseVOS.add(bounsVO);
        }

        //查询打折信息
        SkuLadderEntity skuLadderEntity = skuLadderDao.selectOne(new QueryWrapper<SkuLadderEntity>().eq("sku_id", skuId));
        if (skuLadderEntity != null) {
            SalseVO ladder = new SalseVO();
            ladder.setType("优惠");
            ladder.setDesc("满" + skuLadderEntity.getFullCount() + "件，打"
                    + skuLadderEntity.getDiscount().divide(new BigDecimal(10)) + "折！");
            salseVOS.add(ladder);
        }

        //查询满减信息
        SkuFullReductionEntity reductionEntity = skuFullReductionDao.selectOne(new QueryWrapper<SkuFullReductionEntity>().eq("sku_id", skuId));
        if (reductionEntity != null) {
            SalseVO reductionVO = new SalseVO();
            reductionVO.setType("满减");
            reductionVO.setDesc("满" + reductionEntity.getFullPrice().divide(new BigDecimal(100))
                    + "元，减" + reductionEntity.getReducePrice().divide(new BigDecimal(100)) + "元");
            salseVOS.add(reductionVO);
        }

        return salseVOS;
    }
}