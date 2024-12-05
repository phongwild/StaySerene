package phongtaph31865.poly.stayserene.Screen_user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.imaginativeworld.oopsnointernet.dialogs.signal.DialogPropertiesSignal;
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal;

import java.util.ArrayList;
import java.util.List;

import phongtaph31865.poly.stayserene.Adapter.Adapter_rcv1_home;
import phongtaph31865.poly.stayserene.Adapter.Adapter_rcv2_home;
import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.NetworkUtils.NetworkUtils;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Activity_detail_room;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Activity_more_hotel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_home extends Fragment {
    private RecyclerView rcv1, rcv2;
    private TextView tvMoreHotel, tvMoreRoom, tvLocation, tv_see_more_room;
    private Adapter_rcv1_home adapter1;
    private Adapter_rcv2_home adapter2;
    private List<Room> rooms = new ArrayList<>();
    private List<Hotel> hotels = new ArrayList<>();
    private ProgressBar progressBar1, progressBar2;


    public Fragment_home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo view
        initView(view);

        // Hiển thị thông báo quyền
        requestNotificationPermission();

        // Lấy thông tin người dùng
        fetchAndSaveUserData();

        // Thiết lập sự kiện click
        setClickListeners();

        // Kiểm tra kết nối mạng
        checkNetworkConnection();

        return view;
    }

    private void initView(View view) {
        rcv1 = view.findViewById(R.id.rcv_home_1);
        rcv2 = view.findViewById(R.id.rcv_home_2);
        tvMoreHotel = view.findViewById(R.id.tv_show_more_ht);
        tvMoreRoom = view.findViewById(R.id.tv_show_more_room);
        tvLocation = view.findViewById(R.id.tv_location_home);
        tv_see_more_room = view.findViewById(R.id.tv_see_more_room);
        progressBar1 = view.findViewById(R.id.progressBar_rcv1_home);
        progressBar2 = view.findViewById(R.id.progressBar_rcv2_home);

        // Thiết lập LayoutManager
        rcv1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcv2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void fetchAndSaveUserData() {
        String username = getUsernameFromSharedPreferences();
        if (username == null) {
            Log.e("saveUser", "No username found in SharedPreferences");
            return;
        }

        Api_service.service.get_account_byId(username).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Account acc : response.body()) {
                        saveUserToPreferences(acc);

                        // Cập nhật token nếu có
                        updateTokenIfAvailable(acc);

                        // Cập nhật giao diện
                        tvLocation.setText(acc.getDiaChi());
                    }
                } else {
                    Log.e("saveUser", "Không lấy được tài khoản");
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e("saveUser", "Error: " + throwable.getMessage());
            }
        });
    }

    private void updateTokenIfAvailable(Account acc) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("FCM_TOKEN", Activity.MODE_PRIVATE);
        String token = preferences.getString("token", null);
        if (token != null) {
            acc.setToken(token);
            Api_service.service.update_account(acc.get_id(), acc).enqueue(new Callback<List<Account>>() {
                @Override
                public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                    if (response.isSuccessful()) {
                        Log.e("updateToken", "Token updated: " + acc.getToken());
                    } else {
                        Log.e("updateToken", "Failed to update token");
                    }
                }

                @Override
                public void onFailure(Call<List<Account>> call, Throwable throwable) {
                    Log.e("updateToken", "Error: " + throwable.getMessage());
                }
            });
        }
    }

    private void setClickListeners() {
        tvMoreHotel.setOnClickListener(v -> startActivity(new Intent(getActivity(), Activity_more_hotel.class)));
        tvMoreRoom.setOnClickListener(v -> startActivity(new Intent(getActivity(), Activity_detail_room.class)));
        tv_see_more_room.setOnClickListener(v -> loadMoreItemRooms());
    }

    private void checkNetworkConnection() {
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            loadHotelData(() -> loadRoomData());

        } else {
            showNoInternetDialog();
        }
    }

    private void loadHotelData(Runnable onSuccess) {
        progressBar1.setVisibility(View.VISIBLE);
        Api_service.service.get_hotel().enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    hotels = response.body();
                    adapter1 = new Adapter_rcv1_home(hotels);
                    rcv1.setAdapter(adapter1);
                    onSuccess.run();
                } else {
                    Log.e("loadHotelData", "Không lấy được danh sách khách sạn");
                }
                progressBar1.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                Log.e("loadHotelData", "Error: " + throwable.getMessage());
                progressBar1.setVisibility(View.GONE);
            }
        });
    }

    private void loadRoomData() {
        progressBar2.setVisibility(View.VISIBLE);
        Api_service.service.get_rooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rooms = response.body();
                    adapter2 = new Adapter_rcv2_home(rooms);
                    rcv2.setAdapter(adapter2);
                    adapter2.filterPrice(true);
                } else {
                    Log.e("loadRoomData", "Không lấy được danh sách phòng");
                }
                progressBar2.setVisibility(View.GONE);
                tv_see_more_room.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("loadRoomData", "Error: " + throwable.getMessage());
                progressBar2.setVisibility(View.GONE);
            }
        });
    }

    private void loadMoreItemRooms() {
        adapter2.loadMoreItems();
    }
    private void showNoInternetDialog() {
        NoInternetDialogSignal.Builder builder = new NoInternetDialogSignal.Builder(getActivity(), getLifecycle());
        DialogPropertiesSignal properties = builder.getDialogProperties();
        properties.setConnectionCallback(hasActiveConnection -> {
            if (hasActiveConnection) loadHotelData(this::loadRoomData);
        });
        properties.setCancelable(false);
        builder.build();
    }

    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }

    private void saveUserToPreferences(Account account) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", account.get_id());
        editor.putString("username", account.getUsername());
        editor.putString("sdt", account.getSdt());
        editor.putString("address", account.getDiaChi());
        editor.putString("email", account.getEmail());
        editor.putString("cccd", account.getCccd());
        editor.putString("gender", account.getGioiTinh());
        editor.putString("birthday", account.getNgaySinh());
        editor.putString("avatar", account.getAvt());
        editor.putString("quoctich", account.getQuocTich());
        editor.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Notification is granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Notification is denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
