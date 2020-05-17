package com.oyyo.gmall.search.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: GoodsEntity
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-5-16 16:12
 * @Version: 1.0
 */
@Data
@Document(indexName = "goods",shards = 3,replicas = 2)
public class GoodsEntity {

    @Id
    private Long skuId;
    @Field(type = FieldType.Keyword,index = false)
    private String pic;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Double)
    private Double price;
    //销量
    @Field(type = FieldType.Long)
    private Long sale;
    @Field(type = FieldType.Boolean)
    private Boolean store;
    @Field(type = FieldType.Date)
    private Date createTime;
    @Field(type = FieldType.Long)
    private Long brandId;
    @Field(type = FieldType.Keyword)
    private String brandName;
    @Field(type = FieldType.Long)
    private Long categoryId;
    @Field(type = FieldType.Keyword)
    private String categoryName;
    @Field(type = FieldType.Nested)
    private List<SearchAttr> attrs;

}