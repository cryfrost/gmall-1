package com.oyyo.gmall.search.entity;

import lombok.Data;

@Data
public class SearchParamEntity {
    private String[] catelog3;//三级分类id
    String[] brand;//品牌id
    String keyword;//检索的关键字
    String order;//0 综合排序 1 销量 2 价格
    Integer pageNum = 1;//页码
    String[] props;//页面提交的数组
    Integer pageSize;//每页数量
    Integer priceFrom;//价格区间开始
    Integer priceTo;//价格区间结束


}
