package phongtaph31865.poly.stayserene.Login_Register;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;

import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.R;

public class Add_phoneNumber extends AppCompatActivity {
    private TextInputLayout layout_phoneNumber, layout_address, layout_date;
    private TextInputEditText edt_phoneNumber, edt_address, edt_date;
    private Button btn_choose_date;
    private LinearLayout btn_create;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String phoneNumber, address, date;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_phone_number);
        layout_phoneNumber = findViewById(R.id.layout_phoneNumber);
        layout_address = findViewById(R.id.layout_address);
        layout_date = findViewById(R.id.layout_date_time);
        edt_phoneNumber = findViewById(R.id.ed_phoneNumber);
        edt_address = findViewById(R.id.ed_address);
        edt_date = findViewById(R.id.ed_date_time);
        btn_choose_date = findViewById(R.id.btn_choose_date);
        btn_create = findViewById(R.id.btn_next_addPhoneNumber);
        edt_phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    layout_phoneNumber.setError("Please enter your phone number");
                } else {
                    layout_phoneNumber.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    layout_address.setError("Please enter your address");
                } else {
                    layout_address.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    layout_date.setError("Please enter your birthday");
                } else {
                    layout_date.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_Date_Time();
            }
        });
        Intent intent = getIntent();
        String avt = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTq2k2sI1nZyFTtoaKSXxeVzmAwIPchF4tjwg&s";
        String gioiTinh = "";
        String quocTich = "";
        int role = 1;
        String Uid = UUID.randomUUID().toString();
        String fullname = intent.getStringExtra("fullname");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        //Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Account");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        try {
            btn_create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneNumber = edt_phoneNumber.getText().toString();
                    address = edt_address.getText().toString();
                    date = edt_date.getText().toString();
                    if (phoneNumber.isEmpty() || address.isEmpty() || date.isEmpty()) {
                        if (phoneNumber.isEmpty()) {
                            layout_phoneNumber.setError("Please enter your phone number");
                        } else {
                            layout_phoneNumber.setErrorEnabled(false);
                        }
                        if (address.isEmpty()) {
                            layout_address.setError("Please enter your address");
                        } else {
                            layout_address.setErrorEnabled(false);
                        }
                        if (date.isEmpty()) {
                            layout_date.setError("Please enter your date");
                        } else {
                            layout_date.setErrorEnabled(false);
                        }
                    }else {
                        mAuth.createUserWithEmailAndPassword(email, password);
                        Account newAccount = new Account(Uid, fullname, avt, email, gioiTinh, date, password, quocTich, phoneNumber, address, role);
                        userRef.child(Uid).setValue(newAccount).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                startActivity(new Intent(Add_phoneNumber.this, Activity_success.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Add_phoneNumber.this, "Create " + fullname + " failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void open_Date_Time(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                edt_date.setText(String.format("%02d/%02d/%04d", day, month + 1, year));
            }
        }, year, month, day);
        dialog.show();
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