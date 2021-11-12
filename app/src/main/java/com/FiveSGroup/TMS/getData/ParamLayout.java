package com.FiveSGroup.TMS.getData;

public class ParamLayout {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public ParamLayout() {

    }
    public ParamLayout(String key, String value) {
        this.key = key;
        this.value = value;
    }
}