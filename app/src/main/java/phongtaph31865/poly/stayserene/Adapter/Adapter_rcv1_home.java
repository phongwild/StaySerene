package phongtaph31865.poly.stayserene.Adapter;

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

public class Adapter_rcv1_home extends RecyclerView.Adapter<Adapter_rcv1_home.ViewHolder> {
    private List<Hotel> hotels;
    public Adapter_rcv1_home(List<Hotel> hotels) {
        this.hotels = hotels;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_home_1, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Hotel hotel = hotels.get(position);
        viewHolder.tv_name.setText(hotel.getTenKhachSan());
        viewHolder.tv_rate.setText(String.valueOf(hotel.getDanhGia()));
        viewHolder.tv_address.setText(hotel.getDiaChi());
        if(hotel.getAnhKhachSan() != null){
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

    @Override
    public int getItemCount() {
        if (hotels != null) {
            return hotels.size();
        }
        return 0;
    }
    private void IntentClick(View v, Hotel hotel){
        Intent intent = new Intent(v.getContext(), Activity_list_type_room.class);
        intent.putExtra("id", hotel.get_id());
        intent.putExtra("name", hotel.getTenKhachSan());
        v.getContext().startActivity(intent);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_rate, tv_address;
        private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.item_tv_name_rcv1);
            tv_rate = itemView.findViewById(R.id.item_tv_rating_rcv1);
            tv_address = itemView.findViewById(R.id.item_tv_address_rcv1);
            img = itemView.findViewById(R.id.item_img_rcv1);
        }
    }
}
