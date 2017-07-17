package midori.chef.content.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.chef.content.model.MenuModel;
import midori.kitchen.R;

/**
 * Created by BimoV on 3/11/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Context context;
    private List<MenuModel> menuList = new ArrayList<>();

    public MenuAdapter(Context context, List<MenuModel> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from((parent).getContext()).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MenuModel model = menuList.get(position);
        holder.menu.setText(model.getMenu());
        holder.jumlahPesan.setText("Jumlah Pesan : " + model.getJumlah_pesanan());
        holder.stock.setText("Stock Makanan : "+model.getStock());
        holder.delivery.setText(model.getDeliveryDate());

        Glide.with(context)
                .load(model.getImage())
                .centerCrop()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.menu)
        TextView menu;
        @BindView(R.id.jumlah_pesan)
        TextView jumlahPesan;
        @BindView(R.id.cardview)
        CardView cardview;
        @BindView(R.id.stock)
        TextView stock;
        @BindView(R.id.delivery)
        TextView delivery;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
