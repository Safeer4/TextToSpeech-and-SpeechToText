package com.example.texttospeech;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //This line declares a TextToSpeech object.
    TextToSpeech textToSpeech;

    EditText etText;


    TextView tvText;
    final int speechRequestCode = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etText = (EditText)findViewById(R.id.textToSpeech);
        tvText = (TextView)findViewById(R.id.speechToText);

        //The TextToSpeech object is initialized here. We must implement the the interface OnInitListener.
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH); //We set the language to English
                    textToSpeech.setPitch(0.5f); //We set the voice pitch to 0.5, f means float
                    textToSpeech.setSpeechRate(1.0f); //We set the speech rate of speed to 1.0, f means float
                }
            }
        });
    }

    //This function will be triggered on Text To Speech button.
    //The text from the EditText will be taken and then checked if it is not null.
    //Then we pass the text to speak() method of the TextToSpeech class.
    //The method has three parameters, first is the text, second is the speech queue should be flushed
    //before it speak our text, and the third parameter a Bundle, it can be null
    public void toSpeech(View view) {
        String text = etText.getText().toString();
        if (text != null)
        {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    //This function will be triggered on Speech To Text button.
    public void toText(View view) {

        //A Recognizer intent for speech prompt is created
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //We specify speech model, either FREE_FORM or WEB_SEARCH
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //We tell the recognizer to perform speech in language different than the on set in Locale.getDefault()
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //We specify the prompt text which appears when asking to speak
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please Speak Something");

        //The intent is executed here
        try {
            startActivityForResult(intent, speechRequestCode);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case speechRequestCode: { //When request come from recognizer intent which was executed for speech
                if (resultCode == RESULT_OK && data != null) {

                    //The data is brought in String ArrayList Form, so we get it from data object
                    //and store it in result
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tvText.setText(result.get(0)); //We set the result text to TextView
                }
                break;
            }

        }
    }


    //This method is same as the above one which is for Text To Speech
    public void textViewToSpeech(View view) {

        String text = tvText.getText().toString();
        if (text != null)
        {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}