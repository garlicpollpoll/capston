package com.hello.capston.elasticsearch.controller;

import com.hello.capston.elasticsearch.entity.ElasticItem;
import com.hello.capston.elasticsearch.repository.ElasticItemRepository;
import com.hello.capston.elasticsearch.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;

    @GetMapping("/find/elasticsearch")
    public List<ElasticItem> find(@RequestParam("name") String name) {
        List<ElasticItem> search = elasticsearchService.search(name);

        return search;
    }
}
