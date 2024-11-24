package phongtaph31865.poly.stayserene.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Activity_list_type_room;

public class Adapter_more_hotel extends RecyclerView.Adapter<Adapter_more_hotel.ViewHolder> {
    private List<Hotel> hotels;

    public Adapter_more_hotel(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_hotel, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Hotel hotel = hotels.get(position);
        viewHolder.tv_name.setText(hotel.getTenKhachSan());
        viewHolder.tv_rate.setText(String.valueOf(hotel.getDanhGia()));
        viewHolder.tv_address.setText(hotel.getDiaChi());
        if (hotel.getAnhKhachSan() != null) {
            Picasso picasso = Picasso.get();
            picasso.setIndicatorsEnabled(true);
            picasso.load(hotel.getAnhKhachSan()).into(viewHolder.img);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentClick(v, hotel);
            }
        });
    }

    private void IntentClick(View v, Hotel hotel) {
        Intent intent = new Intent(v.getContext(), Activity_list_type_room.class);
        intent.putExtra("id", hotel.get_id());
        intent.putExtra("name", hotel.getTenKhachSan());
        v.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return (hotels != null) ? hotels.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_rate, tv_address;
        private ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.item_tv_name_more_hotel);
            tv_rate = itemView.findViewById(R.id.item_tv_rating_more_hotel);
            tv_address = itemView.findViewById(R.id.item_tv_address_more_hotel);
            img = itemView.findViewById(R.id.item_img_more_hotel);
        }
    }
}
