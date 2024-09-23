package phongtaph31865.poly.stayserene;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import phongtaph31865.poly.stayserene.Screen_admin.Activity_admin.MainActivity_admin;
import phongtaph31865.poly.stayserene.Screen_user.Activity.MainActivity_user;

@SuppressLint("MissingInflatedId")
public class Loginscreen extends AppCompatActivity {
    private LinearLayout btn_Login;
    private TextView btn_SignUp, btn_forgotPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginscreen);
        btn_Login = findViewById(R.id.btn_Login);
        btn_SignUp = findViewById(R.id.btn_register_login);
        btn_forgotPass = findViewById(R.id.btn_forgot_pass);
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loginscreen.this, Register.class));
            }
        });
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loginscreen.this, MainActivity_admin.class));
            }
        });
    }
}