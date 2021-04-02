package com.example.myapplication.adapter;

import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class VpAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragmentlist;

    public VpAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> fragmentlist) {
        super(fm, behavior);
        this.fragmentlist = fragmentlist;
    }

    public VpAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentlist.size();
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }
}
