package phongtaph31865.poly.stayserene.Screen_user.Activity.Setting;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.BottomSheet.Dialog_OTP;
import phongtaph31865.poly.stayserene.MailConfig.MailConfig;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.databinding.ActivityInformationBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Information extends AppCompatActivity {
    private ActivityInformationBinding binding;
    private Uri imgUri;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private String OTP, EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadUserData();
        setUpListeners();
    }

    private void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        EMAIL = sharedPreferences.getString("email", "");

        binding.edFullNameInfo.setText(sharedPreferences.getString("username", ""));
        binding.edPhoneNumberInfo.setText(sharedPreferences.getString("sdt", ""));
        binding.edAddressInfo.setText(sharedPreferences.getString("address", ""));
        binding.edEmailInfo.setText(EMAIL);
        binding.edCccdInfo.setText(sharedPreferences.getString("cccd", ""));
        binding.edNationalInfo.setText(sharedPreferences.getString("quoctich", ""));
        binding.edDateBirthInfo.setText(sharedPreferences.getString("birthday", ""));

        String gender = sharedPreferences.getString("gender", "");
        binding.rbtnMaleInfo.setChecked("Male".equals(gender));
        binding.rbtnFemaleInfo.setChecked("Female".equals(gender));

        String avatar = sharedPreferences.getString("avatar", "");
        if (avatar != null && !avatar.isEmpty()) {
            Picasso.get().load(avatar).into(binding.ivAddAvtInfo);
            binding.imgInfo.setVisibility(View.GONE);
        } else {
            binding.imgInfo.setVisibility(View.VISIBLE);
            binding.ivAddAvtInfo.setVisibility(View.GONE);
        }
    }

    private void setUpListeners() {
        binding.btnBackAccountInfo.setOnClickListener(v -> finish());

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            imgUri = data.getData();
                            Picasso.get().load(imgUri).resize(140, 140).centerCrop().into(binding.ivAddAvtInfo);
                            binding.imgInfo.setVisibility(View.GONE);
                        }
                    } else {
                        showToast("No image selected");
                    }
                }
        );

        binding.btnAddAvtInfo.setOnClickListener(v -> {
            Intent photoPicker = new Intent();
            photoPicker.setType("image/*");
            photoPicker.setAction(Intent.ACTION_GET_CONTENT);
            launcher.launch(photoPicker);
        });

        binding.btnChooseDateInfo.setOnClickListener(v -> openDatePicker());

        binding.btnSaveInfo.setOnClickListener(v -> {
            PopupDialog.getInstance(Information.this)
                    .standardDialogBuilder()
                    .createIOSDialog()
                    .setHeading("Update information")
                    .setDescription("Are you sure to change information?")
                    .setPositiveButtonText("Yes")
                    .build(new StandardDialogActionListener() {
                        @Override
                        public void onPositiveButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                            sendOTP();
                        }

                        @Override
                        public void onNegativeButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        });
    }
    private void sendOTP() {
        OTP = MailConfig.generateOTP(4);
        MailConfig.sendOtpEmail(EMAIL, OTP);
        Dialog_OTP dialogOtp = Dialog_OTP.newInstance(EMAIL, OTP);
        dialogOtp.show(getSupportFragmentManager(), "Dialog_OTP");
        dialogOtp.setOtpSubmitCallback(new Dialog_OTP.OtpSubmitCallback() {
            @Override
            public void onOtpSubmit(String otp) {
                if (imgUri != null) {
                    uploadAvatarAndSaveInfo();
                } else {
                    saveAccountInfo(null);
                }
            }
        });
    }
    private void uploadAvatarAndSaveInfo() {
        StorageReference imgRef = storageRef.child("images/" + "_avatar." + System.currentTimeMillis() + "." + getFileExtension(imgUri));
        imgRef.putFile(imgUri).addOnSuccessListener(taskSnapshot ->
                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            saveAccountInfo(uri.toString());
                        })
                ).addOnProgressListener(snapshot -> binding.progressBarAddAvtInfo.setVisibility(View.VISIBLE))
                .addOnFailureListener(e -> {
                    binding.progressBarAddAvtInfo.setVisibility(View.GONE);
                    showToast("Upload avatar failed");
                });
    }

    private void saveAccountInfo(String avatarUrl) {
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");

        // Tạo đối tượng Account từ dữ liệu người dùng
        Account account = new Account();
        account.setUsername(binding.edFullNameInfo.getText().toString());
        account.setSdt(binding.edPhoneNumberInfo.getText().toString());
        account.setDiaChi(binding.edAddressInfo.getText().toString());
        account.setEmail(binding.edEmailInfo.getText().toString());
        account.setCccd(binding.edCccdInfo.getText().toString());
        account.setQuocTich(binding.edNationalInfo.getText().toString());
        account.setNgaySinh(binding.edDateBirthInfo.getText().toString());
        account.setGioiTinh(binding.rbtnMaleInfo.isChecked() ? "Male" : "Female");
        account.setRole(1);
        if (avatarUrl != null) account.setAvt(avatarUrl);

        // Gọi API cập nhật thông tin
        Api_service.service.update_account(uid, account).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    saveUserDataToPreferences(uid, account, avatarUrl);
                    showToast("Account updated successfully");
                    finish();
                } else {
                    showToast("Failed to update account");
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                Log.e("Update Info", t.getMessage());
                showToast("Failed to update account");
            }
        });
    }

    private void saveUserDataToPreferences(String uid, Account account, String avatarUrl) {
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", uid);
        editor.putString("username", account.getUsername());
        editor.putString("sdt", account.getSdt());
        editor.putString("address", account.getDiaChi());
        editor.putString("email", account.getEmail());
        editor.putString("cccd", account.getCccd());
        editor.putString("gender", account.getGioiTinh());
        editor.putString("birthday", account.getNgaySinh());
        editor.putString("avatar", avatarUrl);
        editor.putString("quoctich", account.getQuocTich());
        editor.apply();
    }

    private String getFileExtension(Uri fileUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) ->
                binding.edDateBirthInfo.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
