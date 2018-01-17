package com.example.denpa.ghostshuttle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by denpa on 2018/01/17.
 */

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Integer[] iconArray = {
            R.drawable.eraser,
            R.drawable.heart,
            R.drawable.paper,
            R.drawable.pencil,
            R.drawable.rice
                };
    private static class ViewHolder {
        public ImageView hueImageView;
    }

    public GridAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return iconArray.length;
    }

    public Object getItem(int position) {
        return iconArray[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.icon_item, null);
            holder = new ViewHolder();
            holder.hueImageView = convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.hueImageView.setImageResource(iconArray[position]);

        return convertView;
    }

}
