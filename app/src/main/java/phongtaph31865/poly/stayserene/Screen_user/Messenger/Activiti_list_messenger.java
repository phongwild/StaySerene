package phongtaph31865.poly.stayserene.Screen_user.Messenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.adapter.Adapter_list_messenger;
import phongtaph31865.poly.stayserene.Model.Messenger;  // Import Messenger model
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activiti_list_messenger extends AppCompatActivity {

    private TextView tvHotelName;
    private String hotelId, hotelName;
    private ImageView btnBackListHotel, btnSendMessenger;
    private RecyclerView recyclerView;
    private Adapter_list_messenger adapter;
    private TextInputEditText edMessenger;
    private Handler handler = new Handler();
    private Runnable refreshRunnable;
    private static final long REFRESH_INTERVAL = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiti_list_messenger);

        tvHotelName = findViewById(R.id.tv_hotel_name);
        btnBackListHotel = findViewById(R.id.btn_back_list_hotel);
        recyclerView = findViewById(R.id.rcv_list_messenger);
        btnSendMessenger = findViewById(R.id.btn_send_messenger);
        edMessenger = findViewById(R.id.ed_messenger);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBackListHotel.setOnClickListener(v -> {
            finish();
        });

        Intent intent = getIntent();
        hotelId = intent.getStringExtra("IdKhachSan");
        hotelName = intent.getStringExtra("TenKhachSan");

        tvHotelName.setText(hotelName);
        fetchMessages();

        btnSendMessenger.setOnClickListener(v -> sendMessage());
        startPolling();
    }
    private void startPolling() {
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                fetchMessages(); // Fetch new messages
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
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
    private void sendMessage() {
        String messageContent = edMessenger.getText().toString().trim();
        if (messageContent.isEmpty()) {
            Toast.makeText(Activiti_list_messenger.this, "Please enter a message!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userId = sharedPreferences.getString("uid", "");

        if (userId.isEmpty()) {
            Toast.makeText(Activiti_list_messenger.this, "User ID is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (hotelId == null || hotelId.isEmpty()) {
            Toast.makeText(Activiti_list_messenger.this, "Hotel ID is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -7); // Subtract 7 hours
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String currentTime = dateFormat.format(calendar.getTime());

        phongtaph31865.poly.stayserene.Model.Messenger messenger = new phongtaph31865.poly.stayserene.Model.Messenger();
        messenger.setIdKhachSan(hotelId);
        messenger.setUid(userId);
        messenger.setThoiGianGui(currentTime);
        messenger.setNoiDungGui(messageContent);
        messenger.setVaiTro("Khách hàng");
        messenger.setTrangThaiNv(1);
        messenger.setTrangThaiKh(1);

        Api_service apiService = Api_service.service;
        Call<Void> call = apiService.sendMessage(messenger);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Activiti_list_messenger.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    edMessenger.setText("");
                    fetchMessages();
                } else {
                    Toast.makeText(Activiti_list_messenger.this, "Error: Unable to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Activiti_list_messenger.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void fetchMessages() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userId = sharedPreferences.getString("uid", "");

        if (userId.isEmpty()) {
            Toast.makeText(Activiti_list_messenger.this, "User ID is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (hotelId == null || hotelId.isEmpty()) {
            Toast.makeText(Activiti_list_messenger.this, "Hotel ID is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        Api_service apiService = Api_service.service;
        Call<List<Messenger>> call = apiService.getMessengersForHotel(hotelId, userId);

        call.enqueue(new Callback<List<Messenger>>() {
            @Override
            public void onResponse(Call<List<Messenger>> call, Response<List<Messenger>> response) {
                if (response.isSuccessful()) {
                    List<Messenger> messengerList = response.body();
                    if (messengerList != null && !messengerList.isEmpty()) {
                        adapter = new Adapter_list_messenger(Activiti_list_messenger.this, messengerList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    } else {
                        Toast.makeText(Activiti_list_messenger.this, "No messages available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Activiti_list_messenger.this, "Error: Unable to load messages", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Messenger>> call, Throwable t) {
                Toast.makeText(Activiti_list_messenger.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}