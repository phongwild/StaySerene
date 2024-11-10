package phongtaph31865.poly.stayserene.Screen_user.Activity.Setting;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.databinding.ActivityChangeIdCardBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_change_id_card extends AppCompatActivity {
    private ActivityChangeIdCardBinding binding;
    private Uri imgUriFront, imgUriBack;
    private static final int CAMERA_REQUEST_CODE_1 = 101;
    private static final int CAMERA_REQUEST_CODE_2 = 102;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeIdCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");
        setButtonListeners(uid);
    }

    private void setButtonListeners(String uid) {
        binding.btnBackChangeIdCard.setOnClickListener(v -> finish());
        binding.btnChooseFrontIdCard.setOnClickListener(v -> openCamera(CAMERA_REQUEST_CODE_1));
        binding.btnChooseBackIdCard.setOnClickListener(v -> openCamera(CAMERA_REQUEST_CODE_2));

        binding.btnSaveChangeIdCard.setOnClickListener(v -> {
            PopupDialog.getInstance(this)
                    .standardDialogBuilder()
                    .createIOSDialog()
                    .setHeading("Update information")
                    .setDescription("Are you sure to change the information?")
                    .setPositiveButtonText("Yes")
                    .build(new StandardDialogActionListener() {
                        @Override
                        public void onPositiveButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                            handleSave(uid);
                        }

                        @Override
                        public void onNegativeButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        });
    }

    private void handleSave(String uid) {
        if (imgUriFront == null || imgUriBack == null) {
            Toast.makeText(this, "Please choose front and back ID Card", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.progressBarChangeIdCard.setVisibility(View.VISIBLE);
        StorageReference frontIdRef = storageRef.child("id-card/front_" + System.currentTimeMillis() + "." + getFileExtension(imgUriFront));
        StorageReference backIdRef = storageRef.child("id-card/back_" + System.currentTimeMillis() + "." + getFileExtension(imgUriBack));

        uploadSingleImage(frontIdRef, imgUriFront, frontUrl -> {
            uploadSingleImage(backIdRef, imgUriBack, backUrl -> {
                updateDataUser(frontUrl, backUrl, uid);
                binding.progressBarChangeIdCard.setVisibility(View.INVISIBLE);
            });
        });
    }

    private void updateDataUser(String frontUrl, String backUrl, String uid) {
        Account account = new Account();
        account.setImgcccdtruoc(frontUrl);
        account.setImgcccdsau(backUrl);

        Api_service.service.update_account(uid, account).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Activity_change_id_card.this, "Update successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
            }
        });
    }

    private void uploadSingleImage(StorageReference imageRef, Uri imageUri, OnImageUploadCompleteListener listener) {
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> listener.onUploadComplete(uri.toString()))
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Failed to get download URL", e);
                            Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                        }))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to upload image", e);
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }

    interface OnImageUploadCompleteListener {
        void onUploadComplete(String imageUrl);
    }

    private String getFileExtension(Uri uri) {
        if (uri == null) return "jpg"; // Default file extension if null
        String type = getContentResolver().getType(uri);
        return type != null ? MimeTypeMap.getSingleton().getExtensionFromMimeType(type) : "jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (bitmap != null) {
                if (requestCode == CAMERA_REQUEST_CODE_1) {
                    binding.ivFrontIdCard.setImageBitmap(bitmap);
                    imgUriFront = getImageUri(this, bitmap);
                    binding.ivLensFrontIdCard.setVisibility(View.GONE);
                } else if (requestCode == CAMERA_REQUEST_CODE_2) {
                    binding.ivBackIdCard.setImageBitmap(bitmap);
                    imgUriBack = getImageUri(this, bitmap);
                    binding.ivLensBackIdCard.setVisibility(View.GONE);
                }
            }
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "temp_image", null);
        return Uri.parse(path);
    }

    private void openCamera(int requestCode) {
        if (checkCameraPermission()) {
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), requestCode);
        } else {
            requestCameraPermission();
        }
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }
}
