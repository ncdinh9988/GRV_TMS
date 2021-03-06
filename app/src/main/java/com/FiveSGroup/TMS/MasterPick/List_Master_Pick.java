package com.FiveSGroup.TMS.MasterPick;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
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

import com.FiveSGroup.TMS.LoadPallet.LPNwithSO.LPNandSO;
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.PickList.ListPickList;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;

import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.ProductAdapter;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class List_Master_Pick extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    TextView nameproduct;
    //ProductListViewAdapter productListViewAdapter;
    ProductAdapter productListViewAdapter;
    RecyclerView listViewProduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String pro_code = "";
    String pro_name = "";
    String pro_cd = "";
    String batch_number = "";
    String stock = "";
    String expDate = "";
    String key = "";
    String expDate1 = "";
    String master_picklist = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String lpn = "";
    String unique_id = "" ;
    String vitri = "" ;


    int statusGetCust;
    Product_Master_Pick product_qrcode;

    ArrayList<Product_Master_Pick> masterPicks;
    CheckEventbus eventbus;

    Master_Pick_Adapter MasterPickAdapter;
    TextView tvTitle;

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


    private void getDataFromIntent() {
        Intent intent = getIntent();
        value1 = intent.getStringExtra("btn1");
        positonReceive = intent.getStringExtra("returnposition");
        productCd = intent.getStringExtra("returnCD");
        key = intent.getStringExtra("key");
        stock = intent.getStringExtra("returnStock");
        vitri = intent.getStringExtra("vitri");
        batch_number = intent.getStringExtra("batch_number");
        try {
            if (batch_number.equals("---")){
                batch_number = "";
            }
            if(batch_number==null){
                batch_number = "";
            }
        }catch (Exception e){

        }
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        master_picklist = intent.getStringExtra("master_picklist");
        ea_unit = intent.getStringExtra("ea_unit");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        unique_id = intent.getStringExtra("unique_id");
        lpn = intent.getStringExtra("lpn");
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
        pro_cd = intent.getStringExtra("pro_cd");

        stockinDate = intent.getStringExtra("stockin_date");


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
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    private void prepareData() {
//        if (master_picklist != null) {
            if (positonReceive == null) {
                if (key == null || key.equals("")) {
                    if (lpn != null && master_picklist != null) {
                        //TODO
                        alert_show_SP(1);
                    } else if (lpn == null && master_picklist != null) {
                        //TODO
                        alert_show_SP(0);
                    }
                } else {
                    Dialog dialog = new Dialog(List_Master_Pick.this);
                    dialog.showDialog(List_Master_Pick.this, "M?? S???n Ph???m Kh??ng C?? Trong Phi???u");
                }


            } else {
                if (lpn != null) {
                    //TODO
                    alert_show_position(1);
                } else {
                    //TODO
                    alert_show_position(0);
                }

            }
//        }
        masterPicks = DatabaseHelper.getInstance().getAllProduct_Master_Pick(global.getMasterPickCd());
        MasterPickAdapter = new Master_Pick_Adapter(this, masterPicks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(MasterPickAdapter);

        MasterPickAdapter.notifyDataSetChanged();
        master_picklist = "";

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(List_Master_Pick.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(List_Master_Pick.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(List_Master_Pick.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        //Khi nh???n no d??? li???u s??? tr??? v??? ????n v??? l?? 1 c???n ph???i chuy???n t???i m??n h??nh ch??nh n??.
                        dialog.dismiss();
                        finish();
                        Intent i = new Intent(List_Master_Pick.this,List_Master_Pick.class);
                        startActivity(i);


                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();

                        int position = viewHolder.getAdapterPosition();
                        Product_Master_Pick product = masterPicks.get(position);
                        masterPicks.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_Master_Pick_Specific(product.getAUTOINCREMENT());
                        MasterPickAdapter.notifyItemRemoved(position);
                    }
                });
                dialog.show();



            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listViewProduct);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private boolean isNotScanFromOrTo() {
        boolean check = false;
        List<Product_Master_Pick> product = DatabaseHelper.getInstance().getAllProduct_Master_Pick(global.getMasterPickCd());

        for (int i = 0; i < product.size(); i++) {
            Product_Master_Pick masterPick = product.get(i);
            String value0 = "---";
//            String valueAm1 = "-1";
            String valueFromCode = masterPick.getPOSITION_FROM_CODE();
            String valueToCode = masterPick.getPOSITION_TO_CODE();
//            String positionCode = masterPick.getPOSITION_FROM_CODE();
            String lpn_from = masterPick.getLPN_FROM();
            String lpn_to = masterPick.getLPN_TO();
//            String valueCode = masterPick.getLPN_CODE();

            if((valueFromCode.equals("") || valueFromCode.equals(value0)) && (lpn_from.equals(""))){
                check = true;
            }
            if((valueToCode.equals("") || valueToCode.equals(value0)) && (lpn_to.equals(""))){
                check = true;
            }

//            if (valueFromCode.equals("") || valueFromCode.equals("---")) {
//                if ((positionCode.equals(value0) || (positionCode.equals(valueAm1)))) {
//                    check = true;
//                }
//            } else {
//                if ((valueFromCode.equals("")) || (valueToCode.equals("---"))) {
//                    check = true;
//                }
//            }
//
//            if(lpn_from.equals("")){
//                if ((valueFromCode.equals("") || valueFromCode.equals("---"))){
//                    check = true;
//                }
//            }else{
//                if((lpn_to.equals("") && valueToCode.equals(""))){
//                    check = true ;
//                }
//            }

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


    private void init() {
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        nameproduct = findViewById(R.id.nameproduct);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setText("Tr??? V???");
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Danh S??ch SP Master Pick");
    }

    private void startScan() {
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        Intent intent = new Intent(List_Master_Pick.this, Qrcode_Master_Pick.class);
        intent.putExtra("check_to_finish_at_list", "check");
        startActivity(intent);
        finish();
    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<Product_Master_Pick> product = DatabaseHelper.getInstance().getAllProduct_Master_Pick(global.getMasterPickCd());
        for (int i = 0; i < product.size(); i++) {
            Product_Master_Pick putAway = product.get(i);
            String valueQty = putAway.getQTY();
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

    private void synchronizeToService() {
        String saleCode = CmnFns.readDataAdmin();
        Dialog dialog = new Dialog(List_Master_Pick.this);


        if (masterPicks.size() > 0) {
            if (isNotScanFromOrTo()) {
                dialog.showDialog(List_Master_Pick.this, "Ch??a c?? VT T??? ho???c VT ?????n");

            } else if (isQuanityZero()) {
                dialog.showDialog(List_Master_Pick.this, "S??? l?????ng SP kh??ng ???????c b???ng 0");

            } else {
                try {

                    int result = new CmnFns().synchronizeData(saleCode, "WMP", global.getMasterPickCd());
                    if (result >= 1) {
                        ShowSuccessMessage("L??u th??nh c??ng");
//                    Toast.makeText(getApplication(), "L??u th??nh c??ng", Toast.LENGTH_SHORT).show();

                    } else {

                        if (result == -1) {
                            dialog.showDialog(List_Master_Pick.this, "L??u th???t b???i");
                        } else if (result == -2) {
                            dialog.showDialog(List_Master_Pick.this, "S??? l?????ng kh??ng ????? trong t???n kho");

                        } else if (result == -3) {
                            dialog.showDialog(List_Master_Pick.this, "V??? tr?? t??? kh??ng h???p l???");

                        } else if (result == -4) {
                            dialog.showDialog(List_Master_Pick.this, "Tr???ng th??i c???a phi???u kh??ng h???p l???");

                        } else if (result == -5) {
                            dialog.showDialog(List_Master_Pick.this, "V??? tr?? t??? tr??ng v??? tr?? ????n");

                        } else if (result == -6) {
                            dialog.showDialog(List_Master_Pick.this, "V??? tr?? ?????n kh??ng h???p l???");

                        } else if (result == -7) {
                            dialog.showDialog(List_Master_Pick.this, "C???p nh???t tr???ng th??i th???t b???i");

                        } else if (result == -8) {
                            dialog.showDialog(List_Master_Pick.this, "S???n ph???m kh??ng c?? th??ng tin tr??n phi???u ");

                        } else if (result == -13) {
                            dialog.showDialog(List_Master_Pick.this, "D??? li???u kh??ng h???p l???");

                        }else if (result == -24) {
                            dialog.showDialog(List_Master_Pick.this, "Vui L??ng Ki???m Tra L???i S??? L?????ng");

                        }else if (result == -26) {
                            dialog.showDialog(List_Master_Pick.this, "S??? L?????ng V?????t Qu?? Y??u C???u Tr??n SO");

                        }else if (result == -31) {
                            dialog.showDialog(List_Master_Pick.this, "LPN N??y ???? ???????c s??? D???ng Cho SO Kh??c");

                        }else if (result == -34) {
                            dialog.showDialog(List_Master_Pick.this, "S???n Ph???m V???i ??VT Kh??ng ????ng Tr??n SO");

                        }else if (result == -36) {
                            dialog.showDialog(List_Master_Pick.this, "Tr??ng D??? Li???u Vui L??ng Ki???m Tra L???i");
                        }else{
                            dialog.showDialog(List_Master_Pick.this, "Vui L??ng Th??? L???i");
                        }

                    }
                }catch (Exception e){
                    Toast.makeText(this,"Vui L??ng Th??? L???i ..." ,Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        } else {
            dialog.showDialog(List_Master_Pick.this, "Kh??ng c?? s???n ph???m");

        }


    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(List_Master_Pick.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(List_Master_Pick.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                DatabaseHelper.getInstance().deleteProduct_Master_Pick();
                masterPicks.clear();
                MasterPickAdapter.notifyDataSetChanged();
                Intent intentToHomeQRActivity = new Intent(List_Master_Pick.this, Home_Master_Pick.class);
                startActivity(intentToHomeQRActivity);
                finish();
            }
        });
        dialog.show();
    }

    private void actionBack() {
        try {
            List_Master_Pick.this.finish();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan_Barcode:
                startScan();
                break;
            case R.id.buttonOK:
                synchronizeToService();
                break;
            case R.id.buttonBack:
                actionBack();
                break;
        }
    }


    public void alert_show_position(int isLPN) {
        String positionTo = "";
        String positionFrom = "";
        ArrayList<Product_Master_Pick> product_master_picks = new ArrayList<>();
        product_master_picks = DatabaseHelper.getInstance().getAllProduct_Master_Pick_Sync(global.getMasterPickCd());
        for (int i = 0; i < product_master_picks.size(); i++) {
            Product_Master_Pick master_pick = product_master_picks.get(i);
            if (productCd.equals(master_pick.getPRODUCT_CD()) &&
                    expDate1.equals(master_pick.getEXPIRED_DATE()) &&
                    stockinDate.equals(master_pick.getSTOCKIN_DATE()) &&
                    ea_unit_position.equals(master_pick.getUNIT())) {

                if (!master_pick.getLPN_FROM().equals("") || !master_pick.getLPN_TO().equals("")) {
                    positionTo = master_pick.getLPN_TO();
                    positionFrom = master_pick.getLPN_FROM();
                }
                if (!master_pick.getPOSITION_FROM_CODE().equals("") || !master_pick.getPOSITION_TO_CODE().equals("")) {
                    positionTo = master_pick.getPOSITION_TO_CODE();
                    positionFrom = master_pick.getPOSITION_FROM_CODE();
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
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo(unique_id, CmnFns.readDataAdmin(), value1, positonReceive,
                    productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WMP", isLPN);
            ArrayList<Product_Master_Pick> listproduct_master_picks = new ArrayList<>();
            listproduct_master_picks = DatabaseHelper.getInstance().getonePosition_MasterPick(unique_id);
            String position_from_cd = listproduct_master_picks.get(0).getPOSITION_FROM_CD();
            String position_to_cd = listproduct_master_picks.get(0).getPOSITION_TO_CD();

            if((!position_from_cd.equals(""))&&(!position_to_cd.equals(""))){
                String check_position = new CmnFns().Check_Position_Same_SLOC(position_from_cd,position_to_cd,"WMP");

                if(check_position.equals("Th??nh C??ng")){
                    Dialog dialog = new Dialog(List_Master_Pick.this);

                    if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                        dialog.showDialog(List_Master_Pick.this, "Vui L??ng Th??? L???i");

                    } else if (postitionDes.equals("0")) {
                        dialog.showDialog(List_Master_Pick.this, "Sai v??? tr?? g???i ??");

                    }else if (postitionDes.equals("-3")) {
                        dialog.showDialog(List_Master_Pick.this, "V??? tr?? t??? kh??ng h???p l???");

                    } else if (postitionDes.equals("-6")) {
                        dialog.showDialog(List_Master_Pick.this, "V??? tr?? ?????n kh??ng h???p l???");

                    } else if (postitionDes.equals("-5")) {
                        dialog.showDialog(List_Master_Pick.this, "V??? tr?? t??? tr??ng v??? tr?? ?????n");

                    } else if (postitionDes.equals("-14")) {
                        dialog.showDialog(List_Master_Pick.this, "V??? tr?? ?????n tr??ng v??? tr?? t???");

                    } else if (postitionDes.equals("-15")) {
                        dialog.showDialog(List_Master_Pick.this, "V??? tr?? t??? kh??ng c?? trong h??? th???ng");

                    } else if (postitionDes.equals("-10")) {
                        dialog.showDialog(List_Master_Pick.this, "M?? LPN kh??ng c?? trong h??? th???ng");

                    } else if (postitionDes.equals("-17")) {
                        dialog.showDialog(List_Master_Pick.this, "LPN t??? tr??ng LPN ?????n");

                    } else if (postitionDes.equals("-18")) {
                        dialog.showDialog(List_Master_Pick.this, "LPN ?????n tr??ng LPN t???");

                    } else if (postitionDes.equals("-19")) {
                        dialog.showDialog(List_Master_Pick.this, "V??? tr?? ?????n kh??ng c?? trong h??? th???ng");

                    } else if (postitionDes.equals("-12")) {
                        dialog.showDialog(List_Master_Pick.this, "M?? LPN kh??ng c?? trong t???n kho");

                    }else if (postitionDes.equals("-27")) {
                        dialog.showDialog(List_Master_Pick.this, "V??? tr?? t??? ch??a c?? s???n ph???m");

                    }else if (postitionDes.equals("-28")) {
                        dialog.showDialog(List_Master_Pick.this, "LPN ?????n c?? v??? tr?? kh??ng h???p l???");

                    } else {
                        return;
                    }
                }else{
                    if (positonReceive.equals("1") && productCd != null) {
                        DatabaseHelper.getInstance().updatePositionFrom_masterPick(unique_id,"","","","","","","" );

                    }else if (positonReceive.equals("2") && productCd != null) {
                        DatabaseHelper.getInstance().updatePositionTo_masterPick(unique_id,"","","","","","","" );

                    }
                    Dialog dialog = new Dialog(List_Master_Pick.this);
                    dialog.showDialog(List_Master_Pick.this, check_position);
                }
            }else{
                Dialog dialog = new Dialog(List_Master_Pick.this);

                if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                    dialog.showDialog(List_Master_Pick.this, "Vui L??ng Th??? L???i");

                } else if (postitionDes.equals("0")) {
                    dialog.showDialog(List_Master_Pick.this, "Sai v??? tr?? g???i ??");

                }else if (postitionDes.equals("-3")) {
                    dialog.showDialog(List_Master_Pick.this, "V??? tr?? t??? kh??ng h???p l???");

                } else if (postitionDes.equals("-6")) {
                    dialog.showDialog(List_Master_Pick.this, "V??? tr?? ?????n kh??ng h???p l???");

                } else if (postitionDes.equals("-5")) {
                    dialog.showDialog(List_Master_Pick.this, "V??? tr?? t??? tr??ng v??? tr?? ?????n");

                } else if (postitionDes.equals("-14")) {
                    dialog.showDialog(List_Master_Pick.this, "V??? tr?? ?????n tr??ng v??? tr?? t???");

                } else if (postitionDes.equals("-15")) {
                    dialog.showDialog(List_Master_Pick.this, "V??? tr?? t??? kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-10")) {
                    dialog.showDialog(List_Master_Pick.this, "M?? LPN kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-17")) {
                    dialog.showDialog(List_Master_Pick.this, "LPN t??? tr??ng LPN ?????n");

                } else if (postitionDes.equals("-18")) {
                    dialog.showDialog(List_Master_Pick.this, "LPN ?????n tr??ng LPN t???");

                } else if (postitionDes.equals("-19")) {
                    dialog.showDialog(List_Master_Pick.this, "V??? tr?? ?????n kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-12")) {
                    dialog.showDialog(List_Master_Pick.this, "M?? LPN kh??ng c?? trong t???n kho");

                }else if (postitionDes.equals("-27")) {
                    dialog.showDialog(List_Master_Pick.this, "V??? tr?? t??? ch??a c?? s???n ph???m");

                }else if (postitionDes.equals("-28")) {
                    dialog.showDialog(List_Master_Pick.this, "LPN ?????n c?? v??? tr?? kh??ng h???p l???");

                } else {
                    return;
                }

            }

        }catch (Exception e){
            Toast.makeText(this,"Vui L??ng Th??? L???i ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    public void alert_show_SP(int isLPN) {
        try {
            DatabaseHelper.getInstance().deleteallProduct_S_P();
            int postitionDes  = new CmnFns().synchronizeGETProductByZoneMasterPick(List_Master_Pick.this, value1, CmnFns.readDataAdmin(),
                    expDate, ea_unit, stockinDate, global.getMasterPickCd(), isLPN, batch_number,pro_code , pro_name, pro_cd);
//            int postitionDes ;
//            if(isLPN==1){
//                postitionDes = new CmnFns().synchronizeGETProductByZoneMasterPick_LPN(List_Master_Pick.this, value1, CmnFns.readDataAdmin(), expDate, ea_unit, stockinDate, global.getMasterPickCd(), isLPN, vitri);
//
//            }else{
//                 postitionDes = new CmnFns().synchronizeGETProductByZoneMasterPick(List_Master_Pick.this, value1, CmnFns.readDataAdmin(), expDate, ea_unit, stockinDate, global.getMasterPickCd(), isLPN, vitri);

//            }

            Dialog dialog = new Dialog(List_Master_Pick.this);


            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(List_Master_Pick.this, "Vui L??ng Th??? L???i");

            } else if (postitionDes == -8) {
                dialog.showDialog(List_Master_Pick.this, "M?? s???n ph???m kh??ng c?? tr??n phi???u");


            } else if (postitionDes == -10) {
                dialog.showDialog(List_Master_Pick.this, "M?? LPN kh??ng c?? trong h??? th???ng");

            } else if (postitionDes == -11) {

                dialog.showDialog(List_Master_Pick.this, "M?? s???n ph???m kh??ng c?? trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(List_Master_Pick.this, "M?? LPN kh??ng c?? trong kho");

            } else if (postitionDes == -16) {

                dialog.showDialog(List_Master_Pick.this, "S???n ph???m ???? qu??t kh??ng n???m trong LPN n??o");

            } else if (postitionDes == -20) {

                dialog.showDialog(List_Master_Pick.this, "M?? s???n ph???m kh??ng c?? trong h??? th???ng");

            } else if (postitionDes == -21) {

                dialog.showDialog(List_Master_Pick.this, "M?? s???n ph???m kh??ng c?? trong zone");

            } else if (postitionDes == -22) {

                dialog.showDialog(List_Master_Pick.this, "M?? LPN kh??ng c?? trong zone ");

            } else if (postitionDes == -31) {
                dialog.showDialog(List_Master_Pick.this, "LPN N??y ???? ???????c s??? D???ng");

            }else if (postitionDes == -33) {
                dialog.showDialog(List_Master_Pick.this, "LPN C?? S???n Ph???m V???i ??VT Kh??ng ????ng Tr??n SO");

            }
        }catch (Exception e){
            Toast.makeText(this,"Vui L??ng Th??? L???i ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }

    }


}
