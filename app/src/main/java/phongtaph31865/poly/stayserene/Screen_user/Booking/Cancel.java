package phongtaph31865.poly.stayserene.Screen_user.Booking;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Adapter.Adapter_rcv_cancel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Cancel extends Fragment {

    private RecyclerView recyclerView;
    private Adapter_rcv_cancel adapter;
    List<Order_Room> order_rooms;
    private ProgressBar progressBar;
    private TextView tv_cancel;
    private SwipeRefreshLayout refreshLayout;

    public Cancel() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cancel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rcv_cancel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = view.findViewById(R.id.swipe_refresh_cancel);
        progressBar = view.findViewById(R.id.progressBar_cancel);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        refreshLayout.setOnRefreshListener(this::get_orderroom_by_status3);
        get_orderroom_by_status3();
    }
    public void get_orderroom_by_status3() {
        progressBar.setVisibility(View.VISIBLE);
        Api_service.service.get_orderroom_status3(getCurrentUserId()).enqueue(new Callback<List<Order_Room>>() {
            @Override
            public void onResponse(Call<List<Order_Room>> call, Response<List<Order_Room>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        order_rooms = response.body();
                        adapter = new Adapter_rcv_cancel(order_rooms);
                        recyclerView.setAdapter(adapter);
                        if (order_rooms.isEmpty()) {
                            tv_cancel.setVisibility(View.VISIBLE);
                        } else {
                            tv_cancel.setVisibility(View.GONE);
                        }
                    }
                }
                refreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Order_Room>> call, Throwable throwable) {
                Log.e("Error get order room", throwable.getMessage());
                refreshLayout.setRefreshing(false);
            }
        });
    }
    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", getContext().MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }

}