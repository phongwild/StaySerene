package phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.saadahmedev.popupdialog.PopupDialog;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.MainActivity_user;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_order_room extends AppCompatActivity {
    private ImageView btn_back, btn_choose_payment;
    private TextView tv_name_hotel, tv_type_room, tv_number_room, tv_floor, tv_desc, tv_fullName, tv_phone, tv_total, tv_time_in, tv_time_out, tv_paymethod;
    private EditText ed_note;
    private RelativeLayout btn_time_in, btn_time_out;
    private CardView btn_booking;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_room);
        SharedPreferences payMethod = getSharedPreferences("payment_method", MODE_PRIVATE);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(Activity_order_room.this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(Activity_order_room.this);
        Log.e("idgg", String.valueOf(account.getId()));
        //Anh xa
        btn_back = findViewById(R.id.btn_back_order_room);
        tv_name_hotel = findViewById(R.id.tv_hotelName_order_room);
        tv_type_room = findViewById(R.id.tv_name_type_room_order_room);
        tv_number_room = findViewById(R.id.tv_number_room_order_room);
        tv_floor = findViewById(R.id.tv_floor_order_room);
        tv_desc = findViewById(R.id.tv_desc_order_room);
        tv_fullName = findViewById(R.id.tv_full_name_order_room);
        tv_phone = findViewById(R.id.tv_phone_number_order_room);
        tv_total = findViewById(R.id.tv_total_order_room);
        tv_paymethod = findViewById(R.id.tv_payment_method_order_room);
        tv_time_in = findViewById(R.id.tv_time_check_in_order_room);
        tv_time_out = findViewById(R.id.tv_time_check_out_order_room);
        btn_booking = findViewById(R.id.btn_booking_order_room);
        btn_time_in = findViewById(R.id.btn_time_check_in_order_room);
        btn_time_out = findViewById(R.id.btn_time_check_out_order_room);
        ed_note = findViewById(R.id.ed_note_order_room);
        btn_choose_payment = findViewById(R.id.img_choose_payment_method_order_room);
        //Code logic
        btn_choose_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payMethod.edit().clear();
                startActivity(new Intent(Activity_order_room.this, Activity_payment_method.class));
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_TimeIN();
            }
        });
        btn_time_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_TimeOUT();
            }
        });
        //Order room
        Intent intent = getIntent();
        String id_type_room = intent.getStringExtra("id_type_room");
        String id_room = intent.getStringExtra("id_room");
        String img = intent.getStringExtra("img");
        //Get payment method
        String pay_at_checkIn = payMethod.getString("pay_checkin", "");
        String cardPayment = payMethod.getString("pay_card", "");
        Log.e("getPayMethod", pay_at_checkIn + " " + cardPayment);
        if (pay_at_checkIn != null) {
            tv_paymethod.setText(pay_at_checkIn);
        }else Log.e("getPayMethod", "pay_at_checkIn is null");
        if (cardPayment != null) {
            tv_paymethod.setText(cardPayment);
        }else Log.e("getPayMethod", "cardPayment is null");
        if (getUsernameFromSharedPreferences() != null) {
            Api_service.service.get_account_byId(getUsernameFromSharedPreferences()).enqueue(new Callback<List<Account>>() {
                @Override
                public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                    if (response.isSuccessful()) {
                        List<Account> accounts = response.body();
                        Account account = accounts.get(0);
                        tv_fullName.setText(account.getUsername());
                        tv_phone.setText(account.getSdt());
                    } else Log.e("Failure get account", response.message());
                }

                @Override
                public void onFailure(Call<List<Account>> call, Throwable throwable) {
                    Log.e("Failure get account", throwable.getMessage());
                }
            });
        } else if (getUserGoogleFromSharedPreferences() != null) {
            tv_fullName.setText(getUserGoogleFromSharedPreferences());
        }
        if (id_type_room != null) {
            Api_service.service.get_rooms_byId_typeRoom(id_type_room).enqueue(new Callback<List<Room>>() {
                @Override
                public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                    if (response.isSuccessful()) {
                        List<Room> rooms = response.body();
                        Room room = rooms.get(0);
                        tv_number_room.setText(String.valueOf(room.getSoPhong()));
                        tv_floor.setText(String.valueOf(room.getSoTang()));
                    } else Log.e("Failure get room", response.message());
                }

                @Override
                public void onFailure(Call<List<Room>> call, Throwable throwable) {
                    Log.e("Failure get room", throwable.getMessage());
                }
            });
            Api_service.service.get_typeroom_byId(id_type_room).enqueue(new Callback<List<TypeRoom>>() {
                @Override
                public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                    if (response.isSuccessful()) {
                        List<TypeRoom> typeRooms = response.body();
                        TypeRoom typeRoom = typeRooms.get(0);
                        tv_type_room.setText(typeRoom.getTenLoaiPhong());
                        tv_desc.setText(typeRoom.getMoTaLoaiPhong());
                        Api_service.service.get_hotel_byId(typeRoom.getIdKhachSan()).enqueue(new Callback<List<Hotel>>() {
                            @Override
                            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                                if (response.isSuccessful()) {
                                    List<Hotel> hotels = response.body();
                                    Hotel hotel = hotels.get(0);
                                    tv_name_hotel.setText(hotel.getTenKhachSan());
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                                Log.e("Failure get hotel", throwable.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                    Log.e("Failure get type room", throwable.getMessage());
                }
            });
        }
        if (id_room != null) {
            Api_service.service.get_rooms_byId(id_room).enqueue(new Callback<List<Room>>() {
                @Override
                public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                    if (response.isSuccessful()) {
                        List<Room> rooms = response.body();
                        Room room = rooms.get(0);
                        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        tv_total.setText(formatter.format(room.getGiaPhong()));
                    }
                }

                @Override
                public void onFailure(Call<List<Room>> call, Throwable throwable) {
                    Log.e("Failure get room", throwable.getMessage());
                }
            });
        }
        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeIn = tv_time_in.getText().toString();
                String timeOut = tv_time_out.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date dateIn = dateFormat.parse(timeIn);
                    Date dateOut = dateFormat.parse(timeOut);
                    if (dateOut.before(dateIn)) {
                        Toast.makeText(Activity_order_room.this, "Time check out must be after time check in", Toast.LENGTH_SHORT).show();
                    }else {
                        LocalDateTime now = LocalDateTime.now();
                        String date = now.getHour() + ":" + now.getMinute() + "-" + now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear();
                        Order_Room orderRoom = new Order_Room();
                        orderRoom.setOrderTime(date);
                        orderRoom.setNote(ed_note.getText().toString());
                        orderRoom.setImg(img);
                        orderRoom.setTimeGet(tv_time_in.getText().toString());
                        orderRoom.setTimeCheckout(tv_time_out.getText().toString());
                        if (getUsernameFromSharedPreferences() != null) {
                            orderRoom.setUid(getUsernameFromSharedPreferences());
                        }
                        Api_service.service.get_rooms_byId(id_room).enqueue(new Callback<List<Room>>() {
                            @Override
                            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                                if (response.isSuccessful()) {
                                    List<Room> rooms = response.body();
                                    Room room = rooms.get(0);
                                    orderRoom.setIdPhong(room.get_id());
                                    orderRoom.setTotal(room.getGiaPhong());
                                    room.setTinhTrangPhong(1);
                                    Api_service.service.order_room(orderRoom).enqueue(new Callback<List<Room>>() {
                                        @Override
                                        public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                                            if (response.isSuccessful()) {
                                                Api_service.service.update_rooms(id_room, room).enqueue(new Callback<List<Room>>() {
                                                    @Override
                                                    public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                                                        if (response.isSuccessful()) {
                                                            payMethod.edit().clear();
                                                            PopupDialog.getInstance(Activity_order_room.this)
                                                                    .statusDialogBuilder()
                                                                    .createSuccessDialog()
                                                                    .setHeading("Well Done")
                                                                    .setDescription("Your booking is complete!")
                                                                    .build(dialog1 -> startActivity(new Intent(Activity_order_room.this, MainActivity_user.class)))
                                                                    .show();
                                                        } else {
                                                            PopupDialog.getInstance(Activity_order_room.this)
                                                                    .statusDialogBuilder()
                                                                    .createErrorDialog()
                                                                    .setHeading("Uh-Oh")
                                                                    .setDescription("Unexpected error occurred." +
                                                                            " Try again later.")
                                                                    .build(Dialog::dismiss)
                                                                    .show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<Room>> call, Throwable throwable) {
                                                        Log.e("Failure update room", throwable.getMessage());
                                                    }
                                                });
                                            } else {
                                                PopupDialog.getInstance(Activity_order_room.this)
                                                        .statusDialogBuilder()
                                                        .createErrorDialog()
                                                        .setHeading("Uh-Oh")
                                                        .setDescription("Unexpected error occurred." +
                                                                " Try again later.")
                                                        .build(Dialog::dismiss)
                                                        .show();
                                                Log.e("Failure order room", response.message());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<Room>> call, Throwable throwable) {
                                            Log.e("Failure order room", throwable.getMessage());
                                        }
                                    });
                                } else {
                                    Log.e("Failure getRoomById", response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                                Log.e("Failure getRoomById", throwable.getMessage());
                            }
                        });
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(Activity_order_room.this, "Please choose time check in and time check out", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private String getUserGoogleFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_google", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", "");
    }

    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }

    private void choose_TimeIN() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                tv_time_in.setText(formattedDate);
            }
        }, year, month, day);
        dialog.show();
    }

    private void choose_TimeOUT() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                tv_time_out.setText(formattedDate);
            }
        }, year, month, day);
        dialog.show();
    }
}