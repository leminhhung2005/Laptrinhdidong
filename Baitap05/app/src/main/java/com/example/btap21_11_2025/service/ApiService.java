package com.example.btap21_11_2025.service;

import com.example.btap21_11_2025.model.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    // tương ứng http://app.iotstar.vn:8081/appfoods/categories.php
    @GET("categories.php")
    Call<List<Category>> getCategories();
}
