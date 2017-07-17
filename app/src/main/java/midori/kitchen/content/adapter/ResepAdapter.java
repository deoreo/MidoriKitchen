package midori.kitchen.content.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import midori.kitchen.content.activity.DetailActivity;
import midori.kitchen.content.activity.ResepDetailActivity;
import midori.kitchen.content.model.HistoryModel;
import midori.kitchen.content.model.ResepModel;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.AppPrefManager;
import midori.kitchen.manager.JSONControl;

import static android.view.View.VISIBLE;

/**
 * Created by M. Asrof Bayhaqqi on 3/12/2017.
 */

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<ResepModel> resepModels = new ArrayList<>();

    public ResepAdapter(ArrayList<ResepModel> resepModels, Activity activity) {
        super();
        this.activity = activity;
        this.resepModels = resepModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_resep_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ResepModel items = resepModels.get(position);

        final String id = items.getId();
        final String resep = items.getResep();
        final String cara_buat = items.getCara_buat();
        final String image = items.getImage();
        final String url = items.getUrl();
        final String range_price = items.getRange_harga();

        holder.tvResep.setText(resep);
        holder.tvPrice.setText(range_price);
        if (items.getImage().isEmpty()) {
            holder.ivPlaceholder.setVisibility(VISIBLE);
            holder.rvResep.setVisibility(View.INVISIBLE);
        } else if(items.getImage().contains("http")){
            holder.ivPlaceholder.setVisibility(View.INVISIBLE);
            holder.rvResep.setVisibility(VISIBLE);
            Glide
                    .with(activity)
                    .load(image)
                    .centerCrop()
                    .crossFade()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.rvResep);
        }
        else {
            byte[] imageByteArray = Base64.decode(items.getImage(), Base64.DEFAULT);
            holder.ivPlaceholder.setVisibility(View.INVISIBLE);
            holder.rvResep.setVisibility(VISIBLE);
            Glide
                    .with(activity)
                    .load(imageByteArray)
                    .centerCrop()
                    .crossFade()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.rvResep);
        }
    }

    @Override
    public int getItemCount() {
        return resepModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_placeholder)
        ImageView ivPlaceholder;
        @BindView(R.id.tv_resep)
        TextView tvResep;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.rv_resep)
        RoundedImageView rvResep;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.list_item)
        public void onClick(View v) {
            int i = getAdapterPosition();
            final ResepModel item = resepModels.get(i);
            AppData.resepModel = item;
            Intent intent = new Intent(v.getContext(), ResepDetailActivity.class);
            Bundle extras = new Bundle();
            //AppData.id = item.getId();
            //extras.putString("id", item.getId());
            extras.putString("tag_detail", AppData.resep_tag);
            extras.putString("name_detail", item.getResep());
            intent.putExtras(extras);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            activity.startActivity(intent);

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
                    String status_id = params[2];
                    String menu_nama = params[1];


                    JSONControl jsControl = new JSONControl();

                    JSONObject response = jsControl.updateStatus(activity, order_id, status_id);
                    Log.d("json responseStatus", response.toString());
                    if (!response.toString().contains("error")) {
                        AppData.order_id_history = order_id;
                        AppData.menu_nama_history = menu_nama;
                        return "OK";
                    } else {
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
                        sendEmail(AppData.order_id_history, AppData.menu_nama_history, AppData.rekening_user);
                        //Toast.makeText(activity, "Confirmation processed", Toast.LENGTH_SHORT).show();
                        SendBroadcast("refresh", "refresh");
                        break;
                }
            }
        }

        protected void sendEmail(String order_id, String menunama, String norek) {

            BackgroundMail.newBuilder(activity)
                    .withUsername("midorichef@gmail.com")
                    .withPassword("adminmk123")
                    .withMailto("admin@midorikitchen.top")
                    .withType(BackgroundMail.TYPE_PLAIN)
                    .withSubject("Konfirmasi Pesanan no. " + order_id)
                    .withBody("Dear Midori Kitchen,\n\nOrder " + menunama + " dengan no." + order_id + " ,telah dibayar oleh " + AppPrefManager.getInstance(activity).getUser().get(AppPrefManager.KEY_FULLNAME) + " melalui rekening " + norek + "\n\nRegards")
                    .withSendingMessage("Mengirim konfirmasi")
                    .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                        @Override
                        public void onSuccess() {
                            new MaterialDialog.Builder(activity)
                                    .title("Terima kasih")
                                    .content("Admin akan segera mengkonfirmasi pembayaran Anda")
                                    .typeface("GothamRnd-Medium.otf", "Gotham.ttf")
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .negativeText("Close")
                                    .show();
                        }
                    })
                    .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                        @Override
                        public void onFail() {
                            new MaterialDialog.Builder(activity)
                                    .title("Mohon maaf")
                                    .content("Konfirmasi Anda belum terkirim, silahkan menghubungi admin kami melalui +6281310175256 atau  admin@midorikitchen.top")
                                    .typeface("GothamRnd-Medium.otf", "Gotham.ttf")
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .negativeText("Close")
                                    .show();
                        }
                    })
                    .send();


        }

        private void SendBroadcast(String typeBroadcast, String type) {
            Intent intent = new Intent(typeBroadcast);
            // add data
            intent.putExtra("message", type);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        }
    }

}
