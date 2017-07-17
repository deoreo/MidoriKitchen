package midori.chef.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.chef.content.activity.HomeActivity;
import midori.chef.manager.AppController;
import midori.chef.manager.AppData;
import midori.chef.manager.AppPrefManager;
import midori.chef.manager.ConfigManager;
import midori.kitchen.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by BimoV on 3/9/2017.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etPhone)
    MaterialEditText etPhone;
    @BindView(R.id.etPassword)
    MaterialEditText etPassword;
    @BindView(R.id.btnRegister)
    TextView btnRegister;
    @BindView(R.id.btnLogin)
    FloatingActionButton btnLogin;

    private AppPrefManager appPrefManager;
    private ProgressDialog pDialog;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_chef);
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initProgressDialog();
        appPrefManager = new AppPrefManager(getApplicationContext());
        if (appPrefManager.getIsLoggedIn()) {
            launchHome();
        }
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    @OnClick({R.id.btnRegister, R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLogin:
                processLogin();
                break;
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void launchHome() {
        Intent intent1 = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent1);
        finish();
    }

    private boolean validateLogin(String number, String password) {
        boolean valid = true;
        if (number.isEmpty()) {
            etPhone.setError("Masukkan No Telepon Anda");
            valid = false;
        } else {
            etPhone.setError(null);
        }
        if (password.isEmpty()) {
            etPassword.setError("Masukkan Password Anda");
            valid = false;
        } else {
            etPassword.setError(null);
        }
       /* if (!number.equals(phoneNumber_admin) || !password.equals(password_admin)) {
            Toast.makeText(this, "No.Telepon atau Password anda salah", Toast.LENGTH_SHORT).show();
            valid = false;
        }*/
        return valid;
    }

    private void processLogin() {
        if (AppController.isConnected(getApplicationContext())) {
            requestLogin(ConfigManager.LOGIN);
        } else {

        }

    }

    private void requestLogin(String url) {
        String userNumber = etPhone.getText().toString().trim();
        String userPass = etPassword.getText().toString().trim();
        if (validateLogin(userNumber, userPass)) {
            showProgressDialog("Loading");
            Ion.with(getApplicationContext())
                    .load(url)
                    .setLogging("Login", Log.DEBUG)
                    .setHeader("Authorization", AppData.TOKEN)
                    .setBodyParameter("telepon", userNumber)
                    .setBodyParameter("password", userPass)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            try {
                                Log.d("HeaderCode", String.valueOf(result.getHeaders().code()));
                                if (result.getHeaders().code() == 200) {
                                    JsonObject object = result.getResult();
                                    boolean status = object.get("status").getAsBoolean();
                                    if (status) {
                                        String name = object.get("name").getAsString();
                                        String address = object.get("alamat").getAsString();
                                        String phone = object.get("telepon").getAsString();
                                        String key = object.get("apiKey").getAsString();

                                        appPrefManager.setUser(name, address, phone,key);
                                        appPrefManager.setIsLoggedIn(true);
                                        launchHome();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "No.Telpon atau Password anda salah!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            hideProgressDialog();

                        }
                    });
        }
    }

    private void initProgressDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
    }

    public void showProgressDialog(String content) {
        if (!pDialog.isShowing())
            pDialog.setMessage(content);
        pDialog.show();
    }

    public void hideProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }




}
