package com.example.sulemaia.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Adapter.CharacterEditorAdapter;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sulemaia.Helper.Constants.icons;

public class CharacterEditor extends AppCompatActivity {

    private Button btnOk;
    private ImageButton btnDelete;
    private CircleImageView ivImage;
    private EditText etName;
    private RecyclerView rvEditor;
    private CharacterEditorAdapter characterAdapter;
    private ButtonActions buttonActions = new ButtonActions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        CharacterItem item = (CharacterItem) intent.getSerializableExtra("item");
        setContentView(R.layout.activity_character_editor);

        btnDelete = findViewById(R.id.btn_character_editor_delete);
        etName = findViewById(R.id.et_character_editor_name);
        ivImage = findViewById(R.id.iv_character_editor_image);
        rvEditor = findViewById(R.id.rv_character_editor);
        btnOk = findViewById(R.id.fab_character_editor_ok);
        characterAdapter = new CharacterEditorAdapter(item, R.layout.item_character_editor_activity, this);

        etName.setText(item.getName());
        ivImage.setImageDrawable(icons[item.getIcon()]);
        btnOk.setOnClickListener(buttonActions);
        btnDelete.setOnClickListener(buttonActions);

        LinearLayoutManager mainLayoutManager = new LinearLayoutManager(getApplicationContext());
        mainLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvEditor.setLayoutManager(mainLayoutManager);
        rvEditor.setAdapter(characterAdapter);


    }


    private class ButtonActions implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == btnOk) {

            } else if (v == btnDelete) {

            }
        }
    }
}