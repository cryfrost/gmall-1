package com.oyyo.gmall.sms.api;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.sms.vo.SalseVO;
import com.oyyo.gmall.sms.vo.SkuSaleVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface GmallSmsApi {
    @PostMapping("sms/skubounds/sku/sale/save")
    Resp<Object> saveSaleInfo(@RequestBody SkuSaleVO skuSaleVO);

    /**
     * 查询营销信息
     * @param skuId
     * @return
     */
    @GetMapping("sms/skubounds/{skuId}")
    Resp<List<SalseVO>> querySkuSalseBySkuId(@PathVariable("skuId")Long skuId);
}
