package com.oyyo.gmall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.oyyo.gmall.search.entity.GoodsEntity;
import com.oyyo.gmall.search.entity.SearchParamEntity;
import com.oyyo.gmall.search.service.SearchService;
import com.oyyo.gmall.search.vo.SearchResponseAttrVO;
import com.oyyo.gmall.search.vo.SearchResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 搜索
     *
     * 
     * @param searchParamEntity
     * @throws IOException
     * @return
     */
    @Override
    public SearchResponseVO search(SearchParamEntity searchParamEntity) throws IOException {
        if (searchParamEntity == null) {
            return new SearchResponseVO();
        }
        //构建dsl语句
        SearchRequest searchRequest = buildQueryDSL(searchParamEntity);
//        System.out.println(searchRequest.getDescription());
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        System.out.println(searchResponse);

        SearchResponseVO responseVO = parseSearchResult(searchResponse);
        responseVO.setPageNum(searchParamEntity.getPageNum());
        responseVO.setPageSize(searchParamEntity.getPageSize());

        return responseVO;
    }

    /**
     * 解析 searchResponse
     * @param searchResponse
     * @return
     */
    private SearchResponseVO parseSearchResult(SearchResponse searchResponse){
        SearchResponseVO responseVO = new SearchResponseVO();
        //命中结果
        SearchHits hits = searchResponse.getHits();
        //设置命中总记录数
        responseVO.setTotal(hits.getTotalHits().value);
        //解析品牌聚合结果集
        SearchResponseAttrVO brand = new SearchResponseAttrVO();
        brand.setName("品牌");
        Map<String, Aggregation> aggregationMap = searchResponse.getAggregations().asMap();
        ParsedLongTerms brandIdAgg = (ParsedLongTerms) aggregationMap.get("brandIdAgg");
        List<String> brandValues = brandIdAgg.getBuckets().stream().map(bucket -> {
            Map<String, String> map = new HashMap<>();
            //获取品牌id
            map.put("id",bucket.getKeyAsString());
            //获取品牌名称  通过子聚合获取
            Map<String, Aggregation> brandIdSubMap = bucket.getAggregations().asMap();
            ParsedStringTerms brandNameAgg = (ParsedStringTerms) brandIdSubMap.get("brandNameAgg");
            String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();

            map.put("name",brandName);
            return JSON.toJSONString(map);

        }).collect(Collectors.toList());
        brand.setValue(brandValues);
        responseVO.setBrand(brand);

        //分类聚合结果
        ParsedLongTerms categoryIdAgg = (ParsedLongTerms) aggregationMap.get("categoryIdAgg");
        List<String> catelogValues = categoryIdAgg.getBuckets().stream().map(bucket -> {
            Map<String, String> map = new HashMap<>();
            //获取品牌id
            map.put("id",bucket.getKeyAsString());
            //获取品牌名称  通过子聚合获取
            Map<String, Aggregation> categoryIdSubMap = bucket.getAggregations().asMap();
            ParsedStringTerms categoryNameAgg = (ParsedStringTerms) categoryIdSubMap.get("categoryNameAgg");
            String categoryName = categoryNameAgg.getBuckets().get(0).getKeyAsString();

            map.put("name",categoryName);
            return JSON.toJSONString(map);

        }).collect(Collectors.toList());
        SearchResponseAttrVO categoryVO = new SearchResponseAttrVO();
        categoryVO.setName("分类");
        categoryVO.setValue(catelogValues);
        responseVO.setCatelog(categoryVO);
        //从查询结果集 中 获取商品 集合
        SearchHit[] subHits = hits.getHits();
        //GoodsEntity  商品集合
        ArrayList<GoodsEntity> goodsEntities = new ArrayList<>();

        for (SearchHit subHit : subHits) {
            //hits 下面的 _source 中的数据
            String sourceAsString = subHit.getSourceAsString();
            //反序列化为 GoodsEntity 对象
            GoodsEntity goodsEntity = JSON.parseObject(sourceAsString, GoodsEntity.class);
            String title = subHit.getHighlightFields().get("title").getFragments()[0].string();
            goodsEntity.setTitle(title);
            goodsEntities.add(goodsEntity);
        }
        responseVO.setProducts(goodsEntities);

        //规格参数解析
        //获取嵌套聚合对象
        ParsedNested attrAgg = (ParsedNested) aggregationMap.get("attrAgg");
        //获取规格参数 id 聚合对象
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attrIdAgg");
        List<? extends Terms.Bucket> buckets = attrIdAgg.getBuckets();

        if (!CollectionUtils.isEmpty(buckets)) {
            List<SearchResponseAttrVO> searchResponseAttrVOS = buckets.stream().map(bucket -> {
                SearchResponseAttrVO responseAttrVO = new SearchResponseAttrVO();
                //设置规格参数 id
                responseAttrVO.setProductAttributeId(bucket.getKeyAsNumber().longValue());
                //设置规格参数 name
                ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attrNameAgg");
                String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
                responseAttrVO.setName(attrName);
                //设置规格参数值 列表
                ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attrValueAgg");
                List<? extends Terms.Bucket> attrValueBuckets = attrValueAgg.getBuckets();
                List<String> attrValues = attrValueBuckets.stream().map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());
                responseAttrVO.setValue(attrValues);
                return responseAttrVO;
            }).collect(Collectors.toList());
            responseVO.setAttrs(searchResponseAttrVOS);
        }

        return responseVO;
    }

    /**
     * //构建dsl语句
     * @param searchParamEntity
     * @return
     */
    private SearchRequest buildQueryDSL(SearchParamEntity searchParamEntity){
        //查询关键字
        String keyword = searchParamEntity.getKeyword();
        if (StringUtils.isEmpty(keyword)) {
            return null;
        }
        //查询条件的构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //构建查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("title",keyword).operator(Operator.AND));

        //构建品牌 过滤条件
        String[] brand = searchParamEntity.getBrand();
        if (brand != null && brand.length != 0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId",brand));
        }
        //构建分类 过滤条件
        String[] catelog3 = searchParamEntity.getCatelog3();
        if (catelog3 != null && catelog3.length != 0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("categoryId",catelog3));
        }

        //构建规格属性的嵌套过滤
        String[] props = searchParamEntity.getProps();
        if (props != null && props.length != 0) {
            for (String prop : props) {
                //验证参数是否合法
                // 以 ：分割    0-attrId 1-attrValue(xxx-xxx)
                String[] split = StringUtils.split(prop, ":");
                if (split == null || split.length != 2) {
                    continue;
                }
                //以 - 分割 处理 AttrValues
                String[] attrValues = StringUtils.split(split[1], "-");

                //构建嵌套查询
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                //构建嵌套查询的子查询
                BoolQueryBuilder subBoolQuery = QueryBuilders.boolQuery();
                //构建子查询中的过滤条件
                subBoolQuery.must(QueryBuilders.termQuery("attrs.attrId",split[0]));
                subBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue",attrValues));
                //把嵌套查询放入过滤器中
                boolQuery.must(QueryBuilders.nestedQuery("attrs", subBoolQuery, ScoreMode.None));
                boolQueryBuilder.filter(boolQuery);
            }
        }

        //价格区间过滤
        Integer priceFrom = searchParamEntity.getPriceFrom();
        Integer priceTo = searchParamEntity.getPriceTo();
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price");
        if (priceFrom != null) {
            rangeQueryBuilder .gte(priceFrom);
        }
        if (priceTo != null) {
            rangeQueryBuilder .lte(priceTo);
        }
        boolQueryBuilder.filter(rangeQueryBuilder);

        sourceBuilder.query(boolQueryBuilder);


        //构建分页
        Integer pageNum = searchParamEntity.getPageNum();
        Integer pageSize = searchParamEntity.getPageSize();
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        sourceBuilder.from((pageNum - 1)* pageSize);
        sourceBuilder.size(pageSize);

        //构建排序
        String order = searchParamEntity.getOrder();
        if (!StringUtils.isEmpty(order)) {
            String[] split = StringUtils.split(order, ":");
            if (split != null && split.length == 2) {
                String filed = new String();

                switch (split[0]){
                    case "1": filed="sale";break;
                    case "2": filed="price";break;
                    default:break;
                }
                sourceBuilder.sort(filed,StringUtils.equals("asc",split[1] )? SortOrder.ASC : SortOrder.DESC);
            }
        }

        //构建高亮
        sourceBuilder.highlighter(new HighlightBuilder().field("title").preTags("<em>").postTags("</em>"));

        //构建聚合 p品牌聚合
        TermsAggregationBuilder brandAggregation = AggregationBuilders.terms("brandIdAgg").field("brandId")
                .subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName"));
        sourceBuilder.aggregation(brandAggregation);

        //分类聚合
        TermsAggregationBuilder cateGoryAggregation = AggregationBuilders
                .terms("categoryIdAgg").field("categoryId")
                .subAggregation(AggregationBuilders
                        .terms("categoryNameAgg").field("categoryName")
                );
        sourceBuilder.aggregation(cateGoryAggregation);
        //搜索规格属性聚合
        NestedAggregationBuilder attrAggregation = AggregationBuilders.nested("attrAgg", "attrs")
                .subAggregation(AggregationBuilders.terms("attrIdAgg").field("attrs.attrId")
                        .subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName"))
                        .subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue")));

        sourceBuilder.aggregation(attrAggregation);
        System.out.println(sourceBuilder.toString());
        //结果集过滤
        sourceBuilder.fetchSource(new String[]{"skuId","pic","title","price"},null);

        //索引库
        SearchRequest searchRequest = new SearchRequest("goods");

        searchRequest.source(sourceBuilder);


        return searchRequest;
    }

}
