package com.oyyo.gmall.search.service.impl;

import com.oyyo.gmall.search.entity.SearchParamEntity;
import com.oyyo.gmall.search.service.SearchService;
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
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
     */
    @Override
    public void search(SearchParamEntity searchParamEntity) throws IOException {
        //构建dsl语句
        SearchRequest searchRequest = buildQueryDSL(searchParamEntity);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(searchResponse);

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
                boolQuery.must(QueryBuilders.nestedQuery("attrs", boolQuery, ScoreMode.None));
                boolQueryBuilder.filter(boolQuery);
            }
        }

        //价格区间过滤
        Integer priceFrom = searchParamEntity.getPriceFrom();
        Integer priceTo = searchParamEntity.getPriceTo();
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("prive");
        if (priceFrom != null) {
            rangeQueryBuilder .gte(priceFrom);
        }
        if (priceTo != null) {
            rangeQueryBuilder .lte(priceFrom);
        }
        boolQueryBuilder.filter(rangeQueryBuilder);

        sourceBuilder.query(boolQueryBuilder);


        //构建分页
        Integer pageNum = searchParamEntity.getPageNum();
        Integer pageSize = searchParamEntity.getPageSize();
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
                .terms("categoryIdAgg").field("brandId")
                .subAggregation(AggregationBuilders
                        .terms("categoryNameAgg").field("categoryName")
                );
        sourceBuilder.aggregation(cateGoryAggregation);
        //搜索规格属性聚合
        NestedAggregationBuilder attrAggregation = AggregationBuilders.nested("attrAgg", "attrs")
                .subAggregation(AggregationBuilders.terms("attrIdAgg").field("attrsAttrId")
                        .subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName"))
                        .subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue")));

        sourceBuilder.aggregation(attrAggregation);
        System.out.println(sourceBuilder.toString());
        //索引库
        SearchRequest searchRequest = new SearchRequest("goods");

        searchRequest.source(sourceBuilder);


        return searchRequest;
    }

}
