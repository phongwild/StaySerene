package phongtaph31865.poly.stayserene.Screen_user.Messenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import phongtaph31865.poly.stayserene.Adapter.Adapter_list_messenger;
import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.Messenger;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activiti_list_messenger extends AppCompatActivity {

    private static final long REFRESH_INTERVAL = 5000;
    private final Handler handler = new Handler();
    private TextView tvHotelName;
    private String hotelId, userId, fcmToken;
    private ImageView btnBackListHotel, btnSendMessenger;
    private RecyclerView recyclerView;
    private Adapter_list_messenger adapter;
    private EditText edMessenger;
    private Runnable refreshRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiti_list_messenger);

        initView();
        initData();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBackListHotel.setOnClickListener(v -> finish());
        btnSendMessenger.setOnClickListener(v -> sendMessage());

        fetchHotelName();
        fetchMessages();
        startPolling();
    }

    private void initView() {
        tvHotelName = findViewById(R.id.tv_hotel_name);
        btnBackListHotel = findViewById(R.id.btn_back_list_hotel);
        recyclerView = findViewById(R.id.rcv_list_messenger);
        btnSendMessenger = findViewById(R.id.btn_send_messenger);
        edMessenger = findViewById(R.id.ed_messenger);
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        userId = sharedPreferences.getString("uid", "");
        fcmToken = getSharedPreferences("FCM_TOKEN", MODE_PRIVATE).getString("token", null);

        Intent intent = getIntent();
        hotelId = intent.getStringExtra("IdKhachSan");
    }

    private void fetchHotelName() {
        if (hotelId == null || hotelId.isEmpty()) return;

        Api_service.service.get_hotel_byId(hotelId).enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    tvHotelName.setText(response.body().get(0).getTenKhachSan());
                } else {
                    Log.d("TAG", "Hotel name fetch failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                Log.d("TAG", "Hotel name fetch error: " + throwable.getMessage());
            }
        });
    }

    private void fetchMessages() {
        if (userId.isEmpty() || hotelId == null || hotelId.isEmpty()) return;

        Api_service.service.getMessengersForHotel(hotelId, userId).enqueue(new Callback<List<Messenger>>() {
            @Override
            public void onResponse(Call<List<Messenger>> call, Response<List<Messenger>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Messenger> messengerList = response.body();
                    if (adapter == null) {
                        adapter = new Adapter_list_messenger(Activiti_list_messenger.this, messengerList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter = new Adapter_list_messenger(Activiti_list_messenger.this, messengerList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged(); // Làm mới danh sách
                    }
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
            }

            @Override
            public void onFailure(Call<List<Messenger>> call, Throwable t) {
                Log.d("TAG", "Message fetch error: " + t.getMessage());
            }
        });
    }

    private void sendMessage() {
        String messageContent = edMessenger.getText().toString().trim();
        if (messageContent.isEmpty()) {
            Toast.makeText(this, "Please enter a message!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId.isEmpty() || hotelId == null || hotelId.isEmpty()) return;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -7); // Subtract 7 hours
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String currentTime = dateFormat.format(calendar.getTime());

        Messenger messenger = new Messenger();
        messenger.setIdKhachSan(hotelId);
        messenger.setUid(userId);
        messenger.setThoiGianGui(currentTime);
        messenger.setNoiDungGui(messageContent);
        messenger.setVaiTro("Khách hàng");
        messenger.setTrangThaiNv(1);
        messenger.setTrangThaiKh(1);
        messenger.setUserTokenFCM(fcmToken);

        Api_service.service.sendMessage(messenger).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Activiti_list_messenger.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    edMessenger.setText("");
                    fetchMessages(); // Cập nhật danh sách tin nhắn sau khi gửi
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "Message send error: " + t.getMessage());
            }
        });
    }

    private void startPolling() {
        refreshRunnable = () -> {
            fetchMessages(); // Lấy tin nhắn mới
            handler.postDelayed(refreshRunnable, REFRESH_INTERVAL);
        };
        handler.post(refreshRunnable);
    }

    private void stopPolling() {
        handler.removeCallbacks(refreshRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPolling();
    }
}
