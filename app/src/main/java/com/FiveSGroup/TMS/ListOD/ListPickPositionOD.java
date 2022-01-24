package com.FiveSGroup.TMS.ListOD;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;

import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;

import java.util.ArrayList;

public class ListPickPositionOD extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    RecyclerView listViewProduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String stock = "";
    String batch_number = "";
    String expDate = "";
    String expDate1 = "";
    String let_down = "";
    String ea_unit = "";
    String key = "";
    String ea_unit_position = "";
    String stockinDate = "", id_unique_LD = "";
    TextView tvTitle;
    String fromLetDownSuggestionsActivity = "";
    String lpn = "";
    String pro_code = "";
    String pro_name = "";
    String pro_cd = "";
    String cd = "";
    int result;


    ArrayList<Product_OD> productOD;

    PickPositionODAdapter positionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pick_position);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getValueFromIntent();
        init();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        prepareData();
    }

    private void init() {
        Intent intent = getIntent();
        cd = intent.getStringExtra("POSITION_CD");
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        buttonBack = findViewById(R.id.buttonBack);
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Danh Sách SP Pick OD");
        btnscan_barcode.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        btnok.setOnClickListener(this);
    }

    private void prepareData() {

        productOD = DatabaseHelper.getInstance().getAllProductOD();
        positionAdapter = new PickPositionODAdapter(this, productOD);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(positionAdapter);
        positionAdapter.notifyDataSetChanged();
        let_down = "";

    }


    private void getValueFromIntent() {
        Intent intent = getIntent();
        // value1 : giá trị barcode truyền từ LetDownQrCodeActivity
        value1 = intent.getStringExtra("btn1");
        // xác định vị trí là from hay to
        positonReceive = intent.getStringExtra("returnposition");
        productCd = intent.getStringExtra("returnCD");
        key = intent.getStringExtra("key");
        stock = intent.getStringExtra("returnStock");
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
        pro_cd = intent.getStringExtra("pro_cd");
        batch_number = intent.getStringExtra("batch_number");

        // expDate - hiển thị HSD cho người dùng trong list sản phẩm
        expDate = intent.getStringExtra("exp_date");
        id_unique_LD = intent.getStringExtra("id_unique_LD");

        //  expdate1 xử lí position from - to
        expDate1 = intent.getStringExtra("expdate");
        let_down = intent.getStringExtra("let_down");
        // ea_unit : đơn vị trả về từ LetDownQRCodeActivity
        ea_unit = intent.getStringExtra("ea_unit");
        lpn = intent.getStringExtra("lpn");

        stockinDate = intent.getStringExtra("stockin_date");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        fromLetDownSuggestionsActivity = intent.getStringExtra("fromLetDownSuggestionsActivity");

        try {
            if (batch_number.equals("---")) {
                batch_number = "";
            }
            if (batch_number == null) {
                batch_number = "";
            }
        } catch (Exception e) {

        }
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
            LayoutInflater factory = LayoutInflater.from(ListPickPositionOD.this);
            View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
            final AlertDialog dialog = new AlertDialog.Builder(ListPickPositionOD.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                    DatabaseHelper.getInstance().deleteProduct_OD();
                    dialog.dismiss();
//                    if (fromLetDownSuggestionsActivity!=null){
                    Intent intent = new Intent(ListPickPositionOD.this, HomeOD.class);
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

        Dialog dialog = new Dialog(ListPickPositionOD.this);

        if (let_down != null) {
            String saleCode = CmnFns.readDataAdmin();

            if (productOD.size() > 0) {

                    result = new CmnFns().synchronizeData(saleCode, "OD", "");
                    if (result >= 1) {
                        ShowSuccessMessage("Lưu thành công");
                    } else {

                        if (result == -2) {
                            dialog.showDialog(ListPickPositionOD.this, "Số lượng không đủ trong tồn kho");
                        } else if (result == -3) {
                            dialog.showDialog(ListPickPositionOD.this, "Vị trí từ không hợp lệ");
                        } else if (result == -4) {
                            dialog.showDialog(ListPickPositionOD.this, "Trạng thái của phiếu không hợp lệ");
                        } else if (result == -5) {
                            dialog.showDialog(ListPickPositionOD.this, "Vị trí từ trùng vị trí đên");
                        } else if (result == -6) {
                            dialog.showDialog(ListPickPositionOD.this, "Vị trí đến không hợp lệ");
                        } else if (result == -7) {
                            dialog.showDialog(ListPickPositionOD.this, "Cập nhật trạng thái thất bại");
                        } else if (result == -8) {
                            dialog.showDialog(ListPickPositionOD.this, "Sản phẩm không có thông tin trên phiếu ");
                        } else if (result == -13) {
                            dialog.showDialog(ListPickPositionOD.this, "Dữ liệu không hợp lệ");

                        } else if (result == -24) {
                            dialog.showDialog(ListPickPositionOD.this, "Vui Lòng Kiểm Tra Lại Số Lượng");

                        } else if (result == -26) {
                            dialog.showDialog(ListPickPositionOD.this, "Số Lượng Vượt Quá Yêu Cầu Trên SO");

                        } else {
                            dialog.showDialog(ListPickPositionOD.this, "Lưu thất bại");
                        }

                    }

            } else {
                dialog.showDialog(ListPickPositionOD.this, "Không có sản phẩm");

            }


        }
    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(ListPickPositionOD.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(ListPickPositionOD.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
//                        Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                DatabaseHelper.getInstance().deleteProduct_Letdown();
                productOD.clear();
                positionAdapter.notifyDataSetChanged();

                Intent intentToHomeQRActivity = new Intent(ListPickPositionOD.this, HomeOD.class);
                startActivity(intentToHomeQRActivity);
                finish();
            }
        });
        dialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
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
                if (let_down != null) {
                    Intent intent = new Intent(ListPickPositionOD.this, Qrcode_OD.class);
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
            LayoutInflater factory = LayoutInflater.from(ListPickPositionOD.this);
            View layout_cus = factory.inflate(R.layout.layout_request, null);
            final AlertDialog dialog = new AlertDialog.Builder(ListPickPositionOD.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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

}
