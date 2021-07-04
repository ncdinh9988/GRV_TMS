package com.FiveSGroup.TMS.LPN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.LoadPallet.LoadPalletQRCode;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.Warehouse.HomeQRActivity;
import com.FiveSGroup.TMS.Warehouse.ListQrcode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LPNActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonBack, btnCreateLPN, buttonPutToPallet;
    private ProgressDialog progressSyncProgram;
    private TextView createDate , idbarcode;
    String  press ="";
    private RecyclerView rvListLPN;
    private SwipeRefreshLayout swipeRefesh;
    private String chooseDate;
    ArrayList<LPN> arrListLPN;
    private ItemLPNAdapter adapter;
    private Spinner spinner;
    String number = "100";
    final Calendar myCalendar = Calendar.getInstance();


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
        createDate = findViewById(R.id.priceproduct);
        idbarcode = findViewById(R.id.idproduct);
//        spinner = findViewById(R.id.spinner);
        buttonBack = findViewById(R.id.buttonBack);
        btnCreateLPN = findViewById(R.id.btnCreateLPN);
        rvListLPN = findViewById(R.id.rvListLPn);
        swipeRefesh = findViewById(R.id.swipeRefesh);
        arrListLPN = new ArrayList<>();
//        buttonPutToPallet.setEnabled(false);
        buttonBack.setOnClickListener(this);
        btnCreateLPN.setOnClickListener(this);
        buttonPutToPallet.setOnClickListener(this);

        idbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(LPNActivity.this);
                View layout_cus = factory.inflate(R.layout.layout_find, null);
                final AlertDialog dialog = new AlertDialog.Builder(LPNActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                new DatePickerDialog(LPNActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }
    public void updateBarcode(){
        Syncbarcodedata syncbarcodedata = new Syncbarcodedata();
        syncbarcodedata.execute();
    }


    public void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        chooseDate = sdf.format(myCalendar.getTime());
        Log.d("logggggggg", "" + chooseDate);

        createDate.setText(sdf.format(myCalendar.getTime()));

        SyncDate syncDate = new SyncDate();
        syncDate.execute();

    }

    class Syncbarcodedata extends AsyncTask<Void, Integer, Integer> {

        public boolean isSyncSuccess = false;

        @Override
        protected void onPreExecute() {
            CmnFns cmnFns = new CmnFns();
            int status = cmnFns.allowSynchronizeBy3G();
            if (status == 1) {
                progressSyncProgram = new ProgressDialog(LPNActivity.this);
                progressSyncProgram.setMessage("Đang tải thông tin...");
                progressSyncProgram.setCancelable(false);
                progressSyncProgram.setCanceledOnTouchOutside(false);
                progressSyncProgram.show();


            } else if (status == 102) {
                Toast.makeText(LPNActivity.this, "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                //progressSyncProgram.dismiss();

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

//                SetDataSpinner();
                arrListLPN = DatabaseHelper.getInstance().getAllLpnBarcode(press);
                adapter = new ItemLPNAdapter(LPNActivity.this, arrListLPN);
                LinearLayoutManager layoutManager = new LinearLayoutManager(LPNActivity.this, RecyclerView.VERTICAL, false);
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
                progressSyncProgram = new ProgressDialog(LPNActivity.this);
                progressSyncProgram.setMessage("Đang tải thông tin...");
                progressSyncProgram.setCancelable(false);
                progressSyncProgram.setCanceledOnTouchOutside(false);
                progressSyncProgram.show();


            } else if (status == 102) {
                Toast.makeText(LPNActivity.this, "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                //progressSyncProgram.dismiss();

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

//                SetDataSpinner();
                arrListLPN = DatabaseHelper.getInstance().getAllLpn_date(chooseDate);
                adapter = new ItemLPNAdapter(LPNActivity.this, arrListLPN);
                LinearLayoutManager layoutManager = new LinearLayoutManager(LPNActivity.this, RecyclerView.VERTICAL, false);
                rvListLPN.setLayoutManager(layoutManager);
                rvListLPN.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                isSyncSuccess = true;
            }
        }
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
                progressSyncProgram.setMessage("Đang tải thông tin...");
                progressSyncProgram.setCancelable(false);
                progressSyncProgram.setCanceledOnTouchOutside(false);
                progressSyncProgram.show();


            } else if (status == 102) {
                Toast.makeText(LPNActivity.this, "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
               // progressSyncProgram.dismiss();

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

//                SetDataSpinner();
                arrListLPN = DatabaseHelper.getInstance().getAllLpn_limit();
                adapter = new ItemLPNAdapter(LPNActivity.this, arrListLPN);
                LinearLayoutManager layoutManager = new LinearLayoutManager(LPNActivity.this, RecyclerView.VERTICAL, false);
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
                progressSyncProgram = new ProgressDialog(LPNActivity.this);
                progressSyncProgram.setMessage("Đang tạo mã ...");
                progressSyncProgram.setCancelable(false);
                progressSyncProgram.setCanceledOnTouchOutside(false);
                progressSyncProgram.show();


            } else if (status == 102) {
                Toast.makeText(LPNActivity.this, "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                // progressSyncProgram.dismiss();

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

//                SetDataSpinner();
                arrListLPN = DatabaseHelper.getInstance().getAllLpn();
                adapter = new ItemLPNAdapter(LPNActivity.this, arrListLPN);
                LinearLayoutManager layoutManager = new LinearLayoutManager(LPNActivity.this, RecyclerView.VERTICAL, false);
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
                finish();
                break;
            case R.id.btnCreateLPN:
                try {
                    int status = new CmnFns().synchronizeCreate_LPN(getApplication());
                    if (status == -1) {
                        Toast.makeText(this, "Tạo Barcode Thất Bại", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Tạo Barcode Thành Công", Toast.LENGTH_LONG).show();
                        arrListLPN.clear();
                        arrListLPN = DatabaseHelper.getInstance().getAllLpn();
                        createDate.setText("Ngày Tạo");

                        adapter = new ItemLPNAdapter(LPNActivity.this, arrListLPN);
                        Syncbarcode syncProgram = new Syncbarcode();
                        syncProgram.execute();
                    }
                }catch (Exception e){
                    Toast.makeText(this,"Vui Lòng Thử Lại ..." ,Toast.LENGTH_SHORT).show();
                    finish();
                }

//                    LinearLayoutManager layoutManager = new LinearLayoutManager(LPNActivity.this, RecyclerView.VERTICAL, false);
//                    rvListLPN.setLayoutManager(layoutManager);
//                    rvListLPN.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                    // lầy giá trị được chọn của spinner


//                ReloadCreateLPN reloadCreateLPN = new ReloadCreateLPN();
//                reloadCreateLPN.execute();

                break;
            case R.id.buttonPutToPallet:
//                final int block_Warehouse_WPP = new CmnFns().Block_Function_By_Warehouse();
//                if(block_Warehouse_WPP == -29){
//                    Toast.makeText(this,"Kho đang thực hiện kiểm tồn",Toast.LENGTH_LONG).show();
//                    break;
//                }else if(block_Warehouse_WPP == -1){
//                    Toast.makeText(this,"Đã xảy ra lỗi vui lòng thử lại",Toast.LENGTH_LONG).show();
//                    break;
//                }else if(block_Warehouse_WPP == 1){
                    Intent intent = new Intent(LPNActivity.this, LoadPalletQRCode.class);
                    startActivity(intent);
                    break;
//                }
        }
    }

    @Override
    public void onBackPressed() {

    }
}