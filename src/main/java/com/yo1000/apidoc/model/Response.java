package com.yo1000.apidoc.model;

import java.util.List;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public class Response {
    private List<Header> headers;
    private String body;

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
