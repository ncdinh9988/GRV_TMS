package com.FiveSGroup.TMS.QA.Pickup;

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

public class List_Pickup extends AppCompatActivity implements View.OnClickListener {
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
    String pickup = "";
    String pro_code = "";
    String pro_name = "";
    String pro_cd = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String key = "";
    String stockinDate = "";
    String batch_number = "";
    String lpn = "", id_unique_SO = "";

    int statusGetCust;
    Product_Pickup product_qrcode;

    ArrayList<Product_Pickup> product_pickup;
    CheckEventbus eventbus;

    Pickup_Adapter PickupListAdapter;
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
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
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

        stock = intent.getStringExtra("returnStock");
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        pickup = intent.getStringExtra("pickup");
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
                if (lpn != null && pickup != null) {
                    //TODO
                    alert_show_SP(1);
                } else if (lpn == null && pickup != null) {
                    //TODO
                    alert_show_SP(0);
                }
            } else {
                Dialog dialog = new Dialog(List_Pickup.this);
                dialog.showDialog(List_Pickup.this, "Mã Sản Phẩm Không Có Trong Phiếu");
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

        product_pickup = DatabaseHelper.getInstance().getAllProduct_Pickup(global.getPickupCD());
        PickupListAdapter = new Pickup_Adapter(this, product_pickup);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listVieWTPoduct.setLayoutManager(layoutManager);
        listVieWTPoduct.setAdapter(PickupListAdapter);
        PickupListAdapter.notifyDataSetChanged();
        pickup = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(List_Pickup.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(List_Pickup.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(List_Pickup.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(List_Pickup.this, List_Pickup.class);
                        startActivity(i);

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();

                        int position = viewHolder.getAdapterPosition();
                        Product_Pickup product = product_pickup.get(position);
                        product_pickup.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_Pickup_Specific(product.getAUTOINCREMENT());
                        PickupListAdapter.notifyItemRemoved(position);
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
        List<Product_Pickup> product = DatabaseHelper.getInstance().getAllProduct_Pickup(global.getPickupCD());

        for (int i = 0; i < product.size(); i++) {
            Product_Pickup cancelGood = product.get(i);
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
        tvTitle.setText("Danh Sách SP Lấy Hàng");
    }

    private void startScan() {

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        Intent intent = new Intent(List_Pickup.this, Qrcode_Pickup.class);
        intent.putExtra("check_to_finish_at_list", "check");
        startActivity(intent);
        finish();

    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<Product_Pickup> product = DatabaseHelper.getInstance().getAllProduct_Pickup(global.getPickupCD());
        for (int i = 0; i < product.size(); i++) {
            Product_Pickup putAway = product.get(i);
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
        Dialog dialog = new Dialog(List_Pickup.this);


        if (product_pickup.size() > 0) {
            if (isNotScanFromOrTo()) {
                dialog.showDialog(List_Pickup.this, "Chưa Có VT Từ Hoặc VT Đến");

            } else if (isQuanityZero()) {
                dialog.showDialog(List_Pickup.this, "Số lượng SP không được bằng 0");

            } else {
                try {
                    int result = new CmnFns().synchronizeData(saleCode, "WQA", global.getPickupCD());
                    if (result >= 1) {
                        ShowSuccessMessage("Lưu thành công");
//                    Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();

                    } else {

                        if (result == -1) {
                            dialog.showDialog(List_Pickup.this, "Lưu thất bại");
                        } else if (result == -2) {
                            dialog.showDialog(List_Pickup.this, "Số lượng không đủ trong tồn kho");

                        } else if (result == -3) {
                            dialog.showDialog(List_Pickup.this, "Vị trí từ không hợp lệ");

                        } else if (result == -4) {
                            dialog.showDialog(List_Pickup.this, "Trạng thái của phiếu không hợp lệ");

                        } else if (result == -5) {
                            dialog.showDialog(List_Pickup.this, "Vị trí từ trùng vị trí đên");

                        } else if (result == -6) {
                            dialog.showDialog(List_Pickup.this, "Vị trí đến không hợp lệ");

                        } else if (result == -7) {
                            dialog.showDialog(List_Pickup.this, "Cập nhật trạng thái thất bại");

                        } else if (result == -8) {
                            dialog.showDialog(List_Pickup.this, "Sản phẩm không có thông tin trên phiếu ");

                        } else if (result == -13) {
                            dialog.showDialog(List_Pickup.this, "Dữ liệu không hợp lệ");

                        } else if (result == -24) {
                            dialog.showDialog(List_Pickup.this, "Vui Lòng Kiểm Tra Lại Số Lượng");

                        } else if (result == -26) {
                            dialog.showDialog(List_Pickup.this, "Số Lượng Vượt Quá Yêu Cầu Trên SO");

                        } else {
                            dialog.showDialog(List_Pickup.this, "Lưu thất bại");
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Vui Lòng Thử Lại ...", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        } else {
            dialog.showDialog(List_Pickup.this, "Không có sản phẩm");

        }


    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(List_Pickup.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(List_Pickup.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                DatabaseHelper.getInstance().deleteProduct_Pickup(global.getPickupCD());
                product_pickup.clear();
                PickupListAdapter.notifyDataSetChanged();
                Intent intentToHomeQRActivity = new Intent(List_Pickup.this, Home_Pickup.class);
                startActivity(intentToHomeQRActivity);
                finish();
            }
        });
        dialog.show();
    }

    private void actionBack() {
        try {
            List_Pickup.this.finish();
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
        ArrayList<Product_Pickup> cancelGoods = new ArrayList<>();
        cancelGoods = DatabaseHelper.getInstance().getAllProduct_Pickup_Sync(global.getPickupCD());
        for (int i = 0; i < cancelGoods.size(); i++) {
            Product_Pickup cancelGood = cancelGoods.get(i);
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
                // if này là để trả lại giá trị from và to nếu người dùng muốn quét lại VTT và VTĐ
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
                    productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WQA", isLPN);
            ArrayList<Product_Pickup> listpickup = new ArrayList<>();
            listpickup = DatabaseHelper.getInstance().getonePosition_Pickup(id_unique_SO);
            String position_from_cd = listpickup.get(0).getPOSITION_FROM_CD();
            String position_to_cd = listpickup.get(0).getPOSITION_TO_CD();

            if((!position_from_cd.equals(""))&&(!position_to_cd.equals(""))){
                String check_position = new CmnFns().Check_Position_Same_SLOC(position_from_cd,position_to_cd,"WQA");

                if(check_position.equals("Thành Công")){
                    Dialog dialog = new Dialog(List_Pickup.this);

                    if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                        dialog.showDialog(List_Pickup.this, "Vui Lòng Thử Lại");

                    } else if (postitionDes.equals("-3")) {
                        dialog.showDialog(List_Pickup.this, "Vị trí từ không hợp lệ");

                    } else if (postitionDes.equals("-6")) {
                        dialog.showDialog(List_Pickup.this, "Vị trí đến không hợp lệ");

                    } else if (postitionDes.equals("-5")) {
                        dialog.showDialog(List_Pickup.this, "Vị trí từ trùng vị trí đến");

                    } else if (postitionDes.equals("-14")) {
                        dialog.showDialog(List_Pickup.this, "Vị trí đến trùng vị trí từ");

                    } else if (postitionDes.equals("-15")) {
                        dialog.showDialog(List_Pickup.this, "Vị trí từ không có trong hệ thống");

                    } else if (postitionDes.equals("-10")) {
                        dialog.showDialog(List_Pickup.this, "Mã LPN không có trong hệ thống");

                    } else if (postitionDes.equals("-17")) {
                        dialog.showDialog(List_Pickup.this, "LPN từ trùng LPN đến");

                    } else if (postitionDes.equals("-18")) {
                        dialog.showDialog(List_Pickup.this, "LPN đến trùng LPN từ");

                    } else if (postitionDes.equals("-19")) {
                        dialog.showDialog(List_Pickup.this, "Vị trí đến không có trong hệ thống");

                    } else if (postitionDes.equals("-12")) {
                        dialog.showDialog(List_Pickup.this, "Mã LPN không có trong tồn kho");

                    } else if (postitionDes.equals("-27")) {
                        dialog.showDialog(List_Pickup.this, "Vị trí từ chưa có sản phẩm");

                    } else if (postitionDes.equals("-28")) {
                        dialog.showDialog(List_Pickup.this, "LPN đến có vị trí không hợp lệ");

                    } else {
                        return;
                    }
                }else{
                    Dialog dialog = new Dialog(List_Pickup.this);
                    dialog.showDialog(List_Pickup.this, check_position);
                }
            }else{
                Dialog dialog = new Dialog(List_Pickup.this);

                if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                    dialog.showDialog(List_Pickup.this, "Vui Lòng Thử Lại");

                } else if (postitionDes.equals("-3")) {
                    dialog.showDialog(List_Pickup.this, "Vị trí từ không hợp lệ");

                } else if (postitionDes.equals("-6")) {
                    dialog.showDialog(List_Pickup.this, "Vị trí đến không hợp lệ");

                } else if (postitionDes.equals("-5")) {
                    dialog.showDialog(List_Pickup.this, "Vị trí từ trùng vị trí đến");

                } else if (postitionDes.equals("-14")) {
                    dialog.showDialog(List_Pickup.this, "Vị trí đến trùng vị trí từ");

                } else if (postitionDes.equals("-15")) {
                    dialog.showDialog(List_Pickup.this, "Vị trí từ không có trong hệ thống");

                } else if (postitionDes.equals("-10")) {
                    dialog.showDialog(List_Pickup.this, "Mã LPN không có trong hệ thống");

                } else if (postitionDes.equals("-17")) {
                    dialog.showDialog(List_Pickup.this, "LPN từ trùng LPN đến");

                } else if (postitionDes.equals("-18")) {
                    dialog.showDialog(List_Pickup.this, "LPN đến trùng LPN từ");

                } else if (postitionDes.equals("-19")) {
                    dialog.showDialog(List_Pickup.this, "Vị trí đến không có trong hệ thống");

                } else if (postitionDes.equals("-12")) {
                    dialog.showDialog(List_Pickup.this, "Mã LPN không có trong tồn kho");

                } else if (postitionDes.equals("-27")) {
                    dialog.showDialog(List_Pickup.this, "Vị trí từ chưa có sản phẩm");

                } else if (postitionDes.equals("-28")) {
                    dialog.showDialog(List_Pickup.this, "LPN đến có vị trí không hợp lệ");

                } else {
                    return;
                }

            }

        } catch (Exception e) {
            Toast.makeText(this, "Vui Lòng Thử Lại ...", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    public void alert_show_SP(int isLPN) {
        try {
            int postitionDes = new CmnFns().synchronizeGETProductByZonePickup(List_Pickup.this, value1, CmnFns.readDataAdmin(),
                    expDate, ea_unit, stockinDate, global.getPickupCD(), isLPN ,batch_number,pro_code , pro_name, pro_cd);

            Dialog dialog = new Dialog(List_Pickup.this);

            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(List_Pickup.this, "Vui Lòng Thử Lại");

            } else if (postitionDes == -8) {
                dialog.showDialog(List_Pickup.this, "Mã sản phẩm không có trên phiếu");


            } else if (postitionDes == -10) {
                dialog.showDialog(List_Pickup.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes == -11) {

                dialog.showDialog(List_Pickup.this, "Mã sản phẩm không có trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(List_Pickup.this, "Mã LPN không có trong kho");

            } else if (postitionDes == -16) {

                dialog.showDialog(List_Pickup.this, "Sản phẩm đã quét không nằm trong LPN nào");

            } else if (postitionDes == -20) {

                dialog.showDialog(List_Pickup.this, "Mã sản phẩm không có trong hệ thống");

            } else if (postitionDes == -21) {

                dialog.showDialog(List_Pickup.this, "Mã sản phẩm không có trong zone");

            } else if (postitionDes == -22) {

                dialog.showDialog(List_Pickup.this, "Mã LPN không có trong zone");

            }
        } catch (Exception e) {
            Toast.makeText(this, "Vui Lòng Thử Lại ...", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}
