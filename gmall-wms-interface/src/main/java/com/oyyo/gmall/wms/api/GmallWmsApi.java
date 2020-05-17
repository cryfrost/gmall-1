package com.oyyo.gmall.wms.api;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.wms.entity.WareSkuEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface GmallWmsApi {
    @GetMapping("wms/waresku/{skuId}")
    Resp<List<WareSkuEntity>> queryWareSkuBySkuId(@PathVariable("skuId")Long skuId);
}
