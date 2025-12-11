package com.example.btap08.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.btap08.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnSignup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmailSignup);
        edtPassword = findViewById(R.id.edtPasswordSignup);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(v -> doSignup());
    }

    private void doSignup() {
        String email = edtEmail.getText().toString().trim();
        String pass  = edtPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Nhập đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Đăng ký thành công, hãy đăng nhập", Toast.LENGTH_SHORT).show();
                        finish();   // quay lại màn login
                    } else {
                        Toast.makeText(this,
                                "Lỗi: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
