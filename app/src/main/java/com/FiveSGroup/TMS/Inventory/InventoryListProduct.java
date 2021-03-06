package com.FiveSGroup.TMS.Inventory;

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
import com.FiveSGroup.TMS.MasterPick.List_Master_Pick;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.RemoveFromLPN.List_Remove_LPN;
import com.FiveSGroup.TMS.RemoveFromLPN.Product_Remove_LPN;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.Product_Qrcode;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class    InventoryListProduct extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    RecyclerView listViewProduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String stock = "";
    String expDate = "";
    String expDate1 = "";
    String inventory = "";
    String pro_code = "";
    String pro_name = "";
    String pro_cd = "";
    String vitritu = "";
    String key = "";
    String ea_unit = "";
    String batch_number = "";
    String ea_unit_position = "";
    String stockinDate = "", id_unique_IVT = "";
    String lpn = "";
    String fromCd = "";


    int statusGetCust;
    Product_Qrcode product_qrcode;
    TextView tvTitle;


    ArrayList<InventoryProduct> inventoryList;
    CheckEventbus eventbus;

    //PutAwayListAdapter putAwayListAdapter;

    InventoryAdapter inventoryAdapter;

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

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();

        init();
        btnscan_barcode.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        btnok.setOnClickListener(this);

        prepareData();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        SharedPreferences sharedPreferencess = getSharedPreferences("vitrituinventory", Context.MODE_PRIVATE);
        vitritu = sharedPreferencess.getString("vitritu", "");
        value1 = intent.getStringExtra("btn1");
        positonReceive = intent.getStringExtra("returnposition");
        productCd = intent.getStringExtra("returnCD");
        stock = intent.getStringExtra("returnStock");
        key = intent.getStringExtra("key");
//        vitritu = intent.getStringExtra("vitritu");
        try {
            if(vitritu==null){
                vitritu = "";
            }
        }catch (Exception e){

        }
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
        fromCd = intent.getStringExtra("fromCd");
        pro_cd = intent.getStringExtra("pro_cd");
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        id_unique_IVT = intent.getStringExtra("id_unique_IVT");
        inventory = intent.getStringExtra("inventory");
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
    }

    private void prepareData() {
        if (positonReceive == null) {
            if (key == null || key.equals("")) {
                if (lpn != null && value1 != null) {
                    //TODO
                    alert_show_SP(1);
                } else if (lpn == null && value1 != null) {
                    //TODO
                    alert_show_SP(0);
                }
            } else {
                Dialog dialog = new Dialog(InventoryListProduct.this);
                dialog.showDialog(InventoryListProduct.this, "M?? S???n Ph???m Kh??ng C?? Trong Phi???u");
            }


        } else {
            if (lpn != null && value1 != null) {
                //TODO
                alert_show_position(1);
            } else if (lpn == null && value1 != null) {
                //TODO
                alert_show_position(0);
            }

        }

        inventoryList = DatabaseHelper.getInstance().getAllProduct_Inventory_Show(global.getInventoryCD());
        //putAwayListAdapter = new PutAwayListAdapter(putaway, this);
        inventoryAdapter = new InventoryAdapter(inventoryList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(inventoryAdapter);
        inventoryAdapter.notifyDataSetChanged();
        inventory = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(InventoryListProduct.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(InventoryListProduct.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(InventoryListProduct.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(InventoryListProduct.this, InventoryListProduct.class);
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
                        InventoryProduct product = inventoryList.get(position);
                        inventoryList.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_Inventory_Specific(product.getAUTOINCREMENT());
                        inventoryAdapter.notifyItemRemoved(position);
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
        List<InventoryProduct> product = DatabaseHelper.getInstance().getAllProduct_Inventory_Show(global.getInventoryCD());

        for (int i = 0; i < product.size(); i++) {
            InventoryProduct inventory = product.get(i);
            String valueFromCode = inventory.getPOSITION_FROM_CODE();
            String lpn_from = inventory.getLPN_FROM();

            if ((valueFromCode.equals("")) && (lpn_from.equals(""))) {
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


    private void init() {
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setText("Tr??? V???");
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("DANH S??CH SP KI???M T???N");
    }

    private void actionBack() {
        try {
            InventoryListProduct.this.finish();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<InventoryProduct> product = DatabaseHelper.getInstance().getAllProduct_Inventory(global.getInventoryCD());
        for (int i = 0; i < product.size(); i++) {
            InventoryProduct inventory = product.get(i);
            String valueQty = inventory.getQTY();
            if (valueQty.equals("")) {
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
        int result;
        Dialog dialog = new Dialog(InventoryListProduct.this);
        if (inventory != null) {
            String saleCode = CmnFns.readDataAdmin();
            if (inventoryList.size() > 0) {
                if (isNotScanFromOrTo()) {
                    dialog.showDialog(InventoryListProduct.this, "Ch??a c?? VT T???");

                } else if (isQuanityZero()) {
                    dialog.showDialog(InventoryListProduct.this, "S??? l?????ng SP kh??ng ???????c ????? tr???ng");

                } else {
                    //TODO
                    try {
                        result = new CmnFns().synchronizeData(saleCode, "WST", global.getInventoryCD());
                        switch (result) {
                            case 1:
                                ShowSuccessMessage("L??u th??nh c??ng");
                                // Toast.makeText(getApplication(), "L??u th??nh c??ng", Toast.LENGTH_SHORT).show();
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
                            default:
                                if (result >= 1) {
                                    Toast.makeText(getApplication(), "L??u th??nh c??ng", Toast.LENGTH_SHORT).show();
                                    if (inventory != null) {
                                        DatabaseHelper.getInstance().deleteProduct_Inventory_CD(global.getInventoryCD());
                                        inventoryList.clear();
                                        inventoryAdapter.notifyDataSetChanged();
                                        finish();
                                    }
                                } else {
                                    ShowErrorMessage("L??u th???t b???i");
                                }
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Vui L??ng Th??? L???i", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            } else {
                dialog.showDialog(InventoryListProduct.this, "Kh??ng c?? s???n ph???m");

            }


        }

    }

    private void ShowErrorMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(InventoryListProduct.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(InventoryListProduct.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(InventoryListProduct.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(InventoryListProduct.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                if (inventory != null) {
                    DatabaseHelper.getInstance().deleteProduct_Inventory_CD(global.getInventoryCD());
                    inventoryList.clear();
                    inventoryAdapter.notifyDataSetChanged();
                    startActivity(new Intent(InventoryListProduct.this, InventoryHome.class));
                    finish();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan_Barcode:
                DatabaseHelper.getInstance().deleteallEa_Unit();
                DatabaseHelper.getInstance().deleteallExp_date();
                if (inventory != null) {
                    Intent intent = new Intent(InventoryListProduct.this, InventoryScanCode.class);
                    intent.putExtra("check_to_finish_at_list", "check");
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.buttonBack:
                actionBack();
                break;
            case R.id.buttonOK:
                synchronizeToServer();
                break;
        }
    }

    public void alert_show_position(int isLPN) {
        String positionTo = "";
        String positionFrom = "";
        ArrayList<InventoryProduct> inventoryProducts = new ArrayList<>();
        inventoryProducts = DatabaseHelper.getInstance().getAllProduct_Inventory_Sync(global.getInventoryCD());
        for (int i = 0; i < inventoryProducts.size(); i++) {
            InventoryProduct inventoryProduct = inventoryProducts.get(i);
            if (productCd.equals(inventoryProduct.getPRODUCT_CD()) &&
                    expDate1.equals(inventoryProduct.getEXPIRED_DATE()) &&
                    stockinDate.equals(inventoryProduct.getSTOCKIN_DATE()) &&
                    ea_unit_position.equals(inventoryProduct.getUNIT())) {
                if (!inventoryProduct.getLPN_FROM().equals("") || !inventoryProduct.getLPN_TO().equals("")) {
                    positionTo = inventoryProduct.getLPN_TO();
                    positionFrom = inventoryProduct.getLPN_FROM();
                } else {
                    positionTo = inventoryProduct.getPOSITION_TO_CODE();
                    positionFrom = inventoryProduct.getPOSITION_FROM_CODE();

                }


            }
        }
        try {
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo(id_unique_IVT, CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WST", isLPN);

                Dialog dialog = new Dialog(InventoryListProduct.this);

                if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                    dialog.showDialog(InventoryListProduct.this, "Vui L??ng Th??? L???i");

                } else if (postitionDes.equals("-3")) {
                    dialog.showDialog(InventoryListProduct.this, "V??? tr?? t??? kh??ng h???p l???");

                } else if (postitionDes.equals("-6")) {
                    dialog.showDialog(InventoryListProduct.this, "V??? tr?? ?????n kh??ng h???p l???");

                } else if (postitionDes.equals("-5")) {
                    dialog.showDialog(InventoryListProduct.this, "V??? tr?? t??? tr??ng v??? tr?? ?????n");

                } else if (postitionDes.equals("-14")) {
                    dialog.showDialog(InventoryListProduct.this, "V??? tr?? ?????n tr??ng v??? tr?? t???");

                } else if (postitionDes.equals("-15")) {
                    dialog.showDialog(InventoryListProduct.this, "V??? tr?? t??? kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-10")) {
                    dialog.showDialog(InventoryListProduct.this, "M?? LPN kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-17")) {
                    dialog.showDialog(InventoryListProduct.this, "LPN t??? tr??ng LPN ?????n");

                } else if (postitionDes.equals("-18")) {
                    dialog.showDialog(InventoryListProduct.this, "LPN ?????n tr??ng LPN t???");

                } else if (postitionDes.equals("-19")) {
                    dialog.showDialog(InventoryListProduct.this, "V??? tr?? ?????n kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-12")) {
                    dialog.showDialog(InventoryListProduct.this, "M?? LPN kh??ng c?? trong t???n kho");

                } else if (postitionDes.equals("-27")) {
                    dialog.showDialog(InventoryListProduct.this, "V??? tr?? t??? ch??a c?? s???n ph???m");

                } else if (postitionDes.equals("-28")) {
                    dialog.showDialog(InventoryListProduct.this, "LPN ?????n c?? v??? tr?? kh??ng h???p l???");

                } else {
                    return;
                }

        } catch (Exception e) {
            Toast.makeText(this, "Vui L??ng Th??? L???i ...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    public void alert_show_SP(int isLPN) {
        try {
            DatabaseHelper.getInstance().deleteallProduct_S_P();
            int postitionDes = new CmnFns().synchronizeGETProductByZoneInventory(value1, CmnFns.readDataAdmin(), expDate, ea_unit, "WST",
                    global.getInventoryCD(), stockinDate, isLPN,pro_code , pro_name ,batch_number, pro_cd ,vitritu,fromCd);

            Dialog dialog = new Dialog(InventoryListProduct.this);


            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(InventoryListProduct.this, "Vui L??ng Th??? L???i");

            } else if (postitionDes == -8) {
                dialog.showDialog(InventoryListProduct.this, "M?? s???n ph???m kh??ng c?? tr??n phi???u");


            } else if (postitionDes == -10) {
                dialog.showDialog(InventoryListProduct.this, "M?? LPN kh??ng c?? trong h??? th???ng");

            } else if (postitionDes == -11) {

                dialog.showDialog(InventoryListProduct.this, "M?? s???n ph???m kh??ng c?? trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(InventoryListProduct.this, "M?? LPN kh??ng c?? trong kho");

            } else if (postitionDes == -16) {

                dialog.showDialog(InventoryListProduct.this, "S???n ph???m ???? qu??t kh??ng n???m trong LPN n??o");

            } else if (postitionDes == -20) {

                dialog.showDialog(InventoryListProduct.this, "M?? s???n ph???m kh??ng c?? trong h??? th???ng");

            }
        } catch (Exception e) {
            Toast.makeText(this, "Vui L??ng Th??? L???i ...", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}
