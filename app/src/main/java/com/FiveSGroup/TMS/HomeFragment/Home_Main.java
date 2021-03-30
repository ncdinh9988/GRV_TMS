package com.FiveSGroup.TMS.HomeFragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.FiveSGroup.TMS.ChangeCusFragment.CustCodeEventbus;
import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.Map.CustCodeMap;
import com.FiveSGroup.TMS.OriginLinkEventbus;
import com.FiveSGroup.TMS.TakePhotoFragment.GetOrderCDEventbus;

import com.FiveSGroup.TMS.TakePhotoFragment.LinkEvenbus;

import com.FiveSGroup.TMS.TakePhotoFragment.SaveLinkEventbus;

import com.FiveSGroup.TMS.TakePhotoFragment.TransferMenuEventbus;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.global;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.FiveSGroup.TMS.global.getAllowSynchroinzeBy3G;

public class Home_Main extends androidx.fragment.app.Fragment {
    private static WebView wvContent;
    String current = "";
    private boolean home = false;


    File f;
    static  String url = "";
    Handler handler;
    static String urlOrder = "";


    TransferMenuEventbus eventbus;
    LinkEvenbus linkEvenbus;

    Menu menu;
    String newaaa;

    public Home_Main() {

    }

    public Home_Main(Menu menu) {
        this.menu = menu;
    }

    private ProgressBar progressBar;

    OriginLinkEventbus originLinkEventbus;

    public static String webLink = "";
    String urlDelivery =  "";
    SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_main, container, false);
        wvContent = view.findViewById(R.id.wvContent);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        urlDelivery =  DatabaseHelper.getInstance().getParamByKey("URL_Delivery").getValue();

        if(CmnFns.isNetworkAvailable()) {

            linkEvenbus = EventBus.getDefault().getStickyEvent(LinkEvenbus.class);
            try {
                newaaa = linkEvenbus.getUrl();
                EventBus.getDefault().removeStickyEvent(linkEvenbus);
            } catch (Exception e) {

            }


            progressBar = view.findViewById(R.id.progress_circular);
            progressBar.setMax(100);
            setHasOptionsMenu(true);


            if (allowSynchronizeBy3G() == 1) {
                wvContent.setVisibility(View.VISIBLE);

                if ((newaaa == "") || (newaaa == null)) {
                    addEvents(urlDelivery + "?deliverer=" + CmnFns.readDataShipper());
                } else {
                    addEvents(newaaa);
                }


            } else {
                wvContent.setVisibility(View.GONE);
            }
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

        RefeshData();
        return view;
    }
    private void RefeshData() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addEvents(wvContent.getUrl());
                refreshLayout.setRefreshing(false);

            }
        });

    }
    // hàm kiểm tra xem máy có đc đồng bộ dữ liệu bằng 3G hay không
    public int allowSynchronizeBy3G() {

        try {
            ConnectivityManager connManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(
                    getActivity().getApplicationContext().CONNECTIVITY_SERVICE);
            NetworkInfo mMobile = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (!mWifi.isConnected())
                if (!mMobile.isConnected())
                    return 102;
            if (mMobile.isConnected() && getAllowSynchroinzeBy3G() != true)
                return 102;
            return 1;
        } catch (Exception e) {
            // TODO: handle exception

            return -1;
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest wRequest) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(wRequest.getUrl().toString());
                home = false;
                current = wRequest.getUrl().toString();
            }
            return true;
        }

        @Override
        public void onReceivedSslError(final WebView view, final SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("runTMS","OnPageFinish");
            current = url;
            urlOrder = current;

            EventBus.getDefault().postSticky(new SaveLinkEventbus(current));


            eventbus = EventBus.getDefault().getStickyEvent(TransferMenuEventbus.class);

            if (urlOrder.contains("orderCD")) {
                EventBus.getDefault().postSticky(new LinkEvenbus(current));

                String[] lenght = url.split("orderCD=");

                String lastString = lenght[lenght.length - 1];

                String [] lenght2 = lastString.split("&Cust_Code=");

                String Cust_Code = lenght2[lenght2.length - 1];

                String orderCode = lenght2[0];


                if (eventbus != null) {
                    menu = eventbus.getMenu();
                    MenuItem item = menu.findItem(R.id.navigation_capture);
                    MenuItem item1 = menu.findItem(R.id.navigation_update);
                    MenuItem item2 = menu.findItem(R.id.navigation_home);
                    MenuItem item3 = menu.findItem(R.id.navigation_dashboard);
                    MenuItem item4 = menu.findItem(R.id.navigation_map);
                    if (item != null) {
                        item.setVisible(false);
                       // item.setEnabled(true);
                        item1.setVisible(false);
                       // item1.setEnabled(true);
                        item2.setVisible(true);
                        item3.setVisible(true);
                        item4.setVisible(true);
                    }
                    EventBus.getDefault().removeStickyEvent(eventbus);
                } else {
                    if (menu != null) {
                        MenuItem item = menu.findItem(R.id.navigation_capture);
                        MenuItem item1 = menu.findItem(R.id.navigation_update);
                        MenuItem item2 = menu.findItem(R.id.navigation_home);
                        MenuItem item3 = menu.findItem(R.id.navigation_dashboard);
                        MenuItem item4 = menu.findItem(R.id.navigation_map);
                        if (item != null) {
                            item.setVisible(true);
                            //item.setEnabled(true);
                            item1.setVisible(true);
                           /// item1.setEnabled(true);
                            item2.setVisible(false);
                            item3.setVisible(false);
                            item4.setVisible(true);
                        }
                    }
                }
                EventBus.getDefault().postSticky(new GetOrderCDEventbus(orderCode));
                EventBus.getDefault().postSticky(new CustCodeEventbus(Cust_Code));
                EventBus.getDefault().postSticky(new CustCodeMap(Cust_Code));



            } else {
                if (menu != null) {
                    MenuItem item = menu.findItem(R.id.navigation_capture);
                    MenuItem item1 = menu.findItem(R.id.navigation_update);
                    MenuItem item2 = menu.findItem(R.id.navigation_home);
                    MenuItem item3 = menu.findItem(R.id.navigation_dashboard);
                    MenuItem item4 = menu.findItem(R.id.navigation_map);
                    if (item != null) {
                        item.setVisible(false);
                       // item.setEnabled(true);
                        item1.setVisible(false);
                        //item1.setEnabled(true);
                        item2.setVisible(true);
                        item3.setVisible(true);
                        item4.setVisible(true);
                    }
                }
            }
            progressBar.setVisibility(View.GONE);

        }
    }

    private void addEvents(String url) {
        //String url = "https://voucherpg.com/Phase2/LOGINv2.aspx";

        if(CmnFns.isNetworkAvailable()) {
            wvContent.getSettings().setJavaScriptEnabled(true);
            wvContent.getSettings().setUseWideViewPort(true);
            wvContent.getSettings().setLoadWithOverviewMode(true);
            wvContent.canGoBack();
            wvContent.getSettings().setSupportZoom(true);
            wvContent.canGoForward();
            wvContent.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            wvContent.setInitialScale(1);
            wvContent.loadUrl(url);

            wvContent.setWebViewClient(new MyBrowser());

            wvContent.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    //Required functionality here
                    return super.onJsAlert(view, url, message, result);
                }

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    progressBar.setProgress(newProgress);
                    if (newProgress == 100) {
                        try {
                            progressBar.setVisibility(View.GONE);
                        } catch (Exception e) {
                        }

                    }
                }
            });
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


    @Override
    public void onResume() {
        super.onResume();

        Log.d("runTMS","OnResume");

        if(CmnFns.isNetworkAvailable()) {

            // originLinkEventbus = EventBus.getDefault().getStickyEvent(OriginLinkEventbus.class);

            if (wvContent != null) {
                SharedPreferences prefs = getActivity().getApplicationContext().
                        getSharedPreferences(getActivity().getPackageName(), Activity.MODE_PRIVATE);
                String s = prefs.getString("lastUrl", "");
                if (!s.equals("") && global.getCheckAppStop() == "1") {
                    wvContent.loadUrl(s);
                } else {
                    global.setCheckAppStop("1");
                    addEvents(urlDelivery + "?deliverer="  + global.getSaleCode());
                }
            }
            wvContent.setWebViewClient(new MyBrowser());


            //addEvents(global.getURLDELIVERYCUSTOMERlIST());
        }




    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item = menu.findItem(R.id.navigation_capture);
        MenuItem item1 = menu.findItem(R.id.navigation_update);
        if (current.contains("html")) {
            try {
                String[] lenght = url.split("=");
                String orderCode = lenght[lenght.length - 1];

                if (item != null) {
                    item.setEnabled(true);
                    item1.setEnabled(true);
                }
                EventBus.getDefault().postSticky(new GetOrderCDEventbus(orderCode));

            } catch (Exception e) {

            }
            // EventBus.getDefault().postSticky(new NotiEvenbus());

        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("runTMS","OnPause");
        SharedPreferences prefs = getActivity().getApplicationContext().
                getSharedPreferences(getActivity().getPackageName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("lastUrl", wvContent.getUrl());
        String url = wvContent.getUrl();

        edit.commit();   // can use edit.apply() but in this case commit is better
    }

    public boolean canGoBack() {

        return wvContent.canGoBack();
    }

    public void goBack() {
        wvContent.goBack();
    }

    @Override
    public void onStart() {
        Log.d("runTMS","OnStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d("runTMS","OnStop");

        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("runTMS","OnDestroy");

        super.onDestroy();
    }

    public File CreateFile(String folderName, String filename) {
        try {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                String path = Environment.getExternalStorageDirectory()
                        + File.separator + folderName; // folder name

                File filePath = new File(path);

                if (!filePath.exists()) {
                    filePath.mkdirs();
                }

                f = new File(path, filename);

                try {
                    if (!f.exists()) {
                        f.createNewFile();
                    }
                    return f;
                } catch (Exception exp) {

                    Log.i("FileUtils", exp.getMessage());
                }
            } else {
                // Request permission from the user
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            return f;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    private void writeFile(File name) {
        // String url = "https://voucherpg.com/Phase2/LOGINv2.aspx";
        //String url = "https://5smsgr.5stars.com.vn:7443/Login/L5SLogin.aspx";
        //String url =  "https://id.zalo.me/account?continue=https%3A%2F%2Fchat.zalo.me%2F";
        //String url = "https://111.222.3.199:5293/Order/Order.aspx?aspxerrorpath=%2fOrder%2fOrder.aspx";
        String url = "https://pos.5svisions.com:5293/Order/Order.aspx";
        FileWriter writer = null;
        try {
            writer = new FileWriter(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Ghi dữ liệu.
            writer.write(url);
            //writer.write("\n");
            // writer.write(valueTexxt);
            //writer.write("\n");
            writer.close();
            //Toast.makeText(this, "Đã lưu", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void readData1(File nameFile) {
        try {
            // Open stream to read file.

            FileInputStream in = new FileInputStream(nameFile);

            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }

             url = sb.toString();



        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
