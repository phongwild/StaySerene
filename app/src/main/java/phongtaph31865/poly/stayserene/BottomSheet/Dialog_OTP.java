package phongtaph31865.poly.stayserene.BottomSheet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Timer;
import java.util.TimerTask;

import phongtaph31865.poly.stayserene.R;

public class Dialog_OTP extends BottomSheetDialogFragment {
    private String email;
    private String correctOTP;
    private OtpSubmitCallback otpSubmitCallback;
    private OtpResendCallback otpResendCallback;
    private Timer timer;
    private int timeLeft = 0;

    // Interface để gửi OTP về Activity/Fragment
    public interface OtpSubmitCallback {
        void onOtpSubmit(String otp);
    }
    public interface OtpResendCallback {
        void onOtpResend(String email);
    }

    // Phương thức để tạo Bottom Sheet với tham số
    public static Dialog_OTP newInstance(String email, String correctOTP) {
        Dialog_OTP fragment = new Dialog_OTP();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("correctOTP", correctOTP);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy email và OTP đúng từ arguments
        if (getArguments() != null) {
            email = getArguments().getString("email", "");
            correctOTP = getArguments().getString("correctOTP", "");
        }

        // Ánh xạ view
        EditText edt1 = view.findViewById(R.id.edt_otp1_bottom_sheet);
        EditText edt2 = view.findViewById(R.id.edt_otp2_bottom_sheet);
        EditText edt3 = view.findViewById(R.id.edt_otp3_bottom_sheet);
        EditText edt4 = view.findViewById(R.id.edt_otp4_bottom_sheet);
        TextView tvEmail = view.findViewById(R.id.tv_email_otp_bottom_sheet);
        CardView btnSubmit = view.findViewById(R.id.btn_submit_otp_bottom_sheet);
        TextView btn_resend = view.findViewById(R.id.btn_resendCode_bottom_sheet);
        TextView tv_counter_time = view.findViewById(R.id.tv_counter_time_bottom_sheet_otp);

        // Hiển thị email lên TextView
        tvEmail.setText(email);

        // Gắn TextWatcher để điều hướng giữa các ô OTP
        edt1.addTextChangedListener(new OTPTextWatcher(edt1, edt2, null));
        edt2.addTextChangedListener(new OTPTextWatcher(edt2, edt3, edt1));
        edt3.addTextChangedListener(new OTPTextWatcher(edt3, edt4, edt2));
        edt4.addTextChangedListener(new OTPTextWatcher(edt4, null, edt3));

        // Xử lý khi nhấn nút Submit
        btnSubmit.setOnClickListener(v -> {
            String otp = edt1.getText().toString() + edt2.getText().toString() + edt3.getText().toString() + edt4.getText().toString();
            if (!TextUtils.isEmpty(otp) && otp.length() == 4) {
                if (otp.equals(correctOTP)) {
                    if (otpSubmitCallback != null) {
                        otpSubmitCallback.onOtpSubmit(otp); // Gửi OTP qua callback
                    }
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "OTP incorrect!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
            }
        });
        btn_resend.setOnClickListener(v -> {
            if(otpResendCallback != null){
                otpResendCallback.onOtpResend(email);
                startTimer(btn_resend, tv_counter_time);
            }
        });
        startTimer(btn_resend, tv_counter_time);
    }

    // Phương thức bắt đầu đếm ngược với Timer
    private void startTimer(TextView btnResend, TextView tvCounterTime) {
        if (timer != null) {
            timer.cancel(); // Dừng timer cũ nếu có
        }

        timeLeft = 60; // Đặt lại thời gian
        btnResend.setEnabled(false); // Vô hiệu hóa nút resend

        // Tạo TimerTask để thực hiện đếm ngược
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(getActivity() != null && !getActivity().isFinishing() && !isDetached()){
                    getActivity().runOnUiThread(() -> {
                        timeLeft--;
                        Log.d("Timer", "Time left: " + timeLeft);
                        tvCounterTime.setText(String.format("Resend in %d seconds", timeLeft));
                        // Nếu thời gian đã hết
                        if (timeLeft <= 0) {
                            timer.cancel(); // Dừng timer
                            btnResend.setEnabled(true); // Cho phép nút resend
                        }
                    });
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

    // Gắn callback để gửi OTP lại
    public void setOtpResendCallback(OtpResendCallback callback) {
        this.otpResendCallback = callback;
    }

    // Gắn callback để nhận kết quả OTP
    public void setOtpSubmitCallback(OtpSubmitCallback callback) {
        this.otpSubmitCallback = callback;
    }

    // TextWatcher để điều hướng giữa các ô OTP
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

        }

        private void validateOTP(String otp) {

        }
    }
}
