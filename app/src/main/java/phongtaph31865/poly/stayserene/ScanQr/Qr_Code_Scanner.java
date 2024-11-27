package phongtaph31865.poly.stayserene.ScanQr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.InputStream;
import java.util.Arrays;

public class Qr_Code_Scanner {
    private static final String TAG = "QRCodeScanner";

    // Interface callback để trả kết quả quét QR Code
    public interface QRCodeScanCallback {
        void onQRCodeScanned(String qrCodeValue);
        void onQRCodeScanFailed(String error);
    }

    /**
     * Quét mã QR từ URI ảnh
     *
     * @param context Context của activity hoặc fragment
     * @param imgUri URI của ảnh chứa mã QR
     * @param callback Callback để trả kết quả quét
     */
    public static void scanQRCodeFromUri(Context context, Uri imgUri, QRCodeScanCallback callback) {
        try {
            // Lấy InputStream từ URI ảnh
            InputStream imageStream = context.getContentResolver().openInputStream(imgUri);
            // Chuyển InputStream thành Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

            if (bitmap != null) {
                // Chuyển đổi Bitmap thành InputImage để quét mã QR
                InputImage image = InputImage.fromBitmap(bitmap, 0);

                // Tạo BarcodeScanner từ ML Kit
                BarcodeScanner scanner = BarcodeScanning.getClient();

                // Quét mã QR
                scanner.process(image)
                        .addOnSuccessListener(barcodes -> {
                            if (barcodes.size() > 0) {
                                // Lấy giá trị mã QR
                                String qrCodeValue = barcodes.get(0).getRawValue();
                                // Gọi callback với giá trị QR Code quét được
                                callback.onQRCodeScanned(qrCodeValue);
                            } else {
                                // Không tìm thấy mã QR trong ảnh
                                callback.onQRCodeScanFailed("No QR Code found");
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "QR Scan Error: " + e.getMessage());
                            callback.onQRCodeScanFailed("Error while scanning QR Code");
                        });
            } else {
                callback.onQRCodeScanFailed("Failed to decode image");
            }
        } catch (Exception e) {
            Log.e(TAG, "QR Scan Exception: " + e.getMessage());
            callback.onQRCodeScanFailed("Error loading image");
        }
    }
}
