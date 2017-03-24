package midori.kitchen.content.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class CheckoutReviewFragment extends Fragment {

    @BindView(R.id.btn_go_delivery)
    Button btnGoDelivery;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_review, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.btn_go_delivery)
    public void onClick() {
        sendBroadcast("goDelivery");
    }

    private void sendBroadcast(String typeBroadcast) {
        Intent intent = new Intent(typeBroadcast);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
}
