package com.example.sulemaia.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Activity.CharacterEditor;
import com.example.sulemaia.Activity.CharacterSelector;
import com.example.sulemaia.Dialog.CustomCharacterInfoDialog;
import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.Interface.iCharacterSelected;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sulemaia.Helper.Constants.characterIcons;

/**
 * Class implementation for the character selector.
 */
public class CharacterSelectorAdapter extends RecyclerView.Adapter<CharacterSelectorAdapter.Item> {

    private ArrayList<CharacterItem> characterItems;
    private int resource;
    private CharacterSelector activity;
    private iCharacterSelected iSelected;


    /**
     * Constructor of the character selector.
     * @param iSelected Selected character.
     * @param characterItems Items corresponding to the character.
     * @param resource Resource code.
     * @param activity Activity context.
     */
    public CharacterSelectorAdapter(iCharacterSelected iSelected,
                                    ArrayList<CharacterItem> characterItems, int resource,
                                    CharacterSelector activity) {
        this.iSelected = iSelected;
        this.characterItems = characterItems;
        this.resource = resource;
        this.activity = activity;

    }

    /**
     * Method to act over the view holder creation.
     * @param parent Parent viewgroup.
     * @param viewType View type code.
     * @return The view as an item.
     */
    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new Item(view);
    }

    /**
     * Method to act over the creation of the view holder.
     * @param holder Holder item.
     * @param position Position code.
     */
    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {

        holder.cvImage.setImageDrawable(characterIcons[characterItems.get(position).getIcon()]);
        holder.tvName.setText(characterItems.get(position).getName());
        holder.tvMain.setText(characterItems.get(position).getMainLand());
        ButtonActions buttonActions = new ButtonActions(holder, position);

        holder.ibInfo.setOnClickListener(buttonActions);
        holder.ibEdit.setOnClickListener(buttonActions);
        holder.cbSelect.setOnClickListener(buttonActions);
    }

    /**
     * Method to get the item count.
     * @return Item count.
     */
    @Override
    public int getItemCount() {
        return characterItems.size();
    }

    /**
     * Class for the implementation of the buttons.
     */
    public class ButtonActions implements View.OnClickListener {
        Item item;
        int pos;

        /**
         * Button constructor.
         * @param holder Item holder.
         * @param pos Position code.
         */
        public ButtonActions(Item holder, int pos) {
            item = holder;
            this.pos = pos;
        }

        /**
         * Take action depending on which float button was tapped.
         * @param v Button tapped.
         */
        @Override
        public void onClick(View v) {
            if (v == item.ibInfo) {
                new CustomCharacterInfoDialog().showDialog(activity, characterItems.get(pos));
            } else if (v == item.ibEdit) {
                Intent intent = new Intent(activity, CharacterEditor.class);
                intent.putExtra("item", characterItems.get(pos));
                intent.putExtra("position", pos);
                activity.startActivityForResult(intent, Constants.RESULT_FOR_CHARACTER_EDITOR);
            } else if (v == item.cbSelect){
                iSelected.setCharacter(pos);
            }
        }
    }

    /**
     * Class that implements the recyclerview.
     */
    public class Item extends RecyclerView.ViewHolder {

        public CircleImageView cvImage;
        public ImageButton ibInfo;
        public ImageButton ibEdit;
        public AppCompatCheckBox cbSelect;
        public TextView tvName;
        public TextView tvMain;

        /**
         * Implementation of the item view, that cant be null.
         * @param itemView
         */
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
