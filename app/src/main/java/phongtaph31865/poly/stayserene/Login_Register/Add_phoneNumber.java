package phongtaph31865.poly.stayserene.Login_Register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.R;

public class Add_phoneNumber extends AppCompatActivity {
    private TextInputLayout layout_phoneNumber, layout_address, layout_date;
    private TextInputEditText edt_phoneNumber, edt_address, edt_date;
    private Button btn_choose_date;
    private LinearLayout btn_create;
    private RelativeLayout btn_add_avt;
    private ProgressBar progressBar;
    private CircleImageView img_avt;
    private Uri ImgUri;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String phoneNumber, address, date;
    //Firebase
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Account");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
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
        btn_add_avt = findViewById(R.id.btn_add_avt);
        img_avt = findViewById(R.id.iv_add_avt);
        progressBar = findViewById(R.id.progressBar_add_avt);
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
        String gioiTinh = "";
        String quocTich = "";
        int role = 1;
        String Uid = UUID.randomUUID().toString();
        String fullname = intent.getStringExtra("fullname");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == Activity.RESULT_OK){
                            Intent data = o.getData();
                            ImgUri = data.getData();
                            Picasso.get().load(ImgUri).resize(140,140).centerCrop().into(img_avt);
                        }else {
                            Toast.makeText(Add_phoneNumber.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        btn_add_avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setType("image/*");
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(photoPicker);
            }
        });
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
                        StorageReference imageRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(ImgUri));
                        imageRef.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Account newAccount = new Account(Uid, fullname, uri.toString(), email, gioiTinh, date, password, quocTich, phoneNumber, address, role);
                                        userRef.child(Uid).setValue(newAccount).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                startActivity(new Intent(Add_phoneNumber.this, Activity_success.class));
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Add_phoneNumber.this, "Create " + fullname + " failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });
                            }
                        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onPaused(@NonNull UploadTask.TaskSnapshot snapshot) {
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(Add_phoneNumber.this, "Upload avatar failed", Toast.LENGTH_SHORT).show();
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
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(contentResolver.getType(fileUri));
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