package com.yo1000.apidoc.model;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public class Document {
    private Request request;
    private Response response;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
