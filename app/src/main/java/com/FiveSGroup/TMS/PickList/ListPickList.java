package com.FiveSGroup.TMS.PickList;

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
import com.FiveSGroup.TMS.LoadPallet.LoadPalletActivity;
import com.FiveSGroup.TMS.LoadPallet.Product_LoadPallet;
import com.FiveSGroup.TMS.MasterPick.List_Master_Pick;
import com.FiveSGroup.TMS.MasterPick.Product_Master_Pick;
import com.FiveSGroup.TMS.PutAway.List_PutAway;
import com.FiveSGroup.TMS.QA.Pickup.List_Pickup;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.StockOut.ListQrcode_Stockout;
import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.Product_Qrcode;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class ListPickList extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    RecyclerView listViewProduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String stock = "";
    String expDate = "";
    String batch_number = "";
    String pro_code = "";
    String pro_name = "";
    String expDate1 = "";
    String pick_list = "";
    String ea_unit = "";
    String key = "";
    String ea_unit_position = "", id_unique_PL = "";
    String stockinDate = "";
    String lpn = "";
    String postitionDess = "";
    String pro_cd = "";

    int statusGetCust;
    Product_Qrcode product_qrcode;
    TextView tvTitle;

    int result;


    ArrayList<PickList> pickList;
    CheckEventbus eventbus;

    //PutAwayListAdapter putAwayListAdapter;

    PickListAdapter pickListAdapter;

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
        value1 = intent.getStringExtra("btn1");
        positonReceive = intent.getStringExtra("returnposition");
        productCd = intent.getStringExtra("returnCD");
        stock = intent.getStringExtra("returnStock");
        pro_cd = intent.getStringExtra("pro_cd");
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
        id_unique_PL = intent.getStringExtra("id_unique_PL");
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        pick_list = intent.getStringExtra("pick_list");
        ea_unit = intent.getStringExtra("ea_unit");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        lpn = intent.getStringExtra("lpn");
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
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

    }

    private void prepareData() {
//        if (pick_list != null) {
            if (positonReceive == null) {
                if (key == null || key.equals("")) {
                    if (lpn != null && value1 != null) {
                        alert_show_SP(1);
                    } else if (lpn == null && value1 != null) {
                        alert_show_SP(0);

                    }
                } else {
                    Dialog dialog = new Dialog(ListPickList.this);
                    dialog.showDialog(ListPickList.this, "M?? S???n Ph???m Kh??ng C?? Trong Phi???u");
                }


            } else {
                if (lpn != null && value1 != null) {
                    alert_show_position(1);
                } else if (lpn == null && value1 != null) {
                    alert_show_position(0);
                }
            }
//        }

        pickList = DatabaseHelper.getInstance().getAllProduct_PickList_Show(global.getPickListCD());
        //putAwayListAdapter = new PutAwayListAdapter(putaway, this);
        pickListAdapter = new PickListAdapter(this, pickList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(pickListAdapter);
        pickListAdapter.notifyDataSetChanged();
        pick_list = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(ListPickList.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(ListPickList.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(ListPickList.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(ListPickList.this, ListPickList.class);
                        startActivity(i);

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();

                        int position = viewHolder.getAdapterPosition();
                        PickList product = pickList.get(position);
                        pickList.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_PickList_Specific(product.getAUTOINCREMENT());
                        pickListAdapter.notifyItemRemoved(position);
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
        List<PickList> product = DatabaseHelper.getInstance().getAllProduct_PickList_Show(global.getPickListCD());

        for (int i = 0; i < product.size(); i++) {
            PickList pickList = product.get(i);
            String value0 = "---";
            String valueFromCode = pickList.getPOSITION_FROM_CODE();
            String valueToCode = pickList.getPOSITION_TO_CODE();
            String lpn_from = pickList.getLPN_FROM();
            String lpn_to = pickList.getLPN_TO();

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


    private void init() {
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setText("Tr??? V???");
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("DANH S??CH SP PICKLIST");
    }

    private void actionBack() {
        try {
            ListPickList.this.finish();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<PickList> product = DatabaseHelper.getInstance().getAllProduct_PickList(global.getPickListCD());
        for (int i = 0; i < product.size(); i++) {
            PickList putAway = product.get(i);
            String valueQty = putAway.getQTY_SET_AVAILABLE();
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
        Dialog dialog = new Dialog(ListPickList.this);
        if (pick_list != null) {
            String saleCode = CmnFns.readDataAdmin();
            if (pickList.size() > 0) {
                if (isNotScanFromOrTo()) {
                    dialog.showDialog(ListPickList.this, "Ch??a c?? VT T??? ho???c VT ?????n");

                } else if (isQuanityZero()) {
                    dialog.showDialog(ListPickList.this, "S??? l?????ng SP kh??ng ???????c b???ng 0");

                } else {
                    try {
                        result = new CmnFns().synchronizeData(saleCode, "WPL", global.getPickListCD());

                        if (result >= 1) {
                            ShowSuccessMessage("L??u th??nh c??ng");
//                        Toast.makeText(getApplication(), "L??u th??nh c??ng", Toast.LENGTH_SHORT).show();

                        } else {

                            if (result == -1) {
                                dialog.showDialog(ListPickList.this, "L??u th???t b???i");
                            } else if (result == -2) {
                                dialog.showDialog(ListPickList.this, "S??? l?????ng kh??ng ????? trong t???n kho");

                            } else if (result == -3) {
                                dialog.showDialog(ListPickList.this, "V??? tr?? t??? kh??ng h???p l???");

                            } else if (result == -4) {
                                dialog.showDialog(ListPickList.this, "Tr???ng th??i c???a phi???u kh??ng h???p l???");

                            } else if (result == -5) {
                                dialog.showDialog(ListPickList.this, "V??? tr?? t??? tr??ng v??? tr?? ????n");

                            } else if (result == -6) {
                                dialog.showDialog(ListPickList.this, "V??? tr?? ?????n kh??ng h???p l???");

                            } else if (result == -7) {
                                dialog.showDialog(ListPickList.this, "C???p nh???t tr???ng th??i th???t b???i");

                            } else if (result == -8) {
                                dialog.showDialog(ListPickList.this, "S???n ph???m kh??ng c?? th??ng tin tr??n phi???u ");

                            } else if (result == -13) {
                                dialog.showDialog(ListPickList.this, "D??? li???u kh??ng h???p l???");

                            } else if (result == -24) {
                                dialog.showDialog(ListPickList.this, "Vui L??ng Ki???m Tra L???i S??? L?????ng");

                            } else if (result == -26) {
                                dialog.showDialog(ListPickList.this, "S??? L?????ng V?????t Qu?? Y??u C???u Tr??n SO");

                            } else if (result == -31) {
                                dialog.showDialog(ListPickList.this, "LPN N??y ???? ???????c s??? D???ng Cho SO Kh??c");

                            } else if (result == -34) {
                                dialog.showDialog(ListPickList.this, "S???n Ph???m V???i ??VT Kh??ng ????ng Tr??n SO");

                            } else {
                                dialog.showDialog(ListPickList.this, "L??u th???t b???i");
                            }


                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Vui L??ng Th??? L???i ...", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            } else {

                dialog.showDialog(ListPickList.this, "Kh??ng c?? s???n ph???m");

            }


        }

    }

    private void ShowSuccessMessage(String message) {

        LayoutInflater factory = LayoutInflater.from(ListPickList.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(ListPickList.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                if (pickList != null) {
                    DatabaseHelper.getInstance().deleteProduct_PickList_CD(global.getPickListCD());
                    pickList.clear();
                    pickListAdapter.notifyDataSetChanged();
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
                if (pick_list != null) {
                    Intent intent = new Intent(ListPickList.this, PickListQrCode.class);
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
        ArrayList<PickList> pickLists = new ArrayList<>();
        pickLists = DatabaseHelper.getInstance().getAllProduct_PickList_Sync(global.getPickListCD());
        for (int i = 0; i < pickLists.size(); i++) {
            PickList pickList = pickLists.get(i);
            if (productCd.equals(pickList.getPRODUCT_CD()) &&
                    expDate1.equals(pickList.getEXPIRED_DATE()) &&
                    stockinDate.equals(pickList.getSTOCKIN_DATE()) &&
                    ea_unit_position.equals(pickList.getUNIT())) {
                if (!pickList.getLPN_FROM().equals("") || !pickList.getLPN_TO().equals("")) {
                    positionTo = pickList.getLPN_TO();
                    positionFrom = pickList.getLPN_FROM();
                }
                if (!pickList.getPOSITION_FROM_CODE().equals("") || !pickList.getPOSITION_TO_CODE().equals("")) {
                    positionTo = pickList.getPOSITION_TO_CODE();
                    positionFrom = pickList.getPOSITION_FROM_CODE();
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
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo(id_unique_PL, CmnFns.readDataAdmin(), value1, positonReceive,
                    productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WPL", isLPN);
            ArrayList<PickList> listpickLists = new ArrayList<>();
            listpickLists = DatabaseHelper.getInstance().getonePosition_Picklist(id_unique_PL);
            String position_from_cd = listpickLists.get(0).getPOSITION_FROM_CD();
            String position_to_cd = listpickLists.get(0).getPOSITION_TO_CD();

            if((!position_from_cd.equals(""))&&(!position_to_cd.equals(""))){
                String check_position = new CmnFns().Check_Position_Same_SLOC(position_from_cd,position_to_cd,"WPL");

                if(check_position.equals("Th??nh C??ng")){
                    Dialog dialog = new Dialog(ListPickList.this);

                    if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                        dialog.showDialog(ListPickList.this, "Vui L??ng Th??? L???i");

                    } else if (postitionDes.equals("-3")) {
                        dialog.showDialog(ListPickList.this, "V??? tr?? t??? kh??ng h???p l???");

                    } else if (postitionDes.equals("-6")) {
                        dialog.showDialog(ListPickList.this, "V??? tr?? ?????n kh??ng h???p l???");

                    } else if (postitionDes.equals("-5")) {
                        dialog.showDialog(ListPickList.this, "V??? tr?? t??? tr??ng v??? tr?? ?????n");

                    } else if (postitionDes.equals("-14")) {
                        dialog.showDialog(ListPickList.this, "V??? tr?? ?????n tr??ng v??? tr?? t???");

                    } else if (postitionDes.equals("-15")) {
                        dialog.showDialog(ListPickList.this, "V??? tr?? t??? kh??ng c?? trong h??? th???ng");

                    } else if (postitionDes.equals("-10")) {
                        dialog.showDialog(ListPickList.this, "M?? LPN kh??ng c?? trong h??? th???ng");

                    } else if (postitionDes.equals("-17")) {
                        dialog.showDialog(ListPickList.this, "LPN t??? tr??ng LPN ?????n");

                    } else if (postitionDes.equals("-18")) {
                        dialog.showDialog(ListPickList.this, "LPN ?????n tr??ng LPN t???");

                    } else if (postitionDes.equals("-19")) {
                        dialog.showDialog(ListPickList.this, "V??? tr?? ?????n kh??ng c?? trong h??? th???ng");

                    } else if (postitionDes.equals("-12")) {
                        dialog.showDialog(ListPickList.this, "M?? LPN kh??ng c?? trong t???n kho");

                    } else if (postitionDes.equals("-27")) {
                        dialog.showDialog(ListPickList.this, "V??? tr?? t??? ch??a c?? s???n ph???m");

                    } else if (postitionDes.equals("-28")) {
                        dialog.showDialog(ListPickList.this, "LPN ?????n c?? v??? tr?? kh??ng h???p l???");

                    } else {
                        return;
                    }
                }else{
                    if (positonReceive.equals("1") && productCd != null) {
                        DatabaseHelper.getInstance().updatePositionFrom_PickList(id_unique_PL,"","","","","","","" );

                    }else if (positonReceive.equals("2") && productCd != null) {
                        DatabaseHelper.getInstance().updatePositionTo_PickList(id_unique_PL,"","","","","","","" );

                    }

                    Dialog dialog = new Dialog(ListPickList.this);
                    dialog.showDialog(ListPickList.this, check_position);

                }
            }else{
                Dialog dialog = new Dialog(ListPickList.this);

                if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                    dialog.showDialog(ListPickList.this, "Vui L??ng Th??? L???i");

                } else if (postitionDes.equals("-3")) {
                    dialog.showDialog(ListPickList.this, "V??? tr?? t??? kh??ng h???p l???");

                } else if (postitionDes.equals("-6")) {
                    dialog.showDialog(ListPickList.this, "V??? tr?? ?????n kh??ng h???p l???");

                } else if (postitionDes.equals("-5")) {
                    dialog.showDialog(ListPickList.this, "V??? tr?? t??? tr??ng v??? tr?? ?????n");

                } else if (postitionDes.equals("-14")) {
                    dialog.showDialog(ListPickList.this, "V??? tr?? ?????n tr??ng v??? tr?? t???");

                } else if (postitionDes.equals("-15")) {
                    dialog.showDialog(ListPickList.this, "V??? tr?? t??? kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-10")) {
                    dialog.showDialog(ListPickList.this, "M?? LPN kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-17")) {
                    dialog.showDialog(ListPickList.this, "LPN t??? tr??ng LPN ?????n");

                } else if (postitionDes.equals("-18")) {
                    dialog.showDialog(ListPickList.this, "LPN ?????n tr??ng LPN t???");

                } else if (postitionDes.equals("-19")) {
                    dialog.showDialog(ListPickList.this, "V??? tr?? ?????n kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-12")) {
                    dialog.showDialog(ListPickList.this, "M?? LPN kh??ng c?? trong t???n kho");

                } else if (postitionDes.equals("-27")) {
                    dialog.showDialog(ListPickList.this, "V??? tr?? t??? ch??a c?? s???n ph???m");

                } else if (postitionDes.equals("-28")) {
                    dialog.showDialog(ListPickList.this, "LPN ?????n c?? v??? tr?? kh??ng h???p l???");

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
            int postitionDes = new CmnFns().synchronizeGETProductByZonePickList(ListPickList.this, value1, CmnFns.readDataAdmin(),
                    expDate, ea_unit, "WPL", global.getPickListCD(), stockinDate, isLPN, batch_number,pro_code , pro_name, pro_cd);

            Dialog dialog = new Dialog(ListPickList.this);


            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(ListPickList.this, "Vui L??ng Th??? L???i");

            } else if (postitionDes == -8) {
                dialog.showDialog(ListPickList.this, "M?? s???n ph???m kh??ng c?? tr??n phi???u");


            } else if (postitionDes == -10) {
                dialog.showDialog(ListPickList.this, "M?? LPN kh??ng c?? trong h??? th???ng");

            } else if (postitionDes == -11) {

                dialog.showDialog(ListPickList.this, "M?? s???n ph???m kh??ng c?? trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(ListPickList.this, "M?? LPN kh??ng c?? trong kho");

            } else if (postitionDes == -16) {

                dialog.showDialog(ListPickList.this, "S???n ph???m ???? qu??t kh??ng n???m trong LPN n??o");

            } else if (postitionDes == -20) {

                dialog.showDialog(ListPickList.this, "M?? s???n ph???m kh??ng c?? trong h??? th???ng");

            } else if (postitionDes == -21) {

                dialog.showDialog(ListPickList.this, "M?? s???n ph???m kh??ng c?? trong zone");

            } else if (postitionDes == -22) {

                dialog.showDialog(ListPickList.this, "M?? LPN kh??ng c?? trong zone");

            } else if (postitionDes == -31) {
                dialog.showDialog(ListPickList.this, "LPN N??y ???? ???????c s??? D???ng ");

            } else if (postitionDes == -33) {
                dialog.showDialog(ListPickList.this, "LPN C?? S???n Ph???m V???i ??VT Kh??ng ????ng Tr??n SO");

            }
        } catch (Exception e) {
            Toast.makeText(this, "Vui L??ng Th??? L???i ...", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}
