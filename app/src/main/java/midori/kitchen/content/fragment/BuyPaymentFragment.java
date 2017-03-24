package midori.kitchen.content.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;
import midori.kitchen.manager.AppData;

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
        tvTotalPayment.setText("Rp. " + AppData.buyModel.getTotal_pay());
    }

    private void initButtonFinish() {
        Button btnNext = (Button) getActivity().findViewById(R.id.btn_next);
        btnNext.setText(getString(R.string.finish));
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Toast.makeText(getActivity(), "Transaction processed", Toast.LENGTH_SHORT).show();
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
                }
            }
        });



    }
}
