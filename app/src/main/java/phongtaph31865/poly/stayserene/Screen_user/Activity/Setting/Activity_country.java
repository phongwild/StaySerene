package phongtaph31865.poly.stayserene.Screen_user.Activity.Setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.Model.Country;
import phongtaph31865.poly.stayserene.adapter.Adapter_country;
import phongtaph31865.poly.stayserene.databinding.ActivityCountryBinding;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                        .build(new StandardDialogActionListener() {
                            @Override
                            public void onPositiveButtonClicked(Dialog dialog) {
                                updateCountry(country);
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
    private void updateCountry(Country country) {
        Account account = new Account();
        account.setQuocTich(country.getName());
        Api_service.service.update_account(getID(), account).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Activity_country.this, "Change country successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e("onFailure", throwable.getMessage());
            }
        });
    }
    private String getID(){
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", "");
    }
}