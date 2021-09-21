package com.FiveSGroup.TMS.PickList;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.PutAway.Ea_Unit_Tam;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.SelectPropertiesProductActivity;
import com.FiveSGroup.TMS.Warehouse.Exp_Date_Tam;
import com.FiveSGroup.TMS.Warehouse.Product_S_P;
import com.FiveSGroup.TMS.global;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;

public class PickListQrCode extends AppCompatActivity {
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private CheckBox checkBoxGetDVT, checkBoxGetLPN;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    Button btnTruyCap;
    String value1 = "";
    String value2 = "";
    boolean check = false;
    String pro_code = "";
    String pro_name = "";
    Intent intent;
    String position = "";
    String product_cd = "";
    String stock = "";
    String expiredDate = " ";
    String ea_unit_position = " ";
    String stockinDate = "";
    String checkToFinish = "" , id_unique_PL = "";
    TextView textViewTitle;
    //biến để test hiển thị dialog đơn vị tính
    private String expDateTemp2 = "";
    private Button buttonBack, btnSend;
    private EditText edtBarcode;

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_camera);
        init();
        try {
            initialiseDetectorsAndSources();

        } catch (Exception e) {

        }
        check = true;
        getDataFromIntent();

        setCheckBox();
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != null || checkToFinish != null) {
                    Intent intent = new Intent(PickListQrCode.this, ListPickList.class);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    CmnFns.hideSoftKeyboard(PickListQrCode.this);
                }catch (Exception e){

                }
                String barcode = edtBarcode.getText().toString();
                GetData(barcode);
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void init() {
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        btnSend = findViewById(R.id.btnSend);
        edtBarcode = findViewById(R.id.edtBarcode);
        checkBoxGetDVT = findViewById(R.id.checkBoxGetDVT);
        checkBoxGetLPN = findViewById(R.id.checkBoxGetLPN);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        textViewTitle = findViewById(R.id.tvTitle);
        textViewTitle.setText("QUÉT MÃ - PICKLIST");
        buttonBack = findViewById(R.id.buttonQRBack);
    }
    private void setCheckBox() {
        checkBoxGetDVT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxGetLPN.setChecked(false);
                }

            }
        });
        checkBoxGetLPN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxGetDVT.setChecked(false);
                }
            }
        });
    }
    private void getDataFromIntent() {
        intent = getIntent();
        checkToFinish = intent.getStringExtra("check_to_finish_at_list");
        ea_unit_position = intent.getStringExtra("ea_unit_position");
        // position được truyền qua để định danh nó được bấm from hay to
        position = intent.getStringExtra("position");
        // được tuyền qua từ adapter
        product_cd = intent.getStringExtra("product_cd");
        // được truyền qua từ adapter
        stock = intent.getStringExtra("stock");
        id_unique_PL = intent.getStringExtra("id_unique_PL");


        // expiredDate truyền từ adapter để xử lí from - to
        expiredDate = intent.getStringExtra("c");
        // stockindate truyền từ adapter để xử lí from - to
        stockinDate = intent.getStringExtra("stockin_date");
        if (!(position == null)) {
            checkBoxGetDVT.setVisibility(View.INVISIBLE);
            checkBoxGetLPN.setVisibility(View.VISIBLE);
            checkBoxGetLPN.setChecked(false);
            if (position.equals("1")) {
                textViewTitle.setText("QUÉT VỊ TRÍ TỪ");
            } else if (position.equals("2")) {
                textViewTitle.setText("QUÉT VỊ TRÍ ĐẾN");
            }
        } else {
            checkBoxGetDVT.setVisibility(View.VISIBLE);
            checkBoxGetLPN.setVisibility(View.VISIBLE);
            checkBoxGetDVT.setChecked(true);
            textViewTitle.setText("QUÉT MÃ - PICKLIST");
        }
    }

    private void initialiseDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .setRequestedFps(30.0f)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(PickListQrCode.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(PickListQrCode.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    if (check == true) {
                        check = false;
                        Log.d("double", String.valueOf(barcodes.size()));
                        barcodeText.post(new Runnable() {

                            @Override
                            public void run() {

                                    try {
                                        barcodeData = barcodes.valueAt(0).displayValue;
                                        Toast.makeText(PickListQrCode.this, barcodeData + "", Toast.LENGTH_LONG).show();
                                        Log.e("barcode2", "" + barcodeData);

                                        if (barcodeData != null) {
                                            barcodeData = barcodeData.replace("\n","");
                                            edtBarcode.setText(barcodeData);
                                            GetData(barcodeData);
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(PickListQrCode.this, "Vui Lòng Thử Lại", Toast.LENGTH_LONG).show();
                                        Log.d("#777", e.getMessage());
                                        Intent intent = new Intent(PickListQrCode.this, ListPickList.class);
                                        intent.putExtra("pick_list", "333");
                                        intent.putExtra("id_unique_PL", id_unique_PL);

                                        startActivity(intent);
                                        finish();
                                    }


                                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                }
                        });
                    }


                }
            }
        });
    }

    private void GetData(final String barcodeData){
        String texxt = CmnFns.readDataAdmin();

        if (checkBoxGetLPN.isChecked()) {
            if (expiredDate != null) {
                Intent intentt = new Intent(getApplication(), ListPickList.class);
                intentt.putExtra("lpn", "444");
                intentt.putExtra("btn1", barcodeData);
                intentt.putExtra("returnposition", position);
                intentt.putExtra("return_ea_unit_position", ea_unit_position);
                intentt.putExtra("returnCD", product_cd);
                intentt.putExtra("returnStock", stock);
                intentt.putExtra("pick_list", "333");
                intentt.putExtra("id_unique_PL", id_unique_PL);

                // truyền qua cho ListQRCode để xử lí from - to
                intentt.putExtra("expdate", expiredDate);
                intentt.putExtra("stockin_date", stockinDate);

                startActivity(intentt);
                DatabaseHelper.getInstance().deleteallExp_date();
                DatabaseHelper.getInstance().deleteallEa_Unit();
                //editor.commit();
                finish();


            } else {
                Intent intentt = new Intent(getApplication(), ListPickList.class);
                intentt.putExtra("lpn", "444");
                intentt.putExtra("btn1", barcodeData);
                intentt.putExtra("pick_list", "333");
                intentt.putExtra("id_unique_PL", id_unique_PL);
                startActivity(intentt);
                finish();
            }

        } else {

            DatabaseHelper.getInstance().deleteallProduct_S_P();

            int statusGetcode = new CmnFns().getProduct_code(barcodeData);
            if (statusGetcode != 1) {
                ReturnPosition(barcodeData);
            } else {
                final ArrayList<Product_S_P> product_s_ps = DatabaseHelper.getInstance().getallValueSP();
                if (product_s_ps.size() > 1) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(PickListQrCode.this);
                    builder.setTitle("Mã Sản Phẩm - Tên Sản Phẩm");

                    final ArrayList<String> product_code = new ArrayList<>();
                    for (int i = 0; i < product_s_ps.size(); i++) {
                        product_code.add(product_s_ps.get(i).getPRODUCT_CODE() + " - " + product_s_ps.get(i).getPRODUCT_NAME());
                    }
                    // chuyển đổi exp_date thành mảng chuỗi String
                    String[] mStringArray = new String[product_code.size()];
                    mStringArray = product_code.toArray(mStringArray);

                    final String[] mString = mStringArray;
                    builder.setItems(mString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String product_name = mString[which];
                            String[] chuoi = product_name.split(" - ");
                            //int vitri = which;
//                        String product_code = product_s_ps.get(vitri).getPRODUCT_CODE();

                            dialog.dismiss(); // Close Dialog
                            if (product_name != "") {
                                pro_code = chuoi[0];
                                pro_name = chuoi[1];
                                getinformation(barcodeData);
                            }

                            // Do some thing....

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    pro_code = product_s_ps.get(0).getPRODUCT_CODE();
                    getinformation(barcodeData);
                }
            }
        }
    }
    private void getinformation(final String barcodeData) {
        int statusGetCustt = new CmnFns().getDataFromSeverWithBatch2(barcodeData, CmnFns.readDataAdmin(), "WPL", 0, global.getPickListCD());
//        if (statusGetCustt != 1) {
//            ReturnPosition(barcodeData);
//        }
//        else {
//            if (expiredDate != null) {
//
//                ReturnPosition(barcodeData);
//
//            } else {
                // lấy tất cả hạn `sử dụng trong database ra
                final ArrayList<Exp_Date_Tam> expired_date = DatabaseHelper.getInstance().getallValue2(pro_code);


                if (expired_date.size() > 1) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(PickListQrCode.this);
                    builder.setTitle("Chọn Hạn Sử Dụng - Ngày Nhập Kho - Batch Number");

                    final ArrayList<String> exp_date = new ArrayList<>();
                    for (int i = 0; i < expired_date.size(); i++) {
                        exp_date.add(expired_date.get(i).getEXPIRED_DATE_TAM() + " - " + expired_date.get(i).getSTOCKIN_DATE_TAM()
                                + " - " + expired_date.get(i).getBATCH_NUMBER_TAM());                    }

                    // chuyển đổi exp_date thành mảng chuỗi String
                    String[] mStringArray = new String[exp_date.size()];
                    mStringArray = exp_date.toArray(mStringArray);

                    final String[] mString = mStringArray;
                    builder.setItems(mString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String expDate = mString[which];
                            int vitri = which;
                            String product_code = expired_date.get(vitri).getPRODUCT_CODE_TAM();
                            dialog.dismiss(); // Close Dialog
                            if ((pro_code.equals("")) || (pro_code.equals(product_code))) {
                                if (expDate != "") {
                                    expDateTemp2 = expDate; //TEST
                                    String chuoi[] = expDateTemp2.split(" - ");
                                    String c = "";
                                    try {
                                        c = chuoi[2];
                                    }catch (Exception e){

                                    }
                                    if (chuoi[0].equals("Khác")){
                                        Intent intent = new Intent(PickListQrCode.this, SelectPropertiesProductActivity.class);
                                        intent.putExtra("typeScan", "scan_from_pick_list");
                                        intent.putExtra("btn1", barcodeData);
                                        intent.putExtra("pro_code", pro_code);
                                        intent.putExtra("pro_name", pro_name);
                                        intent.putExtra("returnposition", position);
                                        intent.putExtra("returnCD", product_cd);
                                        intent.putExtra("id_unique_PL", id_unique_PL);
                                        intent.putExtra("returnStock", stock);
                                        DatabaseHelper.getInstance().deleteallExp_date();
                                        DatabaseHelper.getInstance().deleteallEa_Unit();
                                        startActivity(intent);
                                        finish();
                                        return;
                                    }
                                    if (!checkBoxGetDVT.isChecked()) {
                                        ReturnProduct(barcodeData, chuoi[0], chuoi[1], c);
                                        //ReturnProduct(barcodeData,expDateTemp2,"");
                                    } else {
                                        ShowDialogUnit(barcodeData, chuoi[0], chuoi[1], c);
                                    }

                                }
                                // Do some thing....
                                // For example: Call method of MainActivity.
                                Toast.makeText(PickListQrCode.this, "You select: " + expDate,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Checkproduct_Code();
                            }


                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else if (expired_date.size() == 1) {
                    String expDatetemp = "" , batch_number = "", product_code = "" , stockin_date = "";
                    try {
                        expDatetemp = expired_date.get(0).getEXPIRED_DATE_TAM();
                        stockin_date = expired_date.get(0).getSTOCKIN_DATE_TAM();
                        batch_number = expired_date.get(0).getBATCH_NUMBER_TAM();
                        product_code = expired_date.get(0).getPRODUCT_CODE_TAM();
                    } catch (Exception e) {

                    }
                    if ((pro_code.equals("")) || (pro_code.equals(product_code))) {
//                            String chuoi[] = expDatetemp.split(" - ");
                        if (!checkBoxGetDVT.isChecked()) {
                            ReturnProduct(barcodeData, expDatetemp, stockin_date ,batch_number);
                        } else {
                            ShowDialogUnit(barcodeData, expDatetemp, stockin_date ,batch_number);
                        }
                    }else{
                        Checkproduct_Code();
                    }


                } else {
                    Toast.makeText(PickListQrCode.this, "Sản Phẩm Không Có Trong Kho", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PickListQrCode.this, ListPickList.class);
//                    intent.putExtra("pick_list", "333");
//                    intent.putExtra("btn1", barcodeData);
//                    intent.putExtra("id_unique_PL", id_unique_PL);
                    startActivity(intent);
                    finish();
                }

//            }
//        }
    }
    private void Checkproduct_Code(){
        Intent intentt = new Intent(getApplication(), ListPickList.class);
        intentt.putExtra("key", "1");
        startActivity(intentt);
    }
    private void ReturnPosition(String barcode) {
        Intent intentt = new Intent(getApplication(), ListPickList.class);
        intentt.putExtra("btn1", barcode);
        intentt.putExtra("returnposition", position);
        intentt.putExtra("returnCD", product_cd);
        intentt.putExtra("returnStock", stock);
        intentt.putExtra("pick_list", "333");
        intentt.putExtra("id_unique_PL", id_unique_PL);
        // truyền qua cho ListQRCode để xử lí from - to
        intentt.putExtra("expdate", expiredDate);
        intentt.putExtra("return_ea_unit_position", ea_unit_position);
        intentt.putExtra("stockin_date", stockinDate);


        startActivity(intentt);
        DatabaseHelper.getInstance().deleteallExp_date();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        finish();

    }

    private void ReturnProduct(String barcode, String expDatetemp ,String stockinDateShow , String batch_number) {
        int statusGetEa_Unit = new CmnFns().getEa_UnitFromServer(barcode, "1",pro_code);
        final ArrayList<Ea_Unit_Tam> ea_unit_tams = DatabaseHelper.getInstance().getallEa_Unit();

        Intent intentt = new Intent(getApplication(), ListPickList.class);
        intentt.putExtra("btn1", barcode);
        intentt.putExtra("returnposition", position);
        intentt.putExtra("returnCD", product_cd);
        intentt.putExtra("pro_code", pro_code);
        intentt.putExtra("pro_name", pro_name);
        intentt.putExtra("batch_number", batch_number);
        intentt.putExtra("returnStock", stock);
        intentt.putExtra("exp_date", expDatetemp);
        intentt.putExtra("id_unique_PL", id_unique_PL);
        intentt.putExtra("pick_list", "333");
        intentt.putExtra("ea_unit", ea_unit_tams.get(0).getEA_UNIT_TAM());
        intentt.putExtra("return_ea_unit_position", ea_unit_position);
        if (stockinDate == null) {
            intentt.putExtra("stockin_date", stockinDateShow);

        } else {
            intentt.putExtra("stockin_date", stockinDate);
        }

        startActivity(intentt);
        DatabaseHelper.getInstance().deleteallExp_date();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        finish();
    }


    private void ShowDialogUnit(final String barcode, final String expDateTemp2 , final String stockinDateShow,final String batch_number) {
        int statusGetEa_Unit = new CmnFns().getEa_UnitFromServer(barcode, "2",pro_code);

        final ArrayList<Ea_Unit_Tam> ea_unit_tams = DatabaseHelper.getInstance().getallEa_Unit();
        String Ea_Unit_temp = "";
        try {
            Ea_Unit_temp = ea_unit_tams.get(0).getEA_UNIT_TAM();
        } catch (Exception e) {

        }

        final ArrayList<String> ea_unit = new ArrayList<>();
        for (int i = 0; i < ea_unit_tams.size(); i++) {
            ea_unit.add(ea_unit_tams.get(i).getEA_UNIT_TAM());
        }

        // chuyển đổi exp_date thành mảng chuỗi String
        String[] mStringArray = new String[ea_unit.size()];
        mStringArray = ea_unit.toArray(mStringArray);
        final String[] mString = mStringArray;

        AlertDialog.Builder builderDVT = new AlertDialog.Builder(PickListQrCode.this);
        builderDVT.setTitle("CHỌN ĐƠN VỊ TÍNH");
        builderDVT.setCancelable(false);
        builderDVT.setItems(mString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PickListQrCode.this, mString[which], Toast.LENGTH_LONG).show();
                Intent intentt = new Intent(getApplication(), ListPickList.class);
                intentt.putExtra("btn1", barcode);
                intentt.putExtra("returnposition", position);
                intentt.putExtra("returnCD", product_cd);
                intentt.putExtra("pro_code", pro_code);
                intentt.putExtra("pro_name", pro_name);
                intentt.putExtra("returnStock", stock);
                intentt.putExtra("batch_number", batch_number);
                intentt.putExtra("id_unique_PL", id_unique_PL);
                intentt.putExtra("return_ea_unit_position", ea_unit_position);
                if (stockinDate == null) {
                    intentt.putExtra("stockin_date", stockinDateShow);

                } else {
                    intentt.putExtra("stockin_date", stockinDate);
                }

                intentt.putExtra("pick_list", "333");
                // truyền qua cho ListQRcode để add vào text HSD
                intentt.putExtra("exp_date", expDateTemp2);
                intentt.putExtra("ea_unit", mString[which]);
                Log.e("barcodeData1", "" + barcodeData);
                Log.e("nhận từ 2 nút", "" + position);
                startActivity(intentt);
                DatabaseHelper.getInstance().deleteallExp_date();
                DatabaseHelper.getInstance().deleteallEa_Unit();
                finish();
            }
        }).show();

        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
    }
}
