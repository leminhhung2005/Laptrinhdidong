package com.example.btap08.activity;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btap08.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import  com.bumptech.glide.Glide;
public class VideoShortActivity extends AppCompatActivity {

    private TextView tvUploaderEmail, tvVideoTitle, tvVideoDescription,
            tvLikeCount, tvDislikeCount;
    private ImageView imgCurrentUserAvatar;

    private Button btnUploadVideo, btnOpenWebview;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Uri selectedVideoUri;

    private ActivityResultLauncher<String> requestVideoPermissionLauncher;
    private ActivityResultLauncher<String> pickVideoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_short);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Ánh xạ view
        imgCurrentUserAvatar = findViewById(R.id.imgCurrentUserAvatar);
        tvUploaderEmail      = findViewById(R.id.tvUploaderEmail);
        tvVideoTitle         = findViewById(R.id.tvVideoTitle);
        tvVideoDescription   = findViewById(R.id.tvVideoDescription);
        tvLikeCount          = findViewById(R.id.tvLikeCount);
        tvDislikeCount       = findViewById(R.id.tvDislikeCount);
        btnUploadVideo       = findViewById(R.id.btnUploadVideo);
        btnOpenWebview       = findViewById(R.id.btnOpenWebview);

        // Set thông tin cơ bản
        tvUploaderEmail.setText(user.getEmail());
        tvVideoTitle.setText("Video Shorts");
        tvVideoDescription.setText("Video Shorts");
        // Demo: set sẵn 200 cho giống hình, sau này có thể thay bằng dữ liệu Firestore
        tvLikeCount.setText("200");
        tvDislikeCount.setText("200");
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .into(imgCurrentUserAvatar);
        }

        // Nhấn ảnh user mở màn profile (nếu em muốn làm thêm)
        imgCurrentUserAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        // Chuẩn bị launcher xin quyền & chọn video
        requestVideoPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        openVideoPicker();
                    } else {
                        Toast.makeText(this, "Không có quyền đọc video", Toast.LENGTH_SHORT).show();
                    }
                });

        pickVideoLauncher =
                registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                    if (uri != null) {
                        selectedVideoUri = uri;
                        uploadVideoToFirebase();
                    }
                });

        btnUploadVideo.setOnClickListener(v -> checkPermissionAndPickVideo());
        btnOpenWebview.setOnClickListener(v ->
                startActivity(new Intent(this, WebViewActivity.class)));
    }

    private void checkPermissionAndPickVideo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO)
                    != PackageManager.PERMISSION_GRANTED) {
                requestVideoPermissionLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO);
            } else {
                openVideoPicker();
            }
        } else {
            openVideoPicker();
        }
    }

    private void openVideoPicker() {
        pickVideoLauncher.launch("video/*");
    }

    private void uploadVideoToFirebase() {
        if (selectedVideoUri == null) return;

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        String fileName = System.currentTimeMillis() + ".mp4";

        StorageReference storageRef = FirebaseStorage.getInstance()
                .getReference("videos")
                .child(uid)
                .child(fileName);

        Toast.makeText(this, "Đang upload video...", Toast.LENGTH_SHORT).show();

        storageRef.putFile(selectedVideoUri)
                .addOnSuccessListener(taskSnapshot ->
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            Toast.makeText(this, "Upload thành công", Toast.LENGTH_SHORT).show();
                            saveVideoInfoToFirestore(downloadUrl);
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Upload lỗi: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }

    private void saveVideoInfoToFirestore(String videoUrl) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        Map<String, Object> data = new HashMap<>();
        data.put("url", videoUrl);
        data.put("ownerId", user.getUid());
        data.put("ownerEmail", user.getEmail());
        data.put("createdAt", FieldValue.serverTimestamp());
        data.put("title", "Video Shorts");
        data.put("likes", 0);
        data.put("dislikes", 0);

        db.collection("videos")
                .add(data)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Đã lưu thông tin video", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lưu thông tin lỗi: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }
}
