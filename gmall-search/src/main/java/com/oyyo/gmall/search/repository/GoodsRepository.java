package com.oyyo.gmall.search.repository;

import com.oyyo.gmall.search.entity.GoodsEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsRepository extends ElasticsearchRepository<GoodsEntity,Long> {
}
