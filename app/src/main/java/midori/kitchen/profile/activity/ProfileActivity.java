package midori.kitchen.profile.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import fr.ganfra.materialspinner.MaterialSpinner;
import midori.kitchen.R;
import midori.kitchen.account.activity.LoginActivity;
import midori.kitchen.content.activity.DeliveryActivity;
import midori.kitchen.content.activity.HomeActivity;
import midori.kitchen.content.fragment.BuyPaymentFragment;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.AppPrefManager;
import midori.kitchen.manager.JSONControl;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.layout.simple_spinner_item;
import static midori.kitchen.R.id.et_province;

/**
 * Created by M. Asrof Bayhaqqi on 3/10/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.civ_photo)
    CircleImageView civPhoto;
    @BindView(R.id.et_name)
    MaterialEditText etName;
    @BindView(R.id.btn_name)
    LinearLayout btnName;
    @BindView(R.id.et_email)
    MaterialEditText etEmail;
    @BindView(R.id.btn_email)
    LinearLayout btnEmail;
    @BindView(R.id.btn_save)
    LinearLayout btnSave;
    @BindView(R.id.tv_title_bar)
    TextView tvTitleBar;
    @BindView(R.id.btn_logout)
    ImageView btnLogout;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.et_phone)
    MaterialEditText etPhone;
    @BindView(R.id.et_address)
    TextView etAddress;


    private AppPrefManager appPrefManager;
    private String fullname, email, photo, phone;
    private Dialog logoutDialog;
    private Activity activity;
    private AlertDialog dialogAddress;
    private BroadcastReceiver changeAlamat;
    private String[] provincesArray, cities, areas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appPrefManager = new AppPrefManager(this);

        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        activity =this;
        getDataUser();
        initToolbar();
        changeAlamat = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("", "broadcast changeAlamat");
                dialogAddress.dismiss();
                InitDialogAddress();
            }
        };

    }

    @OnClick({R.id.btn_save, R.id.et_address})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_save:
                saveDataUser();
                break;
            case R.id.et_address:
                InitDialogAddress();
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(activity).registerReceiver(changeAlamat,
                new IntentFilter("changeAlamat"));

    }

    private void saveDataUser() {
        String editFullname = etName.getText().toString();
        String editEmail = etEmail.getText().toString();
        String editPhone = etPhone.getText().toString();
        if (validate(editFullname, editEmail, editPhone)) {
            appPrefManager.updateUser(editFullname, editEmail,editPhone);
            etName.clearFocus();
            etEmail.clearFocus();
            Drawable drawable = getResources().getDrawable(R.drawable.ic_midori);
            new MaterialDialog.Builder(activity)
                    .title("Informasi")
                    .content("Data Anda sudah tersimpan")
                    .positiveText("OK")
                    .icon(drawable)
                    .typeface("GothamRnd-Book.otf","GothamRnd-Light.otf" )
                    .show();
        }

    }

    private boolean validate(String fullname, String email, String phone) {
        boolean valid = true;
        if (fullname.isEmpty()) {
            etName.setError("Enter full name");
            valid = false;
        } else {
            etName.setError(null);
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter email");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches() || phone.length() < 6 || phone.length() > 13) {
            etPhone.setError("Enter phone");
            valid = false;
        } else {
            etPhone.setError(null);
        }
        return valid;
    }

    private void getDataUser() {
        fullname = appPrefManager.getUser().get(AppPrefManager.KEY_FULLNAME);
        email = appPrefManager.getUser().get(AppPrefManager.KEY_EMAIL);
        photo = appPrefManager.getUser().get(AppPrefManager.KEY_PHOTO);
        phone = appPrefManager.getUser().get(AppPrefManager.KEY_PHONE);

        etName.setText(fullname);
        etEmail.setText(email);
        etPhone.setText(phone);
        Glide
                .with(this)
                .load(photo)
                .centerCrop()
                .placeholder(R.drawable.ic_avatar)
                .crossFade()
                .into(civPhoto);
        String alamat =appPrefManager.getAlamat();
        String propinsi= appPrefManager.getProvince();
        String city = appPrefManager.getCity();
        String area = appPrefManager.getArea();
        String location_detail = appPrefManager.getLocationDetail();
        String delivery_address = alamat+" , "+area+" , "+city+" , "+propinsi+"\n( "+location_detail+" )";
        AppPrefManager.getInstance(activity).setDeliveryAddress(delivery_address);
        etAddress.setText(delivery_address);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tvTitleBar.setText(getString(R.string.profile));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog();
            }
        });
    }

    private void logoutDialog() {
        logoutDialog = new Dialog(this);
        logoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logoutDialog.setContentView(R.layout.dialog_yes_no);
        logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView message = (TextView) logoutDialog.findViewById(R.id.tv_message);
        message.setText(Html.fromHtml(getString(R.string.dialog_signout)));

        RelativeLayout btn_negative = (RelativeLayout) logoutDialog.findViewById(R.id.btn_negative);
        RelativeLayout btn_positive = (RelativeLayout) logoutDialog.findViewById(R.id.btn_positive);
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog.dismiss();
            }
        });
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appPrefManager.logout();
                launchLogin();
            }
        });

        logoutDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        launchHome();
    }

    private void launchHome() {
        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }


    private void InitDialogAddress() {
        dialogAddress = new AlertDialog.Builder(activity)
                .setTitle("")
                .setView(R.layout.popup_address)
                .create();
        if(!dialogAddress.isShowing()) {
            dialogAddress.show();
        }
        final TextView tvAddressMap = (TextView) dialogAddress.findViewById(R.id.et_map_address);
        final MaterialSpinner spinnerProvince = (MaterialSpinner) dialogAddress.findViewById(R.id.et_province);
        final MaterialSpinner spinnerCity = (MaterialSpinner) dialogAddress.findViewById(R.id.et_city);
        final MaterialSpinner spinnerArea = (MaterialSpinner) dialogAddress.findViewById(R.id.et_area);
        final EditText etLocationDetail = (EditText)dialogAddress.findViewById(R.id.et_location_detail);
        new GetProvinces("manual", spinnerProvince).execute();
        tvAddressMap.setText(AppPrefManager.getInstance(activity).getAlamat());

        tvAddressMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(activity, DeliveryActivity.class);
                startActivity(j);
            }
        });

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerProvince.getSelectedItem().toString().equalsIgnoreCase("province")) {
                    new GetCity("manual", spinnerProvince.getSelectedItem().toString(), spinnerCity).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerCity.getSelectedItem().toString().equalsIgnoreCase("city")) {
                    new GetArea("manual", spinnerProvince.getSelectedItem().toString(), spinnerCity.getSelectedItem().toString(), spinnerArea).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        etAddress = (TextView) dialogAddress.findViewById(R.id.et_address);
        // set the custom dialog components - text, image and button
//        txtMessage = (TextView) dialogAddress.findViewById(R.id.txtMessage);
//        txtEmail = (EditText) dialogLogin.findViewById(R.id.txtEmail);
//        txtPassword = (EditText) dialogLogin.findViewById(R.id.txtPassword);
        Button cancel = (Button) dialogAddress.findViewById(R.id.btnCancel);
        Button confirm = (Button) dialogAddress.findViewById(R.id.btnConfirm);
//
//        txtMessage.setText("");
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddress.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPrefManager.getInstance(activity).setAlamat(tvAddressMap.getText().toString());
                AppPrefManager.getInstance(activity).setProvince(spinnerProvince.getSelectedItem().toString());
                AppPrefManager.getInstance(activity).setCity(spinnerCity.getSelectedItem().toString());
                AppPrefManager.getInstance(activity).setArea(spinnerArea.getSelectedItem().toString());
                AppPrefManager.getInstance(activity).setLocationDetail(etLocationDetail.getText().toString());
                String alamat =appPrefManager.getAlamat();
                String propinsi= appPrefManager.getProvince();
                String city = appPrefManager.getCity();
                String area = appPrefManager.getArea();
                String location_detail = appPrefManager.getLocationDetail();
                String delivery_address = alamat+" , "+area+" , "+city+" , "+propinsi+"\n( "+location_detail+" )";
                AppPrefManager.getInstance(activity).setDeliveryAddress(delivery_address);
                etAddress.setText(delivery_address);
                dialogAddress.dismiss();
            }
        });
    }

    private void launchLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private class GetProvinces extends AsyncTask<String, Void, String> {
        private MaterialSpinner spinner;
        private  String TAG;
        private ProgressDialog progressDialog;
        public GetProvinces(String TAG, MaterialSpinner spinner){
            this.TAG = TAG;
            this.spinner = spinner;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Memuat Propinsi. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.BukalapakGetProvinces();
                JSONArray responseJSONArray = response.getJSONArray("provinces");
                provincesArray = new String[responseJSONArray.length()];
                for(int i =0; i< responseJSONArray.length();i++){
                    provincesArray[i] = responseJSONArray.getString(i);
                }

                return "OK";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            switch (result) {
                case "FAIL":
                    break;
                case "OK":
                    if(TAG == "manual") {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, simple_spinner_item, provincesArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                    break;
            }
        }
    }
    private class GetCity extends AsyncTask<String, Void, String> {
        private MaterialSpinner spinner;
        private ProgressDialog progressDialog;
        private  String TAG,province;
        public GetCity(String TAG, String province, MaterialSpinner spinner){
            this.TAG = TAG;
            this.province = province;
            this.spinner = spinner;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Memuat Kota. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.BukalapakGetCity(province);
                JSONArray responseJSONArray = response.getJSONArray("cities");
                cities = new String[responseJSONArray.length()];
                for(int i =0; i< responseJSONArray.length();i++){
                    cities[i] = responseJSONArray.getString(i);
                }
                return "OK";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            switch (result) {
                case "FAIL":
                    break;

                case "OK":
                    if(TAG == "manual") {
                        try {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, simple_spinner_item, cities);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                        }catch (Exception e){}
                    }
                    break;
            }
        }
    }
    private class GetArea extends AsyncTask<String, Void, String> {
        private MaterialSpinner spinner;
        private ProgressDialog progressDialog;
        private  String TAG, province, city;
        public GetArea(String TAG, String province, String city, MaterialSpinner spinner){
            this.TAG = TAG;
            this.province = province;
            this.city = city;
            this.spinner = spinner;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Memuat Area. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.BukalapakGetArea();
                JSONObject responseAddress = response.getJSONObject("address");
                JSONObject responseProvince = responseAddress.getJSONObject(province);
                JSONArray responseCity = responseProvince.getJSONArray(city);
                areas = new String[responseCity.length()];
                for(int i=0;i<responseCity.length();i++){
                    areas[i] = responseCity.getString(i);
                }

                return "OK";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            switch (result) {
                case "FAIL":
                    break;

                case "OK":
                    if(TAG == "manual") {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, simple_spinner_item, areas);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                    break;
            }
        }
    }
}
