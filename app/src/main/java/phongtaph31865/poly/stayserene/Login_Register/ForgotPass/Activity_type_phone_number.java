package phongtaph31865.poly.stayserene.Login_Register.ForgotPass;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.BottomSheet.Dialog_OTP;
import phongtaph31865.poly.stayserene.MailConfig.MailConfig;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.databinding.ActivityTypePhoneNumberBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_type_phone_number extends AppCompatActivity {
    private ActivityTypePhoneNumberBinding binding;
    private String EMAIL, OTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTypePhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up click listeners for the back and submit buttons
        binding.btnBackTypePhone.setOnClickListener(v -> finish());
        binding.btnSubmitTypePhone.setOnClickListener(v -> getOtp());
    }

    private void getOtp() {
        EMAIL = binding.edEmailTypePhone.getText().toString().trim();
        if (TextUtils.isEmpty(EMAIL)) {
            showToast("Please enter your email");
            return;
        }

        checkExistEmail(EMAIL);
    }

    private void checkExistEmail(String email) {
        Api_service.service.get_account().enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    boolean emailExists = false;

                    // Check if the email exists in the response
                    for (Account account : response.body()) {
                        if (account.getEmail().equals(email)) {
                            emailExists = true;
                            break;
                        }
                    }

                    if (emailExists) {
                        sendOTP();
                    } else {
                        showToast("Email does not exist");
                    }
                } else {
                    showToast("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e("TAG", "onFailure: " + throwable.getMessage());
                showToast("Failed to check email: " + throwable.getMessage());
            }
        });
    }

    private void sendOTP() {
        OTP = MailConfig.generateOTP(4);
        MailConfig.sendOtpEmail(EMAIL, OTP);

        Dialog_OTP dialogOtp = Dialog_OTP.newInstance(EMAIL, OTP);
        dialogOtp.show(getSupportFragmentManager(), "Dialog_OTP");

        dialogOtp.setOtpSubmitCallback(otp -> {
            Intent intent = new Intent(Activity_type_phone_number.this, Activity_updatePass.class);
            intent.putExtra("email", EMAIL);
            startActivity(intent);
        });

        dialogOtp.setOtpResendCallback(email -> {
            OTP = MailConfig.generateOTP(4);
            MailConfig.sendOtpEmail(email, OTP);
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
