package phongtaph31865.poly.stayserene.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

import java.text.NumberFormat;
import java.util.Locale;

import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Detail_screen;

public class Adapter_rcv1_home extends FirebaseRecyclerAdapter<Hotel, Adapter_rcv1_home.ViewHolder> {
    private String Uid;
    private Context context;

    public void setUid(String uid) {
        Uid = uid;
    }

    public Adapter_rcv1_home(@NonNull FirebaseRecyclerOptions<Hotel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Hotel hotel) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        viewHolder.tv_name.setText(hotel.getTenKhachSan());
        viewHolder.tv_rate.setText(String.valueOf(hotel.getDanhGia()));
        viewHolder.tv_address.setText(hotel.getDiaChi());
        viewHolder.tv_price.setText(String.valueOf(formatter.format(hotel.getHt_price())));
        if(hotel.getAnhKhachSan() != null){
            Picasso.get().load(hotel.getAnhKhachSan()).into(viewHolder.img);
        }
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDetail(hotel);
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
            tv_price = itemView.findViewById(R.id.item_tv_price_rcv1);
            img = itemView.findViewById(R.id.item_img_rcv1);
        }
    }
    private void gotoDetail(Hotel hotel){
        Intent intent = new Intent(context, Detail_screen.class);
        intent.putExtra("uid", Uid);
        intent.putExtra("img", hotel.getAnhKhachSan());
        intent.putExtra("name", hotel.getTenKhachSan());
        intent.putExtra("rate", String.valueOf(hotel.getDanhGia()));
        intent.putExtra("address", hotel.getDiaChi());
        intent.putExtra("price", String.valueOf(hotel.getHt_price()));
        intent.putExtra("desc", hotel.getMoTaKhachSan());
        context.startActivity(intent);
    }
}
