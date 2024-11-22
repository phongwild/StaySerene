//package phongtaph31865.poly.stayserene.adapter;
//
//import android.app.Dialog;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.saadahmedev.popupdialog.PopupDialog;
//import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;
//
//import java.util.List;
//
//import phongtaph31865.poly.stayserene.Api_service.Api_service;
//import phongtaph31865.poly.stayserene.Model.Account;
//import phongtaph31865.poly.stayserene.Model.Country;
//import phongtaph31865.poly.stayserene.R;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class Adapter_country extends RecyclerView.Adapter<Adapter_country.ViewHolder> {
//    private List<Country> countryList;
//    private String uid;
//    private OnItemClickListener onItemClickListener;
//    public interface OnItemClickListener{
//        void onItemCLick(int position, Country country);
//    }
//    public void setOnItemClickListener(OnItemClickListener listener){
//        this.onItemClickListener = listener;
//    }
//    public String getUid() {
//        return uid;
//    }
//
//    public void setUid(String uid) {
//        this.uid = uid;
//    }
//
//    public Adapter_country(List<Country> countryList) {
//        this.countryList = countryList;
//    }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = View.inflate(parent.getContext(), R.layout.item_country, null);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Country country = countryList.get(position);
//        holder.flag.setImageResource(country.getFlag_country());
//        holder.name.setText(country.getName());
//        holder.itemView.setOnClickListener(v -> {
//            if (onItemClickListener != null) {
//                onItemClickListener.onItemCLick(position, country);
//            }
//        });
//    }
//    @Override
//    public int getItemCount() {
//        if (countryList != null) {
//            return countryList.size();
//        }
//        return 0;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private ImageView flag;
//        private TextView name;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            flag = itemView.findViewById(R.id.item_flag);
//            name = itemView.findViewById(R.id.item_name_country);
//        }
//    }
//}



package phongtaph31865.poly.stayserene.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import phongtaph31865.poly.stayserene.Model.Country;
import phongtaph31865.poly.stayserene.R;

public class Adapter_country extends RecyclerView.Adapter<Adapter_country.ViewHolder> {
    private List<Country> countryList;
    private List<Country> countryListFull;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, Country country);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public Adapter_country(List<Country> countryList) {
        this.countryList = new ArrayList<>(countryList);
        this.countryListFull = new ArrayList<>(countryList);
    }


    public void filter(String text) {
        countryList.clear();
        if (text.isEmpty()) {
            countryList.addAll(countryListFull);
        } else {
            text = text.toLowerCase();
            for (Country country : countryListFull) {
                if (country.getName().toLowerCase().contains(text)) {
                    countryList.add(country);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.flag.setImageResource(country.getFlag_country());
        holder.name.setText(country.getName());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, country);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
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
