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
import com.squareup.picasso.Picasso;

import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.R;

public class Adapter_rcv1_home extends FirebaseRecyclerAdapter<Hotel, Adapter_rcv1_home.ViewHolder> {
    private Context context;

    public Adapter_rcv1_home(@NonNull FirebaseRecyclerOptions<Hotel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Hotel hotel) {
        viewHolder.tv_name.setText(hotel.getTenKhachSan());
        viewHolder.tv_rate.setText(String.valueOf(hotel.getDanhGia()));
        viewHolder.tv_address.setText(hotel.getDiaChi());
        if(hotel.getAnhKhachSan() != null){
            Picasso.get().load(hotel.getAnhKhachSan()).into(viewHolder.img);
        }
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_home_1, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_rate, tv_address, tv_price;
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
