package com.se_lab.se_proj.HealthMgmt.addfood.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.se_lab.se_proj.BaseFragment;
import com.se_lab.se_proj.HealthMgmt.addfood.AddActivity;
import com.se_lab.se_proj.R;
import com.se_lab.se_proj.Utilities;
import com.se_lab.se_proj.databinding.FragmentAddMenuBinding;
import com.se_lab.se_proj.databinding.ItemAddMenuFoodBinding;
import com.se_lab.se_proj.model.Food;

import java.util.List;

/**
 * Fragment which contains menu to enable user to choose what to add: dish, market product or
 * product
 */

public class AddMenuFragment extends BaseFragment implements AddMenuContract.View {

    FragmentAddMenuBinding binding;
    AddMenuContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddMenuBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupPresenter();
        binding.buttonProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onProductsClicked();
            }
        });

    }

    @Override
    public void setupPresenter() {
        mPresenter = new AddMenuPresenter(
                ((AddActivity) getActivity()).getActivityModel(), // Model
                this); // View
        mPresenter.start();
    }

    @Override
    public void setPresenter(AddMenuContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProducts() {
        ((AddActivity) getActivity()).showProducts();
    }

    @Override
    public void showAddDialog() {
        ((AddActivity) getActivity()).showAddFoodDialog();
    }

    @Override
    public void updateFoods(List<Food> foods) {
        for (final Food food : foods) {
            ItemAddMenuFoodBinding itemBinding = ItemAddMenuFoodBinding.inflate(
                    LayoutInflater.from(getContext()));
            itemBinding.foodName.setText(food.getName());
            itemBinding.foodType.setText(getString(
                    Utilities.getFoodTypeString(food.getFoodType())));
            itemBinding.foodWeight.setText(getString(
                    R.string.unit_format_weight_g, food.getWeight()));
            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.onFoodClicked(food);
                }
            });
            itemBinding.foodDate.setText(
                    Utilities.getFormattedTimeDifference(getContext(),
                            food.getAddDate())
            );
            binding.foodLayout.addView(itemBinding.getRoot());
        }
    }

}
