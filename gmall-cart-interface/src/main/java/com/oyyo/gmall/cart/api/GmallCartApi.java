package com.oyyo.gmall.cart.api;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.cart.vo.CartVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface GmallCartApi {
    /**
     * 根据userId查询被选中的购物车信息
     *
     * @param userId
     * @return
     */
    @GetMapping("cart/{userId}")
    Resp<List<CartVO>> queryCheckCartsByUserId(@PathVariable("userId") Long userId);
}
