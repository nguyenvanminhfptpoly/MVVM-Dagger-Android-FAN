package com.minhnv.dagger2androidpro.ui.main.edit;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.androidnetworking.widget.ANImageView;
import com.minhnv.dagger2androidpro.R;
import com.minhnv.dagger2androidpro.data.model.HomeStay;
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

public class EditActivity extends BaseActivity<EditViewModel> implements EditNavigator, View.OnClickListener {

    private ANImageView imgHomes;
    /**
     * TextView
     */
    private TextView tvIdhs;
    /**
     * tên hs
     */
    private EditText edTitles;
    /**
     * địa chỉ
     */
    private EditText edAddresss;
    /**
     * đánh giá
     */
    private EditText edRatings;
    /**
     * Name
     */
    private EditText edPrices;
    /**
     * Name
     */
    private EditText edIdHs;
    /**
     * Name
     */
    private EditText edHastags;
    /**
     * Name
     */
    private EditText edPriceAgos;
    /**
     * Sửa
     */
    private Button btnEdits;
    private int Image = 123;
    String realPath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
    }

    private void initView() {
        imgHomes = (ANImageView) findViewById(R.id.imgHomes);
        tvIdhs = (TextView) findViewById(R.id.tvIdhs);
        edTitles = (EditText) findViewById(R.id.edTitles);
        edAddresss = (EditText) findViewById(R.id.edAddresss);
        edRatings = (EditText) findViewById(R.id.edRatings);
        edPrices = (EditText) findViewById(R.id.edPrices);
        edIdHs = (EditText) findViewById(R.id.edIdHs);
        edHastags = (EditText) findViewById(R.id.edHastags);
        edPriceAgos = (EditText) findViewById(R.id.edPriceAgos);
        btnEdits = (Button) findViewById(R.id.btnEdits);
        btnEdits.setOnClickListener(this);
        initIntent();
    }

    private void initIntent(){
        HomeStay homeStay = (HomeStay) getIntent().getSerializableExtra("detail");
        assert homeStay != null;
        imgHomes.setImageUrl(homeStay.getImage());
        edAddresss.setText(homeStay.getAddress());
        edHastags.setText(homeStay.getHastag());
        edIdHs.setText(homeStay.getIdhomestays());
        edTitles.setText(homeStay.getTitle());
        edPrices.setText(homeStay.getPrice() + "");
        edPriceAgos.setText(homeStay.getPriceago() + "");
        edRatings.setText(homeStay.getRating());
        btnEdits.setOnClickListener(v -> {
            showLoading();
            editHomestay();});
        tvIdhs.setText(homeStay.getId());

        imgHomes.setOnClickListener(v -> {
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
                imgHomes.setImageBitmap(bitmap);
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

    private void editHomestay() {
        String title = edTitles.getText().toString().trim();
        String idHs = tvIdhs.getText().toString().trim();
        String address = edAddresss.getText().toString().trim();
        String rating = edRatings.getText().toString().trim();
        String price = edPrices.getText().toString().trim();
        String idhomestay = edIdHs.getText().toString().trim();
        String hastag = edHastags.getText().toString().trim();
        String priceago = edPriceAgos.getText().toString().trim();

        File fileLuxury = new File(realPath);
        if(title.isEmpty() || address.isEmpty() || rating.isEmpty() || price.isEmpty() || idhomestay.isEmpty() || hastag.isEmpty() || priceago.isEmpty() ||fileLuxury.length() == 0) {
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
                        Call<String> callb = insertData.editData(idHs,title, ApiUtils.baseUrl + "image/" + body, address, rating, price, idhomestay, hastag, priceago);
                        callb.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                                String result = response.body();
                                assert result != null;
                                if (result.equals("Success")) {
                                    hideLoading();
                                    Toast.makeText(getApplicationContext(),"Sửa thành công",Toast.LENGTH_LONG).show();
                                }else if(result.equals("Failed")){
                                    hideLoading();
                                    Toast.makeText(getApplicationContext(),"Sửa không thành công",Toast.LENGTH_LONG).show();
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
            case R.id.btnEdits:
                break;
        }
    }
}
