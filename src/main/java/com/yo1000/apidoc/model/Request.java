package com.yo1000.apidoc.model;

import java.util.List;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public class Request {
    private String endpoint;
    private List<String> methods;
    private List<RequestHeader> headers;
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

    public List<RequestHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<RequestHeader> headers) {
        this.headers = headers;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (endpoint != null ? !endpoint.equals(request.endpoint) : request.endpoint != null) return false;
        if (methods != null ? !methods.equals(request.methods) : request.methods != null) return false;
        if (headers != null ? !headers.equals(request.headers) : request.headers != null) return false;
        return !(parameters != null ? !parameters.equals(request.parameters) : request.parameters != null);

    }

    @Override
    public int hashCode() {
        int result = endpoint != null ? endpoint.hashCode() : 0;
        result = 31 * result + (methods != null ? methods.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }
}
