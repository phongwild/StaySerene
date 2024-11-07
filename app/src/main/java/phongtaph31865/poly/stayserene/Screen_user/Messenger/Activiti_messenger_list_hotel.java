package phongtaph31865.poly.stayserene.Screen_user.Messenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Detail_room_screen;
import phongtaph31865.poly.stayserene.Screen_user.Activity.MainActivity_user;
import phongtaph31865.poly.stayserene.Screen_user.Fragment_user;
import phongtaph31865.poly.stayserene.adapter.Adapter_list_hotel_messenger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activiti_messenger_list_hotel extends AppCompatActivity {
    private ImageView backButton;
    private RecyclerView recyclerView;
    private Adapter_list_hotel_messenger adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiti_messenger_list_hotel);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rcv_hotel_messenger);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backButton = findViewById(R.id.btn_back_fragment_user);

        // Set the click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current activity content with Fragment_user
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, new Fragment_user()); // Replace with the Fragment_user
                transaction.addToBackStack(null); // Optional: Adds the transaction to the back stack
                transaction.commit(); // Apply the transaction
            }
        });
        // Call the API to get list of hotels
        fetchHotels();
    }

    private void fetchHotels() {
        Api_service apiService = Api_service.service;
        Call<List<Hotel>> call = apiService.get_hotel(); // Assume `getHotels` is your API method.

        call.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful()) {
                    List<Hotel> hotelList = response.body();
                    // Initialize the adapter with the list of hotels
                    adapter = new Adapter_list_hotel_messenger(Activiti_messenger_list_hotel.this, hotelList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Activiti_messenger_list_hotel.this, "Failed to load hotels", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {
                Toast.makeText(Activiti_messenger_list_hotel.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
