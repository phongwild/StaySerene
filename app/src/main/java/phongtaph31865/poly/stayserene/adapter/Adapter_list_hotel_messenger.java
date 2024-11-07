package phongtaph31865.poly.stayserene.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

        // Set hotel name
        holder.hotelName.setText(hotel.getTenKhachSan());

        // Load image using Picasso (or Glide)
        Picasso.get().load(hotel.getAnhKhachSan()).into(holder.hotelImage);

        // Set item click listener
        holder.itemView.setOnClickListener(v -> {
            // Get IdKhachSan and TenKhachSan
            String hotelId = hotel.get_id();
            String hotelName = hotel.getTenKhachSan();

            // Create an intent to start Activity_list_messenger
            Intent intent = new Intent(context, Activiti_list_messenger.class);
            intent.putExtra("IdKhachSan", hotelId);
            intent.putExtra("TenKhachSan", hotelName);

            // Start Activity_list_messenger
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
