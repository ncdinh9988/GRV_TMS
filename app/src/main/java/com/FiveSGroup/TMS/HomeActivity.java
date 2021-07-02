package com.FiveSGroup.TMS;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.FiveSGroup.TMS.AddCustomerFragment.FragAddCustomer;
import com.FiveSGroup.TMS.ChangeCusFragment.ChangeCusFragment;
import com.FiveSGroup.TMS.ChangeCusFragment.CustCodeEventbus;
import com.FiveSGroup.TMS.ChangeCusFragment.UpdateCustomer;
import com.FiveSGroup.TMS.Map.CustCodeMainMap;
import com.FiveSGroup.TMS.Map.CustCodeMap;
import com.FiveSGroup.TMS.Map.MainMapActivity;
import com.FiveSGroup.TMS.Map.MapActivity;
import com.FiveSGroup.TMS.TakePhotoFragment.GetOrderCDEventbus;
import com.FiveSGroup.TMS.TakePhotoFragment.LinkEvenbus;
import com.FiveSGroup.TMS.TakePhotoFragment.SaveLinkEventbus;
import com.FiveSGroup.TMS.TakePhotoFragment.TransferMenuEventbus;
import com.FiveSGroup.TMS.TakePhotoFragment.CaptureFragment;
import com.FiveSGroup.TMS.HomeFragment.Home_Main;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    String current = "";
    NotiEvenbus notiEvenbus;
    BottomNavigationView navigation;
    private boolean isLoaded = false;
    static MenuItem item1;
    Menu menu;
    SaveLinkEventbus eventBus;
    WebView webViewPickList;
    FrameLayout content;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        webViewPickList = findViewById(R.id.webViewPickList);
        progressBar = findViewById(R.id.progress_circular);
        progressBar.setMax(100);
        content = findViewById(R.id.content);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        menu = navigation.getMenu();

//        if(global.getIsAdmin().equals("1")){
//            String urlPickList = "http://idv.grv.fieldvision.com.vn:54574/WMS/CreateWarehousePickListForApp.aspx?USER_PICKLIST=" + global.getAdminCode();
//            navigation.setVisibility(View.GONE);
//            content.setVisibility(View.GONE);
//            progressBar.setVisibility(View.VISIBLE);
//            webViewPickList.setVisibility(View.VISIBLE);
//
//            addEvents(urlPickList);
//
//        }else{
            EventBus.getDefault().postSticky(new TransferMenuEventbus(menu));
            //addControls();
            //addEvents();
            loadFragment(new Home_Main());
        //}
    }

//    private void addEvents(String url) {
//        //String url = "https://voucherpg.com/Phase2/LOGINv2.aspx";
//
//        if (CmnFns.isNetworkAvailable()) {
//            webViewPickList.getSettings().setJavaScriptEnabled(true);
//            webViewPickList.getSettings().setUseWideViewPort(true);
//            webViewPickList.getSettings().setLoadWithOverviewMode(true);
//            webViewPickList.canGoBack();
//            webViewPickList.getSettings().setSupportZoom(true);
//            webViewPickList.canGoForward();
//            webViewPickList.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//            webViewPickList.setInitialScale(1);
//            webViewPickList.loadUrl(url);
//
//            webViewPickList.setWebViewClient(new MyBrowser());
//
//            webViewPickList.setWebChromeClient(new WebChromeClient() {
//                @Override
//                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                    //Required functionality here
//                    return super.onJsAlert(view, url, message, result);
//                }
//
//                @Override
//                public void onProgressChanged(WebView view, int newProgress) {
//                    super.onProgressChanged(view, newProgress);
//                    progressBar.setProgress(newProgress);
//                    if (newProgress == 100) {
//                        try {
//                            progressBar.setVisibility(View.GONE);
//                        } catch (Exception e) {
//                        }
//
//                    }
//                }
//            });
//        }
//    }


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
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    isLoaded = true;

//                    WebserviceAuth webserviceAuth = new WebserviceAuth();
//                    webserviceAuth.getInfo();
                   // CmnFns.setAuth();
                    // item1 = navigation.getMenu().findItem(R.id.navigation_capture);
                    EventBus.getDefault().postSticky(new TransferMenuEventbus(menu));
                    fragment = new Home_Main(menu);
                    break;
                case R.id.navigation_capture:
                    navigation.setVisibility(View.GONE);
                    fragment = new CaptureFragment();
                    navigation.getMenu().removeItem(R.id.navigation_home);
                    navigation.getMenu().removeItem(R.id.navigation_dashboard);
                    break;
                case R.id.navigation_update:
                    navigation.setVisibility(View.GONE);
                    fragment = new ChangeCusFragment();
                    break;
                case R.id.navigation_dashboard:
                    navigation.setVisibility(View.VISIBLE);
                    fragment = new FragAddCustomer();
                    break;
                case R.id.navigation_map:
//                    try {
                        eventBus = EventBus.getDefault().getStickyEvent(SaveLinkEventbus.class);
                        // nếu có nút home ở giao diện thì mới lấy tất
                        if (eventBus==null || !eventBus.getUrl().contains("&Cust_Code")) {
                            ArrayList<UpdateCustomer> customers = new CmnFns().synchronizeGetCustomersForMap(HomeActivity.this, "0", global.getSaleCode());
                            for (int i = 0; i < customers.size(); i++) {
                                if (!customers.get(i).getCustomerGeocode().equals("")) {
                                    CustCodeMainMap codeMap = new CustCodeMainMap(customers.get(i).getCustomerCd());
                                    EventBus.getDefault().postSticky(codeMap);
                                    break;
                                }
                            }
                            Intent intent = new Intent(HomeActivity.this, MainMapActivity.class);
                            startActivity(intent);
//                            Toast.makeText(HomeActivity.this,"mainmap",Toast.LENGTH_LONG).show();
                        }else if (eventBus.getUrl().contains("&Cust_Code")){
                            Intent intent = new Intent(HomeActivity.this, MapActivity.class);
                            startActivity(intent);
//                            Toast.makeText(HomeActivity.this,"twomap",Toast.LENGTH_LONG).show();
                        }
//                    } catch (Exception e){
//                        Log.e("eeeee", e.getMessage());
//                    }
                    break;

            }
            return loadFragment(fragment);
        }
    };

    private boolean loadFragment(Fragment fragment) {
        // switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {

//        if (webViewPickList.canGoBack()) {
//            webViewPickList.goBack();
//        } else {
//            //Finish
//            finish();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        notiEvenbus = EventBus.getDefault().getStickyEvent(NotiEvenbus.class);

        if (notiEvenbus != null) {
            navigation.getMenu().findItem(R.id.navigation_capture).setEnabled(true);
            navigation.getMenu().findItem(R.id.navigation_update).setEnabled(true);
            navigation.getMenu().findItem(R.id.navigation_map).setEnabled(true);
            EventBus.getDefault().removeStickyEvent(notiEvenbus);

        } else {
            navigation.getMenu().findItem(R.id.navigation_capture).setEnabled(true);
            navigation.getMenu().findItem(R.id.navigation_update).setEnabled(true);
            navigation.getMenu().findItem(R.id.navigation_map).setEnabled(true);


        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    protected void onDestroy() {
        global.setCheckAppStop("0");
        super.onDestroy();
    }

}
