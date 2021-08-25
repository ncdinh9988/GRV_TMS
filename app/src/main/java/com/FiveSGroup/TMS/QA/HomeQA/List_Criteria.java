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
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.ProductAdapter;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class List_Criteria extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;

    //ProductListViewAdapter productListViewAdapter;
    ProductAdapter productListViewAdapter;
    RecyclerView listVieWTPoduct;
    String value1 = "";
    String positonReceive = "";
    String product_code = "";
    String barcode = "";
    String stock = "";
    String expDate = "";
    String expDate1 = "";
    String transfer_QA = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String batch_number = "";
    String cd = "";
    String lpn = "", id_unique_SO = "";

    int statusGetCust;
    Product_Criteria product_qrcode;

    ArrayList<Product_Criteria> Product_Criteria;
    CheckEventbus eventbus;

    Criteria_Adapter QAlistAdapter;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_criteria);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getDataFromIntent();

        init();
        buttonBack.setOnClickListener(this);
        btnok.setOnClickListener(this);

        prepareData();
    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        value1 = intent.getStringExtra("btn1");
        positonReceive = intent.getStringExtra("returnposition");
        product_code = intent.getStringExtra("product_code");
        batch_number = intent.getStringExtra("batch_number");
        barcode = intent.getStringExtra("barcode");
        cd = intent.getStringExtra("cd");
        stock = intent.getStringExtra("returnStock");
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        transfer_QA = intent.getStringExtra("transfer_QA");
        ea_unit = intent.getStringExtra("ea_unit");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        lpn = intent.getStringExtra("lpn");
        id_unique_SO = intent.getStringExtra("id_unique_SO");


        stockinDate = intent.getStringExtra("stockin_date");
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    private void prepareData() {

        alert_show_SP();

        Product_Criteria = DatabaseHelper.getInstance().getallCriteria(batch_number);
        QAlistAdapter = new Criteria_Adapter(this, Product_Criteria);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listVieWTPoduct.setLayoutManager(layoutManager);
        listVieWTPoduct.setAdapter(QAlistAdapter);
        QAlistAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public void onBackPressed() {

    }


    private void init() {
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setText("Trở Về");
        btnok = findViewById(R.id.buttonOK);
        listVieWTPoduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Danh Sách SP Phân Hàng");
    }

    private void startScan() {

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        Intent intent = new Intent(List_Criteria.this, Qrcode_QA.class);
        intent.putExtra("check_to_finish_at_list", "check");
        startActivity(intent);
        finish();

    }


    private void synchronizeToService() {
        String saleCode = CmnFns.readDataAdmin();
        Dialog dialog = new Dialog(List_Criteria.this);
        DatabaseHelper.getInstance().updateChecked_QA(batch_number ,cd);
        DatabaseHelper.getInstance().getAllProduct_RESULT_QA(cd);
        ShowSuccessMessage("Lưu thành công");

//                    Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();




    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(List_Criteria.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(List_Criteria.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
//                DatabaseHelper.getInstance().deleteallCriteria(batch_number);
                Product_Criteria.clear();
                QAlistAdapter.notifyDataSetChanged();
                Intent intentToHomeQRActivity = new Intent(List_Criteria.this, List_QA.class);
                startActivity(intentToHomeQRActivity);
                finish();
            }
        });
        dialog.show();
    }

    private void actionBack() {
        try {
            List_Criteria.this.finish();
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


    public void alert_show_SP() {
        try {

            int postitionDes = new CmnFns().GetMaterialInspection(List_Criteria.this, barcode, CmnFns.readDataAdmin() ,batch_number , cd);

            Dialog dialog = new Dialog(List_Criteria.this);

//            if (postitionDes == 1) {
//                return;
//            } else if (postitionDes == -1) {
//                dialog.showDialog(List_Criteria.this, "Vui Lòng Thử Lại");
//
//            }
        } catch (Exception e) {
            Toast.makeText(this, "Vui Lòng Thử Lại ...", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}