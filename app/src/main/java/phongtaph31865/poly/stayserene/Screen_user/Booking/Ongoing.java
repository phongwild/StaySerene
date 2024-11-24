package phongtaph31865.poly.stayserene.Screen_user.Booking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private List<Order_Room> order_rooms;

    public Ongoing() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ongoing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rcv_ongoing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = view.findViewById(R.id.swipe_refresh_ongoing);
        progressBar = view.findViewById(R.id.progressBar_ongoing);

        // Thiết lập hành động cho SwipeRefreshLayout
        refreshLayout.setOnRefreshListener(this::get_orderroom_by_status01);

        // Tải dữ liệu ban đầu
        get_orderroom_by_status01();
    }

    public void get_orderroom_by_status01() {
        // Hiển thị trạng thái loading khi gọi API
        progressBar.setVisibility(View.VISIBLE);
        Api_service.service.get_orderroom_status01(getCurrentUserId()).enqueue(new Callback<List<Order_Room>>() {
            @Override
            public void onResponse(Call<List<Order_Room>> call, Response<List<Order_Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    order_rooms = response.body();
                    // Luôn khởi tạo lại adapter mỗi lần có dữ liệu mới
                    adapter = new Adapter_rcv_ongoing(order_rooms);
                    recyclerView.setAdapter(adapter);
                }
                refreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Order_Room>> call, Throwable throwable) {
                Log.e("Error get order room by status", throwable.getMessage());
                // Tắt trạng thái loading của SwipeRefreshLayout
                refreshLayout.setRefreshing(false);
            }
        });
    }


    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", getContext().MODE_PRIVATE);
        return sharedPreferences.getString("uid", "");
    }
}
