package com.FiveSGroup.TMS.ReturnWareHouse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.DatabaseHelper;
import com.FiveSGroup.TMS.R;
import com.FiveSGroup.TMS.ValueEventbus;
import com.FiveSGroup.TMS.Warehouse.CheckEventbus;
import com.FiveSGroup.TMS.global;

import org.greenrobot.eventbus.EventBus;

public class Home_Return_WareHouse extends AppCompatActivity {
    private WebView mWebview;
    private Button btn1, btn2, btn3, btnback, btnShow;
    LinearLayout layout;
    String value1 = "", value2 = "";
    SharedPreferences sharedPref;
    String urlStockReceipt = "";
    ValueEventbus eventbus;
    static String value = "";
    String value3 = "";
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_stockout);
        progressBar = findViewById(R.id.progressbar);
        refreshLayout = findViewById(R.id.swipeRefesh);

        progressBar.setMax(100);


//
//        Log.e("new3","là : " + value3);
//        Log.e("new4","là : " + value4);

        mWebview = (WebView) findViewById(R.id.webview);
        btn1 = (Button) findViewById(R.id.btn1);
        btnback = findViewById(R.id.btnback);
        btnShow = findViewById(R.id.btnShow);
        layout = findViewById(R.id.layout);
        urlStockReceipt = DatabaseHelper.getInstance().getParamByKey("URL_Return").getValue();;

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Return_WareHouse.this, Qrcode_Return_WareHouse.class);
                intent.putExtra("qrcode1", "qrcode1");
                // Log.e("barcodeData",""+ barcodeData);
                startActivity(intent);
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Return_WareHouse.this, List_Return_WareHouse.class));
                EventBus.getDefault().postSticky(new CheckEventbus());

            }
        });

        String urlStockOut =  urlStockReceipt + "?USER_CODE=" + CmnFns.readDataAdmin();
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
                addEvents(urlStockReceipt + "?USER_CODE=" + CmnFns.readDataAdmin());
                refreshLayout.setRefreshing(false);

            }
        });

    }
    private void addEvents(String url) {
        if (CmnFns.isNetworkAvailable()) {
            mWebview.addJavascriptInterface(new JavaScriptInterface(Home_Return_WareHouse.this), "Android");
            mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
            mWebview.getSettings().setUseWideViewPort(true);
            mWebview.getSettings().setLoadWithOverviewMode(true);
            mWebview.canGoBack();
            mWebview.getSettings().setSupportZoom(true);
            mWebview.canGoForward();
            mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            mWebview.setInitialScale(1);
            mWebview.loadUrl(url);

            mWebview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    progressBar.setVisibility(View.VISIBLE);
                }

//                @Override
//                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                    handler.proceed(); // Ignore SSL certificate errors
//                }

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

                    if (url.contains("WarehouseReturnForAppItem.aspx?OrderCD")) {

                        String chuoi[] = url.split("=");
                        String code = chuoi[1];
                        String chuoi2[] = code.split("&");
                        String code2 = chuoi2[0];
                        global.setReturnCD(code2);
                        // Toast.makeText(HomeQRActivity.this, code+"", Toast.LENGTH_SHORT).show();

                        btn1.setVisibility(View.VISIBLE);
                        btnShow.setVisibility(View.VISIBLE);
                        btnback.setVisibility(View.GONE);
                        SharedPreferences sharedPreferences = getSharedPreferences("stockReceipt", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("stock", code);
                        editor.apply();

                    } else {
                        btnShow.setVisibility(View.GONE);
                        btn1.setVisibility(View.GONE);
                        btnback.setVisibility(View.VISIBLE);
                        SharedPreferences settings = getSharedPreferences("name", Context.MODE_PRIVATE);
                        settings.edit().clear().apply();
                        SharedPreferences sharedPreferences = getSharedPreferences("stockReceipt", Context.MODE_PRIVATE);
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
        SharedPreferences settings = this.getSharedPreferences("name_return_warehouse", Context.MODE_PRIVATE);
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
                getSharedPreferences("name_return_warehouse", Activity.MODE_PRIVATE);
        String s = prefs.getString("lastUrl", "");
        if (!s.equals("")) {
            mWebview.loadUrl(s);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = this.
                getSharedPreferences("name_return_warehouse", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("lastUrl", mWebview.getUrl());
        edit.commit();   // can use edit.apply() but in this case commit is better
    }

    @Override
    public void onBackPressed() {

    }
}
