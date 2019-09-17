package com.example.sulemaia.Adapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Activity.CharacterSelector;
import com.example.sulemaia.Dialog.CustomCharacterInfoDialog;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogCharacterInformationAdapter extends RecyclerView.Adapter<DialogCharacterInformationAdapter.Item> {

    private ArrayList<String> biomes;
    private ArrayList<Float> costs;
    private ArrayList<Integer> colors;
    private int resource;
    private Activity activity;

    public DialogCharacterInformationAdapter(ArrayList<String> biomes, ArrayList<Float> costs,
                                             ArrayList<Integer> colors, int resource, Activity activity) {
        this.biomes = biomes;
        this.costs = costs;
        this.colors = colors;
        this.resource = resource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public DialogCharacterInformationAdapter.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.cvImage.setImageDrawable(new ColorDrawable(colors.get(position)));;
        holder.tvName.setText(biomes.get(position));
        holder.tvCost.setText("" + costs.get(position));

    }
    @Override
    public int getItemCount() {
        return biomes.size();
    }

    public class Item extends RecyclerView.ViewHolder {

        private CircleImageView cvImage;
        private TextView tvName;
        private TextView tvCost;

        public Item(@NonNull View itemView) {
            super(itemView);
            cvImage = itemView.findViewById(R.id.iv_item_dialog_character_information_color);
            tvName = itemView.findViewById(R.id.tv_item_dialog_character_name);
            tvCost = itemView.findViewById(R.id.tv_item_dialog_character_cost);
        }
    }
}

