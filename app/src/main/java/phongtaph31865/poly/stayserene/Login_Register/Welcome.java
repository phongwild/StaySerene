package phongtaph31865.poly.stayserene.Login_Register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import phongtaph31865.poly.stayserene.R;


public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Welcome.this, Loginscreen.class);
                startActivity(intent);
//                Toast.makeText(Welcome.this, "fgfdghggf", Toast.LENGTH_SHORT).show();
            }
        },3000);
    }
}