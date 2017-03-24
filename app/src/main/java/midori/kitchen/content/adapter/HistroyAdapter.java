package midori.kitchen.content.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;
import midori.kitchen.content.model.HistoryModel;

/**
 * Created by M. Asrof Bayhaqqi on 3/12/2017.
 */

public class HistroyAdapter extends RecyclerView.Adapter<HistroyAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<HistoryModel> historyItems = new ArrayList<>();

    public HistroyAdapter(ArrayList<HistoryModel> historyItems, Activity activity) {
        super();
        this.activity = activity;
        this.historyItems = historyItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HistoryModel items = historyItems.get(position);

        final String id = items.getId();
        final String menu = items.getMenu();
        final int price = items.getTotal_pay();
        final String time = items.getDeliveryDate();
        final String status = items.getStatus();

        holder.tvMenu.setText(menu);
        holder.tvPrice.setText("Rp. " + String.valueOf(price));
        holder.tvTime.setText(time);
        holder.tvStatus.setText(status);
        holder.listItem.setTag(position);
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_menu)
        TextView tvMenu;
        @BindView(R.id.delivery)
        TextView delivery;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.layout_status)
        LinearLayout layoutStatus;
        @BindView(R.id.list_item)
        LinearLayout listItem;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
