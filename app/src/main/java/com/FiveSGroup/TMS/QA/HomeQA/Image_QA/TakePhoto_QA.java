package com.FiveSGroup.TMS.QA.HomeQA.Image_QA;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.BuildConfig;
import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.HomeActivity;
import com.FiveSGroup.TMS.OrderPhoto;
import com.FiveSGroup.TMS.QA.HomeQA.List_Criteria;
import com.FiveSGroup.TMS.QA.HomeQA.List_QA;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.TakePhotoFragment.ImageOrderAdapter;
import com.FiveSGroup.TMS.global;


import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TakePhoto_QA extends AppCompatActivity implements View.OnClickListener {

    Button btnCap, btnBack, btnUpload;
    ImageView imgViewOrder, imgClose, imgDisplay;
    RecyclerView rvListImageOrder;
    Date date1 , date2;

    File photoFile = null;
    String imageFileName;
    LinearLayout linearLayout;
    FrameLayout frameLayout;
    TextView tvEmptyImage;
    String product_code = "" , unit = "" , batch_number = ""  , exp = "" , stockcd = "" , stockindate = "";
    String exp_date = "" , stockin_date = "";
    private static final int REQUEST_TAKE_PHOTO = 2;

    private Bitmap bitmap, bitmapCanvas;

    private ArrayList<Bitmap> arrImage;

    private ImageOrderAdapter imageOrderAdapter;
    private Product_Photo_QA qa_photo;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takephoto_qa);

        getDataFromIntent();
        init();
        btnCap.setOnClickListener(this);
//        btnUpload.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        getImages();
        prepareData();
    }



    private void getDataFromIntent() {
        Intent intent = getIntent();
        product_code = intent.getStringExtra("product_code");
        batch_number = intent.getStringExtra("batch_number");
        try {
            if (batch_number.equals("---")) {
                batch_number = "";
            }
            if (batch_number == null) {
                batch_number = "";
            }
        } catch (Exception e) {

        }
        stockcd = intent.getStringExtra("stockcd");
        unit = intent.getStringExtra("unit");
        exp = intent.getStringExtra("exp");
        stockindate = intent.getStringExtra("stockindate");
        try {
            String chuoi[] = exp.split("/");
            String code = chuoi[0];
            String code1 = chuoi[1];
            String code2 = chuoi[2];
            exp_date = code2+"-"+code1+"-"+code;
            if(exp==null){
                exp_date = "";
            }
        }catch (Exception e){

        }
        try {
            String chuoi[] = stockindate.split("/");
            String code = chuoi[0];
            String code1 = chuoi[1];
            String code2 = chuoi[2];
            stockin_date = code2+"-"+code1+"-"+code;
            if (stockindate==null){
                stockin_date="";
            }

        }catch (Exception e){

        }


    }

    private void init() {
        btnCap = findViewById(R.id.btnCap);
        btnBack = findViewById(R.id.btnBack);
        imgViewOrder = findViewById(R.id.imagViewOrder);
        linearLayout = findViewById(R.id.layout_capture);
        frameLayout = findViewById(R.id.frameLayout);
        imgClose = findViewById(R.id.imgClose);
        imgDisplay = findViewById(R.id.imgDisplay);
        rvListImageOrder = findViewById(R.id.rv_list_image_order);
        tvEmptyImage = findViewById(R.id.tvEmptyImage);
//        btnUpload = findViewById(R.id.btnUpload);
        arrImage = new ArrayList<>();
    }

    private void getImages(){
        try {
            ArrayList<OrderPhoto> cPhotos = (ArrayList<OrderPhoto>) DatabaseHelper.getInstance().getAllPhotoForQA(stockcd , batch_number,product_code,unit);
            if(cPhotos.size() > 0){
                for(int i = 0; i < cPhotos.size(); i++){
                    OrderPhoto cPhoto = cPhotos.get(i);
                    File checkFile = new File(cPhoto.getPhoto_Path());
                    if (!checkFile.exists()) {
                        DatabaseHelper.getInstance().deletePhotoForOrders(
                                cPhoto.getPhoto_Name());
                        try {
                            checkFile.delete(); // clear file
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        continue;
                    }
                    String file = cPhoto.getPhoto_Path();
                    String nameFile = cPhoto.getPhoto_Name();
                    if(nameFile.contains("QA_"+stockcd+"_"+product_code+"_"+batch_number)) {
                        File image = new File(file); // l???y h??nh ???nh t??? ???????ng d???n
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap1 = BitmapFactory.decodeFile(image.getAbsolutePath(),
                                bmOptions);
                        arrImage.add(bitmap1);
                        // Toast.makeText(FCustomerAddNewEdit.this, cPhoto.getImage()+"", Toast.LENGTH_SHORT).show();
                        prepareData();
                    }
                }
            }else{
                tvEmptyImage.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){

        }

    }

    private void prepareData() {
        if (arrImage.size() > 0) {
            tvEmptyImage.setVisibility(View.GONE);
            imageOrderAdapter = new ImageOrderAdapter(TakePhoto_QA.this, arrImage, frameLayout, linearLayout, imgDisplay, imgClose);
            GridLayoutManager linearLayoutManager = new GridLayoutManager(TakePhoto_QA.this, 3);
            rvListImageOrder.setLayoutManager(linearLayoutManager);
            rvListImageOrder.setAdapter(imageOrderAdapter);
        } else {
            tvEmptyImage.setVisibility(View.VISIBLE);
        }
    }

    class Uploading extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TakePhoto_QA.this);

            //  progressDialog.setMax(0);
            progressDialog.setMessage("Uploading...");

            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            //  progressDialog.incrementProgressBy(5);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // progressDialog.setProgress(10);
//            new CmnFns().synchronizePhotoQA(TakePhoto_QA.this , stockcd);
            //  progressDialog.setProgress(100);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(TakePhoto_QA.this, "?????ng b??? th??nh c??ng", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }


    // h??m n??y ????? set c??c gi?? tr??? v??o h??nh ???nh
    private void GetInfoCustomer() {
        qa_photo = new Product_Photo_QA();
        qa_photo.setPRODUCT_CODE("");
        qa_photo.setSTOCK_QA_CD("");
        qa_photo.setUNIT("");
        qa_photo.setEXPIRED_DATE("");
        qa_photo.setSTOCKIN_DATE("");
        qa_photo.setEXPIRED_DATE("");
        SimpleDateFormat sdf = new SimpleDateFormat(
                global.getFormatDate());
        String currentDateandTime = sdf
                .format(new Date());
        qa_photo.setPHOTO_DATE(currentDateandTime);
    }

    // h??m g???i camera ????? ch???p h??nh ???nh
    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(TakePhoto_QA.this.getPackageManager()) != null) {
                // Create the File where the photo should go

                this.photoFile = createImageQA(); // t???o file h??nh ???nh tr?????c
                // khi ch???p v???i ?????nh d???ng
                // ch??? ?????nh
                // Continue only if the File was successfully created
                if (this.photoFile != null) {
                    //Ki???m tra SDK version thi???t b??? ????? t???o file h??nh ???nh
                    if (Build.VERSION.SDK_INT >= 24) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                FileProvider.getUriForFile(TakePhoto_QA.this,
                                        BuildConfig.APPLICATION_ID + ".provider", this.photoFile));
                    } else {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(this.photoFile));
                    }
                    startActivityForResult(takePictureIntent,
                            REQUEST_TAKE_PHOTO); // g???i t??nh n??ng ch???p ???nh v??
                    // h???ng k???t qu??? tr??? v???
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }


    public void synchronizeToService(){

        if(CmnFns.isNetworkAvailable()) {
            TakePhoto_QA.Uploading uploading = new Uploading();
            uploading.execute();
        }else{
            LayoutInflater factory = LayoutInflater.from(TakePhoto_QA.this);
            View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
            final AlertDialog dialog = new AlertDialog.Builder(TakePhoto_QA.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
            InsetDrawable inset = new InsetDrawable(back, 64);
            dialog.getWindow().setBackgroundDrawable(inset);
            dialog.setView(layout_cus);

            Button btnClose = layout_cus.findViewById(R.id.btnHuy);
            TextView textView = layout_cus.findViewById(R.id.tvText);


            textView.setText("Vui l??ng b???t k???t n???i m???ng");
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });
            dialog.show();
        }

    }

    private void backToHomeScreen() {
        try {
            Intent intentToHomeQRActivity = new Intent(TakePhoto_QA.this, List_QA.class);
            startActivity(intentToHomeQRActivity);
            finish();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    private File createImageQA() {

        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            imageFileName = "QA_"  + stockcd+ "_" + product_code+ "_" + batch_number+ "_" + unit+ "_" + timeStamp + ".png";

//            String path = Environment.getExternalStorageDirectory()
//                    + File.separator
//                    + getApplicationContext().getString(
//                    R.string.PathFolderPicture); // folder


            File file = null;
            File image = null;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                image = File.createTempFile(imageFileName, /* prefix */
                        ".jpg", /* suffix */
                        storageDir /* directory */
                );
            }else {
                image = CreateFile(TakePhoto_QA.this.getApplicationContext().getString(R.string.PathFolderSalePictureQA),
                        imageFileName, file);
            }

            // l??u th??ng tin h??nh ???nh ???????ng d???n ????? l??u v??o DB sau khi ng?????i d??ng
            // ch???p ???nh th??nh c??ng

            return image;
        } catch (Exception e) {
            Toast.makeText(TakePhoto_QA.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
            // TODO: handle exception
            e.printStackTrace();
            // CmnFns.writeLogError("FPhoto createImageFile " + e.getMessage());
            return null;
        }

    }


    public File CreateFile(String folderName, String filename, File filepath) {
        try {
            if (ContextCompat.checkSelfPermission(TakePhoto_QA.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                String path = Environment.getExternalStorageDirectory()
                        + File.separator + folderName; // folder name

                File filePath = new File(path);

                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                filepath = new File(path, filename);

                try {
                    if (!filepath.exists()) {
                        filepath.createNewFile();
                    }
                    return filepath;
                } catch (Exception exp) {

                    Log.i("FileUtils", exp.getMessage());
                }
            } else {
                // Request permission from the user
                ActivityCompat.requestPermissions(TakePhoto_QA.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            return filepath;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }


    //    // h??m ghi th??ng tin l??n b???c ???nh
    public void setTextForImage(OrderPhoto obj) {
        String text = "";
        try {
            File image = new File(obj.getPhoto_Path()); // l???y h??nh ???nh t??? ???????ng
            // d???n

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),
                    bmOptions);
//
            Bitmap alteredBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), bitmap.getConfig());
            Canvas canvas = new Canvas(alteredBitmap);
            Paint paint = new Paint();

            canvas.drawBitmap(bitmap, 0, 0, paint);

            // t???o m??u s???c cho background - v??ng ghi th??ng tin
            Paint strokePaint = new Paint();
            // strokePaint.setARGB(255, 0, 0, 0);
            strokePaint.setColor(TakePhoto_QA.this.getResources().getColor(R.color.white));
            strokePaint.setTextAlign(Paint.Align.LEFT);
            strokePaint.setTextSize(20);
            strokePaint.setTypeface(Typeface.DEFAULT_BOLD);
            strokePaint.setStyle(Paint.Style.FILL);
            strokePaint.setStrokeWidth(20f);

            // t???o m??u s???c cho ch???
            Paint textPaint = new Paint();
            // textPaint.setARGB(255, 255, 255, 255);
            textPaint.setColor(TakePhoto_QA.this.getResources().getColor(R.color.blue2));

            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTextSize(20);
            textPaint.setTypeface(Typeface.DEFAULT);

            canvas.drawText("M?? Phi???u : " +  stockcd , 10, 20 , strokePaint);
            canvas.drawText("M?? S???n Ph???m : " + product_code , 10, 50 , strokePaint);
            canvas.drawText("Unit : " + unit , 10, 80 , strokePaint);
            canvas.drawText("Batch/Cont : " + batch_number , 10, 110 , strokePaint);
            canvas.drawText("Ng??y s???n xu???t : " + stockindate , 10, 140 , strokePaint);
            canvas.drawText("Ng??y ch???p: " + obj.getStrDateTakesPhoto(), 10, 170, strokePaint);
            canvas.drawText("T??n file ???nh : " +  imageFileName , 10, 200, strokePaint);
            // l??u h??nh ???nh sau khi ???? ghi th??ng tin
            bitmapCanvas = alteredBitmap;
            SaveImage(alteredBitmap, obj.getPhoto_Path());
            //date = "";
        } catch (Exception e) {

        }
        //  return text;
    }


    private void SaveImage(Bitmap finalBitmap, String pathPhoto) {

        try {

            File file = new File(pathPhoto);
            if (file.exists())
                file.delete();

            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            //CmnFns.writeLogError("FPhoto SaveImage " + e.getMessage());
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    // gi???m k??ch th?????c h??nh ???nh ????? ko l??m n???ng h??? th???ng
                    new CmnFns().downSize(this.photoFile.getAbsolutePath(),
                            global.getMaxinumKiloByteOfPicture());
                } catch (Exception e) {
                    // TODO: handle exception
                    // CmnFns.writeLogError("FPhoto DownSize " + e.getMessage());
                }
                bitmap = BitmapFactory.decodeFile(this.photoFile
                        .getAbsolutePath());

                long photoCD;

                OrderPhoto file = new OrderPhoto();
                file.setPhoto_Name(this.photoFile.getName());
                file.setPhoto_Path(this.photoFile.getAbsolutePath());
                file.setPhoto_Date(new Date());
                file.setImage(bitmap);

//                Product_Photo_QA listphotoQA = new Product_Photo_QA();
//                listphotoQA.setPRODUCT_CODE(product_code);
//                listphotoQA.setBATCH_NUMBER(batch_number);
//                listphotoQA.setUNIT(unit);
//                listphotoQA.setSTOCKIN_DATE(stockindate);
//                listphotoQA.setEXPIRED_DATE(exp);
//                listphotoQA.setSTOCK_QA_CD(stockcd);
//                listphotoQA.setPHOTO_DATE(file.getStrDateTakesPhoto());
//                listphotoQA.setPHOTO_NAME(imageFileName);
//                DatabaseHelper.getInstance().CreatePhotoQA(listphotoQA);

//                CPhoto file = new CPhoto();
//                file.setPhoto_Name(this.photoFile.getName());
//                file.setPhoto_Path(this.photoFile.getAbsolutePath());
//                file.setDateTakesPhoto(new Date());
//                file.setImage(bitmap);


                //  file.setGeoCode(geoCode);
                // file.setGeoCodeAccuracy(getCodeAccuracy);
                // GetInfoCustomer();
                GetInfoCustomer();
                photoCD = DatabaseHelper.getInstance().createdSaleTakesPhoto(stockcd, file ,product_code, batch_number, unit , exp_date , stockin_date);

                file.setPhotoCD(photoCD); // l??u key sau khi insert v??o

                setTextForImage(file);
                File image = new File(file.getPhoto_Path()); // l???y h??nh ???nh t??? ???????ng d???n
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap1 = BitmapFactory.decodeFile(image.getAbsolutePath(),
                        bmOptions);
                arrImage.add(bitmap1);
                prepareData();
                //   this.setPic(this.photoFile.getAbsolutePath()); // hi???n th??? thumb

                DatabaseHelper.getInstance().update_Image_QA(batch_number ,stockcd , product_code,unit);
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnCap:
                dispatchTakePictureIntent();
                break;
            case R.id.btnUpload:
//                synchronizeToService();
                break;
            case R.id.btnBack:
                backToHomeScreen();
                break;
        }
    }
}
