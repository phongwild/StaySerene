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

public class Adapter_rcv_complete extends RecyclerView.Adapter<Adapter_rcv_complete.ViewHolder> {
    private Context context;
    private List<Order_Room> orderroomslist;

    public Adapter_rcv_complete(List<Order_Room> orderroomslist) {
        this.context = context;
        this.orderroomslist = orderroomslist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complete, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Order_Room orderRoom1 = orderroomslist.get(position);
        Picasso.get().load(orderRoom1.getImg()).into(viewHolder.img);
        String idRoom1 = orderRoom1.getIdPhong();
        Api_service.service.get_rooms_byId(idRoom1).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        for (Room room: response.body()){
                            String idTypeRoom = room.getIdLoaiPhong();
                            Api_service.service.get_typeroom_byId(idTypeRoom).enqueue(new Callback<List<TypeRoom>>() {
                                @Override
                                public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                                    if (response.isSuccessful()){
                                        if (response.body() != null){
                                            for (TypeRoom typeRoom : response.body()){
                                                String idht = typeRoom.getIdKhachSan();
                                                Api_service.service.get_hotel_byId(idht).enqueue(new Callback<List<Hotel>>() {
                                                    @Override
                                                    public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                                                        if (response.isSuccessful()){
                                                            if (response.body() != null){
                                                                for (Hotel hotel : response.body()){
                                                                    viewHolder.ht_name.setText(hotel.getTenKhachSan());
                                                                    viewHolder.ht_location.setText(hotel.getDiaChi());
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
                                public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
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
        return orderroomslist != null ? orderroomslist.size() : 0;
    }

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
