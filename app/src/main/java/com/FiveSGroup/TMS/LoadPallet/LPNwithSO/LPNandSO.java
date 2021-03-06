package com.FiveSGroup.TMS.LoadPallet.LPNwithSO;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.LPN.ItemLPNAdapter;
import com.FiveSGroup.TMS.LPN.LPN;
import com.FiveSGroup.TMS.LoadPallet.LoadPalletQRCode;
import com.FiveSGroup.TMS.MasterPick.Home_Master_Pick;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.global;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LPNandSO extends AppCompatActivity implements View.OnClickListener {

    private Button buttonBack;
    private ProgressDialog progressSyncProgram;
    private TextView createDate , idbarcode , idwarehouse;
    String  press ="";
    private RecyclerView rvListLPN;
    private SwipeRefreshLayout swipeRefesh;
    private String chooseDate;
    ArrayList<LPN> arrListLPN;
    private LPNwithSOAdapter adapter;
    private Spinner spinner;
    String number = "100";
    String master_cd = "";
    String lpn_code = "";
    String order_code = "";
    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lpnwithso);
        init();
        SharedPreferences prefs = this.getSharedPreferences("masterpick", Activity.MODE_PRIVATE);
        master_cd = prefs.getString("masterpick_cd", "");

        LPNandSO.SyncProgram syncProgram = new LPNandSO.SyncProgram();
        syncProgram.execute();

        RefeshData();
    }



    private void init() {
        createDate = findViewById(R.id.priceproduct);
        idwarehouse = findViewById(R.id.idwarehouse);
        idbarcode = findViewById(R.id.idproduct);
//        spinner = findViewById(R.id.spinner);
        buttonBack = findViewById(R.id.buttonBack);
        rvListLPN = findViewById(R.id.rvListLPn);
        swipeRefesh = findViewById(R.id.swipeRefesh);
        arrListLPN = new ArrayList<>();
//        buttonPutToPallet.setEnabled(false);
        buttonBack.setOnClickListener(this);

        idbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(LPNandSO.this);
                View layout_cus = factory.inflate(R.layout.layout_find, null);
                final AlertDialog dialog = new AlertDialog.Builder(LPNandSO.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
                InsetDrawable inset = new InsetDrawable(back, 64);
                dialog.getWindow().setBackgroundDrawable(inset);
                dialog.setView(layout_cus);

                Button btnNo = layout_cus.findViewById(R.id.btnNo);
                Button btnYes = layout_cus.findViewById(R.id.btnYes);
                final EditText editText = layout_cus.findViewById(R.id.tvTextBack);

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
                        press = editText.getText().toString();
                        updateBarcode();
                    }
                });
                dialog.show();
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        createDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LPNandSO.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }
    public void updateBarcode(){
        LPNandSO.Syncbarcodedata syncbarcodedata = new LPNandSO.Syncbarcodedata();
        syncbarcodedata.execute();
    }


    public void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        chooseDate = sdf.format(myCalendar.getTime());
        Log.d("logggggggg", "" + chooseDate);

        createDate.setText(sdf.format(myCalendar.getTime()));

        LPNandSO.SyncDate syncDate = new LPNandSO.SyncDate();
        syncDate.execute();

    }

    class Syncbarcodedata extends AsyncTask<Void, Integer, Integer> {

        public boolean isSyncSuccess = false;

        @Override
        protected void onPreExecute() {
            CmnFns cmnFns = new CmnFns();
            int status = cmnFns.allowSynchronizeBy3G();
            if (status == 1) {
                progressSyncProgram = new ProgressDialog(LPNandSO.this);
                progressSyncProgram.setMessage("??ang t???i th??ng tin...");
                progressSyncProgram.setCancelable(false);
                progressSyncProgram.setCanceledOnTouchOutside(false);
                progressSyncProgram.show();


            } else if (status == 102) {
                Toast.makeText(LPNandSO.this, "Vui l??ng ki???m tra k???t n???i m???ng", Toast.LENGTH_LONG).show();
                //progressSyncProgram.dismiss();

            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            // th???c thi h??m l???y th??ng tin LPN
            DatabaseHelper.getInstance().deleteallProduct_LPN();
            int status = new CmnFns().synchronizeGetLPNwithSO(getApplication(),master_cd);
            return status;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == -1) {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNandSO.this, "?????ng b??? d??? li???u kh??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                this.cancel(true);
                isSyncSuccess = false;

            } else {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNandSO.this, "?????ng b??? d??? li???u th??nh c??ng", Toast.LENGTH_SHORT).show();
                this.cancel(true);

//                SetDataSpinner();
                arrListLPN = DatabaseHelper.getInstance().getAllLpnBarcodewithSO(press);
                adapter = new LPNwithSOAdapter(LPNandSO.this, arrListLPN);
                LinearLayoutManager layoutManager = new LinearLayoutManager(LPNandSO.this, RecyclerView.VERTICAL, false);
                rvListLPN.setLayoutManager(layoutManager);
                rvListLPN.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                isSyncSuccess = true;
            }
        }
    }

    class SyncDate extends AsyncTask<Void, Integer, Integer> {

        public boolean isSyncSuccess = false;

        @Override
        protected void onPreExecute() {
            CmnFns cmnFns = new CmnFns();
            int status = cmnFns.allowSynchronizeBy3G();
            if (status == 1) {
                progressSyncProgram = new ProgressDialog(LPNandSO.this);
                progressSyncProgram.setMessage("??ang t???i th??ng tin...");
                progressSyncProgram.setCancelable(false);
                progressSyncProgram.setCanceledOnTouchOutside(false);
                progressSyncProgram.show();


            } else if (status == 102) {
                Toast.makeText(LPNandSO.this, "Vui l??ng ki???m tra k???t n???i m???ng", Toast.LENGTH_LONG).show();
                //progressSyncProgram.dismiss();

            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            // th???c thi h??m l???y th??ng tin LPN
            DatabaseHelper.getInstance().deleteallProduct_LPN();
            int status = new CmnFns().synchronizeGetLPNwithSO(getApplication(),master_cd);

            return status;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == -1) {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNandSO.this, "?????ng b??? d??? li???u kh??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                this.cancel(true);
                isSyncSuccess = false;

            } else {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNandSO.this, "?????ng b??? d??? li???u th??nh c??ng", Toast.LENGTH_SHORT).show();
                this.cancel(true);

//                SetDataSpinner();
                arrListLPN = DatabaseHelper.getInstance().getAllLpn_date(chooseDate);
                adapter = new LPNwithSOAdapter(LPNandSO.this, arrListLPN);
                LinearLayoutManager layoutManager = new LinearLayoutManager(LPNandSO.this, RecyclerView.VERTICAL, false);
                rvListLPN.setLayoutManager(layoutManager);
                rvListLPN.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                isSyncSuccess = true;
            }
        }
    }



    private void RefeshData() {
        swipeRefesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrListLPN.clear();
                LPNandSO.SyncProgram syncProgram = new LPNandSO.SyncProgram();
                syncProgram.execute();
                swipeRefesh.setRefreshing(false);

            }
        });

    }


    //?????ng b??? param ch????ng tr??nh
    class SyncProgram extends AsyncTask<Void, Integer, Integer> {

        public boolean isSyncSuccess = false;

        @Override
        protected void onPreExecute() {
            CmnFns cmnFns = new CmnFns();
            int status = cmnFns.allowSynchronizeBy3G();
            if (status == 1) {
                progressSyncProgram = new ProgressDialog(LPNandSO.this);
                progressSyncProgram.setMessage("??ang t???i th??ng tin...");
                progressSyncProgram.setCancelable(false);
                progressSyncProgram.setCanceledOnTouchOutside(false);
                progressSyncProgram.show();


            } else if (status == 102) {
                Toast.makeText(LPNandSO.this, "Vui l??ng ki???m tra k???t n???i m???ng", Toast.LENGTH_LONG).show();
                // progressSyncProgram.dismiss();

            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            // th???c thi h??m l???y th??ng tin LPN
            DatabaseHelper.getInstance().deleteallProduct_LPN();
            int status = new CmnFns().synchronizeGetLPNwithSO(getApplication(),master_cd);

            return status;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == -1) {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNandSO.this, "?????ng b??? d??? li???u kh??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                this.cancel(true);
                isSyncSuccess = false;

            } else {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNandSO.this, "?????ng b??? d??? li???u th??nh c??ng", Toast.LENGTH_SHORT).show();
                this.cancel(true);

//                SetDataSpinner();
                arrListLPN = DatabaseHelper.getInstance().getAllLpn_limit();
                adapter = new LPNwithSOAdapter(LPNandSO.this, arrListLPN);
                LinearLayoutManager layoutManager = new LinearLayoutManager(LPNandSO.this, RecyclerView.VERTICAL, false);
                rvListLPN.setLayoutManager(layoutManager);
                rvListLPN.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                isSyncSuccess = true;
            }
        }
    }

    class Syncbarcode extends AsyncTask<Void, Integer, Integer> {

        public boolean isSyncSuccess = false;

        @Override
        protected void onPreExecute() {
            CmnFns cmnFns = new CmnFns();
            int status = cmnFns.allowSynchronizeBy3G();
            if (status == 1) {
                progressSyncProgram = new ProgressDialog(LPNandSO.this);
                progressSyncProgram.setMessage("??ang t???o m?? ...");
                progressSyncProgram.setCancelable(false);
                progressSyncProgram.setCanceledOnTouchOutside(false);
                progressSyncProgram.show();


            } else if (status == 102) {
                Toast.makeText(LPNandSO.this, "Vui l??ng ki???m tra k???t n???i m???ng", Toast.LENGTH_LONG).show();
                // progressSyncProgram.dismiss();

            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            // th???c thi h??m l???y th??ng tin LPN
            DatabaseHelper.getInstance().deleteallProduct_LPN();
            int status = new CmnFns().synchronizeGetLPN(getApplication());

            return status;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == -1) {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNandSO.this, "?????ng b??? d??? li???u kh??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                this.cancel(true);
                isSyncSuccess = false;

            } else {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNandSO.this, "?????ng b??? d??? li???u th??nh c??ng", Toast.LENGTH_SHORT).show();
                this.cancel(true);

//                SetDataSpinner();
                arrListLPN = DatabaseHelper.getInstance().getAllLpn();
                adapter = new LPNwithSOAdapter(LPNandSO.this, arrListLPN);
                LinearLayoutManager layoutManager = new LinearLayoutManager(LPNandSO.this, RecyclerView.VERTICAL, false);
                rvListLPN.setLayoutManager(layoutManager);
                rvListLPN.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                isSyncSuccess = true;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                Intent intenttt = new Intent(LPNandSO.this, Home_Master_Pick.class);
                startActivity(intenttt);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
