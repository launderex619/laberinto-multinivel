package com.example.sulemaia.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Adapter.DialogCharacterEditorAdapter;
import com.example.sulemaia.Interface.iDialogCharacterIconSelected;
import com.example.sulemaia.R;

public class CustomCharacterEditorDialog implements iDialogCharacterIconSelected {

    private Dialog dialog;
    private int iconPos = -1;
    private RecyclerView rvItems;
    private DialogCharacterEditorAdapter adapter;
    private LinearLayoutManager mainLayoutManager;

    public Dialog showDialog(final Activity activity) {
        dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.setTitle(activity.getString(R.string.choose_icon));
        dialog.setContentView(R.layout.dialog_character_editor);

        rvItems = dialog.findViewById(R.id.rv_dialog_character_editor_icon);

        adapter = new DialogCharacterEditorAdapter(this,R.layout.item_dialog_character_editor,
                activity);

        mainLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        mainLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItems.setLayoutManager(mainLayoutManager);
        rvItems.setAdapter(adapter);

        dialog.show();
        return dialog;
    }

    public DialogCharacterEditorAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void iconSelected(int pos) {
        for (int i = 0; i < mainLayoutManager.getChildCount(); i++) {
            CardView cvCard = mainLayoutManager.getChildAt(i).findViewById(R.id.cv_item_dialog_character_editor_holder);
            if (i != pos){
                cvCard.setBackgroundColor(Color.WHITE);
            }
        }
    }
}
