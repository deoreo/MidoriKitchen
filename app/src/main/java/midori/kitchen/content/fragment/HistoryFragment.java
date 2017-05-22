package midori.kitchen.content.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;
import midori.kitchen.content.adapter.HistroyAdapter;
import midori.kitchen.content.adapter.MenuAdapter;
import midori.kitchen.content.data.DataHelper;
import midori.kitchen.content.model.HistoryModel;
import midori.kitchen.content.model.MenuModel;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.AppPrefManager;
import midori.kitchen.manager.JSONControl;

/**
 * Created by M. Asrof Bayhaqqi on 3/12/2017.
 */

public class HistoryFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<HistoryModel> historyItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_payment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitleBar = (TextView) getActivity().findViewById(R.id.tv_title_bar);
        tvTitleBar.setText(R.string.history);

        initRecyclerView();
        //getDataMenu();
        new GetUserOrder(getActivity()).execute();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistroyAdapter(historyItems, getActivity());
        recyclerView.setAdapter(adapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetUserOrder(getActivity()).execute();
            }
        });
    }

    private void getDataMenu() {
        new GetUserOrder(getActivity()).execute();
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    private class GetUserOrder extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;

        public GetUserOrder(Activity activity) {
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
                JSONObject response = jsControl.getOrderUser(activity);

                Log.d("json responseOrders", response.toString());

                historyItems.clear();
                if (!response.toString().contains("error")) {
                    JSONArray orders = response.getJSONArray("orders");
                    for(int i =0; i< orders.length();i++){
                        HistoryModel historyModel = new HistoryModel();
                        JSONObject historyObject = orders.getJSONObject(i);
                        historyModel.setId(""+i);
                        historyModel.setOrder_id(historyObject.getString("order_id"));
                        historyModel.setMenu_id(historyObject.getString("menu_id"));
                        historyModel.setMenu(historyObject.getString("menu_nama"));
                        historyModel.setStatus(historyObject.getString("status_order"));
                        historyModel.setDeliveryDate(historyObject.getString("order_created_at"));
                        historyModel.setTotal_pay(historyObject.getInt("order_total_harga"));
                        historyItems.add(historyModel);

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
                    adapter = new HistroyAdapter(historyItems, activity);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    }

}
