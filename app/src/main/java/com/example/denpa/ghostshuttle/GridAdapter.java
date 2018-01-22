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
        ImageView check = view.findViewById(R.id.check);
        thumbnail.setImageResource(item.getIcon());
        thumbnail.setBackgroundColor(Color.parseColor(item.getColor()));

        if(item.getCheck()){
            check.setVisibility(View.VISIBLE);
        }else{
            check.setVisibility(View.INVISIBLE);
        }

        return view;
    }

}
