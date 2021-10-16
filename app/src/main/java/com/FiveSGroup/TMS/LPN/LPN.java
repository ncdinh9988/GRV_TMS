package com.FiveSGroup.TMS.LPN;

public class LPN {
    private String LPN_NUMBER;
    private String LPN_CODE;
    private String LPN_DATE;
    private String USER_CREATE;
    private String STORAGE;
    private String ORDER_CODE ;

    public String getORDER_CODE() {
        return ORDER_CODE;
    }

    public void setORDER_CODE(String ORDER_CODE) {
        this.ORDER_CODE = ORDER_CODE;
    }

    public String getUSER_CREATE() {
        return USER_CREATE;
    }

    public void setUSER_CREATE(String USER_CREATE) {
        this.USER_CREATE = USER_CREATE;
    }

    public String getSTORAGE() {
        return STORAGE;
    }

    public void setSTORAGE(String STORAGE) {
        this.STORAGE = STORAGE;
    }

    public String getLPN_NUMBER() {
        return LPN_NUMBER;
    }

    public void setLPN_NUMBER(String LPN_NUMBER) {
        this.LPN_NUMBER = LPN_NUMBER;
    }

    public String getLPN_CODE() {
        return LPN_CODE;
    }

    public void setLPN_CODE(String LPN_CODE) {
        this.LPN_CODE = LPN_CODE;
    }

    public String getLPN_DATE() {
        return LPN_DATE;
    }

    public void setLPN_DATE(String LPN_DATE) {
        this.LPN_DATE = LPN_DATE;
    }
}
