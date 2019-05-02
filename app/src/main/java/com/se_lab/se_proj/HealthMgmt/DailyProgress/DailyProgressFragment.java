package com.se_lab.se_proj.HealthMgmt.DailyProgress;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.se_lab.se_proj.BaseFragment;
import com.se_lab.se_proj.HealthMgmt.ActivityHealthMgmt;
import com.se_lab.se_proj.HealthMgmt.DailyProgressPage.DailyProgressPageFragment;
import com.se_lab.se_proj.R;
import com.se_lab.se_proj.Utilities;
import com.se_lab.se_proj.databinding.FragmentMainDailyBinding;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyProgressFragment extends BaseFragment implements DailyProgressContract.View {

    private DailyProgressContract.Presenter mPresenter;

    private FragmentMainDailyBinding mBinding;

    PagerAdapter mPagerAdapter;

    @Override
    public void setupPresenter() {
        mPresenter = new DailyProgressPresenter(
                ((ActivityHealthMgmt) getActivity()).getActivityModel(),
                this
        );
        mPresenter.start();
    }

    @Override
    public void setPresenter(DailyProgressContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMainDailyBinding.inflate(
                LayoutInflater.from(getContext()), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupPresenter();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mPagerAdapter = new PagerAdapter(getChildFragmentManager(), calendar);
        mBinding.viewPager.setAdapter(mPagerAdapter);
        mBinding.viewPager.setAdapter(mPagerAdapter);
        mBinding.viewPager.setOffscreenPageLimit(4);
    }

    @Override
    public void showDayOfWeek(int dayOfWeek) {
        mBinding.viewPager.setCurrentItem(dayOfWeek, true);
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        Calendar mDate;

        @Override
        public CharSequence getPageTitle(int position) {
            // Page titles are based on days in week
            mDate.set(Calendar.DAY_OF_WEEK,
                    mDate.getFirstDayOfWeek() + position);
            return Utilities.getFormattedDay(getContext(), mDate);
        }

        PagerAdapter(FragmentManager fm, Calendar calendar) {
            super(fm);
            mDate = calendar;
        }

        @Override
        public Fragment getItem(int position) {

            // Set date based on positions
            // First page is always first day of week (Monday or Sunday)
            mDate.set(Calendar.DAY_OF_WEEK,
                    mDate.getFirstDayOfWeek() + position);

            // Send date via bundle object
            Bundle bundle = new Bundle();
            bundle.putLong(DailyProgressPageFragment.DATE_KEY, mDate.getTimeInMillis());

            Fragment fragment = new DailyProgressPageFragment();
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return 7;
        }
    }

}
