package com.oyyo.gmall.pms.vo;

import com.oyyo.gmall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: SpuInfoVo
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-5-6 16:32
 * @Version: 1.0
 */
@Data
public class SpuInfoVo extends SpuInfoEntity {
    private List<String> spuImages;
    private List<BaseAttrVO> baseAttrs;
    private List<SkuInfoVO> skus;

}