package com.FiveSGroup.TMS.Warehouse;

public class Product_Qrcode {

    private String QRCODE;
    private String AUTOINCREMENT;
    private String PRODUCT_CODE;
    private String PRODUCT_NAME;
    private String PRODUCT_CD;
    private String SL_SET;
    private String POSITION_FROM;
    private String POSITION_TO;
    private String STOCK_RECEIPT_CD;
    private String EXPIRED_DATE ;
    private String UNIT ;
    private String POSITION_CODE ;
    private String POSITION_DESCRIPTION ;
    private String STOCKIN_DATE;
    private String WAREHOUSE_POSITION_CD;
    private String MANUFACTURING_DATE ;

    public String getMANUFACTURING_DATE() {
        return MANUFACTURING_DATE;
    }

    public void setMANUFACTURING_DATE(String MANUFACTURING_DATE) {
        this.MANUFACTURING_DATE = MANUFACTURING_DATE;
    }

    public String getWAREHOUSE_POSITION_CD() {
        return WAREHOUSE_POSITION_CD;
    }

    public void setWAREHOUSE_POSITION_CD(String WAREHOUSE_POSITION_CD) {
        this.WAREHOUSE_POSITION_CD = WAREHOUSE_POSITION_CD;
    }

    public String getAUTOINCREMENT() {
        return AUTOINCREMENT;
    }

    public void setAUTOINCREMENT(String AUTOINCREMENT) {
        this.AUTOINCREMENT = AUTOINCREMENT;
    }

    public String getSTOCKIN_DATE() {
        return STOCKIN_DATE;
    }

    public void setSTOCKIN_DATE(String STOCKIN_DATE) {
        this.STOCKIN_DATE = STOCKIN_DATE;
    }

    public String getEA_UNIT() {
        return UNIT;
    }

    public void setEA_UNIT(String EA_UNIT) {
        this.UNIT = EA_UNIT;
    }

    public String getPOSITION_CODE() {
        return POSITION_CODE;
    }

    public void setPOSITION_CODE(String POSITION_CODE) {
        this.POSITION_CODE = POSITION_CODE;
    }

    public String getPOSITION_DESCRIPTION() {
        return POSITION_DESCRIPTION;
    }

    public void setPOSITION_DESCRIPTION(String POSITION_DESCRIPTION) {
        this.POSITION_DESCRIPTION = POSITION_DESCRIPTION;
    }

    public String getEXPIRED_DATE() {
        return EXPIRED_DATE;
    }

    public void setEXPIRED_DATE(String EXPIRED_DATE) {
        this.EXPIRED_DATE = EXPIRED_DATE;
    }

    public String getSL_SET() {
        return SL_SET;
    }

    public void setSL_SET(String SL_SET) {
        this.SL_SET = SL_SET;
    }

    public String getSTOCK_RECEIPT_CD() {
        return STOCK_RECEIPT_CD;
    }

    public void setSTOCK_RECEIPT_CD(String STOCK_RECEIPT_CD) {
        this.STOCK_RECEIPT_CD = STOCK_RECEIPT_CD;
    }

    public String getPRODUCT_CD() {
        return PRODUCT_CD;
    }

    public void setPRODUCT_CD(String PRODUCT_CD) {
        this.PRODUCT_CD = PRODUCT_CD;
    }



    public  Product_Qrcode(){

    }

    public Product_Qrcode(String QRCODE, String PRODUCT_CODE, String PRODUCT_NAME, String SET, String PRODUCT_FROM, String PRODUCT_TO) {
        this.QRCODE = QRCODE;
        this.PRODUCT_CODE = PRODUCT_CODE;
        this.PRODUCT_NAME = PRODUCT_NAME;

        this.POSITION_FROM = PRODUCT_FROM;
        this.POSITION_TO = PRODUCT_TO;
    }

    public String getQRCODE() {
        return QRCODE;
    }

    public void setQRCODE(String QRCODE) {
        this.QRCODE = QRCODE;
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



    public String getPRODUCT_FROM() {
        return POSITION_FROM;
    }

    public void setPRODUCT_FROM(String PRODUCT_FROM) {
        this.POSITION_FROM = PRODUCT_FROM;
    }

    public String getPRODUCT_TO() {
        return POSITION_TO;
    }

    public void setPRODUCT_TO(String PRODUCT_TO) {
        this.POSITION_TO = PRODUCT_TO;
    }
}
