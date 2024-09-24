package phongtaph31865.poly.stayserene.Screen_user;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Detail_screen;


public class Fragment_home extends Fragment {
    ImageView imghome1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imghome1 = view.findViewById(R.id.imghome1);
        imghome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Detail_screen.class));
            }
        });
        return view;
    }
}