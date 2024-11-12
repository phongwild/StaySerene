package phongtaph31865.poly.stayserene.Login_Register.ForgotPass;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import phongtaph31865.poly.stayserene.databinding.ActivityTypePhoneNumberBinding;
import phongtaph31865.poly.stayserene.R;

public class Activity_type_phone_number extends AppCompatActivity {
    private ActivityTypePhoneNumberBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTypePhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnBackTypePhone.setOnClickListener(v -> {
            finish();
        });
        binding.btnSubmitTypePhone.setOnClickListener(v -> {
            getOtp(binding);
        });
    }
    private void getOtp(ActivityTypePhoneNumberBinding binding){

    }
}