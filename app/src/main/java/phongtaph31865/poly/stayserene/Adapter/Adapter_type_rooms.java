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

import phongtaph31865.poly.stayserene.Model.TypeRoom;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom.Activity_detail_type_rooms;

public class Adapter_type_rooms extends RecyclerView.Adapter<Adapter_type_rooms.ViewHolder> {
    private List<TypeRoom> list_type_rooms;

    public Adapter_type_rooms(List<TypeRoom> list_type_rooms) {
        this.list_type_rooms = list_type_rooms;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_type_rooms, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TypeRoom typeRoom = list_type_rooms.get(position);
        holder.tv_name.setText(typeRoom.getTenLoaiPhong());
        holder.tv_desc.setText(typeRoom.getMoTaLoaiPhong());
        Picasso.get().load(typeRoom.getAnhLoaiPhong()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity_detail_type_rooms.class);
                intent.putExtra("id_type_room", typeRoom.get_id());
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (list_type_rooms != null) {
            return list_type_rooms.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name;
        private final TextView tv_desc;
        private final ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.item_tv_name_type_rooms);
            img = itemView.findViewById(R.id.item_iv_type_rooms);
            tv_desc = itemView.findViewById(R.id.item_tv_desc_type_rooms);
        }
    }
}
