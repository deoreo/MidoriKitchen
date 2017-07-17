package midori.chef.content.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import midori.kitchen.R;
import midori.chef.content.activity.DetailActivity;
import midori.chef.content.adapter.TransaksiAdapter;
import midori.chef.content.model.TransaksiModel;
import midori.chef.extension.RecyclerItemClickListener;
import midori.chef.manager.AppController;
import midori.chef.manager.AppData;
import midori.chef.manager.ChefPrefManager;
import midori.chef.manager.ConfigManager;

/**
 * Created by BimoV on 3/10/2017.
 */

public class TransaksiFragment extends Fragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.rootLayout)
    LinearLayout rootLayout;

    Unbinder unbinder;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<TransaksiModel> transaksiList = new ArrayList<>();
    ChefPrefManager chefPrefManager;
    //    private List<TransaksiModel> transaksiList2 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaksi, container, false);
        unbinder = ButterKnife.bind(this, view);
        chefPrefManager = new ChefPrefManager(getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();

        downloadDataTransaksi();
    }

    private void downloadDataTransaksi() {
        if (AppController.isConnected(getContext())) {
            requestDataTransaksi(ConfigManager.TRANSAKSI);
        } else {
            AppController.showErrorConnection(rootLayout);
        }
    }

    private void requestDataTransaksi(String url) {
        swiperefresh.setRefreshing(true);
        transaksiList.clear();
        Log.d("APIKEY", chefPrefManager.getUser().get("key"));
        Ion.with(getContext())
                .load(url)
                .setLogging("Request Transaksi", Log.DEBUG)
                .setHeader("Authorization", chefPrefManager.getUser().get("key"))
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        try {
                            if (result.getHeaders().code() == 200) {
                                JsonObject response = result.getResult();
                                Log.d("ObjectResponse", "onCompleted: " + response);
                                JsonArray responseArray = response.get("ibuorders").getAsJsonArray();
                                for (int i = 0; i < responseArray.size(); i++) {
                                    TransaksiModel transaksiModel = new TransaksiModel();
                                    JsonObject object = responseArray.get(i).getAsJsonObject();


                                    String menu_id = object.get("menu_id").getAsString();
                                    Log.d("MenuID", menu_id);
                                    String status_order = object.get("status_order").getAsString();
                                    String menu_nama = object.get("menu_nama").getAsString();
                                    String order_note = object.get("order_note").getAsString();
                                    String jumlah_order = object.get("order_detail_jumlah").getAsString();
                                    String harga_order = object.get("order_detail_harga").getAsString();
                                    String image = object.get("menu_image").getAsString();

                                    transaksiModel.setId(menu_id);
                                    transaksiModel.setMenu(menu_nama);
                                    transaksiModel.setJumlah_pesan(jumlah_order);
                                    transaksiModel.setHarga(harga_order);
                                    transaksiModel.setStatus(status_order);
                                    transaksiModel.setNote(order_note);
                                    transaksiModel.setAvatar(image);


                                    transaksiList.add(transaksiModel);
                                    adapter.notifyDataSetChanged();
                                }

                                for (int i = 0; i < transaksiList.size(); i++) {
                                    if (!AppData.isInteger(transaksiList.get(i).getId())) {
                                        requestDataBukaLapak(transaksiList.get(i).getId());
                                    }
                                }
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        swiperefresh.setRefreshing(false);
                    }
                });

        //requestDataBukaLapak(menu_id);

    }

    private void requestDataBukaLapak(final String id) {
        Log.d("MenuIDBukalapak", id);
        Log.d("TransaksiMenuId", "onCompleted: " + transaksiList.size());
        Ion.with(getContext())
                .load(ConfigManager.READ_PRODUCT + id + ".json")
                .setLogging("RequestDataBukalapak", Log.DEBUG)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        try {
                            if (result.getHeaders().code() == 200) {
                                JsonObject response = result.getResult();
                                JsonObject productResponse = response.get("product").getAsJsonObject();
                                String namas = productResponse.get("name").getAsString();
                                String[] nama = namas.split("-");
                                String idmenu = productResponse.get("id").getAsString();
                                JsonArray imageArray = productResponse.get("images").getAsJsonArray();
                                String images = imageArray.get(0).getAsString();
                                for (int i = 0; i < transaksiList.size(); i++) {
                                    if (transaksiList.get(i).getId().equals(idmenu)) {
                                        Log.d("IDBUKALAPAK", "" + i);
                                        transaksiList.get(i).setMenu(nama[1]);
                                        transaksiList.get(i).setAvatar(images);
                                        adapter.notifyDataSetChanged();
                                    }
                                    Log.d("NewMenuBukalapak", transaksiList.get(i).getMenu());
                                }
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }


    private void initRecyclerView() {
        recyclerview.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(layoutManager);
        adapter = new TransaksiAdapter(getContext(), transaksiList);
        recyclerview.setAdapter(adapter);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRecyclerView();
                downloadDataTransaksi();
            }
        });
        recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {
                TransaksiModel item = transaksiList.get(position);
                Intent intent = new Intent(getContext(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("menu",item.getMenu());
                bundle.putString("order",item.getJumlah_pesan());
                bundle.putString("harga",item.getHarga());
                bundle.putString("note",item.getNote());
                bundle.putString("image",item.getAvatar());
                bundle.putString("status",item.getStatus());
                intent.putExtras(bundle);

                ImageView image = (ImageView)childView.findViewById(R.id.image);
                TextView title = (TextView)childView.findViewById(R.id.menu);
                TextView harga = (TextView)childView.findViewById(R.id.harga);
                TextView order = (TextView)childView.findViewById(R.id.jumlah_pesan);
                TextView status = (TextView)childView.findViewById(R.id.status);

                Pair<View, String> p1 = Pair.create((View) image, "img");
                Pair<View, String> p2 = Pair.create((View) title, "menu");
                Pair<View, String> p3 = Pair.create((View) order, "order");
                Pair<View, String> p4 = Pair.create((View) harga, "harga");
                Pair<View, String> p5 = Pair.create((View) status, "status");
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1,p2,p3,p4,p5);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent,optionsCompat.toBundle());
                    // Call some material design APIs here
                } else {
                    startActivity(intent);
                    // Implement this feature without material design
                }
            }

            @Override
            public void onItemLongPress(View childView, int position) {

            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
