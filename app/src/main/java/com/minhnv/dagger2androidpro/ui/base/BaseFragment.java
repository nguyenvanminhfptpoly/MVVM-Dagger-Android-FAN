package com.minhnv.dagger2androidpro.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.minhnv.dagger2androidpro.R;
import com.minhnv.dagger2androidpro.ViewModelProviderFactory;
import com.minhnv.dagger2androidpro.utils.rx.ScheduleProvider;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseFragment<V extends BaseViewModel> extends DaggerFragment {
    @Inject
    public ViewModelProviderFactory factory;

    @Inject
    public ScheduleProvider schedulerProvider;

    public V viewmodel;

    public BaseActivity baseActivity;

    public static CompositeDisposable compositeDisposable;

    public View mRootView;

    public abstract @LayoutRes int getLayoutId();

    public abstract void onViewReady(View view);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection();
        super.onCreate(savedInstanceState);

    }

    private void performDependencyInjection(){
        AndroidSupportInjection.inject(this);
    }


    public BaseActivity getBaseActivity() {
        return baseActivity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof BaseActivity){
            BaseActivity activity = (BaseActivity) context;
            this.baseActivity = activity;

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), null);
        onViewReady(mRootView);
        return mRootView;
    }


    @Override
    public void onDetach() {
        baseActivity = null;
        super.onDetach();
    }
    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }


}


