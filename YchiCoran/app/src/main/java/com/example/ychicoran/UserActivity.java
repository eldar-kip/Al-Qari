package com.example.ychicoran;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        LinearLayout menuHome = findViewById(R.id.menu_home);
        LinearLayout menuQuran = findViewById(R.id.menu_quran);
        LinearLayout menuPlayList = findViewById(R.id.menu_pley_list);

        menuHome.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, HomeActivity.class);
            startActivity(intent);
            onStop();
        });
        menuQuran.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, QranActivity.class);
            startActivity(intent);
            onStop();
        });
        menuPlayList.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, PlaylistActivity.class);
            startActivity(intent);
            onStop();
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        finish();
    }
    }
