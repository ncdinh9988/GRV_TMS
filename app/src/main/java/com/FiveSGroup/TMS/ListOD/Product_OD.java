package com.FiveSGroup.TMS.ListOD;

public class Product_OD {
    private String AUTOINCREMENT;
    private String PRODUCT_CD;
    private String PRODUCT_CODE;
    private String PRODUCT_NAME;
    private String EXPIRED_DATE ;
    private String STOCKIN_DATE;
    private String UNIT;
    private String QTY;
    private String BATCH_NUMBER;
    private String QTY_OD;
    private String POSITION_CODE;
    private String WAREHOUSE_POSITION_CD;
    private String SUGGESTION;;
    private String CREATE_TIME;

    public String getPRODUCT_CD() {
        return PRODUCT_CD;
    }

    public void setPRODUCT_CD(String PRODUCT_CD) {
        this.PRODUCT_CD = PRODUCT_CD;
    }

    public String getAUTOINCREMENT() {
        return AUTOINCREMENT;
    }

    public void setAUTOINCREMENT(String AUTOINCREMENT) {
        this.AUTOINCREMENT = AUTOINCREMENT;
    }

    public String getPRODUCT_CODE() {
        return PRODUCT_CODE;
    }

    public void setPRODUCT_CODE(String PRODUCT_CODE) {
        this.PRODUCT_CODE = PRODUCT_CODE;
    }

    public String getPRODUCT_NAME() {
        return PRODUCT_NAME;
    }

    public void setPRODUCT_NAME(String PRODUCT_NAME) {
        this.PRODUCT_NAME = PRODUCT_NAME;
    }

    public String getEXPIRED_DATE() {
        return EXPIRED_DATE;
    }

    public void setEXPIRED_DATE(String EXPIRED_DATE) {
        this.EXPIRED_DATE = EXPIRED_DATE;
    }

    public String getSTOCKIN_DATE() {
        return STOCKIN_DATE;
    }

    public void setSTOCKIN_DATE(String STOCKIN_DATE) {
        this.STOCKIN_DATE = STOCKIN_DATE;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getBATCH_NUMBER() {
        return BATCH_NUMBER;
    }

    public void setBATCH_NUMBER(String BATCH_NUMBER) {
        this.BATCH_NUMBER = BATCH_NUMBER;
    }

    public String getQTY_OD() {
        return QTY_OD;
    }

    public void setQTY_OD(String QTY_OD) {
        this.QTY_OD = QTY_OD;
    }

    public String getPOSITION_CODE() {
        return POSITION_CODE;
    }

    public void setPOSITION_CODE(String POSITION_CODE) {
        this.POSITION_CODE = POSITION_CODE;
    }

    public String getWAREHOUSE_POSITION_CD() {
        return WAREHOUSE_POSITION_CD;
    }

    public void setWAREHOUSE_POSITION_CD(String WAREHOUSE_POSITION_CD) {
        this.WAREHOUSE_POSITION_CD = WAREHOUSE_POSITION_CD;
    }

    public String getSUGGESTION() {
        return SUGGESTION;
    }

    public void setSUGGESTION(String SUGGESTION) {
        this.SUGGESTION = SUGGESTION;
    }

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }
}
