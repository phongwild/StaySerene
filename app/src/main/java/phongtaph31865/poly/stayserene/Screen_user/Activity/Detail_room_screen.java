package phongtaph31865.poly.stayserene.Screen_user.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import phongtaph31865.poly.stayserene.R;

public class Detail_room_screen extends AppCompatActivity {
    private ImageView img, btn_back;
    private LinearLayout btn_booking;
    private TextView tv_name, tv_price, tv_description, tv_location, tv_type, tv_floor, tv_status;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_screen);
        img = findViewById(R.id.img_room_detail);
        tv_name = findViewById(R.id.tv_room_name_detail);
        tv_price = findViewById(R.id.tv_room_price_detail);
        tv_description = findViewById(R.id.tv_room_description_detail);
        tv_location = findViewById(R.id.tv_room_location_detail);
        btn_back = findViewById(R.id.btn_back_detail);
        tv_type = findViewById(R.id.tv_room_type_detail);
        tv_floor = findViewById(R.id.tv_room_floor_detail);
        tv_status = findViewById(R.id.tv_room_status_detail);
        btn_booking = findViewById(R.id.btn_room_booking_detail);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Detail_room_screen.this, MainActivity_user.class));
                finish();
            }
        });
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        if(uid != null) Log.e("save", uid);
        else Log.e("save", "null");
        String img = intent.getStringExtra("img");
        String IdRoom = intent.getStringExtra("IdRoom");
        String IdTypeRoom = intent.getStringExtra("IdTypeRoom");
        int price = intent.getIntExtra("price",0);
        String description = intent.getStringExtra("desc");
        int floor = intent.getIntExtra("floor", 0);
        int status = intent.getIntExtra("status", 0);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tv_name.setText(IdRoom);
        Picasso.get().load(img).into(this.img);
        tv_price.setText(formatter.format(price));
        tv_description.setText(description);
        tv_floor.setText(String.valueOf(floor));
        if(status == 0){
            tv_status.setText("Open");
        }else if(status == 1){
            tv_status.setText("Close");
        }
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("LoaiPhong");
            ref.orderByChild("IdLoaiPhong").equalTo(IdTypeRoom).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String type = dataSnapshot.child("tenLoaiPhong").getValue(String.class);
                            tv_type.setText(type);
                            String ID_hotel = dataSnapshot.child("IdHotel").getValue(String.class);
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("KhachSan");
                            reference.orderByChild("IdHotel").equalTo(ID_hotel).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String location = dataSnapshot.child("diaChi").getValue(String.class);
                                            tv_location.setText(location);
                                        }
                                    }else{
                                        Log.e("Detail_Error", "Data not found");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("Detail_Error", error.getMessage());
                                }
                            });
                        }
                    }else{
                        Log.e("Detail_Error", "Data not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Detail_Error", error.getMessage());
                }
            });
        }catch (Exception e){
            Log.e("Detail_Error", e.getMessage());
        }
        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}