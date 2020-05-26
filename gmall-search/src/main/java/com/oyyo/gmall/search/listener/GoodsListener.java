package com.oyyo.gmall.search.listener;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.pms.entity.*;
import com.oyyo.gmall.search.entity.GoodsEntity;
import com.oyyo.gmall.search.entity.SearchAttr;
import com.oyyo.gmall.search.feign.GmallPmsClient;
import com.oyyo.gmall.search.feign.GmallWmsClient;
import com.oyyo.gmall.search.repository.GoodsRepository;
import com.oyyo.gmall.wms.entity.WareSkuEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 监听goods
 */
@Component
public class GoodsListener {
    private static final Logger LOG = LoggerFactory.getLogger(GoodsListener.class);

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GmallPmsClient pmsClient;
    @Autowired
    private GmallWmsClient wmsClient;

    @RabbitListener(bindings =@QueueBinding(
         value = @Queue(value = "${item.rabbitmq.queuevalue}",durable = "true"),
         exchange = @Exchange(value = "${item.rabbitmq.exchange}",type = ExchangeTypes.TOPIC,ignoreDeclarationExceptions = "true"),
         key = {"${item.rabbitmq.routingKey}"}
    ))
    @RabbitHandler
    public void listenerGoods(Long spuId){
        LOG.info("进入listenerGoods,spuId为：" + spuId);
        LOG.info("${item.rabbitmq.queuevalue}");
        Resp<List<SkuInfoEntity>> skusResp = pmsClient.querySkusBySpuId(spuId);
        List<SkuInfoEntity> skuInfoEntities = skusResp.getData();
        if (!CollectionUtils.isEmpty(skuInfoEntities)) {
            //吧sku转换为 goods
            List<GoodsEntity> goodsEntityList = skuInfoEntities.stream().map(skuInfoEntity -> {
                GoodsEntity goodsEntity = new GoodsEntity();
                //查询搜索属性及值
                Resp<List<ProductAttrValueEntity>> attrResp = pmsClient.querySearchAttrValueBySpuId(spuId);
                List<ProductAttrValueEntity> attrValueEntities = attrResp.getData();
                if (!CollectionUtils.isEmpty(attrValueEntities)) {
                    List<SearchAttr> searchAttrs = attrValueEntities.stream().map(productAttrValueEntity -> {
                        SearchAttr searchAttr = new SearchAttr();
                        searchAttr.setAttrId(productAttrValueEntity.getAttrId());
                        searchAttr.setAttrName(productAttrValueEntity.getAttrName());
                        searchAttr.setAttrValue(productAttrValueEntity.getAttrValue());
                        return searchAttr;
                    }).collect(Collectors.toList());
                    goodsEntity.setAttrs(searchAttrs);
                }
                //查询品牌

                Resp<BrandEntity> brandEntityResp = pmsClient.queryBrandById(skuInfoEntity.getBrandId());
                BrandEntity brandEntity = brandEntityResp.getData();
                if (brandEntity != null) {
                    goodsEntity.setBrandId(skuInfoEntity.getBrandId());
                    goodsEntity.setBrandName(brandEntity.getName());
                }
                //查询分类
                Resp<CategoryEntity> categoryEntityResp = pmsClient.queryCategoryById(skuInfoEntity.getCatalogId());
                CategoryEntity categoryEntity = categoryEntityResp.getData();
                if (categoryEntity != null) {
                    goodsEntity.setCategoryId(skuInfoEntity.getCatalogId());
                    goodsEntity.setCategoryName(categoryEntity.getName());
                }

                Resp<SpuInfoEntity> spuInfoEntityResp = pmsClient.querySpuById(spuId);
                SpuInfoEntity spu = spuInfoEntityResp.getData();
                if (spu != null) {
                    goodsEntity.setCreateTime(spu.getCreateTime());
                }
//                        goodsEntity.setPic(skuInfoEntity.getSkuDefaultImg());

                //查询skuImage 设置查询结果图片
                Resp<String> skuImgs = pmsClient.querySkuImgsBySkuId(skuInfoEntity.getSkuId());
                //接口返回  用 ，分隔的字符串  前端需要用，号解析
                goodsEntity.setPic(skuImgs.getData());
                //设置价格
                goodsEntity.setPrice(skuInfoEntity.getPrice().doubleValue());
                goodsEntity.setSale(100L);
                goodsEntity.setSkuId(skuInfoEntity.getSkuId());
                //查询库存
                Resp<List<WareSkuEntity>> listResp = wmsClient.queryWareSkuBySkuId(skuInfoEntity.getSkuId());
                List<WareSkuEntity> data = listResp.getData();
                if (!CollectionUtils.isEmpty(data)) {
                    boolean flag = data.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() > 0);
                    goodsEntity.setStore(flag);
                }else {
                    goodsEntity.setStore(false);
                }
                goodsEntity.setTitle(skuInfoEntity.getSkuTitle());


                return goodsEntity;
            }).collect(Collectors.toList());
            goodsRepository.saveAll(goodsEntityList);
            LOG.info("goodsEntityList___" + goodsEntityList.toString());
            LOG.info("数据同步完成,spuId为：" + spuId);
        }
    }
}
