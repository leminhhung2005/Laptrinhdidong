package com.example.btap08.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.btap08.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView tvGotoSignup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // nếu đã login rồi thì nhảy thẳng vào màn VideoShort
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, VideoShortActivity.class));
            finish();
            return;
        }

        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvGotoSignup = findViewById(R.id.tvGotoSignup);

        btnLogin.setOnClickListener(v -> doLogin());
        tvGotoSignup.setOnClickListener(v ->
                startActivity(new Intent(this, SignupActivity.class)));
    }

    private void doLogin() {
        String email = edtEmail.getText().toString().trim();
        String pass  = edtPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Nhập đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, VideoShortActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this,
                                "Lỗi: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
