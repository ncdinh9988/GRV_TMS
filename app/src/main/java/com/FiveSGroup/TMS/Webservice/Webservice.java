package com.FiveSGroup.TMS.Webservice;

import android.util.Log;

import com.FiveSGroup.TMS.CmnFns;

import com.FiveSGroup.TMS.global;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Webservice {

    private static String salecode = "";
    private final String NAMESPACE = "http://FPIT.5stars.com.vn/";
    private final String SOAP_ACTION = "http://FPIT.5stars.com.vn/";
    private final int timeOut = 120000;// 2:30 second limit
    private final String urlFSID = "https://fsid.5stars.com.vn:9139/WebServiceCenter.asmx";


    private static String authName = "";
    private static String authPasswd = "";
    private String Url = "";


    public String synchronizeCustomerAddNew(String json) {

        String webServiceFunc = "synchronizeCustomerAddNew";

        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        String imei = CmnFns.getImei(global.getAppContext());

        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("imei");
        param1.setValue(imei);
        param1.setType(String.class);
        request.addProperty(param1);

        // Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("obj");
        param2.setValue("SALES_HH");
        param2.setType(String.class);
        request.addProperty(param2);

        // Param 3
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("type");
        param3.setValue("1");
        param3.setType(String.class);
        request.addProperty(param3);

        // Param 4
        PropertyInfo param4 = new PropertyInfo();
        param4.setName("jsonData");
        param4.setValue(json);
        param4.setType(String.class);
        request.addProperty(param4);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize(), timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: AddNew_Customer Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: AddNew_Customer Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }

    public String synchronizePhoto(String jsonData, byte[] img) {

        // TODO Auto-generated method stub
        if (!new CmnFns().isNetworkAvailable()) {
            return "-1";
        }

        String webServiceFunc = "synchronizePhotoV2";
        String imei = CmnFns.getImei(global.getAppContext());
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("imei");
        param1.setValue(imei);
        param1.setType(String.class);
        request.addProperty(param1);

        // Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("obj");
        param2.setValue(global.getSaleCode());
        param2.setType(String.class);
        request.addProperty(param2);

        // Param 3
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("type");
        param3.setValue("1");
        param3.setType(String.class);
        request.addProperty(param3);

        // Param 4
        PropertyInfo param4 = new PropertyInfo();
        param4.setName("jsonData");
        param4.setValue(jsonData);
        param4.setType(String.class);
        request.addProperty(param4);

        // Param 5
        PropertyInfo param5 = new PropertyInfo();
        param5.setName("image");
        param5.setValue(img);
        request.addProperty(param5);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        new MarshalBase64().register(envelope);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize());

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: synchronizePhoto Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: synchronizePhoto Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            // CmnFns.writeLogError("synchronizePhoto:  " + e.getMessage());
            return "-1";
        }
    }

    public String synchronizePhotoForOrder(String obj, String type, String jsonData, byte[] image) {
        // TODO Auto-generated method stub
        if (!new CmnFns().isNetworkAvailable()) {
            return "-1";
        }

        String webServiceFunc = "synchronizePhotoV2";
        String imei = CmnFns.getImei(global.getAppContext());
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("imei");
        param1.setValue(imei);
        param1.setType(String.class);
        request.addProperty(param1);

        // Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("obj");
        param2.setValue(obj);
        param2.setType(String.class);
        request.addProperty(param2);

        // Param 3
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("type");
        param3.setValue(type);
        param3.setType(String.class);
        request.addProperty(param3);

        // Param 4
        PropertyInfo param4 = new PropertyInfo();
        param4.setName("jsonData");
        param4.setValue(jsonData);
        param4.setType(String.class);
        request.addProperty(param4);

        // Param 5
        PropertyInfo param5 = new PropertyInfo();
        param5.setName("image");
        param5.setValue(image);
        request.addProperty(param5);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        new MarshalBase64().register(envelope);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize());

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: synchronizePhoto Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: synchronizePhoto Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            // CmnFns.writeLogError("synchronizePhoto:  " + e.getMessage());
            return "-1";
        }
    }

    // tkd - getHeader 180703
    private Element[] getHeader() {
        // đoạn code scrypt data
        try {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            bis = new BufferedInputStream(new FileInputStream("abc"));
            bos = new BufferedOutputStream(new FileOutputStream("abc", false));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        }

        Element h = new Element().createElement(NAMESPACE, "Authentication");

        Element username = new Element().createElement(NAMESPACE, "UserName");
        username.addChild(Node.TEXT, global.getUserNameAuthWebsevice());
        //global.getUserNameAuthWebsevice()
        h.addChild(Node.ELEMENT, username);

        Element pass = new Element().createElement(NAMESPACE, "Password");
        pass.addChild(Node.TEXT, global.getPasswordNameAuthWebsevice());
        //global.getPasswordNameAuthWebsevice()
        h.addChild(Node.ELEMENT, pass);

        return new Element[]{h};
    }

    public String synchronizeGETCustomerInfo(String CUSTOMER_CODE, String DELIVERY_CODE) {

        if (!new CmnFns().isNetworkAvailable()) {
            return "-1";
        }

        String webServiceFunc = "synchronizeGETCustomerInfo";
        //String imei = CmnFns.getSerial();
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("CUSTOMER_CD");
        param1.setValue(CUSTOMER_CODE);
        param1.setType(String.class);
        request.addProperty(param1);

        // Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("DELIVERER_CODE");
        param2.setValue(global.getSaleCode());
        param2.setType(String.class);
        request.addProperty(param2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        new MarshalBase64().register(envelope);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        String test1 = global.getUrlWebserviceToSynchronize();
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize());

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: synchronizePhoto Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: synchronizePhoto Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            // CmnFns.writeLogError("synchronizePhoto:  " + e.getMessage());
            return "-1";
        }
    }

    public String synchronizeCustomerInfo(String json) {

        String webServiceFunc = "synchronizeCustomerInfo";

        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        // Param 1
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("CustomerCD");
        param3.setValue(global.getCustomercd());
        param3.setType(String.class);
        request.addProperty(param3);

        // Param 2
        PropertyInfo param4 = new PropertyInfo();
        param4.setName("jsonData");
        param4.setValue(json);
        param4.setType(String.class);
        request.addProperty(param4);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize(), timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: AddNew_Customer Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: AddNew_Customer Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }




    public String synchronizeGETProductInfoo(String qrcode, String stock) {


        String webServiceFunc = "synchronizeGETProductInfo";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("BarCode");
        param1.setValue(qrcode);
        param1.setType(String.class);
        request.addProperty(param1);

//         Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("Stock_Receipt_CD");
        param2.setValue(stock);
        param2.setType(String.class);
        request.addProperty(param2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize(), timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: AddNew_Customer Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: AddNew_Customer Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }


    public String GetProductByZone(String qrcode, String salescode, String type , int IsLPN , String CD) {

        String webServiceFunc = "GetProductByZone";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("BarCode");
        param1.setValue(qrcode);
        param1.setType(String.class);
        request.addProperty(param1);

//         Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("UserCode");
        param2.setValue(salescode);
        param2.setType(String.class);
        request.addProperty(param2);

        PropertyInfo param3 = new PropertyInfo();
        param3.setName("Type");
        param3.setValue(type);
        param3.setType(String.class);
        request.addProperty(param3);

        PropertyInfo param4 = new PropertyInfo();
        param4.setName("IsLPN");
        param4.setValue(IsLPN);
        param4.setType(String.class);
        request.addProperty(param4);

        PropertyInfo param5 = new PropertyInfo();
        param5.setName("CD");
        param5.setValue(CD);
        param5.setType(String.class);
        request.addProperty(param5);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize(), timeOut);
        Log.d("checkURL", global.getUrlWebserviceToSynchronize());

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: AddNew_Customer Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: AddNew_Customer Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }

    public String synchronizeGETPositionInfo(String userCode, String barecode, int isLPN , String type, String positionFrom, String positionTo, String typePosition) {

        String isLPNFormat = String.valueOf(isLPN);

        String webServiceFunc = "synchronizeGETPositionInfo";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("BarCode");
        param1.setValue(barecode);
        param1.setType(String.class);
        request.addProperty(param1);

        // Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("IsLPN");
        param2.setValue(isLPNFormat);
        param2.setType(String.class);
        request.addProperty(param2);

        // Param 3
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("Type");
        param3.setValue(type);
        param3.setType(String.class);
        request.addProperty(param3);

        // Param 4
        PropertyInfo param4 = new PropertyInfo();
        param4.setName("From");
        param4.setValue(positionFrom);
        param4.setType(String.class);
        request.addProperty(param4);

        // Param 5
        PropertyInfo param5 = new PropertyInfo();
        param5.setName("To");
        param5.setValue(positionTo);
        param5.setType(String.class);
        request.addProperty(param5);

        // Param 5
        PropertyInfo param6 = new PropertyInfo();
        param6.setName("positionType");
        param6.setValue(typePosition);
        param6.setType(String.class);
        request.addProperty(param6);

        // Param 5
        PropertyInfo param7 = new PropertyInfo();
        param7.setName("UserCode");
        param7.setValue(userCode);
        param7.setType(String.class);
        request.addProperty(param7);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize(), timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: AddNew_Customer Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: AddNew_Customer Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }

    public String synchronizeStockReceiptChecked(String json) {


        String webServiceFunc = "synchronizeStockReceiptChecked";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("jsonData");
        param1.setValue(json);
        param1.setType(String.class);
        request.addProperty(param1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize(), timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: AddNew_Customer Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: AddNew_Customer Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }



    public String synchronizeData(String json, String usercode, String type) {

        String webServiceFunc = "synchronizeData";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("jsonData");
        param1.setValue(json);
        param1.setType(String.class);
        request.addProperty(param1);

        PropertyInfo param2 = new PropertyInfo();
        param2.setName("USER_CODE");
        param2.setValue(usercode);
        param2.setType(String.class);
        request.addProperty(param2);

        PropertyInfo param3 = new PropertyInfo();
        param3.setName("Type");
        param3.setValue(type);
        param3.setType(String.class);
        request.addProperty(param3);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize(), timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: AddNew_Customer Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: AddNew_Customer Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }

    public String GetEa_Unit(String qrcode, String IsDefault) {


        String webServiceFunc = "GetProductUnit";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("BarCode");
        param1.setValue(qrcode);
        param1.setType(String.class);
        request.addProperty(param1);

        PropertyInfo param2 = new PropertyInfo();
        param2.setName("IsDefault");
        param2.setValue(IsDefault);
        param2.setType(String.class);
        request.addProperty(param2);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize(), timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: AddNew_Customer Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: AddNew_Customer Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }






    public String GetHH_Param_Temp(String saleCode) {

        String webServiceFunc = "getInfoParam";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        String value = CmnFns.getImei(global.getAppContext());

        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("imei");
        param1.setValue(value);
        param1.setType(String.class);
        request.addProperty(param1);

        // Param 1
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("obj");
        param2.setValue(saleCode);
        param2.setType(String.class);
        request.addProperty(param2);

        // Param 1
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("type");
        param3.setValue(1);
        param3.setType(String.class);
        request.addProperty(param3);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        //envelope.headerOut = this.getHeader(WebserviceAuth.authName,WebserviceAuth.authPasswd,NAMESPACE_FSID);
//        HttpTransportSE androidHttpTransport = new HttpTransportSE(
//                global.getUrlWebserviceToSynchronize());
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize(), timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: AddNew_Customer Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return WebserviceAuth.decypt(response.toString());

        } catch (Exception e) {
            global.lstLogUp.add("Upload: AddNew_Customer Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }

    public String GetParam_LPN() {

        String webServiceFunc = "showLPNCode";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize(), timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: AddNew_Customer Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: AddNew_Customer Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }

    public String Create_LPN() {

        String webServiceFunc = "createLPNCode";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize(), timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: AddNew_Customer Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: AddNew_Customer Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }

    public String GetProductLetDownSuggestion(String userCode) {


        String webServiceFunc = "Get_Product_LetDown";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("USER_CODE");
        param1.setValue(userCode);
        param1.setType(String.class);
        request.addProperty(param1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                global.getUrlWebserviceToSynchronize(), timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: AddNew_Customer Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: AddNew_Customer Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }


}
