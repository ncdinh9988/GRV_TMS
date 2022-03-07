package com.FiveSGroup.TMS.ChangeCusFragment;

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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.FiveSGroup.TMS.AddCustomerFragment.GpsTracker;
import com.FiveSGroup.TMS.AddCustomerFragment.ImageAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.app.Activity.RESULT_CANCELED;

public class ChangeCusFragment extends Fragment {


    private UpdateCustomer cCustomer;


    private EditText edtCustomerCode, edtCustomerName, edtCustomerAddress, edtCustomerPhone;
    CustCodeEventbus eventbus;

    String custCode;
    private Bitmap bitmap;
    UpdateCustomer customer;
    //CCustomer ccCustomer;
    ImageView imgBtnGPS, capture;
    TextView tvgeocode, tvEmptyImage;
    ProgressDialog progressDialog;

    String code, address, phoneNumber, name, geocode;
    Button btnupload, btnupdate, btnHome;
    static String customercd;
    private ArrayList<Bitmap> arrImage;
    private static final int REQUEST_TAKE_PHOTO = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_layout_update, null);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        imgBtnGPS = view.findViewById(R.id.imgBtnGPS);
        tvgeocode = view.findViewById(R.id.tvGeoCode);

        btnupload = view.findViewById(R.id.btnUpLoad);
        btnupdate = view.findViewById(R.id.btnUpdate);
        btnHome = view.findViewById(R.id.btnHome);

        edtCustomerCode = view.findViewById(R.id.editCustomerCode);
        edtCustomerCode.setFocusable(false);
        edtCustomerName = view.findViewById(R.id.editCustomerName);
        edtCustomerAddress = view.findViewById(R.id.editCustomerAddress);
        edtCustomerPhone = view.findViewById(R.id.editCustomerPhoneNumber);
        capture = view.findViewById(R.id.capture_bill);
        tvEmptyImage = view.findViewById(R.id.tvEmptyImage);
        imgDisplay = view.findViewById(R.id.imgDisplay);
        imgClose = view.findViewById(R.id.imgClose);
        frameLayout =view.findViewById(R.id.frameLayout);
        layout = view.findViewById(R.id.layout_main);
        rvListImage = view.findViewById(R.id.rv_list_image);
        arrImage = new ArrayList<>();
        getCustomers();
        //getCustomerFromDatabase();

        imgBtnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GpsTracker gpsTracker = new GpsTracker(getActivity());
                if (gpsTracker.canGetLocation()) {
                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();
                    tvgeocode.setText(String.valueOf(latitude) + "," + String.valueOf(longitude));
                    //getCompleteAddressString(latitude,longitude);
                } else {
                    gpsTracker.showSettingsAlert();
                }
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                String code = edtCustomerCode.getText().toString();
                String name = edtCustomerName.getText().toString();
                String address = edtCustomerAddress.getText().toString();
                String phoneNumber = edtCustomerPhone.getText().toString();
                String geocode = tvgeocode.getText().toString();
                if(customer != null) {
                    customer.setCustomerCode(code);
                    customer.setCustomerName(name);
                    customer.setCustomerPhone(phoneNumber);
                    customer.setCustomerAddress(address);
                    customer.setCustomerGeocode(geocode);
                    customer.setCustomerCd(customercd);
                    int i = DatabaseHelper.getInstance().updateChangeCustomer(customer, customer.getCustomerCd());
                }else{
                    customer = new UpdateCustomer();
                    customer.setCustomerCode(code);
                    customer.setCustomerName(name);
                    customer.setCustomerPhone(phoneNumber);
                    customer.setCustomerAddress(address);
                    customer.setCustomerGeocode(geocode);
                    customer.setCustomerCd(customercd);
                    int i = DatabaseHelper.getInstance().updateChangeCustomer(customer, customer.getCustomerCd());

                }
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CmnFns.isNetworkAvailable()) {
                       // Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        Uploading uploading = new Uploading();
                        uploading.execute();
                }else{
                    LayoutInflater factory = LayoutInflater.from(getActivity());
                    View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
                    final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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

            }
        });


        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }


    File photoFile = null;

    // hàm gọi camera để chụp hình ảnh
    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go

                this.photoFile = createImageFile(); // tạo file hình ảnh trước
                // khi chụp với định dạng
                // chỉ định
                // Continue only if the File was successfully created
                if (this.photoFile != null) {
                    //Kiểm tra SDK version thiết bị để tạo file hình ảnh
                    if (Build.VERSION.SDK_INT >= 24) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                FileProvider.getUriForFile(getActivity(),
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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

                //  file.setGeoCode(geoCode);
                // file.setGeoCodeAccuracy(getCodeAccuracy);
                GetInfoCustomer();
                photoCD = DatabaseHelper.getInstance().createChangeCustTakesPhoto(
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

    ImageAdapter adapter;
    LinearLayout layout;
    FrameLayout frameLayout;
    private ImageView imgDisplay, imgClose;
    RecyclerView rvListImage;

    private void prepareData() {

        if (arrImage.size() > 0) {
            tvEmptyImage.setVisibility(View.GONE);
            adapter = new ImageAdapter(getActivity(), arrImage, frameLayout, layout, imgDisplay, imgClose);
            GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 3);
            rvListImage.setLayoutManager(linearLayoutManager);
            rvListImage.setAdapter(adapter);
        }
    }

    public String getStrDateTakesPhoto() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        return df.format(new Date());
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


            text = "Mã KH - Tên KH : " + edtCustomerCode.getText().toString() + " - "
                    + edtCustomerName.getText().toString() + "SDT : " + edtCustomerPhone.getText().toString() + "Ngày chụp: " + getStrDateTakesPhoto();
            // tạo background
            canvas.drawText("Mã KH - Tên KH : " + edtCustomerCode.getText().toString() + "-"
                    + edtCustomerName.getText().toString(), 10, 20, strokePaint);
            canvas.drawText("SDT : " + edtCustomerPhone.getText().toString(), 10, 50, strokePaint);
            canvas.drawText("Vị trí : " + tvgeocode.getText().toString(), 10, 80, strokePaint);
            canvas.drawText("Địa chỉ : " + edtCustomerAddress.getText().toString(), 10, 110, strokePaint);

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

    // hàm này để set các giá trị vào hình ảnh
    private void GetInfoCustomer() {
        customer = new UpdateCustomer();
        code = edtCustomerCode.getText().toString();
        address = edtCustomerAddress.getText().toString();
        phoneNumber = edtCustomerPhone.getText().toString();
        name = edtCustomerName.getText().toString();
        geocode = tvgeocode.getText().toString();

        customer.setCustomerCode(code);
        customer.setCustomerName(name);
        customer.setCustomerPhone(phoneNumber);
        customer.setCustomerAddress(address);
        customer.setCustomerGeocode(geocode);

        SimpleDateFormat sdf = new SimpleDateFormat(
                global.getFormatDate());
        String currentDateandTime = sdf
                .format(new Date());
        //customer.setCustomerCreatedDate(currentDateandTime);
    }

    private File createImageFile() {

        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            String imageFileName = "EditCustomer_"+edtCustomerCode.getText().toString() + "_" + timeStamp + ".png";


            File file = null;
            File image = null;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                File storageDir = global.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                image = File.createTempFile(imageFileName, /* prefix */
                        ".jpg", /* suffix */
                        storageDir /* directory */
                );
            }else {
                image = CreateFile(getActivity().getString(R.string.PathFolderPicture), imageFileName, file);
            }

            // lưu thông tin hình ảnh đường dẫn để lưu vào DB sau khi người dùng
            // chụp ảnh thành công

            return image;
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
            // TODO: handle exception
            e.printStackTrace();
            // CmnFns.writeLogError("FPhoto createImageFile " + e.getMessage());
            return null;
        }

    }

    public File CreateFile(String folderName, String filename, File filepath) {
        try {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            return filepath;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void getCustomers() {
        eventbus = EventBus.getDefault().getStickyEvent(CustCodeEventbus.class);
        if (eventbus != null) {
            custCode = eventbus.getCustCode();
            edtCustomerCode.setText(custCode);
            int statusGetCust = new CmnFns().synchronizeGetCustomers(getActivity(), custCode, "");

            if (statusGetCust == 1) {
                ArrayList<UpdateCustomer> arrCustomers = DatabaseHelper.getInstance().getAllChangeCustomer();
                if (arrCustomers.size() > 0) {
                    for (int i = 0; i < arrCustomers.size(); i++) {
                        if (!custCode.equals("")) {
                            if (custCode.equals(arrCustomers.get(i).getCustomerCd())) {
                                customer = new UpdateCustomer();
                                customer.setCustomerCd(arrCustomers.get(i).getCustomerCd());
                                customer.setCustomerCode(arrCustomers.get(i).getCustomerCode());
                                customer.setCustomerName(arrCustomers.get(i).getCustomerName());
                                customer.setCustomerAddress(arrCustomers.get(i).getCustomerAddress());
                                customer.setCustomerPhone(arrCustomers.get(i).getCustomerPhone());
                                tvgeocode.setText(arrCustomers.get(i).getCustomerGeocode());
                                customercd = global.getCustomercd();
                                edtCustomerName.setText(customer.getCustomerName());
                                edtCustomerCode.setText(customer.getCustomerCode());
                                edtCustomerAddress.setText(customer.getCustomerAddress());
                                edtCustomerPhone.setText(customer.getCustomerPhone());
                            }
                        }
                    }


                }
                ArrayList<CPhoto> cPhotos = (ArrayList<CPhoto>) DatabaseHelper.getInstance().getAllChangeCustTakesPhotos();
                if(cPhotos.size() > 0){
                    for(int i = 0; i < cPhotos.size(); i++){
                        CPhoto cPhoto = cPhotos.get(i);
                        File checkFile = new File(cPhoto.getPhotoPath());
                        if (!checkFile.exists()) {
                            DatabaseHelper.getInstance().deleteChangeCustPhoto(
                                    cPhoto.getPhotoName());
                            try {
                                checkFile.delete(); // clear file
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            continue;
                        }
                        String file = cPhoto.getPhotoPath();
                        String nameFile = cPhoto.getPhotoName();
                        if(nameFile.contains("EditCustomer_"+edtCustomerCode.getText())) {
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
                    //Toast.makeText(getActivity(), "Ảnh đã được đồng bộ", Toast.LENGTH_SHORT).show();
                    tvEmptyImage.setVisibility(View.VISIBLE);
                }
            } else {
                tvEmptyImage.setVisibility(View.VISIBLE);
            }
            EventBus.getDefault().removeStickyEvent(eventbus);
        }
    }

    class Uploading extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());

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

            new CmnFns().synchronizeChangeCustomer(getActivity());

            new CmnFns().synchronizeChangeCustPhotos(getActivity());
            //  progressDialog.setProgress(100);

//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getActivity(), "Đồng bộ thành công", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }

    }
}
