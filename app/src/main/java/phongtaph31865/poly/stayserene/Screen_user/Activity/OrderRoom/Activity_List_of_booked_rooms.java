package phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.adapter.Adapter_List_of_booked_rooms;
import phongtaph31865.poly.stayserene.adapter.Adapter_detail_room;
import phongtaph31865.poly.stayserene.adapter.Adapter_type_rooms;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_List_of_booked_rooms extends AppCompatActivity {


    private ImageView btn_back;
    private RecyclerView rcv;
    //    private TextView title;
    Adapter_List_of_booked_rooms adapter;
    private List<Order_Room> rooms;
    @SuppressLint("MissingInflatedId")


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_booked_rooms);
        rcv = findViewById(R.id.rcv_List_of_booked_rooms);
        btn_back = findViewById(R.id.btn_back_List_of_booked_rooms);
        Intent intent = getIntent();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(Activity_list_type_room.this, MainActivity_user.class));
                finish();
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rcv.setLayoutManager(llm);
        get_order_room();
    }
    public void get_order_room(){
        Api_service.service.get_orderroom_status01(checkUid()).enqueue(new Callback<List<Order_Room>>() {
            @Override
            public void onResponse(Call<List<Order_Room>> call, Response<List<Order_Room>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        //rooms.clear();
                        rooms = response.body();
                        adapter = new Adapter_List_of_booked_rooms(rooms);
                        rcv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }else Log.e("Detail_room", "Lỗi");
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