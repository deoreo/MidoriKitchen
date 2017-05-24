package midori.kitchen.content.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;
import midori.kitchen.account.model.ModelUser;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.JSONControl;

/**
 * Created by M. Asrof Bayhaqqi on 3/12/2017.
 */

public class BuyPaymentFragment extends Fragment {

    @BindView(R.id.rb_bank)
    RadioButton rbBank;
    @BindView(R.id.rb_cod)
    RadioButton rbCod;
    @BindView(R.id.tv_head)
    TextView tvHead;
    @BindView(R.id.tv_subhead)
    TextView tvSubhead;
    @BindView(R.id.bank_destination)
    TextView bankDestination;
    @BindView(R.id.tv_bank_destination)
    TextView tvBankDestination;
    @BindView(R.id.account_name)
    TextView accountName;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    @BindView(R.id.discount)
    TextView discount;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.total_payment)
    TextView totalPayment;
    @BindView(R.id.tv_total_payment)
    TextView tvTotalPayment;
    @BindView(R.id.layout_info_bank)
    LinearLayout layoutInfoBank;
    @BindView(R.id.rb_va_bni)
    RadioButton rbVaBni;
    @BindView(R.id.rb_unikqu)
    RadioButton rbUnikqu;
    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;
    @BindView(R.id.layout_info_bni)
    LinearLayout layoutInfoBni;
    @BindView(R.id.tv_my_balance)
    TextView tvMyBalance;
    @BindView(R.id.layout_balance)
    RelativeLayout layoutBalance;
    @BindView(R.id.layout_text)
    LinearLayout layoutText;

    private String order_id,payment_id,kupon_id,delivery_id,order_lat,order_lon,order_jarak,status_order_id,order_note,detail_address, total_harga;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_payment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initButtonFinish();
        initRadioButton();
    }

    private void initView() {
        tvTotalPayment.setText("Rp. " + AppData.menuModel.getTotal_pay());
        payment_id = AppData.payment_id;
        kupon_id =AppData.kupon_id;
        delivery_id=AppData.delivery_id;
        order_lat =""+AppData.latLngDelivery.latitude;
        order_lon =""+AppData.latLngDelivery.longitude;
        order_jarak =""+AppData.distance;
        status_order_id = "1";
        order_note=AppData.note;
        detail_address=AppData.detail_address;
        total_harga = AppData.total_harga;
    }

    private void initButtonFinish() {
        Button btnNext = (Button) getActivity().findViewById(R.id.btn_next);
        btnNext.setText(getString(R.string.finish));
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DoOrder(getActivity()).execute(
                        payment_id,kupon_id,delivery_id,order_lat,order_lon,order_jarak,status_order_id,order_note,detail_address,total_harga
                );
            }
        });
    }

    private void initRadioButton() {
        rbBank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbBank.isChecked()) {
                    rbBank.setChecked(true);
                    rbCod.setChecked(false);
                    rbVaBni.setChecked(false);
                    rbUnikqu.setChecked(false);
                    layoutText.setVisibility(View.VISIBLE);
                    layoutInfoBank.setVisibility(View.VISIBLE);
                    layoutInfoBni.setVisibility(View.GONE);
                    tvHead.setText(R.string.bank_head);
                    tvSubhead.setText(R.string.bank_subhead);
                    layoutBalance.setVisibility(View.GONE);
                    AppData.payment_id= "1";
                    status_order_id="1";
                }
            }
        });
        rbCod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbCod.isChecked()) {
                    rbCod.setChecked(true);
                    rbBank.setChecked(false);
                    rbVaBni.setChecked(false);
                    rbUnikqu.setChecked(false);
                    layoutText.setVisibility(View.VISIBLE);
                    layoutInfoBank.setVisibility(View.GONE);
                    layoutInfoBni.setVisibility(View.GONE);
                    tvHead.setText(R.string.cod_head);
                    tvSubhead.setText(R.string.cod_subhead);
                    layoutBalance.setVisibility(View.GONE);
                    AppData.payment_id= "2";
                    status_order_id="2";
                }
            }
        });
        rbVaBni.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbVaBni.isChecked()) {
                    rbCod.setChecked(false);
                    rbBank.setChecked(false);
                    rbVaBni.setChecked(true);
                    rbUnikqu.setChecked(false);
                    layoutText.setVisibility(View.GONE);
                    layoutInfoBank.setVisibility(View.GONE);
                    layoutInfoBni.setVisibility(View.VISIBLE);
                    layoutBalance.setVisibility(View.GONE);
                    AppData.payment_id= "3";
                    status_order_id="2";
                }
            }
        });
        rbUnikqu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbUnikqu.isChecked()) {
                    rbCod.setChecked(false);
                    rbBank.setChecked(false);
                    rbVaBni.setChecked(false);
                    rbUnikqu.setChecked(true);
                    layoutText.setVisibility(View.GONE);
                    layoutInfoBank.setVisibility(View.GONE);
                    layoutInfoBni.setVisibility(View.GONE);
                    layoutBalance.setVisibility(View.VISIBLE);
                    AppData.payment_id= "4";
                    status_order_id="2";
                }
            }
        });



    }

    private class DoOrder extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;

        public DoOrder(Activity activity) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String payment_id = params[0];
                String kupon_id = params[1];
                String delivery_id = params[2];
                String order_lat = params[3];
                String order_lon = params[4];
                String order_jarak = params[5];
                String status_order_id = params[6];
                String order_note = params[7];
                String detail_address = params[8];
                String total_harga = params[9];

                JSONControl jsControl = new JSONControl();
                JSONObject responseOrder = jsControl.postOrder(activity,payment_id,kupon_id,delivery_id,order_lat,order_lon,order_jarak,status_order_id,order_note,detail_address, total_harga);
                Log.d("json responseOrder", responseOrder.toString());
                if (!responseOrder.toString().contains("error")) {
                    order_id = responseOrder.getString("order_id");
                    jsControl.postOrderDetail(order_id, AppData.menus);
                    return "OK";
                }
                else {
                    return "FAIL";
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            switch (result) {
                case "FAIL":
                    Toast.makeText(getActivity(), "Transaction fail", Toast.LENGTH_SHORT).show();
                    break;
                case "OK":
                    getActivity().finish();
                    Toast.makeText(getActivity(), "Transaction processed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
