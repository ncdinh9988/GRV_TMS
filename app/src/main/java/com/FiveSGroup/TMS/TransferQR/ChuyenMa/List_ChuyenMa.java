package com.FiveSGroup.TMS.TransferQR.ChuyenMa;

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
import com.FiveSGroup.TMS.PoReturn.Home_PoReturn;
import com.FiveSGroup.TMS.QA.HomeQA.List_QA;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.StockOut.ListQrcode_Stockout;
import com.FiveSGroup.TMS.StockOut.Product_StockOut;
import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.ProductAdapter;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class List_ChuyenMa extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    //ProductListViewAdapter productListViewAdapter;
    ProductAdapter productListViewAdapter;
    RecyclerView listVieWTPoduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String stock = "";
    String expDate = "";
    String expDate1 = "";
    String chuyen_ma = "";
    String ea_unit = "";
    String pro_code = "";
    String pro_name = "";
    String ea_unit_position = "";
    String check_chuyenma = "";
    String stockinDate = "";
    String batch_number = "";
    String key = "";
    String lpn = "", id_unique_SO = "";

    int statusGetCust;
    Product_ChuyenMa product_qrcode;

    ArrayList<Product_ChuyenMa> chuyen_Ma;
    CheckEventbus eventbus;

    ChuyenMa_Adapter ChuyenMa_ListAdapter;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transfer);

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
        stock = intent.getStringExtra("returnStock");
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        chuyen_ma = intent.getStringExtra("chuyen_ma");
        check_chuyenma = intent.getStringExtra("check_chuyenma");
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
            Dialog dialog = new Dialog(List_ChuyenMa.this);
            dialog.showDialog(List_ChuyenMa.this, "Mã Sản Phẩm Không Có Trong Phiếu");
        }

        DatabaseHelper.getInstance().getAllProduct_ChuyenMa(global.getChuyenMaCD());
        chuyen_Ma = DatabaseHelper.getInstance().getshow_ChuyenMa(global.getChuyenMaCD());
        ChuyenMa_ListAdapter = new ChuyenMa_Adapter(this, chuyen_Ma);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listVieWTPoduct.setLayoutManager(layoutManager);
        listVieWTPoduct.setAdapter(ChuyenMa_ListAdapter);
        ChuyenMa_ListAdapter.notifyDataSetChanged();
        chuyen_ma = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(List_ChuyenMa.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(List_ChuyenMa.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(List_ChuyenMa.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(List_ChuyenMa.this, List_ChuyenMa.class);
                        startActivity(i);

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();

                        int position = viewHolder.getAdapterPosition();
                        Product_ChuyenMa product = chuyen_Ma.get(position);
                        chuyen_Ma.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_Chuyen_Ma_Specific(product.getPRODUCT_CD(),
                                product.getBATCH_NUMBER(),product.getITEM_BASIC(),product.getEXPIRED_DATE(),product.getWAREHOUSE_POSITION_CD());
                        ChuyenMa_ListAdapter.notifyItemRemoved(position);
                    }
                });
                dialog.show();


            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listVieWTPoduct);
    }

    public void alert_show_SP(int isLPN) {
        if(check_chuyenma != null ) {
            try {
                int postitionDes = new CmnFns().synchronizeGETProductByZoneChuyenMa(value1, CmnFns.readDataAdmin(), "WTP", 0,
                        global.getChuyenMaCD(), expDate , batch_number,stockinDate , ea_unit,pro_code , pro_name);
                Dialog dialog = new Dialog(List_ChuyenMa.this);

                if (postitionDes == 1) {
                    return;
                } else if (postitionDes == -1) {
                    dialog.showDialog(List_ChuyenMa.this, "Vui Lòng Thử Lại");

                } else if (postitionDes == -8) {
                    dialog.showDialog(List_ChuyenMa.this, "Mã sản phẩm không có trên phiếu");


                } else if (postitionDes == -10) {
                    dialog.showDialog(List_ChuyenMa.this, "Mã LPN không có trong hệ thống");

                } else if (postitionDes == -11) {

                    dialog.showDialog(List_ChuyenMa.this, "Mã sản phẩm không có trong kho");


                } else if (postitionDes == -12) {

                    dialog.showDialog(List_ChuyenMa.this, "Mã LPN không có trong kho");

                } else if (postitionDes == -16) {

                    dialog.showDialog(List_ChuyenMa.this, "Sản phẩm đã quét không nằm trong LPN nào");

                } else if (postitionDes == -20) {

                    dialog.showDialog(List_ChuyenMa.this, "Mã sản phẩm không có trong hệ thống");

                } else if (postitionDes == -21) {

                    dialog.showDialog(List_ChuyenMa.this, "Mã sản phẩm không có trong zone");

                } else if (postitionDes == -22) {

                    dialog.showDialog(List_ChuyenMa.this, "Mã LPN không có trong zone");

                }


            } catch(Exception e){
                Toast.makeText(this, "Vui Lòng Thử Lại ...", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        tvTitle.setText("Danh Sách SP Phân Loại");
    }

    private void startScan() {

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        Intent intent = new Intent(List_ChuyenMa.this, Qrcode_ChuyenMa.class);
        intent.putExtra("check_to_finish_at_list", "check");
        startActivity(intent);
        finish();

    }

    private boolean alert_allow() {
        boolean check = false;

        List<Product_ChuyenMa> product = DatabaseHelper.getInstance().getCheckQTy_ChuyenMa(global.getChuyenMaCD());

        for (int i = 0; i < product.size(); i++) {
            Product_ChuyenMa allow_chuyenma = product.get(i);
            float qty = Float.parseFloat(allow_chuyenma.getSUM_QTY());
            float qty_original = Float.parseFloat(allow_chuyenma.getQTY_SET_AVAILABLE_ORIGINAL());
            if(qty>qty_original){
                return true;
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
        Dialog dialog = new Dialog(List_ChuyenMa.this);

        if(alert_allow()){
            dialog.showDialog(List_ChuyenMa.this,"Vui Lòng Kiểm Tra Lại Số Lượng Loại SP");

        }else {

            try {
                int result = new CmnFns().synchronizeData_RQBT_Final(saleCode, "WTP", global.getChuyenMaCD());
                if (result >= 1) {
                    DatabaseHelper.getInstance().deleteallChuyenMa(global.getChuyenMaCD());
                    ShowSuccessMessage("Lưu thành công");
//                    Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();

                } else {

                    if (result == -1) {
                        dialog.showDialog(List_ChuyenMa.this, "Lưu thất bại");
                    } else if (result == -2) {
                        dialog.showDialog(List_ChuyenMa.this, "Số lượng không đủ trong tồn kho");

                    } else if (result == -3) {
                        dialog.showDialog(List_ChuyenMa.this, "Vị trí từ không hợp lệ");

                    } else if (result == -4) {
                        dialog.showDialog(List_ChuyenMa.this, "Trạng thái của phiếu không hợp lệ");

                    } else if (result == -5) {
                        dialog.showDialog(List_ChuyenMa.this, "Vị trí từ trùng vị trí đên");

                    } else if (result == -6) {
                        dialog.showDialog(List_ChuyenMa.this, "Vị trí đến không hợp lệ");

                    } else if (result == -7) {
                        dialog.showDialog(List_ChuyenMa.this, "Cập nhật trạng thái thất bại");

                    } else if (result == -8) {
                        dialog.showDialog(List_ChuyenMa.this, "Sản phẩm không có thông tin trên phiếu ");

                    } else if (result == -13) {
                        dialog.showDialog(List_ChuyenMa.this, "Dữ liệu không hợp lệ");

                    } else if (result == -24) {
                        dialog.showDialog(List_ChuyenMa.this, "Vui Lòng Kiểm Tra Lại Số Lượng");

                    } else if (result == -26) {
                        dialog.showDialog(List_ChuyenMa.this, "Số Lượng Vượt Quá Yêu Cầu Trên SO");

                    } else {
                        dialog.showDialog(List_ChuyenMa.this, "Lưu thất bại");
                    }

                }
            } catch (Exception e) {
                Toast.makeText(this, "Vui Lòng Thử Lại ...", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

}

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(List_ChuyenMa.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(List_ChuyenMa.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
//                DatabaseHelper.getInstance().deleteProduct_ChuyenMa();
                chuyen_Ma.clear();
                ChuyenMa_ListAdapter.notifyDataSetChanged();
                Intent intentToHomeQRActivity = new Intent(List_ChuyenMa.this, Home_ChuyenMa.class);
                startActivity(intentToHomeQRActivity);
                finish();
            }
        });
        dialog.show();
    }

    private void actionBack() {
        try {
            List_ChuyenMa.this.finish();
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




//    public void alert_show_SP(int isLPN) {
//        try {
//            Product_ChuyenMa chuyenma = new Product_ChuyenMa();
////            DatabaseHelper.getInstance().Create_ChuyenMa(chuyenma);
////            int postitionDes = new CmnFns().synchronizeGETProductByZonechuyen_ma(List_ChuyenMa.this, value1, CmnFns.readDataAdmin(), expDate, ea_unit, stockinDate, global.getChuyenMaCD(), isLPN ,batch_number);
//
////            Dialog dialog = new Dialog(List_ChuyenMa.this);
////
////            if (postitionDes == 1) {
////                return;
////            } else if (postitionDes == -1) {
////                dialog.showDialog(List_ChuyenMa.this, "Vui Lòng Thử Lại");
////
////            } else if (postitionDes == -8) {
////                dialog.showDialog(List_ChuyenMa.this, "Mã sản phẩm không có trên phiếu");
////
////
////            } else if (postitionDes == -10) {
////                dialog.showDialog(List_ChuyenMa.this, "Mã LPN không có trong hệ thống");
////
////            } else if (postitionDes == -11) {
////
////                dialog.showDialog(List_ChuyenMa.this, "Mã sản phẩm không có trong kho");
////
////
////            } else if (postitionDes == -12) {
////
////                dialog.showDialog(List_ChuyenMa.this, "Mã LPN không có trong kho");
////
////            } else if (postitionDes == -16) {
////
////                dialog.showDialog(List_ChuyenMa.this, "Sản phẩm đã quét không nằm trong LPN nào");
////
////            } else if (postitionDes == -20) {
////
////                dialog.showDialog(List_ChuyenMa.this, "Mã sản phẩm không có trong hệ thống");
////
////            } else if (postitionDes == -21) {
////
////                dialog.showDialog(List_ChuyenMa.this, "Mã sản phẩm không có trong zone");
////
////            } else if (postitionDes == -22) {
////
////                dialog.showDialog(List_ChuyenMa.this, "Mã LPN không có trong zone");
////
////            }
////        } catch (Exception e) {
////            Toast.makeText(this, "Vui Lòng Thử Lại ...", Toast.LENGTH_SHORT).show();
////            finish();
////        }
//
//
//    }
}
