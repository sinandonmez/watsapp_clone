package com.tilda.watsapp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private String[] baslik = {"", "Sohbetler","Durum","Kişiler"};

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new AramalarFragment();
        }
        else if(position==1){
            return new SohbetlerFragment();
        }
        else if(position==2){
            return new DurumlarFragment();
        }
        else if(position==3){
            return new AramalarFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return baslik[position];
    }
}
