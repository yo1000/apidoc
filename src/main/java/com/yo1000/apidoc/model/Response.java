package com.yo1000.apidoc.model;

import java.util.List;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public class Response {
    private List<Header> headers;
    private int status;
    private String contentType;
    private Object body;

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
