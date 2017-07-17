package midori.chef.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.chef.manager.ChefPrefManager;
import midori.kitchen.R;
import midori.chef.manager.AppController;
import midori.chef.manager.AppData;
import midori.chef.manager.ConfigManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by BimoV on 3/9/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.logo)
    RelativeLayout logo;
    @BindView(R.id.etName)
    MaterialEditText etName;
    @BindView(R.id.etPhone)
    MaterialEditText etPhone;
    @BindView(R.id.etPassword)
    MaterialEditText etPassword;
    @BindView(R.id.etConfirmPassword)
    MaterialEditText etConfirmPassword;
    @BindView(R.id.btnStart)
    Button btnStart;
    @BindView(R.id.etAddress)
    MaterialEditText etAddress;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;

    private ChefPrefManager chefPrefManager;
    private String name;
    private String phone;
    private String address;
    private String password;
    private String confirm_password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        logo.bringToFront();
        chefPrefManager = new ChefPrefManager(getApplicationContext());
        //-7.9346969,112.6535343
    }


    private void dataRegister() {
        name = etName.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        address = etAddress.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirm_password = etConfirmPassword.getText().toString().trim();

        if (validateRegister(name, phone, address, password, confirm_password)) {
            //chefPrefManager.setUser("",name,email,address,phone,"");
            new GetMyLocation(this).execute();
        }

    }

    private void processRegister() {
        if (AppController.isConnected(getApplicationContext())) {
            sendRequestRegister(ConfigManager.REGISTER);
        } else {
            AppController.showErrorConnection(rootLayout);
        }
    }

    private void sendRequestRegister(String url) {
        Ion.with(getApplicationContext())
                .load(url)
                .setLogging("Register", Log.DEBUG)
                .setHeader("Authorization", AppData.TOKEN)
                .setBodyParameter("name", name)
                .setBodyParameter("telepon", phone)
                .setBodyParameter("password", password)
                .setBodyParameter("alamat", address)
                .setBodyParameter("lat", ""+ AppData.LokasiIbu.latitude)
                .setBodyParameter("lon", ""+ AppData.LokasiIbu.longitude)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        try {
                            Log.d("HeaderCode", String.valueOf(result.getHeaders().code()));
                            JsonObject response = result.getResult();
                            boolean status = response.get("status").getAsBoolean();
                            String message = response.get("message").getAsString();
                            Log.d("status", status + " : " + message);
                            if (status) {
                                onBackPressed();
                                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    private boolean validateRegister(String name, String phone, String address, String password, String confirm_password) {
        boolean valid = true;

        if (name.isEmpty()) {
            etName.setError("Masukkan Nama anda");
            valid = false;
        } else {
            etName.setError(null);
        }

        if (phone.isEmpty()) {
            etPhone.setError("Masukkan No.Telepon anda");
            valid = false;
        } else {
            etPhone.setError(null);
        }

        if (address.isEmpty()) {
            etAddress.setError("Masukkan Alamat anda");
            valid = false;
        } else {
            etAddress.setError(null);
        }

        if (password.isEmpty()) {
            etPassword.setError("Masukkan Password anda");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        if (!confirm_password.equals(password)) {
            etPassword.setError("Password dan Konfirmasi Password harus sama");
            etConfirmPassword.setError("Password dan Konfirmasi Password harus sama");
        } else {
            etPassword.setError(null);
            etConfirmPassword.setError(null);
        }
        return valid;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick({R.id.btnBack, R.id.btnStart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnStart:
                dataRegister();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class GetMyLocation extends AsyncTask<String, Void, String> implements LocationListener {
        private Activity activity;
        private Context context;
        private Resources resources;
        private ProgressDialog progressDialog;
        private double latitude, longitude;
        private LocationManager locationManager;
        private String address, detail;

        public GetMyLocation(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Melakukan registering. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            try {
                try {
                    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
                    if (status != ConnectionResult.SUCCESS) {
                        int requestCode = 10;
                    } else {
                        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        Criteria criteria = new Criteria();
                        String provider = locationManager.getBestProvider(criteria, true);
                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                        AppData.LokasiIbu = new LatLng(latitude,  longitude) ;
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
                                        AppData.LokasiIbu = new LatLng(latitude,  longitude) ;
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
            switch (result) {
                case "FAIL":
                    break;
                case "OK":
                    processRegister();
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

}
