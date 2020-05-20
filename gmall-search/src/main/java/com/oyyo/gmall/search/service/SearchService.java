package com.oyyo.gmall.search.service;

import com.oyyo.gmall.search.entity.SearchParamEntity;

import java.io.IOException;

public interface SearchService {
    /**
     * 商品搜索
     * @param searchParamEntity
     */
    void search(SearchParamEntity searchParamEntity) throws IOException;
}
