package com.yo1000.apidoc.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public class DocumentBuilder {
    private ConcurrentHashMap<String, Document> documentMap = this.newDocumentMap();
    private Map<String, Request> requestMap = this.newRequestMap();
    private Map<String, Response> responseMap = this.newResponseMap();

    public boolean containsKeyInRequestMap(String url) {
        return this.getRequestMap().containsKey(url);
    }

    public boolean containsKeyInResponseMap(String url) {
        return this.getRequestMap().containsKey(url);
    }

    public Request getRequest(String url) {
        return this.getRequestMap().get(url);
    }

    public Response getResponse(String url) {
        return this.getResponseMap().get(url);
    }

    public void put(String url, Request request) {
        this.getRequestMap().put(url, request);
    }

    public void put(String url, Response response) {
        this.getResponseMap().put(url, response);
    }

    public Map<String, Request> getRequestMap() {
        return requestMap;
    }

    protected void setRequestMap(Map<String, Request> requestMap) {
        this.requestMap = requestMap;
    }

    protected Map<String, Request> newRequestMap() {
        return new ConcurrentHashMap<String, Request>();
    }

    public Map<String, Response> getResponseMap() {
        return responseMap;
    }

    protected void setResponseMap(Map<String, Response> responseMap) {
        this.responseMap = responseMap;
    }

    public Map<String, Response> newResponseMap() {
        return new ConcurrentHashMap<String, Response>();
    }

    public ConcurrentHashMap<String, Document> getDocumentMap() {
        for (Map.Entry<String, Request> entry : this.getRequestMap().entrySet()) {
            String entrypoint = entry.getValue().getEndpoint();

            Document doc = this.documentMap.containsKey(entrypoint)
                    ? this.documentMap.get(entrypoint)
                    : new Document();

            this.documentMap.put(entrypoint, doc);

            doc.setRequest(entry.getValue());
            doc.getResponses().add(this.getResponseMap().get(entry.getKey()));
        }

        this.setRequestMap(this.newRequestMap());
        this.setResponseMap(this.newResponseMap());

        return documentMap;
    }

    public void setDocumentMap(ConcurrentHashMap<String, Document> documentMap) {
        this.documentMap = documentMap;
    }

    public ConcurrentHashMap<String, Document> newDocumentMap() {
        return new ConcurrentHashMap<String, Document>();
    }}
