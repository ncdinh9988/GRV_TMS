package com.FiveSGroup.TMS;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.FiveSGroup.TMS.AddCustomerFragment.CCustomer;
import com.FiveSGroup.TMS.CancelGood.Product_CancelGood;
import com.FiveSGroup.TMS.ChangeCusFragment.UpdateCustomer;
import com.FiveSGroup.TMS.Inventory.InventoryProduct;
import com.FiveSGroup.TMS.LPN.LPN;
import com.FiveSGroup.TMS.LPN.LPNProduct;
import com.FiveSGroup.TMS.LetDown.LetDownProductSuggest;
import com.FiveSGroup.TMS.LetDown.ProductLetDown;
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
import com.FiveSGroup.TMS.StockOut.Product_StockOut;
import com.FiveSGroup.TMS.StockTransfer.Product_StockTransfer;
import com.FiveSGroup.TMS.TowingContainers.Product_Photo_Containers;
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

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
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
    public static final int DATABASE_VERSION = 172; // version của DB khi thay
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
        db.execSQL(CREATE_TABLE_O_BATCH);
        db.execSQL(CREATE_TABLE_O_CANCEL_GOOD);
        db.execSQL(CREATE_TABLE_O_TRANSFER_UNIT);
        db.execSQL(CREATE_TABLE_O_PO_RETURN);
        db.execSQL(CREATE_TABLE_O_TRANSFER_POSTING);
        db.execSQL(CREATE_TABLE_O_MATERIAL);
        db.execSQL(CREATE_TABLE_O_SP);
        db.execSQL(CREATE_TABLE_O_CHUYENMA);
        db.execSQL(CREATE_TABLE_O_PICKUP);
        db.execSQL(CREATE_TABLE_O_QA);
        db.execSQL(CREATE_TABLE_O_CRITERIA);
        db.execSQL(CREATE_TABLE_O_RESULT_QA);
        db.execSQL(CREATE_TABLE_O_PHOTO_QA);
        db.execSQL(CREATE_TABLE_O_RETURN_QA);
        db.execSQL(CREATE_TABLE_O_PRODUCT_SP);
        db.execSQL(CREATE_TABLE_O_LPN_SO);
        db.execSQL(CREATE_TABLE_O_PHOTO_CONTAINERS);
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

        try {
            db.execSQL("ALTER TABLE " + O_EXP + " ADD COLUMN  "
                    + TOTAL_SHELF_LIFE + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            db.execSQL("ALTER TABLE " + O_EXP + " ADD COLUMN  "
                    + SHELF_LIFE_TYPE + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            db.execSQL("ALTER TABLE " + O_EXP + " ADD COLUMN  "
                    + MIN_REM_SHELF_LIFE + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 74
        try {
            db.execSQL(CREATE_TABLE_O_BATCH);

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 78

        try {
            db.execSQL("ALTER TABLE " + O_QRCODE + " ADD COLUMN  "
                    + BATCH_NUMBER_CODE + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 92
        try {
            db.execSQL(CREATE_TABLE_O_CANCEL_GOOD);

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 94
        try {
            db.execSQL("ALTER TABLE " + O_LPN + " ADD COLUMN  "
                    + USER_CREATE + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            db.execSQL("ALTER TABLE " + O_LPN + " ADD COLUMN  "
                    + STORAGE + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 98
        try {
            db.execSQL(CREATE_TABLE_O_TRANSFER_UNIT);

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 99
        try {
            db.execSQL("ALTER TABLE " + O_TRANSFER_UNIT + " ADD COLUMN  "
                    + BARCODE + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 100
        try {
            db.execSQL("ALTER TABLE " + O_MASTER_PICK + " ADD COLUMN  "
                    + SUGGESTION_POSITION_MASTER_PICK + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 102
        try {
            db.execSQL(CREATE_TABLE_O_PO_RETURN);

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 104
        try {
            db.execSQL("ALTER TABLE " + O_BATCH + " ADD COLUMN  "
                    + AUTOINCREMENT_BATCH + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 108
        try {
            db.execSQL("ALTER TABLE " + O_EXP + " ADD COLUMN  "
                    + BATCH_NUMBER_TAM + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            db.execSQL("ALTER TABLE " + O_PO_RETURN + " ADD COLUMN  "
                    + BATCH_NUMBER_PO_RETURN + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 110
        try {
            db.execSQL("ALTER TABLE " + O_PO_RETURN + " ADD COLUMN  "
                    + MANUFACTURING_DATE_PO_RETURN + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 112
        try {
            db.execSQL(CREATE_TABLE_O_TRANSFER_POSTING);

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 114
        try {
            db.execSQL(CREATE_TABLE_O_MATERIAL);

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 116
        try {
            db.execSQL(CREATE_TABLE_O_SP);

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 118
        try {
            db.execSQL(CREATE_TABLE_O_CHUYENMA);

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 120
        try {
            db.execSQL("ALTER TABLE " + O_CHUYENMA + " ADD COLUMN  "
                    + PRODUCT_NAME_FROM_CHUYENMA + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            db.execSQL("ALTER TABLE " + O_CHUYENMA + " ADD COLUMN  "
                    + PRODUCT_NAME_TO_CHUYENMA + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            db.execSQL("ALTER TABLE " + O_CHUYENMA + " ADD COLUMN  "
                    + UNIT_2_CHUYENMA + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 124
        try {
            db.execSQL(CREATE_TABLE_O_PICKUP);

        } catch (Exception e) {
            // TODO: handle exception
        }
//version DB 126
        try {
            db.execSQL("ALTER TABLE " + O_PICKUP + " ADD COLUMN  "
                    + NOTE_PICKUP + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 128
        try {
            db.execSQL(CREATE_TABLE_O_QA);

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 130
        try {
            db.execSQL("ALTER TABLE " + O_QA + " ADD COLUMN  "
                    + BARCODE_QA + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 132
        try {
            db.execSQL(CREATE_TABLE_O_CRITERIA);

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 134
        try {
            db.execSQL("ALTER TABLE " + O_QA + " ADD COLUMN  "
                    + CHECKED_QA + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 140
        try {
            db.execSQL("ALTER TABLE " + O_CHUYENMA + " ADD COLUMN  "
                    + SUM_QTY_CHUYENMA + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 142
        try {
            db.execSQL(CREATE_TABLE_O_PHOTO_QA);

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 144
        try {
            db.execSQL("ALTER TABLE " + O_SALE_TAKE_PHOTO + " ADD COLUMN  "
                    + SALE_QA_CD_PHOTO + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 146
        try {
            db.execSQL("ALTER TABLE " + O_QA + " ADD COLUMN  "
                    + CHECKED_IMAGE_QA + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 148
        try {
            db.execSQL("ALTER TABLE " + O_SALE_TAKE_PHOTO + " ADD COLUMN  "
                    + BATCH_NUMBER_PHOTO + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 150
        try {
            db.execSQL("ALTER TABLE " + O_SALE_TAKE_PHOTO + " ADD COLUMN  "
                    + PRODUCT_CODE_PHOTO + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            db.execSQL("ALTER TABLE " + O_SALE_TAKE_PHOTO + " ADD COLUMN  "
                    + UNIT_PHOTO + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            db.execSQL("ALTER TABLE " + O_SALE_TAKE_PHOTO + " ADD COLUMN  "
                    + EXPIRED_DATE_PHOTO + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            db.execSQL("ALTER TABLE " + O_SALE_TAKE_PHOTO + " ADD COLUMN  "
                    + STOCKIN_DATE_PHOTO + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
//version DB 152
        try {
            db.execSQL("ALTER TABLE " + O_CRITERIA + " ADD COLUMN  "
                    + UNIT_CRITERIA + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 154
        try {
            db.execSQL("ALTER TABLE " + O_MASTER_PICK + " ADD COLUMN  "
                    + BATCH_NUMBER_MASTER_PICK + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            db.execSQL("ALTER TABLE " + O_PICK_LIST + " ADD COLUMN  "
                    + BATCH_NUMBER_PICKLIST + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 156
        try {
            db.execSQL("ALTER TABLE " + O_STOCK_OUT + " ADD COLUMN  "
                    + BATCH_NUMBER_STOCK_OUT + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 158
        try {
            db.execSQL(CREATE_TABLE_O_PRODUCT_SP);

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 160
        try {
            db.execSQL("ALTER TABLE " + O_EXP + " ADD COLUMN  "
                    + POSITION_CODE_TAM + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 162
        try {
            db.execSQL("ALTER TABLE " + O_EXP + " ADD COLUMN  "
                    + WAREHOUSE_POSITION_CD_TAM + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 163
        try {
            db.execSQL("ALTER TABLE " + O_EXP + " ADD COLUMN  "
                    + LPN_CODE_TAM + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 164
        try {
            db.execSQL("ALTER TABLE " + O_PUT_AWAY + " ADD COLUMN  "
                    + CREATE_TIME_PUTAWAY + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 165
        try {
            db.execSQL("ALTER TABLE " + O_QRCODE + " ADD COLUMN  "
                    + CREATE_TIME + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            db.execSQL("ALTER TABLE " + O_LET_DOWN + " ADD COLUMN  "
                    + CREATE_TIME_LETDOWN + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            db.execSQL("ALTER TABLE " + O_MASTER_PICK + " ADD COLUMN  "
                    + CREATE_TIME_MASTER_PICK + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }
        //version DB 166
        try {
            db.execSQL("ALTER TABLE " + O_STOCK_TRANSFER + " ADD COLUMN  "
                    + CREATE_TIME_STOCK_TRANSFER + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 167
        try {
            db.execSQL("ALTER TABLE " + O_LPN + " ADD COLUMN  "
                    + ORDER_CODE + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 168
        try {
            db.execSQL(CREATE_TABLE_O_LPN_SO);

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 169
        try {
            db.execSQL("ALTER TABLE " + O_PRODUCT_SP + " ADD COLUMN  "
                    + PRODUCT_CD_S_P + " TEXT  ");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //version DB 170
        try {
            db.execSQL(CREATE_TABLE_O_PHOTO_CONTAINERS);

        } catch (Exception e) {
            // TODO: handle exception
        }


        //version DB 172
        try {
            db.execSQL("ALTER TABLE " + O_SALE_TAKE_PHOTO + " ADD COLUMN  "
                    + WAREHOUSE_CONTAINER_CD_PHOTO + " TEXT  ");

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
    public static final String BATCH_NUMBER_WAREHOUSE_ADJUSTMENT = "BATCH_NUMBER_WAREHOUSE_ADJUSTMENT";

    public static final String CREATE_TABLE_O_WAREHOUSE_ADJUSTMENT = "CREATE TABLE "
            + O_WAREHOUSE_ADJUSTMENT + "("
            + AUTOINCREMENT_WAREHOUSE_ADJUSTMENT + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + BATCH_NUMBER_WAREHOUSE_ADJUSTMENT + " TEXT,"
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
//        values.put(AUTOINCREMENT_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getUNIT());
        values.put(BATCH_NUMBER_WAREHOUSE_ADJUSTMENT, warehouse_Adjustment.getBATCH_NUMBER());
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

    public int updatePositionFrom_warehouse_Adjustment_LPN(String id_unique_WA , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String warehouse_AdjustmentinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_WAREHOUSE_ADJUSTMENT, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_WAREHOUSE_ADJUSTMENT, descreption);
        values.put(POSITION_FROM_CODE_WAREHOUSE_ADJUSTMENT, from);
        values.put(LPN_FROM_WAREHOUSE_ADJUSTMENT, from);


        // updating row
        return db.update(O_WAREHOUSE_ADJUSTMENT, values, AUTOINCREMENT_WAREHOUSE_ADJUSTMENT + " = ? ",
                new String[]{String.valueOf(id_unique_WA)});

    }

    public int updatePositionFrom_warehouse_Adjustment(String id_unique_WA , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String warehouse_AdjustmentinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_WAREHOUSE_ADJUSTMENT, wareHouse);
        values.put(POSITION_FROM_CODE_WAREHOUSE_ADJUSTMENT, from);
        values.put(LPN_FROM_WAREHOUSE_ADJUSTMENT, "");
        values.put(POSITION_FROM_DESCRIPTION_WAREHOUSE_ADJUSTMENT, descreption);


        // updating row
        return db.update(O_WAREHOUSE_ADJUSTMENT, values,  AUTOINCREMENT_WAREHOUSE_ADJUSTMENT + " = ? ",
                new String[]{String.valueOf(id_unique_WA)});

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
    getoneProduct_Warehouse_Adjustment(String CD, String expDate, String ea_unit, String warehouse_AdjustmentinDate,
                                       String warehouse_Adjustment_cd, String batch_number) {
        ArrayList<Product_Warehouse_Adjustment> warehouse_Adjustments = new ArrayList<Product_Warehouse_Adjustment>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_WAREHOUSE_ADJUSTMENT + " " + " WHERE "
                + PRODUCT_CD_WAREHOUSE_ADJUSTMENT + " = " + CD + " AND "
                + warehouse_Adjustment_CD + " = " + warehouse_Adjustment_cd + " AND "
                + BATCH_NUMBER_WAREHOUSE_ADJUSTMENT + " like " + " '%" + batch_number + "%'" + " AND "
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
                warehouse_Adjustment.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_WAREHOUSE_ADJUSTMENT))));
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
                warehouse_Adjustment.setAUTOINCREMENT(c.getString(c
                        .getColumnIndex(AUTOINCREMENT_WAREHOUSE_ADJUSTMENT)));
                warehouse_Adjustment.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_WAREHOUSE_ADJUSTMENT))));
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


    public int updateProduct_Warehouse_Adjustment(Product_Warehouse_Adjustment warehouse_Adjustment,String incre_wa, String PRODUCT_CD, String sl, String ea_unit, String warehouse, String warehouse_Adjustment_cd) {
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
        return db.update(O_WAREHOUSE_ADJUSTMENT, values,  AUTOINCREMENT_WAREHOUSE_ADJUSTMENT + " = ?",
                new String[]{String.valueOf(incre_wa)});

    }




    public void deleteProduct_Warehouse_Adjustment(String warehouse) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_WAREHOUSE_ADJUSTMENT + " WHERE " + warehouse_Adjustment_CD + " = " + warehouse);
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

    public ArrayList<ProductLetDown>
    getautoProduct_Letdown() {
        ArrayList<ProductLetDown> letdown = new ArrayList<ProductLetDown>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_LET_DOWN + " ORDER BY " + AUTOINCREMENT_LETDOWN + " DESC LIMIT 1 ";;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                ProductLetDown qrcode_letdown = new ProductLetDown();
                qrcode_letdown.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_LETDOWN))));
                letdown.add(qrcode_letdown);
            } while (c.moveToNext());
        }

        c.close();
        return letdown;
    }

    public ArrayList<ProductLetDown>
    getposition_Letdown() {
        ArrayList<ProductLetDown> qrcode = new ArrayList<ProductLetDown>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_LET_DOWN + " ORDER BY " + AUTOINCREMENT_LETDOWN + " DESC LIMIT 1 ";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                ProductLetDown qrcode_letdown = new ProductLetDown();

                qrcode_letdown.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_LETDOWN))));

                qrcode_letdown.setPOSITION_FROM_CD(c.getString(c
                        .getColumnIndex(POSITION_FROM_LETDOWN)));

                qrcode_letdown.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_LETDOWN))));

                qrcode_letdown.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_LETDOWN))));


                qrcode.add(qrcode_letdown);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
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
    //database from table O_LPN_SO
    public static final String O_LPN_SO = "O_LPN_SO";
    public static final String PRODUCT_CD_LPN_SO = "PRODUCT_CD_LPN_SO";
    public static final String PRODUCT_CODE_LPN_SO = "PRODUCT_CODE_LPN_SO";
    public static final String PRODUCT_NAME_LPN_SO = "PRODUCT_NAME_LPN_SO";
    public static final String POSITION_CODE_LPN_SO= "POSITION_CODE_LPN_SO";
    public static final String BY_ORDER_LPN_SO  = "BY_ORDER_LPN_SO";
    public static final String SO_QTY_LPN_SO  = "SO_QTY_LPN_SO";
    public static final String UNIT_SET_LPN_SO  = "UNIT_SET_LPN_SO";
    public static final String DVT_SET_LPN_SO  = "DVT_SET_LPN_SO";

    public static final String CREATE_TABLE_O_LPN_SO = "CREATE TABLE "
            + O_LPN_SO + "("
            + PRODUCT_CD_LPN_SO + " TEXT,"
            + PRODUCT_CODE_LPN_SO + " TEXT,"
            + PRODUCT_NAME_LPN_SO + " TEXT,"
            + POSITION_CODE_LPN_SO + " TEXT,"
            + BY_ORDER_LPN_SO + " TEXT,"
            + SO_QTY_LPN_SO + " TEXT,"
            + UNIT_SET_LPN_SO + " TEXT,"
            + DVT_SET_LPN_SO + " TEXT" + ")";



    public long CreateLPNwithSO(ProductLpnWithSo lpn) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_LPN_SO, lpn.getPRODUCT_CD());
        values.put(PRODUCT_CODE_LPN_SO, lpn.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_LPN_SO, lpn.getPRODUCT_NAME());
        values.put(POSITION_CODE_LPN_SO, lpn.getPOSITION_CODE());
        values.put(BY_ORDER_LPN_SO, lpn.getBY_ORDER());
        values.put(SO_QTY_LPN_SO, lpn.getSO_QTY());
        values.put(UNIT_SET_LPN_SO, lpn.getUNIT_SET());
        values.put(DVT_SET_LPN_SO, lpn.getDVT_SET());
        // insert row
        long id = db.insert(O_LPN_SO, null, values);
        return id;
    }

    public ArrayList<ProductLpnWithSo>
    getAllLPNSO(String by_order) {
        ArrayList<ProductLpnWithSo> lpn = new ArrayList<ProductLpnWithSo>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_LPN_SO + " WHERE " + BY_ORDER_LPN_SO + " like '%" + by_order + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                ProductLpnWithSo lpn1 = new ProductLpnWithSo();
                lpn1.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_LPN_SO))));
                lpn1.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_LPN_SO))));
                lpn1.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_LPN_SO))));
                lpn1.setPOSITION_CODE((c.getString(c
                        .getColumnIndex(POSITION_CODE_LPN_SO))));
                lpn1.setBY_ORDER((c.getString(c
                        .getColumnIndex(BY_ORDER_LPN_SO))));
                lpn1.setSO_QTY((c.getString(c
                        .getColumnIndex(SO_QTY_LPN_SO))));
                lpn1.setUNIT_SET((c.getString(c
                        .getColumnIndex(UNIT_SET_LPN_SO))));
                lpn1.setDVT_SET((c.getString(c
                        .getColumnIndex(DVT_SET_LPN_SO))));
                lpn.add(lpn1);
            } while (c.moveToNext());
        }
        c.close();
        return lpn;
    }

    public void deleteallProduct_LPN_SO() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_LPN_SO);
    }

    //CLose table O_LPN_SO

    //database from table O_LPN
    public static final String O_LPN = "O_LPN";
    public static final String LPN_CODE = "LPN_CODE";
    public static final String LPN_DATE = "LPN_DATE";
    public static final String LPN_NUMBER = "LPN_NUMBER";
    public static final String USER_CREATE  = "USER_CREATE";
    public static final String STORAGE  = "STORAGE";
    public static final String ORDER_CODE  = "ORDER_CODE";

    public static final String CREATE_TABLE_O_LNP = "CREATE TABLE "
            + O_LPN + "("
            + LPN_NUMBER + " TEXT,"
            + LPN_CODE + " TEXT,"
            + ORDER_CODE + " TEXT,"
            + USER_CREATE + " TEXT,"
            + STORAGE + " TEXT,"
            + LPN_DATE + " TEXT" + ")";



    public long CreateLPN(LPN lpn) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(LPN_CODE, lpn.getLPN_CODE());
        values.put(LPN_DATE, lpn.getLPN_DATE());
        values.put(LPN_NUMBER, lpn.getLPN_NUMBER());
        values.put(STORAGE, lpn.getSTORAGE());
        values.put(USER_CREATE, lpn.getUSER_CREATE());
        values.put(ORDER_CODE, lpn.getORDER_CODE());
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
                lpn1.setUSER_CREATE((c.getString(c
                        .getColumnIndex(USER_CREATE))));
                lpn1.setSTORAGE((c.getString(c
                        .getColumnIndex(STORAGE))));
                lpn.add(lpn1);
            } while (c.moveToNext());
        }
        c.close();
        return lpn;
    }

    public ArrayList<LPN>
    getAllLpnBarcodewithSO(String barcode) {
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
                lpn1.setORDER_CODE((c.getString(c
                        .getColumnIndex(ORDER_CODE))));
                lpn1.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE))));
                lpn1.setLPN_DATE((c.getString(c
                        .getColumnIndex(LPN_DATE))));
                lpn1.setUSER_CREATE((c.getString(c
                        .getColumnIndex(USER_CREATE))));
                lpn1.setSTORAGE((c.getString(c
                        .getColumnIndex(STORAGE))));
                lpn.add(lpn1);
            } while (c.moveToNext());
        }
        c.close();
        return lpn;
    }

    public ArrayList<LPN>
    getAllLpn_date(String lpn_date) {
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
                lpn1.setORDER_CODE((c.getString(c
                        .getColumnIndex(ORDER_CODE))));
                lpn1.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE))));
                lpn1.setLPN_DATE((c.getString(c
                        .getColumnIndex(LPN_DATE))));
                lpn1.setUSER_CREATE((c.getString(c
                        .getColumnIndex(USER_CREATE))));
                lpn1.setSTORAGE((c.getString(c
                        .getColumnIndex(STORAGE))));

                lpn.add(lpn1);
            } while (c.moveToNext());
        }
        c.close();
        return lpn;
    }

    public ArrayList<LPN>
    getAllLpn_limit() {
        ArrayList<LPN> lpn = new ArrayList<LPN>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_LPN + " LIMIT 100" ;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                LPN lpn1 = new LPN();
                lpn1.setLPN_NUMBER((c.getString(c
                        .getColumnIndex(LPN_NUMBER))));
                lpn1.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE))));
                lpn1.setORDER_CODE((c.getString(c
                        .getColumnIndex(ORDER_CODE))));
                lpn1.setLPN_DATE((c.getString(c
                        .getColumnIndex(LPN_DATE))));
                lpn1.setUSER_CREATE((c.getString(c
                        .getColumnIndex(USER_CREATE))));
                lpn1.setSTORAGE((c.getString(c
                        .getColumnIndex(STORAGE))));
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
        String selectQuery = "SELECT * FROM " + O_LPN ;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                LPN lpn1 = new LPN();
                lpn1.setLPN_NUMBER((c.getString(c
                        .getColumnIndex(LPN_NUMBER))));
                lpn1.setORDER_CODE((c.getString(c
                        .getColumnIndex(ORDER_CODE))));
                lpn1.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE))));
                lpn1.setLPN_DATE((c.getString(c
                        .getColumnIndex(LPN_DATE))));
                lpn1.setUSER_CREATE((c.getString(c
                        .getColumnIndex(USER_CREATE))));
                lpn1.setSTORAGE((c.getString(c
                        .getColumnIndex(STORAGE))));
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
    public static final String BATCH_NUMBER_RETURN_WAREHOUSE = "BATCH_NUMBER_RETURN_WAREHOUSE";

    public static final String CREATE_TABLE_O_RETURN_WAREHOUSE = "CREATE TABLE "
            + O_RETURN_WAREHOUSE + "("
            + AUTOINCREMENT_RETURN_WAREHOUSE + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_RETURN_WAREHOUSE + " TEXT,"
            + PRODUCT_NAME_RETURN_WAREHOUSE + " TEXT,"
            + PRODUCT_CODE_RETURN_WAREHOUSE + " TEXT,"
            + BATCH_NUMBER_RETURN_WAREHOUSE + " TEXT,"
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
//        values.put(AUTOINCREMENT_RETURN_WAREHOUSE,returnWarehouse.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_RETURN_WAREHOUSE, returnWarehouse.getUNIT());
        values.put(BATCH_NUMBER_RETURN_WAREHOUSE, returnWarehouse.getBATCH_NUMBER());
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
    getoneProduct_Return_WareHouse(String CD, String expDate, String ea_unit, String return_warehouseinDate, String returnWarehouse_cd,String batch_number) {
        ArrayList<Product_Return_WareHouse> returnWarehouses = new ArrayList<Product_Return_WareHouse>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_RETURN_WAREHOUSE + " " + " WHERE "
                + PRODUCT_CD_RETURN_WAREHOUSE + " = " + CD + " AND "
                + RETURN_CD + " = " + returnWarehouse_cd + " AND "
                + BATCH_NUMBER_RETURN_WAREHOUSE + " like " + " '%" + batch_number + "%'" + " AND "
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
                returnWarehouse.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_RETURN_WAREHOUSE))));
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
                return_warehouse.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_RETURN_WAREHOUSE))));
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
                return_warehouse.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_RETURN_WAREHOUSE))));
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


    public int updateProduct_Return_WareHouse(Product_Return_WareHouse returnWarehouse,String incre_rw, String PRODUCT_CD, String sl, String ea_unit, String return_warehouse, String returnWarehouse_cd) {
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
        return db.update(O_RETURN_WAREHOUSE, values,  AUTOINCREMENT_RETURN_WAREHOUSE + " = ?",
                new String[]{String.valueOf(incre_rw)});

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
    public static final String BATCH_NUMBER_MASTER_PICK = "BATCH_NUMBER";
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
    public static final String CREATE_TIME_MASTER_PICK = "CREATE_TIME_MASTER_PICK";
    public static final String SUGGESTION_POSITION_MASTER_PICK = "SUGGESTION_POSITION_MASTER_PICK";
//    public static final String SUGGESTION_POSITION_TO_MASTER_PICK = "SUGGESTION_POSITION_TO_MASTER_PICK";

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
            + BATCH_NUMBER_MASTER_PICK + " TEXT,"
            + EA_UNIT_MASTER_PICK + " TEXT,"
            + POSITION_FROM_MASTER_PICK + " TEXT,"
            + POSITION_FROM_CODE_MASTER_PICK + " TEXT,"
            + POSITION_FROM_DESCRIPTION_MASTER_PICK + " TEXT,"
            + POSITION_TO_MASTER_PICK + " TEXT,"
            + POSITION_TO_CODE_MASTER_PICK + " TEXT,"
            + POSITION_TO_DESCRIPTION_MASTER_PICK + " TEXT,"
            + MASTER_PICK_CD + " TEXT,"
            + CREATE_TIME_MASTER_PICK + " TEXT,"
            + UNIQUE_CODE_MASTER_PICK + " TEXT ,"
            + LPN_CD_MASTER_PICK + " TEXT ,"
            + LPN_CODE_MASTER_PICK + " TEXT ,"
            + LPN_FROM_MASTER_PICK + " TEXT ,"
            + SUGGESTION_POSITION_MASTER_PICK + " TEXT ,"
            + LPN_TO_MASTER_PICK + " TEXT "
            + ")";


    public long CreateMaster_Pick(Product_Master_Pick masterPick) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
//        values.put(AUTOINCREMENT_MASTER_PICK , masterPick.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_MASTER_PICK, masterPick.getUNIT());
        values.put(PRODUCT_CODE_MASTER_PICK, masterPick.getPRODUCT_CODE());
        values.put(CREATE_TIME_MASTER_PICK, masterPick.getCREATE_TIME());
        values.put(PRODUCT_NAME_MASTER_PICK, masterPick.getPRODUCT_NAME());
        values.put(SUGGESTION_POSITION_MASTER_PICK, masterPick.getSUGGESTION_POSITION());
        values.put(PRODUCT_CD_MASTER_PICK, masterPick.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_MASTER_PICK, masterPick.getQTY());
        values.put(STOCKIN_DATE_MASTER_PICK, masterPick.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_MASTER_PICK, masterPick.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_MASTER_PICK, masterPick.getEXPIRED_DATE());
        values.put(BATCH_NUMBER_MASTER_PICK, masterPick.getBATCH_NUMBER());
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

    public int updatePositionFrom_masterPick_LPN(String unique_id , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_MASTER_PICK, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_MASTER_PICK, descreption);
        values.put(POSITION_FROM_CODE_MASTER_PICK, from);

        values.put(LPN_FROM_MASTER_PICK, from);


        // updating row
        return db.update(O_MASTER_PICK, values,
                AUTOINCREMENT_MASTER_PICK + " = ? ",
                new String[]{String.valueOf(unique_id)});

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

    public int updatePositionTo_masterPick_LPN(String unique_id ,String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_MASTER_PICK, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_MASTER_PICK, descreption);
        values.put(LPN_TO_MASTER_PICK, to);

        values.put(POSITION_TO_CODE_MASTER_PICK, to);
        // updating row
        return db.update(O_MASTER_PICK, values,
                AUTOINCREMENT_MASTER_PICK + " = ? ",
                new String[]{String.valueOf(unique_id)});


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
    getoneListSP_Master_Pick(String unique_id) {
        ArrayList<Product_Master_Pick> masterPicks = new ArrayList<Product_Master_Pick>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_MASTER_PICK + " " + " WHERE "
                + AUTOINCREMENT_MASTER_PICK + " = " + unique_id ;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_Master_Pick masterPick = new Product_Master_Pick();
                masterPick.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_MASTER_PICK))));
                masterPick.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_MASTER_PICK))));
                masterPick.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME_MASTER_PICK))));
                masterPick.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_MASTER_PICK))));
                masterPick.setSUGGESTION_POSITION((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_MASTER_PICK))));
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
                masterPick.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_MASTER_PICK))));
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
    getoneProduct_Master_Pick(String CD, String expDate, String ea_unit, String stockinDate, String master_pick_cd,String batch_number) {
        ArrayList<Product_Master_Pick> masterPicks = new ArrayList<Product_Master_Pick>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_MASTER_PICK + " " + " WHERE "
                + PRODUCT_CD_MASTER_PICK + " = " + CD + " AND "
                + MASTER_PICK_CD + " = " + master_pick_cd + " AND "
                + BATCH_NUMBER_MASTER_PICK + " like " + " '%" + batch_number + "%'" + " AND "
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
                masterPick.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME_MASTER_PICK))));
                masterPick.setSUGGESTION_POSITION((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_MASTER_PICK))));
                masterPick.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_MASTER_PICK))));
                masterPick.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_MASTER_PICK))));
                masterPick.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_MASTER_PICK))));
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

    public int getDuplicate_LoadPallet(){
        String selectQuery =         "SELECT STOCKIN_DATE , PRODUCT_CODE , EXPIRY_DATE , PRODUCT_NAME , EA_UNIT , POSITION_FROM_CODE , POSITION_TO_CODE , COUNT(*) AS COUNT FROM "
                + O_LOAD_PALLET + " GROUP BY STOCKIN_DATE , PRODUCT_CODE , EXPIRY_DATE , PRODUCT_NAME , EA_UNIT , POSITION_FROM_CODE , POSITION_TO_CODE  HAVING COUNT(*) > " + 1 ;
        SQLiteDatabase db = this.getReadableDatabase(DatabaseHelper.PWD);
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        int count = cursor.getColumnIndex(PRODUCT_NAME);
//        cursor.close();
        Cursor cursor = db.rawQuery(selectQuery, null);

// Iterate through cursor
        cursor.moveToFirst();
        int count_check = cursor.getInt(7);
        return count_check;
    }

    public int getDuplicate_MasterPick(String masterCD){
        String selectQuery =         "SELECT STOCKIN_DATE , PRODUCT_CODE , EXPIRY_DATE , PRODUCT_NAME , EA_UNIT , POSITION_FROM_CODE , POSITION_TO_CODE , COUNT(*) AS COUNT   FROM "
                + O_MASTER_PICK + " WHERE " + MASTER_PICK_CD + " = " + masterCD
                + " GROUP BY STOCKIN_DATE , PRODUCT_CODE , EXPIRY_DATE , PRODUCT_NAME , EA_UNIT , POSITION_FROM_CODE , POSITION_TO_CODE  HAVING COUNT(*) > " + 1 ;
        SQLiteDatabase db = this.getReadableDatabase(DatabaseHelper.PWD);
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        int count = cursor.getColumnIndex(PRODUCT_NAME);
//        cursor.close();
        Cursor cursor = db.rawQuery(selectQuery, null);

// Iterate through cursor
        cursor.moveToFirst();
        int count_check = cursor.getInt(7);
        return count_check;
    }


    public ArrayList<Product_Master_Pick>
    getAllProduct_Master_Pick_Sync(String master_pick_cd) {
        ArrayList<Product_Master_Pick> masterPicks = new ArrayList<Product_Master_Pick>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(STOCKIN_DATE,'---','') as STOCKIN_DATE ," +
                " REPLACE(POSITION_FROM_CODE,'---','') " +
                "as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') " +
                "as POSITION_TO_CODE FROM " + O_MASTER_PICK + " where " + MASTER_PICK_CD + " = " + master_pick_cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_Master_Pick masterPick = new Product_Master_Pick();
//                masterPick.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_MASTER_PICK))));
                masterPick.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_MASTER_PICK))));
                masterPick.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME_MASTER_PICK))));
                masterPick.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_MASTER_PICK))));
                masterPick.setSUGGESTION_POSITION((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_MASTER_PICK))));
                masterPick.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_MASTER_PICK))));
                masterPick.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_MASTER_PICK))));
                masterPick.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_MASTER_PICK))));
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
                masterPick.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME_MASTER_PICK))));
                masterPick.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_MASTER_PICK))));
                masterPick.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_MASTER_PICK))));
                masterPick.setSUGGESTION_POSITION((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_MASTER_PICK))));
                masterPick.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_MASTER_PICK))));
                masterPick.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_MASTER_PICK))));
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


    public int updateProduct_Master_Pick(Product_Master_Pick masterPick, String auto_incre,String PRODUCT_CD, String sl, String ea_unit, String stock, String master_pick_cd) {
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
        return db.update(O_MASTER_PICK, values,  AUTOINCREMENT_MASTER_PICK + " = ?",
                new String[]{String.valueOf(auto_incre)});

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
    public static final String BATCH_NUMBER_PICKLIST = "BATCH_NUMBER";
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
            + BATCH_NUMBER_PICKLIST + " TEXT,"
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
        values.put(BATCH_NUMBER_PICKLIST, qrcode.getBATCH_NUMBER());
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
    getoneProduct_PickList(String CD, String expDate, String ea_unit, String PickListCD, String stockindate,String batch_number) {
        ArrayList<PickList> qrcode = new ArrayList<PickList>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_PICK_LIST + " " + " WHERE "
                + PRODUCT_CD_PICKLIST + " = " + CD + " AND "
                + BATCH_NUMBER_PICKLIST + " like " + " '%" + batch_number + "%'" + " AND "
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
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PICKLIST))));
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
    getAllProduct_PickList(String pick) {
        ArrayList<PickList> picklist = new ArrayList<PickList>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_PICK_LIST + " WHERE " + PICKLIST_CD + " = " + pick;
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
                product.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PICKLIST))));
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
                product.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PICKLIST))));
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
        String selectQuery = "SELECT *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(STOCKIN_DATE,'---','') as STOCKIN_DATE , REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_PICK_LIST + " where " + PICKLIST_CD + " = " + pickListCD;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                PickList product = new PickList();
//                product.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_PICKLIST))));
                product.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_PICKLIST))));
                product.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PICKLIST))));
                product.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PICKLIST))));
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

    public int updateProduct_PickList(PickList picklist,String incre_pl, String PRODUCT_CD, String sl, String ea_unit, String stock, String pickListCD) {

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
        return db.update(O_PICK_LIST, values,  AUTOINCREMENT_PICKLIST + " = ?",
                new String[]{String.valueOf(incre_pl)});

    }


    public void deleteProduct_PickList_CD(String pickListCD) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_PICK_LIST + " WHERE " + PICKLIST_CD + " = " + pickListCD);
    }
    public int updatePositionFrom_PickList_LPN(String id_unique_PL , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_PICKLIST, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_PICKLIST, descreption);
        values.put(POSITION_FROM_CODE_PICKLIST, from);
        values.put(LPN_FROM_PICKLIST, from);

        // updating row
        return db.update(O_PICK_LIST, values,  AUTOINCREMENT_PICKLIST + " = ? ",
                new String[]{String.valueOf(id_unique_PL)});

    }

    public int updatePositionFrom_PickList(String id_unique_PL , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_PICKLIST, wareHouse);
        values.put(POSITION_FROM_CODE_PICKLIST, from);
        values.put(LPN_FROM_PICKLIST, "");
        values.put(POSITION_FROM_DESCRIPTION_PICKLIST, descreption);

        // updating row
        return db.update(O_PICK_LIST, values, AUTOINCREMENT_PICKLIST + " = ? ",
                new String[]{String.valueOf(id_unique_PL)});

    }

    public int updatePositionTo_PickList_LPN(String id_unique_PL , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_PICKLIST, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_PICKLIST, descreption);
        values.put(LPN_TO_PICKLIST, to);

        values.put(POSITION_TO_CODE_PICKLIST, to);
        // updating row
        return db.update(O_PICK_LIST, values,
                AUTOINCREMENT_PICKLIST + " = ?",
                new String[]{String.valueOf(id_unique_PL)});


    }


    public int updatePositionTo_PickList(String id_unique_PL , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_PICKLIST, wareHouse);
        values.put(POSITION_TO_CODE_PICKLIST, to);
        values.put(LPN_TO_PICKLIST, "");

        values.put(POSITION_TO_DESCRIPTION_PICKLIST, descreption);
        // updating row
        return db.update(O_PICK_LIST, values,
                AUTOINCREMENT_PICKLIST + " = ?",
                new String[]{String.valueOf(id_unique_PL)});

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
    public static final String BATCH_NUMBER_LOAD_PALLET = "BATCH_NUMBER_LOAD_PALLET";

    public static final String CREATE_TABLE_O_LOAD_PALLET = "CREATE TABLE "
            + O_LOAD_PALLET + "("
            + AUTOINCREMENT_LOAD_PALLET + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_LOAD_PALLET + " TEXT,"
            + PRODUCT_NAME_LOAD_PALLET + " TEXT,"
            + PRODUCT_CODE_LOAD_PALLET + " TEXT,"
            + BATCH_NUMBER_LOAD_PALLET + " TEXT,"
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
//        values.put(AUTOINCREMENT_LOAD_PALLET, product_loadPallet.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_LOAD_PALLET, product_loadPallet.getUNIT());
        values.put(PRODUCT_CODE_LOAD_PALLET, product_loadPallet.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_LOAD_PALLET, product_loadPallet.getPRODUCT_NAME());
        values.put(PRODUCT_CD_LOAD_PALLET, product_loadPallet.getPRODUCT_CD());
        values.put(BATCH_NUMBER_LOAD_PALLET, product_loadPallet.getBATCH_NUMBER());
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

    public ArrayList<Product_LoadPallet> getoneProduct_LoadPallet(String CD, String expDate, String ea_unit, String stockinDate,String batch_number) {
        ArrayList<Product_LoadPallet> loadPallets = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_LOAD_PALLET + " " + " WHERE "
                + PRODUCT_CD_LOAD_PALLET + " = " + CD + " AND "
                + BATCH_NUMBER_LOAD_PALLET + " like " + " '%" + batch_number + "%'" + " AND "
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
                product_loadPallet.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_LOAD_PALLET))));
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

    public ArrayList<Product_LoadPallet> getAllProduct_LoadPallet(String lpn_code) {
        ArrayList<Product_LoadPallet> loadPallets = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_LOAD_PALLET + " WHERE " + LPN_TO_LOAD_PALLET + " like '%" + lpn_code + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_LoadPallet product_loadPallet = new Product_LoadPallet();
                product_loadPallet.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_LOAD_PALLET))));
                product_loadPallet.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_LOAD_PALLET))));
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

    public ArrayList<Product_LoadPallet> getAllProduct_LoadPallet_Sync(String lpn_code) {
        ArrayList<Product_LoadPallet> loadPallets = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') " +
                "as POSITION_TO_CODE FROM " + O_LOAD_PALLET + " WHERE " + LPN_TO_LOAD_PALLET + " like '%" + lpn_code + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_LoadPallet product_loadPallet = new Product_LoadPallet();

//                product_loadPallet.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_LOAD_PALLET))));
                product_loadPallet.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_LOAD_PALLET))));
                product_loadPallet.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_LOAD_PALLET))));
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

    public int updateProduct_LoadPallet(Product_LoadPallet product_loadPallet,String auto_incre, String PRODUCT_CD, String sl, String ea_unit, String stock) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_LOAD_PALLET, PRODUCT_CD);
        values.put(PRODUCT_CODE_LOAD_PALLET, product_loadPallet.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_LOAD_PALLET, product_loadPallet.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_LOAD_PALLET, product_loadPallet.getEXPIRED_DATE());
        values.put(EA_UNIT_LOAD_PALLET, product_loadPallet.getUNIT());
        values.put(QTY_SET_AVAILABLE_LOAD_PALLET, sl);

        // updating row
        return db.update(O_LOAD_PALLET, values,  AUTOINCREMENT_LOAD_PALLET + " = ?",
                new String[]{String.valueOf(auto_incre)});

    }



    public void deleteProduct_LoadPallet() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_LOAD_PALLET );
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
    public static final String BATCH_NUMBER_REMOVE_LPN = "BATCH_NUMBER_REMOVE_LPN";

    public static final String CREATE_TABLE_O_REMOVE_LPN = "CREATE TABLE "
            + O_REMOVE_LPN + "("
            + AUTOINCREMENT_REMOVE_LPN + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_REMOVE_LPN + " TEXT,"
            + PRODUCT_NAME_remove_TRANSFER + " TEXT,"
            + PRODUCT_CODE_REMOVE_LPN + " TEXT,"
            + QTY_EA_AVAILABLE_REMOVE_LPN + " TEXT,"
            + QTY_SET_AVAILABLE_REMOVE_LPN + " TEXT,"
            + EXPIRED_DATE_REMOVE_LPN + " TEXT,"
            + BATCH_NUMBER_REMOVE_LPN + " TEXT,"
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
//        values.put(AUTOINCREMENT_REMOVE_LPN,remove_lpn.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_REMOVE_LPN, remove_lpn.getUNIT());
        values.put(PRODUCT_CODE_REMOVE_LPN, remove_lpn.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_remove_TRANSFER, remove_lpn.getPRODUCT_NAME());
        values.put(BATCH_NUMBER_REMOVE_LPN, remove_lpn.getBATCH_NUMBER());
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
    getoneProduct_Remove_LPN(String CD, String expDate, String ea_unit, String removeinDate,String batch_number) {
        ArrayList<Product_Remove_LPN> remove_lpns = new ArrayList<Product_Remove_LPN>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_REMOVE_LPN + " " + " WHERE "
                + PRODUCT_CD_REMOVE_LPN + " = " + CD + " AND "
                + BATCH_NUMBER_REMOVE_LPN + " like " + " '%" + batch_number + "%'" + " AND "
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
                remove_lpn.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_REMOVE_LPN))));
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
                remove.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_REMOVE_LPN))));
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
//                remove.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_REMOVE_LPN))));
                remove.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_REMOVE_LPN))));
                remove.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_REMOVE_LPN))));
                remove.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_remove_TRANSFER))));
                remove.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_REMOVE_LPN))));
                remove.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_REMOVE_LPN))));
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

    public int updateProduct_Remove_LPN(Product_Remove_LPN remove_lpn,String incre_rl, String PRODUCT_CD, String sl, String ea_unit, String remove) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_REMOVE_LPN, PRODUCT_CD);
        values.put(PRODUCT_CODE_REMOVE_LPN, remove_lpn.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_remove_TRANSFER, remove_lpn.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_REMOVE_LPN, remove_lpn.getEXPIRED_DATE());
        values.put(EA_UNIT_REMOVE_LPN, remove_lpn.getUNIT());
        values.put(QTY_SET_AVAILABLE_REMOVE_LPN, sl);

        // updating row
        return db.update(O_REMOVE_LPN, values, AUTOINCREMENT_REMOVE_LPN + " = ?",
                new String[]{String.valueOf(incre_rl)});

    }

    public int updatePositionFrom_Remove(String id_unique_RML , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_REMOVE_LPN, wareHouse);
        values.put(POSITION_FROM_CODE_REMOVE_LPN, from);
        values.put(LPN_FROM_REMOVE_LPN, "");
        values.put(POSITION_FROM_DESCRIPTION_REMOVE_LPN, descreption);

        // updating row
        return db.update(O_REMOVE_LPN, values,  AUTOINCREMENT_REMOVE_LPN + " = ? ",
                new String[]{String.valueOf(id_unique_RML)});

    }

    public int updatePositionFrom_Remove_LPN(String id_unique_RML , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_REMOVE_LPN, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_REMOVE_LPN, descreption);

        values.put(POSITION_FROM_CODE_REMOVE_LPN, from);
        values.put(LPN_FROM_REMOVE_LPN, from);

        // updating row
        return db.update(O_REMOVE_LPN, values, AUTOINCREMENT_REMOVE_LPN + " = ? ",
                new String[]{String.valueOf(id_unique_RML)});

    }

    public int updatePositionTo_Remove_LPN(String id_unique_RML , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_REMOVE_LPN, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_REMOVE_LPN, descreption);
        values.put(LPN_TO_REMOVE_LPN, to);
        values.put(POSITION_TO_CODE_REMOVE_LPN, to);
        // updating row
        return db.update(O_REMOVE_LPN, values,
                AUTOINCREMENT_REMOVE_LPN + " = ?",
                new String[]{String.valueOf(id_unique_RML)});
    }
    public int updatePositionTo_Remove(String id_unique_RML , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_REMOVE_LPN, wareHouse);
        values.put(POSITION_TO_CODE_REMOVE_LPN, to);
        values.put(LPN_TO_REMOVE_LPN, "");

        values.put(POSITION_TO_DESCRIPTION_REMOVE_LPN, descreption);
        // updating row
        return db.update(O_REMOVE_LPN, values,
                AUTOINCREMENT_REMOVE_LPN + " = ?",
                new String[]{String.valueOf(id_unique_RML)});
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
    public static final String CREATE_TIME_STOCK_TRANSFER = "CREATE_TIME_STOCK_TRANSFER";
    public static final String BATCH_NUMBER_STOCK_TRANSFER = "BATCH_NUMBER_STOCK_TRANSFER";


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
            + BATCH_NUMBER_STOCK_TRANSFER + " TEXT,"
            + EA_UNIT_STOCK_TRANSFER + " TEXT,"
            + POSITION_FROM_STOCK_TRANSFER + " TEXT,"
            + POSITION_FROM_CODE_STOCK_TRANSFER + " TEXT,"
            + POSITION_FROM_DESCRIPTION_STOCK_TRANSFER + " TEXT,"
            + POSITION_TO_STOCK_TRANSFER + " TEXT,"
            + POSITION_TO_CODE_STOCK_TRANSFER + " TEXT,"
            + POSITION_TO_DESCRIPTION_STOCK_TRANSFER + " TEXT,"
            + UNIQUE_CODE_STOCK_TRANSFER + " TEXT ,"
            + CREATE_TIME_STOCK_TRANSFER + " TEXT ,"
            + LPN_CD_STOCK_TRANSFER + " TEXT ,"
            + LPN_CODE_STOCK_TRANSFER + " TEXT ,"
            + LPN_FROM_STOCK_TRANSFER + " TEXT ,"
            + LPN_TO_STOCK_TRANSFER + " TEXT "
            + ")";


    public long CreateSTOCK_TRANSFER(Product_StockTransfer stockTransfer) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
//        values.put(AUTOINCREMENT_STOCK_TRANSFER, stockTransfer.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_STOCK_TRANSFER, stockTransfer.getUNIT());
        values.put(PRODUCT_CODE_STOCK_TRANSFER, stockTransfer.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_STOCK_TRANSFER, stockTransfer.getPRODUCT_NAME());
        values.put(PRODUCT_CD_STOCK_TRANSFER, stockTransfer.getPRODUCT_CD());
        values.put(BATCH_NUMBER_STOCK_TRANSFER, stockTransfer.getBATCH_NUMBER());
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
        values.put(CREATE_TIME_STOCK_TRANSFER, stockTransfer.getCREATE_TIME());
        values.put(LPN_CODE_STOCK_TRANSFER, stockTransfer.getLPN_CODE());
        values.put(LPN_FROM_STOCK_TRANSFER, stockTransfer.getLPN_FROM());
        values.put(LPN_TO_STOCK_TRANSFER, stockTransfer.getLPN_TO());
        // insert row
        long id = db.insert(O_STOCK_TRANSFER, null, values);
        return id;
    }

    public ArrayList<Product_StockTransfer>
    getoneProduct_StockTransfer(String CD, String expDate, String ea_unit, String stockinDate , String batch_number) {
        ArrayList<Product_StockTransfer> stockTransfers = new ArrayList<Product_StockTransfer>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_STOCK_TRANSFER + " " + " WHERE "
                + PRODUCT_CD_STOCK_TRANSFER + " = " + CD + " AND "
                + EA_UNIT_STOCK_TRANSFER + " like " + " '%" + ea_unit + "%'" + " AND "
                + BATCH_NUMBER_STOCK_TRANSFER + " like " + " '%" + batch_number + "%'" + " AND "
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
                stockTransfer.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_STOCK_TRANSFER))));
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
                stock.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_STOCK_TRANSFER))));
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
                stock.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME_STOCK_TRANSFER))));
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
        String selectQuery = "SELECT *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(STOCKIN_DATE,'---','') as STOCKIN_DATE  , REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_STOCK_TRANSFER;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_StockTransfer stock = new Product_StockTransfer();
//                stock.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_STOCK_TRANSFER))));
                stock.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_STOCK_TRANSFER))));
                stock.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_STOCK_TRANSFER))));
                stock.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_STOCK_TRANSFER))));
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
                stock.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME_STOCK_TRANSFER))));
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

    public int updateProduct_StockTransfer(Product_StockTransfer stockTransfer,String incre_st, String PRODUCT_CD, String sl, String ea_unit, String stock) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_STOCK_TRANSFER, PRODUCT_CD);
        values.put(PRODUCT_CODE_STOCK_TRANSFER, stockTransfer.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_STOCK_TRANSFER, stockTransfer.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_STOCK_TRANSFER, stockTransfer.getEXPIRED_DATE());
        values.put(EA_UNIT_STOCK_TRANSFER, stockTransfer.getUNIT());
        values.put(QTY_SET_AVAILABLE_STOCK_TRANSFER, sl);

        // updating row
        return db.update(O_STOCK_TRANSFER, values, AUTOINCREMENT_STOCK_TRANSFER + " = ?",
                new String[]{String.valueOf(incre_st)});

    }



    public void deleteProduct_StockTransfer() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_STOCK_TRANSFER);
    }

    //END TABLE STOCK_TRANSER

    //DATABASE PUT CANCEL_GOOD
    public static final String O_CANCEL_GOOD = "O_CANCEL_GOOD";
    public static final String WAREHOUSE_POSITION_CD_CANCEL_GOOD = "WAREHOUSE_POSITION_CD_CANCEL_GOOD";
    public static final String AUTOINCREMENT_CANCEL_GOOD = "AUTOINCREMENT_CANCEL_GOOD";
    public static final String PRODUCT_CODE_CANCEL_GOOD = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_CANCEL_GOOD = "PRODUCT_NAME";
    public static final String PRODUCT_CD_CANCEL_GOOD = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_CANCEL_GOOD = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_CANCEL_GOOD = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_CANCEL_GOOD = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_CANCEL_GOOD = "STOCKIN_DATE";
    public static final String EA_UNIT_CANCEL_GOOD = "EA_UNIT";
    public static final String POSITION_FROM_CANCEL_GOOD = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_CANCEL_GOOD = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_CANCEL_GOOD = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_CANCEL_GOOD = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_CANCEL_GOOD = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_CANCEL_GOOD = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_CANCEL_GOOD = "UNIQUE_CODE";
    public static final String CANCELGOOD_CD = "CANCELGOOD_CD";
    public static final String LPN_CD_CANCEL_GOOD = "LPN_CD_CANCEL_GOOD";
    public static final String LPN_CODE_CANCEL_GOOD = "LPN_CODE_CANCEL_GOOD";
    public static final String LPN_FROM_CANCEL_GOOD = "LPN_FROM_CANCEL_GOOD";
    public static final String LPN_TO_CANCEL_GOOD = "LPN_TO_CANCEL_GOOD";
    public static final String BATCH_NUMBER_CANCEL_GOOD = "BATCH_NUMBER_CANCEL_GOOD";

    public static final String CREATE_TABLE_O_CANCEL_GOOD = "CREATE TABLE "
            + O_CANCEL_GOOD + "("
            + AUTOINCREMENT_CANCEL_GOOD + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_CANCEL_GOOD + " TEXT,"
            + WAREHOUSE_POSITION_CD_CANCEL_GOOD + " TEXT,"
            + PRODUCT_NAME_CANCEL_GOOD + " TEXT,"
            + PRODUCT_CODE_CANCEL_GOOD + " TEXT,"
            + QTY_EA_AVAILABLE_CANCEL_GOOD + " TEXT,"
            + QTY_SET_AVAILABLE_CANCEL_GOOD + " TEXT,"
            + BATCH_NUMBER_CANCEL_GOOD + " TEXT,"
            + EXPIRED_DATE_CANCEL_GOOD + " TEXT,"
            + STOCKIN_DATE_CANCEL_GOOD + " TEXT,"
            + EA_UNIT_CANCEL_GOOD + " TEXT,"
            + POSITION_FROM_CANCEL_GOOD + " TEXT,"
            + POSITION_FROM_CODE_CANCEL_GOOD + " TEXT,"
            + POSITION_FROM_DESCRIPTION_CANCEL_GOOD + " TEXT,"
            + POSITION_TO_CANCEL_GOOD + " TEXT,"
            + POSITION_TO_CODE_CANCEL_GOOD + " TEXT,"
            + POSITION_TO_DESCRIPTION_CANCEL_GOOD + " TEXT,"
            + CANCELGOOD_CD + " TEXT,"
            + UNIQUE_CODE_CANCEL_GOOD + " TEXT ,"
            + LPN_CD_CANCEL_GOOD + " TEXT ,"
            + LPN_CODE_CANCEL_GOOD + " TEXT ,"
            + LPN_FROM_CANCEL_GOOD + " TEXT ,"
            + LPN_TO_CANCEL_GOOD + " TEXT "
            + ")";


    public long CreateCANCEL_GOOD(Product_CancelGood cancelGood) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
//        values.put(AUTOINCREMENT_CANCEL_GOOD, cancelGood.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_CANCEL_GOOD, cancelGood.getUNIT());
        values.put(BATCH_NUMBER_CANCEL_GOOD, cancelGood.getBATCH_NUMBER());
        values.put(PRODUCT_CODE_CANCEL_GOOD, cancelGood.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_CANCEL_GOOD, cancelGood.getPRODUCT_NAME());
        values.put(WAREHOUSE_POSITION_CD_CANCEL_GOOD, cancelGood.getWAREHOUSE_POSITION_CD());
        values.put(PRODUCT_CD_CANCEL_GOOD, cancelGood.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_CANCEL_GOOD, cancelGood.getQTY());
        values.put(STOCKIN_DATE_CANCEL_GOOD, cancelGood.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_CANCEL_GOOD, cancelGood.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_CANCEL_GOOD, cancelGood.getEXPIRED_DATE());
        values.put(EA_UNIT_CANCEL_GOOD, cancelGood.getUNIT());
        values.put(POSITION_FROM_CANCEL_GOOD, cancelGood.getPOSITION_FROM_CD());
        values.put(POSITION_TO_CANCEL_GOOD, cancelGood.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_CANCEL_GOOD, cancelGood.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_CANCEL_GOOD, cancelGood.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_CANCEL_GOOD, cancelGood.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_CANCEL_GOOD, cancelGood.getPOSITION_TO_DESCRIPTION());
        values.put(CANCELGOOD_CD, cancelGood.getCANCEL_CD());
        values.put(LPN_CODE_CANCEL_GOOD, cancelGood.getLPN_CODE());
        values.put(LPN_FROM_CANCEL_GOOD, cancelGood.getLPN_FROM());
        values.put(LPN_TO_CANCEL_GOOD, cancelGood.getLPN_TO());
        // insert row
        long id = db.insert(O_CANCEL_GOOD, null, values);
        return id;
    }

    public int updatePositionFrom_cancelGood_LPN(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_CANCEL_GOOD, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_CANCEL_GOOD, descreption);

        values.put(POSITION_FROM_CODE_CANCEL_GOOD, from);
        values.put(LPN_FROM_CANCEL_GOOD, from);


        // updating row
        return db.update(O_CANCEL_GOOD, values, AUTOINCREMENT_CANCEL_GOOD + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});

    }

    public int updatePositionFrom_cancelGood(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_CANCEL_GOOD, wareHouse);
        values.put(POSITION_FROM_CODE_CANCEL_GOOD, from);
        values.put(LPN_FROM_CANCEL_GOOD, "");
        values.put(POSITION_FROM_DESCRIPTION_CANCEL_GOOD, descreption);


        // updating row
        return db.update(O_CANCEL_GOOD, values, AUTOINCREMENT_CANCEL_GOOD + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});

    }

    public int updatePositionTo_cancelGood_LPN(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_CANCEL_GOOD, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_CANCEL_GOOD, descreption);
        values.put(LPN_TO_CANCEL_GOOD, to);

        values.put(POSITION_TO_CODE_CANCEL_GOOD, to);
        // updating row
        return db.update(O_CANCEL_GOOD, values,
                AUTOINCREMENT_CANCEL_GOOD + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});


    }


    public int updatePositionTo_cancelGood(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_CANCEL_GOOD, wareHouse);
        values.put(POSITION_TO_CODE_CANCEL_GOOD, to);
        values.put(LPN_TO_CANCEL_GOOD, "");

        values.put(POSITION_TO_DESCRIPTION_CANCEL_GOOD, descreption);
        // updating row
        return db.update(O_CANCEL_GOOD, values,
                AUTOINCREMENT_CANCEL_GOOD + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});


    }


    public ArrayList<Product_CancelGood>
    getoneProduct_CancelGood(String CD, String expDate, String ea_unit, String stockinDate, String cancel ,String batch_number) {
        ArrayList<Product_CancelGood> cancelGoods = new ArrayList<Product_CancelGood>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_CANCEL_GOOD + " " + " WHERE "
                + PRODUCT_CD_CANCEL_GOOD + " = " + CD + " AND "
                + CANCELGOOD_CD + " = " + cancel + " AND "
                + BATCH_NUMBER_CANCEL_GOOD + " like " + " '%" + batch_number + "%'" + " AND "
                + EA_UNIT_CANCEL_GOOD + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_CANCEL_GOOD + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_CANCEL_GOOD + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_CancelGood cancelGood = new Product_CancelGood();
                cancelGood.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_CANCEL_GOOD))));
                cancelGood.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_CANCEL_GOOD))));
                cancelGood.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_CANCEL_GOOD))));
                cancelGood.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_CANCEL_GOOD))));
                cancelGood.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_CANCEL_GOOD))));
                cancelGood.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_CANCEL_GOOD))));
                cancelGood.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_CANCEL_GOOD))));
                cancelGood.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_CANCEL_GOOD))));
                cancelGood.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_CANCEL_GOOD))));
                cancelGood.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_CANCEL_GOOD))));
                cancelGood.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_CANCEL_GOOD))));
                cancelGoods.add(cancelGood);
            } while (c.moveToNext());
        }

        c.close();
        return cancelGoods;
    }


    public ArrayList<Product_CancelGood>
    getAllProduct_CancelGood_Sync(String cancel) {
        ArrayList<Product_CancelGood> cancelGoods = new ArrayList<Product_CancelGood>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , " +
                "REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, " +
                "REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_CANCEL_GOOD +
                " where " + CANCELGOOD_CD + " = " + cancel;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_CancelGood cancelGood = new Product_CancelGood();
//                cancelGood.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_CANCEL_GOOD))));
                cancelGood.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_CANCEL_GOOD))));
                cancelGood.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_CANCEL_GOOD))));
                cancelGood.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_CANCEL_GOOD))));
                cancelGood.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_CANCEL_GOOD))));
                cancelGood.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_CANCEL_GOOD))));
                cancelGood.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_CANCEL_GOOD))));
                cancelGood.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_CANCEL_GOOD))));
                cancelGood.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_CANCEL_GOOD))));
                cancelGood.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_CANCEL_GOOD))));
                cancelGood.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_CANCEL_GOOD))));
                cancelGood.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_CANCEL_GOOD))));
                cancelGood.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_CANCEL_GOOD))));
                cancelGood.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_CANCEL_GOOD))));
                cancelGood.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_CANCEL_GOOD))));
                cancelGood.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_CANCEL_GOOD))));
                cancelGood.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_CANCEL_GOOD))));
                cancelGood.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_CANCEL_GOOD))));
                cancelGood.setCANCEL_CD((c.getString(c
                        .getColumnIndex(CANCELGOOD_CD))));
                cancelGood.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_CANCEL_GOOD))));
                cancelGood.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_CANCEL_GOOD))));
                cancelGood.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_CANCEL_GOOD))));
                cancelGoods.add(cancelGood);
            } while (c.moveToNext());
        }

        c.close();
        return cancelGoods;
    }

    public ArrayList<Product_CancelGood>
    getAllProduct_CancelGood(String cancel) {
        ArrayList<Product_CancelGood> cancelGoods = new ArrayList<Product_CancelGood>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_CANCEL_GOOD + " where " + CANCELGOOD_CD + " = " + cancel;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_CancelGood cancelGood = new Product_CancelGood();
                cancelGood.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_CANCEL_GOOD))));
                cancelGood.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_CANCEL_GOOD))));
                cancelGood.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_CANCEL_GOOD))));
                cancelGood.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_CANCEL_GOOD))));
                cancelGood.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_CANCEL_GOOD))));
                cancelGood.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_CANCEL_GOOD))));
                cancelGood.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_CANCEL_GOOD))));
                cancelGood.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_CANCEL_GOOD))));
                cancelGood.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_CANCEL_GOOD))));
                cancelGood.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_CANCEL_GOOD))));
                cancelGood.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_CANCEL_GOOD))));
                cancelGood.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_CANCEL_GOOD))));
                cancelGood.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_CANCEL_GOOD))));
                cancelGood.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_CANCEL_GOOD))));
                cancelGood.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_CANCEL_GOOD))));
                cancelGood.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_CANCEL_GOOD))));
                cancelGood.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_CANCEL_GOOD))));
                cancelGood.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_CANCEL_GOOD))));
                cancelGood.setCANCEL_CD((c.getString(c
                        .getColumnIndex(CANCELGOOD_CD))));
                cancelGood.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_CANCEL_GOOD))));
                cancelGood.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_CANCEL_GOOD))));
                cancelGood.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_CANCEL_GOOD))));
                cancelGoods.add(cancelGood);
            } while (c.moveToNext());
        }

        c.close();
        return cancelGoods;
    }


    public int updateProduct_CancelGood(Product_CancelGood cancelGood, String incre_so, String PRODUCT_CD, String sl, String ea_unit, String stock, String cancel) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_CANCEL_GOOD, PRODUCT_CD);
        values.put(PRODUCT_CODE_CANCEL_GOOD, cancelGood.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_CANCEL_GOOD, cancelGood.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_CANCEL_GOOD, cancelGood.getEXPIRED_DATE());
        values.put(EA_UNIT_CANCEL_GOOD, cancelGood.getUNIT());
        values.put(QTY_SET_AVAILABLE_CANCEL_GOOD, sl);
        values.put(CANCELGOOD_CD, cancel);

        // updating row
        return db.update(O_CANCEL_GOOD, values,  AUTOINCREMENT_CANCEL_GOOD + " = ?",
                new String[]{String.valueOf(incre_so)});

    }


    public void deleteProduct_CancelGood() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_CANCEL_GOOD);
    }

    //END TABLE O_CANCEL_GOOD

    //DATABASE PUT PO_Return
    public static final String O_PO_RETURN = "O_PO_RETURN";
    public static final String WAREHOUSE_POSITION_CD_PO_RETURN = "WAREHOUSE_POSITION_CD_PO_RETURN";
    public static final String AUTOINCREMENT_PO_RETURN = "AUTOINCREMENT_PO_RETURN";
    public static final String PRODUCT_CODE_PO_RETURN = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_PO_RETURN = "PRODUCT_NAME";
    public static final String PRODUCT_CD_PO_RETURN = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_PO_RETURN = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_PO_RETURN = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_PO_RETURN = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_PO_RETURN = "STOCKIN_DATE";
    public static final String EA_UNIT_PO_RETURN = "EA_UNIT";
    public static final String POSITION_FROM_PO_RETURN = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_PO_RETURN = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_PO_RETURN = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_PO_RETURN = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_PO_RETURN = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_PO_RETURN = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_PO_RETURN = "UNIQUE_CODE";
    public static final String PO_RETURN_CD = "PO_RETURN_CD";
    public static final String LPN_CD_PO_RETURN = "LPN_CD_PO_RETURN";
    public static final String LPN_CODE_PO_RETURN = "LPN_CODE_PO_RETURN";
    public static final String LPN_FROM_PO_RETURN = "LPN_FROM_PO_RETURN";
    public static final String LPN_TO_PO_RETURN = "LPN_TO_PO_RETURN";
    public static final String BATCH_NUMBER_PO_RETURN = "BATCH_NUMBER_PO_RETURN";
    public static final String MANUFACTURING_DATE_PO_RETURN = "MANUFACTURING_DATE_PO_RETURN";

    public static final String CREATE_TABLE_O_PO_RETURN = "CREATE TABLE "
            + O_PO_RETURN + "("
            + AUTOINCREMENT_PO_RETURN + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_PO_RETURN + " TEXT,"
            + WAREHOUSE_POSITION_CD_PO_RETURN + " TEXT,"
            + BATCH_NUMBER_PO_RETURN + " TEXT,"
            + MANUFACTURING_DATE_PO_RETURN + " TEXT,"
            + PRODUCT_NAME_PO_RETURN + " TEXT,"
            + PRODUCT_CODE_PO_RETURN + " TEXT,"
            + QTY_EA_AVAILABLE_PO_RETURN + " TEXT,"
            + QTY_SET_AVAILABLE_PO_RETURN + " TEXT,"
            + EXPIRED_DATE_PO_RETURN + " TEXT,"
            + STOCKIN_DATE_PO_RETURN + " TEXT,"
            + EA_UNIT_PO_RETURN + " TEXT,"
            + POSITION_FROM_PO_RETURN + " TEXT,"
            + POSITION_FROM_CODE_PO_RETURN + " TEXT,"
            + POSITION_FROM_DESCRIPTION_PO_RETURN + " TEXT,"
            + POSITION_TO_PO_RETURN + " TEXT,"
            + POSITION_TO_CODE_PO_RETURN + " TEXT,"
            + POSITION_TO_DESCRIPTION_PO_RETURN + " TEXT,"
            + PO_RETURN_CD + " TEXT,"
            + UNIQUE_CODE_PO_RETURN + " TEXT ,"
            + LPN_CD_PO_RETURN + " TEXT ,"
            + LPN_CODE_PO_RETURN + " TEXT ,"
            + LPN_FROM_PO_RETURN + " TEXT ,"
            + LPN_TO_PO_RETURN + " TEXT "
            + ")";


    public long CreatePo_Return(Product_PoReturn poReturn) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
//        values.put(AUTOINCREMENT_PO_RETURN, poReturn.getAUTOINCREMENT());

        values.put(UNIQUE_CODE_PO_RETURN, poReturn.getUNIT());
        values.put(BATCH_NUMBER_PO_RETURN, poReturn.getBATCH_NUMBER());
        values.put(MANUFACTURING_DATE_PO_RETURN, poReturn.getMANUFACTURING_DATE());
        values.put(PRODUCT_CODE_PO_RETURN, poReturn.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_PO_RETURN, poReturn.getPRODUCT_NAME());
        values.put(WAREHOUSE_POSITION_CD_PO_RETURN, poReturn.getWAREHOUSE_POSITION_CD());
        values.put(PRODUCT_CD_PO_RETURN, poReturn.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_PO_RETURN, poReturn.getQTY());
        values.put(STOCKIN_DATE_PO_RETURN, poReturn.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_PO_RETURN, poReturn.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_PO_RETURN, poReturn.getEXPIRED_DATE());
        values.put(EA_UNIT_PO_RETURN, poReturn.getUNIT());
        values.put(POSITION_FROM_PO_RETURN, poReturn.getPOSITION_FROM_CD());
        values.put(POSITION_TO_PO_RETURN, poReturn.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_PO_RETURN, poReturn.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_PO_RETURN, poReturn.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_PO_RETURN, poReturn.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_PO_RETURN, poReturn.getPOSITION_TO_DESCRIPTION());
        values.put(PO_RETURN_CD, poReturn.getPO_RETURN_CD());
        values.put(LPN_CODE_PO_RETURN, poReturn.getLPN_CODE());
        values.put(LPN_FROM_PO_RETURN, poReturn.getLPN_FROM());
        values.put(LPN_TO_PO_RETURN, poReturn.getLPN_TO());
        // insert row
        long id = db.insert(O_PO_RETURN, null, values);
        return id;
    }

    public int updatePositionFrom_poReturn_LPN(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_PO_RETURN, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_PO_RETURN, descreption);

        values.put(POSITION_FROM_CODE_PO_RETURN, from);
        values.put(LPN_FROM_PO_RETURN, from);


        // updating row
        return db.update(O_PO_RETURN, values, AUTOINCREMENT_PO_RETURN + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});

    }

    public int updatePositionFrom_poReturn(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_PO_RETURN, wareHouse);
        values.put(POSITION_FROM_CODE_PO_RETURN, from);
        values.put(LPN_FROM_PO_RETURN, "");
        values.put(POSITION_FROM_DESCRIPTION_PO_RETURN, descreption);


        // updating row
        return db.update(O_PO_RETURN, values, AUTOINCREMENT_PO_RETURN + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});

    }

    public int updatePositionTo_poReturn_LPN(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_PO_RETURN, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_PO_RETURN, descreption);
        values.put(LPN_TO_PO_RETURN, to);

        values.put(POSITION_TO_CODE_PO_RETURN, to);
        // updating row
        return db.update(O_PO_RETURN, values,
                AUTOINCREMENT_PO_RETURN + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});


    }


    public int updatePositionTo_poReturn(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_PO_RETURN, wareHouse);
        values.put(POSITION_TO_CODE_PO_RETURN, to);
        values.put(LPN_TO_PO_RETURN, "");

        values.put(POSITION_TO_DESCRIPTION_PO_RETURN, descreption);
        // updating row
        return db.update(O_PO_RETURN, values,
                AUTOINCREMENT_PO_RETURN + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});


    }


    public ArrayList<Product_PoReturn>
    getoneProduct_PoReturn(String CD, String expDate, String ea_unit, String stockinDate, String po_return,String batch_number) {
        ArrayList<Product_PoReturn> poReturns = new ArrayList<Product_PoReturn>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_PO_RETURN + " " + " WHERE "
                + PRODUCT_CD_PO_RETURN + " = " + CD + " AND "
                + PO_RETURN_CD + " = " + po_return + " AND "
                + BATCH_NUMBER_PO_RETURN + " like " + " '%" + batch_number + "%'" + " AND "
                + EA_UNIT_PO_RETURN + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_PO_RETURN + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_PO_RETURN + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_PoReturn poReturn = new Product_PoReturn();
                poReturn.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PO_RETURN))));
                poReturn.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PO_RETURN))));
                poReturn.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_PO_RETURN))));
                poReturn.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_PO_RETURN))));
                poReturn.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PO_RETURN))));
                poReturn.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PO_RETURN))));
                poReturn.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PO_RETURN))));
                poReturn.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PO_RETURN))));
                poReturn.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_PO_RETURN))));
                poReturn.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_PO_RETURN))));
                poReturn.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_PO_RETURN))));
                poReturn.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_PO_RETURN))));
                poReturns.add(poReturn);
            } while (c.moveToNext());
        }

        c.close();
        return poReturns;
    }


    public ArrayList<Product_PoReturn>
    getAllProduct_PoReturn_Sync(String po_return) {
        ArrayList<Product_PoReturn> poReturns = new ArrayList<Product_PoReturn>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , " +
                "REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, " +
                "REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_PO_RETURN +
                " where " + PO_RETURN_CD + " = " + po_return;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_PoReturn poReturn = new Product_PoReturn();
//                poReturn.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_PO_RETURN))));
                poReturn.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_PO_RETURN))));
                poReturn.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PO_RETURN))));
                poReturn.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_PO_RETURN))));
                poReturn.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_PO_RETURN))));
                poReturn.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PO_RETURN))));
                poReturn.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PO_RETURN))));
                poReturn.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PO_RETURN))));
                poReturn.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_PO_RETURN))));
                poReturn.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_PO_RETURN))));
                poReturn.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_PO_RETURN))));
                poReturn.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PO_RETURN))));
                poReturn.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_PO_RETURN))));
                poReturn.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_PO_RETURN))));
                poReturn.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_PO_RETURN))));
                poReturn.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_PO_RETURN))));
                poReturn.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_PO_RETURN))));
                poReturn.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_PO_RETURN))));
                poReturn.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_PO_RETURN))));
                poReturn.setPO_RETURN_CD((c.getString(c
                        .getColumnIndex(PO_RETURN_CD))));
                poReturn.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_PO_RETURN))));
                poReturn.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_PO_RETURN))));
                poReturn.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_PO_RETURN))));
                poReturns.add(poReturn);
            } while (c.moveToNext());
        }

        c.close();
        return poReturns;
    }

    public ArrayList<Product_PoReturn>
    getAllProduct_PoReturn(String po_return) {
        ArrayList<Product_PoReturn> poReturns = new ArrayList<Product_PoReturn>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_PO_RETURN + " where " + PO_RETURN_CD + " = " + po_return;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_PoReturn poReturn = new Product_PoReturn();
                poReturn.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PO_RETURN))));
                poReturn.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PO_RETURN))));
                poReturn.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_PO_RETURN))));
                poReturn.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_PO_RETURN))));
                poReturn.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_PO_RETURN))));
                poReturn.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PO_RETURN))));
                poReturn.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PO_RETURN))));
                poReturn.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PO_RETURN))));
                poReturn.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_PO_RETURN))));
                poReturn.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_PO_RETURN))));
                poReturn.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_PO_RETURN))));
                poReturn.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PO_RETURN))));
                poReturn.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_PO_RETURN))));
                poReturn.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_PO_RETURN))));
                poReturn.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_PO_RETURN))));
                poReturn.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_PO_RETURN))));
                poReturn.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_PO_RETURN))));
                poReturn.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_PO_RETURN))));
                poReturn.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_PO_RETURN))));
                poReturn.setPO_RETURN_CD((c.getString(c
                        .getColumnIndex(PO_RETURN_CD))));
                poReturn.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_PO_RETURN))));
                poReturn.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_PO_RETURN))));
                poReturn.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_PO_RETURN))));
                poReturns.add(poReturn);
            } while (c.moveToNext());
        }

        c.close();
        return poReturns;
    }


    public int updateProduct_PoReturn(Product_PoReturn poReturn, String incre_so, String PRODUCT_CD, String sl, String ea_unit, String stock, String po_return) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_PO_RETURN, PRODUCT_CD);
        values.put(PRODUCT_CODE_PO_RETURN, poReturn.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_PO_RETURN, poReturn.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_PO_RETURN, poReturn.getEXPIRED_DATE());
        values.put(EA_UNIT_PO_RETURN, poReturn.getUNIT());
        values.put(QTY_SET_AVAILABLE_PO_RETURN, sl);
        values.put(PO_RETURN_CD, po_return);

        // updating row
        return db.update(O_PO_RETURN, values,  AUTOINCREMENT_PO_RETURN + " = ?",
                new String[]{String.valueOf(incre_so)});

    }


    public void deleteProduct_PoReturn() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_PO_RETURN);
    }

    //END TABLE O_PO_RETURN



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
    public static final String BATCH_NUMBER_STOCK_OUT = "BATCH_NUMBER";
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
            + BATCH_NUMBER_STOCK_OUT + " TEXT,"
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
//        values.put(AUTOINCREMENT_STOCK_OUT, stockOut.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_STOCK_OUT, stockOut.getUNIT());
        values.put(PRODUCT_CODE_STOCK_OUT, stockOut.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_STOCK_OUT, stockOut.getPRODUCT_NAME());
        values.put(BATCH_NUMBER_STOCK_OUT, stockOut.getBATCH_NUMBER());
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

    public int updatePositionFrom_StockOut_LPN(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_STOCK_OUT, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_STOCK_OUT, descreption);

        values.put(POSITION_FROM_CODE_STOCK_OUT, from);
        values.put(LPN_FROM_STOCK_OUT, from);


        // updating row
        return db.update(O_STOCK_OUT, values, AUTOINCREMENT_STOCK_OUT + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});

    }

    public int updatePositionFrom_StockOut(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_STOCK_OUT, wareHouse);
        values.put(POSITION_FROM_CODE_STOCK_OUT, from);
        values.put(LPN_FROM_STOCK_OUT, "");
        values.put(POSITION_FROM_DESCRIPTION_STOCK_OUT, descreption);


        // updating row
        return db.update(O_STOCK_OUT, values, AUTOINCREMENT_STOCK_OUT + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});

    }

    public int updatePositionTo_StockOut_LPN(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_STOCK_OUT, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_STOCK_OUT, descreption);
        values.put(LPN_TO_STOCK_OUT, to);

        values.put(POSITION_TO_CODE_STOCK_OUT, to);
        // updating row
        return db.update(O_STOCK_OUT, values,
                AUTOINCREMENT_STOCK_OUT + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});


    }


    public int updatePositionTo_StockOut(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_STOCK_OUT, wareHouse);
        values.put(POSITION_TO_CODE_STOCK_OUT, to);
        values.put(LPN_TO_STOCK_OUT, "");

        values.put(POSITION_TO_DESCRIPTION_STOCK_OUT, descreption);
        // updating row
        return db.update(O_STOCK_OUT, values,
                AUTOINCREMENT_STOCK_OUT + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});


    }


    public ArrayList<Product_StockOut>
    getoneProduct_stockout(String CD, String expDate, String ea_unit, String stockinDate, String stockout_cd ,String batch_number) {
        ArrayList<Product_StockOut> stockOuts = new ArrayList<Product_StockOut>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_STOCK_OUT + " " + " WHERE "
                + PRODUCT_CD_STOCK_OUT + " = " + CD + " AND "
                + STOCKOUT_CD + " = " + stockout_cd + " AND "
                + BATCH_NUMBER_STOCK_OUT + " like " + " '%" + batch_number + "%'" + " AND "
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
                stockOut.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_STOCK_OUT))));
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
        String selectQuery = "SELECT  *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(STOCKIN_DATE,'---','') as STOCKIN_DATE  , " +
                "REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, " +
                "REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_STOCK_OUT +
                " where " + STOCKOUT_CD + " = " + stockout_cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_StockOut stockOut = new Product_StockOut();
//                stockOut.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_STOCK_OUT))));
                stockOut.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_STOCK_OUT))));
                stockOut.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_STOCK_OUT))));
                stockOut.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_STOCK_OUT))));
                stockOut.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_STOCK_OUT))));
                stockOut.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_STOCK_OUT))));
                stockOut.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_STOCK_OUT))));
                stockOut.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_STOCK_OUT))));
                stockOut.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_STOCK_OUT))));
                stockOut.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_STOCK_OUT))));
                stockOut.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_STOCK_OUT))));
                stockOut.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_STOCK_OUT))));
                stockOut.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_STOCK_OUT))));
                stockOut.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_STOCK_OUT))));
                stockOut.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_STOCK_OUT))));
                stockOut.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_STOCK_OUT))));
                stockOut.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_STOCK_OUT))));
                stockOut.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_STOCK_OUT))));
                stockOut.setSTOCKOUT_CD((c.getString(c
                        .getColumnIndex(STOCKOUT_CD))));
                stockOut.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_STOCK_OUT))));
                stockOut.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_STOCK_OUT))));
                stockOut.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_STOCK_OUT))));
                stockOuts.add(stockOut);
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
                stock.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_STOCK_OUT))));
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


    public int updateProduct_Stockout(Product_StockOut stockOut,String incre_so, String PRODUCT_CD, String sl, String ea_unit, String stock, String stockout_cd) {
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
        return db.update(O_STOCK_OUT, values,  AUTOINCREMENT_STOCK_OUT + " = ?",
                new String[]{String.valueOf(incre_so)});

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
    public static final String CREATE_TIME_LETDOWN = "CREATE_TIME_LETDOWN";
    public static final String SUGGESTION_POSITION_LETDOWN = "SUGGESTION_POSITION";
    public static final String BATCH_NUMBER_LETDOWN = "BATCH_NUMBER_LETDOWN";

    public static final String CREATE_TABLE_O_LET_DOWN = "CREATE TABLE "
            + O_LET_DOWN + "("
            + AUTOINCREMENT_LETDOWN + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_LETDOWN + " TEXT,"
            + PRODUCT_NAME_LETDOWN + " TEXT,"
            + PRODUCT_CODE_LETDOWN + " TEXT,"
            + QTY_EA_AVAILABLE_LETDOWN + " TEXT,"
            + QTY_SET_AVAILABLE_LETDOWN + " TEXT,"
            + BATCH_NUMBER_LETDOWN + " TEXT,"
            + CREATE_TIME_LETDOWN + " TEXT,"
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
//        values.put(AUTOINCREMENT_LETDOWN, qrcode.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_LETDOWN, qrcode.getUNIT());
        values.put(PRODUCT_CODE_LETDOWN, qrcode.getPRODUCT_CODE());
        values.put(CREATE_TIME_LETDOWN, qrcode.getCREATE_TIME());
        values.put(BATCH_NUMBER_LETDOWN, qrcode.getBATCH_NUMBER());
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
    getoneProductLetDown(String CD, String expDate, String ea_unit, String stockinDate,String batch_number) {
        ArrayList<ProductLetDown> qrcode = new ArrayList<ProductLetDown>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_LET_DOWN + " " + " WHERE "
                + PRODUCT_CD_LETDOWN + " = " + CD + " AND "
                + BATCH_NUMBER_LETDOWN + " like " + " '%" + batch_number + "%'" + " AND "
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
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_LETDOWN))));
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
                qrcode.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_LETDOWN))));
                qrcode.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME_LETDOWN))));
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
//                qrcode.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_LETDOWN))));
                qrcode.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_LETDOWN))));
                qrcode.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_LETDOWN))));
                qrcode.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME_LETDOWN))));
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

    public int updateProduct_LetDown(ProductLetDown letdown,String incre_ld, String PRODUCT_CD, String sl, String ea_unit, String stock) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_LETDOWN, PRODUCT_CD);
        values.put(PRODUCT_CODE_LETDOWN, letdown.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_LETDOWN, letdown.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_LETDOWN, letdown.getEXPIRED_DATE());
        values.put(EA_UNIT_LETDOWN, letdown.getUNIT());
        values.put(QTY_SET_AVAILABLE_LETDOWN, sl);

        // updating row
        return db.update(O_LET_DOWN, values,  AUTOINCREMENT_LETDOWN + " = ?",
                new String[]{String.valueOf(incre_ld)});

    }


    public void deleteProduct_Letdown_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_LET_DOWN, AUTOINCREMENT_LETDOWN + " = ?"
                , new String[]{String.valueOf(productCode)});

    }

    public void deleteProduct_Return_QA_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_RETURN_QA, AUTOINCREMENT_RETURN_QA + " = ?"
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
    public void deleteProduct_PO_Return_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_PO_RETURN, AUTOINCREMENT_PO_RETURN + " = ?"
                , new String[]{String.valueOf(productCode)
                });

    }
    public void deleteProduct_Transfer_Posting_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_TRANSFER_POSTING, AUTOINCREMENT_TRANSFER_POSTING + " = ?"
                , new String[]{String.valueOf(productCode)
                });

    }

    public void deleteProduct_QA_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_QA, AUTOINCREMENT_QA+ " = ?"
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
    public void deleteProduct_Pickup_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_PICKUP, AUTOINCREMENT_PICKUP+ " = ?"
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

    public void deleteProduct_Cancel_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_CANCEL_GOOD, AUTOINCREMENT_CANCEL_GOOD + " = ?" ,
                new String[]{String.valueOf(productCode)});

    }

    public void deleteProduct_Warehouse_Adjustment_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_WAREHOUSE_ADJUSTMENT, AUTOINCREMENT_WAREHOUSE_ADJUSTMENT + " = ?" ,
                new String[]{String.valueOf(productCode) });

    }



    public void deleteProduct_Letdown() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_LET_DOWN);
    }

    public int updatePositionFromLetDown_LPN(String id_unique_LD , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_LETDOWN, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_LETDOWN, descreption);
        values.put(POSITION_FROM_CODE_LETDOWN, from);
        values.put(LPN_FROM_LETDOWN, from);

        // updating row
        return db.update(O_LET_DOWN, values,
                AUTOINCREMENT_LETDOWN + " = ?",
                new String[]{String.valueOf(id_unique_LD)});

    }

    public int updatePositionFromLetDown(String id_unique_LD , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_LETDOWN, wareHouse);
        values.put(POSITION_FROM_CODE_LETDOWN, from);
        values.put(LPN_FROM_LETDOWN, "");
        values.put(POSITION_FROM_DESCRIPTION_LETDOWN, descreption);

        // updating row
        return db.update(O_LET_DOWN, values,  AUTOINCREMENT_LETDOWN + " = ?",
                new String[]{String.valueOf(id_unique_LD)});

    }

    public int updatePositionToLetDown_LPN(String id_unique_LD , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_LETDOWN, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_LETDOWN, descreption);
        values.put(POSITION_TO_CODE_LETDOWN, to);
        values.put(LPN_TO_LETDOWN, to);

        values.put(POSITION_TO_CODE_LETDOWN, to);
        // updating row
        return db.update(O_LET_DOWN, values,  AUTOINCREMENT_LETDOWN + " = ?",
                new String[]{String.valueOf(id_unique_LD)});


    }

    public int updatePositionToLetDown(String id_unique_LD , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_LETDOWN, wareHouse);
        values.put(POSITION_TO_CODE_LETDOWN, to);
        values.put(LPN_TO_LETDOWN, "");
        values.put(POSITION_TO_DESCRIPTION_LETDOWN, descreption);
        // updating row
        return db.update(O_LET_DOWN, values, AUTOINCREMENT_LETDOWN + " = ?",
                new String[]{String.valueOf(id_unique_LD)});


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
    public static final String BATCH_NUMBER_PUTAWAY = "BATCH_NUMBER_PUTAWAY";
    public static final String CREATE_TIME_PUTAWAY = "CREATE_TIME_PUTAWAY";


    public static final String CREATE_TABLE_O_PUT_AWAY = "CREATE TABLE "
            + O_PUT_AWAY + "("
            + AUTOINCREMENT_PUT_AWAY + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + BATCH_NUMBER_PUTAWAY + " TEXT,"
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
            + CREATE_TIME_PUTAWAY + " TEXT ,"
            + LPN_CODE_PUTAWAY + " TEXT ,"
            + LPN_FROM_PUTAWAY + " TEXT ,"
            + LPN_TO_PUTAWAY + " TEXT "
            + ")";


    public long CreatePutAway(Product_PutAway qrcode) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
//        values.put(AUTOINCREMENT_PUT_AWAY,qrcode.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_PUTAWAY, qrcode.getUNIQUE_CODE_PUTAWAY());
        values.put(BATCH_NUMBER_PUTAWAY, qrcode.getBATCH_NUMBER());
        values.put(CREATE_TIME_PUTAWAY, qrcode.getCREATE_TIME());
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
    getposition_PutAway() {
        ArrayList<Product_PutAway> qrcode = new ArrayList<Product_PutAway>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_PUT_AWAY + " ORDER BY " + AUTOINCREMENT_PUT_AWAY + " DESC LIMIT 1 ";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_PutAway qrcode_putaway = new Product_PutAway();

                qrcode_putaway.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PUT_AWAY))));

                qrcode_putaway.setPOSITION_FROM_PUTAWAY((c.getString(c
                        .getColumnIndex(POSITION_FROM_PUTAWAY))));

                qrcode_putaway.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE))));

                qrcode_putaway.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION))));


                qrcode.add(qrcode_putaway);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }

    public ArrayList<Product_PutAway>
    getoneProduct_PutAway(String CD, String expDate, String ea_unit, String stockinDate,String batch_number) {
        ArrayList<Product_PutAway> qrcode = new ArrayList<Product_PutAway>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_PUT_AWAY + " " + " WHERE "
                + PRODUCT_CD_PUTAWAY + " = " + CD + " AND "
                + BATCH_NUMBER_PUTAWAY + " like " + " '%" + batch_number + "%'" + " AND "
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
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PUTAWAY))));
                qrcodeq.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME_PUTAWAY))));
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
    getautoProduct_PutAway() {
        ArrayList<Product_PutAway> putaway = new ArrayList<Product_PutAway>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_PUT_AWAY + " ORDER BY " + AUTOINCREMENT_PUT_AWAY + " DESC LIMIT 1 ";;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_PutAway qrcode_putaway = new Product_PutAway();
                qrcode_putaway.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PUT_AWAY))));
                putaway.add(qrcode_putaway);
            } while (c.moveToNext());
        }

        c.close();
        return putaway;
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
                qrcode_putaway.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME_PUTAWAY))));
                qrcode_putaway.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PUTAWAY))));
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
//                qrcode_putaway.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_PUT_AWAY))));
                qrcode_putaway.setUNIQUE_CODE_PUTAWAY((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_PUTAWAY))));
                qrcode_putaway.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PUTAWAY))));
                qrcode_putaway.setPRODUCT_CODE_PUTAWAY((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PUTAWAY))));
                qrcode_putaway.setPRODUCT_NAME_PUTAWAY((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PUTAWAY))));
                qrcode_putaway.setPRODUCT_CD_PUTAWAY((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PUTAWAY))));
                qrcode_putaway.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME_PUTAWAY))));
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

    public int updateProduct_PutAway(Product_PutAway putAway,String incre_pa, String PRODUCT_CD, String sl, String ea_unit, String stock) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_PUTAWAY, PRODUCT_CD);
        values.put(PRODUCT_CODE_PUTAWAY, putAway.getPRODUCT_CODE_PUTAWAY());
        values.put(PRODUCT_NAME_PUTAWAY, putAway.getPRODUCT_NAME_PUTAWAY());
        values.put(EXPIRED_DATE_PUTAWAY, putAway.getEXPIRED_DATE_PUTAWAY());
        values.put(EA_UNIT_PUTAWAY, putAway.getEA_UNIT_PUTAWAY());
        values.put(QTY_SET_AVAILABLE, sl);


        // updating row
        return db.update(O_PUT_AWAY, values,  AUTOINCREMENT_PUT_AWAY + " = ?",
                new String[]{String.valueOf(incre_pa)});

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

    //Table O_Batch để chứa dữ liệu số batch
    public static final String O_BATCH = "O_BATCH";
    public static final String AUTOINCREMENT_BATCH = "AUTOINCREMENT_BATCH";
    public static final String BATCH_NUMBER = "BATCH_NUMBER";
    public static final String PRODUCT_CODE_BATCH = "PRODUCT_CODE_BATCH";
    public static final String PRODUCT_NAME_BATCH = "PRODUCT_NAME_BATCH";
    public static final String PRODUCT_CD_BATCH = "PRODUCT_CD_BATCH";
    public static final String EXPIRED_DATE_BATCH = "EXPIRED_DATE_BATCH";
    public static final String UNIT_BATCH = "UNIT_BATCH";
    public static final String MANUFACTURING_DATE_BATCH = "MANUFACTURING_DATE_BATCH";
    public static final String STOCKIN_DATE_BATCH = "STOCKIN_DATE_BATCH";
    public static final String POSITION_CODE_BATCH = "POSITION_CODE_BATCH";
    public static final String POSITION_DESCRIPTION_BATCH = "POSITION_DESCRIPTION_BATCH";
    public static final String WAREHOUSE_POSITION_CD_BATCH = "WAREHOUSE_POSITION_CD_BATCH";

    public static final String CREATE_TABLE_O_BATCH = "CREATE TABLE "
            + O_BATCH + "("
            + AUTOINCREMENT_BATCH + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CODE_BATCH + " TEXT,"
            + PRODUCT_NAME_BATCH + " TEXT,"
            + PRODUCT_CD_BATCH + " TEXT,"
            + EXPIRED_DATE_BATCH + " TEXT,"
            + UNIT_BATCH + " TEXT,"
            + MANUFACTURING_DATE_BATCH + " TEXT,"
            + STOCKIN_DATE_BATCH + " TEXT,"
            + POSITION_CODE_BATCH + " TEXT,"
            + POSITION_DESCRIPTION_BATCH + " TEXT,"
            + WAREHOUSE_POSITION_CD_BATCH + " TEXT,"
            + BATCH_NUMBER + " TEXT" + ")";

    public long CreateBatch_Number(Batch_number_Tam batch) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        //values.put(QRCODE, qrcode.getQRCODE());
//        values.put(AUTOINCREMENT_BATCH, batch.getAUTOINCREMENT());
        values.put(BATCH_NUMBER, batch.getBATCH_NUMBER());
        values.put(PRODUCT_CODE_BATCH, batch.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_BATCH, batch.getPRODUCT_NAME());
        values.put(PRODUCT_CD_BATCH, batch.getPRODUCT_CD());
        values.put(EXPIRED_DATE_BATCH, batch.getEXPIRED_DATE());
        values.put(UNIT_BATCH, batch.getUNIT());
        values.put(MANUFACTURING_DATE_BATCH, batch.getMANUFACTURING_DATE());
        values.put(STOCKIN_DATE_BATCH, batch.getSTOCKIN_DATE());
        values.put(POSITION_DESCRIPTION_BATCH, batch.getPOSITION_DESCRIPTION());
        values.put(WAREHOUSE_POSITION_CD_BATCH, batch.getWAREHOUSE_POSITION_CD());
        values.put(POSITION_CODE_BATCH, batch.getPOSITION_CODE());
        // insert row
        long id = db.insert(O_BATCH, null, values);
        return id;
    }

    public ArrayList<Batch_number_Tam>
    getoneBatch(String auto) {
        ArrayList<Batch_number_Tam> batch = new ArrayList<Batch_number_Tam>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
//        String selectQuery = "SELECT  * FROM " + O_BATCH + " " + " WHERE "
//                + BATCH_NUMBER + " like " + " '%" + batch_number + "%'";
        String selectQuery = "SELECT  * FROM " + O_BATCH + " " + " WHERE "
                + AUTOINCREMENT_BATCH + " = " + auto ;

        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Batch_number_Tam batch_number_tam = new Batch_number_Tam();
                batch_number_tam.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER))));
                batch_number_tam.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_BATCH))));
                batch_number_tam.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_BATCH))));
                batch_number_tam.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_BATCH))));
                batch_number_tam.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_BATCH))));
                batch_number_tam.setUNIT((c.getString(c
                        .getColumnIndex(UNIT_BATCH))));
                batch_number_tam.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_BATCH))));
                batch_number_tam.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_BATCH))));
                batch_number_tam.setPOSITION_CODE((c.getString(c
                        .getColumnIndex(POSITION_CODE_BATCH))));
                batch_number_tam.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_BATCH))));
                batch_number_tam.setPOSITION_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_DESCRIPTION_BATCH))));

                batch.add(batch_number_tam);
            } while (c.moveToNext());
        }

        c.close();
        return batch;
    }

    public ArrayList<Batch_number_Tam>
    getallBatch() {
        ArrayList<Batch_number_Tam> batch = new ArrayList<Batch_number_Tam>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_BATCH;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Batch_number_Tam batch_number_tam = new Batch_number_Tam();
                batch_number_tam.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER))));
                batch_number_tam.setUNIT((c.getString(c
                        .getColumnIndex(UNIT_BATCH))));
                batch_number_tam.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_BATCH))));

                batch.add(batch_number_tam);
            } while (c.moveToNext());
        }

        c.close();
        return batch;
    }

    public void deleteallBatch_Number() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_BATCH);
    }
    //Table O_RESULT_QA để chứa dữ liệu quét material chuyen ma



    public static final String O_RESULT_QA = "O_RESULT_QA";
    public static final String QRCODE_RESULT_QA = "QRCODE_RESULT_QA";
    public static final String PRODUCT_CODE_RESULT_QA = "PRODUCT_CODE_FROM_RESULT_QA";
    public static final String PRODUCT_NAME_RESULT_QA = "PRODUCT_NAME_FROM_RESULT_QA";
    public static final String PRODUCT_CD_RESULT_QA = "PRODUCT_CD_RESULT_QA";
    public static final String QTY_EA_AVAILABLE_RESULT_QA = "QTY_EA_AVAILABLE_RESULT_QA";
    public static final String QTY_SET_AVAILABLE_RESULT_QA = "QTY_SET_AVAILABLE_RESULT_QA";
    public static final String EXPIRED_DATE_RESULT_QA = "EXPIRED_DATE_RESULT_QA";
    public static final String STOCKIN_DATE_RESULT_QA = "STOCKIN_DATE_RESULT_QA";
    public static final String WAREHOUSE_POSITION_CD_RESULT_QA = "WAREHOUSE_POSITION_CD_RESULT_QA";
    public static final String POSITION_CODE_RESULT_QA = "POSITION_CODE_RESULT_QA";
    public static final String UNIT_RESULT_QA = "UNIT_RESULT_QA";
    public static final String POSITION_DESCRIPTION_RESULT_QA = "POSITION_DESCRIPTION_RESULT_QA";
    public static final String STOCK_QA_CD_RESULT = "STOCK_QA_CD_RESULT";
    public static final String BATCH_NUMBER_RESULT_QA = "BATCH_NUMBER_RESULT_QA";
    public static final String MANUFACTURING_DATE_RESULT_QA = "MANUFACTURING_DATE_RESULT_QA";
    public static final String MIC_CODE_RESULT_QA = "MIC_CODE_RESULT_QA";
    public static final String MIC_DESC_RESULT_QA = "MIC_DESC_RESULT_QA";
    public static final String NOTE_RESULT_QA = "NOTE_RESULT_QA";


    public static final String CREATE_TABLE_O_RESULT_QA = "CREATE TABLE "
            + O_RESULT_QA + "("
            + QRCODE_RESULT_QA + " TEXT,"
            + PRODUCT_CD_RESULT_QA + " TEXT,"
            + PRODUCT_CODE_RESULT_QA + " TEXT,"
            + PRODUCT_NAME_RESULT_QA + " TEXT,"
            + QTY_EA_AVAILABLE_RESULT_QA + " TEXT,"
            + QTY_SET_AVAILABLE_RESULT_QA + " TEXT,"
            + EXPIRED_DATE_RESULT_QA + " TEXT,"
            + STOCKIN_DATE_RESULT_QA + " TEXT,"
            + WAREHOUSE_POSITION_CD_RESULT_QA + " TEXT,"
            + POSITION_CODE_RESULT_QA + " TEXT,"
            + UNIT_RESULT_QA + " TEXT,"
            + MIC_CODE_RESULT_QA + " TEXT,"
            + MIC_DESC_RESULT_QA + " TEXT,"
            + NOTE_RESULT_QA + " TEXT,"
            + POSITION_DESCRIPTION_RESULT_QA + " TEXT,"
            + STOCK_QA_CD_RESULT + " TEXT,"
            + BATCH_NUMBER_RESULT_QA + " TEXT,"
            + MANUFACTURING_DATE_RESULT_QA + " TEXT" + ")";

    public long CreateResultQA(Product_Result_QA chuyenma , String cd ) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_RESULT_QA, chuyenma.getPRODUCT_CD());
        values.put(PRODUCT_CODE_RESULT_QA, chuyenma.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_RESULT_QA, chuyenma.getPRODUCT_NAME());
        values.put(QTY_EA_AVAILABLE_RESULT_QA, chuyenma.getQTY_EA_AVAILABLE());
        values.put(QTY_SET_AVAILABLE_RESULT_QA, chuyenma.getQTY_SET_AVAILABLE());
        values.put(EXPIRED_DATE_RESULT_QA, chuyenma.getEXPIRED_DATE());
        values.put(STOCKIN_DATE_RESULT_QA, chuyenma.getSTOCKIN_DATE());
        values.put(WAREHOUSE_POSITION_CD_RESULT_QA, chuyenma.getWAREHOUSE_POSITION_CD());
        values.put(UNIT_RESULT_QA, chuyenma.getUNIT());
        values.put(MIC_CODE_RESULT_QA, chuyenma.getMIC_CODE());
        values.put(MIC_DESC_RESULT_QA, chuyenma.getMIC_DESC());
        values.put(NOTE_RESULT_QA, chuyenma.getNOTE());
        values.put(STOCK_QA_CD_RESULT, cd);
        values.put(BATCH_NUMBER_RESULT_QA, chuyenma.getBATCH_NUMBER());
        values.put(MANUFACTURING_DATE_RESULT_QA, chuyenma.getMANUFACTURING_DATE());
        // insert row
        long id = db.insert(O_RESULT_QA, null, values);
        return id;
    }


    public ArrayList<Product_Result_QA>
    getAllProduct_RESULT_QA(String cd) {
        ArrayList<Product_Result_QA> qrcode = new ArrayList<Product_Result_QA>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "Select * From O_QA A INNER JOIN O_CRITERIA B ON " +
                " A.BATCH_NUMBER_QA = B.BATCH_NUMBER_CRITERIA " +
                " AND " +
                " A.PRODUCT_CODE = B.PRODUCT_CODE_CRITERIA " +
                " AND " +
                " A.EA_UNIT = B.UNIT_CRITERIA " +
                " WHERE "
                + STOCK_QA_CD_QA + " = " + cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Result_QA qrcodeq = new Product_Result_QA();


                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_QA))));
                qrcodeq.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_QA))));
                qrcodeq.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_CRITERIA))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_QA))));
                qrcodeq.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_QA))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_QA))));
                qrcodeq.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_QA))));
                qrcodeq.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_QA))));
                qrcodeq.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_QA))));
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_QA))));
                qrcodeq.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_QA))));
                qrcodeq.setMIC_CODE((c.getString(c
                        .getColumnIndex(MIC_CODE))));
                qrcodeq.setMIC_DESC((c.getString(c
                        .getColumnIndex(MIC_DESC))));
                qrcodeq.setNOTE((c.getString(c
                        .getColumnIndex(NOTE_CRITERIA))));
                qrcodeq.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_CRITERIA))));
                qrcodeq.setSTOCK_QA_CD((c.getString(c
                        .getColumnIndex(STOCK_QA_CD_QA))));



                qrcode.add(qrcodeq);
                CreateResultQA(qrcodeq , cd);


            } while (c.moveToNext());

        }

        c.close();
        return qrcode;
    }

    public ArrayList<Product_Result_QA>
    getAll_RESULT_QA(String cd) {
        ArrayList<Product_Result_QA> qrcode = new ArrayList<Product_Result_QA>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "Select * From " + O_RESULT_QA + " Where " +
                QTY_SET_AVAILABLE_RESULT_QA + " != '' AND " + STOCK_QA_CD_RESULT + " = " + cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Result_QA qrcodeq = new Product_Result_QA();


                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_RESULT_QA))));
                qrcodeq.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_RESULT_QA))));
                qrcodeq.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_RESULT_QA))));
                qrcodeq.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_RESULT_QA))));
                qrcodeq.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_RESULT_QA))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_RESULT_QA))));
                qrcodeq.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_RESULT_QA))));
                qrcodeq.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_RESULT_QA))));
                qrcodeq.setUNIT((c.getString(c
                        .getColumnIndex(UNIT_RESULT_QA))));
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_RESULT_QA))));
                qrcodeq.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_RESULT_QA))));
                qrcodeq.setMIC_CODE((c.getString(c
                        .getColumnIndex(MIC_CODE_RESULT_QA))));
                qrcodeq.setMIC_DESC((c.getString(c
                        .getColumnIndex(MIC_DESC_RESULT_QA))));
                qrcodeq.setNOTE((c.getString(c
                        .getColumnIndex(NOTE_RESULT_QA))));
                qrcodeq.setSTOCK_QA_CD((c.getString(c
                        .getColumnIndex(STOCK_QA_CD_RESULT))));

                qrcode.add(qrcodeq);
//                CreateResultQA(qrcodeq , cd);


            } while (c.moveToNext());

        }

        c.close();
        return qrcode;
    }

    public void deleteallResult_QA(String cd) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_RESULT_QA + " where " + STOCK_QA_CD_RESULT + " = " + cd);
    }

    //Table O_ChuyenMa để chứa dữ liệu quét material chuyen ma



    public static final String O_CHUYENMA = "O_CHUYENMA";
    public static final String QRCODE_CHUYENMA = "QRCODE_CHUYENMA";
    public static final String PRODUCT_CODE_FROM_CHUYENMA = "PRODUCT_CODE_FROM_CHUYENMA";
    public static final String PRODUCT_CODE_TO_CHUYENMA = "PRODUCT_CODE_TO_CHUYENMA";
    public static final String PRODUCT_NAME_FROM_CHUYENMA = "PRODUCT_NAME_FROM_CHUYENMA";
    public static final String PRODUCT_NAME_TO_CHUYENMA = "PRODUCT_NAME_TO_CHUYENMA";
    public static final String PRODUCT_CD_CHUYENMA = "PRODUCT_CD_CHUYENMA";
    public static final String QTY_EA_AVAILABLE_CHUYENMA = "QTY_EA_AVAILABLE_CHUYENMA";
    public static final String QTY_SET_AVAILABLE_ORIGINAL_CHUYENMA = "QTY_SET_AVAILABLE_ORIGINAL_CHUYENMA";
    public static final String QTY_SET_AVAILABLE_ORIGINAL_2_CHUYENMA = "QTY_SET_AVAILABLE_ORIGINAL_2_CHUYENMA";
    public static final String QTY_SET_AVAILABLE_CHUYENMA = "QTY_SET_AVAILABLE_CHUYENMA";
    public static final String EXPIRED_DATE_CHUYENMA = "EXPIRED_DATE_CHUYENMA";
    public static final String STOCKIN_DATE_CHUYENMA = "STOCKIN_DATE_CHUYENMA";
    public static final String WAREHOUSE_POSITION_CD_CHUYENMA = "WAREHOUSE_POSITION_CD_CHUYENMA";
    public static final String POSITION_CODE_CHUYENMA = "POSITION_CODE_CHUYENMA";
    public static final String UNIT_CHUYENMA = "UNIT_CHUYENMA";
    public static final String UNIT_2_CHUYENMA = "UNIT_2_CHUYENMA";
    public static final String POSITION_DESCRIPTION_CHUYENMA = "POSITION_DESCRIPTION_CHUYENMA";
    public static final String TRANSFER_POSTING_CD_CHUYENMA = "TRANSFER_POSTING_CD_CHUYENMA";
    public static final String BATCH_NUMBER_CHUYENMA = "BATCH_NUMBER_CHUYENMA";
    public static final String MANUFACTURING_DATE_CHUYENMA = "MANUFACTURING_DATE_CHUYENMA";
    public static final String ITEM_BASIC_CHUYENMA = "ITEM_BASIC_CHUYENMA";
    public static final String LPN_FROM_CHUYENMA = "LPN_FROM_CHUYENMA";
    public static final String LPN_TO_CHUYENMA = "LPN_TO_CHUYENMA";
    public static final String SUM_QTY_CHUYENMA = "SUM_QTY_CHUYENMA";


    public static final String CREATE_TABLE_O_CHUYENMA = "CREATE TABLE "
            + O_CHUYENMA + "("
            + QRCODE_CHUYENMA + " TEXT,"
            + PRODUCT_CODE_FROM_CHUYENMA + " TEXT,"
            + PRODUCT_CODE_TO_CHUYENMA + " TEXT,"
            + PRODUCT_NAME_FROM_CHUYENMA + " TEXT,"
            + PRODUCT_NAME_TO_CHUYENMA + " TEXT,"
            + PRODUCT_CD_CHUYENMA + " TEXT,"
            + QTY_EA_AVAILABLE_CHUYENMA + " TEXT,"
            + QTY_SET_AVAILABLE_ORIGINAL_CHUYENMA + " TEXT,"
            + QTY_SET_AVAILABLE_ORIGINAL_2_CHUYENMA + " TEXT,"
            + QTY_SET_AVAILABLE_CHUYENMA + " TEXT,"
            + EXPIRED_DATE_CHUYENMA + " TEXT,"
            + STOCKIN_DATE_CHUYENMA + " TEXT,"
            + WAREHOUSE_POSITION_CD_CHUYENMA + " TEXT,"
            + POSITION_CODE_CHUYENMA + " TEXT,"
            + UNIT_CHUYENMA + " TEXT,"
            + UNIT_2_CHUYENMA + " TEXT,"
            + LPN_FROM_CHUYENMA + " TEXT,"
            + SUM_QTY_CHUYENMA + " TEXT,"
            + LPN_TO_CHUYENMA + " TEXT,"
            + POSITION_DESCRIPTION_CHUYENMA + " TEXT,"
            + TRANSFER_POSTING_CD_CHUYENMA + " TEXT,"
            + BATCH_NUMBER_CHUYENMA + " TEXT,"
            + MANUFACTURING_DATE_CHUYENMA + " TEXT,"
            + ITEM_BASIC_CHUYENMA + " TEXT" + ")";

    public long CreateChuyenma(Product_ChuyenMa chuyenma , String cd ) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(QRCODE_CHUYENMA, chuyenma.getQRCODE());
        values.put(PRODUCT_CODE_FROM_CHUYENMA, chuyenma.getPRODUCT_CODE_FROM());
        values.put(PRODUCT_CODE_TO_CHUYENMA, chuyenma.getPRODUCT_CODE_TO());
        values.put(PRODUCT_NAME_FROM_CHUYENMA, chuyenma.getPRODUCT_NAME_FROM());
        values.put(PRODUCT_NAME_TO_CHUYENMA, chuyenma.getPRODUCT_NAME_TO());
        values.put(PRODUCT_CD_CHUYENMA, chuyenma.getPRODUCT_CD());
        values.put(QTY_EA_AVAILABLE_CHUYENMA, chuyenma.getQTY_EA_AVAILABLE());
        values.put(QTY_SET_AVAILABLE_ORIGINAL_CHUYENMA, chuyenma.getQTY_SET_AVAILABLE_ORIGINAL());
        values.put(QTY_SET_AVAILABLE_ORIGINAL_2_CHUYENMA, chuyenma.getQTY_SET_AVAILABLE_ORIGINAL_2());
        values.put(QTY_SET_AVAILABLE_CHUYENMA, chuyenma.getQTY_SET_AVAILABLE());
        values.put(EXPIRED_DATE_CHUYENMA, chuyenma.getEXPIRED_DATE());
        values.put(STOCKIN_DATE_CHUYENMA, chuyenma.getSTOCKIN_DATE());
        values.put(WAREHOUSE_POSITION_CD_CHUYENMA, chuyenma.getWAREHOUSE_POSITION_CD());
        values.put(POSITION_CODE_CHUYENMA, chuyenma.getPOSITION_CODE());
        values.put(UNIT_CHUYENMA, chuyenma.getUNIT());
        values.put(UNIT_2_CHUYENMA, chuyenma.getUNIT_2());
        values.put(SUM_QTY_CHUYENMA, chuyenma.getSUM_QTY());
        values.put(LPN_FROM_CHUYENMA, " ");
        values.put(LPN_TO_CHUYENMA, " ");
        values.put(POSITION_DESCRIPTION_CHUYENMA, chuyenma.getPOSITION_DESCRIPTION());
        values.put(TRANSFER_POSTING_CD_CHUYENMA, cd);
        values.put(BATCH_NUMBER_CHUYENMA, chuyenma.getBATCH_NUMBER());
        values.put(MANUFACTURING_DATE_CHUYENMA, chuyenma.getMANUFACTURING_DATE());
        values.put(ITEM_BASIC_CHUYENMA, chuyenma.getITEM_BASIC());
        // insert row
        long id = db.insert(O_CHUYENMA, null, values);
        return id;
    }


    public ArrayList<Product_ChuyenMa>
    getAllProduct_ChuyenMa(String cd) {
        ArrayList<Product_ChuyenMa> qrcode = new ArrayList<Product_ChuyenMa>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "Select * From O_MATERIAL A INNER JOIN O_SP B ON A.ITEM_BASIC_MATERIAL = B.ITEM_BASIC_SP ";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_ChuyenMa qrcodeq = new Product_ChuyenMa();

                qrcodeq.setQRCODE((c.getString(c
                        .getColumnIndex(QRCODE_SP))));
                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_SP))));
                qrcodeq.setPRODUCT_CODE_FROM((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_SP))));
                qrcodeq.setPRODUCT_CODE_TO((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_MATERIAL))));
                qrcodeq.setPRODUCT_NAME_FROM((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_SP))));
                qrcodeq.setPRODUCT_NAME_TO((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_MATERIAL))));
                qrcodeq.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_SP))));
                qrcodeq.setQTY_SET_AVAILABLE_ORIGINAL((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_SP))));
                qrcodeq.setQTY_SET_AVAILABLE_ORIGINAL_2((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_SP_2))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_SP))));
                qrcodeq.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_SP))));
                qrcodeq.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_SP))));
                qrcodeq.setPOSITION_CODE((c.getString(c
                        .getColumnIndex(POSITION_CODE_SP))));
                qrcodeq.setUNIT((c.getString(c
                        .getColumnIndex(UNIT_SP))));
                qrcodeq.setUNIT_2((c.getString(c
                        .getColumnIndex(UNIT_SP_2))));
                qrcodeq.setPOSITION_DESCRIPTION(c.getString(c
                        .getColumnIndex(POSITION_DESCRIPTION_SP)));
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_SP))));
                qrcodeq.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_SP))));
                qrcodeq.setITEM_BASIC((c.getString(c
                        .getColumnIndex(ITEM_BASIC_SP))));


                qrcode.add(qrcodeq);
                CreateChuyenma(qrcodeq , cd);


            } while (c.moveToNext());
            DatabaseHelper.getInstance().deleteallMaterial();
            DatabaseHelper.getInstance().deleteallSP();
        }

        c.close();
        return qrcode;
    }

    public ArrayList<Product_ChuyenMa>
    getAll_ChuyenMa(String cd) {
        ArrayList<Product_ChuyenMa> qrcode = new ArrayList<Product_ChuyenMa>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "Select * From O_CHUYENMA Where " +
                QTY_SET_AVAILABLE_CHUYENMA + " != '' AND " + TRANSFER_POSTING_CD_CHUYENMA + " = " + cd  ;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_ChuyenMa qrcodeq = new Product_ChuyenMa();
                qrcodeq.setQRCODE((c.getString(c
                        .getColumnIndex(QRCODE_CHUYENMA))));
                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_CHUYENMA))));
                qrcodeq.setPRODUCT_CODE_FROM((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_FROM_CHUYENMA))));
                qrcodeq.setPRODUCT_CODE_TO((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_TO_CHUYENMA))));
                qrcodeq.setPRODUCT_NAME_FROM((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_FROM_CHUYENMA))));
                qrcodeq.setPRODUCT_NAME_TO((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_TO_CHUYENMA))));
                qrcodeq.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_CHUYENMA))));
                qrcodeq.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_CHUYENMA))));
                qrcodeq.setQTY_SET_AVAILABLE_ORIGINAL((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_ORIGINAL_CHUYENMA))));
                qrcodeq.setQTY_SET_AVAILABLE_ORIGINAL_2((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_ORIGINAL_2_CHUYENMA))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_CHUYENMA))));
                qrcodeq.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_CHUYENMA))));
                qrcodeq.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_CHUYENMA))));
                qrcodeq.setPOSITION_CODE((c.getString(c
                        .getColumnIndex(POSITION_CODE_CHUYENMA))));
                qrcodeq.setUNIT((c.getString(c
                        .getColumnIndex(UNIT_CHUYENMA))));
                qrcodeq.setUNIT_2((c.getString(c
                        .getColumnIndex(UNIT_2_CHUYENMA))));
                qrcodeq.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_CHUYENMA))));
                qrcodeq.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_CHUYENMA))));
                qrcodeq.setPOSITION_DESCRIPTION(c.getString(c
                        .getColumnIndex(POSITION_DESCRIPTION_CHUYENMA)));
                qrcodeq.setTRANSFER_POSTING_CD((c.getString(c
                        .getColumnIndex(TRANSFER_POSTING_CD_CHUYENMA))));
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_CHUYENMA))));
                qrcodeq.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_CHUYENMA))));
                qrcodeq.setITEM_BASIC((c.getString(c
                        .getColumnIndex(ITEM_BASIC_CHUYENMA))));
                qrcode.add(qrcodeq);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }

    public ArrayList<Product_ChuyenMa>
    getCheckQTy_ChuyenMa(String cd) {
        ArrayList<Product_ChuyenMa> qrcode = new ArrayList<Product_ChuyenMa>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "Select SUM(QTY_SET_AVAILABLE_CHUYENMA) as SUM_QTY_CHUYENMA, QTY_SET_AVAILABLE_ORIGINAL_CHUYENMA " +
                "From O_CHUYENMA Where " +
                QTY_SET_AVAILABLE_CHUYENMA + " != '' AND " + TRANSFER_POSTING_CD_CHUYENMA + " = " + cd
                + " GROUP BY PRODUCT_CODE_FROM_CHUYENMA , UNIT_CHUYENMA , BATCH_NUMBER_CHUYENMA , ITEM_BASIC_CHUYENMA " ;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_ChuyenMa qrcodeq = new Product_ChuyenMa();

                qrcodeq.setSUM_QTY((c.getString(c
                        .getColumnIndex(SUM_QTY_CHUYENMA))));
                qrcodeq.setQTY_SET_AVAILABLE_ORIGINAL((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_ORIGINAL_CHUYENMA))));

                qrcode.add(qrcodeq);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }

    public ArrayList<Product_ChuyenMa>
    getDataMaterialbyItemBasic(String product_code_from , String  unit , String cd , String batch) {
        ArrayList<Product_ChuyenMa> listMaterialChuyenma = new ArrayList<Product_ChuyenMa>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "Select * From " + O_CHUYENMA  + " Where "
                + PRODUCT_CODE_FROM_CHUYENMA + " = "  +  product_code_from
                + " AND " + TRANSFER_POSTING_CD_CHUYENMA + " = " + cd
                + " AND " + BATCH_NUMBER_CHUYENMA + " = '" + batch
                + "' AND " + UNIT_CHUYENMA + " = '" + unit
                + "' ORDER BY " + BATCH_NUMBER_CHUYENMA + " DESC";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_ChuyenMa item = new Product_ChuyenMa();

                item.setPRODUCT_CODE_FROM((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_FROM_CHUYENMA))));
                item.setPRODUCT_CODE_TO((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_TO_CHUYENMA))));
                item.setTRANSFER_POSTING_CD((c.getString(c
                        .getColumnIndex(TRANSFER_POSTING_CD_CHUYENMA))));
                item.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_CHUYENMA))));
                item.setQTY_SET_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_CHUYENMA))));
                item.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_CHUYENMA))));
                item.setUNIT((c.getString(c
                        .getColumnIndex(UNIT_CHUYENMA))));
                item.setUNIT_2((c.getString(c
                        .getColumnIndex(UNIT_2_CHUYENMA))));
                item.setPRODUCT_NAME_TO((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_TO_CHUYENMA))));

                listMaterialChuyenma.add(item);
//                CreateChuyenma(qrcodeq);

            } while (c.moveToNext());
        }

        c.close();
        return listMaterialChuyenma;
    }

    public int updateProduct_ChuyenMa(String product_code_from, String product_code_to , String qty , String batch , String unit ) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(QTY_SET_AVAILABLE_CHUYENMA, qty);
        return db.update(O_CHUYENMA, values,  PRODUCT_CODE_FROM_CHUYENMA + " = ? AND " + PRODUCT_CODE_TO_CHUYENMA + " = ? AND " + UNIT_CHUYENMA + " = ? AND " + BATCH_NUMBER_CHUYENMA + " = ? " ,
                new String[]{String.valueOf(product_code_from),String.valueOf(product_code_to),String.valueOf(unit),String.valueOf(batch)});

    }

    public ArrayList<Product_ChuyenMa>
    getshow_ChuyenMa(String chuyenma_cd) {
        ArrayList<Product_ChuyenMa> listchuyenma = new ArrayList<Product_ChuyenMa>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "Select * From O_CHUYENMA Where " + TRANSFER_POSTING_CD_CHUYENMA + " = " + chuyenma_cd +
                " GROUP BY PRODUCT_CODE_FROM_CHUYENMA , PRODUCT_NAME_FROM_CHUYENMA , EXPIRED_DATE_CHUYENMA , BATCH_NUMBER_CHUYENMA ," +
                " UNIT_CHUYENMA ORDER BY "
                + BATCH_NUMBER_CHUYENMA + " DESC";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_ChuyenMa itemchuyenma = new Product_ChuyenMa();

                itemchuyenma.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_CHUYENMA))));
                itemchuyenma.setPRODUCT_CODE_FROM((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_FROM_CHUYENMA))));
                itemchuyenma.setPRODUCT_NAME_FROM((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_FROM_CHUYENMA))));
                itemchuyenma.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_CHUYENMA))));
                itemchuyenma.setITEM_BASIC((c.getString(c
                        .getColumnIndex(ITEM_BASIC_CHUYENMA))));
                itemchuyenma.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_CHUYENMA))));
                itemchuyenma.setTRANSFER_POSTING_CD((c.getString(c
                        .getColumnIndex(TRANSFER_POSTING_CD_CHUYENMA))));
                itemchuyenma.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_CHUYENMA))));
                itemchuyenma.setQTY_SET_AVAILABLE_ORIGINAL((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_ORIGINAL_CHUYENMA))));
                itemchuyenma.setQTY_SET_AVAILABLE_ORIGINAL_2((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_ORIGINAL_2_CHUYENMA))));
                itemchuyenma.setUNIT((c.getString(c
                        .getColumnIndex(UNIT_CHUYENMA))));
                itemchuyenma.setUNIT_2((c.getString(c
                        .getColumnIndex(UNIT_2_CHUYENMA))));


                listchuyenma.add(itemchuyenma);
//                CreateChuyenma(qrcodeq);

            } while (c.moveToNext());
        }

        c.close();
        return listchuyenma;
    }


    public void deleteProduct_Chuyen_Ma_Specific
            (String product_cd, String batch_number , String item_basic , String expired_date , String warehouse_position_cd) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_CHUYENMA + " Where " + PRODUCT_CD_CHUYENMA + " = " + product_cd + " AND "
                + ITEM_BASIC_CHUYENMA + " = " + item_basic + " AND "
                + WAREHOUSE_POSITION_CD_CHUYENMA + " = " + warehouse_position_cd );
    }
    public void deleteallChuyenMa(String cd) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_CHUYENMA + " Where " + TRANSFER_POSTING_CD_CHUYENMA + " = " + cd);
    }

    //end table chuyenma

    //Table O_Material để chứa dữ liệu quét material chuyen ma
    public static final String O_SP = "O_SP";
    public static final String QRCODE_SP = "QRCODE_SP";
    public static final String PRODUCT_CODE_SP = "PRODUCT_CODE_SP";
    public static final String PRODUCT_NAME_SP = "PRODUCT_NAME_SP";
    public static final String PRODUCT_CD_SP = "PRODUCT_CD_SP";
    public static final String QTY_EA_AVAILABLE_SP = "QTY_EA_AVAILABLE_SP";
    public static final String QTY_SET_AVAILABLE_SP = "QTY_SET_AVAILABLE_SP";
    public static final String QTY_SET_AVAILABLE_SP_2 = "QTY_SET_AVAILABLE_SP_2";
    public static final String EXPIRED_DATE_SP = "EXPIRED_DATE_SP";
    public static final String STOCKIN_DATE_SP = "STOCKIN_DATE_SP";
    public static final String WAREHOUSE_POSITION_CD_SP = "WAREHOUSE_POSITION_CD_SP";
    public static final String POSITION_CODE_SP = "POSITION_CODE_SP";
    public static final String UNIT_SP = "UNIT_SP";
    public static final String UNIT_SP_2 = "UNIT_SP_2";
    public static final String POSITION_DESCRIPTION_SP = "POSITION_DESCRIPTION_SP";
    public static final String TRANSFER_POSTING_CD_SP = "TRANSFER_POSTING_CD_SP";
    public static final String BATCH_NUMBER_SP = "BATCH_NUMBER_SP";
    public static final String MANUFACTURING_DATE_SP = "MANUFACTURING_DATE_SP";
    public static final String ITEM_BASIC_SP = "ITEM_BASIC_SP";

    public static final String CREATE_TABLE_O_SP = "CREATE TABLE "
            + O_SP + "("
            + QRCODE_SP + " TEXT,"
            + PRODUCT_CODE_SP + " TEXT,"
            + PRODUCT_NAME_SP + " TEXT,"
            + PRODUCT_CD_SP + " TEXT,"
            + QTY_EA_AVAILABLE_SP + " TEXT,"
            + QTY_SET_AVAILABLE_SP + " TEXT,"
            + QTY_SET_AVAILABLE_SP_2 + " TEXT,"
            + EXPIRED_DATE_SP + " TEXT,"
            + STOCKIN_DATE_SP + " TEXT,"
            + WAREHOUSE_POSITION_CD_SP + " TEXT,"
            + POSITION_CODE_SP + " TEXT,"
            + UNIT_SP + " TEXT,"
            + UNIT_SP_2 + " TEXT,"
            + POSITION_DESCRIPTION_SP + " TEXT,"
            + TRANSFER_POSTING_CD_SP + " TEXT,"
            + BATCH_NUMBER_SP + " TEXT,"
            + MANUFACTURING_DATE_SP + " TEXT,"
            + ITEM_BASIC_SP + " TEXT" + ")";

    public long CreateSP(Product_SP sp) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(QRCODE_SP, sp.getQRCODE());
        values.put(PRODUCT_CODE_SP, sp.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_SP, sp.getPRODUCT_NAME());
        values.put(PRODUCT_CD_SP, sp.getPRODUCT_CD());
        values.put(QTY_EA_AVAILABLE_SP, sp.getQTY_EA_AVAILABLE());
        values.put(QTY_SET_AVAILABLE_SP, sp.getQTY_SET_AVAILABLE());
        values.put(QTY_SET_AVAILABLE_SP_2, sp.getQTY_SET_AVAILABLE_2());
        values.put(EXPIRED_DATE_SP, sp.getEXPIRED_DATE());
        values.put(STOCKIN_DATE_SP, sp.getSTOCKIN_DATE());
        values.put(WAREHOUSE_POSITION_CD_SP, sp.getWAREHOUSE_POSITION_CD());
        values.put(POSITION_CODE_SP, sp.getPOSITION_CODE());
        values.put(UNIT_SP, sp.getUNIT());
        values.put(UNIT_SP_2, sp.getUNIT_2());
        values.put(POSITION_DESCRIPTION_SP, sp.getPOSITION_DESCRIPTION());
        values.put(TRANSFER_POSTING_CD_SP, sp.getTRANSFER_POSTING_CD());
        values.put(BATCH_NUMBER_SP, sp.getBATCH_NUMBER());
        values.put(MANUFACTURING_DATE_SP, sp.getMANUFACTURING_DATE());
        values.put(ITEM_BASIC_SP, sp.getITEM_BASIC());
        // insert row
        long id = db.insert(O_SP, null, values);
        return id;
    }
    public void deleteallSP() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_SP);
    }

    //end table SP

    //Table O_Material để chứa dữ liệu quét material chuyen ma
    public static final String O_MATERIAL = "O_MATERIAL";
    public static final String PRODUCT_CD_MATERIAL = "PRODUCT_CD_MATERIAL";
    public static final String PRODUCT_CODE_MATERIAL = "PRODUCT_CODE_MATERIAL";
    public static final String PRODUCT_NAME_MATERIAL = "PRODUCT_NAME_MATERIAL";
    public static final String BARCODE_MATERIAL = "BARCODE_MATERIAL";
    public static final String ITEM_BASIC_MATERIAL = "ITEM_BASIC_MATERIAL";

    public static final String CREATE_TABLE_O_MATERIAL = "CREATE TABLE "
            + O_MATERIAL + "("
            + PRODUCT_CD_MATERIAL + " TEXT,"
            + PRODUCT_CODE_MATERIAL + " TEXT,"
            + PRODUCT_NAME_MATERIAL + " TEXT,"
            + BARCODE_MATERIAL + " TEXT,"
            + ITEM_BASIC_MATERIAL + " TEXT" + ")";

    public long CreateMaterial(Product_Material material) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        //values.put(QRCODE, qrcode.getQRCODE());
        values.put(PRODUCT_CD_MATERIAL, material.getPRODUCT_CD());
        values.put(PRODUCT_CODE_MATERIAL, material.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_MATERIAL, material.getPRODUCT_NAME());
        values.put(BARCODE_MATERIAL, material.getBARCODE());
        values.put(ITEM_BASIC_MATERIAL, material.getITEM_BABIC());
        // insert row
        long id = db.insert(O_MATERIAL, null, values);
        return id;
    }
    public void deleteallMaterial() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_MATERIAL);
    }

    //end table material
    //Table O_PRODUCT_SP để chứa dữ liệu quét exp lần đầu
    public static final String O_PRODUCT_SP = "O_PRODUCT_SP";
    public static final String PRODUCT_CODE_S_P = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_S_P = "PRODUCT_NAME";
    public static final String PRODUCT_CD_S_P = "PRODUCT_CD";


    public static final String CREATE_TABLE_O_PRODUCT_SP = "CREATE TABLE "
            + O_PRODUCT_SP + "("
            + PRODUCT_NAME_S_P + " TEXT,"
            + PRODUCT_CD_S_P + " TEXT,"
            + PRODUCT_CODE_S_P + " TEXT" + ")";

    public long CreateProduct_SP(Product_S_P product) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(PRODUCT_CODE_S_P, product.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_S_P, product.getPRODUCT_NAME());
        values.put(PRODUCT_CD_S_P, product.getPRODUCT_CD());
        // insert row
        long id = db.insert(O_PRODUCT_SP, null, values);
        return id;
    }

    public ArrayList<Product_S_P>
    getallValueSP() {
        ArrayList<Product_S_P> listproduct = new ArrayList<Product_S_P>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_PRODUCT_SP;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_S_P product = new Product_S_P();
                product.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_S_P))));
                product.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_S_P))));
                product.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_S_P))));
                listproduct.add(product);
            } while (c.moveToNext());
        }

        c.close();
        return listproduct;
    }

    public void deleteallProduct_S_P() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_PRODUCT_SP);
    }
    //End table O_PRODUCT



    //Table O_EXP để chứa dữ liệu quét exp lần đầu
    public static final String O_EXP = "O_EXP";
    public static final String EXPIRED_DATE_TAM = "EXPIRED_DATE_TAM";
    public static final String PRODUCT_CODE_TAM = "PRODUCT_CODE_TAM";
    public static final String STOCKIN_DATE_TAM = "STOCKIN_DATE_TAM";
    public static final String PRODUCT_CD_TAM = "PRODUCT_CD_TAM";
    public static final String TOTAL_SHELF_LIFE = "TOTAL_SHELF_LIFE";
    public static final String SHELF_LIFE_TYPE = "SHELF_LIFE_TYPE";
    public static final String MIN_REM_SHELF_LIFE = "MIN_REM_SHELF_LIFE";
    public static final String BATCH_NUMBER_TAM = "BATCH_NUMBER_TAM";
    public static final String POSITION_CODE_TAM = "POSITION_CODE_TAM";
    public static final String LPN_CODE_TAM = "LPN_CODE_TAM";
    public static final String WAREHOUSE_POSITION_CD_TAM = "WAREHOUSE_POSITION_CD_TAM";

    public static final String CREATE_TABLE_O_EXP = "CREATE TABLE "
            + O_EXP + "("
            + PRODUCT_CODE_TAM + " TEXT,"
            + POSITION_CODE_TAM + " TEXT,"
            + WAREHOUSE_POSITION_CD_TAM + " TEXT,"
            + STOCKIN_DATE_TAM + " TEXT,"
            + LPN_CODE_TAM + " TEXT,"
            + TOTAL_SHELF_LIFE + " TEXT,"
            + PRODUCT_CD_TAM + " TEXT,"
            + BATCH_NUMBER_TAM + " TEXT,"
            + SHELF_LIFE_TYPE + " TEXT,"
            + MIN_REM_SHELF_LIFE + " TEXT,"
            + EXPIRED_DATE_TAM + " TEXT" + ")";

    public long CreateExp_date(Exp_Date_Tam exp) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        //values.put(QRCODE, qrcode.getQRCODE());
        values.put(TOTAL_SHELF_LIFE, exp.getTOTAL_SHELF_LIFE());
        values.put(POSITION_CODE_TAM, exp.getPOSITION_CODE_TAM());
        values.put(LPN_CODE_TAM, exp.getLPN_CODE_TAM());
        values.put(WAREHOUSE_POSITION_CD_TAM, exp.getWAREHOUSE_POSITION_CD_TAM());
        values.put(STOCKIN_DATE_TAM, exp.getSTOCKIN_DATE_TAM());
        values.put(PRODUCT_CD_TAM, exp.getPRODUCT_CD_TAM());
        values.put(PRODUCT_CODE_TAM, exp.getPRODUCT_CODE_TAM());
        values.put(SHELF_LIFE_TYPE, exp.getSHELF_LIFE_TYPE());
        values.put(BATCH_NUMBER_TAM, exp.getBATCH_NUMBER_TAM());
        values.put(MIN_REM_SHELF_LIFE, exp.getMIN_REM_SHELF_LIFE());
        values.put(EXPIRED_DATE_TAM, exp.getEXPIRED_DATE_TAM());
        // insert row
        long id = db.insert(O_EXP, null, values);
        return id;
    }

    public ArrayList<Exp_Date_Tam>
    getallValue() {
        ArrayList<Exp_Date_Tam> exp = new ArrayList<Exp_Date_Tam>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_EXP;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Exp_Date_Tam expd = new Exp_Date_Tam();
                expd.setEXPIRED_DATE_TAM((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_TAM))));
                expd.setBATCH_NUMBER_TAM((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_TAM))));
                expd.setPRODUCT_CODE_TAM((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_TAM))));

                exp.add(expd);
            } while (c.moveToNext());
        }

        c.close();
        return exp;
    }
    public ArrayList<Exp_Date_Tam>
    getallValueinventory(String pro_code , String vitri) {
        ArrayList<Exp_Date_Tam> exp = new ArrayList<Exp_Date_Tam>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "";
        if(vitri.contains("-")){
            selectQuery = "SELECT DISTINCT WAREHOUSE_POSITION_CD_TAM , STOCKIN_DATE_TAM ,EXPIRED_DATE_TAM , POSITION_CODE_TAM,  BATCH_NUMBER_TAM , PRODUCT_CODE_TAM , PRODUCT_CD_TAM FROM " + O_EXP +
                    " where " + PRODUCT_CODE_TAM + " = '" + pro_code + "' AND ( " + POSITION_CODE_TAM  + " = '" + vitri + "' OR " + EXPIRED_DATE_TAM + " = 'Khác') "
                    ;
        }else{
            selectQuery = "SELECT DISTINCT WAREHOUSE_POSITION_CD_TAM , STOCKIN_DATE_TAM ,EXPIRED_DATE_TAM , LPN_CODE_TAM,  BATCH_NUMBER_TAM , PRODUCT_CODE_TAM , PRODUCT_CD_TAM FROM " + O_EXP +
                    " where " + PRODUCT_CODE_TAM + " = '" + pro_code + "' AND ( " + LPN_CODE_TAM  + " = '" + vitri + "'  OR " + EXPIRED_DATE_TAM + " = 'Khác') "
            ;
        }

        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Exp_Date_Tam expd = new Exp_Date_Tam();
                expd.setEXPIRED_DATE_TAM((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_TAM))));
                expd.setPRODUCT_CD_TAM((c.getString(c
                        .getColumnIndex(PRODUCT_CD_TAM))));
                expd.setSTOCKIN_DATE_TAM((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_TAM))));
                expd.setBATCH_NUMBER_TAM((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_TAM))));
                expd.setPRODUCT_CODE_TAM((c.getString(c
                            .getColumnIndex(PRODUCT_CODE_TAM))));
                expd.setWAREHOUSE_POSITION_CD_TAM((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_TAM))));

                exp.add(expd);
            } while (c.moveToNext());
        }

        c.close();
        return exp;
    }

    public ArrayList<Exp_Date_Tam>
    getallValue2(String pro_code) {
        ArrayList<Exp_Date_Tam> exp = new ArrayList<Exp_Date_Tam>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT DISTINCT STOCKIN_DATE_TAM ,EXPIRED_DATE_TAM , BATCH_NUMBER_TAM , PRODUCT_CODE_TAM , PRODUCT_CD_TAM FROM " + O_EXP +
                " where " + PRODUCT_CODE_TAM + " = '" + pro_code + "' "
                ;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Exp_Date_Tam expd = new Exp_Date_Tam();
                expd.setEXPIRED_DATE_TAM((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_TAM))));
                expd.setPRODUCT_CD_TAM((c.getString(c
                        .getColumnIndex(PRODUCT_CD_TAM))));
                expd.setSTOCKIN_DATE_TAM((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_TAM))));
                expd.setBATCH_NUMBER_TAM((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_TAM))));
                expd.setPRODUCT_CODE_TAM((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_TAM))));

                exp.add(expd);
            } while (c.moveToNext());
        }

        c.close();
        return exp;
    }

    public ArrayList<Exp_Date_Tam>
    getallValueStockin() {
        ArrayList<Exp_Date_Tam> exp = new ArrayList<Exp_Date_Tam>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_EXP;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Exp_Date_Tam expd = new Exp_Date_Tam();
                expd.setEXPIRED_DATE_TAM((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_TAM))));
                expd.setTOTAL_SHELF_LIFE((c.getString(c
                        .getColumnIndex(TOTAL_SHELF_LIFE))));
                expd.setSHELF_LIFE_TYPE((c.getString(c
                        .getColumnIndex(SHELF_LIFE_TYPE))));
                expd.setBATCH_NUMBER_TAM((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_TAM))));
                expd.setMIN_REM_SHELF_LIFE((c.getString(c
                        .getColumnIndex(MIN_REM_SHELF_LIFE))));

                exp.add(expd);
            } while (c.moveToNext());
        }

        c.close();
        return exp;
    }
    //Date date1 ;
    public ArrayList<Exp_Date_Tam>
    getallExp_date() {
        ArrayList<Exp_Date_Tam> exp = new ArrayList<Exp_Date_Tam>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT DISTINCT CASE WHEN EXPIRED_DATE_TAM = '' THEN '------' ELSE EXPIRED_DATE_TAM END AS EXPIRED_DATE_TAM FROM " + O_EXP;
//        try {
//            date1 =new SimpleDateFormat("dd/MM/yyyy").parse(EXPIRED_DATE_TAM);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //12/02/2001
//        String selectQuery = "SELECT DISTINCT CASE WHEN EXPIRED_DATE_TAM = '' THEN '------' ELSE EXPIRED_DATE_TAM END AS EXPIRED_DATE_TAM ," +
//                " substr(" + EXPIRED_DATE_TAM + ",7,4) AS year  , substr(" + EXPIRED_DATE_TAM + ",4,2) AS mon , substr(" + EXPIRED_DATE_TAM + ",1,2) AS day FROM "
////        String selectQuery = "SELECT " + EXPIRED_DATE_TAM +  " , substr(" + EXPIRED_DATE_TAM + ",7,4) AS year  , substr(" + EXPIRED_DATE_TAM + ",4,2) AS mon , substr(" + EXPIRED_DATE_TAM + ",1,2) AS day FROM "
//                + O_EXP + " ORDER BY year ASC , mon ASC , day ASC";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Exp_Date_Tam expd = new Exp_Date_Tam();
                expd.setEXPIRED_DATE_TAM((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_TAM))));

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
    //Table Take Photo Containers để chứa dữ liệu
    public static final String O_PHOTO_CONTAINERS = "O_PHOTO_CONTAINERS";
    public static final String WAREHOUSE_CONTAINER_CD = "WAREHOUSE_CONTAINER_CD";
    public static final String PHOTO_DATE_CONTAINERS = "PHOTO_DATE_CONTAINERS";
    public static final String PHOTO_NAME_CONTAINERS = "PHOTO_NAME_CONTAINERS";


    public static final String CREATE_TABLE_O_PHOTO_CONTAINERS = "CREATE TABLE "
            + O_PHOTO_CONTAINERS + "("
            + WAREHOUSE_CONTAINER_CD + " TEXT,"
            + PHOTO_DATE_CONTAINERS + " TEXT,"
            + PHOTO_NAME_CONTAINERS + " TEXT" + ")";

    public long CreatePhotoContainers(Product_Photo_Containers photo) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(WAREHOUSE_CONTAINER_CD, photo.getWAREHOUSE_CONTAINER_CD());
        values.put(PHOTO_DATE_CONTAINERS, photo.getPHOTO_DATE());
        values.put(PHOTO_NAME_CONTAINERS, photo.getPHOTO_NAME());
        // insert row
        long id = db.insert(O_PHOTO_CONTAINERS, null, values);
        return id;
    }

    public ArrayList<Product_Photo_Containers>
    getallPhotoContainers(String cd) {
        ArrayList<Product_Photo_Containers> listphoto = new ArrayList<Product_Photo_Containers>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_PHOTO_CONTAINERS + " where " + WAREHOUSE_CONTAINER_CD + " = " + cd ;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_Photo_Containers item_photo = new Product_Photo_Containers();
                item_photo.setWAREHOUSE_CONTAINER_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_CONTAINER_CD))));
                item_photo.setPHOTO_DATE((c.getString(c
                        .getColumnIndex(PHOTO_DATE_CONTAINERS))));
                item_photo.setPHOTO_NAME((c.getString(c
                        .getColumnIndex(PHOTO_NAME_CONTAINERS))));


                listphoto.add(item_photo);
            } while (c.moveToNext());
        }

        c.close();
        return listphoto;
    }

    public void deleteallPhotoContainer(String cd) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_CRITERIA + " where "
                + WAREHOUSE_CONTAINER_CD + " = " + cd);
    }
    // End Table Take Photo Containers

    //End table EXP
    //Table Take Photo QA để chứa dữ liệu
    public static final String O_PHOTO_QA = "O_PHOTO_QA";
    public static final String PRODUCT_CODE_PHOTO_QA = "PRODUCT_CODE_PHOTO_QA";
    public static final String STOCK_QA_CD_PHOTO_QA = "STOCK_QA_CD_PHOTO_QA";
    public static final String UNIT_PHOTO_QA = "UNIT_PHOTO_QA";
    public static final String EXPIRED_DATE_PHOTO_QA = "EXPIRED_DATE_PHOTO_QA";
    public static final String BATCH_NUMBER_PHOTO_QA = "BATCH_NUMBER_PHOTO_QA";
    public static final String STOCKIN_DATE_PHOTO_QA = "STOCKIN_DATE_PHOTO_QA";
    public static final String PHOTO_DATE = "PHOTO_DATE";
    public static final String PHOTO_NAME = "PHOTO_NAME";


    public static final String CREATE_TABLE_O_PHOTO_QA = "CREATE TABLE "
            + O_PHOTO_QA + "("
            + PRODUCT_CODE_PHOTO_QA + " TEXT,"
            + STOCK_QA_CD_PHOTO_QA + " TEXT,"
            + UNIT_PHOTO_QA + " TEXT,"
            + EXPIRED_DATE_PHOTO_QA + " TEXT,"
            + BATCH_NUMBER_PHOTO_QA + " TEXT,"
            + STOCKIN_DATE_PHOTO_QA + " TEXT,"
            + PHOTO_DATE + " TEXT,"
            + PHOTO_NAME + " TEXT" + ")";

    public long CreatePhotoQA(Product_Photo_QA photo) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        //values.put(QRCODE, qrcode.getQRCODE());
        values.put(PRODUCT_CODE_PHOTO_QA, photo.getPRODUCT_CODE());
        values.put(STOCK_QA_CD_PHOTO_QA, photo.getSTOCK_QA_CD());
        values.put(UNIT_PHOTO_QA, photo.getUNIT());
        values.put(EXPIRED_DATE_PHOTO_QA, photo.getEXPIRED_DATE());
        values.put(BATCH_NUMBER_PHOTO_QA, photo.getBATCH_NUMBER());
        values.put(STOCKIN_DATE_PHOTO_QA, photo.getSTOCKIN_DATE());
        values.put(PHOTO_DATE, photo.getPHOTO_DATE());
        values.put(PHOTO_NAME, photo.getPHOTO_NAME());
        // insert row
        long id = db.insert(O_PHOTO_QA, null, values);
        return id;
    }

    public ArrayList<Product_Photo_QA>
    getallPhotoQA(String cd) {
        ArrayList<Product_Photo_QA> listphoto = new ArrayList<Product_Photo_QA>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_PHOTO_QA + " where " + STOCK_QA_CD_PHOTO_QA + " = " + cd ;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_Photo_QA item_photo = new Product_Photo_QA();
                item_photo.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PHOTO_QA))));
                item_photo.setSTOCK_QA_CD((c.getString(c
                        .getColumnIndex(STOCK_QA_CD_PHOTO_QA))));
                item_photo.setUNIT((c.getString(c
                        .getColumnIndex(UNIT_PHOTO_QA))));
                item_photo.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PHOTO_QA))));
                item_photo.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PHOTO_QA))));
                item_photo.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_PHOTO_QA))));
                item_photo.setPHOTO_DATE((c.getString(c
                        .getColumnIndex(PHOTO_DATE))));
                item_photo.setPHOTO_NAME((c.getString(c
                        .getColumnIndex(PHOTO_NAME))));


                listphoto.add(item_photo);
            } while (c.moveToNext());
        }

        c.close();
        return listphoto;
    }

    public void deleteallPhotoQA(String batch , String cd) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_CRITERIA + " where "
                + BATCH_NUMBER_PHOTO_QA + " = " + batch + " AND "
                + STOCK_QA_CD_PHOTO_QA + " = " + cd);
    }
    // End Table Take Photo QA

    //Table O_CRITERIA để chứa dữ liệu quét exp lần đầu
    public static final String O_CRITERIA = "O_CRITERIA";
    public static final String PRODUCT_CODE_CRITERIA = "PRODUCT_CODE_CRITERIA";
    public static final String MIC_CODE = "MIC_CODE";
    public static final String MIC_DESC = "MIC_DESC";
    public static final String BATCH_NUMBER_CRITERIA = "BATCH_NUMBER_CRITERIA";
    public static final String NOTE_CRITERIA = "NOTE_CRITERIA";
    public static final String QTY_CRITERIA = "QTY_CRITERIA";
    public static final String UNIT_CRITERIA = "UNIT_CRITERIA";
    public static final String MATERIA_CD_CRITERIA = "MATERIA_CD_CRITERIA";


    public static final String CREATE_TABLE_O_CRITERIA = "CREATE TABLE "
            + O_CRITERIA + "("
            + PRODUCT_CODE_CRITERIA + " TEXT,"
            + MIC_CODE + " TEXT,"
            + MIC_DESC + " TEXT,"
            + QTY_CRITERIA + " TEXT,"
            + UNIT_CRITERIA + " TEXT,"
            + BATCH_NUMBER_CRITERIA + " TEXT,"
            + MATERIA_CD_CRITERIA + " TEXT,"
            + NOTE_CRITERIA + " TEXT" + ")";

    public long CreateCriteria(Product_Criteria criteria) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        //values.put(QRCODE, qrcode.getQRCODE());
        values.put(PRODUCT_CODE_CRITERIA, criteria.getPRODUCT_CODE());
        values.put(MIC_CODE, criteria.getMIC_CODE());
        values.put(MIC_DESC, criteria.getMIC_DESC());
        values.put(QTY_CRITERIA, criteria.getQTY());
        values.put(UNIT_CRITERIA, criteria.getUNIT());
        values.put(BATCH_NUMBER_CRITERIA, criteria.getBATCH_NUMBER());
        values.put(MATERIA_CD_CRITERIA, criteria.getMATERIA_CD());
        values.put(NOTE_CRITERIA, criteria.getNOTE());
        // insert row
        long id = db.insert(O_CRITERIA, null, values);
        return id;
    }

    public ArrayList<Product_Criteria>
    getallCriteria(String cd ,String batch , String product_code , String unit) {
        ArrayList<Product_Criteria> listcriteria = new ArrayList<Product_Criteria>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT * FROM " + O_CRITERIA + " where " + BATCH_NUMBER_CRITERIA + " = '" + batch + "' AND "
                + PRODUCT_CODE_CRITERIA + " = " + product_code + " AND "
                + UNIT_CRITERIA + " = '" + unit + "' AND "
                + MATERIA_CD_CRITERIA + " = " + cd ;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_Criteria item_criteria = new Product_Criteria();
                item_criteria.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_CRITERIA))));
                item_criteria.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_CRITERIA))));
                item_criteria.setMIC_CODE((c.getString(c
                        .getColumnIndex(MIC_CODE))));
                item_criteria.setMIC_DESC((c.getString(c
                        .getColumnIndex(MIC_DESC))));
                item_criteria.setUNIT((c.getString(c
                        .getColumnIndex(UNIT_CRITERIA))));
                item_criteria.setQTY((c.getString(c
                        .getColumnIndex(QTY_CRITERIA))));
                item_criteria.setNOTE((c.getString(c
                        .getColumnIndex(NOTE_CRITERIA))));
                item_criteria.setMATERIA_CD((c.getString(c
                        .getColumnIndex(MATERIA_CD_CRITERIA))));

                listcriteria.add(item_criteria);
            } while (c.moveToNext());
        }

        c.close();
        return listcriteria;
    }


    public int updateunit_Criteria(String mic_code , String batch, String qty , String cd , String product_code, String unit) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(QTY_CRITERIA, qty);

        // updating row
        return db.update(O_CRITERIA, values,  MIC_CODE + " = ? AND " + BATCH_NUMBER_CRITERIA + " = ? AND "
                        + MATERIA_CD_CRITERIA + " = ? AND "
                        + UNIT_CRITERIA + " = ? AND "
                        + PRODUCT_CODE_CRITERIA + " = ? ",
                new String[]{String.valueOf(mic_code) , String.valueOf(batch), String.valueOf(cd), String.valueOf(unit), String.valueOf(product_code)});

    }

    public int updatenote_Criteria(String mic_code , String batch, String note ,String cd , String product_code , String unit) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(NOTE_CRITERIA, note);

        // updating row
        return db.update(O_CRITERIA, values,  MIC_CODE + " = ? AND " + BATCH_NUMBER_CRITERIA + " = ? AND "
                        + MATERIA_CD_CRITERIA + " = ? AND "
                        + UNIT_CRITERIA + " = ? AND "
                        + PRODUCT_CODE_CRITERIA + " = ? ",
                new String[]{String.valueOf(mic_code) , String.valueOf(batch), String.valueOf(cd), String.valueOf(unit), String.valueOf(product_code)});

    }


    public void deleterowCriteria(String batch , String cd) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_CRITERIA + " where "
                + BATCH_NUMBER_CRITERIA + " = '" + batch + "' AND "
                + MATERIA_CD_CRITERIA + " = " + cd);
    }
    public void deleteallCriteria( String cd) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_CRITERIA + " where " + MATERIA_CD_CRITERIA + " = " + cd);
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
    public static final String BATCH_NUMBER_CODE = "BATCH_NUMBER_CODE";
    public static final String CREATE_TIME = "CREATE_TIME";


    public static final String CREATE_TABLE_O_QRCODE = "CREATE TABLE "
            + O_QRCODE + "("
            + AUTOINCREMENT_PO + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + STOCK_RECEIPT_CD + " TEXT,"
            + MANUFACTURING_DATE_WST + " TEXT,"
            + PRODUCT_CODE + " TEXT,"
            + PRODUCT_CD + " TEXT,"
            + CREATE_TIME + " TEXT,"
            + PRODUCT_NAME + " TEXT,"
            + WAREHOUSE_POSITION_CD + " TEXT,"
            + EXPIRED_DATE + " TEXT,"
            + SET_UNIT + " TEXT,"
            + EA_UNIT + " TEXT,"
            + POSITION_FROM + " TEXT,"
            + POSITION_TO + " TEXT,"
            + POSITION_CODE + " TEXT,"
            + POSITION_DESCRIPTION + " TEXT,"
            + STOCKIN_DATE + " TEXT,"
            + BATCH_NUMBER_CODE + " TEXT"
            + ")";


    public long CreateProduct_Qrcode(Product_Qrcode qrcode) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
//        values.put(AUTOINCREMENT_PO, qrcode.getAUTOINCREMENT());
        values.put(PRODUCT_CD, qrcode.getPRODUCT_CD());
        values.put(MANUFACTURING_DATE_WST, qrcode.getMANUFACTURING_DATE());
        values.put(STOCK_RECEIPT_CD, qrcode.getSTOCK_RECEIPT_CD());
        values.put(CREATE_TIME, qrcode.getCREATE_TIME());
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
        values.put(BATCH_NUMBER_CODE, qrcode.getBATCH_NUMBER());
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
        values.put(BATCH_NUMBER_CODE, qrcode.getBATCH_NUMBER());
        // updating row
        return db.update(O_QRCODE, values, "PRODUCT_CD" + " = ?" + " AND STOCK_RECEIPT_CD = ? AND EXPIRED_DATE = ? AND STOCKIN_DATE = ?  AND EA_UNIT = ?",
                new String[]{String.valueOf(PRODUCT_CD), String.valueOf(stock), String.valueOf(exp_date),String.valueOf(stockindate) ,String.valueOf(ea_unit)});

    }

    public int updatePositionFrom_LPN(String id_unique_PAW , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(LPN_CD_PUTAWAY, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION, descreption);
        values.put(POSITION_FROM_CODE, from);
        values.put(LPN_FROM_PUTAWAY, from);

        // updating row
        return db.update(O_PUT_AWAY, values,
                AUTOINCREMENT_PUT_AWAY + " = ?",
                new String[]{String.valueOf(id_unique_PAW)});

    }


    public int updatePositionFrom(String id_unique_PAW , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_PUTAWAY, wareHouse);
        values.put(POSITION_FROM_CODE, from);
        values.put(LPN_FROM_PUTAWAY, "");
        values.put(POSITION_FROM_DESCRIPTION, descreption);

        // updating row
        return db.update(O_PUT_AWAY, values,  AUTOINCREMENT_PUT_AWAY + " = ?",
                new String[]{String.valueOf(id_unique_PAW)});

    }
    public int updatePositionFrom_StockTransfer_LPN(String unique_id , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_STOCK_TRANSFER, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_STOCK_TRANSFER, descreption);
        values.put(POSITION_FROM_CODE_STOCK_TRANSFER, from);

        values.put(LPN_FROM_STOCK_TRANSFER, from);

        // updating row
        return db.update(O_STOCK_TRANSFER, values,
                AUTOINCREMENT_STOCK_TRANSFER + " = ? ",
                new String[]{String.valueOf(unique_id)});
    }


    public int updatePositionFrom_StockTransfer(String unique_id , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_STOCK_TRANSFER, wareHouse);
        values.put(POSITION_FROM_CODE_STOCK_TRANSFER, from);
        values.put(LPN_FROM_STOCK_TRANSFER, "");
        values.put(POSITION_FROM_DESCRIPTION_STOCK_TRANSFER, descreption);

        // updating row
        return db.update(O_STOCK_TRANSFER, values,
                AUTOINCREMENT_STOCK_TRANSFER + " = ? ",
                new String[]{String.valueOf(unique_id)});
    }

    public int updatePositionTo_LPN(String id_unique_PAW , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_PUTAWAY, wareHouse);
        values.put(POSITION_TO_DESCRIPTION, descreption);
        values.put(LPN_TO_PUTAWAY, to);

        values.put(POSITION_TO_CODE_LOAD_PALLET, to);
        // updating row
        return db.update(O_PUT_AWAY, values,
                AUTOINCREMENT_PUT_AWAY + " = ? ",
                new String[]{String.valueOf(id_unique_PAW)});


    }

    public int updatePositionTo(String id_unique_PAW , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_PUTAWAY, wareHouse);
        values.put(POSITION_TO_CODE, to);
        values.put(LPN_TO_PUTAWAY, "");

        values.put(POSITION_TO_DESCRIPTION, descreption);
        // updating row
        return db.update(O_PUT_AWAY, values,
                AUTOINCREMENT_PUT_AWAY + " = ? ",
                new String[]{String.valueOf(id_unique_PAW)});

    }
    public int updatePositionTo_StockTransfer_LPN(String unique_id , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_STOCK_TRANSFER, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_STOCK_TRANSFER, descreption);
        values.put(LPN_TO_STOCK_TRANSFER, to);

        values.put(POSITION_TO_CODE_STOCK_TRANSFER, to);
        // updating row
        return db.update(O_STOCK_TRANSFER, values,
                AUTOINCREMENT_STOCK_TRANSFER + " = ?",
                new String[]{String.valueOf(unique_id)});


    }

    public int updatePositionTo_StockTransfer(String unique_id , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_STOCK_TRANSFER, wareHouse);
        values.put(POSITION_TO_CODE_STOCK_TRANSFER, to);
        values.put(LPN_TO_STOCK_TRANSFER, "");

        values.put(POSITION_TO_DESCRIPTION_STOCK_TRANSFER, descreption);
        // updating row
        return db.update(O_STOCK_TRANSFER, values,
                AUTOINCREMENT_STOCK_TRANSFER + " = ?",
                new String[]{String.valueOf(unique_id)});


    }


    public int updateProduct_Qrcode_SL(Product_Qrcode qrcode,String incre_si, String PRODUCT_CD, String stock, String sl, String ea_unit, String stocking_date) {
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
        values.put(BATCH_NUMBER_CODE, qrcode.getBATCH_NUMBER());
        values.put(SET_UNIT, sl);
        values.put(POSITION_FROM, qrcode.getPRODUCT_FROM());
        values.put(POSITION_TO, qrcode.getPRODUCT_TO());
        values.put(STOCKIN_DATE, qrcode.getSTOCKIN_DATE());

        Log.d("so luong : ", sl);
        // updating row
        return db.update(O_QRCODE, values, AUTOINCREMENT_PO + " = ?",
                new String[]{String.valueOf(incre_si)});


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
                qrcodeq.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME))));
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_CODE))));
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
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_CODE))));
                qrcodeq.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME))));
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
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_CODE))));
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
        String selectQuery = "SELECT MANUFACTURING_DATE_WST ,  CREATE_TIME , BATCH_NUMBER_CODE ,  WAREHOUSE_POSITION_CD, STOCK_RECEIPT_CD, PRODUCT_CD, PRODUCT_CODE, PRODUCT_NAME, SET_UNIT, POSITION_FROM, POSITION_TO" +
                ", POSITION_CODE, POSITION_DESCRIPTION, EA_UNIT , REPLACE(EXPIRED_DATE,'------','') as EXPIRED_DATE, REPLACE(STOCKIN_DATE,'------','') as STOCKIN_DATE FROM " + O_QRCODE + " " + " WHERE " + STOCK_RECEIPT_CD + " = " + stock;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Qrcode qrcodeq = new Product_Qrcode();
//                qrcodeq.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_PO))));
                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD))));
                qrcodeq.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_WST))));
                qrcodeq.setCREATE_TIME((c.getString(c
                        .getColumnIndex(CREATE_TIME))));
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
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_CODE))));
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
    public static final String SALE_QA_CD_PHOTO = "SALE_QA_CD_PHOTO";
    public static final String BATCH_NUMBER_PHOTO = "BATCH_NUMBER_PHOTO";
    public static final String PRODUCT_CODE_PHOTO = "PRODUCT_CODE_PHOTO";
    public static final String UNIT_PHOTO = "UNIT_PHOTO";
    public static final String EXPIRED_DATE_PHOTO = "EXPIRED_DATE_PHOTO";
    public static final String STOCKIN_DATE_PHOTO = "STOCKIN_DATE_PHOTO";
    public static final String WAREHOUSE_CONTAINER_CD_PHOTO = "WAREHOUSE_CONTAINER_CD_PHOTO";


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
            + SALE_QA_CD_PHOTO + " TEXT,"
            + BATCH_NUMBER_PHOTO + " TEXT,"
            + PRODUCT_CODE_PHOTO + " TEXT,"
            + UNIT_PHOTO + " TEXT,"
            + EXPIRED_DATE_PHOTO + " TEXT,"
            + STOCKIN_DATE_PHOTO + " TEXT,"
            + WAREHOUSE_CONTAINER_CD_PHOTO + " TEXT,"
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

    public List<OrderPhoto> getAllPhoto_QA(String cd  ) {

        List<OrderPhoto> files = new ArrayList<OrderPhoto>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_SALE_TAKE_PHOTO  + " Where " + SALE_QA_CD_PHOTO + " = " + cd ;

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                try {
                    OrderPhoto file = new OrderPhoto();
                    file.setSTOCK_QA_CD((c.getString(c
                            .getColumnIndex(SALE_QA_CD_PHOTO))));
                    file.setPhoto_Name((c.getString(c
                            .getColumnIndex(SALE_TAKES_PHOTO_FILE_NAME))));
                    file.setBATCH_NUMBER((c.getString(c
                            .getColumnIndex(BATCH_NUMBER_PHOTO))));
                    file.setPRODUCT_CODE((c.getString(c
                            .getColumnIndex(PRODUCT_CODE_PHOTO))));
                    file.setUNIT((c.getString(c
                            .getColumnIndex(UNIT_PHOTO))));
                    file.setPhoto_Path((c.getString(c
                            .getColumnIndex(SALE_TAKES_PHOTO_FULL_PATH_FILE))));
                    file.setSTOCK_IN_DATE((c.getString(c
                            .getColumnIndex(STOCKIN_DATE_PHOTO))));
                    file.setEXPIRED_DATE((c.getString(c
                            .getColumnIndex(EXPIRED_DATE_PHOTO))));

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

    public List<OrderPhoto> getAllPhotoForContainers(String cd ) {

        List<OrderPhoto> files = new ArrayList<OrderPhoto>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_SALE_TAKE_PHOTO  + " Where " + WAREHOUSE_CONTAINER_CD_PHOTO + " = " + cd  ;

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                try {
                    OrderPhoto file = new OrderPhoto();
                    file.setWAREHOUSE_CONTAINER_CD((c.getString(c
                            .getColumnIndex(WAREHOUSE_CONTAINER_CD_PHOTO))));
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

    public List<OrderPhoto> getAllPhotoForQA(String cd , String batch , String product_code ,String unit) {

        List<OrderPhoto> files = new ArrayList<OrderPhoto>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_SALE_TAKE_PHOTO  + " Where " + SALE_QA_CD_PHOTO + " = " + cd + " AND "
                + BATCH_NUMBER_PHOTO + " = '" + batch + "' AND "
                + UNIT_PHOTO + " = '" + unit + "' AND "
                + PRODUCT_CODE_PHOTO + " = " + product_code ;

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                try {
                    OrderPhoto file = new OrderPhoto();
                    file.setSTOCK_QA_CD((c.getString(c
                            .getColumnIndex(SALE_QA_CD_PHOTO))));
                    file.setPhoto_Name((c.getString(c
                            .getColumnIndex(SALE_TAKES_PHOTO_FILE_NAME))));
                    file.setBATCH_NUMBER((c.getString(c
                            .getColumnIndex(BATCH_NUMBER_PHOTO))));
                    file.setPRODUCT_CODE((c.getString(c
                            .getColumnIndex(PRODUCT_CODE_PHOTO))));
                    file.setUNIT((c.getString(c
                            .getColumnIndex(UNIT_PHOTO))));
                    file.setEXPIRED_DATE((c.getString(c
                            .getColumnIndex(EXPIRED_DATE_PHOTO))));
                    file.setSTOCK_IN_DATE((c.getString(c
                            .getColumnIndex(STOCKIN_DATE_PHOTO))));
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

    public long createdSaleTakesPhoto(String orderCD, OrderPhoto files ,String product, String batch , String unit , String exp , String stockin) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(SALE_ORDER_CD, orderCD);
        values.put(SALE_QA_CD_PHOTO, orderCD);
        values.put(WAREHOUSE_CONTAINER_CD_PHOTO, orderCD);

        values.put(PRODUCT_CODE_PHOTO, product);
        values.put(BATCH_NUMBER_PHOTO, batch);
        values.put(UNIT_PHOTO, unit);
        values.put(EXPIRED_DATE_PHOTO, exp);
        values.put(STOCKIN_DATE_PHOTO, stockin);

        values.put(SALE_TAKES_PHOTO_FILE_NAME, files.getPhoto_Name());
        values.put(SALE_TAKES_PHOTO_CREATED_DATE,
                files.getStrDateTakesPhoto());
        values.put(SALE_TAKES_PHOTO_FULL_PATH_FILE, files.getPhoto_Path());

        // insert row
        long id = db.insert(O_SALE_TAKE_PHOTO, null, values);
        return id;
    }


    public int updatePositionFrom_LoadPallet_LPN(String unique_id ,String warehouse , String from, String wareHouse_lpn, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_LOAD_PALLET, warehouse);
        values.put(POSITION_FROM_DESCRIPTION_LOAD_PALLET, descreption);
        // khi quét vị trí là LPN thì phải cập nhật POSITION_FROM_CODE luôn ,
        // khi đó khi đồng bộ hàm isNotScanFromOrTo ms chạy đúng
        values.put(POSITION_FROM_CODE_LOAD_PALLET, from);

        values.put(LPN_FROM_LOAD_PALLET, from);

        // updating row
        return db.update(O_LOAD_PALLET, values,
                AUTOINCREMENT_LOAD_PALLET + " = ? ",
                new String[]{String.valueOf(unique_id)});
    }

    public int updatePositionFrom_LoadPallet(String unique_id,String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_LOAD_PALLET, wareHouse);
        values.put(POSITION_FROM_CODE_LOAD_PALLET, from);
        values.put(LPN_FROM_LOAD_PALLET, "");
        values.put(POSITION_FROM_DESCRIPTION_LOAD_PALLET, descreption);

        // updating row
        return db.update(O_LOAD_PALLET, values,
                AUTOINCREMENT_LOAD_PALLET + " = ? ",
                new String[]{String.valueOf(unique_id)});
    }

    public int updatePositionTo_LoadPallet_LPN(String unique_id ,String wareHouse, String to, String wareHouse_lpn, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_LOAD_PALLET, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_LOAD_PALLET, descreption);
        values.put(LPN_TO_LOAD_PALLET, to);
        values.put(POSITION_TO_CODE_LOAD_PALLET, to);
        // updating row
        return db.update(O_LOAD_PALLET, values,
                AUTOINCREMENT_LOAD_PALLET + " = ?",
                new String[]{String.valueOf(unique_id)});
    }

    public int updatePositionTo_LoadPallet(String unique_id,String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_LOAD_PALLET, wareHouse);
        values.put(POSITION_TO_CODE_LOAD_PALLET, to);
        values.put(LPN_TO_LOAD_PALLET, "");

        values.put(POSITION_TO_DESCRIPTION_LOAD_PALLET, descreption);
        // updating row
        return db.update(O_LOAD_PALLET, values,
                STOCKIN_DATE_LOAD_PALLET + " = ?",
                new String[]{String.valueOf(unique_id)});
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
    public static final String BATCH_NUMBER_INVENTORY = "BATCH_NUMBER_INVENTORY";

    public static final String CREATE_TABLE_O_INVENTORY = "CREATE TABLE "
            + O_INVENTORY + "("
            + AUTOINCREMENT_INVENTORY + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + UNIQUE_CODE_INVENTORY + " TEXT,"
            + PRODUCT_CODE_INVENTORY + " TEXT,"
            + PRODUCT_NAME_INVENTORY + " TEXT,"
            + QTY_SET_AVAILABLE_INVENTORY + " TEXT,"
            + STOCKIN_DATE_INVENTORY + " TEXT,"
            + PRODUCT_CD_INVENTORY + " TEXT,"
            + BATCH_NUMBER_INVENTORY + " TEXT,"
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
//        values.put(AUTOINCREMENT_INVENTORY, qrcode.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_INVENTORY, qrcode.getUNIQUE_CODE());
        values.put(PRODUCT_CODE_INVENTORY, qrcode.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_INVENTORY, qrcode.getPRODUCT_NAME());
        values.put(BATCH_NUMBER_INVENTORY, qrcode.getBATCH_NUMBER());
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
    getautoProduct_Inventory() {
        ArrayList<InventoryProduct> inventory = new ArrayList<InventoryProduct>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_INVENTORY + " ORDER BY " + AUTOINCREMENT_INVENTORY + " DESC LIMIT 1 ";;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                InventoryProduct inventoryProduct = new InventoryProduct();
                inventoryProduct.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_INVENTORY))));
                inventory.add(inventoryProduct);
            } while (c.moveToNext());
        }

        c.close();
        return inventory;
    }

    public ArrayList<InventoryProduct>
    getposition_Inventory() {
        ArrayList<InventoryProduct> qrcode = new ArrayList<InventoryProduct>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_INVENTORY + " ORDER BY " + AUTOINCREMENT_INVENTORY + " DESC LIMIT 1 ";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                InventoryProduct inventoryProduct = new InventoryProduct();

                inventoryProduct.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_INVENTORY))));

                inventoryProduct.setPOSITION_FROM_CD(c.getString(c
                        .getColumnIndex(POSITION_FROM_INVENTORY)));

                inventoryProduct.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_INVENTORY))));

                inventoryProduct.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_INVENTORY))));


                qrcode.add(inventoryProduct);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }

    public ArrayList<InventoryProduct>
    getoneProduct_Inventory(String CD, String expDate, String ea_unit, String INVENTORYCD, String stockindate , String vitritu,String batch_number) {
        ArrayList<InventoryProduct> qrcode = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_INVENTORY + " " + " WHERE "
                + PRODUCT_CD_INVENTORY + " = " + CD + " AND "
                + BATCH_NUMBER_INVENTORY + " like " + " '%" + batch_number + "%'" + " AND "
                + EA_UNIT_INVENTORY + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_INVENTORY + " like " + " '%" + expDate + "%'" + " AND "
                + STOCK_TAKE_CD + " = " + INVENTORYCD + " AND "
                + POSITION_FROM_CODE_INVENTORY + " like " + " '%" + vitritu + "%'" + " AND "
                + STOCKIN_DATE_INVENTORY + " like " + " '%" + stockindate + "%'";

        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                InventoryProduct qrcodeq = new InventoryProduct();
                qrcodeq.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_INVENTORY))));
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_INVENTORY))));
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
    getAllProduct_Inventory(String inventory) {
        ArrayList<InventoryProduct> INVENTORY = new ArrayList<>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_INVENTORY + " WHERE " + STOCK_TAKE_CD + " = " + inventory;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                InventoryProduct product = new InventoryProduct();
                product.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_INVENTORY))));
                product.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_INVENTORY))));
                product.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_INVENTORY))));
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
                product.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_INVENTORY))));
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
                product.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_INVENTORY))));
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

    public int updateProduct_Inventory(InventoryProduct INVENTORY, String incre_iv,String PRODUCT_CD, String sl, String ea_unit, String stock, String INVENTORYCD) {

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
        return db.update(O_INVENTORY, values,  AUTOINCREMENT_INVENTORY + " = ?",
                new String[]{String.valueOf(incre_iv)});

    }

    public void deleteProduct_Inventory_CD(String INVENTORYCD) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_INVENTORY + " WHERE " + STOCK_TAKE_CD + " = " + INVENTORYCD);
    }
    public int updatePositionFrom_Inventory_LPN(String id_unique_IVT , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_INVENTORY, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_INVENTORY, descreption);

        values.put(POSITION_FROM_CODE_INVENTORY, from);
        values.put(LPN_FROM_INVENTORY, from);

        // updating row
        return db.update(O_INVENTORY, values, AUTOINCREMENT_INVENTORY + " = ? ",
                new String[]{String.valueOf(id_unique_IVT)});

    }

    public int updatePositionFrom_Inventory(String id_unique_IVT , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_INVENTORY, wareHouse);
        values.put(POSITION_FROM_CODE_INVENTORY, from);
        values.put(LPN_FROM_INVENTORY, "");
        values.put(POSITION_FROM_DESCRIPTION_INVENTORY, descreption);

        // updating row
        return db.update(O_INVENTORY, values, AUTOINCREMENT_INVENTORY + " = ? ",
                new String[]{String.valueOf(id_unique_IVT)});

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

    public int updatePositionTo_Stockin(String id_unique_SI , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String description, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO, wareHouse);
        values.put(POSITION_CODE, to);
        values.put(POSITION_DESCRIPTION, description);

        // updating row
        try {
            return db.update(O_QRCODE, values,
                    AUTOINCREMENT_PO + " = ?",
                    new String[]{String.valueOf(id_unique_SI)});
        }catch (Exception e){
            Log.d("update vi tri nhap kho", e.getMessage());
        }
        return -1;
    }

    //DATABASE O_TRANSFER_UNIT
    public static final String O_TRANSFER_UNIT = "O_TRANSFER_UNIT";
    public static final String AUTOINCREMENT_TRANSFER_UNIT = "AUTOINCREMENT_TRANSFER_UNIT";
    public static final String SUGGESTION_POSITION_TRANSFER_UNIT_TO = "SUGGESTION_POSITION_TO";
    public static final String PRODUCT_CODE_TRANSFER_UNIT = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_TRANSFER_UNIT = "PRODUCT_NAME";
    public static final String PRODUCT_CD_TRANSFER_UNIT = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_TRANSFER_UNIT = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_TRANSFER_UNIT = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_TRANSFER_UNIT = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_TRANSFER_UNIT = "STOCKIN_DATE";
    public static final String EA_UNIT_TRANSFER_UNIT = "EA_UNIT";
    public static final String POSITION_FROM_TRANSFER_UNIT = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_TRANSFER_UNIT = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_TRANSFER_UNIT = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_TRANSFER_UNIT = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_TRANSFER_UNIT = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_TRANSFER_UNIT = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_TRANSFER_UNIT = "UNIQUE_CODE";
    public static final String LPN_CD_TRANSFER_UNIT = "LPN_CD_TRANSFER_UNIT";
    public static final String LPN_CODE_TRANSFER_UNIT = "LPN_CODE_TRANSFER_UNIT";
    public static final String LPN_FROM_TRANSFER_UNIT = "LPN_FROM_TRANSFER_UNIT";
    public static final String LPN_TO_TRANSFER_UNIT = "LPN_TO_TRANSFER_UNIT";
    public static final String SUGGESTION_POSITION_TRANSFER_UNIT = "SUGGESTION_POSITION";
    public static final String UNIT_CHANGE_TO = "UNIT_CHANGE_TO";
    public static final String BARCODE = "BARCODE";
    public static final String BATCH_NUMBER_TRANSFER_UNIT = "BATCH_NUMBER_TRANSFER_UNIT";

    public static final String CREATE_TABLE_O_TRANSFER_UNIT = "CREATE TABLE "
            + O_TRANSFER_UNIT + "("
            + AUTOINCREMENT_TRANSFER_UNIT + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + BATCH_NUMBER_TRANSFER_UNIT + " TEXT,"
            + PRODUCT_CD_TRANSFER_UNIT + " TEXT,"
            + PRODUCT_NAME_TRANSFER_UNIT + " TEXT,"
            + PRODUCT_CODE_TRANSFER_UNIT + " TEXT,"
            + QTY_EA_AVAILABLE_TRANSFER_UNIT + " TEXT,"
            + QTY_SET_AVAILABLE_TRANSFER_UNIT + " TEXT,"
            + EXPIRED_DATE_TRANSFER_UNIT + " TEXT,"
            + STOCKIN_DATE_TRANSFER_UNIT + " TEXT,"
            + EA_UNIT_TRANSFER_UNIT + " TEXT,"
            + POSITION_FROM_TRANSFER_UNIT + " TEXT,"
            + POSITION_FROM_CODE_TRANSFER_UNIT + " TEXT,"
            + POSITION_FROM_DESCRIPTION_TRANSFER_UNIT + " TEXT,"
            + POSITION_TO_TRANSFER_UNIT + " TEXT,"
            + POSITION_TO_CODE_TRANSFER_UNIT + " TEXT,"
            + POSITION_TO_DESCRIPTION_TRANSFER_UNIT + " TEXT,"
            + UNIQUE_CODE_TRANSFER_UNIT + " TEXT ,"
            + LPN_CD_TRANSFER_UNIT + " TEXT ,"
            + LPN_CODE_TRANSFER_UNIT + " TEXT ,"
            + LPN_FROM_TRANSFER_UNIT + " TEXT ,"
            + LPN_TO_TRANSFER_UNIT + " TEXT ,"
            + UNIT_CHANGE_TO + " TEXT ,"
            + BARCODE + " TEXT ,"
            + SUGGESTION_POSITION_TRANSFER_UNIT + " TEXT ,"
            + SUGGESTION_POSITION_TRANSFER_UNIT_TO + " TEXT"
            + ")";


    public long CreateTransferUnit(TransferUnitProduct qrcode) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
//        values.put(AUTOINCREMENT_TRANSFER_UNIT, qrcode.getAUTOINCREMENT());
        values.put(UNIQUE_CODE_TRANSFER_UNIT, qrcode.getUNIT());
        values.put(BATCH_NUMBER_TRANSFER_UNIT, qrcode.getBATCH_NUMBER());
        values.put(PRODUCT_CODE_TRANSFER_UNIT, qrcode.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_TRANSFER_UNIT, qrcode.getPRODUCT_NAME());
        values.put(PRODUCT_CD_TRANSFER_UNIT, qrcode.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_TRANSFER_UNIT, qrcode.getQTY());
        values.put(STOCKIN_DATE_TRANSFER_UNIT, qrcode.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_TRANSFER_UNIT, qrcode.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_TRANSFER_UNIT, qrcode.getEXPIRED_DATE());
        values.put(EA_UNIT_TRANSFER_UNIT, qrcode.getUNIT());
        values.put(POSITION_FROM_TRANSFER_UNIT, qrcode.getPOSITION_FROM_CD());
        values.put(POSITION_TO_TRANSFER_UNIT, qrcode.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_TRANSFER_UNIT, qrcode.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_TRANSFER_UNIT, qrcode.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_TRANSFER_UNIT, qrcode.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_TRANSFER_UNIT, qrcode.getPOSITION_TO_DESCRIPTION());
        values.put(LPN_CODE_TRANSFER_UNIT, qrcode.getLPN_CODE());
        values.put(LPN_FROM_TRANSFER_UNIT, qrcode.getLPN_FROM());
        values.put(LPN_TO_TRANSFER_UNIT, qrcode.getLPN_TO());
        values.put(UNIT_CHANGE_TO, qrcode.getUNIT_CHANGE_TO());
        values.put(BARCODE, qrcode.getBARCODE());
        values.put(SUGGESTION_POSITION_TRANSFER_UNIT, qrcode.getSUGGESTION_POSITION());
        values.put(SUGGESTION_POSITION_TRANSFER_UNIT_TO, qrcode.getSUGGESTION_POSITION_TO());

        // insert row
        long id = db.insert(O_TRANSFER_UNIT, null, values);
        return id;
    }

    public ArrayList<TransferUnitProduct>
    getoneTransferUnitProduct(String CD, String expDate, String ea_unit, String stockinDate,String batch_number) {
        ArrayList<TransferUnitProduct> qrcode = new ArrayList<TransferUnitProduct>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_TRANSFER_UNIT + " " + " WHERE "
                + PRODUCT_CD_TRANSFER_UNIT + " = " + CD + " AND "
                + BATCH_NUMBER_TRANSFER_UNIT + " like " + " '%" + batch_number + "%'" + " AND "
                + EA_UNIT_TRANSFER_UNIT + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_TRANSFER_UNIT + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_TRANSFER_UNIT + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                TransferUnitProduct qrcodeq = new TransferUnitProduct();
                qrcodeq.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_TRANSFER_UNIT))));
                qrcodeq.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_TRANSFER_UNIT))));
                qrcodeq.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_TRANSFER_UNIT))));
                qrcodeq.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_TRANSFER_UNIT))));
                qrcodeq.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_TRANSFER_UNIT))));
                qrcodeq.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_TRANSFER_UNIT))));
                qrcodeq.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_TRANSFER_UNIT))));
                qrcodeq.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_TRANSFER_UNIT))));
                qrcodeq.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_TRANSFER_UNIT))));
                qrcodeq.setUNIT_CHANGE_TO((c.getString(c
                        .getColumnIndex(UNIT_CHANGE_TO))));
                qrcodeq.setBARCODE((c.getString(c
                        .getColumnIndex(BARCODE))));
                qrcodeq.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_TRANSFER_UNIT))));


                qrcode.add(qrcodeq);
            } while (c.moveToNext());
        }

        c.close();
        return qrcode;
    }



    public ArrayList<TransferUnitProduct>
    getAllTransferUnitProduct() {
        ArrayList<TransferUnitProduct> putaway = new ArrayList<TransferUnitProduct>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *  FROM " + O_TRANSFER_UNIT;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                TransferUnitProduct qrcode = new TransferUnitProduct();
                qrcode.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_TRANSFER_UNIT))));
                qrcode.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_TRANSFER_UNIT))));
                qrcode.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_TRANSFER_UNIT))));
                qrcode.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_TRANSFER_UNIT))));
                qrcode.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_TRANSFER_UNIT))));
                qrcode.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_TRANSFER_UNIT))));
                qrcode.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_TRANSFER_UNIT))));
                qrcode.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_TRANSFER_UNIT))));
                qrcode.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_TRANSFER_UNIT))));
                qrcode.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_TRANSFER_UNIT))));
                qrcode.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_TRANSFER_UNIT))));
                qrcode.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_TRANSFER_UNIT))));
                qrcode.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_TRANSFER_UNIT))));
                qrcode.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_TRANSFER_UNIT))));
                qrcode.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_TRANSFER_UNIT))));
                qrcode.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_TRANSFER_UNIT))));
                qrcode.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_TRANSFER_UNIT))));
                qrcode.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_TRANSFER_UNIT))));
                qrcode.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_TRANSFER_UNIT))));
                qrcode.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_TRANSFER_UNIT))));
                qrcode.setBARCODE((c.getString(c
                        .getColumnIndex(BARCODE))));
                qrcode.setUNIT_CHANGE_TO((c.getString(c
                        .getColumnIndex(UNIT_CHANGE_TO))));
                qrcode.setSUGGESTION_POSITION((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_TRANSFER_UNIT))));
                qrcode.setSUGGESTION_POSITION_TO((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_TRANSFER_UNIT_TO))));
                putaway.add(qrcode);
            } while (c.moveToNext());
        }

        c.close();
        return putaway;
    }


    public ArrayList<TransferUnitProduct>
    getAllProduct_TRANSFER_UNIT_Sync() {
        ArrayList<TransferUnitProduct> letdowns = new ArrayList<TransferUnitProduct>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_TRANSFER_UNIT;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                TransferUnitProduct qrcode = new TransferUnitProduct();
//                qrcode.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_TRANSFER_UNIT))));
                qrcode.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_TRANSFER_UNIT))));
                qrcode.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_TRANSFER_UNIT))));
                qrcode.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_TRANSFER_UNIT))));
                qrcode.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_TRANSFER_UNIT))));
                qrcode.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_TRANSFER_UNIT))));
                qrcode.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_TRANSFER_UNIT))));
                qrcode.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_TRANSFER_UNIT))));
                qrcode.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_TRANSFER_UNIT))));
                qrcode.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_TRANSFER_UNIT))));
                qrcode.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_TRANSFER_UNIT))));
                qrcode.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_TRANSFER_UNIT))));
                qrcode.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_TRANSFER_UNIT))));
                qrcode.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_TRANSFER_UNIT))));
                qrcode.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_TRANSFER_UNIT))));
                qrcode.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_TRANSFER_UNIT))));
                qrcode.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_TRANSFER_UNIT))));
                qrcode.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_TRANSFER_UNIT))));
                qrcode.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_TRANSFER_UNIT))));
                qrcode.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_TRANSFER_UNIT))));
                qrcode.setUNIT_CHANGE_TO((c.getString(c
                        .getColumnIndex(UNIT_CHANGE_TO))));
                qrcode.setBARCODE((c.getString(c
                        .getColumnIndex(BARCODE))));
                qrcode.setSUGGESTION_POSITION((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_TRANSFER_UNIT))));
                qrcode.setSUGGESTION_POSITION_TO((c.getString(c
                        .getColumnIndex(SUGGESTION_POSITION_TRANSFER_UNIT_TO))));
                letdowns.add(qrcode);
            } while (c.moveToNext());
        }
        c.close();
        return letdowns;
    }

    public int updateProduct_TRANSFER_UNIT(TransferUnitProduct letdown, String incre_ld, String PRODUCT_CD, String sl, String ea_unit, String stock) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_TRANSFER_UNIT, PRODUCT_CD);
        values.put(PRODUCT_CODE_TRANSFER_UNIT, letdown.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_TRANSFER_UNIT, letdown.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_TRANSFER_UNIT, letdown.getEXPIRED_DATE());
        values.put(EA_UNIT_TRANSFER_UNIT, letdown.getUNIT());
        values.put(QTY_SET_AVAILABLE_TRANSFER_UNIT, sl);

        // updating row
        return db.update(O_TRANSFER_UNIT, values,  AUTOINCREMENT_TRANSFER_UNIT + " = ?",
                new String[]{String.valueOf(incre_ld)});

    }


    public void deleteProduct_TRANSFER_UNIT_Specific(String productCode) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.delete(O_TRANSFER_UNIT, AUTOINCREMENT_TRANSFER_UNIT + " = ?"
                , new String[]{String.valueOf(productCode)});

    }

    public void deleteProduct_TRANSFER_UNIT() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_TRANSFER_UNIT);
    }

    public int updatePositionFromLetTransferUnit_LPN(String id_unique_LD , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_TRANSFER_UNIT, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_TRANSFER_UNIT, descreption);
        values.put(POSITION_FROM_CODE_TRANSFER_UNIT, from);
        values.put(LPN_FROM_TRANSFER_UNIT, from);

        // updating row
        return db.update(O_TRANSFER_UNIT, values,
                AUTOINCREMENT_TRANSFER_UNIT + " = ?",
                new String[]{String.valueOf(id_unique_LD)});

    }

    public int updatePositionFromTransferUnit(String id_unique_LD , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_TRANSFER_UNIT, wareHouse);
        values.put(POSITION_FROM_CODE_TRANSFER_UNIT, from);
        values.put(LPN_FROM_TRANSFER_UNIT, "");
        values.put(POSITION_FROM_DESCRIPTION_TRANSFER_UNIT, descreption);

        // updating row
        return db.update(O_TRANSFER_UNIT, values,  AUTOINCREMENT_TRANSFER_UNIT + " = ?",
                new String[]{String.valueOf(id_unique_LD)});

    }
    public int updatePositionToTransferUnit(String id_unique_TU, String unit) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(UNIT_CHANGE_TO, unit);


        // updating row
        return db.update(O_TRANSFER_UNIT, values,  AUTOINCREMENT_TRANSFER_UNIT + " = ?",
                new String[]{String.valueOf(id_unique_TU)});

    }


    //END TABLE O_TRANSFER_UNIT
    //DATABASE PUT transfer Posting
    public static final String O_TRANSFER_POSTING = "O_TRANSFER_POSTING";
    public static final String WAREHOUSE_POSITION_CD_TRANSFER_POSTING = "WAREHOUSE_POSITION_CD_TRANSFER_POSTING";
    public static final String AUTOINCREMENT_TRANSFER_POSTING = "AUTOINCREMENT_TRANSFER_POSTING";
    public static final String PRODUCT_CODE_TRANSFER_POSTING = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_TRANSFER_POSTING = "PRODUCT_NAME";
    public static final String PRODUCT_CD_TRANSFER_POSTING = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_TRANSFER_POSTING = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_TRANSFER_POSTING = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_TRANSFER_POSTING = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_TRANSFER_POSTING = "STOCKIN_DATE";
    public static final String EA_UNIT_TRANSFER_POSTING = "EA_UNIT";
    public static final String POSITION_FROM_TRANSFER_POSTING = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_TRANSFER_POSTING = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_TRANSFER_POSTING = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_TRANSFER_POSTING = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_TRANSFER_POSTING = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_TRANSFER_POSTING = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_TRANSFER_POSTING = "UNIQUE_CODE";
    public static final String TRANSFER_POSTING_CD = "TRANSFER_POSTING_CD";
    public static final String LPN_CD_TRANSFER_POSTING = "LPN_CD_TRANSFER_POSTING";
    public static final String LPN_CODE_TRANSFER_POSTING = "LPN_CODE_TRANSFER_POSTING";
    public static final String LPN_FROM_TRANSFER_POSTING = "LPN_FROM_TRANSFER_POSTING";
    public static final String LPN_TO_TRANSFER_POSTING = "LPN_TO_TRANSFER_POSTING";
    public static final String BATCH_NUMBER_TRANSFER_POSTING = "BATCH_NUMBER_TRANSFER_POSTING";
    public static final String MANUFACTURING_DATE_TRANSFER_POSTING = "MANUFACTURING_DATE_TRANSFER_POSTING";

    public static final String CREATE_TABLE_O_TRANSFER_POSTING = "CREATE TABLE "
            + O_TRANSFER_POSTING + "("
            + AUTOINCREMENT_TRANSFER_POSTING + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_TRANSFER_POSTING + " TEXT,"
            + WAREHOUSE_POSITION_CD_TRANSFER_POSTING + " TEXT,"
            + BATCH_NUMBER_TRANSFER_POSTING + " TEXT,"
            + MANUFACTURING_DATE_TRANSFER_POSTING + " TEXT,"
            + PRODUCT_NAME_TRANSFER_POSTING + " TEXT,"
            + PRODUCT_CODE_TRANSFER_POSTING + " TEXT,"
            + QTY_EA_AVAILABLE_TRANSFER_POSTING + " TEXT,"
            + QTY_SET_AVAILABLE_TRANSFER_POSTING + " TEXT,"
            + EXPIRED_DATE_TRANSFER_POSTING + " TEXT,"
            + STOCKIN_DATE_TRANSFER_POSTING + " TEXT,"
            + EA_UNIT_TRANSFER_POSTING + " TEXT,"
            + POSITION_FROM_TRANSFER_POSTING + " TEXT,"
            + POSITION_FROM_CODE_TRANSFER_POSTING + " TEXT,"
            + POSITION_FROM_DESCRIPTION_TRANSFER_POSTING + " TEXT,"
            + POSITION_TO_TRANSFER_POSTING + " TEXT,"
            + POSITION_TO_CODE_TRANSFER_POSTING + " TEXT,"
            + POSITION_TO_DESCRIPTION_TRANSFER_POSTING + " TEXT,"
            + TRANSFER_POSTING_CD + " TEXT,"
            + UNIQUE_CODE_TRANSFER_POSTING + " TEXT ,"
            + LPN_CD_TRANSFER_POSTING + " TEXT ,"
            + LPN_CODE_TRANSFER_POSTING + " TEXT ,"
            + LPN_FROM_TRANSFER_POSTING + " TEXT ,"
            + LPN_TO_TRANSFER_POSTING + " TEXT "
            + ")";


    public long CreateTransfer_Posting(Product_TransferPosting transferPosting) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
//        values.put(AUTOINCREMENT_TRANSFER_POSTING, transferPosting.getAUTOINCREMENT());

        values.put(UNIQUE_CODE_TRANSFER_POSTING, transferPosting.getUNIT());
        values.put(BATCH_NUMBER_TRANSFER_POSTING, transferPosting.getBATCH_NUMBER());
        values.put(MANUFACTURING_DATE_TRANSFER_POSTING, transferPosting.getMANUFACTURING_DATE());
        values.put(PRODUCT_CODE_TRANSFER_POSTING, transferPosting.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_TRANSFER_POSTING, transferPosting.getPRODUCT_NAME());
        values.put(WAREHOUSE_POSITION_CD_TRANSFER_POSTING, transferPosting.getWAREHOUSE_POSITION_CD());
        values.put(PRODUCT_CD_TRANSFER_POSTING, transferPosting.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_TRANSFER_POSTING, transferPosting.getQTY());
        values.put(STOCKIN_DATE_TRANSFER_POSTING, transferPosting.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_TRANSFER_POSTING, transferPosting.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_TRANSFER_POSTING, transferPosting.getEXPIRED_DATE());
        values.put(EA_UNIT_TRANSFER_POSTING, transferPosting.getUNIT());
        values.put(POSITION_FROM_TRANSFER_POSTING, transferPosting.getPOSITION_FROM_CD());
        values.put(POSITION_TO_TRANSFER_POSTING, transferPosting.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_TRANSFER_POSTING, transferPosting.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_TRANSFER_POSTING, transferPosting.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_TRANSFER_POSTING, transferPosting.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_TRANSFER_POSTING, transferPosting.getPOSITION_TO_DESCRIPTION());
        values.put(TRANSFER_POSTING_CD, transferPosting.getSTOCK_TRANSFER_POSTING_CD());
        values.put(LPN_CODE_TRANSFER_POSTING, transferPosting.getLPN_CODE());
        values.put(LPN_FROM_TRANSFER_POSTING, transferPosting.getLPN_FROM());
        values.put(LPN_TO_TRANSFER_POSTING, transferPosting.getLPN_TO());
        // insert row
        long id = db.insert(O_TRANSFER_POSTING, null, values);
        return id;
    }

    public int updatePositionFrom_transferPosting_LPN(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_TRANSFER_POSTING, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_TRANSFER_POSTING, descreption);

        values.put(POSITION_FROM_CODE_TRANSFER_POSTING, from);
        values.put(LPN_FROM_TRANSFER_POSTING, from);


        // updating row
        return db.update(O_TRANSFER_POSTING, values, AUTOINCREMENT_TRANSFER_POSTING + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});

    }

    public int updatePositionFrom_transferPosting(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_TRANSFER_POSTING, wareHouse);
        values.put(POSITION_FROM_CODE_TRANSFER_POSTING, from);
        values.put(LPN_FROM_TRANSFER_POSTING, "");
        values.put(POSITION_FROM_DESCRIPTION_TRANSFER_POSTING, descreption);


        // updating row
        return db.update(O_TRANSFER_POSTING, values, AUTOINCREMENT_TRANSFER_POSTING + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});

    }

    public int updatePositionTo_transferPosting_LPN(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_TRANSFER_POSTING, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_TRANSFER_POSTING, descreption);
        values.put(LPN_TO_TRANSFER_POSTING, to);

        values.put(POSITION_TO_CODE_TRANSFER_POSTING, to);
        // updating row
        return db.update(O_TRANSFER_POSTING, values,
                AUTOINCREMENT_TRANSFER_POSTING + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});


    }


    public int updatePositionTo_transferPosting(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_TRANSFER_POSTING, wareHouse);
        values.put(POSITION_TO_CODE_TRANSFER_POSTING, to);
        values.put(LPN_TO_TRANSFER_POSTING, "");

        values.put(POSITION_TO_DESCRIPTION_TRANSFER_POSTING, descreption);
        // updating row
        return db.update(O_TRANSFER_POSTING, values,
                AUTOINCREMENT_TRANSFER_POSTING + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});


    }


    public ArrayList<Product_TransferPosting>
    getoneProduct_TransferPosting(String CD, String expDate, String ea_unit, String stockinDate, String po_return,String batch_number) {
        ArrayList<Product_TransferPosting> transferPostings = new ArrayList<Product_TransferPosting>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_TRANSFER_POSTING + " " + " WHERE "
                + PRODUCT_CD_TRANSFER_POSTING + " = " + CD + " AND "
                + TRANSFER_POSTING_CD + " = " + po_return + " AND "
                + BATCH_NUMBER_TRANSFER_POSTING + " like " + " '%" + batch_number + "%'" + " AND "
                + EA_UNIT_TRANSFER_POSTING + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_TRANSFER_POSTING + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_TRANSFER_POSTING + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_TransferPosting transferPosting = new Product_TransferPosting();
                transferPosting.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_TRANSFER_POSTING))));
                transferPosting.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_TRANSFER_POSTING))));
                transferPosting.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_TRANSFER_POSTING))));
                transferPosting.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_TRANSFER_POSTING))));
                transferPosting.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_TRANSFER_POSTING))));
                transferPosting.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_TRANSFER_POSTING))));
                transferPosting.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_TRANSFER_POSTING))));
                transferPosting.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_TRANSFER_POSTING))));
                transferPosting.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_TRANSFER_POSTING))));
                transferPosting.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_TRANSFER_POSTING))));
                transferPosting.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_TRANSFER_POSTING))));
                transferPosting.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_TRANSFER_POSTING))));
                transferPostings.add(transferPosting);
            } while (c.moveToNext());
        }

        c.close();
        return transferPostings;
    }


    public ArrayList<Product_TransferPosting>
    getAllProduct_TransferPosting_Sync(String po_return) {
        ArrayList<Product_TransferPosting> transferPostings = new ArrayList<Product_TransferPosting>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , " +
                "REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, " +
                "REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_TRANSFER_POSTING +
                " where " + TRANSFER_POSTING_CD + " = " + po_return;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_TransferPosting transferPosting = new Product_TransferPosting();
//                transferPosting.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_TRANSFER_POSTING))));
                transferPosting.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_TRANSFER_POSTING))));
                transferPosting.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_TRANSFER_POSTING))));
                transferPosting.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_TRANSFER_POSTING))));
                transferPosting.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_TRANSFER_POSTING))));
                transferPosting.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_TRANSFER_POSTING))));
                transferPosting.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_TRANSFER_POSTING))));
                transferPosting.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_TRANSFER_POSTING))));
                transferPosting.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_TRANSFER_POSTING))));
                transferPosting.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_TRANSFER_POSTING))));
                transferPosting.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_TRANSFER_POSTING))));
                transferPosting.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_TRANSFER_POSTING))));
                transferPosting.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_TRANSFER_POSTING))));
                transferPosting.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_TRANSFER_POSTING))));
                transferPosting.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_TRANSFER_POSTING))));
                transferPosting.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_TRANSFER_POSTING))));
                transferPosting.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_TRANSFER_POSTING))));
                transferPosting.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_TRANSFER_POSTING))));
                transferPosting.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_TRANSFER_POSTING))));
                transferPosting.setSTOCK_TRANSFER_POSTING_CD((c.getString(c
                        .getColumnIndex(TRANSFER_POSTING_CD))));
                transferPosting.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_TRANSFER_POSTING))));
                transferPosting.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_TRANSFER_POSTING))));
                transferPosting.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_TRANSFER_POSTING))));
                transferPostings.add(transferPosting);
            } while (c.moveToNext());
        }

        c.close();
        return transferPostings;
    }

    public ArrayList<Product_TransferPosting>
    getAllProduct_TransferPosting(String po_return) {
        ArrayList<Product_TransferPosting> transferPostings = new ArrayList<Product_TransferPosting>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_TRANSFER_POSTING + " where " + TRANSFER_POSTING_CD + " = " + po_return;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_TransferPosting transferPosting = new Product_TransferPosting();
                transferPosting.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_TRANSFER_POSTING))));
                transferPosting.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_TRANSFER_POSTING))));
                transferPosting.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_TRANSFER_POSTING))));
                transferPosting.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_TRANSFER_POSTING))));
                transferPosting.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_TRANSFER_POSTING))));
                transferPosting.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_TRANSFER_POSTING))));
                transferPosting.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_TRANSFER_POSTING))));
                transferPosting.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_TRANSFER_POSTING))));
                transferPosting.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_TRANSFER_POSTING))));
                transferPosting.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_TRANSFER_POSTING))));
                transferPosting.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_TRANSFER_POSTING))));
                transferPosting.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_TRANSFER_POSTING))));
                transferPosting.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_TRANSFER_POSTING))));
                transferPosting.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_TRANSFER_POSTING))));
                transferPosting.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_TRANSFER_POSTING))));
                transferPosting.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_TRANSFER_POSTING))));
                transferPosting.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_TRANSFER_POSTING))));
                transferPosting.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_TRANSFER_POSTING))));
                transferPosting.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_TRANSFER_POSTING))));
                transferPosting.setSTOCK_TRANSFER_POSTING_CD((c.getString(c
                        .getColumnIndex(TRANSFER_POSTING_CD))));
                transferPosting.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_TRANSFER_POSTING))));
                transferPosting.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_TRANSFER_POSTING))));
                transferPosting.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_TRANSFER_POSTING))));
                transferPostings.add(transferPosting);
            } while (c.moveToNext());
        }

        c.close();
        return transferPostings;
    }


    public int updateProduct_TransferPosting(Product_TransferPosting transferPosting, String incre_so, String PRODUCT_CD, String sl, String ea_unit, String stock, String po_return) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_TRANSFER_POSTING, PRODUCT_CD);
        values.put(PRODUCT_CODE_TRANSFER_POSTING, transferPosting.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_TRANSFER_POSTING, transferPosting.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_TRANSFER_POSTING, transferPosting.getEXPIRED_DATE());
        values.put(EA_UNIT_TRANSFER_POSTING, transferPosting.getUNIT());
        values.put(QTY_SET_AVAILABLE_TRANSFER_POSTING, sl);
        values.put(TRANSFER_POSTING_CD, po_return);

        // updating row
        return db.update(O_TRANSFER_POSTING, values,  AUTOINCREMENT_TRANSFER_POSTING + " = ?",
                new String[]{String.valueOf(incre_so)});

    }


    public void deleteProduct_TransferPosting( String cd) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_TRANSFER_POSTING);
    }

    //END TABLE O_TRANSFER_POSTING


//    //DATABASE PUT Chuyen Ma
//    public static final String O_CHUYEN_MA = "O_CHUYEN_MA";
//    public static final String WAREHOUSE_POSITION_CD_CHUYEN_MA = "WAREHOUSE_POSITION_CD_CHUYEN_MA";
//    public static final String AUTOINCREMENT_CHUYEN_MA = "AUTOINCREMENT_CHUYEN_MA";
//    public static final String PRODUCT_CODE_CHUYEN_MA = "PRODUCT_CODE";
//    public static final String PRODUCT_NAME_CHUYEN_MA = "PRODUCT_NAME";
//    public static final String PRODUCT_CD_CHUYEN_MA = "PRODUCT_CD";
//    public static final String QTY_EA_AVAILABLE_CHUYEN_MA = "QTY_EA_AVAILABLE";
//    public static final String QTY_SET_AVAILABLE_CHUYEN_MA = "QTY_SET_AVAILABLE";
//    public static final String EXPIRED_DATE_CHUYEN_MA = "EXPIRY_DATE";
//    public static final String STOCKIN_DATE_CHUYEN_MA = "STOCKIN_DATE";
//    public static final String EA_UNIT_CHUYEN_MA = "EA_UNIT";
//    public static final String POSITION_FROM_CHUYEN_MA = "POSITION_FROM_CD";
//    public static final String POSITION_FROM_CODE_CHUYEN_MA = "POSITION_FROM_CODE";
//    public static final String POSITION_FROM_DESCRIPTION_CHUYEN_MA = "POSITION_FROM_DESCRIPTION";
//    public static final String POSITION_TO_CHUYEN_MA = "POSITION_TO_CD";
//    public static final String POSITION_TO_CODE_CHUYEN_MA = "POSITION_TO_CODE";
//    public static final String POSITION_TO_DESCRIPTION_CHUYEN_MA = "POSITION_TO_DESCRIPTION";
//    public static final String UNIQUE_CODE_CHUYEN_MA = "UNIQUE_CODE";
//    public static final String TRANSFER_POSTING_CD = "TRANSFER_POSTING_CD";
//    public static final String LPN_CD_CHUYEN_MA = "LPN_CD_CHUYEN_MA";
//    public static final String LPN_CODE_CHUYEN_MA = "LPN_CODE_CHUYEN_MA";
//    public static final String LPN_FROM_CHUYEN_MA = "LPN_FROM_CHUYEN_MA";
//    public static final String LPN_TO_CHUYEN_MA = "LPN_TO_CHUYEN_MA";
//    public static final String BATCH_NUMBER_CHUYEN_MA = "BATCH_NUMBER_CHUYEN_MA";
//    public static final String MANUFACTURING_DATE_CHUYEN_MA = "MANUFACTURING_DATE_CHUYEN_MA";
//
//    public static final String CREATE_TABLE_O_CHUYEN_MA = "CREATE TABLE "
//            + O_CHUYEN_MA + "("
//            + AUTOINCREMENT_CHUYEN_MA + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
//            + PRODUCT_CD_CHUYEN_MA + " TEXT,"
//            + WAREHOUSE_POSITION_CD_CHUYEN_MA + " TEXT,"
//            + BATCH_NUMBER_CHUYEN_MA + " TEXT,"
//            + MANUFACTURING_DATE_CHUYEN_MA + " TEXT,"
//            + PRODUCT_NAME_CHUYEN_MA + " TEXT,"
//            + PRODUCT_CODE_CHUYEN_MA + " TEXT,"
//            + QTY_EA_AVAILABLE_CHUYEN_MA + " TEXT,"
//            + QTY_SET_AVAILABLE_CHUYEN_MA + " TEXT,"
//            + EXPIRED_DATE_CHUYEN_MA + " TEXT,"
//            + STOCKIN_DATE_CHUYEN_MA + " TEXT,"
//            + EA_UNIT_CHUYEN_MA + " TEXT,"
//            + POSITION_FROM_CHUYEN_MA + " TEXT,"
//            + POSITION_FROM_CODE_CHUYEN_MA + " TEXT,"
//            + POSITION_FROM_DESCRIPTION_CHUYEN_MA + " TEXT,"
//            + POSITION_TO_CHUYEN_MA + " TEXT,"
//            + POSITION_TO_CODE_CHUYEN_MA + " TEXT,"
//            + POSITION_TO_DESCRIPTION_CHUYEN_MA + " TEXT,"
//            + TRANSFER_POSTING_CD + " TEXT,"
//            + UNIQUE_CODE_CHUYEN_MA + " TEXT ,"
//            + LPN_CD_CHUYEN_MA + " TEXT ,"
//            + LPN_CODE_CHUYEN_MA + " TEXT ,"
//            + LPN_FROM_CHUYEN_MA + " TEXT ,"
//            + LPN_TO_CHUYEN_MA + " TEXT "
//            + ")";
//
//
//    public long CreateChuyen_Ma(Product_ChuyenMa chuyenMa) {
//        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
//
//        ContentValues values = new ContentValues();
////        values.put(AUTOINCREMENT_CHUYEN_MA, chuyenMa.getAUTOINCREMENT());
//
//        values.put(UNIQUE_CODE_CHUYEN_MA, chuyenMa.getUNIT());
//        values.put(BATCH_NUMBER_CHUYEN_MA, chuyenMa.getBATCH_NUMBER());
//        values.put(MANUFACTURING_DATE_CHUYEN_MA, chuyenMa.getMANUFACTURING_DATE());
//        values.put(PRODUCT_CODE_CHUYEN_MA, chuyenMa.getPRODUCT_CODE());
//        values.put(PRODUCT_NAME_CHUYEN_MA, chuyenMa.getPRODUCT_NAME());
//        values.put(WAREHOUSE_POSITION_CD_CHUYEN_MA, chuyenMa.getWAREHOUSE_POSITION_CD());
//        values.put(PRODUCT_CD_CHUYEN_MA, chuyenMa.getPRODUCT_CD());
//        values.put(QTY_SET_AVAILABLE_CHUYEN_MA, chuyenMa.getQTY());
//        values.put(STOCKIN_DATE_CHUYEN_MA, chuyenMa.getSTOCKIN_DATE());
//        values.put(QTY_EA_AVAILABLE_CHUYEN_MA, chuyenMa.getQTY_EA_AVAILABLE());
//        values.put(EXPIRED_DATE_CHUYEN_MA, chuyenMa.getEXPIRED_DATE());
//        values.put(EA_UNIT_CHUYEN_MA, chuyenMa.getUNIT());
//        values.put(POSITION_FROM_CHUYEN_MA, chuyenMa.getPOSITION_FROM_CD());
//        values.put(POSITION_TO_CHUYEN_MA, chuyenMa.getPOSITION_TO_CD());
//        values.put(POSITION_FROM_CODE_CHUYEN_MA, chuyenMa.getPOSITION_FROM_CODE());
//        values.put(POSITION_TO_CODE_CHUYEN_MA, chuyenMa.getPOSITION_TO_CODE());
//        values.put(POSITION_FROM_DESCRIPTION_CHUYEN_MA, chuyenMa.getPOSITION_FROM_DESCRIPTION());
//        values.put(POSITION_TO_DESCRIPTION_CHUYEN_MA, chuyenMa.getPOSITION_TO_DESCRIPTION());
//        values.put(TRANSFER_POSTING_CD, chuyenMa.getSTOCK_CHUYEN_MA_CD());
//        values.put(LPN_CODE_CHUYEN_MA, chuyenMa.getLPN_CODE());
//        values.put(LPN_FROM_CHUYEN_MA, chuyenMa.getLPN_FROM());
//        values.put(LPN_TO_CHUYEN_MA, chuyenMa.getLPN_TO());
//        // insert row
//        long id = db.insert(O_CHUYEN_MA, null, values);
//        return id;
//    }
//
//    public int updatePositionFrom_chuyenMa_LPN(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
//        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
//
//        ContentValues values = new ContentValues();
//
//        values.put(POSITION_FROM_CHUYEN_MA, wareHouse);
//        values.put(POSITION_FROM_DESCRIPTION_CHUYEN_MA, descreption);
//
//        values.put(POSITION_FROM_CODE_CHUYEN_MA, from);
//        values.put(LPN_FROM_CHUYEN_MA, from);
//
//
//        // updating row
//        return db.update(O_CHUYEN_MA, values, AUTOINCREMENT_CHUYEN_MA + " = ? ",
//                new String[]{String.valueOf(id_unique_SO)});
//
//    }
//
//    public int updatePositionFrom_chuyenMa(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
//        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
//
//        ContentValues values = new ContentValues();
//
//        values.put(POSITION_FROM_CHUYEN_MA, wareHouse);
//        values.put(POSITION_FROM_CODE_CHUYEN_MA, from);
//        values.put(LPN_FROM_CHUYEN_MA, "");
//        values.put(POSITION_FROM_DESCRIPTION_CHUYEN_MA, descreption);
//
//
//        // updating row
//        return db.update(O_CHUYEN_MA, values, AUTOINCREMENT_CHUYEN_MA + " = ? ",
//                new String[]{String.valueOf(id_unique_SO)});
//
//    }
//

//
//
//    public int updatePositionTo_chuyenMa(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
//
//        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
//
//        ContentValues values = new ContentValues();
//        values.put(POSITION_TO_CHUYEN_MA, wareHouse);
//        values.put(POSITION_TO_CODE_CHUYEN_MA, to);
//        values.put(LPN_TO_CHUYEN_MA, "");
//
//        values.put(POSITION_TO_DESCRIPTION_CHUYEN_MA, descreption);
//        // updating row
//        return db.update(O_CHUYEN_MA, values,
//                AUTOINCREMENT_CHUYEN_MA + " = ? ",
//                new String[]{String.valueOf(id_unique_SO)});
//
//
//    }
//
//
//    public ArrayList<Product_ChuyenMa>
//    getoneProduct_chuyenMa(String CD, String expDate, String ea_unit, String stockinDate, String po_return) {
//        ArrayList<Product_ChuyenMa> chuyenMas = new ArrayList<Product_ChuyenMa>();
//        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
//        String selectQuery = "SELECT  * FROM " + O_CHUYEN_MA + " " + " WHERE "
//                + PRODUCT_CD_CHUYEN_MA + " = " + CD + " AND "
//                + TRANSFER_POSTING_CD + " = " + po_return + " AND "
//                + EA_UNIT_CHUYEN_MA + " like " + " '%" + ea_unit + "%'" + " AND "
//                + EXPIRED_DATE_CHUYEN_MA + " like " + " '%" + expDate + "%'" + " AND "
//                + STOCKIN_DATE_CHUYEN_MA + " like " + " '%" + stockinDate + "%'";
//        Cursor c = db.rawQuery(selectQuery, null);
//        // looping through all rows and adding to list
//        if (c != null && c.moveToFirst()) {
//            do {
//                Product_ChuyenMa chuyenMa = new Product_ChuyenMa();
//                chuyenMa.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_CHUYEN_MA))));
//                chuyenMa.setBATCH_NUMBER((c.getString(c
//                        .getColumnIndex(BATCH_NUMBER_CHUYEN_MA))));
//                chuyenMa.setMANUFACTURING_DATE((c.getString(c
//                        .getColumnIndex(MANUFACTURING_DATE_CHUYEN_MA))));
//                chuyenMa.setWAREHOUSE_POSITION_CD((c.getString(c
//                        .getColumnIndex(WAREHOUSE_POSITION_CD_CHUYEN_MA))));
//                chuyenMa.setPRODUCT_CD((c.getString(c
//                        .getColumnIndex(PRODUCT_CD_CHUYEN_MA))));
//                chuyenMa.setPRODUCT_CODE((c.getString(c
//                        .getColumnIndex(PRODUCT_CODE_CHUYEN_MA))));
//                chuyenMa.setPRODUCT_NAME((c.getString(c
//                        .getColumnIndex(PRODUCT_NAME_CHUYEN_MA))));
//                chuyenMa.setEXPIRED_DATE((c.getString(c
//                        .getColumnIndex(EXPIRED_DATE_CHUYEN_MA))));
//                chuyenMa.setQTY((c.getString(c
//                        .getColumnIndex(QTY_SET_AVAILABLE_CHUYEN_MA))));
//                chuyenMa.setUNIT((c.getString(c
//                        .getColumnIndex(EA_UNIT_CHUYEN_MA))));
//                chuyenMa.setPOSITION_FROM_CODE((c.getString(c
//                        .getColumnIndex(POSITION_FROM_CODE_CHUYEN_MA))));
//                chuyenMa.setPOSITION_TO_CODE((c.getString(c
//                        .getColumnIndex(POSITION_TO_CODE_CHUYEN_MA))));
//                chuyenMas.add(chuyenMa);
//            } while (c.moveToNext());
//        }
//
//        c.close();
//        return chuyenMas;
//    }
//
//
//    public ArrayList<Product_ChuyenMa>
//    getAllProduct_chuyenMa_Sync(String po_return) {
//        ArrayList<Product_ChuyenMa> chuyenMas = new ArrayList<Product_ChuyenMa>();
//        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
//        String selectQuery = "SELECT  *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , " +
//                "REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, " +
//                "REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_CHUYEN_MA +
//                " where " + TRANSFER_POSTING_CD + " = " + po_return;
//        Cursor c = db.rawQuery(selectQuery, null);
//        // looping through all rows and adding to list
//        if (c != null && c.moveToFirst()) {
//            do {
//
//                Product_ChuyenMa chuyenMa = new Product_ChuyenMa();
////                chuyenMa.setAUTOINCREMENT((c.getString(c
////                        .getColumnIndex(AUTOINCREMENT_CHUYEN_MA))));
//                chuyenMa.setUNIQUE_CODE((c.getString(c
//                        .getColumnIndex(UNIQUE_CODE_CHUYEN_MA))));
//                chuyenMa.setBATCH_NUMBER((c.getString(c
//                        .getColumnIndex(BATCH_NUMBER_CHUYEN_MA))));
//                chuyenMa.setMANUFACTURING_DATE((c.getString(c
//                        .getColumnIndex(MANUFACTURING_DATE_CHUYEN_MA))));
//                chuyenMa.setWAREHOUSE_POSITION_CD((c.getString(c
//                        .getColumnIndex(WAREHOUSE_POSITION_CD_CHUYEN_MA))));
//                chuyenMa.setPRODUCT_CODE((c.getString(c
//                        .getColumnIndex(PRODUCT_CODE_CHUYEN_MA))));
//                chuyenMa.setPRODUCT_NAME((c.getString(c
//                        .getColumnIndex(PRODUCT_NAME_CHUYEN_MA))));
//                chuyenMa.setPRODUCT_CD((c.getString(c
//                        .getColumnIndex(PRODUCT_CD_CHUYEN_MA))));
//                chuyenMa.setQTY((c.getString(c
//                        .getColumnIndex(QTY_SET_AVAILABLE_CHUYEN_MA))));
//                chuyenMa.setSTOCKIN_DATE((c.getString(c
//                        .getColumnIndex(STOCKIN_DATE_CHUYEN_MA))));
//                chuyenMa.setQTY_EA_AVAILABLE((c.getString(c
//                        .getColumnIndex(QTY_EA_AVAILABLE_CHUYEN_MA))));
//                chuyenMa.setEXPIRED_DATE((c.getString(c
//                        .getColumnIndex(EXPIRED_DATE_CHUYEN_MA))));
//                chuyenMa.setUNIT((c.getString(c
//                        .getColumnIndex(EA_UNIT_CHUYEN_MA))));
//                chuyenMa.setPOSITION_FROM_CD((c.getString(c
//                        .getColumnIndex(POSITION_FROM_CHUYEN_MA))));
//                chuyenMa.setPOSITION_TO_CD((c.getString(c
//                        .getColumnIndex(POSITION_TO_CHUYEN_MA))));
//                chuyenMa.setPOSITION_FROM_CODE((c.getString(c
//                        .getColumnIndex(POSITION_FROM_CODE_CHUYEN_MA))));
//                chuyenMa.setPOSITION_TO_CODE((c.getString(c
//                        .getColumnIndex(POSITION_TO_CODE_CHUYEN_MA))));
//                chuyenMa.setPOSITION_FROM_DESCRIPTION((c.getString(c
//                        .getColumnIndex(POSITION_FROM_DESCRIPTION_CHUYEN_MA))));
//                chuyenMa.setPOSITION_TO_DESCRIPTION((c.getString(c
//                        .getColumnIndex(POSITION_TO_DESCRIPTION_CHUYEN_MA))));
//                chuyenMa.setSTOCK_CHUYEN_MA_CD((c.getString(c
//                        .getColumnIndex(TRANSFER_POSTING_CD))));
//                chuyenMa.setLPN_FROM((c.getString(c
//                        .getColumnIndex(LPN_FROM_CHUYEN_MA))));
//                chuyenMa.setLPN_TO((c.getString(c
//                        .getColumnIndex(LPN_TO_CHUYEN_MA))));
//                chuyenMa.setLPN_CODE((c.getString(c
//                        .getColumnIndex(LPN_CODE_CHUYEN_MA))));
//                chuyenMas.add(chuyenMa);
//            } while (c.moveToNext());
//        }
//
//        c.close();
//        return chuyenMas;
//    }
//
//    public ArrayList<Product_ChuyenMa>
//    getAllProduct_chuyenMa(String po_return) {
//        ArrayList<Product_ChuyenMa> chuyenMas = new ArrayList<Product_ChuyenMa>();
//        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
//        String selectQuery = "SELECT  * FROM " + O_CHUYEN_MA + " where " + TRANSFER_POSTING_CD + " = " + po_return;
//        Cursor c = db.rawQuery(selectQuery, null);
//        // looping through all rows and adding to list
//        if (c != null && c.moveToFirst()) {
//            do {
//
//                Product_ChuyenMa chuyenMa = new Product_ChuyenMa();
//                chuyenMa.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_CHUYEN_MA))));
//                chuyenMa.setBATCH_NUMBER((c.getString(c
//                        .getColumnIndex(BATCH_NUMBER_CHUYEN_MA))));
//                chuyenMa.setMANUFACTURING_DATE((c.getString(c
//                        .getColumnIndex(MANUFACTURING_DATE_CHUYEN_MA))));
//                chuyenMa.setWAREHOUSE_POSITION_CD((c.getString(c
//                        .getColumnIndex(WAREHOUSE_POSITION_CD_CHUYEN_MA))));
//                chuyenMa.setUNIQUE_CODE((c.getString(c
//                        .getColumnIndex(UNIQUE_CODE_CHUYEN_MA))));
//                chuyenMa.setPRODUCT_CODE((c.getString(c
//                        .getColumnIndex(PRODUCT_CODE_CHUYEN_MA))));
//                chuyenMa.setPRODUCT_NAME((c.getString(c
//                        .getColumnIndex(PRODUCT_NAME_CHUYEN_MA))));
//                chuyenMa.setPRODUCT_CD((c.getString(c
//                        .getColumnIndex(PRODUCT_CD_CHUYEN_MA))));
//                chuyenMa.setQTY_SET_AVAILABLE((c.getString(c
//                        .getColumnIndex(QTY_SET_AVAILABLE_CHUYEN_MA))));
//                chuyenMa.setSTOCKIN_DATE((c.getString(c
//                        .getColumnIndex(STOCKIN_DATE_CHUYEN_MA))));
//                chuyenMa.setQTY_EA_AVAILABLE((c.getString(c
//                        .getColumnIndex(QTY_EA_AVAILABLE_CHUYEN_MA))));
//                chuyenMa.setEXPIRED_DATE((c.getString(c
//                        .getColumnIndex(EXPIRED_DATE_CHUYEN_MA))));
//                chuyenMa.setUNIT((c.getString(c
//                        .getColumnIndex(EA_UNIT_CHUYEN_MA))));
//                chuyenMa.setPOSITION_FROM_CD((c.getString(c
//                        .getColumnIndex(POSITION_FROM_CHUYEN_MA))));
//                chuyenMa.setPOSITION_TO_CD((c.getString(c
//                        .getColumnIndex(POSITION_TO_CHUYEN_MA))));
//                chuyenMa.setPOSITION_FROM_CODE((c.getString(c
//                        .getColumnIndex(POSITION_FROM_CODE_CHUYEN_MA))));
//                chuyenMa.setPOSITION_TO_CODE((c.getString(c
//                        .getColumnIndex(POSITION_TO_CODE_CHUYEN_MA))));
//                chuyenMa.setPOSITION_FROM_DESCRIPTION((c.getString(c
//                        .getColumnIndex(POSITION_FROM_DESCRIPTION_CHUYEN_MA))));
//                chuyenMa.setPOSITION_TO_DESCRIPTION((c.getString(c
//                        .getColumnIndex(POSITION_TO_DESCRIPTION_CHUYEN_MA))));
//                chuyenMa.setSTOCK_TRANSFER_POSTING_CD((c.getString(c
//                        .getColumnIndex(TRANSFER_POSTING_CD))));
//                chuyenMa.setLPN_FROM((c.getString(c
//                        .getColumnIndex(LPN_FROM_CHUYEN_MA))));
//                chuyenMa.setLPN_TO((c.getString(c
//                        .getColumnIndex(LPN_TO_CHUYEN_MA))));
//                chuyenMa.setLPN_CODE((c.getString(c
//                        .getColumnIndex(LPN_CODE_CHUYEN_MA))));
//                chuyenMas.add(chuyenMa);
//            } while (c.moveToNext());
//        }
//
//        c.close();
//        return chuyenMas;
//    }
//
//
//    public int updateProduct_chuyenMa(Product_ChuyenMa chuyenMa, String incre_so, String PRODUCT_CD, String sl, String ea_unit, String stock, String po_return) {
//        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
//        ContentValues values = new ContentValues();
//        values.put(PRODUCT_CD_CHUYEN_MA, PRODUCT_CD);
//        values.put(PRODUCT_CODE_CHUYEN_MA, chuyenMa.getPRODUCT_CODE());
//        values.put(PRODUCT_NAME_CHUYEN_MA, chuyenMa.getPRODUCT_NAME());
//        values.put(EXPIRED_DATE_CHUYEN_MA, chuyenMa.getEXPIRED_DATE());
//        values.put(EA_UNIT_CHUYEN_MA, chuyenMa.getUNIT());
//        values.put(QTY_SET_AVAILABLE_CHUYEN_MA, sl);
//        values.put(TRANSFER_POSTING_CD, po_return);
//
//        // updating row
//        return db.update(O_CHUYEN_MA, values,  AUTOINCREMENT_CHUYEN_MA + " = ?",
//                new String[]{String.valueOf(incre_so)});
//
//    }
//
//
//    public void deleteProduct_chuyenMa() {
//        // TODO Auto-generated method stub
//        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
//        db.execSQL("delete from " + O_CHUYEN_MA);
//    }
//
//    //END TABLE O_CHUYEN_MA


    //DATABASE PUT cd
    public static final String O_PICKUP = "O_PICKUP";
    public static final String WAREHOUSE_POSITION_CD_PICKUP = "WAREHOUSE_POSITION_CD_PICKUP";
    public static final String AUTOINCREMENT_PICKUP = "AUTOINCREMENT_PICKUP";
    public static final String PRODUCT_CODE_PICKUP = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_PICKUP = "PRODUCT_NAME";
    public static final String PRODUCT_CD_PICKUP = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_PICKUP = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_PICKUP = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_PICKUP = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_PICKUP = "STOCKIN_DATE";
    public static final String EA_UNIT_PICKUP = "EA_UNIT";
    public static final String POSITION_FROM_PICKUP = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_PICKUP = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_PICKUP = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_PICKUP = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_PICKUP = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_PICKUP = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_PICKUP = "UNIQUE_CODE";
    public static final String STOCK_QA_CD = "STOCK_QA_CD";
    public static final String LPN_CD_PICKUP = "LPN_CD_PICKUP";
    public static final String LPN_CODE_PICKUP = "LPN_CODE_PICKUP";
    public static final String LPN_FROM_PICKUP = "LPN_FROM_PICKUP";
    public static final String LPN_TO_PICKUP = "LPN_TO_PICKUP";
    public static final String BATCH_NUMBER_PICKUP = "BATCH_NUMBER_PICKUP";
    public static final String MANUFACTURING_DATE_PICKUP = "MANUFACTURING_DATE_PICKUP";
    public static final String NOTE_PICKUP = "NOTE_PICKUP";

    public static final String CREATE_TABLE_O_PICKUP = "CREATE TABLE "
            + O_PICKUP + "("
            + AUTOINCREMENT_PICKUP + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_PICKUP + " TEXT,"
            + WAREHOUSE_POSITION_CD_PICKUP + " TEXT,"
            + BATCH_NUMBER_PICKUP + " TEXT,"
            + MANUFACTURING_DATE_PICKUP + " TEXT,"
            + PRODUCT_NAME_PICKUP + " TEXT,"
            + PRODUCT_CODE_PICKUP + " TEXT,"
            + QTY_EA_AVAILABLE_PICKUP + " TEXT,"
            + QTY_SET_AVAILABLE_PICKUP + " TEXT,"
            + EXPIRED_DATE_PICKUP + " TEXT,"
            + STOCKIN_DATE_PICKUP + " TEXT,"
            + EA_UNIT_PICKUP + " TEXT,"
            + POSITION_FROM_PICKUP + " TEXT,"
            + POSITION_FROM_CODE_PICKUP + " TEXT,"
            + POSITION_FROM_DESCRIPTION_PICKUP + " TEXT,"
            + POSITION_TO_PICKUP + " TEXT,"
            + POSITION_TO_CODE_PICKUP + " TEXT,"
            + POSITION_TO_DESCRIPTION_PICKUP + " TEXT,"
            + STOCK_QA_CD + " TEXT,"
            + UNIQUE_CODE_PICKUP + " TEXT ,"
            + LPN_CD_PICKUP + " TEXT ,"
            + LPN_CODE_PICKUP + " TEXT ,"
            + LPN_FROM_PICKUP + " TEXT ,"
            + LPN_TO_PICKUP + " TEXT ,"
            + NOTE_PICKUP + " TEXT "
            + ")";


    public long CreatePickup(Product_Pickup pickup) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
//        values.put(AUTOINCREMENT_PICKUP, pickup.getAUTOINCREMENT());

        values.put(UNIQUE_CODE_PICKUP, pickup.getUNIT());
        values.put(BATCH_NUMBER_PICKUP, pickup.getBATCH_NUMBER());
        values.put(MANUFACTURING_DATE_PICKUP, pickup.getMANUFACTURING_DATE());
        values.put(PRODUCT_CODE_PICKUP, pickup.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_PICKUP, pickup.getPRODUCT_NAME());
        values.put(WAREHOUSE_POSITION_CD_PICKUP, pickup.getWAREHOUSE_POSITION_CD());
        values.put(PRODUCT_CD_PICKUP, pickup.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_PICKUP, pickup.getQTY());
        values.put(STOCKIN_DATE_PICKUP, pickup.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_PICKUP, pickup.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_PICKUP, pickup.getEXPIRED_DATE());
        values.put(EA_UNIT_PICKUP, pickup.getUNIT());
        values.put(POSITION_FROM_PICKUP, pickup.getPOSITION_FROM_CD());
        values.put(POSITION_TO_PICKUP, pickup.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_PICKUP, pickup.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_PICKUP, pickup.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_PICKUP, pickup.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_PICKUP, pickup.getPOSITION_TO_DESCRIPTION());
        values.put(STOCK_QA_CD, pickup.getSTOCK_QA_CD());
        values.put(LPN_CODE_PICKUP, pickup.getLPN_CODE());
        values.put(LPN_FROM_PICKUP, pickup.getLPN_FROM());
        values.put(LPN_TO_PICKUP, pickup.getLPN_TO());
        values.put(NOTE_PICKUP, pickup.getNOTE());
        // insert row
        long id = db.insert(O_PICKUP, null, values);
        return id;
    }

    public int updatePositionFrom_pickup_LPN(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_PICKUP, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_PICKUP, descreption);

        values.put(POSITION_FROM_CODE_PICKUP, from);
        values.put(LPN_FROM_PICKUP, from);


        // updating row
        return db.update(O_PICKUP, values, AUTOINCREMENT_PICKUP + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});

    }

    public int updatePositionFrom_pickup(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_PICKUP, wareHouse);
        values.put(POSITION_FROM_CODE_PICKUP, from);
        values.put(LPN_FROM_PICKUP, "");
        values.put(POSITION_FROM_DESCRIPTION_PICKUP, descreption);


        // updating row
        return db.update(O_PICKUP, values, AUTOINCREMENT_PICKUP + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});

    }

    public int updatePositionTo_pickup_LPN(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_PICKUP, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_PICKUP, descreption);
        values.put(LPN_TO_PICKUP, to);

        values.put(POSITION_TO_CODE_PICKUP, to);
        // updating row
        return db.update(O_PICKUP, values,
                AUTOINCREMENT_PICKUP + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});


    }


    public int updatePositionTo_pickup(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_PICKUP, wareHouse);
        values.put(POSITION_TO_CODE_PICKUP, to);
        values.put(LPN_TO_PICKUP, "");

        values.put(POSITION_TO_DESCRIPTION_PICKUP, descreption);
        // updating row
        return db.update(O_PICKUP, values,
                AUTOINCREMENT_PICKUP + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});


    }


    public ArrayList<Product_Pickup>
    getoneProduct_Pickup(String CD, String expDate, String ea_unit, String stockinDate, String cd,String batch_number) {
        ArrayList<Product_Pickup> pickups = new ArrayList<Product_Pickup>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_PICKUP + " " + " WHERE "
                + PRODUCT_CD_PICKUP + " = " + CD + " AND "
                + STOCK_QA_CD + " = " + cd + " AND "
                + BATCH_NUMBER_PICKUP + " like " + " '%" + batch_number + "%'" + " AND "
                + EA_UNIT_PICKUP + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_PICKUP + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_PICKUP + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_Pickup pickup = new Product_Pickup();
                pickup.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PICKUP))));
                pickup.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PICKUP))));
                pickup.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_PICKUP))));
                pickup.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_PICKUP))));
                pickup.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PICKUP))));
                pickup.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PICKUP))));
                pickup.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PICKUP))));
                pickup.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PICKUP))));
                pickup.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_PICKUP))));
                pickup.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_PICKUP))));
                pickup.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_PICKUP))));
                pickup.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_PICKUP))));
                pickup.setNOTE((c.getString(c
                        .getColumnIndex(NOTE_PICKUP))));
                pickups.add(pickup);
            } while (c.moveToNext());
        }

        c.close();
        return pickups;
    }


    public ArrayList<Product_Pickup>
    getAllProduct_Pickup_Sync(String cd) {
        ArrayList<Product_Pickup> pickups = new ArrayList<Product_Pickup>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , " +
                "REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, " +
                "REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_PICKUP +
                " where " + STOCK_QA_CD + " = " + cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Pickup pickup = new Product_Pickup();
//                pickup.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_PICKUP))));
                pickup.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_PICKUP))));
                pickup.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PICKUP))));
                pickup.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_PICKUP))));
                pickup.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_PICKUP))));
                pickup.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PICKUP))));
                pickup.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PICKUP))));
                pickup.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PICKUP))));
                pickup.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_PICKUP))));
                pickup.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_PICKUP))));
                pickup.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_PICKUP))));
                pickup.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PICKUP))));
                pickup.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_PICKUP))));
                pickup.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_PICKUP))));
                pickup.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_PICKUP))));
                pickup.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_PICKUP))));
                pickup.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_PICKUP))));
                pickup.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_PICKUP))));
                pickup.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_PICKUP))));
                pickup.setSTOCK_QA_CD((c.getString(c
                        .getColumnIndex(STOCK_QA_CD))));
                pickup.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_PICKUP))));
                pickup.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_PICKUP))));
                pickup.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_PICKUP))));
                pickup.setNOTE((c.getString(c
                        .getColumnIndex(NOTE_PICKUP))));
                pickups.add(pickup);
            } while (c.moveToNext());
        }

        c.close();
        return pickups;
    }

    public ArrayList<Product_Pickup>
    getAllProduct_Pickup(String cd) {
        ArrayList<Product_Pickup> pickups = new ArrayList<Product_Pickup>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_PICKUP + " where " + STOCK_QA_CD + " = " + cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Pickup pickup = new Product_Pickup();
                pickup.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_PICKUP))));
                pickup.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_PICKUP))));
                pickup.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_PICKUP))));
                pickup.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_PICKUP))));
                pickup.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_PICKUP))));
                pickup.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_PICKUP))));
                pickup.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_PICKUP))));
                pickup.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_PICKUP))));
                pickup.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_PICKUP))));
                pickup.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_PICKUP))));
                pickup.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_PICKUP))));
                pickup.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_PICKUP))));
                pickup.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_PICKUP))));
                pickup.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_PICKUP))));
                pickup.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_PICKUP))));
                pickup.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_PICKUP))));
                pickup.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_PICKUP))));
                pickup.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_PICKUP))));
                pickup.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_PICKUP))));
                pickup.setSTOCK_QA_CD((c.getString(c
                        .getColumnIndex(STOCK_QA_CD))));
                pickup.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_PICKUP))));
                pickup.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_PICKUP))));
                pickup.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_PICKUP))));
                pickup.setNOTE((c.getString(c
                        .getColumnIndex(NOTE_PICKUP))));
                pickups.add(pickup);
            } while (c.moveToNext());
        }

        c.close();
        return pickups;
    }


    public int updateProduct_Pickup(Product_Pickup pickup, String incre_so, String PRODUCT_CD, String sl, String ea_unit, String stock, String cd) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_PICKUP, PRODUCT_CD);
        values.put(PRODUCT_CODE_PICKUP, pickup.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_PICKUP, pickup.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_PICKUP, pickup.getEXPIRED_DATE());
        values.put(EA_UNIT_PICKUP, pickup.getUNIT());
        values.put(QTY_SET_AVAILABLE_PICKUP, sl);
        values.put(STOCK_QA_CD, cd);

        // updating row
        return db.update(O_PICKUP, values,  AUTOINCREMENT_PICKUP + " = ?",
                new String[]{String.valueOf(incre_so)});

    }

    public int updateNote_Pickup(String incre_so  , String note) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();

        values.put(NOTE_PICKUP, note);

        // updating row
        return db.update(O_PICKUP, values,  AUTOINCREMENT_PICKUP + " = ?",
                new String[]{String.valueOf(incre_so)});

    }


    public void deleteProduct_Pickup( String cd) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_PICKUP);
    }

    //END TABLE O_PICKUP

    //DATABASE PUT PO_Return
    public static final String O_QA = "O_QA";
    public static final String WAREHOUSE_POSITION_CD_QA = "WAREHOUSE_POSITION_CD_QA";
    public static final String AUTOINCREMENT_QA = "AUTOINCREMENT_QA";
    public static final String PRODUCT_CODE_QA = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_QA = "PRODUCT_NAME";
    public static final String PRODUCT_CD_QA = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_QA = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_QA = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_QA = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_QA = "STOCKIN_DATE";
    public static final String EA_UNIT_QA = "EA_UNIT";
    public static final String POSITION_FROM_QA = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_QA = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_QA = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_QA = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_QA = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_QA = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_QA = "UNIQUE_CODE";
    public static final String STOCK_QA_CD_QA = "STOCK_QA_CD_QA";
    public static final String LPN_CD_QA = "LPN_CD_QA";
    public static final String LPN_CODE_QA = "LPN_CODE_QA";
    public static final String LPN_FROM_QA = "LPN_FROM_QA";
    public static final String LPN_TO_QA = "LPN_TO_QA";
    public static final String BATCH_NUMBER_QA = "BATCH_NUMBER_QA";
    public static final String MANUFACTURING_DATE_QA = "MANUFACTURING_DATE_QA";
    public static final String BARCODE_QA = "BARCODE_QA";
    public static final String CHECKED_QA = "CHECKED_QA";
    public static final String CHECKED_IMAGE_QA = "CHECKED_IMAGE_QA";

    public static final String CREATE_TABLE_O_QA = "CREATE TABLE "
            + O_QA + "("
            + AUTOINCREMENT_QA + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_QA + " TEXT,"
            + WAREHOUSE_POSITION_CD_QA + " TEXT,"
            + BATCH_NUMBER_QA + " TEXT,"
            + MANUFACTURING_DATE_QA + " TEXT,"
            + PRODUCT_NAME_QA + " TEXT,"
            + PRODUCT_CODE_QA + " TEXT,"
            + QTY_EA_AVAILABLE_QA + " TEXT,"
            + QTY_SET_AVAILABLE_QA + " TEXT,"
            + EXPIRED_DATE_QA + " TEXT,"
            + STOCKIN_DATE_QA + " TEXT,"
            + EA_UNIT_QA + " TEXT,"
            + POSITION_FROM_QA + " TEXT,"
            + POSITION_FROM_CODE_QA + " TEXT,"
            + POSITION_FROM_DESCRIPTION_QA + " TEXT,"
            + POSITION_TO_QA + " TEXT,"
            + CHECKED_QA + " TEXT,"
            + CHECKED_IMAGE_QA + " TEXT,"
            + POSITION_TO_CODE_QA + " TEXT,"
            + POSITION_TO_DESCRIPTION_QA + " TEXT,"
            + STOCK_QA_CD_QA + " TEXT,"
            + UNIQUE_CODE_QA + " TEXT ,"
            + LPN_CD_QA + " TEXT ,"
            + LPN_CODE_QA + " TEXT ,"
            + LPN_FROM_QA + " TEXT ,"
            + BARCODE_QA + " TEXT ,"
            + LPN_TO_QA + " TEXT "
            + ")";


    public long CreateQA(Product_QA list_QA) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
//        values.put(AUTOINCREMENT_QA, list_QA.getAUTOINCREMENT());

        values.put(UNIQUE_CODE_QA, list_QA.getUNIT());
        values.put(BARCODE_QA, list_QA.getBARCODE());
        values.put(BATCH_NUMBER_QA, list_QA.getBATCH_NUMBER());
        values.put(MANUFACTURING_DATE_QA, list_QA.getMANUFACTURING_DATE());
        values.put(PRODUCT_CODE_QA, list_QA.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_QA, list_QA.getPRODUCT_NAME());
        values.put(WAREHOUSE_POSITION_CD_QA, list_QA.getWAREHOUSE_POSITION_CD());
        values.put(PRODUCT_CD_QA, list_QA.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_QA, list_QA.getQTY());
        values.put(STOCKIN_DATE_QA, list_QA.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_QA, list_QA.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_QA, list_QA.getEXPIRED_DATE());
        values.put(EA_UNIT_QA, list_QA.getUNIT());
        values.put(CHECKED_QA, list_QA.getCHECKED());
        values.put(CHECKED_IMAGE_QA, list_QA.getCHECKED_IMAGE());
        values.put(POSITION_FROM_QA, list_QA.getPOSITION_FROM_CD());
        values.put(POSITION_TO_QA, list_QA.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_QA, list_QA.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_QA, list_QA.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_QA, list_QA.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_QA, list_QA.getPOSITION_TO_DESCRIPTION());
        values.put(STOCK_QA_CD_QA, list_QA.getSTOCK_QA_CD());
        values.put(LPN_CODE_QA, list_QA.getLPN_CODE());
        values.put(LPN_FROM_QA, list_QA.getLPN_FROM());
        values.put(LPN_TO_QA, list_QA.getLPN_TO());
        // insert row
        long id = db.insert(O_QA, null, values);
        return id;
    }


    public int updateChecked_QA(String batch , String cd , String product_code,String unit) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(CHECKED_QA, "Yes");
        // updating row
        return db.update(O_QA, values, BATCH_NUMBER_QA + " = ? AND "
                        + STOCK_QA_CD_QA + " = ? AND "
                        + PRODUCT_CODE_QA + " = ? AND "
                        + EA_UNIT_QA + " = ? ",
                new String[]{String.valueOf(batch),String.valueOf(cd),String.valueOf(product_code),String.valueOf(unit)});

    }

    public int update_Image_QA(String batch , String cd , String product_code,String unit) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(CHECKED_IMAGE_QA, "Yes");
        // updating row
        return db.update(O_QA, values, BATCH_NUMBER_QA + " = ? AND "
                        + STOCK_QA_CD_QA + " = ? AND "
                        + PRODUCT_CODE_QA + " = ? AND "
                        + EA_UNIT_QA + " = ? ",
                new String[]{String.valueOf(batch),String.valueOf(cd),String.valueOf(product_code),String.valueOf(unit)});

    }


    public ArrayList<Product_QA>
    getAllProduct_QA(String po_return) {
        ArrayList<Product_QA> list_QAs = new ArrayList<Product_QA>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_QA + " where " + STOCK_QA_CD_QA + " = " + po_return;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_QA list_QA = new Product_QA();
                list_QA.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_QA))));
                list_QA.setBARCODE((c.getString(c
                        .getColumnIndex(BARCODE_QA))));
                list_QA.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_QA))));
                list_QA.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_QA))));
                list_QA.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_QA))));
                list_QA.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_QA))));
                list_QA.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_QA))));
                list_QA.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_QA))));
                list_QA.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_QA))));
                list_QA.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_QA))));
                list_QA.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_QA))));
                list_QA.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_QA))));
                list_QA.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_QA))));
                list_QA.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_QA))));
                list_QA.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_QA))));
                list_QA.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_QA))));
                list_QA.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_QA))));
                list_QA.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_QA))));
                list_QA.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_QA))));
                list_QA.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_QA))));
                list_QA.setSTOCK_QA_CD((c.getString(c
                        .getColumnIndex(STOCK_QA_CD_QA))));
                list_QA.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_QA))));
                list_QA.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_QA))));
                list_QA.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_QA))));
                list_QA.setCHECKED((c.getString(c
                        .getColumnIndex(CHECKED_QA))));
                list_QA.setCHECKED_IMAGE((c.getString(c
                        .getColumnIndex(CHECKED_IMAGE_QA))));
                list_QAs.add(list_QA);
            } while (c.moveToNext());
        }

        c.close();
        return list_QAs;
    }



    public void deleteProduct_QA( String cd) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_QA);
    }

    //END TABLE O_QA

    //DATABASE PUT cd
    public static final String O_RETURN_QA = "O_RETURN_QA";
    public static final String WAREHOUSE_POSITION_CD_RETURN_QA = "WAREHOUSE_POSITION_CD_RETURN_QA";
    public static final String AUTOINCREMENT_RETURN_QA = "AUTOINCREMENT_RETURN_QA";
    public static final String PRODUCT_CODE_RETURN_QA = "PRODUCT_CODE";
    public static final String PRODUCT_NAME_RETURN_QA = "PRODUCT_NAME";
    public static final String PRODUCT_CD_RETURN_QA = "PRODUCT_CD";
    public static final String QTY_EA_AVAILABLE_RETURN_QA = "QTY_EA_AVAILABLE";
    public static final String QTY_SET_AVAILABLE_RETURN_QA = "QTY_SET_AVAILABLE";
    public static final String EXPIRED_DATE_RETURN_QA = "EXPIRY_DATE";
    public static final String STOCKIN_DATE_RETURN_QA = "STOCKIN_DATE";
    public static final String EA_UNIT_RETURN_QA = "EA_UNIT";
    public static final String POSITION_FROM_RETURN_QA = "POSITION_FROM_CD";
    public static final String POSITION_FROM_CODE_RETURN_QA = "POSITION_FROM_CODE";
    public static final String POSITION_FROM_DESCRIPTION_RETURN_QA = "POSITION_FROM_DESCRIPTION";
    public static final String POSITION_TO_RETURN_QA = "POSITION_TO_CD";
    public static final String POSITION_TO_CODE_RETURN_QA = "POSITION_TO_CODE";
    public static final String POSITION_TO_DESCRIPTION_RETURN_QA = "POSITION_TO_DESCRIPTION";
    public static final String UNIQUE_CODE_RETURN_QA = "UNIQUE_CODE";
    public static final String STOCK_RETURN_QA_CD = "STOCK_RETURN_QA_CD";
    public static final String LPN_CD_RETURN_QA = "LPN_CD_RETURN_QA";
    public static final String LPN_CODE_RETURN_QA = "LPN_CODE_RETURN_QA";
    public static final String LPN_FROM_RETURN_QA = "LPN_FROM_RETURN_QA";
    public static final String LPN_TO_RETURN_QA = "LPN_TO_RETURN_QA";
    public static final String BATCH_NUMBER_RETURN_QA = "BATCH_NUMBER_RETURN_QA";
    public static final String MANUFACTURING_DATE_RETURN_QA = "MANUFACTURING_DATE_RETURN_QA";
    public static final String NOTE_RETURN_QA = "NOTE_RETURN_QA";

    public static final String CREATE_TABLE_O_RETURN_QA = "CREATE TABLE "
            + O_RETURN_QA + "("
            + AUTOINCREMENT_RETURN_QA + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + PRODUCT_CD_RETURN_QA + " TEXT,"
            + WAREHOUSE_POSITION_CD_RETURN_QA + " TEXT,"
            + BATCH_NUMBER_RETURN_QA + " TEXT,"
            + MANUFACTURING_DATE_RETURN_QA + " TEXT,"
            + PRODUCT_NAME_RETURN_QA + " TEXT,"
            + PRODUCT_CODE_RETURN_QA + " TEXT,"
            + QTY_EA_AVAILABLE_RETURN_QA + " TEXT,"
            + QTY_SET_AVAILABLE_RETURN_QA + " TEXT,"
            + EXPIRED_DATE_RETURN_QA + " TEXT,"
            + STOCKIN_DATE_RETURN_QA + " TEXT,"
            + EA_UNIT_RETURN_QA + " TEXT,"
            + POSITION_FROM_RETURN_QA + " TEXT,"
            + POSITION_FROM_CODE_RETURN_QA + " TEXT,"
            + POSITION_FROM_DESCRIPTION_RETURN_QA + " TEXT,"
            + POSITION_TO_RETURN_QA + " TEXT,"
            + POSITION_TO_CODE_RETURN_QA + " TEXT,"
            + POSITION_TO_DESCRIPTION_RETURN_QA + " TEXT,"
            + STOCK_RETURN_QA_CD + " TEXT,"
            + UNIQUE_CODE_RETURN_QA + " TEXT ,"
            + LPN_CD_RETURN_QA + " TEXT ,"
            + LPN_CODE_RETURN_QA + " TEXT ,"
            + LPN_FROM_RETURN_QA + " TEXT ,"
            + LPN_TO_RETURN_QA + " TEXT ,"
            + NOTE_RETURN_QA + " TEXT "
            + ")";


    public long CreatereturnQA(Product_Return_QA returnQA) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
//        values.put(AUTOINCREMENT_RETURN_QA, returnQA.getAUTOINCREMENT());

        values.put(UNIQUE_CODE_RETURN_QA, returnQA.getUNIT());
        values.put(BATCH_NUMBER_RETURN_QA, returnQA.getBATCH_NUMBER());
        values.put(MANUFACTURING_DATE_RETURN_QA, returnQA.getMANUFACTURING_DATE());
        values.put(PRODUCT_CODE_RETURN_QA, returnQA.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_RETURN_QA, returnQA.getPRODUCT_NAME());
        values.put(WAREHOUSE_POSITION_CD_RETURN_QA, returnQA.getWAREHOUSE_POSITION_CD());
        values.put(PRODUCT_CD_RETURN_QA, returnQA.getPRODUCT_CD());
        values.put(QTY_SET_AVAILABLE_RETURN_QA, returnQA.getQTY());
        values.put(STOCKIN_DATE_RETURN_QA, returnQA.getSTOCKIN_DATE());
        values.put(QTY_EA_AVAILABLE_RETURN_QA, returnQA.getQTY_EA_AVAILABLE());
        values.put(EXPIRED_DATE_RETURN_QA, returnQA.getEXPIRED_DATE());
        values.put(EA_UNIT_RETURN_QA, returnQA.getUNIT());
        values.put(POSITION_FROM_RETURN_QA, returnQA.getPOSITION_FROM_CD());
        values.put(POSITION_TO_RETURN_QA, returnQA.getPOSITION_TO_CD());
        values.put(POSITION_FROM_CODE_RETURN_QA, returnQA.getPOSITION_FROM_CODE());
        values.put(POSITION_TO_CODE_RETURN_QA, returnQA.getPOSITION_TO_CODE());
        values.put(POSITION_FROM_DESCRIPTION_RETURN_QA, returnQA.getPOSITION_FROM_DESCRIPTION());
        values.put(POSITION_TO_DESCRIPTION_RETURN_QA, returnQA.getPOSITION_TO_DESCRIPTION());
        values.put(STOCK_RETURN_QA_CD, returnQA.getSTOCK_QA_CD());
        values.put(LPN_CODE_RETURN_QA, returnQA.getLPN_CODE());
        values.put(LPN_FROM_RETURN_QA, returnQA.getLPN_FROM());
        values.put(LPN_TO_RETURN_QA, returnQA.getLPN_TO());
        values.put(NOTE_RETURN_QA, returnQA.getNOTE());
        // insert row
        long id = db.insert(O_RETURN_QA, null, values);
        return id;
    }

    public int updatePositionFrom_RETURN_QA_LPN(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_RETURN_QA, wareHouse);
        values.put(POSITION_FROM_DESCRIPTION_RETURN_QA, descreption);

        values.put(POSITION_FROM_CODE_RETURN_QA, from);
        values.put(LPN_FROM_RETURN_QA, from);


        // updating row
        return db.update(O_RETURN_QA, values, AUTOINCREMENT_RETURN_QA + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});

    }

    public int updatePositionFrom_RETURN_QA(String id_unique_SO , String from, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();

        values.put(POSITION_FROM_RETURN_QA, wareHouse);
        values.put(POSITION_FROM_CODE_RETURN_QA, from);
        values.put(LPN_FROM_RETURN_QA, "");
        values.put(POSITION_FROM_DESCRIPTION_RETURN_QA, descreption);


        // updating row
        return db.update(O_RETURN_QA, values, AUTOINCREMENT_RETURN_QA + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});

    }

    public int updatePositionTO_RETURN_QA_LPN(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_RETURN_QA, wareHouse);
        values.put(POSITION_TO_DESCRIPTION_RETURN_QA, descreption);
        values.put(LPN_TO_RETURN_QA, to);

        values.put(POSITION_TO_CODE_RETURN_QA, to);
        // updating row
        return db.update(O_RETURN_QA, values,
                AUTOINCREMENT_RETURN_QA + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});


    }


    public int updatePositionTO_RETURN_QA(String id_unique_SO , String to, String wareHouse, String PRODUCT_CD, String exPiredDate, String descreption, String ea_unit, String stockinDate) {

        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);

        ContentValues values = new ContentValues();
        values.put(POSITION_TO_RETURN_QA, wareHouse);
        values.put(POSITION_TO_CODE_RETURN_QA, to);
        values.put(LPN_TO_RETURN_QA, "");

        values.put(POSITION_TO_DESCRIPTION_RETURN_QA, descreption);
        // updating row
        return db.update(O_RETURN_QA, values,
                AUTOINCREMENT_RETURN_QA + " = ? ",
                new String[]{String.valueOf(id_unique_SO)});


    }


    public ArrayList<Product_Return_QA>
    getoneProduct_Return_QA(String CD, String expDate, String ea_unit, String stockinDate, String cd,String batch_number) {
        ArrayList<Product_Return_QA> returnQAs = new ArrayList<Product_Return_QA>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_RETURN_QA + " " + " WHERE "
                + PRODUCT_CD_RETURN_QA + " = " + CD + " AND "
                + STOCK_RETURN_QA_CD + " = " + cd + " AND "
                + BATCH_NUMBER_RETURN_QA + " like " + " '%" + batch_number + "%'" + " AND "
                + EA_UNIT_RETURN_QA + " like " + " '%" + ea_unit + "%'" + " AND "
                + EXPIRED_DATE_RETURN_QA + " like " + " '%" + expDate + "%'" + " AND "
                + STOCKIN_DATE_RETURN_QA + " like " + " '%" + stockinDate + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                Product_Return_QA returnQA = new Product_Return_QA();
                returnQA.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_RETURN_QA))));
                returnQA.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_RETURN_QA))));
                returnQA.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_RETURN_QA))));
                returnQA.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_RETURN_QA))));
                returnQA.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_RETURN_QA))));
                returnQA.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_RETURN_QA))));
                returnQA.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_RETURN_QA))));
                returnQA.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_RETURN_QA))));
                returnQA.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_RETURN_QA))));
                returnQA.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_RETURN_QA))));
                returnQA.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_RETURN_QA))));
                returnQA.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_RETURN_QA))));
                returnQA.setNOTE((c.getString(c
                        .getColumnIndex(NOTE_RETURN_QA))));
                returnQAs.add(returnQA);
            } while (c.moveToNext());
        }

        c.close();
        return returnQAs;
    }


    public ArrayList<Product_Return_QA>
    getAllProduct_Return_QA_Sync(String cd) {
        ArrayList<Product_Return_QA> returnQAs = new ArrayList<Product_Return_QA>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  *, REPLACE(EXPIRY_DATE,'------','') as EXPIRY_DATE , " +
                "REPLACE(POSITION_FROM_CODE,'---','') as POSITION_FROM_CODE, " +
                "REPLACE(POSITION_TO_CODE,'---','') as POSITION_TO_CODE FROM " + O_RETURN_QA +
                " where " + STOCK_RETURN_QA_CD + " = " + cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Return_QA returnQA = new Product_Return_QA();
//                returnQA.setAUTOINCREMENT((c.getString(c
//                        .getColumnIndex(AUTOINCREMENT_RETURN_QA))));
                returnQA.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_RETURN_QA))));
                returnQA.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_RETURN_QA))));
                returnQA.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_RETURN_QA))));
                returnQA.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_RETURN_QA))));
                returnQA.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_RETURN_QA))));
                returnQA.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_RETURN_QA))));
                returnQA.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_RETURN_QA))));
                returnQA.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_RETURN_QA))));
                returnQA.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_RETURN_QA))));
                returnQA.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_RETURN_QA))));
                returnQA.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_RETURN_QA))));
                returnQA.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_RETURN_QA))));
                returnQA.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_RETURN_QA))));
                returnQA.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_RETURN_QA))));
                returnQA.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_RETURN_QA))));
                returnQA.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_RETURN_QA))));
                returnQA.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_RETURN_QA))));
                returnQA.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_RETURN_QA))));
                returnQA.setSTOCK_QA_CD((c.getString(c
                        .getColumnIndex(STOCK_RETURN_QA_CD))));
                returnQA.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_RETURN_QA))));
                returnQA.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_RETURN_QA))));
                returnQA.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_RETURN_QA))));
                returnQA.setNOTE((c.getString(c
                        .getColumnIndex(NOTE_RETURN_QA))));
                returnQAs.add(returnQA);
            } while (c.moveToNext());
        }

        c.close();
        return returnQAs;
    }

    public ArrayList<Product_Return_QA>
    getAllProduct_Return_QA(String cd) {
        ArrayList<Product_Return_QA> returnQAs = new ArrayList<Product_Return_QA>();
        SQLiteDatabase db = sInstance.getReadableDatabase(DatabaseHelper.PWD);
        String selectQuery = "SELECT  * FROM " + O_RETURN_QA + " where " + STOCK_RETURN_QA_CD + " = " + cd;
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {

                Product_Return_QA returnQA = new Product_Return_QA();
                returnQA.setAUTOINCREMENT((c.getString(c
                        .getColumnIndex(AUTOINCREMENT_RETURN_QA))));
                returnQA.setBATCH_NUMBER((c.getString(c
                        .getColumnIndex(BATCH_NUMBER_RETURN_QA))));
                returnQA.setMANUFACTURING_DATE((c.getString(c
                        .getColumnIndex(MANUFACTURING_DATE_RETURN_QA))));
                returnQA.setWAREHOUSE_POSITION_CD((c.getString(c
                        .getColumnIndex(WAREHOUSE_POSITION_CD_RETURN_QA))));
                returnQA.setUNIQUE_CODE((c.getString(c
                        .getColumnIndex(UNIQUE_CODE_RETURN_QA))));
                returnQA.setPRODUCT_CODE((c.getString(c
                        .getColumnIndex(PRODUCT_CODE_RETURN_QA))));
                returnQA.setPRODUCT_NAME((c.getString(c
                        .getColumnIndex(PRODUCT_NAME_RETURN_QA))));
                returnQA.setPRODUCT_CD((c.getString(c
                        .getColumnIndex(PRODUCT_CD_RETURN_QA))));
                returnQA.setQTY((c.getString(c
                        .getColumnIndex(QTY_SET_AVAILABLE_RETURN_QA))));
                returnQA.setSTOCKIN_DATE((c.getString(c
                        .getColumnIndex(STOCKIN_DATE_RETURN_QA))));
                returnQA.setQTY_EA_AVAILABLE((c.getString(c
                        .getColumnIndex(QTY_EA_AVAILABLE_RETURN_QA))));
                returnQA.setEXPIRED_DATE((c.getString(c
                        .getColumnIndex(EXPIRED_DATE_RETURN_QA))));
                returnQA.setUNIT((c.getString(c
                        .getColumnIndex(EA_UNIT_RETURN_QA))));
                returnQA.setPOSITION_FROM_CD((c.getString(c
                        .getColumnIndex(POSITION_FROM_RETURN_QA))));
                returnQA.setPOSITION_TO_CD((c.getString(c
                        .getColumnIndex(POSITION_TO_RETURN_QA))));
                returnQA.setPOSITION_FROM_CODE((c.getString(c
                        .getColumnIndex(POSITION_FROM_CODE_RETURN_QA))));
                returnQA.setPOSITION_TO_CODE((c.getString(c
                        .getColumnIndex(POSITION_TO_CODE_RETURN_QA))));
                returnQA.setPOSITION_FROM_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_FROM_DESCRIPTION_RETURN_QA))));
                returnQA.setPOSITION_TO_DESCRIPTION((c.getString(c
                        .getColumnIndex(POSITION_TO_DESCRIPTION_RETURN_QA))));
                returnQA.setSTOCK_QA_CD((c.getString(c
                        .getColumnIndex(STOCK_RETURN_QA_CD))));
                returnQA.setLPN_FROM((c.getString(c
                        .getColumnIndex(LPN_FROM_RETURN_QA))));
                returnQA.setLPN_TO((c.getString(c
                        .getColumnIndex(LPN_TO_RETURN_QA))));
                returnQA.setLPN_CODE((c.getString(c
                        .getColumnIndex(LPN_CODE_RETURN_QA))));
                returnQA.setNOTE((c.getString(c
                        .getColumnIndex(NOTE_RETURN_QA))));
                returnQAs.add(returnQA);
            } while (c.moveToNext());
        }

        c.close();
        return returnQAs;
    }


    public int updateProduct_Return_QA(Product_Return_QA returnQA, String incre_so, String PRODUCT_CD, String sl, String ea_unit, String stock, String cd) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CD_RETURN_QA, PRODUCT_CD);
        values.put(PRODUCT_CODE_RETURN_QA, returnQA.getPRODUCT_CODE());
        values.put(PRODUCT_NAME_RETURN_QA, returnQA.getPRODUCT_NAME());
        values.put(EXPIRED_DATE_RETURN_QA, returnQA.getEXPIRED_DATE());
        values.put(EA_UNIT_RETURN_QA, returnQA.getUNIT());
        values.put(QTY_SET_AVAILABLE_RETURN_QA, sl);
        values.put(STOCK_RETURN_QA_CD, cd);

        // updating row
        return db.update(O_RETURN_QA, values,  AUTOINCREMENT_RETURN_QA + " = ?",
                new String[]{String.valueOf(incre_so)});

    }

    public int updateNote_RETURN_QA(String incre_so  , String note) {
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        ContentValues values = new ContentValues();

        values.put(NOTE_RETURN_QA, note);

        // updating row
        return db.update(O_RETURN_QA, values,  AUTOINCREMENT_RETURN_QA + " = ?",
                new String[]{String.valueOf(incre_so)});

    }


    public void deleteProduct_Return_QA( String cd) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = sInstance.getWritableDatabase(DatabaseHelper.PWD);
        db.execSQL("delete from " + O_RETURN_QA);
    }

    //END TABLE O_RETURN_QA




}
