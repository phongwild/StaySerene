package phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom;

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
import androidx.appcompat.app.AppCompatActivity;

import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.Room;
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

        String id_type_room = getIntent().getStringExtra("id_type_room");
        initView();

        btn_back_detail_type_room.setOnClickListener(v -> finish());
        btn_booking.setOnClickListener(v -> showBookingDialog(id_type_room));

        if (id_type_room != null) {
            loadTypeRoomDetails(id_type_room);
        }
    }

    private void initView() {
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
    }

    private void showBookingDialog(String id_type_room) {
        PopupDialog.getInstance(this).standardDialogBuilder()
                .createStandardDialog()
                .setHeading("Booking")
                .setDescription("Would you prefer this type of room? A room will be selected randomly from this room type.")
                .setCancelable(false)
                .setPositiveButtonText("Yes")
                .setNegativeButtonText("No")
                .setPositiveButtonTextColor(R.color.white)
                .setIcon(R.drawable.ic_booking)
                .build(new StandardDialogActionListener() {
                    @Override
                    public void onPositiveButtonClicked(Dialog dialog) {
                        fetchAvailableRoom(id_type_room, dialog);
                    }
                    @Override
                    public void onNegativeButtonClicked(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void fetchAvailableRoom(String id_type_room, Dialog dialog) {
        Api_service.service.get_rooms_byId_typeRoom(id_type_room).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful()) {
                    List<Room> rooms = response.body();
                    Optional<Room> availableRoom = rooms.stream()
                            .filter(room -> room.getTinhTrangPhong() == 0)
                            .findFirst();

                    if (availableRoom.isPresent()) {
                        Room room = availableRoom.get();
                        Intent intent = new Intent(Activity_detail_type_rooms.this, Activity_order_room.class);
                        intent.putExtra("id_type_room", id_type_room);
                        intent.putExtra("id_room", room.get_id());
                        intent.putExtra("img", room.getAnhPhong());
                        intent.putExtra("total", room.getGiaPhong());
                        startActivity(intent);
                    } else {
                        showFullyBookedDialog();
                    }
                } else {
                    Log.e("Response error", "Response is not successful");
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("Failure getRoomByIdTypeRoom", throwable.getMessage());
            }
        });
    }

    private void showFullyBookedDialog() {
        PopupDialog.getInstance(this)
                .statusDialogBuilder()
                .createWarningDialog()
                .setHeading("Room type is fully booked!")
                .setActionButtonText("Okay")
                .setDescription("This room type is fully booked. Please select a different one.")
                .build(Dialog::dismiss)
                .show();
    }

    private void loadTypeRoomDetails(String id_type_room) {
        Api_service.service.get_typeroom().enqueue(new Callback<List<TypeRoom>>() {
            @Override
            public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                if (response.isSuccessful()) {
                    response.body().stream()
                            .filter(typeroom -> typeroom.get_id().equals(id_type_room))
                            .findFirst()
                            .ifPresent(typeroom -> {
                                displayTypeRoomDetails(typeroom);
                                loadRoomAvailability(typeroom.get_id());
                                loadHotelLocation(typeroom.getIdKhachSan());
                            });
                }
            }

            @Override
            public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                Log.e("Failure getTypeRoom", throwable.getMessage());
            }
        });
    }

    private void displayTypeRoomDetails(TypeRoom typeroom) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tv_tienNghi_detail_type_room.setText(String.valueOf(typeroom.getTienNghi()));
        tv_name_detail_type_room.setText(typeroom.getTenLoaiPhong());
        tv_description_detail_type_room.setText(typeroom.getMoTaLoaiPhong());
        tv_price_detail_type_room.setText(formatter.format(typeroom.getGiaLoaiPhong()));
        Picasso.get().load(typeroom.getAnhLoaiPhong()).into(img_detail_type_room);
        tv_name.setText(typeroom.getTenLoaiPhong());
    }

    private void loadRoomAvailability(String id_type_room) {
        Api_service.service.get_rooms_byId_typeRoom(id_type_room).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful()) {
                    long availableCount = response.body().stream()
                            .filter(room -> room.getTinhTrangPhong() == 0)
                            .count();
                    tv_so_luong_phong_detail_type_room.setText(String.valueOf(availableCount));
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("Failure getRoomByIdTypeRoom", throwable.getMessage());
            }
        });
    }

    private void loadHotelLocation(String idHotel) {
        Api_service.service.get_hotel().enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful()) {
                    response.body().stream()
                            .filter(hotel -> hotel.get_id().equals(idHotel))
                            .findFirst()
                            .ifPresent(hotel -> tv_location_detail_type_room.setText(hotel.getDiaChi()));
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                Log.e("Failure getHotel", throwable.getMessage());
            }
        });
    }
}
