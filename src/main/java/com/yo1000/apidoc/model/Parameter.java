package com.yo1000.apidoc.model;

/**
 * Created by yoichi.kikuchi on 2015/07/16.
 */
public class Parameter {
    private String field;
    private String type;
    private boolean requires;

    public Parameter() {}

    public Parameter(String field, String type, boolean requires) {
        this.field = field;
        this.type = type;
        this.requires = requires;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequires() {
        return requires;
    }

    public void setRequires(boolean requires) {
        this.requires = requires;
    }
}
