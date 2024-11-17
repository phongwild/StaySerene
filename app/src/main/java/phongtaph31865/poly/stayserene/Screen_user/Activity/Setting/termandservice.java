package phongtaph31865.poly.stayserene.Screen_user.Activity.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import phongtaph31865.poly.stayserene.databinding.ActivityTermandserviceBinding;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Setting.Setting;

public class termandservice extends AppCompatActivity {
    private ActivityTermandserviceBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTermandserviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
