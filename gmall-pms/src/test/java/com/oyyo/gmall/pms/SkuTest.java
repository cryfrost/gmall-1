package com.oyyo.gmall.pms;

import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.pms.controller.SkuImagesController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SkuTest {

    @Autowired
    private SkuImagesController skuImagesController;

    @Test
    void testImg() {
        Resp<String> res = skuImagesController.getImgs("61", new QueryCondition());
        System.out.println(res.getData());
    }
}
