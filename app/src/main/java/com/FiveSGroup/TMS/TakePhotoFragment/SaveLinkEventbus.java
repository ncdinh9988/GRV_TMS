package com.FiveSGroup.TMS.TakePhotoFragment;

public class SaveLinkEventbus {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SaveLinkEventbus(String url) {
        this.url = url;
    }
}
