package midori.kitchen.content.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.ion.Ion;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
//import midori.kitchen.content.activity.ChangeLocationActivity;
import midori.kitchen.content.adapter.MenuAdapter;
import midori.kitchen.content.model.MenuModel;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.AppPrefManager;
import midori.kitchen.manager.ConfigManager;
import midori.kitchen.manager.GoogleAPIManager;
import midori.kitchen.manager.JSONControl;

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
    @BindView(R.id.et_province)
    public TextView etProvince;
    @BindView(R.id.et_city)
    public TextView etCity;
    @BindView(R.id.et_area)
    public TextView etArea;
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
    private int deliveryPrice = 8000;
    private BroadcastReceiver changeAlamat, changeDistance, changePriceDelivery;
    private String[] provincesArray, cities, areas;
    LatLng ibuLocation = new LatLng(AppData.menuModel.getIbuLat(), AppData.menuModel.getIbuLon());

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
                AppData.detail_address = AppData.address + " , " + AppData.locationDetail;
                AppData.note = etNotes.getText().toString();
                if (AppData.note.isEmpty()) {
                    AppData.note = "tidak ada catatan pelanggan";
                }
                AppData.total_harga = "" + ((AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price());
                AppData.status = "1";
                if (etPromotion.getText().toString().isEmpty()) {
                    AppData.kupon_id = "0";
                }

                if (isKurir) {
                    AppData.delivery_id = "2";
                    if (validate(address)) {
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
                AppData.invoiceModel.setBuyer_notes(AppData.note);


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
                    if (AppData.isInteger(AppData.menuModel.getId())) {
                        AppData.isBukaDompet = false;
                    } else {
                        AppData.isBukaDompet = true;
                    }
                } else if (item.equals("Ambil Sendiri")) {
                    isKurir = false;
                    deliveryPrice = 0;
                    AppData.menuModel.setDelivery_price(deliveryPrice);
                    int totalPay = (AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price();
                    AppData.menuModel.setTotal_pay(totalPay);
                    tvTotalPay.setText("Rp. " + AppData.menuModel.getTotal_pay());
                    layoutDelivery.setVisibility(View.GONE);
                    AppData.isBukaDompet = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean validate(String address) {
        boolean valid = true;
        if (address.isEmpty()) {
            etAddress.setError("Enter address");
            valid = false;
        } else {
            etAddress.setError(null);
        }
//        if (locationDetail.isEmpty()) {
//            etLocationDetail.setError("Enter location detail");
//            valid = false;
//        } else {
//            etLocationDetail.setError(null);
//        }
        return valid;
    }

    private void initView() {

        AppData.menuModel.setTotal_menu(1);
        AppData.menuModel.setDelivery_price(deliveryPrice);
        tvMenu.setText(AppData.menuModel.getMenu());
        tvDistance.setText("0 KM");

        tvPrice.setText("Rp. " + AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu());
        tvMenu.setText(AppData.menuModel.getMenu());
        tvTotalPay.setText("Rp. " + ((AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price()));
        tvCountMenu.setText(String.valueOf(AppData.menuModel.getTotal_menu()));
        tvDeliveryPrice.setText("Rp. " + AppData.menuModel.getDelivery_price());
        AppData.total_harga = "" + ((AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price());

//        changeAlamat = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // Extract data included in the Intent
//                Log.d("", "broadcast changeAlamat");
//                etAddress.setText(AppPrefManager.getInstance(getActivity()).getAlamat());
//            }
//        };

        new GetShippingFee(getActivity()).execute();

        changeDistance = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("", "broadcast changeDistance");
                tvDistance.setText(AppData.distance + " KM");
            }
        };

        changePriceDelivery = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("", "broadcast changeDistance");

                deliveryPrice = AppData.priceDelivery;
                tvDeliveryPrice.setText("Rp. " + deliveryPrice);
                AppData.menuModel.setDelivery_price(deliveryPrice);
                tvTotalPay.setText("Rp. " + ((AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price()));
                AppData.total_harga = "" + ((AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price());
            }
        };
        if(!AppPrefManager.getInstance(getActivity()).getDeliveryAddress().contains("null")) {
            etAddress.setText(AppPrefManager.getInstance(getActivity()).getDeliveryAddress());
        }else{
            etAddress.setHint("");
        }


    }

    @OnClick({R.id.btn_plus, R.id.btn_minus, R.id.et_address, R.id.et_province, R.id.et_city, R.id.et_area})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_plus:
                plusCountMenu();
                break;
            case R.id.btn_minus:
                minusCountMenu();
                break;
            case R.id.et_address:
                //gotoLocation();
                break;
            case R.id.et_province:
                new GetProvinces("manual").execute();
                break;
            case R.id.et_city:
                if (!etProvince.getText().toString().isEmpty()) {
                    new GetCity("manual", etProvince.getText().toString()).execute();
                } else {
                    new MaterialDialog.Builder(getActivity())
                            .title("Mohon maaf...")
                            .content("Silahkan pilih Provinsi pengiriman")
                            .typeface("GothamRnd-Book.otf", "GothamRnd-Light.otf")
                            .positiveText("OK")
                            .show();

                }
                break;
            case R.id.et_area:
                if (etProvince.getText().toString().isEmpty()) {
                    new MaterialDialog.Builder(getActivity())
                            .title("Mohon maaf...")
                            .content("Silahkan pilih Provinsi pengiriman")
                            .typeface("GothamRnd-Book.otf", "GothamRnd-Light.otf")
                            .positiveText("OK")
                            .show();

                } else if (etCity.getText().toString().isEmpty()) {
                    new MaterialDialog.Builder(getActivity())
                            .title("Mohon maaf...")
                            .content("Silahkan pilih Kota pengiriman")
                            .typeface("GothamRnd-Book.otf", "GothamRnd-Light.otf")
                            .positiveText("OK")
                            .show();

                } else {
                    new GetArea("manual", AppData.invoiceModel.getProvince(), AppData.invoiceModel.getCity()).execute();
                }
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
            tvTotalPay.setText("Rp. " + AppData.menuModel.getTotal_pay());
            ;
            Log.d("totalPay", "" + totalPay);
        } else {
            getActivity().finish();
        }
    }

    private void gotoLocation() {
//        Intent j = new Intent(getActivity(), ChangeLocationActivity.class);
//        startActivity(j);
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

    private class GetProvinces extends AsyncTask<String, Void, String> {

        private String TAG;

        public GetProvinces(String TAG) {
            this.TAG = TAG;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.BukalapakGetProvinces();
                JSONArray responseJSONArray = response.getJSONArray("provinces");
                provincesArray = new String[responseJSONArray.length()];
                for (int i = 0; i < responseJSONArray.length(); i++) {
                    provincesArray[i] = responseJSONArray.getString(i);
                }

                return "OK";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            switch (result) {
                case "FAIL":
                    break;
                case "OK":
                    if (TAG == "manual") {
                        new MaterialDialog.Builder(getActivity())
                                .title("Provinces")
                                .items(provincesArray)
                                .typeface("GothamRnd-Book.otf", "GothamRnd-Light.otf")
                                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        try {
                                            etProvince.setText(text.toString());
                                            AppData.invoiceModel.setProvince(text.toString());
                                        } catch (Exception e) {

                                        }
                                        return true;
                                    }
                                })
                                .positiveText("OK")
                                .show();
                    }
                    break;
            }
        }
    }

    private class GetCity extends AsyncTask<String, Void, String> {

        private String TAG, province;

        public GetCity(String TAG, String province) {
            this.TAG = TAG;
            this.province = province;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.BukalapakGetCity(province);
                JSONArray responseJSONArray = response.getJSONArray("cities");
                cities = new String[responseJSONArray.length()];
                for (int i = 0; i < responseJSONArray.length(); i++) {
                    cities[i] = responseJSONArray.getString(i);
                }
                return "OK";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            switch (result) {
                case "FAIL":
                    break;

                case "OK":
                    if (TAG == "manual") {
                        new MaterialDialog.Builder(getActivity())
                                .title("Cities")
                                .items(cities)
                                .typeface("GothamRnd-Book.otf", "GothamRnd-Light.otf")
                                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        try {
                                            etCity.setText(text.toString());
                                            AppData.invoiceModel.setCity(text.toString());
                                        } catch (Exception e) {

                                        }
                                        return true;
                                    }
                                })
                                .positiveText("OK")
                                .show();
                    }
                    break;
            }
        }
    }

    private class GetArea extends AsyncTask<String, Void, String> {

        private String TAG, province, city;

        public GetArea(String TAG, String province, String city) {
            this.TAG = TAG;
            this.province = province;
            this.city = city;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.BukalapakGetArea();
                JSONObject responseAddress = response.getJSONObject("address");
                JSONObject responseProvince = responseAddress.getJSONObject(province);
                JSONArray responseCity = responseProvince.getJSONArray(city);
                areas = new String[responseCity.length()];
                for (int i = 0; i < responseCity.length(); i++) {
                    areas[i] = responseCity.getString(i);
                }

                return "OK";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            switch (result) {
                case "FAIL":
                    break;

                case "OK":
                    if (TAG == "manual") {
                        new MaterialDialog.Builder(getActivity())
                                .title("Areas")
                                .items(areas)
                                .typeface("GothamRnd-Book.otf", "GothamRnd-Light.otf")
                                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        try {
                                            etArea.setText(text.toString());
                                            AppData.invoiceModel.setArea(text.toString());
                                        } catch (Exception e) {

                                        }
                                        return true;
                                    }
                                })
                                .positiveText("OK")
                                .show();
                    }
                    break;
            }
        }
    }


    private class GetShippingFee extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;
        private String courier, weight, origin, destination;
        private double distance;
        private Activity activity;

        public GetShippingFee(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Memuat review pembelian. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                AppData.invoiceModel.setProvince(AppPrefManager.getInstance(activity).getProvince());
                AppData.invoiceModel.setCity(AppPrefManager.getInstance(activity).getCity());
                AppData.invoiceModel.setArea(AppPrefManager.getInstance(activity).getArea());
                AppData.invoiceModel.setPost_code(AppData.invoiceModel.getPost_code());
                Document doc = GoogleAPIManager.getRoute(ibuLocation, AppPrefManager.getInstance(activity).getGeocode(), "driving");
                String strDistance = GoogleAPIManager.getDistanceText(doc);
                AppData.distance = Double.parseDouble(strDistance.split(" ")[0]);
                distance = AppData.distance;
                int price = 0;
                if (distance <= 3.9) {
                    price = 8000;
                } else if (distance >= 4.0 && distance <= 15) {
                    double selisihjarak = distance - 4.0;
                    double hargaselisihjarak = (Math.round(selisihjarak * 2) / 2.0) * 2000;
                    price = 8000 + (int) hargaselisihjarak;
                } else if (distance >= 15) {
                    price = 30000;
                }
                AppData.priceDelivery = price;
                deliveryPrice = AppData.priceDelivery;
                AppData.menuModel.setDelivery_price(deliveryPrice);
                AppData.total_harga = "" + ((AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price());

                return "OK";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            switch (result) {
                case "FAIL":
                    break;

                case "OK":
                    if(!AppPrefManager.getInstance(activity).getDeliveryAddress().contains("null")) {
                        etAddress.setText(AppPrefManager.getInstance(activity).getDeliveryAddress());
                    }else{
                        etAddress.setHint("");
                    }
                    tvDeliveryPrice.setText("Rp. " + deliveryPrice);
                    tvDistance.setText(AppData.distance + " KM");
                    tvTotalPay.setText("Rp. " + ((AppData.menuModel.getPrice_menu() * AppData.menuModel.getTotal_menu()) + AppData.menuModel.getDelivery_price()));
                    break;
            }
        }
    }


}
