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

import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Messenger.Activiti_list_messenger;

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
        String idHotel = remoteMessage.getData().get("idHotel");
        Log.d(TAG, "idHotel: " + idHotel);
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            // Hiển thị thông báo ngay cả khi app đang mở
            showNotification(title, body, idHotel);
        }
    }
    private void showNotification(String title, String body, String idHotel) {
        //Intent để mở Message
        Intent intent = new Intent(this, Activiti_list_messenger.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Đảm bảo Intent không mở Activity mới nếu đã mở

        //Truyen id hotel qua intent
        intent.putExtra("IdKhachSan", idHotel);

        //Pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

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
                .setContentIntent(pendingIntent) // Đặt PendingIntent để mở Activity khi thông báo được nhấp vào
                .setAutoCancel(true); // Thông báo sẽ tự động đóng khi được nhấp vào

        // Hiển thị thông báo
        notificationManager.notify(1, builder.build());
    }
}
