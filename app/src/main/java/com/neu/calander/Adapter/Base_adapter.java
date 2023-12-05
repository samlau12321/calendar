package com.neu.calander.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neu.calander.Adapter.Bean.Bean;
import com.neu.calander.R;

import java.util.List;

public class Base_adapter extends BaseAdapter {
    private List<Bean> data;
    private Context context;

    public Base_adapter(List<Bean> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.list_view, viewGroup, false);
                viewHolder.textView = view.findViewById(R.id.tv);
            view.setTag(viewHolder);
            if(getCount()== 3){
                viewHolder.textView.setGravity(Gravity.CENTER);
            }
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(data.get(i).getName());
        return view;
    }

    private class ViewHolder {
        TextView textView;
    }
}
