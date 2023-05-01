package com.example.texttospeech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText userText;
    Button b1;
    ImageButton imageButton;
    TextToSpeech t1;
    String userToSpeak;
    private final int REQUEST_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userText = findViewById(R.id.userText);
        b1 = findViewById(R.id.button);
        imageButton = findViewById(R.id.imgMic);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userToSpeak = userText.getText().toString();
                if(userToSpeak.equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter Your Text", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),userToSpeak, Toast.LENGTH_SHORT).show();
                    t1.speak(userToSpeak,TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something");

                try{
                    startActivityForResult(intent, REQUEST_CODE);
                }
                catch(Exception ex){
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CODE:
                if(resultCode == RESULT_OK && null !=data){
                    ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    userText.setText(res.get(0));
                    Toast.makeText(getApplicationContext(), res.get(0), Toast.LENGTH_SHORT).show();
                    t1.speak(res.get(0),TextToSpeech.QUEUE_FLUSH, null);
                }
                break;
        }
    }

    public void onDestroy(){
        if(t1 != null){
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }
}