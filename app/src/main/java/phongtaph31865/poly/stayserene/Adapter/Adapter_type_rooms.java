package phongtaph31865.poly.stayserene.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom.Activity_detail_type_rooms;
import phongtaph31865.poly.stayserene.databinding.ItemListTypeRoomsBinding;

public class Adapter_type_rooms extends RecyclerView.Adapter<Adapter_type_rooms.ViewHolder> {
    private List<TypeRoom> list_type_rooms;

    public Adapter_type_rooms(List<TypeRoom> list_type_rooms) {
        this.list_type_rooms = list_type_rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListTypeRoomsBinding binding = ItemListTypeRoomsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TypeRoom typeRoom = list_type_rooms.get(position);

        // Cập nhật UI thông qua ViewBinding
        holder.binding.itemTvNameTypeRooms.setText(typeRoom.getTenLoaiPhong());
        holder.binding.itemTvDescTypeRooms.setText(typeRoom.getMoTaLoaiPhong());

        // Sử dụng Glide để tải ảnh
        Glide.with(holder.itemView.getContext()).load(typeRoom.getAnhLoaiPhong()).into(holder.binding.itemIvTypeRooms);

        // Xử lý sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Activity_detail_type_rooms.class);
            intent.putExtra("id_type_room", typeRoom.get_id());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (list_type_rooms != null) ? list_type_rooms.size() : 0;
    }

    // ViewHolder sử dụng ViewBinding
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemListTypeRoomsBinding binding;

        public ViewHolder(ItemListTypeRoomsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
