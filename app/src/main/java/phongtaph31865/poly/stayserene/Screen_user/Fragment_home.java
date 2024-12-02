package phongtaph31865.poly.stayserene.Screen_user;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
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
import phongtaph31865.poly.stayserene.Adapter.Adapter_rcv1_home;
import phongtaph31865.poly.stayserene.Adapter.Adapter_rcv2_home;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_home extends Fragment {
    private RecyclerView rcv1, rcv2;
    private TextView tv_more_ht, tv_more_room, tv_location;
    private Adapter_rcv1_home adapter_1;
    private Adapter_rcv2_home adapter_2;
    private List<Room> rooms = new ArrayList<Room>();
    private List<Hotel> hotels = new ArrayList<Hotel>();

    public Fragment_home() {
    }

    @SuppressLint({"MissingInflatedId", "MissingPermission"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        showNotifi();

        saveUser();

        initView(v);

        setClick();

        checkNetWorkUtils();
        return v;
    }
    private void saveUser(){
        Api_service.service.get_account_byId(getUsernameFromSharedPreferences()).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (Account acc : response.body()) {
                            saveUserIdToSharedPreferences(acc.get_id(), acc.getUsername(), acc.getSdt(), acc.getDiaChi(), acc.getEmail(), acc.getCccd(), acc.getGioiTinh(), acc.getNgaySinh(), acc.getAvt(), acc.getQuocTich());
                            SharedPreferences preferences = requireActivity().getSharedPreferences("FCM_TOKEN", Activity.MODE_PRIVATE);
                            String token = preferences.getString("token", null);
                            acc.setToken(token);
                            if (token != null) {
                                Api_service.service.update_account(acc.get_id(), acc).enqueue(new Callback<List<Account>>() {
                                    @Override
                                    public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                                        if (response.isSuccessful()) {
                                            Log.e("update token", token);
                                        } else Log.e("update token", response.message());
                                    }

                                    @Override
                                    public void onFailure(Call<List<Account>> call, Throwable throwable) {
                                        Log.e("onFailure id user", "False: " + throwable.getMessage());
                                    }
                                });
                            }
                            tv_location.setText(acc.getDiaChi());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e("onFailure id user", "False: " + throwable.getMessage());
            }
        });
    }

    private void initView(View v){
        rcv1 = v.findViewById(R.id.rcv_home_1);
        rcv2 = v.findViewById(R.id.rcv_home_2);
        tv_more_ht = v.findViewById(R.id.tv_show_more_ht);
        tv_more_room = v.findViewById(R.id.tv_show_more_room);
        tv_location = v.findViewById(R.id.tv_location_home);
    }

    private void setClick(){
        tv_more_ht.setOnClickListener(v -> startActivity(new Intent(getActivity(), Activity_more_hotel.class)));
        tv_more_room.setOnClickListener(v -> startActivity(new Intent(getActivity(), Activity_detail_room.class)));

    }

    private void checkNetWorkUtils(){
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            get_ds_ks();
            get_ds_phong();
        } else if (NetworkUtils.isNetworkConnected(getActivity()) == false) {
            NoInternetDialogSignal.Builder builder = new NoInternetDialogSignal.Builder(
                    getActivity(),
                    getLifecycle()
            );
            DialogPropertiesSignal properties = builder.getDialogProperties();

            properties.setConnectionCallback(new ConnectionCallback() { // Optional
                @Override
                public void hasActiveConnection(boolean hasActiveConnection) {
                    get_ds_ks();
                    get_ds_phong();
                }
            });
            properties.setCancelable(false); // Optional
            properties.setNoInternetConnectionTitle("No Internet"); // Optional
            properties.setNoInternetConnectionMessage("Check your Internet connection and try again"); // Optional
            properties.setShowInternetOnButtons(true); // Optional
            properties.setPleaseTurnOnText("Please turn on"); // Optional
            properties.setWifiOnButtonText("Wifi"); // Optional
            properties.setMobileDataOnButtonText("Mobile data"); // Optional

            properties.setOnAirplaneModeTitle("No Internet"); // Optional
            properties.setOnAirplaneModeMessage("You have turned on the airplane mode."); // Optional
            properties.setPleaseTurnOffText("Please turn off"); // Optional
            properties.setAirplaneModeOffButtonText("Airplane mode"); // Optional
            properties.setShowAirplaneModeOffButtons(true); // Optional
            builder.build();
        }
    }

    public void get_ds_ks() {
        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rcv1.setLayoutManager(llm1);
        Api_service.service.get_hotel().enqueue(new Callback<List<Hotel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        hotels.clear();
                        hotels = response.body();
                        adapter_1 = new Adapter_rcv1_home(hotels);
                        rcv1.setAdapter(adapter_1);
                        adapter_1.notifyDataSetChanged();
                    } else Log.e("rcv1", "False: khong lay duoc du lieu 1");
                } else Log.e("rcv1", "False: khong lay duoc du lieu 2");
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                Log.e("rcv1", "False: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }

    public void get_ds_phong() {
        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcv2.setLayoutManager(llm2);
        Api_service.service.get_rooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        rooms.clear();
                        rooms = response.body();
                        adapter_2 = new Adapter_rcv2_home(rooms);
                        rcv2.setAdapter(adapter_2);
                        adapter_2.notifyDataSetChanged();
                    }
                } else Log.e("rcv2", "False: khong lay duoc du lieu");
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("rcv2", "False: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }

    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }

    private void showNotifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS
                        }, 101
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền
                Toast.makeText(requireContext(), "Notification is granted", Toast.LENGTH_SHORT).show();
            } else {
                // Người dùng từ chối quyền
                Toast.makeText(requireContext(), "Notification is denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUserIdToSharedPreferences(String Uid, String username, String sdt, String address, String email, String cccd, String gender, String birthday, String avatar, String quoctich) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", Uid);
        editor.putString("username", username);
        editor.putString("sdt", sdt);
        editor.putString("address", address);
        editor.putString("email", email);
        editor.putString("cccd", String.valueOf(cccd));
        editor.putString("gender", gender);
        editor.putString("birthday", birthday);
        editor.putString("avatar", avatar);
        editor.putString("quoctich", quoctich);
        editor.apply();
    }
}