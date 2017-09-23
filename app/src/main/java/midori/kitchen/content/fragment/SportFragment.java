package midori.kitchen.content.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;
import midori.kitchen.content.adapter.MenuAdapter;
import midori.kitchen.content.adapter.SportAdapter;
import midori.kitchen.content.data.DataHelper;
import midori.kitchen.content.model.MenuModel;
import midori.kitchen.content.model.SportModel;

/**
 * Created by Januar on 9/23/2017.
 */

public class SportFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<SportModel> sportModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sport, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitleBar = (TextView) getActivity().findViewById(R.id.tv_title_bar);
        tvTitleBar.setText(R.string.midori_kitchen);

        initRecyclerView();
        getDataMenu();

    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SportAdapter(sportModels,getActivity());
        recyclerView.setAdapter(adapter);


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataMenu();
            }
        });
    }

    private void getDataMenu() {
        sportModels.clear();
        sportModels.addAll(DataHelper.getDataSport());
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
