package midori.kitchen.manager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import midori.kitchen.R;

import static android.view.View.GONE;

/**
 * Created by User on 8/18/2015.
 */
public class MySupportMapFragment extends MapFragment implements OnMapReadyCallback {

    protected View mOriginalContentView;
    public TouchableWrapper mTouchView;
    private final String TAG = "MySupportMapFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);
        mTouchView = new TouchableWrapper(getActivity());
        if(NetworkManager.getInstance(getActivity()).isConnectedInternet()) {
            mTouchView.addView(mOriginalContentView);
        }
        return mTouchView;
    }

    @Override
    public View getView() {
        Log.d(TAG, "getView");
        return mOriginalContentView;
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

    }
}