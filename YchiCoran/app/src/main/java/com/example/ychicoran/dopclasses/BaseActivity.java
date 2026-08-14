package com.example.ychicoran.dopclasses;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ychicoran.R;

public abstract class BaseActivity extends AppCompatActivity {
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundResource(R.color.background_layout);
        NavigationProject.setup(this);
    }


}
