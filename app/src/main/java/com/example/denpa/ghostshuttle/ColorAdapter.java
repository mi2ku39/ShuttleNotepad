package com.example.denpa.ghostshuttle;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by denpa on 2018/01/17.
 */

public class ColorAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] iconArray = {"#E0E0E0","#757575","#E57373","#4FC3F7","#81C784","#FFF176","#FF8A65"};
    private static class ViewHolder {
        public RoundedImageView ImageView;
    }

    public ColorAdapter(Context context) {
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

        ColorAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.icon_item, null);
            holder = new ColorAdapter.ViewHolder();
            holder.ImageView = convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ColorAdapter.ViewHolder)convertView.getTag();
        }

        holder.ImageView.setBackgroundColor(Color.parseColor(iconArray[position]));

        return convertView;
    }

}