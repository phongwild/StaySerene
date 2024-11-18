package phongtaph31865.poly.stayserene.Login_Register.ForgotPass;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.saadahmedev.popupdialog.PopupDialog;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Login_Register.Loginscreen;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.Model.ChangePassRequest;
import phongtaph31865.poly.stayserene.Model.RecoveyPassRequest;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Activity_changePass;
import phongtaph31865.poly.stayserene.databinding.ActivityUpdatePassBinding;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_updatePass extends AppCompatActivity {
    private ActivityUpdatePassBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUpdatePassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        binding.btnBackUpdatePass.setOnClickListener(v -> finish());
        binding.btnSavePass.setOnClickListener(v -> {
            handleUpdatePass();
        });
    }
    private void handleUpdatePass() {
        String email = getIntent().getStringExtra("email");
        String newPassword = binding.edNewPassword.getText().toString().trim();
        String confirmPassword = binding.edConfirmPassword.getText().toString().trim();
        if (validateInput()) {
            findAcc(email, newPassword);
        }
    }
    private void findAcc(String email, String newPassword){
        Api_service.service.get_account().enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    List<Account> accounts = response.body();
                    for (Account account : accounts) {
                        if (account.getEmail().equals(email)) {
                            String _id = account.get_id();
                            updatePass(_id, newPassword);
                        }
                    }
                }else Log.e("UpdatePassActivity", "Error: " + response.code());
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e("UpdatePassActivity", "Error: " + throwable.getMessage());
            }
        });
    }
    private void updatePass(String id, String newPassword){
        RecoveyPassRequest request = new RecoveyPassRequest(id, newPassword);
        Api_service.service.recovery_password(request).enqueue(new Callback<RecoveyPassRequest>() {
            @Override
            public void onResponse(Call<RecoveyPassRequest> call, Response<RecoveyPassRequest> response) {
                if (response.isSuccessful()) {
                    PopupDialog.getInstance(Activity_updatePass.this)
                            .statusDialogBuilder()
                            .createSuccessDialog()
                            .setHeading("Well Done")
                            .setDescription("You have successfully" +
                                    " completed the task")
                            .build(dialog -> startActivity(new Intent(Activity_updatePass.this, Loginscreen.class)))
                            .show();
                }else {
                    PopupDialog.getInstance(Activity_updatePass.this)
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
            public void onFailure(Call<RecoveyPassRequest> call, Throwable throwable) {
                Log.e("UpdatePassActivity", "Error: " + throwable.getMessage());
            }
        });
    }
    private void setListeners() {
        binding.edNewPassword.addTextChangedListener(createTextWatcher(binding.layoutNewPassword, "Please enter your new password"));
        binding.edConfirmPassword.addTextChangedListener(createTextWatcher(binding.layoutConfirmPassword, "Please enter your confirm password"));
    }
    private TextWatcher createTextWatcher(TextInputLayout layout, String errorMessage) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    layout.setError(errorMessage);
                } else {
                    layout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }
    private boolean validateInput(){
        if (binding.edNewPassword.getText().toString().trim().isEmpty()) {
            binding.layoutNewPassword.setError("Please enter your new password");
            return false;
        }
        if (binding.edConfirmPassword.getText().toString().trim().isEmpty()) {
            binding.layoutConfirmPassword.setError("Please enter your confirm password");
            return false;
        }
        if (!binding.edNewPassword.getText().toString().trim().equals(binding.edConfirmPassword.getText().toString().trim())) {
            binding.layoutConfirmPassword.setError("Password does not match");
            return false;
        }
        return true;
    }
}