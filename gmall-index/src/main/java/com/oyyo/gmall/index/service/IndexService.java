package com.oyyo.gmall.index.service;

import com.oyyo.gmall.pms.entity.CategoryEntity;
import com.oyyo.gmall.pms.vo.CategoryVO;

import java.util.List;

public interface IndexService {
    List<CategoryEntity> queryLevel1Categories();

    List<CategoryVO> querySubCategories(Long pid);
}
