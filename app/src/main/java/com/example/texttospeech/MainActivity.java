package com.example.texttospeech;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText edText;
    AppCompatButton btn1, btn2;

    TextToSpeech textToSpeech;

    Spinner languageSpinner;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

// diclicer text to speech
        textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
//------------------------------------------


        edText = findViewById(R.id.edText);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        languageSpinner = findViewById(R.id.languageSpinner);


        // list of language
        List<String> languages = Arrays.asList("English", "French", "German", "Spanish", "Bangla", "Hindi");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // select language from Spinner
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                setLanguage(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//---------------------------------------------------
//---------------------------------------------------




// voice Button
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myString = edText.getText().toString();
                textToSpeech.speak(myString, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });


        // Download Button
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myString = edText.getText().toString();
                saveTextAsAudio(myString);
            }
        });
    }

    private void saveTextAsAudio(String text) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File audioFile = new File(downloadsDir, "output_" + timeStamp + ".mp3");

        int result = textToSpeech.synthesizeToFile(text, null, audioFile, "ttsAudio");

        if (result == TextToSpeech.SUCCESS) {
            String filePath = audioFile.getAbsolutePath();
            Toast.makeText(this, "File saved at: " + filePath, Toast.LENGTH_LONG).show();
            System.out.println("File saved at: " + filePath);
        } else {
            Toast.makeText(this, "Failed to save the file.", Toast.LENGTH_SHORT).show();
            System.out.println("Failed to save the file.");
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


    // Method for language
    private void setLanguage(String language) {
        switch (language) {
            case "English":
                textToSpeech.setLanguage(Locale.ENGLISH);
                break;
            case "French":
                textToSpeech.setLanguage(Locale.FRENCH);
                break;
            case "German":
                textToSpeech.setLanguage(Locale.GERMAN);
                break;
            case "Spanish":
                textToSpeech.setLanguage(new Locale("es", "ES"));
                break;
            case "Bangla":
                textToSpeech.setLanguage(new Locale("bn", "BD"));
                break;
            case "Hindi":
                textToSpeech.setLanguage(new Locale("hi", "IN"));
                break;
            default:
                textToSpeech.setLanguage(Locale.ENGLISH);
        }
    }

}
