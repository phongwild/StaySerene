package phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_method);
        namepay1 = findViewById(R.id.namepay1);
        namecard2 = findViewById(R.id.namecard2);
        paytcheckin = findViewById(R.id.paytcheckin);
        cardpayment = findViewById(R.id.cardpayment);



        paytcheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_payment_method.this, Activity_order_room.class);
                intent.putExtra("Pay_in_check_out", namepay1.getText().toString());
                startActivity(intent);
            }
        });

        cardpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_payment_method.this, Activity_order_room.class);
                intent.putExtra("Card_Pay_Ment", namecard2.getText().toString());
                startActivity(intent);
            }
        });

    }
}