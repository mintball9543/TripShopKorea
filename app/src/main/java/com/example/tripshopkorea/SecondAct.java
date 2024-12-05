package com.example.tripshopkorea;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tripshopkorea.databinding.ActSecondBinding;

public class SecondAct extends AppCompatActivity {

    ActSecondBinding binding;
    ProductInfoFetcher pif;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActSecondBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //db = new DatabaseHelper(this);
        db= new DatabaseHelper(this);
        //db.addObserver(this);

        // ProductInfoFetcher 초기화
        pif = new ProductInfoFetcher(this);

        // MainActivity로부터 전달받은 barcodeNumber
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("name")) {
            String value = intent.getStringExtra("name");
            if (value.isEmpty()) {
                String barcodeNumber = intent.getStringExtra("barcodeNumber");
                Log.i("SecondAct barcode", barcodeNumber);
                pif.setProductInfo(barcodeNumber, binding);
            }
            // db 저장된 데이터를 불러올 때
            else{
                updateUIFromIntent(intent);
            }

        }

        binding.swCart.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) { // 데이터 삽입
                boolean success = db.insertData(
                        binding.tvBarcode.getText().toString(),
                        binding.tvName.getText().toString(),
                        binding.tvGroup.getText().toString(),
                        binding.tvDescription.getText().toString(),
                        binding.imageurl.getText().toString()
                );
                if (!success) {
                    Log.e("SecondAct", "데이터 삽입 실패");
                }else {Log.e("SecondAct", "데이터 삽입 성공");}
            } else { // 데이터 삭제
                int rowsAffected = db.deleteData(binding.tvBarcode.getText().toString());
                Log.e("SecondAct", "데이터 삭제 성공");
                if (rowsAffected <= 0) {
                    Log.e("SecondAct", "데이터 삭제 실패");
                }
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.notifyObservers();
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
    }

    private void updateUIFromIntent(Intent intent) {
        String imgurl = intent.getStringExtra("url");
        String barcodeNumber = intent.getStringExtra("barcodeNumber");
        String name = intent.getStringExtra("name");
        String group = intent.getStringExtra("group");
        String detail_msg = intent.getStringExtra("detail_msg");

        Glide.with(this).load(imgurl).into(binding.imageView);
        binding.tvBarcode.setText(barcodeNumber);
        binding.tvName.setText(name);
        binding.tvGroup.setText(group);
        binding.tvDescription.setText(detail_msg);

        binding.swCart.setChecked(true);
    }
}