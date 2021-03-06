package com.FiveSGroup.TMS.LoadPallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.FiveSGroup.TMS.LetDown.LetDownActivity;
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;

import java.util.ArrayList;
import java.util.List;

public class LoadPalletActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    LoadPalletAdapter loadPalletAdapter;
    RecyclerView listViewProduct;
    String value1 = "", positonReceive = "", productCd = "", stock = "";
    String expDate = "";
    String expDate1 = "";
    String load_pallet = "";
    String pro_code = "";
    String pro_name = "";
    String ea_unit = "";
    String pro_cd = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String batch_number = "";
    String lpn = "";
    String key = "";
    String unique_id = "" , lpn_code = "";


    ArrayList<Product_LoadPallet> loadPallets;
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
        SharedPreferences prefs = this.getSharedPreferences("lpn_code", Activity.MODE_PRIVATE);
        lpn_code = prefs.getString("lpn_code", "");

        btnscan_barcode.setOnClickListener(this);

        buttonBack.setOnClickListener(this);
        btnok.setOnClickListener(this);

        prepareData();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        value1 = intent.getStringExtra("btn1");
        positonReceive = intent.getStringExtra("returnposition");
        unique_id = intent.getStringExtra("unique_id");
        productCd = intent.getStringExtra("returnCD");
        stock = intent.getStringExtra("returnStock");
        key = intent.getStringExtra("key");
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        pro_code = intent.getStringExtra("pro_code");
        pro_cd = intent.getStringExtra("pro_cd");
        pro_name = intent.getStringExtra("pro_name");
        load_pallet = intent.getStringExtra("load_pallet");
        ea_unit = intent.getStringExtra("ea_unit");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        lpn = intent.getStringExtra("lpn");
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


    @Override
    protected void onResume() {
        super.onResume();
    }


    private boolean isNotScanFromOrTo() {
        boolean check = false;
        List<Product_LoadPallet> product ;
//        if((lpn_code != null)&&(lpn_code != "")){
//            product = DatabaseHelper.getInstance().getAllProduct_LoadPallet(lpn_code);
//        }else{
            product = DatabaseHelper.getInstance().getAllProduct_LoadPallet("");
//        }


        for (int i = 0; i < product.size(); i++) {
            Product_LoadPallet product_loadPallet = product.get(i);
            String value0 = "---";
            String valueFromCode = product_loadPallet.getPOSITION_FROM_CODE();
            String valueToCode = product_loadPallet.getPOSITION_TO_CODE();
            String lpn_from = product_loadPallet.getLPN_FROM();
            String lpn_to = product_loadPallet.getLPN_TO();

            if((valueFromCode.equals("") || valueFromCode.equals(value0)) && (lpn_from.equals(""))){
                check = true;
            }
            if((valueToCode.equals("") || valueToCode.equals(value0)) && (lpn_to.equals(""))){
                check = true;
            }
        }
        if (check == true) {
            return true;
        } else {
            return false;
        }
    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(LoadPalletActivity.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(LoadPalletActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                DatabaseHelper.getInstance().deleteProduct_LoadPallet();
                loadPallets.clear();
                loadPalletAdapter.notifyDataSetChanged();
                finish();
            }
        });
        dialog.show();
    }


    @Override
    public void onBackPressed() {

    }

    private void startScan() {
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        if (load_pallet != null) {
            Intent intent = new Intent(LoadPalletActivity.this, LoadPalletQRCode.class);
            intent.putExtra("check_to_finish_at_list", "check");
            startActivity(intent);
            finish();
        }
    }
    private void actionSyn(){
        try {
            LayoutInflater factory = LayoutInflater.from(LoadPalletActivity.this);
            View layout_cus = factory.inflate(R.layout.layout_request, null);
            final AlertDialog dialog = new AlertDialog.Builder(LoadPalletActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                    synchronizeToServer();
                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

    }

    private void actionBack() {
        try {
            LayoutInflater factory = LayoutInflater.from(LoadPalletActivity.this);
            View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
            final AlertDialog dialog = new AlertDialog.Builder(LoadPalletActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                    DatabaseHelper.getInstance().deleteProduct_LoadPallet();
                    DatabaseHelper.getInstance().deleteallEa_Unit();
                    dialog.dismiss();
                    Intent intent = new Intent(LoadPalletActivity.this, MainWareHouseActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<Product_LoadPallet> product ;
//        if((lpn_code != null)&&(lpn_code != "")){
//            product  = DatabaseHelper.getInstance().getAllProduct_LoadPallet(lpn_code);
//        }else{
            product  = DatabaseHelper.getInstance().getAllProduct_LoadPallet("");
//        }

        for (int i = 0; i < product.size(); i++) {
            Product_LoadPallet product_loadPallet = product.get(i);
            String valueQty = product_loadPallet.getQTY();
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

    private void synchronizeToServer() {
        if (load_pallet != null) {

            Dialog dialog = new Dialog(LoadPalletActivity.this);
            if (loadPallets.size() > 0) {
                String saleCode = CmnFns.readDataAdmin();

                if (isNotScanFromOrTo()) {

                    dialog.showDialog(LoadPalletActivity.this, "Ch??a c?? VT T??? ho???c VT ?????n");

                } else if (isQuanityZero()) {
                    dialog.showDialog(LoadPalletActivity.this, "S??? l?????ng SP kh??ng ???????c b???ng 0");

                } else {
                    try {

//                        int result = new CmnFns().synchronizeData(saleCode, "WPP", "");

                        int result = new CmnFns().synchronizeDataLoadPallet(saleCode, "WPP", "" ,lpn_code);

                        switch (result) {
                            case 1:

                                ShowSuccessMessage("L??u Th??nh C??ng");
                                //Toast.makeText(getApplication(), "L??u th??nh c??ng", Toast.LENGTH_SHORT).show();
                                //Li??n k???t gi???a master pick v?? SO
                                if((lpn_code!= null) && (lpn_code!= "")){
                                    String checkPosition = new CmnFns().Check_Quantity_LPN_With_SO(lpn_code);
                                    dialog.showInfofromSever(LoadPalletActivity.this, checkPosition);
                                }
                                break;
                            case -1:
                                ShowErrorMessage("L??u th???t b???i");
                                break;
                            case -2:
                                ShowErrorMessage("S??? l?????ng kh??ng ????? trong t???n kho");
                                break;
                            case -3:
                                ShowErrorMessage("V??? tr?? t??? kh??ng h???p l???");
                                break;
                            case -4:
                                ShowErrorMessage("Tr???ng th??i phi???u kh??ng h???p l???");
                                break;
                            case -5:
                                ShowErrorMessage("V??? tr?? t??? tr??ng v??? tr?? ?????n");
                                break;
                            case -6:
                                ShowErrorMessage("V??? tr?? ?????n kh??ng h???p l???");
                                break;
                            case -7:
                                ShowErrorMessage("C???p nh???t tr???ng th??i c???a phi???u th???t b???i");
                                break;
                            case -8:
                                ShowErrorMessage("S???n ph???m kh??ng c?? th??ng tin tr??n phi???u");
                                break;
                            case -13:
                                ShowErrorMessage("D??? li???u kh??ng h???p l???");
                                break;
                            case -24:
                                ShowErrorMessage("Vui L??ng Ki???m Tra L???i S??? L?????ng");
                                break;
                            case -26:
                                ShowErrorMessage("S??? L?????ng V?????t Qu?? Y??u C???u Tr??n SO");
                                break;

                            case -36:
                                ShowErrorMessage("Tr??ng D??? Li???u Vui L??ng Ki???m Tra L???i");
                                break;
                            default:
                                if (result >= 1) {
                                    Toast.makeText(getApplication(), "L??u th??nh c??ng", Toast.LENGTH_SHORT).show();
                                    DatabaseHelper.getInstance().deleteProduct_LoadPallet();
                                    loadPallets.clear();
                                    loadPalletAdapter.notifyDataSetChanged();
                                    finish();
                                } else {
                                    ShowErrorMessage("L??u th???t b???i");
                                }
                        }

                    }catch (Exception e){
                        Toast.makeText(this,"Vui L??ng Th??? L???i ..." ,Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            } else {
                dialog.showDialog(LoadPalletActivity.this, "Kh??ng c?? s???n ph???m");

            }

        }
    }

    private void ShowErrorMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(LoadPalletActivity.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(LoadPalletActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 64);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.setView(layout_cus);

        Button btnClose = layout_cus.findViewById(R.id.btnHuy);
        TextView textView = layout_cus.findViewById(R.id.tvText);


        textView.setText(message);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
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
                Dialog dialog = new Dialog(LoadPalletActivity.this);
                dialog.showDialog(LoadPalletActivity.this, "M?? S???n Ph???m Kh??ng C?? Trong Kho");
            }
        } else {
            if (lpn != null && value1 != null) {
                alert_show_position(1);
            } else if (lpn == null && value1 != null) {
                alert_show_position(0);
            }
        }

//        if((lpn_code != null)&&(lpn_code != "")){
//            loadPallets = DatabaseHelper.getInstance().getAllProduct_LoadPallet(lpn_code);
//        }else{
            loadPallets = DatabaseHelper.getInstance().getAllProduct_LoadPallet("");
//        }


        //putAwayListAdapter = new PutAwayListAdapter(putaway, this);
        loadPalletAdapter = new LoadPalletAdapter(loadPallets, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(loadPalletAdapter);
        loadPalletAdapter.notifyDataSetChanged();
        load_pallet = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(LoadPalletActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(LoadPalletActivity.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(LoadPalletActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(LoadPalletActivity.this,LoadPalletActivity.class);
                        startActivity(i);

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();

                        int position = viewHolder.getAdapterPosition();
                        Product_LoadPallet product = loadPallets.get(position);
                        loadPallets.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_LoadPallet_Specific(product.getAUTOINCREMENT());
                        loadPalletAdapter.notifyItemRemoved(position);
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
        buttonBack.setText("Tr??? V???");
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Danh S??ch SP Ch???t H??ng L??n Pallet");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan_Barcode:
                startScan();
                break;
            case R.id.buttonBack:
                if((lpn_code!=null) && (lpn_code!="")){
                    finish();
                }else{
                    actionBack();
                }
                break;
            case R.id.buttonOK:
                actionSyn();
//                if((lpn_code != null)&&(lpn_code != "")){
//                    loadPallets = DatabaseHelper.getInstance().getAllProduct_LoadPallet(lpn_code);
//                }else{
                    loadPallets = DatabaseHelper.getInstance().getAllProduct_LoadPallet("");
//                }

                //putAwayListAdapter = new PutAwayListAdapter(putaway, this);
                loadPalletAdapter = new LoadPalletAdapter(loadPallets, this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                listViewProduct.setLayoutManager(layoutManager);
                listViewProduct.setAdapter(loadPalletAdapter);
                loadPalletAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void alert_show_position(int isLPN) {
        String positionTo = "";
        String positionFrom = "";
        ArrayList<Product_LoadPallet> loadPallets = new ArrayList<>();
        loadPallets = DatabaseHelper.getInstance().getAllProduct_LoadPallet_Sync("");
        for (int i = 0; i < loadPallets.size(); i++) {
            Product_LoadPallet loadPallet = loadPallets.get(i);
            if (productCd.equals(loadPallet.getPRODUCT_CD()) &&
                    expDate1.equals(loadPallet.getEXPIRED_DATE()) &&
                    stockinDate.equals(loadPallet.getSTOCKIN_DATE()) &&
                    ea_unit_position.equals(loadPallet.getUNIT())) {

                if (!loadPallet.getLPN_FROM().equals("") || !loadPallet.getLPN_TO().equals("")) {
                    positionTo = loadPallet.getLPN_TO();
                    positionFrom = loadPallet.getLPN_FROM();
                }
                // kh??ng ???????c ????? else if - v?? qu??t VTT k ph???i l?? m?? LPN
                if (!loadPallet.getPOSITION_FROM_CODE().equals("") || !loadPallet.getPOSITION_TO_CODE().equals("")) {
                    positionTo = loadPallet.getPOSITION_TO_CODE();
                    positionFrom = loadPallet.getPOSITION_FROM_CODE();
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
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo(unique_id ,CmnFns.readDataAdmin(), value1, positonReceive,
                    productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WPP", isLPN);

                Dialog dialog = new Dialog(LoadPalletActivity.this);

                if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                    dialog.showDialog(LoadPalletActivity.this, "Vui L??ng Th??? L???i");

                } else if (postitionDes.equals("-3")) {
                    dialog.showDialog(LoadPalletActivity.this, "V??? tr?? t??? kh??ng h???p l???");

                } else if (postitionDes.equals("-6")) {
                    dialog.showDialog(LoadPalletActivity.this, "V??? tr?? ?????n kh??ng h???p l???");

                } else if (postitionDes.equals("-5")) {
                    dialog.showDialog(LoadPalletActivity.this, "V??? tr?? t??? tr??ng v??? tr?? ?????n");

                } else if (postitionDes.equals("-14")) {
                    dialog.showDialog(LoadPalletActivity.this, "V??? tr?? ?????n tr??ng v??? tr?? t???");

                } else if (postitionDes.equals("-15")) {
                    dialog.showDialog(LoadPalletActivity.this, "V??? tr?? t??? kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-10")) {
                    dialog.showDialog(LoadPalletActivity.this, "M?? LPN kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-17")) {
                    dialog.showDialog(LoadPalletActivity.this, "LPN t??? tr??ng LPN ?????n");

                } else if (postitionDes.equals("-18")) {
                    dialog.showDialog(LoadPalletActivity.this, "LPN ?????n tr??ng LPN t???");

                } else if (postitionDes.equals("-19")) {
                    dialog.showDialog(LoadPalletActivity.this, "V??? tr?? ?????n kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-12")) {
                    dialog.showDialog(LoadPalletActivity.this, "M?? LPN kh??ng c?? trong t???n kho");

                }else if (postitionDes.equals("-27")) {
                    dialog.showDialog(LoadPalletActivity.this, "V??? tr?? t??? ch??a c?? s???n ph???m");

                }else if (postitionDes.equals("-28")) {
                    dialog.showDialog(LoadPalletActivity.this, "LPN ?????n c?? v??? tr?? kh??ng h???p l???");

                } else {
                    return;
                }



        }catch (Exception e){
            Toast.makeText(this,"Vui L??ng Th??? L???i ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    public void alert_show_SP(int isLPN) {
        try {
            if((lpn_code!= null) && (lpn_code!= "")){
                String checkPosition = new CmnFns().Check_Product_In_SO(pro_cd,pro_code,lpn_code);
                if (checkPosition.equals("1")){
                    getProductByZone(isLPN);
                }else{
                    Dialog dialog = new Dialog(LoadPalletActivity.this);
                    dialog.showDialog(LoadPalletActivity.this, checkPosition);
                }
            }else{
                getProductByZone(isLPN);
            }

        }catch (Exception e){
            Toast.makeText(this,"Vui L??ng Th??? L???i ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }

    }
    public void getProductByZone(int isLPN){
        DatabaseHelper.getInstance().deleteallProduct_S_P();
        int postitionDes = new CmnFns().synchronizeGETProductByZoneLoadPallet(LoadPalletActivity.this, value1,
                CmnFns.readDataAdmin(), expDate, ea_unit, stockinDate, isLPN, pro_code,pro_name,batch_number , pro_cd , lpn_code);


        Dialog dialog = new Dialog(LoadPalletActivity.this);


        if (postitionDes == 1) {
            return;
        } else if (postitionDes == -1) {
            dialog.showDialog(LoadPalletActivity.this, "Vui L??ng Th??? L???i");

        } else if (postitionDes == -8) {
            dialog.showDialog(LoadPalletActivity.this, "M?? s???n ph???m kh??ng c?? tr??n phi???u");


        } else if (postitionDes == -10) {
            dialog.showDialog(LoadPalletActivity.this, "M?? LPN kh??ng c?? trong h??? th???ng");

        } else if (postitionDes == -11) {

            dialog.showDialog(LoadPalletActivity.this, "M?? s???n ph???m kh??ng c?? trong kho");


        } else if (postitionDes == -12) {

            dialog.showDialog(LoadPalletActivity.this, "M?? LPN kh??ng c?? trong kho");

        } else if (postitionDes == -16) {

            dialog.showDialog(LoadPalletActivity.this, "S???n ph???m ???? qu??t kh??ng n???m trong LPN n??o");

        } else if (postitionDes == -20) {

            dialog.showDialog(LoadPalletActivity.this, "M?? s???n ph???m kh??ng c?? trong h??? th???ng");

        }
    }
}
