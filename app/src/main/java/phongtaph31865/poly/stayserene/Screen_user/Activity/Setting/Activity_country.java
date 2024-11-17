package phongtaph31865.poly.stayserene.Screen_user.Activity.Setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;

import java.util.ArrayList;
import java.util.List;

import phongtaph31865.poly.stayserene.Model.Country;
import phongtaph31865.poly.stayserene.adapter.Adapter_country;
import phongtaph31865.poly.stayserene.databinding.ActivityCountryBinding;
import phongtaph31865.poly.stayserene.R;

public class Activity_country extends AppCompatActivity {
    private ActivityCountryBinding binding;
    private Adapter_country adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCountryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        List<Country> countryList = new ArrayList<>();
        countryList.add(new Country(getString(R.string.country_vietnam), R.drawable.flag_vietnam));
        countryList.add(new Country(getString(R.string.country_brunei), R.drawable.flag_brunei));
        countryList.add(new Country(getString(R.string.country_cambodia), R.drawable.flag_cambodia));
        countryList.add(new Country(getString(R.string.country_indonesia), R.drawable.flag_indonesia));
        countryList.add(new Country(getString(R.string.country_laos), R.drawable.flag_laos));
        countryList.add(new Country(getString(R.string.country_timor_leste), R.drawable.flag_timor_leste));
        countryList.add(new Country(getString(R.string.country_myanmar), R.drawable.flag_myanmar));
        countryList.add(new Country(getString(R.string.country_philippines), R.drawable.flag_philippines));
        countryList.add(new Country(getString(R.string.country_singapore), R.drawable.flag_singapore));
        countryList.add(new Country(getString(R.string.country_thailand), R.drawable.flag_thailand));
        countryList.add(new Country(getString(R.string.country_malaysia), R.drawable.flag_malaysia));
        //Logic
        LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.rcvCountry.setLayoutManager(llm);
        adapter = new Adapter_country(countryList);
        adapter.setUid(getID());
        binding.rcvCountry.setAdapter(adapter);
        adapter.setOnItemClickListener(new Adapter_country.OnItemClickListener() {
            @Override
            public void onItemCLick(int position, Country country) {
                PopupDialog.getInstance(Activity_country.this)
                        .standardDialogBuilder()
                        .createIOSDialog()
                        .setHeading("Change Nationality")
                        .setPositiveButtonText("Yes")
                        .setDescription("Are you sure you want to choose this country?")
                        .build(new StandardDialogActionListener() {
                            @Override
                            public void onPositiveButtonClicked(Dialog dialog) {

                                dialog.dismiss();
                            }

                            @Override
                            public void onNegativeButtonClicked(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        binding.btnBackChangeCountry.setOnClickListener(v -> finish());

    }
    private String getID(){
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", "");
    }
}