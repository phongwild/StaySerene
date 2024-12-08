package phongtaph31865.poly.stayserene.Screen_user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);

        // Tìm view một lần và gán vào biến
        initUI(v);

        // Lấy thông tin từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String avatar = sharedPreferences.getString("avatar", "");
        String username = sharedPreferences.getString("username", "");

        tv_username.setText(username);
        tv_email.setText(email);
        if (avatar != null && !avatar.isEmpty()) {
            Picasso.get().load(avatar).into(iv_avt);
        } else {
            iv_avt.setImageResource(R.drawable.icon_user02);
        }

        // Cài đặt sự kiện cho các nút
        handleClick();

        return v;
    }

    private void initUI(View v) {
        btn_logout = v.findViewById(R.id.btn_logout_user);
        iv_avt = v.findViewById(R.id.iv_avt_user);
        tv_username = v.findViewById(R.id.tv_name_user);
        tv_email = v.findViewById(R.id.tv_email_user);
        btn_edit_profile = v.findViewById(R.id.btn_edit_profile_frm_user);
        btn_setting = v.findViewById(R.id.btn_setting_frm_user);
        btn_payment = v.findViewById(R.id.btn_payment_frm_user);
        btn_fav = v.findViewById(R.id.btn_favorite_frm_user);
    }

    public void handleClick() {
        btn_edit_profile.setOnClickListener(v1 -> startActivity(new Intent(getActivity(), Information.class)));
        btn_fav.setOnClickListener(v1 -> startActivity(new Intent(getActivity(), Activiti_messenger_list_hotel.class)));
        btn_setting.setOnClickListener(v1 -> startActivity(new Intent(getActivity(), Setting.class)));
        btn_payment.setOnClickListener(v1 -> startActivity(new Intent(getActivity(), Activity_payment_method.class)));

        //Click avt
        iv_avt.setOnClickListener(v -> {
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.custom_dialog_avt);
            //Ánh xa
            ImageView large_avt = dialog.findViewById(R.id.iv_avt_custom_dialog);
            Glide.with(this).load(iv_avt.getDrawable()).into(large_avt);
            //show
            dialog.show();
        });
        // Xử lý đăng xuất
        btn_logout.setOnClickListener(v1 -> PopupDialog.getInstance(getActivity())
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
                .show());

    }
    // Hàm đăng xuất
    private void Logout() {
        // Xóa toàn bộ SharedPreferences không cần thiết
        clearSharedPreferences("userdata", "loginStatus", "user_data", "user_google");

        //Đăng xuất firebase
        FirebaseAuth.getInstance().signOut();

        //Đăng xuất google
        GoogleSignIn.getClient(getActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
                .addOnCompleteListener(getActivity(), task -> {
                    startActivity(new Intent(getActivity(), Loginscreen.class));
                });
    }

    // Hàm hỗ trợ xóa SharedPreferences
    private void clearSharedPreferences(String... prefs) {
        for (String pref : prefs) {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(pref, Activity.MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
        }
    }
}
