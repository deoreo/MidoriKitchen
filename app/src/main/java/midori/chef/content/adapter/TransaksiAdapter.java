package midori.chef.content.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;
import midori.chef.content.model.TransaksiModel;

/**
 * Created by BimoV on 3/10/2017.
 */

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ViewHolder> {

    private Context context;
    private List<TransaksiModel> transaksiList = new ArrayList<>();

    public TransaksiAdapter(Context context, List<TransaksiModel> transaksiList) {
        this.context = context;
        this.transaksiList = transaksiList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent).getContext()).inflate(R.layout.item_transaksi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TransaksiModel item = transaksiList.get(position);
        holder.menu.setText(item.getMenu());
        holder.jumlahPesan.setText("Jumlah Pesan : " + item.getJumlah_pesan());
        double harga = Double.parseDouble(item.getHarga());
        holder.harga.setText("Harga : " + formatRupiah(harga));
        holder.status.setText("Status : " + item.getStatus());


        if(item.getAvatar().contains("http")){
            Glide
                    .with(context)
                    .load(item.getAvatar())
                    .centerCrop()
                    .crossFade()
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.pic)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image);
        }
        else {
            byte[] imageByteArray = Base64.decode(item.getAvatar(), Base64.DEFAULT);
            Glide
                    .with(context)
                    .load(imageByteArray)
                    .centerCrop()
                    .crossFade()
                    .placeholder(R.drawable.pic)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image);
        }
    }
    private String formatRupiah(double kurs) {
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(formatRp);
        return decimalFormat.format(kurs);
    }
    @Override
    public int getItemCount() {
        return transaksiList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.menu)
        TextView menu;
        @BindView(R.id.jumlah_pesan)
        TextView jumlahPesan;
        @BindView(R.id.harga)
        TextView harga;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.cardview)
        CardView cardview;
        @BindView(R.id.image)
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
