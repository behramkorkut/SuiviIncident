package com.yenimobile.suiviincident.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yenimobile.suiviincident.R;

import java.util.List;

/**
 * Created by bebeNokiaX6 on 11/09/2017.
 */

public class SpinnerOptionsAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mItems;
    private LayoutInflater mInflater;

    public List<String> getItems() {
        return mItems;
    }

    public void setItems(List<String> mItems) {
        this.mItems = mItems;
    }

    public SpinnerOptionsAdapter(Context context, List<String> options){
        this.setItems(options);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
    }

    @Override
    public Object getItem(int i) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(i) : null ;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ViewHolder holder;
        if(v == null) {
            v = mInflater.inflate(R.layout.spinner_item_option, viewGroup, false);
            holder = new ViewHolder();
            holder.txtOptionName = (TextView) v.findViewById(R.id.txt_option_name);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        // fill row data
        String currentItem = (String) getItem(i);
        if(currentItem != null) {
            holder.txtOptionName.setText(currentItem);
        }

        return v;
    }

    class ViewHolder {
        TextView txtOptionName;

    }
}
