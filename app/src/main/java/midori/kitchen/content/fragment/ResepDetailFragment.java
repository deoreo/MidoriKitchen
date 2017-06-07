package midori.kitchen.content.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
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
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import midori.kitchen.content.activity.BuyActivity;
import midori.kitchen.content.model.MenuModel;
import midori.kitchen.content.model.ResepModel;
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
    @BindView(R.id.tv_bahan)
    TextView tv_bahan;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.tv_url)
    TextView tvUrl;
    @BindView(R.id.fab_checkout)
    FloatingActionButton fabCheckout;

    private String id, resep, photo, url, cara_buat;
    private String harga;
    private String bahan = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resep_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getData();
        new GetBahan(getActivity(), AppData.resepModel.getId()).execute();
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
        cara_buat = "\n"+AppData.resepModel.getCara_buat();
        photo = AppData.resepModel.getImage();
        url = AppData.resepModel.getUrl();
        initView();
    }

    private void initView() {
        tvMenu.setText(resep);
        tvDescription.setText(cara_buat);
        tvPrice.setText(resep);
        tv_bahan.setText(bahan);
        tvUrl.setText("Sumber : "+url);
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

    @OnClick({R.id.fab_checkout, R.id.tv_url})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_checkout:
                openApp(getActivity(),"midori.chef");
                break;
            case R.id.tv_url:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
    }

    /** Open another app.
     * @param context current Context, like Activity, App, or Service
     * @param packageName the full package name of the app to open
     * @return true if likely successful, false if unsuccessful
     */
    public void openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id="+packageName));
            context.startActivity(intent);
        }
    }

    private class GetBahan extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        private String id;
        private ProgressDialog progressDialog;
        private String TAG;
        public GetBahan(Activity activity, String id) {
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
            progressDialog.setMessage("Memuat resep makanan. . .");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONControl jsControl = new JSONControl();
                JSONObject responseBahan = jsControl.getBahan(id);

                Log.d("json responseBahan", responseBahan.toString());
                JSONArray bahanArray = responseBahan.getJSONArray("resep");

                for(int i=0;i<bahanArray.length();i++){
                    AppData.resepModel.setBahan_id(bahanArray.getJSONObject(i).getString("bahan_id"));
                    AppData.resepModel.setBahan_nama(bahanArray.getJSONObject(i).getString("bahan_nama"));
                    AppData.resepModel.setBahan_jumlah(bahanArray.getJSONObject(i).getString("bahan_jumlah"));

                    bahan+="\n"+AppData.resepModel.getBahan_jumlah()+" "+AppData.resepModel.getBahan_nama()+" ";
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
            progressDialog.dismiss();
            switch (result) {
                case "FAIL":
                    Toast.makeText(activity,"Sorry", Toast.LENGTH_SHORT);
                    break;
                case "OK":
                    getData();
                    break;
            }
        }
    }



}
