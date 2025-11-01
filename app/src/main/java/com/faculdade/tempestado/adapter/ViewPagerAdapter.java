package com.faculdade.tempestado.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.faculdade.tempestado.fragment.MapaFragment;
import com.faculdade.tempestado.fragment.PrevisaoFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new PrevisaoFragment();
        }
        else {
            return new MapaFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}