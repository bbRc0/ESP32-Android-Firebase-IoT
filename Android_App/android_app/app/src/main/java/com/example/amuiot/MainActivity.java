package com.example.amuiot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnIsik, btnSesliKomut;
    DatabaseReference databaseReference;
    String isikDurumu = "0";

    private static final int SES_KODU = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIsik = findViewById(R.id.btnIsik);
        btnSesliKomut = findViewById(R.id.btnSesliKomut);

        databaseReference = FirebaseDatabase.getInstance().getReference("isik");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isikDurumu = snapshot.getValue(String.class);
                    if ("1".equals(isikDurumu)) {
                        btnIsik.setText("IŞIĞI KAPAT");
                        btnIsik.setBackgroundColor(Color.parseColor("#F44336"));
                    } else {
                        btnIsik.setText("IŞIĞI AÇ");
                        btnIsik.setBackgroundColor(Color.parseColor("#4CAF50"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btnIsik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(isikDurumu)) {
                    databaseReference.setValue("0"); // Kapat komutu gönder
                } else {
                    databaseReference.setValue("1"); // Aç komutu gönder
                }
            }
        });

        btnSesliKomut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sesiDinle();
            }
        });
    }

    private void sesiDinle() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Işığı aç veya kapat deyin...");

        try {
            startActivityForResult(intent, SES_KODU);
        } catch (Exception e) {
            Toast.makeText(this, "Telefonunuz sesli komutu desteklemiyor.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SES_KODU && resultCode == RESULT_OK && data != null) {
            ArrayList<String> sonuclar = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String soylenen = sonuclar.get(0).toLowerCase(Locale.ROOT);

            if (soylenen.contains("aç") || soylenen.contains("yak")) {
                databaseReference.setValue("1");
                Toast.makeText(this, "Komut alındı: Işık Açılıyor 💡", Toast.LENGTH_SHORT).show();
            } else if (soylenen.contains("kapat") || soylenen.contains("söndür")) {
                databaseReference.setValue("0");
                Toast.makeText(this, "Komut alındı: Işık Kapatılıyor 🌙", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Anlayamadım, tekrar söyle kanka: " + soylenen, Toast.LENGTH_SHORT).show();
            }
        }
    }
}