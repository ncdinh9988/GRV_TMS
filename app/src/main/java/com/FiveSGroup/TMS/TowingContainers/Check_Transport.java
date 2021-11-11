package com.FiveSGroup.TMS.TowingContainers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.MainMenu.MainWareHouseActivity;
import com.FiveSGroup.TMS.MasterPick.Home_Master_Pick;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ValueEventbus;
import com.FiveSGroup.TMS.global;

public class Check_Transport extends AppCompatActivity {
    private WebView mWebview;
    private Button btn1, btnLpn, btn3, btnback, btnShow , btnchuyendvt;
    LinearLayout layout;
    String value1 = "", value2 = "";
    SharedPreferences sharedPref;
    String urlStockReceipt = "" , valuewarehouse = "";
    ValueEventbus eventbus;
    static String value = "";
    String value3 = "";
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_picklist);
        progressBar = findViewById(R.id.progressbar);
        refreshLayout = findViewById(R.id.swipeRefesh);

        progressBar.setMax(100);


//
//        Log.e("new3","là : " + value3);
//        Log.e("new4","là : " + value4);

        mWebview = (WebView) findViewById(R.id.webview);
        btn1 = (Button) findViewById(R.id.btn1);
        btnchuyendvt = (Button) findViewById(R.id.btnchuyendvt) ;
        btnback = findViewById(R.id.btnback);
        btnShow = findViewById(R.id.btnShow);
        btnShow.setText("CHỤP ẢNH KIỂM TRA PHƯƠNG TIỆN");
        btnLpn = findViewById(R.id.btnlpn);
        layout = findViewById(R.id.layout);
        btn1.setVisibility(View.GONE);
        btnchuyendvt.setVisibility(View.GONE);
        btnShow.setVisibility(View.GONE);
        btnLpn.setVisibility(View.GONE);
        urlStockReceipt = DatabaseHelper.getInstance().getParamByKey("URL_Check_Transport").getValue();


        String urlStockOut =  urlStockReceipt + "?USER_CODE=" + CmnFns.readDataAdmin();
        addEvents(urlStockOut);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Check_Transport.this, Check_Transport_Photo.class);
                intent.putExtra("qrcode1", "qrcode1");
                // Log.e("barcodeData",""+ barcodeData);
                startActivity(intent);
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttt = new Intent(Check_Transport.this, MainWareHouseActivity.class);
                startActivity(intenttt);
                finish();
            }
        });

        RefeshData();

    }
    private void RefeshData() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addEvents(urlStockReceipt + "?USER_CODE=" + CmnFns.readDataAdmin());
                refreshLayout.setRefreshing(false);

            }
        });

    }
    private void addEvents(String url) {
        if (CmnFns.isNetworkAvailable()) {
            mWebview.addJavascriptInterface(new Check_Transport.JavaScriptInterface(Check_Transport.this), "Android");
            mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
            mWebview.getSettings().setUseWideViewPort(true);
            mWebview.getSettings().setAppCacheEnabled(true); //cho phép sử dụng cache của webview
            mWebview.getSettings().setDomStorageEnabled(true);//cho phép dùng bộ nhớ
            mWebview.getSettings().setLoadWithOverviewMode(true);
            mWebview.canGoBack();
            mWebview.getSettings().setSupportZoom(true);
            mWebview.canGoForward();
            mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            mWebview.setInitialScale(1);
            mWebview.loadUrl(url);
            mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            mWebview.requestFocus();
            mWebview.getSettings().setLightTouchEnabled(true);
            mWebview.getSettings().setGeolocationEnabled(true);


            if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(Check_Transport.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            }

            mWebview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    progressBar.setVisibility(View.VISIBLE);
                }
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed(); // Ignore SSL certificate errors
                }

                public void onPageFinished(WebView view, String url) {
                    SharedPreferences sharedPref = getSharedPreferences("name", Context.MODE_PRIVATE);
                    value3 = sharedPref.getString("btn1", "");


                    //Here you want to use .loadUrl again
                    //on the webview object and pass in
                    //"javascript:<your javaScript function"

                    // mWebview.loadUrl("javascript:ResusltBarCode('"+value3+"')");
                    //mWebview.loadUrl("javascript:ResusltBarCode('"+value3+"','"+value4+"')");
                    ;//if passing in an object. Mapping may need to take place
                    Log.e("urljavascript", "đã chạy dc");


                    //Toast.makeText(HomeQRActivity.this, url+"", Toast.LENGTH_LONG).show();
                    if (url.contains("CheckTransportListItemForApp.aspx?CHECK_TRANSPORT_CD")) {
                        String chuoi[] = url.split("=");
                        String code = chuoi[1];
                        String chuoi2[] = code.split("&");
                        String code2 = chuoi2[0];
                        global.setCheckTransportCd(code2);
                        // Toast.makeText(HomeQRActivity.this, code+"", Toast.LENGTH_SHORT).show();

//                        btn1.setVisibility(View.VISIBLE);
                        btnShow.setVisibility(View.VISIBLE);
                        btnLpn.setVisibility(View.GONE);
//                        btnchuyendvt.setVisibility(View.VISIBLE);
                        btnback.setVisibility(View.GONE);
                        SharedPreferences sharedPreferences = getSharedPreferences("masterpick", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("masterpick_cd", code);
                        editor.apply();


                    } else {
                        btnShow.setVisibility(View.GONE);
//                        btn1.setVisibility(View.GONE);
                        btnLpn.setVisibility(View.GONE);
//                        btnchuyendvt.setVisibility(View.GONE);
                        btnback.setVisibility(View.VISIBLE);
                        SharedPreferences settings = getSharedPreferences("name", Context.MODE_PRIVATE);
                        settings.edit().clear().apply();
                        SharedPreferences sharedPreferences = getSharedPreferences("masterpick", Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });

            mWebview.setWebChromeClient(new WebChromeClient() {
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
        }
    }

    public class JavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences settings = this.getSharedPreferences("name_master_pick", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

    @Override
    public void onResume() {
        super.onResume();
//        eventbus = EventBus.getDefault().getStickyEvent(ValueEventbus.class);
//        if(eventbus != null){
//            value = eventbus.getValue();
//            EventBus.getDefault().removeStickyEvent(ValueEventbus.class);
//        }
        SharedPreferences prefs = this.
                getSharedPreferences("name_master_pick", Activity.MODE_PRIVATE);
        String s = prefs.getString("lastUrl", "");
        if (!s.equals("")) {
            mWebview.loadUrl(s);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = this.
                getSharedPreferences("name_master_pick", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("lastUrl", mWebview.getUrl());
        edit.commit();   // can use edit.apply() but in this case commit is better
    }

    @Override
    public void onBackPressed() {

    }
}