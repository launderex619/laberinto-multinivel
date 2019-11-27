package com.example.sulemaia.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Helper.Parser;
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

    public CharacterItem getCharacterItem() {
        return characterItem;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new Item(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull final Item holder, final int position) {
        holder.etCost.setFilters(new InputFilter[]{new Parser.DecimalDigitsInputFilter(5, 2)});
        holder.etCost.setText(String.format("%.2f", characterItem.getLandsCosts().get(position)));
        holder.etCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    characterItem.getLandsCosts().set(position, Float.parseFloat(holder.etCost.getText().toString()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.tvName.setText(characterItem.getLands().get(position));
        holder.cvImage.setImageDrawable(new ColorDrawable(characterItem.getLandsColors().get(position)));
        holder.cbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.cbSelect.isChecked()) {
                    holder.etCost.setText("");
                    holder.etCost.setHint("N/A");
                    holder.etCost.setEnabled(false);
                    characterItem.getCanPass().set(position, false);
                } else {
                    holder.etCost.setText(String.format("%.2f", characterItem.getLandsCosts().get(position)));
                    holder.etCost.setHint(activity.getString(R.string.cost));
                    holder.etCost.setEnabled(true);
                    characterItem.getCanPass().set(position, true);
                }
            }
        });
        if (!characterItem.getCanPass().get(position)){
            holder.cbSelect.setChecked(false);
            holder.etCost.setText("");
            holder.etCost.setHint("N/A");
            holder.etCost.setEnabled(false);
        }
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
