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
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.databinding.ActivityInformationBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Information extends AppCompatActivity {
    private ActivityInformationBinding binding;
    private Uri ImgUri;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");
        String username = sharedPreferences.getString("username", "");
        String sdt = sharedPreferences.getString("sdt", "");
        String address = sharedPreferences.getString("address", "");
        String email = sharedPreferences.getString("email", "");
        String cccd = sharedPreferences.getString("cccd", "");
        String gender = sharedPreferences.getString("gender", "");
        String birthday = sharedPreferences.getString("birthday", "");
        String avatar = sharedPreferences.getString("avatar", "");
        String quoctich = sharedPreferences.getString("quoctich", "");
        Log.e("img", avatar);
        binding.btnBackAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (username != null) {
            binding.edFullNameInfo.setText(username);
        }
        if (sdt != null) {
            binding.edPhoneNumberInfo.setText(sdt);
        }
        if (address != null) {
            binding.edAddressInfo.setText(address);
        }
        if (email != null) {
            binding.edEmailInfo.setText(email);
        }
        if (cccd != null) {
            binding.edCccdInfo.setText(cccd);
        }
        if (avatar != null && !avatar.isEmpty()) {
            Picasso.get().load(avatar).into(binding.ivAddAvtInfo);
            binding.imgInfo.setVisibility(View.GONE);
        }else {
            binding.imgInfo.setVisibility(View.VISIBLE);
            binding.ivAddAvtInfo.setVisibility(View.GONE);
        }
        if (quoctich != null) {
            binding.edNationalInfo.setText(quoctich);
        }
        if (birthday != null) {
            binding.edDateBirthInfo.setText(birthday);
        }
        if (gender != null) {
            if (gender.equals("Male")) {
                binding.rbtnMaleInfo.setChecked(true);
            } else {
                binding.rbtnFemaleInfo.setChecked(true);
            }
        }
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == Activity.RESULT_OK) {
                            Intent data = o.getData();
                            ImgUri = data.getData();
                            Picasso.get().load(ImgUri).resize(140, 140).centerCrop().into(binding.ivAddAvtInfo);
                            binding.imgInfo.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(Information.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        binding.btnAddAvtInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setType("image/*");
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(photoPicker);
            }
        });
        binding.btnChooseDateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_Date_Time();
            }
        });
        binding.btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupDialog.getInstance(Information.this)
                        .standardDialogBuilder()
                        .createIOSDialog()
                        .setHeading("Update information")
                        .setDescription("Are you sure to change to information?")
                        .setPositiveButtonText("Yes")
                        .build(new StandardDialogActionListener() {
                            @Override
                            public void onPositiveButtonClicked(Dialog dialog) {
                                dialog.dismiss();
                                if (ImgUri != null) {
                                    StorageReference imgRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(ImgUri));
                                    imgRef.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    String newAvt = uri.toString();
                                                    updateAccountInfo(uid, newAvt);
//                                                    binding.progressBarAddAvtInfo.setVisibility(View.INVISIBLE);
                                                }
                                            });
                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                            binding.progressBarAddAvtInfo.setVisibility(View.VISIBLE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            binding.progressBarAddAvtInfo.setVisibility(View.INVISIBLE);
                                            dialog.dismiss();
                                            Toast.makeText(Information.this, "Upload avatar failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    updateAccountInfo(uid, null);
                                }

                            }

                            @Override
                            public void onNegativeButtonClicked(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void updateAccountInfo(String uid, String avatarUrl) {
        // Lấy các giá trị từ form nhập liệu
        String username = binding.edFullNameInfo.getText().toString();
        String phone = binding.edPhoneNumberInfo.getText().toString();
        String address = binding.edAddressInfo.getText().toString();
        String email = binding.edEmailInfo.getText().toString();
        String cccd = binding.edCccdInfo.getText().toString();
        String nationality = binding.edNationalInfo.getText().toString();
        String birthday = binding.edDateBirthInfo.getText().toString();
        String gender = binding.rbtnMaleInfo.isChecked() ? "Male" : "Female";

        // Khởi tạo đối tượng Account và cập nhật thông tin
        Account account = new Account();
        account.setUsername(username);
        account.setSdt(phone);
        account.setDiaChi(address);
        account.setEmail(email);
        account.setCccd(cccd);
        account.setQuocTich(nationality);
        account.setNgaySinh(birthday);
        account.setGioiTinh(gender);
        account.setRole(1);
        if (avatarUrl != null) {
            account.setAvt(avatarUrl);
        }

        // Gọi API để cập nhật tài khoản
        Api_service.service.update_account(uid, account).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    saveUserIdToSharedPreferences(uid, username, phone, address, email, Integer.parseInt(cccd), gender, birthday, avatarUrl, nationality);
                    Toast.makeText(Information.this, "Account updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Information.this, "Failed to update account", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e("update info", throwable.getMessage());
            }
        });
    }

    private String getFileExtension(Uri fileUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    private void open_Date_Time() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                binding.edDateBirthInfo.setText(String.format("%02d/%02d/%04d", day, month + 1, year));
            }
        }, year, month, day);
        dialog.show();
    }

    private void saveUserIdToSharedPreferences(String Uid, String username, String sdt, String address, String email, int cccd, String gender, String birthday, String avatar, String quoctich) {
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", Uid);
        editor.putString("username", username);
        editor.putString("sdt", sdt);
        editor.putString("address", address);
        editor.putString("email", email);
        editor.putString("cccd", String.valueOf(cccd));
        editor.putString("gender", gender);
        editor.putString("birthday", birthday);
        editor.putString("avatar", avatar);
        editor.putString("quoctich", quoctich);
        editor.apply();
    }
}