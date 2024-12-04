package phongtaph31865.poly.stayserene.Screen_user.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Adapter.Adapter_detail_room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_detail_room extends AppCompatActivity {
    private ImageView btn_back, btn_filter;
    private RecyclerView rcv;
    private Adapter_detail_room adapter;
    private ProgressBar progressBar;
    private List<Room> rooms;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_room);
        initView();
        handleView();
        get_room();
    }
    private void initView(){
        btn_back = findViewById(R.id.btn_back_detail_room);
        btn_filter = findViewById(R.id.btn_filter_detail_room);
        rcv = findViewById(R.id.rcv_detail_room);
        progressBar = findViewById(R.id.progressBar_detail_room);
    }
    private void handleView(){
        btn_back.setOnClickListener(v -> finish());
        btn_filter.setOnClickListener(this::showPopupMenu);
    }
    private void showPopupMenu(View view){
        PopupMenu popupMenu = new PopupMenu(Activity_detail_room.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_filter_room, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.filter_option_1) {
                options_hotel();
            }else if(item.getItemId() == R.id.filter_option_2){
                adapter.filterPrice(true);
                adapter.notifyDataSetChanged();
            }else if(item.getItemId() == R.id.filter_option_3){
                adapter.filterPrice(false);
                adapter.notifyDataSetChanged();
            }
            return true;
        });
        popupMenu.show();
    }
    private void options_hotel(){
        Api_service.service.get_hotel().enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Hotel> hotels = response.body();
                    showSubMenuHotel(hotels);
                }else Log.e("Detail_room", response.message());
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                Log.e("Detail_room", "Lỗi: " + throwable.getMessage());
            }
        });
    }
    private void showSubMenuHotel(List<Hotel> hotels){
        // Tạo PopupMenu
        PopupMenu subMenu = new PopupMenu(this, btn_filter);

        // Create menu
        int itemID = 0;
        Menu menu = subMenu.getMenu();
        for (Hotel hotel : hotels) {
            menu.add(Menu.NONE, itemID++, Menu.NONE, hotel.getTenKhachSan())
                    .setOnMenuItemClickListener(item -> {
                        String IdHotel = hotel.get_id();
                        adapter.filterHotel(IdHotel);
                        return true;
                    });
        }

        subMenu.show();
    }
    public void get_room(){
        progressBar.setVisibility(View.VISIBLE);
        GridLayoutManager manager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(manager);
        Api_service.service.get_rooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        rooms = response.body();
                        adapter = new Adapter_detail_room(rooms);
                        rcv.setAdapter(adapter);
                        adapter.filterPrice(true);
                        adapter.notifyDataSetChanged();
                    }
                }else Log.e("Detail_room", "Lỗi");
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("Detail_room", "Lỗi: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }
}