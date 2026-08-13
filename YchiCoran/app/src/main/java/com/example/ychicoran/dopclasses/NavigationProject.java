package com.example.ychicoran.dopclasses;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ychicoran.HomeActivity;
import com.example.ychicoran.PlaylistActivity;
import com.example.ychicoran.QranActivity;
import com.example.ychicoran.R;
import com.example.ychicoran.SettingsActivity;

public class NavigationProject {
    public static void setup(final Activity activity){



        LinearLayout menuHome = activity.findViewById(R.id.menu_home);
        LinearLayout menuQuran = activity.findViewById(R.id.menu_quran);
        LinearLayout menuPlayList = activity.findViewById(R.id.menu_play_list);
        LinearLayout menuUser = activity.findViewById(R.id.menu_settings);

        if (activity instanceof HomeActivity) {
            highlightIcon(activity, R.id.menu_home);
        } else if (activity instanceof QranActivity) {
            highlightIcon(activity, R.id.menu_quran);
        } else if (activity instanceof PlaylistActivity) {
            highlightIcon(activity, R.id.menu_play_list);
        } else if (activity instanceof SettingsActivity) {
            highlightIcon(activity, R.id.menu_settings);
        }

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
            menuUser.setOnClickListener(v -> startingActivity(activity, SettingsActivity.class));
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
    private static void highlightIcon(Activity activity, int layoutId) {
        LinearLayout layout = activity.findViewById(layoutId);
        if (layout != null) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                if (child instanceof ImageView) {
                    child.setBackgroundResource(R.drawable.ic_bc);
                    break;
                }

            }
        }
    }
}
