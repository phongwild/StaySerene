package phongtaph31865.poly.stayserene.Screen_admin.Fragment_admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import phongtaph31865.poly.stayserene.CardAdapter;
import phongtaph31865.poly.stayserene.CardModel;
import phongtaph31865.poly.stayserene.R;


public class Fragment_home_admin extends Fragment {
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private CardAdapter cardAdapter;
    private List<CardModel> cardList1;
    private List<CardModel> cardList2;
    //    private FloatingActionButton fab;
//    private BottomNavigationView bottomNavigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        // Tìm các RecyclerView và các thành phần khác bằng view
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
//        FloatingActionButton fab = view.findViewById(R.id.fab);
//        BottomNavigationView bottomNav = view.findViewById(R.id.bottomNavigationView);

        // Cài đặt Layout cho RecyclerView
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        // Tạo danh sách 20 thẻ cho RecyclerView1
        cardList1 = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            cardList1.add(new CardModel("1d3d432dfd" + i, "4dj1d0q1" + i, "18/9/2024", "2,000,000", "Online payment", "Wait for confirmation"));
        }

        // Tạo danh sách 20 thẻ cho RecyclerView2
        cardList2 = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            cardList2.add(new CardModel("1d3d432dfd" + i, "4dj1d0q1" + i, "18/9/2024", "2,000,000", "Online payment", "Confirmed"));
        }

        // Gán Adapter cho RecyclerView1 và RecyclerView2
        cardAdapter = new CardAdapter(cardList1, getContext());
        recyclerView1.setAdapter(cardAdapter);
        cardAdapter = new CardAdapter(cardList2, getContext());
        recyclerView2.setAdapter(cardAdapter);

        return view; // Trả về view đã inflate
    }

}
