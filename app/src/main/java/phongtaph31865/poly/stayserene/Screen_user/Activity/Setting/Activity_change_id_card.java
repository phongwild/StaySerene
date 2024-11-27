package phongtaph31865.poly.stayserene.Screen_user.Activity.Setting;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.BottomSheet.Dialog_OTP;
import phongtaph31865.poly.stayserene.MailConfig.MailConfig;
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
    private String currentPhotoPath;
    private String DEFAULT_CCCD = "";
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private String OTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeIdCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setButtonListeners(getID());
    }

    private void setButtonListeners(String uid) {
        binding.btnBackChangeIdCard.setOnClickListener(v -> finish());
        binding.btnChooseFrontIdCard.setOnClickListener(v -> open_camera(CAMERA_REQUEST_CODE_1));
        binding.btnChooseBackIdCard.setOnClickListener(v -> open_camera(CAMERA_REQUEST_CODE_2));

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
                            checkValidation();
                        }

                        @Override
                        public void onNegativeButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        });
    }
    private void checkValidation() {
        if (imgUriFront == null && imgUriBack == null) {
            Toast.makeText(Activity_change_id_card.this, "Please choose front and back ID Card", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imgUriFront == null) {
            Toast.makeText(Activity_change_id_card.this, "Please choose front ID Card", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imgUriBack == null) {
            Toast.makeText(Activity_change_id_card.this, "Please choose back ID Card", Toast.LENGTH_SHORT).show();
            return;
        }
        if (DEFAULT_CCCD.equals("0")) {
            Toast.makeText(Activity_change_id_card.this, "Invalid ID Card", Toast.LENGTH_SHORT).show();
            return;
        }
        sendOTP();
    }
    private void handleSave(String uid) {
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
        Api_service.service.get_account_byId(getID()).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    Account account = response.body().get(0);
                    account.setCccd(DEFAULT_CCCD);
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
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
            }
        });
    }
    private void sendOTP() {
        OTP = MailConfig.generateOTP(4);
        MailConfig.sendOtpEmail(getEmail(), OTP);
        Dialog_OTP dialogOtp = Dialog_OTP.newInstance(getEmail(), OTP);
        dialogOtp.show(getSupportFragmentManager(), "Dialog_OTP");
        dialogOtp.setOtpSubmitCallback(new Dialog_OTP.OtpSubmitCallback() {
            @Override
            public void onOtpSubmit(String otp) {
                handleSave(getID());
            }
        });
        dialogOtp.setOtpResendCallback(email -> {
            OTP = MailConfig.generateOTP(4);
            MailConfig.sendOtpEmail(email, OTP);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE_1 || requestCode == CAMERA_REQUEST_CODE_2) {
                // Kiểm tra nếu đường dẫn ảnh không null và file tồn tại
                File file = new File(currentPhotoPath);  // Lấy file ảnh từ đường dẫn đã lưu
                if (file.exists()) {
//                    Uri uri = Uri.fromFile(file);
//                    cropImage(uri);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());  // Đọc ảnh từ file
                    if (bitmap != null) {
                        // Hiển thị ảnh lên ImageView
                        if (requestCode == CAMERA_REQUEST_CODE_1) {
                            binding.ivFrontIdCard.setImageBitmap(bitmap);
                            imgUriFront = Uri.fromFile(file);  // Lưu URI của ảnh
                            recognizeTextFromImage(bitmap);
                            binding.ivLensFrontIdCard.setVisibility(View.INVISIBLE);
                        } else if (requestCode == CAMERA_REQUEST_CODE_2) {
                            binding.ivBackIdCard.setImageBitmap(bitmap);
                            imgUriBack = Uri.fromFile(file);  // Lưu URI của ảnh
                            binding.ivLensBackIdCard.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Log.e("onActivityResult", "Bitmap is null");
                        Toast.makeText(this, "Failed to decode image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("onActivityResult", "File does not exist: " + currentPhotoPath);
                    Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void recognizeTextFromImage(Bitmap bitmap){
        // Chuyển đổi bitmap thành InputImage
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        // Tạo TextRecognizer
        TextRecognizerOptions options = new TextRecognizerOptions.Builder().build();
        TextRecognizer recognizer = TextRecognition.getClient(options);

        // Xử lý ảnh và nhận dạng văn bản
        recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                String recognizedText = text.getText();
                Log.d("RecognizedText", recognizedText);

                String cccd = extractCCCD(recognizedText);
                Log.d("CCCD", cccd);
                DEFAULT_CCCD = cccd;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TextRecognition", "Error: " + e.getMessage());
            }
        });
    }
    private String extractCCCD(String recognizedText) {
        String regex = "\\d{12}"; // Biểu thức chính quy tìm số CCCD 12 chữ số
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(recognizedText);

        if (matcher.find()) {
            return matcher.group();  // Trả về số CCCD tìm được
        } else {
            Toast.makeText(this, "Not found ID Card", Toast.LENGTH_SHORT).show();
            return "0";
        }
    }

    private void open_camera(int requestCode) {
        if (checkCameraPermission()) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Kiểm tra nếu có thể mở camera
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    // Tạo file để lưu ảnh chụp
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Xử lý lỗi tạo file
                    ex.printStackTrace();
                    Log.e(TAG, "Error occurred while creating the file", ex);
                    return;
                }

                if (photoFile != null) {
                    // Lấy Uri của file ảnh vừa tạo
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "phongtaph31865.poly.stayserene.fileprovider", photoFile);

                    // Truyền Uri vào Intent để lưu ảnh
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, requestCode);
                }
            }
        } else {
            requestCameraPermission();
        }
    }
    private File createImageFile() throws IOException {
        // Tạo tên file ảnh tạm
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Tạo file ảnh mới trong bộ nhớ
        File image = File.createTempFile(
                imageFileName, // Tên file
                ".jpg", // Đuôi file
                storageDir // Thư mục lưu
        );

        // Lưu Uri của file để lấy sau
        currentPhotoPath = image.getAbsolutePath();
        Log.d("CreateImageFile", "Image path: " + currentPhotoPath);  // In ra đường dẫn để debug
        return image;
    }
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }
    private String getID() {
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", "");
    }
    private String getEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("email", "");
    }
}
