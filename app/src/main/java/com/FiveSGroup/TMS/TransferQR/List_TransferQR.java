package com.FiveSGroup.TMS.TransferQR;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;
import com.FiveSGroup.TMS.StockOut.Home_Stockout;
import com.FiveSGroup.TMS.StockOut.List_TransferQR;
import com.FiveSGroup.TMS.StockOut.Product_TransferQR;
import com.FiveSGroup.TMS.StockOut.Qrcode_Stock_Out;
import com.FiveSGroup.TMS.StockOut.TransferQRAdapter;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitActivity;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitAdapter;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitProduct;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitQrcode;
import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.Warehouse.ProductAdapter;
import com.FiveSGroup.TMS.Warehouse_Adjustment.ListQrcode_Warehouse_Adjustment;
import com.FiveSGroup.TMS.Warehouse_Adjustment.Product_Warehouse_Adjustment;
import com.FiveSGroup.TMS.Warehouse_Adjustment.Warehouse_Adjustment_Adapter;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.List;

public class List_TransferQR extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    //ProductListViewAdapter productListViewAdapter;
    ProductAdapter productListViewAdapter;
    RecyclerView listViewProduct;
    String value1 = "";
    String positonReceive = "";
    String productCd = "";
    String stock = "";
    String expDate = "";
    String expDate1 = "";
    String stock_out = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String lpn = "" , id_unique_SO = "";

    int statusGetCust;
    Product_TransferQR product_qrcode;

    ArrayList<Product_TransferQR> Stockout;
    CheckEventbus eventbus;

    TransferQRAdapter QRListAdapter;
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
        stock = intent.getStringExtra("returnStock");
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        stock_out = intent.getStringExtra("stock_out");
        ea_unit = intent.getStringExtra("ea_unit");
        ea_unit_position = intent.getStringExtra("return_ea_unit_position");
        lpn = intent.getStringExtra("lpn");
        id_unique_SO = intent.getStringExtra("id_unique_SO");


        stockinDate = intent.getStringExtra("stockin_date");
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    private void prepareData(){

            if (lpn != null && stock_out != null) {
                //TODO
                alert_show_SP(1);
            } else if (lpn == null && stock_out != null){
                //TODO
                alert_show_SP(0);
            }



        Stockout = DatabaseHelper.getInstance().getAllProduct_TransferQR(global.getStockoutCD());
        QRListAdapter = new TransferQRAdapter(this, Stockout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(QRListAdapter);
        QRListAdapter.notifyDataSetChanged();
        stock_out = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(List_TransferQR.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(List_TransferQR.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(List_TransferQR.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(List_TransferQR.this,List_TransferQR.class);
                        startActivity(i);

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();

                        int position = viewHolder.getAdapterPosition();
                        Product_TransferQR product = Stockout.get(position);
                        Stockout.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_TransferQR_Specific(product.getAUTOINCREMENT());
                        QRListAdapter.notifyItemRemoved(position);
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





    @Override
    public void onBackPressed() {

    }


    private void init() {
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setText("Trở Về");
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Danh Sách SP Xuất Kho");
    }

    private void startScan() {
        String check_stockout = new CmnFns().synchronizeGet_Status_Stock_Out(global.getStockoutCD());
        if(check_stockout.equals("1")) {
            DatabaseHelper.getInstance().deleteallEa_Unit();
            DatabaseHelper.getInstance().deleteallExp_date();
            Intent intent = new Intent(List_TransferQR.this, Qrcode_Stock_Out.class);
            intent.putExtra("check_to_finish_at_list", "check");
            startActivity(intent);
            finish();
        }else{
            Dialog dialog = new Dialog(List_TransferQR.this);
            dialog.showDialog(List_TransferQR.this, "Vui Lòng Tiếp Nhận Xuất Hàng Trước Khi Quét Mã");
        }
    }
//    private boolean isQuanityZero() {
//        boolean check = false;
//        List<Product_TransferQR> product = DatabaseHelper.getInstance().getAllProduct_TransferQR(global.getStockoutCD());
//        for (int i = 0; i < product.size(); i++) {
//            Product_TransferQR putAway = product.get(i);
//            String valueQty = putAway.getQTY();
//            if ((valueQty.equals("0") || (valueQty.equals("")) || (valueQty.equals("00")) || (valueQty.equals("000")) || (valueQty.equals("0000")) || (valueQty.equals("00000")))) {
//                check = true;
//            }
//        }
//
//
//        if (check == true) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    private void synchronizeToService() {
        String saleCode = CmnFns.readDataAdmin();
        Dialog dialog = new Dialog(List_TransferQR.this);


        if(Stockout.size() > 0){
//            if(isQuanityZero()){
//                dialog.showDialog(List_TransferQR.this,"Số lượng SP không được bằng 0");
//
//            }else{
                try {
                    int result = new CmnFns().synchronizeData(saleCode, "WSO", global.getStockoutCD());
                    if (result >= 1) {
                        ShowSuccessMessage("Lưu thành công");
//                    Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();

                    } else {

                        if (result == -1) {
                            dialog.showDialog(List_TransferQR.this,"Lưu thất bại");
                        }else if(result == -2){
                            dialog.showDialog(List_TransferQR.this,"Số lượng không đủ trong tồn kho");

                        }else if(result == -3){
                            dialog.showDialog(List_TransferQR.this,"Vị trí từ không hợp lệ");

                        }else if(result == -4){
                            dialog.showDialog(List_TransferQR.this,"Trạng thái của phiếu không hợp lệ");

                        }else if(result == -5){
                            dialog.showDialog(List_TransferQR.this,"Vị trí từ trùng vị trí đên");

                        }else if(result == -6){
                            dialog.showDialog(List_TransferQR.this,"Vị trí đến không hợp lệ");

                        }else if(result == -7){
                            dialog.showDialog(List_TransferQR.this,"Cập nhật trạng thái thất bại");

                        }else if(result == -8){
                            dialog.showDialog(List_TransferQR.this,"Sản phẩm không có thông tin trên phiếu ");

                        } else if (result == -13) {
                            dialog.showDialog(List_TransferQR.this,"Dữ liệu không hợp lệ");

                        }else if (result == -24) {
                            dialog.showDialog(List_TransferQR.this, "Vui Lòng Kiểm Tra Lại Số Lượng");

                        }else if (result == -26) {
                            dialog.showDialog(List_TransferQR.this, "Số Lượng Vượt Quá Yêu Cầu Trên SO");

                        }else if (result == -31) {
                            dialog.showDialog(List_TransferQR.this, "LPN Này Đã Được sử Dụng Cho SO Khác");

                        }else {
                            dialog.showDialog(List_TransferQR.this, "Lưu thất bại");
                        }

                    }
                }catch (Exception e){
                    Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
                    finish();
                }

//            }
        }else{
            dialog.showDialog(List_TransferQR.this,"Không có sản phẩm");

        }


    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(List_TransferQR.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(List_TransferQR.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                DatabaseHelper.getInstance().deleteProduct_TransferQR();
                Stockout.clear();
                QRListAdapter.notifyDataSetChanged();
                Intent intentToHomeQRActivity = new Intent(List_TransferQR.this, Home_Stockout.class);
                startActivity(intentToHomeQRActivity);
                finish();
            }
        });
        dialog.show();
    }

    private void actionBack() {
        try {
            List_TransferQR.this.finish();
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




    public void alert_show_SP(int isLPN){
        try {
            int postitionDes = new CmnFns().synchronizeGETProductByZoneStockout(List_TransferQR.this, value1, CmnFns.readDataAdmin(), expDate, ea_unit, stockinDate,global.getStockoutCD(),isLPN);

            Dialog dialog = new Dialog(List_TransferQR.this);


            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(List_TransferQR.this, "Vui Lòng Thử Lại");

            } else if (postitionDes == -8) {
                dialog.showDialog(List_TransferQR.this, "Mã sản phẩm không có trên phiếu");


            }else if (postitionDes == -10) {
                dialog.showDialog(List_TransferQR.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes == -11) {

                dialog.showDialog(List_TransferQR.this, "Mã sản phẩm không có trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(List_TransferQR.this, "Mã LPN không có trong kho");

            }else if (postitionDes == -16) {

                dialog.showDialog(List_TransferQR.this, "Sản phẩm đã quét không nằm trong LPN nào");

            } else if (postitionDes == -20) {

                dialog.showDialog(List_TransferQR.this, "Mã sản phẩm không có trong hệ thống");

            }else if (postitionDes == -21) {

                dialog.showDialog(List_TransferQR.this, "Mã sản phẩm không có trong zone");

            }else if (postitionDes == -22) {

                dialog.showDialog(List_TransferQR.this, "Mã LPN không có trong zone");

            }else if (postitionDes == -31) {
                dialog.showDialog(List_TransferQR.this, "LPN Này Đã Được sử Dụng ");

            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}
