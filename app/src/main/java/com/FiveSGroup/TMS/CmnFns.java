package com.FiveSGroup.TMS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.FiveSGroup.TMS.AddCustomerFragment.CCustomer;
import com.FiveSGroup.TMS.CancelGood.Product_CancelGood;
import com.FiveSGroup.TMS.ChangeCusFragment.UpdateCustomer;
import com.FiveSGroup.TMS.Inventory.InventoryProduct;
import com.FiveSGroup.TMS.LPN.LPN;
import com.FiveSGroup.TMS.LPN.LPNProduct;
import com.FiveSGroup.TMS.LetDown.LetDownProductSuggest;
import com.FiveSGroup.TMS.LetDown.ProductLetDown;
import com.FiveSGroup.TMS.ListOD.Product_OD;
import com.FiveSGroup.TMS.LoadPallet.LPNwithSO.ProductLpnWithSo;
import com.FiveSGroup.TMS.LoadPallet.Product_LoadPallet;
import com.FiveSGroup.TMS.MasterPick.Product_Master_Pick;
import com.FiveSGroup.TMS.PickList.PickList;
import com.FiveSGroup.TMS.PoReturn.Product_PoReturn;
import com.FiveSGroup.TMS.PutAway.Ea_Unit_Tam;
import com.FiveSGroup.TMS.PutAway.Product_PutAway;
import com.FiveSGroup.TMS.QA.HomeQA.Image_QA.Product_Photo_QA;
import com.FiveSGroup.TMS.QA.HomeQA.Product_Criteria;
import com.FiveSGroup.TMS.QA.HomeQA.Product_QA;
import com.FiveSGroup.TMS.QA.HomeQA.Product_Result_QA;
import com.FiveSGroup.TMS.QA.Pickup.Product_Pickup;
import com.FiveSGroup.TMS.QA.Return_QA.Product_Return_QA;
import com.FiveSGroup.TMS.RemoveFromLPN.Product_Remove_LPN;
import com.FiveSGroup.TMS.ReturnWareHouse.Product_Return_WareHouse;
import com.FiveSGroup.TMS.Security.P5sSecurity;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.StockOut.OD.Product_Stockout_OD;
import com.FiveSGroup.TMS.StockOut.Product_StockOut;
import com.FiveSGroup.TMS.StockTransfer.Product_StockTransfer;
import com.FiveSGroup.TMS.TransferQR.ChuyenMa.Product_ChuyenMa;
import com.FiveSGroup.TMS.TransferQR.ChuyenMa.Product_Material;
import com.FiveSGroup.TMS.TransferQR.ChuyenMa.Product_SP;
import com.FiveSGroup.TMS.TransferQR.TransferPosting.Product_TransferPosting;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitProduct;
import com.FiveSGroup.TMS.Warehouse.Batch_number_Tam;
import com.FiveSGroup.TMS.Warehouse.Exp_Date_Tam;
import com.FiveSGroup.TMS.Warehouse.Product_Qrcode;
import com.FiveSGroup.TMS.Warehouse.Product_S_P;
import com.FiveSGroup.TMS.Warehouse_Adjustment.Product_Warehouse_Adjustment;
import com.FiveSGroup.TMS.Webservice.CParam;
import com.FiveSGroup.TMS.Webservice.Webservice;
import com.FiveSGroup.TMS.Webservice.WebserviceAuth;
import com.FiveSGroup.TMS.getData.ParamLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.FiveSGroup.TMS.global.getAdminCode;
import static com.FiveSGroup.TMS.global.getAppContext;
import static com.FiveSGroup.TMS.global.getSaleCode;

public class CmnFns {
    private static CmnFns cmnFns = null;

    public static CmnFns getInstance() {
        if (cmnFns != null) {
            return cmnFns;
        } else {
            return new CmnFns();
        }
    }

    // h??m ki???m tra xem thi???t b??? c?? t???n t???i nh???ng file c???u h??nh hay ch??a
    public static Boolean isValidDevice() {
        String rootPath = String.format("%s/%s", Environment.getExternalStorageDirectory(), "TMS");
        //String path = Environment.getExternalStorageDirectory()+ File.separator + fsys;
        String path = rootPath;

        File file = new File(path);

        if (!file.isDirectory())
            return false;

        if (getSaleCode().trim().equals(""))
            return true;

        return true;
    }

    // h??m gi???m k??ch th?????c h??nh ???nh
    public void downSize(String path, int size) throws IOException {

        try {

            File file = new File(path);
            File fileTemp = null;

            System.out.print("k??ch th?????c: " + size);

            // t???o 1 file h??nh ???nh t???m ????? ch??a h??nh ???nh sau khi gi???m k??ch th?????c
            fileTemp = new File(file.getParent() + "/TEMP_" + file.getName()); // create
            // filePath
            // temp
            if (file.exists()) {
                fileTemp.delete();
            }

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);

            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            // xoay h??nh ???nh n???u user ch???p ngang
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }

            FileOutputStream fOut = new FileOutputStream(fileTemp);

            Bitmap bitmapTemp = this.scaleDown(bitmap, (float) size, true);
            bitmapTemp.compress(Bitmap.CompressFormat.JPEG, (int) 100, fOut);

            file.delete();// xoa fil goc
            fileTemp.renameTo(new File(path));

            bitmap.recycle();
            bitmap = null;

            bitmapTemp.recycle();
            bitmapTemp = null;

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    // h??m xoay h??nh ???nh
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, true);

        return retVal;
    }

    // h??m gi???m k??ch th?????c h??nh ???nh
    public Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min((float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
            float factor = smallest / radius;
            sbmp = Bitmap.createScaledBitmap(bmp,
                    (int) (bmp.getWidth() / factor),
                    (int) (bmp.getHeight() / factor), false);
        } else {
            sbmp = bmp;
        }

        Bitmap output = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Paint stroke = new Paint();

        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);
        stroke.setAntiAlias(true);

        paint.setFilterBitmap(true);
        stroke.setFilterBitmap(true);

        paint.setDither(true);
        stroke.setDither(true);

        canvas.drawARGB(0, 0, 0, 0);

        // paint.setShadowLayer(6.0f, 3.0f, 3.0f, Color.BLACK);
        paint.setColor(Color.parseColor("#BAB399"));
        stroke.setColor(Color.parseColor("#FFFFFF"));

        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(20f);
        canvas.drawCircle(radius / 2 + 0.7f, radius / 2 + 0.7f,
                radius / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        canvas.drawCircle(radius / 2 + 0.7f, radius / 2 + 0.7f, radius / 2
                - stroke.getStrokeWidth() / 2 + 0.1f, stroke);

        return output;
    }

    private Context context;

    // h??m l???y th???i gian PDA theo ?????nh d???ng format truy???n v??o
    public static String getTimeOfPDA(String format) {
        // insert data v??o b???ng log
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        String dateTimeOfPDA = df.format(c.getTime()); // th???i gian m??y
        return dateTimeOfPDA;
    }


    public static String get_deviceID(Context context) {
        String deviceId;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }
        return deviceId;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getSerial() {
        try {
            String kq = "Unknow";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                kq = get_deviceID(global.getAppContext());

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (ActivityCompat.checkSelfPermission(null, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    kq = Build.getSerial();
                    return kq;
                }
            } else {
                kq = getManufacturerSerialNumber(); //s??? d???ng t??? SDK 25 tr??? xu???ng
            }
            return kq;
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Build.getSerial();
            }
        }
        return Build.getSerial();
    }

    public static String getManufacturerSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serial = (String) get.invoke(c, "ril.serialnumber", "unknown");
        } catch (Exception ignored) {
        }
        return serial;
    }

    // h??m t???o folder
    public static void createFolder(String folerPath) {
        File folder = new File(folerPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    // h??m ghi nh???n log ????? debug l???i
    public static void writeLogError(String content) {
        String folder = global.getAppContext().getString(
                R.string.PathFolderLog);
        FileUtils.writefile(
                        folder,
                        "Log.txt",
                        content + " --> "
                                + CmnFns.getTimeOfPDA(global.getFormatDate()),
                        true);
    }


    // h??m ki???m tra xem m??y c?? ??c ?????ng b??? d??? li???u b???ng 3G hay kh??ng
    public static int allowSynchronizeBy3G() {
        try {
            ConnectivityManager connManager = (ConnectivityManager) global
                    .getAppContext().getSystemService(
                            global.getAppContext().CONNECTIVITY_SERVICE);
            NetworkInfo mMobile = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (!mWifi.isConnected())
                if (!mMobile.isConnected())
                    return 102;
            if (mMobile.isConnected() && !global.getAllowSynchroinzeBy3G())
                return 102;
            return 1;
        } catch (Exception e) {
            // TODO: handle exception
            // CmnFns.writeLogError("Connection: Failed  " + e.getMessage() );
            return -1;
        }
    }

    public void showDialog(Context context, String text) {

        LayoutInflater factory = LayoutInflater.from(context);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 64);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.setView(layout_cus);

        Button btnClose = layout_cus.findViewById(R.id.btnHuy);
        TextView textView = layout_cus.findViewById(R.id.tvText);

        textView.setText(text);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();

    }

//    public String synchronizeGet_Status_Cancel(String cancel_CD) {
//        try {
//
//            int status = this.allowSynchronizeBy3G();
//            if (status == 102 || status == -1) {
//                return "-1";
//            }
//
//
//            Webservice Webservice = new Webservice();
//
//            String result = Webservice.Get_Status_Cancel_Good(cancel_CD);
//            if (result.equals("1")) {
//
//                return "1";
//            } else {
//                // ?????ng b??? kh??ng th??nh c??ng
//                return "-1";
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//
//            return "-1";
//        }
//
//    }

    public String Scan_Outbound_OD(String Warehouse_Position_CD, String OUTBOUND_DELIVERY_CD) {
        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return "-1";
            }


            Webservice Webservice = new Webservice();

            String result = Webservice.Scan_Outbound_OD(Warehouse_Position_CD, OUTBOUND_DELIVERY_CD);
            if (result.equals("1")) {

                return "1";
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return result;
            }
        } catch (Exception e) {
            // TODO: handle exception

            return "-1";
        }

    }

    public String Check_LPN_With_OD(String LPN_CODE, String OUTBOUND_DELIVERY_CD) {
        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return "-1";
            }


            Webservice Webservice = new Webservice();

            String result = Webservice.Check_LPN_With_OD(LPN_CODE, OUTBOUND_DELIVERY_CD);
            if (result.equals("1")) {

                return "1";
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return result;
            }
        } catch (Exception e) {
            // TODO: handle exception

            return "-1";
        }

    }

    public String synchronizeGet_Status_Stock_Out(String order_Cd) {
        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return "-1";
            }


            Webservice Webservice = new Webservice();

            String result = Webservice.Get_Status_Stock_Out(order_Cd);
            if (result.equals("1")) {

                return "1";
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return "-1";
            }
        } catch (Exception e) {
            // TODO: handle exception

            return "-1";
        }

    }

    public String Check_OD_Have_LPN(String OUTBOUND_DELIVERY_CD) {
        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return "-1";
            }


            Webservice Webservice = new Webservice();

            String result = Webservice.Check_OD_Have_LPN(OUTBOUND_DELIVERY_CD);
            if (result.equals("1")) {

                return "1";
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return result;
            }
        } catch (Exception e) {
            // TODO: handle exception

            return e.toString();
        }

    }


    public String Suggest_Product_For_OD_With_Position(String UserCode, String OUTBOUND_DELIVERY_CD, String Warehouse_Position_Cd) {
        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return "-1";
            }

            Webservice Webservice = new Webservice();

            String result = Webservice.Suggest_Product_For_OD_With_Position(UserCode , OUTBOUND_DELIVERY_CD , Warehouse_Position_Cd);

            try {
                JSONArray jsonarray = new JSONArray(result);

                for (int i = 0; i < jsonarray.length(); i++) {
                    // l???y m???t ?????i t?????ng json ?????

                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_cd = jsonobj.getString("PRODUCT_CD");
                    String pro_code = jsonobj.getString("PRODUCT_CODE");
                    String pro_name = jsonobj.getString("PRODUCT_NAME");
                    String expired_date = jsonobj.getString("EXPIRED_DATE");
                    String stockin_date = jsonobj.getString("STOCKIN_DATE");
                    String unit = jsonobj.getString("UNIT");
                    String qty = jsonobj.getString("QTY");
                    String batch_number = jsonobj.getString("BATCH_NUMBER");
                    String qty_od = jsonobj.getString("QTY_OD");
                    String position_code = jsonobj.getString("POSITION_CODE");
                    String warePosition = jsonobj.getString("WAREHOUSE_POSITION_CD");
                    String suggest = jsonobj.getString("SUGGEST");
                    String lpn_code = jsonobj.getString("LPN_CODE");
                    String lpn_to = jsonobj.getString("LPN_TO");
                    String position_to_cd = jsonobj.getString("POSITION_TO_CD");
                    // VT ?????n

                    Product_OD sp = new Product_OD();
                    sp.setPRODUCT_CD(pro_cd);
                    sp.setPRODUCT_CODE(pro_code);
                    sp.setPRODUCT_NAME(pro_name);
                    if (!expired_date.equals("null")) {
                        sp.setEXPIRED_DATE(expired_date);
                    }else{
                        sp.setEXPIRED_DATE("");
                    }
                    if (!stockin_date.equals("null")) {
                        sp.setSTOCKIN_DATE(stockin_date);
                    }else{
                        sp.setSTOCKIN_DATE("");
                    }
                    sp.setUNIT(unit);
                    sp.setQTY(qty);
                    sp.setBATCH_NUMBER(batch_number);
                    sp.setQTY_OD(qty_od);
                    sp.setPOSITION_CODE(position_code);
                    sp.setWAREHOUSE_POSITION_CD(warePosition);
                    sp.setSUGGESTION(suggest);
                    sp.setLPN_TO(lpn_to);
                    if (!lpn_code.equals("null")) {
                        sp.setLPN_CODE(lpn_code);
                    }else{
                        sp.setLPN_CODE("");
                    }
                    if (!position_to_cd.equals("null")) {
                        sp.setPOSITION_TO_CD(position_to_cd);
                    }else{
                        sp.setPOSITION_TO_CD("");
                    }
                    DatabaseHelper.getInstance().CreateOD(sp);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
                return "-1";
            }
            return "1";

        } catch (Exception e) {
            // TODO: handle exception

            return "-1";
        }

    }

    // h??m ????a d??? li???u kh??ch h??ng ??c User th??m m???i
    // b???ng t??nh n??ng Th??m m???i KH t??? HH
    public int synchronizeCustomerAddNew(Context context) {
        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return -1;
            }


            List<CCustomer> customers = DatabaseHelper.getInstance().getAllCustomerNewAllowSynchronize();
            // l???y c??c kh??ch h??ng ch??a ?????ng b???, ????
            // ?????ng b??? v??? r???i
            // th?? s??? ko c???n
            // ph???i ?????ng b??? n???a

            if (customers == null || customers.size() == 0)
                return 1;

            Webservice Webservice = new Webservice();
            //String json = convertToJson(customers);

            Gson gson = new GsonBuilder().create();

            String jsonData = gson.toJson(customers);
            String result = Webservice.synchronizeCustomerAddNew(jsonData);
            if (result.equals("1")) {
                // ???? ?????ng b??? th??nh c??ng update ????? l???n sau kh??ng ?????ng b??? l???i
                DatabaseHelper.getInstance().updateCustomerNewSync();
                return 1;
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return -1;
            }
        } catch (Exception e) {
            // TODO: handle exception

            return -1;
        }

    }

    public int synchronizeChangeCustomer(Context context) {
        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return -1;
            }


            List<UpdateCustomer> customers = DatabaseHelper.getInstance().getAllChangeCustomer();


            // l???y c??c kh??ch h??ng ch??a ?????ng b???, ????
            // ?????ng b??? v??? r???i
            // th?? s??? ko c???n
            // ph???i ?????ng b??? n???a

            if (customers == null || customers.size() == 0)
                return 1;

            Webservice Webservice = new Webservice();
            //String json = convertToJson(customers);

            Gson gson = new GsonBuilder().create();

            String jsonData = gson.toJson(customers);
            String result = Webservice.synchronizeCustomerInfo(jsonData);
            if (result.equals("1")) {
                // ???? ?????ng b??? th??nh c??ng update ????? l???n sau kh??ng ?????ng b??? l???i
                //DatabaseHelper.getInstance().updateChangeCustomer(customers,  );
                return 1;
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return -1;
            }
        } catch (Exception e) {
            // TODO: handle exception

            return -1;
        }

    }
    // h??m ?????ng b??? h??nh ???nh d??? li???u QA v??? Server
    public int synchronizePhoto_Check_Transport(Context context, String cd) {

        try {

            int status = this.allowSynchronizeBy3G();
            if (status != 1)
                return -1;

//            List<OrderPhoto> photos = DatabaseHelper.getInstance()
//                    .getAllPhotoForOrders(); // l???y d??? li???u c??c t???m h??nh ????? ?????ng b???
//            // v???

            List<OrderPhoto> photos = DatabaseHelper.getInstance().getAllPhotoForCheckTransport(cd);  // l???y d??? li???u c??c t???m h??nh ????? ?????ng b???
            // v???
            if (photos == null || photos.size() == 0)
                return 1;

            int countSync = 0;

            Webservice webService = new Webservice();

            int photoSize = photos.size();

            for (int i = 0; i < photoSize; i++) {

                // check valid imagePath
                // ki???m tra image kh??ng t???n t???i tr??n HH th?? s??? kh??ng ?????ng b??? v???
                // Server
                // ?????ng th???i x??a d??? li???u h??nh ???nh n??y trong DB lu??n
                try {
                    File checkFile = new File(photos.get(i).getPhoto_Path());
                    if (!checkFile.exists()) {
                        countSync++;
                        DatabaseHelper.getInstance().deletePhotoForOrders(
                                photos.get(i).getPhoto_Name());
                        try {
                            checkFile.delete(); // clear file
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        continue;
                    }

                    Gson gson = new GsonBuilder().create();

                    // h??m t???o c???u tr??c JSON ????? ?????ng b??? v??? Server
                    String jsonData = gson.toJson(photos.get(i));
                    if (!jsonData.contains("["))
                        jsonData = "[" + jsonData;

                    if (!jsonData.contains("]"))
                        jsonData = jsonData + "]";
                    // 5108 c?? th??? x???y ra l???i trong qu?? tr??nh n??y
                    byte[] image = CmnFns.decodeImageToString(photos.get(i)
                            .getPhoto_Path()); // chuy???n ?????i h??nh ???nh th??nh m???ng
                    // byte

                    String result = webService
                            .synchronizePhotoForOrder(global.getAdminCode(), "5", jsonData, image); // g???i d??? li???u
                    // v??? Server
                    if (result.equals("-1")) {

                    } else {
                        // n???u th??nh c??ng th?? x??a d??? li???u trong DB v?? file tr??n
                        // HH
                        countSync++;
                        DatabaseHelper.getInstance().deletePhotoForOrders(
                                photos.get(i).getPhoto_Name());
                        File file = new File(photos.get(i).getPhoto_Path());
                        file.delete(); // clear file
                    }
                    // 5098 c?? th??? x???y ra l???i trong qu?? tr??nh n??y


                } catch (Exception e) {
                    // TODO: handle exception

                    Log.d("l???i", e.getMessage());
                    // B??? l???i trong qu?? tr??nh chuy???n 2 ph???n c?? th???  5108 + 5098
                    // 2 ph???n n??y c?? th??? x???y ra l???i trong qu?? tr??nh ch???y
                    // Comment ph???n n??y ????? tr??nh x???y ra l???i khi kh??ng th??? x???y ra l???i
//                    DatabaseHelper.getInstance().deletePhoto(
//                            photos.get(i).getPhotoCD());
//                    countSync++;
//                    CmnFns.writeLogError("Photo Upload: "
//                            + e.getMessage());
                }


            }

            if (countSync == photoSize)
                return 1;

            return -1;
        } catch (Exception e) {
            // TODO: handle exception
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return 1;

        }

    }
    // h??m ?????ng b??? h??nh ???nh d??? li???u QA v??? Server
    public int synchronizePhoto_Container(Context context, String cd) {

        try {

            int status = this.allowSynchronizeBy3G();
            if (status != 1)
                return -1;

//            List<OrderPhoto> photos = DatabaseHelper.getInstance()
//                    .getAllPhotoForOrders(); // l???y d??? li???u c??c t???m h??nh ????? ?????ng b???
//            // v???

            List<OrderPhoto> photos = DatabaseHelper.getInstance().getAllPhotoForContainers(cd);  // l???y d??? li???u c??c t???m h??nh ????? ?????ng b???
            // v???
            if (photos == null || photos.size() == 0)
                return 1;

            int countSync = 0;

            Webservice webService = new Webservice();

            int photoSize = photos.size();

            for (int i = 0; i < photoSize; i++) {

                // check valid imagePath
                // ki???m tra image kh??ng t???n t???i tr??n HH th?? s??? kh??ng ?????ng b??? v???
                // Server
                // ?????ng th???i x??a d??? li???u h??nh ???nh n??y trong DB lu??n
                try {
                    File checkFile = new File(photos.get(i).getPhoto_Path());
                    if (!checkFile.exists()) {
                        countSync++;
                        DatabaseHelper.getInstance().deletePhotoForOrders(
                                photos.get(i).getPhoto_Name());
                        try {
                            checkFile.delete(); // clear file
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        continue;
                    }

                    Gson gson = new GsonBuilder().create();

                    // h??m t???o c???u tr??c JSON ????? ?????ng b??? v??? Server
                    String jsonData = gson.toJson(photos.get(i));
                    if (!jsonData.contains("["))
                        jsonData = "[" + jsonData;

                    if (!jsonData.contains("]"))
                        jsonData = jsonData + "]";
                    // 5108 c?? th??? x???y ra l???i trong qu?? tr??nh n??y
                    byte[] image = CmnFns.decodeImageToString(photos.get(i)
                            .getPhoto_Path()); // chuy???n ?????i h??nh ???nh th??nh m???ng
                    // byte

                    String result = webService
                            .synchronizePhotoForOrder(global.getSaleCode(), "4", jsonData, image); // g???i d??? li???u
                    // v??? Server
                    if (result.equals("-1")) {

                    } else {
                        // n???u th??nh c??ng th?? x??a d??? li???u trong DB v?? file tr??n
                        // HH
                        countSync++;
                        DatabaseHelper.getInstance().deletePhotoForOrders(
                                photos.get(i).getPhoto_Name());
                        File file = new File(photos.get(i).getPhoto_Path());
                        file.delete(); // clear file
                    }
                    // 5098 c?? th??? x???y ra l???i trong qu?? tr??nh n??y


                } catch (Exception e) {
                    // TODO: handle exception

                    Log.d("l???i", e.getMessage());
                    // B??? l???i trong qu?? tr??nh chuy???n 2 ph???n c?? th???  5108 + 5098
                    // 2 ph???n n??y c?? th??? x???y ra l???i trong qu?? tr??nh ch???y
                    // Comment ph???n n??y ????? tr??nh x???y ra l???i khi kh??ng th??? x???y ra l???i
//                    DatabaseHelper.getInstance().deletePhoto(
//                            photos.get(i).getPhotoCD());
//                    countSync++;
//                    CmnFns.writeLogError("Photo Upload: "
//                            + e.getMessage());
                }


            }

            if (countSync == photoSize)
                return 1;

            return -1;
        } catch (Exception e) {
            // TODO: handle exception
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return 1;

        }

    }

    // h??m ?????ng b??? h??nh ???nh d??? li???u QA v??? Server
    public int synchronizePhoto_QA(Context context, String cd) {

        try {

            int status = this.allowSynchronizeBy3G();
            if (status != 1)
                return -1;

//            List<OrderPhoto> photos = DatabaseHelper.getInstance()
//                    .getAllPhotoForOrders(); // l???y d??? li???u c??c t???m h??nh ????? ?????ng b???
//            // v???

            List<OrderPhoto> photos = DatabaseHelper.getInstance().getAllPhoto_QA(cd);  // l???y d??? li???u c??c t???m h??nh ????? ?????ng b???
            // v???
            if (photos == null || photos.size() == 0)
                return 1;

            int countSync = 0;

            Webservice webService = new Webservice();

            int photoSize = photos.size();

            for (int i = 0; i < photoSize; i++) {

                // check valid imagePath
                // ki???m tra image kh??ng t???n t???i tr??n HH th?? s??? kh??ng ?????ng b??? v???
                // Server
                // ?????ng th???i x??a d??? li???u h??nh ???nh n??y trong DB lu??n
                try {
                    File checkFile = new File(photos.get(i).getPhoto_Path());
                    if (!checkFile.exists()) {
                        countSync++;
                        DatabaseHelper.getInstance().deletePhotoForOrders(
                                photos.get(i).getPhoto_Name());
                        try {
                            checkFile.delete(); // clear file
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        continue;
                    }

                    Gson gson = new GsonBuilder().create();

                    // h??m t???o c???u tr??c JSON ????? ?????ng b??? v??? Server
                    String jsonData = gson.toJson(photos.get(i));
                    if (!jsonData.contains("["))
                        jsonData = "[" + jsonData;

                    if (!jsonData.contains("]"))
                        jsonData = jsonData + "]";
                    // 5108 c?? th??? x???y ra l???i trong qu?? tr??nh n??y
                    byte[] image = CmnFns.decodeImageToString(photos.get(i)
                            .getPhoto_Path()); // chuy???n ?????i h??nh ???nh th??nh m???ng
                    // byte

                    String result = webService
                            .synchronizePhotoForOrder(global.getAdminCode(), "3", jsonData, image); // g???i d??? li???u
                    // v??? Server
                    if (result.equals("-1")) {

                    } else {
                        // n???u th??nh c??ng th?? x??a d??? li???u trong DB v?? file tr??n
                        // HH
                        countSync++;
                        DatabaseHelper.getInstance().deletePhotoForOrders(
                                photos.get(i).getPhoto_Name());
                        File file = new File(photos.get(i).getPhoto_Path());
                        file.delete(); // clear file
                    }
                    // 5098 c?? th??? x???y ra l???i trong qu?? tr??nh n??y


                } catch (Exception e) {
                    // TODO: handle exception

                    Log.d("l???i", e.getMessage());
                    // B??? l???i trong qu?? tr??nh chuy???n 2 ph???n c?? th???  5108 + 5098
                    // 2 ph???n n??y c?? th??? x???y ra l???i trong qu?? tr??nh ch???y
                    // Comment ph???n n??y ????? tr??nh x???y ra l???i khi kh??ng th??? x???y ra l???i
//                    DatabaseHelper.getInstance().deletePhoto(
//                            photos.get(i).getPhotoCD());
//                    countSync++;
//                    CmnFns.writeLogError("Photo Upload: "
//                            + e.getMessage());
                }


            }

            if (countSync == photoSize)
                return 1;

            return -1;
        } catch (Exception e) {
            // TODO: handle exception
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return 1;

        }

    }

    // h??m ?????ng b??? h??nh ???nh d??? li???u v??? Server
    public int synchronizePhotoForOrders(Context context) {

        try {

            int status = this.allowSynchronizeBy3G();
            if (status != 1)
                return -1;

//            List<OrderPhoto> photos = DatabaseHelper.getInstance()
//                    .getAllPhotoForOrders(); // l???y d??? li???u c??c t???m h??nh ????? ?????ng b???
//            // v???

            List<OrderPhoto> photos = DatabaseHelper.getInstance().getAllPhotoForOrders(); // l???y d??? li???u c??c t???m h??nh ????? ?????ng b???
            // v???
            if (photos == null || photos.size() == 0)
                return 1;

            int countSync = 0;

            Webservice webService = new Webservice();

            int photoSize = photos.size();

            for (int i = 0; i < photoSize; i++) {

                // check valid imagePath
                // ki???m tra image kh??ng t???n t???i tr??n HH th?? s??? kh??ng ?????ng b??? v???
                // Server
                // ?????ng th???i x??a d??? li???u h??nh ???nh n??y trong DB lu??n
                try {
                    File checkFile = new File(photos.get(i).getPhoto_Path());
                    if (!checkFile.exists()) {
                        countSync++;
                        DatabaseHelper.getInstance().deletePhotoForOrders(
                                photos.get(i).getPhoto_Name());
                        try {
                            checkFile.delete(); // clear file
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        continue;
                    }

                    Gson gson = new GsonBuilder().create();

                    // h??m t???o c???u tr??c JSON ????? ?????ng b??? v??? Server
                    String jsonData = gson.toJson(photos.get(i));
                    if (!jsonData.contains("["))
                        jsonData = "[" + jsonData;

                    if (!jsonData.contains("]"))
                        jsonData = jsonData + "]";
                    // 5108 c?? th??? x???y ra l???i trong qu?? tr??nh n??y
                    byte[] image = CmnFns.decodeImageToString(photos.get(i)
                            .getPhoto_Path()); // chuy???n ?????i h??nh ???nh th??nh m???ng
                    // byte

                    String result = webService
                            .synchronizePhotoForOrder(global.getSaleCode(), "2", jsonData, image); // g???i d??? li???u
                    // v??? Server
                    if (result.equals("-1")) {

                    } else {
                        // n???u th??nh c??ng th?? x??a d??? li???u trong DB v?? file tr??n
                        // HH
                        countSync++;
                        DatabaseHelper.getInstance().deletePhotoForOrders(
                                photos.get(i).getPhoto_Name());
                        File file = new File(photos.get(i).getPhoto_Path());
                        file.delete(); // clear file
                    }
                    // 5098 c?? th??? x???y ra l???i trong qu?? tr??nh n??y


                } catch (Exception e) {
                    // TODO: handle exception

                    Log.d("l???i", e.getMessage());
                    // B??? l???i trong qu?? tr??nh chuy???n 2 ph???n c?? th???  5108 + 5098
                    // 2 ph???n n??y c?? th??? x???y ra l???i trong qu?? tr??nh ch???y
                    // Comment ph???n n??y ????? tr??nh x???y ra l???i khi kh??ng th??? x???y ra l???i
//                    DatabaseHelper.getInstance().deletePhoto(
//                            photos.get(i).getPhotoCD());
//                    countSync++;
//                    CmnFns.writeLogError("Photo Upload: "
//                            + e.getMessage());
                }


            }

            if (countSync == photoSize)
                return 1;

            return -1;
        } catch (Exception e) {
            // TODO: handle exception
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return 1;

        }

    }

    // h??m ?????ng b??? h??nh ???nh d??? li???u v??? Server
    public int synchronizePhotos(Context context) {

        try {

            int status = this.allowSynchronizeBy3G();
            if (status != 1)
                return -1;

            List<CPhoto> photos = DatabaseHelper.getInstance()
                    .getAllTakesPhotos(); // l???y d??? li???u c??c t???m h??nh ????? ?????ng b???
            // v???

            if (photos == null || photos.size() == 0)
                return 1;

            int countSync = 0;

            Webservice webService = new Webservice();

            int photoSize = photos.size();

            for (int i = 0; i < photoSize; i++) {

                // check valid imagePath
                // ki???m tra image kh??ng t???n t???i tr??n HH th?? s??? kh??ng ?????ng b??? v???
                // Server
                // ?????ng th???i x??a d??? li???u h??nh ???nh n??y trong DB lu??n
                try {
                    File checkFile = new File(photos.get(i).getPhotoPath());
                    if (!checkFile.exists()) {
                        countSync++;
                        DatabaseHelper.getInstance().deletePhoto(
                                photos.get(i).getPhotoCD());
                        try {
                            checkFile.delete(); // clear file
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        continue;
                    }
                    // 5098 c?? th??? x???y ra l???i trong qu?? tr??nh n??y
                    Gson gson = new GsonBuilder().create();

                    // h??m t???o c???u tr??c JSON ????? ?????ng b??? v??? Server
                    String jsonData = gson.toJson(photos.get(i));
                    if (!jsonData.contains("["))
                        jsonData = "[" + jsonData;

                    if (!jsonData.contains("]"))
                        jsonData = jsonData + "]";
                    // 5108 c?? th??? x???y ra l???i trong qu?? tr??nh n??y
                    byte[] image = CmnFns.decodeImageToString(photos.get(i)
                            .getPhotoPath()); // chuy???n ?????i h??nh ???nh th??nh m???ng
                    // byte

                    String result = webService
                            .synchronizePhoto(jsonData, image); // g???i d??? li???u
                    // v??? Server
                    if (result.equals("-1")) {

                    } else {
                        // n???u th??nh c??ng th?? x??a d??? li???u trong DB v?? file tr??n
                        // HH
                        countSync++;
                        DatabaseHelper.getInstance().deletePhoto(
                                photos.get(i).getPhotoCD());
                        File file = new File(photos.get(i).getPhotoPath());
                        file.delete(); // clear file
                    }

                } catch (Exception e) {
                    // TODO: handle exception

                    // B??? l???i trong qu?? tr??nh chuy???n 2 ph???n c?? th???  5108 + 5098
                    // 2 ph???n n??y c?? th??? x???y ra l???i trong qu?? tr??nh ch???y
                    // Comment ph???n n??y ????? tr??nh x???y ra l???i khi kh??ng th??? x???y ra l???i
//                    DatabaseHelper.getInstance().deletePhoto(
//                            photos.get(i).getPhotoCD());
//                    countSync++;
//                    CmnFns.writeLogError("Photo Upload: "
//                            + e.getMessage());
                }
            }

            if (countSync == photoSize)
                return 1;

            return -1;
        } catch (Exception e) {
            // TODO: handle exception
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return 1;

        }

    }

    public int synchronizeChangeCustPhotos(Context context) {

        try {

            int status = this.allowSynchronizeBy3G();
            if (status != 1)
                return -1;

            List<CPhoto> photos = DatabaseHelper.getInstance()
                    .getAllChangeCustTakesPhotos(); // l???y d??? li???u c??c t???m h??nh ????? ?????ng b???
            // v???

            if (photos == null || photos.size() == 0)
                return 1;

            int countSync = 0;

            Webservice webService = new Webservice();

            int photoSize = photos.size();

            for (int i = 0; i < photoSize; i++) {

                // check valid imagePath
                // ki???m tra image kh??ng t???n t???i tr??n HH th?? s??? kh??ng ?????ng b??? v???
                // Server
                // ?????ng th???i x??a d??? li???u h??nh ???nh n??y trong DB lu??n
                try {
                    CPhoto cPhoto = photos.get(i);
                    File checkFile = new File(cPhoto.getPhotoPath());
                    if (!checkFile.exists()) {
                        countSync++;
                        DatabaseHelper.getInstance().deleteChangeCustPhoto(
                                cPhoto.getPhotoName());
                        try {
                            checkFile.delete(); // clear file
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        continue;
                    }
                    // 5098 c?? th??? x???y ra l???i trong qu?? tr??nh n??y
                    Gson gson = new GsonBuilder().create();

                    // h??m t???o c???u tr??c JSON ????? ?????ng b??? v??? Server
                    String jsonData = gson.toJson(photos.get(i));
                    if (!jsonData.contains("["))
                        jsonData = "[" + jsonData;

                    if (!jsonData.contains("]"))
                        jsonData = jsonData + "]";
                    // 5108 c?? th??? x???y ra l???i trong qu?? tr??nh n??y
                    byte[] image = CmnFns.decodeImageToString(photos.get(i)
                            .getPhotoPath()); // chuy???n ?????i h??nh ???nh th??nh m???ng
                    // byte

                    String result = webService
                            .synchronizePhoto(jsonData, image); // g???i d??? li???u
                    // v??? Server
                    if (result.equals("-1")) {

                    } else {
                        // n???u th??nh c??ng th?? x??a d??? li???u trong DB v?? file tr??n
                        // HH
                        countSync++;
                        DatabaseHelper.getInstance().deleteChangeCustPhoto(
                                photos.get(i).getPhotoName());
                        File file = new File(photos.get(i).getPhotoPath());
                        file.delete(); // clear file
                    }

                } catch (Exception e) {
                    // TODO: handle exception

                    // B??? l???i trong qu?? tr??nh chuy???n 2 ph???n c?? th???  5108 + 5098
                    // 2 ph???n n??y c?? th??? x???y ra l???i trong qu?? tr??nh ch???y
                    // Comment ph???n n??y ????? tr??nh x???y ra l???i khi kh??ng th??? x???y ra l???i
//                    DatabaseHelper.getInstance().deletePhoto(
//                            photos.get(i).getPhotoCD());
//                    countSync++;
//                    CmnFns.writeLogError("Photo Upload: "
//                            + e.getMessage());
                }
            }
            DatabaseHelper.getInstance().deleteListChangeCustPhoto();
            if (countSync == photoSize)
                return 1;

            return -1;
        } catch (Exception e) {
            // TODO: handle exception
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return 1;

        }

    }

    // h??m m?? h??a image sang byte
    public static byte[] decodeImageToString(String imageFullPath) {
        byte[] bytearray = null;

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(imageFullPath);
        } catch (FileNotFoundException e) {
        }

        try {
            bytearray = streamToBytes(inputStream);
            try {
                inputStream.close();
            } catch (IOException e) {
            }

        } finally {

            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }

        return bytearray;

    }

    private static byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
            os.close();
        } catch (java.io.IOException e) {
        }
        return os.toByteArray();
    }

    // h??m ki???m tra xem m??y c?? k???t n???i m???ng hay kh??ng
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) global
                .getAppContext().getSystemService(
                        global.getAppContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public int synchronizeGetCustomers(Context context, String customer_code, String customer_name) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        DatabaseHelper.getInstance().deleteListCust();

        Webservice webService = new Webservice();
        String result = webService.synchronizeGETCustomerInfo(customer_code, customer_name);
        if (result.equals("-1"))
            return -1;

        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);


            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String cust_CD = jsonobj.getString("_CustomerCD");
                if (cust_CD.equals(customer_code)) {
                    String cust_code = jsonobj.getString("_CustomerCode");
                    String cust_name = jsonobj.getString("_CustomerName");
                    String cust_address = jsonobj.getString("_CustomerAddress");
                    String cust_phone = jsonobj.getString("_CustomerPhone");
                    String cust_geocode = jsonobj.getString("_Longlat");


                    UpdateCustomer customer = new UpdateCustomer();
                    global.setCustomercd(cust_CD);
                    customer.setCustomerCode(cust_code);
                    customer.setCustomerName(cust_name);
                    customer.setCustomerAddress(cust_address);
                    customer.setCustomerPhone(cust_phone);
                    customer.setCustomerCd(cust_CD);
                    customer.setCustomerGeocode(cust_geocode);

                    DatabaseHelper.getInstance().CreateCustomerChange(customer);

                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    // ?????ng b??? l???y th??ng tin kh??ch h??ng cho b???n ?????
    public ArrayList<UpdateCustomer> synchronizeGetCustomersForMap(Context context, String customer_code, String customer_name) {

        ArrayList<UpdateCustomer> arrayListCustomer = new ArrayList<>();
        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return arrayListCustomer;

        DatabaseHelper.getInstance().deleteListCust();

        Webservice webService = new Webservice();
        String result = webService.synchronizeGETCustomerInfo(customer_code, customer_name);
        if (result.equals("-1"))
            return arrayListCustomer;

        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return arrayListCustomer;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String cust_code = jsonobj
                        .getString("_CustomerCode");
                String cust_name = jsonobj
                        .getString("_CustomerName");
                String cust_address = jsonobj.getString("_CustomerAddress");
                String cust_phone = jsonobj.getString("_CustomerPhone");
                String cust_CD = jsonobj.getString("_CustomerCD");
                String cust_geocode = jsonobj.getString("_Longlat");

                UpdateCustomer customer = new UpdateCustomer();
                global.setCustomercd(cust_CD);
                customer.setCustomerCode(cust_code);
                customer.setCustomerName(cust_name);
                customer.setCustomerAddress(cust_address);
                customer.setCustomerPhone(cust_phone);
                customer.setCustomerCd(cust_CD);
                customer.setCustomerGeocode(cust_geocode);

                arrayListCustomer.add(customer);

            }

            return arrayListCustomer;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return arrayListCustomer;
        }

    }


    // h??m l???y th??ng tin k???t n???i t???i webservice t??? fsid.5stars.com.vn:9139
    public static int setAuth(String type) {
        try {
            WebserviceAuth webServiceauth = new WebserviceAuth();
            String result = webServiceauth.getInfo();
            Log.d("Links", result);
            String salesCode = "";
            if(type.equals("1")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    salesCode = CmnFns.readDataAdminNew();
                }else{
                    salesCode = CmnFns.readDataAdmin();
                }
            }else if(type.equals("2")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    salesCode = CmnFns.readDataShipperNew();
                }else{
                    salesCode = CmnFns.readDataShipper();
                }
            }

            if(salesCode.equals("S0001") || salesCode.equals("de01") ){
                result = "http://idv.grv.fieldvision.com.vn:54574/Webservice/Synchronize.asmx???5stars.com.vn-Nouser???#*&!@(*!@#&@#&@!6^@!@##@6382734";
            }else{
                result = webServiceauth.getInfo();
                Log.d("Links", result);
            }
            if (result.equals("-1") || result.equals("100")) {
                global.setUrlWebserviceToSynchronize("");
                global.setUserNameAuthWebsevice("");
                global.setPasswordNameAuthWebsevice("");
                return Integer.parseInt(result);
            } else {
                // G??n c??c URL(k???t n???i t???i server n??o), Username, Pass t??? FSID do getInfo l???y ???????c
                String[] arr = result.split(global.SPLIT_KEY);
                SharedPreferences sharedPref = getAppContext().getSharedPreferences("setURL", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("urlConnect", arr[0]);
//            editor.putString("currentTimeIn", a);
                editor.commit();

                global.setUrlWebserviceToSynchronize(arr[0]);
                global.setUserNameAuthWebsevice(arr[1]);
                global.setPasswordNameAuthWebsevice(arr[2]);
                if (isCheckAdmin()) {
                    getHH_Param(readDataAdmin());
                    getHH_Param_Layout(readDataAdmin());
                }
                if (isCheckSale()) {
                    getHH_Param(readDataShipper());
                    getHH_Param_Layout(readDataShipper());
                }
//            String linkWebView = "";
//            String[] linkLoadWebView = arr[0].split("/");
//            for (int i = 0; i < linkLoadWebView.length; i++) {
//                linkWebView += linkLoadWebView[i] + "/";
//                if (i == 2) {
//                    global.setURLDELIVERYCUSTOMERlIST(linkWebView + "TMS/DeliveryCustomerList.aspx?deliverer=");
//                    Log.d("aaaaa", linkWebView + "TMS/DeliveryCustomerList.aspx?deliverer=");
//                    break;
//                }
//            }
            }

            return 1;
        } catch (Exception e) {
            // TODO: handle exception
            Log.d("setAuth ", "" + e.getMessage());
            return -1;
        }
    }


    // h??m l???y Imei c???a m??y
    public static String getImei(Context context) {
        String imei;
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imei = get_deviceID(global.getAppContext());
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                imei = telephonyManager.getDeviceId();
            }
            return imei.trim();


//            if (BuildConfig.DEBUG){
//                Log.e("CmnFns", "Print IMEI Number : "+"357103076747188");
//                return "357103076747188";
//            }

        } catch (Exception e) {
            // TODO: handle exception
            try {
                WifiManager wifiManager = (WifiManager) global.getAppContext().getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);
                WifiInfo wInfo = wifiManager.getConnectionInfo();
                String macAddress = wInfo.getMacAddress();
                return macAddress;
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
        return "";
    }

    // h??m l???y version name t??? file AndroidManifest.xml
    public static String getVersionName() {
        try {
            PackageInfo pInfo = global
                    .getAppContext()
                    .getPackageManager()
                    .getPackageInfo(global.getAppContext().getPackageName(),
                            PackageManager.GET_META_DATA);
            return pInfo.versionName;
        } catch (Exception e) {
            return "1";
        }
    }


    // h??m m?? h??a d??? li???u v???i m???t kh???u ch??? ?????nh
    public static String encrypt(String text, String password) {
        try {

            String secu = P5sSecurity.encrypt(text, password);
            P5sSecurity.encrypt(text, password);
            P5sSecurity.encrypt(text, password);
            P5sSecurity.encrypt(text, password);
            P5sSecurity.encrypt(text, password);
            return P5sSecurity.encrypt(text, password);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("encrypt  " + e.getMessage());
//            CmnFns.writeLogError("getAuth " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("encrypt  " + e.getMessage());
//            CmnFns.writeLogError("getAuth " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
//            CmnFns.writeLogError("encrypt  " + e.getMessage());
//            // TODO Auto-generated catch block
//            CmnFns.writeLogError("getAuth " + e.getMessage());
        } catch (NoSuchPaddingException e) {
//            CmnFns.writeLogError("encrypt  " + e.getMessage());
//            // TODO Auto-generated catch block
//            CmnFns.writeLogError("getAuth " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
//            CmnFns.writeLogError("encrypt  " + e.getMessage());
//            // TODO Auto-generated catch block
//            CmnFns.writeLogError("getAuth " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
//            CmnFns.writeLogError("encrypt  " + e.getMessage());
//            // TODO Auto-generated catch block
//            CmnFns.writeLogError("getAuth " + e.getMessage());
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            //CmnFns.writeLogError("encrypt  " + e.getMessage());
        }
        return "-1";
    }

    public int synchronizeGETProductByZoneChuyenMa(String barcodeData, String sale_codes, String type, int IsLPN, String cd, String expdate,
                                                   String batch, String stockindate, String unit_cm, String product_code,
                                                   String product_name, String product_cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();

        String result = webService.GetSPChuyenMa(barcodeData, sale_codes, type, IsLPN, cd);

        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }
        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
//                if(i==1){
//                    return 1;
//                }else {

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String pro_code = jsonobj.getString("_PRODUCT_CODE");
                String pro_cd = jsonobj.getString("_PRODUCT_CD");
                String pro_name = jsonobj.getString("_PRODUCT_NAME");
                String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                String quanity_2 = jsonobj.getString("_QTY_SET_AVAILABLE_2");
                String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                String ea_unit = jsonobj.getString("_UNIT");
                String ea_unit_2 = jsonobj.getString("_UNIT_2");
                // VT ?????n
                String position_code = jsonobj.getString("_POSITION_CODE");
                String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                // M?? t??? VT ?????n
                String description = jsonobj.getString("_POSITION_DESCRIPTION");
                // VT ?????n
                String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                String manufacturing = jsonobj.getString("_MANUFACTURING_DATE");
                String batch_number = jsonobj.getString("_BATCH_NUMBER");
                String item_basic = jsonobj.getString("_ITEM_BASIC");
                if ((!batch_number.equals(batch))) {

                } else {
                    if (unit_cm.equals(ea_unit)) {
                        Product_SP sp = new Product_SP();

                        if ((product_cd != null) && (!product_cd.equals(""))) {
                            sp.setPRODUCT_CD(product_cd);
                        } else {
                            sp.setPRODUCT_CD(pro_cd);
                        }
                        if ((product_code != null) && (!product_code.equals(""))) {
                            sp.setPRODUCT_CODE(product_code);
                        } else {
                            sp.setPRODUCT_CODE(pro_code);
                        }

                        if ((product_name != null) && (!product_name.equals(""))) {
                            sp.setPRODUCT_NAME(product_name);
                        } else {
                            sp.setPRODUCT_NAME(pro_name);
                        }
                        sp.setQTY_SET_AVAILABLE(quanity);
                        sp.setQTY_SET_AVAILABLE_2(quanity_2);
                        sp.setQTY_EA_AVAILABLE(quanity_ea);
                        sp.setEXPIRED_DATE(expdate);
                        sp.setUNIT(unit_cm);
                        sp.setUNIT_2(ea_unit_2);
                        sp.setPOSITION_CODE(position_code);
                        sp.setSTOCKIN_DATE(stockindate);
                        sp.setPOSITION_DESCRIPTION(description);
                        sp.setWAREHOUSE_POSITION_CD(warePosition);
                        sp.setMANUFACTURING_DATE(manufacturing);
                        sp.setBATCH_NUMBER(batch);
                        sp.setITEM_BASIC(item_basic);

                        DatabaseHelper.getInstance().CreateSP(sp);
                        int statusGetCust2 = new CmnFns().getChuyenMaMateril(barcodeData, "WTP");
                    }
                }
//                }

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int getChuyenMaMateril(String barcodeData, String type) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();

        String result = webService.getChuyenMaMateril(barcodeData, type);

        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }
        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);

                String pro_cd = jsonobj.getString("PRODUCT_CD");
                String pro_code = jsonobj.getString("PRODUCT_CODE");
                String pro_name = jsonobj.getString("PRODUCT_NAME");
                String barcode = jsonobj.getString("BARCODE");
                String item_basic = jsonobj.getString("ITEM_BASIC");


                Product_Material material = new Product_Material();

//                    if((expDate.equals(exxpiredDate)) && (stockDate.equals(stockDate)) && (unit.equals(ea_unit))){
                material.setPRODUCT_CD(pro_cd);
                material.setPRODUCT_CODE(pro_code);
                material.setPRODUCT_NAME(pro_name);
                material.setBARCODE(barcode);
                material.setITEM_BABIC(item_basic);


                DatabaseHelper.getInstance().CreateMaterial(material);


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int getQAFromServer(String barcodeData, String sale_codes, String type, int IsLPN, String cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = "";
        result = webService.GetSPChuyenMa(barcodeData, sale_codes, type, IsLPN, cd);


        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }
        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String pro_exp = jsonobj.getString("_EXPIRY_DATE");
                String pro_stockin = jsonobj.getString("_STOCKIN_DATE");
                String pro_code = jsonobj.getString("_PRODUCT_CODE");
                String pro_cd = jsonobj.getString("_PRODUCT_CD");
                String batch = "";

                batch = jsonobj.getString("_BATCH_NUMBER");


                Exp_Date_Tam exp_date_tam = new Exp_Date_Tam();
                if (pro_exp.equals("")) {
                    exp_date_tam.setEXPIRED_DATE_TAM("---");
                } else {
                    exp_date_tam.setEXPIRED_DATE_TAM(pro_exp);
                }

                if (pro_stockin.equals("")) {
                    exp_date_tam.setSTOCKIN_DATE_TAM("---");
                } else {
                    exp_date_tam.setSTOCKIN_DATE_TAM(pro_stockin);
                }

                exp_date_tam.setBATCH_NUMBER_TAM(batch);
                exp_date_tam.setPRODUCT_CODE_TAM(pro_code);
                exp_date_tam.setPRODUCT_CD_TAM(pro_cd);


                DatabaseHelper.getInstance().CreateExp_date(exp_date_tam);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int getDataFromSeverWithBatch(String barcodeData, String sale_codes, String type, int IsLPN, String cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = "";

        result = webService.GetProductByZone(barcodeData, sale_codes, type, IsLPN, cd);


        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }
        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String pro_exp = jsonobj.getString("_EXPIRY_DATE");
                String pro_stockin = jsonobj.getString("_STOCKIN_DATE");
                String batch = jsonobj.getString("_BATCH_NUMBER");
                String product_code = jsonobj.getString("_PRODUCT_CODE");


                Exp_Date_Tam exp_date_tam = new Exp_Date_Tam();
                if (pro_stockin.equals("")) {
                    exp_date_tam.setEXPIRED_DATE_TAM(pro_exp + " - " + "---");
                } else {
                    exp_date_tam.setEXPIRED_DATE_TAM(pro_exp + " - " + pro_stockin);
                }
                if (batch.equals("")) {
                    exp_date_tam.setBATCH_NUMBER_TAM("---");
                } else {
                    exp_date_tam.setBATCH_NUMBER_TAM(batch);
                }
                exp_date_tam.setPRODUCT_CODE_TAM(product_code);

                DatabaseHelper.getInstance().CreateExp_date(exp_date_tam);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int GetProductByZone_With_Position(String barcodeData, String sale_codes, String type, int IsLPN, String cd , String Warehouse_Position_CD) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = "";

        result = webService.GetProductByZone_With_Position(barcodeData, sale_codes, type, IsLPN, cd, Warehouse_Position_CD);


        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }
        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String pro_exp = jsonobj.getString("_EXPIRY_DATE");
                String pro_stockin = jsonobj.getString("_STOCKIN_DATE");
                String batch = jsonobj.getString("_BATCH_NUMBER");
                String product_code = jsonobj.getString("_PRODUCT_CODE");
                String product_cd = jsonobj.getString("_PRODUCT_CD");
                String position_code = jsonobj.getString("_POSITION_CODE");
                String warehouse_position_cd = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                String lpn_code = jsonobj.getString("_LPN_CODE");


                Exp_Date_Tam exp_date_tam = new Exp_Date_Tam();
                if (pro_exp.equals("")) {
                    exp_date_tam.setEXPIRED_DATE_TAM("---");
                } else {
                    exp_date_tam.setEXPIRED_DATE_TAM(pro_exp);
                }

                if (pro_stockin.equals("")) {
                    exp_date_tam.setSTOCKIN_DATE_TAM("---");
                } else {
                    exp_date_tam.setSTOCKIN_DATE_TAM(pro_stockin);
                }

                if (batch.equals("")) {
                    exp_date_tam.setBATCH_NUMBER_TAM("---");
                } else {
                    exp_date_tam.setBATCH_NUMBER_TAM(batch);
                }
                exp_date_tam.setPRODUCT_CODE_TAM(product_code);
                exp_date_tam.setPRODUCT_CD_TAM(product_cd);
                if(type.equals("WST")){
                    exp_date_tam.setPOSITION_CODE_TAM(position_code);
                    exp_date_tam.setWAREHOUSE_POSITION_CD_TAM(warehouse_position_cd);
                    exp_date_tam.setLPN_CODE_TAM(lpn_code);
                }

                DatabaseHelper.getInstance().CreateExp_date(exp_date_tam);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int getDataFromSeverWithBatch2(String barcodeData, String sale_codes, String type, int IsLPN, String cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = "";

        result = webService.GetProductByZone(barcodeData, sale_codes, type, IsLPN, cd);


        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }
        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String pro_exp = jsonobj.getString("_EXPIRY_DATE");
                String pro_stockin = jsonobj.getString("_STOCKIN_DATE");
                String batch = jsonobj.getString("_BATCH_NUMBER");
                String product_code = jsonobj.getString("_PRODUCT_CODE");
                String product_cd = jsonobj.getString("_PRODUCT_CD");
                String position_code = jsonobj.getString("_POSITION_CODE");
                String warehouse_position_cd = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                String lpn_code = jsonobj.getString("_LPN_CODE");


                Exp_Date_Tam exp_date_tam = new Exp_Date_Tam();
                if (pro_exp.equals("")) {
                    exp_date_tam.setEXPIRED_DATE_TAM("---");
                } else {
                    exp_date_tam.setEXPIRED_DATE_TAM(pro_exp);
                }

                if (pro_stockin.equals("")) {
                    exp_date_tam.setSTOCKIN_DATE_TAM("---");
                } else {
                    exp_date_tam.setSTOCKIN_DATE_TAM(pro_stockin);
                }

                if (batch.equals("")) {
                    exp_date_tam.setBATCH_NUMBER_TAM("---");
                } else {
                    exp_date_tam.setBATCH_NUMBER_TAM(batch);
                }
                exp_date_tam.setPRODUCT_CODE_TAM(product_code);
                exp_date_tam.setPRODUCT_CD_TAM(product_cd);
                if(type.equals("WST")){
                    exp_date_tam.setPOSITION_CODE_TAM(position_code);
                    exp_date_tam.setWAREHOUSE_POSITION_CD_TAM(warehouse_position_cd);
                    exp_date_tam.setLPN_CODE_TAM(lpn_code);
                }

                DatabaseHelper.getInstance().CreateExp_date(exp_date_tam);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }


    public int getPutAwayFromServer(String barcodeData, String sale_codes, String type, int IsLPN, String cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = "";

        result = webService.GetProductByZone(barcodeData, sale_codes, type, IsLPN, cd);


        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }
        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String pro_exp = jsonobj.getString("_EXPIRY_DATE");
                String pro_stockin = jsonobj.getString("_STOCKIN_DATE");
                String batch = "";
                if ((type.equals("WPR")) || (type.equals("WTP")) || (type.equals("WQA")) || (type.equals("WQA_Return"))) {
                    batch = jsonobj.getString("_BATCH_NUMBER");
                }

                Exp_Date_Tam exp_date_tam = new Exp_Date_Tam();
                if (pro_stockin.equals("")) {
                    exp_date_tam.setEXPIRED_DATE_TAM(pro_exp + " - " + "---");
                } else {
                    exp_date_tam.setEXPIRED_DATE_TAM(pro_exp + " - " + pro_stockin);
                }
                if ((type.equals("WPR")) || (type.equals("WTP")) || (type.equals("WQA")) || (type.equals("WQA_Return"))) {
                    exp_date_tam.setBATCH_NUMBER_TAM(batch);
                }

                DatabaseHelper.getInstance().CreateExp_date(exp_date_tam);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int getEa_UnitFromServer(String barcodeData, String IsDefault, String product_code) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetEa_Unit(barcodeData, IsDefault);
        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1"))
            return -1;

        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String ea_unit = jsonobj.getString("_UNIT");
                String pro_code = jsonobj.getString("_PRODUCT_CODE");

                if (pro_code.equals(product_code)) {
                    Ea_Unit_Tam ea_unit_tam = new Ea_Unit_Tam();
                    ea_unit_tam.setEA_UNIT_TAM(ea_unit);
                    DatabaseHelper.getInstance().CreateEa_Unit(ea_unit_tam);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public ArrayList<String> getEa_Unit(String barcodeData, String IsDefault, String product_code) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return null;

        Webservice webService = new Webservice();
        String result = webService.GetEa_Unit(barcodeData, IsDefault);
        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1"))
            return null;

        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return null;
        }

        try {
            ArrayList<String> listUnit = new ArrayList<>();
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String ea_unit = jsonobj.getString("_UNIT");
                String pro_code = jsonobj.getString("_PRODUCT_CODE");

                if (pro_code.equals(product_code)) {
                    listUnit.add(ea_unit);
                }
            }
            return listUnit;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return null;
        }

    }
    public String checkPositionOD(String barcodeData) {
        try{
            int status = this.allowSynchronizeBy3G();
            if (status != 1)
                return "Vui L??ng Ki???m Tra L???i M???ng";

            Webservice webService = new Webservice();
            String result = "" ,ware = "";

            result = webService.synchronizeGETPositionInfo(global.getAdminCode(), barcodeData, 0 , "WSO","", "", "1");

            // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                JSONObject jsonobj = jsonarray.getJSONObject(i);
                ware = jsonobj.getString("_WAREHOUSE_POSITION_CD");

            }
            if (ware.equals("")) {
                // ???? ?????ng b??? th??nh c??ng update ????? l???n sau kh??ng ?????ng b??? l???i
                //DatabaseHelper.getInstance().updateChangeCustomer(customers,  );
                return "error";
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return ware;
            }
        }
        catch (Exception e){
            return "e";
        }
    }
    public String checkPosition(String barcodeData) {
        try{
            int status = this.allowSynchronizeBy3G();
            if (status != 1)
                return "Vui L??ng Ki???m Tra L???i M???ng";

            Webservice webService = new Webservice();
            String result = "" ,ware = "";

            result = webService.synchronizeGETPositionInfo(global.getAdminCode(), barcodeData, 0 , "WRP","", "", "");

            // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                JSONObject jsonobj = jsonarray.getJSONObject(i);
                ware = jsonobj.getString("_WAREHOUSE_POSITION_CD");

            }
            if (ware.equals("")) {
                // ???? ?????ng b??? th??nh c??ng update ????? l???n sau kh??ng ?????ng b??? l???i
                //DatabaseHelper.getInstance().updateChangeCustomer(customers,  );
                return "error";
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return ware;
            }
        }
        catch (Exception e){
            return "e";
        }
    }
    public String getPositionWST(String barcodeData) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return "Vui L??ng Ki???m Tra L???i M???ng";

        Webservice webService = new Webservice();
        String result = "";

        result = webService.Check_Position_With_Usercode_WST(barcodeData);

        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("1")) {
            return "Th??nh C??ng";
        } else {
            return result ;
        }

    }


    public int getProduct_code(String barcodeData) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = "";

        result = webService.GetProduct_Code(barcodeData);

        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("0")) {
            return 0;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }
        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String pro_code = jsonobj.getString("PRODUCT_CODE");
                String pro_name = jsonobj.getString("PRODUCT_NAME");
                String pro_cd = jsonobj.getString("PRODUCT_CD");
                String lot_ind = jsonobj.getString("LOT_IND");


                Product_S_P product = new Product_S_P();
                product.setPRODUCT_CODE(pro_code);
                product.setPRODUCT_NAME(pro_name);
                product.setPRODUCT_CD(pro_cd);
                product.setLOT_IND(lot_ind);

                DatabaseHelper.getInstance().CreateProduct_SP(product);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }


    public int getBatch(String usercode, String barcodeData, String stock, String pro_code) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.synchronizeGETBatch(usercode, barcodeData, stock);
        //
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("0")) {
            return 0;
        }

        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String batch_number = jsonobj.getString("BATCH_NUMBER");
                String product_cd = jsonobj.getString("PRODUCT_CD");
                String product_code = jsonobj.getString("PRODUCT_CODE");
                String product_name = jsonobj.getString("PRODUCT_NAME");
                String stockin_date = jsonobj.getString("STOCKIN_DATE");
                String expired_date = jsonobj.getString("EXPIRED_DATE");
                String unit = jsonobj.getString("UNIT");
                String manufacturing_date = jsonobj.getString("MANUFACTURING_DATE");
                String position_code = jsonobj.getString("POSITION_CODE");
                String position_description = jsonobj.getString("POSITION_DESCRIPTION");
                String warehouse_position_cd = jsonobj.getString("WAREHOUSE_POSITION_CD");

                if (pro_code.equals(product_code)) {
                    Batch_number_Tam batch_number_tam = new Batch_number_Tam();
                    batch_number_tam.setBATCH_NUMBER(batch_number);
                    batch_number_tam.setPRODUCT_CD(product_cd);
                    batch_number_tam.setPRODUCT_CODE(product_code);
                    batch_number_tam.setPRODUCT_NAME(product_name);
                    batch_number_tam.setSTOCKIN_DATE(stockin_date);
                    batch_number_tam.setEXPIRED_DATE(expired_date);
                    batch_number_tam.setUNIT(unit);
                    batch_number_tam.setMANUFACTURING_DATE(manufacturing_date);
                    batch_number_tam.setPOSITION_CODE(position_code);
                    batch_number_tam.setPOSITION_DESCRIPTION(position_description);
                    batch_number_tam.setWAREHOUSE_POSITION_CD(warehouse_position_cd);
                    batch_number_tam.setAUTOINCREMENT(String.valueOf(i));

                    //exp_date_tam.setEXPIRED_DATE_TAM(pro_exp + " - " + pro_stockin);
                    DatabaseHelper.getInstance().CreateBatch_Number(batch_number_tam);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int getBatchAndProduct(String usercode, String barcodeData, String stockReceipt, String batch) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.synchronizeGETBatchAndProduct(usercode, barcodeData, stockReceipt, batch);
        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-20")) {
            return -20;
        }

        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String pro_exp = jsonobj.getString("_EXPIRED_DATE");
                String pro_stockin = jsonobj.getString("_STOCKIN_DATE");

                String total_shelf_life = jsonobj.getString("_TOTAL_SHELF_LIFE");
                String shelf_life_type = jsonobj.getString("_SHELF_LIFE_TYPE");
                String min_rem_shelf_life = jsonobj.getString("_MIN_REM_SHELF_LIFE");


                Exp_Date_Tam exp_date_tam = new Exp_Date_Tam();
                if (pro_stockin.equals("")) {
                    exp_date_tam.setEXPIRED_DATE_TAM(pro_exp + " - " + "---");
                } else {
                    exp_date_tam.setEXPIRED_DATE_TAM(pro_exp + " - " + pro_stockin);
                }
                exp_date_tam.setTOTAL_SHELF_LIFE(total_shelf_life);
                exp_date_tam.setSHELF_LIFE_TYPE(shelf_life_type);
                exp_date_tam.setMIN_REM_SHELF_LIFE(min_rem_shelf_life);


                //exp_date_tam.setEXPIRED_DATE_TAM(pro_exp + " - " + pro_stockin);
                DatabaseHelper.getInstance().CreateExp_date(exp_date_tam);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }


    public int getExpDateFromServer(String usercode, String barcodeData, String stockReceipt) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.synchronizeGETProductInfoo(usercode, barcodeData, stockReceipt);
        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-20")) {
            return -20;
        }

        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String pro_exp = jsonobj.getString("_EXPIRED_DATE");
                String pro_stockin = jsonobj.getString("_STOCKIN_DATE");

                String total_shelf_life = jsonobj.getString("_TOTAL_SHELF_LIFE");
                String shelf_life_type = jsonobj.getString("_SHELF_LIFE_TYPE");
                String min_rem_shelf_life = jsonobj.getString("_MIN_REM_SHELF_LIFE");


                Exp_Date_Tam exp_date_tam = new Exp_Date_Tam();
                if (pro_stockin.equals("")) {
                    exp_date_tam.setEXPIRED_DATE_TAM(pro_exp + " - " + "---");
                } else {
                    exp_date_tam.setEXPIRED_DATE_TAM(pro_exp + " - " + pro_stockin);
                }
                exp_date_tam.setTOTAL_SHELF_LIFE(total_shelf_life);
                exp_date_tam.setSHELF_LIFE_TYPE(shelf_life_type);
                exp_date_tam.setMIN_REM_SHELF_LIFE(min_rem_shelf_life);


                //exp_date_tam.setEXPIRED_DATE_TAM(pro_exp + " - " + pro_stockin);
                DatabaseHelper.getInstance().CreateExp_date(exp_date_tam);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public String getProductName(String typeScan, String barcodeData, String cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return "-1";

        Webservice webService = new Webservice();
        String result = "";
        switch (typeScan) {
            case "scan_from_stock_in":
                result = webService.synchronizeGETProductInfoo(global.getAdminCode(), barcodeData, cd);
                break;
            case "scan_from_inventory":
                result = webService.GetProductByZone(barcodeData, global.getAdminCode(), "WST", 0, global.getInventoryCD());
                break;
            case "scan_from_letdown":
                result = webService.GetProductByZone(barcodeData, global.getAdminCode(), "WLD", 0, "");
                break;
            case "scan_from_load_pallet":
                result = webService.GetProductByZone(barcodeData, global.getAdminCode(), "WPP", 0, "");
                break;
            case "scan_from_pick_list":
                result = webService.GetProductByZone(barcodeData, global.getAdminCode(), "WPL", 0, global.getPickListCD());
                break;
            case "scan_from_put_away":
                result = webService.GetProductByZone(barcodeData, global.getAdminCode(), "WPA", 0, "");
                break;
            case "scan_from_remove_lpn":
                result = webService.GetProductByZone(barcodeData, global.getAdminCode(), "WLP", 0, "");
                break;
            case "scan_from_return_warehouse":
                result = webService.GetProductByZone(barcodeData, global.getAdminCode(), "WRW", 0, global.getReturnCD());
                break;
            case "scan_from_stock_out":
                result = webService.GetProductByZone(barcodeData, global.getAdminCode(), "WSO", 0, global.getStockoutCD());
                break;
            case "scan_from_stock_transfer":
                result = webService.GetProductByZone(barcodeData, global.getAdminCode(), "WOI", 0, "");
                break;
            case "scan_from_warehouse_adjustment":
                result = webService.GetProductByZone(barcodeData, global.getAdminCode(), "WWA", 0, global.getWarehouse_AdjustmentCD());
                break;
        }

        // [{"_PRODUCT_CODE":"10038935","_PRODUCT_NAME":"TL LG GN-D602BL","_PRODUCT_FACTOR":"1","_SET_UNIT":"THUNG","_EA_UNIT":"THUNG"}]
        if (result.equals("-1")) {
            return "-1";
        } else if (result.equals("-8")) {
            return "-8";
        } else if (result.equals("-20")) {
            return "-20";
        }

        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return "1";
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                String productName = jsonobj.getString("_PRODUCT_NAME");
                return productName;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return "-1";
        }

        return "-1";
    }


    public int synchronizeGETProductInfo(String usercode, String qrcode, String stock, String expDate, String stockinDate,
                                         String unit, String positonReceive, String cont, String product_code, String product_name,
                                         String product_cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.synchronizeGETProductInfoo(usercode, qrcode, stock);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                if (i == 1) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
//                    String pro_exp = jsonobj.getString("_EXPIRED_DATE");
                    String pro_warehouse = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String pro_position_code = jsonobj.getString("_POSITION_CODE");
                    String pro_position_des = jsonobj.getString("_POSITION_DESCRIPTION");

                    int pro_set = 1;

                    Product_Qrcode qrcode1 = new Product_Qrcode();

                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        qrcode1.setPRODUCT_CD(product_cd);
                    } else {
                        qrcode1.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        qrcode1.setPRODUCT_CODE(product_code);
                    } else {
                        qrcode1.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        qrcode1.setPRODUCT_NAME(product_name);
                    } else {
                        qrcode1.setPRODUCT_NAME(pro_name);
                    }
                    qrcode1.setCREATE_TIME(getTimeCreate());
                    qrcode1.setWAREHOUSE_POSITION_CD(pro_warehouse);
                    qrcode1.setPOSITION_CODE(pro_position_code);
                    qrcode1.setPOSITION_DESCRIPTION(pro_position_des);
                    qrcode1.setSL_SET(String.valueOf(pro_set));
                    qrcode1.setMANUFACTURING_DATE(stockinDate);
                    if (cont != null && (!cont.equals(""))) {
                        qrcode1.setBATCH_NUMBER(cont);
                    } else {
                        qrcode1.setBATCH_NUMBER("");
                    }


                    qrcode1.setEA_UNIT(unit);
                    qrcode1.setSTOCKIN_DATE(stockinDate);


                    qrcode1.setEXPIRED_DATE(expDate);

                    if (stock == null) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("stockReceipt", Context.MODE_PRIVATE);
                        stock = sharedPreferences.getString("stock", "");
                        qrcode1.setSTOCK_RECEIPT_CD(stock);
                    } else {
                        qrcode1.setSTOCK_RECEIPT_CD(stock);
                    }

                    String positionTo = "";

                    if (positonReceive != null) {
                        positionTo = positonReceive;
                    }
                    String positionFrom = "";

                    qrcode1.setPRODUCT_FROM(positionFrom);
                    qrcode1.setPRODUCT_TO(positionTo);

                    if (expDate != null) {
                        ArrayList<Product_Qrcode> product_qrcodes = DatabaseHelper.getInstance().
                                getoneProduct_Qrcode(qrcode1.getPRODUCT_CD(), qrcode1.getSTOCK_RECEIPT_CD(), expDate, qrcode1.getEA_UNIT(), stockinDate ,cont);
                        if (product_qrcodes.size() > 0) {
                            Product_Qrcode product = product_qrcodes.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getEA_UNIT()))) {
                                Product_Qrcode updateProductQR = product_qrcodes.get(0);
                                float product_set = Float.parseFloat((product_qrcodes.get(0).getSL_SET()));
                                product_set += pro_set;
                                product_qrcodes.get(i).setSL_SET(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_Qrcode(updateProductQR, updateProductQR.getPRODUCT_CD(),
                                        updateProductQR.getSTOCK_RECEIPT_CD(), updateProductQR.getEXPIRED_DATE(), updateProductQR.getEA_UNIT(), updateProductQR.getSTOCKIN_DATE());
                            } else {
                                DatabaseHelper.getInstance().CreateProduct_Qrcode(qrcode1);
                            }

                        } else {
                            DatabaseHelper.getInstance().CreateProduct_Qrcode(qrcode1);
                        }
                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeGETProductByZoneWarehouse_Adjustment(Context context, String qrcode, String admin, String expDate, String unit,
                                                               String stockDate, String warehouseCD, int isLPN, String product_code,
                                                               String product_name, String batch_number, String product_cd) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WWA", isLPN, warehouseCD);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");

                    int pro_set = 1;


                    Product_Warehouse_Adjustment warehouseAdjustment = new Product_Warehouse_Adjustment();
                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        warehouseAdjustment.setPRODUCT_CD(product_cd);
                    } else {
                        warehouseAdjustment.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        warehouseAdjustment.setPRODUCT_CODE(product_code);
                    } else {
                        warehouseAdjustment.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        warehouseAdjustment.setPRODUCT_NAME(product_name);
                    } else {
                        warehouseAdjustment.setPRODUCT_NAME(pro_name);
                    }
                    warehouseAdjustment.setQTY(String.valueOf(pro_set));
                    warehouseAdjustment.setQTY_EA_AVAILABLE(quanity_ea);
                    warehouseAdjustment.setPOSITION_FROM_CD(warePosition);
                    warehouseAdjustment.setBATCH_NUMBER(batch_number);
                    warehouseAdjustment.setPOSITION_TO_CD(warePosition);
                    warehouseAdjustment.setORDER_CD(warehouseCD);
                    String positionTo = "";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    warehouseAdjustment.setLPN_TO(lpn_To);
                    warehouseAdjustment.setLPN_CODE(lpnCode);

                    warehouseAdjustment.setPOSITION_TO_CODE(positionTo);
                    warehouseAdjustment.setPOSITION_TO_DESCRIPTION("");

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            warehouseAdjustment.setSTOCKIN_DATE(stockDate);
                        }
                        warehouseAdjustment.setEXPIRED_DATE(expDate);
                        warehouseAdjustment.setUNIT(unit);
                        warehouseAdjustment.setQTY(String.valueOf(pro_set));
                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ---
                        warehouseAdjustment.setPOSITION_FROM_CODE(positionFrom);
                        warehouseAdjustment.setLPN_FROM(lpn_From);
                        warehouseAdjustment.setPOSITION_FROM_DESCRIPTION("---");
                    } else if (isLPN == 1) {
                        warehouseAdjustment.setSTOCKIN_DATE(strokinDate);
                        warehouseAdjustment.setEXPIRED_DATE(exxpiredDate);
                        warehouseAdjustment.setUNIT(ea_unit);
                        warehouseAdjustment.setQTY(quanity);

                        warehouseAdjustment.setPOSITION_FROM_CODE(position_code);
                        warehouseAdjustment.setLPN_FROM(lpnCode);
                        warehouseAdjustment.setPOSITION_FROM_DESCRIPTION(description);
                    }

                    if (isLPN == 0) {
                        ArrayList<Product_Warehouse_Adjustment> product_warehouse_adjustments = DatabaseHelper.getInstance().
                                getoneProduct_Warehouse_Adjustment(warehouseAdjustment.getPRODUCT_CD(), expDate, warehouseAdjustment.getUNIT(), warehouseAdjustment.getSTOCKIN_DATE(), warehouseCD,batch_number);
                        if (product_warehouse_adjustments.size() > 0) {
                            Product_Warehouse_Adjustment product = product_warehouse_adjustments.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                Product_Warehouse_Adjustment updateProductQR = product_warehouse_adjustments.get(0);
                                float product_set = Float.parseFloat((product_warehouse_adjustments.get(0).getQTY()));
                                float sl = product_set + 1;
                                product_warehouse_adjustments.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_Warehouse_Adjustment(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProductQR.getUNIT(), warehouseAdjustment.getSTOCKIN_DATE(), warehouseCD);
                            } else {
                                DatabaseHelper.getInstance().CreateWAREHOUSE_ADJUSTMENT(warehouseAdjustment);
                            }

                        } else {
                            DatabaseHelper.getInstance().CreateWAREHOUSE_ADJUSTMENT(warehouseAdjustment);
                        }
                    } else if (isLPN == 1) {


                        boolean isExistLPN = false;
                        ArrayList<Product_Warehouse_Adjustment> product_warehouse_adjustments = DatabaseHelper.getInstance().getAllProduct_Warehouse_Adjustment_Sync(global.getWarehouse_AdjustmentCD());
                        if (product_warehouse_adjustments.size() > 0) {
                            for (int j = 0; j < product_warehouse_adjustments.size(); j++) {
                                if (product_warehouse_adjustments.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().CreateWAREHOUSE_ADJUSTMENT(warehouseAdjustment);
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }

                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeGETProductByZoneReturnWareHouse(Context context, String qrcode, String admin, String expDate, String unit,
                                                          String stockDate, String returnCD, int isLPN, String product_code,
                                                          String product_name, String batch_number, String product_cd) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WRW", isLPN, returnCD);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");

                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    // m?? t??? VT ?????n (POSITION_TO_CODE)
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    // VT ?????n ( b??? v??o POSITION_TO_CD)
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    // VT t??? (POSITION_FROM_CODE)
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    // VT t??? (b??? v??o POSITION_FROM_CD)
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");

                    int pro_set = 1;
                    Product_Return_WareHouse return_wareHouse = new Product_Return_WareHouse();
                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        return_wareHouse.setPRODUCT_CD(product_cd);
                    } else {
                        return_wareHouse.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        return_wareHouse.setPRODUCT_CODE(product_code);
                    } else {
                        return_wareHouse.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        return_wareHouse.setPRODUCT_NAME(product_name);
                    } else {
                        return_wareHouse.setPRODUCT_NAME(pro_name);
                    }
                    return_wareHouse.setQTY(String.valueOf(pro_set));
                    return_wareHouse.setQTY_EA_AVAILABLE(quanity_ea);
                    return_wareHouse.setPOSITION_FROM_CD(warePosition);
                    return_wareHouse.setPOSITION_TO_CD(position_code);
                    return_wareHouse.setBATCH_NUMBER(batch_number);
                    return_wareHouse.setRETURN_CD(returnCD);
                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    return_wareHouse.setLPN_TO(lpn_To);
                    return_wareHouse.setLPN_CODE(lpnCode);

                    return_wareHouse.setPOSITION_TO_CODE(quanity_ea);
                    return_wareHouse.setPOSITION_TO_DESCRIPTION(quanity);

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            return_wareHouse.setSTOCKIN_DATE(stockDate);
                        }
                        return_wareHouse.setEXPIRED_DATE(expDate);
                        return_wareHouse.setUNIT(unit);
                        return_wareHouse.setQTY(String.valueOf(pro_set));
                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ---
                        return_wareHouse.setPOSITION_FROM_CODE(description);
                        return_wareHouse.setLPN_FROM(lpn_From);
                        return_wareHouse.setPOSITION_FROM_DESCRIPTION("");
                    } else if (isLPN == 1) {
                        return_wareHouse.setSTOCKIN_DATE(strokinDate);
                        return_wareHouse.setEXPIRED_DATE(exxpiredDate);
                        return_wareHouse.setUNIT(ea_unit);
                        return_wareHouse.setQTY(quanity);

                        return_wareHouse.setPOSITION_FROM_CODE(position_code);
                        return_wareHouse.setLPN_FROM(lpnCode);
                        return_wareHouse.setPOSITION_FROM_DESCRIPTION(description);
                    }

                    if (isLPN == 0) {
                        ArrayList<Product_Return_WareHouse> product_return_wareHouses = DatabaseHelper.getInstance().
                                getoneProduct_Return_WareHouse(return_wareHouse.getPRODUCT_CD(), expDate, return_wareHouse.getUNIT(), return_wareHouse.getSTOCKIN_DATE(), returnCD,batch_number);
                        if (product_return_wareHouses.size() > 0) {
                            Product_Return_WareHouse product = product_return_wareHouses.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                Product_Return_WareHouse updateProductQR = product_return_wareHouses.get(0);
                                float product_set = Float.parseFloat((product_return_wareHouses.get(0).getQTY()));
                                float sl = product_set + 1;
                                product_return_wareHouses.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_Return_WareHouse(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProductQR.getUNIT(), return_wareHouse.getSTOCKIN_DATE(), returnCD);
                            } else {
                                DatabaseHelper.getInstance().Createreturn_warehouse_OUT(return_wareHouse);
                            }
//                            return 10;

                        } else {
                            DatabaseHelper.getInstance().Createreturn_warehouse_OUT(return_wareHouse);
//                            return 10;
                        }
                    } else if (isLPN == 1) {
                        boolean isExistLPN = false;
                        ArrayList<Product_Return_WareHouse> product_return_wareHouses = DatabaseHelper.getInstance().getAllProduct_Return_WareHouse(returnCD);
                        if (product_return_wareHouses.size() > 0) {
                            for (int j = 0; j < product_return_wareHouses.size(); j++) {
                                if (product_return_wareHouses.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().Createreturn_warehouse_OUT(return_wareHouse);
//                            return 10;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }

                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeGETProductByZoneMasterPick_LPN(Context context, String qrcode, String admin, String expDate, String unit, String stockDate, String masterPickCD, int isLPN, String vitri) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WMP", isLPN, masterPickCD);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        } else if (result.equals("-31")) {
            return -31;
        } else if (result.equals("-33")) {
            return -33;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");
                    String suggestionPosition = jsonobj.getString("_Suggest_Position");


                    int pro_set = 1;
                    Product_Master_Pick masterPick = new Product_Master_Pick();
//                    if((expDate.equals(exxpiredDate)) && (stockDate.equals(stockDate)) && (unit.equals(ea_unit))){
                    masterPick.setPRODUCT_CD(pro_cd);
                    masterPick.setPRODUCT_CODE(pro_code);
                    masterPick.setPRODUCT_NAME(pro_name);
                    masterPick.setQTY(String.valueOf(pro_set));
                    masterPick.setQTY_EA_AVAILABLE(quanity_ea);
                    masterPick.setPOSITION_FROM_CD(warePosition);
                    masterPick.setPOSITION_TO_CD(warePosition);
                    masterPick.setMASTER_PICK_CD(masterPickCD);
                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    masterPick.setLPN_TO(lpn_To);
                    masterPick.setLPN_CODE(lpnCode);
                    masterPick.setSUGGESTION_POSITION(suggestionPosition);
                    masterPick.setPOSITION_TO_CODE(positionTo);
                    masterPick.setPOSITION_TO_DESCRIPTION("");
                    masterPick.setSTOCKIN_DATE(strokinDate);
                    masterPick.setEXPIRED_DATE(exxpiredDate);
                    masterPick.setUNIT(ea_unit);
                    masterPick.setQTY(quanity);
                    masterPick.setPOSITION_FROM_CODE(position_code);
                    masterPick.setLPN_FROM(lpnCode);
                    masterPick.setPOSITION_FROM_DESCRIPTION(description);

                    boolean isExistLPN = false;
                    ArrayList<Product_Master_Pick> Product_Master_Pick = DatabaseHelper.getInstance().getAllProduct_Master_Pick(masterPickCD);
                    if (Product_Master_Pick.size() > 0) {
                        for (int j = 0; j < Product_Master_Pick.size(); j++) {
                            if (Product_Master_Pick.get(j).getLPN_CODE().equals(lpnCode)) {
                                isExistLPN = true;
                            }
                        }
                    }
                    if (isExistLPN == false) {
                        DatabaseHelper.getInstance().CreateMaster_Pick(masterPick);
//                                return 10;
                    } else {
                        Dialog dialog = new Dialog(context);
                        dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                    }
                }
//                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    //    public int synchronizeGETProductByZoneMasterPick(Context context, String qrcode, String admin, String expDate, String unit, String stockDate, String masterPickCD, int isLPN , String vitri) {
//
//        int status = this.allowSynchronizeBy3G();
//        if (status != 1)
//            return -1;
//
//        Webservice webService = new Webservice();
//        String result = webService.GetProductByZone(qrcode, admin, "WMP", isLPN, masterPickCD);
//        if (result.equals("-1")) {
//            return -1;
//        } else if (result.equals("1")) {
//            return 1;
//        } else if (result.equals("-8")) {
//            return -8;
//        } else if (result.equals("-10")) {
//            return -10;
//        } else if (result.equals("-11")) {
//            return -11;
//        } else if (result.equals("-12")) {
//            return -12;
//        } else if (result.equals("-16")) {
//            return -16;
//        } else if (result.equals("-20")) {
//            return -20;
//        } else if (result.equals("-21")) {
//            return -21;
//        } else if (result.equals("-22")) {
//            return -22;
//        }else if (result.equals("-31")) {
//            return -31;
//        }else if (result.equals("-33")) {
//            return -33;
//        }
//
//        try {
//            JSONArray jsonarray = new JSONArray(result);
//            for (int i = 0; i < jsonarray.length(); i++) {
//                // l???y m???t ?????i t?????ng json ?????
//                if(vitri.equals(i)){
//                    JSONObject jsonobj = jsonarray.getJSONObject(i);
//                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
//                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
//                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
//                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
//                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
//                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
//                    String ea_unit = jsonobj.getString("_UNIT");
//                    String position_code = jsonobj.getString("_POSITION_CODE");
//                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
//                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
//                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
//                    String lpnCode = jsonobj.getString("_LPN_CODE");
//                    String suggestionPosition = jsonobj.getString("_Suggest_Position");
//
//
//                    int pro_set = 1;
//                    Product_Master_Pick masterPick = new Product_Master_Pick();
////                    if((expDate.equals(exxpiredDate)) && (stockDate.equals(stockDate)) && (unit.equals(ea_unit))){
//                    masterPick.setPRODUCT_CD(pro_cd);
//                    masterPick.setPRODUCT_CODE(pro_code);
//                    masterPick.setPRODUCT_NAME(pro_name);
//                    masterPick.setQTY(String.valueOf(pro_set));
//                    masterPick.setQTY_EA_AVAILABLE(quanity_ea);
//                    masterPick.setPOSITION_FROM_CD(warePosition);
//                    masterPick.setPOSITION_TO_CD(warePosition);
//                    masterPick.setMASTER_PICK_CD(masterPickCD);
//                    String positionTo = "---";
//                    String positionFrom = "---";
//                    String lpn_From = "";
//                    String lpn_To = "";
//
//                    masterPick.setLPN_TO(lpn_To);
//                    masterPick.setLPN_CODE(lpnCode);
//                    masterPick.setSUGGESTION_POSITION(suggestionPosition);
//                    masterPick.setPOSITION_TO_CODE(positionTo);
//                    masterPick.setPOSITION_TO_DESCRIPTION("");
//                    if (stockDate != null) {
//                        masterPick.setSTOCKIN_DATE(stockDate);
//                    }
//                    masterPick.setEXPIRED_DATE(expDate);
//                    masterPick.setUNIT(unit);
//                    masterPick.setQTY(String.valueOf(pro_set));
//                    // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ---
//                    masterPick.setPOSITION_FROM_CODE(positionFrom);
//                    masterPick.setLPN_FROM(lpn_From);
//                    masterPick.setPOSITION_FROM_DESCRIPTION("---");
//
//                    ArrayList<Product_Master_Pick> Product_Master_Picks = DatabaseHelper.getInstance().
//                            getoneProduct_Master_Pick(masterPick.getPRODUCT_CD(), expDate, masterPick.getUNIT(), masterPick.getSTOCKIN_DATE(), masterPickCD);
//                    if (Product_Master_Picks.size() > 0) {
//                        Product_Master_Pick product = Product_Master_Picks.get(0);
//                        if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {
//
//                            Product_Master_Pick updateProductQR = Product_Master_Picks.get(0);
//                            int product_set = Integer.parseInt(Product_Master_Picks.get(0).getQTY());
//                            int sl = product_set + 1;
//                            Product_Master_Picks.get(i).setQTY(String.valueOf(product_set));
//                            DatabaseHelper.getInstance().updateProduct_Master_Pick(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
//                                    String.valueOf(sl), updateProductQR.getUNIT(), masterPick.getSTOCKIN_DATE(), masterPickCD);
//                        } else {
//                            DatabaseHelper.getInstance().CreateMaster_Pick(masterPick);
//                        }
////                                return 10;
//
//                    } else {
//                        DatabaseHelper.getInstance().CreateMaster_Pick(masterPick);
////                                return 10;
//                    }
//                }
//
//            }
//
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
////            CmnFns.writeLogError("Exception "
////                    + e.getMessage());
//            return -1;
//        }
//
//        return 1;
//    }
    public int synchronizeGETProductByZoneMasterPick(Context context, String qrcode, String admin, String expDate, String unit,
                                                     String stockDate, String masterPickCD, int isLPN, String batch_number,
                                                     String product_code, String product_name, String product_cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WMP", isLPN, masterPickCD);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        } else if (result.equals("-31")) {
            return -31;
        } else if (result.equals("-33")) {
            return -33;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");
                    String suggestionPosition = jsonobj.getString("_Suggest_Position");


                    int pro_set = 1;
                    Product_Master_Pick masterPick = new Product_Master_Pick();
                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        masterPick.setPRODUCT_CD(product_cd);
                    } else {
                        masterPick.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        masterPick.setPRODUCT_CODE(product_code);
                    } else {
                        masterPick.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        masterPick.setPRODUCT_NAME(product_name);
                    } else {
                        masterPick.setPRODUCT_NAME(pro_name);
                    }
                    masterPick.setQTY(String.valueOf(pro_set));
                    masterPick.setQTY_EA_AVAILABLE(quanity_ea);
                    masterPick.setPOSITION_FROM_CD("");
                    masterPick.setPOSITION_TO_CD("");
                    masterPick.setMASTER_PICK_CD(masterPickCD);
                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    masterPick.setLPN_TO(lpn_To);
                    masterPick.setLPN_CODE(lpnCode);
                    masterPick.setSUGGESTION_POSITION(suggestionPosition);
                    masterPick.setCREATE_TIME(getTimeCreate());

                    masterPick.setPOSITION_TO_CODE(positionTo);
                    masterPick.setPOSITION_TO_DESCRIPTION("");
                    masterPick.setBATCH_NUMBER(batch_number);

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            masterPick.setSTOCKIN_DATE(stockDate);
                        }
                        masterPick.setEXPIRED_DATE(expDate);
                        masterPick.setUNIT(unit);
                        masterPick.setQTY(String.valueOf(pro_set));
                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ---
                        masterPick.setPOSITION_FROM_CODE(positionFrom);
                        masterPick.setLPN_FROM(lpn_From);
                        masterPick.setPOSITION_FROM_DESCRIPTION("---");
                    } else if (isLPN == 1) {
                        masterPick.setSTOCKIN_DATE(strokinDate);
                        masterPick.setEXPIRED_DATE(exxpiredDate);
                        masterPick.setUNIT(ea_unit);
                        masterPick.setQTY(quanity);

                        masterPick.setPOSITION_FROM_CODE(position_code);
                        masterPick.setLPN_FROM(lpnCode);
                        masterPick.setPOSITION_FROM_DESCRIPTION(description);
                    }
                    if (isLPN == 0) {
                        ArrayList<Product_Master_Pick> Product_Master_Picks = DatabaseHelper.getInstance().
                                getoneProduct_Master_Pick(masterPick.getPRODUCT_CD(), expDate, masterPick.getUNIT(), masterPick.getSTOCKIN_DATE(), masterPickCD,batch_number);
                        if (Product_Master_Picks.size() > 0) {
                            Product_Master_Pick product = Product_Master_Picks.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                Product_Master_Pick updateProductQR = Product_Master_Picks.get(0);
                                float product_set = Float.parseFloat((Product_Master_Picks.get(0).getQTY()));
                                float sl = product_set + 1;
                                Product_Master_Picks.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_Master_Pick(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProductQR.getUNIT(), masterPick.getSTOCKIN_DATE(), masterPickCD);
                            } else {
                                DatabaseHelper.getInstance().CreateMaster_Pick(masterPick);
                            }
//                                return 10;

                        } else {
                            DatabaseHelper.getInstance().CreateMaster_Pick(masterPick);
//                                return 10;
                        }

                    } else if (isLPN == 1) {
                        boolean isExistLPN = false;
                        ArrayList<Product_Master_Pick> Product_Master_Pick = DatabaseHelper.getInstance().getAllProduct_Master_Pick(masterPickCD);
                        if (Product_Master_Pick.size() > 0) {
                            for (int j = 0; j < Product_Master_Pick.size(); j++) {
                                if (Product_Master_Pick.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().CreateMaster_Pick(masterPick);
//                                return 10;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }


                    }
                }
//                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }


    public int synchronizeGETProductByZonePo_Return(Context context, String qrcode, String admin, String expDate, String unit, String stockDate,
                                                    String poreturnCD, int isLPN, String batch_number, String product_code,
                                                    String product_name, String product_cd) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WPR", isLPN, poreturnCD);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
//                 l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    // VT ?????n
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    // M?? t??? VT ?????n
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    // VT ?????n
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");
                    String manufacturing = jsonobj.getString("_MANUFACTURING_DATE");
                    int pro_set = 1;

                    Product_PoReturn poReturn = new Product_PoReturn();

                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        poReturn.setPRODUCT_CD(product_cd);
                    } else {
                        poReturn.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        poReturn.setPRODUCT_CODE(product_code);
                    } else {
                        poReturn.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        poReturn.setPRODUCT_NAME(product_name);
                    } else {
                        poReturn.setPRODUCT_NAME(pro_name);
                    }

                    poReturn.setBATCH_NUMBER(batch_number);
                    poReturn.setQTY(String.valueOf(pro_set));
                    poReturn.setQTY_EA_AVAILABLE(quanity_ea);
                    poReturn.setMANUFACTURING_DATE(manufacturing);

                    poReturn.setPOSITION_TO_CD("");
                    poReturn.setPO_RETURN_CD(poreturnCD);
                    poReturn.setWAREHOUSE_POSITION_CD(warePosition);
                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    poReturn.setLPN_TO(lpn_To);
                    poReturn.setLPN_CODE(lpnCode);

                    poReturn.setPOSITION_TO_CODE(positionTo);
                    poReturn.setPOSITION_TO_DESCRIPTION(positionTo);

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            poReturn.setSTOCKIN_DATE(stockDate);
                        }
                        poReturn.setEXPIRED_DATE(expDate);
                        poReturn.setUNIT(unit);
                        poReturn.setQTY(String.valueOf(pro_set));
                        poReturn.setPOSITION_FROM_CD("");
                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ""
                        poReturn.setPOSITION_FROM_CODE(positionFrom);
                        poReturn.setLPN_FROM(lpn_From);
                        poReturn.setPOSITION_FROM_DESCRIPTION("");
                    } else if (isLPN == 1) {
                        poReturn.setSTOCKIN_DATE(strokinDate);
                        poReturn.setEXPIRED_DATE(exxpiredDate);
                        poReturn.setUNIT(ea_unit);
                        poReturn.setQTY(quanity);
                        poReturn.setPOSITION_FROM_CD(lpn_From);

                        poReturn.setPOSITION_FROM_CODE(lpn_From);
                        poReturn.setLPN_FROM(lpnCode);
                        poReturn.setPOSITION_FROM_DESCRIPTION(lpn_From);
                    }

                    if (isLPN == 0) {
                        ArrayList<Product_PoReturn> Product_PoReturns = DatabaseHelper.getInstance().
                                getoneProduct_PoReturn(poReturn.getPRODUCT_CD(), expDate, poReturn.getUNIT(), poReturn.getSTOCKIN_DATE(), poreturnCD,batch_number);
                        if (Product_PoReturns.size() > 0) {
                            Product_PoReturn product = Product_PoReturns.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                Product_PoReturn updateProductQR = Product_PoReturns.get(0);
                                float product_set = Float.parseFloat((Product_PoReturns.get(0).getQTY()));
                                float sl = product_set + 1;
                                Product_PoReturns.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_PoReturn(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProductQR.getUNIT(), poReturn.getSTOCKIN_DATE(), poreturnCD);
                            } else {
                                DatabaseHelper.getInstance().CreatePo_Return(poReturn);
                            }
//                            return 10 ;
                        } else {
                            DatabaseHelper.getInstance().CreatePo_Return(poReturn);
//                            return 10 ;
                        }
                    } else if (isLPN == 1) {
                        boolean isExistLPN = false;
                        ArrayList<Product_PoReturn> Product_PoReturn = DatabaseHelper.getInstance().getAllProduct_PoReturn(poreturnCD);
                        if (Product_PoReturn.size() > 0) {
                            for (int j = 0; j < Product_PoReturn.size(); j++) {
                                if (Product_PoReturn.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().CreatePo_Return(poReturn);
//                            return 10 ;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }

                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int GetMaterialInspection(Context context, String barcode, String usercode, String batch, String cd, String product_code, String unit) {
        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetMaterialInspection(barcode, usercode, batch);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        }
        try {
            ArrayList<Product_Criteria> Product_Criteria = DatabaseHelper.getInstance().getallCriteria(cd, batch, product_code, unit);
            int check;
            check = Product_Criteria.size();
            if (check == 0) {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
//                 l???y m???t ?????i t?????ng json ?????
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("PRODUCT_CODE");
                    String mic_code = jsonobj.getString("MIC_CODE");
                    String mic_desc = jsonobj.getString("MIC_DESC");
                    String batch_number = jsonobj.getString("BATCH_NUMBER");

                    Product_Criteria listCriteria = new Product_Criteria();
                    listCriteria.setPRODUCT_CODE(pro_code);
                    listCriteria.setMATERIA_CD(cd);
                    listCriteria.setMIC_CODE(mic_code);
                    listCriteria.setMIC_DESC(mic_desc);
                    listCriteria.setUNIT(unit);
                    listCriteria.setBATCH_NUMBER(batch_number);
                    listCriteria.setNOTE("");
                    listCriteria.setQTY("");

                    DatabaseHelper.getInstance().CreateCriteria(listCriteria);
                }
            }
        } catch (Exception e) {

        }

        return 1;

    }

    public int synchronizeGETProductByZoneQA(Context context, String qrcode, String admin, String expDate, String unit, String stockDate,
                                             String listQACD, int isLPN, String batch_number, String product_code, String product_name, String product_cd) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetSPChuyenMa(qrcode, admin, "WQA", isLPN, listQACD);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
//                 l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    // VT ?????n
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    // M?? t??? VT ?????n
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    // VT ?????n
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");
                    String manufacturing = jsonobj.getString("_MANUFACTURING_DATE");
                    int pro_set = 1;

                    Product_QA listQA = new Product_QA();

                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        listQA.setPRODUCT_CD(product_cd);
                    } else {
                        listQA.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        listQA.setPRODUCT_CODE(product_code);
                    } else {
                        listQA.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        listQA.setPRODUCT_NAME(product_name);
                    } else {
                        listQA.setPRODUCT_NAME(pro_name);
                    }
                    listQA.setQTY(String.valueOf(pro_set));
                    listQA.setQTY_EA_AVAILABLE(quanity_ea);
                    listQA.setBATCH_NUMBER(batch_number);
                    listQA.setMANUFACTURING_DATE(manufacturing);
                    listQA.setBARCODE(qrcode);

                    listQA.setPOSITION_TO_CD(warePosition);
                    listQA.setSTOCK_QA_CD(listQACD);
                    listQA.setWAREHOUSE_POSITION_CD(warePosition);
                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";
                    String setvalues = "No";

                    listQA.setLPN_TO(lpn_To);
                    listQA.setLPN_CODE(lpnCode);
                    listQA.setCHECKED(setvalues);
                    listQA.setCHECKED_IMAGE(setvalues);

                    listQA.setPOSITION_TO_CODE(positionTo);
                    listQA.setPOSITION_TO_DESCRIPTION(positionTo);

                    if (stockDate != null) {
                        listQA.setSTOCKIN_DATE(stockDate);
                    }
                    listQA.setEXPIRED_DATE(expDate);
                    listQA.setUNIT(unit);
                    listQA.setQTY(String.valueOf(pro_set));
                    listQA.setPOSITION_FROM_CD(warePosition);
                    // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ""
                    listQA.setPOSITION_FROM_CODE(positionFrom);
                    listQA.setLPN_FROM(lpn_From);
                    listQA.setPOSITION_FROM_DESCRIPTION("");

                    DatabaseHelper.getInstance().CreateQA(listQA);


//                   if (isLPN == 0) {
//                                    ArrayList<Product_QA> Product_QAs = DatabaseHelper.getInstance().
//                                            getoneProduct_QA(listQA.getPRODUCT_CD(), expDate, listQA.getUNIT(), listQA.getSTOCKIN_DATE(), listQACD);
//                                    if (Product_QAs.size() > 0) {
//                                        Product_QA product = Product_QAs.get(0);
//                                        if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {
//
//                                            Product_QA updateProductQA = Product_QAs.get(0);
//                                            int product_set = Integer.parseInt(Product_QAs.get(0).getQTY());
//                                            int sl = product_set + 1;
//                                            Product_QAs.get(i).setQTY(String.valueOf(product_set));
//                                            DatabaseHelper.getInstance().updateProduct_QA(updateProductQA, updateProductQA.getAUTOINCREMENT(),updateProductQA.getPRODUCT_CD(),
//                                                    String.valueOf(sl), updateProductQA.getUNIT(), listQA.getSTOCKIN_DATE(), listQACD);
//                            } else {
//                                DatabaseHelper.getInstance().CreateQA(listQA);
//                            }
////                            return 10 ;
//                        } else {
//                            DatabaseHelper.getInstance().CreateQA(listQA);
////                            return 10 ;
//                        }
//                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeGETProductByZoneTransfer_Posting(Context context, String qrcode, String admin, String expDate, String unit,
                                                           String stockDate, String transferPostingCD, int isLPN, String batch_number,
                                                           String product_code, String product_name
            , String product_cd) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WTP", isLPN, transferPostingCD);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
//                 l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    // VT ?????n
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    // M?? t??? VT ?????n
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    // VT ?????n
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");
                    String manufacturing = jsonobj.getString("_MANUFACTURING_DATE");
                    int pro_set = 1;

                    Product_TransferPosting transferPosting = new Product_TransferPosting();

                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        transferPosting.setPRODUCT_CD(product_cd);
                    } else {
                        transferPosting.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        transferPosting.setPRODUCT_CODE(product_code);
                    } else {
                        transferPosting.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        transferPosting.setPRODUCT_NAME(product_name);
                    } else {
                        transferPosting.setPRODUCT_NAME(pro_name);
                    }
                    transferPosting.setQTY(String.valueOf(pro_set));
                    transferPosting.setQTY_EA_AVAILABLE(quanity_ea);
                    transferPosting.setBATCH_NUMBER(batch_number);
                    transferPosting.setMANUFACTURING_DATE(manufacturing);

                    transferPosting.setPOSITION_TO_CD("");
                    transferPosting.setSTOCK_TRANSFER_POSTING_CD(transferPostingCD);
                    transferPosting.setWAREHOUSE_POSITION_CD(warePosition);
                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    transferPosting.setLPN_TO(lpn_To);
                    transferPosting.setLPN_CODE(lpnCode);

                    transferPosting.setPOSITION_TO_CODE(positionTo);
                    transferPosting.setPOSITION_TO_DESCRIPTION(positionTo);

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            transferPosting.setSTOCKIN_DATE(stockDate);
                        }
                        transferPosting.setEXPIRED_DATE(expDate);
                        transferPosting.setUNIT(unit);
                        transferPosting.setQTY(String.valueOf(pro_set));
                        transferPosting.setPOSITION_FROM_CD("");
                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ""
                        transferPosting.setPOSITION_FROM_CODE(positionFrom);
                        transferPosting.setLPN_FROM(lpn_From);
                        transferPosting.setPOSITION_FROM_DESCRIPTION("");
                    } else if (isLPN == 1) {
                        transferPosting.setSTOCKIN_DATE(strokinDate);
                        transferPosting.setEXPIRED_DATE(exxpiredDate);
                        transferPosting.setUNIT(ea_unit);
                        transferPosting.setQTY(quanity);
                        transferPosting.setPOSITION_FROM_CD(lpn_From);

                        transferPosting.setPOSITION_FROM_CODE(lpn_From);
                        transferPosting.setLPN_FROM(lpnCode);
                        transferPosting.setPOSITION_FROM_DESCRIPTION(lpn_From);
                    }

                    if (isLPN == 0) {
                        ArrayList<Product_TransferPosting> Product_TransferPostings = DatabaseHelper.getInstance().
                                getoneProduct_TransferPosting(transferPosting.getPRODUCT_CD(), expDate, transferPosting.getUNIT(), transferPosting.getSTOCKIN_DATE(), transferPostingCD,batch_number);
                        if (Product_TransferPostings.size() > 0) {
                            Product_TransferPosting product = Product_TransferPostings.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                Product_TransferPosting updateProductQR = Product_TransferPostings.get(0);
                                int product_set = Integer.parseInt(Product_TransferPostings.get(0).getQTY());
                                int sl = product_set + 1;
                                Product_TransferPostings.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_TransferPosting(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProductQR.getUNIT(), transferPosting.getSTOCKIN_DATE(), transferPostingCD);
                            } else {
                                DatabaseHelper.getInstance().CreateTransfer_Posting(transferPosting);
                            }
//                            return 10 ;
                        } else {
                            DatabaseHelper.getInstance().CreateTransfer_Posting(transferPosting);
//                            return 10 ;
                        }
                    } else if (isLPN == 1) {
                        boolean isExistLPN = false;
                        ArrayList<Product_TransferPosting> Product_TransferPosting = DatabaseHelper.getInstance().getAllProduct_TransferPosting(transferPostingCD);
                        if (Product_TransferPosting.size() > 0) {
                            for (int j = 0; j < Product_TransferPosting.size(); j++) {
                                if (Product_TransferPosting.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().CreateTransfer_Posting(transferPosting);
//                            return 10 ;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }

                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeGETProductByZonePickup(Context context, String qrcode, String admin, String expDate, String unit, String stockDate,
                                                 String pickupCD, int isLPN, String batch_number, String product_code,
                                                 String product_name, String product_cd) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WQA", isLPN, pickupCD);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
//                 l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    // VT ?????n
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    // M?? t??? VT ?????n
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    // VT ?????n
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");
                    String manufacturing = jsonobj.getString("_MANUFACTURING_DATE");
                    int pro_set = 1;

                    Product_Pickup pickUp = new Product_Pickup();

                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        pickUp.setPRODUCT_CD(product_cd);
                    } else {
                        pickUp.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        pickUp.setPRODUCT_CODE(product_code);
                    } else {
                        pickUp.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        pickUp.setPRODUCT_NAME(product_name);
                    } else {
                        pickUp.setPRODUCT_NAME(pro_name);
                    }
                    pickUp.setQTY(String.valueOf(pro_set));
                    pickUp.setQTY_EA_AVAILABLE(quanity_ea);
                    pickUp.setBATCH_NUMBER(batch_number);
                    pickUp.setMANUFACTURING_DATE(manufacturing);

                    pickUp.setPOSITION_TO_CD("");
                    pickUp.setSTOCK_QA_CD(pickupCD);
                    pickUp.setWAREHOUSE_POSITION_CD(warePosition);
                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    pickUp.setLPN_TO(lpn_To);
                    pickUp.setLPN_CODE(lpnCode);

                    pickUp.setPOSITION_TO_CODE(positionTo);
                    pickUp.setPOSITION_TO_DESCRIPTION(positionTo);
                    pickUp.setNOTE(lpn_To);


                    if (isLPN == 0) {
                        if (stockDate != null) {
                            pickUp.setSTOCKIN_DATE(stockDate);
                        }

                        pickUp.setEXPIRED_DATE(expDate);

                        pickUp.setUNIT(unit);
                        pickUp.setQTY(String.valueOf(pro_set));
                        pickUp.setPOSITION_FROM_CD("");
                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ""
                        pickUp.setPOSITION_FROM_CODE(positionFrom);
                        pickUp.setLPN_FROM(lpn_From);
                        pickUp.setPOSITION_FROM_DESCRIPTION("");
                    } else if (isLPN == 1) {
                        pickUp.setSTOCKIN_DATE(strokinDate);
                        pickUp.setEXPIRED_DATE(exxpiredDate);
                        pickUp.setUNIT(ea_unit);
                        pickUp.setQTY(quanity);
                        pickUp.setPOSITION_FROM_CD(lpn_From);

                        pickUp.setPOSITION_FROM_CODE(lpn_From);
                        pickUp.setLPN_FROM(lpnCode);
                        pickUp.setPOSITION_FROM_DESCRIPTION(lpn_From);
                    }

                    if (isLPN == 0) {
                        ArrayList<Product_Pickup> Product_Pickups = DatabaseHelper.getInstance().
                                getoneProduct_Pickup(pickUp.getPRODUCT_CD(), expDate, pickUp.getUNIT(), pickUp.getSTOCKIN_DATE(), pickupCD,batch_number);
                        if (Product_Pickups.size() > 0) {
                            Product_Pickup product = Product_Pickups.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                Product_Pickup updateProductQR = Product_Pickups.get(0);
                                float product_set = Float.parseFloat((Product_Pickups.get(0).getQTY()));
                                float sl = product_set + 1;
                                Product_Pickups.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_Pickup(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProductQR.getUNIT(), pickUp.getSTOCKIN_DATE(), pickupCD);
                            } else {
                                DatabaseHelper.getInstance().CreatePickup(pickUp);
                            }
//                            return 10 ;
                        } else {
                            DatabaseHelper.getInstance().CreatePickup(pickUp);
//                            return 10 ;
                        }
                    } else if (isLPN == 1) {
                        boolean isExistLPN = false;
                        ArrayList<Product_Pickup> Product_Pickup = DatabaseHelper.getInstance().getAllProduct_Pickup(pickupCD);
                        if (Product_Pickup.size() > 0) {
                            for (int j = 0; j < Product_Pickup.size(); j++) {
                                if (Product_Pickup.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().CreatePickup(pickUp);
//                            return 10 ;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }

                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeGETProductByZoneReturn_QA(Context context, String qrcode, String admin, String expDate, String unit,
                                                    String stockDate, String returnQACD, int isLPN, String batch_number,
                                                    String product_code, String product_name, String product_cd, int vitri) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WQA_Return", isLPN, returnQACD);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
//                 l???y m???t ?????i t?????ng json ?????

                if(i==vitri){
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    // VT ?????n
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    // M?? t??? VT ?????n
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    // VT ?????n
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");
                    String manufacturing = jsonobj.getString("_MANUFACTURING_DATE");

                    String suggest_position = jsonobj.getString("_Suggest_Position");
                    String suggest_position_to = jsonobj.getString("_Suggest_Position_To");



                    Product_Return_QA returnQA = new Product_Return_QA();

                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        returnQA.setPRODUCT_CD(product_cd);
                    } else {
                        returnQA.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        returnQA.setPRODUCT_CODE(product_code);
                    } else {
                        returnQA.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        returnQA.setPRODUCT_NAME(product_name);
                    } else {
                        returnQA.setPRODUCT_NAME(pro_name);
                    }
                    returnQA.setQTY(quanity);
                    returnQA.setQTY_EA_AVAILABLE(quanity_ea);
                    returnQA.setBATCH_NUMBER(batch_number);
                    returnQA.setMANUFACTURING_DATE(manufacturing);

                    returnQA.setPOSITION_TO_CD("");
                    returnQA.setSTOCK_QA_CD(returnQACD);
                    returnQA.setWAREHOUSE_POSITION_CD(warePosition);
//                    String positionTo = "---";
//                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    returnQA.setLPN_TO(lpn_To);
                    returnQA.setLPN_CODE(lpnCode);

                    returnQA.setPOSITION_TO_CODE(suggest_position_to);
                    returnQA.setPOSITION_TO_DESCRIPTION(suggest_position_to);
                    returnQA.setNOTE(lpn_To);


                    if (stockDate != null) {
                        returnQA.setSTOCKIN_DATE(stockDate);
                    }

                    returnQA.setEXPIRED_DATE(expDate);

                    returnQA.setUNIT(unit);
                    returnQA.setPOSITION_FROM_CD("");
                    // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ""
                    returnQA.setPOSITION_FROM_CODE(suggest_position);
                    returnQA.setLPN_FROM(lpn_From);
//                        returnQA.setPOSITION_FROM_DESCRIPTION("");


                    ArrayList<Product_Return_QA> Product_Return_QAs = DatabaseHelper.getInstance().
                            getoneProduct_Return_QA(returnQA.getPRODUCT_CD(), expDate, returnQA.getUNIT(), returnQA.getSTOCKIN_DATE(), returnQACD,batch_number);
                    if (Product_Return_QAs.size() > 0) {
                        Product_Return_QA product = Product_Return_QAs.get(0);
                        if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                            Product_Return_QA updateProductQR = Product_Return_QAs.get(0);
                            float product_set = Float.parseFloat((Product_Return_QAs.get(0).getQTY()));
                            float sl = product_set + 1;
                            Product_Return_QAs.get(i).setQTY(String.valueOf(product_set));
                            DatabaseHelper.getInstance().updateProduct_Return_QA(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                    String.valueOf(sl), updateProductQR.getUNIT(), returnQA.getSTOCKIN_DATE(), returnQACD);
                        } else {
                            DatabaseHelper.getInstance().CreatereturnQA(returnQA);
                        }
//                            return 10 ;
                    } else {
                        DatabaseHelper.getInstance().CreatereturnQA(returnQA);
//                            return 10 ;
                    }
                }

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeGETProductByZonecancel_Good(Context context, String qrcode, String admin, String expDate, String unit
            , String stockDate, String cancelCD, int isLPN, String product_code, String product_name, String batch_number, String product_cd) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WCG", isLPN, cancelCD);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
//                 l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    // VT ?????n
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    // M?? t??? VT ?????n
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    // VT ?????n
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");
                    int pro_set = 1;

                    Product_CancelGood cancelGood = new Product_CancelGood();

                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        cancelGood.setPRODUCT_CD(product_cd);
                    } else {
                        cancelGood.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        cancelGood.setPRODUCT_CODE(product_code);
                    } else {
                        cancelGood.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        cancelGood.setPRODUCT_NAME(product_name);
                    } else {
                        cancelGood.setPRODUCT_NAME(pro_name);
                    }
                    cancelGood.setQTY(String.valueOf(pro_set));
                    cancelGood.setQTY_EA_AVAILABLE(quanity_ea);
                    cancelGood.setBATCH_NUMBER(batch_number);

                    cancelGood.setPOSITION_TO_CD("");
                    cancelGood.setCANCEL_CD(cancelCD);
                    cancelGood.setWAREHOUSE_POSITION_CD(warePosition);
                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    cancelGood.setLPN_TO(lpn_To);
                    cancelGood.setLPN_CODE(lpnCode);

                    cancelGood.setPOSITION_TO_CODE(position_code);
                    cancelGood.setPOSITION_TO_DESCRIPTION(description);

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            cancelGood.setSTOCKIN_DATE(stockDate);
                        }
                        cancelGood.setEXPIRED_DATE(expDate);
                        cancelGood.setUNIT(unit);
                        cancelGood.setQTY(String.valueOf(pro_set));
                        cancelGood.setPOSITION_FROM_CD("");
                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ""
                        cancelGood.setPOSITION_FROM_CODE(positionFrom);
                        cancelGood.setLPN_FROM(lpn_From);
                        cancelGood.setPOSITION_FROM_DESCRIPTION("");
                    } else if (isLPN == 1) {
                        cancelGood.setSTOCKIN_DATE(strokinDate);
                        cancelGood.setEXPIRED_DATE(exxpiredDate);
                        cancelGood.setUNIT(ea_unit);
                        cancelGood.setQTY(quanity);
                        cancelGood.setPOSITION_FROM_CD(lpn_From);

                        cancelGood.setPOSITION_FROM_CODE(lpn_From);
                        cancelGood.setLPN_FROM(lpnCode);
                        cancelGood.setPOSITION_FROM_DESCRIPTION(lpn_From);
                    }

                    if (isLPN == 0) {
                        ArrayList<Product_CancelGood> Product_CancelGoods = DatabaseHelper.getInstance().
                                getoneProduct_CancelGood(cancelGood.getPRODUCT_CD(), expDate, cancelGood.getUNIT(), cancelGood.getSTOCKIN_DATE(), cancelCD,batch_number);
                        if (Product_CancelGoods.size() > 0) {
                            Product_CancelGood product = Product_CancelGoods.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                Product_CancelGood updateProductQR = Product_CancelGoods.get(0);
                                float product_set = Float.parseFloat((Product_CancelGoods.get(0).getQTY()));
                                float sl = product_set + 1;
                                Product_CancelGoods.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_CancelGood(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProductQR.getUNIT(), cancelGood.getSTOCKIN_DATE(), cancelCD);
                            } else {
                                DatabaseHelper.getInstance().CreateCANCEL_GOOD(cancelGood);
                            }
//                            return 10 ;
                        } else {
                            DatabaseHelper.getInstance().CreateCANCEL_GOOD(cancelGood);
//                            return 10 ;
                        }
                    } else if (isLPN == 1) {
                        boolean isExistLPN = false;
                        ArrayList<Product_CancelGood> Product_CancelGood = DatabaseHelper.getInstance().getAllProduct_CancelGood(cancelCD);
                        if (Product_CancelGood.size() > 0) {
                            for (int j = 0; j < Product_CancelGood.size(); j++) {
                                if (Product_CancelGood.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().CreateCANCEL_GOOD(cancelGood);
//                            return 10 ;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }

                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeGETProductByZoneStockout(Context context, String qrcode, String admin, String expDate, String unit,
                                                   String stockDate, String stockoutCD, int isLPN, String batch_number,
                                                   String product_code, String product_name, String product_cd) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WSO", isLPN, stockoutCD);
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        } else if (result.equals("-31")) {
            return -31;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    // VT ?????n
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    // M?? t??? VT ?????n
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    // VT ?????n
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");
                    int pro_set = 1;
                    Product_StockOut stockOut = new Product_StockOut();
                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        stockOut.setPRODUCT_CD(product_cd);
                    } else {
                        stockOut.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        stockOut.setPRODUCT_CODE(product_code);
                    } else {
                        stockOut.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        stockOut.setPRODUCT_NAME(product_name);
                    } else {
                        stockOut.setPRODUCT_NAME(pro_name);
                    }
                    stockOut.setQTY(String.valueOf(pro_set));
                    stockOut.setQTY_EA_AVAILABLE(quanity_ea);

                    stockOut.setPOSITION_TO_CD(warePosition);
                    stockOut.setSTOCKOUT_CD(stockoutCD);
                    stockOut.setBATCH_NUMBER(batch_number);
                    stockOut.setWAREHOUSE_POSITION_CD(warePosition);
                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    stockOut.setLPN_TO(lpn_To);
                    stockOut.setLPN_CODE(lpnCode);

                    stockOut.setPOSITION_TO_CODE(position_code);
                    stockOut.setPOSITION_TO_DESCRIPTION(description);

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            stockOut.setSTOCKIN_DATE(stockDate);
                        }
                        stockOut.setEXPIRED_DATE(expDate);
                        stockOut.setUNIT(unit);
                        stockOut.setQTY(String.valueOf(pro_set));
                        stockOut.setPOSITION_FROM_CD("");
                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ""
                        stockOut.setPOSITION_FROM_CODE(positionFrom);
                        stockOut.setLPN_FROM(lpn_From);
                        stockOut.setPOSITION_FROM_DESCRIPTION("");
                    } else if (isLPN == 1) {
                        stockOut.setSTOCKIN_DATE(strokinDate);
                        stockOut.setEXPIRED_DATE(exxpiredDate);
                        stockOut.setUNIT(ea_unit);
                        stockOut.setQTY(quanity);
                        stockOut.setPOSITION_FROM_CD(lpn_From);

                        stockOut.setPOSITION_FROM_CODE(lpn_From);
                        stockOut.setLPN_FROM(lpnCode);
                        stockOut.setPOSITION_FROM_DESCRIPTION(lpn_From);
                    }

                    if (isLPN == 0) {
                        ArrayList<Product_StockOut> product_stockOuts = DatabaseHelper.getInstance().
                                getoneProduct_stockout(stockOut.getPRODUCT_CD(), expDate, stockOut.getUNIT(), stockOut.getSTOCKIN_DATE(), stockoutCD,batch_number);
                        if (product_stockOuts.size() > 0) {
                            Product_StockOut product = product_stockOuts.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                Product_StockOut updateProductQR = product_stockOuts.get(0);
                                float product_set = Float.parseFloat((product_stockOuts.get(0).getQTY()));
                                float sl = product_set + 1;
                                product_stockOuts.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_Stockout(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProductQR.getUNIT(), stockOut.getSTOCKIN_DATE(), stockoutCD);
                            } else {
                                DatabaseHelper.getInstance().CreateSTOCK_OUT(stockOut);
                            }
//                            return 10;
                        } else {
                            DatabaseHelper.getInstance().CreateSTOCK_OUT(stockOut);
//                            return 10;
                        }
                    } else if (isLPN == 1) {
                        boolean isExistLPN = false;
                        ArrayList<Product_StockOut> product_stockOut = DatabaseHelper.getInstance().getAllProduct_Stockout(stockoutCD);
                        if (product_stockOut.size() > 0) {
                            for (int j = 0; j < product_stockOut.size(); j++) {
                                if (product_stockOut.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().CreateSTOCK_OUT(stockOut);
//                            return 10;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }

                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeGETProductByZonePutaway(Context context, String qrcode, String admin, String expDate,
                                                  String unit, String stockDate, int isLNP, String product_code,
                                                  String product_name, String batch_number, String product_cd) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();

        String result = webService.GetProductByZone(qrcode, admin, "WPA", isLNP, "");

        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????


                if (i == 1 && isLNP == 0) {
                    return 1;

                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);

                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");
                    String suggestionPosition = jsonobj.getString("_Suggest_Position");


//                String pro_exp = jsonobj.getString("_EXPIRED_DATE");
                    //  String pro_set= jsonobj.getString("_SET_UNIT");
                    int pro_set = 1;
                    Product_PutAway putAway = new Product_PutAway();
                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        putAway.setPRODUCT_CD_PUTAWAY(product_cd);
                    } else {
                        putAway.setPRODUCT_CD_PUTAWAY(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        putAway.setPRODUCT_CODE_PUTAWAY(product_code);
                    } else {
                        putAway.setPRODUCT_CODE_PUTAWAY(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        putAway.setPRODUCT_NAME_PUTAWAY(product_name);
                    } else {
                        putAway.setPRODUCT_NAME_PUTAWAY(pro_name);
                    }
                    putAway.setBATCH_NUMBER(batch_number);

                    putAway.setQTY_EA_AVAILABLE(quanity_ea);
                    putAway.setPOSITION_FROM_PUTAWAY("");
                    putAway.setPOSITION_TO_PUTAWAY("");


                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    putAway.setLPN_TO(lpn_To);
                    putAway.setLPN_CODE(lpnCode);
                    putAway.setCREATE_TIME(getTimeCreate());

                    putAway.setPOSITION_TO_CODE(positionTo);
                    putAway.setPOSITION_TO_DESCRIPTION("");
                    putAway.setSUGGESTION_POSITION(suggestionPosition);

                    // 1 l?? LPN
                    if (isLNP == 1) {
                        putAway.setPOSITION_FROM_CODE(position_code);
                        putAway.setLPN_FROM(lpnCode);
                        putAway.setPOSITION_FROM_DESCRIPTION(description);
                        putAway.setEXPIRED_DATE_PUTAWAY(exxpiredDate);
                        putAway.setEA_UNIT_PUTAWAY(ea_unit);
                        putAway.setSTOCKIN_DATE_PUTAWAY(strokinDate);
                        putAway.setQTY_SET_AVAILABLE(quanity);

                    } else if (isLNP == 0) {
                        putAway.setEXPIRED_DATE_PUTAWAY(expDate);
                        putAway.setEA_UNIT_PUTAWAY(unit);
                        putAway.setQTY_SET_AVAILABLE(String.valueOf(pro_set));
                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ---
                        putAway.setPOSITION_FROM_CODE(positionFrom);
                        putAway.setLPN_FROM(lpn_From);
                        putAway.setPOSITION_FROM_DESCRIPTION("---");
                        if (stockDate != null) {
                            putAway.setSTOCKIN_DATE_PUTAWAY(stockDate);
                        }
                    }
                    if (isLNP == 0) {
                        ArrayList<Product_PutAway> product_putaway = DatabaseHelper.getInstance().
                                getoneProduct_PutAway(putAway.getPRODUCT_CD_PUTAWAY(), expDate, putAway.getEA_UNIT_PUTAWAY(), putAway.getSTOCKIN_DATE_PUTAWAY(),batch_number);
                        if (product_putaway.size() > 0) {
                            Product_PutAway product = product_putaway.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE_PUTAWAY()) && unit.equals(product.getEA_UNIT_PUTAWAY()))) {

                                Product_PutAway updateProductQR = product_putaway.get(0);
                                float product_set = Float.parseFloat((product_putaway.get(0).getQTY_SET_AVAILABLE()));
                                float sl = product_set + 1;
                                product_putaway.get(i).setQTY_SET_AVAILABLE(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_PutAway(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD_PUTAWAY(),
                                        String.valueOf(sl), updateProductQR.getEA_UNIT_PUTAWAY(), putAway.getSTOCKIN_DATE_PUTAWAY());
                            } else {
                                DatabaseHelper.getInstance().CreatePutAway(putAway);
                            }
//                            return 10;

                        } else {
                            DatabaseHelper.getInstance().CreatePutAway(putAway);
//                            return 10 ;

                        }
                    } else if (isLNP == 1) {
                        boolean isExistLPN = false;
                        ArrayList<Product_PutAway> product_putaway = DatabaseHelper.getInstance().getAllProduct_PutAway();
                        if (product_putaway.size() > 0) {
                            for (int j = 0; j < product_putaway.size(); j++) {
                                if (product_putaway.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().CreatePutAway(putAway);
//                            return 10;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }
                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public static String getTimeCreate() {
        SimpleDateFormat sdf = new SimpleDateFormat(global.getFormatDate());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime ;
    }

    public int synchronizeGETProductByZonePickList(Context context, String qrcode, String admin, String expDate, String unit, String type,
                                                   String PickListCD, String stockDate, int isLPN, String batch_number,
                                                   String product_code, String product_name, String product_cd) {


        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();


        String result = webService.GetProductByZone(qrcode, admin, type, isLPN, PickListCD);

        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        } else if (result.equals("-31")) {
            return -31;
        } else if (result.equals("-33")) {
            return -33;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    String position_code = jsonobj.getString("_POSITION_CODE");

                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");

                    int pro_set = 1;
                    PickList pickList = new PickList();
                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        pickList.setPRODUCT_CD(product_cd);
                    } else {
                        pickList.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        pickList.setPRODUCT_CODE(product_code);
                    } else {
                        pickList.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        pickList.setPRODUCT_NAME(product_name);
                    } else {
                        pickList.setPRODUCT_NAME(pro_name);
                    }
                    pickList.setBATCH_NUMBER(batch_number);

                    pickList.setQTY_SET_AVAILABLE(String.valueOf(pro_set));

                    pickList.setQTY_EA_AVAILABLE(quanity_ea);

                    pickList.setPOSITION_FROM_CD("");
                    pickList.setPOSITION_TO_CD("");

                    pickList.setPickListCD(PickListCD);


                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    pickList.setLPN_TO(lpn_To);
                    pickList.setLPN_CODE(lpnCode);

                    pickList.setPOSITION_TO_CODE(positionTo);
                    pickList.setPOSITION_TO_DESCRIPTION("");

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            pickList.setSTOCKIN_DATE(stockDate);
                        }
                        pickList.setEXPIRED_DATE(expDate);
                        pickList.setUNIT(unit);
                        pickList.setQTY_SET_AVAILABLE(String.valueOf(pro_set));

                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ---
                        pickList.setPOSITION_FROM_CODE(positionFrom);
                        pickList.setLPN_FROM(lpn_From);
                        pickList.setPOSITION_FROM_DESCRIPTION("---");
                    } else if (isLPN == 1) {
                        pickList.setSTOCKIN_DATE(strokinDate);
                        pickList.setEXPIRED_DATE(exxpiredDate);
                        pickList.setUNIT(ea_unit);
                        pickList.setPOSITION_FROM_CODE(position_code);
                        pickList.setLPN_FROM(lpnCode);
                        pickList.setPOSITION_FROM_DESCRIPTION(description);
                        pickList.setQTY_SET_AVAILABLE(quanity);
                    }

                    if (isLPN == 0) {
                        ArrayList<PickList> product_pickList = DatabaseHelper.getInstance().
                                getoneProduct_PickList(pickList.getPRODUCT_CD(), expDate, pickList.getUNIT(), PickListCD, stockDate ,batch_number);
                        if (product_pickList.size() > 0) {
                            PickList product = product_pickList.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                PickList updateProductQR = product_pickList.get(0);
                                float product_set = Float.parseFloat((product_pickList.get(0).getQTY_SET_AVAILABLE()));
                                float sl = product_set + 1;
                                product_pickList.get(i).setQTY_SET_AVAILABLE(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_PickList(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProductQR.getUNIT(), pickList.getSTOCKIN_DATE(), PickListCD);
                            } else {
                                DatabaseHelper.getInstance().CreatePickList(pickList);
                            }
//                            return 10 ;

                        } else {
                            DatabaseHelper.getInstance().CreatePickList(pickList);
//                            return 10 ;
                        }
                    } else if (isLPN == 1) {
                        boolean isExistLPN = false;
                        ArrayList<PickList> pickLists = DatabaseHelper.getInstance().getAllProduct_PickList_Sync(global.getPickListCD());
                        if (pickLists.size() > 0) {
                            for (int j = 0; j < pickLists.size(); j++) {
                                if (pickLists.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().CreatePickList(pickList);
//                            return 10 ;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }
                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }
        return 1;
    }


    public String synchronizeGETPositionInfoo(String unique_id, String userCode, String barcode, String positionReceive,
                                              String productCd, String expDate, String ea_unit, String stockin,
                                              String positionFrom, String positionTo, String type, int isLPN) {

        String postitionDes = " ";
        Webservice webService = new Webservice();
        String result = webService.synchronizeGETPositionInfo(userCode, barcode, isLPN, type, positionFrom, positionTo, positionReceive);
        if (result.equals("-1")) {
            return "-1";
        } else if (result.equals("-9")) {
            return "-9";
        } else if (result.equals("-10")) {
            return "-10";
        } else if (result.equals("-3")) {
            return "-3";
        } else if (result.equals("-6")) {
            return "-6";
        } else if (result.equals("-5")) {
            return "-5";
        } else if (result.equals("-14")) {
            return "-14";
        } else if (result.equals("-15")) {
            return "-15";
        } else if (result.equals("-17")) {
            return "-17";
        } else if (result.equals("-18")) {
            return "-18";
        } else if (result.equals("-19")) {
            return "-19";
        } else if (result.equals("-12")) {
            return "-12";
        } else if (result.equals("-27")) {
            return "-27";
        } else if (result.equals("-28")) {
            return "-28";
        }


        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int j = 0; j < jsonarray.length(); j++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(j);
//                String pro_CD = jsonobj.getString("_PRODUCT_CODE");
//                if (pro_CD.equals(qrcode)) {
                String wareHouse = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                String positionCode = jsonobj.getString("_POSITION_CODE");
                postitionDes = jsonobj.getString("_POSITION_DESCRIPTION");
                String code = jsonobj.getString("_BARCODE");

                String lpn_cd = jsonobj.getString("_LPN_CD");
                String lpn_code = jsonobj.getString("_LPN_CODE");


                if (positionReceive != null && expDate != null) {
                    if (positionReceive.equals("1") && productCd != null) {

                        //letdown
                        if (type.equals("WLD")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFromLetDown_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFromLetDown(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WRW")) {
                            //Tr??? H??ng
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_returnWarehouse_LPN(lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_returnWarehouse(positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WWA")) {
                            //Ch???nh kho
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_warehouse_Adjustment_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_warehouse_Adjustment(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }
                        } else if (type.equals("WPA")) {
                            //putaway
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WMP")) {
                            // master pick
                            ArrayList<Product_Master_Pick> listSP = new ArrayList<>();
                            listSP = DatabaseHelper.getInstance().getoneListSP_Master_Pick(unique_id);
                            String ProductCode = listSP.get(0).getPRODUCT_CODE();
                            String PositionCode = "";
                            String LPNCode = "";
                            if (isLPN == 1) {
                                LPNCode = barcode;
                            } else {
                                PositionCode = barcode;
                            }

//                            String check_position = webService.Check_Suggest_Position_Master_Pick(userCode,ProductCode, ea_unit,LPNCode,PositionCode,stockin,expDate ,global.getMasterPickCd());
//                            if (check_position.equals("-1")) {
//                                return "-1";
//                            } else if (result.equals("0")) {
//                                return "0";
//                            }
                            //
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_masterPick_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_masterPick(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WSO")) {
                            //Xu???t kho
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_StockOut_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_StockOut(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WOI")) {
                            //Chuy???n v??? tr??
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_StockTransfer_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_StockTransfer(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WPL")) {
                            //picklist
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_PickList_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_PickList(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WPP")) {
                            //load pallet
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_LoadPallet_LPN(unique_id, wareHouse, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_LoadPallet(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WST")) {
                            //Ki???m kho
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_Inventory_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_Inventory(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WLP")) {
                            //g??? pallet
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_Remove_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_Remove(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WCG")) {
                            //Tr??? H??ng
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_cancelGood_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_cancelGood(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WPR")) {
                            //Tr??? H??ng
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_poReturn_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_poReturn(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WTP")) {
                            // Ph??n h??ng chuy???n m??
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_transferPosting_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_transferPosting(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WQA")) {
                            //L???y h??ng QA
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_pickup_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_pickup(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WQA_Return")) {
                            //Tr??? H??ng QA
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFrom_RETURN_QA_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFrom_RETURN_QA(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        }
                        // DatabaseHelper.getInstance().updatePositionFrom(positionCode, wareHouse, productCd, expDate, postitionDes);
                    } else if (positionReceive.equals("2") && productCd != null) {
                        if (type.equals("WLD")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionToLetDown_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionToLetDown(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }
                        } else if (type.equals("WRW")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionTo_returnWarehouse_LPN(lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionTo_returnWarehouse(positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WMP")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionTo_masterPick_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionTo_masterPick(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }
                        } else if (type.equals("WPA")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionTo_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionTo(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }
                        } else if (type.equals("WSO")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionTo_StockOut_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionTo_StockOut(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WOI")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionTo_StockTransfer_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionTo_StockTransfer(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }

                        } else if (type.equals("WPL")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionTo_PickList_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionTo_PickList(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }
                        } else if (type.equals("WPP")) {
//                            if (isLPN == 1) {
                            DatabaseHelper.getInstance().updatePositionTo_LoadPallet_LPN(unique_id, wareHouse, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
//                            }
//                            else {
//                                DatabaseHelper.getInstance().updatePositionTo_LoadPallet(unique_id ,positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
//                            }
                        } else if (type.equals("WLP")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionTo_Remove_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionTo_Remove(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }
                        } else if (type.equals("WSI")) {
                            DatabaseHelper.getInstance().updatePositionTo_Stockin(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                        } else if (type.equals("WCG")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionTo_cancelGood_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionTo_cancelGood(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }
                        } else if (type.equals("WPR")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionTo_poReturn_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionTo_poReturn(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }
                        } else if (type.equals("WTP")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionTo_transferPosting_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionTo_transferPosting(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }
                        } else if (type.equals("WQA")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionTo_pickup_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionTo_pickup(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }
                        } else if (type.equals("WQA_Return")) {
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionTO_RETURN_QA_LPN(unique_id, lpn_code, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionTO_RETURN_QA(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }
                        }

                        // DatabaseHelper.getInstance().updatePositionTo(positionCode, wareHouse, productCd, expDate, postitionDes);
                    }
                }


            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return "-1";
        }
        return postitionDes;
    }

    public String synchronizeGETPositionInfooUnit(String unique_id, String userCode, String barcode, String positionReceive,
                                                  String productCd, String expDate, String ea_unit, String stockin,
                                                  String positionFrom, String positionTo, String type, int isLPN) {

        String postitionDes = " ";
        Webservice webService = new Webservice();
        String result = webService.synchronizeGETPositionInfo(userCode, barcode, isLPN, type, positionFrom, positionTo, positionReceive);
        if (result.equals("-1")) {
            return "-1";
        } else if (result.equals("-9")) {
            return "-9";
        } else if (result.equals("-10")) {
            return "-10";
        } else if (result.equals("-3")) {
            return "-3";
        } else if (result.equals("-6")) {
            return "-6";
        } else if (result.equals("-5")) {
            return "-5";
        } else if (result.equals("-14")) {
            return "-14";
        } else if (result.equals("-15")) {
            return "-15";
        } else if (result.equals("-17")) {
            return "-17";
        } else if (result.equals("-18")) {
            return "-18";
        } else if (result.equals("-19")) {
            return "-19";
        } else if (result.equals("-12")) {
            return "-12";
        } else if (result.equals("-27")) {
            return "-27";
        } else if (result.equals("-28")) {
            return "-28";
        }


        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int j = 0; j < jsonarray.length(); j++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(j);
//                String pro_CD = jsonobj.getString("_PRODUCT_CODE");
//                if (pro_CD.equals(qrcode)) {
                String wareHouse = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                String positionCode = jsonobj.getString("_POSITION_CODE");
                postitionDes = jsonobj.getString("_POSITION_DESCRIPTION");
                String code = jsonobj.getString("_BARCODE");

                String lpn_cd = jsonobj.getString("_LPN_CD");
                String lpn_code = jsonobj.getString("_LPN_CODE");


                if (positionReceive != null && expDate != null) {
                    if (positionReceive.equals("1") && productCd != null) {

                        if (type.equals("WOI")) {
                            //Chuy???n v??? tr??
                            if (isLPN == 1) {
                                DatabaseHelper.getInstance().updatePositionFromLetTransferUnit_LPN(unique_id, lpn_code, lpn_cd, productCd, expDate, postitionDes, ea_unit, stockin);
                            } else {
                                DatabaseHelper.getInstance().updatePositionFromTransferUnit(unique_id, positionCode, wareHouse, productCd, expDate, postitionDes, ea_unit, stockin);
                            }
                            // DatabaseHelper.getInstance().updatePositionTo(positionCode, wareHouse, productCd, expDate, postitionDes);
                        }
                    }
                }


            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return "-1";
        }
        return postitionDes;
    }


    public int synchronizeStockReceiptChecked(Context context, String usercode) {
        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return -1;
            }


            List<Product_Qrcode> product = DatabaseHelper.getInstance().getAllProduct_Qrcode_Sync(global.getStockReceiptCd());


            // l???y c??c kh??ch h??ng ch??a ?????ng b???, ????
            // ?????ng b??? v??? r???i
            // th?? s??? ko c???n
            // ph???i ?????ng b??? n???a

            if (product == null || product.size() == 0)
                return 1;

//            for (int i = 0; i< product.size(); i++){
//                String notExpDate = "Kh??ng c?? h???n s??? d???ng";
//                if (product.get(i).getEXPIRED_DATE().equals(notExpDate)){
//                    DatabaseHelper.getInstance().updateProduct_Qrcode(product.get(i), product.get(i).getPRODUCT_CD(), product.get(i).getSTOCK_RECEIPT_CD(), "");
//                }
//            }
//
//            List<Product_Qrcode> productAfter = DatabaseHelper.getInstance().getAllProduct_Qrcode(global.getStockReceiptCd());

            Webservice Webservice = new Webservice();
            //String json = convertToJson(customers);

            Gson gson = new GsonBuilder().create();

            String jsonData = gson.toJson(product);
            try {
                JSONArray jsonarray = new JSONArray(jsonData);
                for (int j = 0; j < jsonarray.length(); j++) {
                    // l???y m???t ?????i t?????ng json ?????
                    JSONObject jsonobj = jsonarray.getJSONObject(j);
                    String sl_set = jsonobj.getString("SL_SET");
                    Log.d("ddddddd", sl_set);
                    if (sl_set.equals("0") || sl_set.equals("") || sl_set.equals("00") || sl_set.equals("000")) {
                        return -24;
                    }

                }
            } catch (Exception e) {

            }
            String result = Webservice.synchronizeStockReceiptChecked(jsonData, usercode);
            if (result.equals("1")) {
                // ???? ?????ng b??? th??nh c??ng update ????? l???n sau kh??ng ?????ng b??? l???i
                //DatabaseHelper.getInstance().updateChangeCustomer(customers,  );
                return 1;
            } else if (result.equals("-25")) {
                return -25;
            } else if (result.equals("-35")) {
                return -35;
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return -1;
            }
        } catch (Exception e) {
            // TODO: handle exception

            return -1;
        }

    }

    public String synchronizeConvert_UOM() {

        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return "Vui L??ng Ki???m Tra L???i K???t N???i";
            }

            String jsonData = "";
            Webservice Webservice = new Webservice();
            //String json = convertToJson(customers);
            Gson gson = new GsonBuilder().create();


            List<TransferUnitProduct> product = DatabaseHelper.getInstance().getAllTransferUnitProduct();
            if (product == null || product.size() == 0)
                return "Kh??ng c?? d??? li???u c???a chuy???n ????n v??? t??nh";

            jsonData = gson.toJson(product);


            // l???y c??c kh??ch h??ng ch??a ?????ng b???, ????
            // ?????ng b??? v??? r???i
            // th?? s??? ko c???n
            // ph???i ?????ng b??? n???a

            String result = Webservice.synchronizeData(jsonData);

            return result;

//            int resultCovertToInt = Integer.parseInt(result);
//            if (resultCovertToInt >= 1) {
//                // ???? ?????ng b??? th??nh c??ng update ????? l???n sau kh??ng ?????ng b??? l???i
//                //DatabaseHelper.getInstance().updateChangeCustomer(customers,  );
//                return resultCovertToInt;
//            } else {
//                // ?????ng b??? kh??ng th??nh c??ng
//                return Integer.parseInt(result);
//            }
        } catch (Exception e) {
            // TODO: handle exception

            return "?????ng B??? Kh??ng Th??nh C??ng";
        }

    }
    public String synchronizeDataV2(String usercode, String type, String CD) {

        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return "Vui L??ng Ki???m Tra K???t N???i M???ng";
            }

            String jsonData = "";
            Webservice Webservice = new Webservice();
            //String json = convertToJson(customers);
            Gson gson = new GsonBuilder().create();
//put away
            if (type.equals("WPA")) {
                List<Product_PutAway> product = DatabaseHelper.getInstance().getAllProduct_PutAway_Sync();
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";

                jsonData = gson.toJson(product);
            } // tra hang
            else if (type.equals("WRW")) {
                List<Product_Return_WareHouse> product = DatabaseHelper.getInstance().getAllProduct_Return_WareHouse_Sync(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";

                jsonData = gson.toJson(product);
            } //chat hang len lpn
            else if (type.equals("WLP")) {
                List<Product_Remove_LPN> product = DatabaseHelper.getInstance().getAllProduct_Remove_LPN_Sync();
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";

                jsonData = gson.toJson(product);
            } //let down
            else if (type.equals("WLD")) {
                List<ProductLetDown> product = DatabaseHelper.getInstance().getAllProduct_LetDown_Sync();
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";

                jsonData = gson.toJson(product);
            } // tra hang
            else if (type.equals("WSO")) {
                List<Product_StockOut> product = DatabaseHelper.getInstance().getAllProduct_Stockout_Sync(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            } // chuyen vi tri
            else if (type.equals("WOI")) {
                List<Product_StockTransfer> product = DatabaseHelper.getInstance().getAllProduct_StockTransfer_Sync();
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            }// pick list
            else if (type.equals("WPL")) {
                List<PickList> product = DatabaseHelper.getInstance().getAllProduct_PickList_Sync(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            } else if (type.equals("WPP")) {
                try {
                    int check = DatabaseHelper.getInstance().getDuplicate_LoadPallet();
                    if (check > 1) {
                        return "Tr??ng D??? Li???u Vui L??ng Ki???m Tra L???i";
                    }

                } catch (Exception e) {

                }
                List<Product_LoadPallet> product = DatabaseHelper.getInstance().getAllProduct_LoadPallet_Sync("");
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            } else if (type.equals("WST")) {
                List<InventoryProduct> product = DatabaseHelper.getInstance().getAllProduct_Inventory_Sync(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            } else if (type.equals("WWA")) {
                List<Product_Warehouse_Adjustment> product = DatabaseHelper.getInstance().getAllProduct_Warehouse_Adjustment_Sync(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            }// masster pick
            else if (type.equals("WMP")) {
                try {
                    int check = DatabaseHelper.getInstance().getDuplicate_MasterPick(CD);
                    if (check > 1) {
                        return "Tr??ng D??? Li???u Vui L??ng Ki???m Tra L???i";
                    }

                } catch (Exception e) {

                }

                List<Product_Master_Pick> product = DatabaseHelper.getInstance().getAllProduct_Master_Pick_Sync(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);

            }
            //cancel good (xuat huy)
            else if (type.equals("WCG")) {
                List<Product_CancelGood> product = DatabaseHelper.getInstance().getAllProduct_CancelGood_Sync(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            }
            // po return
            else if (type.equals("WPR")) {
                List<Product_PoReturn> product = DatabaseHelper.getInstance().getAllProduct_PoReturn_Sync(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            }
            // transfer posting
            else if (type.equals("WTP")) {
                List<Product_TransferPosting> product = DatabaseHelper.getInstance().getAllProduct_TransferPosting_Sync(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            }

            // QA
            else if (type.equals("WQA")) {
                List<Product_Pickup> product = DatabaseHelper.getInstance().getAllProduct_Pickup_Sync(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            }
            // return QA
            else if (type.equals("WQA_Return")) {
                List<Product_Return_QA> product = DatabaseHelper.getInstance().getAllProduct_Return_QA_Sync(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            }


            try {

                if ((!type.equals("WWA")) || (!type.equals("WST"))) {
                    JSONArray jsonarray = new JSONArray(jsonData);
                    for (int j = 0; j < jsonarray.length(); j++) {
                        // l???y m???t ?????i t?????ng json ?????
                        JSONObject jsonobj = jsonarray.getJSONObject(j);
                        String qty = jsonobj.getString("QTY");
                        Log.d("ddddddd", qty);
                        if (qty.equals("0") || qty.equals("") || qty.equals("00") || qty.equals("000")) {
                            return "Vui L??ng Ki???m Tra L???i S??? L?????ng";
                        }

                    }
                }
            } catch (Exception e) {

            }

            // l???y c??c kh??ch h??ng ch??a ?????ng b???, ????
            // ?????ng b??? v??? r???i
            // th?? s??? ko c???n
            // ph???i ?????ng b??? n???a

            String result = Webservice.synchronizeData(jsonData, usercode, type);

            if (result.contains("L??u th??nh c??ng")) {
                // ???? ?????ng b??? th??nh c??ng update ????? l???n sau kh??ng ?????ng b??? l???i
                //DatabaseHelper.getInstance().updateChangeCustomer(customers,  );
                return "L??u th??nh c??ng";
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return result;
            }
        } catch (Exception e) {
            // TODO: handle exception

            return "C?? l???i x???y ra vui l??ng th??? l???i";
        }

    }
    public int synchronizeDataLoadPallet(String usercode, String type, String CD , String lpn_code) {

        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return -1;
            }

            String jsonData = "";
            Webservice Webservice = new Webservice();
            //String json = convertToJson(customers);
            Gson gson = new GsonBuilder().create();
            if (type.equals("WPP")) {
                try {
                    int check = DatabaseHelper.getInstance().getDuplicate_LoadPallet();
                    if (check > 1) {
                        return -36;
                    }

                } catch (Exception e) {

                }
                List<Product_LoadPallet> product = DatabaseHelper.getInstance().getAllProduct_LoadPallet_Sync("");
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            }

            // l???y c??c kh??ch h??ng ch??a ?????ng b???, ????
            // ?????ng b??? v??? r???i
            // th?? s??? ko c???n
            // ph???i ?????ng b??? n???a

            String result = Webservice.synchronizeData(jsonData, usercode, type);
            int resultCovertToInt = Integer.parseInt(result);
            if (resultCovertToInt >= 1) {
                // ???? ?????ng b??? th??nh c??ng update ????? l???n sau kh??ng ?????ng b??? l???i
                //DatabaseHelper.getInstance().updateChangeCustomer(customers,  );
                return resultCovertToInt;
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return Integer.parseInt(result);
            }
        } catch (Exception e) {
            // TODO: handle exception

            return -1;
        }

    }

    public int synchronizeData(String usercode, String type, String CD) {

        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return -1;
            }

            String jsonData = "";
            Webservice Webservice = new Webservice();
            //String json = convertToJson(customers);
            Gson gson = new GsonBuilder().create();
//put away
            if (type.equals("WPA")) {
                List<Product_PutAway> product = DatabaseHelper.getInstance().getAllProduct_PutAway_Sync();
                if (product == null || product.size() == 0)
                    return 1;

                jsonData = gson.toJson(product);
            } // tra hang
            else if (type.equals("WRW")) {
                List<Product_Return_WareHouse> product = DatabaseHelper.getInstance().getAllProduct_Return_WareHouse_Sync(CD);
                if (product == null || product.size() == 0)
                    return 1;

                jsonData = gson.toJson(product);
            } //chat hang len lpn
            else if (type.equals("WLP")) {
                List<Product_Remove_LPN> product = DatabaseHelper.getInstance().getAllProduct_Remove_LPN_Sync();
                if (product == null || product.size() == 0)
                    return 1;

                jsonData = gson.toJson(product);
            } //let down
            else if (type.equals("WLD")) {
                List<ProductLetDown> product = DatabaseHelper.getInstance().getAllProduct_LetDown_Sync();
                if (product == null || product.size() == 0)
                    return 1;

                jsonData = gson.toJson(product);
            } // tra hang
            else if (type.equals("WSO")) {
                List<Product_StockOut> product = DatabaseHelper.getInstance().getAllProduct_Stockout_Sync(CD);
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            } // chuyen vi tri
            else if (type.equals("WOI")) {
                List<Product_StockTransfer> product = DatabaseHelper.getInstance().getAllProduct_StockTransfer_Sync();
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            }// pick list
            else if (type.equals("WPL")) {
                List<PickList> product = DatabaseHelper.getInstance().getAllProduct_PickList_Sync(CD);
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            } else if (type.equals("WPP")) {
                try {
                    int check = DatabaseHelper.getInstance().getDuplicate_LoadPallet();
                    if (check > 1) {
                        return -36;
                    }

                } catch (Exception e) {

                }
                List<Product_LoadPallet> product = DatabaseHelper.getInstance().getAllProduct_LoadPallet_Sync("");
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            } else if (type.equals("WST")) {
                List<InventoryProduct> product = DatabaseHelper.getInstance().getAllProduct_Inventory_Sync(CD);
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            } else if (type.equals("WWA")) {
                List<Product_Warehouse_Adjustment> product = DatabaseHelper.getInstance().getAllProduct_Warehouse_Adjustment_Sync(CD);
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            }// masster pick
            else if (type.equals("WMP")) {
                try {
                    int check = DatabaseHelper.getInstance().getDuplicate_MasterPick(CD);
                    if (check > 1) {
                        return -36;
                    }

                } catch (Exception e) {

                }

                List<Product_Master_Pick> product = DatabaseHelper.getInstance().getAllProduct_Master_Pick_Sync(CD);
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);

            }
            //cancel good (xuat huy)
            else if (type.equals("WCG")) {
                List<Product_CancelGood> product = DatabaseHelper.getInstance().getAllProduct_CancelGood_Sync(CD);
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            }
            // po return
            else if (type.equals("WPR")) {
                List<Product_PoReturn> product = DatabaseHelper.getInstance().getAllProduct_PoReturn_Sync(CD);
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            }
            // transfer posting
            else if (type.equals("WTP")) {
                List<Product_TransferPosting> product = DatabaseHelper.getInstance().getAllProduct_TransferPosting_Sync(CD);
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            }

            // QA
            else if (type.equals("WQA")) {
                List<Product_Pickup> product = DatabaseHelper.getInstance().getAllProduct_Pickup_Sync(CD);
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            }
            // return QA
            else if (type.equals("WQA_Return")) {
                List<Product_Return_QA> product = DatabaseHelper.getInstance().getAllProduct_Return_QA_Sync(CD);
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            }
            else if (type.equals("OD")) {
                List<Product_OD> product = DatabaseHelper.getInstance().getAllProductOD();
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            }


            try {
                if(type.equals("OD")){

                }else{
                    if ((!type.equals("WWA")) || (!type.equals("WST"))|| (!type.equals("OD"))) {
                        JSONArray jsonarray = new JSONArray(jsonData);
                        for (int j = 0; j < jsonarray.length(); j++) {
                            // l???y m???t ?????i t?????ng json ?????
                            JSONObject jsonobj = jsonarray.getJSONObject(j);
                            String qty = jsonobj.getString("QTY");
                            Log.d("ddddddd", qty);
                            if (qty.equals("0") || qty.equals("") || qty.equals("00") || qty.equals("000")) {
                                return -24;
                            }

                        }
                    }
                }

            } catch (Exception e) {

            }

            // l???y c??c kh??ch h??ng ch??a ?????ng b???, ????
            // ?????ng b??? v??? r???i
            // th?? s??? ko c???n
            // ph???i ?????ng b??? n???a

            String result = Webservice.synchronizeData(jsonData, usercode, type);
            int resultCovertToInt = Integer.parseInt(result);
            if (resultCovertToInt >= 1) {
                // ???? ?????ng b??? th??nh c??ng update ????? l???n sau kh??ng ?????ng b??? l???i
                //DatabaseHelper.getInstance().updateChangeCustomer(customers,  );
                return resultCovertToInt;
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return Integer.parseInt(result);
            }
        } catch (Exception e) {
            // TODO: handle exception

            return -1;
        }

    }
    public String synchronizeData_RQBT_FinalV2(String usercode, String type, String CD) {

        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return "Vui L??ng Ki???m Tra K???t N???i M???ng";
            }

            String jsonData = "";
            Webservice Webservice = new Webservice();
            //String json = convertToJson(customers);
            Gson gson = new GsonBuilder().create();


            // chuyen ma
            if (type.equals("WTP")) {
                List<Product_ChuyenMa> product = DatabaseHelper.getInstance().getAll_ChuyenMa(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            }
            // chuyen ma
            if (type.equals("WQA")) {
                List<Product_Result_QA> product = DatabaseHelper.getInstance().getAll_RESULT_QA(CD);
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            }

            // l???y c??c kh??ch h??ng ch??a ?????ng b???, ????
            // ?????ng b??? v??? r???i
            // th?? s??? ko c???n
            // ph???i ?????ng b??? n???a

            String result = Webservice.synchronizeData_RQBT_Final(jsonData, usercode, type);
            if (result.contains("L??u th??nh c??ng")) {
                // ???? ?????ng b??? th??nh c??ng update ????? l???n sau kh??ng ?????ng b??? l???i
                //DatabaseHelper.getInstance().updateChangeCustomer(customers,  );
                return "L??u th??nh c??ng";
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return result;
            }
        } catch (Exception e) {
            // TODO: handle exception

            return "C?? l???i x???y ra vui l??ng th??? l???i";
        }

    }

    public String synchronizeData_With_Message(String barcode, String type ) {

        try {
            String usercode = CmnFns.readDataAdmin();

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return "Vui L??ng Ki???m Tra K???t N???i M???ng";
            }

            String jsonData = "";
            Webservice Webservice = new Webservice();
            //String json = convertToJson(customers);
            Gson gson = new GsonBuilder().create();

            Product_Stockout_OD sp = new Product_Stockout_OD();
            sp.setLPN_CODE(barcode);
            sp.setLPN_TO("");
            sp.setOUTBOUND_DELIVERY_CD(global.getOutbound_od_cd());
            sp.setPOSITION_CODE("");
            sp.setPOSITION_TO_CD(global.getStockout_od_cd());

            DatabaseHelper.getInstance().Create_Stockout_OD(sp);


            // xu???t kho OD
            if (type.equals("OD_WSO")) {
                List<Product_Stockout_OD> product = DatabaseHelper.getInstance().getAllProduct_Stockout_OD();
                if (product == null || product.size() == 0)
                    return "Kh??ng C?? Danh S??ch D??? Li???u ";
                jsonData = gson.toJson(product);
            }

            // l???y c??c kh??ch h??ng ch??a ?????ng b???, ????
            // ?????ng b??? v??? r???i
            // th?? s??? ko c???n
            // ph???i ?????ng b??? n???a

            String result = Webservice.synchronizeData_With_Message(jsonData, usercode, type);

            DatabaseHelper.getInstance().deleteProduct_Stockout_OD();
            // ?????ng b??? kh??ng th??nh c??ng
            return result;

        } catch (Exception e) {
            // TODO: handle exception

            return "C?? l???i x???y ra vui l??ng th??? l???i";
        }

    }

    public int synchronizeData_RQBT_Final(String usercode, String type, String CD) {

        try {

            int status = this.allowSynchronizeBy3G();
            if (status == 102 || status == -1) {
                return -1;
            }

            String jsonData = "";
            Webservice Webservice = new Webservice();
            //String json = convertToJson(customers);
            Gson gson = new GsonBuilder().create();


            // chuyen ma
            if (type.equals("WTP")) {
                List<Product_ChuyenMa> product = DatabaseHelper.getInstance().getAll_ChuyenMa(CD);
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            }
            // chuyen ma
            if (type.equals("WQA")) {
                List<Product_Result_QA> product = DatabaseHelper.getInstance().getAll_RESULT_QA(CD);
                if (product == null || product.size() == 0)
                    return 1;
                jsonData = gson.toJson(product);
            }

            // l???y c??c kh??ch h??ng ch??a ?????ng b???, ????
            // ?????ng b??? v??? r???i
            // th?? s??? ko c???n
            // ph???i ?????ng b??? n???a

            String result = Webservice.synchronizeData_RQBT_Final(jsonData, usercode, type);
            int resultCovertToInt = Integer.parseInt(result);
            if (resultCovertToInt >= 1) {
                // ???? ?????ng b??? th??nh c??ng update ????? l???n sau kh??ng ?????ng b??? l???i
                //DatabaseHelper.getInstance().updateChangeCustomer(customers,  );
                return resultCovertToInt;
            } else {
                // ?????ng b??? kh??ng th??nh c??ng
                return Integer.parseInt(result);
            }
        } catch (Exception e) {
            // TODO: handle exception

            return -1;
        }

    }


    public static boolean isCheckAdmin() {
        String fileAdmin = "fsys_tms_admin.txt";
        String folderAdmin = "TMS";


        // folder cho admin
        String folderPathAdmin = Environment.getExternalStorageDirectory()
                + File.separator + folderAdmin; // folder name
        String filePathAdmin = folderPathAdmin + "/" + fileAdmin;
        File mFilePathAdmin = new File(filePathAdmin);
        File mFolderPathAdmin = new File(filePathAdmin);


        //ki???m tra folder "TMS" ???? t???n t???i ch??a
        if (mFolderPathAdmin.exists()) {
            //if(mFilePathAdmin.exists()){
            return true;
            //}else{
            //     return false;
            //   }

        } else {
            return false;
            // Toast.makeText(this, "Vui l??ng nh???p m?? nh??n vi??n", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isCheckSale() {
        String fileName = "fsys_tms.txt";
        String folderName = "TMS";

        // folder cho shipper
        String folderPath = Environment.getExternalStorageDirectory()
                + File.separator + folderName; // folder name
        String filePath = folderPath + "/" + fileName;
        File mFilePath = new File(filePath);
        File mFolderPath = new File(filePath);

        //ki???m tra folder "TMS" ???? t???n t???i ch??a
        if (mFolderPath.exists()) {

            //  if(mFilePath.exists()){
            return true;
            //    }else{
            ////        return  false;
            //    }


        } else {
            // Toast.makeText(this, "Vui l??ng nh???p m?? nh??n vi??n", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static String readDataAdminNew() {
        String fileAdmin = "fsys_tms_admin.txt";
        String folderAdmin = "TMS";
        File filePathAdmin  = new File(folderAdmin + "/" + fileAdmin);
        return readFileSystemNew(filePathAdmin);
    }

    public static String readDataAdmin() {
        String texxt = "";
        String fileAdmin = "fsys_tms_admin.txt";
        String folderAdmin = "TMS";
        File filePathAdminNew  = new File(folderAdmin + "/" + fileAdmin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          texxt = readFileSystemNew(filePathAdminNew);
        }else{
            String folderPathAdmin = Environment.getExternalStorageDirectory()
                    + File.separator + folderAdmin; // folder name
            String filePathAdmin = folderPathAdmin + "/" + fileAdmin;

            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(filePathAdmin));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                    texxt = line.toString();
                    Log.e("file_day_nay", "" + text);
                }
                br.close();
            } catch (IOException e) {
            }
        }
        return texxt;
    }

    public static String readDataShipperNew() {
        String fileAdmin = "fsys_tms.txt";
        String folderAdmin = "TMS";
        File filePathAdmin  = new File(folderAdmin + "/" + fileAdmin);
        return readFileSystemNew(filePathAdmin);
    }

    public static String readFileSystemNew(File file) {
        String sale_code = null;
        String chuoi = String.valueOf(file);
        String chuoi1[] = chuoi.split("/");
        String folder = chuoi1[0];
        String filename = chuoi1[1];
        Uri contentUri = MediaStore.Files.getContentUri("external");

        String selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?";

        String[] selectionArgs = new String[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            selectionArgs = new String[]{Environment.DIRECTORY_DOCUMENTS + "/"+folder+"/"};
        }


        Cursor cursor = global.getAppContext().getContentResolver().query(contentUri, null, selection, selectionArgs, null);

        Uri uri = null;

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String fileNames = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));

            if (fileNames.equals(filename)) {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID));

                uri = ContentUris.withAppendedId(contentUri, id);

                break;
            }
        }

        if (uri == null) {

        } else {
            try {
                InputStream inputStream = global.getAppContext().getContentResolver().openInputStream(uri);
                BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();

                if (inputStream != null) {

                    String line;
                    while ((line=buffreader.readLine()) != null) {
                        sb.append(line);

                    }
                    sale_code = sb.toString();

                }
                inputStream.close();
                buffreader.close();


            } catch (IOException e) {
                e.toString();

            }
        }
        return sale_code;
    }

    public static String readDataShipper() {
        String texxt = "";
        String fileAdmin = "fsys_tms.txt";
        String folderAdmin = "TMS";
        File filePathShipperNew  = new File(folderAdmin + "/" + fileAdmin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            texxt = readFileSystemNew(filePathShipperNew);
        }else{
            String folderPathAdmin = Environment.getExternalStorageDirectory()
                    + File.separator + folderAdmin; // folder name
            String filePathAdmin = folderPathAdmin + "/" + fileAdmin;

            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(filePathAdmin));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                    texxt = line.toString();
                    Log.e("file_day_nay", "" + text);
                }
                br.close();
            } catch (IOException e) {
            }
        }
        return texxt;
    }
    public static int getHH_Param_Layout(String Salescode) {


        int status = allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetHH_Param_Layout(Salescode);
        if (result.equals("-1"))
            return -1;

        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                if (jsonobj.getString("FUNC_NAME").toString().equals("STOCK_IN_DRY")) {
                    ParamLayout param = new ParamLayout();
                    param.setKey(jsonobj.getString("FUNC_NAME"));
                    param.setValue(jsonobj.getString("ALLOW_USED"));
                    if (DatabaseHelper.getInstance().checkExistsParam_Layout(jsonobj.getString("FUNC_NAME"))) {
                        DatabaseHelper.getInstance().updateParam_Layout(param);
                    } else {
                        DatabaseHelper.getInstance().createParam_Layout(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("FUNC_NAME").toString().equals("STOCK_IN_FRESH")) {
                    ParamLayout param = new ParamLayout();
                    param.setKey(jsonobj.getString("FUNC_NAME"));
                    param.setValue(jsonobj.getString("ALLOW_USED"));
                    if (DatabaseHelper.getInstance().checkExistsParam_Layout(jsonobj.getString("FUNC_NAME"))) {
                        DatabaseHelper.getInstance().updateParam_Layout(param);
                    } else {
                        DatabaseHelper.getInstance().createParam_Layout(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public static int getHH_Param(String Salescode) {


        int status = allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        //String result = webService.GetHH_Param(Salescode);
        String result = webService.GetHH_Param_Temp(Salescode);
        if (result.equals("-1"))
            return -1;

        if (result.equals("1")) {
            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);
                if (jsonobj.getString("ParamKey").toString().equals("URL_Delivery")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("ParamKey").toString().equals("LOCK_WH_Adjustment")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                  global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("ParamKey").toString().equals("WAREHOUSE_TYPE_CD")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_PickListHH")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_OutboundDelivery")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_StockReceiptCont")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_Check_Transport")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_StockQAPerform")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_StockQA_Return")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_TransferPosting")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_TransferPostingClassify")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }


                if (jsonobj.getString("ParamKey").toString().equals("URL_StockQA")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_StockTake")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }

//                        global.arrPackageAllow = new ArrayList<String>(Arrays.asList(arr));
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_PickList")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_PickList_Review")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_PickByOrder")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_Stockin")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_WarehouseAdjust")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_StockOutOD")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_StockOut")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_Cancel_Goods")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_PO_RETURN")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_Return")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_Stockin_detail")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }
                }
                if (jsonobj.getString("ParamKey").toString().equals("URL_PreviewStockDetailForApp")) {
                    CParam param = new CParam();
                    param.setKey(jsonobj.getString("ParamKey"));
                    param.setValue(jsonobj.getString("ParamValue"));
                    if (DatabaseHelper.getInstance().checkExistsParam(jsonobj.getString("ParamKey"))) {
                        DatabaseHelper.getInstance().updateParam(param);
                    } else {
                        DatabaseHelper.getInstance().createParam(param);
                    }
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }
    public String Check_Quantity_LPN_With_SO (String lpn_code){
        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return "Vui L??ng Ki???m Tra L???i K???t N???i M???ng";

        Webservice webService = new Webservice();
        String result = webService.Check_Quantity_LPN_With_SO(lpn_code);
        return result;
    }
    public String Check_Product_In_SO(String product_cd , String pro_code , String lpn_code){
        int status = this.allowSynchronizeBy3G();
        if (status != 1)
           return "Vui L??ng Ki???m Tra L???i K???t N???i M???ng";

        Webservice webService = new Webservice();
        String result = webService.Check_Product_In_SO(product_cd, pro_code, lpn_code);
        return result;
    }

    //TODO: t??? d??ng 2155 -> 2280 - ?????i th??nh h??m synchronizeGETProductByZoneLoadPallet
    public int synchronizeGETProductByZoneLoadPallet(Context context, String qrcode, String admin, String expDate, String unit,
                                                     String stockDate, int isLPN, String product_code, String product_name,
                                                     String batch_number, String product_cd , String lpn_code) {
        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WPP", isLPN, "");

        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        }
        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
//                String pro_CD = jsonobj.getString("_PRODUCT_CODE");
//                if (pro_CD.equals(qrcode)) {
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");

                    int pro_set = 1;
                    Product_LoadPallet product_loadPallet = new Product_LoadPallet();

                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        product_loadPallet.setPRODUCT_CD(product_cd);
                    } else {
                        product_loadPallet.setPRODUCT_CD(pro_cd);
                    }

                    if ((product_code != null) && (!product_code.equals(""))) {
                        product_loadPallet.setPRODUCT_CODE(product_code);
                    } else {
                        product_loadPallet.setPRODUCT_CODE(pro_code);
                    }
                    product_loadPallet.setBATCH_NUMBER(batch_number);

                    if ((product_name != null) && (!product_name.equals(""))) {
                        product_loadPallet.setPRODUCT_NAME(product_name);
                    } else {
                        product_loadPallet.setPRODUCT_NAME(pro_name);
                    }
                    product_loadPallet.setQTY(String.valueOf(pro_set));
                    product_loadPallet.setQTY_EA_AVAILABLE(quanity_ea);

                    product_loadPallet.setPOSITION_FROM_CD(warePosition);
                    product_loadPallet.setPOSITION_TO_CD(warePosition);
                    // putAway.setPOSITION_DESCRIPTION(description);
                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";
                    if ((lpn_code != null) && (!lpn_code.equals(""))) {
                        product_loadPallet.setLPN_TO(lpn_code);
                    }else
                    {
                        product_loadPallet.setLPN_TO(lpn_To);
                    }
                    product_loadPallet.setLPN_CODE(lpnCode);
                    product_loadPallet.setPOSITION_TO_CODE(positionTo);
                    product_loadPallet.setPOSITION_TO_DESCRIPTION("");

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            product_loadPallet.setSTOCKIN_DATE(stockDate);
                        }
                        product_loadPallet.setEXPIRED_DATE(expDate);
                        product_loadPallet.setUNIT(unit);
                        product_loadPallet.setQTY(String.valueOf(pro_set));

                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ---
                        product_loadPallet.setPOSITION_FROM_CODE(positionFrom);
                        product_loadPallet.setLPN_FROM(lpn_From);
                        product_loadPallet.setPOSITION_FROM_DESCRIPTION("---");
                    }
//                    else if (isLPN == 1) {
//
//                        product_loadPallet.setSTOCKIN_DATE(strokinDate);
//                        product_loadPallet.setEXPIRED_DATE(exxpiredDate);
//                        product_loadPallet.setUNIT(ea_unit);
//                        product_loadPallet.setQTY(quanity);
//
//                        product_loadPallet.setPOSITION_FROM_CODE(position_code);
//                        product_loadPallet.setLPN_FROM(lpnCode);
//                        product_loadPallet.setPOSITION_FROM_DESCRIPTION(description);
//
//                    }

                    if (isLPN == 0) {
                        ArrayList<Product_LoadPallet> loadPallets = DatabaseHelper.getInstance().
                                getoneProduct_LoadPallet(product_loadPallet.getPRODUCT_CD(), expDate, product_loadPallet.getUNIT(), product_loadPallet.getSTOCKIN_DATE(),batch_number);
                        if (loadPallets.size() > 0) {
                            Product_LoadPallet product = loadPallets.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                Product_LoadPallet updateProduct_loadPallet = loadPallets.get(0);
                                float product_set = Float.parseFloat((loadPallets.get(0).getQTY()));
                                float sl = product_set + 1;
                                loadPallets.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_LoadPallet(updateProduct_loadPallet, updateProduct_loadPallet.getAUTOINCREMENT(), updateProduct_loadPallet.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProduct_loadPallet.getUNIT(), product_loadPallet.getSTOCKIN_DATE());
                            } else {
                                DatabaseHelper.getInstance().CreateLOAD_PALLET(product_loadPallet);
                            }
//                            return 10 ;

                        } else {
                            DatabaseHelper.getInstance().CreateLOAD_PALLET(product_loadPallet);
//                            return 10 ;
                        }

                    } else if (isLPN == 1) {
                        boolean isExistLPN = false;
                        ArrayList<Product_LoadPallet> product_LoadPallet ;
                        if ((lpn_code != null) && (!lpn_code.equals(""))) {
                            product_LoadPallet = DatabaseHelper.getInstance().getAllProduct_LoadPallet(lpn_code);
                        }else
                        {
                            product_LoadPallet = DatabaseHelper.getInstance().getAllProduct_LoadPallet("");
                        }

                        if (product_LoadPallet.size() > 0) {
                            for (int j = 0; j < product_LoadPallet.size(); j++) {
                                if (product_LoadPallet.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().CreateLOAD_PALLET(product_loadPallet);
//                            return 10 ;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;

    }


    public int synchronizeGETProductByZoneStockTransfer(Context context, String qrcode, String admin, String expDate, String unit,
                                                        String stockDate, int isLPN, String batch_number, String product_code
            , String product_name, String product_cd) {
        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WOI", isLPN, "");

        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        }
        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
//                String pro_CD = jsonobj.getString("_PRODUCT_CODE");
//                if (pro_CD.equals(qrcode)) {
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");

                    int pro_set = 1;
                    Product_StockTransfer productStockTransfer = new Product_StockTransfer();
                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        productStockTransfer.setPRODUCT_CD(product_cd);
                    } else {
                        productStockTransfer.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        productStockTransfer.setPRODUCT_CODE(product_code);
                    } else {
                        productStockTransfer.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        productStockTransfer.setPRODUCT_NAME(product_name);
                    } else {
                        productStockTransfer.setPRODUCT_NAME(pro_name);
                    }
                    productStockTransfer.setQTY(String.valueOf(pro_set));
                    productStockTransfer.setQTY_EA_AVAILABLE(quanity_ea);
                    productStockTransfer.setBATCH_NUMBER(batch_number);
                    productStockTransfer.setCREATE_TIME(getTimeCreate());

                    productStockTransfer.setPOSITION_FROM_CD("");
                    productStockTransfer.setPOSITION_TO_CD("");
                    // putAway.setPOSITION_DESCRIPTION(description);
                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    productStockTransfer.setLPN_TO(lpn_To);
                    productStockTransfer.setLPN_CODE(lpnCode);

                    productStockTransfer.setPOSITION_TO_CODE(positionTo);

                    productStockTransfer.setPOSITION_TO_DESCRIPTION("");

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            productStockTransfer.setSTOCKIN_DATE(stockDate);
                        }
                        productStockTransfer.setEXPIRED_DATE(expDate);
                        productStockTransfer.setUNIT(unit);
                        productStockTransfer.setQTY(String.valueOf(pro_set));

                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ---
                        productStockTransfer.setPOSITION_FROM_CODE(positionFrom);
                        productStockTransfer.setLPN_FROM(lpn_From);
                        productStockTransfer.setPOSITION_FROM_DESCRIPTION("---");
                    } else if (isLPN == 1) {

                        productStockTransfer.setSTOCKIN_DATE(strokinDate);
                        productStockTransfer.setEXPIRED_DATE(exxpiredDate);
                        productStockTransfer.setUNIT(ea_unit);
                        productStockTransfer.setQTY(quanity);

                        productStockTransfer.setPOSITION_FROM_CODE(position_code);
                        productStockTransfer.setLPN_FROM(lpnCode);
                        productStockTransfer.setPOSITION_FROM_DESCRIPTION(description);
                    }

                    if (isLPN == 0) {
                        ArrayList<Product_StockTransfer> product_stockTransfers = DatabaseHelper.getInstance().
                                getoneProduct_StockTransfer(productStockTransfer.getPRODUCT_CD(), expDate, productStockTransfer.getUNIT(), productStockTransfer.getSTOCKIN_DATE(),batch_number);
                        if (product_stockTransfers.size() > 0) {
                            Product_StockTransfer product = product_stockTransfers.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                Product_StockTransfer updateProduct_stockTransfer = product_stockTransfers.get(0);
                                float product_set = Float.parseFloat((product_stockTransfers.get(0).getQTY()));
                                float sl = product_set + 1;
                                product_stockTransfers.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_StockTransfer(updateProduct_stockTransfer, updateProduct_stockTransfer.getAUTOINCREMENT(), updateProduct_stockTransfer.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProduct_stockTransfer.getUNIT(), productStockTransfer.getSTOCKIN_DATE());
                            } else {
                                DatabaseHelper.getInstance().CreateSTOCK_TRANSFER(productStockTransfer);
                            }
//                            return 10;

                        } else {
                            DatabaseHelper.getInstance().CreateSTOCK_TRANSFER(productStockTransfer);
//                            return 10;
                        }

                    } else if (isLPN == 1) {
                        boolean isExistLPN = false;
                        ArrayList<Product_StockTransfer> product_stockTransfer = DatabaseHelper.getInstance().getAllProduct_StockTransfer();
                        if (product_stockTransfer.size() > 0) {
                            for (int j = 0; j < product_stockTransfer.size(); j++) {
                                if (product_stockTransfer.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().CreateSTOCK_TRANSFER(productStockTransfer);
//                            return 10;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;

    }

    public int synchronizeGETProductByZoneRemoveLPN(Context context, String qrcode, String admin, String expDate, String unit, String stockDate,
                                                    int isLPN, String batch_number, String product_code, String product_name, String product_cd) {
        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(qrcode, admin, "WLP", isLPN, "");

        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        }
        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
//                String pro_CD = jsonobj.getString("_PRODUCT_CODE");
//                if (pro_CD.equals(qrcode)) {
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");

                    int pro_set = 1;
                    Product_Remove_LPN productRemoveLpn = new Product_Remove_LPN();

                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        productRemoveLpn.setPRODUCT_CD(product_cd);
                    } else {
                        productRemoveLpn.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        productRemoveLpn.setPRODUCT_CODE(product_code);
                    } else {
                        productRemoveLpn.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        productRemoveLpn.setPRODUCT_NAME(product_name);
                    } else {
                        productRemoveLpn.setPRODUCT_NAME(pro_name);
                    }
                    productRemoveLpn.setQTY(String.valueOf(pro_set));
                    productRemoveLpn.setQTY_EA_AVAILABLE(quanity_ea);
                    productRemoveLpn.setBATCH_NUMBER(batch_number);

                    productRemoveLpn.setPOSITION_FROM_CD(warePosition);
                    productRemoveLpn.setPOSITION_TO_CD(warePosition);
                    // putAway.setPOSITION_DESCRIPTION(description);
                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    productRemoveLpn.setLPN_TO(lpn_To);
                    productRemoveLpn.setLPN_CODE(lpnCode);

                    productRemoveLpn.setPOSITION_TO_CODE(positionTo);

                    productRemoveLpn.setPOSITION_TO_DESCRIPTION("");

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            productRemoveLpn.setSTOCKIN_DATE(stockDate);
                        }
                        productRemoveLpn.setEXPIRED_DATE(expDate);
                        productRemoveLpn.setUNIT(unit);
                        productRemoveLpn.setQTY(String.valueOf(pro_set));

                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ---
                        productRemoveLpn.setPOSITION_FROM_CODE(positionFrom);
                        productRemoveLpn.setLPN_FROM(lpn_From);
                        productRemoveLpn.setPOSITION_FROM_DESCRIPTION("---");
                    } else if (isLPN == 1) {

                        productRemoveLpn.setSTOCKIN_DATE(strokinDate);
                        productRemoveLpn.setEXPIRED_DATE(exxpiredDate);
                        productRemoveLpn.setUNIT(ea_unit);
                        productRemoveLpn.setQTY(quanity);

                        productRemoveLpn.setPOSITION_FROM_CODE(position_code);
                        productRemoveLpn.setLPN_FROM(lpnCode);
                        productRemoveLpn.setPOSITION_FROM_DESCRIPTION(description);

                    }

                    if (isLPN == 0) {
                        ArrayList<Product_Remove_LPN> product_remove_lpn = DatabaseHelper.getInstance().
                                getoneProduct_Remove_LPN(productRemoveLpn.getPRODUCT_CD(), expDate, productRemoveLpn.getUNIT(), productRemoveLpn.getSTOCKIN_DATE(),batch_number);
                        if (product_remove_lpn.size() > 0) {
                            Product_Remove_LPN product = product_remove_lpn.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                Product_Remove_LPN updateProduct_remove_lpn = product_remove_lpn.get(0);
                                float product_set = Float.parseFloat((product_remove_lpn.get(0).getQTY()));
                                float sl = product_set + 1;
                                product_remove_lpn.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_Remove_LPN(updateProduct_remove_lpn, updateProduct_remove_lpn.getAUTOINCREMENT(), updateProduct_remove_lpn.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProduct_remove_lpn.getUNIT(), productRemoveLpn.getSTOCKIN_DATE());
                            } else {
                                DatabaseHelper.getInstance().Create_Remove_LPN(productRemoveLpn);
                            }
//                            return 10 ;

                        } else {
                            DatabaseHelper.getInstance().Create_Remove_LPN(productRemoveLpn);
//                            return 10;
                        }

                    } else if (isLPN == 1) {
                        boolean isExistLPN = false;
                        ArrayList<Product_Remove_LPN> product_remove_lpns = DatabaseHelper.getInstance().getAllProduct_Remove_LPN();
                        if (product_remove_lpns.size() > 0) {
                            for (int j = 0; j < product_remove_lpns.size(); j++) {
                                if (product_remove_lpns.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().Create_Remove_LPN(productRemoveLpn);
//                            return 10;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }

                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;

    }

    public String Check_Position_Same_SLOC(String from_cd , String to_cd ,String type) {
        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return "Vui l??ng ki???m tra k??? n???i m???ng";

        Webservice webService = new Webservice();

        String result = webService.Check_Position_Same_SLOC(from_cd, to_cd, type);
        if(result.equals("1")){
            return "Th??nh C??ng";
        }else{
            return result;
        }

    }

    public int Block_Function_By_Warehouse() {
        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();

        String result = webService.Block_Function_By_Warehouse();
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("-29")) {
            return -29;
        } else if (result.equals("1")) {
            return 1;
        }
        return 1;
    }

    public int synchronizeGETProductByZoneLetDown(Context context, String qrcode, String admin, String expDate, String unit,
                                                  String stockDate, int isLPN, String product_code, String product_name,
                                                  String batch_number, String product_cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();

        String result = webService.GetProductByZone(qrcode, admin, "WLD", isLPN, "");
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);

                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");
                    String suggestionPosition = jsonobj.getString("_Suggest_Position");
                    String suggestionPosition_from = "";
                    String suggestionPosition_to = "";
                    String chuoi[] = suggestionPosition.split("???");
                    if (!suggestionPosition.equals("???")) {
                        if (chuoi.length > 1) {
                            suggestionPosition_from = chuoi[0];
                            suggestionPosition_to = chuoi[1];
                        } else {
                            suggestionPosition_from = chuoi[0];
                        }
                    }


                    int pro_set = 1;
                    ProductLetDown letDown = new ProductLetDown();
                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        letDown.setPRODUCT_CD(product_cd);
                    } else {
                        letDown.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        letDown.setPRODUCT_CODE(product_code);
                    } else {
                        letDown.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        letDown.setPRODUCT_NAME(product_name);
                    } else {
                        letDown.setPRODUCT_NAME(pro_name);
                    }
                    letDown.setQTY_SET_AVAILABLE(String.valueOf(pro_set));
                    letDown.setQTY_EA_AVAILABLE(quanity_ea);
                    letDown.setBATCH_NUMBER(batch_number);
                    letDown.setCREATE_TIME(getTimeCreate());

                    letDown.setPOSITION_FROM_CD("");
                    letDown.setPOSITION_TO_CD("");

                    String positionTo = "---";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    letDown.setLPN_TO(lpn_To);
                    letDown.setLPN_CODE(lpnCode);

                    letDown.setPOSITION_TO_CODE(positionTo);
                    letDown.setPOSITION_TO_DESCRIPTION("");

                    letDown.setSUGGESTION_POSITION(suggestionPosition_from);
                    letDown.setSUGGESTION_POSITION_TO(suggestionPosition_to);

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            letDown.setSTOCKIN_DATE(stockDate);
                        }
                        letDown.setUNIT(unit);
                        letDown.setEXPIRED_DATE(expDate);
                        letDown.setQTY_SET_AVAILABLE(String.valueOf(pro_set));

                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ---
                        letDown.setPOSITION_FROM_CODE(positionFrom);
                        letDown.setLPN_FROM(lpn_From);
                        letDown.setPOSITION_FROM_DESCRIPTION("---");
                    } else if (isLPN == 1) {
                        letDown.setSTOCKIN_DATE(strokinDate);
                        letDown.setUNIT(ea_unit);
                        letDown.setEXPIRED_DATE(exxpiredDate);
                        letDown.setQTY_SET_AVAILABLE(quanity);

                        letDown.setPOSITION_FROM_CODE(position_code);
                        letDown.setLPN_FROM(lpnCode);
                        letDown.setPOSITION_FROM_DESCRIPTION(description);
                    }

                    if (isLPN == 0) {
                        ArrayList<ProductLetDown> product_letdown = DatabaseHelper.getInstance().
                                getoneProductLetDown(letDown.getPRODUCT_CD(), expDate, letDown.getUNIT(), letDown.getSTOCKIN_DATE(),batch_number);
                        if (product_letdown.size() > 0) {
                            ProductLetDown product = product_letdown.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                ProductLetDown updateProductQR = product_letdown.get(0);
                                float product_set = Float.parseFloat((product_letdown.get(0).getQTY_SET_AVAILABLE()));
                                float sl = product_set + 1;
                                product_letdown.get(i).setQTY_SET_AVAILABLE(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_LetDown(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProductQR.getUNIT(), letDown.getSTOCKIN_DATE());
                            } else {
                                DatabaseHelper.getInstance().CreateLetDown(letDown);
                            }
//                            return 10 ;

                        } else {
                            DatabaseHelper.getInstance().CreateLetDown(letDown);
//                            return 10 ;

                        }
                    } else if (isLPN == 1) {
                        boolean isExistLPN = false;
                        ArrayList<ProductLetDown> productLetDown = DatabaseHelper.getInstance().getAllProductLetDown();
                        if (productLetDown.size() > 0) {
                            for (int j = 0; j < productLetDown.size(); j++) {
                                if (productLetDown.get(j).getLPN_CODE().equals(lpnCode)) {
                                    isExistLPN = true;
                                }
                            }
                        }
                        if (isExistLPN == false) {
                            DatabaseHelper.getInstance().CreateLetDown(letDown);
//                            return 10 ;
                        } else {
                            Dialog dialog = new Dialog(context);
                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
                        }
                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeGETProductByZoneTransferUnit(Context context, String qrcode, String admin, String expDate, String unit,
                                                       String stockDate, int isLPN, String product_code, String product_name,
                                                       String batch_number, String product_cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();

        String result = webService.GetProductByZone(qrcode, admin, "WOI", isLPN, "");
        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        } else if (result.equals("-21")) {
            return -21;
        } else if (result.equals("-22")) {
            return -22;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {

                    JSONObject jsonobj = jsonarray.getJSONObject(i);

                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    String position_code = jsonobj.getString("_POSITION_CODE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");
                    String suggestionPosition = jsonobj.getString("_Suggest_Position");
                    String suggestionPosition_from = "";
                    String suggestionPosition_to = "";
                    String chuoi[] = suggestionPosition.split("???");
                    if (!suggestionPosition.equals("???")) {
                        if (chuoi.length > 1) {
                            suggestionPosition_from = chuoi[0];
                            suggestionPosition_to = chuoi[1];
                        } else {
                            suggestionPosition_from = chuoi[0];
                        }
                    }

                    int pro_set = 1;

                    TransferUnitProduct transferUnit = new TransferUnitProduct();

                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        transferUnit.setPRODUCT_CD(product_cd);
                    } else {
                        transferUnit.setPRODUCT_CD(pro_cd);
                    }
                    if ((product_code != null) && (!product_code.equals(""))) {
                        transferUnit.setPRODUCT_CODE(product_code);
                    } else {
                        transferUnit.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        transferUnit.setPRODUCT_NAME(product_name);
                    } else {
                        transferUnit.setPRODUCT_NAME(pro_name);
                    }
                    transferUnit.setQTY(String.valueOf(pro_set));
                    transferUnit.setQTY_EA_AVAILABLE(quanity_ea);
                    transferUnit.setBATCH_NUMBER(batch_number);

                    transferUnit.setPOSITION_FROM_CD(warePosition);
                    transferUnit.setPOSITION_TO_CD(warePosition);
                    transferUnit.setBARCODE(qrcode);

                    String positionTo = "";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    transferUnit.setLPN_TO(lpn_To);
                    transferUnit.setLPN_CODE(lpnCode);

                    transferUnit.setPOSITION_TO_CODE(positionTo);
                    transferUnit.setPOSITION_TO_DESCRIPTION("");

                    transferUnit.setSUGGESTION_POSITION(suggestionPosition_from);
                    transferUnit.setSUGGESTION_POSITION_TO(suggestionPosition_to);


                    if (isLPN == 0) {
                        if (stockDate.equals("---")) {
                            transferUnit.setSTOCKIN_DATE("");
                        } else {
                            transferUnit.setSTOCKIN_DATE(stockDate);
                        }
                        transferUnit.setUNIT(unit);
                        transferUnit.setEXPIRED_DATE(expDate);
                        transferUnit.setQTY(String.valueOf(pro_set));

                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ---
                        transferUnit.setPOSITION_FROM_CODE(positionFrom);
                        transferUnit.setLPN_FROM(lpn_From);
                        transferUnit.setPOSITION_FROM_DESCRIPTION("---");
                    } else if (isLPN == 1) {
                        transferUnit.setSTOCKIN_DATE(strokinDate);
                        transferUnit.setUNIT(ea_unit);
                        transferUnit.setEXPIRED_DATE(exxpiredDate);
                        transferUnit.setQTY(quanity);

                        transferUnit.setPOSITION_FROM_CODE(position_code);
                        transferUnit.setLPN_FROM(lpnCode);
                        transferUnit.setPOSITION_FROM_DESCRIPTION(description);
                    }

                    if (isLPN == 0) {
                        ArrayList<TransferUnitProduct> product_transfer = DatabaseHelper.getInstance().
                                getoneTransferUnitProduct(transferUnit.getPRODUCT_CD(), expDate, transferUnit.getUNIT(), transferUnit.getSTOCKIN_DATE(), batch_number);
                        if (product_transfer.size() > 0) {
                            TransferUnitProduct product = product_transfer.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                TransferUnitProduct updateProductQR = product_transfer.get(0);
                                float product_set = Float.parseFloat((product_transfer.get(0).getQTY()));
                                float sl = product_set + 1;
                                product_transfer.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_TRANSFER_UNIT(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProductQR.getUNIT(), transferUnit.getSTOCKIN_DATE());

                            } else {
                                DatabaseHelper.getInstance().CreateTransferUnit(transferUnit);

                            }
//                            return 10;

                        } else {
                            DatabaseHelper.getInstance().CreateTransferUnit(transferUnit);
//                            return 10;

                        }
                    }
//                    else if (isLPN == 1) {
//                        boolean isExistLPN = false;
//                        ArrayList<TransferUnitProduct> TransferUnitProduct = DatabaseHelper.getInstance().getAllTransferUnitProduct();
//                        if (TransferUnitProduct.size() > 0) {
//                            for (int j = 0; j < TransferUnitProduct.size(); j++) {
//                                if (TransferUnitProduct.get(j).getLPN_CODE().equals(lpnCode)) {
//                                    isExistLPN = true;
//                                }
//                            }
//                        }
//                        if (isExistLPN == false) {
//                            DatabaseHelper.getInstance().CreateTransferUnit(transferUnit);
//                        } else {
//                            Dialog dialog = new Dialog(context);
//                            dialog.showDialog(context, "LPN n??y ???? c?? trong danh s??ch");
//                        }
//                    }
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }
    public int GET_SuggetPosition_MasterPick(String master_cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GET_SuggetPosition_MasterPick(master_cd);
        if (result.equals("-1"))
            return -1;

        if (result.equals("1")) {

            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);

                String product_cd = jsonobj.getString("PRODUCT_CD");
                String product_code = jsonobj.getString("PRODUCT_CODE");
                String product_name = jsonobj.getString("PRODUCT_NAME");
                String position_code  = jsonobj.getString("POSITION_CODE");
                String by_order  = jsonobj.getString("BY_ORDER");
                String so_qty  = jsonobj.getString("SO_QTY");
                String unit_set  = jsonobj.getString("UNIT_SET");
                String dvt_set  = jsonobj.getString("DVT_SET");



                ProductLpnWithSo lpnWithSo = new ProductLpnWithSo();
                lpnWithSo.setPRODUCT_CD(product_cd);
                lpnWithSo.setPRODUCT_CODE(product_code);
                lpnWithSo.setPRODUCT_NAME(product_name);
                lpnWithSo.setPOSITION_CODE(position_code);
                lpnWithSo.setBY_ORDER(by_order);
                lpnWithSo.setSO_QTY(so_qty);
                lpnWithSo.setUNIT_SET(unit_set);
                lpnWithSo.setDVT_SET(dvt_set);

                DatabaseHelper.getInstance().CreateLPNwithSO(lpnWithSo);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }


    public int synchronizeGetLPNwithSO(Context context , String master_cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetParam_LPNwithSO(master_cd);
        if (result.equals("-1"))
            return -1;

        if (result.equals("1")) {

            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);

                String lpn_code = jsonobj.getString("LPN_CODE");
                String lpn_date = jsonobj.getString("LPN_DATE");
                String user_create = jsonobj.getString("USER_CREATE");
                String storage = jsonobj.getString("STORAGE");
                String order_code  = jsonobj.getString("ORDER_CODE");


                LPN lpn = new LPN();
                lpn.setLPN_CODE(lpn_code);
                lpn.setLPN_DATE(lpn_date);
                lpn.setORDER_CODE(order_code);
                lpn.setUSER_CREATE(user_create);
                lpn.setSTORAGE(storage);
                lpn.setLPN_NUMBER(String.valueOf(i + 1));


                DatabaseHelper.getInstance().CreateLPN(lpn);

                DatabaseHelper.getInstance().CreateLPNDate(lpn_date);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }


    public int synchronizeGetLPN(Context context) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetParam_LPN();
        if (result.equals("-1"))
            return -1;

        if (result.equals("1")) {

            return 1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);

                String lpn_code = jsonobj.getString("LPN_CODE");
                String lpn_date = jsonobj.getString("LPN_DATE");
                String user_create = jsonobj.getString("USER_CREATE");
                String storage = jsonobj.getString("STORAGE");


                LPN lpn = new LPN();
                lpn.setLPN_CODE(lpn_code);
                lpn.setLPN_DATE(lpn_date);
                lpn.setUSER_CREATE(user_create);
                lpn.setSTORAGE(storage);
                lpn.setLPN_NUMBER(String.valueOf(i + 1));

                DatabaseHelper.getInstance().CreateLPN(lpn);

                DatabaseHelper.getInstance().CreateLPNDate(lpn_date);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeCreate_LPN(Context context) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.Create_LPN();
        if (result.equals("-1"))
            return -1;

        if (result.equals("1")) {
            return 1;
        }

        return 1;
    }

    public int synchronizeGetProductLetDownSuggest(String adminCode) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductLetDownSuggestion(adminCode);
        if (result.equals("-1"))
            return -1;

        if (result.equals("1")) {

            return 1;
        }
        if (result.equals("[]")) {
            return -1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);

                String product_no = String.valueOf(i + 1);
                String product_code = jsonobj.getString("PRODUCT_CODE");
                String product_name = jsonobj.getString("PRODUCT_NAME");
                String product_amount = jsonobj.getString("QTY_SET_AVAILABLE");
                String product_unit = jsonobj.getString("UNIT");
                String product_position_from = jsonobj.getString("POSITION_CODE");
                String product_position_to = jsonobj.getString("VTGY");
//                String product_exp_date = jsonobj.getString("EXPIRY_DATE");
//                String product_stock_date = jsonobj.getString("STOCKIN_DATE");

                LetDownProductSuggest productSuggest = new LetDownProductSuggest();
                productSuggest.setProductSTT(product_no);
                productSuggest.setProductCode(product_code);
                productSuggest.setProductName(product_name);
                productSuggest.setProductAmount(product_amount);
                productSuggest.setProductUnit(product_unit);
                productSuggest.setProductPositionFrom(product_position_from);
                productSuggest.setProductPositionTo(product_position_to);
//                productSuggest.setProductExpDate(product_exp_date);
//                productSuggest.setProductStockDate(product_stock_date);

                DatabaseHelper.getInstance().insertLetDownSuggest(productSuggest);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeGetProductOfLPN(String barcode, String adminCode, String type, int isLPN, String cd) {

        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();
        String result = webService.GetProductByZone(barcode, adminCode, type, isLPN, cd);
        if (result.equals("-1"))
            return -1;

        if (result.equals("1")) {

            return 1;
        }
        if (result.equals("[]")) {
            return -1;
        }

        try {
            JSONArray jsonarray = new JSONArray(result);

            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????

                JSONObject jsonobj = jsonarray.getJSONObject(i);

                String product_no = String.valueOf(i + 1);
                String product_code = jsonobj.getString("_PRODUCT_CODE");
                String product_name = jsonobj.getString("_PRODUCT_NAME");
                String product_amount = jsonobj.getString("_QTY_SET_AVAILABLE");
                String product_unit = jsonobj.getString("_UNIT");
                String product_current_position = jsonobj.getString("_POSITION_CODE");
                String product_exp_date = jsonobj.getString("_EXPIRY_DATE");
                String product_stock_date = jsonobj.getString("_STOCKIN_DATE");

                LPNProduct lpnProduct = new LPNProduct();
                lpnProduct.setProductCode(product_code);
                lpnProduct.setProductName(product_name);
                lpnProduct.setProductAmount(product_amount);
                lpnProduct.setProductUnit(product_unit);
                lpnProduct.setProductCurrentPosition(product_current_position);
                lpnProduct.setProductExpDate(product_exp_date);
                lpnProduct.setProductStockDate(product_stock_date);

                DatabaseHelper.getInstance().insertLPNProduct(lpnProduct);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public int synchronizeGETProductByZoneInventory(String qrcode, String admin, String expDate, String unit,
                                                    String type, String inventoryCD, String stockDate, int isLPN, String product_code,
                                                    String product_name, String batch_number, String product_cd ,String vitritu ,String fromCd) {
        int status = this.allowSynchronizeBy3G();
        if (status != 1)
            return -1;

        Webservice webService = new Webservice();

        String result = webService.GetProductByZone(qrcode, admin, type, isLPN, inventoryCD);

        if (result.equals("-1")) {
            return -1;
        } else if (result.equals("1")) {
            return 1;
        } else if (result.equals("-8")) {
            return -8;
        } else if (result.equals("-11")) {
            return -11;
        } else if (result.equals("-10")) {
            return -10;
        } else if (result.equals("-12")) {
            return -12;
        } else if (result.equals("-16")) {
            return -16;
        } else if (result.equals("-20")) {
            return -20;
        }


        try {
            JSONArray jsonarray = new JSONArray(result);

            // DatabaseHelper.getInstance().deleteAllRorateTimes();
            for (int i = 0; i < jsonarray.length(); i++) {
                // l???y m???t ?????i t?????ng json ?????
                if (i == 1 && isLPN == 0) {
                    return 1;
                } else {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String pro_code = jsonobj.getString("_PRODUCT_CODE");
                    String pro_cd = jsonobj.getString("_PRODUCT_CD");
                    String pro_name = jsonobj.getString("_PRODUCT_NAME");
                    String quanity = jsonobj.getString("_QTY_SET_AVAILABLE");
                    String quanity_ea = jsonobj.getString("_QTY_EA_AVAILABLE");
                    String exxpiredDate = jsonobj.getString("_EXPIRY_DATE");
                    String strokinDate = jsonobj.getString("_STOCKIN_DATE");
                    String ea_unit = jsonobj.getString("_UNIT");
                    String position_code = jsonobj.getString("_POSITION_CODE");

                    String description = jsonobj.getString("_POSITION_DESCRIPTION");
                    String warePosition = jsonobj.getString("_WAREHOUSE_POSITION_CD");
                    String lpnCode = jsonobj.getString("_LPN_CODE");

                    int pro_set = 1;

                    InventoryProduct inventoryProduct = new InventoryProduct();
                    if ((product_cd != null) && (!product_cd.equals(""))) {
                        inventoryProduct.setPRODUCT_CD(product_cd);
                    } else {
                        inventoryProduct.setPRODUCT_CD(pro_cd);
                    }

                    if ((product_code != null) && (!product_code.equals(""))) {
                        inventoryProduct.setPRODUCT_CODE(product_code);
                    } else {
                        inventoryProduct.setPRODUCT_CODE(pro_code);
                    }

                    if ((product_name != null) && (!product_name.equals(""))) {
                        inventoryProduct.setPRODUCT_NAME(product_name);
                    } else {
                        inventoryProduct.setPRODUCT_NAME(pro_name);
                    }

                    inventoryProduct.setQTY(String.valueOf(pro_set));

                    inventoryProduct.setQTY_EA_AVAILABLE(quanity_ea);
                    inventoryProduct.setBATCH_NUMBER(batch_number);

                    inventoryProduct.setPOSITION_FROM_CD(fromCd);

                    inventoryProduct.setPOSITION_TO_CD("");
                    inventoryProduct.setSTOCK_TAKE_CD(inventoryCD);


                    String positionTo = "";
                    String positionFrom = "---";
                    String lpn_From = "";
                    String lpn_To = "";

                    inventoryProduct.setLPN_TO(lpn_To);
                    inventoryProduct.setLPN_CODE(lpnCode);

                    inventoryProduct.setPOSITION_TO_CODE(positionTo);
                    inventoryProduct.setPOSITION_TO_DESCRIPTION("");

                    if (isLPN == 0) {
                        if (stockDate != null) {
                            inventoryProduct.setSTOCKIN_DATE(stockDate);
                        }
                        inventoryProduct.setEXPIRED_DATE(expDate);
                        inventoryProduct.setUNIT(unit);
                        inventoryProduct.setQTY(String.valueOf(pro_set));

                        // n???u kh??ng ph???i lpn th?? position code s??? tr??? v??? "" v?? g??n m???c ?????nh l?? ---
//                        if(vitritu.contains("-")){
//                            inventoryProduct.setPOSITION_FROM_CODE(vitritu);
//                            inventoryProduct.setLPN_FROM(vitritu);
//                        }else{
                            inventoryProduct.setPOSITION_FROM_CODE(vitritu);
                            inventoryProduct.setLPN_FROM(vitritu);
//                        }
                        inventoryProduct.setPOSITION_FROM_DESCRIPTION(positionTo);
                    } else if (isLPN == 1) {
                        inventoryProduct.setSTOCKIN_DATE(strokinDate);
                        inventoryProduct.setEXPIRED_DATE(exxpiredDate);
                        inventoryProduct.setUNIT(ea_unit);
                        inventoryProduct.setPOSITION_FROM_CODE(position_code);
                        inventoryProduct.setLPN_FROM(lpnCode);
                        inventoryProduct.setPOSITION_FROM_DESCRIPTION(description);
                        inventoryProduct.setQTY(quanity);

                    }

                    if (isLPN == 0) {
                        ArrayList<InventoryProduct> inventoryProducts = DatabaseHelper.getInstance().
                                getoneProduct_Inventory(inventoryProduct.getPRODUCT_CD(), expDate, inventoryProduct.getUNIT(), inventoryCD, stockDate ,vitritu,batch_number);
                        if (inventoryProducts.size() > 0) {
                            InventoryProduct product = inventoryProducts.get(0);
                            if ((expDate.equals(product.getEXPIRED_DATE()) && unit.equals(product.getUNIT()))) {

                                InventoryProduct updateProductQR = inventoryProducts.get(0);
                                float product_set = Float.parseFloat((inventoryProducts.get(0).getQTY()));
                                float sl = product_set + 1;
                                inventoryProducts.get(i).setQTY(String.valueOf(product_set));
                                DatabaseHelper.getInstance().updateProduct_Inventory(updateProductQR, updateProductQR.getAUTOINCREMENT(), updateProductQR.getPRODUCT_CD(),
                                        String.valueOf(sl), updateProductQR.getUNIT(), inventoryProduct.getSTOCKIN_DATE(), inventoryCD);
                            } else {
                                ArrayList<InventoryProduct> getpossition_inventory = DatabaseHelper.getInstance().getposition_Inventory();
                                DatabaseHelper.getInstance().CreateInventory(inventoryProduct);
                                try {
                                    int id_1 = Integer.parseInt(getpossition_inventory.get(0).getAUTOINCREMENT());
                                    if (id_1 >= 1) {
                                        ArrayList<InventoryProduct> getpossition_inventory_2 = DatabaseHelper.getInstance().getautoProduct_Inventory();
                                        String id_3 = getpossition_inventory_2.get(0).getAUTOINCREMENT();

                                        DatabaseHelper.getInstance().updatePositionFrom_Inventory(id_3, getpossition_inventory.get(0).getPOSITION_FROM_CODE(),
                                                getpossition_inventory.get(0).getPOSITION_FROM_CD(), "", "",
                                                getpossition_inventory.get(0).getPOSITION_FROM_DESCRIPTION(), "", "");
                                    }
                                } catch (Exception e) {

                                }


                            }
//                            return 10 ;

                        } else {
                            ArrayList<InventoryProduct> getpossition_inventory = DatabaseHelper.getInstance().getposition_Inventory();
                            DatabaseHelper.getInstance().CreateInventory(inventoryProduct);
//                            try {
//                                int id_1 = Integer.parseInt(getpossition_inventory.get(0).getAUTOINCREMENT());
//                                if (id_1 >= 1) {
//                                    ArrayList<InventoryProduct> getpossition_inventory_2 = DatabaseHelper.getInstance().getautoProduct_Inventory();
//                                    String id_3 = getpossition_inventory_2.get(0).getAUTOINCREMENT();
//
//                                    DatabaseHelper.getInstance().updatePositionFrom_Inventory(id_3, getpossition_inventory.get(0).getPOSITION_FROM_CODE(),
//                                            getpossition_inventory.get(0).getPOSITION_FROM_CD(), "", "",
//                                            getpossition_inventory.get(0).getPOSITION_FROM_DESCRIPTION(), "", "");
//                                }
//                            } catch (Exception e) {
//
//                            }
//                            return 10 ;
                        }
                    } else if (isLPN == 1) {
                        DatabaseHelper.getInstance().CreateInventory(inventoryProduct);
//                        return 10 ;
                    }
                }


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            CmnFns.writeLogError("Exception "
//                    + e.getMessage());
            return -1;
        }

        return 1;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }


}

