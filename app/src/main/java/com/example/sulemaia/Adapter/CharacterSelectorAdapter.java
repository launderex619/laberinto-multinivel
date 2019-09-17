package com.example.sulemaia.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class CharacterSelectorAdapter extends RecyclerView.Adapter<CharacterSelectorAdapter.Item>{

    private ArrayList<CharacterItem> characterItems;
    private int resource;
    private CharacterSelector activity;

    public CharacterSelectorAdapter(ArrayList<CharacterItem> characterItems, int resource,
                                    CharacterSelector activity) {
        this.characterItems = characterItems;
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
        holder.cvImage.setImageDrawable(characterItems.get(position).getIcon());
        holder.tvName.setText(characterItems.get(position).getName());
        holder.tvMain.setText(characterItems.get(position).getMainLand());
        ButtonActions buttonActions = new ButtonActions(holder, position);

        holder.ibInfo.setOnClickListener(buttonActions);
    }

    public class ButtonActions implements View.OnClickListener {
        Item item;
        int pos;
        public ButtonActions(Item holder, int pos) {
            item = holder;
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            if(v == item.ibInfo){
                new CustomCharacterInfoDialog().showDialog(activity, characterItems.get(pos));
            }
        }
    }
    @Override
    public int getItemCount() {
        return characterItems.size();
    }

    public class Item extends RecyclerView.ViewHolder {

        private CircleImageView cvImage;
        private ImageButton ibInfo;
        private ImageButton ibEdit;
        private AppCompatCheckBox cbSelect;
        private TextView tvName;
        private TextView tvMain;

        public Item(@NonNull View itemView) {
            super(itemView);
            cvImage = itemView.findViewById(R.id.iv_item_image_character);
            tvName = itemView.findViewById(R.id.tv_item_character_name);
            tvMain = itemView.findViewById(R.id.tv_item_character_main);
            ibInfo = itemView.findViewById(R.id.btn_item_info_character);
            ibEdit = itemView.findViewById(R.id.btn_item_edit_character);
            cbSelect = itemView.findViewById(R.id.cb_item_select_character);
        }
    }
}
