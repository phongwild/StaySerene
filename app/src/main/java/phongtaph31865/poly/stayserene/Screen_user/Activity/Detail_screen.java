package phongtaph31865.poly.stayserene.Screen_user.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import phongtaph31865.poly.stayserene.R;

public class Detail_screen extends AppCompatActivity {
    private ImageView img, btn_back;
    private LinearLayout btn_booking;
    private TextView tv_name, tv_price, tv_description, tv_location, tv_rating;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_screen);
        img = findViewById(R.id.img_hotel_detail);
        tv_name = findViewById(R.id.tv_hotel_name_detail);
        tv_price = findViewById(R.id.tv_hotel_price_detail);
        tv_description = findViewById(R.id.tv_hotel_description_detail);
        tv_location = findViewById(R.id.tv_location_detail);
        tv_rating = findViewById(R.id.tv_rating_detail);
        btn_back = findViewById(R.id.btn_back_detail);
        btn_booking = findViewById(R.id.btn_booking_detail);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Detail_screen.this, MainActivity_user.class));
                finish();
            }
        });
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String img = intent.getStringExtra("img");
        String name = intent.getStringExtra("name");
        String rate = intent.getStringExtra("rate");
        String location = intent.getStringExtra("address");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("desc");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        int price_vnd = Integer.parseInt(price);
        float rating = Float.parseFloat(rate);
        if(img != null && name != null && rate != null && location != null && price != null && description != null){
            Picasso.get().load(img).into(this.img);
            tv_name.setText(name);
            tv_rating.setText(String.valueOf(rating));
            tv_location.setText(location);
            tv_price.setText(formatter.format(price_vnd));
            tv_description.setText(description);
            Log.d("Detail_screen", uid + img + name + rate + location + price + description);
        }else{
            Log.d("Detail_screen", "Error");
        }
        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}