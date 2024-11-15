package phongtaph31865.poly.stayserene.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel; // Assuming there's a Hotel model
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom.Activity_show_detail_booking;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Order_Room orderRoom = order_rooms.get(position);
        loadStatus(holder, orderRoom);
        loadRoomDetails(holder, orderRoom, position);
        setupCancelButton(holder, orderRoom, position);
        Intent intent = createIntentWithExtras(holder, orderRoom);
        holder.btn_show_ticket.setOnClickListener(v -> v.getContext().startActivity(intent));
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
    private void loadStatus(ViewHolder holder, Order_Room orderRoom) {
        if (orderRoom.getStatus() == 0) {
            holder.status.setText("Paid");
        }else if(orderRoom.getStatus() == 1){
            holder.status.setText("Checked in");
        }
    }
    private void loadRoomDetails(ViewHolder holder, Order_Room orderRoom, int position) {
        String idRoom = orderRoom.getIdPhong();
        Api_service.service.get_rooms_byId(idRoom).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Room room = response.body().get(0); // Giả sử mỗi phòng trả về chỉ có 1 phòng
                    loadRoomImage(holder, room);
                    loadHotelDetails(holder, room, position);
                } else {
                    Log.e("Error get room by id", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("Error get room by id", throwable.getMessage());
            }
        });
    }

    private void loadRoomImage(ViewHolder holder, Room room) {
        if (room.getAnhPhong() != null) {
            Picasso.get().load(room.getAnhPhong()).fit().centerCrop().into(holder.img);
        }
    }

    private void loadHotelDetails(ViewHolder holder, Room room, int position) {
        String idTypeRoom = room.getIdLoaiPhong();
        Api_service.service.get_typeroom_byId(idTypeRoom).enqueue(new Callback<List<TypeRoom>>() {
            @Override
            public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String idHotel = response.body().get(0).getIdKhachSan();
                    loadHotelById(holder, idHotel, position);
                } else {
                    Log.e("Error get type room by id", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                Log.e("Error get type room by id", throwable.getMessage());
            }
        });
    }

    private void loadHotelById(ViewHolder holder, String idHotel, int position) {
        Api_service.service.get_hotel_byId(idHotel).enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Hotel hotel = response.body().get(0);
                    holder.ht_name.setText(hotel.getTenKhachSan());
                    holder.ht_location.setText(hotel.getDiaChi());
                } else {
                    Log.e("Error get hotel by id", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                Log.e("Error get hotel by id", throwable.getMessage());
            }
        });
    }

    private void setupCancelButton(ViewHolder holder, Order_Room orderRoom, int position) {
        holder.btn_cancel.setOnClickListener(v -> {
            showCancelDialog(holder.itemView.getContext(), orderRoom, position);
        });
    }

    private void showCancelDialog(Context context, Order_Room orderRoom, int position) {
        PopupDialog.getInstance(context)
                .standardDialogBuilder()
                .createIOSDialog()
                .setHeading("Cancel Booking")
                .setDescription("Are you sure you want cancel this booking? This action cannot be undone.")
                .setPositiveButtonText("Yes")
                .build(new StandardDialogActionListener() {
                    @Override
                    public void onPositiveButtonClicked(Dialog dialog) {
                        cancelBooking(orderRoom, position, dialog);
                    }

                    @Override
                    public void onNegativeButtonClicked(Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void cancelBooking(Order_Room orderRoom, int position, Dialog dialog) {
        Room room = new Room();
        room.setTinhTrangPhong(0);
        Api_service.service.update_rooms(orderRoom.getIdPhong(), room).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful()) {
                    orderRoom.setStatus(3);
                    Api_service.service.update_orderroom(orderRoom.get_id(), orderRoom).enqueue(new Callback<List<Order_Room>>() {
                        @Override
                        public void onResponse(Call<List<Order_Room>> call, Response<List<Order_Room>> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(dialog.getContext(), "Cancel booking successfully", Toast.LENGTH_SHORT).show();
                                order_rooms.set(position, orderRoom); // Cập nhật lại dữ liệu trong list
                                notifyItemChanged(position); // Cập nhật lại item trong RecyclerView
                                dialog.dismiss();
                            } else {
                                Log.e("Error update order room", response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Order_Room>> call, Throwable throwable) {
                            Log.e("Error update order room", throwable.getMessage());
                            dialog.dismiss();
                        }
                    });
                } else {
                    Log.e("Error update room", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable throwable) {
                Log.e("Error update room", throwable.getMessage());
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
        private TextView ht_name, ht_location, status, btn_cancel, btn_show_ticket;
        private ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ht_name = itemView.findViewById(R.id.hotel_name);
            ht_location = itemView.findViewById(R.id.hotel_location);
            status = itemView.findViewById(R.id.status);
            img = itemView.findViewById(R.id.hotel_image);
            btn_cancel = itemView.findViewById(R.id.cancelBookingButton);
            btn_show_ticket = itemView.findViewById(R.id.btn_view_ticket3);
        }
    }
}

