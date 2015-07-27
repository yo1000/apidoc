package com.yo1000.apidoc.model;

import java.util.List;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public class Response {
    private String requestUrl;
    private List<ResponseHeader> headers;
    private int status;
    private String contentType;
    private Object body;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public List<ResponseHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<ResponseHeader> headers) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Response response = (Response) o;

        if (status != response.status) return false;
        if (headers != null ? !headers.equals(response.headers) : response.headers != null) return false;
        if (contentType != null ? !contentType.equals(response.contentType) : response.contentType != null)
            return false;
        return !(body != null ? !body.equals(response.body) : response.body != null);

    }

    @Override
    public int hashCode() {
        int result = headers != null ? headers.hashCode() : 0;
        result = 31 * result + status;
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}
