package phongtaph31865.poly.stayserene.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Messenger.Activiti_list_messenger;

public class Adapter_list_hotel_messenger extends RecyclerView.Adapter<Adapter_list_hotel_messenger.HotelViewHolder> {

    private Context context;
    private List<Hotel> hotelList;

    public Adapter_list_hotel_messenger(Context context, List<Hotel> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
    }

    @Override
    public HotelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rcv_messenger_list_hotel, parent, false);
        return new HotelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);

        holder.hotelName.setText(hotel.getTenKhachSan());
        Glide.with(holder.itemView.getContext()).load(hotel.getAnhKhachSan()).into(holder.hotelImage);

        holder.itemView.setOnClickListener(v -> {
            String hotelId = hotel.get_id();
            String hotelName = hotel.getTenKhachSan();

            Intent intent = new Intent(context, Activiti_list_messenger.class);
            intent.putExtra("IdKhachSan", hotelId);
            intent.putExtra("TenKhachSan", hotelName);

            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView hotelName;
        ImageView hotelImage;

        public HotelViewHolder(View itemView) {
            super(itemView);
            hotelName = itemView.findViewById(R.id.item_tv_name_hotel_messenger);
            hotelImage = itemView.findViewById(R.id.item_img_hotel_messenger);
        }
    }
}