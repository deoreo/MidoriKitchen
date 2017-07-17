package midori.chef.content.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.RelativeLayout;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import midori.kitchen.R;
import midori.chef.content.activity.AddMenuActivity;
import midori.chef.content.activity.MenuDetailActivity;
import midori.chef.content.adapter.MenuAdapter;
import midori.chef.content.model.MenuModel;
import midori.chef.extension.RecyclerItemClickListener;
import midori.chef.manager.AppController;
import midori.chef.manager.AppData;
import midori.chef.manager.AppListMenu;
import midori.chef.manager.AppPrefManager;
import midori.chef.manager.ConfigManager;

/**
 * Created by BimoV on 3/10/2017.
 */

public class MenuFragment extends Fragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.btnAddMenu)
    FloatingActionButton btnAddMenu;
    @BindView(R.id.no_menu)
    TextView tvNoMenu;
    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;
    Unbinder unbinder;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<MenuModel> menuList = new ArrayList<>();
    private AppListMenu appListMenu;
    private AppPrefManager appPrefManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, v);
        appListMenu = new AppListMenu();
        appPrefManager = new AppPrefManager(getContext());
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        downloadDataMenu();
    }

    private void downloadDataMenu() {
        if (AppController.isConnected(getContext())) {
            requestDataMenu(ConfigManager.GET_MY_LAPAK + appPrefManager.getUser().get("name").replaceAll(" ", "%20"));
        } else {
            AppController.showErrorConnection(rootLayout);
        }
    }

    private void requestDataMenu(String url) {
        swiperefresh.setRefreshing(true);
        Ion.with(getContext())
                .load(url)
                .setLogging("GetMyLapak", Log.DEBUG)
                .basicAuthentication(AppData.USER_AUTH, AppData.PASS_AUTH)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        menuList.clear();
                        try {
                            if (result.getHeaders().code() == 200) {
                                JsonObject object = result.getResult();
                                String status = object.get("status").getAsString();
                                Log.d("Response", object.toString());

                                JsonArray jsonArray = object.get("products").getAsJsonArray();
                                if (jsonArray.size() != 0) {
                                    recyclerview.setVisibility(View.VISIBLE);
                                    tvNoMenu.setVisibility(View.GONE);

                                }
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    MenuModel menuModel = new MenuModel();
                                    JsonObject productObject = jsonArray.get(i).getAsJsonObject();
                                    String name = productObject.get("name").getAsString();
                                    String delivery = productObject.get("desc").getAsString();
                                    String[] nameSeparate = name.split("-");
                                    String[] descSeparate = delivery.split("-");
                                    int jml_pesan = productObject.get("sold_count").getAsInt();
                                    int stock = productObject.get("stock").getAsInt() - jml_pesan;
                                    String harga = productObject.get("price").getAsString();

                                    JsonArray imageArray = productObject.get("images").getAsJsonArray();
                                    String images = imageArray.get(0).getAsString();

                                    menuModel.setMenu(nameSeparate[1]);
                                    menuModel.setDescription(descSeparate[0]);
                                    menuModel.setDeliveryDate(descSeparate[1]);
                                    menuModel.setStock(String.valueOf(stock));
                                    menuModel.setImage(images);
                                    menuModel.setJumlah_pesanan(String.valueOf(jml_pesan));
                                    menuModel.setHarga(harga);
                                    menuList.add(menuModel);
                                    Log.d("ImageArray", menuModel.getImage());
                                    Log.d("MenuArray", menuList.get(0).getMenu());
                                    Log.d("MenuArraySize", jsonArray.size() + "");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        swiperefresh.setRefreshing(false);

                    }
                });
    }

    private void initRecyclerView() {
        recyclerview.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(layoutManager);

        adapter = new MenuAdapter(getContext(), menuList);
        recyclerview.setAdapter(adapter);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRecyclerView();
                downloadDataMenu();
            }
        });
        recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {
                MenuModel item = menuList.get(position);
                Intent intent = new Intent(getContext(), MenuDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("menu",item.getMenu());
                bundle.putString("order",item.getJumlah_pesanan());
                bundle.putString("stock",item.getStock());
                bundle.putString("delivery",item.getDeliveryDate());
                bundle.putString("image",item.getImage());
                bundle.putString("desc",item.getDescription());
                bundle.putString("harga",item.getHarga());
                intent.putExtras(bundle);

                ImageView image = (ImageView)childView.findViewById(R.id.image);
                TextView title = (TextView)childView.findViewById(R.id.menu);
                TextView order = (TextView)childView.findViewById(R.id.jumlah_pesan);
                TextView stock = (TextView)childView.findViewById(R.id.stock);
                TextView delivery = (TextView)childView.findViewById(R.id.delivery);
                LinearLayout d1 = (LinearLayout)childView.findViewById(R.id.diagonal);
                LinearLayout d2 = (LinearLayout)childView.findViewById(R.id.diagonal2);

                Pair<View, String> p1 = Pair.create((View) image, "img");
                Pair<View, String> p2 = Pair.create((View) title, "menu");
                Pair<View, String> p3 = Pair.create((View) order, "order");
                Pair<View, String> p4 = Pair.create((View) stock, "stock");
                Pair<View, String> p5 = Pair.create((View) delivery, "delivery");
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

    @OnClick(R.id.btnAddMenu)
    public void onClick() {
        Intent intent = new Intent(getContext(), AddMenuActivity.class);
        startActivity(intent);
 /*       for (int i=0; i<appListMenu.getFavorites(getContext()).size();i++){
            MenuModel menu = appListMenu.getFavorites(getContext()).get(i);
            Toast.makeText(getContext(), ""+menu.getMenu(), Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppData.refreshDataMenu) {
            initRecyclerView();
            downloadDataMenu();
        }
    }
}
