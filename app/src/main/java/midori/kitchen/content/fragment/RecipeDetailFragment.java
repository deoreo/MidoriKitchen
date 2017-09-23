package midori.kitchen.content.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.delivery)
    TextView delivery;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.tv_calories)
    TextView tvCalories;
    @BindView(R.id.tv_owner)
    TextView tvOwner;
    @BindView(R.id.tv_recipe)
    TextView tvRecipe;

    private String recipe, description, owner, photo;
    private int calories;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    private void getData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            recipe = bundle.getString("recipe");
            description = bundle.getString("description");
            owner = bundle.getString("owner");
            calories = bundle.getInt("calories");
            photo = bundle.getString("photo");
            initView();
        }
    }

    private void initView() {
        tvRecipe.setText(recipe);
        tvDescription.setText(description);
        tvOwner.setText(owner);
        tvCalories.setText(calories + " Calories");
        Glide
                .with(this)
                .load(photo)
                .centerCrop()
                .crossFade()
                .thumbnail(0.5f)
                .placeholder(R.drawable.bg_menu)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPhoto);
        ratingBar.setRating(3);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
