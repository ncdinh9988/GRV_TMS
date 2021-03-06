package com.FiveSGroup.TMS.Warehouse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.Inventory.InventoryListProduct;
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.StockTransfer.ListStockTransfer;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class ListQrcode extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    //ProductListViewAdapter productListViewAdapter;
    ProductAdapter productListViewAdapter;
    RecyclerView listViewProduct;
    String value1 = "";
    String batch = "";
    String positonReceive = "";
    String productCd = "";
    String stock = "";
    String pro_code = "";
    String pro_name = "";
    String pro_cd = "";
    String expDate = "";
    String cont = "";
    String expDate1 = "";
    String stock_in = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "" , id_unique_SI = "" , vitri = "";


    String clickShowListCode = "";

    String lpn = "";

    int result;
    String saleCode = CmnFns.readDataAdmin();
    int statusGetCust;
    Product_Qrcode product_qrcode;

    ArrayList<Product_Qrcode> qrcode1;
    ArrayList<Batch_number_Tam> batch_number ;
//    ArrayList<Product_PutAway> putaway;
    CheckEventbus eventbus;

    TextView tvTitle;
    //PutAwayListAdapter putAwayListAdapter;

//    public static PutAwayAdapter putAwayListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_qrcode);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getDataFromIntent();

        init();
        btnscan_barcode.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        btnok.setOnClickListener(this);

        prepareData();


    }


    private void prepareData() {

            tvTitle.setText("Danh S??ch SP Nh???p Kho");
            //D??nh cho nh???p kho ch??? g???i l??n qrcode
            if (clickShowListCode != null) {
                qrcode1 = DatabaseHelper.getInstance().getAllProduct_Qrcode(global.getStockReceiptCd());
                productListViewAdapter = new ProductAdapter(this, qrcode1);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                listViewProduct.setLayoutManager(layoutManager);
                listViewProduct.setAdapter(productListViewAdapter);
                return;
            }

        if(stock_in!=null){
            if (positonReceive == null) {
                alert_show_SP_Stock_in();
            }
        }

            qrcode1 = DatabaseHelper.getInstance().getAllProduct_Qrcode(global.getStockReceiptCd());
            if (qrcode1 != null) {
                //productListViewAdapter = new ProductListViewAdapter(qrcode1, this);
                productListViewAdapter = new ProductAdapter(this, qrcode1);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                listViewProduct.setLayoutManager(layoutManager);
                listViewProduct.setAdapter(productListViewAdapter);
                productListViewAdapter.notifyDataSetChanged();
                ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        Toast.makeText(ListQrcode.this, "on Move", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                        LayoutInflater factory = LayoutInflater.from(ListQrcode.this);
                        View layout_cus = factory.inflate(R.layout.layout_delete, null);
                        final AlertDialog dialog = new AlertDialog.Builder(ListQrcode.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
                        InsetDrawable inset = new InsetDrawable(back, 64);
                        dialog.getWindow().setBackgroundDrawable(inset);
                        dialog.setView(layout_cus);

                        Button btnNo = layout_cus.findViewById(R.id.btnNo);
                        Button btnYes = layout_cus.findViewById(R.id.btnYes);
                        TextView textView = layout_cus.findViewById(R.id.tvTextBack);


                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Khi nh???n no d??? li???u s??? tr??? v??? ????n v??? tr?????c ???? c???n ph???i chuy???n t???i m??n h??nh ch??nh n??.
                                dialog.dismiss();
                                finish();
                                Intent i = new Intent(ListQrcode.this,ListQrcode.class);
                                startActivity(i);

                            }
                        });
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Remove swiped item from list and notify the RecyclerView
                                dialog.dismiss();

                                int position = viewHolder.getAdapterPosition();
                                Product_Qrcode product = qrcode1.get(position);
                                qrcode1.remove(position);
                                DatabaseHelper.getInstance().deleteProduct_PO_Specific(product.getAUTOINCREMENT());
                                productListViewAdapter.notifyItemRemoved(position);
                            }
                        });
                        dialog.show();


                    }
                };
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                itemTouchHelper.attachToRecyclerView(listViewProduct);
            }
//        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        value1 = intent.getStringExtra("btn1");
        batch = intent.getStringExtra("batch");
        vitri = intent.getStringExtra("vitri");
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
        pro_cd = intent.getStringExtra("pro_cd");
        positonReceive = intent.getStringExtra("returnposition");
        productCd = intent.getStringExtra("returnCD");
        stock = intent.getStringExtra("returnStock");
        expDate = intent.getStringExtra("exp_date");
        cont = intent.getStringExtra("cont");
        expDate1 = intent.getStringExtra("expdate");
        stock_in = intent.getStringExtra("stock_in");
        ea_unit = intent.getStringExtra("ea_unit");
        id_unique_SI = intent.getStringExtra("id_unique_SI");


        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        stockinDate = intent.getStringExtra("stockin_date");
        lpn = intent.getStringExtra("lpn");
        clickShowListCode = intent.getStringExtra("clickShowListCode");

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();

    }


    private boolean isQuanityZero() {
        boolean check = false;
//        if (put_away != null) {
//            List<Product_PutAway> product = DatabaseHelper.getInstance().getAllProduct_PutAway();
//            for (int i = 0; i < product.size(); i++) {
//                Product_PutAway putAway = product.get(i);
//                String valueQty = putAway.getQTY_SET_AVAILABLE();
//                if ((valueQty.equals("0") || (valueQty.equals("")) || (valueQty.equals("00")) || (valueQty.equals("000")) || (valueQty.equals("0000")) || (valueQty.equals("00000")))) {
//                    check = true;
//                }
//            }
//        } else {
            List<Product_Qrcode> product = DatabaseHelper.getInstance().getAllProduct_Qrcode(global.getStockReceiptCd());
            for (int i = 0; i < product.size(); i++) {
                Product_Qrcode putAway = product.get(i);
                String valueQty = putAway.getSL_SET();
                if ((valueQty.equals("0") || (valueQty.equals("")) || (valueQty.equals("00")) || (valueQty.equals("000")) || (valueQty.equals("0000")) || (valueQty.equals("00000")))) {
                    check = true;
                }
//            }
        }

        if (check == true) {
            return true;
        } else {
            return false;
        }
    }

//    private boolean isNotScanFromOrTo() {
//        boolean check = false;
//        if (put_away != null) {
//            List<Product_PutAway> product = DatabaseHelper.getInstance().getAllProduct_PutAway();
//
//            for (int i = 0; i < product.size(); i++) {
//                Product_PutAway putAway = product.get(i);
//                String value0 = "---";
//                String valueAm1 = "-1";
//                String valueFromCode = putAway.getPOSITION_FROM_CODE();
//                String valueToCode = putAway.getPOSITION_TO_CODE();
//                String positionCode = putAway.getPOSITION_FROM_CODE();
//                String valueCode = putAway.getLPN_CODE();
//                if (valueFromCode.equals("") || valueFromCode.equals("---")) {
//                    if ((positionCode.equals(value0) || (positionCode.equals(valueAm1)))) {
//                        check = true;
//                    }
//                } else {
//                    if ((valueFromCode.equals("")) || (valueToCode.equals("---"))) {
//                        check = true;
//                    }
//                }
//            }
//            if (check == true) {
//                return check;
//            } else {
//                return false;
//            }
//        }
//        return check;
//    }

    private boolean isNotScanFromOrToWarehouse() {
        boolean check = false;
        List<Product_Qrcode> product = DatabaseHelper.getInstance().getAllProduct_Qrcode(global.getStockReceiptCd());

        for (int i = 0; i < product.size(); i++) {
            Product_Qrcode product_qrcode = product.get(i);
            String value0 = "---";
            String valueAm1 = "-1";
            String valueToCode = product_qrcode.getPRODUCT_TO();
            String positionCode = product_qrcode.getPOSITION_CODE();
            if (valueToCode.equals("")) {
                if (positionCode.equals("") || positionCode.equals(value0) || positionCode.equals(valueAm1)) {
                    check = true;
                }
            }
        }
        if (check == true) {
            return check;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void init() {
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setText("Tr??? V???");
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
    }

    private void actionBack() {
//        if (put_away == null) {
//            LayoutInflater factory = LayoutInflater.from(ListQrcode.this);
//            View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
//            final AlertDialog dialog = new AlertDialog.Builder(ListQrcode.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
//            InsetDrawable inset = new InsetDrawable(back, 64);
//            dialog.getWindow().setBackgroundDrawable(inset);
//            dialog.setView(layout_cus);
//
//            Button btnNo = layout_cus.findViewById(R.id.btnNo);
//            Button btnYes = layout_cus.findViewById(R.id.btnYes);
//            TextView textView = layout_cus.findViewById(R.id.tvTextBack);
//
//
//            btnNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//
//                }
//            });
//            btnYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    DatabaseHelper.getInstance().deleteAllProduct_Qrcode();
//                    DatabaseHelper.getInstance().deleteallEa_Unit();
//                    dialog.dismiss();
//                    Intent intent = new Intent(ListQrcode.this, HomeQRActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            });
//            dialog.show();
            Intent intent = new Intent(ListQrcode.this, HomeQRActivity.class);
            startActivity(intent);
            finish();
//        } else {
//            try {
//                LayoutInflater factory = LayoutInflater.from(ListQrcode.this);
//                View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
//                final AlertDialog dialog = new AlertDialog.Builder(ListQrcode.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
//                InsetDrawable inset = new InsetDrawable(back, 64);
//                dialog.getWindow().setBackgroundDrawable(inset);
//                dialog.setView(layout_cus);
//
//                Button btnNo = layout_cus.findViewById(R.id.btnNo);
//                Button btnYes = layout_cus.findViewById(R.id.btnYes);
//                TextView textView = layout_cus.findViewById(R.id.tvTextBack);
//
//
//                btnNo.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//
//                    }
//                });
//                btnYes.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DatabaseHelper.getInstance().deleteProduct_PutAway();
//                        DatabaseHelper.getInstance().deleteallEa_Unit();
//                        dialog.dismiss();
//                        Intent intent = new Intent(ListQrcode.this, MainWareHouseActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//                dialog.show();
//            } catch (Exception e) {
//                Log.e("Exception", e.getMessage());
//            }
//
//        }
    }

    private void synchronizeToServer() {

        Dialog dialog = new Dialog(ListQrcode.this);
        try {

            if (qrcode1.size() > 0) {
                if (isNotScanFromOrToWarehouse()) {
                    dialog.showDialog(ListQrcode.this, "Ch??a c?? VT T??? ho???c VT ?????n");

                } else if (isQuanityZero()) {
                    dialog.showDialog(ListQrcode.this, "S??? l?????ng SP kh??ng ???????c b???ng 0");

                } else {

                    result = new CmnFns().synchronizeStockReceiptChecked(ListQrcode.this,saleCode);

                    if (result >= 1) {
                        DatabaseHelper.getInstance().deleteallBatch_Number();
                        DatabaseHelper.getInstance().deleteProduct_Qrcode(global.getStockReceiptCd());
                        ShowSuccessMessage("L??u th??nh c??ng");
                    } else {
                        if (result == -2) {
                            dialog.showDialog(ListQrcode.this, "S??? l?????ng kh??ng ????? trong t???n kho");
                        } else if (result == -3) {
                            dialog.showDialog(ListQrcode.this, "V??? tr?? t??? kh??ng h???p l???");
                        } else if (result == -4) {
                            dialog.showDialog(ListQrcode.this, "Tr???ng th??i c???a phi???u kh??ng h???p l???");
                        } else if (result == -5) {
                            dialog.showDialog(ListQrcode.this, "V??? tr?? t??? tr??ng v??? tr?? ????n");
                        } else if (result == -6) {
                            dialog.showDialog(ListQrcode.this, "V??? tr?? ?????n kh??ng h???p l???");
                        } else if (result == -7) {
                            dialog.showDialog(ListQrcode.this, "C???p nh???t tr???ng th??i th???t b???i");
                        } else if (result == -8) {
                            dialog.showDialog(ListQrcode.this, "S???n ph???m kh??ng c?? th??ng tin tr??n phi???u ");
                        } else if (result == -13) {
                            dialog.showDialog(ListQrcode.this, "D??? li???u kh??ng h???p l???");
                        } else if (result == -24) {
                            dialog.showDialog(ListQrcode.this, "Vui L??ng Ki???m Tra L???i S??? L?????ng");
                        }else if (result == -25) {
                            dialog.showDialog(ListQrcode.this, "S??? L?????ng V?????t Qu?? Y??u C???u Tr??n PO");
                        }else if (result == -26) {
                            dialog.showDialog(ListQrcode.this, "Vui L??ng Ki???m Tra L???i S??? L?????ng");

                        }
                        else if (result == -35) {
                            dialog.showDialog(ListQrcode.this, "C?? S???n Ph???m Sai ??TV");

                        }
                        else {
                            dialog.showDialog(ListQrcode.this, "L??u th???t b???i");
                        }

                    }
                }
            } else {
                dialog.showDialog(ListQrcode.this, "Kh??ng c?? s???n ph???m");

            }
        } catch (Exception e) {

        }

    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(ListQrcode.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(ListQrcode.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 64);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.setView(layout_cus);
        dialog.setCancelable(false);

        Button btnClose = layout_cus.findViewById(R.id.btnHuy);
        TextView textView = layout_cus.findViewById(R.id.tvText);
        btnClose.setText("OK");


        textView.setText(message);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                if (put_away != null) {
//                    DatabaseHelper.getInstance().deleteProduct_PutAway();
//                    putaway.clear();
//                    putAwayListAdapter.notifyDataSetChanged();
//
//                    Intent intentToHomeQRActivity = new Intent(ListQrcode.this, Wv_ShowResultQrode.class);
//                    intentToHomeQRActivity.putExtra("result_WPA", result);
//                    intentToHomeQRActivity.putExtra("type_WPA", "WPA");
//
//                    startActivity(intentToHomeQRActivity);
//
//                    finish();
//                } else {
                    DatabaseHelper.getInstance().deleteProduct_Qrcode(global.getStockReceiptCd());
                    qrcode1.clear();
                    productListViewAdapter.notifyDataSetChanged();
                    Intent intentToHomeQRActivity = new Intent(ListQrcode.this, HomeQRActivity.class);
                    startActivity(intentToHomeQRActivity);

                    finish();
                }
//            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan_Barcode:
                DatabaseHelper.getInstance().deleteallEa_Unit();
                DatabaseHelper.getInstance().deleteallExp_date();
//                if (put_away != null) {
//                    Intent intent = new Intent(ListQrcode.this, Qrcode_PutAway.class);
//                    intent.putExtra("check_to_finish_at_list", "check");
//                    startActivity(intent);
//                    finish();
//                } else {
                    Intent intent = new Intent(ListQrcode.this, Qrcode.class);
                    intent.putExtra("check_to_finish_at_list", "check");
                    startActivity(intent);
                    finish();
//                }
                break;

            case R.id.buttonBack:
                actionBack();
                break;
            case R.id.buttonOK:
//                if(put_away!=null){
//                    actionSyn();
//                }else{
                    synchronizeToServer();
//                }
                break;
        }
    }




    @Override
    protected void onResume() {

        super.onResume();
    }


    public void alert_show_position_warehouse(int isLPN) {
        String positionTo = "";
        String positionFrom = "";
        ArrayList<Product_Qrcode> product_qrcodes = new ArrayList<>();
        product_qrcodes = DatabaseHelper.getInstance().getAllProduct_Qrcode_Sync(stock);
        for (int i = 0; i < product_qrcodes.size(); i++) {
            Product_Qrcode product_qrcode = product_qrcodes.get(i);
            if (productCd.equals(product_qrcode.getPRODUCT_CD()) &&
                    expDate.equals(product_qrcode.getEXPIRED_DATE()) &&
                    stockinDate.equals(product_qrcode.getSTOCKIN_DATE()) &&
                    ea_unit.equals(product_qrcode.getEA_UNIT())) {
                if (!product_qrcode.getPOSITION_CODE().equals("")) {
                    positionTo = product_qrcode.getPOSITION_CODE();
                }

                // if n??y l?? ????? tr??? l???i gi?? tr??? from v?? to n???u ng?????i d??ng mu???n qu??t l???i VTT v?? VT??
                if (positonReceive.equals("1")) {
                    if (!positionTo.equals("") && !positionFrom.equals("")) {
                        positionFrom = "";
                    }
                } else {
                    if (!positionTo.equals("") && !positionFrom.equals("")) {
                        positionTo = "";
                    }
                }
            }
        }
        try {
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo(id_unique_SI ,CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate, ea_unit, stockinDate, positionFrom, positionTo, "WSI", isLPN);

            Dialog dialog = new Dialog(ListQrcode.this);

            if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                dialog.showDialog(ListQrcode.this, "Vui L??ng Th??? L???i");

            } else if (postitionDes.equals("-9")) {
                dialog.showDialog(ListQrcode.this, "V??? tr?? kh??ng h???p l???");

            } else if (postitionDes.equals("-3")) {
                dialog.showDialog(ListQrcode.this, "V??? tr?? t??? kh??ng h???p l???");

            } else if (postitionDes.equals("-6")) {
                dialog.showDialog(ListQrcode.this, "V??? tr?? ?????n kh??ng h???p l???");

            } else if (postitionDes.equals("-5")) {
                dialog.showDialog(ListQrcode.this, "V??? tr?? t??? tr??ng v??? tr?? ?????n");

            } else if (postitionDes.equals("-14")) {
                dialog.showDialog(ListQrcode.this, "V??? tr?? ?????n tr??ng v??? tr?? t???");

            } else if (postitionDes.equals("-15")) {
                dialog.showDialog(ListQrcode.this, "V??? tr?? t??? kh??ng c?? trong h??? th???ng");

            } else if (postitionDes.equals("-10")) {
                dialog.showDialog(ListQrcode.this, "M?? LPN kh??ng c?? trong h??? th???ng");

            } else if (postitionDes.equals("-17")) {
                dialog.showDialog(ListQrcode.this, "LPN t??? tr??ng LPN ?????n");

            } else if (postitionDes.equals("-18")) {
                dialog.showDialog(ListQrcode.this, "LPN ?????n tr??ng LPN t???");

            } else if (postitionDes.equals("-19")) {
                dialog.showDialog(ListQrcode.this, "V??? tr?? ?????n kh??ng c?? trong h??? th???ng");

            } else if (postitionDes.equals("-12")) {
                dialog.showDialog(ListQrcode.this, "M?? LPN kh??ng c?? trong t???n kho");

            }else if (postitionDes.equals("-27")) {
                dialog.showDialog(ListQrcode.this, "V??? tr?? t??? ch??a c?? s???n ph???m");

            }else if (postitionDes.equals("-28")) {
                dialog.showDialog(ListQrcode.this, "LPN ?????n c?? v??? tr?? kh??ng h???p l???");

            } else {
                return;
            }
        }catch (Exception e){
            Toast.makeText(this,"Vui L??ng Th??? L???i ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }

    }



    public void alert_show_SP_Stock_in() {
        try {
            SharedPreferences sharedPreferencess = getSharedPreferences("vitriPO", Context.MODE_PRIVATE);
            String vitri1 = sharedPreferencess.getString("vitri", "");
            SharedPreferences sharedPreferences = getSharedPreferences("stockReceipt", Context.MODE_PRIVATE);
            String stockReceipt = sharedPreferences.getString("stock", "");
            int statusGetCust ;
            if((batch!= null) && (!batch.equals(""))){
                batch_number = DatabaseHelper.getInstance().getoneBatch(vitri1);
                String pro_cd = batch_number.get(0).getPRODUCT_CD();
                String pro_code = batch_number.get(0).getPRODUCT_CODE();
                String pro_name = batch_number.get(0).getPRODUCT_NAME();
                String unit = batch_number.get(0).getUNIT();
                String stk_date = batch_number.get(0).getSTOCKIN_DATE();
                String exp_date = batch_number.get(0).getEXPIRED_DATE();
                String position_code = batch_number.get(0).getPOSITION_CODE();
                String position_description = batch_number.get(0).getPOSITION_DESCRIPTION();
                String warehouse_position_cd = batch_number.get(0).getWAREHOUSE_POSITION_CD();

                Product_Qrcode qrcode1 = new Product_Qrcode();
                qrcode1.setPRODUCT_CD(pro_cd);
                qrcode1.setPRODUCT_CODE(pro_code);
                qrcode1.setPRODUCT_NAME(pro_name);
                qrcode1.setEA_UNIT(unit);
                qrcode1.setSTOCKIN_DATE(stk_date);
                qrcode1.setEXPIRED_DATE(exp_date);
                qrcode1.setSTOCK_RECEIPT_CD(global.getStockReceiptCd());
                qrcode1.setBATCH_NUMBER(batch);


                qrcode1.setWAREHOUSE_POSITION_CD(warehouse_position_cd);
                qrcode1.setCREATE_TIME(CmnFns.getTimeCreate());
                qrcode1.setPOSITION_CODE(position_code);
                qrcode1.setPOSITION_DESCRIPTION(position_description);
                qrcode1.setSL_SET("");
                qrcode1.setPRODUCT_FROM("");
                qrcode1.setPRODUCT_TO("");


                DatabaseHelper.getInstance().CreateProduct_Qrcode(qrcode1);
                DatabaseHelper.getInstance().deleteallBatch_Number();

            }else{
                DatabaseHelper.getInstance().deleteallProduct_S_P();
                 statusGetCust = new CmnFns().synchronizeGETProductInfo(saleCode ,value1, stockReceipt, expDate, stockinDate,
                        ea_unit, positonReceive , cont ,pro_code , pro_name ,pro_cd);
                Dialog dialog = new Dialog(ListQrcode.this);
                if (statusGetCust == 1) {
                    return;
                } else if (statusGetCust == -1) {
                    dialog.showDialog(ListQrcode.this, "Vui L??ng Th??? L???i");
                } else if (statusGetCust == -8) {
                    dialog.showDialog(ListQrcode.this, "M?? s???n ph???m kh??ng c?? tr??n phi???u");
                } else if (statusGetCust == -20) {
                    dialog.showDialog(ListQrcode.this, "M?? s???n ph???m kh??ng c?? trong h??? th???ng");
                }
            }


        }catch (Exception e){
            Toast.makeText(this,"Vui L??ng Th??? L???i ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }

    }

}
