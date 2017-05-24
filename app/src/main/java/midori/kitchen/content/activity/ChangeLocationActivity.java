package midori.kitchen.content.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import midori.kitchen.R;
import midori.kitchen.content.adapter.AdapterSuggestion;
import midori.kitchen.content.model.GeocodeModel;
import midori.kitchen.content.model.PlaceModel;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.AppPrefManager;
import midori.kitchen.manager.GoogleAPIManager;
import midori.kitchen.manager.JSONControl;
import midori.kitchen.manager.PermissionUtils;
import midori.kitchen.manager.TouchableWrapper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ChangeLocationActivity extends AppCompatActivity
        implements OnMapClickListener,
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener

{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private TextView lblChange, btnSimpan, txtAddress;
    private ImageView btnBack;
    private RelativeLayout wrapperSimpan;
    private static Activity mActivity;
    private GoogleMap mGoogleMap;
    private String strDetail;
    private LatLng posTemp;
    public static Marker markerTemp, markerIbu;
    AppPrefManager applicationManager;
    ProgressDialog progressDialog;
    private BroadcastReceiver changeHouseLocation, changeOfficeLocation;
    public static boolean mTouchMap = true;
    private static RelativeLayout layoutSuggestion;
    private ProgressBar pSuggestion;
    private FrameLayout itemCurrent;
    private ListView mListView;
    private List<PlaceModel> LIST_PLACE = null;
    private String description = "";
    private static final String KEY_ID = "place_id";
    private static final String KEY_DESCRIPTION = "description";
    private PlaceModel mPlace;
    private AdapterSuggestion mAdapter;
    private LinearLayout layoutfillForm;
    private MapView mapView;
    protected View mOriginalContentView;
    public TouchableWrapper mTouchView;
    LatLng ibuLocation = new LatLng(AppData.menuModel.getIbuLat(), AppData.menuModel.getIbuLon());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.change_location);
        mActivity = this;
        btnSimpan = (TextView) findViewById(R.id.btnSimpan);
        wrapperSimpan = (RelativeLayout) findViewById(R.id.wrapperSimpan);
        lblChange = (TextView) findViewById(R.id.txtLocation);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        pSuggestion = (ProgressBar) findViewById(R.id.progressSuggestion);
        layoutSuggestion = (RelativeLayout) findViewById(R.id.layoutSuggestion);
        itemCurrent = (FrameLayout) findViewById(R.id.itemCurrent);
        mListView = (ListView) findViewById(R.id.lvSuggestion);
        layoutfillForm = (LinearLayout) findViewById(R.id.layoutfillForm);
        Log.v("responseIbu", ibuLocation.toString());
//
//        mapView = (MapView) findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
        //mapView.getMapAsync(this);

        MapFragment mySupportMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        mySupportMapFragment.getMapAsync(this);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtAddress.getText().toString().isEmpty() && AppData.latLngDelivery != null) {
                    AppPrefManager.getInstance(mActivity).setAlamat(txtAddress.getText().toString());
                    SendBroadcast("changeAlamat", txtAddress.getText().toString());
                    finish();
                }

            }
        });

        wrapperSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtAddress.getText().toString().isEmpty() && AppData.latLngDelivery != null && AppData.distance<18000) {
                    AppPrefManager.getInstance(mActivity).setAlamat(txtAddress.getText().toString());
                    SendBroadcast("changeAlamat", txtAddress.getText().toString());
                    finish();
                }
                else if(AppData.distance>=18000){
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_midori);
                    new MaterialDialog.Builder(mActivity)
                            .title("Mohon maaf...")
                            .content("Jarak pengiriman max. 17999 Km")
                            .positiveText("OK")
                            .icon(drawable)
                            .typeface("GothamRnd-Book.otf","GothamRnd-Light.otf" )
                            .show();
                }
            }
        });

        layoutfillForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtAddress.getText().toString().isEmpty() && AppData.latLngDelivery != null) {
                    AppPrefManager.getInstance(mActivity).setAlamat(txtAddress.getText().toString());
                    SendBroadcast("changeAlamat", txtAddress.getText().toString());
                    finish();
                }
            }
        });

        txtAddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //do stuff here
                    mTouchMap = false;

                }
                return false;
            }
        });

        txtAddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (txtAddress.getText().toString().equals("")) {
                    if (markerTemp != null) {
                        markerTemp.remove();
                        posTemp = null;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!mTouchMap) {
                    if (s.length() >= 3) {
                        new GetSuggestion(mActivity, s.toString()).execute();
                    } else if (s.length() == 0) {
                        layoutSuggestion.setVisibility(GONE);
                    }
                }


            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();

    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }


    public String getAddress(Activity activity, LatLng latlng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        double lat = latlng.latitude;
        double lng = latlng.longitude;
        String addressLine = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            addressLine = obj.getAddressLine(0);
            strDetail = obj.getAddressLine(1) + " , " + obj.getAddressLine(2);


        } catch (IOException e) {
        } catch (Exception e) {
        }
        return addressLine;
    }



    public void GetUserLocation(LatLng latLng) {
        mGoogleMap.clear();
        posTemp = latLng;
        markerTemp = mGoogleMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                txtAddress.setText(getAddress(mActivity, posTemp));
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        new GetMyLocation(mActivity, mGoogleMap).execute();

    }



    @Override
    public void onMapClick(final LatLng latLng) {

        Log.d("map click", "Map Click");
        mGoogleMap.clear();
        posTemp = latLng;
        AppData.latLngDelivery = latLng;
        float zoom = mGoogleMap.getCameraPosition().zoom;
        if (zoom <= 15) {
            zoom = 15;
        }
        mGoogleMap.addMarker(
                new MarkerOptions()
                        .position(ibuLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ibu)));

        mGoogleMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));

        Document doc = GoogleAPIManager.getRoute(ibuLocation, AppData.latLngDelivery, "driving");
        String strDistance = GoogleAPIManager.getDistanceText(doc);
        AppData.distance = Double.parseDouble(strDistance.split(" ")[0]);
        CalculatePrice(AppData.distance);
        SendBroadcast("changeDistance", ""+AppData.distance);

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {

                String address = getAddress(mActivity, posTemp);
                AppData.latLngDelivery = latLng;
                String detail = strDetail;
                txtAddress.setText(address);

            }

            @Override
            public void onCancel() {
            }
        });
        layoutSuggestion.setVisibility(GONE);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mGoogleMap != null) {
            // Access to the location has been granted to the app.
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private class GetMyLocation extends AsyncTask<String, Void, String> implements LocationListener {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;
        private double latitude, longitude;
        private GoogleMap googleMap;
        private LocationManager locationManager;
        private String address, detail;

        public GetMyLocation(Activity activity, GoogleMap googleMap) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
            this.googleMap = googleMap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Memuat lokasi anda. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            try {
                try {
                    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
                    if (status != ConnectionResult.SUCCESS) {
                        int requestCode = 10;
                    } else {
                        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        Criteria criteria = new Criteria();
                        String provider = locationManager.getBestProvider(criteria, true);
                        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return "OK";
                        }
                        Location location;
                        if (!isGPSEnabled && !isNetworkEnabled) {
                            return "FAIL";
                        } else {
                            if (isNetworkEnabled) {
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this, Looper.getMainLooper());
                                Log.v("locationManager", "Network");
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                        address = getAddress(activity, new LatLng(latitude, longitude));
                                        detail = strDetail;
                                    }
                                }
                            }
                            if (isGPSEnabled) {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, Looper.getMainLooper());
                                Log.v("locationManager", "GPS");
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                        address = getAddress(activity, new LatLng(latitude, longitude));
                                        detail = strDetail;

                                    }
                                }
                            }
                        }
                    }

                    //Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("posisi gps", "onPost");
            progressDialog.dismiss();
            switch (result) {
                case "FAIL":
                    break;
                case "OK":
                    try {
                        LatLng currentLocation = new LatLng(latitude,longitude);
                        AppData.latLngDelivery = currentLocation;
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        googleMap.addMarker(
                                new MarkerOptions()
                                        .position(ibuLocation)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ibu)));


                            googleMap.addMarker(
                                    new MarkerOptions()
                                            .position(currentLocation)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));


                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 15);
                        googleMap.animateCamera(cameraUpdate);
                        txtAddress.setText(address);

                        Document doc = GoogleAPIManager.getRoute(ibuLocation, AppData.latLngDelivery, "driving");
                        String strDistance = GoogleAPIManager.getDistanceText(doc);
                        AppData.distance = Double.parseDouble(strDistance.split(" ")[0]);
                        CalculatePrice(AppData.distance);
                        SendBroadcast("changeDistance", ""+AppData.distance);

                        Log.v("posisi gps", currentLocation.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }


        }


        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
    @Override
    public final void onPause()
    {
        super.onPause();
    }

    @Override
    public final void onLowMemory()
    {
        super.onLowMemory();
    }

    @Override
    public final void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        Log.i("adding receiver", "fragment ontainer for profile");
        LocalBroadcastManager.getInstance(this).registerReceiver(changeHouseLocation,
                new IntentFilter("changeHouseLocation"));
        LocalBroadcastManager.getInstance(this).registerReceiver(changeOfficeLocation,
                new IntentFilter("changeOfficeLocation"));

    }


    public class GetSuggestion extends AsyncTask<String, Void, JSONArray> {
        String address, tag;
        Activity activity;
        LatLng latLng;
        public GetSuggestion(Activity activity, String address) {
            this.address = address;
            this.tag = tag;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            layoutSuggestion.setVisibility(VISIBLE);
            pSuggestion.setVisibility(VISIBLE);
            itemCurrent.setVisibility(GONE);
            mListView.setVisibility(VISIBLE);
            mListView.setAdapter(null);
        }

        @Override
        protected JSONArray doInBackground(String... arg) {
            JSONArray json = null;
            LIST_PLACE = new ArrayList<PlaceModel>();
            JSONControl JSONControl = new JSONControl();
            try {
                json = JSONControl.listPlace(address);
            } catch (Exception e) {
            }
            if (json != null) {
                for (int i = 0; i < json.length(); i++) {
                    String id = "";
                    description = "";
                    address = "";
                    String detail = "";
                    boolean status = true;
                    try {
                        JSONObject jsonObject = json.getJSONObject(i);
                        id = jsonObject.getString(KEY_ID);
                        description = jsonObject.getString(KEY_DESCRIPTION);
                        String[] descSplit = description.split(",");
                        address = descSplit[0]+ "," + descSplit[1];
                        detail = descSplit[1] + "," + descSplit[2];
                        status = true;

                    } catch (JSONException e) {
                    } catch (Exception e) {

                    }

                    if (status) {
                        mPlace = new PlaceModel(id, address, detail);
                        LIST_PLACE.add(mPlace);
                    }
                }
                try {
                    mAdapter = new AdapterSuggestion(activity, LIST_PLACE);
                } catch (NullPointerException e) {
                }
            } else {
                LIST_PLACE = null;
            }

            return json;
        }

        @Override
        protected void onPostExecute(final JSONArray json) {
            // TODO Auto-generated method stub
            super.onPostExecute(json);
            pSuggestion.setVisibility(GONE);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        mTouchMap = true;
                        mGoogleMap.clear();
                        PlaceModel selectedPlace = LIST_PLACE.get(position);
                        txtAddress.setText(selectedPlace.getAddress());
                        if (markerTemp != null) {
                            markerTemp.remove();
                        }
                        layoutSuggestion.setVisibility(GONE);
                        hideKeyboard();
                        GeocodeModel geocode = GoogleAPIManager.geocode(selectedPlace.getAddress());
                        latLng = new LatLng(geocode.getLat(), geocode.getLon());
                        AppData.latLngDelivery = latLng;
                            markerTemp = mGoogleMap.addMarker(
                                    new MarkerOptions()
                                            .position(latLng)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));

                        mGoogleMap.addMarker(
                                new MarkerOptions()
                                        .position(ibuLocation)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ibu)));

                        Document doc = GoogleAPIManager.getRoute(ibuLocation, AppData.latLngDelivery, "driving");
                        String strDistance = GoogleAPIManager.getDistanceText(doc);
                        AppData.distance = Double.parseDouble(strDistance.split(" ")[0]);
                        CalculatePrice(AppData.distance);
                        SendBroadcast("changeDistance", ""+AppData.distance);

                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {

                                String address = getAddress(activity, latLng);
                                String detail = strDetail;
                                txtAddress.setText(address);

                            }

                            @Override
                            public void onCancel() {
                            }
                        });


                    } catch (Exception e) {
                        layoutSuggestion.setVisibility(GONE);
                        hideKeyboard();
                    }
                }
            });

        }
    }


    private void SendBroadcast(String typeBroadcast, String type) {
    Intent intent = new Intent(typeBroadcast);
    // add data
    intent.putExtra("message", type);
    LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
}

    private void CalculatePrice(Double distance){
        int price = 0;
        if(distance<=3.9){
            price = 8000;
        } else if(distance>=4.0 && distance <=15){
            double selisihjarak = distance - 4.0;
            double hargaselisihjarak = (Math.round(selisihjarak*2)/2.0) * 2000;
            price = 8000 + (int)hargaselisihjarak;
        } else if(distance>=15){
            price = 30000;
        }
        AppData.priceDelivery = price;
        SendBroadcast("changePriceDelivery", ""+AppData.priceDelivery);
    }


    public static void hideKeyboard() {
        View view = mActivity.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            layoutSuggestion.setVisibility(GONE);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
