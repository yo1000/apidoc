package com.yo1000.apidoc.component;

import com.yo1000.apidoc.model.Document;
import com.yo1000.apidoc.model.Header;
import com.yo1000.apidoc.model.Response;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public abstract class ApidocHandlerInterceptor extends HandlerInterceptorAdapter {
    public abstract ConcurrentHashMap<String, Document> getDocumentMap();

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);

        String key = request.getRequestURL().toString();

        if (!this.getDocumentMap().containsKey(key)) {
            return;
        }

        Document apidoc = this.getDocumentMap().get(key);

        if (apidoc.getResponse() == null) {
            apidoc.setResponse(new Response());
        }

        if (apidoc.getResponse().getHeaders() == null) {
            apidoc.getResponse().setHeaders(new ArrayList<Header>());
        }

        for (String name : response.getHeaderNames()) {
            Header header = new Header();
            header.setName(name);

            String value =response.getHeader(name);
            header.setValue(value != null ? value : "");

            apidoc.getResponse().getHeaders().add(header);
        }

        apidoc.getResponse().setStatus(response.getStatus());
        apidoc.getResponse().setContentType(response.getContentType());
    }
}
