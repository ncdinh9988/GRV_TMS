package com.FiveSGroup.TMS.Warehouse;

public class Batch_number_Tam {
    private String BATCH_NUMBER;
    private String PRODUCT_CODE;
    private String PRODUCT_NAME;
    private String PRODUCT_CD;
    private String EXPIRED_DATE ;
    private String MANUFACTURING_DATE ;
    private String STOCKIN_DATE;
    private String UNIT ;
    private String POSITION_CODE ;
    private String POSITION_DESCRIPTION ;
    private String WAREHOUSE_POSITION_CD;

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

    public String getWAREHOUSE_POSITION_CD() {
        return WAREHOUSE_POSITION_CD;
    }

    public void setWAREHOUSE_POSITION_CD(String WAREHOUSE_POSITION_CD) {
        this.WAREHOUSE_POSITION_CD = WAREHOUSE_POSITION_CD;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
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

    public String getPRODUCT_CD() {
        return PRODUCT_CD;
    }

    public void setPRODUCT_CD(String PRODUCT_CD) {
        this.PRODUCT_CD = PRODUCT_CD;
    }

    public String getEXPIRED_DATE() {
        return EXPIRED_DATE;
    }

    public void setEXPIRED_DATE(String EXPIRED_DATE) {
        this.EXPIRED_DATE = EXPIRED_DATE;
    }

    public String getMANUFACTURING_DATE() {
        return MANUFACTURING_DATE;
    }

    public void setMANUFACTURING_DATE(String MANUFACTURING_DATE) {
        this.MANUFACTURING_DATE = MANUFACTURING_DATE;
    }

    public String getSTOCKIN_DATE() {
        return STOCKIN_DATE;
    }

    public void setSTOCKIN_DATE(String STOCKIN_DATE) {
        this.STOCKIN_DATE = STOCKIN_DATE;
    }

    public String getBATCH_NUMBER() {
        return BATCH_NUMBER;
    }

    public void setBATCH_NUMBER(String BATCH_NUMBER) {
        this.BATCH_NUMBER = BATCH_NUMBER;
    }
}
