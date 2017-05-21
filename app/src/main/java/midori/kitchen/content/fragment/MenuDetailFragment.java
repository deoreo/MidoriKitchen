package midori.kitchen.content.fragment;

import android.app.Activity;
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
import midori.kitchen.content.adapter.MenuAdapter;
import midori.kitchen.content.model.BuyModel;
import midori.kitchen.content.model.MenuModel;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.JSONControl;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class MenuDetailFragment extends Fragment {

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
    @BindView(R.id.fab_checkout)
    FloatingActionButton fabCheckout;

    private String id, menu, description, price, delivery_date, photo;

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
    }

    private void getData() {
        id = AppData.menuModel.getId();
        menu = AppData.menuModel.getMenu();
        description = AppData.menuModel.getDescription();
        price = ""+AppData.menuModel.getPrice_menu();
        delivery_date = AppData.menuModel.getDeliveryDate();
        photo = AppData.menuModel.getPhoto();
        initView();
    }

    private void initView() {
        tvMenu.setText(menu);
        tvDescription.setText(description);
        tvPrice.setText("Rp. " + price);
        tvTime.setText(delivery_date);
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
        new GetProdukDetail(getActivity(),id).execute();
    }

    private class GetProdukDetail extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private String id;

        public GetProdukDetail(Activity activity, String id) {
            super();
            this.activity = activity;
            this.context = activity.getApplicationContext();
            this.resources = activity.getResources();
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                menuModel.setIbuNama(response.getString("ibuNama"));
                menuModel.setIbuAlamat(response.getString("ibuAlamat"));
                menuModel.setIbuLat(response.getDouble("ibuLat"));
                menuModel.setIbuLon(response.getDouble("ibuLon"));
                AppData.menuModel = menuModel;
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
                    Toast.makeText(activity,"Sorry", Toast.LENGTH_SHORT);
                    break;
                case "OK":
                    Intent intent = new Intent(getActivity(), BuyActivity.class);
                    getActivity().startActivity(intent);
                    break;
            }
        }
    }
}
