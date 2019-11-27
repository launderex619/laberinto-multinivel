package com.example.sulemaia.Adapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Implementation class of the Character information Dialog.
 */
public class DialogCharacterInformationAdapter extends RecyclerView.Adapter<DialogCharacterInformationAdapter.Item> {

    private CharacterItem item;
    private int resource;
    private Activity activity;

    /**
     * Constructor of the character information dialog.
     * @param item Character item.
     * @param resource Resource code.
     * @param activity Activity context.
     */
    public DialogCharacterInformationAdapter(CharacterItem item, int resource, Activity activity) {
        this.item = item;
        this.resource = resource;
        this.activity = activity;
    }

    /**
     * Take action on the view holder creation.
     * @param parent Viewgroup parent, that cant be null.
     * @param viewType View type code.
     * @return View as an item.
     */
    @NonNull
    @Override
    public DialogCharacterInformationAdapter.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new Item(view);
    }

    /**
     * Take action on the view holder creation.
     * @param holder Item holder, that cant be null.
     * @param position Position code.
     */
    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.cvImage.setImageDrawable(new ColorDrawable(item.getLandsColors().get(position)));
        holder.tvName.setText(item.getLands().get(position));
        if (item.getCanPass().get(position)) {
            holder.tvCost.setText(String.format("%.2f", item.getLandsCosts().get(position)));
        } else {
            holder.tvCost.setText("N/A");
        }

    }

    /**
     * Return the item count.
     * @return item count.
     */
    @Override
    public int getItemCount() {
        return item.getLands().size();
    }

    /**
     * Class implementation of the recycler view.
     */
    public class Item extends RecyclerView.ViewHolder {

        private CircleImageView cvImage;
        private TextView tvName;
        private TextView tvCost;

        /**
         * Constructor of the itemview, that cant be null.
         * @param itemView item view.
         */
        public Item(@NonNull View itemView) {
            super(itemView);
            cvImage = itemView.findViewById(R.id.iv_item_dialog_character_information_color);
            tvName = itemView.findViewById(R.id.tv_item_dialog_character_name);
            tvCost = itemView.findViewById(R.id.tv_item_dialog_character_cost);
        }
    }
}

