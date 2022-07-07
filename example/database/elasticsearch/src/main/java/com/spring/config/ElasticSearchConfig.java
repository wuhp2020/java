package com.spring.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Value("${elasticsearch.nodes}")
    private String elasticsearchNodes;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        String[] nodes = elasticsearchNodes.split(",");
        HttpHost[] httpHosts = new HttpHost[nodes.length];
        for (int i = 0;i < nodes.length;i++) {
            String[] nodeInfo = nodes[i].split(":");
            httpHosts[i] = new HttpHost(nodeInfo[0], Integer.valueOf(nodeInfo[1]), "http");
        }
        RestClientBuilder builder = RestClient.builder(httpHosts);
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}
