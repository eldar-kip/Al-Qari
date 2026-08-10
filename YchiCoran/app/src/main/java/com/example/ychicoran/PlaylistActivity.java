package com.example.ychicoran;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    LinearLayout menuHome = findViewById(R.id.menu_home);
    LinearLayout menuQuran = findViewById(R.id.menu_quran);
    LinearLayout menuUser = findViewById(R.id.menu_user);

    menuHome.setOnClickListener( v -> {
        Intent intent = new Intent(PlaylistActivity.this, HomeActivity.class);
        startActivity(intent);
        onStop();
    });
    menuQuran.setOnClickListener( v -> {
        Intent intent = new Intent(PlaylistActivity.this, QranActivity.class);
        startActivity(intent);
        onStop();
    });
    menuUser.setOnClickListener( v -> {
        Intent intent = new Intent(PlaylistActivity.this, UserActivity.class);
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

