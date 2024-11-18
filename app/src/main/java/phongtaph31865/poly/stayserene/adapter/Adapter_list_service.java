package phongtaph31865.poly.stayserene.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import phongtaph31865.poly.stayserene.Model.Service;
import phongtaph31865.poly.stayserene.R;

public class Adapter_list_service extends RecyclerView.Adapter<Adapter_list_service.ServiceViewHolder> {

    private Context context;
    private List<Service> serviceList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String serviceId, String serviceName, double servicePrice);
    }

    public Adapter_list_service(Context context, List<Service> serviceList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.serviceList = serviceList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);

        holder.item_tv_ten_service.setText(service.getTenDichVu());
        holder.item_tv_mo_ta_service.setText(service.getMotaDichVu());
        holder.item_tv_gia_service.setText(String.format("%,d", service.getGiaDichVu()));
        Glide.with(context)
                .load(service.getAnhDichVu())
                .into(holder.item_img_service);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(service.get_id(), service.getTenDichVu(), service.getGiaDichVu());
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {

        TextView item_tv_ten_service, item_tv_mo_ta_service, item_tv_gia_service;
        ImageView item_img_service;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            item_tv_ten_service = itemView.findViewById(R.id.item_tv_ten_service);
            item_tv_mo_ta_service = itemView.findViewById(R.id.item_tv_mo_ta_service);
            item_tv_gia_service = itemView.findViewById(R.id.item_tv_gia_service);
            item_img_service = itemView.findViewById(R.id.item_img_service);
        }
    }
}
