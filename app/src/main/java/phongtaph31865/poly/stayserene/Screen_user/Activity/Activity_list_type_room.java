package phongtaph31865.poly.stayserene.Screen_user.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Adapter.Adapter_type_rooms;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_list_type_room extends AppCompatActivity {
    private ImageView btn_back;
    private RecyclerView rcv;
    private TextView title;
    private Adapter_type_rooms adapter;
    private ProgressBar progressBar;
    private List<TypeRoom> typeRoomList;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_type_room);

        initView();

        handleClick();

        get_ds_loaiPhong();
    }
    private void initView(){
        rcv = findViewById(R.id.rcv_list_type_rooms);
        btn_back = findViewById(R.id.btn_back_type_rooms);
        title = findViewById(R.id.tv_title_list_type_rooms);
        progressBar = findViewById(R.id.progressBar_rcv_list_type_rooms);
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("name"));
    }
    private void handleClick(){
        btn_back.setOnClickListener(v -> finish());
    }
    public void get_ds_loaiPhong() {
        Intent intent = getIntent();
        String _id = intent.getStringExtra("id");
        progressBar.setVisibility(View.VISIBLE);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rcv.setLayoutManager(llm);
        Api_service.service.get_typeroom_byId_hotel(_id).enqueue(new Callback<List<TypeRoom>>() {
            @Override
            public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        typeRoomList = response.body();
                        adapter = new Adapter_type_rooms(typeRoomList);
                        rcv.setAdapter(adapter);
                    }
                }else {
                    Log.e("List_type_rooms", "False: " + response.message());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                Log.e("List_type_rooms", "False: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }
}