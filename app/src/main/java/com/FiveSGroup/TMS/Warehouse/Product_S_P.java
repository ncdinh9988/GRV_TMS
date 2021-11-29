package com.FiveSGroup.TMS.Warehouse;

public class Product_S_P {
    private String PRODUCT_CODE;
    private String PRODUCT_NAME;
    private String PRODUCT_CD;
    private String LOT_IND;

    public String getLOT_IND() {
        return LOT_IND;
    }

    public void setLOT_IND(String LOT_IND) {
        this.LOT_IND = LOT_IND;
    }

    public String getPRODUCT_CD() {
        return PRODUCT_CD;
    }

    public void setPRODUCT_CD(String PRODUCT_CD) {
        this.PRODUCT_CD = PRODUCT_CD;
    }

    public String getPRODUCT_NAME() {
        return PRODUCT_NAME;
    }

    public void setPRODUCT_NAME(String PRODUCT_NAME) {
        this.PRODUCT_NAME = PRODUCT_NAME;
    }

    public String getPRODUCT_CODE() {
        return PRODUCT_CODE;
    }

    public void setPRODUCT_CODE(String PRODUCT_CODE) {
        this.PRODUCT_CODE = PRODUCT_CODE;
    }
}
