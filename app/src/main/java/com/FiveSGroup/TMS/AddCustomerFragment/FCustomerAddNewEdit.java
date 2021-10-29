package com.FiveSGroup.TMS.AddCustomerFragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.BuildConfig;
import com.FiveSGroup.TMS.CPhoto;
import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.HomeActivity;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.global;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class FCustomerAddNewEdit extends Activity implements View.OnClickListener {
    RecyclerView rvListImage;
    private static final int REQUEST_TAKE_PHOTO = 1;

    private ArrayList<Bitmap> arrImage;
    private ArrayList<Bitmap> arrImageCustomer;

    private Bitmap bitmap;
    ImageAdapter adapter;

    private ImageView capture, imgDisplay, imgClose;
    EditText editCustomerCode;
    EditText editCustomerName;
    EditText editCustomerPhoneNumber;
    EditText editCustomerAddress;
    TextView tvGeoCode, tvEmptyImage;

    GpsTracker gpsTracker;
    String code, address, phoneNumber, name, geocode;
    CustomerEventbus customerEventbus;

    CCustomer vCustomer, customer;

    LinearLayout layout;
    FrameLayout frameLayout;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_layout_addnew_edit);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        capture = findViewById(R.id.capture_bill);
        editCustomerCode = findViewById(R.id.editCustomerCode);
        editCustomerName = findViewById(R.id.editCustomerName);
        editCustomerPhoneNumber = findViewById(R.id.editCustomerPhoneNumber);
        editCustomerAddress = findViewById(R.id.editCustomerAddress);
        tvGeoCode = findViewById(R.id.tvGeoCode);
        arrImage = new ArrayList<>();
        rvListImage = findViewById(R.id.rv_list_image);
        imgDisplay = findViewById(R.id.imgDisplay);
        layout = findViewById(R.id.layout_main);
        frameLayout = findViewById(R.id.frameLayout);
        imgClose = findViewById(R.id.imgClose);
        tvEmptyImage = findViewById(R.id.tvEmptyImage);
        arrImageCustomer = new ArrayList<>();

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editCustomerName.getText().toString().matches("")
                        || !editCustomerCode.getText().toString().matches("")
                        || !editCustomerPhoneNumber.getText().toString().matches("")) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(FCustomerAddNewEdit.this, "Bạn chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }

            }
        });

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        prepareData();
    }


    // hàm này để set các giá trị vào hình ảnh
    private void GetInfoCustomer() {
        customer = new CCustomer();
        code = editCustomerCode.getText().toString();
        address = editCustomerAddress.getText().toString();
        phoneNumber = editCustomerPhoneNumber.getText().toString();
        name = editCustomerName.getText().toString();
        geocode = tvGeoCode.getText().toString();

        customer.setCustomerCode(code);
        customer.setCustomerName(name);
        customer.setCustomerPhoneNumber(phoneNumber);
        customer.setCustomerAddress(address);
        customer.setCustomerLocation(geocode);

        SimpleDateFormat sdf = new SimpleDateFormat(
                global.getFormatDate());
        String currentDateandTime = sdf
                .format(new Date());
        customer.setCustomerCreatedDate(currentDateandTime);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btnHome:
//                    Intent t = new Intent(FCustomerAddNewEdit.this, HomeActivity.class);
//                    startActivity(t);
//
//                    Intent i = new Intent();
//                    if (customer == null) {
//                        customer = new CCustomer();
//                    }
                    finish();
                    break;
                case R.id.btnUpdate:
                    if (!editCustomerName.getText().toString().matches("")
                            || !editCustomerCode.getText().toString().matches("")
                            || !editCustomerPhoneNumber.getText().toString().matches("")
                            || arrImage.size() != 0) {
                        this.InsertUpdateInfoCustomer();
                        name = "";
                        code = "";
                        address = "";
                        phoneNumber = "";
                        geocode = "";
                    } else {
                        Toast.makeText(FCustomerAddNewEdit.this, R.string.Condition, Toast.LENGTH_SHORT).show();
                    }
                    vCustomer = null;
                    break;
                case R.id.btnUpLoad:


                    if (!editCustomerName.getText().toString().matches("")
                            || !editCustomerCode.getText().toString().matches("")
                            || !editCustomerPhoneNumber.getText().toString().matches("")
                            || arrImage.size() != 0) {
                        //new CmnFns().synchronizePhotoForOrders(FCustomerAddNewEdit.this);
                            Uploading uploading = new Uploading();
                            uploading.execute();

                    }
                    //Toast.makeText(this, "Đã upload thành công", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(FCustomerAddNewEdit.this, R.string.Condition, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.imgBtnGPS:
                    gpsTracker = new GpsTracker(FCustomerAddNewEdit.this);
                    if (gpsTracker.canGetLocation()) {
                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();
                        tvGeoCode.setText(String.valueOf(latitude) + "," + String.valueOf(longitude));
                        //getCompleteAddressString(latitude,longitude);
                        //Toast.makeText(FCustomerAddNewEdit.this,getAddress(FCustomerAddNewEdit.this, latitude, longitude)+ "", Toast.LENGTH_SHORT).show();
                    } else {
                        gpsTracker.showSettingsAlert();
                    }
                    break;
            }

        } catch (Exception e) {

        }

    }
    public  String getAddress(Context context, double LATITUDE, double LONGITUDE){
        String addressDetail = "";
        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                addressDetail = address + " "+city + " ";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressDetail;
    }
    private void prepareData() {
        if (arrImage.size() > 0) {
            tvEmptyImage.setVisibility(View.GONE);
            adapter = new ImageAdapter(this, arrImage, frameLayout, layout, imgDisplay, imgClose);
            GridLayoutManager linearLayoutManager = new GridLayoutManager(FCustomerAddNewEdit.this, 3);
            rvListImage.setLayoutManager(linearLayoutManager);
            rvListImage.setAdapter(adapter);
        }
    }

    File photoFile = null;

    // hàm gọi camera để chụp hình ảnh
    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go

                this.photoFile = createImageFile(); // tạo file hình ảnh trước
                // khi chụp với định dạng
                // chỉ định
                // Continue only if the File was successfully created
                if (this.photoFile != null) {
                    //Kiểm tra SDK version thiết bị để tạo file hình ảnh
                    if (Build.VERSION.SDK_INT >= 24) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                FileProvider.getUriForFile(this,
                                        BuildConfig.APPLICATION_ID + ".provider", this.photoFile));
                    } else {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(this.photoFile));
                    }
                    startActivityForResult(takePictureIntent,
                            REQUEST_TAKE_PHOTO); // gọi tính năng chụp ảnh và
                    // hứng kết quả trả về
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }

    private File createImageFile() {

        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            String imageFileName = "AddNew_" + editCustomerCode.getText().toString() + "_" + timeStamp + ".png";

//            String path = Environment.getExternalStorageDirectory()
//                    + File.separator
//                    + getApplicationContext().getString(
//                    R.string.PathFolderPicture); // folder


            File file = null;
            File image = CreateFile(getApplicationContext().getString(R.string.PathFolderPicture), imageFileName, file);

            // lưu thông tin hình ảnh đường dẫn để lưu vào DB sau khi người dùng
            // chụp ảnh thành công

            return image;
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
            // TODO: handle exception
            e.printStackTrace();
            // CmnFns.writeLogError("FPhoto createImageFile " + e.getMessage());
            return null;
        }

    }

    public File CreateFile(String folderName, String filename, File filepath) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            return filepath;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }


    //    // hàm ghi thông tin lên bức ảnh
    public void setTextForImage(CPhoto obj) {
        String text = "";
        try {
            File image = new File(obj.getPhotoPath()); // lấy hình ảnh từ đường
            // dẫn

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),
                    bmOptions);
//
            Bitmap alteredBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), bitmap.getConfig());
            Canvas canvas = new Canvas(alteredBitmap);
            Paint paint = new Paint();

            canvas.drawBitmap(bitmap, 0, 0, paint);

            // tạo màu sắc cho background - vùng ghi thông tin
            Paint strokePaint = new Paint();
            strokePaint.setColor(this.getResources().getColor(R.color.white));
            strokePaint.setTextAlign(Paint.Align.LEFT);
            strokePaint.setTextSize(20);
            strokePaint.setTypeface(Typeface.DEFAULT_BOLD);
            strokePaint.setStyle(Paint.Style.FILL);
            strokePaint.setStrokeWidth(20f);

            // tạo màu sắc cho chữ
            Paint textPaint = new Paint();
            textPaint.setARGB(255, 255, 255, 255);
            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTextSize(20);
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);


            text = "Mã KH - Tên KH : " + editCustomerCode.getText().toString() + " - "
                    + editCustomerName.getText().toString() + "SDT : " + editCustomerPhoneNumber.getText().toString() + "Ngày chụp: " + getStrDateTakesPhoto();
            // tạo background
            canvas.drawText("Mã KH - Tên KH : " + editCustomerCode.getText().toString() + "-"
                    + editCustomerName.getText().toString(), 10, 20, strokePaint);
            canvas.drawText("SDT : " + editCustomerPhoneNumber.getText().toString(), 10, 50, strokePaint);
            canvas.drawText("Vị trí : " + tvGeoCode.getText().toString(), 10, 80, strokePaint);
            canvas.drawText("Địa chỉ : " + editCustomerAddress.getText().toString(), 10, 110, strokePaint);

            canvas.drawText("Ngày chụp: " + obj.getStrDateTakesPhoto(), 10,
                    140, strokePaint);
            // lưu hình ảnh sau khi đã ghi thông tin
            SaveImage(alteredBitmap, obj.getPhotoPath());
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

    public void InsertUpdateInfoCustomer() {

        try {
            Log.d("customer_code", "lỗi : ");
            if (vCustomer != null) {
                String code = editCustomerCode.getText().toString();
                String address = editCustomerAddress.getText().toString();
                String phoneNumber = editCustomerPhoneNumber.getText().toString();
                String name = editCustomerName.getText().toString();
                vCustomer.setCustomerCode(code);
                vCustomer.setCustomerName(name);
                vCustomer.setCustomerPhoneNumber(phoneNumber);
                vCustomer.setCustomerAddress(address);
                vCustomer.setCustomerLocation(tvGeoCode.getText().toString());
                vCustomer.setCustomerAllowSync(true);
                vCustomer.setCustomerCategory("0");
                vCustomer.setCustomerRoute("0");
                vCustomer.setCustomerInfo1("0");
                vCustomer.setCustomerInfo2("0");
                vCustomer.setCustomerInfo3("0");
                vCustomer.setCustomerInfo4("0");
                vCustomer.setCustomerInfo5("0");
                vCustomer.setCustomerLocationAddress("0");
                vCustomer.setCustomerLocationAccuracy("");
                vCustomer.setCustomerActive(true);
                SimpleDateFormat sdf = new SimpleDateFormat(
                        global.getFormatDate());
                String currentDateandTime = sdf
                        .format(new Date());
                vCustomer.setCustomerCreatedDate(currentDateandTime);

                DatabaseHelper.getInstance().updateCustomerAddNew(vCustomer, vCustomer.getCustomerCode());
                Toast.makeText(this, R.string.SaveCustomer, Toast.LENGTH_SHORT).show();

            } else {
                customer = new CCustomer(); //khỏi tạo đối tượng KH với các thông tin đc nhập

//                String customerOldCode = customer
//                        .getCustomerCode();
                String code = editCustomerCode.getText().toString();
                String address = editCustomerAddress.getText().toString();
                String phoneNumber = editCustomerPhoneNumber.getText().toString();
                String name = editCustomerName.getText().toString();


                customer.setCustomerCode(code);
                customer.setCustomerName(name);
                customer.setCustomerPhoneNumber(phoneNumber);
                customer.setCustomerAddress(address);
                Log.d("customer_code", "lỗi1 : " + customer);
                customer.setCustomerLocation(tvGeoCode.getText().toString());
                customer.setCustomerLocationAddress("0");
                customer.setCustomerLocationAccuracy("0");
                customer.setCustomerActive(true);
                customer.setCustomerCategory("0");
                customer.setCustomerInfo1("0");
                customer.setCustomerInfo2("0");
                customer.setCustomerInfo3("0");
                customer.setCustomerInfo4("0");
                customer.setCustomerInfo5("0");
                customer.setCustomerRoute("0");
                customer.setCustomerAllowSync(true);

                SimpleDateFormat sdf = new SimpleDateFormat(
                        global.getFormatDate());
                String currentDateandTime = sdf
                        .format(new Date());
                customer.setCustomerCreatedDate(currentDateandTime);
                Log.d("customer_code", "lỗi2 : " + customer);

                long k = DatabaseHelper.getInstance().createCustomerNew(customer);

                if (k != -1) {

                    Toast.makeText(
                            getApplicationContext(),
                            getResources()
                                    .getString(
                                            R.string.msgInsertSuccess),
                            Toast.LENGTH_SHORT)
                            .show();

                    Intent returnIntent = new Intent();
                    returnIntent
                            .putExtra(
                                    "CustomerCode",
                                    customer.getCustomerCode());
                    setResult(RESULT_OK,
                            returnIntent);
                    //   finish();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            getResources()
                                    .getString(
                                            R.string.msgInsertFaild),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();

        }
    }

    //hàm kiểm tra xem các thông tin người dùng nhập vào có hợp lệ hay không
    private boolean isValid() {

        try {
            if (editCustomerCode.getText().toString().trim().length() <= 0) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.msgCustomerCode),
                        Toast.LENGTH_SHORT).show();
                editCustomerCode.requestFocus();
                return false;
            }


            if (editCustomerName.getText().toString().trim().length() <= 0) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.msgCustomerName),
                        Toast.LENGTH_SHORT).show();
                editCustomerName.requestFocus();
                return false;
            }

            if (editCustomerAddress.getText().toString().trim().length() <= 0) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.msgCustomerAddress),
                        Toast.LENGTH_SHORT).show();
                editCustomerAddress.requestFocus();
                return false;
            }

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    // giảm kích thước hình ảnh để ko làm nặng hệ thống
                    new CmnFns().downSize(this.photoFile.getAbsolutePath(),
                            global.getMaxinumKiloByteOfPicture());
                } catch (Exception e) {
                    // TODO: handle exception
                    // CmnFns.writeLogError("FPhoto DownSize " + e.getMessage());
                }
                bitmap = BitmapFactory.decodeFile(this.photoFile
                        .getAbsolutePath());

                long photoCD;

                CPhoto file = new CPhoto();
                file.setDateTakesPhoto(new Date());
                file.setPhotoName(this.photoFile.getName());
                file.setPhotoPath(this.photoFile.getAbsolutePath());
                file.setImage(bitmap);
                // file.setGeoCodeAccuracy(getCodeAccuracy);
                GetInfoCustomer();
                file.setGeoCode(geocode);
                photoCD = DatabaseHelper.getInstance().createTakesPhoto(
                        customer, file);

                file.setPhotoCD(photoCD); // lưu key sau khi insert vào

                setTextForImage(file);

                File image = new File(file.getPhotoPath()); // lấy hình ảnh từ đường dẫn
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap1 = BitmapFactory.decodeFile(image.getAbsolutePath(),
                        bmOptions);
                arrImage.add(bitmap1);
                prepareData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getStrDateTakesPhoto() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        return df.format(new Date());
    }

    private void GetDataFromIntent() {
        if (customerEventbus != null) {
            vCustomer = customerEventbus.getcCustomer();
            String code = customerEventbus.getcCustomer().getCustomerCode();
            String name = customerEventbus.getcCustomer().getCustomerName();

            String phone = customerEventbus.getcCustomer().getCustomerPhoneNumber();

            String address = customerEventbus.getcCustomer().getCustomerAddress();

            String geocode = customerEventbus.getcCustomer().getCustomerLocation();

            editCustomerCode.setText(code);
            editCustomerName.setText(name);
            editCustomerPhoneNumber.setText(phone);
            editCustomerAddress.setText(address);
            tvGeoCode.setText(geocode);
            ArrayList<CPhoto> cPhotos = (ArrayList<CPhoto>) DatabaseHelper.getInstance().getAllTakesPhotos();
            if(cPhotos.size() > 0){
                for(int i = 0; i < cPhotos.size(); i++){
                    CPhoto cPhoto = cPhotos.get(i);
                    File checkFile = new File(cPhoto.getPhotoPath());
                    if (!checkFile.exists()) {
                        DatabaseHelper.getInstance().deletePhoto(
                                cPhoto.getPhotoCD());
                        try {
                            checkFile.delete(); // clear file
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        continue;
                    }
                    String file = cPhoto.getPhotoPath();
                    String nameFile = cPhoto.getPhotoName();
                    if(nameFile.contains("AddNew_"+editCustomerCode.getText())) {
                        File image = new File(file); // lấy hình ảnh từ đường dẫn
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap1 = BitmapFactory.decodeFile(image.getAbsolutePath(),
                                bmOptions);
                        arrImage.add(bitmap1);
                        // Toast.makeText(FCustomerAddNewEdit.this, cPhoto.getImage()+"", Toast.LENGTH_SHORT).show();
                        prepareData();
                    }
                }
            }else{
                //Toast.makeText(FCustomerAddNewEdit.this, "Ảnh đã được đồng bộ", Toast.LENGTH_SHORT).show();
                tvEmptyImage.setVisibility(View.VISIBLE);

            }
            EventBus.getDefault().removeStickyEvent(customerEventbus);
        }
    }


    private void CheckEditText() {
        if (editCustomerName.getText().toString().matches("")
                || editCustomerCode.getText().toString().matches("")
                || editCustomerPhoneNumber.getText().toString().matches("")
                || arrImage.size() != 0) {
            editCustomerName.setText(name);
            editCustomerCode.setText(code);
            editCustomerAddress.setText(address);
            editCustomerPhoneNumber.setText(phoneNumber);
            tvGeoCode.setText(geocode);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        customerEventbus = EventBus.getDefault().getStickyEvent(CustomerEventbus.class);
        CheckEditText();
        GetDataFromIntent();

    }

    @Override
    public void onBackPressed() {
        if (layout.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else {

        }

    }

    class Uploading extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(FCustomerAddNewEdit.this);

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

                new CmnFns().synchronizeCustomerAddNew(FCustomerAddNewEdit.this);

                // progressDialog.setProgress(70);
                new CmnFns().synchronizePhotos(FCustomerAddNewEdit.this);
                //  progressDialog.setProgress(100);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(CmnFns.isNetworkAvailable()) {
                Toast.makeText(FCustomerAddNewEdit.this, R.string.UploadResult, Toast.LENGTH_SHORT).show();
            }else{
                LayoutInflater factory = LayoutInflater.from(FCustomerAddNewEdit.this);
                View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
                final AlertDialog dialog = new AlertDialog.Builder(FCustomerAddNewEdit.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
                InsetDrawable inset = new InsetDrawable(back, 64);
                dialog.getWindow().setBackgroundDrawable(inset);
                dialog.setView(layout_cus);

                Button btnClose = layout_cus.findViewById(R.id.btnHuy);
                TextView textView = layout_cus.findViewById(R.id.tvText);


                textView.setText("Vui lòng bật kết nối mạng");
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }
}
