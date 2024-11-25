package phongtaph31865.poly.stayserene.Screen_user.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Fragment_booking;
import phongtaph31865.poly.stayserene.Screen_user.Fragment_calendar;
import phongtaph31865.poly.stayserene.Screen_user.Fragment_home;
import phongtaph31865.poly.stayserene.Screen_user.Fragment_user;
import phongtaph31865.poly.stayserene.Adapter.Menu_User_Adapter;

public class MainActivity_user extends AppCompatActivity {
    BottomNavigationView view;
    Menu_User_Adapter adapter;
    ViewPager2 pager2;
    ArrayList<Fragment> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.bottom_nav);
        pager2 = findViewById(R.id.pagerMain);
        initView();
    }

    private void initView() {
        list.add(new Fragment_home());
        list.add(new Fragment_calendar());
        list.add(new Fragment_booking());
        list.add(new Fragment_user());
        adapter = new Menu_User_Adapter(this, list);
        pager2.setAdapter(adapter);
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        view.setSelectedItemId(R.id.item_user_home);
                        break;
                    case 1:
                        view.setSelectedItemId(R.id.item_user_calendar);
                        break;
                    case 2:
                        view.setSelectedItemId(R.id.item_user_booking);
                        break;
                    case 3:
                        view.setSelectedItemId(R.id.item_user_person);
                        break;
                }
                super.onPageSelected(position);
            }
        });
        view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.item_user_home){
                    pager2.setCurrentItem(0);
                }
                if(item.getItemId() == R.id.item_user_calendar){
                    pager2.setCurrentItem(1);
                }
                if(item.getItemId() == R.id.item_user_booking){
                    pager2.setCurrentItem(2);
                }
                if(item.getItemId() == R.id.item_user_person){
                    pager2.setCurrentItem(3);
                }
                return true;
            }
        });
    }
}