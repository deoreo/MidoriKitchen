package midori.kitchen.content.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import midori.kitchen.content.fragment.BuyReviewFragment;
import midori.kitchen.manager.AppData;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class BuyActivity extends AppCompatActivity {

    @BindView(R.id.container_buy)
    FrameLayout containerBuy;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title_bar)
    TextView tvTitleBar;
    @BindView(R.id.toolbar_buy)
    Toolbar toolbarBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buy);
        ButterKnife.bind(this);

        initToolbar();
        displayFragment(new BuyReviewFragment(), AppData.buy_review_tag);
    }


    private void initToolbar() {
        setSupportActionBar(toolbarBuy);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tvTitleBar.setText(getString(R.string.payment));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void displayFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_buy, fragment, tag).commit();
    }

    @OnClick(R.id.btn_next)
    public void onClick() {

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        String fragmentTag = fragment.getTag();
        AppData.backstack_buy_tag = fragmentTag;
        Log.d("backstack_buy_tag", AppData.backstack_buy_tag);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
