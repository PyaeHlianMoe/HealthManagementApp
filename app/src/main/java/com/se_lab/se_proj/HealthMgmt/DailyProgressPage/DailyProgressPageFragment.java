package com.se_lab.se_proj.HealthMgmt.DailyProgressPage;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.se_lab.se_proj.BaseFragment;
import com.se_lab.se_proj.HealthMgmt.ActivityHealthMgmt;
import com.se_lab.se_proj.HealthMgmt.HealthContract;
import com.se_lab.se_proj.R;
import com.se_lab.se_proj.Utilities;
import com.se_lab.se_proj.databinding.FragmentMainDailyPageBinding;
import com.se_lab.se_proj.databinding.ItemMainMealBinding;
import com.se_lab.se_proj.model.BaseMeal;
import com.se_lab.se_proj.model.Diet;
import com.se_lab.se_proj.model.DietProgress;
import com.se_lab.se_proj.model.Meal;
import com.se_lab.se_proj.model.Nutrient;

import java.util.ArrayList;
import java.util.List;

public class DailyProgressPageFragment extends BaseFragment implements DailyProgressPageContract.View{

    public static final String DATE_KEY = "date";

    private DailyProgressPageContract.Presenter mPresenter;

    private FragmentMainDailyPageBinding binding;

    private MealsAdapter mMealsAdapter;


    @Override
    public void setupPresenter() {
        mPresenter = new DailyProgressPagePresenter(
                ((ActivityHealthMgmt) getActivity()).getActivityModel(),
                this
        );
        mPresenter.start();
    }

    @Override
    public void setPresenter(DailyProgressPageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainDailyPageBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMealsAdapter = new MealsAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.mealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mealsRecyclerView.setNestedScrollingEnabled(true);
        binding.mealsRecyclerView.setAdapter(mMealsAdapter);

        setupPresenter();

        long date = getArguments().getLong(DATE_KEY);
        mPresenter.onDateLoaded(date);
    }

    @Override
    public void showDailyDietProgress(DietProgress dailyDietProgress) {

        mMealsAdapter.setMeals(dailyDietProgress.getMealsData());

        // Daily calories
        binding.caloriesProgress.setProgress(dailyDietProgress.getUsedCaloriesPercent());

        binding.caloriesPercent.setText(getString(R.string.unit_format_percent,
                dailyDietProgress.getUsedCaloriesPercent()));

        binding.caloriesValue.setText(getString(
                R.string.unit_format_calories_per_calories,
                (int) dailyDietProgress.getTotalNutrientValue(Nutrient.ENERGY),
                dailyDietProgress.getDailyCaloriesLimit()));

        // Protein
        binding.proteinValue.setText(getString(R.string.unit_format_weight_per_weight_g,
                (int) dailyDietProgress.getTotalNutrientValue(Nutrient.PROTEIN),
                dailyDietProgress.getProteinWeight()));
        binding.proteinProgress.setProgress(dailyDietProgress.getUsedRatioTypePercent(Diet.PROTEIN_PERCENT));
        binding.proteinPercent.setText(getString(R.string.unit_format_percent,
                dailyDietProgress.getUsedRatioTypePercent(Diet.PROTEIN_PERCENT)));

        // Carbs
        binding.carbsValue.setText(getString(R.string.unit_format_weight_per_weight_g,
                (int) dailyDietProgress.getTotalNutrientValue(Nutrient.CARBOHYDRATE),
                dailyDietProgress.getCarboWeight()));
        binding.carbsProgress.setProgress(dailyDietProgress.getUsedRatioTypePercent(Diet.CARBO_PERCENT));
        binding.carbsPercent.setText(getString(R.string.unit_format_percent,
                dailyDietProgress.getUsedRatioTypePercent(Diet.CARBO_PERCENT)));

        // Fat
        binding.fatValue.setText(getString(R.string.unit_format_weight_per_weight_g,
                (int) dailyDietProgress.getTotalNutrientValue(Nutrient.FAT),
                dailyDietProgress.getFatWeight()));
        binding.fatProgress.setProgress(dailyDietProgress.getUsedRatioTypePercent(Diet.FAT_PERCENT));
        binding.fatPercent.setText(getString(R.string.unit_format_percent,
                dailyDietProgress.getUsedRatioTypePercent(Diet.FAT_PERCENT)));

        // Setup PieChart
        setupNutrientsPieChart(dailyDietProgress);

    }

    private void setupNutrientsPieChart(DietProgress dietProgress) {

        // Create list of entries
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(dietProgress.getRatioTypePercent(Diet.PROTEIN_PERCENT),
                getString(R.string.nutrient_protein)));
        entries.add(new PieEntry(dietProgress.getRatioTypePercent(Diet.CARBO_PERCENT),
                getString(R.string.nutrient_carbohydrates)));
        entries.add(new PieEntry(dietProgress.getRatioTypePercent(Diet.FAT_PERCENT),
                getString(R.string.nutrient_fat)));

        // Configure data set design
        PieDataSet set = new PieDataSet(entries, "");
        set.setSliceSpace(getResources().getDimension(R.dimen.macronutrients_ratio_pie_chart_center_slice_space));
        set.setValueTextSize(18f);
        set.setValueFormatter(new PercentFormatter());
        set.setValueTextColor(Color.WHITE);
        set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        set.setHighlightEnabled(true);
        set.setColors(new int[]{
                R.color.colorDeepOrange700,
                R.color.colorGreen700,
                R.color.colorDeepPurple700}, getContext());


        PieData pieData = new PieData(set);

        binding.nutrientsPieChart.setData(pieData);

        // Hide legend and description
        binding.nutrientsPieChart.getLegend().setEnabled(false);
        binding.nutrientsPieChart.getDescription().setEnabled(false);

        // Configure labels (protein, carbs, fat)
        ((PieChartRenderer) binding.nutrientsPieChart.getRenderer()).getPaintEntryLabels().setColor(Color.BLACK);

        binding.nutrientsPieChart.setCenterText(getString(R.string.main_daily_macronutrients_ratio_label));
        binding.nutrientsPieChart.setUsePercentValues(true);
        binding.nutrientsPieChart.invalidate();
    }

    @Override
    public void showAddFood(int mealNumber) {
        ((HealthContract.View) getActivity()).showAddFood(mealNumber);
    }

    private class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {

        List<Meal> mMeals;

        class MealViewHolder extends RecyclerView.ViewHolder {
            private ItemMainMealBinding binding;

            MealViewHolder(View itemView) {
                super(itemView);
                binding = DataBindingUtil.bind(itemView);
            }

            public ItemMainMealBinding getBinding() {
                return binding;
            }

        }

        @Override
        public MealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_main_meal, parent, false);
            return new MealViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MealViewHolder holder, int position) {
            final int adapterPosition = holder.getAdapterPosition();
            final Meal meal = mMeals.get(adapterPosition);
            holder.getBinding().mealNumber.setText(String.valueOf(meal.getNumber()));

            if (meal.getNameId() == BaseMeal.TYPE_OTHER) holder.getBinding()
                    .mealName.setText(meal.getName());
            else holder.getBinding()
                    .mealName.setText(Utilities.getMealNameByNameId(meal.getNameId()));

            holder.getBinding().mealCaloriesDailyLimit.setText(
                    getString(R.string.unit_format_calories_per_calories,
                            (int) meal.getTotalNutrientValue(Nutrient.ENERGY),
                            meal.getCaloriesLimit()));
            holder.getBinding().mealTime.setText(Utilities.getTimeToDisplay(
                    meal.getTimeHour(),
                    meal.getTimeMinutes()
            ));

            holder.getBinding().mealCaloriesProgress.setProgress(meal.getCaloriesUsedPercent());
            holder.getBinding().mealCaloriesPercentValue.setText(getString(
                    R.string.unit_format_percent, meal.getCaloriesUsedPercent()));

            holder.getBinding().mealButtonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.onAddFoodClicked(adapterPosition);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mMeals == null) return 0;
            return mMeals.size();
        }

        public void setMeals(List<Meal> meals) {
            this.mMeals = meals;
            notifyDataSetChanged();
        }

    }
}
