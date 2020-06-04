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

}
