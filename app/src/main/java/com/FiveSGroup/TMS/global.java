package com.FiveSGroup.TMS;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class global extends Application {

    // biến lưu trữ tài khoản và mật khẩu để kết nối tới web Service
    // Webservice có áp dụng cơ chế chứng thực nên cần phải có user và mật khẩu
    // để kết nối
    // tham số này được lấy từ Server
    private static String UserNameAuthWebsevice = "";
    private static String PasswordNameAuthWebsevice = "";
    private static String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";

//http:///Webservice/Synchronize.asmx
//    private static String UrlDeliveryCusomerList = "http://grv.fieldvision.com.vn:54573/TMS/DeliveryCustomerList.aspx?deliverer=";

//   private static String UrlDeliveryCusomerList = "http://111.222.3.199:54572/TMS/DeliveryCustomerList.aspx?deliverer=";

//    private static String UrlDeliveryCusomerList = "http://lcd.fieldvision.com.vn:54570/";

//    private static String UrlDeliveryCusomerList = "https://www.24h.com.vn";

//    private static String UrlDeliveryCusomerList = "http://111.222.3.199:54572/TMS/DeliveryConfirm.aspx?orderCD=2";

    //    private static String UrlDeliveryCusomerList = "http://idv.grv.fieldvision.com.vn:54574/TMS/DeliveryCustomerList.aspx?deliverer=";
    private static String UrlDeliveryCusomerList = "";

    private static String CreateWarehousePickListForApp = "";

    public static String getCreateWarehousePickListForApp() {
        return CreateWarehousePickListForApp;
    }

    public static void setCreateWarehousePickListForApp(String createWarehousePickListForApp) {
        CreateWarehousePickListForApp = createWarehousePickListForApp;
    }

    private static String SALE_CODE = null;

    private static String ADMIN_CODE = null;

    public static String getAdminCode() {
        return ADMIN_CODE;
    }

    public static void setAdminCode(String adminCode) {
        ADMIN_CODE = adminCode;
    }

    // 0 mặc định là không có gì trong file text
    private static String IS_ACTIVE = "0";

    private static String IS_ADMIN = "0";

    public static String getIsAdmin() {
        return IS_ADMIN;
    }

    public static void setIsAdmin(String isAdmin) {
        IS_ADMIN = isAdmin;
    }

    // 0 là app đang dừng
    private static String CHECK_APP_STOP = "0";

    // 0 mặc định là chưa được kích hoạt


    public static String getCheckAppStop() {
        return CHECK_APP_STOP;
    }

    public static void setCheckAppStop(String checkAppStop) {
        CHECK_APP_STOP = checkAppStop;
    }

    public static String getIsActive() {
        return IS_ACTIVE;
    }

    public static void setIsActive(String isActive) {
        IS_ACTIVE = isActive;
    }

    public static String getSaleCode() {
        return SALE_CODE;
    }

    public static void setSaleCode(String saleCode) {
        SALE_CODE = saleCode;
    }


    public static String getURLDELIVERYCUSTOMERlIST() {
        return UrlDeliveryCusomerList;
    }

    public static void setURLDELIVERYCUSTOMERlIST(String URLDELIVERYCUSTOMERlIST) {
        global.UrlDeliveryCusomerList = URLDELIVERYCUSTOMERlIST;
    }

    public static String SPLIT_KEY = "≡";

    public static String getUserNameAuthWebsevice() {
        return UserNameAuthWebsevice;
    }

    public static void setUserNameAuthWebsevice(String userNameAuthWebsevice) {
        UserNameAuthWebsevice = userNameAuthWebsevice;
    }

    public static String getPasswordNameAuthWebsevice() {
        return PasswordNameAuthWebsevice;
    }


    public static List<String> lstLogUp = new ArrayList<>();


    public static void setPasswordNameAuthWebsevice(
            String passwordNameAuthWebsevice) {
        PasswordNameAuthWebsevice = passwordNameAuthWebsevice;
    }


    // tham số này xử dụng đẻ lưu đường link đồng bộ dữ liệu về Server
    // được xử dụng ở hàm setAuth (Class: CmnFns.cs)

//    private static String UrlWebserviceToSynchronize = "http://grv.fieldvision.com.vn:54573/Webservice/Synchronize.asmx";

    //  private static String UrlWebserviceToSynchronize = "http://111.222.3.199:54572/Webservice/Synchronize.asmx";
    private  static String STOCK_RECEIPT_CD = "";

    public static String getStockReceiptCd() {
        return STOCK_RECEIPT_CD;
    }

    public static void setStockReceiptCd(String stockReceiptCd) {
        STOCK_RECEIPT_CD = stockReceiptCd;
    }

    private static String UrlWebserviceToSynchronize = "";

    private static String Warehouse_AdjustmentCD = "";


    public static String getWarehouse_AdjustmentCD() {
        return Warehouse_AdjustmentCD;
    }

    public static void setWarehouse_AdjustmentCD(String warehouse_AdjustmentCD) {
        Warehouse_AdjustmentCD = warehouse_AdjustmentCD;
    }
    private static String MasterPickCd = "";

    public static String getMasterPickCd() {
        return MasterPickCd;
    }

    public static void setMasterPickCd(String masterPickCd) {
        MasterPickCd = masterPickCd;
    }

    private static String Hide_Warehouse_Adjustment ="";

    public static String getHide_Warehouse_Adjustment() {
        return Hide_Warehouse_Adjustment;
    }

    public static void setHide_Warehouse_Adjustment(String hide_Warehouse_Adjustment) {
        Hide_Warehouse_Adjustment = hide_Warehouse_Adjustment;
    }
    private static String TransferPostingCD = "";
    private static String ChuyenMaCD = "";
    private static String PickupCD = "";
    private static String QACD = "";
    private static String ReturnQACD = "";

    public static String getReturnQACD() {
        return ReturnQACD;
    }

    public static void setReturnQACD(String returnQACD) {
        ReturnQACD = returnQACD;
    }

    public static String getPickupCD() {
        return PickupCD;
    }

    public static void setPickupCD(String pickupCD) {
        PickupCD = pickupCD;
    }

    public static String getQACD() {
        return QACD;
    }

    public static void setQACD(String QACD) {
        global.QACD = QACD;
    }

    public static String getChuyenMaCD() {
        return ChuyenMaCD;
    }

    public static void setChuyenMaCD(String chuyenMaCD) {
        ChuyenMaCD = chuyenMaCD;
    }

    public static String getTransferPostingCD() {
        return TransferPostingCD;
    }

    public static void setTransferPostingCD(String transferPostingCD) {
        TransferPostingCD = transferPostingCD;
    }

    private static String PoReturnCD = "";

    public static String getPoReturnCD() {
        return PoReturnCD;
    }

    public static void setPoReturnCD(String poReturnCD) {
        PoReturnCD = poReturnCD;
    }

    private static String CancelCD = "";


    public static String getCancelCD() {
        return CancelCD;
    }

    public static void setCancelCD(String cancelCD) {
        CancelCD = cancelCD;
    }

    private static String ReturnCD = "";

    public static String getReturnCD() {
        return ReturnCD;
    }

    public static void setReturnCD(String returnCD) {
        ReturnCD = returnCD;
    }

    private static String StockoutCD = "";

    public static String getStockoutCD() {
        return StockoutCD;
    }

    public static void setStockoutCD(String stockoutCD) {
        StockoutCD = stockoutCD;
    }

    private static String PickListCD = "";

    public static String getPickListCD() {
        return PickListCD;
    }

    public static void setPickListCD(String pickListCD) {
        PickListCD = pickListCD;
    }

    private static String InventoryCD = "";

    public static void setInventoryCD(String inventoryCD) {
        InventoryCD = inventoryCD;
    }

    public static String getInventoryCD() {
        return InventoryCD;
    }

    public static void setUrlWebserviceToSynchronize(String urlWebserviceToSynchronize) {
        UrlWebserviceToSynchronize = urlWebserviceToSynchronize;
    }

    public static String getUrlWebserviceToSynchronize() {

        return UrlWebserviceToSynchronize;
    }

    private static String lpn_code = "";
    private static String order_code = "";

    public static String getLpn_code() {
        return lpn_code;
    }

    public static void setLpn_code(String lpn_code) {
        global.lpn_code = lpn_code;
    }

    public static String getOrder_code() {
        return order_code;
    }

    public static void setOrder_code(String order_code) {
        global.order_code = order_code;
    }

    // mặc định 0 là không được chọn , 1 là được chọn
    public static String IsChooseFrom = "0";
    // mặc định 0 là không được chọn , 1 là được chọn
    public static String IsChooseTo = "0";

    public static String getIsChooseTo() {
        return IsChooseTo;
    }

    public static void setIsChooseTo(String isChooseTo) {
        IsChooseTo = isChooseTo;
    }

    public static String getIsChooseFrom() {
        return IsChooseFrom;
    }

    public static void setIsChooseFrom(String isChooseFrom) {
        IsChooseFrom = isChooseFrom;
    }

    // tham số lấy từ Server
    // thể hiện số kilobyte hình ảnh tối đa được phép chụp nếu vượt quá tham số
    // này thì hệ thống sẽ tự giảm kích thước
    // mục đích để làm nhẹ hệ thống
    private static Boolean AllowSynchroinzeBy3G = true;

    public static Boolean getAllowSynchroinzeBy3G() {
        return AllowSynchroinzeBy3G;
    }

    public static void setAllowSynchroinzeBy3G(Boolean allowSynchroinzeBy3G) {
        AllowSynchroinzeBy3G = allowSynchroinzeBy3G;
    }

    private static int MaxinumKiloByteOfPicture = 1000; // 1MB
    public static String RegexReplaceStringNonASCII = "[^\\x00-\\x7F]"; // regex
    // remove
    // các
    // kí tự
    // đặc
    // biệt





    // biến toàn bộ về Context
    private static Context appContext;
    private static Context appContext1;
    public static  String barcode="";

    public static String getBarcode() {
        return barcode;
    }

    public static void setBarcode(String barcode) {
        global.barcode = barcode;
    }

    public static  String Customercd="";
    public static String getCustomercd() {
        return Customercd;
    }

    public static void setCustomercd(String customercd) {
        Customercd = customercd;
    }

    public static void setAppContext(Context appContext) {
        //global.appContext = appContext;
    }


    public static int getMaxinumKiloByteOfPicture() {
        return MaxinumKiloByteOfPicture;
    }

    public static void setMaxinumKiloByteOfPicture(int maxinumKiloByteOfPicture) {
        MaxinumKiloByteOfPicture = maxinumKiloByteOfPicture;
    }

    public static String getFormatDate() {
        return FORMAT_DATE;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        global.appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return global.appContext;
    }

//    public static String URL_Delivery = DatabaseHelper.getInstance().getParamByKey("URL_Delivery").getValue();
//    public static String URL_PickList = DatabaseHelper.getInstance().getParamByKey("URL_PickList").getValue();
//    public static String URL_PickList_Review=DatabaseHelper.getInstance().getParamByKey("URL_PickList_Review").getValue();
//    public static String URL_Stockin = DatabaseHelper.getInstance().getParamByKey("URL_Stockin").getValue();
//    public static String URL_Stockin_detail = DatabaseHelper.getInstance().getParamByKey("URL_Stockin_detail").getValue();
}
