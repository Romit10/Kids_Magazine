package com.iitbhu.KidsMagazine;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.Locale;

public class Details extends AppCompatActivity {
    int flag;
    TextView storyContent,otherContent;
    private SeekBar mSeekBarPitch;
    private SeekBar mSeekBarSpeed;
    private Button mButtonSpeak,mButtonSpeakOther;
    private TextToSpeech mTTS,mTTSOther;
    String content;
    String contentOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final LinearLayout lm=findViewById(R.id.story_layout);
        Intent i = getIntent();
        String title = i.getStringExtra("titleOfStory");
        content = i.getStringExtra("contentOfStory");
        flag = i.getIntExtra("key",1);
        storyContent = findViewById(R.id.contentOfStory);
        otherContent =findViewById(R.id.otherContent);
        final ToggleButton tb=findViewById(R.id.toggleButton);

        // set the appbar title as Story title
        getSupportActionBar().setTitle(title);
        translateText();
         //set content of the story to textview
        Button b =  (Button) findViewById(R.id.button_speak_other);
        if(flag == 2)
        b.setText("Speak Kannada!!");
        else b.setText("Speak Bengali!!");
        if(flag == 1)
        {
            otherContent.setText(i.getStringExtra("contentOfStoryBeng"));
            otherContent.setMovementMethod(new ScrollingMovementMethod());
        }
        else {
            otherContent.setText(i.getStringExtra("contentOfStoryKnd"));
            otherContent.setMovementMethod(new ScrollingMovementMethod());
        }

        mButtonSpeakOther=findViewById(R.id.button_speak_other);

        mTTSOther = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result;
                    if(flag==1){
                        result = mTTSOther.setLanguage(new Locale("bn_IN"));
                    }
                    else result = mTTSOther.setLanguage(new Locale("kn_IN"));
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Intent installIntent = new Intent();
                        installIntent.setAction(
                                TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        startActivity(installIntent);
                        Log.e("TTS", "Language not supported");
                    } else {
                        mButtonSpeakOther.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        mSeekBarPitch = findViewById(R.id.seek_bar_pitch);
        mSeekBarSpeed = findViewById(R.id.seek_bar_speed);

        mButtonSpeakOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOther(content);
            }
        });
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LinearLayout.LayoutParams temp=(LinearLayout.LayoutParams) storyContent.getLayoutParams();
                temp.topMargin=50;
                temp.leftMargin=30;
                temp.rightMargin=30;
                if(b)
                {

                    temp.height= lm.getHeight()/2-compoundButton.getHeight()/2-50;
                    storyContent.setLayoutParams(temp);

                }
                else
                {
                    temp.height= lm.getHeight();
                    storyContent.setLayoutParams(temp);
                }
            }
        });
        mSeekBarPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(Details.this,"Pitch changed",Toast.LENGTH_SHORT).show();
                mButtonSpeakOther.performClick();
            }
        });
        mSeekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(Details.this,"Speed Changed",Toast.LENGTH_SHORT).show();
                mButtonSpeakOther.performClick();
            }
        });
        // enable back button to main activity or recyclerview
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void speakOther(String text) {
        float pitch = (float) mSeekBarPitch.getProgress() / 50;
        if (pitch < 0.1) pitch = 0.1f;
        float speed = (float) mSeekBarSpeed.getProgress() / 50;
        if (speed < 0.1) speed = 0.1f;

        mTTSOther.setPitch(pitch);
        mTTSOther.setSpeechRate(speed);
        mTTSOther.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);

    }

    @Override
    protected void onDestroy() {
        if (mTTSOther != null) {
            mTTSOther.stop();
            mTTSOther.shutdown();
        }
        super.onDestroy();
    }

    private void translateText() {
        if(flag == 1) {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.BN)

                    .build();

            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);

            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();

            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(content).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            storyContent.setText(s);
                            storyContent.setMovementMethod(new ScrollingMovementMethod());
                            content = s;

                        }
                    });
                }
            });
        }
        else
        {
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    //from language
                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                    // to language
                    .setTargetLanguage(FirebaseTranslateLanguage.KN)

                    .build();

            final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                    .getTranslator(options);

            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                    .build();

            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(content).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {

                            storyContent.setText(s);
                            storyContent.setMovementMethod(new ScrollingMovementMethod());

                            content = s;

                        }
                    });
                }
            });
        }
    }
}

