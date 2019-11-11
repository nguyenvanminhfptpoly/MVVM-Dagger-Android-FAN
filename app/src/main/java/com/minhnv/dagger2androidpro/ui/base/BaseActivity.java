package com.minhnv.dagger2androidpro.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.minhnv.dagger2androidpro.R;
import com.minhnv.dagger2androidpro.ViewModelProviderFactory;
import com.minhnv.dagger2androidpro.utils.CommonUtils;
import com.minhnv.dagger2androidpro.utils.rx.ScheduleProvider;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity<V extends BaseViewModel> extends DaggerAppCompatActivity {
    @Inject
    public ViewModelProviderFactory factory;
    @Inject
    public ScheduleProvider schedulerProvider;

    //ViewModel
    public V viewmodel;

    public View mRootView;

    public CommonUtils commonUtils;
    //Observable
    public static CompositeDisposable compositeDisposable;

    private ProgressDialog progressDialog;
    public long mLastClickTime = 0;

    public abstract @LayoutRes
    int getLayoutId();

    public abstract void onCreateActivity(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection();
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        setContentView(getLayoutId());
        onCreateActivity(savedInstanceState);
    }


    //Android Inject
    public void performDependencyInjection(){
        AndroidInjection.inject(this);
    }

    public void showLoading(){
        progressDialog = CommonUtils.showLoadingDialog(this);
    }
    public void hideLoading(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void pushTo(Fragment fragment,String TAG){
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .add(R.id.clRootView, fragment, TAG)
                .commit();
    }

}