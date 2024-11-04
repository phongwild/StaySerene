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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.R;

@SuppressLint("MissingInflatedId")
public class Register extends AppCompatActivity {
    String fullName, email, password, confirmPassword;
    private TextInputLayout layout_fullName, layout_email, layout_password, layout_confirmPassword;
    private TextInputEditText ed_fullName, ed_email, ed_password, ed_confirmPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        TextView btnregisterlogin = findViewById(R.id.btn_register_login);
        LinearLayout btnsingup = findViewById(R.id.btn_SignUp);
        layout_fullName = findViewById(R.id.layout_fullname);
        layout_email = findViewById(R.id.layout_email);
        layout_password = findViewById(R.id.layout_password_register);
        layout_confirmPassword = findViewById(R.id.layout_confirmPassword_register);
        ed_fullName = findViewById(R.id.ed_fullname_register);
        ed_email = findViewById(R.id.ed_email_register);
        ed_password = findViewById(R.id.ed_password_register);
        ed_confirmPassword = findViewById(R.id.ed_confirmPassword_register);
        btnregisterlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Check input
        ed_fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    layout_fullName.setError("Please enter your full name");
                } else {
                    layout_fullName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean check = s.toString().matches(emailPattern);
                if (s.length() == 0) {
                    layout_email.setError("Please enter your email");
                } else if (!check) {
                    layout_email.setError("Email invalid");
                } else {
                    layout_email.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    layout_password.setError("Please enter your password");
                } else {
                    layout_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed_confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    layout_confirmPassword.setError("Please enter your confirm password");
                } else {
                    layout_confirmPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Account");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        try {
            btnsingup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullName = ed_fullName.getText().toString().trim();
                    email = ed_email.getText().toString().trim();
                    password = ed_password.getText().toString().trim();
                    confirmPassword = ed_confirmPassword.getText().toString().trim();
                    if (!email.isEmpty()) {
                        Query emailQuery = userRef.orderByChild("email").equalTo(email);
                        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    layout_email.setError("Email already exists");
                                } else {
                                    if(fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                                        if(fullName.isEmpty()) {
                                            layout_fullName.setError("Please enter your email");
                                        } else layout_fullName.setErrorEnabled(false);
                                        if(email.isEmpty()) {
                                            layout_email.setError("Please enter your full name");
                                        } else layout_email.setErrorEnabled(false);
                                        if(password.isEmpty()) {
                                            layout_password.setError("Please enter your password");
                                        } else layout_password.setErrorEnabled(false);
                                        if(confirmPassword.isEmpty()) {
                                            layout_confirmPassword.setError("Please enter your confirm password");
                                        } else layout_confirmPassword.setErrorEnabled(false);
                                    } else {
                                        if(!password.equals(confirmPassword)) {
                                            layout_confirmPassword.setError("Password does not match");
                                        } else if (password.length() < 6) {
                                            layout_password.setError("Password must be at least 6 characters");
                                        } else {
                                            Intent intent = new Intent(Register.this, Add_phoneNumber.class);
                                            intent.putExtra("fullName", fullName);
                                            intent.putExtra("email", email);
                                            intent.putExtra("password", password);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        layout_email.setError("Please enter your email");
                    }
                }
            });
        } catch (Exception e) {
            Log.e("Linked database false", e.toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }
    public void reload(){
        //
    }
}