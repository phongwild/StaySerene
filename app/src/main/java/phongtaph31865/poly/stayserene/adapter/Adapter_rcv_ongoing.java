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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel; // Assuming there's a Hotel model
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_rcv_ongoing extends RecyclerView.Adapter<Adapter_rcv_ongoing.ViewHolder> {
    private List<Order_Room> order_rooms;
    public Adapter_rcv_ongoing(List<Order_Room> order_rooms){
        this.order_rooms = order_rooms;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ongoing, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order_Room orderRoom = order_rooms.get(position);
        String idRoom = orderRoom.getIdPhong();
        Api_service.service.get_rooms_byId(idRoom).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful()) {
                    List<Room> rooms = response.body();
                    for (Room room : rooms) {
                        if (room.getAnhPhong() != null) {
                            Picasso.get().load(room.getAnhPhong()).into(holder.img);
                        }
                        String idTypeRoom = room.getIdLoaiPhong();
                        Api_service.service.get_typeroom_byId(idTypeRoom).enqueue(new Callback<List<TypeRoom>>() {
                            @Override
                            public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                                if (response.isSuccessful()) {
                                    List<TypeRoom> typeRooms = response.body();
                                    for (TypeRoom typeRoom : typeRooms){
                                        String idHotel = typeRoom.getIdKhachSan();
                                        Api_service.service.get_hotel_byId(idHotel).enqueue(new Callback<List<Hotel>>() {
                                            @Override
                                            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                                                if (response.isSuccessful()) {
                                                    List<Hotel> hotels = response.body();
                                                    for (Hotel hotel : hotels){
                                                        holder.ht_name.setText(hotel.getTenKhachSan());
                                                        holder.ht_location.setText(hotel.getDiaChi());
                                                    }
                                                }else Log.e("Error get hotel by id", response.message());
                                            }

                                            @Override
                                            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                                                Log.e("Error get hotel by id", throwable.getMessage());
                                            }
                                        });
                                    }
                                }else Log.e("Error get type room by id", response.message());
                            }

                            @Override
                            public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                                Log.e("Error get type room by id", throwable.getMessage());
                            }
                        });
                    }
                }else Log.e("Error get room by id", response.message());
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("Error get room by id", throwable.getMessage());
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

