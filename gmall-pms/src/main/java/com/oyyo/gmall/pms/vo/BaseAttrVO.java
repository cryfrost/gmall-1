package com.oyyo.gmall.pms.vo;

import com.oyyo.gmall.pms.entity.ProductAttrValueEntity;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: BaseAttrVO
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-5-6 16:34
 * @Version: 1.0
 */
@Data
public class BaseAttrVO extends ProductAttrValueEntity {

    public void setValueSelected(List<String> valueSelected){
        if (CollectionUtils.isEmpty(valueSelected)) {
            return;
        }
        setAttrValue(StringUtils.join(valueSelected,","));
    }
}