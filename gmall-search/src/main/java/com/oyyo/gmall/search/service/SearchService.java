package com.oyyo.gmall.search.service;

import com.oyyo.gmall.search.entity.SearchParamEntity;
import com.oyyo.gmall.search.vo.SearchResponseVO;

import java.io.IOException;

public interface SearchService {
    /**
     * 商品搜索
     * @param searchParamEntity
     * @return
     */
    SearchResponseVO search(SearchParamEntity searchParamEntity) throws IOException;
}
