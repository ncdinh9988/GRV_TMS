package com.FiveSGroup.TMS.AddCustomerFragment;

import android.graphics.Bitmap;

import androidx.annotation.Keep;

import com.FiveSGroup.TMS.global;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


@Keep
public class CCustomer implements Comparable,Serializable {

    String customerCode;
    String customerName;
    String customerPhoneNumber;
    String customerAddress;
    String customerCategory;
    float customerDistance;
    String customerLocation;
    String customerLocationAccuracy;
    Boolean customerActive;
    String customerCreatedDate;
    int customerMtd = 0;

    String customerInfo1;
    String customerInfo2;
    String customerInfo3;
    String customerInfo4;
    String customerInfo5;

    String customerLocationAddress;


    Boolean customerAllowSync;
    String customerRoute;

    String cusMtd;
    String cus2M;
    String cus3M;

    ArrayList<Bitmap> arrBitmaps;

    public ArrayList<Bitmap> getArrBitmaps() {
        return arrBitmaps;
    }

    public void setArrBitmaps(ArrayList<Bitmap> arrBitmaps) {
        this.arrBitmaps = arrBitmaps;
    }

    public String getCusMtd() {
        return cusMtd;
    }

    public void setCusMtd(String cusMtd) {
        this.cusMtd = cusMtd;
    }

    public String getCus2M() {
        return cus2M;
    }

    public void setCus2M(String cus2M) {
        this.cus2M = cus2M;
    }

    public String getCus3M() {
        return cus3M;
    }

    public void setCus3M(String cus3M) {
        this.cus3M = cus3M;
    }

    public String getCustomerLocationAddress() {
        return customerLocationAddress;
    }

    public void setCustomerLocationAddress(String customerLocationAddress) {
        this.customerLocationAddress = customerLocationAddress;
    }



    public Boolean getCustomerAllowSync() {
        return customerAllowSync;
    }

    public void setCustomerAllowSync(Boolean customerAllowSync) {
        this.customerAllowSync = customerAllowSync;
    }

    public String getCustomerRoute() {
        return customerRoute.replaceAll(global.RegexReplaceStringNonASCII, "");
    }

    public void setCustomerRoute(String customerRoute) {
        this.customerRoute = customerRoute.replaceAll(global.RegexReplaceStringNonASCII, "");
    }

    public String getCustomerCreatedDate() {
        return customerCreatedDate;
    }

    public void setCustomerCreatedDate(String customerCreatedDate) {
        this.customerCreatedDate = customerCreatedDate;
    }


    public Boolean getCustomerActive() {
        return customerActive;
    }

    public void setCustomerActive(Boolean customerActive) {
        this.customerActive = customerActive;
    }

    public String getCustomerLocationAccuracy() {
        return customerLocationAccuracy;
    }

    public void setCustomerLocationAccuracy(String customerLocationAccuracy) {
        this.customerLocationAccuracy = customerLocationAccuracy;
    }


    public int getCustomerMtd() {
        return customerMtd;
    }

    public void setCustomerMtd(int customerMtd) {
        this.customerMtd = customerMtd;
    }


    Date customerTimeIn;
    Date customerTimeOut;



    public CCustomer()
    {
        this.customerCode = "";
        this.customerName = "";
        this.customerAddress = "";
        this.customerPhoneNumber = "";
        this.customerCategory = "";
        this.customerDistance = 0;
        this.customerLocation = "";
        this.customerLocationAccuracy = "";
        this.customerTimeIn = null;
        this.customerTimeOut = null;
        this.customerActive = true;
        this.customerRoute = "";
        this.customerAllowSync = false;
        this.customerLocationAddress = "";
    }

    public CCustomer(String customerCode, String customerName, String customerAddress,
                     String customerCategory, float customerDistance,
                     String customerLocation, String customerLocationAccuracy, Boolean customerActive,String customerRoute,
                     Boolean customerAllowSync, String customerLocationAddress)
    {
        this.customerCode = customerCode; // code;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerCategory = customerCategory;
        this.customerDistance = customerDistance;
        this.customerLocation = customerLocation;
        this.customerLocationAccuracy = customerLocationAccuracy;
        this.customerTimeIn = null;
        this.customerTimeOut = null;
        this.customerActive = customerActive;
        this.customerRoute = customerRoute;
        this.customerAllowSync = customerAllowSync;
        this.customerLocationAddress = customerLocationAddress;

    }




    public CCustomer(String customerCode, String customerName, String customerAddress,
                     String customerCategory, float customerDistance,
                     String customerLocation,Boolean customerActive,String customerRoute,Boolean customerAllowSync, String customerLocationAddress
    )
    {
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerCategory = customerCategory;
        this.customerDistance = customerDistance;
        this.customerLocation = customerLocation;
        this.customerLocationAccuracy = "";
        this.customerTimeIn = null;
        this.customerTimeOut = null;
        this.customerActive = customerActive;
        this.customerRoute = customerRoute;
        this.customerAllowSync = customerAllowSync;
        this.customerLocationAddress = customerLocationAddress;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof CCustomer &&
                ((CCustomer) o).getCustomerCode().equals(this.getCustomerCode()) );
    }

    public String getCustomerCode() {
        return customerCode.replaceAll(global.RegexReplaceStringNonASCII, "");
    }
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode.replaceAll(global.RegexReplaceStringNonASCII, ""); // code;;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }
    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerCategory() {
        return customerCategory.replaceAll(global.RegexReplaceStringNonASCII, "");
    }
    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory.replaceAll(global.RegexReplaceStringNonASCII, "");
    }

    public float getCustomerDistance() {
        return customerDistance;
    }
    public void setCustomerDistance(float customerDistance) {
        this.customerDistance = customerDistance;
        this.customerDistance = Math.round(this.customerDistance);
    }
    public String getCustomerLocation() {
        return customerLocation;
    }
    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }
    public Date getCustomerTimeIn() {
        return customerTimeIn;
    }
    public void setCustomerTimeIn(Date customerTimeIn) {
        this.customerTimeIn = customerTimeIn;
    }
    public Date getCustomerTimeOut() {
        return customerTimeOut;
    }
    public void setCustomerTimeOut(Date customerTimeOut) {
        this.customerTimeOut = customerTimeOut;
    }


    public String getCustomerInfo1() {
        return customerInfo1;
    }

    public void setCustomerInfo1(String customerInfo1) {
        this.customerInfo1 = customerInfo1;
    }

    public String getCustomerInfo2() {
        return customerInfo2;
    }

    public void setCustomerInfo2(String customerInfo2) {
        this.customerInfo2 = customerInfo2;
    }

    public String getCustomerInfo3() {
        return customerInfo3;
    }

    public void setCustomerInfo3(String customerInfo3) {
        this.customerInfo3 = customerInfo3;
    }

    public String getCustomerInfo4() {
        return customerInfo4;
    }

    public void setCustomerInfo4(String customerInfo4) {
        this.customerInfo4 = customerInfo4;
    }

    public String getCustomerInfo5() {
        return customerInfo5;
    }

    public void setCustomerInfo5(String customerInfo5) {
        this.customerInfo5 = customerInfo5;
    }


    //hàm quan trọng để xác định các thông tin nào của KH được tìm kiếm
    //hàm này đc xử dụng ở class ACustomer.java
    public String autoFilter() {
        return this.customerCode.toLowerCase() + ";" + this.customerName.toLowerCase() ;
    }


    //hàm so sanh khoảng cách giữa 2 đối tượng KH
    //đc xử dụng để sắp xếp khách hàng theo khoảng cách
    @Override
    public int compareTo(Object c) {

        CCustomer cust = (CCustomer)c;

        if (this.customerDistance > cust.getCustomerDistance() ) {
            return 1;
        }
        else if (this.customerDistance < cust.getCustomerDistance() ) {
            return -1;
        }
        else {
            return 0;
        }

    }


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.customerCode + this.customerName + this.customerCategory;
    }

    public List<CCustomer> arrCustNotBought(String json)
    {
        List<CCustomer> arrCust = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobj = jsonArray.getJSONObject(i);
                CCustomer ccustomer = new CCustomer();
                ccustomer.setCustomerCode(jsonobj.getString("CustomerCode"));
                ccustomer.setCustomerName(jsonobj.getString("CustomerName"));
                ccustomer.setCustomerPhoneNumber(jsonobj.getString("CustomerPhoneNumber"));
                ccustomer.setCustomerAddress(jsonobj.getString("CustomerAddress"));
                ccustomer.setCustomerCategory(jsonobj.getString("CustomerChainCode"));
                ccustomer.setCustomerActive(jsonobj.getBoolean("CustomerActive"));

                ccustomer.setCustomerLocation(jsonobj.getString("CustomerLatitudeLongitude"));
                ccustomer.setCustomerLocationAccuracy(jsonobj.getString("CustomerLatitudeLongitudeAccuracy"));
                ccustomer.setCustomerRoute(jsonobj.getString("CustomerRoute"));
                arrCust.add(ccustomer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrCust;
    }


}

