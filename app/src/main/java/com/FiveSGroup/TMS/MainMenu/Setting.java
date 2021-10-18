package com.FiveSGroup.TMS.MainMenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.FiveSGroup.TMS.R;

public class Setting extends AppCompatActivity {
    Button btn_save , btn_back;
    RadioButton radioButton , radio_default , radio_honeywell  ;
    RadioGroup radioGroup_diffLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        getinit();
        action();
        loadSetting();

    }

    private void getinit(){
        btn_save = findViewById(R.id.button_save);
        btn_back= findViewById(R.id.button_back);
        radioGroup_diffLevel = findViewById(R.id.radioGroup_diffLevel);
//        radio_bluetooth = findViewById(R.id.radio_bluetooth);
        radio_default = findViewById(R.id.radio_default);
        radio_honeywell = findViewById(R.id.radio_honeywell);
    }
    private void action(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSetting();

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveSetting(){
        SharedPreferences sharedPreferences= this.getSharedPreferences("appSetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Checked RadioButton ID.
        int checkedRadioButtonId = radioGroup_diffLevel.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(checkedRadioButtonId);
        Toast.makeText(Setting.this,
                radioButton.getText(), Toast.LENGTH_SHORT).show();
        editor.putInt("checkedRadioButtonId", checkedRadioButtonId);
        editor.putString("checked",radioButton.getText().toString());
        // Save with default id : 2131296695 honeywell id : 2131296696  bluetooth id :
        editor.apply();
        Toast.makeText(this,"App Setting saved!",Toast.LENGTH_LONG).show();

    }

    private void loadSetting(){
        SharedPreferences sharedPreferences= this.getSharedPreferences("appSetting", Context.MODE_PRIVATE);
        if(sharedPreferences!= null) {
            int checkedRadioButtonId = sharedPreferences.getInt("checkedRadioButtonId", R.id.radio_default);
            radioGroup_diffLevel.check(checkedRadioButtonId);

        } else {
            radioGroup_diffLevel.check(R.id.radio_default);
            Toast.makeText(this,"Use the default app setting",Toast.LENGTH_LONG).show();
        }

    }


}
