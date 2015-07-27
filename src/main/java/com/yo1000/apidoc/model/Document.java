package com.yo1000.apidoc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoichi.kikuchi on 2015/07/28.
 */
public class Document {
    private Request request = new Request();
    private List<Response> responses = new ArrayList<Response>();

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }
}
