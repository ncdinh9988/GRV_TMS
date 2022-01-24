package com.FiveSGroup.TMS.StockOut.OD;

public class Product_Stockout_OD {
    private String AUTOINCREMENT;
    private String LPN_CODE;
    private String LPN_TO;
    private String POSITION_CODE;
    private String POSITION_TO_CD;
    private String OUTBOUND_DELIVERY_CD;

    public String getAUTOINCREMENT() {
        return AUTOINCREMENT;
    }

    public void setAUTOINCREMENT(String AUTOINCREMENT) {
        this.AUTOINCREMENT = AUTOINCREMENT;
    }

    public String getLPN_CODE() {
        return LPN_CODE;
    }

    public void setLPN_CODE(String LPN_CODE) {
        this.LPN_CODE = LPN_CODE;
    }

    public String getLPN_TO() {
        return LPN_TO;
    }

    public void setLPN_TO(String LPN_TO) {
        this.LPN_TO = LPN_TO;
    }

    public String getPOSITION_CODE() {
        return POSITION_CODE;
    }

    public void setPOSITION_CODE(String POSITION_CODE) {
        this.POSITION_CODE = POSITION_CODE;
    }

    public String getPOSITION_TO_CD() {
        return POSITION_TO_CD;
    }

    public void setPOSITION_TO_CD(String POSITION_TO_CD) {
        this.POSITION_TO_CD = POSITION_TO_CD;
    }

    public String getOUTBOUND_DELIVERY_CD() {
        return OUTBOUND_DELIVERY_CD;
    }

    public void setOUTBOUND_DELIVERY_CD(String OUTBOUND_DELIVERY_CD) {
        this.OUTBOUND_DELIVERY_CD = OUTBOUND_DELIVERY_CD;
    }
}
