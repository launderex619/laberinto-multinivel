package com.example.sulemaia.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Interface.iDialogCharacterIconSelected;
import com.example.sulemaia.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sulemaia.Helper.Constants.characterIcons;

/**
 * Class to implement the Character editor dialog.
 */
public class DialogCharacterEditorAdapter extends RecyclerView.Adapter<DialogCharacterEditorAdapter.Item> {

    int iconSelected = -1;
    private int resource;
    private Activity activity;
    private iDialogCharacterIconSelected iIconSelected;

    /**
     * Constructor of the character editor dialog.
     * @param iIconSelected Character icon dialog.
     * @param resource Resource code.
     * @param activity Activity context.
     */
    public DialogCharacterEditorAdapter(iDialogCharacterIconSelected iIconSelected, int resource, Activity activity) {
        this.resource = resource;
        this.activity = activity;
        this.iIconSelected = iIconSelected;
    }

    /**
     * Take action on the view holder creation.
     * @param parent Parent viewgroup.
     * @param viewType Viewtype code.
     * @return View as an item.
     */
    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new Item(view);
    }

    /**
     * Take action on the bind view holder.
     * @param holder Item holder, that cant be null.
     * @param position Position code.
     */
    @Override
    public void onBindViewHolder(@NonNull final Item holder, final int position) {
        holder.cvImage.setImageDrawable(characterIcons[position]);
        holder.cvImage.setOnClickListener(new View.OnClickListener() {
            /**
             * Implementation of the tapped button.
             * @param v
             */
            @Override
            public void onClick(View v) {
                iconSelected = position;
                iIconSelected.iconSelected(position);
                holder.cvCard.setBackgroundColor(0xff80e5ec);
            }
        });
    }

    /**
     * Return the selected icon.
     * @return Selected icon.
     */
    public int getIconSelected() {
        return iconSelected;
    }

    /**
     * Return the item count.
     * @return item count.
     */
    @Override
    public int getItemCount() {
        return characterIcons.length;
    }

    /**
     * Implementation of the recycler view.
     */
    public class Item extends RecyclerView.ViewHolder {

        private CircleImageView cvImage;
        private CardView cvCard;

        /**
         * Constructor of the item view, that cant be null.
         * @param itemView item view.
         */
        public Item(@NonNull View itemView) {
            super(itemView);
            cvImage = itemView.findViewById(R.id.iv_item_dialog_character_editor_icon);
            cvCard = itemView.findViewById(R.id.cv_item_dialog_character_editor_holder);
        }
    }
}
