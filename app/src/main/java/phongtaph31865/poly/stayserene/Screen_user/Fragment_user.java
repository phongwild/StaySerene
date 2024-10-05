package phongtaph31865.poly.stayserene.Screen_user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import phongtaph31865.poly.stayserene.Login_Register.Loginscreen;
import phongtaph31865.poly.stayserene.R;

public class Fragment_user extends Fragment {
    private LinearLayout btn_logout;
    private CircleImageView iv_avt;
    private TextView tv_username, tv_email;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        btn_logout = v.findViewById(R.id.btn_logout_user);
        iv_avt = v.findViewById(R.id.iv_avt_user);
        tv_username = v.findViewById(R.id.tv_name_user);
        tv_email = v.findViewById(R.id.tv_email_user);
        iv_avt = v.findViewById(R.id.iv_avt_user);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        String getUsername = getUsernameFromSharedPreferences();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Account");
        if (getUsername != null) {
            userRef.orderByChild("uid").equalTo(getUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String uid = snapshot1.getKey();
                            String name = snapshot1.child("username").getValue(String.class);
                            String email = snapshot1.child("email").getValue(String.class);
                            String avt = snapshot1.child("avt").getValue(String.class);
                            Log.d("UserData", uid + name + email + avt);
                            tv_username.setText(name);
                            tv_email.setText(email);
                            if (avt != null) {
                                Picasso.get().load(avt).into(iv_avt);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (account != null) {
            String name = account.getDisplayName();
            String email = account.getEmail();
            String avt = account.getPhotoUrl().toString();
            tv_username.setText(name);
            tv_email.setText(email);
            if (avt != null) {
                Picasso.get().load(avt).into(iv_avt);
            }
        }
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });
        return v;
    }

    private void Logout() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);
        SharedPreferences preferences1 = requireActivity().getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        preferences.edit().clear().apply();
        preferences1.edit().clear().apply();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), Loginscreen.class));
    }

    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }
}