package midori.kitchen.content.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.triggertrap.seekarc.SeekArc;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import midori.kitchen.R;
import midori.kitchen.manager.AppPrefManager;

/**
 * Created by masrofbayhaqqi on 7/29/17.
 */

public class CaloriesFragment extends Fragment {

    @BindView(R.id.tv_fullname)
    TextView tvFullname;
    @BindView(R.id.civ_photo)
    CircleImageView civPhoto;
    @BindView(R.id.tv_cash)
    TextView tvCash;
    @BindView(R.id.tv_point)
    TextView tvPoint;
    @BindView(R.id.seekArc)
    SeekArc seekArc;
    @BindView(R.id.txtxseekArcProgress)
    TextView txtxseekArcProgress;

    private AppPrefManager appPrefManager;
    private String fullname;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calories, container, false);
        ButterKnife.bind(this, view);
        appPrefManager = new AppPrefManager(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitleBar = (TextView) getActivity().findViewById(R.id.tv_title_bar);
        tvTitleBar.setText(R.string.midori_kitchen);

        getDataUser();
        initView();

        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onProgressChanged(SeekArc seekArc, int progress,
                                          boolean fromUser) {
                txtxseekArcProgress.setText(String.valueOf(progress*130) + "/13000 kkal");
            }
        });

    }

    private void getDataUser() {
        fullname = appPrefManager.getUser().get(AppPrefManager.KEY_FULLNAME);
    }

    private void initView() {
        tvFullname.setText(fullname);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
