package com.example.frontalis.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.frontalis.QuestionFragment;

import java.util.List;

public class QuestionFragmentAdapter extends FragmentPagerAdapter {

    Context context;
    List<QuestionFragment> fragmentList;

    public QuestionFragmentAdapter(FragmentManager fm, Context context, List<QuestionFragment> fragmentList) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return new StringBuilder("Question ").append(position + 1).toString();
    }
}
