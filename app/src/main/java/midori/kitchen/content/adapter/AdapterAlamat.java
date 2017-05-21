package midori.kitchen.content.adapter;

/**
 * Created by deoreo06 on 22/10/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.List;

import midori.kitchen.R;
import midori.kitchen.content.model.PlaceModel;
import midori.kitchen.database.DatabaseHandler;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.AppPrefManager;


public class AdapterAlamat extends BaseAdapter {
    private Activity mAct;
    private List<PlaceModel> mSourceData, mFilterData;
    private LayoutInflater mInflater =null;
    private boolean mKeyIsEmpty = false;
    private int height=0,width=0;
    private DecimalFormat decimalFormat;
    private DatabaseHandler db;

    public AdapterAlamat(Activity activity, List<PlaceModel> d) {
        mAct = activity;
        mSourceData = d;
        mInflater = (LayoutInflater) mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(d==null || d.isEmpty()){
            mKeyIsEmpty = true;
        }
        db = new DatabaseHandler(activity);

    }

    @Override
    public int getCount() {
        if(mKeyIsEmpty)
            return 1;
        return mSourceData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(mKeyIsEmpty){
            convertView = mInflater.inflate(R.layout.row_delivery_empty2, null);
        }
        else {
            final ViewHolder holder;
            convertView = mInflater.inflate(R.layout.row_alamat, null);
            holder = new ViewHolder();
            holder.txtAlamat = (TextView) convertView.findViewById(R.id.txtAlamat);
            holder.txtAlamatDetail = (TextView) convertView.findViewById(R.id.txtAlamatDetail);
            holder.btnAlamat = (LinearLayout) convertView.findViewById(R.id.layoutAlamat);
            convertView.setTag(position);

            final PlaceModel modelMenu = mSourceData.get(position);
            final String ID = modelMenu.getPlaceId();
            final String ALAMAT = modelMenu.getAddress();
            final String ALAMAT_DETAIL = modelMenu.getAddressDetail();

            holder.txtAlamat.setText(ALAMAT);
            holder.txtAlamatDetail.setText(ALAMAT_DETAIL);

            holder.btnAlamat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppData.address = ALAMAT+","+ALAMAT_DETAIL;
                    AppPrefManager.getInstance(mAct).setAlamat(ALAMAT);
                    SendBroadcast("changeAlamat", ALAMAT);

                }
            });


        }
        return convertView;
    }

    private static class ViewHolder {
        public LinearLayout btnAlamat;
        public TextView txtAlamat;
        public TextView txtAlamatDetail;
    }

    private void SendBroadcast(String typeBroadcast,String type){
        Intent intent = new Intent(typeBroadcast);
        // add data
        intent.putExtra("message", type);
        LocalBroadcastManager.getInstance(mAct).sendBroadcast(intent);
    }




}
