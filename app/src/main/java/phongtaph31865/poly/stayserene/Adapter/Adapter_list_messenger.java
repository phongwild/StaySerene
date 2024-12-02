package phongtaph31865.poly.stayserene.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import phongtaph31865.poly.stayserene.Model.Messenger;
import phongtaph31865.poly.stayserene.R;

public class Adapter_list_messenger extends RecyclerView.Adapter<Adapter_list_messenger.MessengerViewHolder> {

    private final Context context;
    private final List<Messenger> messengerList;
    private List<Boolean> timeVisibleList;
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private final SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    public Adapter_list_messenger(Context context, List<Messenger> messengerList) {
        this.context = context;
        this.messengerList = messengerList;
        this.timeVisibleList = new ArrayList<>(Collections.nCopies(messengerList.size(), false)); // Khởi tạo trạng thái mặc định là false cho tất cả item
    }

    @Override
    public MessengerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_messenger, parent, false);
        return new MessengerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessengerViewHolder holder, int position) {
        Messenger messenger = messengerList.get(position);

        // Đặt giao diện tin nhắn theo vai trò
        if (messenger.getVaiTro().equals("Khách hàng")) {
            setMessageStyle(holder, Gravity.END, 200, 0, "#FFFFFF", R.drawable.message_background_customer, Gravity.END);
        } else if (messenger.getVaiTro().equals("Khách sạn")) {
            setMessageStyle(holder, Gravity.START, 0, 200, "#3498db", R.drawable.message_background_hotel, Gravity.START);
        }

        // Định dạng thời gian gửi
        String formattedTime = formatTime(messenger.getThoiGianGui());
        holder.tvThoiGianGui.setText(formattedTime != null ? formattedTime : "Invalid Date");
        // Cập nhật visibility của thời gian dựa trên trạng thái trong timeVisibleList
        holder.tvThoiGianGui.setVisibility(timeVisibleList.get(position) ? View.VISIBLE : View.GONE);
        // Đặt nội dung tin nhắn
        holder.tvNoiDung.setText(messenger.getNoiDungGui());
        holder.itemView.setOnClickListener(v -> {
            // Lật lại trạng thái của timeVisible tại vị trí item
            boolean isVisible = !timeVisibleList.get(position);
            timeVisibleList.set(position, isVisible);
            if (isVisible) {
                holder.tvThoiGianGui.setAlpha(0f);
                holder.tvThoiGianGui.setTranslationY(-50);
                holder.tvThoiGianGui.animate()
                        .alpha(1f)
                        .translationY(0)
                        .setDuration(300)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
            } else {
                holder.tvThoiGianGui.animate()
                        .alpha(0f)
                        .translationY(-50)
                        .setDuration(300)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
            }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return messengerList.size();
    }

    /**
     * Định dạng thời gian từ chuỗi JSON sang chuỗi hiển thị.
     */
    private String formatTime(String thoiGianGui) {
        try {
            Date date = inputFormat.parse(thoiGianGui);

            // Cộng thêm 7 giờ (nếu cần thiết)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, 7);
            return outputFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Đặt kiểu hiển thị tin nhắn dựa trên vai trò.
     */
    private void setMessageStyle(MessengerViewHolder holder, int gravity, int leftMargin, int rightMargin, String textColor, int backgroundResource, int textGravity) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.messageContainer.getLayoutParams();
        params.gravity = gravity;
        params.leftMargin = leftMargin;
        params.rightMargin = rightMargin;
        holder.messageContainer.setLayoutParams(params);
        holder.tvNoiDung.setGravity(textGravity);
        holder.tvNoiDung.setTextColor(Color.parseColor(textColor));
        holder.messageContainer.setBackgroundResource(backgroundResource);
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
