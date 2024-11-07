package phongtaph31865.poly.stayserene.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import phongtaph31865.poly.stayserene.Model.Messenger;
import phongtaph31865.poly.stayserene.R;

public class Adapter_list_messenger extends RecyclerView.Adapter<Adapter_list_messenger.MessengerViewHolder> {

    private Context context;
    private List<Messenger> messengerList;

    public Adapter_list_messenger(Context context, List<Messenger> messengerList) {
        this.context = context;
        this.messengerList = messengerList;
    }

    @Override
    public MessengerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout for each message
        View view = LayoutInflater.from(context).inflate(R.layout.item_messenger, parent, false);
        return new MessengerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessengerViewHolder holder, int position) {
        // Get the message object at the current position
        Messenger messenger = messengerList.get(position);

        // Convert the time to the desired format: HH:mm:ss dd/MM/yyyy
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // Updated format
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

            // Parse the original time
            Date date = inputFormat.parse(messenger.getThoiGianGui());

            // Add 7 hours to the parsed date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, 7); // Add 7 hours
            Date newDate = calendar.getTime(); // Get the updated time

            // Format the new time and display it
            String formattedTime = outputFormat.format(newDate);
            holder.tvThoiGianGui.setText(formattedTime);
        } catch (Exception e) {
            e.printStackTrace();
            holder.tvThoiGianGui.setText("Invalid Date");
        }

        // Bind other data
        holder.tvNoiDung.setText(messenger.getNoiDungGui());
        holder.tvVaiTro.setText(messenger.getVaiTro());
        holder.tvTrangThaiKh.setText(String.valueOf(messenger.getTrangThaiKh()));
        holder.tvTrangThaiNv.setText(String.valueOf(messenger.getTrangThaiNv()));
    }




    @Override
    public int getItemCount() {
        return messengerList.size();
    }

    // ViewHolder class to hold the view references
    public static class MessengerViewHolder extends RecyclerView.ViewHolder {
        TextView tvThoiGianGui, tvNoiDung, tvVaiTro, tvTrangThaiKh, tvTrangThaiNv;

        public MessengerViewHolder(View itemView) {
            super(itemView);
            tvThoiGianGui = itemView.findViewById(R.id.tvThoiGianGui);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
            tvVaiTro = itemView.findViewById(R.id.tvVaiTro);
            tvTrangThaiKh = itemView.findViewById(R.id.tvTrangThaiKh);
            tvTrangThaiNv = itemView.findViewById(R.id.tvTrangThaiNv);
        }
    }
}
