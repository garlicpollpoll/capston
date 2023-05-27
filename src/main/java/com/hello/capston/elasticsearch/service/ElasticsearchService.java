package com.hello.capston.elasticsearch.service;

import com.hello.capston.elasticsearch.entity.ElasticItem;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final ElasticsearchRestTemplate template;

    public List<ElasticItem> search(String name) {
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("view_name", name);
        SearchHits<ElasticItem> searchHits = template.search(new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(0, 10))
                .build(), ElasticItem.class);

        List<ElasticItem> collect = searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());

        return collect;
    }
}
