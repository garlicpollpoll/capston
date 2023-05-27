package com.hello.capston.elasticsearch.repository;

import com.hello.capston.elasticsearch.entity.ElasticItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticItemRepository extends ElasticsearchRepository<ElasticItem, String> {


}
