package com.web.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.web.model.DocDO;
import com.web.vo.doc.DocAddVO;
import com.web.vo.doc.DocVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DocService {

    public static final String DOC_INDEX = "doc";
    public static final String DOC_CONTENT_FIELD = "content";
    public static final String DOC_TYPES_FIELD = "types";

    @Autowired
    private RestHighLevelClient client;

    /**
     * 创建索引
     * @throws Exception
     */
    public void createIndex() throws Exception {
        if (!this.indexExists(DOC_INDEX)) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(DOC_INDEX);

            Map<String, Object> content = new HashMap<>();
            content.put("type", "text");

            Map<String, Object> types = new HashMap<>();
            types.put("type", "keyword");

            Map<String, Object> properties = new HashMap<>();
            properties.put(DOC_CONTENT_FIELD, content);
            properties.put(DOC_TYPES_FIELD, types);

            Map<String, Object> mapping = new HashMap<>();
            mapping.put("properties", properties);
            createIndexRequest.mapping(mapping);

            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            log.info("创建索引完成: ", createIndexResponse);
        }
    }

    public boolean indexExists(String indexName) {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        boolean isExists = false;
        try {
            isExists = client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询文章索引失败", e);
        }
        return isExists;
    }

    /**
     * 添加文档
     * @param docAddVO
     * @throws Exception
     */
    public void addDoc(DocAddVO docAddVO) throws Exception {
        DocDO docDO = new DocDO();
        BeanUtils.copyProperties(docAddVO, docDO);
        docDO.setId(UUID.randomUUID().toString().replace("-", ""));
        docDO.setDate(new Date());

        Gson gson = new Gson();
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest(DOC_INDEX).id(docDO.getId()).source(gson.toJson(docDO), XContentType.JSON));
        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);

        log.info("文章添加成功: ", docDO.getId());
    }

    /**
     * 根据关键字模糊搜索文档
     * @param keyWords
     * @return
     * @throws Exception
     */
    public List<DocVO> searchByKeyWords(String keyWords) throws Exception {

        Gson gson = new Gson();
        SearchRequest request = new SearchRequest(DOC_INDEX);
        request.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(50).timeout(new TimeValue(5, TimeUnit.SECONDS));
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(DOC_CONTENT_FIELD, keyWords);
        sourceBuilder.query(matchQueryBuilder);
        request.source(sourceBuilder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        List<DocVO> docVOs = Arrays.stream(searchResponse.getHits().getHits())
                .sorted(Comparator.comparing(SearchHit::getScore).reversed())
                .map(hit -> gson.fromJson(hit.getSourceAsString(), DocVO.class))
                .collect(Collectors.toList());

        log.info("查询到数据: ", docVOs);

        return docVOs;
    }

    /**
     * 按文章类型精确搜索文档
     * @param types
     * @return
     * @throws Exception
     */
    public List<DocVO> searchByTypes(List<String> types) throws Exception {

        Gson gson = new Gson();
        MultiSearchRequest multiRequest = new MultiSearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(50).timeout(new TimeValue(5, TimeUnit.SECONDS));
        types.stream().forEach(type -> {
            SearchRequest request = new SearchRequest(DOC_INDEX);
            sourceBuilder.query(QueryBuilders.matchQuery(DOC_TYPES_FIELD, type));
            request.source(sourceBuilder);
            multiRequest.add(request);

        });
        MultiSearchResponse multiSearchResponse = client.msearch(multiRequest, RequestOptions.DEFAULT);
        List<DocVO> docVOS = Lists.newArrayList();
        multiSearchResponse.forEach(t ->{
            SearchResponse searchResponse = t.getResponse();
            docVOS.addAll(Arrays.stream(searchResponse.getHits().getHits())
                .sorted(Comparator.comparing(SearchHit::getScore).reversed())
                .map(hit -> gson.fromJson(hit.getSourceAsString(), DocVO.class))
                .collect(Collectors.toList()));
        });
        log.info("查询数据成功");

        return docVOS.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 删除文档
     * @param ids
     * @throws Exception
     */
    public void deleteDocs(List<String> ids) throws Exception {

        ids.parallelStream().forEach(id -> {
            try {
                client.delete(new DeleteRequest(DOC_INDEX).id(id), RequestOptions.DEFAULT);
            } catch (IOException e) {

            }
        });

        log.info("文章删除成功: ", ids);
    }
}
