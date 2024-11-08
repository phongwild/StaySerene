package phongtaph31865.poly.stayserene.Screen_user.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.saadahmedev.popupdialog.PopupDialog;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Login_Register.Add_phoneNumber;
import phongtaph31865.poly.stayserene.Login_Register.Loginscreen;
import phongtaph31865.poly.stayserene.Model.ChangePassRequest;
import phongtaph31865.poly.stayserene.databinding.ActivityChangePassBinding;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_changePass extends AppCompatActivity {
    private ActivityChangePassBinding binding;
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
                ChangePassword(getEmailFromSharedPreferences(), oldPassword, newPassword);
            }
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
                            .build(dialog -> finish())
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