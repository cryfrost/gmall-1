package com.oyyo.gmall.item.controller;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.item.service.ItemService;
import com.oyyo.gmall.item.vo.ItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: ItemController
 * @Description: 商品详情页
 * @Author: LiKui
 * @Date: 2020-6-1 12:26
 * @Version: 1.0
 */
@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("{skuId}")
    public Resp<ItemVO> queryItemVO(@PathVariable("skuId") Long skuId){

        ItemVO itemVO = itemService.queryItemVO(skuId);
        return Resp.ok(itemVO);
    }
}