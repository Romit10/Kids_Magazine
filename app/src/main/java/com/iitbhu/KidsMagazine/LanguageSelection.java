package com.iitbhu.KidsMagazine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class LanguageSelection extends Activity {
    // Array of strings...
    String[] languages = {"Bengali","Kannada"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languageselection);

        Button bn = (Button) findViewById((R.id.BN));
        Button kn = (Button) findViewById((R.id.KN));

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int key = 1;
                Intent intent = new Intent(LanguageSelection.this, HomeActivity.class);
                intent.putExtra("lang_key",key);
                startActivity(intent);
            }
        });
        kn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int key = 2;
                Intent intent = new Intent(LanguageSelection.this, HomeActivity.class);
                intent.putExtra("lang_key",key);
                startActivity(intent);
            }
        });
    }
}
