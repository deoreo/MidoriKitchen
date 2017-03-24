package midori.kitchen.content.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import midori.kitchen.manager.AppData;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class BuyReviewFragment extends Fragment {

    @BindView(R.id.tv_total_pay)
    TextView tvTotalPay;
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
    MaterialEditText etAddress;
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
    private int deliveryPrice = 10000;

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
                if (isKurir) {
                    if (validate(address, locationDetail)) {
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.container_buy, new BuyPaymentFragment(), AppData.buy_payment_tag).addToBackStack(null).commit();
                    }
                } else {
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
                    deliveryPrice = 10000;
                    AppData.buyModel.setDelivery_price(deliveryPrice);
                    int totalPay = (AppData.buyModel.getPrice_menu() * AppData.buyModel.getTotal_menu()) + AppData.buyModel.getDelivery_price();
                    AppData.buyModel.setTotal_pay(totalPay);
                    tvTotalPay.setText("Rp. " + AppData.buyModel.getTotal_pay());
                    layoutDelivery.setVisibility(View.VISIBLE);
                } else if (item.equals("Ambil Sendiri")) {
                    isKurir = false;
                    deliveryPrice = 0;
                    AppData.buyModel.setDelivery_price(deliveryPrice);
                    int totalPay = (AppData.buyModel.getPrice_menu() * AppData.buyModel.getTotal_menu()) + AppData.buyModel.getDelivery_price();
                    AppData.buyModel.setTotal_pay(totalPay);
                    tvTotalPay.setText("Rp. " + AppData.buyModel.getTotal_pay());
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
        AppData.buyModel.setDelivery_price(deliveryPrice);
        tvMenu.setText(AppData.buyModel.getMenu());
        tvPrice.setText("Rp. " + AppData.buyModel.getPrice_menu() * AppData.buyModel.getTotal_menu());
        tvMenu.setText(AppData.buyModel.getMenu());
        tvTotalPay.setText("Rp. " + ((AppData.buyModel.getPrice_menu() * AppData.buyModel.getTotal_menu()) + AppData.buyModel.getDelivery_price()));
        tvCountMenu.setText(String.valueOf(AppData.buyModel.getTotal_menu()));
        tvDeliveryPrice.setText("Rp. " + AppData.buyModel.getDelivery_price());
    }

    @OnClick({R.id.btn_plus, R.id.btn_minus})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_plus:
                plusCountMenu();
                break;
            case R.id.btn_minus:
                minusCountMenu();
                break;
        }
    }

    private void plusCountMenu() {
        int total = AppData.buyModel.getTotal_menu();
        total = total + 1;
        AppData.buyModel.setTotal_menu(total);
        tvCountMenu.setText(String.valueOf(AppData.buyModel.getTotal_menu()));
        Log.d("totalCountMenu", "" + AppData.buyModel.getTotal_menu());
        int totalPay = (AppData.buyModel.getPrice_menu() * AppData.buyModel.getTotal_menu()) + AppData.buyModel.getDelivery_price();
        int price = AppData.buyModel.getPrice_menu() * AppData.buyModel.getTotal_menu();
        AppData.buyModel.setTotal_pay_menu(price);
        AppData.buyModel.setTotal_pay(totalPay);
        tvPrice.setText("Rp. " + AppData.buyModel.getTotal_pay_menu());
        tvTotalPay.setText("Rp. " + AppData.buyModel.getTotal_pay());
        Log.d("totalPay", "" + totalPay);
    }

    private void minusCountMenu() {
        int total = AppData.buyModel.getTotal_menu();
        if (total > 1) {
            total = total - 1;
            AppData.buyModel.setTotal_menu(total);
            tvCountMenu.setText(String.valueOf(AppData.buyModel.getTotal_menu()));
            Log.d("totalCountMenu", "" + AppData.buyModel.getTotal_menu());
            int totalPay = (AppData.buyModel.getPrice_menu() * AppData.buyModel.getTotal_menu()) + AppData.buyModel.getDelivery_price();
            int price = AppData.buyModel.getPrice_menu() * AppData.buyModel.getTotal_menu();
            AppData.buyModel.setTotal_pay_menu(price);
            AppData.buyModel.setTotal_pay(totalPay);
            tvPrice.setText("Rp. " + AppData.buyModel.getTotal_pay_menu());
            tvTotalPay.setText("Rp. " + AppData.buyModel.getTotal_pay());;
            Log.d("totalPay", "" + totalPay);
        } else {
            getActivity().finish();
        }
    }
}
