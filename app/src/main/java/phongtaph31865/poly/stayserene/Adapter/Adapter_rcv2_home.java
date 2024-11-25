package phongtaph31865.poly.stayserene.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class Adapter_rcv2_home extends RecyclerView.Adapter<Adapter_rcv2_home.ViewHolder> {
    private String Uid;
    private List<Room> rooms;
    public Adapter_rcv2_home(List<Room> rooms) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv2_home, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);
        if(room != null){
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            int status = room.getTinhTrangPhong();
            if (status == 0) {
                holder.tv_address.setText("Open");
            } else if (status == 1) {
                holder.tv_address.setText("Close");
            }
            String idType = room.getIdLoaiPhong();
            try {
                Api_service.service.get_typeroom().enqueue(new Callback<List<TypeRoom>>() {
                    @Override
                    public void onResponse(Call<List<TypeRoom>> call, Response<List<TypeRoom>> response) {
                        if (response.isSuccessful()){
                            for (TypeRoom typeRoom : response.body()){
                                if (typeRoom.get_id().equals(idType)){
                                    holder.tv_name.setText(typeRoom.getTenLoaiPhong());
                                    holder.tv_price.setText(formatter.format(typeRoom.getGiaLoaiPhong()));
                                }
                            }
                        }else Log.e("rcv2", "False: Khong lay duoc id loai phong");
                    }
                    @Override
                    public void onFailure(Call<List<TypeRoom>> call, Throwable throwable) {
                        Log.e("rcv2", "False: " + throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
            if(room.getAnhPhong() != null){
                Picasso.get().load(room.getAnhPhong()).error(R.drawable.room_image1).fit().centerCrop().into(holder.img);
            }

            holder.btn_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetail(room, v);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (rooms != null){
            return rooms.size();
        }
        return 0;
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
    public void showDetail(Room room, View v){
        Intent intent = new Intent(v.getContext(), Detail_room_screen.class);
        intent.putExtra("uid", Uid);
        intent.putExtra("IdRoom", room.get_id());
        intent.putExtra("IdTypeRoom", room.getIdLoaiPhong());
        intent.putExtra("img", room.getAnhPhong());
        intent.putExtra("price", room.getGiaPhong());
        intent.putExtra("status", room.getTinhTrangPhong());
        intent.putExtra("floor", room.getSoTang());
        intent.putExtra("desc", room.getMoTaPhong());
        intent.putExtra("numberroom", room.getSoPhong());
        v.getContext().startActivity(intent);
    }
}
