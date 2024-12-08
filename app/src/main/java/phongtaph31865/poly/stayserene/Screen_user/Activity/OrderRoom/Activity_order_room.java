package phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.saadahmedev.popupdialog.PopupDialog;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import phongtaph31865.poly.stayserene.Api.CreateOrder;
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
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class Activity_order_room extends AppCompatActivity {
    private ImageView btn_back, btn_service;
    private TextView tv_name_hotel, tv_type_room, tv_number_room, tv_floor, tv_desc, tv_fullName, tv_phone, tv_total, tv_time_in, tv_time_out, tv_id_service, tv_price_service, tv_service;
    private EditText ed_note;
    private RelativeLayout btn_time_in, btn_time_out;
    private CardView btn_booking;
    private String ID_ROOM, ID_TYPE_ROOM;
    private int TIME_LEFT = 0;
    private Timer timer;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_room);
        //Anh xa
        initView();

        //Đếm ngược 10p đặt phòng
        countDownTimer();

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        //Xử lí click
        handleClick();

    }

    private void initView() {
        btn_back = findViewById(R.id.btn_back_order_room);
        tv_name_hotel = findViewById(R.id.tv_hotelName_order_room);
        tv_type_room = findViewById(R.id.tv_name_type_room_order_room);
        tv_number_room = findViewById(R.id.tv_number_room_order_room);
        tv_floor = findViewById(R.id.tv_floor_order_room);
        tv_desc = findViewById(R.id.tv_desc_order_room);
        tv_fullName = findViewById(R.id.tv_full_name_order_room);
        tv_phone = findViewById(R.id.tv_phone_number_order_room);
        tv_total = findViewById(R.id.tv_total_order_room);
        tv_time_in = findViewById(R.id.tv_time_check_in_order_room);
        tv_time_out = findViewById(R.id.tv_time_check_out_order_room);
        tv_id_service = findViewById(R.id.tv_id_service);
        tv_price_service = findViewById(R.id.tv_price_service);
        btn_booking = findViewById(R.id.btn_booking_order_room);
        btn_time_in = findViewById(R.id.btn_time_check_in_order_room);
        btn_time_out = findViewById(R.id.btn_time_check_out_order_room);
        ed_note = findViewById(R.id.ed_note_order_room);
        btn_service = findViewById(R.id.img_choose_service_order_room);
        tv_service = findViewById(R.id.tv_service_order_room);

        Intent intent = getIntent();
        ID_TYPE_ROOM = intent.getStringExtra("id_type_room");
        ID_ROOM = intent.getStringExtra("id_room");
        tv_service.setText("No service required");
        tv_id_service.setText("6707ed79df6d7c9585d8eb99");
        tv_price_service.setText("0");
    }
    private void handleClick(){
        btn_service.setOnClickListener(v -> {
            Intent intent1 = new Intent(Activity_order_room.this, Activity_list_service.class);
            startActivityForResult(intent1, 1);
        });
        btn_back.setOnClickListener(v -> {
            getRoomById();
            timer.cancel();
            finish();
        });
        btn_time_in.setOnClickListener(v -> choose_TimeIN(ID_ROOM));
        btn_time_out.setOnClickListener(v -> choose_TimeOUT(ID_ROOM));
        tv_price_service.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateTotalPrice(ID_ROOM);
            }
        });
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
        if (ID_TYPE_ROOM != null) {
            Api_service.service.get_rooms_byId_typeRoom(ID_TYPE_ROOM).enqueue(new Callback<List<Room>>() {
                @Override
                public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                    if (response.isSuccessful()) {
                        List<Room> rooms = response.body();
                        Room room = rooms.get(0);
                        tv_number_room.setText(String.valueOf(room.getSoPhong()));
                        tv_floor.setText(String.valueOf(room.getSoTang()));
                        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        tv_total.setText(formatter.format(room.getGiaPhong()));
                    } else Log.e("Failure get room", response.message());
                }

                @Override
                public void onFailure(Call<List<Room>> call, Throwable throwable) {
                    Log.e("Failure get room", throwable.getMessage());
                }
            });
            Api_service.service.get_typeroom_byId(ID_TYPE_ROOM).enqueue(new Callback<List<TypeRoom>>() {
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
                                } else Log.e("Failure get hotel", response.message());
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
        if (ID_ROOM != null) {
            Api_service.service.get_rooms_byId(ID_ROOM).enqueue(new Callback<List<Room>>() {
                @Override
                public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                    if (response.isSuccessful()) {
                        List<Room> rooms = response.body();
                        Room room = rooms.get(0);
                        tv_number_room.setText(String.valueOf(room.getSoPhong()));
                        tv_floor.setText(String.valueOf(room.getSoTang()));
                    }
                }

                @Override
                public void onFailure(Call<List<Room>> call, Throwable throwable) {
                    Log.e("Failure get room", throwable.getMessage());
                }
            });
        }
        btn_booking.setOnClickListener(v -> {
            checkIdCard(isIdCard -> {
                if (isIdCard) {

                    Toast.makeText(Activity_order_room.this, "You should add an ID card before booking a room.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String timeIn = tv_time_in.getText().toString();
                String timeOut = tv_time_out.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date dateIn = dateFormat.parse(timeIn.split(" ")[1]);
                    Date dateOut = dateFormat.parse(timeOut.split(" ")[1]);
                    LocalDateTime now = LocalDateTime.now();
                    String date = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond() + " " + now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear();
                    Date dateOrder = dateFormat.parse(date.split(" ")[1]);

                    if (dateIn.before(dateOrder)){
                        Toast.makeText(Activity_order_room.this, "Time check in must be after time order", Toast.LENGTH_SHORT).show();
                    }else if (dateOut.before(dateIn)) {
                        Toast.makeText(Activity_order_room.this, "Time check out must be after time check in", Toast.LENGTH_SHORT).show();
                    } else {
                        long diffInMillis = dateOut.getTime() - dateIn.getTime();
                        double roundedNumDays = (double) diffInMillis / (1000 * 60 * 60 * 24);
                        int numDays = (int) Math.ceil(roundedNumDays);
                        Order_Room orderRoom = new Order_Room();
                        orderRoom.setOrderTime(date);
                        orderRoom.setNote(ed_note.getText().toString());
                        orderRoom.setTimeGet(tv_time_in.getText().toString());
                        orderRoom.setTimeCheckout(tv_time_out.getText().toString());
                        orderRoom.setIdDichVu(tv_id_service.getText().toString());
                        if (getUsernameFromSharedPreferences() != null) {
                            orderRoom.setUid(getUsernameFromSharedPreferences());
                        }
                        Api_service.service.get_rooms_byId(ID_ROOM).enqueue(new Callback<List<Room>>() {
                            @Override
                            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                                if (response.isSuccessful()) {
                                    List<Room> rooms = response.body();
                                    Room room = rooms.get(0);
                                    String priceString = tv_price_service.getText().toString();
                                    priceString = priceString.replaceAll("[^\\d.]", "");
                                    float priceService = Float.parseFloat(priceString);
                                    float total = room.getGiaPhong() * numDays + priceService;
                                    orderRoom.setImg(room.getAnhPhong());
                                    orderRoom.setIdPhong(room.get_id());
                                    orderRoom.setTotal(total);
                                    orderRoom.setUserTokenFCM(userTokenFCM());
                                    room.setTinhTrangPhong(1);
                                    cardPayment(orderRoom, room, total);
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
            });
        });
    }
    private void cardPayment(Order_Room orderRoom, Room room, float total) {
        orderRoom.setStatus(0);
        CreateOrder orderApi = new CreateOrder();
        try {
            float totalPay = total * 0.1f;
            String totalPayString = String.format("%.0f", totalPay);
            JSONObject data = orderApi.createOrder(totalPayString);
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(Activity_order_room.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        Api_service.service.order_room(orderRoom).enqueue(new Callback<List<Room>>() {
                            @Override
                            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                                if (response.isSuccessful()) {
                                    Api_service.service.update_rooms(ID_ROOM, room).enqueue(new Callback<List<Room>>() {
                                        @SuppressLint("CommitPrefEdits")
                                        @Override
                                        public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                                            if (response.isSuccessful()) {
                                                PopupDialog popupDialog = PopupDialog.getInstance(Activity_order_room.this)
                                                        .statusDialogBuilder()
                                                        .createSuccessDialog()
                                                        .setHeading("Well Done")
                                                        .setDescription("Your booking is complete!")
                                                        .build(dialog1 -> {
                                                            timer.cancel();
                                                            startActivity(new Intent(Activity_order_room.this, MainActivity_user.class));
                                                        });
                                                popupDialog.getDialog().setOnDismissListener(dialog -> {
                                                    startActivity(new Intent(Activity_order_room.this, MainActivity_user.class));
                                                });
                                                popupDialog.getDialog().setCancelable(true);
                                                popupDialog.show();

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
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        PopupDialog.getInstance(Activity_order_room.this)
                                .statusDialogBuilder()
                                .createErrorDialog()
                                .setHeading("Payment Canceled")
                                .setDescription("Please make the payment again")
                                .build(Dialog::dismiss)
                                .show();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        PopupDialog.getInstance(Activity_order_room.this)
                                .statusDialogBuilder()
                                .createErrorDialog()
                                .setHeading("Payment Error")
                                .setDescription("The payment is encountering an issue.")
                                .build(Dialog::dismiss)
                                .show();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void countDownTimer() {
        if (timer != null) {
            timer.cancel(); // Cancel any existing timer
        }

        TIME_LEFT = 600;  // Reset time
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (TIME_LEFT > 0) {
                    TIME_LEFT--;
                    Log.e("TIME_LEFT", String.valueOf(TIME_LEFT));
                } else {
                    timer.cancel();  // Stop the timer once time is up
                    getRoomById();
                    startActivity(new Intent(Activity_order_room.this, MainActivity_user.class));
                }
            }
        };
        timer.schedule(task, 0, 1000);  // Run every second
    }
    private void getRoomById(){
        Api_service.service.get_rooms_byId(ID_ROOM).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful()) {
                    List<Room> rooms = response.body();
                    Room room = rooms.get(0);
                    room.setTinhTrangPhong(0);
                    updateStatusRoom(room);
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("Failure get room", throwable.getMessage());
            }
        });
    }
    private void updateStatusRoom(Room room) {
        Api_service.service.update_rooms(ID_ROOM, room).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Activity_order_room.this, "End time", Toast.LENGTH_SHORT).show();
                }else Log.e("Failure update room", response.message());
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("Failure update room", throwable.getMessage());
            }
        });
    }
    private void checkIdCard(final CheckIdCardCallback callback) {
        Api_service.service.get_account_byId(getUsernameFromSharedPreferences()).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                boolean isIdCard = false;
                if (response.isSuccessful() && response.body() != null) {
                    for (Account account : response.body()) {
                        if (account.getCccd().isEmpty()) {
                            isIdCard = true;
                            break;
                        }
                    }
                }
                callback.onResult(isIdCard);
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e("Failure get account", throwable.getMessage());
                callback.onResult(false);
            }
        });
    }

    public interface CheckIdCardCallback {
        void onResult(boolean isIdCard);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getRoomById();
        timer.cancel();
        finish();
    }

    private String getUserGoogleFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_google", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", "");
    }

    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }

    private void choose_TimeIN(String id_room) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TimePickerDialog timePicker = new TimePickerDialog(Activity_order_room.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int selectedSecond = 00;
                        String formattedDateTime = String.format("%02d:%02d:%02d %02d/%02d/%04d",
                                hourOfDay, minute, selectedSecond, dayOfMonth, month + 1, year);
                        tv_time_in.setText(formattedDateTime);
                        updateTotalPrice(id_room);
                    }
                }, hour, minute, true);
                timePicker.show();
            }
        }, year, month, day);
        dialog.show();
    }

    private void choose_TimeOUT(String id_room) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TimePickerDialog timePicker = new TimePickerDialog(Activity_order_room.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int selectedSecond = 00;
                        String formattedDateTime = String.format("%02d:%02d:%02d %02d/%02d/%04d",
                                hourOfDay, minute, selectedSecond, dayOfMonth, month + 1, year);
                        tv_time_out.setText(formattedDateTime);
                        updateTotalPrice(id_room);
                    }
                }, hour, minute, true);
                timePicker.show();
            }
        }, year, month, day);
        dialog.show();
    }

    private void updateTotalPrice(String id_room) {
        String timeIn = tv_time_in.getText().toString();
        String timeOut = tv_time_out.getText().toString();
        String priceString = tv_price_service.getText().toString();
        priceString = priceString.replaceAll("[^\\d.]", "");
        float priceService = Float.parseFloat(priceString);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());

        try {
            Date dateIn = dateFormat.parse(timeIn);
            Date dateOut = dateFormat.parse(timeOut);

            // Kiểm tra nếu dateOut trước dateIn thì thông báo lỗi
            if (dateOut != null && dateIn != null && dateOut.before(dateIn)) {
                Toast.makeText(Activity_order_room.this, "Time check out must be after time check in", Toast.LENGTH_SHORT).show();
            } else {
                // Tính số ngày thuê phòng
                long diffInMillis = dateOut.getTime() - dateIn.getTime();
                double roundedNumDays = (double) diffInMillis / (1000 * 60 * 60 * 24);
                int numDays = (int) Math.ceil(roundedNumDays);
                Api_service.service.get_rooms_byId(id_room).enqueue(new Callback<List<Room>>() {
                    @Override
                    public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                        if (response.isSuccessful()) {
                            List<Room> rooms = response.body();
                            Room room = rooms.get(0);
                            // Tính tổng tiền
                            float total = room.getGiaPhong() * numDays + priceService;
                            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                            tv_total.setText(String.format(formatter.format(total))); // Cập nhật tổng tiền
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Room>> call, Throwable throwable) {
                        Log.e("Failure get room", throwable.getMessage());
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(Activity_order_room.this, "Please choose time check in and time check out", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String serviceName = data.getStringExtra("service_name");
            String serviceId = data.getStringExtra("service_id");
            String servicePrice = data.getStringExtra("service_price");
            if (serviceName != null && !serviceName.isEmpty()) {
                tv_service.setText(serviceName);
            } else {
                tv_service.setText("No service required");
            }
            if (serviceId != null && !serviceId.isEmpty()) {
                tv_id_service.setText(serviceId);
            } else {
                tv_id_service.setText("6707ed79df6d7c9585d8eb99");
            }

            if (servicePrice != null && !servicePrice.isEmpty()) {
                tv_price_service.setText(servicePrice);
            } else {
                tv_price_service.setText("0");
            }
        }
    }
    private String userTokenFCM(){
        SharedPreferences preferences = getSharedPreferences("FCM_TOKEN", MODE_PRIVATE);
        return preferences.getString("token", null);
    }
}