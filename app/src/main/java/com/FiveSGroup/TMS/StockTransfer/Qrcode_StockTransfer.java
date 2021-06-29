package com.FiveSGroup.TMS.StockTransfer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.PutAway.Ea_Unit_Tam;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.SelectPropertiesProductActivity;
import com.FiveSGroup.TMS.Warehouse.Exp_Date_Tam;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Qrcode_StockTransfer extends AppCompatActivity {
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    private CheckBox checkBoxGetDVT, checkBoxGetLPN;
    boolean check = false;
    android.hardware.Camera camera;
    Intent intent;
    String position = "";
    String product_cd = "";
    String stock = "";
    String expiredDate = " ";
    String ea_unit_position = " ";
    String stockinDate = "";
    String id_unique_STF="";

    TextView textViewTitle;
    //biến để test hiển thị dialog đơn vị tính
    private String expDateTemp2 = "";
    View viewScan;
    Button buttonBack, btnSend;
    private EditText edtBarcode;;
    android.hardware.Camera.Parameters params;

    private boolean isUp;
    String checkToFinish = "";

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
        isUp = false;
        try {
            initialiseDetectorsAndSources();

        } catch (Exception e) {

        }
        check = true;

        setCheckBox();
        getDataFromIntent();
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != null || checkToFinish != null) {
                    Intent intent = new Intent(Qrcode_StockTransfer.this, ListStockTransfer.class);
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
                    CmnFns.hideSoftKeyboard(Qrcode_StockTransfer.this);
                }catch (Exception e){

                }
                String barcode = edtBarcode.getText().toString();
                GetData(barcode);
            }
        });
    }

    private void init() {
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        btnSend = findViewById(R.id.btnSend);
        edtBarcode = findViewById(R.id.edtBarcode);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        checkBoxGetDVT = findViewById(R.id.checkBoxGetDVT);
        checkBoxGetLPN = findViewById(R.id.checkBoxGetLPN);
        textViewTitle = findViewById(R.id.tvTitle);
        textViewTitle.setText("QUÉT MÃ - CHUYỂN VỊ TRÍ");
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
        // được tuyền qua từ adapter
        product_cd = intent.getStringExtra("product_cd_position");
        // position được truyền qua để định danh nó được bấm from hay to
        position = intent.getStringExtra("position");
        // expiredDate truyền từ adapter để xử lí from - to
        expiredDate = intent.getStringExtra("c_position");
        // stockindate truyền từ adapter để xử lí from - to
        stockinDate = intent.getStringExtra("stockin_date_position");

        id_unique_STF = intent.getStringExtra("id_unique_STF");


        // được truyền qua từ adapter
        stock = intent.getStringExtra("stock");


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
            textViewTitle.setText("QUÉT MÃ - CHUYỂN VỊ TRÍ");
        }
    }


    private void initialiseDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(Qrcode_StockTransfer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(Qrcode_StockTransfer.this, new
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

        surfaceView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                cameraFocus(event, cameraSource, android.hardware.Camera.Parameters.FOCUS_MODE_AUTO, surfaceView);

                return false;
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                Log.d("TAG", "receiveDetections: " + barcodes);
                if (barcodes.size() != 0) {
                    if (check == true) {
                        check = false;
                        Log.d("double", String.valueOf(barcodes.size()));
                        barcodeText.post(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    barcodeData = barcodes.valueAt(0).displayValue;
                                    Toast.makeText(Qrcode_StockTransfer.this, barcodeData + "", Toast.LENGTH_LONG).show();
                                    Log.e("barcode2", "" + barcodeData);


                                    if (barcodeData != null) {
                                        barcodeData = barcodeData.replace("\n","");
                                        edtBarcode.setText(barcodeData);
                                        GetData(barcodeData);
                                    }
                                } catch (Exception e) {
                                    Log.d("#777: ", e.getMessage());
                                    Intent intent = new Intent(Qrcode_StockTransfer.this, ListStockTransfer.class);
                                    intent.putExtra("btn1", barcodeData);
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


    private boolean cameraFocus(MotionEvent event, @NonNull CameraSource cameraSource, @NonNull String focusMode, SurfaceView cameraView) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);
        // Get the pointer's current position
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        float touchMajor = event.getTouchMajor();
        float touchMinor = event.getTouchMinor();

        Rect touchRect = new Rect((int) (x - touchMajor / 2), (int) (y - touchMinor / 2), (int) (x + touchMajor / 2), (int) (y + touchMinor / 2));

        Rect focusArea = new Rect();

        focusArea.set(touchRect.left * 2000 / cameraView.getWidth() - 1000,
                touchRect.top * 2000 / cameraView.getHeight() - 1000,
                touchRect.right * 2000 / cameraView.getWidth() - 1000,
                touchRect.bottom * 2000 / cameraView.getHeight() - 1000);

        // Submit focus area to camera

        ArrayList<android.hardware.Camera.Area> focusAreas = new ArrayList<android.hardware.Camera.Area>();
        focusAreas.add(new android.hardware.Camera.Area(focusArea, 1000));

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    camera = (android.hardware.Camera) field.get(cameraSource);
                    if (camera != null) {
                        params = camera.getParameters();
                        params.setFocusMode(focusMode);
                        params.setFocusAreas(focusAreas);
                        camera.setParameters(params);

                        // Start the autofocus operation

                        camera.autoFocus(new android.hardware.Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, android.hardware.Camera camera) {
                                // currently set to auto-focus on single touch
                            }
                        });
                        return true;
                    }

                    return false;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
        return false;
    }

    private void GetData(final String barcodeData){
        String texxt = CmnFns.readDataAdmin();
        //TODO: Kiểm tra lại có dùng được chung hàm getPutAwayFromServer() không?

        if (checkBoxGetLPN.isChecked()) {
            if (expiredDate != null) {
                Intent intentt = new Intent(getApplication(), ListStockTransfer.class);
                intentt.putExtra("lpn", "444");

                intentt.putExtra("btn1", barcodeData);
                intentt.putExtra("id_unique_STF", id_unique_STF);
                intentt.putExtra("returnposition", position);
                intentt.putExtra("return_ea_unit_position", ea_unit_position);
                intentt.putExtra("returnCD", product_cd);
                intentt.putExtra("returnStock", stock);
                //intentt.putExtra("stock_transfer", "333");

                // truyền qua cho ListQRCode để xử lí from - to
                intentt.putExtra("expdate", expiredDate);
                intentt.putExtra("stockin_date", stockinDate);

                Log.e("barcodeData1", "" + barcodeData);
                Log.e("nhận từ 2 nút", "" + position);
                startActivity(intentt);
                DatabaseHelper.getInstance().deleteallExp_date();
                DatabaseHelper.getInstance().deleteallEa_Unit();
                //editor.commit();
                finish();


            } else {
                Intent intentt = new Intent(getApplication(), ListStockTransfer.class);
                intentt.putExtra("lpn", "444");
                intentt.putExtra("btn1", barcodeData);
                intentt.putExtra("id_unique_STF", id_unique_STF);
                //intentt.putExtra("stock_transfer", "333");
                startActivity(intentt);
                finish();
            }

        } else {
            int statusGetCustt = new CmnFns().getPutAwayFromServer(barcodeData, texxt, "WOI", 0, "");

            // expiredDate nhận giá trị từ adapter để xử lí position
            if (statusGetCustt != 1) {
                ReturnPosition(barcodeData);
            } else {
                if (expiredDate != null) {

                    ReturnPosition(barcodeData);

                } else {
                    try {
                        // lấy tất cả hạn sử dụng trong database ra
                        final ArrayList<Exp_Date_Tam> expired_date = DatabaseHelper.getInstance().getallExp_date();

                        if (expired_date.size() > 1) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Qrcode_StockTransfer.this);
                            builder.setTitle("Chọn Hạn Sử Dụng - Ngày Nhập Kho");

                            final ArrayList<String> exp_date = new ArrayList<>();
                            for (int i = 0; i < expired_date.size(); i++) {
                                exp_date.add(expired_date.get(i).getEXPIRED_DATE_TAM());
                            }

                            // chuyển đổi exp_date thành mảng chuỗi String
                            String[] mStringArray = new String[exp_date.size()];
                            mStringArray = exp_date.toArray(mStringArray);

                            final String[] mString = mStringArray;
                            builder.setItems(mString, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String expDate = mString[which];

                                    dialog.dismiss(); // Close Dialog

                                    if (expDate != "") {
                                        // expDateTemp2 lấy giá trị HSD được người dùng chọn
                                        expDateTemp2 = expDate;
                                        String chuoi[] = expDateTemp2.split(" - ");
                                        if (chuoi[0].equals("Khác")){
                                            Intent intent = new Intent(Qrcode_StockTransfer.this, SelectPropertiesProductActivity.class);
                                            intent.putExtra("typeScan", "scan_from_stock_transfer");
                                            intent.putExtra("btn1", barcodeData);
                                            intent.putExtra("returnposition", position);
                                            intent.putExtra("returnCD", product_cd);
                                            intent.putExtra("id_unique_STF", id_unique_STF);
                                            intent.putExtra("returnStock", stock);
                                            DatabaseHelper.getInstance().deleteallExp_date();
                                            DatabaseHelper.getInstance().deleteallEa_Unit();
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }
                                        if (!checkBoxGetDVT.isChecked()) {
                                            ReturnProduct(barcodeData, chuoi[0], chuoi[1]);

                                        } else {
                                            ShowDialogUnit(barcodeData, chuoi[0], chuoi[1]);


                                        }

                                    }
                                    // Do some thing....
                                    // For example: Call method of MainActivity.
                                    Toast.makeText(Qrcode_StockTransfer.this, "You select: " + expDate,
                                            Toast.LENGTH_LONG).show();

                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else if (expired_date.size() == 1) {
                            String expDatetemp = "";
                            try {
                                expDatetemp = expired_date.get(0).getEXPIRED_DATE_TAM();
                            } catch (Exception e) {

                            }
                            String chuoi[] = expDatetemp.split(" - ");

                            if (!checkBoxGetDVT.isChecked()) {
                                ReturnProduct(barcodeData, chuoi[0], chuoi[1]);
                            } else {
                                ShowDialogUnit(barcodeData, chuoi[0], chuoi[1]);
                            }
                        } else {
                            Intent intent = new Intent(Qrcode_StockTransfer.this, ListStockTransfer.class);
                            intent.putExtra("btn1", barcodeData);
                            startActivity(intent);
                            finish();

                        }
                    } catch (Exception e) {
                        Log.d("#778:", e.getMessage());
                    }

                }
            }
        }
    }
    private void ReturnPosition(String barcode) {
        Intent intentt = new Intent(getApplication(), ListStockTransfer.class);
        intentt.putExtra("btn1", barcode);
        intentt.putExtra("returnposition", position);
        intentt.putExtra("return_ea_unit_position", ea_unit_position);
        intentt.putExtra("returnCD", product_cd);
        intentt.putExtra("returnStock", stock);
        intentt.putExtra("stock_transfer", "333");
        intentt.putExtra("id_unique_STF", id_unique_STF);
        intentt.putExtra("stockin_date", stockinDate);

        // truyền qua cho ListQRCode để xử lí from - to
        intentt.putExtra("expdate", expiredDate);

        Log.e("barcodeData1", "" + barcodeData);
        Log.e("nhận từ 2 nút", "" + position);
        startActivity(intentt);
        DatabaseHelper.getInstance().deleteallExp_date();
        DatabaseHelper.getInstance().deleteallEa_Unit();
        //editor.commit();
        finish();

    }

    private void ReturnProduct(String barcode, String expDatetemp, String stockinDateShow) {

        int statusGetEa_Unit = new CmnFns().getEa_UnitFromServer(barcode, "1");
        final ArrayList<Ea_Unit_Tam> ea_unit_tams = DatabaseHelper.getInstance().getallEa_Unit();

        Intent intentt = new Intent(getApplication(), ListStockTransfer.class);
        intentt.putExtra("btn1", barcode);
        intentt.putExtra("returnposition", position);
        intentt.putExtra("id_unique_STF", id_unique_STF);
        intentt.putExtra("return_ea_unit_position", ea_unit_position);
        intentt.putExtra("returnCD", product_cd);
        intentt.putExtra("returnStock", stock);
        intentt.putExtra("exp_date", expDatetemp);
       // intentt.putExtra("stock_transfer", "333");
        if (stockinDate == null) {
            intentt.putExtra("stockin_date", stockinDateShow);

        } else {
            intentt.putExtra("stockin_date", stockinDate);
        }
        if (ea_unit_tams.size() > 0) {
            intentt.putExtra("ea_unit", ea_unit_tams.get(0).getEA_UNIT_TAM());

        } else {
            intentt.putExtra("ea_unit", " ");
        }

        startActivity(intentt);
        DatabaseHelper.getInstance().deleteallEa_Unit();
        DatabaseHelper.getInstance().deleteallExp_date();
        //editor.commit();
        finish();
    }


    private void ShowDialogUnit(final String barcode, final String expDateTemp2, final String stockinDateShow) {
        int statusGetEa_Unit = new CmnFns().getEa_UnitFromServer(barcode, "2");

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


        AlertDialog.Builder builderDVT = new AlertDialog.Builder(Qrcode_StockTransfer.this);
        builderDVT.setTitle("CHỌN ĐƠN VỊ TÍNH");
        builderDVT.setCancelable(false);
        builderDVT.setItems(mString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Qrcode_StockTransfer.this, mString[which], Toast.LENGTH_LONG).show();
                Intent intentt = new Intent(getApplication(), ListStockTransfer.class);
                intentt.putExtra("btn1", barcode);
                intentt.putExtra("returnposition", position);
                intentt.putExtra("return_ea_unit_position", ea_unit_position);
                intentt.putExtra("returnCD", product_cd);
                //intentt.putExtra("stock_transfer", "333");
                intentt.putExtra("returnStock", stock);
                intentt.putExtra("stock_in", stockinDate);
                intentt.putExtra("id_unique_STF", id_unique_STF);
                // truyền qua cho ListQRcode để add vào text HSD
                intentt.putExtra("exp_date", expDateTemp2);
                intentt.putExtra("ea_unit", mString[which]);
                if (stockinDate == null) {
                    intentt.putExtra("stockin_date", stockinDateShow);

                } else {
                    intentt.putExtra("stockin_date", stockinDate);
                }

                startActivity(intentt);
                DatabaseHelper.getInstance().deleteallEa_Unit();
                DatabaseHelper.getInstance().deleteallExp_date();
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
        DatabaseHelper.getInstance().deleteallExp_date();
        DatabaseHelper.getInstance().deleteallEa_Unit();
    }
}
