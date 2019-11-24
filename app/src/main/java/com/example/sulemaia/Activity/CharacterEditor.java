package com.example.sulemaia.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Adapter.CharacterEditorAdapter;
import com.example.sulemaia.Dialog.CustomCharacterEditorDialog;
import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.Helper.TapTargetHelper;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sulemaia.Helper.Constants.characterIcons;

public class CharacterEditor extends AppCompatActivity {

    private CharacterItem item;
    private int position;
    private Button btnOk;
    private ImageButton btnDelete;
    private CircleImageView ivImage;
    private EditText etName;
    private RecyclerView rvEditor;
    private CharacterEditorAdapter characterAdapter;
    private LinearLayoutManager mainLayoutManager;
    private ButtonActions buttonActions = new ButtonActions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        item = (CharacterItem) intent.getSerializableExtra("item");
        position = intent.getIntExtra("position", 0);
        setContentView(R.layout.activity_character_editor);

        btnDelete = findViewById(R.id.btn_character_editor_delete);
        etName = findViewById(R.id.et_character_editor_name);
        ivImage = findViewById(R.id.iv_character_editor_image);
        rvEditor = findViewById(R.id.rv_character_editor);
        btnOk = findViewById(R.id.fab_character_editor_ok);
        characterAdapter = new CharacterEditorAdapter(item, R.layout.item_character_editor_activity, this);

        etName.setText(item.getName());
        ivImage.setImageDrawable(characterIcons[item.getIcon()]);
        btnOk.setOnClickListener(buttonActions);
        btnDelete.setOnClickListener(buttonActions);
        ivImage.setOnClickListener(buttonActions);

        mainLayoutManager = new LinearLayoutManager(getApplicationContext()){
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                boolean firstStart = PreferenceManager.getDefaultSharedPreferences(CharacterEditor.this)
                        .getBoolean(Constants.PREF_KEY_FIRST_START_CHARACTER_EDITOR, true);
                //checamos si es la primera vez que la aplicacion inicia:
                if (firstStart) {
                    View view = mainLayoutManager.getChildAt(0);
                    AppCompatCheckBox cbSelect = view.findViewById(R.id.cb_item_character_editor_apply);
                    EditText etCost = view.findViewById(R.id.et_item_character_editor_cost);

                    //iniciamos el tutorial
                    PreferenceManager.getDefaultSharedPreferences(CharacterEditor.this).edit()
                            .putBoolean(Constants.PREF_KEY_FIRST_START_CHARACTER_EDITOR, false)
                            .apply();
                    new TapTargetSequence(CharacterEditor.this).targets(
                            new TapTargetHelper(CharacterEditor.this,
                                    ivImage,
                                    getString(R.string.icon),
                                    getString(R.string.icon_character_description)).Create(),
                            new TapTargetHelper(CharacterEditor.this, btnDelete,
                                    getString(R.string.delete_character),
                                    getString(R.string.delete_character_description)).Create(),
                            new TapTargetHelper(CharacterEditor.this, etName,
                                    getString(R.string.character_name),
                                    getString(R.string.change_name_description)).Create(),
                            new TapTargetHelper(CharacterEditor.this, cbSelect,
                                    getString(R.string.aply_cost),
                                    getString(R.string.aply_cost_description)).Create(),
                            new TapTargetHelper(CharacterEditor.this, etCost,
                                    getString(R.string.cost),
                                    getString(R.string.cost_description)).Create()
                    ).start();
                }

            }
        };
        mainLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvEditor.setLayoutManager(mainLayoutManager);
        rvEditor.setAdapter(characterAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }

    private class ButtonActions implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == btnOk) {
                boolean canOk = true;
                CharacterItem item = characterAdapter.getCharacterItem();
                String name = etName.getText().toString();
                if (name.equals("")) {
                    etName.setError(getString(R.string.not_valid_value), getDrawable(R.drawable.ic_warning_lime_24dp));
                    canOk = false;
                } else {
                    item.setName(name);
                }
                for (int i = 0; i < mainLayoutManager.getChildCount(); i++) {
                    View view = mainLayoutManager.getChildAt(i);
                    AppCompatCheckBox cbSelect = view.findViewById(R.id.cb_item_character_editor_apply);
                    EditText etCost = view.findViewById(R.id.et_item_character_editor_cost);
                    if (cbSelect.isChecked()) {
                        String etTextCost = etCost.getText().toString();
                        if (etTextCost.equals("") || etTextCost.equals(".")) {
                            etCost.setError(getString(R.string.not_valid_value), getDrawable(android.R.drawable.ic_dialog_alert));
                            canOk = false;
                        } else {
                            float cost = 0.00f;
                            cost += Float.parseFloat(etTextCost);
                            if (cost < 0) {
                                etCost.setError(getString(R.string.not_valid_value), getDrawable(android.R.drawable.ic_dialog_alert));
                                canOk = false;
                            }
                        }
                    }
                }
                if (canOk) {
                    Intent result = new Intent();
                    result.putExtra("item", item);
                    result.putExtra("isDeleted", false);
                    result.putExtra("position", position);
                    setResult(RESULT_OK, result);
                    finish();
                }
            } else if (v == btnDelete) {
                new AlertDialog.Builder(CharacterEditor.this)
                        .setIcon(R.drawable.ic_warning_lime_24dp)
                        .setTitle(getString(R.string.delete_character))
                        .setMessage(getString(R.string.confirmation_delete_character))
                        .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent result = new Intent();
                                result.putExtra("item", item);
                                result.putExtra("isDeleted", true);
                                result.putExtra("position", position);
                                setResult(RESULT_OK, result);
                                finish();
                            }

                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
            } else if (v == ivImage) {
                final CustomCharacterEditorDialog cdialog = new CustomCharacterEditorDialog();
                final Dialog dialog = cdialog.showDialog(CharacterEditor.this);
                Button mDialogOk = dialog.findViewById(R.id.btn_dialog_character_editor_ok);
                mDialogOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int iconPos = cdialog.getAdapter().getIconSelected();
                        if (iconPos != -1) {
                            ivImage.setImageDrawable(characterIcons[iconPos]);
                            item.setIcon(iconPos);
                        }
                        dialog.dismiss();
                    }
                });
            }
        }
    }
}