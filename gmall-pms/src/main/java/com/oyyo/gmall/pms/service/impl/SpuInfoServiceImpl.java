package com.oyyo.gmall.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.gmall.pms.dao.SkuInfoDao;
import com.oyyo.gmall.pms.dao.SpuInfoDao;
import com.oyyo.gmall.pms.dao.SpuInfoDescDao;
import com.oyyo.gmall.pms.entity.*;
import com.oyyo.gmall.pms.feign.GmallSmsClient;
import com.oyyo.gmall.pms.service.ProductAttrValueService;
import com.oyyo.gmall.pms.service.SkuImagesService;
import com.oyyo.gmall.pms.service.SkuSaleAttrValueService;
import com.oyyo.gmall.pms.service.SpuInfoService;
import com.oyyo.gmall.pms.vo.BaseAttrVO;
import com.oyyo.gmall.pms.vo.SkuInfoVO;
import com.oyyo.gmall.pms.vo.SpuInfoVo;
import com.oyyo.gmall.sms.vo.SkuSaleVO;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDao spuInfoDao;
    @Autowired
    private SpuInfoDescDao spuInfoDescDao;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private SkuInfoDao skuInfoDao;
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private GmallSmsClient gmallSmsClient;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo querySpuByPage(QueryCondition queryCondition, Long catId) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        //判断分类是否为0
        if (catId != 0) {
            wrapper.eq("catalog_id",catId);
        }
        String key = queryCondition.getKey();
        //判断关键字是否为空
        //select * from pms_spu_info where catalog_id = 225 and (id = '华为' or spu_name like '%华为%')
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(t -> t.eq("id",key).or().like("spu_name",key));
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(queryCondition),
                wrapper
        );

        return new PageVo(page);

    }

    /**
     * 保存 商品信息
     * @param spuInfoVo
     */
    @GlobalTransactional
    @Override
    public void saveGoodsInfo(SpuInfoVo spuInfoVo) {

        //1 保存spu相关信息
        Long spuId = saveSpuInfo(spuInfoVo);
        saveSpuInfoDesc(spuInfoVo, spuId);
        saveBaseAttr(spuInfoVo, spuId);

        // 2 保存相关 sku 相关信息
        saveSkuAndSale(spuInfoVo, spuId);

    }

    private void saveSkuAndSale(SpuInfoVo spuInfoVo, Long spuId) {
        List<SkuInfoVO> skus = spuInfoVo.getSkus();
        if (CollectionUtils.isEmpty(skus)) {
            return;
        }
        skus.forEach(skuInfoVO -> {
            skuInfoVO.setSpuId(spuId);
            skuInfoVO.setSkuCode(UUID.randomUUID().toString());
            skuInfoVO.setBrandId(spuInfoVo.getBrandId());
            List<String> skuInfoVOImages = skuInfoVO.getImages();
            //设置默认图片
            if (!CollectionUtils.isEmpty(skuInfoVOImages)) {
                skuInfoVO.setSkuDefaultImg(StringUtils.isNotBlank(skuInfoVO.getSkuDefaultImg()) ? skuInfoVO.getSkuDefaultImg() : skuInfoVOImages.get(0));
            }
            skuInfoDao.insert(skuInfoVO);
            //2. 2 保存pms_sku_images
            Long skuId = skuInfoVO.getSkuId();
            if (!CollectionUtils.isEmpty(skuInfoVOImages)) {
                List<SkuImagesEntity> skuImagesEntities = skuInfoVOImages.stream().map(image -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setImgUrl(image);
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setDefaultImg(StringUtils.equals(skuInfoVO.getSkuDefaultImg(), image) ? 1 : 0);
                    return skuImagesEntity;
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntities);
            }

            List<SkuSaleAttrValueEntity> saleAttrs = skuInfoVO.getSaleAttrs();
            if (!CollectionUtils.isEmpty(saleAttrs)) {
                saleAttrs.forEach(skuSaleAttrValueEntity -> skuSaleAttrValueEntity.setSkuId(skuId));
                skuSaleAttrValueService.saveBatch(saleAttrs);
            }
            //远程调用 feign 保存商品相关营销信息 3张表
            SkuSaleVO skuSaleVO = new SkuSaleVO();
            BeanUtils.copyProperties(skuInfoVO,skuSaleVO);
            skuSaleVO.setSkuId(skuId);
            gmallSmsClient.saveSaleInfo(skuSaleVO);

        });
    }

    private void saveBaseAttr(SpuInfoVo spuInfoVo, Long spuId) {
        //|_ 保存product_attr_value
        List<BaseAttrVO> baseAttrs = spuInfoVo.getBaseAttrs();
        if (!CollectionUtils.isEmpty(baseAttrs)) {
            //强转
            List<ProductAttrValueEntity> attrValueEntities = baseAttrs.stream().map(
                    baseAttrVO -> {
                        ProductAttrValueEntity attrValueEntity = baseAttrVO;
                        attrValueEntity.setSpuId(spuId);
                        return attrValueEntity;
                    }).collect(Collectors.toList());
            productAttrValueService.saveBatch(attrValueEntities);
        }
    }

    private void saveSpuInfoDesc(SpuInfoVo spuInfoVo, Long spuId) {
        //|_ 保存spuInfo_desc
        List<String> spuImages = spuInfoVo.getSpuImages();
        if (!CollectionUtils.isEmpty(spuImages)) {
            SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
            spuInfoDescEntity.setSpuId(spuId);
            spuInfoDescEntity.setDecript(StringUtils.join(spuImages,","));
            spuInfoDescDao.insert(spuInfoDescEntity);
        }
    }

    private Long saveSpuInfo(SpuInfoVo spuInfoVo) {
        //|_ 保存spuInfo
        spuInfoVo.setCreateTime(new Date());
        spuInfoVo.setUodateTime(spuInfoVo.getCreateTime());
        save(spuInfoVo);
        return spuInfoVo.getId();
    }

}