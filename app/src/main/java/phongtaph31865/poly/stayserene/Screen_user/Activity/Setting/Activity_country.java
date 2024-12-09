package phongtaph31865.poly.stayserene.Screen_user.Activity.Setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;

import java.util.ArrayList;
import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.Model.Country;
import phongtaph31865.poly.stayserene.Adapter.Adapter_country;
import phongtaph31865.poly.stayserene.databinding.ActivityCountryBinding;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.widget.SearchView;

public class Activity_country extends AppCompatActivity {
    private ActivityCountryBinding binding;
    private Adapter_country adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCountryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize country list
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
        countryList.add(new Country(getString(R.string.country_australia), R.drawable.flag_ustrailia));
        countryList.add(new Country(getString(R.string.country_america), R.drawable.flag_america));
        countryList.add(new Country(getString(R.string.country_england), R.drawable.flag_england));
        countryList.add(new Country(getString(R.string.country_france), R.drawable.flag_france));
        countryList.add(new Country(getString(R.string.country_finland), R.drawable.flag_finlan));
        countryList.add(new Country(getString(R.string.country_libya), R.drawable.flag_libya));
        countryList.add(new Country(getString(R.string.country_netherlands), R.drawable.flag_netherlands));
        countryList.add(new Country(getString(R.string.country_russia), R.drawable.flag_russia));
        countryList.add(new Country(getString(R.string.country_switzerland), R.drawable.flag_switzerland));
        countryList.add(new Country(getString(R.string.country_turkey), R.drawable.flag_turkey));
        countryList.add(new Country(getString(R.string.country_germany), R.drawable.flag_germany));
        countryList.add(new Country(getString(R.string.country_india), R.drawable.flag_india));
        countryList.add(new Country(getString(R.string.country_brazil), R.drawable.flag_brasil));
        countryList.add(new Country(getString(R.string.country_argentina), R.drawable.flag_argentina));
        countryList.add(new Country(getString(R.string.country_saudi_arabia), R.drawable.flag_saudi_abaria));
        countryList.add(new Country(getString(R.string.country_japan), R.drawable.flag_japan));
        countryList.add(new Country(getString(R.string.country_china), R.drawable.flag_china));
        countryList.add(new Country(getString(R.string.country_korea), R.drawable.flag_korea));
        countryList.add(new Country(getString(R.string.country_usa), R.drawable.flag_america));
        countryList.add(new Country(getString(R.string.country_canada), R.drawable.flag_canada));
        countryList.add(new Country(getString(R.string.country_tongo), R.drawable.flag_tongo));
        countryList.add(new Country(getString(R.string.country_sunda), R.drawable.flag_sudan));
        countryList.add(new Country(getString(R.string.country_somalia), R.drawable.flag_somalia));
        countryList.add(new Country(getString(R.string.country_niger), R.drawable.flag_niger));
        countryList.add(new Country(getString(R.string.country_namibia), R.drawable.flag_namibia));
        countryList.add(new Country(getString(R.string.country_gambia), R.drawable.flag_gambia));
        countryList.add(new Country(getString(R.string.country_gabon), R.drawable.flag_gabon));
        countryList.add(new Country(getString(R.string.country_eswatini), R.drawable.flag_eswatini));
        countryList.add(new Country(getString(R.string.country_cameroon), R.drawable.flag_cameroom));
        countryList.add(new Country(getString(R.string.country_angola), R.drawable.flag_angola));
        countryList.add(new Country(getString(R.string.country_senegal), R.drawable.flag_senegal));
        countryList.add(new Country(getString(R.string.country_tanzania), R.drawable.flag_tanzania));
        countryList.add(new Country(getString(R.string.country_mali), R.drawable.flag_mali));
        countryList.add(new Country(getString(R.string.country_algeria), R.drawable.flag_algeria));
        countryList.add(new Country(getString(R.string.country_ethiopia), R.drawable.flag_ethiopia));
        countryList.add(new Country(getString(R.string.country_uganda), R.drawable.flag_uganda));
        countryList.add(new Country(getString(R.string.country_ghana), R.drawable.flag_ghana));
        countryList.add(new Country(getString(R.string.country_maroc), R.drawable.flag_maroc));
        countryList.add(new Country(getString(R.string.country_lesotho), R.drawable.flag_lesotho));
        countryList.add(new Country(getString(R.string.country_latvia), R.drawable.flag_latvia));
        countryList.add(new Country(getString(R.string.country_kiribati), R.drawable.flag_kiribati));
        countryList.add(new Country(getString(R.string.country_jamaica), R.drawable.flag_jamaica));
        countryList.add(new Country(getString(R.string.country_jordan), R.drawable.flag_jordan));
        countryList.add(new Country(getString(R.string.country_iran), R.drawable.flag_iran));
        countryList.add(new Country(getString(R.string.country_iraq), R.drawable.flag_iraq));
        countryList.add(new Country(getString(R.string.country_haiti), R.drawable.flag_haiti));
        countryList.add(new Country(getString(R.string.country_hungary), R.drawable.flag_hungary));
        countryList.add(new Country(getString(R.string.country_guyana), R.drawable.flag_guyana));
        countryList.add(new Country(getString(R.string.country_guatemala), R.drawable.flag_guatemala));
        countryList.add(new Country(getString(R.string.country_estonia), R.drawable.flag_estonia));
        countryList.add(new Country(getString(R.string.country_ecuador), R.drawable.flag_ecuador));
        countryList.add(new Country(getString(R.string.country_djibouti), R.drawable.flag_djibouti));
        countryList.add(new Country(getString(R.string.country_dominica), R.drawable.flag_dominica));
        countryList.add(new Country(getString(R.string.country_comoros), R.drawable.flag_comoros));
        countryList.add(new Country(getString(R.string.country_costarica), R.drawable.flag_costarica));
        countryList.add(new Country(getString(R.string.country_croatia), R.drawable.flag_croatia));
        countryList.add(new Country(getString(R.string.country_cuba), R.drawable.flag_cuba));
        countryList.add(new Country(getString(R.string.country_bulgaria), R.drawable.flag_bulgaria));
        countryList.add(new Country(getString(R.string.country_botswana), R.drawable.flag_botswana));
        countryList.add(new Country(getString(R.string.country_benin), R.drawable.flag_benin));
        countryList.add(new Country(getString(R.string.country_belize), R.drawable.flag_belize));
        countryList.add(new Country(getString(R.string.country_bahamas), R.drawable.flag_bahamas));
        countryList.add(new Country(getString(R.string.country_bangladesh), R.drawable.flag_bangladesh));
        countryList.add(new Country(getString(R.string.country_poland), R.drawable.flag_balan));
        countryList.add(new Country(getString(R.string.country_andorra), R.drawable.flag_andorra));
        countryList.add(new Country(getString(R.string.country_albania), R.drawable.flag_albania));
        countryList.add(new Country(getString(R.string.country_cyprus), R.drawable.flag_cyprus));
        countryList.add(new Country(getString(R.string.country_seychelles), R.drawable.flag_seychelles));
        countryList.add(new Country(getString(R.string.country_tchad), R.drawable.flag_tchad));
        countryList.add(new Country(getString(R.string.country_tunisia), R.drawable.flag_tunisia));
        countryList.add(new Country(getString(R.string.country_tonga), R.drawable.flag_tonga));
        countryList.add(new Country(getString(R.string.country_north_korea), R.drawable.flag_north_korea));
        countryList.add(new Country(getString(R.string.country_turkmenistan), R.drawable.flag_turkmenistan));
        countryList.add(new Country(getString(R.string.country_ukraine), R.drawable.flag_ukraina));
        countryList.add(new Country(getString(R.string.country_uruguay), R.drawable.flag_uruguay));
        countryList.add(new Country(getString(R.string.country_uzbekistan), R.drawable.flag_uzbekistan));
        countryList.add(new Country(getString(R.string.country_vanuatu), R.drawable.flag_vanuatu));
        countryList.add(new Country(getString(R.string.country_vatican), R.drawable.flag_vatican));
        countryList.add(new Country(getString(R.string.country_venezuela), R.drawable.flag_venezula));
        countryList.add(new Country(getString(R.string.country_italy), R.drawable.flag_italya));
        countryList.add(new Country(getString(R.string.country_yemen), R.drawable.flag_yemen));
        countryList.add(new Country(getString(R.string.country_zambia), R.drawable.flag_zambia));
        countryList.add(new Country(getString(R.string.country_zimbabwe), R.drawable.flag_zimbabwe));
        countryList.add(new Country(getString(R.string.country_abkhazia), R.drawable.flag_abkhazia));
        countryList.add(new Country(getString(R.string.country_aruba), R.drawable.flag_aruba));
        countryList.add(new Country(getString(R.string.country_greenland), R.drawable.flag_greenland));
        countryList.add(new Country(getString(R.string.country_kosovo), R.drawable.flag_kosovo));
        countryList.add(new Country(getString(R.string.country_niue), R.drawable.flag_niue));
        countryList.add(new Country(getString(R.string.country_scotland), R.drawable.flag_scotland));
        countryList.add(new Country(getString(R.string.country_somaliland), R.drawable.flag_somaliand));
        countryList.add(new Country(getString(R.string.country_south_ossetia), R.drawable.flag_south_ossetia));
        countryList.add(new Country(getString(R.string.country_transnistria), R.drawable.flag_transnistria));
        countryList.add(new Country(getString(R.string.country_wales), R.drawable.flag_wales));
        countryList.add(new Country(getString(R.string.country_honduras), R.drawable.flag_honduras));
        countryList.add(new Country(getString(R.string.country_georgia), R.drawable.flag_gruzia)); // Gruzia
        countryList.add(new Country(getString(R.string.country_grenada), R.drawable.flag_grenada));
        countryList.add(new Country(getString(R.string.country_cape_verde), R.drawable.flag_cape_verde));
        countryList.add(new Country(getString(R.string.country_burkina_faso), R.drawable.flag_burkina_faso));
        countryList.add(new Country(getString(R.string.country_portugal), R.drawable.flag_portugal));
        countryList.add(new Country(getString(R.string.country_barbados), R.drawable.flag_barbados));
        countryList.add(new Country(getString(R.string.country_taliban), R.drawable.flag_taliban));
        countryList.add(new Country(getString(R.string.country_azerbaijan), R.drawable.flag_azerbaijan));



        binding.rcvCountry.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_country(countryList);
        binding.rcvCountry.setAdapter(adapter);


        SearchView searchView = binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        // Handle item click
        adapter.setOnItemClickListener((position, country) -> {
            PopupDialog.getInstance(Activity_country.this)
                    .standardDialogBuilder()
                    .createIOSDialog()
                    .setHeading("Change Nationality")
                    .setPositiveButtonText("Yes")
                    .setDescription("Are you sure you want to choose this country?")
                    .build(new StandardDialogActionListener() {
                        @Override
                        public void onPositiveButtonClicked(Dialog dialog) {
                            getDataUser(country);
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegativeButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        });


        binding.btnBackChangeCountry.setOnClickListener(v -> finish());
    }

    private void getDataUser(Country country){
        Api_service.service.get_account_byId(getID()).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Account account = response.body().get(0);
                    updateCountry(account, country);
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e("onFailure", throwable.getMessage());
            }
        });
    }
    private void updateCountry(Account account, Country country) {
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


    private String getID() {
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("uid", "");
    }
}
