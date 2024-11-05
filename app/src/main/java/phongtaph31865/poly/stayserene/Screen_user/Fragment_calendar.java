package phongtaph31865.poly.stayserene.Screen_user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.adapter.Adapter_schedule;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_calendar extends Fragment {
    private CalendarView calendarView;
    private RecyclerView rcv;
    private TextView btn_see_all;
    private List<Order_Room> order_rooms = new ArrayList<Order_Room>();
    private Adapter_schedule adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        //Code Fragment
        calendarView = view.findViewById(R.id.calendar_frm);
        rcv = view.findViewById(R.id.rcv_calendar);
        btn_see_all = view.findViewById(R.id.btn_see_all_calendar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(layoutManager);
        get_order_by_id_user();
        return view;
    }
    public void get_order_by_id_user(){
        Api_service.service.get_orderroom_byUid(checkUid()).enqueue(new Callback<List<Order_Room>>() {
            @Override
            public void onResponse(Call<List<Order_Room>> call, Response<List<Order_Room>> response) {
                if(response.isSuccessful()){
                    order_rooms = response.body();
                    adapter = new Adapter_schedule(order_rooms);
                    rcv.setAdapter(adapter);
                    adapter.setOnItemClickListener(new Adapter_schedule.OnItemClickListener() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onItemClick(int position, Order_Room order_room) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                Date timeCheckIn = dateFormat.parse(order_room.getTimeGet());
                                Date timeCheckOut = dateFormat.parse(order_room.getTimeCheckout());

                                long startDateMillis = timeCheckIn.getTime();
                                long endDateMillis = timeCheckOut.getTime();

                                Calendar startCalendar = Calendar.getInstance();
                                startCalendar.setTimeInMillis(startDateMillis);

                                Calendar endCalendar = Calendar.getInstance();
                                endCalendar.setTimeInMillis(endDateMillis);

                                Log.d("Date range", "Start Date: " + startCalendar.getTime() +" End Date: " + endCalendar.getTime());

                                calendarView.setMinDate(startCalendar.getTimeInMillis());
                                calendarView.setMaxDate(endCalendar.getTimeInMillis());
                            } catch (ParseException e) {
                                Log.e("Error parsing date", "onItemClick: " + e.getMessage());
                            }
                        }
                    });
                    adapter.notifyDataSetChanged();
                }else{
                    Log.e("Response order by id", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Order_Room>> call, Throwable throwable) {
                Log.e("Failure order by id", "onFailure: " + throwable.getMessage());
            }
        });
    }
    public String checkUid(){
        if(getUsernameFromSharedPreferences() != null){
            return getUsernameFromSharedPreferences();
        }else if(getEmailFromSharedPreferences() != null){
            return getEmailFromSharedPreferences();
        }
        return "";
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }

    private String getEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }
}