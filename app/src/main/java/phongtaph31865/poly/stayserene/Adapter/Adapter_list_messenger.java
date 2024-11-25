package phongtaph31865.poly.stayserene.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_messenger, parent, false);
        return new MessengerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessengerViewHolder holder, int position) {
        Messenger messenger = messengerList.get(position);

        if (messenger.getVaiTro().equals("Khách hàng")) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.messageContainer.getLayoutParams();
            params.gravity = Gravity.END;
            params.leftMargin = 200;
            holder.messageContainer.setLayoutParams(params);
            holder.tvNoiDung.setGravity(Gravity.END);
            holder.tvNoiDung.setTextColor(Color.parseColor("#FFFFFF"));

            holder.messageContainer.setBackgroundResource(R.drawable.message_background_customer);
        } else if (messenger.getVaiTro().equals("Khách sạn")) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.messageContainer.getLayoutParams();
            params.gravity = Gravity.START;
            params.rightMargin = 200;
            holder.messageContainer.setLayoutParams(params);
            holder.tvNoiDung.setGravity(Gravity.START);
            holder.tvNoiDung.setTextColor(Color.parseColor("#3498db"));

            holder.messageContainer.setBackgroundResource(R.drawable.message_background_hotel);
        }

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            Date date = inputFormat.parse(messenger.getThoiGianGui());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, 7);
            Date newDate = calendar.getTime();

            String formattedTime = outputFormat.format(newDate);
            holder.tvThoiGianGui.setText(formattedTime);
        } catch (Exception e) {
            e.printStackTrace();
            holder.tvThoiGianGui.setText("Invalid Date");
        }

        holder.tvNoiDung.setText(messenger.getNoiDungGui());

    }




    @Override
    public int getItemCount() {
        return messengerList.size();
    }

    public static class MessengerViewHolder extends RecyclerView.ViewHolder {
        TextView tvThoiGianGui, tvNoiDung;
        LinearLayout messageContainer;

        public MessengerViewHolder(View itemView) {
            super(itemView);
            tvThoiGianGui = itemView.findViewById(R.id.tvThoiGianGui);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
            messageContainer = itemView.findViewById(R.id.messageContainer);

        }
    }

}