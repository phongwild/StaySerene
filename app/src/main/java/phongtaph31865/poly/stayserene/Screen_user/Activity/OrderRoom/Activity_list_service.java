package phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Service;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Adapter.Adapter_list_service;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Activity_detail_room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_list_service extends AppCompatActivity {

    private RecyclerView rcvService;
    private Adapter_list_service adapter;
    private List<Service> serviceList;
    private ImageView btn_back;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_service);

        sharedPreferences = getSharedPreferences("ServiceCache", MODE_PRIVATE);

        rcvService = findViewById(R.id.rcv_service);
        rcvService.setLayoutManager(new LinearLayoutManager(this));
        btn_back = findViewById(R.id.btn_back_order_room);
        progressBar = findViewById(R.id.progressBar_list_service);
        btn_back.setOnClickListener(v -> finish());

        fetchServices();
    }

    private void fetchServices() {
        progressBar.setVisibility(View.VISIBLE);
        Api_service.service.get_service().enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    serviceList = response.body();

                    // Lưu cache
                    saveToCache(serviceList);

                    // Cập nhật RecyclerView
                    setupAdapter();
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(Activity_list_service.this, "Failed to load services", Toast.LENGTH_SHORT).show();
                    // Thử lấy dữ liệu từ cache
                    serviceList = loadFromCache();
                    setupAdapter();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    showToast("Weak network, please try again later.");
                } else if (t instanceof UnknownHostException) {
                    showToast("No network connection available.");
                } else {
                    Log.e("Detail_room", "Lỗi: " + t.getMessage());
                }
                // Thử lấy dữ liệu từ cache
                serviceList = loadFromCache();
                setupAdapter();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(Activity_list_service.this, message, Toast.LENGTH_SHORT).show();
    }
    private void setupAdapter() {
        if (serviceList == null || serviceList.isEmpty()) {
            Toast.makeText(this, "No services available.", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new Adapter_list_service(this, serviceList, (serviceId, serviceName, servicePrice) -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("service_id", serviceId);
                resultIntent.putExtra("service_name", serviceName);
                resultIntent.putExtra("service_price", String.valueOf(servicePrice));
                setResult(RESULT_OK, resultIntent);
                finish();
            });
            rcvService.setAdapter(adapter);
        }
    }

    private void saveToCache(List<Service> services) {
        String json = new Gson().toJson(services);
        sharedPreferences.edit().putString("services", json).apply();
    }

    private List<Service> loadFromCache() {
        String json = sharedPreferences.getString("services", "");
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Service>>() {}.getType();
        return new Gson().fromJson(json, type);
    }
}
