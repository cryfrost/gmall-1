package com.oyyo.gmall.wms.api;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.wms.entity.WareSkuEntity;
import com.oyyo.gmall.wms.vo.SkuLockVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface GmallWmsApi {
    /**
     * 查询库存 并锁定
     *
     * @param skuLockVOS
     * @return
     */
    @PostMapping("wms/waresku")
    Resp<Object> checkAndLockStore(@RequestBody List<SkuLockVO> skuLockVOS);
    /**
     * 查询某个sku的库存信息
     * @param skuId
     * @return
     */
    @GetMapping("wms/waresku/{skuId}")
    Resp<List<WareSkuEntity>> queryWareSkuBySkuId(@PathVariable("skuId")Long skuId);
}
