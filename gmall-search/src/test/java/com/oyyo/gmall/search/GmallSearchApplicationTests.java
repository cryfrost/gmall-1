package com.oyyo.gmall.search;

import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.pms.entity.*;
import com.oyyo.gmall.search.entity.GoodsEntity;
import com.oyyo.gmall.search.entity.SearchAttr;
import com.oyyo.gmall.search.feign.GmallPmsClient;
import com.oyyo.gmall.search.feign.GmallWmsClient;
import com.oyyo.gmall.search.repository.GoodsRepository;
import com.oyyo.gmall.wms.entity.WareSkuEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class GmallSearchApplicationTests {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GmallPmsClient pmsClient;
    @Autowired
    private GmallWmsClient wmsClient;

    @Test
    void contextLoads() {
        //设置索引信息(绑定实体类)  返回IndexOperations
        IndexOperations indexOperations = restTemplate.indexOps(GoodsEntity.class);
        //创建索引库
        indexOperations.create();
        indexOperations.putMapping(indexOperations.createMapping());
        System.out.println(indexOperations.getMapping());

    }

    @Test
    void importData(){
        Long pageNum = 1l;
        Long pageSize = 100l;

        do {
            //分页查询spu
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setPage(pageNum);
            queryCondition.setLimit(pageSize);
            Resp<List<SpuInfoEntity>> spuResp = pmsClient.querySpusByPage(queryCondition);
            List<SpuInfoEntity> spus = spuResp.getData();

            //遍历spu 查询sku
            spus.forEach(spuInfoEntity -> {
                Resp<List<SkuInfoEntity>> skusResp = pmsClient.querySkusBySpuId(spuInfoEntity.getId());
                List<SkuInfoEntity> skuInfoEntities = skusResp.getData();
                if (!CollectionUtils.isEmpty(skuInfoEntities)) {
                    //吧sku转换为 goods
                    List<GoodsEntity> goodsEntityList = skuInfoEntities.stream().map(skuInfoEntity -> {
                        GoodsEntity goodsEntity = new GoodsEntity();
                        //查询搜索属性及值
                        Resp<List<ProductAttrValueEntity>> attrResp = pmsClient.querySearchAttrValueBySpuId(spuInfoEntity.getId());
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

                        goodsEntity.setCreateTime(spuInfoEntity.getCreateTime());
                        goodsEntity.setPic(skuInfoEntity.getSkuDefaultImg());
                        goodsEntity.setPrice(skuInfoEntity.getPrice().doubleValue());
                        goodsEntity.setSale(100l);
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
                }

            });

            //把 sku 转换成 goods 对象

            //导入索引库
            pageSize = (long)spus.size();
            pageNum++;
        }while (pageSize == 100);
    }

}
