package com.example.sulemaia.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Activity.CharacterSelector;
import com.example.sulemaia.Adapter.CharacterSelectorAdapter;
import com.example.sulemaia.Adapter.DialogCharacterInformationAdapter;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomCharacterInfoDialog {

    public void showDialog(final Activity activity, CharacterItem item) {
        final Dialog dialog = new Dialog(activity);
        Button mDialogOk;
        CircleImageView ivImage;
        TextView tvName;
        RecyclerView rvItems;
        DialogCharacterInformationAdapter adapter;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_item_character_information);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mDialogOk = dialog.findViewById(R.id.btn_dialog_item_character_ok);
        tvName =  dialog.findViewById(R.id.tv_dialog_item_character_name);
        ivImage = dialog.findViewById(R.id.iv_dialog_item_character_image);
        rvItems = dialog.findViewById(R.id.rv_dialog_item_character);

        tvName.setText(item.getName());
        ivImage.setImageDrawable(item.getIcon());
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"Okay" , Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        adapter = new DialogCharacterInformationAdapter(item.getLands(),
                item.getLandsCosts(),
                item.getLandsColors(),
                R.layout.item_dialog_character_information,
                activity);

        LinearLayoutManager mainLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        mainLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItems.setLayoutManager(mainLayoutManager);
        rvItems.setAdapter(adapter);


        dialog.show();
    }
}
