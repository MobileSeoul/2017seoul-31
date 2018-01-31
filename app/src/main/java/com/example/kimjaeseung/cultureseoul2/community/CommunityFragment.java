package com.example.kimjaeseung.cultureseoul2.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kimjaeseung.cultureseoul2.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kimjaeseung on 2017. 7. 11..
 */

public class CommunityFragment extends Fragment {
    private static final String TAG = CommunityFragment.class.getSimpleName();
    private static int PAGE_NUMBER = 3;
    private static int PAGE_SELECT = 0;

    @Bind(R.id.community_tablayout)
    TabLayout tabLayout;
    @Bind(R.id.community_viewpager)
    ViewPager viewPager;

    public CommunityFragment() {
    }

    public static Fragment getInstance() {
        CommunityFragment communityFragment = new CommunityFragment();
        return communityFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        makePager();
    }

    private void makePager() {
        tabLayout.addTab(tabLayout.newTab().setText("실시간"));
        tabLayout.addTab(tabLayout.newTab().setText("나의공연"));
        tabLayout.addTab(tabLayout.newTab().setText("날짜"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(PAGE_SELECT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                PAGE_SELECT = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        private int tabCount;

        public PagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CommunityRealTimeFragment();
                case 1:
                    return new CommunityMyFragment();
                case 2:
                    return new CommunityDateFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return PAGE_NUMBER;
        }
    }
}