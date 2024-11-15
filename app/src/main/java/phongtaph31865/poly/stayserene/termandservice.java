package phongtaph31865.poly.stayserene;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import phongtaph31865.poly.stayserene.Screen_user.Activity.Setting.Setting;

public class termandservice extends AppCompatActivity {
    private ImageView btn_back_termandservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_termandservice);

        // Initialize the back button ImageView
        btn_back_termandservice = findViewById(R.id.btn_back_termandservice);

        // Set click listener for the back button to navigate to the Settings screen
        btn_back_termandservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Setting activity
               finish();
            }
        });
    }
}
