package com.FiveSGroup.TMS.TakePhotoFragment;

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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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

import com.FiveSGroup.TMS.AddCustomerFragment.CCustomer;
import com.FiveSGroup.TMS.BuildConfig;
import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.HomeActivity;
import com.FiveSGroup.TMS.OrderPhoto;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.TowingContainers.TakePhoto_Containers;
import com.FiveSGroup.TMS.global;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;

public class CaptureFragment extends Fragment {

    Button btnCap, btnBack, btnUpload;
    ImageView imgViewOrder, imgClose, imgDisplay;
    RecyclerView rvListImageOrder;

    File photoFile = null;
    String imageFileName;
    LinearLayout linearLayout;
    FrameLayout frameLayout;
    TextView tvEmptyImage;

    private static final int REQUEST_TAKE_PHOTO = 2;

    GetOrderCDEventbus getOrderCDEventbus = EventBus.getDefault().getStickyEvent(GetOrderCDEventbus.class);
    String codeOrder;
    private Bitmap bitmap, bitmapCanvas;

    private ArrayList<Bitmap> arrImage;

    private ImageOrderAdapter imageOrderAdapter;
    private CCustomer cCustomer;
    View view;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.capture_fragment, null);
        btnCap = view.findViewById(R.id.btnCap);
        btnBack = view.findViewById(R.id.btnBack);
        imgViewOrder = view.findViewById(R.id.imagViewOrder);
        linearLayout = view.findViewById(R.id.layout_capture);
        frameLayout = view.findViewById(R.id.frameLayout);
        imgClose = view.findViewById(R.id.imgClose);
        imgDisplay = view.findViewById(R.id.imgDisplay);
        rvListImageOrder = view.findViewById(R.id.rv_list_image_order);
        tvEmptyImage = view.findViewById(R.id.tvEmptyImage);
        btnUpload = view.findViewById(R.id.btnUpload);
        arrImage = new ArrayList<>();
        btnCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToHomeScreen();
            }
        });
        if (getOrderCDEventbus != null) {
            codeOrder = getOrderCDEventbus.getOrderCD();
            EventBus.getDefault().removeStickyEvent(getOrderCDEventbus);
        }
        getImages();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CmnFns.isNetworkAvailable()) {
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
        });
        prepareData();
//        imgViewOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                linearLayout.setVisibility(View.GONE);
//                frameLayout.setVisibility(View.VISIBLE);
//                imgDisplay.setImageBitmap(bitmapCanvas);
//                imgDisplay.setScaleType(ImageView.ScaleType.FIT_XY);
//                imgClose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        frameLayout.setVisibility(View.GONE);
//                        linearLayout.setVisibility(View.VISIBLE);
//                    }
//                });
//            }
//        });
        return view;

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


            new CmnFns().synchronizePhotoForOrders(getActivity());
            //  progressDialog.setProgress(100);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getActivity(), "?????ng b??? th??nh c??ng", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }



    private void backToHomeScreen() {
        Intent intentHome = new Intent(getActivity(), HomeActivity.class);
        startActivity(intentHome);
    }

    // h??m n??y ????? set c??c gi?? tr??? v??o h??nh ???nh
    private void GetInfoCustomer() {
        cCustomer = new CCustomer();
        String nameCustomer = "";
        String address = "";
        String geocode = "";
        String phoneNumber = "";

        cCustomer.setCustomerName("");
        cCustomer.setCustomerAddress("");
        cCustomer.setCustomerLocation("");
        cCustomer.setCustomerPhoneNumber("");
        SimpleDateFormat sdf = new SimpleDateFormat(
                global.getFormatDate());
        String currentDateandTime = sdf
                .format(new Date());
        cCustomer.setCustomerCreatedDate(currentDateandTime);
    }


    private void prepareData() {
        if (arrImage.size() > 0) {
            tvEmptyImage.setVisibility(View.GONE);
            imageOrderAdapter = new ImageOrderAdapter(getActivity(), arrImage, frameLayout, linearLayout, imgDisplay, imgClose);
            GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 3);
            rvListImageOrder.setLayoutManager(linearLayoutManager);
            rvListImageOrder.setAdapter(imageOrderAdapter);
        } else {
            tvEmptyImage.setVisibility(View.VISIBLE);
        }
    }

    // h??m g???i camera ????? ch???p h??nh ???nh
    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go

                this.photoFile = createImageFile(); // t???o file h??nh ???nh tr?????c
                // khi ch???p v???i ?????nh d???ng
                // ch??? ?????nh
                // Continue only if the File was successfully created
                if (this.photoFile != null) {
                    //Ki???m tra SDK version thi???t b??? ????? t???o file h??nh ???nh
                    if (Build.VERSION.SDK_INT >= 24) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                FileProvider.getUriForFile(getActivity(),
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

    private File createImageFile() {

        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            imageFileName = "OrderPhoto_"  + codeOrder + "_" + timeStamp + ".png";

//            String path = Environment.getExternalStorageDirectory()
//                    + File.separator
//                    + getApplicationContext().getString(
//                    R.string.PathFolderPicture); // folder


            File file = null;
            File image = null;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                File storageDir = global.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                image = File.createTempFile(imageFileName, /* prefix */
                        ".jpg", /* suffix */
                        storageDir /* directory */
                );
            }else {
                image = CreateFile(getActivity().getApplicationContext().getString(R.string.PathFolderSalePicture),
                        imageFileName, file);
            }

            // l??u th??ng tin h??nh ???nh ???????ng d???n ????? l??u v??o DB sau khi ng?????i d??ng
            // ch???p ???nh th??nh c??ng

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
            strokePaint.setColor(getActivity().getResources().getColor(R.color.white));
            strokePaint.setTextAlign(Paint.Align.LEFT);
            strokePaint.setTextSize(20);
            strokePaint.setTypeface(Typeface.DEFAULT_BOLD);
            strokePaint.setStyle(Paint.Style.FILL);
            strokePaint.setStrokeWidth(20f);

            // t???o m??u s???c cho ch???
            Paint textPaint = new Paint();
            // textPaint.setARGB(255, 255, 255, 255);
            textPaint.setColor(getActivity().getResources().getColor(R.color.blue2));

            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTextSize(20);
            textPaint.setTypeface(Typeface.DEFAULT);

            canvas.drawText("T??n KH : " + cCustomer.getCustomerName()  , 10, 20 , strokePaint);
            canvas.drawText("?????a ch??? : " + cCustomer.getCustomerLocation(), 10, 50 , strokePaint);
            canvas.drawText("S??? ??T : " + cCustomer.getCustomerLocation() , 10, 80 , strokePaint);
            canvas.drawText("T??n file ???nh : " +  imageFileName + " .png", 10, 110, strokePaint);
            canvas.drawText("Ng??y ch???p: " + obj.getStrDateTakesPhoto(), 10, 140, strokePaint);
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

//                CPhoto file = new CPhoto();
//                file.setPhoto_Name(this.photoFile.getName());
//                file.setPhoto_Path(this.photoFile.getAbsolutePath());
//                file.setDateTakesPhoto(new Date());
//                file.setImage(bitmap);


                //  file.setGeoCode(geoCode);
                // file.setGeoCodeAccuracy(getCodeAccuracy);
                // GetInfoCustomer();
                GetInfoCustomer();
                photoCD = DatabaseHelper.getInstance().createdSaleTakesPhoto(codeOrder, file , "" , "","","", "");

                file.setPhotoCD(photoCD); // l??u key sau khi insert v??o

                setTextForImage(file);
                File image = new File(file.getPhoto_Path()); // l???y h??nh ???nh t??? ???????ng d???n
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap1 = BitmapFactory.decodeFile(image.getAbsolutePath(),
                        bmOptions);
                arrImage.add(bitmap1);
                prepareData();
                //   this.setPic(this.photoFile.getAbsolutePath()); // hi???n th??? thumb


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    private void getImages(){
        ArrayList<OrderPhoto> cPhotos = (ArrayList<OrderPhoto>) DatabaseHelper.getInstance().getAllPhotoForOrders();
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
                if(nameFile.contains("OrderPhoto_"+codeOrder)) {
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
    }

}
