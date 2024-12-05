package phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import phongtaph31865.poly.stayserene.Adapter.Adapter_List_of_booked_rooms;
import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_List_of_booked_rooms extends AppCompatActivity {


    private ImageView btn_back;
    private RecyclerView rcv;
    private Adapter_List_of_booked_rooms adapter;
    private List<Order_Room> rooms;
    private TextView textView;

    @SuppressLint("MissingInflatedId")


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_booked_rooms);

        initUI();

        btn_back.setOnClickListener(v -> finish());

        get_order_room();
    }

    private void initUI() {
        rcv = findViewById(R.id.rcv_List_of_booked_rooms);
        btn_back = findViewById(R.id.btn_back_List_of_booked_rooms);
        textView = findViewById(R.id.tv_list_booked_rooms);
    }

    public void get_order_room() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rcv.setLayoutManager(llm);
        Api_service.service.get_orderroom_status01(checkUid()).enqueue(new Callback<List<Order_Room>>() {
            @Override
            public void onResponse(Call<List<Order_Room>> call, Response<List<Order_Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rooms = response.body();
                    adapter = new Adapter_List_of_booked_rooms(rooms);
                    rcv.setAdapter(adapter);
                    if (rooms.isEmpty()){
                        textView.setVisibility(View.VISIBLE);
                    }else textView.setVisibility(View.GONE);

                } else Log.e("Detail_room", "Lỗi");
            }

            @Override
            public void onFailure(Call<List<Order_Room>> call, Throwable throwable) {
                Log.e("Detail_room", "Lỗi: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }

    private String checkUid() {
        String uid = getSharedPreferenceData("uid");
        return (uid != null) ? uid : getSharedPreferenceData("email");
    }

    // Fetch user data from SharedPreferences
    private String getSharedPreferenceData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}