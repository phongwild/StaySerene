package phongtaph31865.poly.stayserene.Screen_user.Activity.Setting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;

import phongtaph31865.poly.stayserene.databinding.ActivityPolicyBinding;

public class Activity_policy extends AppCompatActivity {
    private ActivityPolicyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnBackPolicy.setOnClickListener(v -> {
            finish();
        });
        binding.btnPassPolicy.setOnClickListener(v -> {
            startActivity(new Intent(Activity_policy.this, Activity_changePass.class));
        });
        binding.btn2faPolicy.setOnClickListener(v -> {});
        binding.btnDeleteAccPolicy.setOnClickListener(v -> {
            diaLogDel();
        });
    }
    private void diaLogDel(){
        PopupDialog.getInstance(this)
                .standardDialogBuilder()
                .createIOSDialog()
                .setHeading("Delete account")
                .setPositiveButtonText("Yes")
                .setDescription("Are you sure you want to delele this account?" +
                        " This action cannot be undone")
                .build(new StandardDialogActionListener() {
                    @Override
                    public void onPositiveButtonClicked(Dialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeButtonClicked(Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}