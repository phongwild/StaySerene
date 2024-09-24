package phongtaph31865.poly.stayserene.Screen_admin.Activity_admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_admin.Fragment_admin.Fragment_home_admin;
import phongtaph31865.poly.stayserene.Screen_admin.Fragment_admin.Fragment_notification_admin;
import phongtaph31865.poly.stayserene.Screen_admin.Fragment_admin.Fragment_personal_admin;
import phongtaph31865.poly.stayserene.Screen_admin.Fragment_admin.Fragment_statical_admin;
import phongtaph31865.poly.stayserene.adapter.Menu_admin_adapter;

public class MainActivity_admin extends AppCompatActivity {
    BottomNavigationView view;
    Menu_admin_adapter adapter;
    ViewPager2 pager2;
    ArrayList<Fragment> list = new ArrayList<>();
    FloatingActionButton manage, employee, room, customer;
    Animation fabOpen, fabClose, rotateOpen, rotateClose;
    boolean isOpen = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        view = findViewById(R.id.bottomNavigationView_admin);
        pager2 = findViewById(R.id.pagerMain_Admin);

        manage = findViewById(R.id.manage);
        employee = findViewById(R.id.employee_manager);
        room = findViewById(R.id.rooms_manager);
        customer = findViewById(R.id.customer_manager);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.open_fab);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.close_fab);
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_fab);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_fab);

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        view.setBackground(null);
        initView();
    }

    private void animateFab() {
        if (isOpen) {
            manage.startAnimation(rotateClose);
            employee.startAnimation(fabClose);
            room.startAnimation(fabClose);
            customer.startAnimation(fabClose);
            employee.setVisibility(View.INVISIBLE);
            room.setVisibility(View.INVISIBLE);
            customer.setVisibility(View.INVISIBLE);
            isOpen = false;
        } else {
            manage.startAnimation(rotateOpen);
            employee.startAnimation(fabOpen);
            room.startAnimation(fabOpen);
            customer.startAnimation(fabOpen);
            employee.setVisibility(View.VISIBLE);
            room.setVisibility(View.VISIBLE);
            customer.setVisibility(View.VISIBLE);
            isOpen = true;
        }
    }

    private void initView() {
        list.add(new Fragment_home_admin());
        list.add(new Fragment_statical_admin());
        list.add(new Fragment_notification_admin());
        list.add(new Fragment_personal_admin());
        adapter = new Menu_admin_adapter(this, list);
        pager2.setAdapter(adapter);
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        view.setSelectedItemId(R.id.item_admin_home);
                        break;
                    case 1:
                        view.setSelectedItemId(R.id.item_admin_chart);
                        break;
                    case 2:
                        view.setSelectedItemId(R.id.item_admin_notification);
                        break;
                    case 3:
                        view.setSelectedItemId(R.id.item_admin_person);
                        break;
                }
                super.onPageSelected(position);
            }
        });
        view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.item_admin_home) {
                    pager2.setCurrentItem(0);
                }
                if (item.getItemId() == R.id.item_admin_chart) {
                    pager2.setCurrentItem(1);
                }
                if (item.getItemId() == R.id.item_admin_notification) {
                    pager2.setCurrentItem(2);
                }
                if (item.getItemId() == R.id.item_admin_person) {
                    pager2.setCurrentItem(3);
                }
                return true;
            }
        });
    }
}