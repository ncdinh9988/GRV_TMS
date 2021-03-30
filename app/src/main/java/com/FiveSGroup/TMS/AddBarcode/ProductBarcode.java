package com.FiveSGroup.TMS.AddBarcode;

public class ProductBarcode {
    private String code;
    private String name;
    private String barcode;

    public ProductBarcode(String code, String name, String barcode) {
        this.code = code;
        this.name = name;
        this.barcode = barcode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}


