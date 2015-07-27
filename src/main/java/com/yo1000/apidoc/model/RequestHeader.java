package com.yo1000.apidoc.model;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public class RequestHeader {
    private String name;
    private String value;
    private boolean requires;

    public RequestHeader() {}

    public RequestHeader(String name, String value, boolean requires) {
        this.name = name;
        this.value = value;
        this.requires = requires;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isRequires() {
        return requires;
    }

    public void setRequires(boolean requires) {
        this.requires = requires;
    }
}
