package com.viatom.largedata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_ecg;

        MyViewHolder(View v, Context context) {
            super(v);
            this.rl_ecg = v.findViewById(R.id.rl_ecg);
            rl_ecg.measure(0,0);
        }
    }

    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_data_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder vh, int p) {

        float[] viewBytes = DataController.getViewDataByIndex(p);
        if (viewBytes != null) {
            EcgView ecgView = new EcgView(context);
            ecgView.setData(viewBytes);
            vh.rl_ecg.addView(ecgView);
        }
    }

    @Override
    public int getItemCount() {
        return DataController.getPages();
    }
}
