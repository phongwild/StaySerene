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
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import phongtaph31865.poly.stayserene.Login_Register.Loginscreen;
import phongtaph31865.poly.stayserene.R;
import phongtaph31865.poly.stayserene.Screen_user.Activity.OrderRoom.Activity_payment_method;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Setting.Information;
import phongtaph31865.poly.stayserene.Screen_user.Activity.Setting.Setting;
import phongtaph31865.poly.stayserene.Screen_user.Messenger.Activiti_messenger_list_hotel;

public class Fragment_user extends Fragment {
    private CardView btn_logout, btn_edit_profile, btn_setting, btn_payment, btn_fav;
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
        btn_edit_profile = v.findViewById(R.id.btn_edit_profile_frm_user);
        btn_setting = v.findViewById(R.id.btn_setting_frm_user);
        iv_avt = v.findViewById(R.id.iv_avt_user);
        btn_payment = v.findViewById(R.id.btn_payment_frm_user);
        btn_fav = v.findViewById(R.id.btn_favorite_frm_user);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        if (sharedPreferences != null) {
            String email = sharedPreferences.getString("email", "");
            String avatar = sharedPreferences.getString("avatar", "");
            String username = sharedPreferences.getString("username", "");
            tv_username.setText(username);
            tv_email.setText(email);
            if (avatar != null) {
                Picasso.get().load(avatar).error(R.drawable.icon_user02).into(iv_avt);
            }else Picasso.get().load(R.drawable.icon_user02).into(iv_avt);
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
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Information.class));
            }
        });
        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activiti_messenger_list_hotel.class));
            }
        });
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Setting.class));
            }
        });
        btn_payment.setOnClickListener(v1 -> {
            startActivity(new Intent(getActivity(), Activity_payment_method.class));
        });
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
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        SharedPreferences preferences = requireActivity().getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);
        SharedPreferences preferences1 = requireActivity().getSharedPreferences("user_data", Activity.MODE_PRIVATE);
        SharedPreferences preferences2 = requireActivity().getSharedPreferences("user_google", Activity.MODE_PRIVATE);
//        SharedPreferences preferences3 = requireActivity().getSharedPreferences("id_google_account", Activity.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
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