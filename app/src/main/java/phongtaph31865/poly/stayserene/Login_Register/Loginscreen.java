package phongtaph31865.poly.stayserene.Login_Register;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.saadahmedev.popupdialog.PopupDialog;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Login_Register.ForgotPass.Activity_type_phone_number;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.MainActivity_user;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("MissingInflatedId")
public class Loginscreen extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION = 100;
    private LinearLayout btn_Login;
    private TextView btn_SignUp, btn_forgotPass;
    private TextInputLayout layout_email, layout_pass;
    private TextInputEditText edt_email, edt_pass;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private LinearLayout btn_login_google, btn_login_fb;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginscreen);

        // Kiểm tra quyền location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
        }

        // Khởi tạo views
        initializeViews();

        // Đặt các sự kiện lắng nghe
        setListeners();
    }

    private void initializeViews() {
        btn_Login = findViewById(R.id.btn_Login);
        btn_SignUp = findViewById(R.id.btn_register_login);
        btn_forgotPass = findViewById(R.id.btn_forgot_pass);
        layout_email = findViewById(R.id.layout_email_login);
        layout_pass = findViewById(R.id.layout_password_login);
        edt_email = findViewById(R.id.ed_email_login);
        edt_pass = findViewById(R.id.ed_password_login);
        btn_login_google = findViewById(R.id.btn_login_with_google);
        btn_login_fb = findViewById(R.id.btn_login_with_facebook);
        progressBar = findViewById(R.id.progressBar_login); // Thêm ProgressBar vào layout

        mAuth = FirebaseAuth.getInstance();

        // Đăng nhập bằng Google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
    }

    private void setListeners() {
        edt_email.addTextChangedListener(createTextWatcher(layout_email, "Please enter your email"));
        edt_pass.addTextChangedListener(createTextWatcher(layout_pass, "Please enter your password"));

        btn_SignUp.setOnClickListener(v -> startActivity(new Intent(Loginscreen.this, Register.class)));

        // Đăng nhập bằng Google
        btn_login_google.setOnClickListener(v -> {
            Intent signInIntent = gsc.getSignInIntent();
            startActivityForResult(signInIntent, 1000);
        });

        // Đăng nhập bằng Facebook (chưa thực hiện)
        btn_login_fb.setOnClickListener(v -> {});

        // Quên mật khẩu
        btn_forgotPass.setOnClickListener(v -> startActivity(new Intent(Loginscreen.this, Activity_type_phone_number.class)));

        // Đăng nhập
        btn_Login.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        email = edt_email.getText().toString().trim();
        password = edt_pass.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) layout_email.setError("Please enter your email");
            else layout_email.setErrorEnabled(false);
            if (password.isEmpty()) layout_pass.setError("Please enter your password");
            else layout_pass.setErrorEnabled(false);
        } else {
            showProgressBar(); // Hiển thị ProgressBar khi bắt đầu đăng nhập
            login(email, password);
        }
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE); // Ẩn ProgressBar
    }

    private void showLoginErrorDialog() {
        PopupDialog.getInstance(Loginscreen.this)
                .statusDialogBuilder()
                .createErrorDialog()
                .setHeading("Uh-Oh")
                .setDescription("Wrong email or password! Please try again")
                .setActionButtonText("Try again")
                .build(Dialog::dismiss)
                .show();
    }

    private TextWatcher createTextWatcher(TextInputLayout layout, String errorMessage) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) layout.setError(errorMessage);
                else layout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }

    // Lưu trạng thái đăng nhập
    private void saveLoginStatus(boolean isLoggedIN, String email, String password, int role) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIN);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("role", String.valueOf(role));
        editor.apply();
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
                showProgressBar();
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
                Toast.makeText(Loginscreen.this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
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
                hideProgressBar(); // Ẩn ProgressBar khi có kết quả
                if (response.isSuccessful()) {
                    List<Account> accountList = response.body();
                    if (accountList != null && accountList.size() > 0 && accountList.get(0).getRole() == 1) {
                        // Lưu thông tin tài khoản vào SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Activity.MODE_PRIVATE);
                        sharedPreferences.edit().putString("uid", accountList.get(0).get_id()).apply();
                        // Lưu trạng thái đăng nhập
                        saveLoginStatus(true, accountList.get(0).getEmail(), accountList.get(0).getPassword(), accountList.get(0).getRole());
                        // Chuyển đến màn hình chính
                        Intent intent = new Intent(Loginscreen.this, MainActivity_user.class);
                        intent.putExtra("Username", accountList.get(0).get_id());
                        hideProgressBar();
                        Toast.makeText(Loginscreen.this, "Login success", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                } else {
                    showLoginErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                hideProgressBar(); // Ẩn ProgressBar khi thất bại
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
                    startActivity(new Intent(Loginscreen.this, MainActivity_user.class));
                    Toast.makeText(Loginscreen.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    // Nếu là user, chuyển đến màn hình chính người dùng
                    startActivity(new Intent(Loginscreen.this, MainActivity_user.class));
                    Toast.makeText(Loginscreen.this, "Welcome back", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            finish(); // Dừng lại ở màn hình đăng nhập nếu đã đăng nhập
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        checkLoginStatus();
    }
}
