package com.yo1000.apidoc.model;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public class ResponseHeader {
    private String name;
    private String value;

    public ResponseHeader() {}

    public ResponseHeader(String name, String value, boolean requires) {
        this.name = name;
        this.value = value;
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
}
