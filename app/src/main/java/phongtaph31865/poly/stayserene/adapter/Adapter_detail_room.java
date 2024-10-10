package phongtaph31865.poly.stayserene.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.text.NumberFormat;
import java.util.Locale;

import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Detail_room_screen;

public class Adapter_detail_room extends FirebaseRecyclerAdapter<Room, Adapter_detail_room.ViewHolder> {
    private final Context context;
    private String Uid;

    public Adapter_detail_room(@NonNull FirebaseRecyclerOptions<Room> options, Context context) {
        super(options);
        this.context = context;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
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
        String idType = room.getIdLoaiPhong();
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("LoaiPhong");
            ref.orderByChild("IdLoaiPhong").equalTo(idType).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            viewHolder.tv_address.setText(dataSnapshot.child("tenLoaiPhong").getValue(String.class));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.tv_price.setText(formatter.format(room.getGiaPhong()));
        Picasso.get().load(room.getAnhPhong()).into(viewHolder.img);
        viewHolder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail(room);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_room, parent, false);
        return new ViewHolder(v);
    }

    public void showDetail(Room room) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name;
        private final TextView tv_address;
        private final TextView tv_price;
        private final ImageView img;
        private final RelativeLayout btn_detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.item_tv_name_detail_room);
            tv_address = itemView.findViewById(R.id.item_tv_address_detail_room);
            tv_price = itemView.findViewById(R.id.item_tv_price_detail_room);
            img = itemView.findViewById(R.id.item_iv_detail_room);
            btn_detail = itemView.findViewById(R.id.item_detail_room);
        }
    }
}
