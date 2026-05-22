package com.example.gymmanager.utils;

import android.app.Activity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationHelper {

    public static void fadeIn(View view) {
        AlphaAnimation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(500);
        view.startAnimation(animation);
    }

    public static void slideUp(View view) {
        TranslateAnimation animation = new TranslateAnimation(
                0,
                0,
                80,
                0
        );

        animation.setDuration(500);
        view.startAnimation(animation);
    }

    public static void applyOpenTransition(Activity activity) {
        activity.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
    }
}