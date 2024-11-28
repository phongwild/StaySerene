package phongtaph31865.poly.stayserene.Login_Register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import phongtaph31865.poly.stayserene.R;


public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                   if (!task.isSuccessful()) {
                       return;
                   }
                   String token = task.getResult();
                   Log.d("Token", token);
                   SharedPreferences preferences = getSharedPreferences("FCM_TOKEN", MODE_PRIVATE);
                   preferences.edit().putString("token", token).apply();
                });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Welcome.this, Loginscreen.class);
                startActivity(intent);
            }
        },3000);
    }
}