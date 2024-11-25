package phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Service;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Adapter.Adapter_list_service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_list_service extends AppCompatActivity {

    private RecyclerView rcvService;
    private Adapter_list_service adapter;
    private List<Service> serviceList;
    private ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_service);

        rcvService = findViewById(R.id.rcv_service);
        rcvService.setLayoutManager(new LinearLayoutManager(this));
        btn_back = findViewById(R.id.btn_back_order_room);

        btn_back.setOnClickListener(v -> finish());

        fetchServices();
    }

    private void fetchServices() {
        Api_service.service.get_service().enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    serviceList = response.body();

                    adapter = new Adapter_list_service(Activity_list_service.this, serviceList, (serviceId, serviceName, servicePrice) -> {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("service_id", serviceId);
                        resultIntent.putExtra("service_name", serviceName);
                        resultIntent.putExtra("service_price", String.valueOf(servicePrice));
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    });

                    rcvService.setAdapter(adapter);
                } else {
                    Toast.makeText(Activity_list_service.this, "Failed to load services", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                Toast.makeText(Activity_list_service.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

