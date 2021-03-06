package com.FiveSGroup.TMS.PutAway;

import android.app.AlertDialog;
import android.content.Intent;
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
import com.FiveSGroup.TMS.LetDown.LetDownActivity;
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.PickList.ListPickList;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.Warehouse.Wv_ShowResultQrode;

import java.util.ArrayList;
import java.util.List;

public class List_PutAway extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok, buttonBackList;
    ImageButton btnscan_barcode;
    RecyclerView listViewProduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String batch_number = "";
    String stock = "";
    String expDate = "";
    String expDate1 = "";
    String put_away = "";
    String pro_cd = "";
    String key = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String id_unique_PAW = "";
    String pro_code = "";
    String pro_name = "";
    TextView tvTitle;
    String lpn = "";
    int result;


    ArrayList<Product_PutAway> putAways;

    PutAwayAdapter putAwayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_qrcode);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getValueFromIntent();

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();

        init();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        prepareData();
    }

    private void prepareData() {
            if (positonReceive == null) {
                if (key == null || key.equals("")) {
                    if (lpn != null && value1 != null) {
                        alert_show_SP(1);
                    } else if (lpn == null && value1 != null) {
                        alert_show_SP(0);
                    }
                } else {
                    Dialog dialog = new Dialog(List_PutAway.this);
                    dialog.showDialog(List_PutAway.this, "M?? S???n Ph???m Kh??ng C?? Trong Kho");
                }
            } else {
                if (lpn != null && value1 != null) {
                    alert_show_position(1);
                } else if (lpn == null && value1 != null) {
                    alert_show_position(0);
                }
            }

        putAways = DatabaseHelper.getInstance().getAllProduct_PutAway();
        putAwayAdapter = new PutAwayAdapter(this, putAways);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(putAwayAdapter);
        putAwayAdapter.notifyDataSetChanged();
        put_away = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(List_PutAway.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(List_PutAway.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(List_PutAway.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(List_PutAway.this, List_PutAway.class);
                        startActivity(i);

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();
                        //Remove swiped item from list and notify the RecyclerView
                        int position = viewHolder.getAdapterPosition();
                        Product_PutAway product = putAways.get(position);
                        putAways.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_PutAway_Specific(product.getAUTOINCREMENT());
                        putAwayAdapter.notifyItemRemoved(position);
                    }
                });
                dialog.show();


            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listViewProduct);
    }

    private void init() {
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBackList = findViewById(R.id.buttonBackList);
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Danh S??ch SP Put Away");
        btnscan_barcode.setOnClickListener(this);

//        buttonBackList.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(this);
        buttonBackList.setOnClickListener(this);

        btnok.setOnClickListener(this);
    }


    private void getValueFromIntent() {
        Intent intent = getIntent();
        // value1 : gi?? tr??? barcode truy???n t??? PutaWayQrCodeActivity
        value1 = intent.getStringExtra("btn1");
        // x??c ?????nh v??? tr?? l?? from hay to
        positonReceive = intent.getStringExtra("returnposition");
        productCd = intent.getStringExtra("returnCD");
        stock = intent.getStringExtra("returnStock");
        pro_cd = intent.getStringExtra("pro_cd");
        key = intent.getStringExtra("key");
        // expDate - hi???n th??? HSD cho ng?????i d??ng trong list s???n ph???m
        expDate = intent.getStringExtra("exp_date");
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
        //  expdate1 x??? l?? position from - to
        expDate1 = intent.getStringExtra("expdate");
        put_away = intent.getStringExtra("put_away");
        id_unique_PAW = intent.getStringExtra("id_unique_PAW");
        // ea_unit : ????n v??? tr??? v??? t??? PutaWayQrCodeActivity
        ea_unit = intent.getStringExtra("ea_unit");
        lpn = intent.getStringExtra("lpn");
        batch_number = intent.getStringExtra("batch_number");
        try {
            if (batch_number.equals("---")) {
                batch_number = "";
            }
            if (batch_number == null) {
                batch_number = "";
            }
        } catch (Exception e) {

        }
        stockinDate = intent.getStringExtra("stockin_date");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        try {
            if (expDate.equals("---")){
                expDate = "";
            }
            if(expDate==null){
                expDate = "";
            }
        }catch (Exception e){

        }

        try {
            if (stockinDate.equals("---")){
                stockinDate = "";
            }
            if(stockinDate==null){
                stockinDate = "";
            }
        }catch (Exception e){

        }
    }


    private void ShowNoti() {
        try {
            LayoutInflater factory = LayoutInflater.from(List_PutAway.this);
            View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
            final AlertDialog dialog = new AlertDialog.Builder(List_PutAway.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                    dialog.dismiss();

                }
            });
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper.getInstance().deleteProduct_PutAway();
                    DatabaseHelper.getInstance().deleteallEa_Unit();
                    DatabaseHelper.getInstance().deleteallExp_date();
                    dialog.dismiss();
                    Intent intent = new Intent(List_PutAway.this, MainWareHouseActivity.class);
                    startActivity(intent);
                    finish();
                    //  }

                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }


    private void SynchronizeToServer() {

        Dialog dialog = new Dialog(List_PutAway.this);

        if (put_away != null) {
            String saleCode = CmnFns.readDataAdmin();

            if (putAways.size() > 0) {
                if (isNotScanFromOrTo()) {

                    dialog.showDialog(List_PutAway.this, "Ch??a c?? VT T??? ho???c VT ?????n");

                } else if (isQuanityZero()) {
                    dialog.showDialog(List_PutAway.this, "S??? l?????ng SP kh??ng ???????c b???ng 0");

                } else {
                    result = new CmnFns().synchronizeData(saleCode, "WPA", "");
                    if (result >= 1) {
                        ShowSuccessMessage("L??u th??nh c??ng");
                    } else {

                        if (result == -2) {
                            dialog.showDialog(List_PutAway.this, "S??? l?????ng kh??ng ????? trong t???n kho");
                        } else if (result == -3) {
                            dialog.showDialog(List_PutAway.this, "V??? tr?? t??? kh??ng h???p l???");
                        } else if (result == -4) {
                            dialog.showDialog(List_PutAway.this, "Tr???ng th??i c???a phi???u kh??ng h???p l???");
                        } else if (result == -5) {
                            dialog.showDialog(List_PutAway.this, "V??? tr?? t??? tr??ng v??? tr?? ????n");
                        } else if (result == -6) {
                            dialog.showDialog(List_PutAway.this, "V??? tr?? ?????n kh??ng h???p l???");
                        } else if (result == -7) {
                            dialog.showDialog(List_PutAway.this, "C???p nh???t tr???ng th??i th???t b???i");
                        } else if (result == -8) {
                            dialog.showDialog(List_PutAway.this, "S???n ph???m kh??ng c?? th??ng tin tr??n phi???u ");
                        } else if (result == -13) {
                            dialog.showDialog(List_PutAway.this, "D??? li???u kh??ng h???p l???");

                        } else if (result == -24) {
                            dialog.showDialog(List_PutAway.this, "Vui L??ng Ki???m Tra L???i S??? L?????ng");

                        } else if (result == -26) {
                            dialog.showDialog(List_PutAway.this, "S??? L?????ng V?????t Qu?? Y??u C???u Tr??n SO");

                        } else {
                            dialog.showDialog(List_PutAway.this, "L??u th???t b???i");
                        }

                    }
                }
            } else {
                dialog.showDialog(List_PutAway.this, "Kh??ng c?? s???n ph???m");

            }


        }
    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(List_PutAway.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(List_PutAway.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
//                        Toast.makeText(getApplication(), "L??u th??nh c??ng", Toast.LENGTH_SHORT).show();
                DatabaseHelper.getInstance().deleteProduct_PutAway();
                putAways.clear();
                putAwayAdapter.notifyDataSetChanged();

                Intent intentToHomeQRActivity = new Intent(List_PutAway.this, Wv_ShowResultQrode.class);
                intentToHomeQRActivity.putExtra("result_WPA", result);
                intentToHomeQRActivity.putExtra("type_WPA", "WPA");
                startActivity(intentToHomeQRActivity);
                finish();
            }
        });
        dialog.show();
    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<Product_PutAway> product = DatabaseHelper.getInstance().getAllProduct_PutAway();
        for (int i = 0; i < product.size(); i++) {
            Product_PutAway product_putAway = product.get(i);
            String valueQty = product_putAway.getQTY_SET_AVAILABLE();
            if ((valueQty.equals("0") || (valueQty.equals("")) || (valueQty.equals("00")) || (valueQty.equals("000")) || (valueQty.equals("0000")) || (valueQty.equals("00000")))) {
                check = true;
            }
        }


        if (check == true) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private boolean isNotScanFromOrTo() {
        boolean check = false;
        List<Product_PutAway> product = DatabaseHelper.getInstance().getAllProduct_PutAway();

        for (int i = 0; i < product.size(); i++) {
            Product_PutAway putAway = product.get(i);
            String value0 = "---";
            String valueFromCode = putAway.getPOSITION_FROM_CODE();
            String valueToCode = putAway.getPOSITION_TO_CODE();
            String lpn_from = putAway.getLPN_FROM();
            String lpn_to = putAway.getLPN_TO();

            if ((valueFromCode.equals("") || valueFromCode.equals(value0)) && (lpn_from.equals(""))) {
                check = true;
            }
            if ((valueToCode.equals("") || valueToCode.equals(value0)) && (lpn_to.equals(""))) {
                check = true;
            }
        }
        if (check == true) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan_Barcode:
                DatabaseHelper.getInstance().deleteallEa_Unit();
                DatabaseHelper.getInstance().deleteallExp_date();
                if (put_away != null) {
                    Intent intent = new Intent(List_PutAway.this, Qrcode_PutAway.class);
                    intent.putExtra("check_to_finish_at_list", "check");
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.buttonBack:
                ShowNoti();

                break;
            case R.id.buttonOK:
                actionSyn();
                break;
        }
    }

    private void actionSyn() {
        try {
            LayoutInflater factory = LayoutInflater.from(List_PutAway.this);
            View layout_cus = factory.inflate(R.layout.layout_request, null);
            final AlertDialog dialog = new AlertDialog.Builder(List_PutAway.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                    dialog.dismiss();

                }
            });
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    SynchronizeToServer();
                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

    }

    public void alert_show_position(int isLPN) {
        String positionTo = "";
        String positionFrom = "";
        ArrayList<Product_PutAway> putAways = new ArrayList<>();
        putAways = DatabaseHelper.getInstance().getAllProduct_PutAway_Sync();
        for (int i = 0; i < putAways.size(); i++) {
            Product_PutAway putAway = putAways.get(i);
            if (productCd.equals(putAway.getPRODUCT_CD_PUTAWAY()) &&
                    expDate1.equals(putAway.getEXPIRED_DATE_PUTAWAY()) &&
                    stockinDate.equals(putAway.getSTOCKIN_DATE_PUTAWAY()) &&
                    ea_unit_position.equals(putAway.getEA_UNIT_PUTAWAY())) {

                if (!putAway.getLPN_FROM().equals("") || !putAway.getLPN_TO().equals("")) {
                    positionTo = putAway.getLPN_TO();
                    positionFrom = putAway.getLPN_FROM();
                }
                if (!putAway.getPOSITION_FROM_CODE().equals("") || !putAway.getPOSITION_TO_CODE().equals("")) {
                    positionTo = putAway.getPOSITION_TO_CODE();
                    positionFrom = putAway.getPOSITION_FROM_CODE();
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
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo(id_unique_PAW, CmnFns.readDataAdmin(), value1, positonReceive,
                    productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WPA", isLPN);

            ArrayList<Product_PutAway> listputAway = new ArrayList<>();
            listputAway = DatabaseHelper.getInstance().getonePosition_PutAway(id_unique_PAW);
            String position_from_cd = listputAway.get(0).getPOSITION_FROM_PUTAWAY();
            String position_to_cd = listputAway.get(0).getPOSITION_TO_PUTAWAY();

            if((!position_from_cd.equals(""))&&(!position_to_cd.equals(""))){
                String check_position = new CmnFns().Check_Position_Same_SLOC(position_from_cd,position_to_cd,"WPA");

                if(check_position.equals("Th??nh C??ng")){
                    Dialog dialog = new Dialog(List_PutAway.this);

                    if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                        dialog.showDialog(List_PutAway.this, "Vui L??ng Th??? L???i");

                    } else if (postitionDes.equals("-3")) {
                        dialog.showDialog(List_PutAway.this, "V??? tr?? t??? kh??ng h???p l???");

                    } else if (postitionDes.equals("-6")) {
                        dialog.showDialog(List_PutAway.this, "V??? tr?? ?????n kh??ng h???p l???");

                    } else if (postitionDes.equals("-5")) {
                        dialog.showDialog(List_PutAway.this, "V??? tr?? t??? tr??ng v??? tr?? ?????n");

                    } else if (postitionDes.equals("-14")) {
                        dialog.showDialog(List_PutAway.this, "V??? tr?? ?????n tr??ng v??? tr?? t???");

                    } else if (postitionDes.equals("-15")) {
                        dialog.showDialog(List_PutAway.this, "V??? tr?? t??? kh??ng c?? trong h??? th???ng");

                    } else if (postitionDes.equals("-10")) {
                        dialog.showDialog(List_PutAway.this, "M?? LPN kh??ng c?? trong h??? th???ng");

                    } else if (postitionDes.equals("-17")) {
                        dialog.showDialog(List_PutAway.this, "LPN t??? tr??ng LPN ?????n");

                    } else if (postitionDes.equals("-18")) {
                        dialog.showDialog(List_PutAway.this, "LPN ?????n tr??ng LPN t???");

                    } else if (postitionDes.equals("-19")) {
                        dialog.showDialog(List_PutAway.this, "V??? tr?? ?????n kh??ng c?? trong h??? th???ng");

                    } else if (postitionDes.equals("-12")) {
                        dialog.showDialog(List_PutAway.this, "M?? LPN kh??ng c?? trong t???n kho");

                    } else if (postitionDes.equals("-27")) {
                        dialog.showDialog(List_PutAway.this, "V??? tr?? t??? ch??a c?? s???n ph???m");

                    } else if (postitionDes.equals("-28")) {
                        dialog.showDialog(List_PutAway.this, "LPN ?????n c?? v??? tr?? kh??ng h???p l???");

                    } else {
                        return;
                    }
                }else{

                    if (positonReceive.equals("1") && productCd != null) {
                        DatabaseHelper.getInstance().updatePositionFrom(id_unique_PAW,"","","","","","","" );

                    }else if (positonReceive.equals("2") && productCd != null) {
                        DatabaseHelper.getInstance().updatePositionTo(id_unique_PAW,"","","","","","","" );

                    }
                    Dialog dialog = new Dialog(List_PutAway.this);
                    dialog.showDialog(List_PutAway.this, check_position);
                }

            }else{
                Dialog dialog = new Dialog(List_PutAway.this);

                if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                    dialog.showDialog(List_PutAway.this, "Vui L??ng Th??? L???i");

                } else if (postitionDes.equals("-3")) {
                    dialog.showDialog(List_PutAway.this, "V??? tr?? t??? kh??ng h???p l???");

                } else if (postitionDes.equals("-6")) {
                    dialog.showDialog(List_PutAway.this, "V??? tr?? ?????n kh??ng h???p l???");

                } else if (postitionDes.equals("-5")) {
                    dialog.showDialog(List_PutAway.this, "V??? tr?? t??? tr??ng v??? tr?? ?????n");

                } else if (postitionDes.equals("-14")) {
                    dialog.showDialog(List_PutAway.this, "V??? tr?? ?????n tr??ng v??? tr?? t???");

                } else if (postitionDes.equals("-15")) {
                    dialog.showDialog(List_PutAway.this, "V??? tr?? t??? kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-10")) {
                    dialog.showDialog(List_PutAway.this, "M?? LPN kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-17")) {
                    dialog.showDialog(List_PutAway.this, "LPN t??? tr??ng LPN ?????n");

                } else if (postitionDes.equals("-18")) {
                    dialog.showDialog(List_PutAway.this, "LPN ?????n tr??ng LPN t???");

                } else if (postitionDes.equals("-19")) {
                    dialog.showDialog(List_PutAway.this, "V??? tr?? ?????n kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-12")) {
                    dialog.showDialog(List_PutAway.this, "M?? LPN kh??ng c?? trong t???n kho");

                } else if (postitionDes.equals("-27")) {
                    dialog.showDialog(List_PutAway.this, "V??? tr?? t??? ch??a c?? s???n ph???m");

                } else if (postitionDes.equals("-28")) {
                    dialog.showDialog(List_PutAway.this, "LPN ?????n c?? v??? tr?? kh??ng h???p l???");

                } else {
                    return;
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Vui L??ng Th??? L???i ...", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    public void alert_show_SP(int isLPN) {
        try {

            DatabaseHelper.getInstance().deleteallProduct_S_P();
            int postitionDes = new CmnFns().synchronizeGETProductByZonePutaway(List_PutAway.this, value1, CmnFns.readDataAdmin(),
                    expDate, ea_unit, stockinDate, isLPN, pro_code, pro_name, batch_number, pro_cd);
            Dialog dialog = new Dialog(List_PutAway.this);
            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(List_PutAway.this, "Vui L??ng Th??? L???i");

            } else if (postitionDes == -8) {
                dialog.showDialog(List_PutAway.this, "M?? s???n ph???m kh??ng c?? tr??n phi???u");


            } else if (postitionDes == -10) {
                dialog.showDialog(List_PutAway.this, "M?? LPN kh??ng c?? trong h??? th???ng");

            } else if (postitionDes == -11) {

                dialog.showDialog(List_PutAway.this, "M?? s???n ph???m kh??ng c?? trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(List_PutAway.this, "M?? LPN kh??ng c?? trong kho");

            } else if (postitionDes == -16) {

                dialog.showDialog(List_PutAway.this, "S???n ph???m ???? qu??t kh??ng n???m trong LPN n??o");

            } else if (postitionDes == -20) {

                dialog.showDialog(List_PutAway.this, "M?? s???n ph???m kh??ng c?? trong h??? th???ng");

            } else if (postitionDes == -21) {

                dialog.showDialog(List_PutAway.this, "M?? s???n ph???m kh??ng c?? trong zone");

            } else if (postitionDes == -22) {

                dialog.showDialog(List_PutAway.this, "M?? LPN kh??ng c?? trong zone");

            }


        } catch (Exception e) {
            Toast.makeText(this, "Vui L??ng Th??? L???i ...", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}
