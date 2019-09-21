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

public class DialogCharacterEditorAdapter extends RecyclerView.Adapter<DialogCharacterEditorAdapter.Item> {

    int iconSelected = -1;
    private int resource;
    private Activity activity;
    private iDialogCharacterIconSelected iIconSelected;

    public DialogCharacterEditorAdapter(iDialogCharacterIconSelected iIconSelected, int resource, Activity activity) {
        this.resource = resource;
        this.activity = activity;
        this.iIconSelected = iIconSelected;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Item holder, final int position) {
        holder.cvImage.setImageDrawable(characterIcons[position]);
        holder.cvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconSelected = position;
                iIconSelected.iconSelected(position);
                holder.cvCard.setBackgroundColor(0xff80e5ec);
            }
        });
    }

    public int getIconSelected() {
        return iconSelected;
    }

    @Override
    public int getItemCount() {
        return characterIcons.length;
    }

    public class Item extends RecyclerView.ViewHolder {

        private CircleImageView cvImage;
        private CardView cvCard;

        public Item(@NonNull View itemView) {
            super(itemView);
            cvImage = itemView.findViewById(R.id.iv_item_dialog_character_editor_icon);
            cvCard = itemView.findViewById(R.id.cv_item_dialog_character_editor_holder);
        }
    }
}
