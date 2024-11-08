package phongtaph31865.poly.stayserene.Screen_user.Booking;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.adapter.Adapter_rcv_cancel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Cancel extends Fragment {

    private RecyclerView recyclerView;
    private Adapter_rcv_cancel adapter;
    private List<Order_Room> orderroom2 = new ArrayList<Order_Room>();

    public Cancel() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v =  inflater.inflate(R.layout.fragment_cancel, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rcv_cancel);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        get_ds_cancle();
    }

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", getContext().MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }
    public void get_ds_cancle(){
        Api_service.service.get_orderroom_byUid(getCurrentUserId()).enqueue(new Callback<List<Order_Room>>() {
            @Override
            public void onResponse(Call<List<Order_Room>> call, Response<List<Order_Room>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        orderroom2 = response.body();
                        adapter = new Adapter_rcv_cancel(orderroom2);
                        recyclerView.setAdapter(adapter);
                    }else {
                        Log.e("cancle", "False: khong lay duoc du lieu 1");

                    }
                }else {
                    Log.e("cancle", "False: khong lay duoc du lieu 1");

                }
            }

            @Override
            public void onFailure(Call<List<Order_Room>> call, Throwable throwable) {
                Log.e("cancle", "False:" + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }

}