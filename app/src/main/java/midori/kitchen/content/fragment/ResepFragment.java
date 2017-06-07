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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;
import midori.kitchen.content.adapter.HistroyAdapter;
import midori.kitchen.content.adapter.ResepAdapter;
import midori.kitchen.content.model.HistoryModel;
import midori.kitchen.content.model.ResepModel;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.JSONControl;

/**
 * Created by M. Asrof Bayhaqqi on 3/12/2017.
 */

public class ResepFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<ResepModel> resepModels = new ArrayList<>();
    private BroadcastReceiver refresh;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resep, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitleBar = (TextView) getActivity().findViewById(R.id.tv_title_bar);
        tvTitleBar.setText(R.string.resep);

        initRecyclerView();
        //getDataMenu();
        new GetResep(getActivity()).execute();
        refresh = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                new GetResep(getActivity()).execute();
            }
        };
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ResepAdapter(resepModels, getActivity());
        recyclerView.setAdapter(adapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetResep(getActivity()).execute();
            }
        });
    }

    private void getDataMenu() {
        new GetResep(getActivity()).execute();
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    private class GetResep extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;
        int price, min, max;

        public GetResep(Activity activity) {
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
                JSONControl jsControl = new JSONControl();
                JSONObject response = jsControl.getResep();

                Log.d("json responseOrders", response.toString());

                resepModels.clear();
                if (!response.toString().contains("error")) {
                    JSONArray resep = response.getJSONArray("resep");
                    for(int i =0; i< resep.length();i++){
                        ResepModel resepModel = new ResepModel();
                        JSONObject resepObj = resep.getJSONObject(i);
                        resepModel.setId(resepObj.getString("resep_id"));
                        resepModel.setResep(resepObj.getString("resep_nama"));
                        resepModel.setImage(resepObj.getString("resep_image"));
                        resepModel.setCara_buat(resepObj.getString("resep_cara_buat"));
                        resepModel.setUrl(resepObj.getString("resep_url"));
                        JSONObject responseBahan = jsControl.bukalapakSearchProduct(resepObj.getString("resep_nama"));
                        JSONArray products = responseBahan.getJSONArray("products");

                        for(int x=0;x<products.length();x++){
                            price = products.getJSONObject(x).getInt("price");
                            if(x==0){
                                min = price;
                                max = price;
                            } else{
                                if(price > max) {
                                    max = price;
                                }
                                else if(price < min){
                                    min = price;
                                }
                            }

                        }
                        resepModel.setRange_harga("Rp "+min+" - Rp "+max);
                        resepModels.add(resepModel);


                    }

                    return "OK";
                } else {
                    return "FAIL";
                }


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
                    adapter = new ResepAdapter(resepModels, activity);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(refresh,
                new IntentFilter("refresh"));
    }

}
