package midori.chef.content.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import midori.chef.manager.ChefPrefManager;
import midori.kitchen.R;

/**
 * Created by BimoV on 3/11/2017.
 */

public class AkunFragment extends Fragment {

    @BindView(R.id.image_background)
    ImageView imageBackground;
    @BindView(R.id.avatar)
    RoundedImageView avatar;
    @BindView(R.id.etName)
    MaterialEditText etName;
    @BindView(R.id.etEmail)
    MaterialEditText etEmail;
    @BindView(R.id.etPhone)
    MaterialEditText etPhone;
    @BindView(R.id.etAddress)
    MaterialEditText etAddress;
    @BindView(R.id.profile_layout)
    CardView profileLayout;
    Unbinder unbinder;

    private ChefPrefManager chefPrefManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_akun, container, false);
        unbinder = ButterKnife.bind(this, v);
        chefPrefManager = new ChefPrefManager(getContext());
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName.setText(chefPrefManager.getUser().get("name"));
        etAddress.setText(chefPrefManager.getUser().get("address"));
        etPhone.setText(chefPrefManager.getUser().get("phone"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
