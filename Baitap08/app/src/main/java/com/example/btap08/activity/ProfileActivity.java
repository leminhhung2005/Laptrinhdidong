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

import com.bumptech.glide.Glide;
import com.example.btap08.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgProfileAvatar;
    private TextView tvProfileEmail, tvTotalVideos;
    private Button btnChangeAvatar, btnLogout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ActivityResultLauncher<String> permissionImageLauncher;
    private ActivityResultLauncher<String> pickImageLauncher;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        imgProfileAvatar = findViewById(R.id.imgProfileAvatar);
        tvProfileEmail   = findViewById(R.id.tvProfileEmail);
        tvTotalVideos    = findViewById(R.id.tvTotalVideos);
        btnChangeAvatar  = findViewById(R.id.btnChangeAvatar);
        btnLogout        = findViewById(R.id.btnLogout);

        tvProfileEmail.setText(user.getEmail());

        // Load avatar nếu có:
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .into(imgProfileAvatar);
        }

        // Đếm số video của user trong collection "videos"
        db.collection("videos")
                .whereEqualTo("ownerId", user.getUid())
                .get()
                .addOnSuccessListener(query -> {
                    int count = query.size();
                    tvTotalVideos.setText("Tổng số video: " + count);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi lấy số video: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());

        // Chuẩn bị launcher xin quyền + chọn ảnh
        permissionImageLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        openImagePicker();
                    } else {
                        Toast.makeText(this, "Không có quyền đọc ảnh", Toast.LENGTH_SHORT).show();
                    }
                });

        pickImageLauncher =
                registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        uploadAvatarToFirebase();
                    }
                });

        btnChangeAvatar.setOnClickListener(v -> checkPermissionAndPickImage());

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void checkPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionImageLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                openImagePicker();
            }
        } else {
            openImagePicker();
        }
    }

    private void openImagePicker() {
        pickImageLauncher.launch("image/*");
    }

    private void uploadAvatarToFirebase() {
        if (selectedImageUri == null) return;

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        // Lưu avatar vào Firebase Storage: avatars/uid.jpg
        String uid = user.getUid();
        com.google.firebase.storage.StorageReference ref =
                com.google.firebase.storage.FirebaseStorage.getInstance()
                        .getReference("avatars")
                        .child(uid + ".jpg");

        Toast.makeText(this, "Đang upload avatar...", Toast.LENGTH_SHORT).show();

        ref.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot ->
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Cập nhật photoUrl cho FirebaseAuth user
                            UserProfileChangeRequest profileUpdate =
                                    new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(uri)
                                            .build();

                            user.updateProfile(profileUpdate)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(this, "Cập nhật avatar thành công",
                                                    Toast.LENGTH_SHORT).show();

                                            // Load avatar mới lên ImageView
                                            Glide.with(this)
                                                    .load(uri)
                                                    .placeholder(android.R.drawable.sym_def_app_icon)
                                                    .into(imgProfileAvatar);
                                        } else {
                                            Toast.makeText(this,
                                                    "Lỗi update profile: "
                                                            + task.getException().getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Upload avatar lỗi: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }
}
