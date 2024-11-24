package phongtaph31865.poly.stayserene.Screen_user.Activity.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import phongtaph31865.poly.stayserene.Screen_user.Activity.Activity_changePass;
import phongtaph31865.poly.stayserene.databinding.ActivitySettingBinding;

public class Setting extends AppCompatActivity {
    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.btnBackSetting.setOnClickListener(v -> {
            finish();

        });

        binding.btnAccountFrmUser.setOnClickListener(v -> {
            startActivity(new Intent(Setting.this, Information.class));

        });
        binding.btnChangePassFrmUser.setOnClickListener(v -> {
            startActivity(new Intent(Setting.this, Activity_changePass.class));

        });
        binding.btnChangeCardIdFrmUser.setOnClickListener(v -> {
            startActivity(new Intent(Setting.this, Activity_change_id_card.class));
        });
        binding.btnPolicyFrmUser.setOnClickListener(v -> {
            startActivity(new Intent(Setting.this, Activity_policy.class));
        });
        binding.texttermanddservice.setOnClickListener(v -> {
            startActivity(new Intent(Setting.this, termandservice.class));
        });
        binding.btnCountryFrmUser.setOnClickListener(v -> {
            startActivity(new Intent(Setting.this, Activity_country.class));
        });
    }
}