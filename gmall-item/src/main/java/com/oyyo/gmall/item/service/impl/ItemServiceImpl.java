package com.oyyo.gmall.item.service.impl;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.item.feign.GmallPmsClient;
import com.oyyo.gmall.item.feign.GmallSmsClient;
import com.oyyo.gmall.item.feign.GmallWmsClient;
import com.oyyo.gmall.item.service.ItemService;
import com.oyyo.gmall.item.vo.ItemVO;
import com.oyyo.gmall.pms.entity.*;
import com.oyyo.gmall.pms.vo.ItemGroupVO;
import com.oyyo.gmall.sms.vo.SalseVO;
import com.oyyo.gmall.wms.entity.WareSkuEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName: ItemServiceImpl
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-1 14:35
 * @Version: 1.0
 */
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private GmallPmsClient pmsClient;
    @Autowired
    private GmallSmsClient smsClient;
    @Autowired
    private GmallWmsClient wmsClient;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public ItemVO queryItemVO(Long skuId) {

        ItemVO itemVO = new ItemVO();

        //根据skuId 查询 sku ，设置sku 相关字段
        itemVO.setSkuId(skuId);

        //使用线程的异步编排,优化查询效率，多线程
        CompletableFuture<Object> skuCompletable = CompletableFuture.supplyAsync(() -> {

            Resp<SkuInfoEntity> skuResp = pmsClient.querySkuBySkuId(skuId);
            SkuInfoEntity skuInfoEntity = skuResp.getData();
            if (skuInfoEntity == null) {
                return itemVO;
            }
            itemVO.setSkuTitle(skuInfoEntity.getSkuTitle());
            itemVO.setSkuSubTitle(skuInfoEntity.getSkuSubtitle());
            itemVO.setPrice(skuInfoEntity.getPrice());
            itemVO.setWeight(skuInfoEntity.getWeight());
            //根据 sku 中的 spuId，查询 spu 并设置 spu 相关字段
            //获取spuid
            itemVO.setSpuId(skuInfoEntity.getSpuId());

            return skuInfoEntity;
        }, threadPoolExecutor);

        CompletableFuture<Void> spuCompletable = skuCompletable.thenAcceptAsync(sku -> {
            //根据spuId查询spu
            Resp<SpuInfoEntity> spuResp = pmsClient.querySpuById(((SkuInfoEntity) sku).getSpuId());
            SpuInfoEntity spuInfoEntity = spuResp.getData();
            if (spuInfoEntity == null) {
                itemVO.setSpuName(spuInfoEntity.getSpuName());
            }
        }, threadPoolExecutor);

        CompletableFuture<Void> skuImagesCompletableFuture = CompletableFuture.runAsync(() -> {
            //根据skuId查询sku的图片列表 并设置相关字段
            Resp<List<SkuImagesEntity>> skuImgesResp = pmsClient.querySkuImagesBySkuId(skuId);
            List<SkuImagesEntity> skuImagesEntities = skuImgesResp.getData();
            itemVO.setSkuImagesEntities(skuImagesEntities);
        }, threadPoolExecutor);

        skuCompletable.thenAcceptAsync(sku -> {
            //根据 sku 中的 categoryId ，brandId 查询品牌和分类 并设置相关字段
            //查询品牌
            Resp<BrandEntity> brandEntityResp = pmsClient.queryBrandById(((SkuInfoEntity) sku).getBrandId());
            BrandEntity brandEntity = brandEntityResp.getData();
            itemVO.setBrandEntity(brandEntity);
        }, threadPoolExecutor);

        CompletableFuture<Void> cateCompletableFuture = skuCompletable.thenAcceptAsync(sku -> {
            //查询分类
            Resp<CategoryEntity> categoryEntityResp = pmsClient.queryCategoryById(((SkuInfoEntity) sku).getCatalogId());
            CategoryEntity categoryEntity = categoryEntityResp.getData();
            itemVO.setCategoryEntity(categoryEntity);
        }, threadPoolExecutor);

        CompletableFuture<Void> skuSalseCompletableFuture = CompletableFuture.runAsync(() -> {
            // 根据skuId -> sms  查询营销信息
            Resp<List<SalseVO>> skuSalseResp = smsClient.querySkuSalseBySkuId(skuId);
            List<SalseVO> salseVOList = skuSalseResp.getData();

            itemVO.setSales(salseVOList);
        }, threadPoolExecutor);

        CompletableFuture<Void> wareCompletableFuture = CompletableFuture.runAsync(() -> {
            //根据skuId -> wms 查询库存信息
            Resp<List<WareSkuEntity>> wareResp = wmsClient.queryWareSkuBySkuId(skuId);
            List<WareSkuEntity> wareSkuEntities = wareResp.getData();

            itemVO.setStore(wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() > 0));
        }, threadPoolExecutor);

        CompletableFuture<Void> salesAttrCompletableFuture = skuCompletable.thenAcceptAsync(sku -> {
            //根据 spuId -> 查询 所有 skuId -> 再去查询 所有销售属性
            Resp<List<SkuSaleAttrValueEntity>> salesResp = pmsClient.querySkuSalesAttrValuesBySpuId(((SkuInfoEntity) sku).getSpuId());
            List<SkuSaleAttrValueEntity> saleAttrValueEntities = salesResp.getData();

            itemVO.setSaleAttrs(saleAttrValueEntities);
        }, threadPoolExecutor);

        CompletableFuture<Void> spuDescCompletableFuture = skuCompletable.thenAcceptAsync(sku -> {
            //根据 spuId 查询描述 （海报）信息
            Resp<SpuInfoDescEntity> spuInfoDescEntityResp = pmsClient.querySpuDescBySpuId(((SkuInfoEntity) sku).getSpuId());
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescEntityResp.getData();
            if (spuInfoDescEntity != null) {
                String[] spuInfoDesc = StringUtils.split(spuInfoDescEntity.getDecript(), ",");
                itemVO.setPosterImgs(Arrays.asList(spuInfoDesc));
            }
        }, threadPoolExecutor);

        CompletableFuture<Void> itmeCompletableFuture = skuCompletable.thenAcceptAsync(sku -> {
            //根据 spuId 和 cateId 查询 组 及组下的规格参数（值）
            Resp<List<ItemGroupVO>> itemGroupResp = pmsClient.queryItemGroupVOByCidAndSpuId(((SkuInfoEntity) sku).getCatalogId(), ((SkuInfoEntity) sku).getSpuId());
            List<ItemGroupVO> itemGroupVOS = itemGroupResp.getData();
            itemVO.setItemGroupVOS(itemGroupVOS);
        }, threadPoolExecutor);
        //阻塞全部任务，所有任务完成之后返回结果
        CompletableFuture.allOf(spuCompletable,skuImagesCompletableFuture,cateCompletableFuture,
                skuSalseCompletableFuture,wareCompletableFuture,salesAttrCompletableFuture,
                spuDescCompletableFuture,itmeCompletableFuture).join();
        return itemVO;
    }

}