package com.oyyo.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.dao.SkuImagesDao;
import com.oyyo.gmall.pms.entity.SkuImagesEntity;
import com.oyyo.gmall.pms.service.SkuImagesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * @author likui
 */
@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    @Autowired
    private SkuImagesDao skuImagesDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuImagesEntity> page = this.page(
                new Query<SkuImagesEntity>().getPage(params),
                new QueryWrapper<SkuImagesEntity>()
        );

        return new PageVo(page);
    }

    /**
     * 查询 sku 图片
     * @param skuId
     * @return
     */
    @Override
    public String querySkuImgsBySkuId(String skuId) {

        List<String> skuImgs = skuImagesDao.querySkuImgsBySkuId(skuId);
        skuImgs.forEach(System.out::println);
        if (!CollectionUtils.isEmpty(skuImgs)) {
            return StringUtils.join(skuImgs, ",");
        }
        return "";
    }

}