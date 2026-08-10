package com.example.ychicoran.dopclasses;

import android.app.Activity;
import android.content.Intent;
import android.widget.LinearLayout;

import com.example.ychicoran.HomeActivity;
import com.example.ychicoran.PlaylistActivity;
import com.example.ychicoran.QranActivity;
import com.example.ychicoran.R;
import com.example.ychicoran.UserActivity;

public class NavigationProject {
    public static void setup(final Activity activity){



        LinearLayout menuHome = activity.findViewById(R.id.menu_home);
        LinearLayout menuQuran = activity.findViewById(R.id.menu_quran);
        LinearLayout menuPlayList = activity.findViewById(R.id.menu_pley_list);
        LinearLayout menuUser = activity.findViewById(R.id.menu_user);
        if (menuHome != null){
            menuHome.setOnClickListener(v -> startingActivity(activity, HomeActivity.class));
        }
        if (menuQuran != null){
            menuQuran.setOnClickListener(v -> startingActivity(activity, QranActivity.class));
        }
        if (menuPlayList != null){
            menuPlayList.setOnClickListener(v -> startingActivity(activity, PlaylistActivity.class));
        }
        if (menuUser != null){
            menuUser.setOnClickListener(v -> startingActivity(activity, UserActivity.class));
        }

    }
    private static void startingActivity(Activity activity, Class<?> activityClass){
        if(activity.getClass().equals(activityClass)){
            return;
        }
        else {
            Intent intent = new Intent(activity, activityClass);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}
