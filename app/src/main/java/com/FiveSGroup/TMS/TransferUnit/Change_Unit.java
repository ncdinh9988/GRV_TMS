package com.FiveSGroup.TMS.TransferUnit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.global;

import java.util.ArrayList;
import java.util.Date;

public class Change_Unit extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private TextView tvProductSelectName;
    private EditText edtSelectProductExpiredDate, edtSelectProductStockinDate, edtSelectShelfLife, edtSelectShelfLifeDate;
    private Spinner spinnerProductUnit;
    private DatePickerDialog pickerDialog;
    private ArrayList<String> units;
    private ArrayAdapter<String> adapter;
    private Button btnBack, btnConfirm;
    private Date date, date2, date_exp, date_current, newDate_curent;
    private Integer shelfLife1, shelfLife2;
    private CheckBox checkBoxDate, checkBoxMonth;
    private String barcode = "",
            returnposition = "",
            returnCD = "",
            returnStock = "",
            selectedUnit = "",
            currentDateandTime = "",
            stockinDate = "",
            expiredDate = "",
            shelfLife = "",
            shelfLifeDate = "",
            typeScan = "",
            stockin = "",
            id_unique_TU = "",
            exp_date = "";
    String total_shelf_life = "";
    String shelf_life_type = "";
    String min_rem_shelf_life = "";
    int min_to_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        init();

    }
    private void init() {
        Intent intent = getIntent();
        // value1 : giá trị barcode truyền từ LetDownQrCodeActivity
        id_unique_TU = intent.getStringExtra("id_unique_TU");
        barcode = intent.getStringExtra("barcode");


        spinnerProductUnit = findViewById(R.id.spinnerProductUnit);


        //set unit in adapter
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


    private void IntentActivity( final String unit) {
        Intent intent = new Intent(this, TransferUnitActivity.class);
        intent.putExtra("transferunit", unit);
        intent.putExtra("id_unique_TU", id_unique_TU);
        startActivity(intent);
        finish();
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) view).setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackSelectProductProperties:
                Intent intent = new Intent(this,TransferUnitActivity.class);
                startActivity(intent);
//                finish();
                break;
            case R.id.btnConfirmProductProperties:
                savePropertiesToProduct();
                break;

        }

    }

    private void savePropertiesToProduct() {
        final String unit = spinnerProductUnit.getSelectedItem().toString();
        IntentActivity(unit);
    }
}
