package phongtaph31865.poly.stayserene.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
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
import phongtaph31865.poly.stayserene.databinding.ItemRcv2HomeBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_rcv2_home extends RecyclerView.Adapter<Adapter_rcv2_home.ViewHolder> {

    private List<Room> rooms;
    private Map<String, TypeRoom> typeRoomMap = new HashMap<>();
    private int visibleItemCount = 10; // Số lượng bản ghi hiển thị ban đầu
    private boolean isLoading = false; // Biến kiểm soát trạng thái đang tải

    public Adapter_rcv2_home(List<Room> rooms) {
        this.rooms = rooms;
        loadTypeRoomData();
    }

    public void loadMoreItems() {
        if (isLoading) return; // Nếu đang tải thì không làm gì
        isLoading = true;

        // Tăng số lượng item hiển thị
        int totalItems = rooms.size();
        int nextLimit = Math.min(visibleItemCount + 10, totalItems); // Load thêm 10 items

        if (visibleItemCount < nextLimit) {
            visibleItemCount = nextLimit;  // Cập nhật lại số lượng items hiển thị
            notifyDataSetChanged(); // Cập nhật lại giao diện
        }

        isLoading = false;  // Xong việc tải
    }

    // Tải dữ liệu loại phòng và lưu trữ vào Map
    private void loadTypeRoomData() {
        Api_service.service.get_typeroom().enqueue(new Callback<List<TypeRoom>>() {
            @Override
            public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (TypeRoom typeRoom : response.body()) {
                        typeRoomMap.put(typeRoom.get_id(), typeRoom);
                    }
                    notifyDataSetChanged(); // Cập nhật lại RecyclerView khi dữ liệu loại phòng đã sẵn sàng
                } else {
                    Log.e("Adapter_rcv2_home", "Không thể tải loại phòng");
                }
            }

            @Override
            public void onFailure(Call<List<TypeRoom>> call, Throwable t) {
                Log.e("Adapter_rcv2_home", "Lỗi khi tải loại phòng: " + t.getMessage());
            }
        });
    }

    // Sắp xếp theo giá
    private static final Comparator<Room> PRICE_ASCENDING = (o1, o2) -> Integer.compare(o1.getGiaPhong(), o2.getGiaPhong());
    private static final Comparator<Room> PRICE_DESCENDING = (o1, o2) -> Integer.compare(o2.getGiaPhong(), o1.getGiaPhong());

    public void filterPrice(boolean isAscending) {
        if (isAscending) {
            Collections.sort(rooms, PRICE_ASCENDING);
        } else {
            Collections.sort(rooms, PRICE_DESCENDING);
        }
        notifyDataSetChanged(); // Cập nhật lại giao diện sau khi sắp xếp
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRcv2HomeBinding binding = ItemRcv2HomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);
        if (room != null) {
            holder.bind(room, typeRoomMap.get(room.getIdLoaiPhong()), v -> showDetail(room, v));
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(visibleItemCount, rooms.size()); // Số lượng item cần hiển thị, tối đa là visibleItemCount
    }

    // Hiển thị chi tiết phòng
    private void showDetail(Room room, View v) {
        Intent intent = new Intent(v.getContext(), Detail_room_screen.class);
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

    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemRcv2HomeBinding binding;

        public ViewHolder(@NonNull ItemRcv2HomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Room room, TypeRoom typeRoom, View.OnClickListener onClickListener) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            // Cập nhật thông tin phòng
            binding.itemTvNameRcv2.setText(typeRoom != null ? typeRoom.getTenLoaiPhong() : "Unknown");
            binding.itemTvPriceRcv2.setText(typeRoom != null ? formatter.format(typeRoom.getGiaLoaiPhong()) : "N/A");
            binding.itemTvAddressRcv2.setText(room.getTinhTrangPhong() == 0 ? "Open" : "Close");

            // Hiển thị ảnh phòng
            Glide.with(binding.getRoot().getContext())
                    .load(room.getAnhPhong())
                    .fitCenter()
                    .into(binding.itemImgRcv2);

            // Xử lý sự kiện click
            binding.itemBtnShowDetailRcv2.setOnClickListener(onClickListener);
        }
    }
}
