package com.FiveSGroup.TMS.LPN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.LoadPallet.LoadPalletQRCode;
import com.FiveSGroup.TMS.R;

import java.util.ArrayList;

public class LPNActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonBack, btnCreateLPN, buttonPutToPallet;
    private ProgressDialog progressSyncProgram;
    private RecyclerView rvListLPN;
    private SwipeRefreshLayout swipeRefesh;
    ArrayList<LPN> arrListLPN;
    private ItemLPNAdapter adapter;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l_p_n);

        init();

//        Handler handler = new Handler();
//        TaskSyncProgramCanceler taskCanceler;
        SyncProgram syncProgram = new SyncProgram();
//        taskCanceler = new TaskSyncProgramCanceler(syncProgram);
//        handler.postDelayed(taskCanceler, 1000 * 40);
        syncProgram.execute();

        RefeshData();
    }

    public class TaskSyncProgramCanceler implements Runnable {
        private SyncProgram task;

        public TaskSyncProgramCanceler(SyncProgram task) {
            this.task = task;
        }

        @Override
        public void run() {
            if ((task.getStatus() == AsyncTask.Status.FINISHED) && task.isSyncSuccess == false) {
                task.cancel(true);
                Toast.makeText(LPNActivity.this, "Đồng bộ dữ liệu không thành công", Toast.LENGTH_SHORT).show();
                progressSyncProgram.dismiss();
            }
        }
    }

    private void init() {
        buttonPutToPallet = findViewById(R.id.buttonPutToPallet);
        spinner = findViewById(R.id.spinner);
        buttonBack = findViewById(R.id.buttonBack);
        btnCreateLPN = findViewById(R.id.btnCreateLPN);
        rvListLPN = findViewById(R.id.rvListLPn);
        swipeRefesh = findViewById(R.id.swipeRefesh);
        arrListLPN = new ArrayList<>();
//        buttonPutToPallet.setEnabled(false);
        buttonBack.setOnClickListener(this);
        btnCreateLPN.setOnClickListener(this);
        buttonPutToPallet.setOnClickListener(this);


    }

    private void SetDataSpinner() {
        ArrayList<String> arrDateTemp = new ArrayList<>();
        ArrayList<String> arrDate = new ArrayList<>();

        ArrayList<LPN> arrayList = DatabaseHelper.getInstance().getAllLpn();

        arrDateTemp = DatabaseHelper.getInstance().getAllDatesLPN();

        // chỉ lấy 30 ngày gần đây
        if(arrDateTemp.size() < 30){
            arrDate = arrDateTemp;
        }else{
            for(int i = 0; i <= arrDateTemp.size(); i++){
                if(i < 30){
                    arrDate.add(arrDateTemp.get(i));
                }
            }
        }


        if (arrDate.size() > 0 ) {
            // gán giá trị vào spinner
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.layout_item_spinner, arrDate);

            arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            spinner.setSelection(0);

            spinner.setAdapter(arrayAdapter);

            // lầy giá trị được chọn của spinner
            String dateChoosed = spinner.getSelectedItem().toString();
            // add danh sách lpn vào mảng
            for (int i = 0; i < arrayList.size(); i++) {
                if (dateChoosed.equals(arrayList.get(i).getLPN_DATE())) {
                    arrListLPN.add(arrayList.get(i));
                }
            }
            adapter = new ItemLPNAdapter(LPNActivity.this, arrListLPN);
            LinearLayoutManager layoutManager = new LinearLayoutManager(LPNActivity.this, RecyclerView.VERTICAL, false);
            rvListLPN.setLayoutManager(layoutManager);
            rvListLPN.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            // hàm thay đổi lựa chọn cho sp
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<LPN> arrList = DatabaseHelper.getInstance().getAllLpn();

                    ((TextView) view).setTextColor(getResources().getColor(R.color.black));
                    ((TextView) view).setGravity(Gravity.CENTER);
                    spinner.setSelection(position);
                    if (arrListLPN.size() > 0) {
                        arrListLPN.clear();
                    }
                    String dateChoosed = spinner.getSelectedItem().toString();
                    for (int i = 0; i < arrList.size(); i++) {
                        if (dateChoosed.equals(arrList.get(i).getLPN_DATE())) {
                            arrListLPN.add(arrList.get(i));
                        }
                    }

                    adapter = new ItemLPNAdapter(LPNActivity.this, arrListLPN);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(LPNActivity.this, RecyclerView.VERTICAL, false);
                    rvListLPN.setLayoutManager(layoutManager);
                    rvListLPN.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


    }


    private void RefeshData() {
        swipeRefesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrListLPN.clear();
                SyncProgram syncProgram = new SyncProgram();
                syncProgram.execute();
                swipeRefesh.setRefreshing(false);

            }
        });

    }


    //đồng bộ param chương trình
    class SyncProgram extends AsyncTask<Void, Integer, Integer> {

        public boolean isSyncSuccess = false;

        @Override
        protected void onPreExecute() {
            CmnFns cmnFns = new CmnFns();
            int status = cmnFns.allowSynchronizeBy3G();
            if (status == 1) {
                progressSyncProgram = new ProgressDialog(LPNActivity.this);
                progressSyncProgram.setMessage("Đang load thông tin...");
                progressSyncProgram.setCancelable(false);
                progressSyncProgram.setCanceledOnTouchOutside(false);
                progressSyncProgram.show();


            } else if (status == 102) {
                Toast.makeText(LPNActivity.this, "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                progressSyncProgram.dismiss();

            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            // thực thi hàm lấy thông tin LPN
            DatabaseHelper.getInstance().deleteallProduct_LPN();
            int status = new CmnFns().synchronizeGetLPN(getApplication());

            return status;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == -1) {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNActivity.this, "Đồng bộ dữ liệu không thành công", Toast.LENGTH_SHORT).show();
                this.cancel(true);
                isSyncSuccess = false;

            } else {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNActivity.this, "Đồng bộ dữ liệu thành công", Toast.LENGTH_SHORT).show();
                this.cancel(true);

                SetDataSpinner();


                //               arrListLPN = DatabaseHelper.getInstance().getAllLpn();
//                adapter = new ItemLPNAdapter(LPNActivity.this, arrListLPN);
//                LinearLayoutManager layoutManager = new LinearLayoutManager(LPNActivity.this, RecyclerView.VERTICAL, false);
//                rvListLPN.setLayoutManager(layoutManager);
//                rvListLPN.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                isSyncSuccess = true;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                finish();
                break;
            case R.id.btnCreateLPN:
//                int status = new CmnFns().synchronizeCreate_LPN(getApplication());
//                if (status == -1) {
//                    Toast.makeText(this, "Tạo Barcode Thất Bại", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(this, "Tạo Barcode Thành Công", Toast.LENGTH_LONG).show();
//                    arrListLPN.clear();
//                    arrListLPN = DatabaseHelper.getInstance().getAllLpn();
//
//                    adapter = new ItemLPNAdapter(this, arrListLPN);
//                    rvListLPN.setAdapter(adapter);
//                    // lầy giá trị được chọn của spinner


                ReloadCreateLPN reloadCreateLPN = new ReloadCreateLPN();
                reloadCreateLPN.execute();

                // }
                break;
            case R.id.buttonPutToPallet:
                Intent intent = new Intent(LPNActivity.this, LoadPalletQRCode.class);
                startActivity(intent);
                break;
        }
    }

    //đồng bộ param chương trình
    class ReloadCreateLPN extends AsyncTask<Void, Integer, Integer> {

        public boolean isSyncSuccess = false;

        @Override
        protected void onPreExecute() {
            CmnFns cmnFns = new CmnFns();
            int status = cmnFns.allowSynchronizeBy3G();
            if (status == 1) {
                progressSyncProgram = new ProgressDialog(LPNActivity.this);
                progressSyncProgram.setMessage("Đang tạo mã...");
                progressSyncProgram.setCancelable(false);
                progressSyncProgram.setCanceledOnTouchOutside(false);
                progressSyncProgram.show();


            } else if (status == 102) {
                Toast.makeText(LPNActivity.this, "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                progressSyncProgram.dismiss();

            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int status = new CmnFns().synchronizeCreate_LPN(getApplication());

            return status;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == -1) {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNActivity.this, "Tạo barcode không thành công", Toast.LENGTH_SHORT).show();
                this.cancel(true);
                isSyncSuccess = false;

            } else {
                progressSyncProgram.dismiss();
                Toast.makeText(LPNActivity.this, "Tạo barcode thành công", Toast.LENGTH_SHORT).show();
                 this.cancel(true);

                if (arrListLPN.size() > 0) {
                    arrListLPN.clear();
                }
                try {
                    DatabaseHelper.getInstance().deleteallProduct_LPN();
                    int status = new CmnFns().synchronizeGetLPN(getApplication());
                    SetDataSpinner();
                } catch (Exception e) {

                }


            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}