package com.example.denpa.ghostshuttle.ListViewClasses.EnhancedListView;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.denpa.ghostshuttle.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by denpa on 2018/01/10.
 */

public class ShuttleListAdapter extends ArrayAdapter<ShuttleListItem> {

    private int mResource;
    private List<ShuttleListItem> mItems;
    private LayoutInflater mInflater;

    public ShuttleListAdapter(Context context, int resource, List<ShuttleListItem> items) {
        super(context, resource, items);
        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(mResource, null);
        }

        // リストビューに表示する要素を取得
        ShuttleListItem item = mItems.get(position);

        // サムネイル画像を設定
        RoundedImageView thumbnail = view.findViewById(R.id.icon);
        thumbnail.setImageResource(item.getThumbnail());
        thumbnail.setBackgroundColor(Color.parseColor(item.getColor()));

        // タイトルを設定
        TextView title = view.findViewById(R.id.title);
        title.setText(item.getmTitle());

        TextView detail = view.findViewById(R.id.detail);
        detail.setText(item.getDetail());

        return view;

    }

}
