package com.FiveSGroup.TMS.TowingContainers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.FiveSGroup.TMS.AddCustomerFragment.FragAddCustomer;
import com.FiveSGroup.TMS.ChangeCusFragment.ChangeCusFragment;
import com.FiveSGroup.TMS.ChangeCusFragment.UpdateCustomer;
import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.HomeActivity;
import com.FiveSGroup.TMS.HomeFragment.Home_Main;
import com.FiveSGroup.TMS.LPN.LPNActivity;
import com.FiveSGroup.TMS.LoadPallet.LPNwithSO.LPNandSO;
import com.FiveSGroup.TMS.Map.CustCodeMainMap;
import com.FiveSGroup.TMS.Map.MainMapActivity;
import com.FiveSGroup.TMS.Map.MapActivity;
import com.FiveSGroup.TMS.MasterPick.Home_Master_Pick;
import com.FiveSGroup.TMS.MasterPick.List_Master_Pick;
import com.FiveSGroup.TMS.MasterPick.Qrcode_Master_Pick;
import com.FiveSGroup.TMS.NotiEvenbus;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.TakePhotoFragment.CaptureFragment;
import com.FiveSGroup.TMS.TakePhotoFragment.SaveLinkEventbus;
import com.FiveSGroup.TMS.TakePhotoFragment.TransferMenuEventbus;
import com.FiveSGroup.TMS.TransferUnit.TransferUnitQrcode;
import com.FiveSGroup.TMS.ValueEventbus;
import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.global;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class Towing_Containers extends AppCompatActivity {
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
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 0);

        mWebview = (WebView) findViewById(R.id.webview);
        btn1 = (Button) findViewById(R.id.btn1);
        btnchuyendvt = (Button) findViewById(R.id.btnchuyendvt) ;
        btnback = findViewById(R.id.btnback);
        btnShow = findViewById(R.id.btnShow);
        btnLpn = findViewById(R.id.btnlpn);
        layout = findViewById(R.id.layout);
        btn1.setVisibility(View.GONE);
        btnchuyendvt.setVisibility(View.GONE);
        btnShow.setVisibility(View.GONE);
        btnLpn.setVisibility(View.GONE);
        urlStockReceipt = DatabaseHelper.getInstance().getParamByKey("URL_StockReceiptCont").getValue();


        String urlStockOut =  urlStockReceipt + "?USER_CODE=" + CmnFns.readDataShipper();
        addEvents(urlStockOut);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RefeshData();

    }
    private void RefeshData() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addEvents(urlStockReceipt + "?USER_CODE=" + CmnFns.readDataShipper());
                refreshLayout.setRefreshing(false);

            }
        });

    }
    private void addEvents(String url) {
        if (CmnFns.isNetworkAvailable()) {
            mWebview.addJavascriptInterface(new Towing_Containers.JavaScriptInterface(Towing_Containers.this), "Android");
            mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
            mWebview.getSettings().setUseWideViewPort(true);
            mWebview.getSettings().setGeolocationEnabled(true);
            mWebview.getSettings().setLoadWithOverviewMode(true);
            mWebview.canGoBack();
            mWebview.getSettings().setSupportZoom(true);
            mWebview.canGoForward();
            mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            mWebview.setInitialScale(1);
            mWebview.loadUrl(url);

            mWebview.requestFocus();
            mWebview.getSettings().setLightTouchEnabled(true);
            mWebview.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                    callback.invoke(origin, true, false);
                }
            });

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
//                    if (url.contains("WarehousePickListForAppItemV2.aspx?PickListCD")) {
//
//                        String chuoi[] = url.split("=");
//                        String code = chuoi[1];
//                        global.setMasterPickCd(code);
//                        // Toast.makeText(HomeQRActivity.this, code+"", Toast.LENGTH_SHORT).show();
//
//                        btn1.setVisibility(View.VISIBLE);
//                        btnShow.setVisibility(View.VISIBLE);
//                        btnLpn.setVisibility(View.VISIBLE);
//                        btnchuyendvt.setVisibility(View.VISIBLE);
//                        btnback.setVisibility(View.GONE);
//                        SharedPreferences sharedPreferences = getSharedPreferences("masterpick", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("masterpick_cd", code);
//                        editor.apply();
//
//                    } else {
//                        btnShow.setVisibility(View.GONE);
//                        btn1.setVisibility(View.GONE);
//                        btnLpn.setVisibility(View.GONE);
//                        btnchuyendvt.setVisibility(View.GONE);
//                        btnback.setVisibility(View.VISIBLE);
//                        SharedPreferences settings = getSharedPreferences("name", Context.MODE_PRIVATE);
//                        settings.edit().clear().apply();
//                        SharedPreferences sharedPreferences = getSharedPreferences("masterpick", Context.MODE_PRIVATE);
//                        sharedPreferences.edit().clear().apply();
//
//                    }
//                    progressBar.setVisibility(View.GONE);
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