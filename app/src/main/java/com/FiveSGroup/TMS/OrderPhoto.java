package com.FiveSGroup.TMS;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class OrderPhoto {
    private String Order_CD;
    private String Photo_Name;
    private String Photo_Path;
    private String Photo_CD;
    private Bitmap image;
    private long   photoCD = -1;

    private String STOCK_QA_CD ;
    private String BATCH_NUMBER;
    private String PRODUCT_CODE;
    private String UNIT;
    private String EXPIRED_DATE;
    private String STOCK_IN_DATE;


    private String WAREHOUSE_CONTAINER_CD;
    private String CHECK_TRANSPORT_CD;

    public String getCHECK_TRANSPORT_CD() {
        return CHECK_TRANSPORT_CD;
    }

    public void setCHECK_TRANSPORT_CD(String CHECK_TRANSPORT_CD) {
        this.CHECK_TRANSPORT_CD = CHECK_TRANSPORT_CD;
    }

    public String getWAREHOUSE_CONTAINER_CD() {
        return WAREHOUSE_CONTAINER_CD;
    }

    public void setWAREHOUSE_CONTAINER_CD(String WAREHOUSE_CONTAINER_CD) {
        this.WAREHOUSE_CONTAINER_CD = WAREHOUSE_CONTAINER_CD;
    }

    public String getPRODUCT_CODE() {
        return PRODUCT_CODE;
    }

    public void setPRODUCT_CODE(String PRODUCT_CODE) {
        this.PRODUCT_CODE = PRODUCT_CODE;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getEXPIRED_DATE() {
        return EXPIRED_DATE;
    }

    public void setEXPIRED_DATE(String EXPIRED_DATE) {
        this.EXPIRED_DATE = EXPIRED_DATE;
    }

    public String getSTOCK_IN_DATE() {
        return STOCK_IN_DATE;
    }

    public void setSTOCK_IN_DATE(String STOCK_IN_DATE) {
        this.STOCK_IN_DATE = STOCK_IN_DATE;
    }

    public String getBATCH_NUMBER() {
        return BATCH_NUMBER;
    }

    public void setBATCH_NUMBER(String BATCH_NUMBER) {
        this.BATCH_NUMBER = BATCH_NUMBER;
    }


    public String getSTOCK_QA_CD() {
        return STOCK_QA_CD;
    }

    public void setSTOCK_QA_CD(String STOCK_QA_CD) {
        this.STOCK_QA_CD = STOCK_QA_CD;
    }

    public long getPhotoCD() {
        return photoCD;
    }

    public void setPhotoCD(long photoCD) {
        this.photoCD = photoCD;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getPhoto_Path() {
        return Photo_Path;
    }

    public void setPhoto_Path(String photo_Path) {
        Photo_Path = photo_Path;
    }

    public String getPhoto_CD() {
        return Photo_CD;
    }

    public void setPhoto_CD(String photo_CD) {
        Photo_CD = photo_CD;
    }

    private Date Photo_Date;

    public String getOrder_CD() {
        return Order_CD;
    }

    public void setOrder_CD(String order_CD) {
        Order_CD = order_CD;
    }

    public String getPhoto_Name() {
        return Photo_Name;
    }

    public void setPhoto_Name(String photo_Name) {
        Photo_Name = photo_Name;
    }

    public Date getPhoto_Date() {
        return Photo_Date;
    }

    public void setPhoto_Date(Date photo_Date) {
        Photo_Date = photo_Date;
    }

    public String getStrDateTakesPhoto() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        return  df.format(this.getPhoto_Date().getTime());
    }
}
