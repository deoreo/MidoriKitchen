package midori.kitchen.content.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import midori.kitchen.content.activity.BuyActivity;
import midori.kitchen.content.activity.ChangeLocationActivity;
import midori.kitchen.content.activity.LocationActivity;
import midori.kitchen.content.model.MenuModel;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.AppPrefManager;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class BuyReviewFragment extends Fragment {

    @BindView(R.id.tv_total_pay)
    TextView tvTotalPay;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.btn_plus)
    LinearLayout btnPlus;
    @BindView(R.id.tv_count_menu)
    TextView tvCountMenu;
    @BindView(R.id.btn_minus)
    LinearLayout btnMinus;
    @BindView(R.id.et_notes)
    MaterialEditText etNotes;
    @BindView(R.id.point)
    ImageView point;
    @BindView(R.id.et_address)
    public TextView etAddress;
    @BindView(R.id.notes)
    ImageView notes;
    @BindView(R.id.et_location_detail)
    MaterialEditText etLocationDetail;
    @BindView(R.id.delivery)
    TextView delivery;
    @BindView(R.id.tv_delivery_price)
    TextView tvDeliveryPrice;
    @BindView(R.id.et_promotion)
    MaterialEditText etPromotion;
    @BindView(R.id.layout_delivery)
    LinearLayout layoutDelivery;
    @BindView(R.id.spinner_delivery)
    Spinner spinnerDelivery;

    private boolean isKurir = true;
    private int deliveryPrice = AppData.priceDelivery;
    private BroadcastReceiver changeAlamat,changeDistance,changePriceDelivery;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_review, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initSpinnerDelivery();
        initButtonNext();
    }

    private void initButtonNext() {
        Button btnNext = (Button) getActivity().findViewById(R.id.btn_next);
        btnNext.setText(getString(R.string.next));
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = etAddress.getText().toString();
                String locationDetail = etLocationDetail.getText().toString();
                AppData.address = address;
                AppData.locationDetail = locationDetail;
                AppData.detail_address = AppData.address+" , "+AppData.locationDetail;
                AppData.note = etNotes.getText().toString();
                if(AppData.note.isEmpty()){
                    AppData.note = "tidak ada catatan pelanggan";
                }
                AppData.total_harga = ""+((AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price());
                AppData.status = "1";
                if(etPromotion.getText().toString().isEmpty()){
                    AppData.kupon_id = "0";
                }

                if (isKurir) {
                    AppData.delivery_id = "2";
                    if (validate(address, locationDetail)) {
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.container_buy, new BuyPaymentFragment(), AppData.buy_payment_tag).addToBackStack(null).commit();
                    }
                } else {
                    AppData.delivery_id = "1";
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .replace(R.id.container_buy, new BuyPaymentFragment(), AppData.buy_payment_tag).addToBackStack(null).commit();
                }


            }
        });
    }

    private void initSpinnerDelivery() {
        ArrayAdapter adapter_delivery = ArrayAdapter.createFromResource(getContext(), R.array.spinner_delivery, android.R.layout.simple_spinner_item);
        adapter_delivery.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDelivery.setAdapter(adapter_delivery);
        spinnerDelivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("Jasa Kurir")) {
                    isKurir = true;
                    deliveryPrice = AppData.priceDelivery;
                    AppData.menuModel.setDelivery_price(deliveryPrice);
                    int totalPay = (AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price();
                    AppData.menuModel.setTotal_pay(totalPay);
                    tvTotalPay.setText("Rp. " + AppData.menuModel.getTotal_pay());
                    layoutDelivery.setVisibility(View.VISIBLE);
                } else if (item.equals("Ambil Sendiri")) {
                    isKurir = false;
                    deliveryPrice = 0;
                    AppData.menuModel.setDelivery_price(deliveryPrice);
                    int totalPay = (AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price();
                    AppData.menuModel.setTotal_pay(totalPay);
                    tvTotalPay.setText("Rp. " + AppData.menuModel.getTotal_pay());
                    layoutDelivery.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean validate(String address, String locationDetail) {
        boolean valid = true;
        if (address.isEmpty()) {
            etAddress.setError("Enter address");
            valid = false;
        } else {
            etAddress.setError(null);
        }
        if (locationDetail.isEmpty()) {
            etLocationDetail.setError("Enter location detail");
            valid = false;
        } else {
            etLocationDetail.setError(null);
        }
        return valid;
    }

    private void initView() {

        AppData.menuModel.setTotal_menu(1);
        AppData.menuModel.setDelivery_price(deliveryPrice);
        tvMenu.setText(AppData.menuModel.getMenu());
        tvDistance.setText((int)AppData.distance+" KM");

        tvPrice.setText("Rp. " + AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu());
        tvMenu.setText(AppData.menuModel.getMenu());
        tvTotalPay.setText("Rp. " + ((AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price()));
        tvCountMenu.setText(String.valueOf(AppData.menuModel.getTotal_menu()));
        tvDeliveryPrice.setText("Rp. " + AppData.menuModel.getDelivery_price());
        AppData.total_harga = ""+((AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price());

        changeAlamat = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("", "broadcast changeAlamat");
                etAddress.setText(AppPrefManager.getInstance(getActivity()).getAlamat());
            }
        };


        changeDistance = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("", "broadcast changeDistance");
                tvDistance.setText(AppData.distance+" KM");
            }
        };

        changePriceDelivery = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("", "broadcast changeDistance");

                deliveryPrice = AppData.priceDelivery;
                tvDeliveryPrice.setText("Rp. "+deliveryPrice);
                AppData.menuModel.setDelivery_price(deliveryPrice);
                tvTotalPay.setText("Rp. " + ((AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price()));
                AppData.total_harga = ""+((AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price());
            }
        };

//        if(AppPrefManager.getInstance(getActivity()).getAlamat()!=""){
//            etAddress.setText(AppPrefManager.getInstance(getActivity()).getAlamat());
//        }


    }

    @OnClick({R.id.btn_plus, R.id.btn_minus, R.id.et_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_plus:
                plusCountMenu();
                break;
            case R.id.btn_minus:
                minusCountMenu();
                break;
            case R.id.et_address:
                gotoLocation();
                break;
        }
    }

    private void plusCountMenu() {
        int total = AppData.menuModel.getTotal_menu();
        total = total + 1;
        AppData.menuModel.setTotal_menu(total);
        tvCountMenu.setText(String.valueOf(AppData.menuModel.getTotal_menu()));
        Log.d("totalCountMenu", "" + AppData.menuModel.getTotal_menu());
        int totalPay = (AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price();
        int price = AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu();
        AppData.menuModel.setTotal_pay_menu(price);
        AppData.menuModel.setTotal_pay(totalPay);
        tvPrice.setText("Rp. " + AppData.menuModel.getTotal_pay_menu());
        tvTotalPay.setText("Rp. " + AppData.menuModel.getTotal_pay());
        Log.d("totalPay", "" + totalPay);
    }

    private void minusCountMenu() {
        int total = AppData.menuModel.getTotal_menu();
        if (total > 1) {
            total = total - 1;
            AppData.menuModel.setTotal_menu(total);
            tvCountMenu.setText(String.valueOf(AppData.menuModel.getTotal_menu()));
            Log.d("totalCountMenu", "" + AppData.menuModel.getTotal_menu());
            int totalPay = (AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price();
            int price = AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu();
            AppData.menuModel.setTotal_pay_menu(price);
            AppData.menuModel.setTotal_pay(totalPay);
            tvPrice.setText("Rp. " + AppData.menuModel.getTotal_pay_menu());
            tvTotalPay.setText("Rp. " + AppData.menuModel.getTotal_pay());;
            Log.d("totalPay", "" + totalPay);
        } else {
            getActivity().finish();
        }
    }

    private void gotoLocation(){
        Intent j = new Intent(getActivity(), ChangeLocationActivity.class);
        startActivity(j);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(changeAlamat,
                new IntentFilter("changeAlamat"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(changeDistance,
                new IntentFilter("changeDistance"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(changePriceDelivery,
                new IntentFilter("changePriceDelivery"));

    }
}
