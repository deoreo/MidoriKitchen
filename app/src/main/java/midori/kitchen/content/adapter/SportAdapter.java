package midori.kitchen.content.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;
import midori.kitchen.content.model.SportModel;

/**
 * Created by Januar on 9/23/2017.
 */

public class SportAdapter extends RecyclerView.Adapter<SportAdapter.ViewHolder> {



    private Activity activity;
    private ArrayList<SportModel> sportModel = new ArrayList<>();

    public SportAdapter(ArrayList<SportModel> sportModel, Activity activity) {
        super();
        this.activity = activity;
        this.sportModel = sportModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sport_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SportModel items = sportModel.get(position);

        final String id = items.getId();
        final String name = items.getName();
        final int price = items.getPrice();
        final String addres = items.getAddress();
        final String challenge = items.getChalenge();
        final String photo = items.getPhoto();

        if (items.getPhoto().isEmpty()) {
            holder.layoutPlaceholder.setVisibility(View.VISIBLE);
            holder.rvAvatar.setVisibility(View.INVISIBLE);
        } else {
            holder.layoutPlaceholder.setVisibility(View.INVISIBLE);
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
        holder.titleSport.setText(name);
        holder.tvPrice.setText("Rp. " + String.valueOf(price));
        holder.listItem.setTag(position);
        holder.challenge.setText("challenge : "+ challenge);
        holder.addressSport.setText(addres);

    }

    @Override
    public int getItemCount() {
        return sportModel.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_placeholder)
        RelativeLayout layoutPlaceholder;
        @BindView(R.id.rv_avatar)
        RoundedImageView rvAvatar;
        @BindView(R.id.title_sport)
        TextView titleSport;
        @BindView(R.id.address_sport)
        TextView addressSport;
        @BindView(R.id.avatar_layout)
        RelativeLayout avatarLayout;
        @BindView(R.id.image_layout)
        RelativeLayout imageLayout;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.list_item)
        LinearLayout listItem;
        @BindView(R.id.challenge)
        TextView challenge;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
