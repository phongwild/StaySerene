package phongtaph31865.poly.stayserene.Screen_user.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom.Activity_detail_type_rooms;
import phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom.Activity_order_room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        int soPhong = intent.getIntExtra("numberroom", 0);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tv_name.setText(String.valueOf(soPhong));
        Picasso.get().load(img).into(this.img);
        tv_price.setText(formatter.format(price));
        tv_description.setText(description);
        tv_floor.setText(String.valueOf(floor));
        if(status == 0){
            tv_status.setText("Open");
        }else if(status == 1){
            tv_status.setText("Close");
        }
        Intent intent1 = new Intent(Detail_room_screen.this, Activity_order_room.class);
        getTypeRoom(IdTypeRoom, intent1);
        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupDialog.getInstance(Detail_room_screen.this).standardDialogBuilder().createStandardDialog()
                        .setHeading("Booking")
                        .setDescription("Would you prefer this type of room?" + "A room will be selected randomly from this room type")
                        .setCancelable(false)
                        .setPositiveButtonText("Yes")
                        .setNegativeButtonText("No")
                        .setPositiveButtonTextColor(R.color.white)
                        .setIcon(R.drawable.ic_booking)
                        .build(new StandardDialogActionListener() {
                            @Override
                            public void onPositiveButtonClicked(Dialog dialog) {
                                intent1.putExtra("id_type_room", IdTypeRoom);
                                intent1.putExtra("id_room", IdRoom);
                                intent1.putExtra("img", img);
                                dialog.dismiss();
                                startActivity(intent1);
                            }

                            @Override
                            public void onNegativeButtonClicked(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();

            }
        });
    }
    private void getTypeRoom(String id, Intent intent1){
        Api_service.service.get_typeroom_byId(id).enqueue(new Callback<List<TypeRoom>>() {
            @Override
            public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                if (response.isSuccessful()){
                    for (TypeRoom typeRoom : response.body()){
                        tv_type.setText(typeRoom.getTenLoaiPhong());
                        String idHT = typeRoom.getIdKhachSan();
                        intent1.putExtra("total", typeRoom.getGiaLoaiPhong());
                        getLocation(idHT);
                    }
                }else Log.e("Response error", "Response is not successful");
            }

            @Override
            public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                Log.e("Failure getTypeRoom", throwable.getMessage());
            }
        });
    }
    private void getLocation(String id){
        Api_service.service.get_hotel_byId(id).enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful()){
                    for (Hotel hotel : response.body()){
                        tv_location.setText(hotel.getDiaChi());
                    }

                }else Log.e("Response error", response.message());
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                Log.e("Failure getLocation", throwable.getMessage());
            }
        });
    }
}