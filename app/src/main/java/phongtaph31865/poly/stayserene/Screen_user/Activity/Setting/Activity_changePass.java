package phongtaph31865.poly.stayserene.Screen_user.Activity.Setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.saadahmedev.popupdialog.PopupDialog;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.BottomSheet.Dialog_OTP;
import phongtaph31865.poly.stayserene.MailConfig.MailConfig;
import phongtaph31865.poly.stayserene.Model.ChangePassRequest;
import phongtaph31865.poly.stayserene.databinding.ActivityChangePassBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_changePass extends AppCompatActivity {
    private ActivityChangePassBinding binding;
    private String OTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChangePassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnBackChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnSaveChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = binding.edOldPass.getText().toString();
                String newPassword = binding.edNewPassword.getText().toString();
                String confirmPassword = binding.edConfirmPassword.getText().toString();
                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(Activity_changePass.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(Activity_changePass.this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (oldPassword.equals(newPassword)) {
                    Toast.makeText(Activity_changePass.this, "New password cannot be the same as the old password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (oldPassword.equals(confirmPassword)) {
                    Toast.makeText(Activity_changePass.this, "New password cannot be the same as the confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.length() < 6) {
                    Toast.makeText(Activity_changePass.this, "New password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }
               sendOTP(oldPassword, newPassword);
            }
        });
    }
    private void sendOTP(String oldPassword, String newPassword) {
        OTP = MailConfig.generateOTP(4);
        MailConfig.sendOtpEmail(getEmailFromSharedPreferences(), OTP);
        Dialog_OTP dialogOtp = Dialog_OTP.newInstance(getEmailFromSharedPreferences(), OTP);
        dialogOtp.show(getSupportFragmentManager(), "Dialog_OTP");
        dialogOtp.setOtpSubmitCallback(new Dialog_OTP.OtpSubmitCallback() {
            @Override
            public void onOtpSubmit(String otp) {
                ChangePassword(getEmailFromSharedPreferences(), oldPassword, newPassword);
            }
        });
        dialogOtp.setOtpResendCallback(email -> {
            OTP = MailConfig.generateOTP(4);
            Toast.makeText(this, "Send OTP email successfully to " + email, Toast.LENGTH_SHORT).show();
            MailConfig.sendOtpEmail(email, OTP);
        });
    }
    private void ChangePassword(String email, String oldPassword, String newPassword) {
        ChangePassRequest request = new ChangePassRequest(email, oldPassword, newPassword);
        Api_service.service.change_password(request).enqueue(new Callback<ChangePassRequest>() {
            @Override
            public void onResponse(Call<ChangePassRequest> call, Response<ChangePassRequest> response) {
                if (response.isSuccessful()) {
                    PopupDialog.getInstance(Activity_changePass.this)
                            .statusDialogBuilder()
                            .createSuccessDialog()
                            .setHeading("Well Done")
                            .setDescription("You have successfully" +
                                    " completed the task")
                            .build(dialog -> {
                                dialog.dismiss();
                                finish();
                            })
                            .show();
                }else {
                    PopupDialog.getInstance(Activity_changePass.this)
                            .statusDialogBuilder()
                            .createErrorDialog()
                            .setHeading("Uh-Oh")
                            .setDescription("Wrong email or password!" + " Please try again")
                            .setActionButtonText("Try again")
                            .build(Dialog::dismiss)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ChangePassRequest> call, Throwable throwable) {
                Log.e("Error change password", throwable.getMessage());
            }
        });
    }
    private String getEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("email", "");

    }
}