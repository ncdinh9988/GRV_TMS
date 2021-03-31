package com.FiveSGroup.TMS;

import android.graphics.Bitmap;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Keep
public class CPhoto implements Serializable,Comparable {

	private long   photoCD = -1;
	private String photoPath = "";
	private String photoName = "";
	private String photoCustomerCode = "";
	private String photoCustomerPhone = "";
	private String photoCustomerName = "";

	public String getPhotoCustomerName() {
		return photoCustomerName;
	}

	public void setPhotoCustomerName(String photoCustomerName) {
		this.photoCustomerName = photoCustomerName;
	}

	private String photoCustomerAddress = "";

	public String getPhotoCustomerAddress() {
		return photoCustomerAddress;
	}

	public void setPhotoCustomerAddress(String photoCustomerAddress) {
		this.photoCustomerAddress = photoCustomerAddress;
	}

	public String getPhotoCustomerPhone() {
		return photoCustomerPhone;
	}

	public void setPhotoCustomerPhone(String photoCustomerPhone) {
		this.photoCustomerPhone = photoCustomerPhone;
	}

	private Bitmap image;
	String infPhoto;

	private String Order_CD;
	private String Photo_Name;
	private String Photo_Path;
	private String Photo_CD;

//	private Bitmap image;
//	private long   photoCD = -1;
//
//	public long getPhotoCD() {
//		return photoCD;
//	}
//
//	public void setPhotoCD(long photoCD) {
//		this.photoCD = photoCD;
//	}

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

//	public String getStrDateTakesPhoto() {
//		Calendar c = Calendar.getInstance();
//		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
//		df.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
//		return  df.format(this.getPhoto_Date().getTime());
//	}

	public String getfPhoto() {
		return infPhoto;
	}

	public void setfPhoto(String fPhoto) {
		this.infPhoto = fPhoto;
	}

//	public Bitmap getImage() {
//		return image;
//	}
//
//	public void setImage(Bitmap image) {
//		this.image = image;
//	}

	private String photoTradeProgramCD= "";
	
	private String photoNameBrieft = "";
	
	public String getPhotoCustomerCode() {
		return photoCustomerCode;
	}


	public void setPhotoCustomerCode(String photoCustomerCode) {
		this.photoCustomerCode = photoCustomerCode;
	}


	private Date   dateTakesPhoto;
	
	private String geoCode = "";
	private String geoCodeAccuracy = "0";
	private String photoDecodeToString = "";
	 
		
	
	public Date getDateTakesPhoto() {
		return dateTakesPhoto;
	}
	
	public String getStrDateTakesPhoto() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
		return  df.format(this.getDateTakesPhoto().getTime());			
	}

	public String getPhotoPath() {
		return photoPath;
	}


	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getPhotoName() {
		return photoName;
	}


	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}



	public String getGeoCode() {
		return geoCode;
	}


	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}


	public String getDateTakesPhotoDisplay() {	
		try {
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		 
			 return sdf.format(this.dateTakesPhoto);
		} catch (Exception e) {
			// TODO: handle exception
			//CmnFns.writeLogError("FPhoto getDateTakesPhotoDisplay " + e.getMessage());
			return "";
		}
		
	}

//	public String getDateTakesPhotoToInsertDB() {
//		 SimpleDateFormat sdf = new SimpleDateFormat(global.getFormatDate());
//		return sdf.format(this.dateTakesPhoto);
//	}

	public CPhoto()
	{
		
	}	

	
	
	public CPhoto(Date DateTakesPhoto, String PhotoPath, String PhotoName, Bitmap image, String fPhoto)
	{
		this.dateTakesPhoto = DateTakesPhoto;
		this.photoPath = PhotoPath;
		this.photoName = PhotoName;
		this.image = image;
		this.infPhoto = fPhoto;
		//this.geoCode  = GeoCode;
	}	





	public void setDateTakesPhoto(Date dateTakesPhoto) {
		this.dateTakesPhoto = dateTakesPhoto;
	}

	public String getDateTakesPhotoToInsertDB() {
		SimpleDateFormat sdf = new SimpleDateFormat(global.getFormatDate());
		return sdf.format(this.dateTakesPhoto);
	}

	@Override
	public int compareTo(Object another) 
	{
		
	   	 CPhoto cPhoto = (CPhoto)another;			
			long diffInMs = (this.getDateTakesPhoto().getTime() -  cPhoto.getDateTakesPhoto().getTime());
	    	long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
			
			if(diffInSec == 0 )
				return 0;
			else
				if(diffInSec > 0 )
					return  -1 ;
				else
					return 1;	         
	}


	public long getPhotoCD() {
		return photoCD;
	}


	public void setPhotoCD(long photoCD) {
		this.photoCD = photoCD;
	}


	public String getPhotoDecodeToString() {
		return photoDecodeToString;
	}


	public void setPhotoDecodeToString(String photoDecodeToString) {
		this.photoDecodeToString = photoDecodeToString;
	}


	public String getGeoCodeAccuracy() {		
		return geoCodeAccuracy;
	}


	public void setGeoCodeAccuracy(String geoCodeAccuracy) {
		this.geoCodeAccuracy = geoCodeAccuracy;
	}


	public String getPhotoNameBrieft() {
		
		if(this.photoName.length() >= 30 )
			return this.photoName.substring(0, 30 ) + "...";
		else
			return   this.photoName;
	}




	/**
	 * @return the photoNote2
	 */


	

}
