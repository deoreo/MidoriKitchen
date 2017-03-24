package midori.kitchen.content.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import midori.kitchen.content.fragment.CheckoutDeliveryFragment;
import midori.kitchen.content.fragment.CheckoutPaymentFragment;
import midori.kitchen.content.fragment.CheckoutReviewFragment;
import midori.kitchen.content.fragment.CheckoutVoucherFragment;
import midori.kitchen.manager.AppData;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class CheckoutActivity extends AppCompatActivity {

    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.tv_bayar)
    TextView tvBayar;
    @BindView(R.id.line_delivery)
    View lineDelivery;
    @BindView(R.id.line_voucher)
    View lineVoucher;
    @BindView(R.id.line_payment)
    View linePayment;
    @BindView(R.id.container_checkout)
    FrameLayout containerCheckout;
    @BindView(R.id.line_review)
    View lineReview;

    private final int REVIEW = 0, DELIVERY = 1, VOUCHER = 2, PAYMENT = 3;
    private BroadcastReceiver goDelivery, goVoucher, goPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        displayFragment(REVIEW);

        goDelivery = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                displayFragment(DELIVERY);
            }
        };
        goVoucher = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                displayFragment(VOUCHER);
            }
        };
        goPayment = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                displayFragment(PAYMENT);
            }
        };

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void displayFragment(int position) {
        Fragment fragment = null;
        String tag = "";
        switch (position) {
            case REVIEW:
                fragment = new CheckoutReviewFragment();
                tag = AppData.checkout_review_tag;
                lineReview();
                break;
            case DELIVERY:
                fragment = new CheckoutDeliveryFragment();
                tag = AppData.checkout_delivery_tag;
                lineDelivery();
                break;
            case VOUCHER:
                fragment = new CheckoutVoucherFragment();
                tag = AppData.checkout_voucher_tag;
                lineVoucher();
                break;
            case PAYMENT:
                fragment = new CheckoutPaymentFragment();
                tag = AppData.checkout_payment_tag;
                linePayment();
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_checkout, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
        }

    }

    private void linePayment() {
        lineReview.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        lineDelivery.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        lineVoucher.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        linePayment.setBackgroundColor(getResources().getColor(R.color.colorGreen));
    }

    private void lineVoucher() {
        lineReview.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        lineDelivery.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        lineVoucher.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        linePayment.setBackgroundColor(getResources().getColor(R.color.colorGrey));
    }

    private void lineDelivery() {
        lineReview.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        lineDelivery.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        lineVoucher.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        linePayment.setBackgroundColor(getResources().getColor(R.color.colorGrey));
    }

    private void lineReview() {
        lineReview.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        lineDelivery.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        lineVoucher.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        linePayment.setBackgroundColor(getResources().getColor(R.color.colorGrey));
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        String fragmentTag = fragment.getTag();
        AppData.backstack_tag = fragmentTag;
        Log.d("backstack_tag", AppData.backstack_tag);
    }

    @Override
    public void onBackPressed() {
        if (AppData.backstack_tag.equals(AppData.checkout_review_tag)) {
            launchHome();
        } else if (AppData.backstack_tag.equals(AppData.checkout_delivery_tag)) {
            lineReview();
            displayFragment(REVIEW);
        } else if (AppData.backstack_tag.equals(AppData.checkout_voucher_tag)) {
            lineDelivery();
            displayFragment(DELIVERY);
        } else if (AppData.backstack_tag.equals(AppData.checkout_payment_tag)) {
            lineVoucher();
            displayFragment(VOUCHER);
        }
    }

    @OnClick(R.id.btn_close)
    public void onClick() {
        launchHome();
    }

    private void launchHome() {
        startActivity(new Intent(CheckoutActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(goDelivery,
                new IntentFilter("goDelivery"));
        LocalBroadcastManager.getInstance(this).registerReceiver(goVoucher,
                new IntentFilter("goVoucher"));
        LocalBroadcastManager.getInstance(this).registerReceiver(goPayment,
                new IntentFilter("goPayment"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(goDelivery);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(goVoucher);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(goPayment);
    }

}
