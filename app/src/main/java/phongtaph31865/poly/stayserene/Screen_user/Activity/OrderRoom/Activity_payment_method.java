package phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import phongtaph31865.poly.stayserene.R;

public class Activity_payment_method extends AppCompatActivity {
    private LinearLayout paytcheckin, cardpayment;
    private TextView namepay1,namecard2;
    private ImageView btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_method);
        namepay1 = findViewById(R.id.namepay1);
        namecard2 = findViewById(R.id.namecard2);
        paytcheckin = findViewById(R.id.paytcheckin);
        cardpayment = findViewById(R.id.cardpayment);
        btn_back = findViewById(R.id.btn_back_order_room);
        paytcheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("payment_method", MODE_PRIVATE);
                preferences.edit().putString("pay", "Pay at check in").apply();
                finish();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cardpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("payment_method", MODE_PRIVATE);
                preferences.edit().putString("pay", "Card payment").apply();
                finish();
            }
        });

    }
}