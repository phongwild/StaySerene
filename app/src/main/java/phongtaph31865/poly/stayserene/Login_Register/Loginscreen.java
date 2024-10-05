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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_admin.Activity_admin.MainActivity_admin;
import phongtaph31865.poly.stayserene.Screen_user.Activity.MainActivity_user;

@SuppressLint("MissingInflatedId")
public class Loginscreen extends AppCompatActivity {
    private LinearLayout btn_Login;
    private TextView btn_SignUp, btn_forgotPass;
    private TextInputLayout layout_email, layout_pass;
    private TextInputEditText edt_email, edt_pass;
    float v = 0;
    private FirebaseAuth mAuth;
    private LinearLayout btn_login_google, btn_login_fb;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginscreen);
        btn_Login = findViewById(R.id.btn_Login);
        btn_SignUp = findViewById(R.id.btn_register_login);
        btn_forgotPass = findViewById(R.id.btn_forgot_pass);
        layout_email = findViewById(R.id.layout_email_login);
        layout_pass = findViewById(R.id.layout_password_login);
        edt_email = findViewById(R.id.ed_email_login);
        edt_pass = findViewById(R.id.ed_password_login);
        btn_login_google = findViewById(R.id.btn_login_with_google);
        btn_login_fb = findViewById(R.id.btn_login_with_facebook);
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    layout_email.setError("Please enter your email");
                }else {
                    layout_email.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    layout_pass.setError("Please enter your password");
                }else {
                    layout_pass.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loginscreen.this, Register.class));
            }
        });
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Account");
        mAuth = FirebaseAuth.getInstance();
        //Đăng nhập bằng google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        btn_login_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = gsc.getSignInIntent();
                startActivityForResult(signInIntent, 1000);
            }
        });
        //Đăng nhập bằng facebook
        btn_login_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edt_email.getText().toString();
                password = edt_pass.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    if (email.isEmpty()) {
                        layout_email.setError("Please enter your email");
                    } else {
                        layout_email.setErrorEnabled(false);
                    }
                    if (password.isEmpty()) {
                        layout_pass.setError("Please enter your password");
                    } else {
                        layout_pass.setErrorEnabled(false);
                    }
                }else {
                    databaseRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Account account = dataSnapshot.getValue(Account.class);
                                    SharedPreferences sharedPreferences = getSharedPreferences("user_data", Activity.MODE_PRIVATE);
                                    sharedPreferences.edit().putString("uid", account.getUid()).apply();
                                    if (account != null && account.getPassword().equals(password)) {
                                        saveLoginStatus(true, email, password, account.getRole());
                                        if (account.getRole() == 0) {
                                            Intent intent = new Intent(Loginscreen.this, MainActivity_admin.class);
                                            Toast.makeText(Loginscreen.this, "Login success", Toast.LENGTH_SHORT).show();
                                            intent.putExtra("Username", account.getUid());
                                            startActivity(intent);
                                        } else if (account.getRole() == 1) {
                                            Intent intent = new Intent(Loginscreen.this, MainActivity_user.class);
                                            Toast.makeText(Loginscreen.this, "Login success", Toast.LENGTH_SHORT).show();
                                            intent.putExtra("Username", account.getUid());
                                            startActivity(intent);
                                        }
                                    }else {
                                        layout_pass.setError("Password is incorrect!!");
                                    }
                                }
                            }else {
                                layout_email.setError("Email is not exist!!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
    //Google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String email = account.getEmail();
                String name = account.getDisplayName();
                String avt = String.valueOf(account.getPhotoUrl());
                finish();
                startActivity(new Intent(Loginscreen.this, MainActivity_user.class));
                Toast.makeText(Loginscreen.this, "Login success", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(Loginscreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveLoginStatus(boolean isLoggedIN, String email, String password, int role){
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
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        String roleString = sharedPreferences.getString("role", "");

        int userRole = -1; // Đặt giá trị mặc định là -1

        if (!roleString.isEmpty()) {
            try {
                userRole = Integer.parseInt(roleString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Xử lý khi chuỗi không thể chuyển đổi thành số nguyên
            }
        }

        if (isLoggedIn) {
            switch (userRole) {
                case 0:
                    startActivity(new Intent(Loginscreen.this, MainActivity_admin.class));
                    Toast.makeText(Loginscreen.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    startActivity(new Intent(Loginscreen.this, MainActivity_user.class));
                    Toast.makeText(Loginscreen.this, "Welcome back", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        checkLoginStatus();
    }
}