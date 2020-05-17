package com.oyyo.gmall.search.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @ClassName: SearchAttr
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-5-16 16:35
 * @Version: 1.0
 */
@Data
public class SearchAttr {
    @Field(type = FieldType.Long)
    private Long attrId;
    @Field(type = FieldType.Keyword)
    private String attrName;
    @Field(type = FieldType.Keyword)
    private String attrValue;
}