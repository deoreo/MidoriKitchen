package midori.kitchen.content.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import midori.kitchen.content.model.HistoryModel;
import midori.kitchen.content.model.MenuModel;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.JSONControl;

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

        if(status.equalsIgnoreCase("waiting payment")){
            holder.layoutStatus.setBackgroundResource(R.drawable.rounded_red);
        }else if(status.equalsIgnoreCase("confirm payment")){
            holder.layoutStatus.setBackgroundResource(R.drawable.rounded_blue);
        }
        else{
            holder.layoutStatus.setBackgroundResource(R.drawable.rounded_bg_transparent);
        }
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

        @OnClick(R.id.list_item)
        public void onClick(View v) {

            int i = getAdapterPosition();
            final HistoryModel item = historyItems.get(i);
            if(item.getStatus().equalsIgnoreCase("waiting payment")) {
                new MaterialDialog.Builder(activity)
                        .title("Konfirmasi Pembayaran" + item.getMenu())
                        .typeface("GothamRnd-Medium.otf", "Gotham.ttf")
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .input("Nomor Rekening Anda", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                            }
                        }).inputType(InputType.TYPE_CLASS_NUMBER)
                        .cancelable(false)
                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new DoUpdateStatus(activity).execute(item.getOrder_id(),"6");
                            }
                        })
                        .alwaysCallInputCallback()
                        .show();
            }
            else if(item.getStatus().contains("cooking")){
                new MaterialDialog.Builder(activity)
                        .title("Terima kasih")
                        .content(item.getMenu()+" akan dikirim pada "+item.getDeliveryDate())
                        .typeface("GothamRnd-Medium.otf", "Gotham.ttf")
                        .cancelable(false)
                        .show();
            }
        }

    }

    private class DoUpdateStatus extends AsyncTask<String, Void, String> {
        private Activity activity;
        private Context context;
        private Resources resources;

        public DoUpdateStatus(Activity activity) {
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
                String order_id = params[0];
                String status_id = params[1];


                JSONControl jsControl = new JSONControl();

                JSONObject response = jsControl.updateStatus(activity, order_id, status_id);
                Log.d("json responseStatus", response.toString());
                if (!response.toString().contains("error")) {
                    return "OK";
                }
                else {
                    return "FAIL";
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return "FAIL";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            switch (result) {
                case "FAIL":
                    Toast.makeText(activity, "Confirmation fail", Toast.LENGTH_SHORT).show();
                    break;
                case "OK":
                    Toast.makeText(activity, "Confirmation processed", Toast.LENGTH_SHORT).show();
                    SendBroadcast("refresh", "refresh");
                    break;
            }
        }
    }

    private void SendBroadcast(String typeBroadcast, String type) {
        Intent intent = new Intent(typeBroadcast);
        // add data
        intent.putExtra("message", type);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
    }

}
