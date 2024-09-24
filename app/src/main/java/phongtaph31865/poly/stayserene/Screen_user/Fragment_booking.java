package phongtaph31865.poly.stayserene.Screen_user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.adapter.Menu_Booking_Adapter;


public class Fragment_booking extends Fragment {
    private TabLayout tabLayoutBooking;
    private ViewPager2 viewPagerBooking;
    private Menu_Booking_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        // Initialize views
        tabLayoutBooking = view.findViewById(R.id.tabLayoutBooking);
        viewPagerBooking = view.findViewById(R.id.pagerMain2);

        // Set up adapter
        adapter = new Menu_Booking_Adapter(requireActivity());
        viewPagerBooking.setAdapter(adapter);

        // Link TabLayout with ViewPager2
        new TabLayoutMediator(tabLayoutBooking, viewPagerBooking,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Ongoing");
                            break;
                        case 1:
                            tab.setText("Complete");
                            break;
                        case 2:
                            tab.setText("Cancel");
                            break;
                    }
                }).attach();

        return view;
    }
}