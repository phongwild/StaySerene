package phongtaph31865.poly.stayserene.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import phongtaph31865.poly.stayserene.Screen_user.Booking.Cancel;
import phongtaph31865.poly.stayserene.Screen_user.Booking.Complete;
import phongtaph31865.poly.stayserene.Screen_user.Booking.Ongoing;
import phongtaph31865.poly.stayserene.Screen_user.Fragment_booking;

public class Menu_Booking_Adapter extends FragmentStateAdapter {
    public Menu_Booking_Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Ongoing(); // Tab Ongoing
            case 1:
                return new Complete(); // Tab Complete
            case 2:
                return new Cancel(); // Tab Cancel
            default:
                return new Ongoing(); // Default case
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Number of tabs
    }
}
