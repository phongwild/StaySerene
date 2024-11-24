package phongtaph31865.poly.stayserene.Login_Register.ForgotPass;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import phongtaph31865.poly.stayserene.databinding.ActivityOtpAuthenBinding;
import phongtaph31865.poly.stayserene.R;

public class Activity_otp_authen extends AppCompatActivity {
    private ActivityOtpAuthenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOtpAuthenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String otp = intent.getStringExtra("otp");
        //Logic
        binding.btnBackOtpAuthen.setOnClickListener(v -> finish());
        binding.tvEmailOtpAuth.setText(email);
        addTextWatchers();
        binding.btnResendCode.setOnClickListener(v -> {});
        binding.btnSubmitOtp.setOnClickListener(v -> {
            String OTP = binding.edtOtp1.getText().toString() + binding.edtOtp2.getText().toString() + binding.edtOtp3.getText().toString() + binding.edtOtp4.getText().toString();
            if (OTP.equals(otp)) {
                Intent i = new Intent(this, Activity_updatePass.class);
                i.putExtra("email", email);
                startActivity(i);
            }else Toast.makeText(this, "OTP is incorrect, please try again", Toast.LENGTH_SHORT).show();
        });
    }
    private void addTextWatchers() {
        binding.edtOtp1.addTextChangedListener(new OTPTextWatcher(binding.edtOtp1, binding.edtOtp2, null));
        binding.edtOtp2.addTextChangedListener(new OTPTextWatcher(binding.edtOtp2, binding.edtOtp3, binding.edtOtp1));
        binding.edtOtp3.addTextChangedListener(new OTPTextWatcher(binding.edtOtp3, binding.edtOtp4, binding.edtOtp2));
        binding.edtOtp4.addTextChangedListener(new OTPTextWatcher(binding.edtOtp4, null, binding.edtOtp3));
    }
    private class OTPTextWatcher implements TextWatcher {
        private EditText currentEDT;
        private EditText nextEDT;
        private EditText prevEDT;


        public OTPTextWatcher(EditText currentEDT, EditText nextEDT, EditText prevEDT) {
            this.currentEDT = currentEDT;
            this.nextEDT = nextEDT;
            this.prevEDT = prevEDT;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Nếu người dùng nhập 1 ký tự, chuyển sang trường tiếp theo
            if (s.length() == 1 && nextEDT != null) {
                nextEDT.requestFocus();
            }
            // Nếu người dùng xóa ký tự
            else if (s.length() == 0) {
                // Trường hợp xóa ký tự ở edtOtp1 (không có trường trước, không làm gì)
                if (prevEDT != null) {
                    prevEDT.requestFocus(); // Chuyển focus về trường trước nếu có
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (binding.edtOtp1.getText().length() > 0 && binding.edtOtp2.getText().length() > 0 && binding.edtOtp3.getText().length() > 0 && binding.edtOtp4.getText().length() > 0){
                String OTP = binding.edtOtp1.getText().toString() + binding.edtOtp2.getText().toString() + binding.edtOtp3.getText().toString() + binding.edtOtp4.getText().toString();
                validateOTP(OTP);
            }
        }
        private void validateOTP(String otp) {

        }
    }
}