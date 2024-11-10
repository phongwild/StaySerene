package phongtaph31865.poly.stayserene.Login_Register;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saadahmedev.popupdialog.PopupDialog;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_phoneNumber extends AppCompatActivity {
    private TextInputLayout layout_phoneNumber, layout_address, layout_date;
    private TextInputEditText edt_phoneNumber, edt_address, edt_date;
    private Button btn_choose_date;
    private LinearLayout btn_create;
    private RelativeLayout btn_add_avt;
    private ProgressBar progressBar;
    private CircleImageView img_avt;
    private CardView btn_front_idCard, btn_back_idCard;
    private ImageView img, btn_back, front_idCard_img, back_idCard_img, iv_lens_front, iv_lens_back;
    private Uri ImgUri, front_ImgUri, back_ImgUri;
    private FirebaseAuth mAuth;
    private String fullname, email, password;
    private static final int CAMERA_REQUEST_CODE_1 = 101;
    private static final int CAMERA_REQUEST_CODE_2 = 102;
    private static final String DEFAULT_GENDER = "";
    private static final String DEFAULT_NATIONALITY = "";
    private static final int DEFAULT_CCCD = 0;
    private static final int DEFAULT_ROLE = 1;

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_phone_number);

        initUI();
        initFirebaseAuth();
        retrieveIntentData();

        setButtonListeners();
        setupFieldValidation();
    }

    private void initUI() {
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
        btn_back = findViewById(R.id.btn_back_add_number);
        img = findViewById(R.id.img_add_phone_number);
        progressBar = findViewById(R.id.progressBar_add_avt);
        btn_front_idCard = findViewById(R.id.btn_add_front_cccd);
        btn_back_idCard = findViewById(R.id.btn_add_back_cccd);
        front_idCard_img = findViewById(R.id.iv_front_cccd);
        back_idCard_img = findViewById(R.id.iv_back_cccd);
        iv_lens_front = findViewById(R.id.iv_lens_front_cccd);
        iv_lens_back = findViewById(R.id.iv_lens_back_cccd);
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void retrieveIntentData() {
        Intent intent = getIntent();
        fullname = intent.getStringExtra("fullName");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
    }

    private void setButtonListeners() {
        btn_back.setOnClickListener(v -> finish());

        btn_choose_date.setOnClickListener(v -> openDatePickerDialog());

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::onImageSelected
        );

        btn_add_avt.setOnClickListener(v -> selectImage(launcher));

        btn_create.setOnClickListener(v -> createAccount());

        btn_front_idCard.setOnClickListener(v -> {
            open_camera(CAMERA_REQUEST_CODE_1);
        });
        btn_back_idCard.setOnClickListener(v -> {
            open_camera(CAMERA_REQUEST_CODE_2);
        });
    }

    private void open_camera(int requestCode) {
        if (checkCameraPermission()) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, requestCode);
        } else {
            // Nếu chưa có quyền, yêu cầu quyền camera
            requestCameraPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền camera đã được cấp, bạn có thể tiếp tục thực hiện các hành động cần thiết
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Quyền không được cấp, thông báo cho người dùng
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                if (bitmap != null) {
                    if (requestCode == CAMERA_REQUEST_CODE_1) {
                        front_idCard_img.setImageBitmap(bitmap);
                        front_ImgUri = getImageUri(this, bitmap);
                        iv_lens_front.setVisibility(View.GONE);
                    } else if (requestCode == CAMERA_REQUEST_CODE_2) {
                        back_idCard_img.setImageBitmap(bitmap);
                        back_ImgUri = getImageUri(this, bitmap);
                        iv_lens_back.setVisibility(View.GONE);
                    }
                } else {
                    Log.e(TAG, "Bitmap is null");
                }
            } else {
                Log.e(TAG, "Intent or bundle is null");
            }

        }
    }
    private Uri getImageUri(Context context, Bitmap bitmap) {
        // Tạo một tên tệp tạm thời cho ảnh
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "temp_image", null);
        return Uri.parse(path);
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 100);
    }

    private void setupFieldValidation() {
        setupTextWatcher(edt_phoneNumber, layout_phoneNumber, "Please enter your phone number");
        setupTextWatcher(edt_address, layout_address, "Please enter your address");
        setupTextWatcher(edt_date, layout_date, "Please enter your birthday");
    }

    private void setupTextWatcher(TextInputEditText editText, TextInputLayout layout, String errorMessage) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout.setError(s.length() == 0 ? errorMessage : null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void openDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                this,
                this::onDateSet,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void onDateSet(DatePicker view, int year, int month, int day) {
        edt_date.setText(String.format("%02d/%02d/%04d", day, month + 1, year));
    }

    private void onImageSelected(ActivityResult result) {
        // Lấy dữ liệu từ kết quả
        Uri imageUri = result.getData().getData();

        if (imageUri != null) {
            // Lưu URI ảnh vào biến
            ImgUri = imageUri;
            // Nếu có URI ảnh thì tải ảnh vào ImageView
            Picasso.get().load(imageUri).resize(140, 140).centerCrop().into(img_avt);
            img.setVisibility(View.GONE);
        } else {
            // Nếu không có ảnh được chọn, hiển thị thông báo
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void selectImage(ActivityResultLauncher<Intent> launcher) {
        Intent photoPicker = new Intent();
        photoPicker.setType("image/*");
        photoPicker.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(photoPicker);
    }

    private void createAccount() {
        String phoneNumber = edt_phoneNumber.getText().toString();
        String address = edt_address.getText().toString();
        String date = edt_date.getText().toString();

        if (!validateFields(phoneNumber, address, date)) return;
        uploadImages(phoneNumber, address, date);
    }

    private boolean validateFields(String phoneNumber, String address, String date) {
        boolean isValid = true;
        if (phoneNumber.isEmpty()) {
            layout_phoneNumber.setError("Please enter your phone number");
            isValid = false;
        }
        if (address.isEmpty()) {
            layout_address.setError("Please enter your address");
            isValid = false;
        }
        if (date.isEmpty()) {
            layout_date.setError("Please enter your birthday");
            isValid = false;
        }
        return isValid;
    }

    private void uploadImages(String phoneNumber, String address, String date) {
        // Kiểm tra nếu chưa có ảnh nào trong 3 ảnh yêu cầu
        if (ImgUri == null && front_ImgUri == null && back_ImgUri == null) {
            Toast.makeText(this, "Please select all images", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        // Tạo StorageReference cho từng ảnh
        StorageReference avatarRef = storageRef.child("images/" + "_avatar." + System.currentTimeMillis()+ "." + getFileExtension(ImgUri));
        StorageReference frontIdRef = storageRef.child("id-card/" + "_front_id." + System.currentTimeMillis()+ "." + getFileExtension(front_ImgUri));
        StorageReference backIdRef = storageRef.child("id-card/" + "_back_id." + System.currentTimeMillis()+ "." + getFileExtension(back_ImgUri));

        // Upload từng ảnh và lấy URL của chúng
        uploadSingleImage(avatarRef, ImgUri, avatarUrl ->
                uploadSingleImage(frontIdRef, front_ImgUri, frontIdUrl ->
                        uploadSingleImage(backIdRef, back_ImgUri, backIdUrl -> {
                            // Khi cả ba ảnh đã upload xong, gọi hàm tạo tài khoản trong Firestore
                            createAccountInDatabase(avatarUrl, frontIdUrl, backIdUrl, phoneNumber, address, date);
                            progressBar.setVisibility(View.INVISIBLE);
                        })));
    }

    // Hàm upload từng ảnh riêng lẻ và trả về URL qua callback
    private void uploadSingleImage(StorageReference imageRef, Uri imageUri, OnImageUploadCompleteListener listener) {
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Lấy URL tải về sau khi upload thành công
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        listener.onUploadComplete(uri.toString());  // Trả về URL
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "Failed to get download URL", e);
                                        Toast.makeText(Add_phoneNumber.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to upload image", e);
                        Toast.makeText(Add_phoneNumber.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Giao diện callback để xử lý URL sau khi tải lên thành công
    interface OnImageUploadCompleteListener {
        void onUploadComplete(String imageUrl);
    }

    // Hàm tạo tài khoản sau khi đã upload tất cả ảnh lên Firebase
    private void createAccountInDatabase(String avatarUrl, String frontIdUrl, String backIdUrl, String phoneNumber, String address, String date) {
        Account account = new Account();
        account.setUsername(fullname);
        account.setSdt(phoneNumber);
        account.setEmail(email);
        account.setPassword(password);
        account.setDiaChi(address);
        account.setNgaySinh(date);
        account.setGioiTinh(DEFAULT_GENDER);
        account.setQuocTich(DEFAULT_NATIONALITY);
        account.setRole(DEFAULT_ROLE);
        account.setAvt(avatarUrl);
        account.setImgcccdtruoc(frontIdUrl);  // Mặt trước CCCD
        account.setImgcccdsau(backIdUrl);    // Mặt sau CCCD
        account.setCccd(DEFAULT_CCCD);
        account.setUid("");

        Api_service.service.create_account(account).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Add_phoneNumber.this, "Create " + fullname + " success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Add_phoneNumber.this, "Create " + fullname + " failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e(TAG, "Error: " + throwable.getMessage());
            }
        });

        PopupDialog.getInstance(this)
                .statusDialogBuilder()
                .createSuccessDialog()
                .setHeading("Well Done")
                .setDescription("You have successfully completed the task")
                .build(dialog -> startActivity(new Intent(Add_phoneNumber.this, Loginscreen.class)))
                .show();
    }

    private String getFileExtension(Uri uri) {
        if (uri == null) {
            return null; // Hoặc xử lý lỗi nếu cần
        }

        String type = getContentResolver().getType(uri);
        if (type != null) {
            // Phân tích loại MIME và lấy phần mở rộng tệp
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(type);
        }
        return null; // Trường hợp không thể xác định loại MIME
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) reload();
    }

    private void reload() {
        // Reload the activity, if needed.
    }
}
