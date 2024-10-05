package phongtaph31865.poly.stayserene.Screen_user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Detail_screen;
import phongtaph31865.poly.stayserene.adapter.Adapter_rcv1_home;
import phongtaph31865.poly.stayserene.adapter.Adapter_rcv2_home;


public class Fragment_home extends Fragment {
    private RecyclerView rcv1, rcv2;
    private Adapter_rcv1_home adapter_1;
    private Adapter_rcv2_home adapter_2;

    public Fragment_home() {}
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Account");
        String saveUser = getUsernameFromSharedPreferences();
        if (saveUser != null) {
            ref.child("uid").equalTo(saveUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String uid = snapshot1.getKey();
                            String name = snapshot1.child("username").getValue(String.class);
                            String email = snapshot1.child("email").getValue(String.class);
                            String sdt = snapshot1.child("sdt").getValue(String.class);
                            String address = snapshot1.child("diaChi").getValue(String.class);
                            saveUserIdToSharedPreferences(uid, name, sdt, address, email);
                            adapter_1.setUid(saveUser);
                            adapter_2.setUid(saveUser);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        rcv1 = v.findViewById(R.id.rcv_home_1);
        rcv2 = v.findViewById(R.id.rcv_home_2);
        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcv1.setLayoutManager(llm1);
        rcv2.setLayoutManager(llm2);
        FirebaseRecyclerOptions<Hotel> options_1 =
                new FirebaseRecyclerOptions.Builder<Hotel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("KhachSan"), Hotel.class)
                        .build();
        FirebaseRecyclerOptions<Hotel> options_2 =
                new FirebaseRecyclerOptions.Builder<Hotel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("KhachSan"), Hotel.class)
                        .build();
        adapter_1 = new Adapter_rcv1_home(options_1, getActivity());
        adapter_2 = new Adapter_rcv2_home(options_2, getActivity());
        rcv1.setAdapter(adapter_1);
        rcv2.setAdapter(adapter_2);
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter_1.startListening();
        adapter_2.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter_1.stopListening();
        adapter_2.stopListening();
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", "");
    }
    private void saveUserIdToSharedPreferences(String Uid, String username, String sdt, String address, String email){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", Uid);
        editor.putString("username", username);
        editor.putString("sdt", sdt);
        editor.putString("address", address);
        editor.putString("email", email);
        editor.apply();
    }
}