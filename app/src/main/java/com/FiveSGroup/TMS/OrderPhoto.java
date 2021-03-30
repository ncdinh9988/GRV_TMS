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
