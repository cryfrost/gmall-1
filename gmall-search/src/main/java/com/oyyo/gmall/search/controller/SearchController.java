package com.oyyo.gmall.search.controller;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.search.entity.SearchParamEntity;
import com.oyyo.gmall.search.service.SearchService;
import com.oyyo.gmall.search.vo.SearchResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public Resp<SearchResponseVO> search(SearchParamEntity searchParamEntity) throws IOException {
        System.out.println("test search");
        SearchResponseVO responseVO = searchService.search(searchParamEntity);
        return Resp.ok(responseVO);
    }

    @GetMapping("test")
    public Resp<String> test(){
        return Resp.ok("test");
    }

}
