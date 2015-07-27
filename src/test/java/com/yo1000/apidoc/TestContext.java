package com.yo1000.apidoc;

import com.yo1000.apidoc.component.ApidocConfigurer;
import com.yo1000.apidoc.component.ApidocFormatHandlerInterceptor;
import com.yo1000.apidoc.model.Document;
import com.yo1000.apidoc.model.DocumentBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yoichi.kikuchi on 2015/07/17.
 */
@SpringBootApplication
public class TestContext extends ApidocConfigurer {
    @Autowired
    private DocumentBuilder documentBuilder;

    @Override
    public DocumentBuilder getDocumentBuilder() {
        return documentBuilder;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApidocFormatHandlerInterceptor() {
            @Override
            public ConcurrentHashMap<String, Document> getDocumentMap() {
                return TestContext.this.getDocumentBuilder().getDocumentMap();
            }
        });

        super.addInterceptors(registry);
    }

    @Bean
    public DocumentBuilder documentBuilder() {
        return new DocumentBuilder();
    }

    @Bean
    @Autowired
    public RestTemplate restTemplate(HttpClient httpClient) throws Exception {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);

        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClientBuilder.create()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .build();
    }
}
