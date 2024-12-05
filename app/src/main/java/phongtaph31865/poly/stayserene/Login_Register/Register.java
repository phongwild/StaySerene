package phongtaph31865.poly.stayserene.Login_Register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private String fullName, email, password, confirmPassword;
    private TextInputLayout layout_fullName, layout_email, layout_password, layout_confirmPassword;
    private TextInputEditText ed_fullName, ed_email, ed_password, ed_confirmPassword;
    private ProgressBar progressBar;
    private LinearLayout btnSignUp;
    private final String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Khởi tạo các view
        initializeViews();

        // Đặt các sự kiện lắng nghe
        setListeners();
    }

    private void initializeViews() {
        layout_fullName = findViewById(R.id.layout_fullname);
        layout_email = findViewById(R.id.layout_email);
        layout_password = findViewById(R.id.layout_password_register);
        layout_confirmPassword = findViewById(R.id.layout_confirmPassword_register);
        ed_fullName = findViewById(R.id.ed_fullname_register);
        ed_email = findViewById(R.id.ed_email_register);
        ed_password = findViewById(R.id.ed_password_register);
        ed_confirmPassword = findViewById(R.id.ed_confirmPassword_register);
        progressBar = findViewById(R.id.progressBar_register);
        TextView btnRegisterLogin = findViewById(R.id.btn_register_login);
        btnSignUp = findViewById(R.id.btn_SignUp);

        // Nút quay lại màn hình đăng nhập
        btnRegisterLogin.setOnClickListener(v -> finish());

        // Nút đăng ký
        btnSignUp.setOnClickListener(v -> handleSignUp());
    }

    private void setListeners() {
        ed_fullName.addTextChangedListener(createTextWatcher(layout_fullName, "Please enter your full name"));
        ed_email.addTextChangedListener(createTextWatcher(layout_email, "Please enter your email"));
        ed_password.addTextChangedListener(createTextWatcher(layout_password, "Please enter your password"));
        ed_confirmPassword.addTextChangedListener(createTextWatcher(layout_confirmPassword, "Please enter your confirm password"));
    }

    private void handleSignUp() {
        fullName = ed_fullName.getText().toString().trim();
        email = ed_email.getText().toString().trim();
        password = ed_password.getText().toString().trim();
        confirmPassword = ed_confirmPassword.getText().toString().trim();

        if (validateInputs()) {
            progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar
            checkEmailExists();
        }
    }

    private boolean validateInputs() {
        clearAllErrors(); // Xóa lỗi trước khi validate

        if (fullName.isEmpty()) {
            showError(layout_fullName, "Please enter your full name");
            return false;
        }
        if (email.isEmpty()) {
            showError(layout_email, "Please enter your email");
            return false;
        }
        if (!email.matches(emailPattern)) {
            showError(layout_email, "Email invalid");
            return false;
        }
        if (password.isEmpty()) {
            showError(layout_password, "Please enter your password");
            return false;
        }
        if (password.length() < 6) {
            showError(layout_password, "Password must be at least 6 characters");
            return false;
        }
        if (confirmPassword.isEmpty()) {
            showError(layout_confirmPassword, "Please enter your confirm password");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            showError(layout_confirmPassword, "Password does not match");
            return false;
        }
        return true;
    }

    private void checkEmailExists() {
        Api_service.service.get_account().enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                progressBar.setVisibility(View.GONE); // Ẩn ProgressBar khi API hoàn tất
                if (response.isSuccessful() && response.body() != null) {
                    for (Account account : response.body()) {
                        if (account.getEmail().equals(email)) {
                            showError(layout_email, "Email already exists");
                            return;
                        }
                    }
                    proceedToPhoneNumber(); // Email không tồn tại, tiếp tục
                } else {
                    proceedToPhoneNumber(); // Nếu không có dữ liệu hoặc lỗi, vẫn tiếp tục
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE); // Ẩn ProgressBar khi thất bại
                Log.e("CheckEmail", "Error checking email: " + throwable.getMessage());
                Toast.makeText(Register.this, "Failed to check email. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void proceedToPhoneNumber() {
        Intent intent = new Intent(Register.this, Add_phoneNumber.class);
        intent.putExtra("fullName", fullName);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    private void showError(TextInputLayout layout, String message) {
        layout.setError(message);
    }

    private void clearAllErrors() {
        layout_fullName.setError(null);
        layout_email.setError(null);
        layout_password.setError(null);
        layout_confirmPassword.setError(null);
    }

    private TextWatcher createTextWatcher(TextInputLayout layout, String errorMessage) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    layout.setError(errorMessage);
                } else {
                    layout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }
}
