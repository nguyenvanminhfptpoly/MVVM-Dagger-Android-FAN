package com.minhnv.dagger2androidpro.ui.main.add;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.minhnv.dagger2androidpro.R;
import com.minhnv.dagger2androidpro.data.remote.DataClient;
import com.minhnv.dagger2androidpro.ui.base.BaseActivity;
import com.minhnv.dagger2androidpro.utils.ApiUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHsActivity extends BaseActivity<AddHsViewModel> implements AddHsNavigator, View.OnClickListener {

    /**
     * tên hs
     */
    private EditText edName;
    private ImageView imgHomestay;
    /**
     * địa chỉ
     */
    private EditText edAddress;
    /**
     * Đánh giá
     */
    private EditText edRating;
    /**
     * giá homestay
     */
    private EditText edPrice;
    /**
     * idHomestay
     */
    private EditText edIdHomestay;
    /**
     * Hastag
     */
    private EditText edHastag;
    /**
     * Giá cũ
     */
    private EditText edPriceAgo;
    /**
     * thêm
     */
    private Button btnAdd;
    private int Image = 123;
    String realPath = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_hs;
    }

    @Override
    public void onCreateActivity(@Nullable Bundle savedInstanceState) {
        viewmodel = ViewModelProviders.of(this, factory).get(AddHsViewModel.class);
        viewmodel.setNavigator(this);
        initView();
    }



    public void initView() {
        edName = (EditText) findViewById(R.id.edName);
        imgHomestay = (ImageView) findViewById(R.id.imgHomestay);
        edAddress = (EditText) findViewById(R.id.edAddress);
        edRating = (EditText) findViewById(R.id.edRating);
        edPrice = (EditText) findViewById(R.id.edPrice);
        edIdHomestay = (EditText) findViewById(R.id.edIdHomestay);
        edHastag = (EditText) findViewById(R.id.edHastag);
        edPriceAgo = (EditText) findViewById(R.id.edPriceAgo);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        imgHomestay.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, Image);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Image && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            realPath = getRealPathFromURI(uri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgHomestay.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    private void postLuxury() {
        String title = edName.getText().toString().trim();
        String address = edAddress.getText().toString().trim();
        String rating = edRating.getText().toString().trim();
        String price = edPrice.getText().toString().trim();
        String idhomestay = edIdHomestay.getText().toString().trim();
        String hastag = edHastag.getText().toString().trim();
        String priceago = edPriceAgo.getText().toString().trim();

        File fileLuxury = new File(realPath);
        if(title.isEmpty() || address.isEmpty() || rating.isEmpty() || price.isEmpty() || idhomestay.isEmpty() || hastag.isEmpty() || priceago.isEmpty() || fileLuxury.length() == 0) {
            hideLoading();
            Toast.makeText(this, "Nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
        }else {
            String fileLuxuryAbsolutePath = fileLuxury.getAbsolutePath();
            String[] split = fileLuxuryAbsolutePath.split("\\.");
            fileLuxuryAbsolutePath = split[0] + System.currentTimeMillis() + "." + split[0];
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileLuxury);

            MultipartBody.Part uploadedFile = MultipartBody.Part.createFormData("uploaded_file", fileLuxuryAbsolutePath, requestBody);

            DataClient dataClient = ApiUtils.getData();
            Call<String> call = dataClient.UploadPhot(uploadedFile);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                    String body = response.body();
                    assert body != null;
                    if (body.length() > 0) {
                        DataClient insertData = ApiUtils.getData();
                        Call<String> callb = insertData.insertData(title, ApiUtils.baseUrl + "image/" + body, address, rating, price, idhomestay, hastag, priceago);
                        callb.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                                String result = response.body();
                                assert result != null;
                                if (result.equals("Success")) {
                                    hideLoading();
                                    Toast.makeText(getApplicationContext(),"Thêm thành công",Toast.LENGTH_LONG).show();
                                }else if(result.equals("Failed")){
                                    hideLoading();
                                    Toast.makeText(getApplicationContext(),"Thêm không thành công",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                                hideLoading();
                                Log.d("Failed", "onFailure: "+t.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                    Log.d("lỗi", "onFailure: "+t.getMessage());
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btnAdd:
                showLoading();
                postLuxury();
                break;
        }
    }
}
