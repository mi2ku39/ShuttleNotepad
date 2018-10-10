package com.example.denpa.ghostshuttle.ListViewClasses.SimpleListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.denpa.ghostshuttle.R;

import java.util.List;

/**
 * Created by denpa on 2018/01/22.
 */

public class SimpleListAdapter extends ArrayAdapter<SimpleListItem> {

    private int mResource;
    private List<SimpleListItem> mItems;
    private LayoutInflater mInflater;

    public SimpleListAdapter(Context context, int resource, List<SimpleListItem> items) {
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
        SimpleListItem item = mItems.get(position);

        TextView title = view.findViewById(R.id.title);
        title.setText(item.getTitle());

        return view;

    }

}
