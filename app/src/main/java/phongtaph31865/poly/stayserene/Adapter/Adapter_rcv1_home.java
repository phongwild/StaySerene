package phongtaph31865.poly.stayserene.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Activity_list_type_room;
import phongtaph31865.poly.stayserene.databinding.ItemRcvHome1Binding;

public class Adapter_rcv1_home extends RecyclerView.Adapter<Adapter_rcv1_home.ViewHolder> {
    private List<Hotel> hotels;

    public Adapter_rcv1_home(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRcvHome1Binding binding = ItemRcvHome1Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Hotel hotel = hotels.get(position);

        // Cập nhật UI thông qua ViewBinding
        viewHolder.binding.itemTvNameRcv1.setText(hotel.getTenKhachSan());
        viewHolder.binding.itemTvRatingRcv1.setText(String.valueOf(hotel.getDanhGia()));
        viewHolder.binding.itemTvAddressRcv1.setText(hotel.getDiaChi());

        if (hotel.getAnhKhachSan() != null) {
            Glide.with(viewHolder.itemView.getContext()).load(hotel.getAnhKhachSan()).into(viewHolder.binding.itemImgRcv1);
        }

        // Xử lý sự kiện click
        viewHolder.itemView.setOnClickListener(v -> IntentClick(v, hotel));
    }

    @Override
    public int getItemCount() {
        return (hotels != null) ? hotels.size() : 0;
    }

    // Hàm mở Activity khi nhấn vào item
    private void IntentClick(View v, Hotel hotel) {
        Intent intent = new Intent(v.getContext(), Activity_list_type_room.class);
        intent.putExtra("id", hotel.get_id());
        intent.putExtra("name", hotel.getTenKhachSan());
        v.getContext().startActivity(intent);
    }

    // ViewHolder sử dụng ViewBinding
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRcvHome1Binding binding;

        public ViewHolder(ItemRcvHome1Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
