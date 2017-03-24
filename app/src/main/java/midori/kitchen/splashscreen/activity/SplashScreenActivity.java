package midori.kitchen.splashscreen.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;
import midori.kitchen.account.activity.LoginActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.kbv_background)
    KenBurnsView kbvBackground;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_welcome_title)
    TextView tvWelcomeTitle;
    @BindView(R.id.tv_welcome_description)
    TextView tvWelcomeDescription;
    @BindView(R.id.layout_welcome)
    LinearLayout layoutWelcome;
    @BindView(R.id.pb_welcome)
    ProgressBar pbWelcome;

    private int mWaited = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        animateBackground();
        animateTransitionLogo();
        animateFadeLogo();
        animateTextWelcome();
        loadingProgressBar();
    }

    private void loadingProgressBar() {
        final Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i <= 1000; i++) {
                        sleep(4);
                        pbWelcome.setProgress(mWaited / 10);
                        mWaited += 1;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            }

        };
        splashThread.start();

    }

    private void animateBackground() {
        kbvBackground.setImageResource(R.drawable.bg_splash_screen);
        RandomTransitionGenerator generator = new RandomTransitionGenerator(10000, new AccelerateDecelerateInterpolator());
        kbvBackground.setTransitionGenerator(generator);
    }

    private void animateTransitionLogo() {
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(ivLogo, "scaleX", 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(1200);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(ivLogo, "scaleY", 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(1200);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(ivLogo, "alpha", 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(1200);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.setStartDelay(500);
        animatorSet.start();
    }

    private void animateFadeLogo() {
        ivLogo.setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);
        ivLogo.startAnimation(anim);
    }

    private void animateTextWelcome() {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(layoutWelcome, "alpha", 0.0F, 1.0F);
        alphaAnimation.setStartDelay(2000);
        alphaAnimation.setDuration(500);
        alphaAnimation.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
