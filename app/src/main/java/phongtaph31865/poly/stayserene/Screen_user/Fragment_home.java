package phongtaph31865.poly.stayserene.Screen_user;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import phongtaph31865.poly.stayserene.R;


public class Fragment_home extends Fragment {
    ImageView imghome1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        View view = inflater.inflate(R.layout.fragment_home,container,false);
//        imghome1 = view.findViewById(R.id.imghome1);
//        imghome1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////
////                startActivity(new Intent(Fragment_home.this, Fragment_booking.class));
//            }
//        });
        return view;
    }
}