package com.FiveSGroup.TMS.QA.HomeQA;

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
import com.FiveSGroup.TMS.QA.HomeQA.Image_QA.TakePhoto_QA;
import com.FiveSGroup.TMS.QA.Pickup.List_Pickup;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;

import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.ProductAdapter;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class List_QA extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    //ProductListViewAdapter productListViewAdapter;
    ProductAdapter productListViewAdapter;
    RecyclerView listVieWTPoduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String product_code = "";
    String pro_code = "";
    String pro_name = "";
    String pro_cd = "";

    String stock = "";
    String expDate = "";
    String key = "";
    String expDate1 = "";
    String transfer_QA = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String allow = "";
    String batch_number = "";
    String lpn = "", id_unique_SO = "";

    int statusGetCust;
    Product_QA product_qrcode;

    ArrayList<Product_QA> product_QA;
    CheckEventbus eventbus;

    QA_Adapter QAlistAdapter;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_qa);

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
        product_code = intent.getStringExtra("product_code");
        productCd = intent.getStringExtra("returnCD");
        pro_cd = intent.getStringExtra("pro_cd");
        allow = intent.getStringExtra("allow");
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

        stock = intent.getStringExtra("returnStock");
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        transfer_QA = intent.getStringExtra("transfer_QA");
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
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
        if (key == null || key.equals("")) {
            alert_show_SP(0);
        } else {
            Dialog dialog = new Dialog(List_QA.this);
            dialog.showDialog(List_QA.this, "M?? S???n Ph???m Kh??ng C?? Trong Phi???u");
        }

        product_QA = DatabaseHelper.getInstance().getAllProduct_QA(global.getQACD());
        QAlistAdapter = new QA_Adapter(this, product_QA);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listVieWTPoduct.setLayoutManager(layoutManager);
        listVieWTPoduct.setAdapter(QAlistAdapter);
        QAlistAdapter.notifyDataSetChanged();
        transfer_QA = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(List_QA.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(List_QA.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(List_QA.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(List_QA.this, List_QA.class);
                        startActivity(i);

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();
                        int position = viewHolder.getAdapterPosition();
                        Product_QA product = product_QA.get(position);
                        product_QA.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_QA_Specific(product.getAUTOINCREMENT());
                        DatabaseHelper.getInstance().deleterowCriteria(product.getBATCH_NUMBER(),global.getQACD());
                        QAlistAdapter.notifyItemRemoved(position);
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


    private boolean isChecked() {
        boolean check = false;
        List<Product_QA> product = DatabaseHelper.getInstance().getAllProduct_QA(global.getQACD());

        for (int i = 0; i < product.size(); i++) {
            Product_QA qa = product.get(i);
            String values = qa.getCHECKED();

            if (values.equals("No")) {
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
        tvTitle.setText("Danh S??ch SP Ki???m ?????nh");
    }

    private void startScan() {

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        Intent intent = new Intent(List_QA.this, Qrcode_QA.class);
        intent.putExtra("check_to_finish_at_list", "check");
        startActivity(intent);
        finish();

    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<Product_QA> product = DatabaseHelper.getInstance().getAllProduct_QA(global.getQACD());
        for (int i = 0; i < product.size(); i++) {
            Product_QA putAway = product.get(i);
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
        Dialog dialog = new Dialog(List_QA.this);


        if (product_QA.size() > 0) {
            if (isChecked()) {
                dialog.showDialog(List_QA.this, "Kh??ng C?? K???t Qu??? Ki???m ?????nh ????? ?????ng B???");

            }else{
                try {
                    new CmnFns().synchronizePhoto_QA(List_QA.this , global.getQACD() );
                    DatabaseHelper.getInstance().getAllProduct_RESULT_QA(global.getQACD());
                    int result = new CmnFns().synchronizeData_RQBT_Final(saleCode, "WQA", global.getQACD());
                    if (result >= 1) {
//                    new CmnFns().synchronizePhoto_QA(List_QA.this , global.getQACD() );
                        DatabaseHelper.getInstance().deleteallResult_QA(global.getQACD());
                        DatabaseHelper.getInstance().deleteProduct_QA(global.getQACD());
                        DatabaseHelper.getInstance().deleteallCriteria(global.getQACD());
                        ShowSuccessMessage("L??u th??nh c??ng");
//                    Toast.makeText(getApplication(), "L??u th??nh c??ng", Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseHelper.getInstance().deleteallResult_QA(global.getQACD());
                        if (result == -1) {
                            dialog.showDialog(List_QA.this, "L??u th???t b???i");
                        } else if (result == -2) {
                            dialog.showDialog(List_QA.this, "S??? l?????ng kh??ng ????? trong t???n kho");

                        } else if (result == -3) {
                            dialog.showDialog(List_QA.this, "V??? tr?? t??? kh??ng h???p l???");

                        } else if (result == -4) {
                            dialog.showDialog(List_QA.this, "Tr???ng th??i c???a phi???u kh??ng h???p l???");

                        } else if (result == -5) {
                            dialog.showDialog(List_QA.this, "V??? tr?? t??? tr??ng v??? tr?? ????n");

                        } else if (result == -6) {
                            dialog.showDialog(List_QA.this, "V??? tr?? ?????n kh??ng h???p l???");

                        } else if (result == -7) {
                            dialog.showDialog(List_QA.this, "C???p nh???t tr???ng th??i th???t b???i");

                        } else if (result == -8) {
                            dialog.showDialog(List_QA.this, "S???n ph???m kh??ng c?? th??ng tin tr??n phi???u ");

                        } else if (result == -13) {
                            dialog.showDialog(List_QA.this, "D??? li???u kh??ng h???p l???");

                        } else if (result == -24) {
                            dialog.showDialog(List_QA.this, "Vui L??ng Ki???m Tra L???i S??? L?????ng");

                        } else if (result == -26) {
                            dialog.showDialog(List_QA.this, "S??? L?????ng V?????t Qu?? Y??u C???u Tr??n SO");

                        } else {
                            dialog.showDialog(List_QA.this, "L??u th???t b???i");
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Vui L??ng Th??? L???i ...", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }


        } else {
            dialog.showDialog(List_QA.this, "Kh??ng c?? s???n ph???m");

        }

    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(List_QA.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(List_QA.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                product_QA.clear();
                QAlistAdapter.notifyDataSetChanged();
                Intent intentToHomeQRActivity = new Intent(List_QA.this, Home_QA.class);
                startActivity(intentToHomeQRActivity);
                finish();
            }
        });
        dialog.show();
    }

    private void actionBack() {
        try {
            List_QA.this.finish();
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



    public void alert_show_SP(int isLPN) {
        if(allow != null ){
            try {

                int postitionDes = new CmnFns().synchronizeGETProductByZoneQA(List_QA.this, value1, CmnFns.readDataAdmin(), expDate,
                        ea_unit, stockinDate, global.getQACD(), isLPN, batch_number,pro_code , pro_name, pro_cd);

                Dialog dialog = new Dialog(List_QA.this);

                if (postitionDes == 1) {
                    return;
                }
                else if (postitionDes == -1) {
                    dialog.showDialog(List_QA.this, "Vui L??ng Th??? L???i");

                }
                else if (postitionDes == -8) {
                    dialog.showDialog(List_QA.this, "M?? s???n ph???m kh??ng c?? tr??n phi???u");


                } else if (postitionDes == -10) {
                    dialog.showDialog(List_QA.this, "M?? LPN kh??ng c?? trong h??? th???ng");

                } else if (postitionDes == -11) {

                    dialog.showDialog(List_QA.this, "M?? s???n ph???m kh??ng c?? trong kho");


                } else if (postitionDes == -12) {

                    dialog.showDialog(List_QA.this, "M?? LPN kh??ng c?? trong kho");

                } else if (postitionDes == -16) {

                    dialog.showDialog(List_QA.this, "S???n ph???m ???? qu??t kh??ng n???m trong LPN n??o");

                } else if (postitionDes == -20) {

                    dialog.showDialog(List_QA.this, "M?? s???n ph???m kh??ng c?? trong h??? th???ng");

                } else if (postitionDes == -21) {

                    dialog.showDialog(List_QA.this, "M?? s???n ph???m kh??ng c?? trong zone");

                } else if (postitionDes == -22) {

                    dialog.showDialog(List_QA.this, "M?? LPN kh??ng c?? trong zone");

                }

            } catch(Exception e){
                Toast.makeText(this, "Vui L??ng Th??? L???i ...", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
}