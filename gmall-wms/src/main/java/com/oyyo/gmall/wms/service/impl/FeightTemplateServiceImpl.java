package com.oyyo.gmall.wms.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;

import com.oyyo.gmall.wms.dao.FeightTemplateDao;
import com.oyyo.gmall.wms.entity.FeightTemplateEntity;
import com.oyyo.gmall.wms.service.FeightTemplateService;


@Service("feightTemplateService")
public class FeightTemplateServiceImpl extends ServiceImpl<FeightTemplateDao, FeightTemplateEntity> implements FeightTemplateService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<FeightTemplateEntity> page = this.page(
                new Query<FeightTemplateEntity>().getPage(params),
                new QueryWrapper<FeightTemplateEntity>()
        );

        return new PageVo(page);
    }

}