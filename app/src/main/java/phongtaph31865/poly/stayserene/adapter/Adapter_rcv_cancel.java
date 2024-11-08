package phongtaph31865.poly.stayserene.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_rcv_cancel extends RecyclerView.Adapter<Adapter_rcv_cancel.ViewHolder> {
    private Context context;
    private List<Order_Room> orderRoomLists;

    // Constructor nhận danh sách Order_Room
    public Adapter_rcv_cancel(List<Order_Room> orderRoomLists) {
        this.context = context;
        this.orderRoomLists = orderRoomLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_cancel
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cancel, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Order_Room orderRooms = orderRoomLists.get(position);
        Picasso.get().load(orderRooms.getImg()).into(viewHolder.img);
        String idRooms = orderRooms.getIdPhong();

        viewHolder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Api_service.service.xoaLoaiPhong(idRooms).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("Delete TypeRoom", "Loại phòng đã được xóa thành công");
                        } else {
                            Log.e("Delete TypeRoom", "Không thể xóa loại phòng: " + response.code() + ", " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Delete TypeRoom", "Lỗi: " + t.getMessage());

                    }
                });


            }
        });

        Api_service.service.get_rooms_byId(idRooms).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        for (Room room: response.body()){
                            String idht = room.getIdLoaiPhong();
                            Api_service.service.get_hotel_byId(idht).enqueue(new Callback<List<Hotel>>() {
                                @Override
                                public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                                    if (response.isSuccessful()){
                                        if (response.body() != null){
                                            for (Hotel hotel: response.body()){
                                                viewHolder.ht_name.setText(hotel.getTenKhachSan());
                                                viewHolder.ht_location.setText(hotel.getDiaChi());
                                                viewHolder.status.setText(hotel.getStatus());

                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                                    Log.e("Failure get ht by id", throwable.getMessage());

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("Failure get ht by id", throwable.getMessage());

            }
        });
    }

    @Override
    public int getItemCount() {
        // Trả về kích thước của danh sách orderRoomList
        return orderRoomLists != null ? orderRoomLists.size() : 0;
    }

    // ViewHolder chứa các thành phần giao diện của item_cancel
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ht_name, ht_location, status;
        private ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ht_name = itemView.findViewById(R.id.hotel_name);
            ht_location = itemView.findViewById(R.id.hotel_location);
            status = itemView.findViewById(R.id.status);
            img = itemView.findViewById(R.id.hotel_image);
        }
    }
}
