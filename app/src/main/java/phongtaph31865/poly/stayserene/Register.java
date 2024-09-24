package phongtaph31865.poly.stayserene;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import phongtaph31865.poly.stayserene.Model.Account;

@SuppressLint("MissingInflatedId")
public class Register extends AppCompatActivity {
    String fullName, email, password, confirmPassword;
    private TextInputLayout layout_fullName, layout_email, layout_password, layout_confirmPassword;
    private TextInputEditText ed_fullName, ed_email, ed_password, ed_confirmPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
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
                startActivity(new Intent(Register.this, Loginscreen.class));
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
        try {
            btnsingup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullName = ed_fullName.getText().toString();
                    email = ed_email.getText().toString();
                    password = ed_password.getText().toString();
                    confirmPassword = ed_confirmPassword.getText().toString();
                    if (!email.isEmpty()) {
                        Query emailQuery = userRef.orderByChild("email").equalTo(email);
                        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String avt = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTq2k2sI1nZyFTtoaKSXxeVzmAwIPchF4tjwg&s";
                                String gioiTinh = "";
                                String ngaySinh = "";
                                String sdt = "";
                                String diaChi = "";
                                String quocTich = "";
                                int role = 1;
                                String Uid = UUID.randomUUID().toString();
                                if (snapshot.exists()) {
                                    layout_email.setError("Email already exists");
                                } else {
                                    if (!password.equals(confirmPassword)) {
                                        layout_confirmPassword.setError("Password does not match");
                                    } else if (password.length() < 6) {
                                        layout_password.setError("Password must be at least 6 characters");
                                    } else {
                                        Account newAccount = new Account(Uid, fullName, avt, email, gioiTinh, ngaySinh, password, quocTich, sdt, diaChi, role);
                                        userRef.child(Uid).setValue(newAccount).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                startActivity(new Intent(Register.this, Loginscreen.class));
                                                Toast.makeText(Register.this, "Create " + fullName + " successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Register.this, "Create " + fullName + " failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
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
}