package com.lazy.component.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.Fragment;
import androidx.core.app.FragmentManager;
import androidx.core.app.FragmentStatePagerAdapter;
import androidx.core.view.PagerAdapter;

import java.util.List;

/**
 * Implementation of {@link PagerAdapter} that
 * uses a {@link Fragment} to manage each page. This class also handles
 * saving and restoring of fragment's state.
 *
 * @author zdxiang
 * @date 2018-07-27
 */
public class BaseFragmentStateAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private String[] titles;

    public BaseFragmentStateAdapter(List<Fragment> list, FragmentManager fm) {
        super(fm);
        this.fragments = list;
    }

    public BaseFragmentStateAdapter(List<Fragment> list, FragmentManager fm, String[] titles) {
        super(fm);
        this.fragments = list;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
