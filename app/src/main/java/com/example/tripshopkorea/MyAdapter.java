package com.example.tripshopkorea;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<PaintTitle> mDataset;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<PaintTitle> myDataset) {
        //this.context = context;
        mDataset = myDataset;
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem, parent, false);  // recyclerview
        // View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewitem, parent, false);  // cardview
        MyViewHolder vh = new  MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int newpos = position;
        final Context mycontext = holder.itemView.getContext();

        Glide.with(mycontext)
                .load(mDataset.get(position).imageURL)
                .into(holder.imageView);
        holder.tv1.setText(mDataset.get(position).barcode);
        holder.tv2.setText(mDataset.get(position).name);



        // 버튼 클릭 이벤트
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = mDataset.get(newpos).barcode;
                String name = mDataset.get(newpos).name;
                String url = mDataset.get(newpos).imageURL;
                String group = mDataset.get(newpos).group;
                String detail_msg = mDataset.get(newpos).detail_msg;

                Intent intent = new Intent(mycontext, SecondAct.class);
                intent.putExtra("group", group);
                intent.putExtra("barcodeNumber", barcode);
                intent.putExtra("name", name);
                intent.putExtra("url", url);
                intent.putExtra("detail_msg", detail_msg);
                ((Activity) mycontext).startActivityForResult(intent,1);

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
          return mDataset.size();
    }

}
