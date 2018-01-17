package com.example.denpa.ghostshuttle;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by denpa on 2018/01/17.
 */

public class GridAdapter extends ArrayAdapter<IconGridItem> {


    private int mResource;
    private List<IconGridItem> mItems;
    private LayoutInflater mInflater;

    public GridAdapter(Context context, int resource, List<IconGridItem> items) {
        super(context, resource, items);
        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            view = mInflater.inflate(mResource, null);
        }

        // リストビューに表示する要素を取得
        IconGridItem item = mItems.get(position);
        RoundedImageView thumbnail = view.findViewById(R.id.icon);
        thumbnail.setImageResource(item.getIcon());
        thumbnail.setBackgroundColor(Color.parseColor(item.getColor()));

        return view;
    }

    /* private Context mContext;
    private String color = "#ffffff";
    private LayoutInflater mLayoutInflater;
    private Integer[] iconArray = {
            R.drawable.eraser,
            R.drawable.heart,
            R.drawable.paper,
            R.drawable.pencil,
            R.drawable.rice};
    private static class ViewHolder {
        public ImageView ImageView;
    }

    public void setColor(String color){
        this.color = color;
    }

    public GridAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            holder.ImageView = convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.ImageView.setImageResource(iconArray[position]);
        holder.ImageView.setBackgroundColor(Color.parseColor("#000000"));

        return convertView;
    } */

}
