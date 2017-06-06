package midori.kitchen.content.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import midori.kitchen.content.activity.BuyActivity;
import midori.kitchen.content.model.MenuModel;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.JSONControl;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class ResepDetailFragment extends Fragment {

    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.layout_photo)
    RelativeLayout layoutPhoto;
    @BindView(R.id.layout_add)
    LinearLayout layoutAdd;
    @BindView(R.id.delivery)
    TextView delivery;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.tv_stok)
    TextView tvStok;
    @BindView(R.id.fab_checkout)
    FloatingActionButton fabCheckout;

    private String id, resep, photo, url, cara_buat;
    private int stok;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
//        if(AppData.TAG == "Midori") {
//            new GetProdukDetail(getActivity(), id, "detail").execute();
//        }
//        else{
//            new GetProdukBukalapak(getActivity(), id, "detail").execute();
//        }
    }

    private void getData() {
        id = AppData.resepModel.getId();
        resep = AppData.resepModel.getResep();
        cara_buat = AppData.resepModel.getCara_buat();
        photo = AppData.resepModel.getImage();
        url = AppData.resepModel.getUrl();
        initView();
    }

    private void initView() {
        tvMenu.setText(resep);
        tvDescription.setText(cara_buat);
        tvPrice.setText(resep);
        //tvTime.setText(delivery_date);
        tvStok.setText(url);
        try {
            if (photo.contains("http")) {
                Glide
                        .with(this)
                        .load(photo)
                        .centerCrop()
                        .crossFade()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivPhoto);
            } else {
                byte[] imageByteArray = Base64.decode(photo, Base64.DEFAULT);
                Glide
                        .with(this)
                        .load(imageByteArray)
                        .centerCrop()
                        .crossFade()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivPhoto);
            }
        }catch (Exception e){


        }
    }

    @OnClick(R.id.fab_checkout)
    public void onClick() {
//        AppData.buyModel.setMenu(menu);
//        AppData.buyModel.setTotal_menu(1);
//        AppData.buyModel.setPrice_menu(Integer.parseInt(price));
//        Intent intent = new Intent(getActivity(), BuyActivity.class);
//        getActivity().startActivity(intent);
//        if(AppData.TAG == "Midori") {
//            new GetProdukDetail(getActivity(), id, "buy").execute();
//        }
//        else{
//            new GetProdukBukalapak(getActivity(), id, "buy").execute();
//        }
    }

    private class GetProdukDetail extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private String id;
        private ProgressDialog progressDialog;
        private String TAG;
        public GetProdukDetail(Activity activity, String id, String TAG) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
            this.id = id;
            this.TAG = TAG;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Memuat menu makanan. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.getMenuDetail(activity,id);

                Log.d("json responseDetail", response.toString());
                MenuModel menuModel = new MenuModel();
                menuModel.setId(response.getString("menuId"));
                menuModel.setMenu(response.getString("menuNama"));
                menuModel.setPrice_menu(response.getInt("menuHarga"));
                menuModel.setDescription(response.getString("menuDeskripsi"));
                menuModel.setStok(response.getInt("menuStok"));
                menuModel.setDeliveryDate(response.getString("menuJadwal"));
                menuModel.setPhoto(response.getString("menuImage"));
                menuModel.setIbuId(""+response.getInt("ibuId"));
                menuModel.setIbuNama(response.getString("ibuNama"));
                menuModel.setIbuAlamat(response.getString("ibuAlamat"));
                menuModel.setIbuLat(response.getDouble("ibuLat"));
                menuModel.setIbuLon(response.getDouble("ibuLon"));
                AppData.menuModel = menuModel;
                AppData.menus.add(AppData.menuModel);
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
                    Toast.makeText(activity,"Sorry", Toast.LENGTH_SHORT);
                    break;
                case "OK":
                    if(TAG == "buy") {
                        Intent intent = new Intent(getActivity(), BuyActivity.class);
                        getActivity().startActivity(intent);
                    } else if(TAG == "detail"){
                        getData();
                    }
                    break;
            }
        }
    }

}
