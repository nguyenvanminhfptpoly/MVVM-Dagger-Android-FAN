package com.minhnv.dagger2androidpro.ui.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.minhnv.dagger2androidpro.R;
import com.minhnv.dagger2androidpro.data.model.HomeStay;
import com.minhnv.dagger2androidpro.ui.base.BaseActivity;
import com.minhnv.dagger2androidpro.ui.main.adapter.MainAdapter;
import com.minhnv.dagger2androidpro.ui.main.demo_fragment.BlankFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<MainViewModel> implements MainNavigator {
    private static final String TAG = "MainActivity";
    private List<HomeStay> homeStays;
    private MainAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreateActivity(@Nullable Bundle savedInstanceState) {
        viewmodel = ViewModelProviders.of(this,factory).get(MainViewModel.class);
        viewmodel.setNavigator(this);
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //call back
        viewmodel.ServerLoad();
        homeStays = new ArrayList<>();
        adapter = new MainAdapter(homeStays, getApplicationContext(), position -> {
            pushTo(BlankFragment.newInstance(),"S");
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onError(Throwable throwable) {
        Log.d(TAG, "onError: "+throwable);
    }

    @Override
    public void onSuccess() {
        adapter.notifyDataSetChanged();
        Log.d(TAG, "onSuccess: ");
    }

    @Override
    public void ServerLoadList() {
        viewmodel.loadList();
        //It emits all the items of the source Observable, regardless of when the subscriber subscribes
        compositeDisposable.add(viewmodel.listPublishSubject.share()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(data -> {
                homeStays.addAll(data);
                adapter.notifyDataSetChanged();
            },throwable -> {
                Log.d(TAG, "ServerLoadList: "+throwable);
            }));
    }
}
