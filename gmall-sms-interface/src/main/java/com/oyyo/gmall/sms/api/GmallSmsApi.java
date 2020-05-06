package com.oyyo.gmall.sms.api;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.sms.vo.SkuSaleVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface GmallSmsApi {
    @PostMapping("sms/skubounds/sku/sale/save")
    public Resp<Object> saveSaleInfo(@RequestBody SkuSaleVO skuSaleVO);

}
