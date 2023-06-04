package com.hello.capston.unit.repository.elasticsearch;

import com.hello.capston.elasticsearch.entity.ElasticItem;
import com.hello.capston.elasticsearch.entity.TestData;
import com.hello.capston.elasticsearch.repository.ElasticItemRepository;
import com.hello.capston.elasticsearch.service.ElasticsearchService;
import com.hello.capston.entity.Item;
import com.hello.capston.repository.ItemRepository;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class ElasticsearchRepositoryTest {

    @Autowired
    ElasticItemRepository elasticItemRepository;

    @Autowired
    ElasticsearchService elasticsearchService;

    @Autowired
    ElasticsearchRestTemplate template;

    @Autowired
    ItemRepository itemRepository;


    @Test
    @DisplayName("Elasticsearch 데이터 조회 테스트")
    public void test1() throws Exception {
        //given
        String name = "itemName";
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("view_name", name);
        SearchHits<ElasticItem> searchHits = template.search(new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(0, 10))
                .build(), ElasticItem.class);
        //when
        List<ElasticItem> collect = searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());
        ElasticItem elasticItem = collect.get(0);
        System.out.println("ViewName : " + elasticItem.getView_name());
        System.out.println("ItemName : " + elasticItem.getItem_name());
        System.out.println("ItemURL : " + elasticItem.getItem_url());
        System.out.println("ItemPrice : " + elasticItem.getPrice());
    }

    @Test
    @DisplayName("일반 JPA Like 연산자 데이터 조회 테스트")
    public void test2() throws Exception {
        //given
        PageRequest page = PageRequest.of(0, 10);
        //when
        for (int i = 0; i < 1000; i++) {
            List<Item> manToMan = itemRepository.findByCategory("맨투맨", page);
            //then
            Item item = manToMan.get(0);
            System.out.println("ViewName : " + item.getViewName());
            System.out.println("ItemName : " + item.getItemName());
            System.out.println("BrandName : " + item.getBrandName());
        }
    }
}
