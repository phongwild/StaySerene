package phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.databinding.ActivityShowDetailBookingBinding;

import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_show_detail_booking extends AppCompatActivity {
    private ActivityShowDetailBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityShowDetailBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String sdt = sharedPreferences.getString("sdt", "");
        binding.tvFullNameDetailBooking.setText(username);
        binding.tvPhoneNumberDetailBooking.setText(sdt);
        if (intent != null) {
            String name_hotel = intent.getStringExtra("name_hotel");
            String id_room = intent.getStringExtra("id_room");
            String time_checkin = intent.getStringExtra("time_checkin");
            String time_checkout = intent.getStringExtra("time_checkout");
            Float total = intent.getFloatExtra("total", 0);
            String img = intent.getStringExtra("img");
            String note = intent.getStringExtra("note");
            String price = formatter.format(total);
            Log.d("GetIntent", "onCreate: " + name_hotel + " " + id_room + " " + time_checkin + " " + time_checkout + " " + total + " " + img + " " + note);
            binding.tvHotelNameDetailBooking.setText(name_hotel);
            binding.tvTimeCheckinDetailBooking.setText(time_checkin);
            binding.tvTimeCheckoutDetailBooking.setText(time_checkout);
            binding.tvTotalDetailBooking.setText(price);
            Picasso.get().load(img).into(binding.imgDetailBooking);
            binding.tvNoteOrderRoom.setText(note);
            Api_service.service.get_rooms_byId(id_room).enqueue(new Callback<List<Room>>() {
                @Override
                public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                    if (response.isSuccessful()) {
                        List<Room> rooms = response.body();
                        Room room = rooms.get(0);
                        binding.tvNumberRoomDetailBooking.setText(String.valueOf(room.getSoPhong()));
                        binding.tvFloorDetailBooking.setText(String.valueOf(room.getSoTang()));
                        String id_type_room = room.getIdLoaiPhong();
                        Api_service.service.get_typeroom_byId(id_type_room).enqueue(new Callback<List<TypeRoom>>() {
                            @Override
                            public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                                if (response.isSuccessful()) {
                                    List<TypeRoom> typeRooms = response.body();
                                    TypeRoom typeRoom = typeRooms.get(0);
                                    binding.tvNameTypeRoomDetailBooking.setText(typeRoom.getTenLoaiPhong());
                                    binding.tvDescDetailBooking.setText(typeRoom.getMoTaLoaiPhong());
                                    Api_service.service.get_hotel_byId(typeRoom.getIdKhachSan()).enqueue(new Callback<List<Hotel>>() {
                                        @Override
                                        public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                                            if (response.isSuccessful()) {
                                                List<Hotel> hotels = response.body();
                                                Hotel hotel = hotels.get(0);
                                                binding.tvAddressDetailBooking.setText(hotel.getDiaChi());
                                                binding.tvHotelPhoneDetailBooking.setText(hotel.getSdt());
                                                binding.tvEmailDetailBooking.setText(hotel.getEmail());
                                                binding.tvDescHotelDetailBooking.setText(hotel.getMoTaKhachSan());

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                                            Log.e("Failure get hotel by id", throwable.getMessage());
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                                Log.e("Failure get type room by id", throwable.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<Room>> call, Throwable throwable) {
                    Log.e("Failure get room by id", throwable.getMessage());
                }
            });
        }else Log.e("GetIntent", "onCreate: intent is null");
        binding.btnBackDetailBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}