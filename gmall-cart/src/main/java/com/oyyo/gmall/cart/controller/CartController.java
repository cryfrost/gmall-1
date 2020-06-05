package com.oyyo.gmall.cart.controller;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.cart.service.CartService;
import com.oyyo.gmall.cart.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     * @param cartVO
     * @return
     */
    @PostMapping
    public Resp<Boolean> addCart(@RequestBody CartVO cartVO){

        Boolean result = cartService.addCart(cartVO);
        return Resp.ok(result);
    }

    /**
     * 查询购物车
     * @return
     */
    @GetMapping
    public Resp<List<CartVO>> queryCarts(){
        List<CartVO> carts = cartService.queryCarts();
        return Resp.ok(carts);
    }

    /**
     * 更新购物车数量
     * @return
     */
    @PostMapping("update")
    public Resp<Boolean> updateCartCount(@RequestBody CartVO cartVO){
        Boolean result = cartService.updateCartCount(cartVO);
        return Resp.ok(result);
    }

    /**
     * 删除购物车
     * @param skuIds
     * @return
     */
    @PostMapping("delete")
    public Resp<Boolean> deleteCart(@RequestBody List<Long> skuIds){
        Boolean delResult = cartService.deleteCart(skuIds);
        return Resp.ok(delResult);
    }

}
