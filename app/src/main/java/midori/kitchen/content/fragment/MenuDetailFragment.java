package midori.kitchen.content.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import midori.kitchen.content.activity.BuyActivity;
import midori.kitchen.manager.AppData;

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
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.fab_checkout)
    FloatingActionButton fabCheckout;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.tv_calories)
    TextView tvCalories;
    @BindView(R.id.tv_owner)
    TextView tvOwner;

    private String menu, description, price, owner, photo;
    private int calories;

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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            menu = bundle.getString("menu");
            description = bundle.getString("description");
            price = bundle.getString("price");
            owner = bundle.getString("owner");
            calories = bundle.getInt("calories");
            photo = bundle.getString("photo");
            initView();
        }
    }

    private void initView() {
        tvMenu.setText(menu);
        tvDescription.setText(description);
        tvPrice.setText("Rp. " + price);
        tvOwner.setText(owner);
        tvCalories.setText(calories + " Calories");
        Glide
                .with(this)
                .load(photo)
                .centerCrop()
                .crossFade()
                .thumbnail(0.5f)
                .placeholder(R.drawable.bg_menu)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPhoto);
        ratingBar.setRating(3);
    }

    @OnClick(R.id.fab_checkout)
    public void onClick() {
        AppData.buyModel.setMenu(menu);
        AppData.buyModel.setTotal_menu(1);
        AppData.buyModel.setPrice_menu(Integer.parseInt(price));
        Intent intent = new Intent(getActivity(), BuyActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
