package phongtaph31865.poly.stayserene.Messaging_Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.MainActivity_user;
import phongtaph31865.poly.stayserene.Screen_user.Messenger.Activiti_list_messenger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";
    private static final String CHANNEL_ID = "chat_notification"; // Kênh thông báo
    private static final String CHANNEL_NAME = "Notification Channel";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "FCM Token: " + token);
        // Bạn có thể gửi token này về server để lưu trữ và sử dụng sau
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Xử lý thông báo khi nhận được
        Log.d(TAG, "Message received from: " + remoteMessage.getFrom());

        // Lấy dữ liệu từ payload
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String idHotel = remoteMessage.getData().get("idHotel"); // ID khách sạn
            String body = remoteMessage.getNotification().getBody();// Nội dung thông báo

            Api_service.service.get_hotel_byId(idHotel).enqueue(new Callback<List<Hotel>>() {
                @Override
                public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Hotel hotel = response.body().get(0);
                        String name = hotel.getTenKhachSan();
                        showMessageNotification(name, body, idHotel);
                    } else Log.d("TAG", "onResponse: " + response.message());
                }

                @Override
                public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                    Log.d("TAG", "onFailure: " + throwable.getMessage());
                }
            });

        }

        // Xử lý thông báo (Notification payload nếu có)
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    /**
     * Hiển thị thông báo tin nhắn.
     */
    private void showMessageNotification(String title, String body, String idHotel) {
        // Intent để mở màn hình tin nhắn
        Intent intent = new Intent(this, Activiti_list_messenger.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Đảm bảo không tạo Activity mới nếu đã mở
        intent.putExtra("IdKhachSan", idHotel); // Truyền ID khách sạn qua Intent

        // Tạo PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        // Hiển thị thông báo
        createNotification(title, body, pendingIntent);
    }

    /**
     * Phương thức chung để tạo thông báo.
     */
    private void createNotification(String title, String body, PendingIntent pendingIntent) {
        // Lấy NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Tạo Notification Channel nếu cần (cho Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Icon của thông báo
                .setContentTitle(title) // Tiêu đề
                .setContentText(body) // Nội dung
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Đặt mức ưu tiên cao
                .setContentIntent(pendingIntent) // Đặt Intent khi nhấn vào thông báo
                .setAutoCancel(true); // Tự động đóng thông báo khi nhấn

        // Hiển thị thông báo
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
