package jp.ghostserver.ghostshuttle.ListViewClasses.SimpleListView;

import android.content.Context;
import android.support.annotation.NonNull;
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

    private int _resource;
    private List<SimpleListItem> _items;
    private LayoutInflater _inflater;

    public SimpleListAdapter(Context context, int resource, List<SimpleListItem> items) {
        super(context, resource, items);
        _resource = resource;
        _items = items;
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = _inflater.inflate(_resource, null);
        }

        // リストビューに表示する要素を取得
        SimpleListItem item = _items.get(position);

        TextView title = view.findViewById(R.id.title);
        title.setText(item.getTitle());

        return view;

    }

}
