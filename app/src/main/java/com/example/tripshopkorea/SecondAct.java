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

        db = new DatabaseHelper(this);

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
                String imgurl = intent.getStringExtra("url");
                String barcodeNumber = intent.getStringExtra("barcodeNumber");
                String name = intent.getStringExtra("name");
                String group = intent.getStringExtra("group");
                String detail_msg = intent.getStringExtra("detail_msg");

                Glide.with(this)
                        .load(imgurl)
                        .into(binding.imageView);
                binding.tvBarcode.setText(barcodeNumber);
                binding.tvName.setText(name);
                binding.tvGroup.setText(group);
                binding.tvDescription.setText(detail_msg);

                binding.swCart.setChecked(true);
            }

        }


        binding.swCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){ //db 저장
                    db.insertData(binding.tvBarcode.getText().toString(), binding.tvName.getText().toString(),
                            binding.tvGroup.getText().toString(), binding.tvDescription.getText().toString(), binding.imageurl.getText().toString());
                }
                else {
                    int result = db.deleteData(binding.tvBarcode.getText().toString());
                    Log.i("db_delte", String.valueOf(result));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
    }
}
