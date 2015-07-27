package com.yo1000.apidoc.component;

import com.yo1000.apidoc.model.DocumentBuilder;
import com.yo1000.apidoc.model.Response;
import com.yo1000.apidoc.model.ResponseHeader;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public abstract class ApidocHandlerInterceptor extends HandlerInterceptorAdapter {
    public abstract DocumentBuilder getDocument();

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        String url = request.getRequestURL().toString();
        String query = request.getQueryString();

        String key = url + (query != null ? ("?" + query) : "");

        if (!this.getDocument().containsKeyInRequestMap(key)) {
            return;
        }

        if (!this.getDocument().containsKeyInResponseMap(key)) {
            return;
        }

        Response docResp = this.getDocument().getResponse(key);

        if (docResp.getHeaders() == null) {
            docResp.setHeaders(new ArrayList<ResponseHeader>());
        }

        for (String name : response.getHeaderNames()) {
            ResponseHeader requestHeader = new ResponseHeader();
            requestHeader.setName(name);

            String value =response.getHeader(name);
            requestHeader.setValue(value != null ? value : "");

            docResp.getHeaders().add(requestHeader);
        }

        docResp.setStatus(response.getStatus());
        docResp.setContentType(response.getContentType());

        super.afterCompletion(request, response, handler, ex);
    }
}
