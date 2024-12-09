package phongtaph31865.poly.stayserene.Login_Register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.MainActivity_user;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private String fullName, email, password, confirmPassword;
    private TextInputLayout layout_fullName, layout_email, layout_password, layout_confirmPassword;
    private TextInputEditText ed_fullName, ed_email, ed_password, ed_confirmPassword;
    private ProgressBar progressBar;
    private LinearLayout btnSignUp, btn_signUp_Google;
    private final String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

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
        btn_signUp_Google = findViewById(R.id.btn_signUp_Google);
        // Đăng nhập bằng Google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        // Nút quay lại màn hình đăng nhập
        btnRegisterLogin.setOnClickListener(v -> finish());

        // Nút đăng ký
        btnSignUp.setOnClickListener(v -> handleSignUp());

        btn_signUp_Google.setOnClickListener(v -> {
            Intent signInIntent = gsc.getSignInIntent();
            startActivityForResult(signInIntent, 1000);
        });
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
    // Google sign-in
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                task.getResult(ApiException.class);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String Uid = account.getId();
                progressBar.setVisibility(View.VISIBLE);
                Api_service.service.check_user_google(Uid).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            boolean userExist = response.body();
                            if (!userExist) {
                                Create_acc_gg(Uid, account.getDisplayName(), account.getEmail(), account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTq2k2sI1nZyFTtoaKSXxeVzmAwIPchF4tjwg&s");
                                Log.d("Login"," Uid: " + Uid + " Email: " + account.getEmail());
                            } else {
                                login(account.getEmail(), "");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable throwable) {
                        Log.e("Login", "onFailure: " + throwable.getMessage());
                    }
                });
            } catch (Exception e) {
                Toast.makeText(Register.this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
                Log.e("Login", "onActivityResult: " + e.getMessage());
            }
        }
    }
    private void login(String email, String password){
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(password);
        Api_service.service.login(account).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                progressBar.setVisibility(View.GONE); // Ẩn ProgressBar khi có kết quả
                if (response.isSuccessful()) {
                    List<Account> accountList = response.body();
                    if (accountList != null && accountList.size() > 0 && accountList.get(0).getRole() == 1) {
                        // Lưu thông tin tài khoản vào SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Activity.MODE_PRIVATE);
                        sharedPreferences.edit().putString("uid", accountList.get(0).get_id()).apply();
                        // Lưu trạng thái đăng nhập
                        saveLoginStatus(true, accountList.get(0).getEmail(), accountList.get(0).getPassword(), accountList.get(0).getRole());
                        // Chuyển đến màn hình chính
                        Intent intent = new Intent(Register.this, MainActivity_user.class);
                        intent.putExtra("Username", accountList.get(0).get_id());
                        progressBar.setVisibility(View.GONE); // Ẩn ProgressBar khi có kết quả
                        Toast.makeText(Register.this, "Login success", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE); // Ẩn ProgressBar khi thất bại
                Log.e("Login", "onFailure: " + throwable.getMessage());
            }
        });
    }
    // Tạo tài khoản Google
    private void Create_acc_gg(String Uid, String name, String email, String photo) {
        Account account = new Account();
        account.setUid(Uid);
        account.setUsername(name);
        account.setSdt("");
        account.setEmail(email);
        account.setPassword("");
        account.setDiaChi("");
        account.setNgaySinh("");
        account.setGioiTinh("");
        account.setQuocTich("");
        account.setRole(1);
        account.setAvt(photo);
        account.setImgcccdtruoc("");  // Mặt trước CCCD
        account.setImgcccdsau("");    // Mặt sau CCCD
        account.setCccd("");
        Api_service.service.create_account(account).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Account acc : response.body()) {
                        login(acc.getEmail(), "");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e("error create acc gg", throwable.getMessage());
            }
        });
    }
    private void saveLoginStatus(boolean isLoggedIN, String email, String password, int role) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIN);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("role", String.valueOf(role));
        editor.apply();
    }
    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false); // Kiểm tra trạng thái đăng nhập

        // Kiểm tra nếu người dùng đã đăng nhập trước đó
        if (isLoggedIn) {
            // Nếu đã đăng nhập, chuyển đến màn hình chính
            String roleString = sharedPreferences.getString("role", "");
            int userRole = -1; // Giá trị mặc định là -1

            if (!roleString.isEmpty()) {
                try {
                    userRole = Integer.parseInt(roleString); // Chuyển đổi role thành số nguyên
                } catch (NumberFormatException e) {
                    e.printStackTrace(); // Xử lý khi chuỗi không thể chuyển thành số nguyên
                }
            }

            // Kiểm tra vai trò người dùng và chuyển đến màn hình phù hợp
            switch (userRole) {
                case 0:
                    // Nếu là admin, chuyển đến màn hình quản trị (chưa xây dựng)
                    startActivity(new Intent(Register.this, MainActivity_user.class));
                    Toast.makeText(Register.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    // Nếu là user, chuyển đến màn hình chính người dùng
                    startActivity(new Intent(Register.this, MainActivity_user.class));
                    Toast.makeText(Register.this, "Welcome back", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            finish(); // Dừng lại ở màn hình đăng nhập nếu đã đăng nhập
        }
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
    @Override
    protected void onStart() {
        super.onStart();
        checkLoginStatus();
    }
}
