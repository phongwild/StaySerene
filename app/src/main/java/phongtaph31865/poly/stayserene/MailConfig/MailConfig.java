package phongtaph31865.poly.stayserene.MailConfig;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;
public class MailConfig {
    public static final String EMAIL = "stayserene.otp@gmail.com"; // your email

    public static final String PASSWORD = "vkpa zaze kizc bifo"; // your password

    public static String generateOTP(int length) {
        String otp = "";
        for (int i = 0; i < length; i++) {
            otp += (int) (Math.random() * 10); // Tạo ngẫu nhiên các chữ số từ 0 đến 9
        }
        return otp;
    }
    // Hàm gửi email với OTP
    public static void sendOtpEmail(String toEmail, String otp) {
        new SendEmailTask().execute(toEmail, otp);
    }

    // AsyncTask để gửi email trong background
    private static class SendEmailTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String toEmail = params[0];
            String otp = params[1];

            try {
                // Cấu hình SMTP
                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");

                // Tạo session
                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL, PASSWORD);
                    }
                });
                session.setDebug(true);
                // Tạo email message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setHeader("Content-Type", "text/html; charset=UTF-8");
                message.setSubject("OTP Code Authentication for StaySerene");
                message.setText("Your OTP code is: " + otp);

                // Gửi email
                Transport.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
