package com.example.sulemaia.Helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.example.sulemaia.R;
import com.getkeepsafe.taptargetview.TapTarget;

/**
 * Class implementation for the tutorials.
 */
public class TapTargetHelper {
    private Context context;
    private View target;
    private String title;
    private String description;

    /**
     * Designation of the basic variables for the method.
     * @param context activity context.
     * @param target target view.
     * @param title tutorial title.
     * @param description tutorial description.
     */
    public TapTargetHelper(Context context, View target, String title, String description) {
        this.context = context;
        this.target = target;
        this.title = title;
        this.description = description;
    }

    /**
     * Creation of the tap target helper, with specific graphic characteristics.
     * @return taptarget for view.
     */
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

    /**
     * The same method as the last one, but with an icon; Deprecated.
     * @param icon icon that was going to be shown with each tutoria point.
     * @return taptarget for view.
     */
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
