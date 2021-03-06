package com.FiveSGroup.TMS.TransferQR.TransferPosting;

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
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;

import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.ProductAdapter;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class List_TransferPosting extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    //ProductListViewAdapter productListViewAdapter;
    ProductAdapter productListViewAdapter;
    RecyclerView listVieWTPoduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String pro_code = "";
    String pro_name = "";
    String stock = "";
    String expDate = "";
    String expDate1 = "";
    String pro_cd = "";
    String transfer_posting = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String batch_number = "";
    String lpn = "", id_unique_SO = "";
    String key = "";

    int statusGetCust;
    Product_TransferPosting product_qrcode;

    ArrayList<Product_TransferPosting> transfer_Posting;
    CheckEventbus eventbus;

    TransferPosting_Adapter TransferPostingListAdapter;
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
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
        stock = intent.getStringExtra("returnStock");
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        transfer_posting = intent.getStringExtra("transfer_posting");
        ea_unit = intent.getStringExtra("ea_unit");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        lpn = intent.getStringExtra("lpn");
        id_unique_SO = intent.getStringExtra("id_unique_SO");


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

    private void prepareData() {
        if (positonReceive == null) {
            if (key == null || key.equals("")) {
                if (lpn != null && transfer_posting != null) {
                    //TODO
                    alert_show_SP(1);
                } else if (lpn == null && transfer_posting != null) {
                    //TODO
                    alert_show_SP(0);
                }
            } else {
                Dialog dialog = new Dialog(List_TransferPosting.this);
                dialog.showDialog(List_TransferPosting.this, "M?? S???n Ph???m Kh??ng C?? Trong Phi???u");
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

        transfer_Posting = DatabaseHelper.getInstance().getAllProduct_TransferPosting(global.getTransferPostingCD());
        TransferPostingListAdapter = new TransferPosting_Adapter(this, transfer_Posting);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listVieWTPoduct.setLayoutManager(layoutManager);
        listVieWTPoduct.setAdapter(TransferPostingListAdapter);
        TransferPostingListAdapter.notifyDataSetChanged();
        transfer_posting = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(List_TransferPosting.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(List_TransferPosting.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(List_TransferPosting.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(List_TransferPosting.this, List_TransferPosting.class);
                        startActivity(i);

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();

                        int position = viewHolder.getAdapterPosition();
                        Product_TransferPosting product = transfer_Posting.get(position);
                        transfer_Posting.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_Transfer_Posting_Specific(product.getAUTOINCREMENT());
                        TransferPostingListAdapter.notifyItemRemoved(position);
                    }
                });
                dialog.show();


            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listVieWTPoduct);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private boolean isNotScanFromOrTo() {
        boolean check = false;
        List<Product_TransferPosting> product = DatabaseHelper.getInstance().getAllProduct_TransferPosting(global.getTransferPostingCD());

        for (int i = 0; i < product.size(); i++) {
            Product_TransferPosting cancelGood = product.get(i);
            String value0 = "---";
            String valueFromCode = cancelGood.getPOSITION_FROM_CODE();
            String valueToCode = cancelGood.getPOSITION_TO_CODE();
            String lpn_from = cancelGood.getLPN_FROM();
            String lpn_to = cancelGood.getLPN_TO();

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
        listVieWTPoduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Danh S??ch SP L???y H??ng");
    }

    private void startScan() {

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        Intent intent = new Intent(List_TransferPosting.this, Qrcode_TransferPosting.class);
        intent.putExtra("check_to_finish_at_list", "check");
        startActivity(intent);
        finish();

    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<Product_TransferPosting> product = DatabaseHelper.getInstance().getAllProduct_TransferPosting(global.getTransferPostingCD());
        for (int i = 0; i < product.size(); i++) {
            Product_TransferPosting putAway = product.get(i);
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
        Dialog dialog = new Dialog(List_TransferPosting.this);


        if (transfer_Posting.size() > 0) {
            if (isNotScanFromOrTo()) {
                dialog.showDialog(List_TransferPosting.this, "Ch??a C?? VT T??? Ho???c VT ?????n");

            } else if (isQuanityZero()) {
                dialog.showDialog(List_TransferPosting.this, "S??? l?????ng SP kh??ng ???????c b???ng 0");

            } else {
                try {
                    String result = new CmnFns().synchronizeDataV2(saleCode, "WTP", global.getTransferPostingCD());
//                    int result = new CmnFns().synchronizeData(saleCode, "WTP", global.getTransferPostingCD());
                    if (result.contains("L??u th??nh c??ng")) {
                        ShowSuccessMessage(result);
//                    Toast.makeText(getApplication(), "L??u th??nh c??ng", Toast.LENGTH_SHORT).show();

                    } else {
                        dialog.showDialog(List_TransferPosting.this, result);


//                        if (result == -1) {
//                            dialog.showDialog(List_TransferPosting.this, "L??u th???t b???i");
//                        } else if (result == -2) {
//                            dialog.showDialog(List_TransferPosting.this, "S??? l?????ng kh??ng ????? trong t???n kho");
//
//                        } else if (result == -3) {
//                            dialog.showDialog(List_TransferPosting.this, "V??? tr?? t??? kh??ng h???p l???");
//
//                        } else if (result == -4) {
//                            dialog.showDialog(List_TransferPosting.this, "Tr???ng th??i c???a phi???u kh??ng h???p l???");
//
//                        } else if (result == -5) {
//                            dialog.showDialog(List_TransferPosting.this, "V??? tr?? t??? tr??ng v??? tr?? ????n");
//
//                        } else if (result == -6) {
//                            dialog.showDialog(List_TransferPosting.this, "V??? tr?? ?????n kh??ng h???p l???");
//
//                        } else if (result == -7) {
//                            dialog.showDialog(List_TransferPosting.this, "C???p nh???t tr???ng th??i th???t b???i");
//
//                        } else if (result == -8) {
//                            dialog.showDialog(List_TransferPosting.this, "S???n ph???m kh??ng c?? th??ng tin tr??n phi???u ");
//
//                        } else if (result == -13) {
//                            dialog.showDialog(List_TransferPosting.this, "D??? li???u kh??ng h???p l???");
//
//                        } else if (result == -24) {
//                            dialog.showDialog(List_TransferPosting.this, "Vui L??ng Ki???m Tra L???i S??? L?????ng");
//
//                        } else if (result == -26) {
//                            dialog.showDialog(List_TransferPosting.this, "S??? L?????ng V?????t Qu?? Y??u C???u Tr??n SO");
//
//                        } else {
//                            dialog.showDialog(List_TransferPosting.this, "L??u th???t b???i");
//                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Vui L??ng Th??? L???i ...", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        } else {
            dialog.showDialog(List_TransferPosting.this, "Kh??ng c?? s???n ph???m");

        }


    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(List_TransferPosting.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(List_TransferPosting.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                DatabaseHelper.getInstance().deleteProduct_TransferPosting(global.getTransferPostingCD());
                transfer_Posting.clear();
                TransferPostingListAdapter.notifyDataSetChanged();
                Intent intentToHomeQRActivity = new Intent(List_TransferPosting.this, Home_TransferPosting.class);
                startActivity(intentToHomeQRActivity);
                finish();
            }
        });
        dialog.show();
    }

    private void actionBack() {
        try {
            List_TransferPosting.this.finish();
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
        ArrayList<Product_TransferPosting> cancelGoods = new ArrayList<>();
        cancelGoods = DatabaseHelper.getInstance().getAllProduct_TransferPosting_Sync(global.getTransferPostingCD());
        for (int i = 0; i < cancelGoods.size(); i++) {
            Product_TransferPosting cancelGood = cancelGoods.get(i);
            if (productCd.equals(cancelGood.getPRODUCT_CD()) &&
                    expDate1.equals(cancelGood.getEXPIRED_DATE()) &&
                    stockinDate.equals(cancelGood.getSTOCKIN_DATE()) &&
                    ea_unit_position.equals(cancelGood.getUNIT())) {

                if (!cancelGood.getLPN_FROM().equals("") || !cancelGood.getLPN_TO().equals("")) {
                    positionTo = cancelGood.getLPN_TO();
                    positionFrom = cancelGood.getLPN_FROM();
                }
                if (!cancelGood.getPOSITION_FROM_CODE().equals("") || !cancelGood.getPOSITION_TO_CODE().equals("")) {
                    positionTo = cancelGood.getPOSITION_TO_CODE();
                    positionFrom = cancelGood.getPOSITION_FROM_CODE();
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
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo(id_unique_SO, CmnFns.readDataAdmin(), value1, positonReceive,
                    productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WTP", isLPN);
            ArrayList<Product_TransferPosting> listtransferposting = new ArrayList<>();
            listtransferposting = DatabaseHelper.getInstance().getonePosition_transferposting(id_unique_SO);
            String position_from_cd = listtransferposting.get(0).getPOSITION_FROM_CD();
            String position_to_cd = listtransferposting.get(0).getPOSITION_TO_CD();

            if((!position_from_cd.equals(""))&&(!position_to_cd.equals(""))){
                String check_position = new CmnFns().Check_Position_Same_SLOC(position_from_cd,position_to_cd,"WTP");

                if(check_position.equals("Th??nh C??ng")){
                    Dialog dialog = new Dialog(List_TransferPosting.this);

                    if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                        dialog.showDialog(List_TransferPosting.this, "Vui L??ng Th??? L???i");

                    } else if (postitionDes.equals("-3")) {
                        dialog.showDialog(List_TransferPosting.this, "V??? tr?? t??? kh??ng h???p l???");

                    } else if (postitionDes.equals("-6")) {
                        dialog.showDialog(List_TransferPosting.this, "V??? tr?? ?????n kh??ng h???p l???");

                    } else if (postitionDes.equals("-5")) {
                        dialog.showDialog(List_TransferPosting.this, "V??? tr?? t??? tr??ng v??? tr?? ?????n");

                    } else if (postitionDes.equals("-14")) {
                        dialog.showDialog(List_TransferPosting.this, "V??? tr?? ?????n tr??ng v??? tr?? t???");

                    } else if (postitionDes.equals("-15")) {
                        dialog.showDialog(List_TransferPosting.this, "V??? tr?? t??? kh??ng c?? trong h??? th???ng");

                    } else if (postitionDes.equals("-10")) {
                        dialog.showDialog(List_TransferPosting.this, "M?? LPN kh??ng c?? trong h??? th???ng");

                    } else if (postitionDes.equals("-17")) {
                        dialog.showDialog(List_TransferPosting.this, "LPN t??? tr??ng LPN ?????n");

                    } else if (postitionDes.equals("-18")) {
                        dialog.showDialog(List_TransferPosting.this, "LPN ?????n tr??ng LPN t???");

                    } else if (postitionDes.equals("-19")) {
                        dialog.showDialog(List_TransferPosting.this, "V??? tr?? ?????n kh??ng c?? trong h??? th???ng");

                    } else if (postitionDes.equals("-12")) {
                        dialog.showDialog(List_TransferPosting.this, "M?? LPN kh??ng c?? trong t???n kho");

                    } else if (postitionDes.equals("-27")) {
                        dialog.showDialog(List_TransferPosting.this, "V??? tr?? t??? ch??a c?? s???n ph???m");

                    } else if (postitionDes.equals("-28")) {
                        dialog.showDialog(List_TransferPosting.this, "LPN ?????n c?? v??? tr?? kh??ng h???p l???");

                    } else {
                        return;
                    }
                }else{
                    if (positonReceive.equals("1") && productCd != null) {
                        DatabaseHelper.getInstance().updatePositionFrom_transferPosting(id_unique_SO,"","","","","","","" );

                    }else if (positonReceive.equals("2") && productCd != null) {
                        DatabaseHelper.getInstance().updatePositionTo_transferPosting(id_unique_SO,"","","","","","","" );

                    }
                    Dialog dialog = new Dialog(List_TransferPosting.this);
                    dialog.showDialog(List_TransferPosting.this, check_position);
                }
            }else{
                Dialog dialog = new Dialog(List_TransferPosting.this);

                if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                    dialog.showDialog(List_TransferPosting.this, "Vui L??ng Th??? L???i");

                } else if (postitionDes.equals("-3")) {
                    dialog.showDialog(List_TransferPosting.this, "V??? tr?? t??? kh??ng h???p l???");

                } else if (postitionDes.equals("-6")) {
                    dialog.showDialog(List_TransferPosting.this, "V??? tr?? ?????n kh??ng h???p l???");

                } else if (postitionDes.equals("-5")) {
                    dialog.showDialog(List_TransferPosting.this, "V??? tr?? t??? tr??ng v??? tr?? ?????n");

                } else if (postitionDes.equals("-14")) {
                    dialog.showDialog(List_TransferPosting.this, "V??? tr?? ?????n tr??ng v??? tr?? t???");

                } else if (postitionDes.equals("-15")) {
                    dialog.showDialog(List_TransferPosting.this, "V??? tr?? t??? kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-10")) {
                    dialog.showDialog(List_TransferPosting.this, "M?? LPN kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-17")) {
                    dialog.showDialog(List_TransferPosting.this, "LPN t??? tr??ng LPN ?????n");

                } else if (postitionDes.equals("-18")) {
                    dialog.showDialog(List_TransferPosting.this, "LPN ?????n tr??ng LPN t???");

                } else if (postitionDes.equals("-19")) {
                    dialog.showDialog(List_TransferPosting.this, "V??? tr?? ?????n kh??ng c?? trong h??? th???ng");

                } else if (postitionDes.equals("-12")) {
                    dialog.showDialog(List_TransferPosting.this, "M?? LPN kh??ng c?? trong t???n kho");

                } else if (postitionDes.equals("-27")) {
                    dialog.showDialog(List_TransferPosting.this, "V??? tr?? t??? ch??a c?? s???n ph???m");

                } else if (postitionDes.equals("-28")) {
                    dialog.showDialog(List_TransferPosting.this, "LPN ?????n c?? v??? tr?? kh??ng h???p l???");

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
            int postitionDes = new CmnFns().synchronizeGETProductByZoneTransfer_Posting(List_TransferPosting.this, value1, CmnFns.readDataAdmin(),
                    expDate, ea_unit, stockinDate, global.getTransferPostingCD(), isLPN ,batch_number,pro_code , pro_name, pro_cd);

            Dialog dialog = new Dialog(List_TransferPosting.this);

            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(List_TransferPosting.this, "Vui L??ng Th??? L???i");

            } else if (postitionDes == -8) {
                dialog.showDialog(List_TransferPosting.this, "M?? s???n ph???m kh??ng c?? tr??n phi???u");


            } else if (postitionDes == -10) {
                dialog.showDialog(List_TransferPosting.this, "M?? LPN kh??ng c?? trong h??? th???ng");

            } else if (postitionDes == -11) {

                dialog.showDialog(List_TransferPosting.this, "M?? s???n ph???m kh??ng c?? trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(List_TransferPosting.this, "M?? LPN kh??ng c?? trong kho");

            } else if (postitionDes == -16) {

                dialog.showDialog(List_TransferPosting.this, "S???n ph???m ???? qu??t kh??ng n???m trong LPN n??o");

            } else if (postitionDes == -20) {

                dialog.showDialog(List_TransferPosting.this, "M?? s???n ph???m kh??ng c?? trong h??? th???ng");

            } else if (postitionDes == -21) {

                dialog.showDialog(List_TransferPosting.this, "M?? s???n ph???m kh??ng c?? trong zone");

            } else if (postitionDes == -22) {

                dialog.showDialog(List_TransferPosting.this, "M?? LPN kh??ng c?? trong zone");

            }
        } catch (Exception e) {
            Toast.makeText(this, "Vui L??ng Th??? L???i ...", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}
