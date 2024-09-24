package phongtaph31865.poly.stayserene.Screen_admin.Fragment_admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import phongtaph31865.poly.stayserene.R;


public class Fragment_statical_admin extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statical_admin, container, false);

        // Khởi tạo các view từ file layout bằng cách sử dụng view.findViewById()
        TextView title = view.findViewById(R.id.title);
        TextView date = view.findViewById(R.id.date);
        Button btnThongKe = view.findViewById(R.id.btnThongKe);
        TableLayout tableLayout = view.findViewById(R.id.tableLayout);

        // Thêm sự kiện cho nút Thống kê
        btnThongKe.setOnClickListener(v -> {
            // Xử lý logic thống kê tại đây
            // Ví dụ: bạn có thể thêm dữ liệu mới vào bảng

            // Thêm một hàng mới vào bảng
            TableRow tableRow = new TableRow(getContext());

            TextView maPhong = new TextView(getContext());
            maPhong.setText("P002");
            tableRow.addView(maPhong);

            TextView tenPhong = new TextView(getContext());
            tenPhong.setText("Phòng 2");
            tableRow.addView(tenPhong);

            TextView time = new TextView(getContext());
            time.setText("10:00 - 11:00");
            tableRow.addView(time);

            TextView tongTien = new TextView(getContext());
            tongTien.setText("250.000 VNĐ");
            tableRow.addView(tongTien);

            tableLayout.addView(tableRow);  // Thêm hàng mới vào bảng
        });

        return view;  // Trả về view đã inflate
    }
}