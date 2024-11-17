package phongtaph31865.poly.stayserene.adapter;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;

import java.util.List;

import phongtaph31865.poly.stayserene.Api_service.Api_service;
import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.Model.Country;
import phongtaph31865.poly.stayserene.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_country extends RecyclerView.Adapter<Adapter_country.ViewHolder> {
    private List<Country> countryList;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Adapter_country(List<Country> countryList) {
        this.countryList = countryList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_country, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.flag.setImageResource(country.getFlag_country());
        holder.name.setText(country.getName());
        holder.itemView.setOnClickListener(v -> {
            PopupDialog.getInstance(holder.itemView.getContext())
                    .standardDialogBuilder()
                    .createIOSDialog()
                    .setHeading("Change Nationality")
                    .setDescription("Are you sure you want to choose this country?")
                    .build(new StandardDialogActionListener() {
                        @Override
                        public void onPositiveButtonClicked(Dialog dialog) {
                            updateCountry(country, holder);
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegativeButtonClicked(Dialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        });
    }
    private void updateCountry(Country country, ViewHolder holder) {
        Account account = new Account();
        account.setQuocTich(country.getName());
        Api_service.service.update_account(getUid(), account).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(holder.itemView.getContext(), "Change country successfully", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {
                Log.e("onFailure", throwable.getMessage());
            }
        });
    }
    @Override
    public int getItemCount() {
        if (countryList != null) {
            return countryList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView flag;
        private TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.item_flag);
            name = itemView.findViewById(R.id.item_name_country);

        }
    }
}
