package com.oyyo.gmall.index.controller;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.index.service.IndexService;
import com.oyyo.gmall.pms.entity.CategoryEntity;
import com.oyyo.gmall.pms.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("cates")
    public Resp<List<CategoryEntity>> queryLevel1Categories(){

        List<CategoryEntity> categoryEntities = indexService.queryLevel1Categories();
        return Resp.ok(categoryEntities);
    }

    @GetMapping("cates/{pid}")
    public Resp<List<CategoryVO>> querySubCategories(@PathVariable("pid")Long pid){
        List<CategoryVO> categoryVOS = indexService.querySubCategories(pid);
        return Resp.ok(categoryVOS);
    }

}
