package phongtaph31865.poly.stayserene.Login_Register.ForgotPass;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.PhoneAuthProvider;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.MailConfig.MailConfig;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.databinding.ActivityTypePhoneNumberBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_type_phone_number extends AppCompatActivity {
    private ActivityTypePhoneNumberBinding binding;
    private Timer timer;
    private int timeLeft = 0;

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
        if (timeLeft > 0) {
            showToast("Please wait " + timeLeft + " seconds before requesting again");
            return;
        }
        String email = binding.edEmailTypePhone.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            showToast("Please enter your email");
            return;
        }
        checkExistEmail(email);

    }

    private void checkExistEmail(String email) {
        Api_service.service.get_account().enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    List<Account> accounts = response.body();
                    boolean emailExists = false;

                    for (Account account : accounts) {
                        if (account.getEmail().equals(email)) {
                            emailExists = true;
                            break;
                        }
                    }

                    if (!emailExists) {
                        showToast("Email does not exist");
                    } else {
                        // Proceed to send OTP email
                        String otp = MailConfig.generateOTP(4);
                        MailConfig.sendOtpEmail(email, otp);
                        Toast.makeText(Activity_type_phone_number.this, "Send OTP email successfully to " + email, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_type_phone_number.this, Activity_otp_authen.class);
                        intent.putExtra("email", email);
                        intent.putExtra("otp", otp);
                        startActivity(intent);
                        startOtpTimer();
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

    private void startOtpTimer() {
        if (timer != null) {
            timer.cancel(); // Cancel any existing timer
        }

        timeLeft = 60;  // Reset time
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (timeLeft > 0) {
                    timeLeft--;
                    runOnUiThread(() -> binding.tvCounterTimeTypePhone.setText(String.valueOf(timeLeft)));
                } else {
                    runOnUiThread(() -> binding.tvCounterTimeTypePhone.setText("0"));
                    timer.cancel();  // Stop the timer once time is up
                }
            }
        };
        timer.schedule(task, 0, 1000);  // Run every second
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
