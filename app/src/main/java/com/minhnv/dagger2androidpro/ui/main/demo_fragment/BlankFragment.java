package com.minhnv.dagger2androidpro.ui.main.demo_fragment;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minhnv.dagger2androidpro.R;
import com.minhnv.dagger2androidpro.ui.base.BaseFragment;

public class BlankFragment extends BaseFragment<BlankViewModel> implements BlankFragmentNavigator {

    public static BlankFragment newInstance() {
        Bundle args = new Bundle();
        BlankFragment fragment = new BlankFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.blank_fragment;
    }

    @Override
    public void onViewReady(View view) {
        viewmodel = ViewModelProviders.of(this,factory).get(BlankViewModel.class);
        viewmodel.setNavigator(this);
    }
}
