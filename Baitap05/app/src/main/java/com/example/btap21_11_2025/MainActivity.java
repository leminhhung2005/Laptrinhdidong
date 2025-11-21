package com.example.btap21_11_2025;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.btap21_11_2025.adapter.CategoryAdapter;
import com.example.btap21_11_2025.model.Category;
import com.example.btap21_11_2025.service.ApiService;
import com.example.btap21_11_2025.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;
    // them thanh load nha
    private ProgressBar progressBar;

    private static final String TAG = "MainActivityRetrofit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set layout chinh
        setContentView(R.layout.activity_main);
        // anh xa id layout
        initView();
        progressBar = new ProgressBar(this);
        // Nếu muốn hiển thị progressBar, nên thêm vào layout, ở đây mình lược bớt cho gọn.

        rcvCategory.setLayoutManager(new LinearLayoutManager(this));
        // khoi tạo adapter
        categoryAdapter = new CategoryAdapter(this, new ArrayList<Category>());
        rcvCategory.setAdapter(categoryAdapter);

        callApiGetCategories();
    }

    private void initView() {
        rcvCategory = findViewById(R.id.rcvCategory);
    }
    private void callApiGetCategories() {
        progressBar.setVisibility(View.VISIBLE);
        // khởi tạo retrofit
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Category>> call = apiService.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> list = response.body();
                    categoryAdapter.setData(list);
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi dữ liệu!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Gọi API thất bại!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}
