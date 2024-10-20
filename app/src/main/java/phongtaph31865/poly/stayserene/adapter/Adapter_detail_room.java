package phongtaph31865.poly.stayserene.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import java.util.List;
import java.util.Locale;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Detail_room_screen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_detail_room extends RecyclerView.Adapter<Adapter_detail_room.ViewHolder> {
    private String Uid;
    private List<Room> rooms;

    public Adapter_detail_room(List<Room> rooms) {
        this.rooms = rooms;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_room, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        int status = room.getTinhTrangPhong();
        if (status == 0) {
            holder.tv_name.setText("Open");
        } else if (status == 1) {
            holder.tv_name.setText("Close");
        }
        String idType = room.getIdLoaiPhong();
        try {
            Api_service.service.get_typeroom().enqueue(new Callback<List<TypeRoom>>() {
                @Override
                public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                    if (response.isSuccessful()){
                        for (TypeRoom typeRoom : response.body()){
                            if (typeRoom.get_id().equals(idType)){
                                holder.tv_address.setText(typeRoom.getTenLoaiPhong());
                            }
                        }
                    }else Log.e("Detail Room", "False: Khong lay duoc id loai phong");
                }
                @Override
                public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                    Log.e("Detail Room", "False: " + throwable.getMessage());
                    throwable.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tv_price.setText(formatter.format(room.getGiaPhong()));
        Picasso.get().load(room.getAnhPhong()).into(holder.img);
        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail(room, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (rooms != null) {
            return rooms.size();
        }
        return 0;
    }

    public void showDetail(Room room, View v) {
        Intent intent = new Intent(v.getContext(), Detail_room_screen.class);
        intent.putExtra("uid", Uid);
        intent.putExtra("IdRoom", room.get_id());
        intent.putExtra("IdTypeRoom", room.getIdLoaiPhong());
        intent.putExtra("img", room.getAnhPhong());
        intent.putExtra("price", room.getGiaPhong());
        intent.putExtra("status", room.getTinhTrangPhong());
        intent.putExtra("floor", room.getSoTang());
        intent.putExtra("desc", room.getMoTaPhong());
        v.getContext().startActivity(intent);
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
