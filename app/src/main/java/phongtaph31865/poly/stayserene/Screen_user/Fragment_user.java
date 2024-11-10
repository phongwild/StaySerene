package phongtaph31865.poly.stayserene.Screen_user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Login_Register.Loginscreen;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.adapter.Adapter_rcv1_home;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if (getUsername != null) {
            Api_service.service.get_account().enqueue(new Callback<List<Account>>() {
                @Override
                public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                    if (response.isSuccessful()) {
                        for (Account account : response.body()) {
                            if (account.get_id().equals(getUsername)) {
                                tv_email.setText(account.getEmail());
                                tv_username.setText(account.getUsername());
                                Picasso.get().load(account.getAvt()).into(iv_avt);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Account>> call, Throwable throwable) {
                    Log.e("UserData", throwable.getMessage());
                }
            });
        } else if (account != null) {
            String name = account.getDisplayName();
            String email = account.getEmail();
            String avt = null;
            if (account.getPhotoUrl() != null) {
                avt = account.getPhotoUrl().toString();
            } else {
                avt = null;
            }
            tv_username.setText(name);
            tv_email.setText(email);
            if (avt != null) {
                Picasso.get().load(avt).into(iv_avt);
            }
            Log.d("UserData", name + " " + email + " " + avt);
        }
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupDialog.getInstance(getActivity())
                        .standardDialogBuilder()
                        .createIOSDialog()
                        .setHeading("Logout")
                        .setDescription("Are you sure you want to logout?")
                        .setPositiveButtonText("Yes")
                        .build(new StandardDialogActionListener() {
                            @Override
                            public void onPositiveButtonClicked(Dialog dialog) {
                                Logout();
                            }

                            @Override
                            public void onNegativeButtonClicked(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        return v;
    }
    private void Logout() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);
        SharedPreferences preferences1 = requireActivity().getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        SharedPreferences preferences2 = requireActivity().getSharedPreferences("user_google", Activity.MODE_PRIVATE);
//        SharedPreferences preferences3 = requireActivity().getSharedPreferences("id_google_account", Activity.MODE_PRIVATE);
        preferences.edit().clear().apply();
        preferences1.edit().clear().apply();
        preferences2.edit().clear().apply();
//        preferences3.edit().clear().apply();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), Loginscreen.class));
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", null);
    }
}