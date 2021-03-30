package com.FiveSGroup.TMS;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.FiveSGroup.TMS.Inventory.InventoryListProduct;
import com.FiveSGroup.TMS.LetDown.LetDownActivity;
import com.FiveSGroup.TMS.LoadPallet.LoadPalletActivity;
import com.FiveSGroup.TMS.PickList.ListPickList;
import com.FiveSGroup.TMS.RemoveFromLPN.List_Remove_LPN;
import com.FiveSGroup.TMS.ReturnWareHouse.List_Return_WareHouse;
import com.FiveSGroup.TMS.StockOut.ListQrcode_Stockout;
import com.FiveSGroup.TMS.StockTransfer.ListStockTransfer;
import com.FiveSGroup.TMS.Warehouse.ListQrcode;
import com.FiveSGroup.TMS.Warehouse_Adjustment.ListQrcode_Warehouse_Adjustment;

import java.util.ArrayList;
import java.util.Calendar;

public class SelectPropertiesProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView tvProductSelectName;
    private EditText edtSelectProductExpiredDate, edtSelectProductStockinDate;
    private Spinner spinnerProductUnit;
    private DatePickerDialog pickerDialog;
    private ArrayList<String> units;
    private ArrayAdapter<String> adapter;
    private Button btnBack, btnConfirm;
    private String barcode = "",
            returnposition = "",
            returnCD = "",
            returnStock = "",
            selectedUnit = "",
            typeScan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_properties_product);

        getDataFromWareHouseScanCode();
        init();
        getProductName();
    }

    private void getProductName() {
        switch (typeScan) {
            case "scan_from_stock_in":
                setProductName(typeScan);
                break;
            case "scan_from_inventory":
                setProductName(typeScan);
                break;
            case "scan_from_letdown":
                setProductName(typeScan);
                break;
            case "scan_from_load_pallet":
                setProductName(typeScan);
                break;
            case "scan_from_pick_list":
                setProductName(typeScan);
                break;
            case "scan_from_put_away":
                setProductName(typeScan);
                break;
            case "scan_from_remove_lpn":
                setProductName(typeScan);
                break;
            case "scan_from_return_warehouse":
                setProductName(typeScan);
                break;
            case "scan_from_stock_out":
                setProductName(typeScan);
                break;
            case "scan_from_stock_transfer":
                setProductName(typeScan);
                break;
            case "scan_from_warehouse_adjustment":
                setProductName(typeScan);
                break;
        }

    }

    private void setProductName(String typeScan) {
        String data = new CmnFns().getProductName(typeScan, barcode, returnCD);
        if (isNumeric(data)) {
            tvProductSelectName.setText("SẢN PHẨM KHÔNG HỢP LỆ, VUI LÒNG THỰC HIỆN QUÉT LẠI SẢN PHẨM");
        } else {
            tvProductSelectName.setText(data);
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void getDataFromWareHouseScanCode() {
        Intent intent = getIntent();
        typeScan = intent.getStringExtra("typeScan");
        barcode = intent.getStringExtra("btn1");
        returnposition = intent.getStringExtra("returnposition");
        returnCD = global.getStockReceiptCd();
        returnStock = intent.getStringExtra("returnStock");
    }

    private void init() {
        tvProductSelectName = findViewById(R.id.tvSelectProductName);
        edtSelectProductExpiredDate = findViewById(R.id.edtSelectProductExpiredDate);
        edtSelectProductStockinDate = findViewById(R.id.edtSelectProductStockinDate);
        spinnerProductUnit = findViewById(R.id.spinnerProductUnit);
        edtSelectProductExpiredDate.setOnClickListener(this);
        edtSelectProductStockinDate.setOnClickListener(this);
        edtSelectProductStockinDate.setInputType(InputType.TYPE_NULL);
        edtSelectProductExpiredDate.setInputType(InputType.TYPE_NULL);
        units = new ArrayList<>();
        units = new CmnFns().getEa_Unit(barcode, "2");
        if (units == null) {
            units = new ArrayList<>();
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerProductUnit.setAdapter(adapter);
        spinnerProductUnit.setOnItemSelectedListener(this);
        btnBack = findViewById(R.id.btnBackSelectProductProperties);
        btnConfirm = findViewById(R.id.btnConfirmProductProperties);
        btnBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edtSelectProductExpiredDate:
                selectDate(R.id.edtSelectProductExpiredDate);
                break;
            case R.id.edtSelectProductStockinDate:
                selectDate(R.id.edtSelectProductStockinDate);
                break;
            case R.id.btnBackSelectProductProperties:
                finish();
                break;
            case R.id.btnConfirmProductProperties:
                savePropertiesToProduct();
                break;
        }
    }

    private void savePropertiesToProduct() {
        switch (typeScan) {
            case "scan_from_stock_in":
                createProduct(ListQrcode.class, "stock_in");
                break;
            case "scan_from_inventory":
                createProduct(InventoryListProduct.class, "inventory");
                break;
            case "scan_from_letdown":
                createProduct(LetDownActivity.class, "let_down");
                break;
            case "scan_from_load_pallet":
                createProduct(LoadPalletActivity.class, "load_pallet");
                break;
            case "scan_from_pick_list":
                createProduct(ListPickList.class, "pick_list");
                break;
            case "scan_from_put_away":
                createProduct(ListQrcode.class, "put_away");
                break;
            case "scan_from_remove_lpn":
                createProduct(List_Remove_LPN.class, "remove_lpn");
                break;
            case "scan_from_return_warehouse":
                createProduct(List_Return_WareHouse.class, "return_warehouse");
                break;
            case "scan_from_stock_out":
                createProduct(ListQrcode_Stockout.class, "stock_out");
                break;
            case "scan_from_stock_transfer":
                createProduct(ListStockTransfer.class, "stock_transfer");
                break;
            case "scan_from_warehouse_adjustment":
                createProduct(ListQrcode_Warehouse_Adjustment.class, "warehouse_adjustment");
                break;
        }

    }

    private void createProduct(Class activity, String type) {
        if (edtSelectProductExpiredDate.getText().toString().trim().isEmpty() ||
                edtSelectProductStockinDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn hạn sử dụng và ngày nhập", Toast.LENGTH_SHORT).show();
        } else {
            String expiredDate = edtSelectProductExpiredDate.getText().toString().trim();
            String stockinDate = edtSelectProductStockinDate.getText().toString().trim();
            String unit = spinnerProductUnit.getSelectedItem().toString();

            Intent intent = new Intent(SelectPropertiesProductActivity.this, activity);
            intent.putExtra("btn1", barcode);
            intent.putExtra("returnposition", returnposition);
            intent.putExtra("returnCD", returnCD);
            intent.putExtra("returnStock", returnStock);
            intent.putExtra("exp_date", expiredDate);
            intent.putExtra("stockin_date", stockinDate);
            intent.putExtra("ea_unit", unit);
            intent.putExtra(type, type);
            startActivity(intent);
            finish();
        }
    }

    private void selectDate(final int viewId) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        pickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String monthFormat = "";
                String dayFormat = "";
                if (monthOfYear + 1 > 9) {
                    monthFormat = String.valueOf(monthOfYear + 1);
                } else {
                    monthFormat = "0" + (monthOfYear + 1);
                }
                if (dayOfMonth > 9) {
                    dayFormat = String.valueOf(dayOfMonth);
                } else {
                    dayFormat = "0" + dayOfMonth;
                }

                if (viewId == R.id.edtSelectProductExpiredDate) {
                    edtSelectProductExpiredDate.setText(dayFormat + "/" + monthFormat + "/" + year);
                } else {
                    edtSelectProductStockinDate.setText(dayFormat + "/" + monthFormat + "/" + year);
                }

            }
        }, year, month, day);
        pickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) view).setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}