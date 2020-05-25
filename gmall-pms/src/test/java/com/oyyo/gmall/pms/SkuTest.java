package com.oyyo.gmall.pms;

import com.oyyo.gmall.pms.controller.SkuImagesController;
import com.oyyo.gmall.pms.service.SkuImagesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SkuTest {

    @Autowired
    private SkuImagesController skuImagesController;

    @Autowired
    private SkuImagesService skuImagesService;

    @Test
    void testImg() {
        String s = skuImagesService.querySkuImgsBySkuId(61l);
        System.out.println(s);
    }
}
