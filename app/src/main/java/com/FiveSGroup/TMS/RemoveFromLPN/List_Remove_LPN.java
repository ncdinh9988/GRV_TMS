package com.FiveSGroup.TMS.RemoveFromLPN;

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
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.PickList.ListPickList;
import com.FiveSGroup.TMS.PutAway.List_PutAway;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.StockTransfer.ListStockTransfer;
import com.FiveSGroup.TMS.StockTransfer.Product_StockTransfer;

import java.util.ArrayList;
import java.util.List;

public class List_Remove_LPN extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    Remove_LPN_Adapter remove_LPN_Adapter;
    RecyclerView listViewProduct;
    String value1 = "", positonReceive = "", productCd = "", stock = "";
    String expDate = "";
    String expDate1 = "";
    String removeLPN = "";
    String ea_unit = "";
    String batch_number = "";
    String key = "";
    String pro_cd = "";
    String pro_code = "";
    String pro_name = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String lpn = "" , id_unique_RML = "";


    ArrayList<Product_Remove_LPN> remove_lpn;
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

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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
        stock = intent.getStringExtra("returnStock");
        pro_cd = intent.getStringExtra("pro_cd");
        id_unique_RML = intent.getStringExtra("id_unique_RML");
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
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
        removeLPN = intent.getStringExtra("remove_lpn");
        ea_unit = intent.getStringExtra("ea_unit");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        lpn = intent.getStringExtra("lpn");

        stockinDate = intent.getStringExtra("stockin_date");
        key = intent.getStringExtra("key");
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
        List<Product_Remove_LPN> product = DatabaseHelper.getInstance().getAllProduct_Remove_LPN();

        for (int i = 0; i < product.size(); i++) {
            Product_Remove_LPN remove_lpn = product.get(i);
            String value0 = "---";
            String valueFromCode = remove_lpn.getPOSITION_FROM_CODE();
            String valueToCode = remove_lpn.getPOSITION_TO_CODE();
            String lpn_from = remove_lpn.getLPN_FROM();
            String lpn_to = remove_lpn.getLPN_TO();

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


    @Override
    public void onBackPressed() {

    }

    private void startScan() {
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        if (removeLPN != null) {
            Intent intent = new Intent(List_Remove_LPN.this, Qrcode_Remove_LPN.class);
            intent.putExtra("check_to_finish_at_list", "check");
            startActivity(intent);
            finish();
        }
    }

    private void actionBack() {
        try {
            LayoutInflater factory = LayoutInflater.from(List_Remove_LPN.this);
            View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
            final AlertDialog dialog = new AlertDialog.Builder(List_Remove_LPN.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                    Intent i = new Intent(List_Remove_LPN.this,List_Remove_LPN.class);
                    startActivity(i);

                }
            });
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper.getInstance().deleteProduct_Remove_LPN();
                    DatabaseHelper.getInstance().deleteallEa_Unit();
                    dialog.dismiss();
                    Intent intent = new Intent(List_Remove_LPN.this, MainWareHouseActivity.class);
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
        List<Product_Remove_LPN> product = DatabaseHelper.getInstance().getAllProduct_Remove_LPN();
        for (int i = 0; i < product.size(); i++) {
            Product_Remove_LPN remove_LPN = product.get(i);
            String valueQty = remove_LPN.getQTY();
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
        if (removeLPN != null) {

            String saleCode = CmnFns.readDataAdmin();
            Dialog dialog = new Dialog(List_Remove_LPN.this);

            if (remove_lpn.size() > 0) {
                if (isNotScanFromOrTo()) {
                    dialog.showDialog(List_Remove_LPN.this, "Ch??a c?? VT T??? ho???c VT ?????n");

                } else if (isQuanityZero()) {
                    dialog.showDialog(List_Remove_LPN.this, "S??? l?????ng SP kh??ng ???????c b???ng 0");

                } else {
                    try {
                        int result = new CmnFns().synchronizeData(saleCode, "WLP", "");

                        if (result >= 1) {
                            ShowSuccessMessage("L??u th??nh c??ng");
//                        Toast.makeText(getApplication(), "L??u th??nh c??ng", Toast.LENGTH_SHORT).show();

                        } else {
                            if (result == -1) {
                                dialog.showDialog(List_Remove_LPN.this, "L??u th???t b???i");
                            } else if (result == -2) {
                                dialog.showDialog(List_Remove_LPN.this, "S??? l?????ng kh??ng ????? trong t???n kho");

                            } else if (result == -3) {
                                dialog.showDialog(List_Remove_LPN.this, "V??? tr?? t??? kh??ng h???p l???");

                            } else if (result == -4) {
                                dialog.showDialog(List_Remove_LPN.this, "Tr???ng th??i c???a phi???u kh??ng h???p l???");

                            } else if (result == -5) {
                                dialog.showDialog(List_Remove_LPN.this, "V??? tr?? t??? tr??ng v??? tr?? ????n");

                            } else if (result == -6) {
                                dialog.showDialog(List_Remove_LPN.this, "V??? tr?? ?????n kh??ng h???p l???");

                            } else if (result == -7) {
                                dialog.showDialog(List_Remove_LPN.this, "C???p nh???t tr???ng th??i th???t b???i");

                            } else if (result == -8) {
                                dialog.showDialog(List_Remove_LPN.this, "S???n ph???m kh??ng c?? th??ng tin tr??n phi???u ");

                            } else if (result == -13) {
                                dialog.showDialog(List_Remove_LPN.this, "D??? li???u kh??ng h???p l???");

                            }else if (result == -24) {
                                dialog.showDialog(List_Remove_LPN.this, "Vui L??ng Ki???m Tra L???i S??? L?????ng");

                            }else if (result == -26) {
                                dialog.showDialog(List_Remove_LPN.this, "S??? L?????ng V?????t Qu?? Y??u C???u Tr??n SO");

                            }else if (result == -26) {
                                dialog.showDialog(List_Remove_LPN.this, "S??? L?????ng V?????t Qu?? Y??u C???u Tr??n SO");

                            }else {
                                dialog.showDialog(List_Remove_LPN.this, "L??u th???t b???i");
                            }

                        }
                    }catch (Exception e){
                        Toast.makeText(this,"Vui L??ng Th??? L???i ..." ,Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            } else {
                dialog.showDialog(List_Remove_LPN.this, "Kh??ng c?? s???n ph???m");


            }
        }
    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(List_Remove_LPN.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(List_Remove_LPN.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                DatabaseHelper.getInstance().deleteProduct_Remove_LPN();
                remove_lpn.clear();
                remove_LPN_Adapter.notifyDataSetChanged();
                finish();
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
                Dialog dialog = new Dialog(List_Remove_LPN.this);
                dialog.showDialog(List_Remove_LPN.this, "M?? S???n Ph???m Kh??ng C?? Trong Phi???u");
            }


        } else {
            if (lpn != null && value1 != null) {
                alert_show_position(1);
            } else if (lpn == null && value1 != null) {
                alert_show_position(0);
            }
        }
        remove_lpn = DatabaseHelper.getInstance().getAllProduct_Remove_LPN();
        //putAwayListAdapter = new PutAwayListAdapter(putaway, this);
        remove_LPN_Adapter = new Remove_LPN_Adapter(remove_lpn, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(remove_LPN_Adapter);
        remove_LPN_Adapter.notifyDataSetChanged();
        removeLPN = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(List_Remove_LPN.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }


            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(List_Remove_LPN.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(List_Remove_LPN.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        remove_LPN_Adapter.notifyDataSetChanged();

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();
                        int position = viewHolder.getAdapterPosition();
                        Product_Remove_LPN product = remove_lpn.get(position);
                        remove_lpn.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_remove_lpn_Specific(product.getAUTOINCREMENT());
                        remove_LPN_Adapter.notifyItemRemoved(position);
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
        tvTitle.setText("Danh S??ch SP G??? LPN");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan_Barcode:
                startScan();
                break;
            case R.id.buttonBack:
                actionBack();
                break;
            case R.id.buttonOK:
                actionSyn();
                break;
        }
    }

    private void actionSyn(){
        try {
            LayoutInflater factory = LayoutInflater.from(List_Remove_LPN.this);
            View layout_cus = factory.inflate(R.layout.layout_request, null);
            final AlertDialog dialog = new AlertDialog.Builder(List_Remove_LPN.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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

    public void alert_show_position(int isLPN) {
        String positionTo = "";
        String positionFrom = "";
        ArrayList<Product_Remove_LPN> removeLpns = new ArrayList<>();
        removeLpns = DatabaseHelper.getInstance().getAllProduct_Remove_LPN_Sync();
        for (int i = 0; i < removeLpns.size(); i++) {
            Product_Remove_LPN removeLpn = removeLpns.get(i);
            if (productCd.equals(removeLpn.getPRODUCT_CD()) &&
                    expDate1.equals(removeLpn.getEXPIRED_DATE()) &&
                    stockinDate.equals(removeLpn.getSTOCKIN_DATE()) &&
                    ea_unit_position.equals(removeLpn.getUNIT())) {
                if (!removeLpn.getLPN_FROM().equals("") || !removeLpn.getLPN_TO().equals("")) {
                    positionTo = removeLpn.getLPN_TO();
                    positionFrom = removeLpn.getLPN_FROM();
                }
                if (!removeLpn.getPOSITION_FROM_CODE().equals("") || !removeLpn.getPOSITION_TO_CODE().equals("")) {
                    positionTo = removeLpn.getPOSITION_TO_CODE();
                    positionFrom = removeLpn.getPOSITION_FROM_CODE();
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
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo(id_unique_RML,CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WLP", isLPN);

            Dialog dialog = new Dialog(List_Remove_LPN.this);

            if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                dialog.showDialog(List_Remove_LPN.this, "Vui L??ng Th??? L???i");

            } else if (postitionDes.equals("-3")) {
                dialog.showDialog(List_Remove_LPN.this, "V??? tr?? t??? kh??ng h???p l???");

            } else if (postitionDes.equals("-6")) {
                dialog.showDialog(List_Remove_LPN.this, "V??? tr?? ?????n kh??ng h???p l???");

            } else if (postitionDes.equals("-5")) {
                dialog.showDialog(List_Remove_LPN.this, "V??? tr?? t??? tr??ng v??? tr?? ?????n");

            } else if (postitionDes.equals("-14")) {
                dialog.showDialog(List_Remove_LPN.this, "V??? tr?? ?????n tr??ng v??? tr?? t???");

            } else if (postitionDes.equals("-15")) {
                dialog.showDialog(List_Remove_LPN.this, "V??? tr?? t??? kh??ng c?? trong h??? th???ng");

            } else if (postitionDes.equals("-10")) {
                dialog.showDialog(List_Remove_LPN.this, "M?? LPN kh??ng c?? trong h??? th???ng");

            } else if (postitionDes.equals("-17")) {
                dialog.showDialog(List_Remove_LPN.this, "LPN t??? tr??ng LPN ?????n");

            } else if (postitionDes.equals("-18")) {
                dialog.showDialog(List_Remove_LPN.this, "LPN ?????n tr??ng LPN t???");

            } else if (postitionDes.equals("-19")) {
                dialog.showDialog(List_Remove_LPN.this, "V??? tr?? ?????n kh??ng c?? trong h??? th???ng");

            } else if (postitionDes.equals("-12")) {
                dialog.showDialog(List_Remove_LPN.this, "M?? LPN kh??ng c?? trong t???n kho");

            }else if (postitionDes.equals("-27")) {
                dialog.showDialog(List_Remove_LPN.this, "V??? tr?? t??? ch??a c?? s???n ph???m");

            }else if (postitionDes.equals("-28")) {
                dialog.showDialog(List_Remove_LPN.this, "LPN ?????n c?? v??? tr?? kh??ng h???p l???");

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
            int postitionDes = new CmnFns().synchronizeGETProductByZoneRemoveLPN(List_Remove_LPN.this, value1, CmnFns.readDataAdmin(),
                    expDate, ea_unit, stockinDate, isLPN, batch_number,pro_code , pro_name,pro_cd);

            Dialog dialog = new Dialog(List_Remove_LPN.this);


            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(List_Remove_LPN.this, "Vui L??ng Th??? L???i");

            } else if (postitionDes == -8) {
                dialog.showDialog(List_Remove_LPN.this, "M?? s???n ph???m kh??ng c?? tr??n phi???u");


            } else if (postitionDes == -10) {
                dialog.showDialog(List_Remove_LPN.this, "M?? LPN kh??ng c?? trong h??? th???ng");

            } else if (postitionDes == -11) {

                dialog.showDialog(List_Remove_LPN.this, "M?? s???n ph???m kh??ng c?? trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(List_Remove_LPN.this, "M?? LPN kh??ng c?? trong kho");

            } else if (postitionDes == -16) {

                dialog.showDialog(List_Remove_LPN.this, "S???n ph???m ???? qu??t kh??ng n???m trong LPN n??o");

            } else if (postitionDes == -20) {

                dialog.showDialog(List_Remove_LPN.this, "M?? s???n ph???m kh??ng c?? trong h??? th???ng");

            }
        }catch (Exception e){
            Toast.makeText(this,"Vui L??ng Th??? L???i ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
