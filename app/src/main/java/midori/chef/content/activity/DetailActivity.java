package midori.chef.content.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by BimoV on 3/10/2017.
 */

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.content)
    CardView content;
    @BindView(R.id.backButton)
    ImageView backButton;
    @BindView(R.id.content_note)
    CardView contentNote;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.tvOrder)
    TextView tvOrder;
    @BindView(R.id.tvHarga)
    TextView tvHarga;
    @BindView(R.id.tvNote)
    TextView tvNote;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_chef);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportPostponeEnterTransition();
            // Call some material design APIs here
        }
        getData();

    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String menu = bundle.getString("menu");
            String order = bundle.getString("order");
            String harga = bundle.getString("harga");
            String note = bundle.getString("note");
            String img = bundle.getString("image");
            String status = bundle.getString("status");

            tvMenu.setText(menu);
            tvOrder.setText(order);
            tvHarga.setText(formatRupiah(Double.parseDouble(harga)));
            tvNote.setText(note);
            tvStatus.setText(status);

            Glide.with(DetailActivity.this)
                    .load(img)
                    .centerCrop()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                supportStartPostponedEnterTransition();
                                // Call some material design APIs here
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                supportStartPostponedEnterTransition();
                                // Call some material design APIs here
                            }
                            return false;
                        }
                    })
                    .into(image);
        }
    }

    private String formatRupiah(double kurs) {
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(formatRp);
        return decimalFormat.format(kurs);
    }

    @OnClick({R.id.backButton, R.id.btnCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.btnCancel:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
        super.onBackPressed();
    }
}
