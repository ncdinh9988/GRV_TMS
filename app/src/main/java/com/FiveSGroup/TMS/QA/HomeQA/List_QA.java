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
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    private void prepareData() {
        if (key == null || key.equals("")) {
            alert_show_SP(0);
        } else {
            Dialog dialog = new Dialog(List_QA.this);
            dialog.showDialog(List_QA.this, "Mã Sản Phẩm Không Có Trong Phiếu");
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
                        //Khi nhấn no dữ liệu sẽ trả về đơn vị trước đó cần phải chuyển tới màn hình chính nó.
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


    private boolean isNotScanFromOrTo() {
        boolean check = false;
        List<Product_QA> product = DatabaseHelper.getInstance().getAllProduct_QA(global.getQACD());

        for (int i = 0; i < product.size(); i++) {
            Product_QA cancelGood = product.get(i);
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
        buttonBack.setText("Trở Về");
        btnok = findViewById(R.id.buttonOK);
        listVieWTPoduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Danh Sách SP Kiểm Định");
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

            try {
                new CmnFns().synchronizePhoto_QA(List_QA.this , global.getQACD() );
                DatabaseHelper.getInstance().getAllProduct_RESULT_QA(global.getQACD());
                int result = new CmnFns().synchronizeData_RQBT_Final(saleCode, "WQA", global.getQACD());
                if (result >= 1) {
//                    new CmnFns().synchronizePhoto_QA(List_QA.this , global.getQACD() );
                    DatabaseHelper.getInstance().deleteallResult_QA(global.getQACD());
                    DatabaseHelper.getInstance().deleteProduct_QA(global.getQACD());
                    DatabaseHelper.getInstance().deleteallCriteria(global.getQACD());
                    ShowSuccessMessage("Lưu thành công");
//                    Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseHelper.getInstance().deleteallResult_QA(global.getQACD());
                    if (result == -1) {
                        dialog.showDialog(List_QA.this, "Lưu thất bại");
                    } else if (result == -2) {
                        dialog.showDialog(List_QA.this, "Số lượng không đủ trong tồn kho");

                    } else if (result == -3) {
                        dialog.showDialog(List_QA.this, "Vị trí từ không hợp lệ");

                    } else if (result == -4) {
                        dialog.showDialog(List_QA.this, "Trạng thái của phiếu không hợp lệ");

                    } else if (result == -5) {
                        dialog.showDialog(List_QA.this, "Vị trí từ trùng vị trí đên");

                    } else if (result == -6) {
                        dialog.showDialog(List_QA.this, "Vị trí đến không hợp lệ");

                    } else if (result == -7) {
                        dialog.showDialog(List_QA.this, "Cập nhật trạng thái thất bại");

                    } else if (result == -8) {
                        dialog.showDialog(List_QA.this, "Sản phẩm không có thông tin trên phiếu ");

                    } else if (result == -13) {
                        dialog.showDialog(List_QA.this, "Dữ liệu không hợp lệ");

                    } else if (result == -24) {
                        dialog.showDialog(List_QA.this, "Vui Lòng Kiểm Tra Lại Số Lượng");

                    } else if (result == -26) {
                        dialog.showDialog(List_QA.this, "Số Lượng Vượt Quá Yêu Cầu Trên SO");

                    } else {
                        dialog.showDialog(List_QA.this, "Lưu thất bại");
                    }

                }
            } catch (Exception e) {
                Toast.makeText(this, "Vui Lòng Thử Lại ...", Toast.LENGTH_SHORT).show();
                finish();
            }


        } else {
            dialog.showDialog(List_QA.this, "Không có sản phẩm");

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
                        ea_unit, stockinDate, global.getQACD(), isLPN, batch_number,pro_code , pro_name);

                Dialog dialog = new Dialog(List_QA.this);

                if (postitionDes == 1) {
                    return;
                }
                else if (postitionDes == -1) {
                    dialog.showDialog(List_QA.this, "Vui Lòng Thử Lại");

                }
                else if (postitionDes == -8) {
                    dialog.showDialog(List_QA.this, "Mã sản phẩm không có trên phiếu");


                } else if (postitionDes == -10) {
                    dialog.showDialog(List_QA.this, "Mã LPN không có trong hệ thống");

                } else if (postitionDes == -11) {

                    dialog.showDialog(List_QA.this, "Mã sản phẩm không có trong kho");


                } else if (postitionDes == -12) {

                    dialog.showDialog(List_QA.this, "Mã LPN không có trong kho");

                } else if (postitionDes == -16) {

                    dialog.showDialog(List_QA.this, "Sản phẩm đã quét không nằm trong LPN nào");

                } else if (postitionDes == -20) {

                    dialog.showDialog(List_QA.this, "Mã sản phẩm không có trong hệ thống");

                } else if (postitionDes == -21) {

                    dialog.showDialog(List_QA.this, "Mã sản phẩm không có trong zone");

                } else if (postitionDes == -22) {

                    dialog.showDialog(List_QA.this, "Mã LPN không có trong zone");

                }

            } catch(Exception e){
                Toast.makeText(this, "Vui Lòng Thử Lại ...", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
}