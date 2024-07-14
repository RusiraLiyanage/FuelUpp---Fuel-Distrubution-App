package com.example.eadfrontend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {
    List<String> mapDataArr;

    public ListViewAdapter(Map<String, String> hashDataMap) {
        this.mapDataArr = new ArrayList<>(hashDataMap.values());
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        public TextView textView01;
        public TextView textView02;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView01 = itemView.findViewById(R.id.textViewvehicleType);
            textView02 = itemView.findViewById(R.id.textViewcountbytype);
        }
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ListViewHolder lvh = new ListViewHolder(v);
        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.textView01.setText(this.mapDataArr.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }




}
