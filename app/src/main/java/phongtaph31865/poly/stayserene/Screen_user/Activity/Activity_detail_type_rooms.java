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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_detail_type_rooms extends AppCompatActivity {
    private ImageView btn_back_detail_type_room, img_detail_type_room;
    private TextView tv_name_detail_type_room, tv_name, tv_location_detail_type_room, tv_price_detail_type_room, tv_so_luong_phong_detail_type_room, tv_tienNghi_detail_type_room, tv_dienTich_detail_type_room, tv_description_detail_type_room;
    private LinearLayout btn_booking;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_type_rooms);
        Intent intent = getIntent();
        String id_type_room = intent.getStringExtra("id_type_room");
        btn_back_detail_type_room = findViewById(R.id.btn_back_detail_type_room);
        img_detail_type_room = findViewById(R.id.img_detail_type_room);
        tv_name_detail_type_room = findViewById(R.id.tv_name_detail_type_room);
        tv_location_detail_type_room = findViewById(R.id.tv_location_detail_type_room);
        tv_price_detail_type_room = findViewById(R.id.tv_price_detail_type_room);
        tv_so_luong_phong_detail_type_room = findViewById(R.id.tv_so_luong_phong_detail_type_room);
        tv_tienNghi_detail_type_room = findViewById(R.id.tv_tienNghi_detail_type_room);
        tv_dienTich_detail_type_room = findViewById(R.id.tv_dienTich_detail_type_room);
        tv_description_detail_type_room = findViewById(R.id.tv_description_detail_type_room);
        tv_name = findViewById(R.id.tv_name2_detail_type_room);
        btn_booking = findViewById(R.id.btn_booking_detail_type_room);
        btn_back_detail_type_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (id_type_room != null) {
            Api_service.service.get_typeroom().enqueue(new Callback<List<TypeRoom>>() {
                @Override
                public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                    if (response.isSuccessful()) {
                        for (TypeRoom typeroom : response.body()) {
                            if (typeroom.get_id().equals(id_type_room)) {
                                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                                tv_tienNghi_detail_type_room.setText(String.valueOf(typeroom.getTienNghi()));
                                tv_so_luong_phong_detail_type_room.setText(String.valueOf(typeroom.getSoLuongPhong()));
                                tv_description_detail_type_room.setText(typeroom.getMoTaLoaiPhong());
                                double price = typeroom.getGiaLoaiPhong();
                                tv_price_detail_type_room.setText(formatter.format(price));
                                Picasso.get().load(typeroom.getAnhLoaiPhong()).into(img_detail_type_room);
                                tv_name.setText(typeroom.getTenLoaiPhong());
                                String idHotel = typeroom.getIdKhachSan();
                                Api_service.service.get_hotel().enqueue(new Callback<List<Hotel>>() {
                                    @Override
                                    public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                                        if (response.isSuccessful()){
                                            for(Hotel hotel: response.body()){
                                                if (hotel.get_id().equals(idHotel)){
                                                    tv_location_detail_type_room.setText(hotel.getDiaChi());

                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<Hotel>> call, Throwable throwable) {

                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                    Log.e("dsfs", throwable.getMessage());
                }
            });
        }

    }
}