package com.example.sulemaia.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.Model.LandItem;
import com.example.sulemaia.R;

import java.util.ArrayList;
import java.util.Hashtable;

import top.defaults.colorpicker.ColorPickerPopup;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.Item> {

    private Hashtable<Integer, ArrayList<LandItem>> hashCodes;
    private int resource;
    private Activity mainActivity;

    public MainActivityAdapter(Hashtable<Integer, ArrayList<LandItem>> hashCodes, int resource, Activity mainActivity) {
        this.hashCodes = hashCodes;
        this.resource = resource;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Item holder, int position) {
        final int key = Integer.parseInt(hashCodes.keySet().toArray()[position].toString());
        final LandItem item = hashCodes.get(key).get(0);
        final String name = Constants.biomes[position];
        int color = item.getColor();
        String code = String.valueOf(item.getCode());

        for (LandItem i :hashCodes.get(key)) {
            i.setName(name);
        }

        holder.color.setBackgroundColor(color);
        holder.code.setText(code);
        holder.name.setText(name);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText text = new EditText(mainActivity);
                text.setInputType(InputType.TYPE_CLASS_TEXT);
                text.setText(name);

                new AlertDialog.Builder(mainActivity)
                        .setView(text)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (LandItem i :hashCodes.get(key)) {
                                    holder.name.setText(text.getText().toString());
                                    i.setName(text.getText().toString());
                                }
                            }
                        })
                        .setMessage(mainActivity.getString(R.string.change_name_message))
                        .setTitle(mainActivity.getString(R.string.change_name))
                        .show();
            }
        });
        holder.color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerPopup.Builder(mainActivity)
                        .initialColor(Color.RED) // Set initial color
                        .enableBrightness(true) // Enable brightness slider or not
                        .enableAlpha(false) // Enable alpha slider or not
                        .okTitle("Aceptar")
                        .cancelTitle("Cancelar")
                        .showIndicator(true)
                        .showValue(false)
                        .build()
                        .show(v, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                for (LandItem i :hashCodes.get(key)) {
                                    holder.color.setBackgroundColor(color);
                                    i.setColor(color);
                                }
                            }
                        });
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return hashCodes.size();
    }


    public class Item extends RecyclerView.ViewHolder {

        private ImageView color;
        private TextView name;
        private TextView code;

        public Item(@NonNull View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.item_land_color);
            name = itemView.findViewById(R.id.item_land_name);
            code = itemView.findViewById(R.id.item_land_code);
        }
    }
}
