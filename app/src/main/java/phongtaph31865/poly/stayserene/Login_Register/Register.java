package phongtaph31865.poly.stayserene.Login_Register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

@SuppressLint("MissingInflatedId")
public class Register extends AppCompatActivity {
    private String fullName, email, password, confirmPassword;
    private TextInputLayout layout_fullName, layout_email, layout_password, layout_confirmPassword;
    private TextInputEditText ed_fullName, ed_email, ed_password, ed_confirmPassword;
    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Initialize views
        initializeViews();

        // Set listeners
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

        TextView btnregisterlogin = findViewById(R.id.btn_register_login);
        LinearLayout btnsingup = findViewById(R.id.btn_SignUp);

        // Button to go back to login screen
        btnregisterlogin.setOnClickListener(v -> finish());

        // Signup button
        btnsingup.setOnClickListener(v -> handleSignUp());
    }

    private void setListeners() {
        ed_fullName.addTextChangedListener(createTextWatcher(layout_fullName, "Please enter your full name"));
        ed_email.addTextChangedListener(createTextWatcher(layout_email, "Please enter your email"));
        ed_password.addTextChangedListener(createTextWatcher(layout_password, "Please enter your password"));
        ed_confirmPassword.addTextChangedListener(createTextWatcher(layout_confirmPassword, "Please enter your confirm password"));
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

    private void handleSignUp() {
        fullName = ed_fullName.getText().toString().trim();
        email = ed_email.getText().toString().trim();
        password = ed_password.getText().toString().trim();
        confirmPassword = ed_confirmPassword.getText().toString().trim();

        if (validateInputs()) {
            checkEmailExists();
        }
    }

    private boolean validateInputs() {
        if (fullName.isEmpty()) {
            layout_fullName.setError("Please enter your full name");
            return false;
        }
        if (email.isEmpty()) {
            layout_email.setError("Please enter your email");
            return false;
        }
        if (!email.matches(emailPattern)) {
            layout_email.setError("Email invalid");
            return false;
        }
        if (password.isEmpty()) {
            layout_password.setError("Please enter your password");
            return false;
        }
        if (password.length() < 6) {
            layout_password.setError("Password must be at least 6 characters");
            return false;
        }
        if (confirmPassword.isEmpty()) {
            layout_confirmPassword.setError("Please enter your confirm password");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            layout_confirmPassword.setError("Password does not match");
            return false;
        }
        return true;
    }

    private void checkEmailExists() {
        Api_service.service.get_account().enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    List<Account> accounts = response.body();
                    for (Account account : accounts) {
                        if (account.getEmail().equals(email)) {
                            layout_email.setError("Email already exists");
                            return;
                        }
                    }
                    proceedToPhoneNumber();
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e("Linked database false", throwable.toString());
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
}
