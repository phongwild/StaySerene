package phongtaph31865.poly.stayserene.Screen_user.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import phongtaph31865.poly.stayserene.R;

public class Activity_detail_type_rooms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_type_rooms);
        Intent intent = getIntent();
        String id_type_room = intent.getStringExtra("id_type_room");

    }
}