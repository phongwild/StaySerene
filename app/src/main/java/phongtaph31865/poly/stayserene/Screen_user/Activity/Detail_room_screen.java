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
import phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom.Activity_order_room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail_room_screen extends AppCompatActivity {
    private ImageView img, btn_back;
    private LinearLayout btn_booking;
    private TextView tv_name, tv_price, tv_description, tv_location, tv_type, tv_floor, tv_status;
    private Intent bookingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_screen);

        initView();
        handleClick();
        setupRoomDetails();
    }

    private void initView() {
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
    }

    private void handleClick() {
        btn_back.setOnClickListener(v -> finish());
    }

    private void setupRoomDetails() {
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String imgUrl = intent.getStringExtra("img");
        String idRoom = intent.getStringExtra("IdRoom");
        String idTypeRoom = intent.getStringExtra("IdTypeRoom");
        int price = intent.getIntExtra("price", 0);
        String description = intent.getStringExtra("desc");
        int floor = intent.getIntExtra("floor", 0);
        int status = intent.getIntExtra("status", 0);
        int soPhong = intent.getIntExtra("numberroom", 0);

        // Log thông tin nếu cần debug
        Log.e("Room UID", uid != null ? uid : "null");

        // Thiết lập dữ liệu giao diện
        setRoomInfo(imgUrl, soPhong, price, description, floor, status);

        // Tạo intent cho trang đặt phòng
        bookingIntent = new Intent(Detail_room_screen.this, Activity_order_room.class);
        bookingIntent.putExtra("id_room", idRoom);
        bookingIntent.putExtra("img", imgUrl);

        // Lấy thông tin loại phòng
        fetchTypeRoomInfo(idTypeRoom);
        setupBookingAction(idTypeRoom);
    }

    private void setRoomInfo(String imgUrl, int soPhong, int price, String description, int floor, int status) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        tv_name.setText(String.valueOf(soPhong));
        Picasso.get().load(imgUrl).into(img);
        tv_price.setText(formatter.format(price));
        tv_description.setText(description);
        tv_floor.setText(String.valueOf(floor));
        tv_status.setText(status == 0 ? "Open" : "Close");
    }

    private void fetchTypeRoomInfo(String idTypeRoom) {
        Api_service.service.get_typeroom_byId(idTypeRoom).enqueue(new Callback<List<TypeRoom>>() {
            @Override
            public void onResponse(@NonNull Call<List<TypeRoom>> call, @NonNull Response<List<TypeRoom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TypeRoom typeRoom = response.body().get(0); // Lấy loại phòng đầu tiên
                    tv_type.setText(typeRoom.getTenLoaiPhong());
                    bookingIntent.putExtra("total", typeRoom.getGiaLoaiPhong());
                    fetchHotelLocation(typeRoom.getIdKhachSan());
                } else {
                    Log.e("Response error", "Failed to fetch type room info");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TypeRoom>> call, @NonNull Throwable throwable) {
                Log.e("Fetch TypeRoom Error", throwable.getMessage());
            }
        });
    }

    private void fetchHotelLocation(String hotelId) {
        Api_service.service.get_hotel_byId(hotelId).enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(@NonNull Call<List<Hotel>> call, @NonNull Response<List<Hotel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Hotel hotel = response.body().get(0); // Lấy khách sạn đầu tiên
                    tv_location.setText(hotel.getDiaChi());
                } else {
                    Log.e("Response error", "Failed to fetch hotel location");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Hotel>> call, @NonNull Throwable throwable) {
                Log.e("Fetch Hotel Error", throwable.getMessage());
            }
        });
    }

    private void setupBookingAction(String idTypeRoom) {
        btn_booking.setOnClickListener(v -> PopupDialog.getInstance(Detail_room_screen.this).standardDialogBuilder()
                .createStandardDialog()
                .setHeading("Booking")
                .setDescription(String.format("Would you prefer this type of room? A room will be selected randomly from this room type"))
                .setCancelable(false)
                .setPositiveButtonText("Yes")
                .setNegativeButtonText("No")
                .setPositiveButtonTextColor(R.color.white)
                .setIcon(R.drawable.ic_booking)
                .build(new StandardDialogActionListener() {
                    @Override
                    public void onPositiveButtonClicked(Dialog dialog) {
                        bookingIntent.putExtra("id_type_room", idTypeRoom);
                        dialog.dismiss();
                        startActivity(bookingIntent);
                    }

                    @Override
                    public void onNegativeButtonClicked(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).show());
    }
}
