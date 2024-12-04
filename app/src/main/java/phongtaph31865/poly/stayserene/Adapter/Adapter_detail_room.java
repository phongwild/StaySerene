package phongtaph31865.poly.stayserene.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Detail_room_screen;
import phongtaph31865.poly.stayserene.databinding.ItemDetailRoomBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_detail_room extends RecyclerView.Adapter<Adapter_detail_room.ViewHolder> {
    private String Uid;
    private List<Room> rooms;
    private List<Room> roomList;
    private Map<String, String> typeRoomMap = new HashMap<>(); // Cache tên loại phòng

    public Adapter_detail_room(List<Room> rooms) {
        this.rooms = rooms;
        this.roomList = new ArrayList<>(rooms);
        fetchTypeRooms(); // Load danh sách loại phòng một lần
    }

    // Lọc theo giá phòng
    public void filterPrice(boolean isAscending) {
        Collections.sort(rooms, (o1, o2) -> isAscending
                ? Integer.compare(o1.getGiaPhong(), o2.getGiaPhong())
                : Integer.compare(o2.getGiaPhong(), o1.getGiaPhong()));
    }

    // Lọc danh sách phòng theo ID khách sạn
    public void filterHotel(String idHotel) {
        Api_service.service.get_typeroom_byId_hotel(idHotel).enqueue(new Callback<List<TypeRoom>>() {
            @Override
            public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TypeRoom> typeRooms = response.body();
                    List<String> idTypeList = new ArrayList<>();
                    for (TypeRoom typeRoom : typeRooms) {
                        idTypeList.add(typeRoom.get_id());
                    }

                    // Lọc danh sách phòng theo loại phòng
                    rooms.clear();
                    for (Room room : roomList) {
                        if (idTypeList.contains(room.getIdLoaiPhong())) {
                            rooms.add(room);
                        }
                    }

                    notifyDataSetChanged();
                } else {
                    Log.e("Detail Room", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                Log.e("Detail Room", "Error: " + throwable.getMessage());
            }
        });
    }

    // Load tên loại phòng và cache
    private void fetchTypeRooms() {
        Api_service.service.get_typeroom().enqueue(new Callback<List<TypeRoom>>() {
            @Override
            public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (TypeRoom typeRoom : response.body()) {
                        typeRoomMap.put(typeRoom.get_id(), typeRoom.getTenLoaiPhong());
                    }
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                Log.e("Detail Room", "Error fetching type rooms: " + throwable.getMessage());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDetailRoomBinding binding = ItemDetailRoomBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Hiển thị tình trạng phòng
        holder.binding.itemTvAddressDetailRoom.setText(room.getTinhTrangPhong() == 0 ? "Open" : "Close");

        // Hiển thị tên loại phòng từ cache
        String idType = room.getIdLoaiPhong();
        holder.binding.itemTvNameDetailRoom.setText(typeRoomMap.getOrDefault(idType, "Unknown"));

        // Hiển thị giá phòng
        holder.binding.itemTvPriceDetailRoom.setText(formatter.format(room.getGiaPhong()));

        // Hiển thị ảnh phòng
        Glide.with(holder.binding.getRoot().getContext())
                .load(room.getAnhPhong())
                .fitCenter()
                .into(holder.binding.itemIvDetailRoom);

        // Xử lý sự kiện nhấn nút chi tiết
        holder.binding.itemDetailRoom.setOnClickListener(v -> showDetail(room, v));
    }

    @Override
    public int getItemCount() {
        return rooms != null ? rooms.size() : 0;
    }

    public void showDetail(Room room, View v) {
        Intent intent = new Intent(v.getContext(), Detail_room_screen.class);
        intent.putExtra("uid", Uid);
        intent.putExtra("IdRoom", room.get_id());
        intent.putExtra("IdTypeRoom", room.getIdLoaiPhong());
        intent.putExtra("img", room.getAnhPhong());
        intent.putExtra("price", room.getGiaPhong());
        intent.putExtra("status", room.getTinhTrangPhong());
        intent.putExtra("floor", room.getSoTang());
        intent.putExtra("desc", room.getMoTaPhong());
        intent.putExtra("numberroom", room.getSoPhong());
        v.getContext().startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemDetailRoomBinding binding;

        public ViewHolder(ItemDetailRoomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
