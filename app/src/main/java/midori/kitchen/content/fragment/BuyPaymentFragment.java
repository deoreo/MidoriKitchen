package midori.kitchen.content.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;
import midori.kitchen.content.adapter.HistroyAdapter;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.AppPrefManager;
import midori.kitchen.manager.GoogleAPIManager;
import midori.kitchen.manager.JSONControl;
import midori.kitchen.manager.MultiInputMaterialDialogBuilder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
    @BindView(R.id.rb_bukadompet)
    RadioButton rbBukadompet;
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
    @BindView(R.id.layout_bukadompet)
    LinearLayout layoutBukadompet;

    private AlertDialog dialogLogin;
    private EditText txtEmail, txtPassword;
    private TextView txtMessage;
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
        if(AppData.isBukaDompet) {
            layoutBukadompet.setVisibility(VISIBLE);
        }
        else{
            layoutBukadompet.setVisibility(GONE);
        }
        AppData.invoiceModel.setPhone(AppPrefManager.getInstance(getActivity()).getUser().get("phone"));
        if(AppData.invoiceModel.getPhone().isEmpty()){
            Drawable drawable = getResources().getDrawable(R.drawable.ic_midori);
            new MaterialDialog.Builder(getActivity())
                    .title("Konfirmasi")
                    .content("Silahkan input nomor telepon anda")
                    .inputType(InputType.TYPE_CLASS_PHONE)
                    .input("+62813xxxx", "", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            AppPrefManager.getInstance(getActivity()).setUser(
                                    AppPrefManager.getInstance(getActivity()).getUser().get("idaccount"),
                                    AppPrefManager.getInstance(getActivity()).getUser().get("fullname"),
                                    AppPrefManager.getInstance(getActivity()).getUser().get("email"),
                                    input.toString()

                            );
                        }

                    })
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            AppData.invoiceModel.setPhone(AppPrefManager.getInstance(getActivity()).getUser().get("phone"));
                        }
                    })
                    .icon(drawable)
                    .typeface("GothamRnd-Book.otf","GothamRnd-Light.otf" )
                    .show();
        }
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
                    rbBukadompet.setChecked(false);
                    layoutText.setVisibility(VISIBLE);
                    layoutInfoBank.setVisibility(VISIBLE);
                    layoutInfoBni.setVisibility(GONE);
                    tvHead.setText(R.string.bank_head);
                    tvSubhead.setText(R.string.bank_subhead);
                    layoutBalance.setVisibility(GONE);
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
                    rbBukadompet.setChecked(false);
                    layoutText.setVisibility(VISIBLE);
                    layoutInfoBank.setVisibility(GONE);
                    layoutInfoBni.setVisibility(GONE);
                    tvHead.setText(R.string.cod_head);
                    tvSubhead.setText(R.string.cod_subhead);
                    layoutBalance.setVisibility(GONE);
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
                    rbBukadompet.setChecked(false);
                    layoutText.setVisibility(GONE);
                    layoutInfoBank.setVisibility(GONE);
                    layoutInfoBni.setVisibility(VISIBLE);
                    layoutBalance.setVisibility(GONE);
                    AppData.payment_id= "3";
                    status_order_id="2";
                }
            }
        });
        rbBukadompet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbBukadompet.isChecked()) {
                    rbCod.setChecked(false);
                    rbBank.setChecked(false);
                    rbVaBni.setChecked(false);
                    rbBukadompet.setChecked(true);
                    layoutText.setVisibility(GONE);
                    layoutInfoBank.setVisibility(GONE);
                    layoutInfoBni.setVisibility(GONE);
                    layoutBalance.setVisibility(VISIBLE);
                    AppData.payment_id= "4";
                    status_order_id="2";
                    InitDialogLogin();
                }
            }
        });
    }

    private void InitDialogLogin() {
        dialogLogin = new AlertDialog.Builder(getActivity())
                .setTitle("Login BukaDompet")
                .setView(R.layout.popup_login_bukalapak)
                .create();
        dialogLogin.show();
        // set the custom dialog components - text, image and button
        txtMessage = (TextView) dialogLogin.findViewById(R.id.txtMessage);
        txtEmail = (EditText) dialogLogin.findViewById(R.id.txtEmail);
        txtPassword = (EditText) dialogLogin.findViewById(R.id.txtPassword);
        Button cancel = (Button) dialogLogin.findViewById(R.id.btnCancel);
        Button confirm = (Button) dialogLogin.findViewById(R.id.btnConfirm);

        txtMessage.setText("");
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail.setText("");
                txtPassword.setText("");
                dialogLogin.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppData.email_bukalapak = txtEmail.getText().toString();
                AppData.password_bukalapak = txtPassword.getText().toString();
                AppData.invoiceModel.setPassword_bukadompet(AppData.password_bukalapak);
                new GetToken(getActivity()).execute(AppData.email_bukalapak, AppData.password_bukalapak);
            }
        });
    }

    private class GetToken extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;

        public GetToken(Activity activity) {
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
                String username = params[0];
                String password = params[1];

                JSONControl jsControl = new JSONControl();
                JSONObject responseToken = jsControl.bukalapakGetToken(username,password);
                Log.d("json responseOrder", responseToken.toString());
                if(responseToken.getString("status").contains("OK")){
                    String user_id = ""+responseToken.getInt("user_id");
                    String token = responseToken.getString("token");
                    String base64EncodedCredentials = Base64.encodeToString(
                            (user_id+":"+token).getBytes(),
                            Base64.NO_WRAP);

                    AppData.Base64KeyUser = base64EncodedCredentials;
                    JSONObject responseUser = jsControl.bukalapakUserSummary(base64EncodedCredentials);
                    if(responseUser.getString("status").contains("OK")){
                        JSONObject jsonObjectSummary = responseUser.getJSONObject("summary");
                        AppData.BukaDompetBalance = ""+jsonObjectSummary.getInt("balance");
                        JSONObject responseCart = jsControl.bukalapakAddCart(AppData.menuModel.getId(), AppData.menuModel.getTotal_menu());
                        String cart_id = responseCart.getString("cart_id");
                        AppData.invoiceModel.setCart_id(cart_id);
                        JSONArray carts = responseCart.getJSONArray("cart");
                        for(int i = 0;i<carts.length();i++){
                            JSONObject cartObj = carts.getJSONObject(i);
                            JSONObject seller = cartObj.getJSONObject("seller");
                            String seller_id = seller.getString("id");
                            if(seller_id.equalsIgnoreCase("33172156")) {
                                AppData.invoiceModel.setSeller_id(seller_id);
                                JSONArray itemArray = cartObj.getJSONArray("items");
                                for (int t = 0; t < itemArray.length(); t++) {
                                    String item_id = itemArray.getJSONObject(t).getString("id");
                                    AppData.invoiceModel.setItem_id(item_id);
                                }
                            }
                        }

                        AppData.invoiceModel.setNama_penerima(AppPrefManager.getInstance(getActivity()).getUser().get("fullname"));
                        AppData.invoiceModel.setPhone(AppPrefManager.getInstance(getActivity()).getUser().get("phone"));


                        int item_id = Integer.parseInt(AppData.invoiceModel.getItem_id());//
                        String nama_penerima = AppData.invoiceModel.getNama_penerima();//
                        String phone = AppData.invoiceModel.getPhone();//masih harus diisi
                        String province =  AppData.invoiceModel.getProvince();//
                        String city = AppData.invoiceModel.getCity();//
                        String area =  AppData.invoiceModel.getArea();//
                        String address =  AppData.invoiceModel.getAddress();//
                        String post_code = AppData.invoiceModel.getPost_code();//
                        int seller_id = Integer.parseInt(AppData.invoiceModel.getSeller_id());//
                        String buyer_notes =  AppData.invoiceModel.getBuyer_notes();//
                        String password_bukadompet =  AppData.invoiceModel.getPassword_bukadompet();//
                        int cartid = Integer.parseInt(AppData.invoiceModel.getCart_id());//
                        JSONObject responseInvoice = jsControl.bukalapakCreateInvoice(item_id,nama_penerima,phone,province,city,area,address,post_code,seller_id,buyer_notes,password_bukadompet,cartid);
                        if(responseInvoice.getString("status").contains("ERROR")){
                            AppData.invoice_message = responseInvoice.getString("message");
                            return "SALDO FAIL";
                        }
                        return "OK";
                    }else
                    {
                        return "FAIL";
                    }
                } else{
                    return "FAIL";
                }


            } catch (Exception e) {
                e.printStackTrace();
                return "FAIL";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            switch (result) {
                case "FAIL":
                    dialogLogin.dismiss();
                    txtMessage.setText("Login Gagal");
                    break;
                case "SALDO FAIL":
                    dialogLogin.dismiss();
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_midori);
                    new MaterialDialog.Builder(activity)
                            .title("Mohon maaf...")
                            .content( AppData.invoice_message)
                            .positiveText("OK")
                            .icon(drawable)
                            .typeface("GothamRnd-Book.otf","GothamRnd-Light.otf" )
                            .show();
                    break;
                case "OK":
                    //txtMessage.setText("Login Sukses" + AppData.Base64KeyUser);
                    tvMyBalance.setText("Rp. "+ AppData.BukaDompetBalance);
                    dialogLogin.dismiss();
                    break;
            }
        }
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
                    if(AppData.payment_id=="1") {
                        sendEmail();
                    }
                    //Toast.makeText(getActivity(), "Transaction processed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    protected void sendEmail() {

        BackgroundMail.newBuilder(getActivity())
                .withUsername("midorichef@gmail.com")
                .withPassword("musLim@06")
                .withMailto(AppPrefManager.getInstance(getActivity()).getUser().get(AppPrefManager.KEY_EMAIL))
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Konfirmasi Pesanan no. "+order_id)
                .withBody("Dear "+AppPrefManager.getInstance(getActivity()).getUser().get(AppPrefManager.KEY_FULLNAME)+",\n\nTerima kasih telah memesan melalui Midori Kitchen.\nSegera lakukan pembayaran ke rekening CIMB Niaga - 703293580200 dan konfirmasi melalui Menu History\n\nBest Regards,\nMidori Kitchen")
                .withSendingMessage("Mengirim konfirmasi")
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        new MaterialDialog.Builder(getActivity())
                                .title("Terima kasih")
                                .content("Segera lakukan pembayaran ke rekening CIMB Niaga - 703293580200 dan konfirmasi melalui Menu History")
                                .typeface("GothamRnd-Medium.otf", "Gotham.ttf")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        getActivity().finish();
                                    }
                                })
                                .negativeText("Close")
                                .show();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        new MaterialDialog.Builder(getActivity())
                                .title("Terima Kasih")
                                .content("Segera lakukan pembayaran ke rekening CIMB Niaga - 703293580200 dan konfirmasi melalui Menu History")
                                .typeface("GothamRnd-Medium.otf", "Gotham.ttf")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        getActivity().finish();
                                    }
                                })
                                .negativeText("Close")
                                .show();
                    }
                })
                .send();



    }

}
