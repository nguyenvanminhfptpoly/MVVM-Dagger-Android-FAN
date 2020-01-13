package com.minhnv.dagger2androidpro.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.minhnv.dagger2androidpro.R;
import com.minhnv.dagger2androidpro.data.model.HomeStay;
import com.minhnv.dagger2androidpro.ui.base.BaseActivity;
import com.minhnv.dagger2androidpro.ui.main.adapter.MainAdapter;
import com.minhnv.dagger2androidpro.ui.main.add.AddHsActivity;
import com.minhnv.dagger2androidpro.ui.main.edit.EditActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.androidnetworking.internal.ANImageLoader.initialize;

public class MainActivity extends BaseActivity<MainViewModel> implements MainNavigator, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private List<HomeStay> homeStays;
    private MainAdapter adapter;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipe;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewmodel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        viewmodel.setNavigator(this);
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //call back
        viewmodel.ServerLoad();
        homeStays = new ArrayList<>();
        adapter = new MainAdapter(homeStays, getApplicationContext(), position -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_edit_hs, null);
            Button btnEdit = view.findViewById(R.id.btnEdits);
            Button btnDelete = view.findViewById(R.id.btnDelete);
            ImageButton imgCancel = view.findViewById(R.id.imageButton);
            builder.setView(view);
            Dialog dialog = builder.create();
            dialog.show();
            HomeStay homeStay = homeStays.get(position);
            int id = Integer.parseInt(homeStay.getId());
            btnDelete.setOnClickListener(v -> {
                viewmodel.delete(id);
                homeStays.clear();
                viewmodel.loadList();
                showLoading();
                dialog.dismiss();
            });
            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("detail", homeStays.get(position));
                startActivity(intent);
                dialog.dismiss();
            });
            imgCancel.setOnClickListener(v -> {
                dialog.dismiss();
            });
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        checkPermissions();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnClickListener(this);
        swipe.setOnRefreshListener(() -> {
            homeStays.clear();
            viewmodel.loadList();
            new Handler().postDelayed(() -> {
                // Stop animation (This will be after 3 seconds)
                swipe.setRefreshing(false);
            }, 1500);
        });
    }

    @Override
    public void onError(Throwable throwable) {
        Log.d(TAG, "onError: " + throwable);
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
                }, throwable -> {
                    Log.d(TAG, "ServerLoadList: " + throwable);
                }));
    }

    @Override
    public void onDeleteSuccess() {
        Toast.makeText(this, "xóa thành công", Toast.LENGTH_SHORT).show();
        hideLoading();
    }

    @Override
    public void onDeleteFailed() {
        Toast.makeText(this, "xóa không thành công", Toast.LENGTH_SHORT).show();
        hideLoading();
    }

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            for (int index = permissions.length - 1; index >= 0; --index) {
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    // exit the app if one permission is not granted
                    Toast.makeText(this, "Required permission '" + permissions[index]
                            + "' not granted, exiting", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
            // all permissions were granted
            initialize();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.floatingActionButton:
                startActivity(new Intent(this, AddHsActivity.class));
                break;
            case R.id.swipe:
                break;
        }
    }


}
