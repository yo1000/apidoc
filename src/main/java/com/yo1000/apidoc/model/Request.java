package com.yo1000.apidoc.model;

import java.util.List;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public class Request {
    private String endpoint;
    private List<String> methods;
    private List<Header> headers;
    private List<Parameter> parameters;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
