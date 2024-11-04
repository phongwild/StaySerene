package phongtaph31865.poly.stayserene.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_schedule extends RecyclerView.Adapter<Adapter_schedule.ViewHolder>{
    private OnItemClickListener onItemClickListener;
    private List<Order_Room> order_rooms;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public Adapter_schedule(List<Order_Room> order_rooms) {
        this.order_rooms = order_rooms;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order_Room order_room = order_rooms.get(position);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String idRoom = order_room.getIdPhong();
        Api_service.service.get_rooms_byId(idRoom).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(response.isSuccessful()){
                    List<Room> rooms = response.body();
                    for(Room room : rooms){
                        String idTypeRoom = room.getIdLoaiPhong();
                        Api_service.service.get_typeroom_byId(idTypeRoom).enqueue(new Callback<List<TypeRoom>>() {
                            @Override
                            public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                                if(response.isSuccessful()){
                                    List<TypeRoom> typeRooms = response.body();
                                    for(TypeRoom typeRoom : typeRooms){
                                        String idHotel = typeRoom.getIdKhachSan();
                                        Api_service.service.get_hotel_byId(idHotel).enqueue(new Callback<List<Hotel>>() {
                                            @Override
                                            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                                                if (response.isSuccessful()){
                                                    for(Hotel hotel : response.body()){
                                                        holder.name.setText(hotel.getTenKhachSan());
                                                    }
                                                }else {
                                                    Log.e("Response hotel by id", "onResponse: " + response.message());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                                                Log.e("Failure hotel by id", "onFailure: " + throwable.getMessage());
                                            }
                                        });
                                    }
                                }else{
                                    Log.e("Response type room by id", "onResponse: " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                                Log.e("Failure type room by id", "onFailure: " + throwable.getMessage());
                            }
                        });
                    }
                }else{
                    Log.e("Response room by id", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("Failure room by id", "onFailure: " + throwable.getMessage());
            }
        });
        holder.date.setText(order_room.getTimeGet());
        holder.price.setText(formatter.format(order_room.getTotal()));
        Picasso.get().load(order_room.getImg()).into(holder.img);
        holder.btn_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(position, order_room);
                }
            }
        });
    }
    public interface OnItemClickListener{
        void onItemClick(int position, Order_Room order_room);
    }
    @Override
    public int getItemCount() {
        if(order_rooms != null){
            return order_rooms.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name, date, price;
        RelativeLayout btn_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_img_schedule);
            name = itemView.findViewById(R.id.item_name_schedule);
            date = itemView.findViewById(R.id.item_date_schedule);
            price = itemView.findViewById(R.id.item_price_schedule);
            btn_item = itemView.findViewById(R.id.btn_item_schedule);
        }
    }
}
