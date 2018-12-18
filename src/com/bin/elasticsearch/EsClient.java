package com.bin.elasticsearch;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class EsClient {
    public static void main(String[] args) {

        Settings settings = Settings.builder().put("cluster.name", "es-cluster").build();
        try (TransportClient transportClient = new PreBuiltTransportClient(settings)) {
            transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.20.110"), 9300));

            SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch("epocket-quan-index-02");
            SearchResponse response = searchRequestBuilder.get();
            System.out.println("查询结果：" + response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
