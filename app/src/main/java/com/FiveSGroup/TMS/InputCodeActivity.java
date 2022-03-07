
package com.FiveSGroup.TMS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.Warehouse.HomeQRActivity;
import com.FiveSGroup.TMS.Webservice.WebserviceAuth;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InputCodeActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextInputCode;
    Button buttonSaveCode;
    TextView tvDeviceId;
    ProgressDialog progressDialogCheckExistCode;
    File f;
    String sale_code;
    private String code = "";
    private CheckBox ckShipper, ckAdmin;
    private TextView textViewVersion;

    private String PERMISSION_READ_WRITE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_code);
        //  dialogCheckPermission();

        RequesetPermission();

        Init();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //CmnFns.setAuth();
        String version;
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            version = pInfo.versionName;
            textViewVersion.setText("Version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


//        if (ActivityCompat.shouldShowRequestPermissionRationale(InputCodeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                || ActivityCompat.shouldShowRequestPermissionRationale(InputCodeActivity.this, Manifest.permission.READ_PHONE_STATE)) {
//            ActivityCompat.requestPermissions(InputCodeActivity.this, new String[]{
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_PHONE_STATE
//            }, 1);
//        }
//        //Test chơi


        setCheckBox();
        RegisterListener();

    }

    private void RegisterListener() {
        buttonSaveCode.setOnClickListener(this);
    }

    private void Init() {
        textViewVersion = findViewById(R.id.textViewVersionApp);
        editTextInputCode = findViewById(R.id.editTextInputCode);
        buttonSaveCode = findViewById(R.id.buttonSaveCode);
        ckShipper = findViewById(R.id.ckshipper);
        ckAdmin = findViewById(R.id.ckadmin);
        tvDeviceId = findViewById(R.id.tvDeviceId);
        tvDeviceId.setText("Device Id : " + CmnFns.get_deviceID(this));
    }

    private void setCheckBox() {
        ckShipper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ckAdmin.setChecked(false);
                }

            }
        });
        ckAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ckShipper.setChecked(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSaveCode:
                // lần đầu nhập mã và nhấn checkbox
                if (CmnFns.isNetworkAvailable()) {
                     if (ckShipper.isChecked() == true) {
                        CheckEmptyCode();
                    } else if (ckAdmin.isChecked() == true) {
                        CheckCodeAdmin();
                    } else {
                        Toast.makeText(this, "Vui lòng chọn chế độ truy cập", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    LayoutInflater factory = LayoutInflater.from(this);
                    View layout_cus = factory.inflate(R.layout.layout_show_check_wifi, null);
                    final AlertDialog dialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_MinWidth).create();
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
                break;
        }
    }

    private void CheckEmptyCode() {
        String fileName = "fsys_tms.txt";
        String folderName = "TMS";
        code = editTextInputCode.getText().toString().trim();

        if (code.isEmpty()) {

            Toast.makeText(this, "Vui lòng nhập mã nhân viên để truy cập!", Toast.LENGTH_SHORT).show();
        } else {

            InitCodeFile(fileName, folderName, code);
            readData1(f, 0);

        }
    }

    private void CheckCodeAdmin() {
        String fileName = "fsys_tms_admin.txt";
        String folderName = "TMS";
        code = editTextInputCode.getText().toString().trim();

        if (code.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã admin để truy cập!", Toast.LENGTH_SHORT).show();
        } else {

            InitCodeFile(fileName, folderName, code);
            readData1(f, 1);

        }
    }


    public void createfilenew(String folder, String file, String content){


        ContentValues valuess = new ContentValues();
        valuess.put(MediaStore.MediaColumns.DISPLAY_NAME, file); //file name
        valuess.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            valuess.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/" + folder + "/");
        }

        Uri contentUri = MediaStore.Files.getContentUri("external");

        String selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?";

        String[] selectionArgs = new String[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            selectionArgs = new String[]{Environment.DIRECTORY_DOCUMENTS + "/"+folder+"/"};
        }

        Cursor cursor = global.getAppContext().getContentResolver().query(contentUri, null, selection, selectionArgs, null);

        Uri uri = null;

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String fileNames = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));

            if (fileNames.equals(file)) {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID));

                uri = ContentUris.withAppendedId(contentUri, id);

                break;
            }
        }

        if (uri == null) {
            try {

                OutputStream outputStream = global.getAppContext().getContentResolver().openOutputStream(createfilesystem(folder,file,"text/plain"));
                outputStream.write(content.getBytes());
                outputStream.close();

            } catch (IOException e) {
                e.toString();

            }
        } else {
            try {

                OutputStream outputStream = global.getAppContext().getContentResolver().openOutputStream(uri);
                outputStream.write(content.getBytes());
                outputStream.close();

            } catch (IOException e) {
                e.toString();

            }
        }
    }

    public Uri createfilesystem(String folder,String file, String type ){
        Uri uri;
        ContentValues values = new ContentValues();

        values.put(MediaStore.MediaColumns.DISPLAY_NAME, file);       //file name
        values.put(MediaStore.MediaColumns.MIME_TYPE, type);        //file extension, will automatically add to file
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/"+ folder +"/");     //end "/" is not mandatory
        }

        // file not exist, insert
        uri = global.getAppContext().getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);      //important!
        return uri;
    }




    private void InitCodeFile(String fileName, String folderName, String code) {

//        String rootPath = Environment.getExternalStorageDirectory()
//                .getAbsolutePath() + "/" + folderName + "/";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            String rootPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + folderName + "/";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                createfilenew(folderName,fileName,code);
                f = new File(folderName + "/" + fileName);
            }else{
                File root = new File(rootPath);
                if (!root.exists()) {
                    root.mkdirs();
                }
                f = new File(rootPath + fileName);
                if (!f.exists()) {
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    out.write(code.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        } else {
            // Request permission from the user

        }

        try {
            String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/";
            File root = new File(rootPath);
            if (!root.exists()) {
                root.mkdirs();
            }
            File f = new File(rootPath + fileName);
            if (f.exists()) {
                f.delete();
            }

            FileOutputStream out = new FileOutputStream(f);
            out.write(code.getBytes());
        } catch (Exception e) {
            Log.d("exc", e.toString());
        }


    }

    private class CheckExistCode extends AsyncTask<Void, Boolean, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogCheckExistCode = new ProgressDialog(InputCodeActivity.this);
            progressDialogCheckExistCode.setMessage("Vui lòng chờ...");
            progressDialogCheckExistCode.setCancelable(false);
            progressDialogCheckExistCode.setCanceledOnTouchOutside(false);
            progressDialogCheckExistCode.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String fileName = "fsys_tms.txt";
            String folderName = "TMS";
            boolean isEmpty = false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                File filePathShipperNew  = new File(folderName + "/" + fileName);

                String texxt = readFileSystemNew(filePathShipperNew);

                SharedPreferences sharedPref = getSharedPreferences("name", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                if (texxt == null || texxt.equals("")) {
                    isEmpty = false;
                    return isEmpty;
                }else{
                    editor.putString("code", "111");
                    editor.commit();

                    isEmpty = true;
                    readData1(filePathShipperNew, 0);
                    return isEmpty;

                }
            }else{
                // folder cho shipper

                String folderPath = Environment.getExternalStorageDirectory()
                        + File.separator + folderName; // folder name
                String filePath = folderPath + "/" + fileName;
                File mFilePath = new File(folderPath);
                File mFolderPath = new File(filePath);

                SharedPreferences sharedPref = getSharedPreferences("name", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //kiểm tra folder "TMS" đã tồn tại chưa
                if (mFolderPath.exists()) {
                    //kiểm tra file "fsys_tms.txt" đã tồn tại chưa
                    if (mFilePath.exists()) {
                        editor.putString("code", "111");
                        editor.commit();

                        isEmpty = true;
                        readData1(mFolderPath, 0);
                        return isEmpty;
                    } else {
                        isEmpty = false;
                        return isEmpty;
                    }

                }
            }
            return isEmpty;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                SharedPreferences sharedPref = getSharedPreferences("name", Context.MODE_PRIVATE);
                String value = sharedPref.getString("code", "");
                progressDialogCheckExistCode.cancel();
                if (value.equals("111")) {
//                    Intent intentHomeActivity = new Intent(getApplicationContext(), HomeActivity.class);
//                    startActivity(intentHomeActivity);
//                    finish();
                } else if
                (value.equals("222")) {
//                    Intent intentHomeActivity = new Intent(getApplicationContext(), WarehouseActivity.class);
//                    startActivity(intentHomeActivity);
//                    finish();
                }
            } else {
                progressDialogCheckExistCode.cancel();
                Toast.makeText(InputCodeActivity.this, "Vui lòng nhập mã nhân viên", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CheckExistAdminCode extends AsyncTask<Void, Boolean, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogCheckExistCode = new ProgressDialog(InputCodeActivity.this);
            progressDialogCheckExistCode.setMessage("Vui lòng chờ...");
            progressDialogCheckExistCode.setCancelable(false);
            progressDialogCheckExistCode.setCanceledOnTouchOutside(false);
            progressDialogCheckExistCode.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String fileAdmin = "fsys_tms_admin.txt";
            String folderAdmin = "TMS";
            boolean isEmpty = false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                File filePathAdminNew  = new File(folderAdmin + "/" + fileAdmin);

                String texxt = readFileSystemNew(filePathAdminNew);
                SharedPreferences sharedPref = getSharedPreferences("name", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                //kiểm tra folder "TMS" đã tồn tại chưa
                if (texxt == null || texxt.equals("")) {
                    isEmpty = false;
                    return isEmpty;
                }else{
                    editor.putString("code", "222");
                    editor.commit();
                    isEmpty = true;
                    readData1(filePathAdminNew, 1);
                }
            }else {

                // folder cho admin
                String folderPathAdmin = Environment.getExternalStorageDirectory()
                        + File.separator + folderAdmin; // folder name
                String filePathAdmin = folderPathAdmin + "/" + fileAdmin;
                File mFilePathAdmin = new File(folderPathAdmin);
                File mFolderPathAdmin = new File(filePathAdmin);
                SharedPreferences sharedPref = getSharedPreferences("name", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mFolderPathAdmin.exists()) {
                    //kiểm tra file "fsys_tms_admin.txt" đã tồn tại chưa
                    if (mFilePathAdmin.exists()) {
                        editor.putString("code", "222");
                        editor.commit();
                        isEmpty = true;
                        readData1(mFolderPathAdmin, 1);
                        // return isEmpty;
                    } else {
                        isEmpty = false;
                        //return isEmpty;
                    }

                }
            }

            return isEmpty;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                SharedPreferences sharedPref = getSharedPreferences("name", Context.MODE_PRIVATE);
                String value = sharedPref.getString("code", "");
                progressDialogCheckExistCode.cancel();
                if (value.equals("111")) {
//                    Intent intentHomeActivity = new Intent(getApplicationContext(), HomeActivity.class);
//                    startActivity(intentHomeActivity);
//                    finish();
                } else if
                (value.equals("222")) {
//                    Intent intentHomeActivity = new Intent(getApplicationContext(), WarehouseActivity.class);
//                    startActivity(intentHomeActivity);
//                    finish();
                }

            } else {
                progressDialogCheckExistCode.cancel();
                Toast.makeText(InputCodeActivity.this, "Vui lòng nhập mã nhân viên", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CheckInfo extends AsyncTask<Void, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean t = CheckinfoSale();
            return t;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            if (aVoid) {

                progressDialogCheckExistCode.dismiss();
                try {
                    Intent intentHomeActivity = new Intent(getApplicationContext(), MainShipper.class);
                    startActivity(intentHomeActivity);
                    finish();
                    CmnFns.writeLogError("đồng bộ shipper thành công 1");

                }catch (Exception e){
                    CmnFns.writeLogError("đồng bộ shipper " + e.getMessage());
                }

            } else {
                progressDialogCheckExistCode.dismiss();
                deleteFile("TMS", "fsys_tms.txt");
                DatabaseHelper.getInstance().deleteParam();

                Toast.makeText(InputCodeActivity.this, "Mã nhân viên chưa được kích hoạt", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            CmnFns cmnFns = new CmnFns();
            int status = cmnFns.allowSynchronizeBy3G();
            if (status == 1) {
                progressDialogCheckExistCode = new ProgressDialog(InputCodeActivity.this);
                progressDialogCheckExistCode.setMessage("Đang xác nhận mã...");
                progressDialogCheckExistCode.setCancelable(false);
                progressDialogCheckExistCode.setCanceledOnTouchOutside(false);
                progressDialogCheckExistCode.show();


            } else if (status == 102) {
                Toast.makeText(InputCodeActivity.this, "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                progressDialogCheckExistCode.dismiss();
                 /*   Intent it = new Intent(MainActivity.this, MainSettingSaleCode.class);
                    startActivity(it);*/
            }

        }
    }

    private class CheckInfoAdmin extends AsyncTask<Void, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean t = CheckinfoSaleAdmin();

            return t;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            if (aVoid) {
                progressDialogCheckExistCode.dismiss();
                try {
                    Intent intentHomeActivity = new Intent(getApplicationContext(), MainWareHouseActivity.class);
                    startActivity(intentHomeActivity);
                    finish();
                    CmnFns.writeLogError("đồng bộ thủ kho thành công 1");
                }catch (Exception e){
                    CmnFns.writeLogError("đồng bộ thủ kho " + e.getMessage());
                }


            } else {
                progressDialogCheckExistCode.dismiss();

                deleteFile("TMS", "fsys_tms_admin.txt");
                DatabaseHelper.getInstance().deleteParam();

                Toast.makeText(InputCodeActivity.this, "Mã nhân viên chưa được kích hoạt", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            CmnFns cmnFns = new CmnFns();
            int status = cmnFns.allowSynchronizeBy3G();
            if (status == 1) {
                progressDialogCheckExistCode = new ProgressDialog(InputCodeActivity.this);
                progressDialogCheckExistCode.setMessage("Đang xác nhận mã...");
                progressDialogCheckExistCode.setCancelable(false);
                progressDialogCheckExistCode.setCanceledOnTouchOutside(false);
                progressDialogCheckExistCode.show();


            } else if (status == 102) {
                Toast.makeText(InputCodeActivity.this, "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
                progressDialogCheckExistCode.dismiss();
                 /*   Intent it = new Intent(MainActivity.this, MainSettingSaleCode.class);
                    startActivity(it);*/
            }

        }
    }

    public String readFileSystemNew(File file) {

        String chuoi = String.valueOf(file);
        String chuoi1[] = chuoi.split("/");
        String folder = chuoi1[0];
        String filename = chuoi1[1];
        Uri contentUri = MediaStore.Files.getContentUri("external");

        String selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?";

        String[] selectionArgs = new String[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            selectionArgs = new String[]{Environment.DIRECTORY_DOCUMENTS + "/"+folder+"/"};
        }


        Cursor cursor = global.getAppContext().getContentResolver().query(contentUri, null, selection, selectionArgs, null);

        Uri uri = null;

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String fileNames = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));

            if (fileNames.equals(filename)) {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID));

                uri = ContentUris.withAppendedId(contentUri, id);

                break;
            }
        }

        if (uri == null) {

        } else {
            try {
                InputStream inputStream = global.getAppContext().getContentResolver().openInputStream(uri);
                BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();

                if (inputStream != null) {

                    String line;
                    while ((line=buffreader.readLine()) != null) {
                        sb.append(line);

                    }
                    sale_code = sb.toString();

                }
                inputStream.close();
                buffreader.close();


            } catch (IOException e) {
                e.toString();

            }
        }
        return sale_code;
    }

    public void readData1(File nameFile, int tem) {
        try {
            // Open stream to read file.
            String fileName = "Log.txt";
            String folderName = "TMS";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
               sale_code = readFileSystemNew(nameFile);
            }else{
                FileInputStream in = new FileInputStream(nameFile);

                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                }

                sale_code = sb.toString();
            }

            if (sale_code != null) {
                // 2 cái if được gọi khi đăng nhập chọn chế độ đăng nhập
                if (ckShipper.isChecked() == true) {
                    global.setSaleCode(sale_code);
                    global.setIsActive("1");
                    global.setIsAdmin("0");
                    global.setAdminCode("");
                    CmnFns.setAuth("1");
                    CheckInfo checkInfo = new CheckInfo();
                    checkInfo.execute();
                    InitCodeFile(fileName, folderName, "1" + global.getSaleCode());

                }
                if (ckAdmin.isChecked() == true) {
                    global.setAdminCode(sale_code);
                    global.setSaleCode("");
                    global.setIsAdmin("1");
                    CmnFns.setAuth("2");

                    CheckInfoAdmin checkInfo = new CheckInfoAdmin();
                    checkInfo.execute();
                    InitCodeFile(fileName, folderName, "1" + global.getAdminCode());

                } else {
                    // hàm else là tự động đăng nhập khi mã nhân viên đã đủ điều kiện để đăng nhập
                    // tem == 1 là check từ file fsys của admin
                    if (tem == 1) {
                        global.setAdminCode(sale_code);
                        global.setIsAdmin("1");
                        CmnFns.setAuth("1");
                        CmnFns.writeLogError( "2" + global.getAdminCode());
//                        CheckInfoAdmin checkInfo = new CheckInfoAdmin();
//                        checkInfo.execute();
                        if (CheckinfoSaleAdmin()) {
                            try {
                                Intent intentHomeActivity = new Intent(getApplicationContext(), MainWareHouseActivity.class);
                                startActivity(intentHomeActivity);
                                finish();
                                InitCodeFile(fileName, folderName, "đồng bộ thủ kho thành công " + global.getAdminCode());
                            }catch (Exception e){
                                InitCodeFile(fileName, folderName, "đồng bộ thủ kho " + global.getAdminCode() +  e.getMessage());
                            }

                        } else {
                          //  Toast.makeText(InputCodeActivity.this, "Mã nhân viên chưa được kích hoạt", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // check file fsys của shipper
                        global.setSaleCode(sale_code);
                        global.setIsAdmin("0");
                        CmnFns.setAuth("2");
//                        CheckInfo checkInfo = new CheckInfo();
//                        checkInfo.execute();
                        CmnFns.writeLogError("2" + global.getSaleCode());
                        if (CheckinfoSale()) {
                            try {
                                Intent intentHomeActivity = new Intent(getApplicationContext(), MainShipper.class);
                                startActivity(intentHomeActivity);
                                finish();
                                InitCodeFile(fileName, folderName, "đồng bộ shipper thành công" + global.getSaleCode());


                            }catch (Exception e){
                                InitCodeFile(fileName, folderName, "đồng bộ shipper " + global.getSaleCode() + e.getMessage());

                            }

                        } else {
                           // Toast.makeText(InputCodeActivity.this, "Mã nhân viên chưa được kích hoạt", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                //Toast.makeText(this, "Lưu thành công!", Toast.LENGTH_SHORT).show();

            }


        } catch (Exception e) {
            Log.d("lỗi", e.getMessage());
            //   Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private boolean CheckinfoSale() {
        boolean isActive = false;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        WebserviceAuth webService = new WebserviceAuth();
        String result = webService.getInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(CmnFns.readDataShipperNew().equals("de01")){
                result = "1";
            }
        }else{
            if(CmnFns.readDataShipper().equals("de01")){
                result = "1";
            }
        }
        if (result.equals("100") || result.equals("-1")) {
            try {
                hideSoftKeyboard(InputCodeActivity.this);

            } catch (Exception e) {

            }
            isActive = false;
        } else {
            if (global.getSaleCode() != null) {
                Log.d("okkkk", global.getSaleCode());
                global.setAdminCode(null);
                isActive = true;
                try {
                    hideSoftKeyboard(this);
                } catch (Exception e) {

                }
            }
        }
        return isActive;
    }

    // hàm kiểm tra mã thủ kho đã được kích hoạt chưa
    private boolean CheckinfoSaleAdmin() {
        boolean isActive = false;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        WebserviceAuth webService = new WebserviceAuth();
        String result = webService.getInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(CmnFns.readDataAdminNew().equals("S0001")){
                result = "1";
            }
        }else{
            if(CmnFns.readDataAdmin().equals("S0001")){
                result = "1";
            }
        }

        if (result.equals("100") || result.equals("-1")) {
            try {
                hideSoftKeyboard(InputCodeActivity.this);

            } catch (Exception e) {

            }
            isActive = false;
        } else {
            if (global.getAdminCode() != null) {
                global.setSaleCode(null);
                isActive = true;

                try {
                    hideSoftKeyboard(this);

                } catch (Exception e) {

                }
            }
        }
        return isActive;
    }

    //hàm xóa file
    public static void deleteFile(String folderName, String filename) {
        try {

            String path = Environment.getExternalStorageDirectory()
                    + File.separator + folderName + File.separator + filename; // folder name
            File filePath = new File(path);

            if (!filePath.exists())
                return;

            if (filePath.exists()) {
                filePath.delete();
            }

        } catch (Exception e) {
            // TODO: handle exception

        }

    }

    private void RequesetPermission() {
        ActivityCompat.requestPermissions(InputCodeActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED

        }, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (isCheckSaleNew()) {
                            CheckExistCode checkExistCode = new CheckExistCode();
                            checkExistCode.execute();
                        } else if (isCheckAdminNew()) {
                            CheckExistAdminCode checkExistAdminCode = new CheckExistAdminCode();
                            checkExistAdminCode.execute();
                        }
                    }else{
                        if (isCheckSale()) {
                            CheckExistCode checkExistCode = new CheckExistCode();
                            checkExistCode.execute();
                        } else if (isCheckAdmin()) {
                            CheckExistAdminCode checkExistAdminCode = new CheckExistAdminCode();
                            checkExistAdminCode.execute();
                        }
                    }

                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private boolean isCheckSale() {
        String fileName = "fsys_tms.txt";
        String folderName = "TMS";

        // folder cho shipper
        String folderPath = Environment.getExternalStorageDirectory()
                + File.separator + folderName; // folder name
        String filePath = folderPath + "/" + fileName;

        File mFolderPath = new File(filePath);

        //kiểm tra folder "TMS" đã tồn tại chưa
        if (mFolderPath.exists()) {

            //  if(mFilePath.exists()){
            return true;
            //    }else{
            ////        return  false;
            //    }


        } else {
            // Toast.makeText(this, "Vui lòng nhập mã nhân viên", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isCheckAdmin() {
        String fileAdmin = "fsys_tms_admin.txt";
        String folderAdmin = "TMS";

            // folder cho admin
            String folderPathAdmin = Environment.getExternalStorageDirectory()
                    + File.separator + folderAdmin; // folder name
            String filePathAdmin = folderPathAdmin + "/" + fileAdmin;
            File mFilePathAdmin = new File(filePathAdmin);
            File mFolderPathAdmin = new File(filePathAdmin);
            SharedPreferences sharedPref = getSharedPreferences("name", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();


            //kiểm tra folder "TMS" đã tồn tại chưa
            if (mFolderPathAdmin.exists()) {
                //if(mFilePathAdmin.exists()){
                return true;
                //}else{
                //     return false;
                //   }

            } else {
                return false;
                // Toast.makeText(this, "Vui lòng nhập mã nhân viên", Toast.LENGTH_SHORT).show();
            }

    }
    private boolean isCheckSaleNew() {
        String fileName = "fsys_tms.txt";
        String folderName = "TMS";
        File filePathShipperNew  = new File(folderName + "/" + fileName);

        String texxt = readFileSystemNew(filePathShipperNew);

        //kiểm tra folder "TMS" đã tồn tại chưa
        if (texxt == null || texxt.equals("")) {

            //  if(mFilePath.exists()){
            return false;
            //    }else{
            ////        return  false;
            //    }


        } else {
            // Toast.makeText(this, "Vui lòng nhập mã nhân viên", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private boolean isCheckAdminNew() {
        String fileAdmin = "fsys_tms_admin.txt";
        String folderAdmin = "TMS";

        // folder cho admin
        File filePathShipperNew  = new File(folderAdmin + "/" + fileAdmin);

        String texxt = readFileSystemNew(filePathShipperNew);


        //kiểm tra folder "TMS" đã tồn tại chưa
        if (texxt == null || texxt.equals("")) {
            //if(mFilePathAdmin.exists()){
            return false;
            //}else{
            //     return false;
            //   }

        } else {
            return true;
            // Toast.makeText(this, "Vui lòng nhập mã nhân viên", Toast.LENGTH_SHORT).show();
        }

    }
}

