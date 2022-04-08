package com.FiveSGroup.TMS.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.FiveSGroup.TMS.ChangeCusFragment.UpdateCustomer;
import com.FiveSGroup.TMS.CmnFns;
import com.FiveSGroup.TMS.HomeActivity;
import com.FiveSGroup.TMS.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class MainMapActivity extends AppCompatActivity implements OnMapReadyCallback
        , TaskLoadedCallback
        , View.OnClickListener, GoogleMap.OnMarkerClickListener {

    private static final int REQUEST_CODE = 101;
    GoogleMap googleMap;
    Button buttonGetDirection, buttonBack, buttonGetMyLocation;
    MarkerOptions place1, place2;
    Polyline currentPolyline;
    FusedLocationProviderClient client;
    MapFragment mapFragment;
    Location currentLocation;
    CustCodeMainMap eventBus;
    String custCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        //Hiển thị Map ở màn hình chính bao gồm tất cả tuyến đường
        //Khởi tạo view
        init();
        //Lấy vị trí hiện tại khi chạy map lần đầu tiên
        getCurrentLocation();

    }

    //khởi tạo tuyến đường
    private void findRoute(LatLng currentPostion, LatLng destinationPosition) {
        try {
            String url = getUrl(currentPostion, destinationPosition, "driving");
            new FetchURL(MainMapActivity.this).execute(url, "driving");
        } catch (Exception e) {
            Log.d("Map error", "#001" + e.getMessage());
            Toast.makeText(MainMapActivity.this, "#001", Toast.LENGTH_SHORT).show();
        }
    }

    // Đánh dấu địa điểm đến trên bản đồ
    private void addDestinationPlace() {
        try {
            eventBus = EventBus.getDefault().getStickyEvent(CustCodeMainMap.class);
            if (eventBus != null) {
                custCode = eventBus.getCustCode();
                ArrayList<UpdateCustomer> listCustomer = new CmnFns().synchronizeGetCustomersForMap(this, custCode, "");
                if (listCustomer.size() > 0) {
                    ArrayList<UpdateCustomer> arrCustomers = listCustomer;
                    if (arrCustomers.size() > 0) {
                        for (int i = 0; i < arrCustomers.size(); i++) {
                            if (!custCode.equals("")) {
                                Bitmap bitmap = null;
                                if (i + 1 < 10) {
                                    bitmap = makeBitmap(this, "0" + (i + 1));
                                } else {
                                    bitmap = makeBitmap(this, String.valueOf(i + 1));
                                }

                                if (custCode.equals(arrCustomers.get(i).getCustomerCd())) {
                                    if (!arrCustomers.get(i).getCustomerGeocode().equals("")) {
                                        String[] latlng = arrCustomers.get(i).getCustomerGeocode().split(",");
                                        double lat = Float.parseFloat(latlng[0]);
                                        double lng = Float.parseFloat(latlng[1]);
                                        if (place2 == null) {
                                            String content = arrCustomers.get(i).getCustomerCode() + " - "
                                                    + arrCustomers.get(i).getCustomerName() + " - "
                                                    + arrCustomers.get(i).getCustomerAddress();
                                            place2 = new MarkerOptions().
                                                    position(new LatLng(lat, lng)).
                                                    title("Địa điểm giao hàng").
                                                    snippet(content).
                                                    icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                            googleMap.addMarker(place2);
                                        }
                                    } else {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainMapActivity.this);
                                        builder.setTitle("Không có điểm đến").
                                                setMessage("Đơn hàng này không có tọa độ hoặc đã được giao.").
                                                setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                }).show();
                                        buttonGetDirection.setEnabled(false);
                                    }
                                } else {
                                    if (!arrCustomers.get(i).getCustomerGeocode().equals("")) {
                                        String[] latlng = arrCustomers.get(i).getCustomerGeocode().split(",");
                                        double lat = Float.parseFloat(latlng[0]);
                                        double lng = Float.parseFloat(latlng[1]);
                                        String content = arrCustomers.get(i).getCustomerCode() + " - "
                                                + arrCustomers.get(i).getCustomerName() + " - "
                                                + arrCustomers.get(i).getCustomerAddress();
                                        MarkerOptions otherMarkers = new MarkerOptions()
                                                .position(new LatLng(lat, lng))
                                                .title("Địa điểm giao hàng")
                                                .snippet(content)
                                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                        googleMap.addMarker(otherMarkers);

                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.d("Map error", "#002" + e.getMessage());
            Toast.makeText(MainMapActivity.this, "#002", Toast.LENGTH_SHORT).show();
        }
    }

    // tạo layout marker trên bản đồ có kèm text
    public Bitmap makeBitmap(Context context, String text) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        TextView textViewSTT = (TextView) view.findViewById(R.id.textViewSTT);
        textViewSTT.setText(text);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    // Khởi tạo view và biến
    private void init() {
        buttonGetDirection = (Button) findViewById(R.id.buttonMainGetDirection);
        buttonGetMyLocation = (Button) findViewById(R.id.buttonMainGetCurrentLocation);
        buttonBack = (Button) findViewById(R.id.buttonBackMain);
        buttonBack.setOnClickListener(this);
        buttonGetMyLocation.setOnClickListener(this);
        buttonGetDirection.setOnClickListener(this);

        client = LocationServices.getFusedLocationProviderClient(this);
    }

    // Lấy vị trí hiện tại
    private void getCurrentLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE);
                return;
            } else {
                Task<Location> task = client.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
                            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mainMapFrag);
                            mapFragment.getMapAsync(MainMapActivity.this);
                        }
                    }
                });
            }

        } catch (Exception e) {
            Log.d("Map Error", "#003" + e.getMessage());
            Toast.makeText(MainMapActivity.this, "#003", Toast.LENGTH_SHORT).show();
        }

    }

    // Tạo url để gọi API Google Map Direction
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        //Building the parameters to the webservice
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web services
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyCS2xm9XB-_87ELqkR9CpDKoER6n8kS2Vs";
        Log.d("urlGoogleMap", url);
        return url;
    }

    // sự kiện load map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(this);
        try {
            //Lấy vị trí hiện tại và load khu vực map tại vị trí đó
            LatLng latLngCurrent = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            place1 = new MarkerOptions().
                    position(latLngCurrent).
                    title("Vị trí hiện tại").
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrent, 15f));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bạn chưa bật quyền truy cập vị trí", Toast.LENGTH_SHORT).show();
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            // đánh dấu những vị trí giao hàng
            addDestinationPlace();

//            if (place2 != null) {
                findRoute(place1.getPosition(), place2.getPosition());
//            }
        } catch (Exception e) {
            Log.d("Map error", "#005" + e.getMessage());
            Toast.makeText(MainMapActivity.this, "#005", Toast.LENGTH_SHORT).show();
        }

    }

    //Vẽ tuyến đường
    @Override
    public void onTaskDone(Object... values) {
        try {
            if (currentPolyline != null) {
                currentPolyline.remove();
            }
            currentPolyline = googleMap.addPolyline((PolylineOptions) values[0]);
        } catch (Exception e) {
            Log.d("Map error", "#006" + e.getMessage());
            Toast.makeText(MainMapActivity.this, "#006", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonMainGetDirection:
                findRoute(place1.getPosition(), place2.getPosition());
                break;
            case R.id.buttonBackMain:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonMainGetCurrentLocation:
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                currentLocation = location;
                                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));
                            }
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 440);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 440) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    // khóa nút back
    @Override
    public void onBackPressed() {

    }

    // sự kiện nhấn vào những marker
    @Override
    public boolean onMarkerClick(Marker marker) {
//        Toast.makeText(this, ""+marker.getPosition().latitude+" - "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
        LatLng latLngCurrent = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        LatLng latLngDestination = marker.getPosition();
        findRoute(latLngCurrent, latLngDestination);
        return false;
    }
}