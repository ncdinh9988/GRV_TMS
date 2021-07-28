package com.FiveSGroup.TMS.QA;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitProduct;

import java.util.ArrayList;

public class List_QA extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack, btnok, buttonBackList;
    ImageButton btnscan_barcode;
    RecyclerView listViewProduct;
    Spinner idUnit;
    String value1 = "" , id_unique_TU = "";
    String positonReceive = "";
    String productCd = "";
    String stock = "";
    String expDate = "";
    String expDate1 = "";
    String transfer_unit = "" ,transferunit = "";
    String ea_unit = "";
    String ea_unit_position = "";
    String stockinDate = "" , id_unique_LD = "";
    TextView tvTitle , VTDen;
    String fromLetDownSuggestionsActivity = "";
    String lpn = "";
    int result;


    ArrayList<TransferUnitProduct> transferUnitarr;

    com.FiveSGroup.TMS.TransferUnit.TransferUnitAdapter TransferUnitAdapter;
    private Object TransferQRAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transfer);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getValueFromIntent();

        init();

    }
    private void prepareData() {



    }



    private void init() {
        btnscan_barcode = findViewById(R.id.buttonScan_Barcode);
        buttonBack = findViewById(R.id.buttonBack);

        buttonBackList = findViewById(R.id.buttonBackList);
        btnok = findViewById(R.id.buttonOK);
        listViewProduct = findViewById(R.id.LoadWebService);
        btnscan_barcode.setOnClickListener(this);

        buttonBackList.setVisibility(View.GONE);
        buttonBack.setOnClickListener(this);
        buttonBackList.setOnClickListener(this);

        btnok.setOnClickListener(this);
    }


    private void getValueFromIntent() {

    }


    @Override
    public void onClick(View v) {

    }
}
