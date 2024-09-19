package phongtaph31865.poly.stayserene.Screen_user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import phongtaph31865.poly.stayserene.R;


public class Fragment_booking extends Fragment {
    TextView backbookingButton ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_booking, container, false);
        View view = inflater.inflate(R.layout.fragment_booking,container,false);
        return view;
    }
}