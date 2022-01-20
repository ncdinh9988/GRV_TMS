package com.FiveSGroup.TMS.Webservice;

import android.content.Context;
import android.content.SharedPreferences;
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

import static com.FiveSGroup.TMS.global.getAppContext;

public class Webservice {

    private static String salecode = "";
    private final String NAMESPACE = "http://FPIT.5stars.com.vn/";
    private final String SOAP_ACTION = "http://FPIT.5stars.com.vn/";
    private final int timeOut = 120000;// 2:30 second limit
    private final String urlFSID = "https://fsid.5stars.com.vn:9139/WebServiceCenter.asmx";


    private static String authName = "";
    private static String authPasswd = "";
    private String Url = "";

    SharedPreferences sharedPref = getAppContext().getSharedPreferences("setURL", Context.MODE_PRIVATE);
    String UrlWebserviceToSynchronize = sharedPref.getString("urlConnect", "");

//    public  String Get_Status_Cancel_Good(String cancel_Cd){
//        String webServiceFunc = "Get_Status_Stock_Out";
//
//        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
//        String imei = CmnFns.getImei(global.getAppContext());
//
//        // Param 1
//        PropertyInfo param1 = new PropertyInfo();
//        param1.setName("CANCEL_CD");
//        param1.setValue(cancel_Cd);
//        param1.setType(String.class);
//        request.addProperty(param1);
//
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//                SoapEnvelope.VER11);
//        envelope.dotNet = true;
//        envelope.headerOut = this.getHeader();
//        envelope.setOutputSoapObject(request);
//        HttpTransportSE androidHttpTransport = new HttpTransportSE(
//                UrlWebserviceToSynchronize, timeOut);
//
//        try {
//            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
//            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
//            global.lstLogUp.add("Check Cancel Good : " + CmnFns.getTimeOfPDA(global.getFormatDate()));
//            return response.toString();
//
//        } catch (Exception e) {
//            global.lstLogUp.add("Check Cancel Good : " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
//            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
//            return "-1";
//        }
//    }

    public  String Suggest_Product_For_OD_With_Position(String UserCode ,String OUTBOUND_DELIVERY_CD ,String Warehouse_Position_Cd){
        String webServiceFunc = "Suggest_Product_For_OD_With_Position";

        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        String imei = CmnFns.getImei(getAppContext());

        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("UserCode");
        param1.setValue(UserCode);
        param1.setType(String.class);
        request.addProperty(param1);

        // Param 1
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("OUTBOUND_DELIVERY_CD");
        param2.setValue(OUTBOUND_DELIVERY_CD);
        param2.setType(String.class);
        request.addProperty(param2);

        // Param 1
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("Warehouse_Position_Cd");
        param3.setValue(Warehouse_Position_Cd);
        param3.setType(String.class);
        request.addProperty(param3);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Check Stockout : " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Check Stockout : " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }
    public  String Scan_Outbound_OD(String Warehouse_Position_CD, String OUTBOUND_DELIVERY_CD){
        String webServiceFunc = "Scan_Outbound_OD";

        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        String imei = CmnFns.getImei(getAppContext());

        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("Warehouse_Position_CD");
        param1.setValue(Warehouse_Position_CD);
        param1.setType(String.class);
        request.addProperty(param1);
        // Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("UserCode");
        param2.setValue(CmnFns.readDataAdmin());
        param2.setType(String.class);
        request.addProperty(param2);
        // Param 3
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("OUTBOUND_DELIVERY_CD");
        param3.setValue(OUTBOUND_DELIVERY_CD);
        param3.setType(String.class);
        request.addProperty(param3);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Check Stockout : " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Check Stockout : " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }

    public  String Check_LPN_With_OD(String LPN_CODE, String OUTBOUND_DELIVERY_CD){
        String webServiceFunc = "Check_LPN_With_OD";

        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        String imei = CmnFns.getImei(getAppContext());

        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("LPN_CODE");
        param1.setValue(LPN_CODE);
        param1.setType(String.class);
        request.addProperty(param1);
        // Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("UserCode");
        param2.setValue(CmnFns.readDataAdmin());
        param2.setType(String.class);
        request.addProperty(param2);
        // Param 3
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("OUTBOUND_DELIVERY_CD");
        param3.setValue(OUTBOUND_DELIVERY_CD);
        param3.setType(String.class);
        request.addProperty(param3);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Check Stockout : " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Check Stockout : " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }

    public  String Check_OD_Have_LPN(String OUTBOUND_DELIVERY_CD){
        String webServiceFunc = "Check_OD_Have_LPN";

        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        String imei = CmnFns.getImei(getAppContext());

        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("UserCode");
        param1.setValue(CmnFns.readDataAdmin());
        param1.setType(String.class);
        request.addProperty(param1);

        // Param 1
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("OUTBOUND_DELIVERY_CD");
        param2.setValue(OUTBOUND_DELIVERY_CD);
        param2.setType(String.class);
        request.addProperty(param2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Check Stockout : " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Check Stockout : " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }

    public  String Get_Status_Stock_Out(String order_cd){
        String webServiceFunc = "Get_Status_Stock_Out";

        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        String imei = CmnFns.getImei(getAppContext());

        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("ORDER_CD");
        param1.setValue(order_cd);
        param1.setType(String.class);
        request.addProperty(param1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Check Stockout : " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Check Stockout : " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }

    public String Block_Function_By_Warehouse(){
        String webServiceFunc = "Block_Function_By_Warehouse";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        //         Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("Usercode");
        param1.setValue(global.getAdminCode());
        param1.setType(String.class);
        request.addProperty(param1);
//
//        //         Param 1
//        PropertyInfo param2 = new PropertyInfo();
//        param2.setName("type");
//        param2.setValue(type);
//        param2.setType(String.class);
//        request.addProperty(param2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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

    public String Check_Position_Same_SLOC(String from_cd , String to_cd , String type){
        String webServiceFunc = "Check_Position_Same_SLOC";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        //         Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("POSITION_FROM_CD");
        param1.setValue(from_cd);
        param1.setType(String.class);
        request.addProperty(param1);

        //         Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("POSITION_TO_CD");
        param2.setValue(to_cd);
        param2.setType(String.class);
        request.addProperty(param2);

        //         Param 3
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("TYPE");
        param3.setValue(type);
        param3.setType(String.class);
        request.addProperty(param3);



        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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


    public String synchronizeCustomerAddNew(String json) {

        String webServiceFunc = "synchronizeCustomerAddNew";

        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        String imei = CmnFns.getImei(getAppContext());

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
                UrlWebserviceToSynchronize, timeOut);

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
        String imei = CmnFns.getImei(getAppContext());
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
                UrlWebserviceToSynchronize);

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
        String imei = CmnFns.getImei(getAppContext());
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
                UrlWebserviceToSynchronize);

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
        String test1 = UrlWebserviceToSynchronize;
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize);

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

    public String Check_Position_With_Usercode_WST(String barcode) {

        String webServiceFunc = "Check_Position_With_Usercode_WST";

        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        // Param 2
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("UserCode");
        param1.setValue(CmnFns.readDataAdmin());
        param1.setType(String.class);
        request.addProperty(param1);

        // Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("Barcode");
        param2.setValue(barcode);
        param2.setType(String.class);
        request.addProperty(param2);

        // Param 2
        PropertyInfo param4 = new PropertyInfo();
        param4.setName("Stock_Take_CD");
        param4.setValue(global.getInventoryCD());
        param4.setType(String.class);
        request.addProperty(param4);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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

    public String GetProduct_Code(String barcode) {

        String webServiceFunc = "Get_Product_Code_With_Barcode";

        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        // Param 2
        PropertyInfo param4 = new PropertyInfo();
        param4.setName("Barcode");
        param4.setValue(barcode);
        param4.setType(String.class);
        request.addProperty(param4);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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
                UrlWebserviceToSynchronize, timeOut);

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


    public String synchronizeGETBatch(String usercode , String qrcode , String stock) {

        String webServiceFunc = "Get_Batch_For_PO";
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
        param2.setValue(usercode);
        param2.setType(String.class);
        request.addProperty(param2);
        //         Param 3
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("Stock_Receipt_CD");
        param3.setValue(stock);
        param3.setType(String.class);
        request.addProperty(param3);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

        try {
            androidHttpTransport.call(SOAP_ACTION + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            global.lstLogUp.add("Upload: Create Batch Number Success" + CmnFns.getTimeOfPDA(global.getFormatDate()));
            return response.toString();

        } catch (Exception e) {
            global.lstLogUp.add("Upload: Create Batch Number Failed: " + e.getMessage() + " " + CmnFns.getTimeOfPDA(global.getFormatDate()));
            //  CmnFns.writeLogError("AddNew:  " + e.getMessage());
            return "-1";
        }
    }


    public String synchronizeGETBatchAndProduct(String usercode,String qrcode, String stock, String batch) {


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
        //         Param 3
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("Usercode");
        param3.setValue(usercode);
        param3.setType(String.class);
        request.addProperty(param3);

        //         Param 4
        PropertyInfo param4 = new PropertyInfo();
        param4.setName("Batch");
        param4.setValue(batch);
        param4.setType(String.class);
        request.addProperty(param4);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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




    public String synchronizeGETProductInfoo(String usercode,String qrcode, String stock) {


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
        //         Param 3
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("Usercode");
        param3.setValue(usercode);
        param3.setType(String.class);
        request.addProperty(param3);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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

    public String getChuyenMaMateril(String qrcode, String type ) {
        String webServiceFunc = "";

        webServiceFunc = "GetMaterialItemBasicGroup";


        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("BarCode");
        param1.setValue(qrcode);
        param1.setType(String.class);
        request.addProperty(param1);



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
                UrlWebserviceToSynchronize, timeOut);
        Log.d("checkURL", UrlWebserviceToSynchronize);

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

    public String GetMaterialInspection(String barcode, String usercode, String batch) {
        String webServiceFunc = "";

        webServiceFunc = "Get_Material_Inspection";


        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("UserCode");
        param1.setValue(usercode);

        param1.setType(String.class);
        request.addProperty(param1);

//         Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("BarCode");
        param2.setValue(barcode);
        param2.setType(String.class);
        request.addProperty(param2);

        PropertyInfo param3 = new PropertyInfo();
        param3.setName("Batch");
        param3.setValue(batch);
        param3.setType(String.class);
        request.addProperty(param3);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);
        Log.d("checkURL", UrlWebserviceToSynchronize);

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


    public String GetSPChuyenMa(String qrcode, String salescode, String type , int IsLPN , String CD) {
        String webServiceFunc = "";

        webServiceFunc = "GetProductByZone_RQBT_Final";


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
                UrlWebserviceToSynchronize, timeOut);
        Log.d("checkURL", UrlWebserviceToSynchronize);

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
    public String Check_Quantity_LPN_With_SO(String lpn_code ) {
        String webServiceFunc = "";

        webServiceFunc = "Check_Quantity_LPN_With_SO";

        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        PropertyInfo param3 = new PropertyInfo();
        param3.setName("LPN_CODE");
        param3.setValue(lpn_code);
        param3.setType(String.class);
        request.addProperty(param3);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);
        Log.d("checkURL", UrlWebserviceToSynchronize);

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

    public String Check_Product_In_SO(String product_cd, String product_code, String lpn_code ) {
        String webServiceFunc = "";

        webServiceFunc = "Check_Product_In_SO";

        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("PRODUCT_CD");
        param1.setValue(product_cd);
        param1.setType(String.class);
        request.addProperty(param1);

//         Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("PRODUCT_CODE");
        param2.setValue(product_code);
        param2.setType(String.class);
        request.addProperty(param2);

        PropertyInfo param3 = new PropertyInfo();
        param3.setName("LPN_CODE");
        param3.setValue(lpn_code);
        param3.setType(String.class);
        request.addProperty(param3);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);
        Log.d("checkURL", UrlWebserviceToSynchronize);

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

    public String GetProductByZone_With_Position(String qrcode, String salescode, String type , int IsLPN , String CD ,String Warehouse_Position_CD) {
        String webServiceFunc = "";

        webServiceFunc = "GetProductByZone_With_Position";


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

        PropertyInfo param6 = new PropertyInfo();
        param6.setName("Warehouse_Position_CD");
        param6.setValue(Warehouse_Position_CD);
        param6.setType(String.class);
        request.addProperty(param6);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);
        Log.d("checkURL", UrlWebserviceToSynchronize);

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
        String webServiceFunc = "";
        if(type.equals("WTP") || type.equals("WQA") || type.equals("WQA_Return")){
            webServiceFunc = "GetProductByZone_RQBT";
        }else{
            webServiceFunc = "GetProductByZone";
        }

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
                UrlWebserviceToSynchronize, timeOut);
        Log.d("checkURL", UrlWebserviceToSynchronize);

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

    public String Check_Suggest_Position_Master_Pick(String UserCode, String ProductCode, String Unit ,
                                                     String LPNCode, String PositionCode, String Stockin, String Expired , String CD) {

        String webServiceFunc = "Check_Suggest_Position_Master_Pick";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("UserCode");
        param1.setValue(UserCode);
        param1.setType(String.class);
        request.addProperty(param1);

        // Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("ProductCode");
        param2.setValue(ProductCode);
        param2.setType(String.class);
        request.addProperty(param2);

        // Param 3
        PropertyInfo param3 = new PropertyInfo();
        param3.setName("Unit");
        param3.setValue(Unit);
        param3.setType(String.class);
        request.addProperty(param3);

        // Param 4
        PropertyInfo param4 = new PropertyInfo();
        param4.setName("LPNCode");
        param4.setValue(LPNCode);
        param4.setType(String.class);
        request.addProperty(param4);

        // Param 5
        PropertyInfo param5 = new PropertyInfo();
        param5.setName("PositionCode");
        param5.setValue(PositionCode);
        param5.setType(String.class);
        request.addProperty(param5);

        // Param 5
        PropertyInfo param6 = new PropertyInfo();
        param6.setName("Stockin");
        param6.setValue(Stockin);
        param6.setType(String.class);
        request.addProperty(param6);

        // Param 5
        PropertyInfo param7 = new PropertyInfo();
        param7.setName("Expired");
        param7.setValue(Expired);
        param7.setType(String.class);
        request.addProperty(param7);

        PropertyInfo param8 = new PropertyInfo();
        param8.setName("CD");
        param8.setValue(CD);
        param8.setType(String.class);
        request.addProperty(param8);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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
        String webServiceFunc = "";
        if(type.equals("WTP") || type.equals("WQA")|| type.equals("WQA_Return")){
            webServiceFunc = "synchronizeGETPositionInfo_RQBT";
        }else{
            webServiceFunc = "synchronizeGETPositionInfo";
        }

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
                UrlWebserviceToSynchronize, timeOut);

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

    public String synchronizeStockReceiptChecked(String json , String usercode) {


        String webServiceFunc = "synchronizeStockReceiptChecked";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("jsonData");
        param1.setValue(json);
        param1.setType(String.class);
        request.addProperty(param1);

        PropertyInfo param2 = new PropertyInfo();
        param2.setName("UserCode");
        param2.setValue(usercode);
        param2.setType(String.class);
        request.addProperty(param2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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
    public String synchronizeData(String json) {

        String webServiceFunc = "Convert_UOM";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);
        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("jsonData");
        param1.setValue(json);
        param1.setType(String.class);
        request.addProperty(param1);

        PropertyInfo param2 = new PropertyInfo();
        param2.setName("USER_CODE");
        param2.setValue(CmnFns.readDataAdmin());
        param2.setType(String.class);
        request.addProperty(param2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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
        String webServiceFunc ;
        if((type.equals("WTP")) || (type.equals("WQA")) || (type.equals("WQA_Return"))){
            webServiceFunc = "synchronizeData_RQBT";
        }else{
            webServiceFunc = "synchronizeData";
        }

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
                UrlWebserviceToSynchronize, timeOut);

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

    public String synchronizeData_RQBT_Final(String json, String usercode, String type) {
        String webServiceFunc = "synchronizeData_RQBT_Final";


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
                UrlWebserviceToSynchronize, timeOut);

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
                UrlWebserviceToSynchronize, timeOut);

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

    public String GetHH_Param_Layout(String saleCode) {

        String webServiceFunc = "GET_PROFILE_USER";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        String value = CmnFns.getImei(getAppContext());

        // Param 1
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("USER_CODE");
        param2.setValue(saleCode);
        param2.setType(String.class);
        request.addProperty(param2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        //envelope.headerOut = this.getHeader(WebserviceAuth.authName,WebserviceAuth.authPasswd,NAMESPACE_FSID);
//        HttpTransportSE androidHttpTransport = new HttpTransportSE(
//                UrlWebserviceToSynchronize);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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

        String value = CmnFns.getImei(getAppContext());

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
//                UrlWebserviceToSynchronize);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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
    public String GET_SuggetPosition_MasterPick(String cd) {

        String webServiceFunc = "GET_SuggetPosition_MasterPick";
//        String webServiceFunc = "showLPNCode";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("UserCode");
        param1.setValue(CmnFns.readDataAdmin());
        param1.setType(String.class);
        request.addProperty(param1);

//        // Param 2
        PropertyInfo param2 = new PropertyInfo();
        param2.setName("Picklist_CD");
        param2.setValue(cd);
        param2.setType(String.class);
        request.addProperty(param2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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

    public String GetParam_LPNwithSO(String cd) {

        String webServiceFunc = "showLPNCode_In_Master_Pick";
//        String webServiceFunc = "showLPNCode";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

//        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("PICKLIST_CD");
        param1.setValue(cd);
        param1.setType(String.class);
        request.addProperty(param1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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


    public String GetParam_LPN() {

        String webServiceFunc = "showLPNCode_By_User";
//        String webServiceFunc = "showLPNCode";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

//        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("UserCode");
        param1.setValue(CmnFns.readDataAdmin());
        param1.setType(String.class);
        request.addProperty(param1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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

        String webServiceFunc = "createLPNCode_By_User";
        SoapObject request = new SoapObject(this.NAMESPACE, webServiceFunc);

        // Param 1
        PropertyInfo param1 = new PropertyInfo();
        param1.setName("UserCode");
        param1.setValue(CmnFns.readDataAdmin());
        param1.setType(String.class);
        request.addProperty(param1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.headerOut = this.getHeader();
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                UrlWebserviceToSynchronize, timeOut);

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
                UrlWebserviceToSynchronize, timeOut);

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
