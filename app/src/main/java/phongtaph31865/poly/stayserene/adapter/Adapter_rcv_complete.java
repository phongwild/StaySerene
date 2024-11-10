package phongtaph31865.poly.stayserene.adapter;

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

public class Adapter_rcv_complete extends RecyclerView.Adapter<Adapter_rcv_complete.ViewHolder> {
    List<Order_Room> order_rooms;

    public Adapter_rcv_complete(List<Order_Room> order_rooms) {
        this.order_rooms = order_rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complete, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order_Room orderRoom = order_rooms.get(position);
        loadRoomDetails(holder, orderRoom);
    }

    private void loadRoomDetails(ViewHolder holder, Order_Room orderRoom) {
        String idRoom = orderRoom.getIdPhong();

        // Gọi API để lấy thông tin phòng
        Api_service.service.get_rooms_byId(idRoom).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Room room = response.body().get(0); // Giả sử mỗi phòng chỉ có 1 phòng duy nhất
                    updateRoomImage(holder, room);
                    loadTypeRoomDetails(holder, room);
                } else {
                    Log.e("Error", "Failed to get room details");
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("Error", throwable.getMessage());
            }
        });
    }

    private void updateRoomImage(ViewHolder holder, Room room) {
        if (room.getAnhPhong() != null) {
            Picasso.get().load(room.getAnhPhong()).into(holder.img);
        }
    }

    private void loadTypeRoomDetails(ViewHolder holder, Room room) {
        String idTypeRoom = room.getIdLoaiPhong();

        // Gọi API để lấy thông tin loại phòng
        Api_service.service.get_typeroom_byId(idTypeRoom).enqueue(new Callback<List<TypeRoom>>() {
            @Override
            public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TypeRoom typeRoom = response.body().get(0); // Giả sử mỗi loại phòng chỉ có 1
                    loadHotelDetails(holder, typeRoom);
                } else {
                    Log.e("Error", "Failed to get type room details");
                }
            }

            @Override
            public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                Log.e("Error", throwable.getMessage());
            }
        });
    }

    private void loadHotelDetails(ViewHolder holder, TypeRoom typeRoom) {
        String idHotel = typeRoom.getIdKhachSan();

        // Gọi API để lấy thông tin khách sạn
        Api_service.service.get_hotel_byId(idHotel).enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Hotel hotel = response.body().get(0); // Giả sử mỗi khách sạn chỉ có 1
                    holder.ht_name.setText(hotel.getTenKhachSan());
                    holder.ht_location.setText(hotel.getDiaChi());
                } else {
                    Log.e("Error", "Failed to get hotel details");
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                Log.e("Error", throwable.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (order_rooms != null) {
            return order_rooms.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView ht_name;
        private final TextView ht_location;
        private final TextView status;
        private final ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ht_name = itemView.findViewById(R.id.hotel_name);
            ht_location = itemView.findViewById(R.id.hotel_location);
            status = itemView.findViewById(R.id.status);
            img = itemView.findViewById(R.id.hotel_image);
        }
    }
}