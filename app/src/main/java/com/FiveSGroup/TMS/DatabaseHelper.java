package com.FiveSGroup.TMS;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.FiveSGroup.TMS.AddCustomerFragment.CCustomer;
import com.FiveSGroup.TMS.ChangeCusFragment.UpdateCustomer;
import com.FiveSGroup.TMS.Inventory.InventoryProduct;
import com.FiveSGroup.TMS.LPN.LPN;
import com.FiveSGroup.TMS.LPN.LPNProduct;
import com.FiveSGroup.TMS.LetDown.LetDownProductSuggest;
import com.FiveSGroup.TMS.LetDown.ProductLetDown;
import com.FiveSGroup.TMS.LoadPallet.Product_LoadPallet;
import com.FiveSGroup.TMS.MasterPick.Product_Master_Pick;
import com.FiveSGroup.TMS.PickList.PickList;
import com.FiveSGroup.TMS.PutAway.Ea_Unit_Tam;
import com.FiveSGroup.TMS.PutAway.Product_PutAway;
import com.FiveSGroup.TMS.RemoveFromLPN.Product_Remove_LPN;
import com.FiveSGroup.TMS.ReturnWareHouse.Product_Return_WareHouse;
import com.FiveSGroup.TMS.StockOut.Product_StockOut;
import com.FiveSGroup.TMS.StockTransfer.Product_StockTransfer;
import com.FiveSGroup.TMS.Warehouse.Exp_Date_Tam;
import com.FiveSGroup.TMS.Warehouse.Product_Qrcode;
import com.FiveSGroup.TMS.Warehouse_Adjustment.Product_Warehouse_Adjustment;
import com.FiveSGroup.TMS.Webservice.CParam;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static DatabaseHelper sInstance = null;

    public static String PWD = "";

    public static DatabaseHelper getInstance() {

        // đoạn code crypt data
        try {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            bis = new BufferedInputStream(new FileInputStream("abc"));
            bos = new BufferedOutputStream(new FileOutputStream("abc", false));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        }

        // tạo password cho database
        if (PWD.equals("")) {
            try {

                int k = 0;
                for (int i = 0; i <= 98; i++) {
                    if (i == 10 || i == 27)
                        k += i * 291;
                    else if (i % 2 == 0)
                        k += i * 10;
                    else if (i % 3 == 0)
                        k += i * 12;
                    else
                        k += i * 88;

                }
//                PWD = k
//                        + global.getAppContext()
//                        .getString(R.string.TextViewPWD) + k;
                PWD = String.valueOf(k);
                //187763Storesnotvisitedbutsold187763
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See sInstance article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(global.getAppContext());
        }

        /*
         * SharedPreferences pref =
         * global.getAppContext().getSharedPreferences("PWD",
         * global.getAppContext().MODE_PRIVATE); String passwd =
         * pref.getString("PWD", null); SharedPreferences.Editor editor =
         * global.getAppContext().getSharedPreferences("PWD",
         * global.getAppContext().MODE_PRIVATE).edit();
         *
         *
         * if (passwd != null) { if( !passwd.equals(global.getPasswordDB()) &&
         * global.getPasswordDB().length() > 0 ) { SQLiteDatabase db =
         * sInstance.getWritableDatabase( CmnFns.encrypt(passwd)); String pwdT =
         * CmnFns.encrypt(global.getPasswordDB()); db.execSQL("PRAGMA key = '" +
         * pwdT + "'"); editor.putString("PWD",global.getPasswordDB());
         * editor.commit(); PWD = pwdT; } else { PWD = CmnFns.encrypt(passwd); }
         * } else { editor.putString("PWD",PWD); SQLiteDatabase db =
         * sInstance.getWritableDatabase( PWD); String pwdT =
         * CmnFns.encrypt(PWD); db.execSQL("PRAGMA key = '" + pwdT + "'"); PWD =
         * pwdT; editor.commit(); }
         */

        return sInstance;
    }

    // Database Version
    public static final int DATABASE_VERSION = 71; // version của DB khi thay
    // đổi cấu trúc DB phải tăng
    // số version lên

    // Database Name
    public static final String DATABASE_NAME = "TMS.db"; // tên DB Sqlite

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);


        SQLiteDatabase.loadLibs(global.getAppContext());


        // TODO Auto-generated constructor stub
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_O_CUSTOMER_NEW);
        db.execSQL(CREATE_TABLE_O_TAKES_PHOTO);
        db.execSQL(CREATE_TABLE_O_SALE_TAKES_PHOTO);
        db.execSQL(CREATE_TABLE_O_SHIP_CHANGE_CUSTOMER);
        db.execSQL(CREATE_TABLE_O_CHANGE_CUST_TAKE_PHOTO);
        db.execSQL(CREATE_TABLE_O_QRCODE);
        db.execSQL(CREATE_TABLE_O_EXP);
        db.execSQL(CREATE_TABLE_O_PUT_AWAY);
        db.execSQL(CREATE_TABLE_O_EA_UNIT);
        db.execSQL(CREATE_TABLE_O_PARAM);
        db.execSQL(CREATE_TABLE_O_LET_DOWN);
        db.execSQL(CREATE_TABLE_O_STOCK_OUT);
        db.execSQL(CREATE_TABLE_O_STOCK_TRANSFER);
        db.execSQL(CREATE_TABLE_O_PICKLIST);
        db.execSQL(CREATE_TABLE_O_LNP);
        db.execSQL(CREATE_TABLE_O_LETDOWN_SUGGEST);
        db.execSQL(CREATE_TABLE_O_LOAD_PALLET);
        db.execSQL(CREATE_TABLE_O_PRODUCT_OF_LPN);
        db.execSQL(CREATE_TABLE_O_INVENTORY);
        db.execSQL(CREATE_TABLE_O_WAREHOUSE_ADJUSTMENT);
        db.execSQL(CREATE_TABLE_O_REMOVE_LPN);
        db.execSQL(CREATE_TABLE_O_RETURN_WAREHOUSE);
        db.execSQL(CREATE_TABLE_O_MASTER_PICK);
        db.execSQL(CREATE_TABLE_O_DATE_LPN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //version DB 64
        try {
            db.execSQL(CREATE_TABLE_O_DATE_LPN);

        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            db.execSQL("ALTER TABLE " + O_QRCODE + " ADD COLUMN  "
                    + WAREHOUSE_POSITION_CD + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            db.execSQL("ALTER TABLE " + O_QRCODE + " ADD COLUMN  "
                    + MANUFACTURING_DATE_WST + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }


        try {
            db.execSQL("ALTER TABLE " + O_STOCK_OUT + " ADD COLUMN  "
                    + WAREHOUSE_POSITION_CD_STOCK_OUT + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            db.execSQL("ALTER TABLE " + O_LET_DOWN + " ADD COLUMN  "
                    + SUGGESTION_POSITION_LETDOWN_TO + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }


    }

    //database from table O_LETDOWN_SUGGEST
    public static final String O_LETDOWN_SUGGEST = "O_LETDOWN_SUGGEST";
    public static final String O_LETDOWN_SUGGEST_NO = "O_LETDOWN_SUGGEST_NO";
    public static final String O_LETDOWN_SUGGEST_CODE = "O_LETDOWN_SUGGEST_CODE";
    public static final String O_LETDOWN_SUGGEST_NAME = "O_LETDOWN_SUGGEST_NAME";
    public static final String O_LETDOWN_SUGGEST_AMOUNT = "O_LETDOWN_SUGGEST_AMOUNT";
    public static final String O_LETDOWN_SUGGEST_UNIT = "O_LETDOWN_SUGGEST_UNIT";
    public static final String O_LETDOWN_SUGGEST_POSITION_FROM = "O_LETDOWN_SUGGEST_POSITION_FROM";
    public static final String O_LETDOWN_SUGGEST_POSITION_TO = "O_LETDOWN_SUGGEST_POSITION_TO";
    public static final String O_LETDOWN_SUGGEST_EXP_DATE = "O_LETDOWN_SUGGEST_EXP_DATE";
    public static final String O_LETDOWN_SUGGEST_STOCK_DATE = "O_LETDOWN_SUGGEST_STOCK_DATE";


    //DATABASE PUT WAREHOUSE_ADJUSTMENT
    public static final String O_WAREHOUSE_ADJUSTMENT = "O_WAREHOUSE_ADJUSTMENT";
    public static final String AUTOINCREMENT_WAREHOUSE_ADJUSTMENT = "AUTOINCREMENT_WAREHOUSE_ADJUSTMENT";
    public static final String PRODUCT_CODE_WAREHOUSE_ADJUSTMENT = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_WAREHOUSE_ADJUSTMENT = "PRODUCT_NAME";
    public static final String PRODUCT_CD_WAREHOUSE_ADJUSTMENT = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_WAREHOUSE_ADJUSTMENT = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_WAREHOUSE_ADJUSTMENT = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_WAREHOUSE_ADJUSTMENT = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_WAREHOUSE_ADJUSTMENT = "STOCKIN_DATE_WAREHOUSE_ADJUSTMENT";
    public static final String EA_UNIT_WAREHOUSE_ADJUSTMENT = "EA_UNIT";
    public static final String POSITION_FROM_WAREHOUSE_ADJUSTMENT = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_WAREHOUSE_ADJUSTMENT = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_WAREHOUSE_ADJUSTMENT = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_WAREHOUSE_ADJUSTMENT = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_WAREHOUSE_ADJUSTMENT = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_WAREHOUSE_ADJUSTMENT = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_WAREHOUSE_ADJUSTMENT = "UNIQUE_CODE";
    public static final String warehouse_Adjustment_CD = "warehouse_Adjustment_CD";
    public static final String LPN_CD_WAREHOUSE_ADJUSTMENT = "LPN_CD_WAREHOUSE_ADJUSTMENT";
    public static final String LPN_CODE_WAREHOUSE_ADJUSTMENT = "LPN_CODE_WAREHOUSE_ADJUSTMENT";
    public static final String LPN_FROM_WAREHOUSE_ADJUSTMENT = "LPN_FROM_WAREHOUSE_ADJUSTMENT";
    public static final String LPN_TO_WAREHOUSE_ADJUSTMENT = "LPN_TO_WAREHOUSE_ADJUSTMENT";

    public static final String CREATE_TABLE_O_WAREHOUSE_ADJUSTMENT = "CREATE TABLE "
            + O_WAREHOUSE_ADJUSTMENT + "("
            + AUTOINCREMENT_WAREHOUSE_ADJUSTMENT + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + PRODUCT_NAME_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + PRODUCT_CODE_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + QTY_EA_AVAILABLE_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + QTY_SET_AVAILABLE_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + EXPIRED_DATE_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + STOCKIN_DATE_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + EA_UNIT_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + POSITION_FROM_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + POSITION_FROM_CODE_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + POSITION_FROM_DESCRIPTION_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + POSITION_TO_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + POSITION_TO_CODE_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + POSITION_TO_DESCRIPTION_WAREHOUSE_ADJUSTMENT + " TEXT,"
            + warehouse_Adjustment_CD + " TEXT,"
            + UNIQUE_CODE_WAREHOUSE_ADJUSTMENT + " TEXT ,"
            + LPN_CD_WAREHOUSE_ADJUSTMENT + " TEXT ,"
            + LPN_CODE_WAREHOUSE_ADJUSTMENT + " TEXT ,"
            + LPN_FROM_WAREHOUSE_ADJUSTMENT + " TEXT ,"
            + LPN_TO_WAREHOUSE_ADJUSTMENT + " TEXT "
            + ")";


    public long CreateWAREHOUSE_ADJUSTMENT(Product_Warehouse_Adjustment warehouse_Adjustment) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(AUTOINCREMENT_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getUNIT());
        values.put(PRODUCT_CODE_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getPRODUCT_NAME());
        values.put(PRODUCT_CD_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getQTY());
        values.put(STOCKIN_DATE_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getEXPIRED_DATE());
        values.put(EA_UNIT_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getUNIT());
        values.put(POSITION_FROM_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getPOSITION_FROM_CD());
        values.put(POSITION_TO_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getPOSITION_TO_DESCRIPTION());
        values.put(warehouse_Adjustment_CD, warehouse_Adjustment.getORDER_CD());
        values.put(LPN_CODE_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getLPN_CODE());
        values.put(LPN_FROM_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getLPN_FROM());
        values.put(LPN_TO_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getLPN_TO());
        // insert row
        long id = db.insert(O_WAREHOUSE_ADJUSTMENT, null, values);
        return id;
    }

    public int updatePositionFrom_warehouse_Adjustment_LPN(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String warehouse_AdjustmentinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_WAREHOUSE_ADJUSTMENT, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_WAREHOUSE_ADJUSTMENT, descreption);
        values.put(POSITION_FROM_CODE_WAREHOUSE_ADJUSTMENT, from);
        values.put(LPN_FROM_WAREHOUSE_ADJUSTMENT, from);


        // updating row
        return db.update(O_WAREHOUSE_ADJUSTMENT, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_WAREHOUSE_ADJUSTMENT + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(warehouse_AdjustmentinDate)});

    }

    public int updatePositionFrom_warehouse_Adjustment(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String warehouse_AdjustmentinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_WAREHOUSE_ADJUSTMENT, wareHouse);
        values.put(POSITION_FROM_CODE_WAREHOUSE_ADJUSTMENT, from);
        values.put(LPN_FROM_WAREHOUSE_ADJUSTMENT, "");
        values.put(POSITION_FROM_DESCRIPTION_WAREHOUSE_ADJUSTMENT, descreption);


        // updating row
        return db.update(O_WAREHOUSE_ADJUSTMENT, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_WAREHOUSE_ADJUSTMENT + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(warehouse_AdjustmentinDate)});

    }

    public int updatePositionTo_warehouse_Adjustment_LPN(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String warehouse_AdjustmentinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_WAREHOUSE_ADJUSTMENT, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_WAREHOUSE_ADJUSTMENT, descreption);
        values.put(LPN_TO_WAREHOUSE_ADJUSTMENT, to);

        values.put(POSITION_TO_CODE_WAREHOUSE_ADJUSTMENT, to);
        // updating row
        return db.update(O_WAREHOUSE_ADJUSTMENT, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_WAREHOUSE_ADJUSTMENT + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(warehouse_AdjustmentinDate)});


    }


    public int updatePositionTo_warehouse_Adjustment(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String warehouse_AdjustmentinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_WAREHOUSE_ADJUSTMENT, wareHouse);
        values.put(POSITION_TO_CODE_WAREHOUSE_ADJUSTMENT, to);

        values.put(POSITION_TO_DESCRIPTION_WAREHOUSE_ADJUSTMENT, descreption);
        // updating row
        return db.update(O_WAREHOUSE_ADJUSTMENT, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_WAREHOUSE_ADJUSTMENT + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(warehouse_AdjustmentinDate)});


    }


    public ArrayList<Product_Warehouse_Adjustment>
    getoneProduct_Warehouse_Adjustment(String CD, String expDate, String ea_unit, String warehouse_AdjustmentinDate, String warehouse_Adjustment_cd) {
        ArrayList<Product_Warehouse_Adjustment> warehouse_Adjustments = new ArrayList<Product_Warehouse_Adjustment>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_WAREHOUSE_ADJUSTMENT + " " + " WHERE "
                + PRODUCT_CD_WAREHOUSE_ADJUSTMENT + " = " + CD + " AND "
                + warehouse_Adjustment_CD + " = " + warehouse_Adjustment_cd + " AND "
                + EA_UNIT_WAREHOUSE_ADJUSTMENT + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_WAREHOUSE_ADJUSTMENT + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_WAREHOUSE_ADJUSTMENT + " like " + " '%" + warehouse_AdjustmentinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_Warehouse_Adjustment warehouse_Adjustment = new Product_Warehouse_Adjustment();
                warehouse_Adjustment.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustments.add(warehouse_Adjustment);
            } while (c.moveToNext());
        }

        c.close();
        return warehouse_Adjustments;
    }


    public ArrayList<Product_Warehouse_Adjustment>
    getAllProduct_Warehouse_Adjustment_Sync(String warehouse_Adjustment_cd) {
        ArrayList<Product_Warehouse_Adjustment> warehouse_Adjustments = new ArrayList<Product_Warehouse_Adjustment>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_WAREHOUSE_ADJUSTMENT + " where " + warehouse_Adjustment_CD + " = " + warehouse_Adjustment_cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Warehouse_Adjustment warehouse_Adjustment = new Product_Warehouse_Adjustment();

                warehouse_Adjustment.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setORDER_CD((c.getString(c
                        .getColumnIndex(warehouse_Adjustment_CD))));
                warehouse_Adjustment.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustment.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_WAREHOUSE_ADJUSTMENT))));
                warehouse_Adjustments.add(warehouse_Adjustment);
            } while (c.moveToNext());
        }

        c.close();
        return warehouse_Adjustments;
    }


    public int updateProduct_Warehouse_Adjustment(Product_Warehouse_Adjustment warehouse_Adjustment, String PRODUCT_CD, String sl, String ea_unit, String warehouse, String warehouse_Adjustment_cd) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_WAREHOUSE_ADJUSTMENT, PRODUCT_CD);
        values.put(PRODUCT_CODE_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getEXPIRED_DATE());
        values.put(EA_UNIT_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getUNIT());
        values.put(QTY_SET_AVAILABLE_WAREHOUSE_ADJUSTMENT, sl);
        values.put(warehouse_Adjustment_CD, warehouse_Adjustment_cd);

        // updating row
        return db.update(O_WAREHOUSE_ADJUSTMENT, values, PRODUCT_CD_WAREHOUSE_ADJUSTMENT + " = ?" + " AND " + EXPIRED_DATE_WAREHOUSE_ADJUSTMENT + " = ?"
                        + " AND " + EA_UNIT_WAREHOUSE_ADJUSTMENT + " = ?" + " AND " + STOCKIN_DATE_WAREHOUSE_ADJUSTMENT + " = ?" + " AND " + warehouse_Adjustment_CD + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(warehouse_Adjustment.getEXPIRED_DATE()), String.valueOf(ea_unit), String.valueOf(warehouse), warehouse_Adjustment_cd});

    }


    public void deleteProduct_Warehouse_Adjustment() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_WAREHOUSE_ADJUSTMENT);
    }

    //Done table O_Warehouse_Adjustment

    public static final String CREATE_TABLE_O_LETDOWN_SUGGEST = "CREATE TABLE "
            + O_LETDOWN_SUGGEST
            + " ("
            + O_LETDOWN_SUGGEST_NO + " TEXT, "
            + O_LETDOWN_SUGGEST_CODE + " TEXT, "
            + O_LETDOWN_SUGGEST_NAME + " TEXT, "
            + O_LETDOWN_SUGGEST_AMOUNT + " TEXT, "
            + O_LETDOWN_SUGGEST_UNIT + " TEXT, "
            + O_LETDOWN_SUGGEST_POSITION_FROM + " TEXT, "
            + O_LETDOWN_SUGGEST_POSITION_TO + " TEXT "
//            + O_LETDOWN_SUGGEST_EXP_DATE + " TEXT, "
//            + O_LETDOWN_SUGGEST_STOCK_DATE + " TEXT"
            + ")";

    public ArrayList<LetDownProductSuggest> getAllLetDownProductSuggest() {
        ArrayList<LetDownProductSuggest> productSuggests = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_LETDOWN_SUGGEST;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                LetDownProductSuggest productSuggest = new LetDownProductSuggest();
                productSuggest.setProductSTT((c.getString(c
                        .getColumnIndex(O_LETDOWN_SUGGEST_NO))));
                productSuggest.setProductCode((c.getString(c
                        .getColumnIndex(O_LETDOWN_SUGGEST_CODE))));
                productSuggest.setProductName((c.getString(c
                        .getColumnIndex(O_LETDOWN_SUGGEST_NAME))));
                productSuggest.setProductAmount((c.getString(c
                        .getColumnIndex(O_LETDOWN_SUGGEST_AMOUNT))));
                productSuggest.setProductUnit((c.getString(c
                        .getColumnIndex(O_LETDOWN_SUGGEST_UNIT))));
                productSuggest.setProductPositionFrom((c.getString(c
                        .getColumnIndex(O_LETDOWN_SUGGEST_POSITION_FROM))));
                productSuggest.setProductPositionTo((c.getString(c
                        .getColumnIndex(O_LETDOWN_SUGGEST_POSITION_TO))));
//                productSuggest.setProductExpDate((c.getString(c
//                        .getColumnIndex(O_LETDOWN_SUGGEST_EXP_DATE))));
//                productSuggest.setProductStockDate((c.getString(c
//                        .getColumnIndex(O_LETDOWN_SUGGEST_STOCK_DATE))));

                productSuggests.add(productSuggest);
            } while (c.moveToNext());
        }
        c.close();
        return productSuggests;
    }

    // insert O_LETDOWN_SUGGEST
    public long insertLetDownSuggest(LetDownProductSuggest productSuggest) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(O_LETDOWN_SUGGEST_NO, productSuggest.getProductSTT());
        values.put(O_LETDOWN_SUGGEST_CODE, productSuggest.getProductCode());
        values.put(O_LETDOWN_SUGGEST_NAME, productSuggest.getProductName());
        values.put(O_LETDOWN_SUGGEST_AMOUNT, productSuggest.getProductAmount());
        values.put(O_LETDOWN_SUGGEST_UNIT, productSuggest.getProductUnit());
        values.put(O_LETDOWN_SUGGEST_POSITION_FROM, productSuggest.getProductPositionFrom());
        values.put(O_LETDOWN_SUGGEST_POSITION_TO, productSuggest.getProductPositionTo());
//        values.put(O_LETDOWN_SUGGEST_EXP_DATE, productSuggest.getProductExpDate());
//        values.put(O_LETDOWN_SUGGEST_STOCK_DATE, productSuggest.getProductStockDate());
        // insert row
        long id = db.insert(O_LETDOWN_SUGGEST, null, values);
        return id;
    }

    // delete O_LETDOWN_SUGGEST
    public void deleteLetDownSuggest() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_LETDOWN_SUGGEST);
    }

    //database from table O_PRODUCT_OF_LPN
    public static final String O_PRODUCT_OF_LPN = "O_PRODUCT_OF_LPN";
    public static final String PRODUCT_OF_LPN_CODE = "PRODUCT_OF_LPN_CODE";
    public static final String PRODUCT_OF_LPN_NAME = "PRODUCT_OF_LPN_NAME";
    public static final String PRODUCT_OF_LPN_AMOUNT = "PRODUCT_OF_LPN_AMOUNT";
    public static final String PRODUCT_OF_LPN_UNIT = "PRODUCT_OF_LPN_UNIT";
    public static final String PRODUCT_OF_LPN_CURRENT_POSITION = "PRODUCT_OF_LPN_CURRENT_POSITION";
    public static final String PRODUCT_OF_LPN_EXP_DATE = "PRODUCT_OF_LPN_EXP_DATE";
    public static final String PRODUCT_OF_LPN_STOCK_DATE = "PRODUCT_OF_LPN_STOCK_DATE";

    public static final String CREATE_TABLE_O_PRODUCT_OF_LPN = "CREATE TABLE "
            + O_PRODUCT_OF_LPN
            + " ("
            + PRODUCT_OF_LPN_CODE + " TEXT, "
            + PRODUCT_OF_LPN_NAME + " TEXT, "
            + PRODUCT_OF_LPN_AMOUNT + " TEXT, "
            + PRODUCT_OF_LPN_UNIT + " TEXT, "
            + PRODUCT_OF_LPN_CURRENT_POSITION + " TEXT, "
            + PRODUCT_OF_LPN_EXP_DATE + " TEXT, "
            + PRODUCT_OF_LPN_STOCK_DATE + " TEXT"
            + ")";

    public ArrayList<LPNProduct> getAllLPNProduct() {
        ArrayList<LPNProduct> lpnProducts = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_PRODUCT_OF_LPN;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                LPNProduct lpnProduct = new LPNProduct();
                lpnProduct.setProductCode((c.getString(c
                        .getColumnIndex(PRODUCT_OF_LPN_CODE))));
                lpnProduct.setProductName((c.getString(c
                        .getColumnIndex(PRODUCT_OF_LPN_NAME))));
                lpnProduct.setProductAmount((c.getString(c
                        .getColumnIndex(PRODUCT_OF_LPN_AMOUNT))));
                lpnProduct.setProductUnit((c.getString(c
                        .getColumnIndex(PRODUCT_OF_LPN_UNIT))));
                lpnProduct.setProductCurrentPosition((c.getString(c
                        .getColumnIndex(PRODUCT_OF_LPN_CURRENT_POSITION))));
                lpnProduct.setProductExpDate((c.getString(c
                        .getColumnIndex(PRODUCT_OF_LPN_EXP_DATE))));
                lpnProduct.setProductStockDate((c.getString(c
                        .getColumnIndex(PRODUCT_OF_LPN_STOCK_DATE))));

                lpnProducts.add(lpnProduct);
            } while (c.moveToNext());
        }
        c.close();
        return lpnProducts;
    }

    // insert O_LETDOWN_SUGGEST
    public long insertLPNProduct(LPNProduct lpnProduct) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(PRODUCT_OF_LPN_CODE, lpnProduct.getProductCode());
        values.put(PRODUCT_OF_LPN_NAME, lpnProduct.getProductName());
        values.put(PRODUCT_OF_LPN_AMOUNT, lpnProduct.getProductAmount());
        values.put(PRODUCT_OF_LPN_UNIT, lpnProduct.getProductUnit());
        values.put(PRODUCT_OF_LPN_CURRENT_POSITION, lpnProduct.getProductCurrentPosition());
        values.put(PRODUCT_OF_LPN_EXP_DATE, lpnProduct.getProductExpDate());
        values.put(PRODUCT_OF_LPN_STOCK_DATE, lpnProduct.getProductStockDate());
        // insert row
        long id = db.insert(O_PRODUCT_OF_LPN, null, values);
        return id;
    }

    // delete O_LETDOWN_SUGGEST
    public void deleteLPNProduct() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_PRODUCT_OF_LPN);
    }
    //database from table O_DATE_LPN
    public static final String O_DATE_LPN = "O_DATE_LPN";
    public static final String LPN_DATE_TEMP = "LPN_DATE_TEMP";

    public static final String CREATE_TABLE_O_DATE_LPN= "CREATE TABLE "
            + O_DATE_LPN + "("
            + LPN_DATE_TEMP + " TEXT" + ")";

    public long CreateLPNDate(String date) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(LPN_DATE_TEMP, date);
        // insert row
        long id = db.insert(O_DATE_LPN, null, values);
        return id;
    }

    public ArrayList<String>
    getAllDatesLPN() {
        ArrayList<String> dates = new ArrayList<String>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT DISTINCT * FROM " + O_DATE_LPN;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                dates.add((c.getString(c
                        .getColumnIndex(LPN_DATE_TEMP))));
            } while (c.moveToNext());
        }
        c.close();
        return dates;
    }

    //database from table O_LPN
    public static final String O_LPN = "O_LPN";
    public static final String LPN_CODE = "LPN_CODE";
    public static final String LPN_DATE = "LPN_DATE";
    public static final String LPN_NUMBER = "LPN_NUMBER";


    public static final String CREATE_TABLE_O_LNP = "CREATE TABLE "
            + O_LPN + "("
            + LPN_NUMBER + " TEXT,"
            + LPN_CODE + " TEXT,"
            + LPN_DATE + " TEXT" + ")";



    public long CreateLPN(LPN lpn) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(LPN_CODE, lpn.getLPN_CODE());
        values.put(LPN_DATE, lpn.getLPN_DATE());
        values.put(LPN_NUMBER, lpn.getLPN_NUMBER());
        // insert row
        long id = db.insert(O_LPN, null, values);
        return id;
    }
    public long CreateDateLPN(LPN lpn) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(LPN_CODE, lpn.getLPN_CODE());
        values.put(LPN_DATE, lpn.getLPN_DATE());
        values.put(LPN_NUMBER, lpn.getLPN_NUMBER());
        // insert row
        long id = db.insert(O_LPN, null, values);
        return id;
    }

    public ArrayList<LPN>
    getAllLpnBarcode(String barcode) {
        ArrayList<LPN> lpn = new ArrayList<LPN>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_LPN + " WHERE " + LPN_CODE + " like '%" + barcode + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                LPN lpn1 = new LPN();
                lpn1.setLPN_NUMBER((c.getString(c
                        .getColumnIndex(LPN_NUMBER))));
                lpn1.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE))));
                lpn1.setLPN_DATE((c.getString(c
                        .getColumnIndex(LPN_DATE))));
                lpn.add(lpn1);
            } while (c.moveToNext());
        }
        c.close();
        return lpn;
    }

    public ArrayList<LPN>
    getAllLpn(String lpn_date) {
        ArrayList<LPN> lpn = new ArrayList<LPN>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_LPN + " WHERE " + LPN_DATE + " like '%" + lpn_date + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                LPN lpn1 = new LPN();
                lpn1.setLPN_NUMBER((c.getString(c
                        .getColumnIndex(LPN_NUMBER))));
                lpn1.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE))));
                lpn1.setLPN_DATE((c.getString(c
                        .getColumnIndex(LPN_DATE))));
                lpn.add(lpn1);
            } while (c.moveToNext());
        }
        c.close();
        return lpn;
    }

    public ArrayList<LPN>
    getAllLpn() {
        ArrayList<LPN> lpn = new ArrayList<LPN>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_LPN;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                LPN lpn1 = new LPN();
                lpn1.setLPN_NUMBER((c.getString(c
                        .getColumnIndex(LPN_NUMBER))));
                lpn1.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE))));
                lpn1.setLPN_DATE((c.getString(c
                        .getColumnIndex(LPN_DATE))));
                lpn.add(lpn1);
            } while (c.moveToNext());
        }
        c.close();
        return lpn;
    }
    public ArrayList<LPN>
    getAllLpn_Distinct() {
        ArrayList<LPN> lpn = new ArrayList<LPN>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT DISTINCT LPN_DATE FROM " + O_LPN;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                LPN lpn1 = new LPN();
                lpn1.setLPN_DATE((c.getString(c
                        .getColumnIndex(LPN_DATE))));
                lpn.add(lpn1);
            } while (c.moveToNext());
        }
        c.close();
        return lpn;
    }
    public void deleteallProduct_LPN() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_LPN);
    }

    //DATABASE PUT return_warehouse_OUT
    public static final String O_RETURN_WAREHOUSE = "O_RETURN_WAREHOUSE";
    public static final String AUTOINCREMENT_RETURN_WAREHOUSE  = "AUTOINCREMENT_RETURN_WAREHOUSE";
    public static final String PRODUCT_CODE_RETURN_WAREHOUSE = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_RETURN_WAREHOUSE = "PRODUCT_NAME";
    public static final String PRODUCT_CD_RETURN_WAREHOUSE = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_RETURN_WAREHOUSE = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_RETURN_WAREHOUSE = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_RETURN_WAREHOUSE = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_RETURN_WAREHOUSE = "STOCKIN_DATE";
    public static final String EA_UNIT_RETURN_WAREHOUSE = "EA_UNIT";
    public static final String POSITION_FROM_RETURN_WAREHOUSE = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_RETURN_WAREHOUSE = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_RETURN_WAREHOUSE = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_RETURN_WAREHOUSE = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_RETURN_WAREHOUSE = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_RETURN_WAREHOUSE = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_RETURN_WAREHOUSE = "UNIQUE_CODE";
    public static final String RETURN_CD = "RETURN_CD";
    public static final String LPN_CD_RETURN_WAREHOUSE = "LPN_CD_RETURN_WAREHOUSE";
    public static final String LPN_CODE_RETURN_WAREHOUSE = "LPN_CODE_RETURN_WAREHOUSE";
    public static final String LPN_FROM_RETURN_WAREHOUSE = "LPN_FROM_RETURN_WAREHOUSE";
    public static final String LPN_TO_RETURN_WAREHOUSE = "LPN_TO_RETURN_WAREHOUSE";

    public static final String CREATE_TABLE_O_RETURN_WAREHOUSE = "CREATE TABLE "
            + O_RETURN_WAREHOUSE + "("
            + AUTOINCREMENT_RETURN_WAREHOUSE + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_RETURN_WAREHOUSE + " TEXT,"
            + PRODUCT_NAME_RETURN_WAREHOUSE + " TEXT,"
            + PRODUCT_CODE_RETURN_WAREHOUSE + " TEXT,"
            + QTY_EA_AVAILABLE_RETURN_WAREHOUSE + " TEXT,"
            + QTY_SET_AVAILABLE_RETURN_WAREHOUSE + " TEXT,"
            + EXPIRED_DATE_RETURN_WAREHOUSE + " TEXT,"
            + STOCKIN_DATE_RETURN_WAREHOUSE + " TEXT,"
            + EA_UNIT_RETURN_WAREHOUSE + " TEXT,"
            + POSITION_FROM_RETURN_WAREHOUSE + " TEXT,"
            + POSITION_FROM_CODE_RETURN_WAREHOUSE + " TEXT,"
            + POSITION_FROM_DESCRIPTION_RETURN_WAREHOUSE + " TEXT,"
            + POSITION_TO_RETURN_WAREHOUSE + " TEXT,"
            + POSITION_TO_CODE_RETURN_WAREHOUSE + " TEXT,"
            + POSITION_TO_DESCRIPTION_RETURN_WAREHOUSE + " TEXT,"
            + RETURN_CD + " TEXT,"
            + UNIQUE_CODE_RETURN_WAREHOUSE + " TEXT ,"
            + LPN_CD_RETURN_WAREHOUSE + " TEXT ,"
            + LPN_CODE_RETURN_WAREHOUSE + " TEXT ,"
            + LPN_FROM_RETURN_WAREHOUSE + " TEXT ,"
            + LPN_TO_RETURN_WAREHOUSE + " TEXT "
            + ")";


    public long Createreturn_warehouse_OUT(Product_Return_WareHouse returnWarehouse) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(AUTOINCREMENT_RETURN_WAREHOUSE,returnWarehouse.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_RETURN_WAREHOUSE, returnWarehouse.getUNIT());
        values.put(PRODUCT_CODE_RETURN_WAREHOUSE, returnWarehouse.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_RETURN_WAREHOUSE, returnWarehouse.getPRODUCT_NAME());
        values.put(PRODUCT_CD_RETURN_WAREHOUSE, returnWarehouse.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_RETURN_WAREHOUSE, returnWarehouse.getQTY());
        values.put(STOCKIN_DATE_RETURN_WAREHOUSE, returnWarehouse.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_RETURN_WAREHOUSE, returnWarehouse.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_RETURN_WAREHOUSE, returnWarehouse.getEXPIRED_DATE());
        values.put(EA_UNIT_RETURN_WAREHOUSE, returnWarehouse.getUNIT());
        values.put(POSITION_FROM_RETURN_WAREHOUSE, returnWarehouse.getPOSITION_FROM_CD());
        values.put(POSITION_TO_RETURN_WAREHOUSE, returnWarehouse.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_RETURN_WAREHOUSE, returnWarehouse.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_RETURN_WAREHOUSE, returnWarehouse.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_RETURN_WAREHOUSE, returnWarehouse.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_RETURN_WAREHOUSE, returnWarehouse.getPOSITION_TO_DESCRIPTION());
        values.put(RETURN_CD, returnWarehouse.getRETURN_CD());
        values.put(LPN_CODE_RETURN_WAREHOUSE, returnWarehouse.getLPN_CODE());
        values.put(LPN_FROM_RETURN_WAREHOUSE, returnWarehouse.getLPN_FROM());
        values.put(LPN_TO_RETURN_WAREHOUSE, returnWarehouse.getLPN_TO());
        // insert row
        long id = db.insert(O_RETURN_WAREHOUSE, null, values);
        return id;
    }

    public int updatePositionFrom_returnWarehouse_LPN(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String return_warehouseinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_RETURN_WAREHOUSE, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_RETURN_WAREHOUSE, descreption);
        values.put(POSITION_FROM_CODE_RETURN_WAREHOUSE, from);
        values.put(LPN_FROM_RETURN_WAREHOUSE, from);


        // updating row
        return db.update(O_RETURN_WAREHOUSE, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_RETURN_WAREHOUSE + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(return_warehouseinDate)});

    }

    public int updatePositionFrom_returnWarehouse(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String return_warehouseinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_RETURN_WAREHOUSE, wareHouse);
        values.put(POSITION_FROM_CODE_RETURN_WAREHOUSE, from);
        values.put(LPN_FROM_RETURN_WAREHOUSE, "");
        values.put(POSITION_FROM_DESCRIPTION_RETURN_WAREHOUSE, descreption);


        // updating row
        return db.update(O_RETURN_WAREHOUSE, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_RETURN_WAREHOUSE + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(return_warehouseinDate)});

    }

    public int updatePositionTo_returnWarehouse_LPN(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String return_warehouseinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_RETURN_WAREHOUSE, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_RETURN_WAREHOUSE, descreption);
        values.put(LPN_TO_RETURN_WAREHOUSE, to);
        values.put(POSITION_TO_CODE_RETURN_WAREHOUSE, to);

        // updating row
        return db.update(O_RETURN_WAREHOUSE, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_RETURN_WAREHOUSE + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(return_warehouseinDate)});


    }


    public int updatePositionTo_returnWarehouse(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String return_warehouseinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_RETURN_WAREHOUSE, wareHouse);
        values.put(POSITION_TO_CODE_RETURN_WAREHOUSE, to);
        values.put(LPN_TO_RETURN_WAREHOUSE, "");

        values.put(POSITION_TO_DESCRIPTION_RETURN_WAREHOUSE, descreption);
        // updating row
        return db.update(O_RETURN_WAREHOUSE, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_RETURN_WAREHOUSE + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(return_warehouseinDate)});


    }


    public ArrayList<Product_Return_WareHouse>
    getoneProduct_Return_WareHouse(String CD, String expDate, String ea_unit, String return_warehouseinDate, String returnWarehouse_cd) {
        ArrayList<Product_Return_WareHouse> returnWarehouses = new ArrayList<Product_Return_WareHouse>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_RETURN_WAREHOUSE + " " + " WHERE "
                + PRODUCT_CD_RETURN_WAREHOUSE + " = " + CD + " AND "
                + RETURN_CD + " = " + returnWarehouse_cd + " AND "
                + EA_UNIT_RETURN_WAREHOUSE + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_RETURN_WAREHOUSE + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_RETURN_WAREHOUSE + " like " + " '%" + return_warehouseinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_Return_WareHouse returnWarehouse = new Product_Return_WareHouse();
                returnWarehouse.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_RETURN_WAREHOUSE))));
                returnWarehouse.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_RETURN_WAREHOUSE))));
                returnWarehouse.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_RETURN_WAREHOUSE))));
                returnWarehouse.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_RETURN_WAREHOUSE))));
                returnWarehouse.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_RETURN_WAREHOUSE))));
                returnWarehouse.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_RETURN_WAREHOUSE))));
                returnWarehouse.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_RETURN_WAREHOUSE))));
                returnWarehouse.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_RETURN_WAREHOUSE))));
                returnWarehouse.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_RETURN_WAREHOUSE))));

                returnWarehouses.add(returnWarehouse);
            } while (c.moveToNext());
        }

        c.close();
        return returnWarehouses;
    }


    public ArrayList<Product_Return_WareHouse>
    getAllProduct_Return_WareHouse_Sync(String returnWarehouse_cd) {
        ArrayList<Product_Return_WareHouse> returnWarehouses = new ArrayList<Product_Return_WareHouse>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_RETURN_WAREHOUSE + " where " + RETURN_CD + " = " + returnWarehouse_cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Return_WareHouse return_warehouse = new Product_Return_WareHouse();

                return_warehouse.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_RETURN_WAREHOUSE))));
                return_warehouse.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_RETURN_WAREHOUSE))));
                return_warehouse.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_RETURN_WAREHOUSE))));
                return_warehouse.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_RETURN_WAREHOUSE))));
                return_warehouse.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_RETURN_WAREHOUSE))));
                return_warehouse.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_RETURN_WAREHOUSE))));
                return_warehouse.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_RETURN_WAREHOUSE))));
                return_warehouse.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_RETURN_WAREHOUSE))));
                return_warehouse.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_RETURN_WAREHOUSE))));
                return_warehouse.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_RETURN_WAREHOUSE))));
                return_warehouse.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_RETURN_WAREHOUSE))));
                return_warehouse.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_RETURN_WAREHOUSE))));
                return_warehouse.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_RETURN_WAREHOUSE))));
                return_warehouse.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_RETURN_WAREHOUSE))));
                return_warehouse.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_RETURN_WAREHOUSE))));
                return_warehouse.setRETURN_CD((c.getString(c
                        .getColumnIndex(RETURN_CD))));
                return_warehouse.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_RETURN_WAREHOUSE))));
                return_warehouse.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_RETURN_WAREHOUSE))));
                return_warehouse.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_RETURN_WAREHOUSE))));
                returnWarehouses.add(return_warehouse);
            } while (c.moveToNext());
        }

        c.close();
        return returnWarehouses;
    }

    public ArrayList<Product_Return_WareHouse>
    getAllProduct_Return_WareHouse(String returnWarehouse_cd) {
        ArrayList<Product_Return_WareHouse> returnWarehouses = new ArrayList<Product_Return_WareHouse>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_RETURN_WAREHOUSE + " where " + RETURN_CD + " = " + returnWarehouse_cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Return_WareHouse return_warehouse = new Product_Return_WareHouse();
                return_warehouse.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_RETURN_WAREHOUSE))));
                return_warehouse.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_RETURN_WAREHOUSE))));
                return_warehouse.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_RETURN_WAREHOUSE))));
                return_warehouse.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_RETURN_WAREHOUSE))));
                return_warehouse.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_RETURN_WAREHOUSE))));
                return_warehouse.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_RETURN_WAREHOUSE))));
                return_warehouse.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_RETURN_WAREHOUSE))));
                return_warehouse.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_RETURN_WAREHOUSE))));
                return_warehouse.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_RETURN_WAREHOUSE))));
                return_warehouse.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_RETURN_WAREHOUSE))));
                return_warehouse.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_RETURN_WAREHOUSE))));
                return_warehouse.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_RETURN_WAREHOUSE))));
                return_warehouse.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_RETURN_WAREHOUSE))));
                return_warehouse.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_RETURN_WAREHOUSE))));
                return_warehouse.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_RETURN_WAREHOUSE))));
                return_warehouse.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_RETURN_WAREHOUSE))));
                return_warehouse.setRETURN_CD((c.getString(c
                        .getColumnIndex(RETURN_CD))));
                return_warehouse.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_RETURN_WAREHOUSE))));
                return_warehouse.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_RETURN_WAREHOUSE))));
                return_warehouse.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_RETURN_WAREHOUSE))));
                returnWarehouses.add(return_warehouse);
            } while (c.moveToNext());
        }

        c.close();
        return returnWarehouses;
    }


    public int updateProduct_Return_WareHouse(Product_Return_WareHouse returnWarehouse, String PRODUCT_CD, String sl, String ea_unit, String return_warehouse, String returnWarehouse_cd) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_RETURN_WAREHOUSE, PRODUCT_CD);
        values.put(PRODUCT_CODE_RETURN_WAREHOUSE, returnWarehouse.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_RETURN_WAREHOUSE, returnWarehouse.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_RETURN_WAREHOUSE, returnWarehouse.getEXPIRED_DATE());
        values.put(EA_UNIT_RETURN_WAREHOUSE, returnWarehouse.getUNIT());
        values.put(QTY_SET_AVAILABLE_RETURN_WAREHOUSE, sl);
        values.put(RETURN_CD, returnWarehouse_cd);

        // updating row
        return db.update(O_RETURN_WAREHOUSE, values, PRODUCT_CD_RETURN_WAREHOUSE + " = ?" + " AND " + EXPIRED_DATE_RETURN_WAREHOUSE + " = ?"
                        + " AND " + EA_UNIT_RETURN_WAREHOUSE + " = ?" + " AND " + STOCKIN_DATE_RETURN_WAREHOUSE + " = ?" + " AND " + RETURN_CD + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(returnWarehouse.getEXPIRED_DATE()), String.valueOf(ea_unit), String.valueOf(return_warehouse), returnWarehouse_cd});

    }


    public void deleteProduct_Return_WareHouse() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_RETURN_WAREHOUSE);
    }

    //END TABLE O_RETURN_WAREHOUSE
    //DATABASE O_MASTER_PICK
    public static final String O_MASTER_PICK = "O_MASTER_PICK";
    public static final String AUTOINCREMENT_MASTER_PICK = "AUTOINCREMENT_MASTER_PICK";
    public static final String PRODUCT_CODE_MASTER_PICK = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_MASTER_PICK = "PRODUCT_NAME";
    public static final String PRODUCT_CD_MASTER_PICK = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_MASTER_PICK = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_MASTER_PICK = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_MASTER_PICK = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_MASTER_PICK = "STOCKIN_DATE";
    public static final String EA_UNIT_MASTER_PICK = "EA_UNIT";
    public static final String POSITION_FROM_MASTER_PICK = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_MASTER_PICK = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_MASTER_PICK = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_MASTER_PICK = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_MASTER_PICK = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_MASTER_PICK = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_MASTER_PICK = "UNIQUE_CODE";
    public static final String MASTER_PICK_CD = "MASTER_PICK_CD";
    public static final String LPN_CD_MASTER_PICK = "LPN_CD_MASTER_PICK";
    public static final String LPN_CODE_MASTER_PICK = "LPN_CODE_MASTER_PICK";
    public static final String LPN_FROM_MASTER_PICK = "LPN_FROM_MASTER_PICK";
    public static final String LPN_TO_MASTER_PICK = "LPN_TO_MASTER_PICK";

    public static final String CREATE_TABLE_O_MASTER_PICK = "CREATE TABLE "
            + O_MASTER_PICK + "("
            + AUTOINCREMENT_MASTER_PICK + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_MASTER_PICK + " TEXT,"
            + PRODUCT_NAME_MASTER_PICK + " TEXT,"
            + PRODUCT_CODE_MASTER_PICK + " TEXT,"
            + QTY_EA_AVAILABLE_MASTER_PICK + " TEXT,"
            + QTY_SET_AVAILABLE_MASTER_PICK + " TEXT,"
            + EXPIRED_DATE_MASTER_PICK + " TEXT,"
            + STOCKIN_DATE_MASTER_PICK + " TEXT,"
            + EA_UNIT_MASTER_PICK + " TEXT,"
            + POSITION_FROM_MASTER_PICK + " TEXT,"
            + POSITION_FROM_CODE_MASTER_PICK + " TEXT,"
            + POSITION_FROM_DESCRIPTION_MASTER_PICK + " TEXT,"
            + POSITION_TO_MASTER_PICK + " TEXT,"
            + POSITION_TO_CODE_MASTER_PICK + " TEXT,"
            + POSITION_TO_DESCRIPTION_MASTER_PICK + " TEXT,"
            + MASTER_PICK_CD + " TEXT,"
            + UNIQUE_CODE_MASTER_PICK + " TEXT ,"
            + LPN_CD_MASTER_PICK + " TEXT ,"
            + LPN_CODE_MASTER_PICK + " TEXT ,"
            + LPN_FROM_MASTER_PICK + " TEXT ,"
            + LPN_TO_MASTER_PICK + " TEXT "
            + ")";


    public long CreateMaster_Pick(Product_Master_Pick masterPick) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
//        values.put(AUTOINCREMENT_MASTER_PICK , masterPick.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_MASTER_PICK, masterPick.getUNIT());
        values.put(PRODUCT_CODE_MASTER_PICK, masterPick.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_MASTER_PICK, masterPick.getPRODUCT_NAME());
        values.put(PRODUCT_CD_MASTER_PICK, masterPick.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_MASTER_PICK, masterPick.getQTY());
        values.put(STOCKIN_DATE_MASTER_PICK, masterPick.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_MASTER_PICK, masterPick.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_MASTER_PICK, masterPick.getEXPIRED_DATE());
        values.put(EA_UNIT_MASTER_PICK, masterPick.getUNIT());
        values.put(POSITION_FROM_MASTER_PICK, masterPick.getPOSITION_FROM_CD());
        values.put(POSITION_TO_MASTER_PICK, masterPick.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_MASTER_PICK, masterPick.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_MASTER_PICK, masterPick.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_MASTER_PICK, masterPick.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_MASTER_PICK, masterPick.getPOSITION_TO_DESCRIPTION());
        values.put(MASTER_PICK_CD, masterPick.getMASTER_PICK_CD());
        values.put(LPN_CODE_MASTER_PICK, masterPick.getLPN_CODE());
        values.put(LPN_FROM_MASTER_PICK, masterPick.getLPN_FROM());
        values.put(LPN_TO_MASTER_PICK, masterPick.getLPN_TO());
        // insert row
        long id = db.insert(O_MASTER_PICK, null, values);
        return id;
    }

    public int updatePositionFrom_masterPick_LPN(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_MASTER_PICK, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_MASTER_PICK, descreption);
        values.put(POSITION_FROM_CODE_MASTER_PICK, from);

        values.put(LPN_FROM_MASTER_PICK, from);


        // updating row
        return db.update(O_MASTER_PICK, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_MASTER_PICK + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }

    public int updatePositionFrom_masterPick(String unique_id, String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_MASTER_PICK, wareHouse);
        values.put(POSITION_FROM_CODE_MASTER_PICK, from);
        values.put(LPN_FROM_MASTER_PICK, "");
        values.put(POSITION_FROM_DESCRIPTION_MASTER_PICK, descreption);


        // updating row
        return db.update(O_MASTER_PICK, values,
                         AUTOINCREMENT_MASTER_PICK + " = ? ",
                new String[]{String.valueOf(unique_id)});

    }

    public int updatePositionTo_masterPick_LPN(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_MASTER_PICK, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_MASTER_PICK, descreption);
        values.put(LPN_TO_MASTER_PICK, to);

        values.put(POSITION_TO_CODE_MASTER_PICK, to);
        // updating row
        return db.update(O_MASTER_PICK, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_MASTER_PICK + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }


    public int updatePositionTo_masterPick(String unique_id,String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_MASTER_PICK, wareHouse);
        values.put(POSITION_TO_CODE_MASTER_PICK, to);
        values.put(LPN_TO_MASTER_PICK, "");

        values.put(POSITION_TO_DESCRIPTION_MASTER_PICK, descreption);
        // updating row
        return db.update(O_MASTER_PICK, values,
                        AUTOINCREMENT_MASTER_PICK + " = ? ",
                new String[]{String.valueOf(unique_id)});


    }


    public ArrayList<Product_Master_Pick>
    getoneProduct_Master_Pick(String CD, String expDate, String ea_unit, String stockinDate, String master_pick_cd) {
        ArrayList<Product_Master_Pick> masterPicks = new ArrayList<Product_Master_Pick>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_MASTER_PICK + " " + " WHERE "
                + PRODUCT_CD_MASTER_PICK + " = " + CD + " AND "
                + MASTER_PICK_CD + " = " + master_pick_cd + " AND "
                + EA_UNIT_MASTER_PICK + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_MASTER_PICK + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_MASTER_PICK + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_Master_Pick masterPick = new Product_Master_Pick();
                masterPick.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_MASTER_PICK))));
                masterPick.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_MASTER_PICK))));
                masterPick.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_MASTER_PICK))));
                masterPick.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_MASTER_PICK))));
                masterPick.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_MASTER_PICK))));
                masterPick.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_MASTER_PICK))));
                masterPick.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_MASTER_PICK))));
                masterPick.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_MASTER_PICK))));
                masterPick.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_MASTER_PICK))));
                masterPicks.add(masterPick);
            } while (c.moveToNext());
        }

        c.close();
        return masterPicks;
    }


    public ArrayList<Product_Master_Pick>
    getAllProduct_Master_Pick_Sync(String master_pick_cd) {
        ArrayList<Product_Master_Pick> masterPicks = new ArrayList<Product_Master_Pick>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(POSITION_FROM_CODE,'---','') " +
                "as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') " +
                "as POSITION_TO_CODE FROM " + O_MASTER_PICK + " where " + MASTER_PICK_CD + " = " + master_pick_cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Master_Pick masterPick = new Product_Master_Pick();
                masterPick.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_MASTER_PICK))));
                masterPick.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_MASTER_PICK))));
                masterPick.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_MASTER_PICK))));
                masterPick.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_MASTER_PICK))));
                masterPick.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_MASTER_PICK))));
                masterPick.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_MASTER_PICK))));
                masterPick.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_MASTER_PICK))));
                masterPick.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_MASTER_PICK))));
                masterPick.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_MASTER_PICK))));
                masterPick.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_MASTER_PICK))));
                masterPick.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_MASTER_PICK))));
                masterPick.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_MASTER_PICK))));
                masterPick.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_MASTER_PICK))));
                masterPick.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_MASTER_PICK))));
                masterPick.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_MASTER_PICK))));
                masterPick.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_MASTER_PICK))));
                masterPick.setMASTER_PICK_CD((c.getString(c
                        .getColumnIndex(MASTER_PICK_CD))));
                masterPick.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_MASTER_PICK))));
                masterPick.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_MASTER_PICK))));
                masterPick.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_MASTER_PICK))));
                masterPicks.add(masterPick);
            } while (c.moveToNext());
        }

        c.close();
        return masterPicks;
    }

    public ArrayList<Product_Master_Pick>
    getAllProduct_Master_Pick(String master_pick_cd) {
        ArrayList<Product_Master_Pick> masterPicks = new ArrayList<Product_Master_Pick>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_MASTER_PICK + " where " + MASTER_PICK_CD + " = " + master_pick_cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Master_Pick masterPick = new Product_Master_Pick();
                masterPick.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_MASTER_PICK))));
                masterPick.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_MASTER_PICK))));
                masterPick.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_MASTER_PICK))));
                masterPick.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_MASTER_PICK))));
                masterPick.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_MASTER_PICK))));
                masterPick.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_MASTER_PICK))));
                masterPick.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_MASTER_PICK))));
                masterPick.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_MASTER_PICK))));
                masterPick.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_MASTER_PICK))));
                masterPick.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_MASTER_PICK))));
                masterPick.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_MASTER_PICK))));
                masterPick.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_MASTER_PICK))));
                masterPick.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_MASTER_PICK))));
                masterPick.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_MASTER_PICK))));
                masterPick.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_MASTER_PICK))));
                masterPick.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_MASTER_PICK))));
                masterPick.setMASTER_PICK_CD((c.getString(c
                        .getColumnIndex(MASTER_PICK_CD))));
                masterPick.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_MASTER_PICK))));
                masterPick.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_MASTER_PICK))));
                masterPick.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_MASTER_PICK))));
                masterPicks.add(masterPick);
            } while (c.moveToNext());
        }

        c.close();
        return masterPicks;
    }


    public int updateProduct_Master_Pick(Product_Master_Pick masterPick, String PRODUCT_CD, String sl, String ea_unit, String stock, String master_pick_cd) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_MASTER_PICK, PRODUCT_CD);
        values.put(PRODUCT_CODE_MASTER_PICK, masterPick.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_MASTER_PICK, masterPick.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_MASTER_PICK, masterPick.getEXPIRED_DATE());
        values.put(EA_UNIT_MASTER_PICK, masterPick.getUNIT());
        values.put(QTY_SET_AVAILABLE_MASTER_PICK, sl);
        values.put(MASTER_PICK_CD, master_pick_cd);

        // updating row
        return db.update(O_MASTER_PICK, values, PRODUCT_CD_MASTER_PICK + " = ?" + " AND " + EXPIRED_DATE_MASTER_PICK + " = ?"
                        + " AND " + EA_UNIT_MASTER_PICK + " = ?" + " AND " + STOCKIN_DATE_MASTER_PICK + " = ?" + " AND " + MASTER_PICK_CD + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(masterPick.getEXPIRED_DATE()), String.valueOf(ea_unit), String.valueOf(stock), master_pick_cd});

    }


    public void deleteProduct_Master_Pick() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_MASTER_PICK);
    }

    //END TABLE O_MASTER_PICK

    //database from table O_PICK_LIST
    public static final String O_PICK_LIST = "O_PICK_LIST";
    public static final String AUTOINCREMENT_PICKLIST = "AUTOINCREMENT_PICKLIST";
    public static final String PRODUCT_CODE_PICKLIST = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_PICKLIST = "PRODUCT_NAME";
    public static final String PRODUCT_CD_PICKLIST = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_PICKLIST = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_PICKLIST = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_PICKLIST = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_PICKLIST = "STOCKIN_DATE";
    public static final String EA_UNIT_PICKLIST = "EA_UNIT";
    public static final String POSITION_FROM_PICKLIST = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_PICKLIST = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_PICKLIST = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_PICKLIST = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_PICKLIST = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_PICKLIST = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_PICKLIST = "UNIQUE_CODE";
    public static final String PICKLIST_CD = "PICKLIST_CD";
    public static final String LPN_CD_PICKLIST = "LPN_CD_PICKLIST";
    public static final String LPN_CODE_PICKLIST = "LPN_CODE_PICKLIST";
    public static final String LPN_FROM_PICKLIST = "LPN_FROM_PICKLIST";
    public static final String LPN_TO_PICKLIST = "LPN_TO_PICKLIST";

    public static final String CREATE_TABLE_O_PICKLIST = "CREATE TABLE "
            + O_PICK_LIST + "("
            + AUTOINCREMENT_PICKLIST + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + UNIQUE_CODE_PICKLIST + " TEXT,"
            + PRODUCT_CODE_PICKLIST + " TEXT,"
            + PRODUCT_NAME_PICKLIST + " TEXT,"
            + QTY_SET_AVAILABLE_PICKLIST + " TEXT,"
            + STOCKIN_DATE_PICKLIST + " TEXT,"
            + PRODUCT_CD_PICKLIST + " TEXT,"
            + QTY_EA_AVAILABLE_PICKLIST + " TEXT,"
            + POSITION_FROM_PICKLIST + " TEXT,"
            + EXPIRED_DATE_PICKLIST + " TEXT,"
            + EA_UNIT_PICKLIST + " TEXT,"
            + POSITION_FROM_CODE_PICKLIST + " TEXT,"
            + POSITION_TO_CODE_PICKLIST + " TEXT,"
            + POSITION_TO_PICKLIST + " TEXT,"
            + POSITION_FROM_DESCRIPTION_PICKLIST + " TEXT,"
            + PICKLIST_CD + " TEXT,"
            + POSITION_TO_DESCRIPTION_PICKLIST + " TEXT,"
            + LPN_CD_PICKLIST + " TEXT,"
            + LPN_CODE_PICKLIST + " TEXT,"
            + LPN_FROM_PICKLIST + " TEXT,"
            + LPN_TO_PICKLIST + " TEXT"
            + ")";


    public long CreatePickList(PickList qrcode) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(AUTOINCREMENT_PICKLIST,qrcode.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_PICKLIST, qrcode.getUNIQUE_CODE());
        values.put(PRODUCT_CODE_PICKLIST, qrcode.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_PICKLIST, qrcode.getPRODUCT_NAME());
        values.put(PRODUCT_CD_PICKLIST, qrcode.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_PICKLIST, qrcode.getQTY_SET_AVAILABLE());
        values.put(STOCKIN_DATE_PICKLIST, qrcode.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_PICKLIST, qrcode.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_PICKLIST, qrcode.getEXPIRED_DATE());
        values.put(EA_UNIT_PICKLIST, qrcode.getUNIT());
        values.put(POSITION_FROM_PICKLIST, qrcode.getPOSITION_FROM_CD());
        values.put(POSITION_TO_PICKLIST, qrcode.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_PICKLIST, qrcode.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_PICKLIST, qrcode.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_PICKLIST, qrcode.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_PICKLIST, qrcode.getPOSITION_TO_DESCRIPTION());
        values.put(PICKLIST_CD, qrcode.getPickListCD());
        values.put(LPN_CODE_PICKLIST, qrcode.getLPN_CODE());
        values.put(LPN_FROM_PICKLIST, qrcode.getLPN_FROM());
        values.put(LPN_TO_PICKLIST, qrcode.getLPN_TO());

        // insert row
        long id = db.insert(O_PICK_LIST, null, values);
        return id;
    }

    public ArrayList<PickList>
    getoneProduct_PickList(String CD, String expDate, String ea_unit, String PickListCD, String stockindate) {
        ArrayList<PickList> qrcode = new ArrayList<PickList>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_PICK_LIST + " " + " WHERE "
                + PRODUCT_CD_PICKLIST + " = " + CD + " AND "
                + EA_UNIT_PICKLIST + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_PICKLIST + " like " + " '%" + expDate + "%'" + " AND "
                + PICKLIST_CD + " = " + PickListCD + " AND "
                + STOCKIN_DATE_PICKLIST + " like " + " '%" + stockindate + "%'";

        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                PickList qrcodeq = new PickList();
                qrcodeq.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PICKLIST))));
                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PICKLIST))));
                qrcodeq.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PICKLIST))));
                qrcodeq.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PICKLIST))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PICKLIST))));
                qrcodeq.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_PICKLIST))));
                qrcodeq.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_PICKLIST))));
                qrcodeq.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_PICKLIST))));
                qrcodeq.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_PICKLIST))));


                qrcode.add(qrcodeq);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }


    public ArrayList<PickList>
    getAllProduct_PickList() {
        ArrayList<PickList> picklist = new ArrayList<PickList>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_PICK_LIST;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                PickList product = new PickList();
                product.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PICKLIST))));
                product.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_PICKLIST))));
                product.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PICKLIST))));
                product.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PICKLIST))));
                product.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PICKLIST))));
                product.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_PICKLIST))));
                product.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_PICKLIST))));
                product.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_PICKLIST))));
                product.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PICKLIST))));
                product.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_PICKLIST))));
                product.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_PICKLIST))));
                product.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_PICKLIST))));
                product.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_PICKLIST))));
                product.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_PICKLIST))));
                product.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_PICKLIST))));
                product.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_PICKLIST))));
                product.setPickListCD((c.getString(c
                        .getColumnIndex(PICKLIST_CD))));
                product.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_PICKLIST))));
                product.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_PICKLIST))));
                product.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_PICKLIST))));
                picklist.add(product);
            } while (c.moveToNext());
        }

        c.close();
        return picklist;
    }

    public ArrayList<PickList>
    getAllProduct_PickList_Show(String pickListCD) {
        ArrayList<PickList> picklist = new ArrayList<PickList>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_PICK_LIST + " where " + PICKLIST_CD + " = " + pickListCD;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                PickList product = new PickList();
                product.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PICKLIST))));
                product.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_PICKLIST))));
                product.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PICKLIST))));
                product.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PICKLIST))));
                product.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PICKLIST))));
                product.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_PICKLIST))));
                product.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_PICKLIST))));
                product.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_PICKLIST))));
                product.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PICKLIST))));
                product.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_PICKLIST))));
                product.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_PICKLIST))));
                product.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_PICKLIST))));
                product.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_PICKLIST))));
                product.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_PICKLIST))));
                product.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_PICKLIST))));
                product.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_PICKLIST))));
                product.setPickListCD((c.getString(c
                        .getColumnIndex(PICKLIST_CD))));
                product.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_PICKLIST))));
                product.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_PICKLIST))));
                product.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_PICKLIST))));
                picklist.add(product);
            } while (c.moveToNext());
        }

        c.close();
        return picklist;
    }

    public ArrayList<PickList>
    getAllProduct_PickList_Sync(String pickListCD) {
        ArrayList<PickList> picklist = new ArrayList<PickList>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_PICK_LIST + " where " + PICKLIST_CD + " = " + pickListCD;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                PickList product = new PickList();

                product.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_PICKLIST))));
                product.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PICKLIST))));
                product.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PICKLIST))));
                product.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PICKLIST))));
                product.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_PICKLIST))));
                product.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_PICKLIST))));
                product.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_PICKLIST))));
                product.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PICKLIST))));
                product.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_PICKLIST))));
                product.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_PICKLIST))));
                product.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_PICKLIST))));
                product.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_PICKLIST))));
                product.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_PICKLIST))));
                product.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_PICKLIST))));
                product.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_PICKLIST))));
                product.setPickListCD((c.getString(c
                        .getColumnIndex(PICKLIST_CD))));
                product.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_PICKLIST))));
                product.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_PICKLIST))));
                product.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_PICKLIST))));
                picklist.add(product);
            } while (c.moveToNext());
        }

        c.close();
        return picklist;
    }

    public int updateProduct_PickList(PickList picklist, String PRODUCT_CD, String sl, String ea_unit, String stock, String pickListCD) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_PICKLIST, PRODUCT_CD);
        values.put(PRODUCT_CODE_PICKLIST, picklist.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_PICKLIST, picklist.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_PICKLIST, picklist.getEXPIRED_DATE());
        values.put(EA_UNIT_PICKLIST, picklist.getUNIT());
        values.put(QTY_SET_AVAILABLE_PICKLIST, sl);
        values.put(PICKLIST_CD, pickListCD);


        // updating row
        return db.update(O_PICK_LIST, values, PRODUCT_CD_PICKLIST + " = ?" + " AND " + EXPIRED_DATE_PICKLIST + " = ?"
                        + " AND " + EA_UNIT_PICKLIST + " = ?" + " AND " + STOCKIN_DATE_PICKLIST + " = ?" + " AND " + PICKLIST_CD + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(picklist.getEXPIRED_DATE()), String.valueOf(ea_unit), String.valueOf(stock), String.valueOf(pickListCD)});

    }


    public void deleteProduct_PickList_CD(String pickListCD) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_PICK_LIST + " WHERE " + PICKLIST_CD + " = " + pickListCD);
    }
    public int updatePositionFrom_PickList_LPN(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_PICKLIST, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_PICKLIST, descreption);
        values.put(POSITION_FROM_CODE_PICKLIST, from);
        values.put(LPN_FROM_PICKLIST, from);

        // updating row
        return db.update(O_PICK_LIST, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_PICKLIST + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }

    public int updatePositionFrom_PickList(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_PICKLIST, wareHouse);
        values.put(POSITION_FROM_CODE_PICKLIST, from);
        values.put(LPN_FROM_PICKLIST, "");
        values.put(POSITION_FROM_DESCRIPTION_PICKLIST, descreption);

        // updating row
        return db.update(O_PICK_LIST, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_PICKLIST + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }

    public int updatePositionTo_PickList_LPN(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_PICKLIST, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_PICKLIST, descreption);
        values.put(LPN_TO_PICKLIST, to);

        values.put(POSITION_TO_CODE_PICKLIST, to);
        // updating row
        return db.update(O_PICK_LIST, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_PICKLIST + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }


    public int updatePositionTo_PickList(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_PICKLIST, wareHouse);
        values.put(POSITION_TO_CODE_PICKLIST, to);
        values.put(LPN_TO_PICKLIST, "");

        values.put(POSITION_TO_DESCRIPTION_PICKLIST, descreption);
        // updating row
        return db.update(O_PICK_LIST, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_PICKLIST + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }
    //end table PICK_LIST

    //SQLite Create Table O_Param
    public static final String O_PARAM = "O_PARAM";
    public static final String PARAM_KEY = "PARAM_KEY";
    public static final String PARAM_VALUES = "PARAM_VALUES";

    public static final String CREATE_TABLE_O_PARAM = "CREATE TABLE " + O_PARAM + " ("
            + PARAM_KEY + " TEXT," + PARAM_VALUES + " TEXT)";

    public CParam getParamByKey(String primaryKey) {

        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_PARAM + " WHERE "
                + PARAM_KEY + " = ?";
        CParam cParam = new CParam();
        android.database.Cursor c = db.rawQuery(selectQuery,
                new String[]{String.valueOf(primaryKey)});
        if (c != null && c.moveToFirst()) {
            do {
                cParam.setKey(c.getString(c.getColumnIndex(PARAM_KEY)));
                cParam.setValue(c.getString(c.getColumnIndex(PARAM_VALUES)));
            } while (c.moveToNext());
        }
        c.close();
        return cParam;
    }

    public long updateParam(CParam ccValue) {

        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(PARAM_VALUES, ccValue.getValue());

        // updating row
        long a = db.update(O_PARAM, values, PARAM_KEY + " = '" + ccValue.getKey() + "'", null);
        return a;
    }

    public long createParam(CParam ccValue) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PARAM_KEY, ccValue.getKey());
        values.put(PARAM_VALUES, ccValue.getValue());

        long todo_id = db.insert(O_PARAM, null, values);
        return todo_id;
    }

    public boolean checkExistsParam(String primaryKey) {

        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_PARAM + " WHERE "
                + PARAM_KEY + " = ?";

        android.database.Cursor c = db.rawQuery(selectQuery,
                new String[]{String.valueOf(primaryKey)});
        // looping through all rows and adding to list
        try {
            if (c != null && c.moveToFirst()) {
                return true;
            }
        } finally {
            c.close();
        }
        return false;
    }

    public void deleteParam() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_PARAM);
    }
    //END TABLE

    //DATABASE LOAD_PALLET
    public static final String O_LOAD_PALLET = "O_LOAD_PALLET";
    public static final String AUTOINCREMENT_LOAD_PALLET = "AUTOINCREMENT_LOAD_PALLET";
    public static final String PRODUCT_CODE_LOAD_PALLET = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_LOAD_PALLET = "PRODUCT_NAME";
    public static final String PRODUCT_CD_LOAD_PALLET = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_LOAD_PALLET = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_LOAD_PALLET = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_LOAD_PALLET= "EXPIRY_DATE";
    public static final String STOCKIN_DATE_LOAD_PALLET = "STOCKIN_DATE";
    public static final String EA_UNIT_LOAD_PALLET = "EA_UNIT";
    public static final String POSITION_FROM_LOAD_PALLET = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_LOAD_PALLET = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_LOAD_PALLET = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_LOAD_PALLET = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_LOAD_PALLET = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_LOAD_PALLET = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_LOAD_PALLET = "UNIQUE_CODE";
    public static final String LPN_CD_LOAD_PALLET = "LPN_CD_LOAD_PALLET";
    public static final String LPN_CODE_LOAD_PALLET = "LPN_CODE_LOAD_PALLET";
    public static final String LPN_FROM_LOAD_PALLET = "LPN_FROM_LOAD_PALLET";
    public static final String LPN_TO_LOAD_PALLET = "LPN_TO_LOAD_PALLET";

    public static final String CREATE_TABLE_O_LOAD_PALLET = "CREATE TABLE "
            + O_LOAD_PALLET + "("
            + AUTOINCREMENT_LOAD_PALLET + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_LOAD_PALLET + " TEXT,"
            + PRODUCT_NAME_LOAD_PALLET + " TEXT,"
            + PRODUCT_CODE_LOAD_PALLET + " TEXT,"
            + QTY_EA_AVAILABLE_LOAD_PALLET + " TEXT,"
            + QTY_SET_AVAILABLE_LOAD_PALLET + " TEXT,"
            + EXPIRED_DATE_LOAD_PALLET + " TEXT,"
            + STOCKIN_DATE_LOAD_PALLET + " TEXT,"
            + EA_UNIT_LOAD_PALLET + " TEXT,"
            + POSITION_FROM_LOAD_PALLET + " TEXT,"
            + POSITION_FROM_CODE_LOAD_PALLET + " TEXT,"
            + POSITION_FROM_DESCRIPTION_LOAD_PALLET + " TEXT,"
            + POSITION_TO_LOAD_PALLET + " TEXT,"
            + POSITION_TO_CODE_LOAD_PALLET + " TEXT,"
            + POSITION_TO_DESCRIPTION_LOAD_PALLET + " TEXT,"
            + UNIQUE_CODE_LOAD_PALLET + " TEXT ,"
            + LPN_CD_LOAD_PALLET + " TEXT ,"
            + LPN_CODE_LOAD_PALLET + " TEXT ,"
            + LPN_FROM_LOAD_PALLET + " TEXT ,"
            + LPN_TO_LOAD_PALLET + " TEXT "
            + ")";

    public long CreateLOAD_PALLET(Product_LoadPallet product_loadPallet) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(AUTOINCREMENT_LOAD_PALLET, product_loadPallet.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_LOAD_PALLET, product_loadPallet.getUNIT());
        values.put(PRODUCT_CODE_LOAD_PALLET, product_loadPallet.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_LOAD_PALLET, product_loadPallet.getPRODUCT_NAME());
        values.put(PRODUCT_CD_LOAD_PALLET, product_loadPallet.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_LOAD_PALLET, product_loadPallet.getQTY());
        values.put(STOCKIN_DATE_LOAD_PALLET, product_loadPallet.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_LOAD_PALLET, product_loadPallet.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_LOAD_PALLET, product_loadPallet.getEXPIRED_DATE());
        values.put(EA_UNIT_LOAD_PALLET, product_loadPallet.getUNIT());
        values.put(POSITION_FROM_LOAD_PALLET, product_loadPallet.getPOSITION_FROM_CD());
        values.put(POSITION_TO_LOAD_PALLET, product_loadPallet.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_LOAD_PALLET, product_loadPallet.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_LOAD_PALLET, product_loadPallet.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_LOAD_PALLET, product_loadPallet.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_LOAD_PALLET, product_loadPallet.getPOSITION_TO_DESCRIPTION());
        values.put(LPN_CODE_LOAD_PALLET, product_loadPallet.getLPN_CODE());
        values.put(LPN_FROM_LOAD_PALLET, product_loadPallet.getLPN_FROM());
        values.put(LPN_TO_LOAD_PALLET, product_loadPallet.getLPN_TO());
        // insert row
        long id = db.insert(O_LOAD_PALLET, null, values);
        return id;
    }

    public ArrayList<Product_LoadPallet> getoneProduct_LoadPallet(String CD, String expDate, String ea_unit, String stockinDate) {
        ArrayList<Product_LoadPallet> loadPallets = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_LOAD_PALLET + " " + " WHERE "
                + PRODUCT_CD_LOAD_PALLET + " = " + CD + " AND "
                + EA_UNIT_LOAD_PALLET + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_LOAD_PALLET + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_LOAD_PALLET + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_LoadPallet product_loadPallet = new Product_LoadPallet();
                product_loadPallet.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_LOAD_PALLET))));
                product_loadPallet.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_LOAD_PALLET))));
                product_loadPallet.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_LOAD_PALLET))));
                product_loadPallet.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_LOAD_PALLET))));
                product_loadPallet.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_LOAD_PALLET))));
                product_loadPallet.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_LOAD_PALLET))));
                product_loadPallet.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_LOAD_PALLET))));
                product_loadPallet.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_LOAD_PALLET))));
                product_loadPallet.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_LOAD_PALLET))));

                loadPallets.add(product_loadPallet);
            } while (c.moveToNext());
        }

        c.close();
        return loadPallets;
    }

    public ArrayList<Product_LoadPallet> getAllProduct_LoadPallet() {
        ArrayList<Product_LoadPallet> loadPallets = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_LOAD_PALLET;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_LoadPallet product_loadPallet = new Product_LoadPallet();
                product_loadPallet.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_LOAD_PALLET))));
                product_loadPallet.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_LOAD_PALLET))));
                product_loadPallet.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_LOAD_PALLET))));
                product_loadPallet.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_LOAD_PALLET))));
                product_loadPallet.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_LOAD_PALLET))));
                product_loadPallet.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_LOAD_PALLET))));
                product_loadPallet.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_LOAD_PALLET))));
                product_loadPallet.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_LOAD_PALLET))));
                product_loadPallet.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_LOAD_PALLET))));
                product_loadPallet.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_LOAD_PALLET))));
                product_loadPallet.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_LOAD_PALLET))));
                product_loadPallet.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_LOAD_PALLET))));
                product_loadPallet.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_LOAD_PALLET))));
                product_loadPallet.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_LOAD_PALLET))));
                product_loadPallet.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_LOAD_PALLET))));
                product_loadPallet.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_LOAD_PALLET))));
                product_loadPallet.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_LOAD_PALLET))));
                product_loadPallet.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_LOAD_PALLET))));
                product_loadPallet.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_LOAD_PALLET))));
                loadPallets.add(product_loadPallet);
            } while (c.moveToNext());
        }

        c.close();
        return loadPallets;
    }

    public ArrayList<Product_LoadPallet> getAllProduct_LoadPallet_Sync() {
        ArrayList<Product_LoadPallet> loadPallets = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_LOAD_PALLET;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_LoadPallet product_loadPallet = new Product_LoadPallet();

                product_loadPallet.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_LOAD_PALLET))));
                product_loadPallet.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_LOAD_PALLET))));
                product_loadPallet.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_LOAD_PALLET))));
                product_loadPallet.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_LOAD_PALLET))));
                product_loadPallet.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_LOAD_PALLET))));
                product_loadPallet.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_LOAD_PALLET))));
                product_loadPallet.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_LOAD_PALLET))));
                product_loadPallet.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_LOAD_PALLET))));
                product_loadPallet.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_LOAD_PALLET))));
                product_loadPallet.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_LOAD_PALLET))));
                product_loadPallet.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_LOAD_PALLET))));
                product_loadPallet.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_LOAD_PALLET))));
                product_loadPallet.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_LOAD_PALLET))));
                product_loadPallet.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_LOAD_PALLET))));
                product_loadPallet.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_LOAD_PALLET))));
                product_loadPallet.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_LOAD_PALLET))));
                product_loadPallet.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_LOAD_PALLET))));
                product_loadPallet.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_LOAD_PALLET))));
                loadPallets.add(product_loadPallet);
            } while (c.moveToNext());
        }
        c.close();
        return loadPallets;
    }

    public int updateProduct_LoadPallet(Product_LoadPallet product_loadPallet, String PRODUCT_CD, String sl, String ea_unit, String stock) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_LOAD_PALLET, PRODUCT_CD);
        values.put(PRODUCT_CODE_LOAD_PALLET, product_loadPallet.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_LOAD_PALLET, product_loadPallet.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_LOAD_PALLET, product_loadPallet.getEXPIRED_DATE());
        values.put(EA_UNIT_LOAD_PALLET, product_loadPallet.getUNIT());
        values.put(QTY_SET_AVAILABLE_LOAD_PALLET, sl);

        // updating row
        return db.update(O_LOAD_PALLET, values, PRODUCT_CD_LOAD_PALLET + " = ?" + " AND " + EXPIRED_DATE_LOAD_PALLET + " = ?"
                        + " AND " + EA_UNIT_LOAD_PALLET + " = ?" + " AND " + STOCKIN_DATE_LOAD_PALLET + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(product_loadPallet.getEXPIRED_DATE()), String.valueOf(ea_unit), String.valueOf(stock)});

    }



    public void deleteProduct_LoadPallet() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_LOAD_PALLET);
    }

    //DATABASE remove_lpn
    public static final String O_REMOVE_LPN = "O_REMOVE_LPN";
    public static final String AUTOINCREMENT_REMOVE_LPN = "AUTOINCREMENT_REMOVE_LPN";
    public static final String PRODUCT_CODE_REMOVE_LPN = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_remove_TRANSFER = "PRODUCT_NAME";
    public static final String PRODUCT_CD_REMOVE_LPN = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_REMOVE_LPN = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_REMOVE_LPN = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_REMOVE_LPN = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_REMOVE_LPN = "STOCKIN_DATE_REMOVE_LPN";
    public static final String EA_UNIT_REMOVE_LPN = "EA_UNIT";
    public static final String POSITION_FROM_REMOVE_LPN = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_REMOVE_LPN = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_REMOVE_LPN = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_REMOVE_LPN = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_REMOVE_LPN = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_REMOVE_LPN = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_REMOVE_LPN = "UNIQUE_CODE";
    public static final String LPN_CD_REMOVE_LPN = "LPN_CD_REMOVE_LPN";
    public static final String LPN_CODE_REMOVE_LPN = "LPN_CODE_REMOVE_LPN";
    public static final String LPN_FROM_REMOVE_LPN = "LPN_FROM_REMOVE_LPN";
    public static final String LPN_TO_REMOVE_LPN = "LPN_TO_REMOVE_LPN";

    public static final String CREATE_TABLE_O_REMOVE_LPN = "CREATE TABLE "
            + O_REMOVE_LPN + "("
            + AUTOINCREMENT_REMOVE_LPN + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_REMOVE_LPN + " TEXT,"
            + PRODUCT_NAME_remove_TRANSFER + " TEXT,"
            + PRODUCT_CODE_REMOVE_LPN + " TEXT,"
            + QTY_EA_AVAILABLE_REMOVE_LPN + " TEXT,"
            + QTY_SET_AVAILABLE_REMOVE_LPN + " TEXT,"
            + EXPIRED_DATE_REMOVE_LPN + " TEXT,"
            + STOCKIN_DATE_REMOVE_LPN + " TEXT,"
            + EA_UNIT_REMOVE_LPN + " TEXT,"
            + POSITION_FROM_REMOVE_LPN + " TEXT,"
            + POSITION_FROM_CODE_REMOVE_LPN + " TEXT,"
            + POSITION_FROM_DESCRIPTION_REMOVE_LPN + " TEXT,"
            + POSITION_TO_REMOVE_LPN + " TEXT,"
            + POSITION_TO_CODE_REMOVE_LPN + " TEXT,"
            + POSITION_TO_DESCRIPTION_REMOVE_LPN + " TEXT,"
            + UNIQUE_CODE_REMOVE_LPN + " TEXT ,"
            + LPN_CD_REMOVE_LPN + " TEXT ,"
            + LPN_CODE_REMOVE_LPN + " TEXT ,"
            + LPN_FROM_REMOVE_LPN + " TEXT ,"
            + LPN_TO_REMOVE_LPN + " TEXT "
            + ")";


    public long Create_Remove_LPN(Product_Remove_LPN remove_lpn) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(AUTOINCREMENT_REMOVE_LPN,remove_lpn.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_REMOVE_LPN, remove_lpn.getUNIT());
        values.put(PRODUCT_CODE_REMOVE_LPN, remove_lpn.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_remove_TRANSFER, remove_lpn.getPRODUCT_NAME());
        values.put(PRODUCT_CD_REMOVE_LPN, remove_lpn.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_REMOVE_LPN, remove_lpn.getQTY());
        values.put(STOCKIN_DATE_REMOVE_LPN, remove_lpn.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_REMOVE_LPN, remove_lpn.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_REMOVE_LPN, remove_lpn.getEXPIRED_DATE());
        values.put(EA_UNIT_REMOVE_LPN, remove_lpn.getUNIT());
        values.put(POSITION_FROM_REMOVE_LPN, remove_lpn.getPOSITION_FROM_CD());
        values.put(POSITION_TO_REMOVE_LPN, remove_lpn.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_REMOVE_LPN, remove_lpn.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_REMOVE_LPN, remove_lpn.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_REMOVE_LPN, remove_lpn.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_REMOVE_LPN, remove_lpn.getPOSITION_TO_DESCRIPTION());
        values.put(LPN_CODE_REMOVE_LPN, remove_lpn.getLPN_CODE());
        values.put(LPN_FROM_REMOVE_LPN, remove_lpn.getLPN_FROM());
        values.put(LPN_TO_REMOVE_LPN, remove_lpn.getLPN_TO());
        // insert row
        long id = db.insert(O_REMOVE_LPN, null, values);
        return id;
    }

    public ArrayList<Product_Remove_LPN>
    getoneProduct_Remove_LPN(String CD, String expDate, String ea_unit, String removeinDate) {
        ArrayList<Product_Remove_LPN> remove_lpns = new ArrayList<Product_Remove_LPN>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_REMOVE_LPN + " " + " WHERE "
                + PRODUCT_CD_REMOVE_LPN + " = " + CD + " AND "
                + EA_UNIT_REMOVE_LPN + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_REMOVE_LPN + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_REMOVE_LPN + " like " + " '%" + removeinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Remove_LPN remove_lpn = new Product_Remove_LPN();
                remove_lpn.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_REMOVE_LPN))));
                remove_lpn.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_REMOVE_LPN))));
                remove_lpn.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_REMOVE_LPN))));
                remove_lpn.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_remove_TRANSFER))));
                remove_lpn.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_REMOVE_LPN))));
                remove_lpn.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_REMOVE_LPN))));
                remove_lpn.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_REMOVE_LPN))));
                remove_lpn.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_REMOVE_LPN))));
                remove_lpn.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_REMOVE_LPN))));

                remove_lpns.add(remove_lpn);
            } while (c.moveToNext());
        }

        c.close();
        return remove_lpns;
    }


    public ArrayList<Product_Remove_LPN>
    getAllProduct_Remove_LPN() {
        ArrayList<Product_Remove_LPN> remove_lpns = new ArrayList<Product_Remove_LPN>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_REMOVE_LPN;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Remove_LPN remove = new Product_Remove_LPN();
                remove.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_REMOVE_LPN))));
                remove.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_REMOVE_LPN))));
                remove.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_REMOVE_LPN))));
                remove.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_remove_TRANSFER))));
                remove.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_REMOVE_LPN))));
                remove.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_REMOVE_LPN))));
                remove.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_REMOVE_LPN))));
                remove.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_REMOVE_LPN))));
                remove.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_REMOVE_LPN))));
                remove.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_REMOVE_LPN))));
                remove.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_REMOVE_LPN))));
                remove.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_REMOVE_LPN))));
                remove.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_REMOVE_LPN))));
                remove.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_REMOVE_LPN))));
                remove.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_REMOVE_LPN))));
                remove.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_REMOVE_LPN))));
                remove.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_REMOVE_LPN))));
                remove.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_REMOVE_LPN))));
                remove.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_REMOVE_LPN))));
                remove_lpns.add(remove);
            } while (c.moveToNext());
        }

        c.close();
        return remove_lpns;
    }


    public ArrayList<Product_Remove_LPN>
    getAllProduct_Remove_LPN_Sync() {
        ArrayList<Product_Remove_LPN> remove_lpns = new ArrayList<Product_Remove_LPN>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_REMOVE_LPN;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Remove_LPN remove = new Product_Remove_LPN();

                remove.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_REMOVE_LPN))));
                remove.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_REMOVE_LPN))));
                remove.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_remove_TRANSFER))));
                remove.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_REMOVE_LPN))));
                remove.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_REMOVE_LPN))));
                remove.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_REMOVE_LPN))));
                remove.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_REMOVE_LPN))));
                remove.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_REMOVE_LPN))));
                remove.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_REMOVE_LPN))));
                remove.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_REMOVE_LPN))));
                remove.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_REMOVE_LPN))));
                remove.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_REMOVE_LPN))));
                remove.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_REMOVE_LPN))));
                remove.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_REMOVE_LPN))));
                remove.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_REMOVE_LPN))));
                remove.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_REMOVE_LPN))));
                remove.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_REMOVE_LPN))));
                remove.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_REMOVE_LPN))));
                remove_lpns.add(remove);
            } while (c.moveToNext());
        }
        c.close();
        return remove_lpns;
    }

    public int updateProduct_Remove_LPN(Product_Remove_LPN remove_lpn, String PRODUCT_CD, String sl, String ea_unit, String remove) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_REMOVE_LPN, PRODUCT_CD);
        values.put(PRODUCT_CODE_REMOVE_LPN, remove_lpn.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_remove_TRANSFER, remove_lpn.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_REMOVE_LPN, remove_lpn.getEXPIRED_DATE());
        values.put(EA_UNIT_REMOVE_LPN, remove_lpn.getUNIT());
        values.put(QTY_SET_AVAILABLE_REMOVE_LPN, sl);

        // updating row
        return db.update(O_REMOVE_LPN, values, PRODUCT_CD_REMOVE_LPN + " = ?" + " AND " + EXPIRED_DATE_REMOVE_LPN + " = ?"
                        + " AND " + EA_UNIT_REMOVE_LPN + " = ?" + " AND " + STOCKIN_DATE_REMOVE_LPN + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(remove_lpn.getEXPIRED_DATE()), String.valueOf(ea_unit), String.valueOf(remove)});

    }

    public int updatePositionFrom_Remove(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_REMOVE_LPN, wareHouse);
        values.put(POSITION_FROM_CODE_REMOVE_LPN, from);
        values.put(LPN_FROM_REMOVE_LPN, "");
        values.put(POSITION_FROM_DESCRIPTION_REMOVE_LPN, descreption);

        // updating row
        return db.update(O_REMOVE_LPN, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_REMOVE_LPN + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }

    public int updatePositionFrom_Remove_LPN(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_REMOVE_LPN, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_REMOVE_LPN, descreption);

        values.put(POSITION_FROM_CODE_REMOVE_LPN, from);
        values.put(LPN_FROM_REMOVE_LPN, from);

        // updating row
        return db.update(O_REMOVE_LPN, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_REMOVE_LPN + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }

    public int updatePositionTo_Remove_LPN(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_REMOVE_LPN, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_REMOVE_LPN, descreption);
        values.put(LPN_TO_REMOVE_LPN, to);
        values.put(POSITION_TO_CODE_REMOVE_LPN, to);
        // updating row
        return db.update(O_REMOVE_LPN, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_REMOVE_LPN + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});
    }
    public int updatePositionTo_Remove(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_REMOVE_LPN, wareHouse);
        values.put(POSITION_TO_CODE_REMOVE_LPN, to);
        values.put(LPN_TO_REMOVE_LPN, "");

        values.put(POSITION_TO_DESCRIPTION_REMOVE_LPN, descreption);
        // updating row
        return db.update(O_REMOVE_LPN, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_REMOVE_LPN + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});
    }

    public void deleteProduct_Remove_LPN() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_REMOVE_LPN);
    }

    //END TABLE remove_lpn



    //DATABASE STOCK_TRANSFER
    public static final String O_STOCK_TRANSFER = "O_STOCK_TRANSFER";
    public static final String AUTOINCREMENT_STOCK_TRANSFER = "AUTOINCREMENT_STOCK_TRANSFER";
    public static final String PRODUCT_CODE_STOCK_TRANSFER = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_STOCK_TRANSFER = "PRODUCT_NAME";
    public static final String PRODUCT_CD_STOCK_TRANSFER = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_STOCK_TRANSFER = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_STOCK_TRANSFER = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_STOCK_TRANSFER = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_STOCK_TRANSFER = "STOCKIN_DATE";
    public static final String EA_UNIT_STOCK_TRANSFER = "EA_UNIT";
    public static final String POSITION_FROM_STOCK_TRANSFER = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_STOCK_TRANSFER = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_STOCK_TRANSFER = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_STOCK_TRANSFER = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_STOCK_TRANSFER = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_STOCK_TRANSFER = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_STOCK_TRANSFER = "UNIQUE_CODE";
    public static final String LPN_CD_STOCK_TRANSFER = "LPN_CD_STOCK_TRANSFER";
    public static final String LPN_CODE_STOCK_TRANSFER = "LPN_CODE_STOCK_TRANSFER";
    public static final String LPN_FROM_STOCK_TRANSFER = "LPN_FROM_STOCK_TRANSFER";
    public static final String LPN_TO_STOCK_TRANSFER = "LPN_TO_STOCK_TRANSFER";

    public static final String CREATE_TABLE_O_STOCK_TRANSFER = "CREATE TABLE "
            + O_STOCK_TRANSFER + "("
            + AUTOINCREMENT_STOCK_TRANSFER + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_STOCK_TRANSFER + " TEXT,"
            + PRODUCT_NAME_STOCK_TRANSFER + " TEXT,"
            + PRODUCT_CODE_STOCK_TRANSFER + " TEXT,"
            + QTY_EA_AVAILABLE_STOCK_TRANSFER + " TEXT,"
            + QTY_SET_AVAILABLE_STOCK_TRANSFER + " TEXT,"
            + EXPIRED_DATE_STOCK_TRANSFER + " TEXT,"
            + STOCKIN_DATE_STOCK_TRANSFER + " TEXT,"
            + EA_UNIT_STOCK_TRANSFER + " TEXT,"
            + POSITION_FROM_STOCK_TRANSFER + " TEXT,"
            + POSITION_FROM_CODE_STOCK_TRANSFER + " TEXT,"
            + POSITION_FROM_DESCRIPTION_STOCK_TRANSFER + " TEXT,"
            + POSITION_TO_STOCK_TRANSFER + " TEXT,"
            + POSITION_TO_CODE_STOCK_TRANSFER + " TEXT,"
            + POSITION_TO_DESCRIPTION_STOCK_TRANSFER + " TEXT,"
            + UNIQUE_CODE_STOCK_TRANSFER + " TEXT ,"
            + LPN_CD_STOCK_TRANSFER + " TEXT ,"
            + LPN_CODE_STOCK_TRANSFER + " TEXT ,"
            + LPN_FROM_STOCK_TRANSFER + " TEXT ,"
            + LPN_TO_STOCK_TRANSFER + " TEXT "
            + ")";


    public long CreateSTOCK_TRANSFER(Product_StockTransfer stockTransfer) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(AUTOINCREMENT_STOCK_TRANSFER, stockTransfer.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_STOCK_TRANSFER, stockTransfer.getUNIT());
        values.put(PRODUCT_CODE_STOCK_TRANSFER, stockTransfer.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_STOCK_TRANSFER, stockTransfer.getPRODUCT_NAME());
        values.put(PRODUCT_CD_STOCK_TRANSFER, stockTransfer.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_STOCK_TRANSFER, stockTransfer.getQTY());
        values.put(STOCKIN_DATE_STOCK_TRANSFER, stockTransfer.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_STOCK_TRANSFER, stockTransfer.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_STOCK_TRANSFER, stockTransfer.getEXPIRED_DATE());
        values.put(EA_UNIT_STOCK_TRANSFER, stockTransfer.getUNIT());
        values.put(POSITION_FROM_STOCK_TRANSFER, stockTransfer.getPOSITION_FROM_CD());
        values.put(POSITION_TO_STOCK_TRANSFER, stockTransfer.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_STOCK_TRANSFER, stockTransfer.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_STOCK_TRANSFER, stockTransfer.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_STOCK_TRANSFER, stockTransfer.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_STOCK_TRANSFER, stockTransfer.getPOSITION_TO_DESCRIPTION());
        values.put(LPN_CODE_STOCK_TRANSFER, stockTransfer.getLPN_CODE());
        values.put(LPN_FROM_STOCK_TRANSFER, stockTransfer.getLPN_FROM());
        values.put(LPN_TO_STOCK_TRANSFER, stockTransfer.getLPN_TO());
        // insert row
        long id = db.insert(O_STOCK_TRANSFER, null, values);
        return id;
    }

    public ArrayList<Product_StockTransfer>
    getoneProduct_StockTransfer(String CD, String expDate, String ea_unit, String stockinDate) {
        ArrayList<Product_StockTransfer> stockTransfers = new ArrayList<Product_StockTransfer>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_STOCK_TRANSFER + " " + " WHERE "
                + PRODUCT_CD_STOCK_TRANSFER + " = " + CD + " AND "
                + EA_UNIT_STOCK_TRANSFER + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_STOCK_TRANSFER + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_STOCK_TRANSFER + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_StockTransfer stockTransfer = new Product_StockTransfer();
                stockTransfer.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_STOCK_TRANSFER))));
                stockTransfer.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_STOCK_TRANSFER))));
                stockTransfer.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_STOCK_TRANSFER))));
                stockTransfer.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_STOCK_TRANSFER))));
                stockTransfer.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_STOCK_TRANSFER))));
                stockTransfer.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_STOCK_TRANSFER))));
                stockTransfer.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_STOCK_TRANSFER))));
                stockTransfer.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_STOCK_TRANSFER))));
                stockTransfer.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_STOCK_TRANSFER))));

                stockTransfers.add(stockTransfer);
            } while (c.moveToNext());
        }

        c.close();
        return stockTransfers;
    }


    public ArrayList<Product_StockTransfer>
    getAllProduct_StockTransfer() {
        ArrayList<Product_StockTransfer> stockTransfers = new ArrayList<Product_StockTransfer>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_STOCK_TRANSFER;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_StockTransfer stock = new Product_StockTransfer();
                stock.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_STOCK_TRANSFER))));
                stock.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_STOCK_TRANSFER))));
                stock.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_STOCK_TRANSFER))));
                stock.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_STOCK_TRANSFER))));
                stock.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_STOCK_TRANSFER))));
                stock.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_STOCK_TRANSFER))));
                stock.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_STOCK_TRANSFER))));
                stock.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_STOCK_TRANSFER))));
                stock.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_STOCK_TRANSFER))));
                stock.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_STOCK_TRANSFER))));
                stock.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_STOCK_TRANSFER))));
                stock.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_STOCK_TRANSFER))));
                stock.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_STOCK_TRANSFER))));
                stock.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_STOCK_TRANSFER))));
                stock.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_STOCK_TRANSFER))));
                stock.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_STOCK_TRANSFER))));
                stock.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_STOCK_TRANSFER))));
                stock.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_STOCK_TRANSFER))));
                stock.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_STOCK_TRANSFER))));
                stockTransfers.add(stock);
            } while (c.moveToNext());
        }

        c.close();
        return stockTransfers;
    }


    public ArrayList<Product_StockTransfer>
    getAllProduct_StockTransfer_Sync() {
        ArrayList<Product_StockTransfer> stockTransfers = new ArrayList<Product_StockTransfer>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_STOCK_TRANSFER;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_StockTransfer stock = new Product_StockTransfer();

                stock.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_STOCK_TRANSFER))));
                stock.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_STOCK_TRANSFER))));
                stock.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_STOCK_TRANSFER))));
                stock.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_STOCK_TRANSFER))));
                stock.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_STOCK_TRANSFER))));
                stock.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_STOCK_TRANSFER))));
                stock.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_STOCK_TRANSFER))));
                stock.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_STOCK_TRANSFER))));
                stock.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_STOCK_TRANSFER))));
                stock.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_STOCK_TRANSFER))));
                stock.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_STOCK_TRANSFER))));
                stock.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_STOCK_TRANSFER))));
                stock.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_STOCK_TRANSFER))));
                stock.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_STOCK_TRANSFER))));
                stock.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_STOCK_TRANSFER))));
                stock.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_STOCK_TRANSFER))));
                stock.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_STOCK_TRANSFER))));
                stock.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_STOCK_TRANSFER))));
                stockTransfers.add(stock);
            } while (c.moveToNext());
        }
        c.close();
        return stockTransfers;
    }

    public int updateProduct_StockTransfer(Product_StockTransfer stockTransfer, String PRODUCT_CD, String sl, String ea_unit, String stock) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_STOCK_TRANSFER, PRODUCT_CD);
        values.put(PRODUCT_CODE_STOCK_TRANSFER, stockTransfer.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_STOCK_TRANSFER, stockTransfer.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_STOCK_TRANSFER, stockTransfer.getEXPIRED_DATE());
        values.put(EA_UNIT_STOCK_TRANSFER, stockTransfer.getUNIT());
        values.put(QTY_SET_AVAILABLE_STOCK_TRANSFER, sl);

        // updating row
        return db.update(O_STOCK_TRANSFER, values, PRODUCT_CD_STOCK_TRANSFER + " = ?" + " AND " + EXPIRED_DATE_STOCK_TRANSFER + " = ?"
                        + " AND " + EA_UNIT_STOCK_TRANSFER + " = ?" + " AND " + STOCKIN_DATE_STOCK_TRANSFER + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(stockTransfer.getEXPIRED_DATE()), String.valueOf(ea_unit), String.valueOf(stock)});

    }



    public void deleteProduct_StockTransfer() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_STOCK_TRANSFER);
    }

    //END TABLE STOCK_TRANSER



    //DATABASE PUT STOCK_OUT
    public static final String O_STOCK_OUT = "O_STOCK_OUT";
    public static final String WAREHOUSE_POSITION_CD_STOCK_OUT = "WAREHOUSE_POSITION_CD_STOCK_OUT";
    public static final String AUTOINCREMENT_STOCK_OUT = "AUTOINCREMENT_STOCK_OUT";
    public static final String PRODUCT_CODE_STOCK_OUT = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_STOCK_OUT = "PRODUCT_NAME";
    public static final String PRODUCT_CD_STOCK_OUT = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_STOCK_OUT = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_STOCK_OUT = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_STOCK_OUT = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_STOCK_OUT = "STOCKIN_DATE";
    public static final String EA_UNIT_STOCK_OUT = "EA_UNIT";
    public static final String POSITION_FROM_STOCK_OUT = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_STOCK_OUT = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_STOCK_OUT = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_STOCK_OUT = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_STOCK_OUT = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_STOCK_OUT = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_STOCK_OUT = "UNIQUE_CODE";
    public static final String STOCKOUT_CD = "STOCKOUT_CD";
    public static final String LPN_CD_STOCK_OUT = "LPN_CD_STOCK_OUT";
    public static final String LPN_CODE_STOCK_OUT = "LPN_CODE_STOCK_OUT";
    public static final String LPN_FROM_STOCK_OUT = "LPN_FROM_STOCK_OUT";
    public static final String LPN_TO_STOCK_OUT = "LPN_TO_STOCK_OUT";

    public static final String CREATE_TABLE_O_STOCK_OUT = "CREATE TABLE "
            + O_STOCK_OUT + "("
            + AUTOINCREMENT_STOCK_OUT + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_STOCK_OUT + " TEXT,"
            + WAREHOUSE_POSITION_CD_STOCK_OUT + " TEXT,"
            + PRODUCT_NAME_STOCK_OUT + " TEXT,"
            + PRODUCT_CODE_STOCK_OUT + " TEXT,"
            + QTY_EA_AVAILABLE_STOCK_OUT + " TEXT,"
            + QTY_SET_AVAILABLE_STOCK_OUT + " TEXT,"
            + EXPIRED_DATE_STOCK_OUT + " TEXT,"
            + STOCKIN_DATE_STOCK_OUT + " TEXT,"
            + EA_UNIT_STOCK_OUT + " TEXT,"
            + POSITION_FROM_STOCK_OUT + " TEXT,"
            + POSITION_FROM_CODE_STOCK_OUT + " TEXT,"
            + POSITION_FROM_DESCRIPTION_STOCK_OUT + " TEXT,"
            + POSITION_TO_STOCK_OUT + " TEXT,"
            + POSITION_TO_CODE_STOCK_OUT + " TEXT,"
            + POSITION_TO_DESCRIPTION_STOCK_OUT + " TEXT,"
            + STOCKOUT_CD + " TEXT,"
            + UNIQUE_CODE_STOCK_OUT + " TEXT ,"
            + LPN_CD_STOCK_OUT + " TEXT ,"
            + LPN_CODE_STOCK_OUT + " TEXT ,"
            + LPN_FROM_STOCK_OUT + " TEXT ,"
            + LPN_TO_STOCK_OUT + " TEXT "
            + ")";


    public long CreateSTOCK_OUT(Product_StockOut stockOut) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(AUTOINCREMENT_STOCK_OUT, stockOut.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_STOCK_OUT, stockOut.getUNIT());
        values.put(PRODUCT_CODE_STOCK_OUT, stockOut.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_STOCK_OUT, stockOut.getPRODUCT_NAME());
        values.put(WAREHOUSE_POSITION_CD_STOCK_OUT, stockOut.getWAREHOUSE_POSITION_CD());
        values.put(PRODUCT_CD_STOCK_OUT, stockOut.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_STOCK_OUT, stockOut.getQTY());
        values.put(STOCKIN_DATE_STOCK_OUT, stockOut.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_STOCK_OUT, stockOut.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_STOCK_OUT, stockOut.getEXPIRED_DATE());
        values.put(EA_UNIT_STOCK_OUT, stockOut.getUNIT());
        values.put(POSITION_FROM_STOCK_OUT, stockOut.getPOSITION_FROM_CD());
        values.put(POSITION_TO_STOCK_OUT, stockOut.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_STOCK_OUT, stockOut.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_STOCK_OUT, stockOut.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_STOCK_OUT, stockOut.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_STOCK_OUT, stockOut.getPOSITION_TO_DESCRIPTION());
        values.put(STOCKOUT_CD, stockOut.getSTOCKOUT_CD());
        values.put(LPN_CODE_STOCK_OUT, stockOut.getLPN_CODE());
        values.put(LPN_FROM_STOCK_OUT, stockOut.getLPN_FROM());
        values.put(LPN_TO_STOCK_OUT, stockOut.getLPN_TO());
        // insert row
        long id = db.insert(O_STOCK_OUT, null, values);
        return id;
    }

    public int updatePositionFrom_StockOut_LPN(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_STOCK_OUT, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_STOCK_OUT, descreption);

        values.put(POSITION_FROM_CODE_STOCK_OUT, from);
        values.put(LPN_FROM_STOCK_OUT, from);


        // updating row
        return db.update(O_STOCK_OUT, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_STOCK_OUT + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }

    public int updatePositionFrom_StockOut(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_STOCK_OUT, wareHouse);
        values.put(POSITION_FROM_CODE_STOCK_OUT, from);
        values.put(LPN_FROM_STOCK_OUT, "");
        values.put(POSITION_FROM_DESCRIPTION_STOCK_OUT, descreption);


        // updating row
        return db.update(O_STOCK_OUT, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_STOCK_OUT + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }

    public int updatePositionTo_StockOut_LPN(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_STOCK_OUT, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_STOCK_OUT, descreption);
        values.put(LPN_TO_STOCK_OUT, to);

        values.put(POSITION_TO_CODE_STOCK_OUT, to);
        // updating row
        return db.update(O_STOCK_OUT, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_STOCK_OUT + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }


    public int updatePositionTo_StockOut(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_STOCK_OUT, wareHouse);
        values.put(POSITION_TO_CODE_STOCK_OUT, to);
        values.put(LPN_TO_STOCK_OUT, "");

        values.put(POSITION_TO_DESCRIPTION_STOCK_OUT, descreption);
        // updating row
        return db.update(O_STOCK_OUT, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_STOCK_OUT + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }


    public ArrayList<Product_StockOut>
    getoneProduct_stockout(String CD, String expDate, String ea_unit, String stockinDate, String stockout_cd) {
        ArrayList<Product_StockOut> stockOuts = new ArrayList<Product_StockOut>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_STOCK_OUT + " " + " WHERE "
                + PRODUCT_CD_STOCK_OUT + " = " + CD + " AND "
                + STOCKOUT_CD + " = " + stockout_cd + " AND "
                + EA_UNIT_STOCK_OUT + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_STOCK_OUT + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_STOCK_OUT + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_StockOut stockOut = new Product_StockOut();
                stockOut.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_STOCK_OUT))));
                stockOut.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_STOCK_OUT))));
                stockOut.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_STOCK_OUT))));
                stockOut.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_STOCK_OUT))));
                stockOut.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_STOCK_OUT))));
                stockOut.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_STOCK_OUT))));
                stockOut.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_STOCK_OUT))));
                stockOut.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_STOCK_OUT))));
                stockOut.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_STOCK_OUT))));
                stockOut.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_STOCK_OUT))));
                stockOuts.add(stockOut);
            } while (c.moveToNext());
        }

        c.close();
        return stockOuts;
    }


    public ArrayList<Product_StockOut>
    getAllProduct_Stockout_Sync(String stockout_cd) {
        ArrayList<Product_StockOut> stockOuts = new ArrayList<Product_StockOut>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , " +
                "REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, " +
                "REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_STOCK_OUT +
                " where " + STOCKOUT_CD + " = " + stockout_cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_StockOut stock = new Product_StockOut();

                stock.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_STOCK_OUT))));
                stock.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_STOCK_OUT))));
                stock.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_STOCK_OUT))));
                stock.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_STOCK_OUT))));
                stock.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_STOCK_OUT))));
                stock.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_STOCK_OUT))));
                stock.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_STOCK_OUT))));
                stock.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_STOCK_OUT))));
                stock.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_STOCK_OUT))));
                stock.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_STOCK_OUT))));
                stock.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_STOCK_OUT))));
                stock.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_STOCK_OUT))));
                stock.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_STOCK_OUT))));
                stock.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_STOCK_OUT))));
                stock.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_STOCK_OUT))));
                stock.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_STOCK_OUT))));
                stock.setSTOCKOUT_CD((c.getString(c
                        .getColumnIndex(STOCKOUT_CD))));
                stock.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_STOCK_OUT))));
                stock.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_STOCK_OUT))));
                stock.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_STOCK_OUT))));
                stockOuts.add(stock);
            } while (c.moveToNext());
        }

        c.close();
        return stockOuts;
    }

    public ArrayList<Product_StockOut>
    getAllProduct_Stockout(String stockout_cd) {
        ArrayList<Product_StockOut> stockOuts = new ArrayList<Product_StockOut>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_STOCK_OUT + " where " + STOCKOUT_CD + " = " + stockout_cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_StockOut stock = new Product_StockOut();
                stock.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_STOCK_OUT))));
                stock.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_STOCK_OUT))));
                stock.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_STOCK_OUT))));
                stock.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_STOCK_OUT))));
                stock.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_STOCK_OUT))));
                stock.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_STOCK_OUT))));
                stock.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_STOCK_OUT))));
                stock.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_STOCK_OUT))));
                stock.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_STOCK_OUT))));
                stock.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_STOCK_OUT))));
                stock.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_STOCK_OUT))));
                stock.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_STOCK_OUT))));
                stock.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_STOCK_OUT))));
                stock.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_STOCK_OUT))));
                stock.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_STOCK_OUT))));
                stock.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_STOCK_OUT))));
                stock.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_STOCK_OUT))));
                stock.setSTOCKOUT_CD((c.getString(c
                        .getColumnIndex(STOCKOUT_CD))));
                stock.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_STOCK_OUT))));
                stock.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_STOCK_OUT))));
                stock.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_STOCK_OUT))));
                stockOuts.add(stock);
            } while (c.moveToNext());
        }

        c.close();
        return stockOuts;
    }


    public int updateProduct_Stockout(Product_StockOut stockOut, String PRODUCT_CD, String sl, String ea_unit, String stock, String stockout_cd) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_STOCK_OUT, PRODUCT_CD);
        values.put(PRODUCT_CODE_STOCK_OUT, stockOut.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_STOCK_OUT, stockOut.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_STOCK_OUT, stockOut.getEXPIRED_DATE());
        values.put(EA_UNIT_STOCK_OUT, stockOut.getUNIT());
        values.put(QTY_SET_AVAILABLE_STOCK_OUT, sl);
        values.put(STOCKOUT_CD, stockout_cd);

        // updating row
        return db.update(O_STOCK_OUT, values, PRODUCT_CD_STOCK_OUT + " = ?" + " AND " + EXPIRED_DATE_STOCK_OUT + " = ?"
                        + " AND " + EA_UNIT_STOCK_OUT + " = ?" + " AND " + STOCKIN_DATE_STOCK_OUT + " = ?" + " AND " + STOCKOUT_CD + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(stockOut.getEXPIRED_DATE()), String.valueOf(ea_unit), String.valueOf(stock), stockout_cd});

    }


    public void deleteProduct_Stockout() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_STOCK_OUT);
    }

    //END TABLE O_STOCK_OUT

    //DATABASE LET_DOWN
    public static final String O_LET_DOWN = "O_LET_DOWN";
    public static final String AUTOINCREMENT_LETDOWN = "AUTOINCREMENT_LETDOWN";
    public static final String SUGGESTION_POSITION_LETDOWN_TO = "SUGGESTION_POSITION_TO";
    public static final String PRODUCT_CODE_LETDOWN = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_LETDOWN = "PRODUCT_NAME";
    public static final String PRODUCT_CD_LETDOWN = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_LETDOWN = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_LETDOWN = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_LETDOWN = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_LETDOWN = "STOCKIN_DATE";
    public static final String EA_UNIT_LETDOWN = "EA_UNIT";
    public static final String POSITION_FROM_LETDOWN = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_LETDOWN = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_LETDOWN = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_LETDOWN = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_LETDOWN = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_LETDOWN = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_LETDOWN = "UNIQUE_CODE";
    public static final String LPN_CD_LETDOWN = "LPN_CD_LETDOWN";
    public static final String LPN_CODE_LETDOWN = "LPN_CODE_LETDOWN";
    public static final String LPN_FROM_LETDOWN = "LPN_FROM_LETDOWN";
    public static final String LPN_TO_LETDOWN = "LPN_TO_LETDOWN";
    public static final String SUGGESTION_POSITION_LETDOWN = "SUGGESTION_POSITION";

    public static final String CREATE_TABLE_O_LET_DOWN = "CREATE TABLE "
            + O_LET_DOWN + "("
            + AUTOINCREMENT_LETDOWN + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_LETDOWN + " TEXT,"
            + PRODUCT_NAME_LETDOWN + " TEXT,"
            + PRODUCT_CODE_LETDOWN + " TEXT,"
            + QTY_EA_AVAILABLE_LETDOWN + " TEXT,"
            + QTY_SET_AVAILABLE_LETDOWN + " TEXT,"
            + EXPIRED_DATE_LETDOWN + " TEXT,"
            + STOCKIN_DATE_LETDOWN + " TEXT,"
            + EA_UNIT_LETDOWN + " TEXT,"
            + POSITION_FROM_LETDOWN + " TEXT,"
            + POSITION_FROM_CODE_LETDOWN + " TEXT,"
            + POSITION_FROM_DESCRIPTION_LETDOWN + " TEXT,"
            + POSITION_TO_LETDOWN + " TEXT,"
            + POSITION_TO_CODE_LETDOWN + " TEXT,"
            + POSITION_TO_DESCRIPTION_LETDOWN + " TEXT,"
            + UNIQUE_CODE_LETDOWN + " TEXT ,"
            + LPN_CD_LETDOWN + " TEXT ,"
            + LPN_CODE_LETDOWN + " TEXT ,"
            + LPN_FROM_LETDOWN + " TEXT ,"
            + LPN_TO_LETDOWN + " TEXT ,"
            + SUGGESTION_POSITION_LETDOWN + " TEXT ,"
            + SUGGESTION_POSITION_LETDOWN_TO + " TEXT"
            + ")";


    public long CreateLetDown(ProductLetDown qrcode) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(AUTOINCREMENT_LETDOWN, qrcode.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_LETDOWN, qrcode.getUNIT());
        values.put(PRODUCT_CODE_LETDOWN, qrcode.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_LETDOWN, qrcode.getPRODUCT_NAME());
        values.put(PRODUCT_CD_LETDOWN, qrcode.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_LETDOWN, qrcode.getQTY_SET_AVAILABLE());
        values.put(STOCKIN_DATE_LETDOWN, qrcode.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_LETDOWN, qrcode.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_LETDOWN, qrcode.getEXPIRED_DATE());
        values.put(EA_UNIT_LETDOWN, qrcode.getUNIT());
        values.put(POSITION_FROM_LETDOWN, qrcode.getPOSITION_FROM_CD());
        values.put(POSITION_TO_LETDOWN, qrcode.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_LETDOWN, qrcode.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_LETDOWN, qrcode.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_LETDOWN, qrcode.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_LETDOWN, qrcode.getPOSITION_TO_DESCRIPTION());
        values.put(LPN_CODE_LETDOWN, qrcode.getLPN_CODE());
        values.put(LPN_FROM_LETDOWN, qrcode.getLPN_FROM());
        values.put(LPN_TO_LETDOWN, qrcode.getLPN_TO());
        values.put(SUGGESTION_POSITION_LETDOWN, qrcode.getSUGGESTION_POSITION());
        values.put(SUGGESTION_POSITION_LETDOWN_TO, qrcode.getSUGGESTION_POSITION_TO());

        // insert row
        long id = db.insert(O_LET_DOWN, null, values);
        return id;
    }

    public ArrayList<ProductLetDown>
    getoneProductLetDown(String CD, String expDate, String ea_unit, String stockinDate) {
        ArrayList<ProductLetDown> qrcode = new ArrayList<ProductLetDown>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_LET_DOWN + " " + " WHERE "
                + PRODUCT_CD_LETDOWN + " = " + CD + " AND "
                + EA_UNIT_LETDOWN + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_LETDOWN + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_LETDOWN + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                ProductLetDown qrcodeq = new ProductLetDown();
                qrcodeq.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_LETDOWN))));
                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_LETDOWN))));
                qrcodeq.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_LETDOWN))));
                qrcodeq.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_LETDOWN))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_LETDOWN))));
                qrcodeq.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_LETDOWN))));
                qrcodeq.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_LETDOWN))));
                qrcodeq.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_LETDOWN))));
                qrcodeq.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_LETDOWN))));


                qrcode.add(qrcodeq);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }



    public ArrayList<ProductLetDown>
    getAllProductLetDown() {
        ArrayList<ProductLetDown> putaway = new ArrayList<ProductLetDown>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_LET_DOWN;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                ProductLetDown qrcode = new ProductLetDown();
                qrcode.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_LETDOWN))));
                qrcode.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_LETDOWN))));
                qrcode.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_LETDOWN))));
                qrcode.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_LETDOWN))));
                qrcode.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_LETDOWN))));
                qrcode.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_LETDOWN))));
                qrcode.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_LETDOWN))));
                qrcode.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_LETDOWN))));
                qrcode.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_LETDOWN))));
                qrcode.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_LETDOWN))));
                qrcode.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_LETDOWN))));
                qrcode.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_LETDOWN))));
                qrcode.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_LETDOWN))));
                qrcode.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_LETDOWN))));
                qrcode.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_LETDOWN))));
                qrcode.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_LETDOWN))));
                qrcode.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_LETDOWN))));
                qrcode.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_LETDOWN))));
                qrcode.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_LETDOWN))));
                qrcode.setSUGGESTION_POSITION((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_LETDOWN))));
                qrcode.setSUGGESTION_POSITION_TO((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_LETDOWN_TO))));
                putaway.add(qrcode);
            } while (c.moveToNext());
        }

        c.close();
        return putaway;
    }


    public ArrayList<ProductLetDown>
    getAllProduct_LetDown_Sync() {
        ArrayList<ProductLetDown> letdowns = new ArrayList<ProductLetDown>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_LET_DOWN;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                ProductLetDown qrcode = new ProductLetDown();

                qrcode.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_LETDOWN))));
                qrcode.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_LETDOWN))));
                qrcode.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_LETDOWN))));
                qrcode.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_LETDOWN))));
                qrcode.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_LETDOWN))));
                qrcode.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_LETDOWN))));
                qrcode.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_LETDOWN))));
                qrcode.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_LETDOWN))));
                qrcode.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_LETDOWN))));
                qrcode.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_LETDOWN))));
                qrcode.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_LETDOWN))));
                qrcode.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_LETDOWN))));
                qrcode.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_LETDOWN))));
                qrcode.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_LETDOWN))));
                qrcode.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_LETDOWN))));
                qrcode.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_LETDOWN))));
                qrcode.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_LETDOWN))));
                qrcode.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_LETDOWN))));
                qrcode.setSUGGESTION_POSITION((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_LETDOWN))));
                qrcode.setSUGGESTION_POSITION_TO((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_LETDOWN_TO))));
                letdowns.add(qrcode);
            } while (c.moveToNext());
        }
        c.close();
        return letdowns;
    }

    public int updateProduct_LetDown(ProductLetDown letdown, String PRODUCT_CD, String sl, String ea_unit, String stock) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_LETDOWN, PRODUCT_CD);
        values.put(PRODUCT_CODE_LETDOWN, letdown.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_LETDOWN, letdown.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_LETDOWN, letdown.getEXPIRED_DATE());
        values.put(EA_UNIT_LETDOWN, letdown.getUNIT());
        values.put(QTY_SET_AVAILABLE_LETDOWN, sl);

        // updating row
        return db.update(O_LET_DOWN, values, PRODUCT_CD_LETDOWN + " = ?" + " AND " + EXPIRED_DATE_LETDOWN + " = ?"
                        + " AND " + EA_UNIT_LETDOWN + " = ?" + " AND " + STOCKIN_DATE_LETDOWN + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(letdown.getEXPIRED_DATE()), String.valueOf(ea_unit), String.valueOf(stock)});

    }


    public void deleteProduct_Letdown_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_LET_DOWN, AUTOINCREMENT_LETDOWN + " = ?"
                , new String[]{String.valueOf(productCode)});

    }

    public void deleteProduct_PutAway_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_PUT_AWAY, AUTOINCREMENT_PUT_AWAY + " = ?" ,
                new String[]{String.valueOf(productCode)});

    }

    public void deleteProduct_StockTransfer_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_STOCK_TRANSFER, AUTOINCREMENT_STOCK_TRANSFER + " = ?",
                new String[]{String.valueOf(productCode)});

    }

    public void deleteProduct_remove_lpn_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_REMOVE_LPN, AUTOINCREMENT_REMOVE_LPN + " = ?"
                , new String[]{String.valueOf(productCode)});

    }

    public void deleteProduct_LoadPallet_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_LOAD_PALLET, AUTOINCREMENT_LOAD_PALLET + " = ?"
                , new String[]{String.valueOf(productCode)
                });

    }

    public void deleteProduct_PO_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_QRCODE, AUTOINCREMENT_PO + " = ?"
                , new String[]{String.valueOf(productCode)
        });

    }

    public void deleteProduct_PickList_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_PICK_LIST, AUTOINCREMENT_PICKLIST + " = ?"
                , new String[]{String.valueOf(productCode)});

    }

    public void deleteProduct_Return_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_RETURN_WAREHOUSE, AUTOINCREMENT_RETURN_WAREHOUSE + " = ?" ,
                new String[]{String.valueOf(productCode)});

    }

    public void deleteProduct_Master_Pick_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_MASTER_PICK, AUTOINCREMENT_MASTER_PICK + " = ?",
                new String[]{String.valueOf(productCode)});

    }

    public void deleteProduct_StockOut_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_STOCK_OUT, AUTOINCREMENT_STOCK_OUT + " = ?" ,
                new String[]{String.valueOf(productCode)});

    }

    public void deleteProduct_Warehouse_Adjustment_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_WAREHOUSE_ADJUSTMENT, AUTOINCREMENT_WAREHOUSE_ADJUSTMENT + " = ?" ,
                new String[]{String.valueOf(productCode)});

    }



    public void deleteProduct_Letdown() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_LET_DOWN);
    }

    public int updatePositionFromLetDown_LPN(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_LETDOWN, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_LETDOWN, descreption);
        values.put(POSITION_FROM_CODE_LETDOWN, from);
        values.put(LPN_FROM_LETDOWN, from);

        // updating row
        return db.update(O_LET_DOWN, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? "
                        + " AND " + STOCKIN_DATE_LETDOWN + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }

    public int updatePositionFromLetDown(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_LETDOWN, wareHouse);
        values.put(POSITION_FROM_CODE_LETDOWN, from);
        values.put(LPN_FROM_LETDOWN, "");
        values.put(POSITION_FROM_DESCRIPTION_LETDOWN, descreption);

        // updating row
        return db.update(O_LET_DOWN, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? "
                        + " AND " + STOCKIN_DATE_LETDOWN + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }

    public int updatePositionToLetDown_LPN(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_LETDOWN, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_LETDOWN, descreption);
        values.put(POSITION_TO_CODE_LETDOWN, to);
        values.put(LPN_TO_LETDOWN, to);

        values.put(POSITION_TO_CODE_LETDOWN, to);
        // updating row
        return db.update(O_LET_DOWN, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_LETDOWN + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }

    public int updatePositionToLetDown(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_LETDOWN, wareHouse);
        values.put(POSITION_TO_CODE_LETDOWN, to);
        values.put(LPN_TO_LETDOWN, "");
        values.put(POSITION_TO_DESCRIPTION_LETDOWN, descreption);
        // updating row
        return db.update(O_LET_DOWN, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_LETDOWN + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }
    //END TABLE O_LET_DOWN

    //database from table PUT AWAY
    public static final String O_PUT_AWAY = "O_PUT_AWAY";
    public static final String AUTOINCREMENT_PUT_AWAY = "AUTOINCREMENT_PUT_AWAY";
    public static final String PRODUCT_CODE_PUTAWAY = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_PUTAWAY = "PRODUCT_NAME";
    public static final String PRODUCT_CD_PUTAWAY = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_PUTAWAY = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_PUTAWAY = "STOCKIN_DATE";
    public static final String EA_UNIT_PUTAWAY = "EA_UNIT";
    public static final String POSITION_FROM_PUTAWAY = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_PUTAWAY = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_PUTAWAY = "UNIQUE_CODE";
    public static final String POSITION_DESCRIPTION_PUT_AWAY = "POSITION_DESCRIPTION";
    public static final String LPN_CD_PUTAWAY = "LPN_CD_PUTAWAY";
    public static final String LPN_CODE_PUTAWAY = "LPN_CODE_PUTAWAY";
    public static final String LPN_FROM_PUTAWAY = "LPN_FROM_PUTAWAY";
    public static final String LPN_TO_PUTAWAY = "LPN_TO_PUTAWAY";
    public static final String SUGGESTION_POSITION_PUTAWAY = "SUGGESTION_POSITION";


    public static final String CREATE_TABLE_O_PUT_AWAY = "CREATE TABLE "
            + O_PUT_AWAY + "("
            + AUTOINCREMENT_PUT_AWAY + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + UNIQUE_CODE_PUTAWAY + " TEXT,"
            + PRODUCT_CODE_PUTAWAY + " TEXT,"
            + PRODUCT_NAME_PUTAWAY + " TEXT,"
            + QTY_SET_AVAILABLE + " TEXT,"
            + STOCKIN_DATE_PUTAWAY + " TEXT,"
            + PRODUCT_CD_PUTAWAY + " TEXT,"
            + QTY_EA_AVAILABLE + " TEXT,"
            + POSITION_FROM_PUTAWAY + " TEXT,"
            + EXPIRED_DATE_PUTAWAY + " TEXT,"
            + EA_UNIT_PUTAWAY + " TEXT,"
            + POSITION_FROM_CODE + " TEXT,"
            + POSITION_TO_CODE + " TEXT,"
            + POSITION_TO_PUTAWAY + " TEXT,"
            + POSITION_FROM_DESCRIPTION + " TEXT,"
            + POSITION_TO_DESCRIPTION + " TEXT ,"
            + SUGGESTION_POSITION_PUTAWAY + " TEXT ,"
            + LPN_CD_PUTAWAY + " TEXT ,"
            + LPN_CODE_PUTAWAY + " TEXT ,"
            + LPN_FROM_PUTAWAY + " TEXT ,"
            + LPN_TO_PUTAWAY + " TEXT "
            + ")";


    public long CreatePutAway(Product_PutAway qrcode) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(AUTOINCREMENT_PUT_AWAY,qrcode.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_PUTAWAY, qrcode.getUNIQUE_CODE_PUTAWAY());
        values.put(PRODUCT_CODE_PUTAWAY, qrcode.getPRODUCT_CODE_PUTAWAY());
        values.put(PRODUCT_NAME_PUTAWAY, qrcode.getPRODUCT_NAME_PUTAWAY());
        values.put(PRODUCT_CD_PUTAWAY, qrcode.getPRODUCT_CD_PUTAWAY());
        values.put(QTY_SET_AVAILABLE, qrcode.getQTY_SET_AVAILABLE());
        values.put(STOCKIN_DATE_PUTAWAY, qrcode.getSTOCKIN_DATE_PUTAWAY());
        values.put(QTY_EA_AVAILABLE, qrcode.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_PUTAWAY, qrcode.getEXPIRED_DATE_PUTAWAY());
        values.put(EA_UNIT_PUTAWAY, qrcode.getEA_UNIT_PUTAWAY());
        values.put(POSITION_FROM_PUTAWAY, qrcode.getPOSITION_FROM_PUTAWAY());
        values.put(POSITION_TO_PUTAWAY, qrcode.getPOSITION_TO_PUTAWAY());
        values.put(POSITION_FROM_CODE, qrcode.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE, qrcode.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION, qrcode.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION, qrcode.getPOSITION_TO_DESCRIPTION());
        values.put(LPN_FROM_PUTAWAY, qrcode.getLPN_FROM());
        values.put(LPN_TO_PUTAWAY, qrcode.getLPN_TO());
        values.put(LPN_CODE_PUTAWAY, qrcode.getLPN_CODE());
        values.put(SUGGESTION_POSITION_PUTAWAY, qrcode.getSUGGESTION_POSITION());
        // insert row
        long id = db.insert(O_PUT_AWAY, null, values);

        return id;
    }

    public ArrayList<Product_PutAway>
    getoneProduct_PutAway(String CD, String expDate, String ea_unit, String stockinDate) {
        ArrayList<Product_PutAway> qrcode = new ArrayList<Product_PutAway>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_PUT_AWAY + " " + " WHERE "
                + PRODUCT_CD_PUTAWAY + " = " + CD + " AND "
                + EA_UNIT_PUTAWAY + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_PUTAWAY + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_PUTAWAY + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_PutAway qrcodeq = new Product_PutAway();
                qrcodeq.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PUT_AWAY))));
                qrcodeq.setPRODUCT_CD_PUTAWAY((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PUTAWAY))));
                qrcodeq.setPRODUCT_CODE_PUTAWAY((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PUTAWAY))));
                qrcodeq.setPRODUCT_NAME_PUTAWAY((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PUTAWAY))));
                qrcodeq.setEXPIRED_DATE_PUTAWAY((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PUTAWAY))));
                qrcodeq.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE))));
                qrcodeq.setEA_UNIT_PUTAWAY((c.getString(c
                        .getColumnIndex(EA_UNIT_PUTAWAY))));
                qrcodeq.setPOSITION_FROM_PUTAWAY((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE))));
                qrcodeq.setPOSITION_TO_PUTAWAY((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE))));


                qrcode.add(qrcodeq);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }


    public ArrayList<Product_PutAway>
    getAllProduct_PutAway() {
        ArrayList<Product_PutAway> putaway = new ArrayList<Product_PutAway>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_PUT_AWAY;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_PutAway qrcode_putaway = new Product_PutAway();
                qrcode_putaway.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PUT_AWAY))));
                qrcode_putaway.setUNIQUE_CODE_PUTAWAY((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_PUTAWAY))));
                qrcode_putaway.setPRODUCT_CODE_PUTAWAY((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PUTAWAY))));
                qrcode_putaway.setPRODUCT_NAME_PUTAWAY((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PUTAWAY))));
                qrcode_putaway.setPRODUCT_CD_PUTAWAY((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PUTAWAY))));
                qrcode_putaway.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE))));
                qrcode_putaway.setSTOCKIN_DATE_PUTAWAY((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_PUTAWAY))));
                qrcode_putaway.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE))));
                qrcode_putaway.setEXPIRED_DATE_PUTAWAY((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PUTAWAY))));
                qrcode_putaway.setEA_UNIT_PUTAWAY((c.getString(c
                        .getColumnIndex(EA_UNIT_PUTAWAY))));
                qrcode_putaway.setPOSITION_FROM_PUTAWAY((c.getString(c
                        .getColumnIndex(POSITION_FROM_PUTAWAY))));
                qrcode_putaway.setPOSITION_TO_PUTAWAY((c.getString(c
                        .getColumnIndex(POSITION_TO_PUTAWAY))));
                qrcode_putaway.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE))));
                qrcode_putaway.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE))));
                qrcode_putaway.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION))));
                qrcode_putaway.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION))));
                qrcode_putaway.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_PUTAWAY))));
                qrcode_putaway.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_PUTAWAY))));
                qrcode_putaway.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_PUTAWAY))));
                qrcode_putaway.setSUGGESTION_POSITION((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_PUTAWAY))));

                putaway.add(qrcode_putaway);
            } while (c.moveToNext());
        }

        c.close();
        return putaway;
    }

    public ArrayList<Product_PutAway>
    getAllProduct_PutAway_Sync() {
        ArrayList<Product_PutAway> putaway = new ArrayList<Product_PutAway>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE, REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_PUT_AWAY;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_PutAway qrcode_putaway = new Product_PutAway();
                qrcode_putaway.setUNIQUE_CODE_PUTAWAY((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_PUTAWAY))));
                qrcode_putaway.setPRODUCT_CODE_PUTAWAY((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PUTAWAY))));
                qrcode_putaway.setPRODUCT_NAME_PUTAWAY((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PUTAWAY))));
                qrcode_putaway.setPRODUCT_CD_PUTAWAY((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PUTAWAY))));
                qrcode_putaway.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE))));
                qrcode_putaway.setSTOCKIN_DATE_PUTAWAY((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_PUTAWAY))));
                qrcode_putaway.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE))));
                qrcode_putaway.setEXPIRED_DATE_PUTAWAY((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PUTAWAY))));
                qrcode_putaway.setEA_UNIT_PUTAWAY((c.getString(c
                        .getColumnIndex(EA_UNIT_PUTAWAY))));
                qrcode_putaway.setPOSITION_FROM_PUTAWAY((c.getString(c
                        .getColumnIndex(POSITION_FROM_PUTAWAY))));
                qrcode_putaway.setPOSITION_TO_PUTAWAY((c.getString(c
                        .getColumnIndex(POSITION_TO_PUTAWAY))));
                qrcode_putaway.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE))));
                qrcode_putaway.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE))));
                qrcode_putaway.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION))));
                qrcode_putaway.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION))));
                qrcode_putaway.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_PUTAWAY))));
                qrcode_putaway.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_PUTAWAY))));
                qrcode_putaway.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_PUTAWAY))));
                qrcode_putaway.setSUGGESTION_POSITION((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_PUTAWAY))));
                putaway.add(qrcode_putaway);
            } while (c.moveToNext());
        }

        c.close();
        return putaway;
    }

    public int updateProduct_PutAway(Product_PutAway putAway, String PRODUCT_CD, String sl, String ea_unit, String stock) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_PUTAWAY, PRODUCT_CD);
        values.put(PRODUCT_CODE_PUTAWAY, putAway.getPRODUCT_CODE_PUTAWAY());
        values.put(PRODUCT_NAME_PUTAWAY, putAway.getPRODUCT_NAME_PUTAWAY());
        values.put(EXPIRED_DATE_PUTAWAY, putAway.getEXPIRED_DATE_PUTAWAY());
        values.put(EA_UNIT_PUTAWAY, putAway.getEA_UNIT_PUTAWAY());
        values.put(QTY_SET_AVAILABLE, sl);


        // updating row
        return db.update(O_PUT_AWAY, values, PRODUCT_CD_PUTAWAY + " = ?" + " AND " + EXPIRED_DATE_PUTAWAY + " = ?"
                        + " AND " + EA_UNIT_PUTAWAY + " = ?" + " AND " + STOCKIN_DATE_PUTAWAY + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(putAway.getEXPIRED_DATE_PUTAWAY()), String.valueOf(ea_unit), String.valueOf(stock)});

    }


    public void deleteProduct_PutAway() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_PUT_AWAY);
    }

    //end table PUT AWAY

    //Table O_EA_UNIT để chứa dữ liệu quét exp lần đầu
    public static final String O_EA_UNIT = "O_EA_UNIT";
    public static final String EA_UNIT_TAM = "EA_UNIT_TAM";
    public static final String CREATE_TABLE_O_EA_UNIT = "CREATE TABLE "
            + O_EA_UNIT + "("
            + EA_UNIT_TAM + " TEXT" + ")";

    public long CreateEa_Unit(Ea_Unit_Tam ea_unit) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(EA_UNIT_TAM, ea_unit.getEA_UNIT_TAM());
        // insert row
        long id = db.insert(O_EA_UNIT, null, values);
        return id;
    }

    public ArrayList<Ea_Unit_Tam>
    getallEa_Unit() {
        ArrayList<Ea_Unit_Tam> ea_unit = new ArrayList<Ea_Unit_Tam>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT DISTINCT * FROM " + O_EA_UNIT;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Ea_Unit_Tam ea_unitt = new Ea_Unit_Tam();
                ea_unitt.setEA_UNIT_TAM((c.getString(c
                        .getColumnIndex(EA_UNIT_TAM))));
                ea_unit.add(ea_unitt);
            } while (c.moveToNext());
        }

        c.close();
        return ea_unit;
    }

    public void deleteallEa_Unit() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_EA_UNIT);
    }

    //END TABLE O_EA_UNIT


    //Table O_EXP để chứa dữ liệu quét exp lần đầu
    public static final String O_EXP = "O_EXP";
    public static final String EXPIRED_DATE_TAM = "EXPIRED_DATE_TAM";
    public static final String STOCKIN_DATE_TAM = "STOCKIN_DATE_TAM";
    public static final String CREATE_TABLE_O_EXP = "CREATE TABLE "
            + O_EXP + "("
            + EXPIRED_DATE_TAM + " TEXT" + ")";

    public long CreateExp_date(Exp_Date_Tam exp) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        //values.put(QRCODE, qrcode.getQRCODE());
        values.put(EXPIRED_DATE_TAM, exp.getEXPIRED_DATE_TAM());
        // values.put(STOCKIN_DATE_TAM, exp.getSTOCKIN_DATE_TAM());
        // insert row
        long id = db.insert(O_EXP, null, values);
        return id;
    }

    public ArrayList<Exp_Date_Tam>
    getallExp_date() {
        ArrayList<Exp_Date_Tam> exp = new ArrayList<Exp_Date_Tam>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT DISTINCT CASE WHEN EXPIRED_DATE_TAM = '' THEN '------' ELSE EXPIRED_DATE_TAM END AS EXPIRED_DATE_TAM FROM " + O_EXP;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Exp_Date_Tam expd = new Exp_Date_Tam();
                expd.setEXPIRED_DATE_TAM((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_TAM))));
//                expd.setSTOCKIN_DATE_TAM((c.getString(c
//                        .getColumnIndex(STOCKIN_DATE_TAM))));
                exp.add(expd);
            } while (c.moveToNext());
        }

        c.close();
        return exp;
    }

    public void deleteallExp_date() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_EXP);
    }

    //End table EXP

    //Table O_QRCODE dành cho Nhập Kho trong THỦ KHO

    public static final String O_QRCODE = "O_QRCODE";
    public static final String MANUFACTURING_DATE_WST = "MANUFACTURING_DATE_WST";
    public static final String AUTOINCREMENT_PO = "AUTOINCREMENT_PO";
    public static final String PRODUCT_CODE = "PRODUCT_CODE";
    public static final String STOCK_RECEIPT_CD = "STOCK_RECEIPT_CD";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String PRODUCT_CD = "PRODUCT_CD";
    public static final String WAREHOUSE_POSITION_CD = "WAREHOUSE_POSITION_CD";
    public static final String SET_UNIT = "SET_UNIT";
    public static final String EA_UNIT = "EA_UNIT";
    public static final String POSITION_FROM = "POSITION_FROM";
    public static final String POSITION_TO = "POSITION_TO";
    public static final String EXPIRED_DATE = "EXPIRED_DATE";
    public static final String POSITION_CODE = "POSITION_CODE";
    public static final String POSITION_DESCRIPTION = "POSITION_DESCRIPTION";
    public static final String STOCKIN_DATE = "STOCKIN_DATE";


    public static final String CREATE_TABLE_O_QRCODE = "CREATE TABLE "
            + O_QRCODE + "("
            + AUTOINCREMENT_PO + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + STOCK_RECEIPT_CD + " TEXT,"
            + MANUFACTURING_DATE_WST + " TEXT,"
            + PRODUCT_CODE + " TEXT,"
            + PRODUCT_CD + " TEXT,"
            + PRODUCT_NAME + " TEXT,"
            + WAREHOUSE_POSITION_CD + " TEXT,"
            + EXPIRED_DATE + " TEXT,"
            + SET_UNIT + " TEXT,"
            + EA_UNIT + " TEXT,"
            + POSITION_FROM + " TEXT,"
            + POSITION_TO + " TEXT,"
            + POSITION_CODE + " TEXT,"
            + POSITION_DESCRIPTION + " TEXT,"
            + STOCKIN_DATE + " TEXT"
            + ")";


    public long CreateProduct_Qrcode(Product_Qrcode qrcode) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(AUTOINCREMENT_PO, qrcode.getAUTOINCREMENT());
        values.put(PRODUCT_CD, qrcode.getPRODUCT_CD());
        values.put(MANUFACTURING_DATE_WST, qrcode.getMANUFACTURING_DATE());
        values.put(STOCK_RECEIPT_CD, qrcode.getSTOCK_RECEIPT_CD());
        values.put(PRODUCT_CODE, qrcode.getPRODUCT_CODE());
        values.put(PRODUCT_NAME, qrcode.getPRODUCT_NAME());
        values.put(WAREHOUSE_POSITION_CD, qrcode.getWAREHOUSE_POSITION_CD());
        values.put(EXPIRED_DATE, qrcode.getEXPIRED_DATE());
        values.put(SET_UNIT, qrcode.getSL_SET());
        values.put(EA_UNIT, qrcode.getEA_UNIT());
        values.put(POSITION_FROM, qrcode.getPRODUCT_FROM());
        values.put(POSITION_TO, qrcode.getPRODUCT_TO());
        values.put(POSITION_CODE, qrcode.getPOSITION_CODE());
        values.put(POSITION_DESCRIPTION, qrcode.getPOSITION_DESCRIPTION());
        values.put(STOCKIN_DATE, qrcode.getSTOCKIN_DATE());
        // insert row
        long id = db.insert(O_QRCODE, null, values);
        return id;
    }

    public int updateProduct_Qrcode(Product_Qrcode qrcode, String PRODUCT_CD, String stock, String exp_date, String ea_unit , String stockindate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put("PRODUCT_CD", PRODUCT_CD);
        values.put(STOCK_RECEIPT_CD, qrcode.getSTOCK_RECEIPT_CD());
        values.put(PRODUCT_CODE, qrcode.getPRODUCT_CODE());
        values.put(PRODUCT_NAME, qrcode.getPRODUCT_NAME());
        values.put(MANUFACTURING_DATE_WST, qrcode.getMANUFACTURING_DATE());
        values.put(EXPIRED_DATE, exp_date);
        values.put(WAREHOUSE_POSITION_CD, qrcode.getWAREHOUSE_POSITION_CD());
        values.put(SET_UNIT, qrcode.getSL_SET());
        values.put(EA_UNIT, qrcode.getEA_UNIT());
        values.put(POSITION_FROM, qrcode.getPRODUCT_FROM());
        values.put(POSITION_TO, qrcode.getPRODUCT_TO());
        values.put(STOCKIN_DATE, qrcode.getSTOCKIN_DATE());
        // updating row
        return db.update(O_QRCODE, values, "PRODUCT_CD" + " = ?" + " AND STOCK_RECEIPT_CD = ? AND EXPIRED_DATE = ? AND STOCKIN_DATE = ?  AND EA_UNIT = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(stock), String.valueOf(exp_date),String.valueOf(stockindate) ,String.valueOf(ea_unit)});

    }

    public int updatePositionFrom_LPN(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(LPN_CD_PUTAWAY, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION, descreption);
        values.put(POSITION_FROM_CODE, from);
        values.put(LPN_FROM_PUTAWAY, from);

        // updating row
        return db.update(O_PUT_AWAY, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_PUTAWAY + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }


    public int updatePositionFrom(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_PUTAWAY, wareHouse);
        values.put(POSITION_FROM_CODE, from);
        values.put(LPN_FROM_PUTAWAY, "");
        values.put(POSITION_FROM_DESCRIPTION, descreption);

        // updating row
        return db.update(O_PUT_AWAY, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_PUTAWAY + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }
    public int updatePositionFrom_StockTransfer_LPN(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_STOCK_TRANSFER, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_STOCK_TRANSFER, descreption);
        values.put(POSITION_FROM_CODE_STOCK_TRANSFER, from);

        values.put(LPN_FROM_STOCK_TRANSFER, from);

        // updating row
        return db.update(O_STOCK_TRANSFER, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_STOCK_TRANSFER + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});
    }


    public int updatePositionFrom_StockTransfer(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_STOCK_TRANSFER, wareHouse);
        values.put(POSITION_FROM_CODE_STOCK_TRANSFER, from);
        values.put(LPN_FROM_STOCK_TRANSFER, "");
        values.put(POSITION_FROM_DESCRIPTION_STOCK_TRANSFER, descreption);

        // updating row
        return db.update(O_STOCK_TRANSFER, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_STOCK_TRANSFER + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});
    }

    public int updatePositionTo_LPN(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_PUTAWAY, wareHouse);
        values.put(POSITION_TO_DESCRIPTION, descreption);
        values.put(LPN_TO_PUTAWAY, to);

        values.put(POSITION_TO_CODE_LOAD_PALLET, to);
        // updating row
        return db.update(O_PUT_AWAY, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_PUTAWAY + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }

    public int updatePositionTo(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_PUTAWAY, wareHouse);
        values.put(POSITION_TO_CODE, to);
        values.put(LPN_TO_PUTAWAY, "");

        values.put(POSITION_TO_DESCRIPTION, descreption);
        // updating row
        return db.update(O_PUT_AWAY, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_PUTAWAY + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }
    public int updatePositionTo_StockTransfer_LPN(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_STOCK_TRANSFER, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_STOCK_TRANSFER, descreption);
        values.put(LPN_TO_STOCK_TRANSFER, to);

        values.put(POSITION_TO_CODE_STOCK_TRANSFER, to);
        // updating row
        return db.update(O_STOCK_TRANSFER, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_STOCK_TRANSFER + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }

    public int updatePositionTo_StockTransfer(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_STOCK_TRANSFER, wareHouse);
        values.put(POSITION_TO_CODE_STOCK_TRANSFER, to);
        values.put(LPN_TO_STOCK_TRANSFER, "");

        values.put(POSITION_TO_DESCRIPTION_STOCK_TRANSFER, descreption);
        // updating row
        return db.update(O_STOCK_TRANSFER, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_STOCK_TRANSFER + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }


    public int updateProduct_Qrcode_SL(Product_Qrcode qrcode, String PRODUCT_CD, String stock, String sl, String ea_unit, String stocking_date) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put("PRODUCT_CD", PRODUCT_CD);
        values.put(STOCK_RECEIPT_CD, qrcode.getSTOCK_RECEIPT_CD());
        values.put(PRODUCT_CODE, qrcode.getPRODUCT_CODE());
        values.put(PRODUCT_NAME, qrcode.getPRODUCT_NAME());
        values.put(MANUFACTURING_DATE_WST, qrcode.getMANUFACTURING_DATE());
        values.put(WAREHOUSE_POSITION_CD,qrcode.getWAREHOUSE_POSITION_CD());
        values.put(EXPIRED_DATE, qrcode.getEXPIRED_DATE());
        values.put(EA_UNIT, qrcode.getEA_UNIT());
        values.put(SET_UNIT, sl);
        values.put(POSITION_FROM, qrcode.getPRODUCT_FROM());
        values.put(POSITION_TO, qrcode.getPRODUCT_TO());
        values.put(STOCKIN_DATE, qrcode.getSTOCKIN_DATE());

        Log.d("so luong : ", sl);
        // updating row
        return db.update(O_QRCODE, values, "PRODUCT_CD" + " = ?" + " AND STOCK_RECEIPT_CD = ?"
                        + " AND EXPIRED_DATE =  ?" + " AND STOCKIN_DATE = ?" + " AND EA_UNIT =  ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(stock), String.valueOf(qrcode.getEXPIRED_DATE()), String.valueOf(stocking_date), String.valueOf(ea_unit)});


    }


    public ArrayList<Product_Qrcode>
    getoneProduct_Qrcode(String CD, String stock, String expDate, String ea_unit, String stockinDate) {
        ArrayList<Product_Qrcode> qrcode = new ArrayList<Product_Qrcode>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_QRCODE + " " + " WHERE "
                + PRODUCT_CD + " = " + CD + " AND "
                + STOCK_RECEIPT_CD + " = " + stock + " AND "
                + EA_UNIT + " like " + " '%" + ea_unit + "%' AND "
                + EXPIRED_DATE + " like " + " '%" + expDate + "%' AND "
                + STOCKIN_DATE + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Qrcode qrcodeq = new Product_Qrcode();
                qrcodeq.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PO))));
                qrcodeq.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_WST))));
                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD))));
                qrcodeq.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE))));
                qrcodeq.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME))));
                qrcodeq.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE))));
                qrcodeq.setSL_SET((c.getString(c
                        .getColumnIndex(SET_UNIT))));
                qrcodeq.setEA_UNIT((c.getString(c
                        .getColumnIndex(EA_UNIT))));
                qrcodeq.setSTOCK_RECEIPT_CD((c.getString(c
                        .getColumnIndex(STOCK_RECEIPT_CD))));
                qrcodeq.setPRODUCT_FROM((c.getString(c
                        .getColumnIndex(POSITION_FROM))));
                qrcodeq.setPRODUCT_TO((c.getString(c
                        .getColumnIndex(POSITION_TO))));
                qrcodeq.setPOSITION_CODE((c.getString(c
                        .getColumnIndex(POSITION_CODE))));
                qrcodeq.setPOSITION_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_DESCRIPTION))));
                qrcodeq.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE))));


                qrcode.add(qrcodeq);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }




    public ArrayList<Product_Qrcode>
    getAllProduct_Qrcode(String stock) {
        if (stock == "" || stock == null) {
            return null;
        }
        ArrayList<Product_Qrcode> qrcode = new ArrayList<Product_Qrcode>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT *  FROM " + O_QRCODE + " WHERE " + STOCK_RECEIPT_CD + " = " + stock;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Qrcode qrcodeq = new Product_Qrcode();
                qrcodeq.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PO))));
                qrcodeq.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_WST))));
                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD))));
                qrcodeq.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE))));
                qrcodeq.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME))));
                qrcodeq.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE))));
                qrcodeq.setSL_SET((c.getString(c
                        .getColumnIndex(SET_UNIT))));
                qrcodeq.setEA_UNIT((c.getString(c
                        .getColumnIndex(EA_UNIT))));
                qrcodeq.setSTOCK_RECEIPT_CD((c.getString(c
                        .getColumnIndex(STOCK_RECEIPT_CD))));
                qrcodeq.setPRODUCT_FROM((c.getString(c
                        .getColumnIndex(POSITION_FROM))));
                qrcodeq.setPRODUCT_TO((c.getString(c
                        .getColumnIndex(POSITION_TO))));
                qrcodeq.setPOSITION_CODE((c.getString(c
                        .getColumnIndex(POSITION_CODE))));
                qrcodeq.setPOSITION_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_DESCRIPTION))));
                qrcodeq.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE))));


                qrcode.add(qrcodeq);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }

    public ArrayList<Product_Qrcode>
    getAllProduct_Qrcode_Sync(String stock) {
        ArrayList<Product_Qrcode> qrcode = new ArrayList<Product_Qrcode>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT MANUFACTURING_DATE_WST , WAREHOUSE_POSITION_CD, STOCK_RECEIPT_CD, PRODUCT_CD, PRODUCT_CODE, PRODUCT_NAME, SET_UNIT, POSITION_FROM, POSITION_TO" +
                ", POSITION_CODE, POSITION_DESCRIPTION, EA_UNIT , REPLACE(EXPIRED_DATE,'------','') as EXPIRED_DATE, REPLACE(STOCKIN_DATE,'------','') as STOCKIN_DATE FROM " + O_QRCODE + " " + " WHERE " + STOCK_RECEIPT_CD + " = " + stock;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Qrcode qrcodeq = new Product_Qrcode();
                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD))));
                qrcodeq.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_WST))));
                qrcodeq.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE))));
                qrcodeq.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME))));
                qrcodeq.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE))));
                qrcodeq.setSL_SET((c.getString(c
                        .getColumnIndex(SET_UNIT))));
                qrcodeq.setEA_UNIT((c.getString(c
                        .getColumnIndex(EA_UNIT))));
                qrcodeq.setSTOCK_RECEIPT_CD((c.getString(c
                        .getColumnIndex(STOCK_RECEIPT_CD))));
                qrcodeq.setPRODUCT_FROM((c.getString(c
                        .getColumnIndex(POSITION_FROM))));
                qrcodeq.setPRODUCT_TO((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD))));
                qrcodeq.setPOSITION_CODE((c.getString(c
                        .getColumnIndex(POSITION_CODE))));
                qrcodeq.setPOSITION_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_DESCRIPTION))));
                qrcodeq.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE))));


                qrcode.add(qrcodeq);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }

    public ArrayList<Product_Qrcode>
    getAllProduct_Qrcode() {
        ArrayList<Product_Qrcode> qrcode = new ArrayList<Product_Qrcode>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_QRCODE;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Qrcode qrcodeq = new Product_Qrcode();
                qrcodeq.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PO))));
                qrcodeq.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_WST))));
                qrcodeq.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD))));
                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD))));
                qrcodeq.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE))));
                qrcodeq.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE))));
                qrcodeq.setSL_SET((c.getString(c
                        .getColumnIndex(SET_UNIT))));
                qrcodeq.setEA_UNIT((c.getString(c
                        .getColumnIndex(EA_UNIT))));
                qrcodeq.setSTOCK_RECEIPT_CD((c.getString(c
                        .getColumnIndex(STOCK_RECEIPT_CD))));
                qrcodeq.setPRODUCT_FROM((c.getString(c
                        .getColumnIndex(POSITION_FROM))));
                qrcodeq.setPRODUCT_TO((c.getString(c
                        .getColumnIndex(POSITION_TO))));
                qrcodeq.setPOSITION_CODE((c.getString(c
                        .getColumnIndex(POSITION_CODE))));
                qrcodeq.setPOSITION_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_DESCRIPTION))));
                qrcodeq.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE))));

                qrcode.add(qrcodeq);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }

    public void deleteProduct_Qrcode(String stock) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_QRCODE + " WHERE " + STOCK_RECEIPT_CD + " = " + stock);
    }

    public void deleteAllProduct_Qrcode() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_QRCODE);
    }


    //End Table O_QRCODE


    public static final String CUSTOMER_NEW_CODE = "CUSTOMER_NEW_CODE";
    public static final String CUSTOMER_NEW_NAME = "CUSTOMER_NEW_NAME";
    public static final String CUSTOMER_NEW_PHONE_NUMBER = "CUSTOMER_NEW_PHONE_NUMBER";
    public static final String CUSTOMER_NEW_ADDRESS = "CUSTOMER_NEW_ADDRESS";
    public static final String CUSTOMER_NEW_CHANEL = "CUSTOMER_NEW_CHANEL";
    public static final String CUSTOMER_NEW_ACTIVE = "CUSTOMER_NEW_ACTIVE";
    public static final String CUSTOMER_NEW_LATITUDE_LONGITUDE = "CUSTOMER_NEW_LATITUDE_LONGITUDE";
    public static final String CUSTOMER_NEW_LATITUDE_LONGITUDE_ACCURACY = "CUSTOMER_NEW_LATITUDE_LONGITUDE_ACCURACY";
    public static final String CUSTOMER_NEW_ROUTE = "CUSTOMER_NEW_ROUTE";
    public static final String CUSTOMER_NEW_ALLOW_SYNC = "CUSTOMER_NEW_ALLOW_SYNC";
    public static final String CUSTOMER_NEW_CREATED_DATE = "CUSTOMER_NEW_CREATED_DATE";
    public static final String CUSTOMER_NEW_LATITUDE_LONGITUDE_ADDRESS = "CUSTOMER_NEW_LATITUDE_LONGITUDE_ADDRESS";

    public static final String CUSTOMER_NEW_INFO_1 = "CUSTOMER_NEW_INFO_1";
    public static final String CUSTOMER_NEW_INFO_2 = "CUSTOMER_NEW_INFO_2";
    public static final String CUSTOMER_NEW_INFO_3 = "CUSTOMER_NEW_INFO_3";
    public static final String CUSTOMER_NEW_INFO_4 = "CUSTOMER_NEW_INFO_4";
    public static final String CUSTOMER_NEW_INFO_5 = "CUSTOMER_NEW_INFO_5";


    // Table Names O_CUSTOMER_NEW

    public static final String O_CUSTOMER_NEW = "O_CUSTOMER_NEW";

    // O_TAKES_PHOTO table create statement
    public static final String O_TAKES_PHOTO = "O_TAKES_PHOTO";
    public static final String TAKES_PHOTO_PRIMARY_KEY = "TAKES_PHOTO_CD";
    public static final String TAKES_PHOTO_CUSTOMER_CODE = "TAKES_PHOTO_CUSTOMER_CODE";
    public static final String TAKES_PHOTO_CUSTOMER_NAME = "TAKES_PHOTO_CUSTOMER_NAME";
    public static final String TAKES_PHOTO_CUSTOMER_PHONE_NUMBER = "TAKES_PHOTO_CUSTOMER_PHONE_NUMBER";
    public static final String TAKES_PHOTO_CUSTOMER_ADDRESS = "TAKES_PHOTO_CUSTOMER_ADDRESS";

    public static final String TAKES_PHOTO_LATITUDE_LONGITUDE = "TAKES_PHOTO_LATITUDE_LONGITUDE";

    public static final String TAKES_PHOTO_FILE_NAME = "TAKES_PHOTO_FILE_NAME";
    public static final String TAKES_PHOTO_FULL_PATH_FILE = "TAKES_PHOTO_FULL_PATH_FILE";
    public static final String TAKES_PHOTO_CREATED_DATE = "CREATED_DATE";

    public static final String O_SALE_TAKE_PHOTO = "O_SALE_TAKE_PHOTO";
    public static final String SALE_TAKES_PHOTO_PRIMARY_KEY = "SALE_TAKES_PHOTO_CD";
    public static final String SALE_TAKES_PHOTO_FILE_NAME = "PHOTO_NAME";
    public static final String SALE_ORDER_CD = "ORDER_CD";

    public static final String SALE_TAKES_PHOTO_FULL_PATH_FILE = "SALE_TAKES_PHOTO_FULL_PATH_FILE";
    public static final String SALE_TAKES_PHOTO_CREATED_DATE = "PHOTO_DATE";

    public static final String O_CHANGE_CUST_TAKE_PHOTO = "O_CHANGE_CUST_TAKE_PHOTO";
    public static final String CHANGE_CUST_PHOTO_PRIMARY_KEY = "CHANGE_CUST_PHOTO_PRIMARY_KEY";

    public static final String CHANGE_CUST_CODE_PHOTO = "CHANGE_CUST_CODE_PHOTO";
    public static final String CHANGE_CUST_NAME_PHOTO = "CHANGE_CUST_NAME_PHOTO";
    public static final String CHANGE_CUST_ADDRESS_PHOTO = "CHANGE_CUST_ADDRESS_PHOTO";
    public static final String CHANGE_CUST_LATITUDE_LONGITUDE_PHOTO = "CHANGE_CUST_LATITUDE_LONGITUDE_PHOTO";

    public static final String CHANGE_CUST_PHONE_PHOTO = "CHANGE_CUST_PHONE_PHOTO";
    public static final String CHANGE_CUST_FILE_NAME = "CHANGE_CUST_FILE_NAME";

    public static final String CHANGE_CUST_NAME_PATH_FILE = "CHANGE_CUST_NAME_PATH_FILE";
    public static final String CHANGE_CUST_CREATE_DATE = "CHANGE_CUST_CREATE_DATE";

//    // O_CUSTOMER_NEW table create statement
//    public static final String CREATE_TABLE_O_CUSTOMER_NEW = "CREATE TABLE "
//            + O_CUSTOMER_NEW + "(" + CUSTOMER_NEW_CODE + " TEXT PRIMARY KEY ,"
//            + CUSTOMER_NEW_NAME + " TEXT," + CUSTOMER_NEW_ADDRESS + " TEXT,"
//            + CUSTOMER_NEW_PHONE_NUMBER + " TEXT,"
//            + CUSTOMER_NEW_LATITUDE_LONGITUDE + " TEXT,"
//            + CUSTOMER_NEW_ALLOW_SYNC + " INTEGER DEFAULT 0  ,"
//            + CUSTOMER_NEW_CREATED_DATE + " TEXT "
//
//            + ")";

    public static final String O_SHIP_CHANGE_CUSTOMER = "O_SHIP_CHANGE_CUSTOMER";
    public static final String SHIP_CHANGE_CUSTOMER_PRIMARY_KEY = "SHIP_CHANGE_CUSTOMER_CD";
    public static final String SHIP_CHANGE_CUSTOMER_CODE = "SHIP_CHANGE_CUSTOMER_CODE";
    public static final String SHIP_CHANGE_CUSTOMER_CD = "SHIP_CHANGE_CUSTOMER_CD";
    public static final String SHIP_CHANGE_CUSTOMER_GEOCODE = "SHIP_CHANGE_CUSTOMER_GEOCODE";

    public static final String SHIP_CHANGE_CUSTOMER_NAME = "SHIP_CHANGE_CUSTOMER_NAME";

    public static final String SHIP_CHANGE_CUSTOMER_ADDRESS = "SHIP_CHANGE_CUSTOMER_ADDRESS";
    public static final String SHIP_CHANGE_CUSTOMER_PHONE = "SHIP_CHANGE_CUSTOMER_PHONE";

    // O_CUSTOMER_NEW table create statement
    public static final String CREATE_TABLE_O_CUSTOMER_NEW = "CREATE TABLE "
            + O_CUSTOMER_NEW + "(" + CUSTOMER_NEW_CODE + " TEXT PRIMARY KEY ,"
            + CUSTOMER_NEW_NAME + " TEXT," + CUSTOMER_NEW_ADDRESS + " TEXT,"
            + CUSTOMER_NEW_PHONE_NUMBER + " TEXT," + CUSTOMER_NEW_CHANEL
            + " TEXT," + CUSTOMER_NEW_ACTIVE + " INTEGER DEFAULT 0 ,"
            + CUSTOMER_NEW_LATITUDE_LONGITUDE + " TEXT,"
            + CUSTOMER_NEW_LATITUDE_LONGITUDE_ACCURACY + " TEXT,"
            + CUSTOMER_NEW_ROUTE + " TEXT," + CUSTOMER_NEW_ALLOW_SYNC
            + " INTEGER DEFAULT 0  ," + CUSTOMER_NEW_CREATED_DATE + " TEXT ,"
            + CUSTOMER_NEW_INFO_1 + " TEXT ," + CUSTOMER_NEW_INFO_2 + " TEXT ,"
            + CUSTOMER_NEW_INFO_3 + " TEXT ," + CUSTOMER_NEW_INFO_4 + " TEXT ,"
            + CUSTOMER_NEW_INFO_5 + " TEXT ,"
            + CUSTOMER_NEW_LATITUDE_LONGITUDE_ADDRESS + " TEXT " + ")";


    public static final String CREATE_TABLE_O_TAKES_PHOTO = "CREATE TABLE "
            + O_TAKES_PHOTO + "(" + TAKES_PHOTO_PRIMARY_KEY
            + "  INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + TAKES_PHOTO_CUSTOMER_CODE + " TEXT,"
            + TAKES_PHOTO_CUSTOMER_NAME + " TEXT,"
            + TAKES_PHOTO_CUSTOMER_PHONE_NUMBER + " TEXT,"
            + TAKES_PHOTO_CUSTOMER_ADDRESS + " TEXT,"
            + TAKES_PHOTO_LATITUDE_LONGITUDE + " TEXT,"
            + TAKES_PHOTO_FILE_NAME + " TEXT,"
            + TAKES_PHOTO_FULL_PATH_FILE + " TEXT," + TAKES_PHOTO_CREATED_DATE
            + " TEXT" + ")";


    //    // O_SHIP_CHANGE_CUSTOMER table create statement
    public static final String CREATE_TABLE_O_SHIP_CHANGE_CUSTOMER = "CREATE TABLE "
            + O_SHIP_CHANGE_CUSTOMER + "(" + SHIP_CHANGE_CUSTOMER_PRIMARY_KEY + " TEXT,"
            + SHIP_CHANGE_CUSTOMER_CODE + " TEXT,"
            + SHIP_CHANGE_CUSTOMER_GEOCODE + " TEXT,"
            + SHIP_CHANGE_CUSTOMER_NAME + " TEXT,"
            + SHIP_CHANGE_CUSTOMER_ADDRESS + " TEXT,"
            + SHIP_CHANGE_CUSTOMER_PHONE + " TEXT" + ")";


    //    // O_SALE_TAKE_PHOTO table create statement
    public static final String CREATE_TABLE_O_SALE_TAKES_PHOTO = "CREATE TABLE "
            + O_SALE_TAKE_PHOTO + "(" + SALE_TAKES_PHOTO_PRIMARY_KEY + " TEXT,"
            + SALE_ORDER_CD + " TEXT,"
            + SALE_TAKES_PHOTO_FILE_NAME + " TEXT,"
            + SALE_TAKES_PHOTO_FULL_PATH_FILE + " TEXT,"
            + SALE_TAKES_PHOTO_CREATED_DATE + " TEXT" + ")";

    public static final String CREATE_TABLE_O_CHANGE_CUST_TAKE_PHOTO = "CREATE TABLE "
            + O_CHANGE_CUST_TAKE_PHOTO + "(" + CHANGE_CUST_PHOTO_PRIMARY_KEY + " TEXT,"
            + CHANGE_CUST_CODE_PHOTO + " TEXT,"
            + CHANGE_CUST_NAME_PHOTO + " TEXT,"
            + CHANGE_CUST_ADDRESS_PHOTO + " TEXT,"
            + CHANGE_CUST_PHONE_PHOTO + " TEXT,"
            + CHANGE_CUST_FILE_NAME + " TEXT,"
            + CHANGE_CUST_NAME_PATH_FILE + " TEXT,"
            + CHANGE_CUST_CREATE_DATE + " TEXT,"
            + CHANGE_CUST_LATITUDE_LONGITUDE_PHOTO + " TEXT" + ")";

    public long CreateCustomerChange(UpdateCustomer customer) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(SHIP_CHANGE_CUSTOMER_CODE, customer.getCustomerCode());
        values.put(SHIP_CHANGE_CUSTOMER_NAME, customer.getCustomerName());
        values.put(SHIP_CHANGE_CUSTOMER_ADDRESS, customer.getCustomerAddress());
        values.put(SHIP_CHANGE_CUSTOMER_PHONE, customer.getCustomerPhone());
        values.put(SHIP_CHANGE_CUSTOMER_CD, customer.getCustomerCd());
        values.put(SHIP_CHANGE_CUSTOMER_GEOCODE, customer.getCustomerGeocode());

        // insert row
        long id = db.insert(O_SHIP_CHANGE_CUSTOMER, null, values);
        return id;

    }

    public long createCustomerNew(CCustomer customer) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(CUSTOMER_NEW_CODE, customer.getCustomerCode());
        values.put(CUSTOMER_NEW_NAME, customer.getCustomerName());
        values.put(CUSTOMER_NEW_PHONE_NUMBER, customer.getCustomerPhoneNumber());
        values.put(CUSTOMER_NEW_ADDRESS, customer.getCustomerAddress());
        values.put(CUSTOMER_NEW_CHANEL, customer.getCustomerCategory());
        values.put(CUSTOMER_NEW_ACTIVE, customer.getCustomerActive());
        values.put(CUSTOMER_NEW_LATITUDE_LONGITUDE,
                customer.getCustomerLocation());
        values.put(CUSTOMER_NEW_LATITUDE_LONGITUDE_ACCURACY,
                customer.getCustomerLocationAccuracy());
        values.put(CUSTOMER_NEW_CHANEL, customer.getCustomerCategory());
        values.put(CUSTOMER_NEW_ROUTE, customer.getCustomerRoute());
        values.put(CUSTOMER_NEW_ALLOW_SYNC, "1");
        values.put(CUSTOMER_NEW_CREATED_DATE, customer.getCustomerCreatedDate());

        values.put(CUSTOMER_NEW_INFO_1, customer.getCustomerInfo1());
        values.put(CUSTOMER_NEW_INFO_2, customer.getCustomerInfo2());
        values.put(CUSTOMER_NEW_INFO_3, customer.getCustomerInfo3());
        values.put(CUSTOMER_NEW_INFO_4, customer.getCustomerInfo4());
        values.put(CUSTOMER_NEW_INFO_5, customer.getCustomerInfo5());

        values.put(CUSTOMER_NEW_LATITUDE_LONGITUDE_ADDRESS,
                customer.getCustomerLocationAddress());

        // insert row
        long id = db.insert(O_CUSTOMER_NEW, null, values);
        return id;

    }


    public ArrayList<CCustomer> getAllCustomerNew() {
        ArrayList<CCustomer> customers = new ArrayList<CCustomer>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_CUSTOMER_NEW;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                CCustomer ccustomer = new CCustomer();
                ccustomer.setCustomerCode((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_CODE))));
                ccustomer.setCustomerName((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_NAME))));
                ccustomer.setCustomerAddress((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_ADDRESS))));
                ccustomer.setCustomerPhoneNumber((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_PHONE_NUMBER))));
                ccustomer.setCustomerCategory((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_CHANEL))));
                ccustomer.setCustomerActive(c.getInt(c
                        .getColumnIndex(CUSTOMER_NEW_ACTIVE)) == 1);
                ccustomer.setCustomerLocation((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_LATITUDE_LONGITUDE))));
                ccustomer
                        .setCustomerLocationAccuracy((c.getString(c
                                .getColumnIndex(CUSTOMER_NEW_LATITUDE_LONGITUDE_ACCURACY))));
                ccustomer.setCustomerRoute((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_ROUTE))));
                ccustomer.setCustomerAllowSync(c.getInt(c
                        .getColumnIndex(CUSTOMER_NEW_ALLOW_SYNC)) == 1);
                ccustomer.setCustomerCreatedDate(c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_CREATED_DATE)));

                ccustomer.setCustomerInfo1((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_INFO_1))));
                ccustomer.setCustomerInfo2((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_INFO_2))));
                ccustomer.setCustomerInfo3((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_INFO_3))));
                ccustomer.setCustomerInfo4((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_INFO_4))));
                ccustomer.setCustomerInfo5((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_INFO_5))));

                ccustomer
                        .setCustomerLocationAddress((c.getString(c
                                .getColumnIndex(CUSTOMER_NEW_LATITUDE_LONGITUDE_ADDRESS))));


                customers.add(ccustomer);
            } while (c.moveToNext());
        }

        c.close();
        return customers;
    }

    public int updateCustomerNewSync() {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_NEW_ALLOW_SYNC, "0");

        // updating row
        return db.update(O_CUSTOMER_NEW, values, CUSTOMER_NEW_ALLOW_SYNC
                + " = ?", new String[]{String.valueOf("1")});
    }

    public int updateChangeCustomer(UpdateCustomer customer, String CustomerCD) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(SHIP_CHANGE_CUSTOMER_CODE, customer.getCustomerCode());
        values.put(SHIP_CHANGE_CUSTOMER_NAME, customer.getCustomerName());
        values.put(SHIP_CHANGE_CUSTOMER_ADDRESS, customer.getCustomerAddress());
        values.put(SHIP_CHANGE_CUSTOMER_PHONE, customer.getCustomerPhone());
        values.put(SHIP_CHANGE_CUSTOMER_CD, customer.getCustomerCd());
        values.put(SHIP_CHANGE_CUSTOMER_GEOCODE, customer.getCustomerGeocode());

        // updating row
        return db.update(O_SHIP_CHANGE_CUSTOMER, values, SHIP_CHANGE_CUSTOMER_CD + " = ?",
                new String[]{String.valueOf(CustomerCD)});
    }

    public ArrayList<UpdateCustomer> getAllChangeCustomer() {
        ArrayList<UpdateCustomer> customers = new ArrayList<UpdateCustomer>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_SHIP_CHANGE_CUSTOMER;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                UpdateCustomer ccustomer = new UpdateCustomer();
                ccustomer.setCustomerCode((c.getString(c
                        .getColumnIndex(SHIP_CHANGE_CUSTOMER_CODE))));
                ccustomer.setCustomerName((c.getString(c
                        .getColumnIndex(SHIP_CHANGE_CUSTOMER_NAME))));
                ccustomer.setCustomerAddress((c.getString(c
                        .getColumnIndex(SHIP_CHANGE_CUSTOMER_ADDRESS))));
                ccustomer.setCustomerPhone((c.getString(c
                        .getColumnIndex(SHIP_CHANGE_CUSTOMER_PHONE))));
                ccustomer.setCustomerGeocode((c.getString(c
                        .getColumnIndex(SHIP_CHANGE_CUSTOMER_GEOCODE))));
                ccustomer.setCustomerCd((c.getString(c
                        .getColumnIndex(SHIP_CHANGE_CUSTOMER_CD))));


                customers.add(ccustomer);
            } while (c.moveToNext());
        }

        c.close();
        return customers;
    }

    public List<CCustomer> getAllCustomerNewAllowSynchronize() {

        List<CCustomer> customers = new ArrayList<CCustomer>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_CUSTOMER_NEW + " WHERE "
                + CUSTOMER_NEW_ALLOW_SYNC + " =  1 ";

        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                CCustomer ccustomer = new CCustomer();
                ccustomer.setCustomerCode((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_CODE))));
                ccustomer.setCustomerName((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_NAME))));
                ccustomer.setCustomerAddress((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_ADDRESS))));
                ccustomer.setCustomerPhoneNumber((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_PHONE_NUMBER))));
                ccustomer.setCustomerCategory((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_CHANEL))));
                ccustomer.setCustomerActive(c.getInt(c
                        .getColumnIndex(CUSTOMER_NEW_ACTIVE)) == 1);
                ccustomer.setCustomerLocation((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_LATITUDE_LONGITUDE))));
                ccustomer
                        .setCustomerLocationAccuracy((c.getString(c
                                .getColumnIndex(CUSTOMER_NEW_LATITUDE_LONGITUDE_ACCURACY))));
                ccustomer.setCustomerRoute((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_ROUTE))));
                ccustomer.setCustomerAllowSync(c.getInt(c
                        .getColumnIndex(CUSTOMER_NEW_ALLOW_SYNC)) == 1);
                ccustomer.setCustomerCreatedDate(c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_CREATED_DATE)));

                ccustomer.setCustomerInfo1((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_INFO_1))));
                ccustomer.setCustomerInfo2((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_INFO_2))));
                ccustomer.setCustomerInfo3((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_INFO_3))));
                ccustomer.setCustomerInfo4((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_INFO_4))));

                ccustomer.setCustomerInfo5((c.getString(c
                        .getColumnIndex(CUSTOMER_NEW_INFO_5))));

                ccustomer
                        .setCustomerLocationAddress((c.getString(c
                                .getColumnIndex(CUSTOMER_NEW_LATITUDE_LONGITUDE_ADDRESS))));

                customers.add(ccustomer);
            } while (c.moveToNext());
        }

        c.close();
        return customers;
    }

    public int updateCustomerAddNew(CCustomer customer, String customerOldCode) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        Log.i("code", customer.getCustomerCode());
        Log.i("phonenumber", customer.getCustomerPhoneNumber());
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_NEW_CODE, customer.getCustomerCode());
        values.put(CUSTOMER_NEW_NAME, customer.getCustomerName());
        values.put(CUSTOMER_NEW_PHONE_NUMBER, customer.getCustomerPhoneNumber());
        values.put(CUSTOMER_NEW_ADDRESS, customer.getCustomerAddress());
        values.put(CUSTOMER_NEW_ALLOW_SYNC, customer.getCustomerAllowSync());
        values.put(CUSTOMER_NEW_CREATED_DATE, customer.getCustomerCreatedDate());
        values.put(CUSTOMER_NEW_LATITUDE_LONGITUDE,
                customer.getCustomerLocation());
        // updating row
        return db.update(O_CUSTOMER_NEW, values, CUSTOMER_NEW_CODE + " = ?",
                new String[]{String.valueOf(customerOldCode)});
    }

    public void deleteCustomerAddNew(String customerCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_CUSTOMER_NEW, CUSTOMER_NEW_CODE + " = ?",
                new String[]{String.valueOf(customerCode)});
    }

    public void deleteCustomerChanged(String customerCd) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_SHIP_CHANGE_CUSTOMER, SHIP_CHANGE_CUSTOMER_CD + " = ?",
                new String[]{String.valueOf(customerCd)});
    }

    public void deleteListCust() {
        SQLiteDatabase db = getInstance().getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("DELETE FROM " + O_SHIP_CHANGE_CUSTOMER); //delete all rows in a table
        db.close();
    }

    public void deleteListChangeCustPhoto() {
        SQLiteDatabase db = getInstance().getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("DELETE FROM " + O_CHANGE_CUST_TAKE_PHOTO); //delete all rows in a table
        db.close();
    }

    public long createTakesPhoto(CCustomer customer, CPhoto files) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(TAKES_PHOTO_CUSTOMER_CODE, customer.getCustomerCode());
        values.put(TAKES_PHOTO_CUSTOMER_NAME, customer.getCustomerName());
        values.put(TAKES_PHOTO_CUSTOMER_PHONE_NUMBER, customer.getCustomerPhoneNumber());
        values.put(TAKES_PHOTO_CUSTOMER_ADDRESS, customer.getCustomerAddress());

        values.put(TAKES_PHOTO_LATITUDE_LONGITUDE, files.getGeoCode());

        values.put(TAKES_PHOTO_FILE_NAME, files.getPhotoName());
        values.put(TAKES_PHOTO_FULL_PATH_FILE, files.getPhotoPath());
        values.put(TAKES_PHOTO_CREATED_DATE,
                files.getDateTakesPhotoToInsertDB());

        // insert row
        long id = db.insert(O_TAKES_PHOTO, null, values);
        return id;
    }

    public long createChangeCustTakesPhoto(UpdateCustomer customer, CPhoto files) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(CHANGE_CUST_CODE_PHOTO, customer.getCustomerCode());
        values.put(CHANGE_CUST_NAME_PHOTO, customer.getCustomerName());
        values.put(CHANGE_CUST_PHONE_PHOTO, customer.getCustomerPhone());
        values.put(CHANGE_CUST_ADDRESS_PHOTO, customer.getCustomerAddress());

        values.put(CHANGE_CUST_LATITUDE_LONGITUDE_PHOTO, files.getGeoCode());

        values.put(CHANGE_CUST_FILE_NAME, files.getPhotoName());
        values.put(CHANGE_CUST_NAME_PATH_FILE, files.getPhotoPath());
        values.put(CHANGE_CUST_CREATE_DATE,
                files.getDateTakesPhotoToInsertDB());

        // insert row
        long id = db.insert(O_CHANGE_CUST_TAKE_PHOTO, null, values);
        return id;
    }

    public List<CPhoto> getAllTakesPhotos() {

        List<CPhoto> files = new ArrayList<CPhoto>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_TAKES_PHOTO;

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                try {
                    CPhoto file = new CPhoto();
                    file.setPhotoCD(c.getLong(c
                            .getColumnIndex(TAKES_PHOTO_PRIMARY_KEY)));
                    file.setPhotoCustomerCode((c.getString(c
                            .getColumnIndex(TAKES_PHOTO_CUSTOMER_CODE))));
                    file.setPhotoCustomerName((c.getString(c
                            .getColumnIndex(TAKES_PHOTO_CUSTOMER_NAME))));
                    file.setPhotoCustomerPhone((c.getString(c
                            .getColumnIndex(TAKES_PHOTO_CUSTOMER_PHONE_NUMBER))));
                    file.setPhotoCustomerAddress((c.getString(c
                            .getColumnIndex(TAKES_PHOTO_CUSTOMER_ADDRESS))));

                    file.setPhotoName((c.getString(c
                            .getColumnIndex(TAKES_PHOTO_FILE_NAME))));
                    file.setPhotoPath((c.getString(c
                            .getColumnIndex(TAKES_PHOTO_FULL_PATH_FILE))));

                    file.setGeoCode((c.getString(c
                            .getColumnIndex(TAKES_PHOTO_LATITUDE_LONGITUDE))));

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            global.getFormatDate());
                    Date convertedDate = new Date();
                    try {
                        convertedDate = dateFormat.parse((c.getString(c
                                .getColumnIndex(TAKES_PHOTO_CREATED_DATE))));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    file.setDateTakesPhoto(convertedDate);
                    files.add(file);

                } catch (Exception e) {
                    // TODO: handle exception
                }

            } while (c.moveToNext());
        }
        c.close();
        return files;
    }

    public List<CPhoto> getAllChangeCustTakesPhotos() {

        List<CPhoto> files = new ArrayList<CPhoto>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_CHANGE_CUST_TAKE_PHOTO;

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                try {
                    CPhoto file = new CPhoto();
                    file.setPhotoCD(c.getLong(c
                            .getColumnIndex(CHANGE_CUST_PHOTO_PRIMARY_KEY)));
                    file.setPhotoCustomerCode((c.getString(c
                            .getColumnIndex(CHANGE_CUST_CODE_PHOTO))));
                    file.setPhotoCustomerName((c.getString(c
                            .getColumnIndex(CHANGE_CUST_NAME_PHOTO))));
                    file.setPhotoCustomerAddress((c.getString(c
                            .getColumnIndex(CHANGE_CUST_ADDRESS_PHOTO))));
                    file.setPhotoCustomerPhone((c.getString(c
                            .getColumnIndex(CHANGE_CUST_PHONE_PHOTO))));

                    file.setPhotoName((c.getString(c
                            .getColumnIndex(CHANGE_CUST_FILE_NAME))));
                    file.setPhotoPath((c.getString(c
                            .getColumnIndex(CHANGE_CUST_NAME_PATH_FILE))));


                    file.setGeoCode((c.getString(c
                            .getColumnIndex(CHANGE_CUST_LATITUDE_LONGITUDE_PHOTO))));

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            global.getFormatDate());
                    Date convertedDate = new Date();
                    try {
                        convertedDate = dateFormat.parse((c.getString(c
                                .getColumnIndex(CHANGE_CUST_CREATE_DATE))));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    file.setDateTakesPhoto(convertedDate);
                    files.add(file);

                } catch (Exception e) {
                    // TODO: handle exception
                }

            } while (c.moveToNext());
        }
        c.close();
        return files;
    }

    public List<OrderPhoto> getAllPhotoForOrders() {

        List<OrderPhoto> files = new ArrayList<OrderPhoto>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_SALE_TAKE_PHOTO;

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                try {
                    OrderPhoto file = new OrderPhoto();
                    file.setOrder_CD((c.getString(c
                            .getColumnIndex(SALE_ORDER_CD))));
                    file.setPhoto_Name((c.getString(c
                            .getColumnIndex(SALE_TAKES_PHOTO_FILE_NAME))));
                    file.setPhoto_Path((c.getString(c
                            .getColumnIndex(SALE_TAKES_PHOTO_FULL_PATH_FILE))));
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            global.getFormatDate());
                    Date convertedDate = new Date();
                    try {
                        convertedDate = dateFormat.parse((c.getString(c
                                .getColumnIndex(SALE_TAKES_PHOTO_CREATED_DATE))));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    file.setPhoto_Date(convertedDate);
                    files.add(file);

                } catch (Exception e) {
                    // TODO: handle exception
                }

            } while (c.moveToNext());
        }
        c.close();
        return files;
    }

    public void deletePhoto(long primaryKey) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_TAKES_PHOTO, TAKES_PHOTO_PRIMARY_KEY + " = ?",
                new String[]{String.valueOf(primaryKey)});

    }

    public void deleteChangeCustPhoto(String name) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_CHANGE_CUST_TAKE_PHOTO, CHANGE_CUST_FILE_NAME + " = ?",
                new String[]{String.valueOf(name)});

    }

    public void deletePhotoForOrders(String name) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_SALE_TAKE_PHOTO, SALE_TAKES_PHOTO_FILE_NAME + " = ?",
                new String[]{String.valueOf(name)});

    }

    public long createdSaleTakesPhoto(String orderCD, OrderPhoto files) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(SALE_ORDER_CD, orderCD);
        values.put(SALE_TAKES_PHOTO_FILE_NAME, files.getPhoto_Name());
        values.put(SALE_TAKES_PHOTO_CREATED_DATE,
                files.getStrDateTakesPhoto());
        values.put(SALE_TAKES_PHOTO_FULL_PATH_FILE, files.getPhoto_Path());

        // insert row
        long id = db.insert(O_SALE_TAKE_PHOTO, null, values);
        return id;
    }


    public int updatePositionFrom_LoadPallet_LPN(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_LOAD_PALLET, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_LOAD_PALLET, descreption);
        // khi quét vị trí là LPN thì phải cập nhật POSITION_FROM_CODE luôn ,
        // khi đó khi đồng bộ hàm isNotScanFromOrTo ms chạy đúng
        values.put(POSITION_FROM_CODE_LOAD_PALLET, from);

        values.put(LPN_FROM_LOAD_PALLET, from);

        // updating row
        return db.update(O_LOAD_PALLET, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_LOAD_PALLET + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});
    }

    public int updatePositionFrom_LoadPallet(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_LOAD_PALLET, wareHouse);
        values.put(POSITION_FROM_CODE_LOAD_PALLET, from);
        values.put(LPN_FROM_LOAD_PALLET, "");
        values.put(POSITION_FROM_DESCRIPTION_LOAD_PALLET, descreption);

        // updating row
        return db.update(O_LOAD_PALLET, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_LOAD_PALLET + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});
    }

    public int updatePositionTo_LoadPallet_LPN(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_LOAD_PALLET, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_LOAD_PALLET, descreption);
        values.put(LPN_TO_LOAD_PALLET, to);
        values.put(POSITION_TO_CODE_LOAD_PALLET, to);
        // updating row
        return db.update(O_LOAD_PALLET, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_LOAD_PALLET + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});
    }

    public int updatePositionTo_LoadPallet(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_LOAD_PALLET, wareHouse);
        values.put(POSITION_TO_CODE_LOAD_PALLET, to);
        values.put(LPN_TO_LOAD_PALLET, "");

        values.put(POSITION_TO_DESCRIPTION_LOAD_PALLET, descreption);
        // updating row
        return db.update(O_LOAD_PALLET, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_LOAD_PALLET + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});
    }

    //------------------------------------------------------------------------------------------------------------------
    //database from table O_INVENTORY
    public static final String O_INVENTORY = "O_INVENTORY";
    public static final String AUTOINCREMENT_INVENTORY = "AUTOINCREMENT_INVENTORY";
    public static final String PRODUCT_CODE_INVENTORY = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_INVENTORY = "PRODUCT_NAME";
    public static final String PRODUCT_CD_INVENTORY = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_INVENTORY = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_INVENTORY = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_INVENTORY = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_INVENTORY = "STOCKIN_DATE";
    public static final String EA_UNIT_INVENTORY = "EA_UNIT";
    public static final String POSITION_FROM_INVENTORY = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_INVENTORY = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_INVENTORY = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_INVENTORY = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_INVENTORY = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_INVENTORY = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_INVENTORY = "UNIQUE_CODE";
    public static final String STOCK_TAKE_CD = "STOCK_TAKE_CD";
    public static final String LPN_CD_INVENTORY = "LPN_CD_INVENTORY";
    public static final String LPN_CODE_INVENTORY = "LPN_CODE_INVENTORY";
    public static final String LPN_FROM_INVENTORY = "LPN_FROM_INVENTORY";
    public static final String LPN_TO_INVENTORY = "LPN_TO_INVENTORY";

    public static final String CREATE_TABLE_O_INVENTORY = "CREATE TABLE "
            + O_INVENTORY + "("
            + AUTOINCREMENT_INVENTORY + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + UNIQUE_CODE_INVENTORY + " TEXT,"
            + PRODUCT_CODE_INVENTORY + " TEXT,"
            + PRODUCT_NAME_INVENTORY + " TEXT,"
            + QTY_SET_AVAILABLE_INVENTORY + " TEXT,"
            + STOCKIN_DATE_INVENTORY + " TEXT,"
            + PRODUCT_CD_INVENTORY + " TEXT,"
            + QTY_EA_AVAILABLE_INVENTORY + " TEXT,"
            + POSITION_FROM_INVENTORY + " TEXT,"
            + EXPIRED_DATE_INVENTORY + " TEXT,"
            + EA_UNIT_INVENTORY + " TEXT,"
            + POSITION_FROM_CODE_INVENTORY + " TEXT,"
            + POSITION_TO_CODE_INVENTORY + " TEXT,"
            + POSITION_TO_INVENTORY + " TEXT,"
            + POSITION_FROM_DESCRIPTION_INVENTORY + " TEXT,"
            + STOCK_TAKE_CD + " TEXT,"
            + POSITION_TO_DESCRIPTION_INVENTORY + " TEXT,"
            + LPN_CD_INVENTORY + " TEXT,"
            + LPN_CODE_INVENTORY + " TEXT,"
            + LPN_FROM_INVENTORY + " TEXT,"
            + LPN_TO_INVENTORY + " TEXT"
            + ")";

    public long CreateInventory(InventoryProduct qrcode) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(AUTOINCREMENT_INVENTORY, qrcode.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_INVENTORY, qrcode.getUNIQUE_CODE());
        values.put(PRODUCT_CODE_INVENTORY, qrcode.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_INVENTORY, qrcode.getPRODUCT_NAME());
        values.put(PRODUCT_CD_INVENTORY, qrcode.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_INVENTORY, qrcode.getQTY());
        values.put(STOCKIN_DATE_INVENTORY, qrcode.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_INVENTORY, qrcode.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_INVENTORY, qrcode.getEXPIRED_DATE());
        values.put(EA_UNIT_INVENTORY, qrcode.getUNIT());
        values.put(POSITION_FROM_INVENTORY, qrcode.getPOSITION_FROM_CD());
        values.put(POSITION_TO_INVENTORY, qrcode.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_INVENTORY, qrcode.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_INVENTORY, qrcode.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_INVENTORY, qrcode.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_INVENTORY, qrcode.getPOSITION_TO_DESCRIPTION());
        values.put(STOCK_TAKE_CD, qrcode.getSTOCK_TAKE_CD());
        values.put(LPN_CODE_INVENTORY, qrcode.getLPN_CODE());
        values.put(LPN_FROM_INVENTORY, qrcode.getLPN_FROM());
        values.put(LPN_TO_INVENTORY, qrcode.getLPN_TO());

        // insert row
        long id = db.insert(O_INVENTORY, null, values);
        return id;
    }

    public ArrayList<InventoryProduct>
    getoneProduct_Inventory(String CD, String expDate, String ea_unit, String INVENTORYCD, String stockindate) {
        ArrayList<InventoryProduct> qrcode = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_INVENTORY + " " + " WHERE "
                + PRODUCT_CD_INVENTORY + " = " + CD + " AND "
                + EA_UNIT_INVENTORY + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_INVENTORY + " like " + " '%" + expDate + "%'" + " AND "
                + STOCK_TAKE_CD + " = " + INVENTORYCD + " AND "
                + STOCKIN_DATE_INVENTORY + " like " + " '%" + stockindate + "%'";

        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                InventoryProduct qrcodeq = new InventoryProduct();
                qrcodeq.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_INVENTORY))));
                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_INVENTORY))));
                qrcodeq.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_INVENTORY))));
                qrcodeq.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_INVENTORY))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_INVENTORY))));
                qrcodeq.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_INVENTORY))));
                qrcodeq.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_INVENTORY))));
                qrcodeq.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_INVENTORY))));
                qrcodeq.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_INVENTORY))));
                qrcode.add(qrcodeq);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }

    public ArrayList<InventoryProduct>
    getAllProduct_Inventory() {
        ArrayList<InventoryProduct> INVENTORY = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_INVENTORY;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                InventoryProduct product = new InventoryProduct();
                product.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_INVENTORY))));
                product.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_INVENTORY))));
                product.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_INVENTORY))));
                product.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_INVENTORY))));
                product.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_INVENTORY))));
                product.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_INVENTORY))));
                product.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_INVENTORY))));
                product.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_INVENTORY))));
                product.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_INVENTORY))));
                product.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_INVENTORY))));
                product.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_INVENTORY))));
                product.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_INVENTORY))));
                product.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_INVENTORY))));
                product.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_INVENTORY))));
                product.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_INVENTORY))));
                product.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_INVENTORY))));
                product.setSTOCK_TAKE_CD((c.getString(c
                        .getColumnIndex(STOCK_TAKE_CD))));
                product.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_INVENTORY))));
                product.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_INVENTORY))));
                product.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_INVENTORY))));
                INVENTORY.add(product);
            } while (c.moveToNext());
        }

        c.close();
        return INVENTORY;
    }

    public ArrayList<InventoryProduct>
    getAllProduct_Inventory_Show(String INVENTORYCD) {
        ArrayList<InventoryProduct> INVENTORY = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_INVENTORY + " where " + STOCK_TAKE_CD + " = " + INVENTORYCD;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                InventoryProduct product = new InventoryProduct();
                product.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_INVENTORY))));
                product.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_INVENTORY))));
                product.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_INVENTORY))));
                product.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_INVENTORY))));
                product.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_INVENTORY))));
                product.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_INVENTORY))));
                product.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_INVENTORY))));
                product.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_INVENTORY))));
                product.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_INVENTORY))));
                product.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_INVENTORY))));
                product.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_INVENTORY))));
                product.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_INVENTORY))));
                product.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_INVENTORY))));
                product.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_INVENTORY))));
                product.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_INVENTORY))));
                product.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_INVENTORY))));
                product.setSTOCK_TAKE_CD((c.getString(c
                        .getColumnIndex(STOCK_TAKE_CD))));
                product.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_INVENTORY))));
                product.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_INVENTORY))));
                product.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_INVENTORY))));
                INVENTORY.add(product);
            } while (c.moveToNext());
        }

        c.close();
        return INVENTORY;
    }

    public ArrayList<InventoryProduct>
    getAllProduct_Inventory_Sync(String INVENTORYCD) {
        ArrayList<InventoryProduct> INVENTORY = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE,  REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_INVENTORY + " where " + STOCK_TAKE_CD + " = " + INVENTORYCD;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                InventoryProduct product = new InventoryProduct();

                product.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_INVENTORY))));
                product.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_INVENTORY))));
                product.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_INVENTORY))));
                product.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_INVENTORY))));
                product.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_INVENTORY))));
                product.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_INVENTORY))));
                product.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_INVENTORY))));
                product.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_INVENTORY))));
                product.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_INVENTORY))));
                product.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_INVENTORY))));
                product.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_INVENTORY))));
                product.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_INVENTORY))));
                product.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_INVENTORY))));
                product.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_INVENTORY))));
                product.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_INVENTORY))));
                product.setSTOCK_TAKE_CD((c.getString(c
                        .getColumnIndex(STOCK_TAKE_CD))));
                product.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_INVENTORY))));
                product.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_INVENTORY))));
                product.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_INVENTORY))));
                INVENTORY.add(product);
            } while (c.moveToNext());
        }

        c.close();
        return INVENTORY;
    }

    public int updateProduct_Inventory(InventoryProduct INVENTORY, String PRODUCT_CD, String sl, String ea_unit, String stock, String INVENTORYCD) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_INVENTORY, PRODUCT_CD);
        values.put(PRODUCT_CODE_INVENTORY, INVENTORY.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_INVENTORY, INVENTORY.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_INVENTORY, INVENTORY.getEXPIRED_DATE());
        values.put(EA_UNIT_INVENTORY, INVENTORY.getUNIT());
        values.put(QTY_SET_AVAILABLE_INVENTORY, sl);
        values.put(STOCK_TAKE_CD, INVENTORYCD);


        // updating row
        return db.update(O_INVENTORY, values, PRODUCT_CD_INVENTORY + " = ?" + " AND " + EXPIRED_DATE_INVENTORY + " = ?"
                        + " AND " + EA_UNIT_INVENTORY + " = ?" + " AND " + STOCKIN_DATE_INVENTORY + " = ?" + " AND " + STOCK_TAKE_CD + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(INVENTORY.getEXPIRED_DATE()), String.valueOf(ea_unit), String.valueOf(stock), String.valueOf(INVENTORYCD)});

    }

    public void deleteProduct_Inventory_CD(String INVENTORYCD) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_INVENTORY + " WHERE " + STOCK_TAKE_CD + " = " + INVENTORYCD);
    }
    public int updatePositionFrom_Inventory_LPN(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_INVENTORY, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_INVENTORY, descreption);

        values.put(POSITION_FROM_CODE_INVENTORY, from);
        values.put(LPN_FROM_INVENTORY, from);

        // updating row
        return db.update(O_INVENTORY, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_INVENTORY + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }

    public int updatePositionFrom_Inventory(String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_INVENTORY, wareHouse);
        values.put(POSITION_FROM_CODE_INVENTORY, from);
        values.put(LPN_FROM_INVENTORY, "");
        values.put(POSITION_FROM_DESCRIPTION_INVENTORY, descreption);

        // updating row
        return db.update(O_INVENTORY, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ? AND EA_UNIT = ? " + " AND "
                        + STOCKIN_DATE_INVENTORY + " = ? ",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});

    }

    public int updatePositionTo_Inventory_LPN(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_INVENTORY, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_INVENTORY, descreption);
        values.put(LPN_TO_INVENTORY, to);

        values.put(POSITION_TO_CODE_INVENTORY, to);
        // updating row
        return db.update(O_INVENTORY, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_INVENTORY + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }


    public int updatePositionTo_Inventory(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_INVENTORY, wareHouse);
        values.put(POSITION_TO_CODE_INVENTORY, to);

        values.put(POSITION_TO_DESCRIPTION_INVENTORY, descreption);
        // updating row
        return db.update(O_INVENTORY, values, "PRODUCT_CD" + " = ?" + " AND EXPIRY_DATE = ?  AND EA_UNIT = ?" + " AND " +
                        STOCKIN_DATE_INVENTORY + " = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});


    }
    //end table INVENTORY

    public void deleteProduct_Inventory_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_INVENTORY, AUTOINCREMENT_INVENTORY + " = ?" ,
                new String[]{String.valueOf(productCode) });

    }

    public int updatePositionTo_Stockin(String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String description, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO, wareHouse);
        values.put(POSITION_CODE, to);
        values.put(POSITION_DESCRIPTION, description);

        // updating row
        try {
            return db.update(O_QRCODE, values, "PRODUCT_CD" + " = ?" + " AND EXPIRED_DATE = ?  AND EA_UNIT = ?" + " AND " +
                            STOCKIN_DATE + " = ?",
                    new String[]{String.valueOf(PRODUCT_CD), String.valueOf(exPiredDate), String.valueOf(ea_unit), String.valueOf(stockinDate)});
        }catch (Exception e){
            Log.d("update vi tri nhap kho", e.getMessage());
        }
        return -1;
    }

}
