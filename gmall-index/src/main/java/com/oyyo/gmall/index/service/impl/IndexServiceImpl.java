package com.oyyo.gmall.index.service.impl;

import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.index.fegin.GmallPmsClient;
import com.oyyo.gmall.index.service.IndexService;
import com.oyyo.gmall.pms.entity.CategoryEntity;
import com.oyyo.gmall.pms.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private GmallPmsClient gmallPmsClient;

    @Override
    public List<CategoryEntity> queryLevel1Categories() {
        Resp<List<CategoryEntity>> listResp = gmallPmsClient.queryCategoriesByPidOrLevel(1, null);

        return listResp.getData();
    }

    @Override
    public List<CategoryVO> querySubCategories(Long pid) {

        Resp<List<CategoryVO>> listResp = gmallPmsClient.querySubCategories(pid);
        return listResp.getData();
    }
}
