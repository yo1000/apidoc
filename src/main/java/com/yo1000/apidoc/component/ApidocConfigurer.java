package com.yo1000.apidoc.component;

import com.yo1000.apidoc.model.DocumentBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
@Configuration
public abstract class ApidocConfigurer extends WebMvcConfigurerAdapter {
    public abstract DocumentBuilder getDocumentBuilder();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApidocHandlerInterceptor() {
            @Override
            public DocumentBuilder getDocument() {
                return ApidocConfigurer.this.getDocumentBuilder();
            }
        });

        super.addInterceptors(registry);
    }
}
