package com.example.sulemaia.Adapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CharacterEditorAdapter extends RecyclerView.Adapter<CharacterEditorAdapter.Item> {

    private CharacterItem characterItem;
    private int resource;
    private Activity activity;

    public CharacterEditorAdapter(CharacterItem characterItem, int resource,
                                  Activity activity) {
        this.characterItem = characterItem;
        this.resource = resource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        //holder.etCost.setText(characterItem.getLandsCosts().get(position).toString());
        holder.tvName.setText(characterItem.getName());
        holder.cvImage.setImageDrawable(new ColorDrawable(characterItem.getLandsColors().get(position)));
    }

    @Override
    public int getItemCount() {
        return characterItem.getLands().size();
    }

    public class Item extends RecyclerView.ViewHolder {

        public CircleImageView cvImage;
        public AppCompatCheckBox cbSelect;
        public TextView tvName;
        public EditText etCost;

        public Item(@NonNull View itemView) {
            super(itemView);
            cvImage = itemView.findViewById(R.id.iv_item_character_editor_information_color);
            tvName = itemView.findViewById(R.id.tv_item_character_editor_name);
            cbSelect = itemView.findViewById(R.id.cb_item_character_editor_apply);
            etCost = itemView.findViewById(R.id.et_item_character_editor_cost);
        }
    }
}
