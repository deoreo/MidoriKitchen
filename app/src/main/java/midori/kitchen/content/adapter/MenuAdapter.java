package midori.kitchen.content.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import midori.kitchen.content.activity.DetailActivity;
import midori.kitchen.content.model.MenuModel;
import midori.kitchen.manager.AppData;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<MenuModel> menuItems = new ArrayList<>();

    public MenuAdapter(ArrayList<MenuModel> menuItems, Activity activity) {
        super();
        this.activity = activity;
        this.menuItems = menuItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_menu_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MenuModel items = menuItems.get(position);

        final String id = items.getId();
        final String menu = items.getMenu();
        final int price = items.getPrice();
        final String time = items.getDeliveryDate();
        final String photo = items.getPhoto();

        if (items.getPhoto().isEmpty()) {
            holder.ivPlaceholder.setVisibility(View.VISIBLE);
            holder.rvAvatar.setVisibility(View.INVISIBLE);
        } else {
            holder.ivPlaceholder.setVisibility(View.INVISIBLE);
            holder.rvAvatar.setVisibility(View.VISIBLE);
            Glide
                    .with(activity)
                    .load(photo)
                    .centerCrop()
                    .crossFade()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.rvAvatar);
        }
        holder.tvMenu.setText(menu);
        holder.tvPrice.setText("Rp. " + String.valueOf(price));
        holder.tvTime.setText(time);
        holder.listItem.setTag(position);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_placeholder)
        ImageView ivPlaceholder;
        @BindView(R.id.rv_avatar)
        RoundedImageView rvAvatar;
        @BindView(R.id.tv_menu)
        TextView tvMenu;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.list_item)
        LinearLayout listItem;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.list_item)
        public void onClick(View v) {
            int i = getAdapterPosition();
            MenuModel item = menuItems.get(i);
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            Bundle extras = new Bundle();
            extras.putString("tag_detail", AppData.detail_menu_tag);
            extras.putString("name_detail", item.getMenu());
            extras.putString("menu", item.getMenu());
            extras.putString("description", item.getDescription());
            extras.putString("price", String.valueOf(item.getPrice()));
            extras.putString("delivery_date", item.getDeliveryDate());
            extras.putString("photo", item.getPhoto());
            intent.putExtras(extras);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            activity.startActivity(intent);
        }
    }
}
