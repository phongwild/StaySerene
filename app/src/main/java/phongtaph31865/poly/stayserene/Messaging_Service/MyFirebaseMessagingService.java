package phongtaph31865.poly.stayserene.Messaging_Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import phongtaph31865.poly.stayserene.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";
    private static final String CHANNEL_ID = "chat_notification"; // Đặt ID cho kênh thông báo
    private static final String CHANNEL_NAME = "Message";
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "FCM Token: " + token);
        // Bạn có thể gửi token này về server để lưu trữ và sử dụng sau
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Xử lý thông báo khi nhận được
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            // Hiển thị thông báo ngay cả khi app đang mở
            showNotification(title, body);
        }
    }
    private void showNotification(String title, String body) {
        // Tạo Notification Channel cho Android 8+ (Oreo)
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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
                .setContentTitle(title) // Tiêu đề thông báo
                .setContentText(body)   // Nội dung thông báo
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Đặt mức ưu tiên cao
                .setAutoCancel(true); // Thông báo sẽ tự động đóng khi được nhấp vào

        // Hiển thị thông báo
        notificationManager.notify(1, builder.build());
    }
}
