package phongtaph31865.poly.stayserene.Screen_user.Activity;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import phongtaph31865.poly.stayserene.Adapter.Menu_User_Adapter;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Fragment_booking;
import phongtaph31865.poly.stayserene.Screen_user.Fragment_calendar;
import phongtaph31865.poly.stayserene.Screen_user.Fragment_home;
import phongtaph31865.poly.stayserene.Screen_user.Fragment_user;

public class MainActivity_user extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager; // Sử dụng ViewPager cũ
    private Menu_User_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        bottomNavigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.pagerMain);

        // Khởi tạo ViewPager và Fragment
        initViewPager();

        if (getIntent().getExtras() != null && "booking".equals(getIntent().getStringExtra("fragment"))) {
            OpenBooking();
        }

        // Khởi tạo BottomNavigationView
        initBottomNavigationView();
    }
    private void OpenBooking(){
        Fragment_booking booking = new Fragment_booking();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.pagerMain, booking)
                .commit();
    }
    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Fragment_home());
        fragments.add(new Fragment_calendar());
        fragments.add(new Fragment_booking());
        fragments.add(new Fragment_user());

        adapter = new Menu_User_Adapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        // Lắng nghe thay đổi trang trong ViewPager
        viewPager.addOnPageChangeListener(new androidx.viewpager.widget.ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                // Tự động chọn item tương ứng trong BottomNavigationView
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void initBottomNavigationView() {
        // Map vị trí ViewPager với ID trong BottomNavigationView
        SparseArray<Integer> positionToMenuItem = new SparseArray<>();
        positionToMenuItem.put(0, R.id.item_user_home);
        positionToMenuItem.put(1, R.id.item_user_calendar);
        positionToMenuItem.put(2, R.id.item_user_booking);
        positionToMenuItem.put(3, R.id.item_user_person);

        // Lắng nghe chọn mục trong BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Tìm vị trí của mục đã chọn
                int position = getPositionFromMenuItem(item.getItemId(), positionToMenuItem);
                if (position != -1) {
                    viewPager.setCurrentItem(position, true); // Chuyển sang trang tương ứng
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Hàm ánh xạ từ MenuItem ID sang vị trí ViewPager
     */
    private int getPositionFromMenuItem(int menuItemId, SparseArray<Integer> positionToMenuItem) {
        for (int i = 0; i < positionToMenuItem.size(); i++) {
            if (positionToMenuItem.valueAt(i) == menuItemId) {
                return positionToMenuItem.keyAt(i);
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }
}
