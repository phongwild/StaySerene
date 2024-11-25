package phongtaph31865.poly.stayserene.Screen_user.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Adapter.Adapter_more_hotel;
import phongtaph31865.poly.stayserene.databinding.ActivityMoreHotelBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_more_hotel extends AppCompatActivity {
    private ActivityMoreHotelBinding binding;
    private List<Hotel> hotels = new ArrayList<Hotel>();
    private Adapter_more_hotel adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMoreHotelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnBackMoreHotel.setOnClickListener(v -> {
            finish();
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcvMoreHotel.setLayoutManager(linearLayoutManager);
        getHotel();
    }
    private void getHotel(){
        Api_service.service.get_hotel().enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if(response.isSuccessful() && response.body() != null){
                    hotels = response.body();
                    adapter = new Adapter_more_hotel(hotels);
                    binding.rcvMoreHotel.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{
                    Log.e("Error", "False: khong lay duoc du lieu");
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                Log.e("Error", throwable.getMessage());
            }
        });
    }
}