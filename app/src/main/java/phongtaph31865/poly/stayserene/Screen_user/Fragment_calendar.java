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
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom.Activity_List_of_booked_rooms;
import phongtaph31865.poly.stayserene.Adapter.Adapter_schedule;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_calendar extends Fragment {
    private CalendarView calendarView;
    private RecyclerView rcv;
    private TextView btn_see_all, tv_schedule;
    private List<Order_Room> order_rooms = new ArrayList<>();
    private Adapter_schedule adapter;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        initView(view);

        btn_see_all.setOnClickListener(v ->  startActivity(new Intent(getActivity(), Activity_List_of_booked_rooms.class)));

        refreshLayout.setOnRefreshListener(this::get_order_by_id_user);

        get_order_by_id_user();

        return view;
    }
    private void initView(View view){
        calendarView = view.findViewById(R.id.calendar_frm);
        rcv = view.findViewById(R.id.rcv_calendar);
        btn_see_all = view.findViewById(R.id.btn_see_all_calendar);
        refreshLayout = view.findViewById(R.id.refresh_calendar);
        tv_schedule = view.findViewById(R.id.tv_schedule);
        progressBar = view.findViewById(R.id.progressBar_schedule);
    }
    // Fetch order list based on user ID
    public void get_order_by_id_user() {
        progressBar.setVisibility(View.VISIBLE);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        Api_service.service.get_orderroom_status01(checkUid()).enqueue(new Callback<List<Order_Room>>() {
            @Override
            public void onResponse(Call<List<Order_Room>> call, Response<List<Order_Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    order_rooms = response.body();
                    adapter = new Adapter_schedule(order_rooms);
                    rcv.setAdapter(adapter);
                    if (order_rooms.isEmpty()) {
                        tv_schedule.setVisibility(View.VISIBLE);
                    } else {
                        tv_schedule.setVisibility(View.GONE);
                    }
                    adapter.setOnItemClickListener(new Adapter_schedule.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position, Order_Room order_room) {
                            updateCalendarView(order_room);
                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                } else {
                    Log.e("Response order by id", "onResponse: " + response.message());
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Order_Room>> call, Throwable throwable) {
                Log.e("Failure order by id", "onFailure: " + throwable.getMessage());
                refreshLayout.setRefreshing(false);
            }
        });
    }

    // Update Calendar view with booking dates
    private void updateCalendarView(Order_Room order_room) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date timeCheckIn = originalFormat.parse(order_room.getTimeGet());
            Date timeCheckOut = originalFormat.parse(order_room.getTimeCheckout());
            String dateCheckIn = dateFormat.format(timeCheckIn);
            String dateCheckOut = dateFormat.format(timeCheckOut);
            Date dateCheckIn1 = dateFormat.parse(dateCheckIn);
            Date dateCheckOut1 = dateFormat.parse(dateCheckOut);
            long startDateMillis = dateCheckIn1.getTime();
            long endDateMillis = dateCheckOut1.getTime();

            calendarView.setMinDate(startDateMillis);
            calendarView.setMaxDate(endDateMillis);

            Log.d("Date range", "Start Date: " + timeCheckIn + " End Date: " + timeCheckOut);

        } catch (ParseException e) {
            Log.e("Error parsing date", "onItemClick: " + e.getMessage());
        }
    }

    // Check UID or Email from SharedPreferences
    private String checkUid() {
        String uid = getSharedPreferenceData("uid");
        return (uid != null) ? uid : getSharedPreferenceData("email");
    }

    // Fetch user data from SharedPreferences
    private String getSharedPreferenceData(String key) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
