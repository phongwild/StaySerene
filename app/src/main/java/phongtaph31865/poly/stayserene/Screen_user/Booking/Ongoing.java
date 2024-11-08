package phongtaph31865.poly.stayserene.Screen_user.Booking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.adapter.Adapter_rcv_ongoing;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ongoing extends Fragment {

    private RecyclerView recyclerView;
    private Adapter_rcv_ongoing adapter;
    private List<Order_Room> orderroom = new ArrayList<Order_Room>();

    public Ongoing() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ongoing, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        recyclerView = v.findViewById(R.id.rcv_ongoing);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        get_ds_ongoing();
    }

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", getContext().MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }


    public void get_ds_ongoing() {
        Api_service.service.get_orderroom_byUid(getCurrentUserId()).enqueue(new Callback<List<Order_Room>>() {
            @Override
            public void onResponse(Call<List<Order_Room>> call, Response<List<Order_Room>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        orderroom = response.body();
                        adapter = new Adapter_rcv_ongoing(orderroom);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.e("ongoing", "False: khong lay duoc du lieu 1");
                    }
                } else {
                    Log.e("ongoing", "False: khong lay duoc du lieu 1");
                }
            }

            @Override
            public void onFailure(Call<List<Order_Room>> call, Throwable throwable) {
                Log.e("ongoing", "False:" + throwable.getMessage());
                throwable.printStackTrace();
            }
        }
        );

    }
}