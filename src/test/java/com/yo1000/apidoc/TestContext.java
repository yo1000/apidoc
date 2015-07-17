package com.yo1000.apidoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yo1000.apidoc.component.ApidocConfigurer;
import com.yo1000.apidoc.model.Document;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yoichi.kikuchi on 2015/07/17.
 */
@SpringBootApplication
public class TestContext extends ApidocConfigurer {
    @Autowired
    private ConcurrentHashMap<String, Document> documentMap;

    @Override
    public ConcurrentHashMap<String, Document> getDocumentMap() {
        return documentMap;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                        Object handler, Exception ex) throws Exception {
                super.afterCompletion(request, response, handler, ex);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

                System.out.println(objectMapper.writeValueAsString(TestContext.this.getDocumentMap()));
            }
        });

        super.addInterceptors(registry);
    }

    @Bean
    public ConcurrentHashMap<String, Document> documentMap() {
        return new ConcurrentHashMap<String, Document>();
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
