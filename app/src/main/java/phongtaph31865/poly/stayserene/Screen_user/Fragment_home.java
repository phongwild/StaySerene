package phongtaph31865.poly.stayserene.Screen_user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import io.ipgeolocation.api.Geolocation;
import io.ipgeolocation.api.IPGeolocationAPI;
import io.ipgeolocation.api.exceptions.IPGeolocationError;
import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.NetworkUtils.NetworkUtils;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Activity_detail_room;
import phongtaph31865.poly.stayserene.adapter.Adapter_rcv1_home;
import phongtaph31865.poly.stayserene.adapter.Adapter_rcv2_home;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @noinspection ALL
 */
public class Fragment_home extends Fragment {
    String API_KEY_LOCATION = "1131ca2e24684123bca828e5717c9792";
    private RecyclerView rcv1, rcv2;
    private TextView tv_more_ht, tv_more_room, tv_location;
    private Adapter_rcv1_home adapter_1;
    private Adapter_rcv2_home adapter_2;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private List<Room> rooms = new ArrayList<Room>();
    private List<Hotel> hotels = new ArrayList<Hotel>();

    public Fragment_home() {
    }

    @SuppressLint({"MissingInflatedId", "MissingPermission"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        //lấy vị trí người dùng
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        Api_service.service.get_account_byId(getUsernameFromSharedPreferences()).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (Account acc : response.body()) {
                            saveUserIdToSharedPreferences(acc.getUid(), acc.getUsername(), acc.getSdt(), acc.getDiaChi(), acc.getEmail());
                            tv_location.setText(acc.getDiaChi());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {

            }
        });
        rcv1 = v.findViewById(R.id.rcv_home_1);
        rcv2 = v.findViewById(R.id.rcv_home_2);
        tv_more_ht = v.findViewById(R.id.tv_show_more_ht);
        tv_more_room = v.findViewById(R.id.tv_show_more_room);
        tv_location = v.findViewById(R.id.tv_location_home);
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

        return v;
    }


    public void get_ds_ks() {
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

    private String getEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_google", Activity.MODE_PRIVATE);
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