package phongtaph31865.poly.stayserene.Screen_user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Activity_detail_room;
import phongtaph31865.poly.stayserene.adapter.Adapter_rcv1_home;
import phongtaph31865.poly.stayserene.adapter.Adapter_rcv2_home;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_home extends Fragment {
    private LocationManager locationManager = null;
    private Activity activity = getActivity();
    private RecyclerView rcv1, rcv2;
    private TextView tv_more_ht, tv_more_room;
    private Adapter_rcv1_home adapter_1;
    private Adapter_rcv2_home adapter_2;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private List<Room> rooms = new ArrayList<Room>();
    private List<Hotel> hotels = new ArrayList<Hotel>();

    public Fragment_home() { }

    @SuppressLint({"MissingInflatedId", "MissingPermission"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        //lấy vị trí người dùng
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Log.d("location", "lat: " + latitude + " long: " + longitude);
            }
        };
        if (activity != null) {
            locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        }
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }else {
            Log.d("location", "null");
        }

        rcv1 = v.findViewById(R.id.rcv_home_1);
        rcv2 = v.findViewById(R.id.rcv_home_2);
        tv_more_ht = v.findViewById(R.id.tv_show_more_ht);
        tv_more_room = v.findViewById(R.id.tv_show_more_room);
        tv_more_ht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_more_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activity_detail_room.class));
            }
        });
        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcv1.setLayoutManager(llm1);
        rcv2.setLayoutManager(llm2);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        String getUsername = getUsernameFromSharedPreferences();
        String getUid_google = getUser_google();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Account");
        if (getUsername != null) {
            ref.orderByChild("uid").equalTo(getUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String uid = snapshot1.getKey();
                            String name = snapshot1.child("username").getValue(String.class);
                            String email = snapshot1.child("email").getValue(String.class);
                            String sdt = snapshot1.child("sdt").getValue(String.class);
                            String address = snapshot1.child("diaChi").getValue(String.class);
                            saveUserIdToSharedPreferences(uid, name, sdt, address, email);
                            adapter_1.setUid(getUsername);
                            adapter_2.setUid(getUsername);
//                            Log.d("save", "user Realtime: " + getUsername);
                        }
                    } else {
                        Log.d("save_1", "user null");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (getUid_google != null) {
            //Google
            String name = account.getDisplayName();
            String email = account.getEmail();
            String sdt = "";
            String address = "";
            saveUserIdToSharedPreferences(getUid_google, name, sdt, address, email);
            Log.d("save", "User google: " + getUid_google);
            if (adapter_2 != null) {
                adapter_2.setUid(getUid_google);
            }
            if (adapter_1 != null) {
                adapter_1.setUid(getUid_google);
            }
        } else {
            //Facebook
        }
        get_ds_ks();
        get_ds_phong();
        return v;
    }

    public void get_ds_ks() {
        Api_service.service.get_hotel().enqueue(new Callback<List<Hotel>>() {
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

    private String getUser_google() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_google", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }

    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }

    private void saveUserIdToSharedPreferences(String Uid, String username, String sdt, String address, String email) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", Uid);
        editor.putString("username", username);
        editor.putString("sdt", sdt);
        editor.putString("address", address);
        editor.putString("email", email);
        editor.apply();
    }
}