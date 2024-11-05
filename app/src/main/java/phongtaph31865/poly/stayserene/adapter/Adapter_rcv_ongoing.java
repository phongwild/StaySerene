package phongtaph31865.poly.stayserene.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import phongtaph31865.poly.stayserene.Model.Hotel; // Assuming there's a Hotel model
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.R;

public class Adapter_rcv_ongoing extends FirebaseRecyclerAdapter<Order_Room, Adapter_rcv_ongoing.ViewHolder> {
    private Context context;

    public Adapter_rcv_ongoing(@NonNull FirebaseRecyclerOptions<Order_Room> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Adapter_rcv_ongoing.ViewHolder viewHolder, int position, @NonNull Order_Room booking) {
//        if (booking == null || (!"Paid".equals(booking.getTrangThai()) && !"Deposited".equals(booking.getTrangThai()))) {
//            viewHolder.itemView.setVisibility(View.GONE);
//            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//            return;
//        }

        // Nếu trạng thái là "Paid", hiển thị item bình thường
        DatabaseReference hotelRef = FirebaseDatabase.getInstance().getReference("KhachSan").child(String.valueOf(booking.getIdPhong()));
        hotelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Hotel hotel = snapshot.getValue(Hotel.class);

                    if (hotel != null) {
                        viewHolder.ht_name.setText(hotel.getTenKhachSan());
                        viewHolder.ht_location.setText(hotel.getDiaChi());

                        if (hotel.getAnhKhachSan() != null && !hotel.getAnhKhachSan().isEmpty()) {
                            Picasso.get().load(hotel.getAnhKhachSan()).into(viewHolder.img);
                        } else {
                            viewHolder.img.setImageResource(R.drawable.hotel_popular_image);
                        }
                    }
                } else {
                    viewHolder.ht_name.setText("Khách sạn không tìm thấy");
                    viewHolder.ht_location.setText("");
                    viewHolder.img.setImageResource(R.drawable.hotel_popular_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

//        viewHolder.status.setText(booking.getTrangThai() != null ? booking.getTrangThai() : "Chưa xác định");
    }

    @NonNull
    @Override
    public Adapter_rcv_ongoing.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ongoing, parent, false);
        return new Adapter_rcv_ongoing.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ht_name, ht_location, status;
        private ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ht_name = itemView.findViewById(R.id.hotel_name);
            ht_location = itemView.findViewById(R.id.hotel_location);
            status = itemView.findViewById(R.id.status);
            img = itemView.findViewById(R.id.hotel_image);
        }
    }
}

