package phongtaph31865.poly.stayserene;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<CardModel> cardList;
    private Context context;

    public CardAdapter(List<CardModel> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardModel card = cardList.get(position);

        holder.billId.setText("Bill id: " + card.getBillId());
        holder.hotelId.setText("Hotel id: " + card.getHotelId());
        holder.dateTime.setText("Date/time: " + card.getDateTime());
        holder.totalAmount.setText("Total: " + card.getTotalAmount() + " VND");
        holder.paymentMethod.setText("Method: " + card.getPaymentMethod());
        holder.status.setText("Status: " + card.getStatus());

        holder.confirmButton.setOnClickListener(v -> {
            // Xử lý khi nhấn nút Confirm
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView billId, hotelId, dateTime, totalAmount, paymentMethod, status;
        Button confirmButton;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            billId = itemView.findViewById(R.id.billId);
            hotelId = itemView.findViewById(R.id.hotelId);
            dateTime = itemView.findViewById(R.id.dateTime);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            status = itemView.findViewById(R.id.status);
            confirmButton = itemView.findViewById(R.id.confirmButton);
        }
    }
}
