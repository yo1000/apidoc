package com.yo1000.apidoc.component;

import com.yo1000.apidoc.model.Document;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
@Configuration
public abstract class ApidocConfigurer extends WebMvcConfigurerAdapter {
    public abstract ConcurrentHashMap<String, Document> getDocumentMap();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApidocHandlerInterceptor() {
            @Override
            public ConcurrentHashMap<String, Document> getDocumentMap() {
                return ApidocConfigurer.this.getDocumentMap();
            }
        });

        super.addInterceptors(registry);
    }
}
