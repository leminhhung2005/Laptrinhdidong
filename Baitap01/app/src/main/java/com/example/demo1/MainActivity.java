package com.example.demo1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 🔹 THÊM 2 BIẾN NÀY
    EditText inputNumbers;
    Button btnProcess;
    EditText inputText;
    Button btnReverse;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // BÀI TẬP 4
        inputNumbers = findViewById(R.id.inputNumbers);
        btnProcess = findViewById(R.id.btnProcess);

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputNumbers.getText().toString().trim();

                if (input.isEmpty()) {
                    Log.d("Error", "Bạn chưa nhập mảng số!");
                    return;
                }

                // --- Chuyển chuỗi thành ArrayList<Integer> ---
                String[] parts = input.split(",");
                ArrayList<Integer> numbers = new ArrayList<>();

                for (String p : parts) {
                    try {
                        numbers.add(Integer.parseInt(p.trim()));
                    } catch (NumberFormatException e) {
                        Log.d("Error", "Bỏ qua giá trị không hợp lệ: " + p);
                    }
                }

                // --- Tách số chẵn và lẻ ---
                ArrayList<Integer> evenNumbers = new ArrayList<>();
                ArrayList<Integer> oddNumbers = new ArrayList<>();

                for (int n : numbers) {
                    if (n % 2 == 0) {
                        evenNumbers.add(n);
                    } else {
                        oddNumbers.add(n);
                    }
                }

                // --- In ra Logcat ---
                Log.d("EvenNumbers", evenNumbers.toString());
                Log.d("OddNumbers", oddNumbers.toString());
            }
        });

    // BÀI TẬP 5
        inputText = findViewById(R.id.inputText);
        btnReverse = findViewById(R.id.btnReverse);
        tvResult = findViewById(R.id.tvResult);

        btnReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = inputText.getText().toString().trim();

                if (s.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập chuỗi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // --- Đảo ngược các từ ---
                String[] words = s.split("\\s+");
                StringBuilder reversed = new StringBuilder();

                for (int i = words.length - 1; i >= 0; i--) {
                    reversed.append(words[i]);
                    if (i > 0) reversed.append(" ");
                }

                // --- In hoa toàn bộ chuỗi ---
                String result = reversed.toString().toUpperCase();

                // --- Hiển thị lên TextView ---
                tvResult.setText(result);

                // --- Hiển thị Toast ---
                Toast.makeText(MainActivity.this, "Kết quả: " + result, Toast.LENGTH_LONG).show();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
