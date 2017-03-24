package midori.kitchen.profile.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import midori.kitchen.R;
import midori.kitchen.account.activity.LoginActivity;
import midori.kitchen.content.activity.HomeActivity;
import midori.kitchen.manager.AppPrefManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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

    private AppPrefManager appPrefManager;
    private String fullname, email, photo, phone;
    private Dialog logoutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appPrefManager = new AppPrefManager(this);

        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        getDataUser();
        initToolbar();
    }

    @OnClick(R.id.btn_save)
    public void onClick() {
        saveDataUser();
    }

    private void saveDataUser() {
        String editFullname = etName.getText().toString();
        String editEmail = etEmail.getText().toString();
        String editPhone = etPhone.getText().toString();
        if (validate(editFullname, editEmail, editPhone)) {
            appPrefManager.updateUser(editFullname, editEmail,editPhone);
            etName.clearFocus();
            etEmail.clearFocus();
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
}
