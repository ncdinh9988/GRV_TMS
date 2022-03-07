package com.FiveSGroup.TMS.Webservice;

import android.os.Build;
import android.util.Log;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.Security.P5sSecurity;
import com.FiveSGroup.TMS.global;

import org.json.JSONArray;
import org.json.JSONException;
import org.ksoap2.SoapEnvelope;
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
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class WebserviceAuth {

    private final String NAMESPACE_FSID = "http://5stars.com.vn/";
    private final String SOAP_ACTION_FSID = "http://5stars.com.vn/";

    // lấy link https:/    			https://fsid.5stars.com.vn:9139/WebServiceCenter.asmx
    private final String urlFSID = "https://fsid.5stars.com.vn:9139/WebServiceCenter.asmx";



    private final int timeOut = 120000;// 30 second limit

    private static String authName = "";
    private static String authPasswd = "";
    private String Url = "";

    // hàm đăng ký Https
    private Boolean setAccessHttps() {
        try {
            trustAllHttpsCertificates();
            // Now you are telling the JRE to ignore the hostname
            HostnameVerifier hv = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    // TODO Auto-generated method stub
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            return true;

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            //CmnFns.writeLogError("setAccessHttps " + e1.getMessage());
            return false;
        }

    }

    // đoạn code xử dung cho HTTPs
    // Just add these two functions in your program
    public static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }


    // đoạn code xử dung cho HTTPs
    private static void trustAllHttpsCertificates() throws Exception {

        // Create a trust manager that does not validate certificate chains:

        javax.net.ssl.TrustManager[] trustAllCerts =

                new javax.net.ssl.TrustManager[1];

        javax.net.ssl.TrustManager tm = new miTM();

        trustAllCerts[0] = tm;

        javax.net.ssl.SSLContext sc =

                javax.net.ssl.SSLContext.getInstance("SSL");

        sc.init(null, trustAllCerts, null);

        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(

                sc.getSocketFactory());

    }

    // hàm khỏi tạo chứng thực với thông tin mặc định được xử dụng cho lần kết
    // nối đầu tiên tới FSID
    public Element[] getHeader() {
        // đoạn code crypt data
        try {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            bis = new BufferedInputStream(new FileInputStream("abc"));
            bos = new BufferedOutputStream(new FileOutputStream("abc", false));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        }

        Element h = new Element().createElement(NAMESPACE_FSID, "Authentication");
        Element username = new Element().createElement(NAMESPACE_FSID, "UserName");
        username.addChild(Node.TEXT, "5stars.c33Gom*37237XZAAGF");
        h.addChild(Node.ELEMENT, username);
        Element pass = new Element().createElement(NAMESPACE_FSID, "Password");
        pass.addChild(Node.TEXT,
                "#*&!@((!*37327x4356*!@#&@#&@!6^@!@##@63827341232SS1@25423432");
        h.addChild(Node.ELEMENT, pass);

        return new Element[] { h };
    }
    // hàm khỏi tạo chứng thực với tham số truyền vào
    public Element[] getHeader(String userNameAuth, String passwordAuth,String nameSPACE) {
        // đoạn code crypt data
        try {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            bis = new BufferedInputStream(new FileInputStream("abc"));
            bos = new BufferedOutputStream(new FileOutputStream("abc", false));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        }

        Element h = new Element().createElement(nameSPACE, "Authentication");
        Element username = new Element().createElement(nameSPACE, "UserName");
        username.addChild(Node.TEXT, userNameAuth);
        h.addChild(Node.ELEMENT, username);

        Element pass = new Element().createElement(nameSPACE, "Password");
        pass.addChild(Node.TEXT, passwordAuth);
        h.addChild(Node.ELEMENT, pass);

        return new Element[] { h };
    }

    // hàm kết nối tới Webservice của FSID để lấy các thông tin kết nối tới
    // project MMV-FPIT
    private String getAuth() {
        if (!new CmnFns().isNetworkAvailable()) {
            return "-1";
        }
        if (!this.setAccessHttps())
            return "-1";
        String webServiceFunc = "synchronizeGetAuth";
        // NIC change name_space
        SoapObject request = new SoapObject(this.NAMESPACE_FSID, webServiceFunc);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.headerOut = this.getHeader();
        // Sử dung link FSID service
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                urlFSID, timeOut);

        try {

            androidHttpTransport.call(SOAP_ACTION_FSID + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            String xe = this.decypt(response.toString());
            if (response == null)
                return "-1";
            return xe;

        } catch (Exception e) {
            e.printStackTrace();
          //  CmnFns.writeLogError("getAuth " + e.getMessage());
            return "-1";
        }
    }
    //180720 ***
    public String getInfo() {
        // đoạn code crypt data
        try {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            bis = new BufferedInputStream(new FileInputStream("abc"));
            bos = new BufferedOutputStream(new FileOutputStream("abc", false));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        }

        if (!new CmnFns().isNetworkAvailable()) {
            return "-1";
        }

        // kết nối tới FSID để lấy các thông tin tài khoản và mật khẩu dùng để
        // lấy thông tin về đường link để kết nối tới MMV-FPIT
        // hàm lấy User và Pass, loại mã hóa để lấy từ FSID
        String resultAuth = this.getAuth();
        Log.d("JSON", resultAuth);

        if (resultAuth.equals("-1"))
            return "-1";

        String passowrdEncrypt = "";

        try {
            // gán tài khỏa và mật khẩu đùng để làm thong tin kết nối tới FSID
            // và lấy đường link kết nối
            JSONArray jsonarray = new JSONArray(resultAuth);
            WebserviceAuth.authName = jsonarray.getJSONObject(0)
                    .getString("VALUE").toString();
            WebserviceAuth.authPasswd = jsonarray.getJSONObject(0)
                    .getString("VALUE").toString();
            passowrdEncrypt = jsonarray.getJSONObject(1).getString("VALUE")
                    .toString();
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
           // CmnFns.writeLogError("getInfo " + e1.getMessage());
        }

        if (authName.equals("") || passowrdEncrypt.equals(""))
            return "-1";

        String imei = CmnFns.getImei(global.getAppContext());
        String serial = CmnFns.getSerial();
        String code = "";
        if(CmnFns.isCheckSale()){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                code = CmnFns.readDataShipperNew();
            }else{
                code = CmnFns.readDataShipper();
            }
        }
        if(CmnFns.isCheckAdmin()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                code = CmnFns.readDataAdminNew();
            }else{
                code = CmnFns.readDataAdmin();
            }

        }

//       if(global.getSaleCode() != "") {
//           if(global.getSaleCode() != null){
//               code = global.getSaleCode();
//           }
//        }
//       if(global.getAdminCode() != ""){
//           if(global.getAdminCode() != null){
//               code  = global.getAdminCode();
//           }
//        }

        String data = imei + global.SPLIT_KEY + code + global.SPLIT_KEY
                + serial + global.SPLIT_KEY + CmnFns.getVersionName();

        if (!this.setAccessHttps())
            return "-1";

        String webServiceFunc = "synchronizeGetInfomationFPIT"; // kết nối tới
        // Webservice
        // FSID để lấy
        // thông tin về
        // đường link
        // kết nối
        SoapObject request = new SoapObject(this.NAMESPACE_FSID, webServiceFunc);

        PropertyInfo paramPI = new PropertyInfo();
        paramPI.setName("data");
        paramPI.setValue(CmnFns.encrypt(data, passowrdEncrypt));
        paramPI.setType(String.class);
        request.addProperty(paramPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        // username, pas lấy từ getAuth
        envelope.headerOut = this.getHeader(WebserviceAuth.authName,WebserviceAuth.authPasswd,NAMESPACE_FSID);
        // kết nối service FSID
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                urlFSID, timeOut);
        try {

            androidHttpTransport.call(SOAP_ACTION_FSID + webServiceFunc, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            return this.decypt(response.toString()); // mã hóa kết quả trả về từ
            // FSID

        } catch (Exception e) {
            //CmnFns.writeLogError("getInfo: HttpTransportSE " + e.getMessage());
            return "-1";
        }
    }

    public static String decypt(String text) {
        try {
            String de = P5sSecurity.decrypt(text);
            return de;
        } catch (KeyException e) {
            // TODO Auto-generated catch block
          //  CmnFns.writeLogError("decypt WebserviceAuth " + e.getMessage());
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            //CmnFns.writeLogError("decypt WebserviceAuth " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            //CmnFns.writeLogError("decypt WebserviceAuth " + e.getMessage());
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            //CmnFns.writeLogError("decypt WebserviceAuth " + e.getMessage());
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            // TODO Auto-generated catch block
            //CmnFns.writeLogError("decypt WebserviceAuth " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //CmnFns.writeLogError("decypt WebserviceAuth  " + e.getMessage());
            e.printStackTrace();
        }
        return "-1";
    }






}
