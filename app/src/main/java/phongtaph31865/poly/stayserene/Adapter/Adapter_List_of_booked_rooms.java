package phongtaph31865.poly.stayserene.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom.Activity_show_detail_booking;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_List_of_booked_rooms extends RecyclerView.Adapter<Adapter_List_of_booked_rooms.ViewHolder> {
    private final List<Order_Room> orderRooms;
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private Adapter_schedule.OnItemClickListener onItemClickListener;

    public Adapter_List_of_booked_rooms(List<Order_Room> orderRooms) {
        this.orderRooms = orderRooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_of_booked_rooms, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order_Room orderRoom = orderRooms.get(position);
        SimpleDateFormat originalFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy"); // Note the spaces around the hyphen

        // Định dạng mới chỉ để lấy dd/MM/yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date timeCheckIn = originalFormat.parse(orderRoom.getTimeGet());
            Date timeCheckOut = originalFormat.parse(orderRoom.getTimeCheckout());
            String dateCheckIn = dateFormat.format(timeCheckIn);
            String dateCheckOut = dateFormat.format(timeCheckOut);
            holder.start_date.setText(dateCheckIn);
            holder.end_date.setText(dateCheckOut);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        holder.price.setText(formatter.format(orderRoom.getTotal()));
// Set up Intent with orderRoom data
        Intent intent = createIntentWithExtras(holder, orderRoom);

        // Load hotel details based on room ID
        loadHotelDetails(orderRoom.getIdPhong(), holder, intent);

        holder.btn_item.setOnClickListener(v -> v.getContext().startActivity(intent));
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, orderRoom);
            }
        });
    }

    private Intent createIntentWithExtras(ViewHolder holder, Order_Room orderRoom) {
        Intent intent = new Intent(holder.itemView.getContext(), Activity_show_detail_booking.class);
        intent.putExtra("id_room", orderRoom.getIdPhong());
        intent.putExtra("time_checkin", orderRoom.getTimeGet());
        intent.putExtra("time_checkout", orderRoom.getTimeCheckout());
        intent.putExtra("total", orderRoom.getTotal());
        intent.putExtra("img", orderRoom.getImg());
        intent.putExtra("note", orderRoom.getNote());
        return intent;
    }

    private void loadHotelDetails(String roomId, ViewHolder holder, Intent intent) {
        Api_service.service.get_rooms_byId(roomId).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Room room = response.body().get(0);
                    Picasso.get().load(room.getAnhPhong()).fit().centerCrop().into(holder.img);
                    loadTypeRoomDetails(room.getIdLoaiPhong(), holder, intent);
                } else {
                    Log.e("Room Fetch", "Failed to fetch room details: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("Room Fetch Failure", throwable.getMessage());
            }
        });
    }

    private void loadTypeRoomDetails(String typeRoomId, ViewHolder holder, Intent intent) {
        Api_service.service.get_typeroom_byId(typeRoomId).enqueue(new Callback<List<TypeRoom>>() {
            @Override
            public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    TypeRoom typeRoom = response.body().get(0);
                    loadHotelName(typeRoom.getIdKhachSan(), holder, intent);
                } else {
                    Log.e("Type Room Fetch", "Failed to fetch type room details: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                Log.e("Type Room Fetch Failure", throwable.getMessage());
            }
        });
    }

    private void loadHotelName(String hotelId, ViewHolder holder, Intent intent) {
        Api_service.service.get_hotel_byId(hotelId).enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Hotel hotel = response.body().get(0);
                    holder.name.setText(hotel.getTenKhachSan());
                    intent.putExtra("name_hotel", hotel.getTenKhachSan());
                } else {
                    Log.e("Hotel Fetch", "Failed to fetch hotel details: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                Log.e("Hotel Fetch Failure", throwable.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (orderRooms != null) ? orderRooms.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, start_date, end_date, price;
        RelativeLayout btn_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_img_booking);
            name = itemView.findViewById(R.id.item_name_booking);
            start_date = itemView.findViewById(R.id.item_start_date_booking);
            end_date = itemView.findViewById(R.id.item_end_date_booking);
            price = itemView.findViewById(R.id.item_price_booking);
            btn_item = itemView.findViewById(R.id.btn_item_booking);
        }
    }
}