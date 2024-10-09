package phongtaph31865.poly.stayserene.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Detail_room_screen;

public class Adapter_rcv2_home extends FirebaseRecyclerAdapter<Room, Adapter_rcv2_home.ViewHolder> {
    private String Uid;
    private Context context;

    public Adapter_rcv2_home(@NonNull FirebaseRecyclerOptions<Room> options, Context context) {
        super(options);
        this.context = context;
    }

    public void setUid(String uid) {
        Uid = uid;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv2_home, parent, false);
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Room room) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        int status = room.getTinhTrangPhong();
        if (status == 0) {
            viewHolder.tv_name.setText("Open");
        } else if (status == 1) {
            viewHolder.tv_name.setText("Close");
        }
        viewHolder.tv_address.setText(room.getMoTaPhong());
        viewHolder.tv_price.setText(formatter.format(room.getGiaPhong()));
        Picasso.get().load(room.getAnhPhong()).into(viewHolder.img);
        viewHolder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail(room);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_address, tv_price;
        private ImageView img;
        private LinearLayout btn_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.item_tv_name_rcv2);
            tv_address = itemView.findViewById(R.id.item_tv_address_rcv2);
            tv_price = itemView.findViewById(R.id.item_tv_price_rcv2);
            img = itemView.findViewById(R.id.item_img_rcv2);
            btn_detail = itemView.findViewById(R.id.item_btn_showDetail_rcv2);
        }
    }
    public void showDetail(Room room){
        Intent intent = new Intent(context, Detail_room_screen.class);
        intent.putExtra("uid", Uid);
        intent.putExtra("IdRoom", room.getIdPhong());
        intent.putExtra("IdTypeRoom", room.getIdLoaiPhong());
        intent.putExtra("img", room.getAnhPhong());
        intent.putExtra("price", room.getGiaPhong());
        intent.putExtra("status", room.getTinhTrangPhong());
        intent.putExtra("floor", room.getSoTang());
        intent.putExtra("desc", room.getMoTaPhong());
        context.startActivity(intent);
    }
}
