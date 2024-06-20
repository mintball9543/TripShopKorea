package com.example.tripshopkorea;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    DatabaseHelper db;
    static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        loadRecyclerViewData();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBarcodeScan();
            }
        });

        //언어 종류
        /*new Translation().getLanguages(new TranslationCallback() {
            @Override
            public void onSuccess(String translatedText) {
                Log.i("Translation callback", translatedText);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });*/
        // 번역 테스트
        /*new Translation().translateText(new TranslationCallback() {
            @Override
            public void onSuccess(String translatedText) {
                Log.i("Translation callback", translatedText);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            alertdialog();
        }

        return super.onOptionsItemSelected(item);
    }

    //언어 선택 다이얼로그
    private void alertdialog() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);

        View dialogView;
        dialogView = (View) View.inflate(MainActivity.this, R.layout.alert_language, null);

        dlg.setTitle("Select Language");
        dlg.setIcon(R.mipmap.ic_launcher);
        dlg.setView(dialogView);

        AutoCompleteTextView autoCompleteTextView = dialogView.findViewById(R.id.text_item);

        String[] items = {
                "Afrikaans", "Albanian", "Amharic", "Arabic", "Armenian", "Assamese", "Aymara", "Azerbaijani", "Bambara",
                "Basque", "Belarusian", "Bengali", "Bhojpuri", "Bosnian", "Bulgarian", "Catalan", "Cebuano",
                "Chinese (Simplified)", "Chinese (Traditional)", "Corsican", "Croatian", "Czech", "Danish", "Divehi",
                "Dogri", "Dutch", "English", "Esperanto", "Estonian", "Ewe", "Filipino (Tagalog)", "Finnish", "French",
                "Frisian", "Galician", "Georgian", "German", "Greek", "Guarani", "Gujarati", "Haitian Creole", "Hausa",
                "Hawaiian", "Hebrew", "Hindi", "Hmong", "Hungarian", "Icelandic", "Igbo", "Ilocano", "Indonesian", "Irish",
                "Italian", "Japanese", "Javanese", "Kannada", "Kazakh", "Khmer", "Kinyarwanda", "Konkani", "Korean", "Krio",
                "Kurdish", "Kurdish (Sorani)", "Kyrgyz", "Lao", "Latin", "Latvian", "Lingala", "Lithuanian", "Luganda",
                "Luxembourgish", "Macedonian", "Maithili", "Malagasy", "Malay", "Malayalam", "Maltese", "Maori", "Marathi",
                "Meiteilon (Manipuri)", "Mizo", "Mongolian", "Myanmar (Burmese)", "Nepali", "Norwegian", "Nyanja (Chichewa)",
                "Odia (Oriya)", "Oromo", "Pashto", "Persian", "Polish", "Portuguese (Portugal, Brazil)", "Punjabi",
                "Quechua", "Romanian", "Russian", "Samoan", "Sanskrit", "Scots Gaelic", "Sepedi", "Serbian", "Sesotho",
                "Shona", "Sindhi", "Sinhala (Sinhalese)", "Slovak", "Slovenian", "Somali", "Spanish", "Sundanese", "Swahili",
                "Swedish", "Tagalog (Filipino)", "Tajik", "Tamil", "Tatar", "Telugu", "Thai", "Tigrinya", "Tsonga",
                "Turkish", "Turkmen", "Twi (Akan)", "Ukrainian", "Urdu", "Uyghur", "Uzbek", "Vietnamese", "Welsh", "Xhosa",
                "Yiddish", "Yoruba", "Zulu"
        };
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(MainActivity.this,
                R.layout.dropdown_item_list, items);
        autoCompleteTextView.setAdapter(itemAdapter);

        // 언어와 ISO-639 코드 매핑
        HashMap<String, String> languageCodeMap = new HashMap<>();
        languageCodeMap.put("Afrikaans", "af");
        languageCodeMap.put("Albanian", "sq");
        languageCodeMap.put("Amharic", "am");
        languageCodeMap.put("Arabic", "ar");
        languageCodeMap.put("Armenian", "hy");
        languageCodeMap.put("Assamese", "as");
        languageCodeMap.put("Aymara", "ay");
        languageCodeMap.put("Azerbaijani", "az");
        languageCodeMap.put("Bambara", "bm");
        languageCodeMap.put("Basque", "eu");
        languageCodeMap.put("Belarusian", "be");
        languageCodeMap.put("Bengali", "bn");
        languageCodeMap.put("Bhojpuri", "bho");
        languageCodeMap.put("Bosnian", "bs");
        languageCodeMap.put("Bulgarian", "bg");
        languageCodeMap.put("Catalan", "ca");
        languageCodeMap.put("Cebuano", "ceb");
        languageCodeMap.put("Chinese (Simplified)", "zh-CN");
        languageCodeMap.put("Chinese (Traditional)", "zh-TW");
        languageCodeMap.put("Corsican", "co");
        languageCodeMap.put("Croatian", "hr");
        languageCodeMap.put("Czech", "cs");
        languageCodeMap.put("Danish", "da");
        languageCodeMap.put("Divehi", "dv");
        languageCodeMap.put("Dogri", "doi");
        languageCodeMap.put("Dutch", "nl");
        languageCodeMap.put("English", "en");
        languageCodeMap.put("Esperanto", "eo");
        languageCodeMap.put("Estonian", "et");
        languageCodeMap.put("Ewe", "ee");
        languageCodeMap.put("Filipino (Tagalog)", "fil");
        languageCodeMap.put("Finnish", "fi");
        languageCodeMap.put("French", "fr");
        languageCodeMap.put("Frisian", "fy");
        languageCodeMap.put("Galician", "gl");
        languageCodeMap.put("Georgian", "ka");
        languageCodeMap.put("German", "de");
        languageCodeMap.put("Greek", "el");
        languageCodeMap.put("Guarani", "gn");
        languageCodeMap.put("Gujarati", "gu");
        languageCodeMap.put("Haitian Creole", "ht");
        languageCodeMap.put("Hausa", "ha");
        languageCodeMap.put("Hawaiian", "haw");
        languageCodeMap.put("Hebrew", "he");
        languageCodeMap.put("Hindi", "hi");
        languageCodeMap.put("Hmong", "hmn");
        languageCodeMap.put("Hungarian", "hu");
        languageCodeMap.put("Icelandic", "is");
        languageCodeMap.put("Igbo", "ig");
        languageCodeMap.put("Ilocano", "ilo");
        languageCodeMap.put("Indonesian", "id");
        languageCodeMap.put("Irish", "ga");
        languageCodeMap.put("Italian", "it");
        languageCodeMap.put("Japanese", "ja");
        languageCodeMap.put("Javanese", "jv");
        languageCodeMap.put("Kannada", "kn");
        languageCodeMap.put("Kazakh", "kk");
        languageCodeMap.put("Khmer", "km");
        languageCodeMap.put("Kinyarwanda", "rw");
        languageCodeMap.put("Konkani", "kok");
        languageCodeMap.put("Korean", "ko");
        languageCodeMap.put("Krio", "kri");
        languageCodeMap.put("Kurdish", "ku");
        languageCodeMap.put("Kurdish (Sorani)", "ckb");
        languageCodeMap.put("Kyrgyz", "ky");
        languageCodeMap.put("Lao", "lo");
        languageCodeMap.put("Latin", "la");
        languageCodeMap.put("Latvian", "lv");
        languageCodeMap.put("Lingala", "ln");
        languageCodeMap.put("Lithuanian", "lt");
        languageCodeMap.put("Luganda", "lg");
        languageCodeMap.put("Luxembourgish", "lb");
        languageCodeMap.put("Macedonian", "mk");
        languageCodeMap.put("Maithili", "mai");
        languageCodeMap.put("Malagasy", "mg");
        languageCodeMap.put("Malay", "ms");
        languageCodeMap.put("Malayalam", "ml");
        languageCodeMap.put("Maltese", "mt");
        languageCodeMap.put("Maori", "mi");
        languageCodeMap.put("Marathi", "mr");
        languageCodeMap.put("Meiteilon (Manipuri)", "mni-Mtei");
        languageCodeMap.put("Mizo", "lus");
        languageCodeMap.put("Mongolian", "mn");
        languageCodeMap.put("Myanmar (Burmese)", "my");
        languageCodeMap.put("Nepali", "ne");
        languageCodeMap.put("Norwegian", "no");
        languageCodeMap.put("Nyanja (Chichewa)", "ny");
        languageCodeMap.put("Odia (Oriya)", "or");
        languageCodeMap.put("Oromo", "om");
        languageCodeMap.put("Pashto", "ps");
        languageCodeMap.put("Persian", "fa");
        languageCodeMap.put("Polish", "pl");
        languageCodeMap.put("Portuguese (Portugal, Brazil)", "pt");
        languageCodeMap.put("Punjabi", "pa");
        languageCodeMap.put("Quechua", "qu");
        languageCodeMap.put("Romanian", "ro");
        languageCodeMap.put("Russian", "ru");
        languageCodeMap.put("Samoan", "sm");
        languageCodeMap.put("Sanskrit", "sa");
        languageCodeMap.put("Scots Gaelic", "gd");
        languageCodeMap.put("Sepedi", "nso");
        languageCodeMap.put("Serbian", "sr");
        languageCodeMap.put("Sesotho", "st");
        languageCodeMap.put("Shona", "sn");
        languageCodeMap.put("Sindhi", "sd");
        languageCodeMap.put("Sinhala (Sinhalese)", "si");
        languageCodeMap.put("Slovak", "sk");
        languageCodeMap.put("Slovenian", "sl");
        languageCodeMap.put("Somali", "so");
        languageCodeMap.put("Spanish", "es");
        languageCodeMap.put("Sundanese", "su");
        languageCodeMap.put("Swahili", "sw");
        languageCodeMap.put("Swedish", "sv");
        languageCodeMap.put("Tagalog (Filipino)", "tl");
        languageCodeMap.put("Tajik", "tg");
        languageCodeMap.put("Tamil", "ta");
        languageCodeMap.put("Tatar", "tt");
        languageCodeMap.put("Telugu", "te");
        languageCodeMap.put("Thai", "th");
        languageCodeMap.put("Tigrinya", "ti");
        languageCodeMap.put("Tsonga", "ts");
        languageCodeMap.put("Turkish", "tr");
        languageCodeMap.put("Turkmen", "tk");
        languageCodeMap.put("Twi (Akan)", "ak");
        languageCodeMap.put("Ukrainian", "uk");
        languageCodeMap.put("Urdu", "ur");
        languageCodeMap.put("Uyghur", "ug");
        languageCodeMap.put("Uzbek", "uz");
        languageCodeMap.put("Vietnamese", "vi");
        languageCodeMap.put("Welsh", "cy");
        languageCodeMap.put("Xhosa", "xh");
        languageCodeMap.put("Yiddish", "yi");
        languageCodeMap.put("Yoruba", "yo");
        languageCodeMap.put("Zulu", "zu");

        // 버튼 추가
        dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedLanguage = autoCompleteTextView.getText().toString();
                String languageCode = languageCodeMap.get(selectedLanguage);

                if (languageCode != null) {
                    Toast.makeText(MainActivity.this, "Selected Language Code: " + languageCode, Toast.LENGTH_SHORT).show();

                    // DB 모든 데이터 번역
                    Cursor res = db.getAllData();
                    while (res.moveToNext()) {
                        String id = res.getString(0);
                        final String[] name = {res.getString(1)};
                        final String[] group = {res.getString(2)};
                        final String[] description = {res.getString(3)};

                        Log.i("DB get data", id + " " + name[0] + " " + group[0] + " " + description[0]);


                        // DB 업데이트
                        new TranslateAndSave(MainActivity.this, id, name, group, description, languageCode).execute();
                    }
                    res.close();

                    //SharedPreferences에 저장
                    SharedPreferences sharedPref = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("languageCode", languageCode);
                    editor.apply();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid language selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dlg.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivityResult", String.valueOf(requestCode));
        if (requestCode == 1) {
            loadRecyclerViewData();
            return;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String barcodeNumber = result.getContents();
                Toast.makeText(this, "Barcode Number: " + barcodeNumber, Toast.LENGTH_LONG).show();

                //Second_Act 호출
                Intent intent = new Intent(this, SecondAct.class);
                intent.putExtra("name", "");
                intent.putExtra("barcodeNumber", barcodeNumber);
//                startActivity(intent);
                startActivityForResult(intent,1);
            }
            else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        }
    }

    public void loadRecyclerViewData() {


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<PaintTitle> myDataset = new ArrayList<PaintTitle>();

        // 아이템 추가
        db = new DatabaseHelper(this);
        Cursor res = db.getAllData();

        while (res.moveToNext()) {
            myDataset.add(new PaintTitle(res.getString(4), res.getString(0), res.getString(1),
                    res.getString(2), res.getString(3)));
        }
        res.close();


        recyclerView.setAdapter(new MyAdapter(myDataset));
    }

    private void startBarcodeScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }
}