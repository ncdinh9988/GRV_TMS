package com.FiveSGroup.TMS.QA.HomeQA;

public class Product_Criteria {
    private String PRODUCT_CODE;
    private String MIC_CODE;
    private String MIC_DESC;
    private String BATCH_NUMBER;
    private String NOTE;
    private String QTY;
    private String UNIT;
    private String MATERIA_CD;

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getMATERIA_CD() {
        return MATERIA_CD;
    }

    public void setMATERIA_CD(String MATERIA_CD) {
        this.MATERIA_CD = MATERIA_CD;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getNOTE() {
        return NOTE;
    }

    public void setNOTE(String NOTE) {
        this.NOTE = NOTE;
    }

    public String getPRODUCT_CODE() {
        return PRODUCT_CODE;
    }

    public void setPRODUCT_CODE(String PRODUCT_CODE) {
        this.PRODUCT_CODE = PRODUCT_CODE;
    }

    public String getMIC_CODE() {
        return MIC_CODE;
    }

    public void setMIC_CODE(String MIC_CODE) {
        this.MIC_CODE = MIC_CODE;
    }

    public String getMIC_DESC() {
        return MIC_DESC;
    }

    public void setMIC_DESC(String MIC_DESC) {
        this.MIC_DESC = MIC_DESC;
    }

    public String getBATCH_NUMBER() {
        return BATCH_NUMBER;
    }

    public void setBATCH_NUMBER(String BATCH_NUMBER) {
        this.BATCH_NUMBER = BATCH_NUMBER;
    }
}
