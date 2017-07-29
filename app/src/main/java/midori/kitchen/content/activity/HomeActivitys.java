package midori.kitchen.content.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import midori.kitchen.R;
import midori.kitchen.content.fragment.HistoryFragment;
import midori.kitchen.content.fragment.MenuFragment;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.AppPrefManager;
import midori.kitchen.profile.activity.ProfileActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by M. Asrof Bayhaqqi on 3/10/2017.
 */

public class HomeActivitys extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.container)
    FrameLayout container;

    private AppPrefManager appPrefManager;
    private String fullname, email, photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appPrefManager = new AppPrefManager(this);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        getDataUser();
        initToolbar();
        initDefaultFragment();
        initDrawer();
    }

    private void getDataUser() {
        fullname = appPrefManager.getUser().get(AppPrefManager.KEY_FULLNAME);
        email = appPrefManager.getUser().get(AppPrefManager.KEY_EMAIL);
        photo = appPrefManager.getUser().get(AppPrefManager.KEY_PHOTO);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initDefaultFragment() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.container, new MenuFragment(), AppData.menu_tag).commit();
    }

    private void displayFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.container, fragment, tag).addToBackStack(null).commit();
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        View headerLayout = navView.getHeaderView(0);
        LinearLayout layoutNavHeader = (LinearLayout) headerLayout.findViewById(R.id.layout_nav_header);
        TextView tvFullname = (TextView) headerLayout.findViewById(R.id.tv_fullname);
        TextView tvEmail = (TextView) headerLayout.findViewById(R.id.tv_email);
        CircleImageView civPhoto = (CircleImageView) headerLayout.findViewById(R.id.civ_photo);

        layoutNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivitys.this, ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        tvFullname.setText(fullname
        );
        if (!email.isEmpty()) {
            tvEmail.setText(email);
        } else {
            tvEmail.setVisibility(View.GONE);
        }
        Glide
                .with(this)
                .load(photo)
                .centerCrop()
                .placeholder(R.drawable.ic_avatar)
                .crossFade()
                .into(civPhoto);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                for (int entry = 0; entry < fm.getBackStackEntryCount(); entry++) {
                    getSupportFragmentManager().popBackStack();
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            displayFragment(new MenuFragment(), AppData.menu_tag);
        } else if (id == R.id.nav_history) {
            displayFragment(new HistoryFragment(), AppData.history_tag);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
