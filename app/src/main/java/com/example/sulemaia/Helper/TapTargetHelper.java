package com.example.sulemaia.Helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.example.sulemaia.R;
import com.getkeepsafe.taptargetview.TapTarget;

public class TapTargetHelper {
    private Context context;
    private View target;
    private String title;
    private String description;

    public TapTargetHelper(Context context, View target, String title, String description) {
        this.context = context;
        this.target = target;
        this.title = title;
        this.description = description;
    }

    public TapTarget Create(){
        return TapTarget.forView(target, title, description)
                // All options below are optional
                .titleTextSize(40)                  // Specify the size (in sp) of the title text
                .titleTextColor(R.color.white)      // Specify the color of the title text
                .descriptionTextSize(30)            // Specify the size (in sp) of the description text
                .drawShadow(true)                   // Whether to draw a drop shadow or not
                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                .tintTarget(false)                   // Whether to tint the target view's color
                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                .targetRadius(60);                  // Specify the target radius (in dp)
    }

    public TapTarget CreateWithIcon(Drawable icon) {
        return TapTarget.forView(target, title, description)
                // All options below are optional
                .icon(icon)
                .titleTextSize(40)                  // Specify the size (in sp) of the title text
                .titleTextColor(R.color.white)      // Specify the color of the title text
                .descriptionTextSize(30)            // Specify the size (in sp) of the description text
                .drawShadow(true)                   // Whether to draw a drop shadow or not
                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                .tintTarget(false)                   // Whether to tint the target view's color
                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                .targetRadius(60);                  // Specify the target radius (in dp)
    }
}
