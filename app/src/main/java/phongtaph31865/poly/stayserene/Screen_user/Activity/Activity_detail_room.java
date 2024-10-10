package phongtaph31865.poly.stayserene.Screen_user.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.adapter.Adapter_detail_room;

public class Activity_detail_room extends AppCompatActivity {
    private ImageView btn_back;
    private RecyclerView rcv;
    Adapter_detail_room adapter;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_room);
        btn_back = findViewById(R.id.btn_back_detail_room);
        rcv = findViewById(R.id.rcv_detail_room);
        GridLayoutManager manager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(manager);
        FirebaseRecyclerOptions<Room> options =
                new FirebaseRecyclerOptions.Builder<Room>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Phong"), Room.class)
                        .build();
        adapter = new Adapter_detail_room(options, this);
        if(adapter != null){
            rcv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        String getUsername = getUsernameFromSharedPreferences();
        String getUid_google = getUser_google();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Account");
        if (getUsername != null) {
            ref.orderByChild("uid").equalTo(getUsername).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            adapter.setUid(getUsername);
                            Log.d("save", "user: " + adapter.getUid());
                            Log.d("save", "user Realtime: " + getUsername);
                        }
                    }else {
                        Log.d("save_1", "user null");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(getUid_google != null){
            //Google
            String name = account.getDisplayName();
            String email = account.getEmail();
            String sdt = "";
            String address = "";
            saveUserIdToSharedPreferences(getUid_google, name, sdt, address, email);
            if(adapter != null){
                adapter.setUid(getUid_google);
            }
            Log.d("save", "User google: " + adapter.getUid());
        }else{
            //Facebook
        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_detail_room.this, MainActivity_user.class));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private String getUser_google(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_google", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }
    private void saveUserIdToSharedPreferences(String Uid, String username, String sdt, String address, String email){
        SharedPreferences sharedPreferences = this.getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", Uid);
        editor.putString("username", username);
        editor.putString("sdt", sdt);
        editor.putString("address", address);
        editor.putString("email", email);
        editor.apply();
    }
}