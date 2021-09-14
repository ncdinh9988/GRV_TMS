package com.FiveSGroup.TMS.LoadPallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.FiveSGroup.TMS.LetDown.LetDownActivity;
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ShowDialog.Dialog;

import java.util.ArrayList;
import java.util.List;

public class LoadPalletActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok;
    ImageButton btnscan_barcode;
    LoadPalletAdapter loadPalletAdapter;
    RecyclerView listViewProduct;
    String value1 = "", positonReceive = "", productCd = "", stock = "";
    String expDate = "";
    String expDate1 = "";
    String load_pallet = "";
    String pro_code = "";
    String pro_name = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "";
    String batch_number = "";
    String lpn = "";
    String key = "";
    String unique_id = "" ;

    ArrayList<Product_LoadPallet> loadPallets;
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
        unique_id = intent.getStringExtra("unique_id");
        productCd = intent.getStringExtra("returnCD");
        stock = intent.getStringExtra("returnStock");
        key = intent.getStringExtra("key");
        expDate = intent.getStringExtra("exp_date");
        expDate1 = intent.getStringExtra("expdate");
        pro_code = intent.getStringExtra("pro_code");
        pro_name = intent.getStringExtra("pro_name");
        load_pallet = intent.getStringExtra("load_pallet");
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
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private boolean isNotScanFromOrTo() {
        boolean check = false;
        List<Product_LoadPallet> product = DatabaseHelper.getInstance().getAllProduct_LoadPallet();

        for (int i = 0; i < product.size(); i++) {
            Product_LoadPallet product_loadPallet = product.get(i);
            String value0 = "---";
            String valueFromCode = product_loadPallet.getPOSITION_FROM_CODE();
            String valueToCode = product_loadPallet.getPOSITION_TO_CODE();
            String lpn_from = product_loadPallet.getLPN_FROM();
            String lpn_to = product_loadPallet.getLPN_TO();

            if((valueFromCode.equals("") || valueFromCode.equals(value0)) && (lpn_from.equals(""))){
                check = true;
            }
            if((valueToCode.equals("") || valueToCode.equals(value0)) && (lpn_to.equals(""))){
                check = true;
            }
        }
        if (check == true) {
            return true;
        } else {
            return false;
        }
    }

    private void ShowSuccessMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(LoadPalletActivity.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(LoadPalletActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                DatabaseHelper.getInstance().deleteProduct_LoadPallet();
                loadPallets.clear();
                loadPalletAdapter.notifyDataSetChanged();
                finish();
            }
        });
        dialog.show();
    }


    @Override
    public void onBackPressed() {

    }

    private void startScan() {
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        if (load_pallet != null) {
            Intent intent = new Intent(LoadPalletActivity.this, LoadPalletQRCode.class);
            intent.putExtra("check_to_finish_at_list", "check");
            startActivity(intent);
            finish();
        }
    }
    private void actionSyn(){
        try {
            LayoutInflater factory = LayoutInflater.from(LoadPalletActivity.this);
            View layout_cus = factory.inflate(R.layout.layout_request, null);
            final AlertDialog dialog = new AlertDialog.Builder(LoadPalletActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                    synchronizeToServer();
                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

    }

    private void actionBack() {
        try {
            LayoutInflater factory = LayoutInflater.from(LoadPalletActivity.this);
            View layout_cus = factory.inflate(R.layout.layout_back_putaway, null);
            final AlertDialog dialog = new AlertDialog.Builder(LoadPalletActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                    DatabaseHelper.getInstance().deleteProduct_LoadPallet();
                    DatabaseHelper.getInstance().deleteallEa_Unit();
                    dialog.dismiss();
                    Intent intent = new Intent(LoadPalletActivity.this, MainWareHouseActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    private boolean isQuanityZero() {
        boolean check = false;
        List<Product_LoadPallet> product = DatabaseHelper.getInstance().getAllProduct_LoadPallet();
        for (int i = 0; i < product.size(); i++) {
            Product_LoadPallet product_loadPallet = product.get(i);
            String valueQty = product_loadPallet.getQTY();
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
        if (load_pallet != null) {

            Dialog dialog = new Dialog(LoadPalletActivity.this);
            if (loadPallets.size() > 0) {
                String saleCode = CmnFns.readDataAdmin();

                if (isNotScanFromOrTo()) {

                    dialog.showDialog(LoadPalletActivity.this, "Chưa có VT Từ hoặc VT Đến");

                } else if (isQuanityZero()) {
                    dialog.showDialog(LoadPalletActivity.this, "Số lượng SP không được bằng 0");

                } else {
                    try {
                        int result = new CmnFns().synchronizeData(saleCode, "WPP", "");

                        switch (result) {
                            case 1:
                                ShowSuccessMessage("Lưu Thành Công");
                                //Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                                break;
                            case -1:
                                ShowErrorMessage("Lưu thất bại");
                                break;
                            case -2:
                                ShowErrorMessage("Số lượng không đủ trong tồn kho");
                                break;
                            case -3:
                                ShowErrorMessage("Vị trí từ không hợp lệ");
                                break;
                            case -4:
                                ShowErrorMessage("Trạng thái phiếu không hợp lệ");
                                break;
                            case -5:
                                ShowErrorMessage("Vị trí từ trùng vị trí đến");
                                break;
                            case -6:
                                ShowErrorMessage("Vị trí đến không hợp lệ");
                                break;
                            case -7:
                                ShowErrorMessage("Cập nhật trạng thái của phiếu thất bại");
                                break;
                            case -8:
                                ShowErrorMessage("Sản phẩm không có thông tin trên phiếu");
                                break;
                            case -13:
                                ShowErrorMessage("Dữ liệu không hợp lệ");
                                break;
                            case -24:
                                ShowErrorMessage("Vui Lòng Kiểm Tra Lại Số Lượng");
                                break;
                            case -26:
                                ShowErrorMessage("Số Lượng Vượt Quá Yêu Cầu Trên SO");
                                break;

                            case -36:
                                ShowErrorMessage("Trùng Dữ Liệu Vui Lòng Kiểm Tra Lại");
                                break;
                            default:
                                if (result >= 1) {
                                    Toast.makeText(getApplication(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                                    DatabaseHelper.getInstance().deleteProduct_LoadPallet();
                                    loadPallets.clear();
                                    loadPalletAdapter.notifyDataSetChanged();
                                    finish();
                                } else {
                                    ShowErrorMessage("Lưu thất bại");
                                }
                        }

                    }catch (Exception e){
                        Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            } else {
                dialog.showDialog(LoadPalletActivity.this, "Không có sản phẩm");

            }

        }
    }

    private void ShowErrorMessage(String message) {
        LayoutInflater factory = LayoutInflater.from(LoadPalletActivity.this);
        View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
        final AlertDialog dialog = new AlertDialog.Builder(LoadPalletActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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

    private void prepareData() {
        if (positonReceive == null) {
            if (key == null || key.equals("")) {
                if (lpn != null && value1 != null) {
                    alert_show_SP(1);
                } else if (lpn == null && value1 != null) {
                    alert_show_SP(0);

                }
            } else {
                Dialog dialog = new Dialog(LoadPalletActivity.this);
                dialog.showDialog(LoadPalletActivity.this, "Mã Sản Phẩm Không Có Trong Kho");
            }


        } else {
            if (lpn != null && value1 != null) {
                alert_show_position(1);
            } else if (lpn == null && value1 != null) {
                alert_show_position(0);
            }
        }
        loadPallets = DatabaseHelper.getInstance().getAllProduct_LoadPallet();
        //putAwayListAdapter = new PutAwayListAdapter(putaway, this);
        loadPalletAdapter = new LoadPalletAdapter(loadPallets, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        listViewProduct.setLayoutManager(layoutManager);
        listViewProduct.setAdapter(loadPalletAdapter);
        loadPalletAdapter.notifyDataSetChanged();
        load_pallet = "";
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(LoadPalletActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                LayoutInflater factory = LayoutInflater.from(LoadPalletActivity.this);
                View layout_cus = factory.inflate(R.layout.layout_delete, null);
                final AlertDialog dialog = new AlertDialog.Builder(LoadPalletActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                        Intent i = new Intent(LoadPalletActivity.this,LoadPalletActivity.class);
                        startActivity(i);

                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove swiped item from list and notify the RecyclerView
                        dialog.dismiss();

                        int position = viewHolder.getAdapterPosition();
                        Product_LoadPallet product = loadPallets.get(position);
                        loadPallets.remove(position);
                        DatabaseHelper.getInstance().deleteProduct_LoadPallet_Specific(product.getAUTOINCREMENT());
                        loadPalletAdapter.notifyItemRemoved(position);
                    }
                });
                dialog.show();



            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listViewProduct);


    }

    private void init() {
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setText("Trở Về");
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Danh Sách SP Chất Hàng Lên Pallet");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan_Barcode:
                startScan();
                break;
            case R.id.buttonBack:
                actionBack();
                break;
            case R.id.buttonOK:
                actionSyn();
                loadPallets = DatabaseHelper.getInstance().getAllProduct_LoadPallet();
                //putAwayListAdapter = new PutAwayListAdapter(putaway, this);
                loadPalletAdapter = new LoadPalletAdapter(loadPallets, this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                listViewProduct.setLayoutManager(layoutManager);
                listViewProduct.setAdapter(loadPalletAdapter);
                loadPalletAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void alert_show_position(int isLPN) {
        String positionTo = "";
        String positionFrom = "";
        ArrayList<Product_LoadPallet> loadPallets = new ArrayList<>();
        loadPallets = DatabaseHelper.getInstance().getAllProduct_LoadPallet_Sync();
        for (int i = 0; i < loadPallets.size(); i++) {
            Product_LoadPallet loadPallet = loadPallets.get(i);
            if (productCd.equals(loadPallet.getPRODUCT_CD()) &&
                    expDate1.equals(loadPallet.getEXPIRED_DATE()) &&
                    stockinDate.equals(loadPallet.getSTOCKIN_DATE()) &&
                    ea_unit_position.equals(loadPallet.getUNIT())) {

                if (!loadPallet.getLPN_FROM().equals("") || !loadPallet.getLPN_TO().equals("")) {
                    positionTo = loadPallet.getLPN_TO();
                    positionFrom = loadPallet.getLPN_FROM();
                }
                // không được để else if - vì quét VTT k phải là mã LPN
                if (!loadPallet.getPOSITION_FROM_CODE().equals("") || !loadPallet.getPOSITION_TO_CODE().equals("")) {
                    positionTo = loadPallet.getPOSITION_TO_CODE();
                    positionFrom = loadPallet.getPOSITION_FROM_CODE();
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
            String postitionDes = new CmnFns().synchronizeGETPositionInfoo(unique_id ,CmnFns.readDataAdmin(), value1, positonReceive, productCd, expDate1, ea_unit_position, stockinDate, positionFrom, positionTo, "WPP", isLPN);

            Dialog dialog = new Dialog(LoadPalletActivity.this);

            if (postitionDes.equals("1") || postitionDes.equals("-1")) {
                dialog.showDialog(LoadPalletActivity.this, "Vui Lòng Thử Lại");

            } else if (postitionDes.equals("-3")) {
                dialog.showDialog(LoadPalletActivity.this, "Vị trí từ không hợp lệ");

            } else if (postitionDes.equals("-6")) {
                dialog.showDialog(LoadPalletActivity.this, "Vị trí đến không hợp lệ");

            } else if (postitionDes.equals("-5")) {
                dialog.showDialog(LoadPalletActivity.this, "Vị trí từ trùng vị trí đến");

            } else if (postitionDes.equals("-14")) {
                dialog.showDialog(LoadPalletActivity.this, "Vị trí đến trùng vị trí từ");

            } else if (postitionDes.equals("-15")) {
                dialog.showDialog(LoadPalletActivity.this, "Vị trí từ không có trong hệ thống");

            } else if (postitionDes.equals("-10")) {
                dialog.showDialog(LoadPalletActivity.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes.equals("-17")) {
                dialog.showDialog(LoadPalletActivity.this, "LPN từ trùng LPN đến");

            } else if (postitionDes.equals("-18")) {
                dialog.showDialog(LoadPalletActivity.this, "LPN đến trùng LPN từ");

            } else if (postitionDes.equals("-19")) {
                dialog.showDialog(LoadPalletActivity.this, "Vị trí đến không có trong hệ thống");

            } else if (postitionDes.equals("-12")) {
                dialog.showDialog(LoadPalletActivity.this, "Mã LPN không có trong tồn kho");

            }else if (postitionDes.equals("-27")) {
                dialog.showDialog(LoadPalletActivity.this, "Vị trí từ chưa có sản phẩm");

            }else if (postitionDes.equals("-28")) {
                dialog.showDialog(LoadPalletActivity.this, "LPN đến có vị trí không hợp lệ");

            } else {
                return;
            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    public void alert_show_SP(int isLPN) {
        try {
            DatabaseHelper.getInstance().deleteallProduct_S_P();
            int postitionDes = new CmnFns().synchronizeGETProductByZoneLoadPallet(LoadPalletActivity.this, value1,
                    CmnFns.readDataAdmin(), expDate, ea_unit, stockinDate, isLPN, pro_code,pro_name,batch_number);


            Dialog dialog = new Dialog(LoadPalletActivity.this);


            if (postitionDes == 1) {
                return;
            } else if (postitionDes == -1) {
                dialog.showDialog(LoadPalletActivity.this, "Vui Lòng Thử Lại");

            } else if (postitionDes == -8) {
                dialog.showDialog(LoadPalletActivity.this, "Mã sản phẩm không có trên phiếu");


            } else if (postitionDes == -10) {
                dialog.showDialog(LoadPalletActivity.this, "Mã LPN không có trong hệ thống");

            } else if (postitionDes == -11) {

                dialog.showDialog(LoadPalletActivity.this, "Mã sản phẩm không có trong kho");


            } else if (postitionDes == -12) {

                dialog.showDialog(LoadPalletActivity.this, "Mã LPN không có trong kho");

            } else if (postitionDes == -16) {

                dialog.showDialog(LoadPalletActivity.this, "Sản phẩm đã quét không nằm trong LPN nào");

            } else if (postitionDes == -20) {

                dialog.showDialog(LoadPalletActivity.this, "Mã sản phẩm không có trong hệ thống");

            }
        }catch (Exception e){
            Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
