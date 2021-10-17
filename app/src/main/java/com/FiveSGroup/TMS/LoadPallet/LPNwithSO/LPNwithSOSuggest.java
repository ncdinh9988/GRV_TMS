package com.FiveSGroup.TMS.LoadPallet.LPNwithSO;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.LPN.LPN;
import com.FiveSGroup.TMS.LoadPallet.LoadPalletQRCode;
import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class LPNwithSOSuggest extends AppCompatActivity implements View.OnClickListener {

    Button buttonBack , buttonPutToPallet ;
    private RecyclerView rvListLPN;
    private ProgressDialog progressSyncProgram;
    ArrayList<ProductLpnWithSo> arrListLPN;
    private LPNwithSOSuggestAdapter adapter;
    String  press ="" ,master_cd = "", lpn_code = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lpnwithsosuggest);
        init();
        getDataFromIntent();
        SharedPreferences prefs = this.getSharedPreferences("masterpick", Activity.MODE_PRIVATE);
        master_cd = prefs.getString("masterpick_cd", "");
        updateBarcode();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        lpn_code = intent.getStringExtra("lpn_code");
    }
    public void updateBarcode(){
        LPNwithSOSuggest.Syncbarcodedata syncbarcodedata = new LPNwithSOSuggest.Syncbarcodedata();
        syncbarcodedata.execute();
    }
    class Syncbarcodedata extends AsyncTask<Void, Integer, Integer> {

        public boolean isSyncSuccess = false;

        @Override
        protected void onPreExecute() {
            CmnFns cmnFns = new CmnFns();
            int status = cmnFns.allowSynchronizeBy3G();
            if (status == 1) {
                progressSyncProgram = new ProgressDialog(LPNwithSOSuggest.this);
                progressSyncProgram.setMessage("Đang tải thông tin...");
                progressSyncProgram.setCancelable(false);
                progressSyncProgram.setCanceledOnTouchOutside(false);
                progressSyncProgram.show();


            } else if (status == 102) {
                Toast.makeText(LPNwithSOSuggest.this, "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                //progressSyncProgram.dismiss();

            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            // thực thi hàm lấy thông tin LPN
            DatabaseHelper.getInstance().deleteallProduct_LPN_SO();
            int status = new CmnFns().GET_SuggetPosition_MasterPick(master_cd);

            return status;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == -1) {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNwithSOSuggest.this, "Đồng bộ dữ liệu không thành công", Toast.LENGTH_SHORT).show();
                this.cancel(true);
                isSyncSuccess = false;

            } else {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNwithSOSuggest.this, "Đồng bộ dữ liệu thành công", Toast.LENGTH_SHORT).show();
                this.cancel(true);

//                SetDataSpinner();
                arrListLPN = DatabaseHelper.getInstance().getAllLPNSO(press);
                adapter = new LPNwithSOSuggestAdapter(LPNwithSOSuggest.this, arrListLPN);
                LinearLayoutManager layoutManager = new LinearLayoutManager(LPNwithSOSuggest.this, RecyclerView.VERTICAL, false);
                rvListLPN.setLayoutManager(layoutManager);
                rvListLPN.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                isSyncSuccess = true;
            }
        }
    }
    private void init(){
        buttonBack = findViewById(R.id.buttonBack);
        buttonPutToPallet = findViewById(R.id.buttonPutToPallet);
        buttonBack.setOnClickListener(this);
        buttonPutToPallet.setOnClickListener(this);
        rvListLPN = findViewById(R.id.rvListLPn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonBack:
                finish();
                break;
            case R.id.buttonPutToPallet:
                Intent intent = new Intent(LPNwithSOSuggest.this, LoadPalletQRCode.class);
                SharedPreferences sharedPreferences = getSharedPreferences("lpn_code", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("lpn_code", lpn_code);
                editor.apply();
                startActivity(intent);
                break;
        }


    }
}
