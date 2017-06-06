package midori.kitchen.content.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;
import midori.kitchen.content.fragment.MenuDetailFragment;
import midori.kitchen.content.fragment.ResepDetailFragment;
import midori.kitchen.manager.AppData;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class ResepDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title_bar)
    TextView tvTitleBar;
    @BindView(R.id.container_detail)
    FrameLayout containerDetail;
    @BindView(R.id.toolbar_detail)
    Toolbar toolbarDetail;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    private String tag_detail, name_detail;
    private String id, menu, resep, cara_buat, url, photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            tag_detail = bundle.getString("tag_detail");
            name_detail = bundle.getString("name_detail");
            if (tag_detail.equals(AppData.resep_tag)) {
                menu = AppData.resepModel.getResep();
                url = AppData.resepModel.getUrl();
                cara_buat = AppData.resepModel.getCara_buat();
                photo = AppData.resepModel.getImage();
                displayFragment(new ResepDetailFragment(),tag_detail );
            }
            initToolbar();
        }
    }

    private void displayFragment(Fragment fragment, String tag) {
       getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_detail, fragment, tag).commit();

    }

    private void initToolbar() {
        setSupportActionBar(toolbarDetail);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tvTitleBar.setText(name_detail);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResepDetailActivity.this, HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
