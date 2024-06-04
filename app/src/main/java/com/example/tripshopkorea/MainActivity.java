package com.example.tripshopkorea;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    DatabaseHelper db;
    RecyclerView recyclerView;

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

        String[] items = {"item1", "item2", "item3", "item4", "item5"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(MainActivity.this,
                R.layout.dropdown_item_list, items);
        autoCompleteTextView.setAdapter(itemAdapter);

        //ok 눌렀을 때
        dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //취소 눌렀을 때
        dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
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

    private void loadRecyclerViewData() {


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